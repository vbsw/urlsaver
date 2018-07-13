/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.resources.ResourcesConfig;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;


/**
 * @author Vitali Baumtrok
 */
public final class URLsLoadService extends Service<DBRecord> {

	public final DBRecord record;

	public URLsLoadService ( final DBRecord record ) {
		this.record = record;
	}

	public boolean isSucceeded ( ) {
		return super.getState() == Worker.State.SUCCEEDED;
	}

	@Override
	protected Task<DBRecord> createTask ( ) {
		final Task<DBRecord> loadingTask = new URLsLoadTask(record);
		return loadingTask;
	}

	private static final class URLsLoadTask extends Task<DBRecord> {

		private final DBRecord record;

		public URLsLoadTask ( final DBRecord record ) {
			this.record = record;
		}

		@Override
		protected DBRecord call ( ) throws Exception {
			final byte[] bytes = getBytesFromFile(record.getPath());
			if ( bytes != null && bytes.length > 0 )
				parseURLs(bytes);
			else
				super.updateProgress(100,100);
			return record;
		}

		private byte[] getBytesFromFile ( final Path path ) {
			try {
				return Files.readAllBytes(path);
			} catch ( IOException e ) {
				e.printStackTrace();
			}
			return null;
		}

		private void parseURLs ( final byte[] bytes ) {
			int offset = 0;
			int progress = 0;
			while ( (offset < bytes.length) && (super.isCancelled() == false) ) {
				offset = Parser.seekContent(bytes,offset,bytes.length);
				offset = parseURLAndTags(bytes,offset);
				progress = updateProgressIfNecessary(offset,bytes.length,progress);
			}
		}

		private int updateProgressIfNecessary ( final int bytesRead, final int bytesTotal, final int progress ) {
			final int progressNew = bytesRead * 100 / bytesTotal;
			if ( progressNew > progress ) {
				super.updateProgress(progressNew,100);
				return progressNew;
			}
			return progress;
		}

		private int parseURLAndTags ( final byte[] bytes, final int offset ) {
			final int indexLineEnd = Parser.seekLineEnd(bytes,offset,bytes.length);
			final int indexWordEnd = Parser.seekContentReverse(bytes,indexLineEnd,offset);
			final int wordLength = indexWordEnd - offset;

			if ( wordLength > 0 ) {
				final String url = new String(bytes,offset,wordLength,ResourcesConfig.FILE_CHARSET);
				final int indexUrl = record.addUrl(url);
				final int indexTagsLineBegin = Parser.seekContent(bytes,indexLineEnd,bytes.length);
				final int indexTagsLineEnd = Parser.seekLineEnd(bytes,indexTagsLineBegin,bytes.length);
				int indexTagBegin = indexTagsLineBegin;
				int indexTagEnd = Parser.seekWhitespace(bytes,indexTagBegin,indexTagsLineEnd);
				int tagLength = indexTagEnd - indexTagBegin;

				while ( tagLength > 0 ) {
					final String tag = new String(bytes,indexTagBegin,tagLength,ResourcesConfig.FILE_CHARSET);
					indexTagBegin = Parser.seekContent(bytes,indexTagEnd,indexTagsLineEnd);
					indexTagEnd = Parser.seekWhitespace(bytes,indexTagBegin,indexTagsLineEnd);
					tagLength = indexTagEnd - indexTagBegin;
					record.addTagToUrl(indexUrl,tag);
				}
				return indexTagsLineEnd;
			}
			return indexLineEnd;
		}

	}

}

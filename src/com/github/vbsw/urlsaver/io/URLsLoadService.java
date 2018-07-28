/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.vbsw.urlsaver.api.ResourceLoader;
import com.github.vbsw.urlsaver.api.URLMeta;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.utility.Parser;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;


/**
 * @author Vitali Baumtrok
 */
public final class URLsLoadService extends Service<DBRecord> {

	protected final ResourceLoader resourceLoader;
	protected final URLMeta urlMeta;
	public final DBRecord record;

	public URLsLoadService ( final ResourceLoader resourceLoader, final URLMeta urlMeta, final DBRecord record ) {
		this.resourceLoader = resourceLoader;
		this.urlMeta = urlMeta;
		this.record = record;
	}

	public boolean isSucceeded ( ) {
		return super.getState() == Worker.State.SUCCEEDED;
	}

	@Override
	protected Task<DBRecord> createTask ( ) {
		final Task<DBRecord> loadingTask = new URLsLoadTask(urlMeta,record,resourceLoader.getCharset());
		return loadingTask;
	}

	private static final class URLsLoadTask extends Task<DBRecord> {

		private final URLMeta urlMeta;
		private final DBRecord record;
		private final Charset charset;

		public URLsLoadTask ( final URLMeta urlMeta, final DBRecord record, final Charset charset ) {
			this.urlMeta = urlMeta;
			this.record = record;
			this.charset = charset;
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
				offset = parseURLTagsAndMeta(bytes,offset);
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

		private int parseURLTagsAndMeta ( final byte[] bytes, final int offset ) {
			final int indexLineEnd = Parser.seekLineEnd(bytes,offset,bytes.length);
			final int indexWordEnd = Parser.seekContentReverse(bytes,indexLineEnd,offset);
			final int wordLength = indexWordEnd - offset;

			/* wordLength == 0 is when file end */
			if ( wordLength > 0 ) {
				final String url = new String(bytes,offset,wordLength,charset);
				final int indexURL = record.addUrl(url);
				final int indexTagsLineBegin = Parser.seekContent(bytes,indexLineEnd,bytes.length);
				final int indexTagsLineEnd = Parser.seekLineEnd(bytes,indexTagsLineBegin,bytes.length);
				int indexTagBegin = indexTagsLineBegin;
				int indexTagEnd = Parser.seekWhitespace(bytes,indexTagBegin,indexTagsLineEnd);
				int tagLength = indexTagEnd - indexTagBegin;

				while ( tagLength > 0 ) {
					final String tag = new String(bytes,indexTagBegin,tagLength,charset);
					indexTagBegin = Parser.seekContent(bytes,indexTagEnd,indexTagsLineEnd);
					indexTagEnd = Parser.seekWhitespace(bytes,indexTagBegin,indexTagsLineEnd);
					tagLength = indexTagEnd - indexTagBegin;
					record.addTagToUrl(indexURL,tag);
				}
				final int indexNext = parseURLMetaData(bytes,indexURL,indexTagsLineEnd);
				return indexNext;
			}
			return indexLineEnd;
		}

		private int parseURLMetaData ( final byte[] bytes, final int indexURL, final int offset ) {
			final int indexMetaDataLineBegin = Parser.seekContent(bytes,offset,bytes.length);

			if ( urlMeta.isMetaDataSignature(bytes,indexMetaDataLineBegin) ) {
				final int indexMetaDataLineEnd = Parser.seekLineEnd(bytes,indexMetaDataLineBegin,bytes.length);
				int indexMetaDataBegin = indexMetaDataLineBegin;
				int indexMetaDataEnd = Parser.seekWhitespace(bytes,indexMetaDataBegin,indexMetaDataLineEnd);
				int metaDataLength = indexMetaDataEnd - indexMetaDataBegin;

				while ( metaDataLength > 0 ) {
					final int indexSeparator = Parser.seekByte(bytes,indexMetaDataBegin,indexMetaDataEnd,(byte) '=');
					final int indexMetaValueBegin = indexSeparator + 1;
					final int metaKeyLength = indexSeparator - indexMetaDataBegin;
					final int metaValueLength = indexMetaDataEnd - indexMetaValueBegin;
					final String metaKey = new String(bytes,indexMetaDataBegin,metaKeyLength,charset);
					final int metaKeyID = urlMeta.getMetaKeyID(metaKey);

					if ( metaValueLength > 0 ) {
						final String metaValue = new String(bytes,indexMetaDataBegin,metaKeyLength,charset);
						addURLMetaValue(bytes,indexURL,metaKeyID,metaKey,metaValue);
					} else {
						addURLMetaKey(bytes,indexURL,metaKeyID,metaKey);
					}
					indexMetaDataBegin = Parser.seekContent(bytes,indexMetaDataEnd,indexMetaDataLineEnd);
					indexMetaDataEnd = Parser.seekWhitespace(bytes,indexMetaDataBegin,indexMetaDataLineEnd);
					metaDataLength = indexMetaDataEnd - indexMetaDataBegin;
				}
				return indexMetaDataLineEnd;
			}
			return indexMetaDataLineBegin;
		}

		private void addURLMetaValue ( final byte[] bytes, final int indexURL, final int metaKeyID, final String metaKey, final String metaValue ) {
			switch ( metaKeyID ) {
				case URLMeta.ACCESSED:
				case URLMeta.SCORE:
				record.setMetaData(indexURL,metaKeyID,metaValue);

				case URLMeta.UNKNOWN:
				System.out.println("unsuported key \"" + metaKey + "\" in \"" + record.getFileName() + "\"");
			}
		}

		private void addURLMetaKey ( final byte[] bytes, final int indexURL, final int metaKeyID, final String metaKey ) {
			switch ( metaKeyID ) {
				case URLMeta.ACCESSED:
				case URLMeta.SCORE:
				System.out.println("key \"" + metaKey + "\" has no value in " + record.getFileName());

				case URLMeta.UNKNOWN:
				System.out.println("unsuported key \"" + metaKey + "\" in \"" + record.getFileName() + "\"");
			}
		}

	}

}

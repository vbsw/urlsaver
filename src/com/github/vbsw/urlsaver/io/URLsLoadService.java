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

import com.github.vbsw.urlsaver.api.URLMetaDefinition;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.utility.Parser;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;


/**
 * @author Vitali Baumtrok
 */
public final class URLsLoadService extends Service<DBURLs> {

	protected final Charset inputCharset;
	protected final URLMetaDefinition urlMeta;
	public final DBURLs dbTable;

	public URLsLoadService ( final Charset inputCharset, final URLMetaDefinition urlMeta, final DBURLs dbTable ) {
		this.inputCharset = inputCharset;
		this.urlMeta = urlMeta;
		this.dbTable = dbTable;
	}

	public boolean isSucceeded ( ) {
		return super.getState() == Worker.State.SUCCEEDED;
	}

	@Override
	protected Task<DBURLs> createTask ( ) {
		final Task<DBURLs> loadingTask = new URLsLoadTask(urlMeta,dbTable,inputCharset);
		return loadingTask;
	}

	private static final class URLsLoadTask extends Task<DBURLs> {

		private final URLMetaDefinition urlMeta;
		private final DBURLs dbTable;
		private final Charset charset;

		public URLsLoadTask ( final URLMetaDefinition urlMeta, final DBURLs dbTable, final Charset charset ) {
			this.urlMeta = urlMeta;
			this.dbTable = dbTable;
			this.charset = charset;
		}

		@Override
		protected DBURLs call ( ) throws Exception {
			final byte[] bytes = getBytesFromFile(dbTable.getPath());
			if ( bytes != null && bytes.length > 0 )
				parseURLs(bytes);
			else
				super.updateProgress(100,100);
			return dbTable;
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
				final int indexURL = dbTable.addUrl(url);
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
					dbTable.addTagToUrl(indexURL,tag);
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
					final String metaValue = metaValueLength > 0 ? new String(bytes,indexMetaValueBegin,metaValueLength,charset) : "";
					urlMeta.setMeta(metaKey,metaValue);
					addURLMetaValue(indexURL);

					indexMetaDataBegin = Parser.seekContent(bytes,indexMetaDataEnd,indexMetaDataLineEnd);
					indexMetaDataEnd = Parser.seekWhitespace(bytes,indexMetaDataBegin,indexMetaDataLineEnd);
					metaDataLength = indexMetaDataEnd - indexMetaDataBegin;
				}
				return indexMetaDataLineEnd;
			}
			return indexMetaDataLineBegin;
		}

		private void addURLMetaValue ( final int indexURL ) {
			final String metaValue = urlMeta.metaValue;
			final int metaKeyID = urlMeta.metaKeyID;
			if ( !metaValue.isEmpty() ) {
				switch ( metaKeyID ) {
					case URLMetaDefinition.DATE:
					case URLMetaDefinition.SCORE:
					dbTable.setMetaData(indexURL,metaKeyID,metaValue);
					break;
					case URLMetaDefinition.UNKNOWN:
					System.out.println("unsuported key \"" + urlMeta.metaKey + "\" in \"" + dbTable.getFileName() + "\"");
					break;
				}
			} else {
				switch ( metaKeyID ) {
					case URLMetaDefinition.DATE:
					case URLMetaDefinition.SCORE:
					System.out.println("key \"" + urlMeta.metaKey + "\" has no value in " + dbTable.getFileName());
					break;

					case URLMetaDefinition.UNKNOWN:
					System.out.println("unsuported key \"" + urlMeta.metaKey + "\" in \"" + dbTable.getFileName() + "\"");
				}
			}
		}

	}

}

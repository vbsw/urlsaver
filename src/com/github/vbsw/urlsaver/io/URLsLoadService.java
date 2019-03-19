/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
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

import javafx.concurrent.Service;
import javafx.concurrent.Task;


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

	@Override
	protected Task<DBURLs> createTask ( ) {
		final Task<DBURLs> loadingTask = new URLsLoadTask(inputCharset,urlMeta,dbTable);
		return loadingTask;
	}

	private static final class URLsLoadTask extends Task<DBURLs> {

		private final URLMetaDefinition urlMeta;
		private final DBURLs dbURLs;
		private final Charset inputCharset;

		public URLsLoadTask ( final Charset inputCharset, final URLMetaDefinition urlMeta, final DBURLs dbURLs ) {
			this.urlMeta = urlMeta;
			this.dbURLs = dbURLs;
			this.inputCharset = inputCharset;
		}

		@Override
		protected DBURLs call ( ) throws Exception {
			final byte[] bytes = getBytesFromFile(dbURLs.getPath());
			if ( bytes != null && bytes.length > 0 )
				parseURLs(bytes);
			else
				super.updateProgress(100,100);
			return dbURLs;
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
			final URLsParser urlsParser = new URLsParser(inputCharset,urlMeta,dbURLs,bytes);
			while ( !super.isCancelled() && urlsParser.parseContent() ) {
				final long progress = urlsParser.getProgress();
				super.updateProgress(progress,100);
			}
		}

	}

}

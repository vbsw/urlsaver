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
import com.github.vbsw.urlsaver.db.DBURLsImport;
import com.github.vbsw.urlsaver.utility.Parser;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


/**
 * @author Vitali Baumtrok
 */
public class URLsImportLoadService extends Service<DBURLsImport> {

	protected final Charset inputCharset;
	protected final URLMetaDefinition urlMeta;
	public final DBURLsImport dbURLsImport;

	public URLsImportLoadService ( final Charset inputCharset, final URLMetaDefinition urlMeta, final DBURLsImport dbURLsImport ) {
		this.inputCharset = inputCharset;
		this.urlMeta = urlMeta;
		this.dbURLsImport = dbURLsImport;
	}

	@Override
	protected Task<DBURLsImport> createTask ( ) {
		final Task<DBURLsImport> urlsImportTask = new URLsImportTask(inputCharset,urlMeta,dbURLsImport);
		return urlsImportTask;
	}

	protected static final class URLsImportTask extends Task<DBURLsImport> {

		protected final Charset inputCharset;
		protected final URLMetaDefinition urlMeta;
		public final DBURLsImport dbURLsImport;

		public URLsImportTask ( final Charset inputCharset, final URLMetaDefinition urlMeta, final DBURLsImport dbURLsImport ) {
			this.inputCharset = inputCharset;
			this.urlMeta = urlMeta;
			this.dbURLsImport = dbURLsImport;
		}

		@Override
		protected DBURLsImport call ( ) throws Exception {
			final byte[] bytes = getBytesFromFile(dbURLsImport.getPath());
			if ( bytes != null && bytes.length > 0 )
				if ( Parser.isMyAnimeListXML(bytes,0,bytes.length) )
					parseMyAnimeListXML(bytes);
				else
					parseURLs(bytes);
			else
				super.updateProgress(100,100);
			applyFilter();
			return dbURLsImport;
		}

		private void parseMyAnimeListXML ( final byte[] bytes ) {
			super.updateProgress(100,100);
			// TODO parseMyAnimeListXML
		}

		private byte[] getBytesFromFile ( final Path path ) {
			try {
				return Files.readAllBytes(path);
			} catch ( IOException e ) {
				e.printStackTrace();
			}
			return null;
		}

		private void parseURLs ( final byte[] bytes ) throws Exception {
			final DBURLs dbURLs = dbURLsImport.dbURLs;
			final URLsParser urlsParser = new URLsParser(inputCharset,urlMeta,dbURLs,bytes);
			while ( !super.isCancelled() && urlsParser.parseContent() ) {
				final long progress = urlsParser.getProgress();
				super.updateProgress(progress,100);
			}
		}

		private void applyFilter ( ) {
			// TODO applyFilter
		}

	}

}

/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.nio.file.Path;


/**
 * URLs record for the view "import".
 * 
 * @author Vitali Baumtrok
 */
public class DBURLsImport {

	public final DBURLs dbURLs = new DBURLs();

	private Path path;
	private String pathString;
	private String listLabel;
	private String fileName;
	private long fileSize;
	private String urlsImportKeysString;

	public DBURLsImport ( final Path path ) {
		setPath(path);
	}

	public void setPath ( final Path path ) {
		this.path = path;
		dbURLs.setPath(path);
		if ( path != null ) {
			this.pathString = path.toString();
			this.fileName = path.getFileName().toString();
			this.fileSize = dbURLs.getFileSize();
		} else {
			setStubs();
		}
	}

	public Path getPath ( ) {
		return path;
	}

	public String getPathAsString ( ) {
		return pathString;
	}

	public void setListLabel ( final String listLabel ) {
		this.listLabel = listLabel;
	}

	public String getListLabel ( ) {
		return listLabel;
	}

	public String getFileName ( ) {
		return fileName;
	}

	public long getFileSize ( ) {
		return fileSize;
	}

	public void setURLsImportKeysString ( final String importKeys ) {
		urlsImportKeysString = importKeys;
	}

	public String getURLsImportKeysString ( ) {
		return urlsImportKeysString;
	}

	public void beginImport ( ) {
		dbURLs.beginLoading();
	}

	public void importFinished ( ) {
		dbURLs.loadingFinished();
		fileSize = dbURLs.getFileSize();
	}

	private void setStubs ( ) {
		final String pathNotSet = "path not set";
		pathString = pathNotSet;
		listLabel = pathNotSet;
		fileName = pathNotSet;
		fileSize = -1;
	}

}

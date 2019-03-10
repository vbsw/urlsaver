/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * URLs record for the view "import".
 * 
 * @author Vitali Baumtrok
 */
public class DBURLsImport {

	private Path path;
	private String pathString;
	private String listLabel;
	private String fileName;
	private long fileSize;

	public DBURLsImport ( final Path path ) {
		setPath(path);
	}

	public String getListLabel ( ) {
		return null;
	}

	public void setPath ( final Path path ) {
		this.path = path;
		if ( path != null ) {
			this.pathString = path.toString();
			this.fileName = path.getFileName().toString();
			refreshFileSize();
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

	public String getFileName ( ) {
		return fileName;
	}

	public long getFileSize ( ) {
		return fileSize;
	}

	private void refreshFileSize ( ) {
		try {
			fileSize = Files.size(path);
		} catch ( IOException e ) {
			fileSize = -1;
			e.printStackTrace();
		}
	}

	private void setStubs ( ) {
		final String pathNotSet = "path not set";
		pathString = pathNotSet;
		listLabel = pathNotSet;
		fileName = pathNotSet;
		fileSize = -1;
	}

}

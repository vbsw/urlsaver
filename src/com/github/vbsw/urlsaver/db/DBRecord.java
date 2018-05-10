/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.nio.file.Path;


/**
 * @author Vitali Baumtrok
 */
public class DBRecord {

	private Path path;
	private String pathString;
	private String listLabel;
	private String fileName;
	private boolean dirtyFlag;

	public DBRecord ( ) {
		setStubs();
	}

	private void setStubs ( ) {
		final String pathNotSet = "path not set";
		pathString = pathNotSet;
		listLabel = pathNotSet;
		fileName = pathNotSet;
	}

	public DBRecord ( final Path path ) {
		setPath(path);
	}

	public void setPath ( final Path path ) {
		if ( path != null ) {
			this.path = path;
			this.pathString = path.toString();
			this.fileName = path.getFileName().toString();
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

	public void setDirty ( final boolean dirtyFlag ) {
		this.dirtyFlag = dirtyFlag;
	}

	public boolean isDirty ( ) {
		return dirtyFlag;
	}

}

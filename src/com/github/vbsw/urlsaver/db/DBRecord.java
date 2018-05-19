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

	private static final int INITIAL_CAPACITY = 250;

	private final DynArrayOfString urls = new DynArrayOfString(INITIAL_CAPACITY);
	private final DynArrayOfString tags = new DynArrayOfString(INITIAL_CAPACITY);
	private final DynArrayOfString2 tagsToURLs = new DynArrayOfString2(INITIAL_CAPACITY);
	private final DynArrayOfString2 urlsToTags = new DynArrayOfString2(INITIAL_CAPACITY);

	private Path path;
	private String pathString;
	private String listLabel;
	private String fileName;
	private boolean dirtyFlag;
	private boolean loadedFlag;

	private void setStubs ( ) {
		final String pathNotSet = "path not set";
		pathString = pathNotSet;
		listLabel = pathNotSet;
		fileName = pathNotSet;
	}

	private final int addTag ( final String tag ) {
		final int index = getTagIndex(tag);
		if ( index < 0 ) {
			final int insertIndex = -index - 1;
			final DynArrayOfString urlsToTag = new DynArrayOfString();
			tags.add(insertIndex,tag);
			urlsToTags.add(insertIndex,urlsToTag);
			return insertIndex;
		}
		return index;
	}

	public DBRecord ( ) {
		setStubs();
	}

	public DBRecord ( final Path path ) {
		setPath(path);
	}

	public void setPath ( final Path path ) {
		this.path = path;
		if ( path != null ) {
			this.pathString = path.toString();
			this.fileName = path.getFileName().toString();
		} else {
			setStubs();
		}
	}

	public final int getUrlIndex ( final String url ) {
		return urls.binarySearch(url);
	}

	public final int getTagIndex ( final String tag ) {
		return tags.binarySearch(tag);
	}

	public final int addUrl ( final String url ) {
		final int index = getUrlIndex(url);
		if ( index < 0 ) {
			final int insertIndex = -index - 1;
			final DynArrayOfString tagsToURL = new DynArrayOfString();
			urls.add(insertIndex,url);
			tagsToURLs.add(insertIndex,tagsToURL);
			return insertIndex;
		}
		return index;
	}

	public boolean addTagToUrl ( final int index, final String tag ) {
		final DynArrayOfString tagsToURL = tagsToURLs.values[index];
		final int indexTagToURL = tagsToURL.binarySearch(tag);
		// URL doesn't have tag
		if ( indexTagToURL < 0 ) {
			final int indexTag = addTag(tag); // (ensured tag is available)
			final int indexInsertTagToURL = -indexTagToURL - 1;
			final DynArrayOfString urlsToTag = urlsToTags.values[indexTag];
			final String url = urls.values[index];
			final int indexURLToTag = urlsToTag.binarySearch(url);
			// tag doesn't have URL
			if ( indexURLToTag < 0 ) {
				final int indexInsertURLToTag = -indexURLToTag - 1;
				urlsToTag.add(indexInsertURLToTag,url);
			}
			tagsToURL.add(indexInsertTagToURL,tag);
			return true;
		}
		return false;
	}

	public void clearURLs ( ) {
		urls.clear();
		tags.clear();
		urlsToTags.clear();
		tagsToURLs.clear();
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

	public void setLoaded ( final boolean loadedFlag ) {
		this.loadedFlag = loadedFlag;
	}

	public boolean isLoaded ( ) {
		return loadedFlag;
	}

}

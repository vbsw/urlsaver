/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
public class DBRecord {

	private static final int INITIAL_CAPACITY = 250;

	private final DynArrayOfString urls = new DynArrayOfString(INITIAL_CAPACITY);
	private final DynArrayOfString tags = new DynArrayOfString(INITIAL_CAPACITY);
	private final DynArrayOfString2 tagsToURLs = new DynArrayOfString2(INITIAL_CAPACITY);
	private final DynArrayOfString2 urlsToTags = new DynArrayOfString2(INITIAL_CAPACITY);
	private final WordSearch urlsSearch = new WordSearch(tags,urlsToTags);

	private int urlsCountSaved;
	private int tagsCountSaved;
	private Path path;
	private String pathString;
	private String listLabel;
	private String fileName;
	private boolean dirtyFlag;
	private boolean loadedFlag;
	private String urlsSearchString;
	private int selectedURLIndex;

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

	public void resetCountSaved ( ) {
		urlsCountSaved = urls.valuesLength;
		tagsCountSaved = tags.valuesLength;
	}

	public int getURLsCountModified ( ) {
		return urls.valuesLength;
	}

	public int getTagsCountModified ( ) {
		return tags.valuesLength;
	}

	public int getURLsCountSaved ( ) {
		return urlsCountSaved;
	}

	public int getTagsCountSaved ( ) {
		return tagsCountSaved;
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

	public final int getURLIndex ( final String url ) {
		return urls.binarySearch(url);
	}

	public final int getTagIndex ( final String tag ) {
		return tags.binarySearch(tag);
	}

	public final int addUrl ( final String url ) {
		final int index = getURLIndex(url);
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

	public String getTagsAsString ( final int index ) {
		final DynArrayOfString tagsToURL = tagsToURLs.values[index];
		final StringBuilder stringBuilder = new StringBuilder();
		for ( int i = 0; i < tagsToURL.valuesLength; i += 1 ) {
			if ( i > 0 )
				stringBuilder.append(' ');
			stringBuilder.append(tagsToURL.values[i]);
		}
		return stringBuilder.toString();
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

	public void setURLsSearchString ( final String searchString ) {
		this.urlsSearchString = searchString;
	}

	public String getURLsSearchString ( ) {
		return urlsSearchString;
	}

	public ArrayList<String> getURLsSearchResult ( ) {
		return urlsSearch.getResult();
	}

	public void setSelectedURLIndex ( final int selectedURLIndex ) {
		this.selectedURLIndex = selectedURLIndex;
	}

	public int getSelectedURLIndex ( ) {
		return selectedURLIndex;
	}

	public void write ( final BufferedWriter writer ) throws IOException {
		for ( int i = 0; i < urls.valuesLength; i += 1 ) {
			final String url = urls.values[i];
			final DynArrayOfString tagsToURL = tagsToURLs.values[i];
			writer.write(url);
			writer.newLine();
			if ( tagsToURL.valuesLength > 0 ) {
				writer.write(tagsToURL.values[0]);
				for ( int j = 1; j < tagsToURL.valuesLength; j += 1 ) {
					writer.write(" ");
					writer.write(tagsToURL.values[i]);
				}
			}
			writer.newLine();
		}
	}

	public void searchURLs ( final DynArrayOfString searchTags, final boolean searchByPrefix ) {
		if ( searchByPrefix )
			urlsSearch.searchByPrefix(searchTags);
		else
			urlsSearch.searchByWord(searchTags);
	}

}

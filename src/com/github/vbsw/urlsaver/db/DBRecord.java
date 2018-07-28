/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.api.URLMeta;


/**
 * @author Vitali Baumtrok
 */
public class DBRecord {

	private static final int INITIAL_CAPACITY = 250;

	private final DynArrayOfString urls = new DynArrayOfString(INITIAL_CAPACITY);
	private final DynArrayOfString tags = new DynArrayOfString(INITIAL_CAPACITY);
	private final DynArrayOfString2 tagsOnURLs = new DynArrayOfString2(INITIAL_CAPACITY);
	private final DynArrayOfString2 urlsOnTags = new DynArrayOfString2(INITIAL_CAPACITY);
	private final DynArrayOfString2 metaDataOnURLs = new DynArrayOfString2(INITIAL_CAPACITY);
	private final WordSearch urlsSearch = new WordSearch(tags,urlsOnTags);

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
	private long fileSize;

	public DBRecord ( ) {
		setStubs();
	}

	public DBRecord ( final Path path ) {
		setPath(path);
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
			refreshFileSize();
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
			final DynArrayOfString tagsOnURL = new DynArrayOfString();
			final DynArrayOfString metaDataOnURL = new DynArrayOfString();
			dirtyFlag = true;
			urls.add(insertIndex,url);
			tagsOnURLs.add(insertIndex,tagsOnURL);
			metaDataOnURLs.add(insertIndex,metaDataOnURL);
			return insertIndex;
		}
		return index;
	}

	public boolean addTagToUrl ( final int index, final String tag ) {
		final DynArrayOfString tagsOnURL = tagsOnURLs.values[index];
		final int indexTagToURL = tagsOnURL.binarySearch(tag);
		// URL doesn't have tag
		if ( indexTagToURL < 0 ) {
			final int indexTag = addTag(tag); // (ensured tag is available)
			final int indexInsertTagToURL = -indexTagToURL - 1;
			final DynArrayOfString urlsOnTag = urlsOnTags.values[indexTag];
			final String url = urls.values[index];
			final int indexURLToTag = urlsOnTag.binarySearch(url);
			// tag doesn't have URL
			if ( indexURLToTag < 0 ) {
				final int indexInsertURLToTag = -indexURLToTag - 1;
				dirtyFlag = true;
				urlsOnTag.add(indexInsertURLToTag,url);
			}
			tagsOnURL.add(indexInsertTagToURL,tag);
			return true;
		}
		return false;
	}

	public void setMetaData ( final int index, final int metaKeyID, final String metaValue ) {
		final int indexMetaKeyID = metaKeyID - (URLMeta.NONE + 1);
		metaDataOnURLs.values[index].set(indexMetaKeyID,metaValue);
	}

	public String getTagsAsString ( final int index ) {
		final DynArrayOfString tagsOnURL = tagsOnURLs.values[index];
		final StringBuilder stringBuilder = new StringBuilder();
		for ( int i = 0; i < tagsOnURL.valuesLength; i += 1 ) {
			if ( i > 0 )
				stringBuilder.append(' ');
			stringBuilder.append(tagsOnURL.values[i]);
		}
		return stringBuilder.toString();
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

	public boolean isDirty ( ) {
		return dirtyFlag;
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

	public long getFileSize ( ) {
		return fileSize;
	}

	public void write ( final BufferedWriter writer ) throws IOException {
		for ( int i = 0; i < urls.valuesLength; i += 1 ) {
			final String url = urls.values[i];
			final DynArrayOfString tagsOnURL = tagsOnURLs.values[i];
			writer.write(url);
			writer.newLine();
			if ( tagsOnURL.valuesLength > 0 ) {
				writer.write(tagsOnURL.values[0]);
				for ( int j = 1; j < tagsOnURL.valuesLength; j += 1 ) {
					writer.write(" ");
					writer.write(tagsOnURL.values[j]);
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

	public void beginLoading ( ) {
		loadedFlag = false;
		dirtyFlag = false;
		urls.clear();
		tags.clear();
		urlsOnTags.clear();
		tagsOnURLs.clear();
	}

	public void endLoading ( ) {
		refreshFileSize();
		urlsCountSaved = urls.valuesLength;
		tagsCountSaved = tags.valuesLength;
		dirtyFlag = false;
		loadedFlag = true;
	}

	public void endSave ( ) {
		endLoading();
	}

	public boolean isEqualTags ( final int urlIndex, final DynArrayOfString tags ) {
		final DynArrayOfString tagsOfURL = tagsOnURLs.values[urlIndex];
		final boolean equalTags = tagsOfURL.equals(tags);
		return equalTags;
	}

	public void setTags ( final int urlIndex, final DynArrayOfString tags ) {
		final DynArrayOfString tagsOnURL = tagsOnURLs.values[urlIndex];
		final DynArrayOfString tagsOnURLUnused = tagsOnURL.clone();
		tagsOnURLUnused.remove(tags);
		while ( tagsOnURL.valuesLength > 0 ) {
			final int tagsOnURLIndex = tagsOnURL.valuesLength - 1;
			final String tag = tagsOnURL.values[tagsOnURLIndex];
			final int tagIndex = this.tags.binarySearch(tag);
			final DynArrayOfString urlsOnTag = urlsOnTags.values[tagIndex];
			final String url = urls.values[urlIndex];
			final int urlsOnTagIndex = urlsOnTag.binarySearch(url);
			tagsOnURL.remove(tagsOnURLIndex);
			urlsOnTag.remove(urlsOnTagIndex);
		}
		for ( int i = 0; i < tags.valuesLength; i += 1 )
			addTagToUrl(urlIndex,tags.values[i]);
		removeUnusedTags(tagsOnURLUnused);
	}

	public void removeURL ( final int urlIndex ) {
		if ( urlIndex >= 0 ) {
			final String url = urls.values[urlIndex];
			final DynArrayOfString tagsOnURLUnused = tagsOnURLs.values[urlIndex];
			dirtyFlag = true;
			for ( int i = 0; i < tagsOnURLUnused.valuesLength; i += 1 ) {
				final String tag = tagsOnURLUnused.values[i];
				final int tagIndex = getTagIndex(tag);
				final DynArrayOfString urlsOnTag = urlsOnTags.values[tagIndex];
				final int urlsOnTagIndex = urlsOnTag.binarySearch(url);
				urlsOnTag.remove(urlsOnTagIndex);
			}
			urls.remove(urlIndex);
			tagsOnURLs.remove(urlIndex);
			removeUnusedTags(tagsOnURLUnused);
			urlsSearch.removeFromResult(url);
		}
	}

	private void setStubs ( ) {
		final String pathNotSet = "path not set";
		pathString = pathNotSet;
		listLabel = pathNotSet;
		fileName = pathNotSet;
		fileSize = -1;
	}

	private final int addTag ( final String tag ) {
		final int index = getTagIndex(tag);
		if ( index < 0 ) {
			final int insertIndex = -index - 1;
			final DynArrayOfString urlsOnTag = new DynArrayOfString();
			tags.add(insertIndex,tag);
			urlsOnTags.add(insertIndex,urlsOnTag);
			return insertIndex;
		}
		return index;
	}

	private void refreshFileSize ( ) {
		try {
			fileSize = Files.size(path);
		} catch ( IOException e ) {
			fileSize = -1;
			e.printStackTrace();
		}
	}

	private void removeUnusedTags ( final DynArrayOfString tags ) {
		for ( int i = 0; i < tags.valuesLength; i += 1 ) {
			final int tagsIndex = this.tags.binarySearch(tags.values[i]);
			if ( tagsIndex >= 0 && this.urlsOnTags.values[tagsIndex].valuesLength == 0 ) {
				this.tags.remove(tagsIndex);
				this.urlsOnTags.remove(tagsIndex);
			}
		}
	}

}

/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2016 Vitali Baumtrok
 * 
 * This file is part of URL Saver.
 * 
 * URL Saver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * URL Saver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with URL Saver.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.vbsw.urlsaver;


import java.nio.file.Path;
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
public class FileData {

	private static final int INITIAL_CAPACITY = 10;
	private static final String MODIFIED_TOKEN = "* ";

	private final TaggedWordsReader reader;
	private final ArrayList<String> prevSearchedTags;
	private final ArrayList<String> currSearchedTags;
	public final Path filePath;
	public final String filePathStr;
	public final String fileName;
	public final TaggedWords.SearchResult searchedUrls;

	private double loaded;
	private boolean modified;
	public TaggedWords urls;
	public String listName;
	public boolean searchedTagsEqual;

	public FileData ( final Path filePath ) {
		this.filePath = filePath;
		this.filePathStr = filePath.toString();
		this.fileName = filePath.getFileName().toString();
		this.searchedUrls = new TaggedWords.SearchResult();
		this.urls = new TaggedWords();
		this.reader = new TaggedWordsReader(filePath);
		this.prevSearchedTags = new ArrayList<String>(INITIAL_CAPACITY);
		this.currSearchedTags = new ArrayList<String>(INITIAL_CAPACITY);

		this.reader.progressProperty().addListener(new Listener.ProgressFileLoading(this));
		this.reader.setOnSucceeded(new Listener.SuccessFileRead(this));
		this.reader.setOnFailed(new Listener.FailFileRead());

		setLoaded(0);
	}

	public void setModified ( final boolean value ) {
		modified = value;
		listName = createListName();
	}

	public boolean isModified ( ) {
		return modified;
	}

	/**
	 * Sets loading progress.
	 * 
	 * @param value Must be between 0 and 1.
	 */
	public void setLoaded ( final double value ) {
		loaded = value;
		listName = createListName();
	}

	/**
	 * Reads the urls file.
	 */
	public void read ( ) {
		final FileData selectedItem = App.nodes.fileList.getSelectionModel().getSelectedItem();
		if ( selectedItem==this ) {
			App.nodes.urlsTab.setDisable(true);
		}
		reader.restart();
	}

	public boolean isLoaded ( ) {
		return loaded>=1;
	}

	public void setSearchedTags ( final String tagsString ) {
		int currIndex, offset;
		currSearchedTags.clear();

		for ( currIndex = 0, offset = 0; currIndex<tagsString.length(); currIndex += 1 ) {
			final char character = tagsString.charAt(currIndex);

			if ( Parser.isWhiteSpace(character) ) {
				addKey(currSearchedTags,offset,currIndex,tagsString);
				offset = currIndex+1;
			}
		}
		addKey(currSearchedTags,offset,currIndex,tagsString);
		updateSearchedTagsEqual();
	}
	
	/**
	 * Updates {@code searchedUrls} by searched tags.
	 */
	public void updateSearchedUrls() {
		prevSearchedTags.clear();
		prevSearchedTags.addAll(currSearchedTags);
		searchedUrls.searchAND(urls,currSearchedTags);
	}

	public void clearSearch ( ) {
		prevSearchedTags.clear();
		currSearchedTags.clear();
	}

	private void updateSearchedTagsEqual ( ) {
		int counter = 0;
		Outer:
		for ( String currKeyStr: currSearchedTags ) {
			for ( String prevKeyStr: prevSearchedTags ) {
				if ( currKeyStr.equals(prevKeyStr) ) {
					counter += 1;
					continue Outer;
				}
			}
			searchedTagsEqual = false;
			return;
		}
		searchedTagsEqual = prevSearchedTags.size()==counter;
	}

	private void addKey ( final ArrayList<String> keys, final int offset, final int currIndex, final String keysStr ) {
		final int keyLength = currIndex-offset;
		if ( keyLength>0 ) {
			final String keyStr = keysStr.substring(offset,currIndex);
			if ( keys.contains(keyStr)==false ) {
				keys.add(keyStr);
			}
		}
	}

	private String createListName ( ) {
		final int percent = (int) ( loaded*100 );

		if ( percent<100 ) {
			final String prefix = "   "; //$NON-NLS-1$
			final String postfix = "%"; //$NON-NLS-1$

			if ( modified ) {
				return MODIFIED_TOKEN+fileName+prefix+percent+postfix;

			} else {
				return fileName+prefix+percent+postfix;
			}

		} else if ( modified ) {
			return MODIFIED_TOKEN+fileName;

		} else {
			return fileName;
		}
	}

}

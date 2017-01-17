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


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;


/**
 * @author Vitali Baumtrok
 */
public class FileData {

	private static final int INITIAL_CAPACITY = 10;
	private static final String MODIFIED_TOKEN = " *"; //$NON-NLS-1$

	private final TaggedWordsReader reader;
	private final StringList newSearchedTags;
	public final Path filePath;
	public final String filePathStr;
	public final String fileName;
	public final StringList searchedTags;
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
		this.newSearchedTags = new StringList(INITIAL_CAPACITY);
		this.searchedTags = new StringList(INITIAL_CAPACITY);

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
		if ( selectedItem == this ) {
			App.nodes.urlsTab.setDisable(true);
		}
		reader.restart();
	}

	public boolean isLoaded ( ) {
		return loaded >= 1;
	}

	public void setSearchedTags ( final String tagsString ) {
		newSearchedTags.set(tagsString);
		searchedTagsEqual = searchedTags.equals(newSearchedTags);
	}

	public void updateSearchedUrls ( ) {
		searchedTags.clear();
		searchedTags.addAll(newSearchedTags);
		searchedUrls.searchAND(urls,searchedTags);
	}

	public void save ( ) {
		final int space = ' ';
		try ( final BufferedWriter writer = Files.newBufferedWriter(filePath,App.STRING_ENCODING) ) {
			for ( TaggedWords.Word word: urls ) {
				writer.write(word.string);
				writer.newLine();
				if ( word.tags.size() > 0 ) {
					writer.write(word.tags.get(0).string);
				}
				for ( int i = 1; i < word.tags.size(); i += 1 ) {
					writer.write(space);
					writer.write(word.tags.get(i).string);
				}
				writer.newLine();
			}
			setModified(false);

		} catch ( final IOException e ) {
			e.printStackTrace();
		}
	}

	private String createListName ( ) {
		final int percent = (int) (loaded * 100);

		if ( percent < 100 ) {
			final String prefix = "   "; //$NON-NLS-1$
			final String postfix = "%"; //$NON-NLS-1$

			if ( modified ) {
				return fileName + MODIFIED_TOKEN + prefix + percent + postfix;

			} else {
				return fileName + prefix + percent + postfix;
			}

		} else if ( modified ) {
			return fileName + MODIFIED_TOKEN;

		} else {
			return fileName;
		}
	}

}

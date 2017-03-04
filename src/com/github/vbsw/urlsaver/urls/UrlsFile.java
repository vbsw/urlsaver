/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
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


package com.github.vbsw.urlsaver.urls;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.SortedUniqueStringList;
import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.resources.Resources;


/**
 * @author Vitali Baumtrok
 */
public class UrlsFile {

	private UrlsData data;
	private boolean dirty;
	private Path path;
	private UrlsLoadService loadService;
	private String itemText;

	private int selectedUrlIndex;
	private String searchTagsString;
	private SearchUrlList searchUrls;

	public UrlsFile ( final Path filePath ) {
		data = null;
		dirty = false;
		path = filePath;
		loadService = new UrlsLoadService(filePath);
		itemText = getLoadingListViewText(0);
		searchTagsString = ""; //$NON-NLS-1$
		searchUrls = new SearchUrlList();

		loadService.progressProperty().addListener(new UrlsLoadProgressListener(this));
		loadService.setOnFailed(new UrlsLoadFailedListener());
		loadService.setOnSucceeded(new UrlsLoadSucceededListener(this));
	}

	public void load ( ) {
		data = null;
		searchTagsString = ""; //$NON-NLS-1$

		if ( isSelected() ) {
			App.scene.tf.urlSearch.setText(searchTagsString);
			App.scene.lv.urls.getItems().clear();
		}
		searchUrls.clear();
		App.files.loadedProperty().set(isSelected() == false);
		loadService.restart();
	}

	public boolean isSelected ( ) {
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();

		return this == selectedUrlsFile;
	}

	public UrlsData getData ( ) {
		return data;
	}

	public void setData ( final UrlsData urlsData ) {
		data = urlsData;
	}

	public boolean isDirty ( ) {
		return dirty;
	}

	public String getPath ( ) {
		return path.toString();
	}

	public String getItemText ( ) {
		return itemText;
	}

	public void setItemText ( final String text ) {
		itemText = text;

		App.scene.lv.files.refresh();
	}

	public boolean isDefault ( ) {
		final String defaultUrlsFileName = App.settings.getUrlsFileSelect();

		return path.getFileName().toString().equals(defaultUrlsFileName);
	}

	public String getFileName ( ) {
		return path.getFileName().toString();
	}

	public String getLoadingListViewText ( final int percent ) {
		final String listViewText;

		if ( percent < 0 ) {
			listViewText = getPath() + "  0%"; //$NON-NLS-1$

		} else if ( percent < 100 ) {
			listViewText = getPath() + "  " + percent + "%"; //$NON-NLS-1$

		} else {
			listViewText = getPath();
		}
		return listViewText;
	}

	public void save ( ) {
		final int space = ' ';

		try ( final BufferedWriter writer = Files.newBufferedWriter(path,Resources.ENCODING) ) {
			for ( int i = 0; i < data.urls.size(); i += 1 ) {
				final String url = data.urls.get(i);
				final SortedUniqueStringList urlTags = data.urlTagsList.get(i);

				writer.write(url);
				writer.newLine();

				if ( urlTags.size() > 0 ) {
					writer.write(urlTags.get(0));
				}
				for ( int j = 1; j < urlTags.size(); j += 1 ) {
					writer.write(space);
					writer.write(urlTags.get(j));
				}
				writer.newLine();
			}
			dirty = false;

		} catch ( final IOException e ) {
			e.printStackTrace();
		}
	}

	public void setSearchTagsString ( final String tagsString ) {
		searchTagsString = tagsString;
	}

	public void updateUrlsSearchView ( ) {
		App.scene.tf.urlSearch.setText(searchTagsString);
		App.scene.lv.urls.getItems().setAll(searchUrls);
		App.scene.lv.urls.getSelectionModel().select(selectedUrlIndex);
	}

	public void fillSearchUrls ( ) {
		if ( App.settings.isSearchByPrefix() ) {
			searchUrls.fillByPrefix(data,searchTagsString);

		} else {
			searchUrls.fillByWord(data,searchTagsString);
		}
	}

	public ArrayList<String> getSearchUrls ( ) {
		return searchUrls;
	}

	public SortedUniqueStringList getSearchTags ( ) {
		return searchUrls.getTags();
	}

	public void setUrlIndex ( final int index ) {
		selectedUrlIndex = index;
	}

	public int getUrlIndex ( ) {
		return selectedUrlIndex;
	}

	public void setDirty ( ) {
		dirty = true;
	}

}

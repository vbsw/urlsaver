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


import java.util.ArrayList;

import com.github.vbsw.urlsaver.Convert;
import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.SortedUniqueStringList;
import com.github.vbsw.urlsaver.app.App;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;


/**
 * @author Vitali Baumtrok
 */
public class Urls {

	private final SortedUniqueStringList tagsTmp = new SortedUniqueStringList();
	private final SimpleBooleanProperty available = new SimpleBooleanProperty();
	private final SimpleBooleanProperty selected = new SimpleBooleanProperty();
	private final SimpleBooleanProperty deleteRequested = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlModified = new SimpleBooleanProperty();
	private final SimpleBooleanProperty tagsModified = new SimpleBooleanProperty();

	private UrlsData data = null;
	private UrlsViewData viewData = null;

	public void setData ( final UrlsData urlsData, final UrlsViewData urlsViewData ) {
		data = urlsData;
		viewData = urlsViewData;

		available.set(urlsData != null);
		selected.set(false);
		deleteRequested.set(false);
		urlModified.set(false);
		tagsModified.set(false);

		if ( urlsViewData != null ) {
			App.scene.tf.urlSearch.setText(urlsViewData.searchTagsString);
			App.scene.lv.urls.getItems().setAll(urlsViewData.searchResult);

			if ( App.scene.lv.urls.getItems().size() > 0 ) {
				App.scene.lv.urls.requestFocus();
				App.scene.lv.urls.getSelectionModel().select(urlsViewData.selectedIndex);
			}
		}
	}

	public void search ( ) {
		if ( App.settings.isSearchByPrefix() ) {
			viewData.searchResult.fillByPrefix(data,viewData.searchTagsString);

		} else {
			viewData.searchResult.fillByWord(data,viewData.searchTagsString);
		}
		App.scene.lv.urls.getItems().setAll(viewData.searchResult);

		if ( App.scene.lv.urls.getItems().size() > 0 ) {
			App.scene.lv.urls.requestFocus();
			App.scene.lv.urls.getSelectionModel().select(0);
		}
		App.urls.resetSelectedInfo();
	}

	public UrlsData getData ( ) {
		return data;
	}

	public UrlsViewData getViewData ( ) {
		return viewData;
	}

	public SimpleBooleanProperty availableProperty ( ) {
		return available;
	}

	public SimpleBooleanProperty selectedProperty ( ) {
		return selected;
	}

	public ObservableBooleanValue deleteRequestedProperty ( ) {
		return deleteRequested;
	}

	public ObservableBooleanValue urlModifiedProperty ( ) {
		return urlModified;
	}

	public ObservableBooleanValue tagsModifiedProperty ( ) {
		return tagsModified;
	}

	public void resetSelectedInfo ( ) {
		final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();

		if ( selectedUrl != null ) {
			final int urlIndex = data.getUrlIndex(selectedUrl);
			final SortedUniqueStringList urlTags = data.urlTagsList.get(urlIndex);
			final String tagsString = Convert.toString(urlTags);

			viewData.selectedIndex = App.scene.lv.urls.getSelectionModel().getSelectedIndex();

			App.scene.tf.url.setText(selectedUrl);
			App.scene.ta.tags.setText(tagsString);

		} else {
			final String emptyString = ""; //$NON-NLS-1$

			viewData.selectedIndex = -1;

			App.scene.tf.url.setText(emptyString);
			App.scene.ta.tags.setText(emptyString);
		}
		selected.setValue(selectedUrl != null);
		deleteRequested.set(false);
	}

	public void deleteSelected ( ) {
		deleteRequested.set(true);
		App.scene.btn.urlCancel.requestFocus();
	}

	public void cancel ( ) {
		resetSelectedInfo();
		App.scene.lv.urls.requestFocus();
	}

	public void updateUrlModifiedProperty ( ) {
		final String urlSelected = App.scene.lv.urls.getSelectionModel().getSelectedItem();
		final String urlTyped = Parser.trim(App.scene.tf.url.getText());

		if ( urlSelected != null ) {
			urlModified.set(data.getUrlIndex(urlTyped) < 0);

		} else {
			urlModified.set(urlTyped.length() > 0);
		}
	}

	public void updateTagsModifiedProperty ( ) {
		final String urlSelected = App.scene.lv.urls.getSelectionModel().getSelectedItem();
		final String tagsTyped = Parser.trim(App.scene.ta.tags.getText());

		if ( urlSelected != null ) {
			final int urlIndex = data.getUrlIndex(urlSelected);
			final SortedUniqueStringList tagsSelected = data.urlTagsList.get(urlIndex);

			tagsTmp.setStrings(tagsTyped);
			tagsModified.setValue(tagsTmp.isEqualByStrings(tagsSelected) == false);

		} else {
			tagsModified.setValue(tagsTyped.length() > 0);
		}
	}

	public void confirmDelete ( ) {
		final int fileIndex = App.files.getSelectedFileIndex();
		final String urlSelected = App.scene.lv.getSelectedUrl();
		final int selectedIndex = viewData.selectedIndex;

		data.removeUrl(urlSelected);
		viewData.searchResult.remove(selectedIndex);
		App.scene.lv.urls.getItems().remove(selectedIndex);
		App.files.setDirty(fileIndex);

		if ( viewData.searchResult.size() > selectedIndex ) {
			App.scene.lv.urls.getSelectionModel().select(selectedIndex);
		}
		deleteRequested.setValue(false);
		App.scene.lv.urls.requestFocus();
	}

	public void confirmCreate ( ) {
		final int fileIndex = App.files.getSelectedFileIndex();
		final String url = Parser.trim(App.scene.tf.url.getText());
		final int urlIndex = data.addUrl(url);

		tagsTmp.setStrings(App.scene.ta.tags.getText());
		data.setTagsOfUrl(urlIndex,tagsTmp);
		App.files.setDirty(fileIndex);

		final ArrayList<String> urlTags = data.urlTagsList.get(urlIndex);
		final String tagsString = Convert.toString(urlTags);

		App.scene.ta.tags.setText(tagsString);
		App.scene.lv.urls.requestFocus();
		urlModified.set(false);
		tagsModified.set(false);
	}

	public void confirmEdit ( ) {
		final int fileIndex = App.files.getSelectedFileIndex();
		final int urlIndex = data.getUrlIndex(App.scene.lv.getSelectedUrl());

		tagsTmp.setStrings(App.scene.ta.tags.getText());
		data.setTagsOfUrl(urlIndex,tagsTmp);
		App.files.setDirty(fileIndex);

		final ArrayList<String> urlTags = data.urlTagsList.get(urlIndex);
		final String tagsString = Convert.toString(urlTags);

		App.scene.ta.tags.setText(tagsString);
		App.scene.lv.urls.requestFocus();
		tagsModified.set(false);
	}

	public void confirmAny ( ) {
		if ( App.scene.btn.urlDeleteOK.isDisable() == false ) {
			confirmDelete();

		} else if ( App.scene.btn.urlCreateOK.isDisable() == false ) {
			confirmCreate();

		} else if ( App.scene.btn.urlEditOK.isDisable() == false ) {
			confirmEdit();
		}
	}

}

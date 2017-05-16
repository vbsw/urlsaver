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


package com.github.vbsw.urlsaver.app.window.urls;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.utility.Convert;
import com.github.vbsw.urlsaver.utility.Parser;
import com.github.vbsw.urlsaver.utility.SortedUniqueStringList;


/**
 * @author Vitali Baumtrok
 */
public class Urls {

	private final SortedUniqueStringList tagsTmp = new SortedUniqueStringList();

	private UrlsData data = null;
	private UrlsViewData viewData = null;

	public final UrlsViewModel vm = new UrlsViewModel();

	public void setData ( final UrlsData urlsData, final UrlsViewData urlsViewData ) {
		data = urlsData;
		viewData = urlsViewData;

		vm.initProperties(urlsData != null);

		if ( urlsData != null ) {
			final int selectedIndex = urlsViewData.selectedIndex;

			App.scene.tf.urlSearch.setText(urlsViewData.searchTagsString);
			// setAll() resets selectedIndex
			App.scene.lv.urls.getItems().setAll(urlsViewData.searchResult);

			viewData.selectedIndex = selectedIndex;

			if ( App.scene.lv.urls.getItems().size() > 0 ) {
				App.scene.lv.urls.requestFocus();
				App.scene.lv.urls.getSelectionModel().select(urlsViewData.selectedIndex);
			}
		}
	}

	public void updateSearchResult ( ) {
		if ( App.settings.isSearchByPrefix() ) {
			viewData.searchResult.fillByPrefix(data,viewData.searchTagsString);

		} else {
			viewData.searchResult.fillByWord(data,viewData.searchTagsString);
		}
	}

	public void updateSearchResultListView ( ) {
		App.scene.lv.urls.getItems().setAll(viewData.searchResult);

		if ( App.scene.lv.urls.getItems().size() > 0 ) {
			App.scene.lv.urls.requestFocus();
			App.scene.lv.urls.getSelectionModel().select(0);
		}
	}

	public void setSelectedAsInfoView ( ) {
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
	}

	public void focusUrlsListOrSearchView ( ) {
		if ( App.scene.lv.urls.getItems().size() > 0 ) {
			App.scene.lv.urls.requestFocus();

		} else {
			App.scene.tf.urlSearch.requestFocus();
		}
	}

	public UrlsData getData ( ) {
		return data;
	}

	public UrlsViewData getViewData ( ) {
		return viewData;
	}

	public void confirmDelete ( ) {
		final String urlTyped = Parser.trim(App.scene.tf.url.getText());
		final String urlSelected = App.scene.lv.getSelectedUrl();
		final int fileIndex = App.files.getSelectedFileIndex();

		data.removeUrl(urlTyped);
		App.files.setDirty(fileIndex);

		if ( urlTyped.equals(urlSelected) ) {
			final int selectedIndex = viewData.selectedIndex;

			viewData.searchResult.remove(selectedIndex);
			App.scene.lv.urls.getItems().remove(selectedIndex);

			if ( viewData.searchResult.size() > selectedIndex ) {
				App.scene.lv.urls.getSelectionModel().select(selectedIndex);
			}
		}
		App.scene.lv.urls.requestFocus();
	}

	public void confirmCreate ( ) {
		final int fileIndex = App.files.getSelectedFileIndex();
		final String url = Parser.trim(App.scene.tf.url.getText());
		final int urlIndex = data.addUrl(url);

		tagsTmp.addStringsSeparatedByWhiteSpace(App.scene.ta.tags.getText());
		data.setTagsOfUrl(urlIndex,tagsTmp);
		tagsTmp.clear();
		App.files.setDirty(fileIndex);

		final ArrayList<String> urlTags = data.urlTagsList.get(urlIndex);
		final String tagsString = Convert.toString(urlTags);

		App.scene.ta.tags.setText(tagsString);
		App.scene.lv.urls.requestFocus();
	}

	public void confirmEdit ( ) {
		final String url = Parser.trim(App.scene.tf.url.getText());
		final int fileIndex = App.files.getSelectedFileIndex();
		final int urlIndex = data.getUrlIndex(url);

		tagsTmp.addStringsSeparatedByWhiteSpace(App.scene.ta.tags.getText());
		data.setTagsOfUrl(urlIndex,tagsTmp);
		tagsTmp.clear();
		App.files.setDirty(fileIndex);

		final ArrayList<String> urlTags = data.urlTagsList.get(urlIndex);
		final String tagsString = Convert.toString(urlTags);

		App.scene.ta.tags.setText(tagsString);
		App.scene.lv.urls.requestFocus();
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

	public void focusUrlCancel ( ) {
		App.scene.btn.urlCancel.requestFocus();
	}

}

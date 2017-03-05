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


package com.github.vbsw.urlsaver.scene.controller;


import com.github.vbsw.urlsaver.Convert;
import com.github.vbsw.urlsaver.SortedUniqueStringList;
import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.urls.UrlsData;
import com.github.vbsw.urlsaver.urls.UrlsFile;


/**
 * @author Vitali Baumtrok
 */
public final class UrlsCtrl {

	private static final SortedUniqueStringList tagsTmp = new SortedUniqueStringList();

	public static void selectedUrlCreateOrEdit ( ) {
		if ( App.scene.btn.urlCreateOK.isDisable() == false ) {
			UrlsCtrl.selectedUrlCreateOK();

		} else if ( App.scene.btn.urlEditOK.isDisable() == false ) {
			UrlsCtrl.selectedUrlEditOK();
		}
	}

	public static void selectedUrlEditOK ( ) {
		final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();
		final UrlsData urlsData = selectedUrlsFile.getData();
		final int selectedUrlIndex = urlsData.getUrlIndex(selectedUrl);
		final SortedUniqueStringList selectedTags = urlsData.urlTagsList.get(selectedUrlIndex);

		tagsTmp.setStrings(App.scene.ta.tags.getText());
		selectedTags.setAllSorted(tagsTmp);
		selectedUrlsFile.setDirty();
		App.files.dirtyProperty().setValue(true);
		UrlsCtrl.updateUrlsView(selectedUrlsFile,selectedUrl,selectedTags);
	}

	public static void selectedUrlCreateOK ( ) {
		final String url = App.scene.tf.url.getText();
		final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();
		final UrlsData urlsData = selectedUrlsFile.getData();
		final int selectedUrlIndex = urlsData.getUrlIndex(selectedUrl);
		final SortedUniqueStringList selectedTags = urlsData.urlTagsList.get(selectedUrlIndex);
		final int urlIndex = urlsData.addUrl(url);

		tagsTmp.setStrings(App.scene.ta.tags.getText());

		for ( String tag: tagsTmp ) {
			urlsData.addTagToUrl(urlIndex,tag);
		}
		selectedUrlsFile.setDirty();
		App.files.dirtyProperty().setValue(true);
		UrlsCtrl.updateUrlsView(selectedUrlsFile,selectedUrl,selectedTags);
	}

	public static void searchCurrent ( ) {
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();

		selectedUrlsFile.fillSearchUrls();
		App.scene.lv.urls.getItems().setAll(selectedUrlsFile.getSearchUrls());

		if ( App.scene.lv.urls.getItems().size() > 0 ) {
			App.scene.lv.urls.requestFocus();
			App.scene.lv.urls.getSelectionModel().select(0);
		}
	}

	public static void deleteCurrentWithConfirm ( ) {
		App.scene.deletingModeProperty().setValue(true);
		App.scene.btn.urlCancel.requestFocus();
	}

	public static void selectCurrent ( ) {
		final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();

		if ( selectedUrl != null ) {
			UrlsCtrl.updateUrlsView(selectedUrl);

		} else {
			UrlsCtrl.updateUrlsView();
		}
		App.scene.deletingModeProperty().setValue(false);
	}

	public static void cancel ( ) {
		final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();

		if ( selectedUrl != null ) {
			final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();
			final UrlsData urlsData = selectedUrlsFile.getData();
			final int selectedUrlIndex = urlsData.getUrlIndex(selectedUrl);
			final SortedUniqueStringList selectedTags = urlsData.urlTagsList.get(selectedUrlIndex);
			final String tags = Convert.toString(selectedTags);

			App.scene.tf.url.setText(selectedUrl);
			App.scene.ta.tags.setText(tags);
			App.scene.lv.urls.requestFocus();

		} else {
			App.scene.tf.url.setText("");
			App.scene.ta.tags.setText("");
		}
		App.scene.deletingModeProperty().setValue(false);
	}

	public static void deleteCurrent ( ) {
		final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();
		final int selectedUrlIndex = App.scene.lv.urls.getSelectionModel().getSelectedIndex();

		App.scene.deletingModeProperty().setValue(false);

	}

	private static void updateUrlsView ( final UrlsFile selectedUrlsFile, final String selectedUrl, final SortedUniqueStringList selectedTags ) {
		final String tagsString = Convert.toString(selectedTags);

		App.scene.tf.url.setText(selectedUrl);
		App.scene.ta.tags.setText(tagsString);
		App.scene.lv.urls.requestFocus();
		App.scene.updateWindowTitle();
	}

	private static void updateUrlsView ( final String selectedUrl ) {
		final int selectedUrlIndex = App.scene.lv.urls.getSelectionModel().getSelectedIndex();
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();
		final UrlsData urlsData = selectedUrlsFile.getData();
		final int urlIndex = urlsData.getUrlIndex(selectedUrl);
		final SortedUniqueStringList urlTags = urlsData.urlTagsList.get(urlIndex);
		final String tagsString = Convert.toString(urlTags);

		App.scene.tf.url.setText(selectedUrl);
		App.scene.ta.tags.setText(tagsString);
		selectedUrlsFile.setUrlIndex(selectedUrlIndex);
	}

	private static void updateUrlsView ( ) {
		final String emptyString = ""; //$NON-NLS-1$

		App.scene.tf.url.setText(emptyString);
		App.scene.ta.tags.setText(emptyString);
	}

}

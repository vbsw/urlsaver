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
		UrlsCtrl.updateUrlsView(selectedUrlsFile,selectedUrl,selectedTags);
	}

	private static void updateUrlsView ( final UrlsFile selectedUrlsFile, final String selectedUrl, final SortedUniqueStringList selectedTags ) {
		final String tagsString = Convert.toString(selectedTags);

		App.scene.tf.url.setText(selectedUrl);
		App.scene.ta.tags.setText(tagsString);
		App.scene.lv.urls.requestFocus();
		App.scene.updateWindowTitle();
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.worker;

import com.github.vbsw.urlsaver.gui.Properties;

/**
 * @author Vitali Baumtrok
 */
public class URLsLogic {

	public static void updateSearchResult ( ) {
		// TODO Auto-generated method stub
	}

	public static void updateSearchResultListView ( ) {
		// TODO Auto-generated method stub
	}

	public static void setSelectedAsInfoView ( ) {
		// TODO Auto-generated method stub
	}

	public static void finalizeURLSearch ( ) {
//		exists.set(App.scene.tf.url.getText().length() > 0);
//		deleteRequested.set(false);
//		urlModified.set(false);
//		tagsModified.set(false);
	}

	public static void focusUrlsListOrSearchView ( ) {
		// TODO Auto-generated method stub
	}

	public static void finalizeURLCancel ( ) {
//		exists.set(App.scene.tf.url.getText().length() > 0);
//		deleteRequested.set(false);
//		urlModified.set(false);
//		tagsModified.set(false);
	}

	public static void confirmURLDelete ( ) {
//		final String urlTyped = Parser.trim(GUI.tf.url.getText());
//		final String urlSelected = URLsLogic.getSelectedUrl();
//		final int fileIndex = App.files.getSelectedFileIndex();
//
//		data.removeUrl(urlTyped);
//		App.files.setDirty(fileIndex);
//
//		if ( urlTyped.equals(urlSelected) ) {
//			final int selectedIndex = viewData.selectedIndex;
//
//			viewData.searchResult.remove(selectedIndex);
//			App.scene.lv.urls.getItems().remove(selectedIndex);
//
//			if ( viewData.searchResult.size() > selectedIndex ) {
//				App.scene.lv.urls.getSelectionModel().select(selectedIndex);
//			}
//		}
//		App.scene.lv.urls.requestFocus();
	}

	public static void finalizeURLDelete ( ) {
		Properties.urlDeleteRequestedProperty().set(false);
	}

	public static String getSelectedUrl ( ) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void confirmURLCreate ( ) {
		// TODO Auto-generated method stub
	}

	public static void finalizeURLCreate ( ) {
		Properties.urlExistsProperty().set(true);
		Properties.urlModifiedProperty().set(false);
	}

	public static void confirmEdit ( ) {
		// TODO Auto-generated method stub
	}

}

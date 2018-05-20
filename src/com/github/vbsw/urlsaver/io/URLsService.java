/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.gui.ListViews;
import com.github.vbsw.urlsaver.gui.Properties;
import com.github.vbsw.urlsaver.gui.TextAreas;
import com.github.vbsw.urlsaver.gui.TextFields;


/**
 * @author Vitali Baumtrok
 */
public class URLsService {

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

	public static void confirmURLEdit ( ) {
		// TODO Auto-generated method stub
	}

	public static String getSelectedUrl ( ) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void confirmURLCreate ( ) {
		final DBRecord selectedRecord = ListViews.files.control.getSelectionModel().getSelectedItem();
		final String url = Parser.trim(TextFields.url.control.getText());
		final int urlIndex = selectedRecord.addUrl(url);
		final ArrayList<String> tags = URLsService.stringToList(TextAreas.tags.control.getText());
		selectedRecord.setDirty(true);
		for ( final String tag: tags )
			selectedRecord.addTagToUrl(urlIndex,tag);
		final String tagsString = selectedRecord.getTagsAsString(urlIndex);
		TextAreas.tags.control.setText(tagsString);
		ListViews.urls.control.requestFocus();
		Properties.urlExistsProperty().set(true);
		Properties.urlModifiedProperty().set(false);
		GUI.refreshFileInfo();
	}

	private static ArrayList<String> stringToList ( final String text ) {
		final ArrayList<String> list = new ArrayList<>();
		int beginIndex = Parser.seekContent(text,0,text.length());
		while ( beginIndex < text.length() ) {
			final int endIndex = Parser.seekWhitespace(text,beginIndex,text.length());
			final String key = text.substring(beginIndex,endIndex);
			beginIndex = Parser.seekContent(text,endIndex,text.length());
			list.add(key);
		}
		return list;
	}

}

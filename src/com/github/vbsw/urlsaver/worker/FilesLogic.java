/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.worker;


import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.ListViews;
import com.github.vbsw.urlsaver.gui.Properties;
import com.github.vbsw.urlsaver.gui.TextFields;


/**
 * @author Vitali Baumtrok
 */
public class FilesLogic {

	public static void saveAllFiles ( ) {
		// TODO Auto-generated method stub
	}

	public static void reloadSelectedFile ( ) {
		// TODO Auto-generated method stub
	}

	public static void saveSelectedFile ( ) {
		// TODO Auto-generated method stub
	}

	public static void cancelFileAction ( ) {
		// TODO Auto-generated method stub
	}

	public static void confirmSaveSelectedFile ( ) {
		// TODO Auto-generated method stub
	}

	public static void processFileSelection ( ) {
		final DBRecord selectedRecord = ListViews.files.control.getSelectionModel().getSelectedItem();
		final String pathString;
		// TODO
		//		final UrlsData urlsData;
		//		final UrlsViewData urlsViewData;

		if ( selectedRecord != null ) {
			pathString = selectedRecord.getPathAsString();
			//			urlsData = getUrlsData(dbPathIndex);
			//			urlsViewData = getUrlsViewData(dbPathIndex);

		} else {
			pathString = "";
			//			urlsData = null;
			//			urlsViewData = null;
		}
		TextFields.fileName.control.setText(pathString);
		//		App.urls.setData(urlsData,urlsViewData);
		//		final Path selectedFilePath = App.scene.lv.files.getSelectionModel().getSelectedItem();
		//		final int fileDataIndex = App.files.getDataIndex(selectedFilePath);
		//		final boolean dirty;
		//
		//		if ( fileDataIndex >= 0 ) {
		//			dirty = App.files.isDirty(fileDataIndex);
		//		} else {
		//			dirty = false;
		//		}
		//		selectedFileDirty.set(dirty);
		Properties.selectedProperty().set(selectedRecord != null);
		Properties.confirmingSaveProperty().set(false);
	}

}

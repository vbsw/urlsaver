/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.logic;


import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.JarFile;
import com.github.vbsw.urlsaver.db.DBFiles;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.pref.Preferences;


/**
 * @author Vitali Baumtrok
 */
public class FilesLogic {

	public static void refreshFilesList ( ) {
		final String fileExtension = Preferences.getURLsFileExtension().getSavedValue();
		final ArrayList<Path> filePaths = JarFile.getFilePaths(fileExtension);
		final ArrayList<Path> filePathsSorted = DBFiles.getSortedPaths(filePaths);
		final ArrayList<String> fileLabels = FilesLogic.getInitialFileLabels(filePathsSorted);

		DBFiles.setPathList(filePaths);
		DBFiles.setLabelList(fileLabels);
		
		GUI.listViews.files.getItems().addAll(DBFiles.getPaths());
	}

	public static void saveAllFiles ( ) {
		// TODO Auto-generated method stub
	}

	public static void reloadSelectedFile ( ) {
		// TODO Auto-generated method stub
	}

	public static void reloadAllFiles ( ) {
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

	public static void loadInitialFiles ( ) {
		if ( Preferences.getURLsFileAutoloadAll().getModifiedValue() )
			FilesLogic.reloadAllFiles();
	}

	private static ArrayList<String> getInitialFileLabels ( final ArrayList<Path> filePaths ) {
		final ArrayList<String> labels = new ArrayList<>(filePaths.size());
		for ( Path filePath: filePaths ) {
			final String label = FilesLogic.getFilesListLabel(filePath,0);
			labels.add(label);
		}
		return labels;
	}

	private static String getFilesListLabel ( final Path filePath, final int percentLoaded ) {
		final String listViewText;
		if ( percentLoaded < 0 )
			listViewText = filePath.toString() + "  0%";
		else if ( percentLoaded < 100 )
			listViewText = filePath.toString() + "  " + percentLoaded + "%";
		else
			listViewText = filePath.toString();
		return listViewText;
	}

	public static void selectDefaultFile ( ) {
		final String fileToSelect = Preferences.getURLsFileSelect().getModifiedValue();
		final int pathIndex = DBFiles.getIndexByFileName(fileToSelect);
		if ( pathIndex >= 0 ) {
			GUI.listViews.files.requestFocus();
			GUI.listViews.files.getSelectionModel().select(pathIndex);
		}
	}

	public static void processFileSelection ( ) {
		final Path selectedPath = GUI.listViews.files.getSelectionModel().getSelectedItem();
		final int dbPathIndex = DBFiles.getIndex(selectedPath);
		final String pathString;
		// TODO
		//		final UrlsData urlsData;
		//		final UrlsViewData urlsViewData;

		if ( dbPathIndex >= 0 ) {
			pathString = selectedPath.toString();
			//			urlsData = getUrlsData(dbPathIndex);
			//			urlsViewData = getUrlsViewData(dbPathIndex);

		} else {
			pathString = "";
			//			urlsData = null;
			//			urlsViewData = null;
		}
		GUI.textFields.fileName.setText(pathString);
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
		//		selected.set(fileDataIndex >= 0);
		//		confirmingSave.set(false);
	}

}

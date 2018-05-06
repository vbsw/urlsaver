/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.logic;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.db.DBFiles;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.pref.Preferences;


/**
 * @author Vitali Baumtrok
 */
public class WindowLogic {

	public static void showQuitAppConfirmation ( ) {
		/*
		 * if ( App.scene.tp.top.getSelectionModel().getSelectedItem() !=
		 * App.scene.topTab.about ) {
		 * App.scene.tp.top.getSelectionModel().select(App.scene.topTab.about);
		 * }
		 * if ( App.about.mv.confirmQuitAppProperty().get() == false ) {
		 * App.about.setConfirmQuitAppProperty(true);
		 * App.scene.btn.quitAppSave.requestFocus();
		 * }
		 */
	}

	public static void refreshTitle ( ) {
		final Path filePath = GUI.listViews.files.getSelectionModel().getSelectedItem();
		final String windowTitleCustom = Preferences.getWindowTitle().getSavedValue();
		final String windowTitle;

		if ( filePath != null ) {
			final int dbPathIndex = DBFiles.getIndex(filePath);
			if ( DBFiles.isDirty(dbPathIndex) )
				windowTitle = windowTitleCustom + " (" + filePath.getFileName() + " *)";
			else
				windowTitle = windowTitleCustom + " (" + filePath.getFileName() + ")";
		} else {
			windowTitle = windowTitleCustom;
		}
		GUI.setWindowTitle(windowTitle);
	}

	public static void selectPreferencesTab ( ) {
		GUI.tabPanes.top.getSelectionModel().select(GUI.topTabs.preferences);
		GUI.tabPanes.top.requestFocus();
	}

}

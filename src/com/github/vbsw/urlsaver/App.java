/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import com.github.vbsw.urlsaver.db.DB;
import com.github.vbsw.urlsaver.gui.Buttons;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.gui.HotKeys;
import com.github.vbsw.urlsaver.gui.TabPanes;
import com.github.vbsw.urlsaver.io.URLsIO;
import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class App extends Application {

	@Override
	public void start ( final Stage primaryStage ) throws Exception {
		Preferences.initialize(getParameters().getRaw());
		DB.initialize();
		GUI.initialize();

		primaryStage.setOnCloseRequest(event -> HotKeys.onCloseRequest(event));
		primaryStage.setScene(GUI.scene);
		primaryStage.setMaximized(Preferences.getWindowMaximized().getSavedValue());
		primaryStage.show();

		GUI.refreshPreferencesView();
		GUI.selectDefaultFile();
		URLsIO.initialize();
	}

	public static void quit ( ) {
		if ( DB.isSaved() ) {
			App.quitUnconditionally();
		} else {
			TabPanes.top.control.getSelectionModel().select(TabPanes.top.about.control);
			Buttons.quitAppSave.control.requestFocus();
		}
	}

	public static void quitUnconditionally ( ) {
		Platform.exit();
	}

}

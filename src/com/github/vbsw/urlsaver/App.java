/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import com.github.vbsw.urlsaver.db.DB;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.logic.FilesLogic;
import com.github.vbsw.urlsaver.logic.WindowCallbacks;
import com.github.vbsw.urlsaver.logic.WindowLogic;
import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class App extends Application {

	@Override
	public void start ( Stage primaryStage ) throws Exception {
		Preferences.initialize(getParameters().getRaw());
		GUI.initialize();
		DB.initialize();
		FilesLogic.reloadAllFiles();

		primaryStage.setOnCloseRequest(event -> WindowCallbacks.onCloseRequest(event));
		primaryStage.setScene(GUI.scene);
		primaryStage.setMaximized(Preferences.getWindowMaximized().getSavedValue());
		primaryStage.show();
	}

	public static void quit ( ) {
		if ( DB.isSaved() )
			App.quitUnconditionally();
		else
			WindowLogic.showQuitAppConfirmation();
	}

	public static void quitUnconditionally ( ) {
		Platform.exit();
	}

}

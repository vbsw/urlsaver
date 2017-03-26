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


package com.github.vbsw.urlsaver.app;


import com.github.vbsw.urlsaver.files.Files;
import com.github.vbsw.urlsaver.scene.Scene;
import com.github.vbsw.urlsaver.scene.handlers.WindowCloseHandler;
import com.github.vbsw.urlsaver.settings.Settings;
import com.github.vbsw.urlsaver.urls.Urls;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public final class App extends Application {

	public static Settings settings;
	public static Files files;
	public static Urls urls;
	public static Scene scene;
	public static boolean urlsFileAutoSelectRequested;

	public static void main ( final String[] args ) {
		final Help help = new Help(args);

		if ( help.isNone() ) {
			Application.launch(App.class,args);

		} else {
			help.print();
			Platform.exit();
		}
	}

	@Override
	public void start ( final Stage primaryStage ) throws Exception {
		App.settings = new Settings();
		App.files = new Files();
		App.urls = new Urls();
		App.scene = new Scene();

		App.files.initialize();
		App.scene.loadFXML();
		App.scene.loadCSS();

		primaryStage.setOnCloseRequest(new WindowCloseHandler());
		primaryStage.setScene(scene);
		primaryStage.setMaximized(App.settings.isWindowMaximized());
		primaryStage.show();

		App.scene.updateWindowTitle();
		App.scene.setDecorationSize(primaryStage.getWidth(),primaryStage.getHeight());
		App.files.selectDefault();
		App.files.processAutoLoad();
	}

	public static void close ( ) {
		if ( App.files.isAnyDirty() ) {
			if ( App.scene.tp.top.getSelectionModel().getSelectedItem() != App.scene.topTab.about ) {
				App.scene.tp.top.getSelectionModel().select(App.scene.topTab.about);
				App.scene.btn.quitApp.setDisable(true);
				App.scene.btn.quitAppSave.setDisable(false);
				App.scene.btn.quitAppOK.setDisable(false);
				App.scene.btn.quitAppSave.requestFocus();

			} else if ( App.scene.btn.quitApp.isDisable() == false ) {
				App.scene.btn.quitApp.setDisable(false);
				App.scene.btn.quitAppSave.setDisable(true);
				App.scene.btn.quitAppOK.setDisable(true);
				App.scene.btn.quitAppSave.requestFocus();
			}

		} else {
			App.exit();
		}
	}

	public static void exit ( ) {
		Platform.exit();
	}

	public static void topTabSelected ( final Tab tab ) {
		if ( tab != App.scene.topTab.about ) {
			App.scene.btn.quitApp.setDisable(false);
			App.scene.btn.quitAppSave.setDisable(true);
			App.scene.btn.quitAppOK.setDisable(true);
		}
	}

}

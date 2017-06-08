
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app;


import com.github.vbsw.urlsaver.app.window.Window;
import com.github.vbsw.urlsaver.app.window.about.About;
import com.github.vbsw.urlsaver.app.window.files.Files;
import com.github.vbsw.urlsaver.app.window.scene.Scene;
import com.github.vbsw.urlsaver.app.window.settings.Settings;
import com.github.vbsw.urlsaver.app.window.urls.Urls;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public final class App extends Application {

	public static Settings settings;
	public static Files files;
	public static Urls urls;
	public static About about;
	public static Scene scene;
	public static Window window;
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
		App.about = new About();
		App.scene = new Scene();
		App.window = new Window(primaryStage);

		App.window.setHotKeys(App.scene);
		App.files.initialize();
		App.scene.loadFXML();
		App.scene.loadCSS();

		primaryStage.setOnCloseRequest(event -> App.window.mv.onCloseRequest(event));
		primaryStage.setScene(scene);
		primaryStage.setMaximized(App.settings.isWindowMaximized());
		primaryStage.show();

		App.window.updateTitle();
		App.window.setDecorationSize(App.scene.getWidth(),App.scene.getHeight());
		App.files.selectDefault();
		App.files.processAutoLoad();
	}

	public static void close ( ) {
		if ( App.files.isAnyDirty() ) {
			if ( App.scene.tp.top.getSelectionModel().getSelectedItem() != App.scene.topTab.about ) {
				App.scene.tp.top.getSelectionModel().select(App.scene.topTab.about);
			}

			if ( App.about.mv.confirmQuitAppProperty().get() == false ) {
				App.about.setConfirmQuitAppProperty(true);
				App.scene.btn.quitAppSave.requestFocus();
			}

		} else {
			App.exit();
		}
	}

	public static void exit ( ) {
		Platform.exit();
	}

}

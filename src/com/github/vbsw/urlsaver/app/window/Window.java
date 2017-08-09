
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.app.window.scene.Scene;

import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class Window {

	private final Stage stage;

	private double decorationWidth;
	private double decorationHeight;

	public final WindowModelView mv = new WindowModelView();

	public Window ( final Stage primaryStage ) {
		stage = primaryStage;
	}

	public void updateTitle ( ) {
		final Path filePath = App.scene.lv.files.getSelectionModel().getSelectedItem();
		final String windowTitle;

		if ( filePath != null ) {
			final int fileDataIndex = App.files.getDataIndex(filePath);

			if ( App.files.isDirty(fileDataIndex) ) {
				windowTitle = App.settings.getWindowTitle() + " (" + filePath.getFileName() + " *)";

			} else {
				windowTitle = App.settings.getWindowTitle() + " (" + filePath.getFileName() + ")";
			}

		} else {
			windowTitle = App.settings.getWindowTitle();
		}
		stage.setTitle(windowTitle);
	}

	public void setHotKeys ( final Scene scene ) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> App.window.mv.keyPressed(event));
	}

	public void setDecorationSize ( final double sceneWidth, final double sceneHeight ) {
		decorationWidth = stage.getWidth() - sceneWidth;
		decorationHeight = stage.getHeight() - sceneHeight;
	}

	public void setSize ( final double sceneWidth, final double sceneHeight ) {
		stage.setWidth(sceneWidth + decorationWidth);
		stage.setHeight(sceneHeight + decorationHeight);
	}

	public void topTabSelected ( final Tab tab ) {
		if ( tab != App.scene.topTab.about ) {
			App.about.setConfirmQuitAppProperty(false);
		}
	}

	public boolean isMaximized ( ) {
		return stage.isMaximized();
	}

	public void setMaximized ( final boolean maximized ) {
		stage.setMaximized(maximized);
	}

}

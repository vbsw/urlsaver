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


package com.github.vbsw.urlsaver.app.window;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.app.Version;
import com.github.vbsw.urlsaver.app.window.scene.Scene;

import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class Window {

	private final Stage stage;

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
				windowTitle = App.settings.getWindowTitle() + " " + Version.STRING + " (" + filePath.getFileName() + " *)";

			} else {
				windowTitle = App.settings.getWindowTitle() + " " + Version.STRING + " (" + filePath.getFileName() + ")";
			}

		} else {
			windowTitle = App.settings.getWindowTitle() + " " + Version.STRING;
		}
		stage.setTitle(windowTitle);
	}

	public void setHotKeys ( final Scene scene ) {
		scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> App.window.mv.keyPressed(event));
	}

}

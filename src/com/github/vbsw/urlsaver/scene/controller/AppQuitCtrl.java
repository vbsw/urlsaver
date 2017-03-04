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


package com.github.vbsw.urlsaver.scene.controller;


import com.github.vbsw.urlsaver.app.App;

import javafx.application.Platform;
import javafx.scene.control.Tab;


/**
 * @author Vitali Baumtrok
 */
public class AppQuitCtrl {

	public static void closeApplication ( ) {
		if ( App.files.isDirty() ) {
			if ( App.scene.tp.top.getSelectionModel().getSelectedItem() != App.scene.topTab.about ) {
				App.scene.tp.top.getSelectionModel().select(App.scene.topTab.about);
				AppQuitCtrl.enableQuitConfirmation();
				App.scene.btn.quitAppSave.requestFocus();

			} else if ( App.scene.btn.quitApp.isDisable() == false ) {
				AppQuitCtrl.enableQuitConfirmation();
				App.scene.btn.quitAppSave.requestFocus();
			}

		} else {
			AppQuitCtrl.exitApplication();
		}
	}

	public static void exitApplication ( ) {
		Platform.exit();
	}

	public static void topTabSelected ( final Tab tab ) {
		if ( tab != App.scene.topTab.about ) {
			disableQuitConfirmation();
		}
	}

	private static void enableQuitConfirmation ( ) {
		App.scene.btn.quitApp.setDisable(true);
		App.scene.btn.quitAppSave.setDisable(false);
		App.scene.btn.quitAppOK.setDisable(false);
	}

	private static void disableQuitConfirmation ( ) {
		App.scene.btn.quitApp.setDisable(false);
		App.scene.btn.quitAppSave.setDisable(true);
		App.scene.btn.quitAppOK.setDisable(true);
	}

}

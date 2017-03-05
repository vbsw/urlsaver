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
import com.github.vbsw.urlsaver.urls.UrlsFile;


/**
 * @author Vitali Baumtrok
 */
public final class FilesCtrl {

	public static void saveCurrent ( ) {
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();

		if ( selectedUrlsFile != null ) {
			UrlsCtrl.selectedUrlCreateOrEdit();

			if ( selectedUrlsFile.isDirty() ) {
				selectedUrlsFile.save();
				App.files.savingModeProperty().setValue(false);
				App.scene.updateWindowTitle();
			}
		}
	}

	public static void reloadCurrent ( ) {
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();

		if ( selectedUrlsFile != null ) {
			selectedUrlsFile.load();
		}
	}

	public static void reloadAll ( ) {
		for ( UrlsFile urlsFile: App.scene.lv.files.getItems() ) {
			urlsFile.load();
		}
	}

	public static void selectCurrent ( ) {
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();

		if ( selectedUrlsFile != null ) {
			FilesCtrl.updateUrlsFileView(selectedUrlsFile);

		} else {
			FilesCtrl.updateUrlsFileView();
		}
		App.files.savingModeProperty().setValue(false);
		App.scene.updateWindowTitle();
	}

	public static void saveCurrentWithConfirm ( ) {
		App.files.savingModeProperty().setValue(true);
	}

	public static void saveCurrentCancel ( ) {
		App.files.savingModeProperty().setValue(false);
	}

	private static void updateUrlsFileView ( final UrlsFile selectedUrlsFile ) {
		final String filePathStr = selectedUrlsFile.getPath();

		App.scene.tf.fileName.setText(filePathStr);

		if ( selectedUrlsFile.getData() != null ) {
			App.files.loadedProperty().setValue(true);
			App.files.dirtyProperty().setValue(selectedUrlsFile.isDirty());

		} else {
			App.files.loadedProperty().setValue(false);
			App.files.dirtyProperty().setValue(false);
		}
	}

	private static void updateUrlsFileView ( ) {
		final String emptyString = ""; //$NON-NLS-1$

		App.scene.tf.fileName.setText(emptyString);
		App.files.loadedProperty().setValue(false);
		App.files.dirtyProperty().setValue(false);
	}

}

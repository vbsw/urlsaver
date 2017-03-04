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


package com.github.vbsw.urlsaver.scene.listeners;


import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.urls.UrlsFile;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * @author Vitali Baumtrok
 */
public class UrlsTabSelectionListener implements ChangeListener<Boolean> {

	private UrlsFile previouslySelectedUrlsFile = null;

	@Override
	public void changed ( final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue ) {
		if ( newValue ) {
			updateUrlsTabContent();
			setFocus();
		}
	}

	private void updateUrlsTabContent ( ) {
		final UrlsFile selectedFile = App.scene.lv.files.getSelectionModel().getSelectedItem();

		if ( previouslySelectedUrlsFile != selectedFile ) {
			previouslySelectedUrlsFile = selectedFile;

			previouslySelectedUrlsFile.updateUrlsSearchView();
		}
	}

	private void setFocus ( ) {
		if ( App.scene.lv.urls.getItems().isEmpty() == false ) {
			App.scene.lv.urls.requestFocus();

		} else {
			App.scene.tf.urlSearch.requestFocus();
		}
	}

}
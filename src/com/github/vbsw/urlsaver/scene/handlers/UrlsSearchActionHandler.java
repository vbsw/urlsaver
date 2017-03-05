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


package com.github.vbsw.urlsaver.scene.handlers;


import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.urls.UrlsFile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
public final class UrlsSearchActionHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle ( final ActionEvent event ) {
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();

		selectedUrlsFile.fillSearchUrls();
		App.scene.lv.urls.getItems().setAll(selectedUrlsFile.getSearchUrls());

		if ( App.scene.lv.urls.getItems().size() > 0 ) {
			App.scene.lv.urls.requestFocus();
			App.scene.lv.urls.getSelectionModel().select(0);
		}
	}

}

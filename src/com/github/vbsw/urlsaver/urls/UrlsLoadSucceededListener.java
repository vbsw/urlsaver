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


package com.github.vbsw.urlsaver.urls;


import com.github.vbsw.urlsaver.app.App;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
final class UrlsLoadSucceededListener implements EventHandler<WorkerStateEvent> {

	private final UrlsFile urlsFile;

	public UrlsLoadSucceededListener ( final UrlsFile urlsFile ) {
		this.urlsFile = urlsFile;
	}

	@Override
	public void handle ( final WorkerStateEvent event ) {
		final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();
		final UrlsData urlsData = (UrlsData) event.getSource().getValue();

		urlsFile.setData(urlsData);

		if ( urlsFile == selectedUrlsFile ) {
			App.files.loadedProperty().set(true);
		}

		if ( urlsFile.isDefault() && App.urlsFileAutoSelectRequested ) {
			App.urlsFileAutoSelectRequested = false;
			App.scene.lv.files.getSelectionModel().select(urlsFile);
			App.scene.tp.top.getSelectionModel().select(App.scene.topTab.urls);
			App.scene.tf.urlSearch.requestFocus();
		}
	}

}

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


package com.github.vbsw.urlsaver.files;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.urls.UrlsData;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
final class FileLoadSucceededListener implements EventHandler<WorkerStateEvent> {

	private final Path filePath;

	public FileLoadSucceededListener ( final Path filePath ) {
		this.filePath = filePath;
	}

	@Override
	public void handle ( final WorkerStateEvent event ) {
		final UrlsData urlsData = (UrlsData) event.getSource().getValue();

		App.files.setUrlsData(filePath,urlsData);
	}

}

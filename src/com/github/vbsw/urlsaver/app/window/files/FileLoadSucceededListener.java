
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.files;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.app.window.urls.UrlsData;

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

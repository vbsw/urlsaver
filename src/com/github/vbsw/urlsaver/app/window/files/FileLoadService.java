
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.files;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.window.urls.UrlsData;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


/**
 * @author Vitali Baumtrok
 */
final class FileLoadService extends Service<UrlsData> {

	private final Path urlsFilePath;

	public FileLoadService ( final Path urlsFilePath ) {
		this.urlsFilePath = urlsFilePath;
	}

	@Override
	protected Task<UrlsData> createTask ( ) {
		final Task<UrlsData> loadingTask = new FileLoadTask(urlsFilePath);

		return loadingTask;
	}

}

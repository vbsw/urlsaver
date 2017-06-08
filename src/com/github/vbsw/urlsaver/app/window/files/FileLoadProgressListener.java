
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.files;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * @author Vitali Baumtrok
 */
public class FileLoadProgressListener implements ChangeListener<Number> {

	private final Path filePath;

	public FileLoadProgressListener ( final Path filePath ) {
		this.filePath = filePath;
	}

	@Override
	public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
		final int percent = (int) (newValue.doubleValue() * 100);

		App.files.setFileLoadProgress(filePath,percent);
	}

}

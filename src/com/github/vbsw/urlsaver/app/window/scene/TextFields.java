
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.scene;


import com.github.vbsw.urlsaver.app.App;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.TextField;


/**
 * @author Vitali Baumtrok
 */
public final class TextFields {

	public final TextField fileName;
	public final TextField urlSearch;
	public final TextField url;
	public final TextField title;
	public final TextField width;
	public final TextField height;
	public final TextField fileExtension;
	public final TextField defaultFile;

	TextFields ( final Parent root ) {
		final String fileNameSelector = "#file_name_tf"; //$NON-NLS-1$
		final String urlSearchSelector = "#url_search_tf"; //$NON-NLS-1$
		final String urldSelector = "#url_tf"; //$NON-NLS-1$
		final String titleSelector = "#settings_title_tf"; //$NON-NLS-1$
		final String widthSelector = "#settings_width_tf"; //$NON-NLS-1$
		final String heightSelector = "#settings_height_tf"; //$NON-NLS-1$
		final String fileExtensionSelector = "#settings_file_extension_tf"; //$NON-NLS-1$
		final String defaultFileSelector = "#settings_default_file_tf"; //$NON-NLS-1$

		fileName = (TextField) root.lookup(fileNameSelector);
		urlSearch = (TextField) root.lookup(urlSearchSelector);
		url = (TextField) root.lookup(urldSelector);
		title = (TextField) root.lookup(titleSelector);
		width = (TextField) root.lookup(widthSelector);
		height = (TextField) root.lookup(heightSelector);
		fileExtension = (TextField) root.lookup(fileExtensionSelector);
		defaultFile = (TextField) root.lookup(defaultFileSelector);
	}

	public void configure ( ) {
		urlSearch.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.textField_urlSearch_changed(observable,oldValue,newValue));
		urlSearch.setOnAction(event -> App.urls.vm.textField_urlSearch_enterPressed(event));
		url.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.textField_url_changed(observable,oldValue,newValue));
	}

}

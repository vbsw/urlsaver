
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

	TextFields ( final Parent root ) {
		final String fileNameTextFieldSelector = "#file_name_tf"; //$NON-NLS-1$
		final String urlSearchTextFieldSelector = "#url_search_tf"; //$NON-NLS-1$
		final String urlTextFieldSelector = "#url_tf"; //$NON-NLS-1$

		fileName = (TextField) root.lookup(fileNameTextFieldSelector);
		urlSearch = (TextField) root.lookup(urlSearchTextFieldSelector);
		url = (TextField) root.lookup(urlTextFieldSelector);
	}

	public void configure ( ) {
		urlSearch.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.textField_urlSearch_changed(observable,oldValue,newValue));
		urlSearch.setOnAction(event -> App.urls.vm.textField_urlSearch_enterPressed(event));
		url.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.textField_url_changed(observable,oldValue,newValue));
	}

}

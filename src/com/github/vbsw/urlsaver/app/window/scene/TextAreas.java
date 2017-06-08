
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.scene;


import com.github.vbsw.urlsaver.app.App;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;


/**
 * @author Vitali Baumtrok
 */
public final class TextAreas {

	public final TextArea tags;

	TextAreas ( final Parent root ) {
		final String tagsTextAreaSelector = "#tags_ta"; //$NON-NLS-1$

		tags = (TextArea) root.lookup(tagsTextAreaSelector);
	}

	public void configure ( ) {
		tags.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.textField_tags_changed(observable,oldValue,newValue));
	}

}

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


package com.github.vbsw.urlsaver.scene;


import com.github.vbsw.urlsaver.scene.handlers.UrlsSearchActionHandler;
import com.github.vbsw.urlsaver.scene.listeners.UrlsSearchChangeListener;

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
		urlSearch.textProperty().addListener(new UrlsSearchChangeListener());
		urlSearch.setOnAction(new UrlsSearchActionHandler());
	}

}

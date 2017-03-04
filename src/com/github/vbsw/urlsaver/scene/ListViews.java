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


import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.scene.factories.FilesListViewCellFactory;
import com.github.vbsw.urlsaver.scene.factories.UrlsCellFactory;
import com.github.vbsw.urlsaver.scene.handlers.FilesKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.listeners.FileItemSelectionListener;
import com.github.vbsw.urlsaver.scene.listeners.UrlItemSelectionListener;
import com.github.vbsw.urlsaver.urls.UrlsFile;

import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;


/**
 * @author Vitali Baumtrok
 */
public final class ListViews {

	public final ListView<UrlsFile> files;
	public final ListView<String> urls;

	@SuppressWarnings ( "unchecked" )
	ListViews ( final Parent root ) {
		final String fileListViewSelector = "#file_list_view"; //$NON-NLS-1$
		final String urlListViewSelector = "#url_list_view"; //$NON-NLS-1$

		files = (ListView<UrlsFile>) root.lookup(fileListViewSelector);
		urls = (ListView<String>) root.lookup(urlListViewSelector);

		files.getItems().addAll(App.files);
	}

	public UrlsFile getSelectedUrlsFile ( ) {
		final MultipleSelectionModel<UrlsFile> selectionModel = files.getSelectionModel();
		final UrlsFile selectedItem = selectionModel.getSelectedItem();

		return selectedItem;
	}

	public String getSelectedUrl ( ) {
		final MultipleSelectionModel<String> selectionModel = urls.getSelectionModel();
		final String selectedItem = selectionModel.getSelectedItem();

		return selectedItem;
	}

	void configure ( ) {
		files.setCellFactory(new FilesListViewCellFactory());
		files.getSelectionModel().selectedItemProperty().addListener(new FileItemSelectionListener());
		files.setOnKeyPressed(new FilesKeyPressedHandler());
		urls.getSelectionModel().selectedItemProperty().addListener(new UrlItemSelectionListener());
		urls.setCellFactory(new UrlsCellFactory());
		urls.setOnKeyPressed(new UrlsKeyPressedHandler());
	}

}

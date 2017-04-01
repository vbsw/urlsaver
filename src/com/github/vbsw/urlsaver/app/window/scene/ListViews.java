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


package com.github.vbsw.urlsaver.app.window.scene;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;


/**
 * @author Vitali Baumtrok
 */
public final class ListViews {

	public final ListView<Path> files;
	public final ListView<String> urls;

	@SuppressWarnings ( "unchecked" )
	ListViews ( final Parent root ) {
		final String fileListViewSelector = "#file_list_view"; //$NON-NLS-1$
		final String urlListViewSelector = "#url_list_view"; //$NON-NLS-1$

		files = (ListView<Path>) root.lookup(fileListViewSelector);
		urls = (ListView<String>) root.lookup(urlListViewSelector);

		files.getItems().addAll(App.files.getPaths());
	}

	public String getSelectedUrl ( ) {
		final MultipleSelectionModel<String> selectionModel = urls.getSelectionModel();
		final String selectedItem = selectionModel.getSelectedItem();

		return selectedItem;
	}

	void configure ( ) {
		files.setCellFactory( ( ListView<Path> param ) -> App.files.mv.cellFactory(param));
		files.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) -> App.files.mv.listViewItem_selected(observable,oldValue,newValue));
		files.setOnKeyPressed(event -> App.files.mv.listView_keyPressed(event));
		urls.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.mv.listView_itemSelected(observable,oldValue,newValue));
		urls.setCellFactory( ( ListView<String> param ) -> App.urls.mv.cellFactory(param));
		urls.setOnKeyPressed(event -> App.urls.mv.listView_keyPressed(event));
	}

}


//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


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
		files.setCellFactory( ( ListView<Path> param ) -> App.files.vm.cellFactory(param));
		files.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) -> App.files.vm.listViewItem_selected(observable,oldValue,newValue));
		files.setOnKeyPressed(event -> App.files.vm.listView_keyPressed(event));
		urls.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.listViewItem_selected(observable,oldValue,newValue));
		urls.setCellFactory( ( ListView<String> param ) -> App.urls.vm.cellFactory(param));
		urls.setOnKeyPressed(event -> App.urls.vm.listView_keyPressed(event));
	}

}

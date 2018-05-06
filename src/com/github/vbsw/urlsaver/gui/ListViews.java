/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.db.DBFiles;
import com.github.vbsw.urlsaver.logic.FilesLogic;
import com.github.vbsw.urlsaver.logic.WindowLogic;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


/**
 * @author Vitali Baumtrok
 */
public class ListViews {

	public static final Files files = new Files();
	public static final URLs urls = new URLs();

	public static void build ( final Parent root ) {
		files.build(root);
		urls.build(root);
	}

	private static void files_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			event.consume();
			/*
			 * TODO
			 * if ( App.scene.topTab.urls.isDisable() == false ) {
			 * App.scene.tp.top.getSelectionModel().select(App.scene.topTab.urls
			 * );
			 * App.scene.tf.urlSearch.requestFocus();
			 * }
			 */
		}
	}

	private static void files_selected ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) {
		FilesLogic.processFileSelection();
		WindowLogic.refreshTitle();
	}

	private static void urls_selected ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO
	}

	private static void urls_keyPressed ( final KeyEvent event ) {
		// TODO Auto-generated method stub
	}

	private static ListCell<Path> filesCellFactory ( ListView<Path> param ) {
		final ListCell<Path> listCell = new FilePathListCell();
		return listCell;
	}

	private static ListCell<String> urlsCellFactory ( ListView<String> param ) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void filePathListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 )
			FilesLogic.processFileSelection();
	}

	public static final class Files {
		public ListView<Path> control;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ListView<Path>) root.lookup("#file_list_view");
			control.setCellFactory( ( ListView<Path> param ) -> ListViews.filesCellFactory(param));
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) -> ListViews.files_selected(observable,oldValue,newValue));
			control.setOnKeyPressed(event -> ListViews.files_keyPressed(event));
		}
	}

	public static final class URLs {
		public ListView<String> control;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ListView<String>) root.lookup("#url_list_view");
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> ListViews.urls_selected(observable,oldValue,newValue));
			control.setCellFactory( ( ListView<String> param ) -> ListViews.urlsCellFactory(param));
			control.setOnKeyPressed(event -> ListViews.urls_keyPressed(event));
		}
	}

	private static final class FilePathListCell extends ListCell<Path> {
		@Override
		protected void updateItem ( final Path item, final boolean empty ) {
			super.updateItem(item,empty);
			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);
			} else {
				final int listIndex = DBFiles.getIndex(item);
				final String text = DBFiles.getLabel(listIndex);
				setText(text);
				setOnMouseClicked(event -> ListViews.filePathListViewItem_clicked(event));
			}
		}
	}

}

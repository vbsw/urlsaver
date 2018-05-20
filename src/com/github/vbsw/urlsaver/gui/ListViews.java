/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.WebBrowserAccess;
import com.github.vbsw.urlsaver.db.DBRecord;

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

			//			TODO
			//			if ( App.scene.topTab.urls.isDisable() == false ) {
			//				App.scene.tp.top.getSelectionModel().select(App.scene.topTab.urls);
			//				App.scene.tf.urlSearch.requestFocus();
			//			}
		}
	}

	private static void files_selected ( final ObservableValue<? extends DBRecord> observable, final DBRecord oldValue, final DBRecord newValue ) {
		GUI.refereshFileState();
		GUI.refreshFileInfo();
		GUI.refreshTitle();
		GUI.refreshURLsView();
		GUI.refreshURLsInfo();
	}

	private static void urls_selected ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		GUI.refreshURLsInfo();
		Properties.resetURLsProperties();
	}

	private static void urls_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			final String selectedUrl = Parser.trim(ListViews.urls.control.getSelectionModel().getSelectedItem());
			if ( selectedUrl != null )
				WebBrowserAccess.openURL(selectedUrl);
		} else if ( keyCode == KeyCode.DELETE ) {
			Properties.urlDeleteRequestedProperty().set(true);
			Buttons.urlCancel.control.requestFocus();
		}
	}

	private static ListCell<DBRecord> filesCellFactory ( final ListView<DBRecord> param ) {
		final ListCell<DBRecord> listCell = new FilePathListCell();
		return listCell;
	}

	private static ListCell<String> urlsCellFactory ( final ListView<String> param ) {
		final ListCell<String> listCell = new UrlListCell();
		return listCell;
	}

	private static void filePathListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			GUI.refereshFileState();
			GUI.refreshFileInfo();
		}
	}

	private static void urlsListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			final UrlListCell cell = (UrlListCell) event.getSource();
			final String url = cell.getItem();
			WebBrowserAccess.openURL(url);
		}
	}

	public static final class Files {
		public ListView<DBRecord> control;
		public boolean autoSelectRequested;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ListView<DBRecord>) root.lookup("#file_list_view");
			control.setCellFactory( ( ListView<DBRecord> param ) -> ListViews.filesCellFactory(param));
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends DBRecord> observable, DBRecord oldValue, DBRecord newValue ) -> ListViews.files_selected(observable,oldValue,newValue));
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

		public void showSearchResults ( ) {
			final DBRecord record = GUI.getCurrentDBRecord();
			final ArrayList<String> searchResult = record.getURLsSearchResult();
			control.getItems().setAll(searchResult);
			if ( control.getItems().size() > 0 ) {
				control.requestFocus();
				control.getSelectionModel().select(0);
			}
		}
	}

	private static final class FilePathListCell extends ListCell<DBRecord> {
		@Override
		protected void updateItem ( final DBRecord item, final boolean empty ) {
			super.updateItem(item,empty);
			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);
			} else {
				final String text = item.getListLabel();
				setText(text);
				setOnMouseClicked(event -> ListViews.filePathListViewItem_clicked(event));
			}
		}
	}

	private static final class UrlListCell extends ListCell<String> {

		@Override
		protected void updateItem ( final String item, final boolean empty ) {
			super.updateItem(item,empty);
			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);
			} else {
				setText(item);
				setOnMouseClicked(event -> ListViews.urlsListViewItem_clicked(event));
			}
		}

	}

}

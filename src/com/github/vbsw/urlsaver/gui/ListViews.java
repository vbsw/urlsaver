/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.utility.Parser;
import com.github.vbsw.urlsaver.utility.WebBrowserAccess;

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

	public final Files files = new Files();
	public final URLs urls = new URLs();

	protected StdGUI stdGUI;

	public ListViews ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
		files.build(root);
		urls.build(root);
	}

	private void files_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			event.consume();
			if ( !stdGUI.tabPanes.top.urls.control.isDisable() ) {
				stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.urls.control);
				stdGUI.textFields.urlSearch.control.requestFocus();
			}
		}
	}

	private void files_selected ( final ObservableValue<? extends DBRecord> observable, final DBRecord oldValue, final DBRecord newValue ) {
		stdGUI.global.getDataBase().setSelectedRecord(newValue);
		stdGUI.refreshFileSelection();
		stdGUI.refreshURLsView();
		stdGUI.refreshURLsInfo();
	}

	private void urls_selected ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		stdGUI.refreshURLsInfo();
		stdGUI.resetURLsProperties();
	}

	private void urls_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			final String selectedUrl = Parser.trim(urls.control.getSelectionModel().getSelectedItem());
			if ( selectedUrl != null )
				WebBrowserAccess.openURL(selectedUrl);
		} else if ( keyCode == KeyCode.DELETE ) {
			stdGUI.properties.urlDeleteRequestedProperty().set(true);
			stdGUI.buttons.urlCancel.control.requestFocus();
		}
	}

	private ListCell<DBRecord> filesCellFactory ( final ListView<DBRecord> param ) {
		final ListCell<DBRecord> listCell = new FilePathListCell();
		return listCell;
	}

	private ListCell<String> urlsCellFactory ( final ListView<String> param ) {
		final ListCell<String> listCell = new UrlListCell();
		return listCell;
	}

	private void filePathListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			stdGUI.properties.confirmingSaveProperty().set(false);
			stdGUI.refereshFileState();
			stdGUI.refreshFileInfo();
		}
	}

	private void urlsListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			final UrlListCell cell = (UrlListCell) event.getSource();
			final String url = cell.getItem();
			WebBrowserAccess.openURL(url);
		}
	}

	public final class Files {
		public ListView<DBRecord> control;
		public boolean autoSelectRequested;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ListView<DBRecord>) root.lookup("#file_list_view");
			control.setCellFactory( ( ListView<DBRecord> param ) -> filesCellFactory(param));
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends DBRecord> observable, DBRecord oldValue, DBRecord newValue ) -> files_selected(observable,oldValue,newValue));
			control.setOnKeyPressed(event -> files_keyPressed(event));
		}
	}

	public final class URLs {
		public ListView<String> control;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ListView<String>) root.lookup("#url_list_view");
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> urls_selected(observable,oldValue,newValue));
			control.setCellFactory( ( ListView<String> param ) -> urlsCellFactory(param));
			control.setOnKeyPressed(event -> urls_keyPressed(event));
		}

		public void showSearchResults ( ) {
			final DBRecord record = stdGUI.getCurrentDBRecord();
			final ArrayList<String> searchResult = record.getURLsSearchResult();
			control.getItems().setAll(searchResult);
			if ( control.getItems().size() > 0 ) {
				control.requestFocus();
				control.getSelectionModel().select(0);
			}
		}
	}

	private final class FilePathListCell extends ListCell<DBRecord> {
		@Override
		protected void updateItem ( final DBRecord item, final boolean empty ) {
			super.updateItem(item,empty);
			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);
			} else {
				final String text = item.getListLabel();
				setText(text);
				setOnMouseClicked(event -> filePathListViewItem_clicked(event));
			}
		}
	}

	private final class UrlListCell extends ListCell<String> {

		@Override
		protected void updateItem ( final String item, final boolean empty ) {
			super.updateItem(item,empty);
			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);
			} else {
				setText(item);
				setOnMouseClicked(event -> urlsListViewItem_clicked(event));
			}
		}

	}

}

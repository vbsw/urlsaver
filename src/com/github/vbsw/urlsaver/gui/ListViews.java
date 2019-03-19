/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.DBURLsImport;

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
	public final ImportURLs importURLs = new ImportURLs();

	protected GUI stdGUI;

	public ListViews ( final GUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
		files.build(root);
		importURLs.build(root);
	}

	private void files_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			event.consume();
			/* select URLs tab */
			if ( !stdGUI.tabPanes.top.urls.control.isDisable() ) {
				stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.urls.control);
				stdGUI.textFields.urlSearch.control.requestFocus();
			}
		}
	}

	private void import_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			event.consume();
			stdGUI.textFields.importKeys.control.requestFocus();
		}
	}

	private void files_selected ( final ObservableValue<? extends DBURLs> observable, final DBURLs oldValue, final DBURLs newValue ) {
		Global.db.setSelectedURLs(newValue);
		stdGUI.refreshFileSelection();
		stdGUI.refreshURLsView();
		stdGUI.refreshURLsInfo();
	}

	private void import_selected ( final ObservableValue<? extends DBURLsImport> observable, final DBURLsImport oldValue, final DBURLsImport newValue ) {
		Global.db.setSelectedURLsImport(newValue);
		stdGUI.properties.confirmingImportURLsProperty().set(false);
		/* no info to refresh */
	}

	private ListCell<DBURLs> filesCellFactory ( final ListView<DBURLs> param ) {
		final ListCell<DBURLs> listCell = new DBURLsListCell();
		return listCell;
	}

	private void filePathListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			event.consume();
			stdGUI.properties.confirmingSaveProperty().set(false);
			stdGUI.refereshFileState();
			stdGUI.refreshFileInfo();
		}
	}

	private ListCell<DBURLsImport> importCellFactory ( final ListView<DBURLsImport> param ) {
		final ListCell<DBURLsImport> listCell = new DBURLsImportListCell();
		return listCell;
	}

	private void importPathListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			event.consume();
			/* select filter */
			/* TODO select filter */
		}
	}

	public final class Files {
		public ListView<DBURLs> control;
		public boolean autoSelectRequested;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ListView<DBURLs>) root.lookup("#file_list_view");
			control.setCellFactory( ( ListView<DBURLs> param ) -> filesCellFactory(param));
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends DBURLs> observable, DBURLs oldValue, DBURLs newValue ) -> files_selected(observable,oldValue,newValue));
			control.setOnKeyPressed(event -> files_keyPressed(event));
		}
	}

	public final class ImportURLs {
		public ListView<DBURLsImport> control;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ListView<DBURLsImport>) root.lookup("#import_list_view");
			control.setCellFactory( ( ListView<DBURLsImport> param ) -> importCellFactory(param));
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends DBURLsImport> observable, DBURLsImport oldValue, DBURLsImport newValue ) -> import_selected(observable,oldValue,newValue));
			control.setOnKeyPressed(event -> import_keyPressed(event));
		}
	}

	private final class DBURLsListCell extends ListCell<DBURLs> {
		@Override
		protected void updateItem ( final DBURLs item, final boolean empty ) {
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

	private final class DBURLsImportListCell extends ListCell<DBURLsImport> {
		@Override
		protected void updateItem ( final DBURLsImport item, final boolean empty ) {
			super.updateItem(item,empty);
			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);
			} else {
				final String text = item.getListLabel();
				setText(text);
				setOnMouseClicked(event -> importPathListViewItem_clicked(event));
			}
		}
	}

}

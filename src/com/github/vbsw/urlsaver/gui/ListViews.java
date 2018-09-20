/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.db.DBTable;

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

	protected StdGUI stdGUI;

	public ListViews ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
		files.build(root);
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

	private void files_selected ( final ObservableValue<? extends DBTable> observable, final DBTable oldValue, final DBTable newValue ) {
		Global.db.setSelectedTable(newValue);
		stdGUI.refreshFileSelection();
		stdGUI.refreshURLsView();
		stdGUI.refreshURLsInfo();
	}

	private ListCell<DBTable> filesCellFactory ( final ListView<DBTable> param ) {
		final ListCell<DBTable> listCell = new FilePathListCell();
		return listCell;
	}

	private void filePathListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			stdGUI.properties.confirmingSaveProperty().set(false);
			stdGUI.refereshFileState();
			stdGUI.refreshFileInfo();
		}
	}

	public final class Files {
		public ListView<DBTable> control;
		public boolean autoSelectRequested;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ListView<DBTable>) root.lookup("#file_list_view");
			control.setCellFactory( ( ListView<DBTable> param ) -> filesCellFactory(param));
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends DBTable> observable, DBTable oldValue, DBTable newValue ) -> files_selected(observable,oldValue,newValue));
			control.setOnKeyPressed(event -> files_keyPressed(event));
		}
	}

	private final class FilePathListCell extends ListCell<DBTable> {
		@Override
		protected void updateItem ( final DBTable item, final boolean empty ) {
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

}

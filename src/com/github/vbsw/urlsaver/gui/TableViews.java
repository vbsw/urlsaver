/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.util.ArrayList;

import org.junit.jupiter.params.shadow.com.univocity.parsers.common.ColumnMap;

import com.github.vbsw.urlsaver.db.DBTable;
import com.github.vbsw.urlsaver.db.URLsSearchResult;
import com.github.vbsw.urlsaver.utility.Parser;
import com.github.vbsw.urlsaver.utility.WebBrowserAccess;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


/**
 * @author Vitali Baumtrok
 */
public class TableViews {

	protected StdGUI stdGUI;

	public final URLs urls = new URLs();

	public TableViews ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( Parent root ) {
		urls.build(root);
	}

	private void urls_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			final String selectedUrl = Parser.trim(urls.control.getSelectionModel().getSelectedItem().getURL());
			if ( selectedUrl != null )
				WebBrowserAccess.openURL(selectedUrl);
		} else if ( keyCode == KeyCode.DELETE ) {
			stdGUI.properties.urlDeleteRequestedProperty().set(true);
			stdGUI.buttons.urlCancel.control.requestFocus();
		}
	}

	private void urls_selected ( final ObservableValue<? extends URLsSearchResult> observable, final URLsSearchResult oldValue, final URLsSearchResult newValue ) {
		stdGUI.refreshURLsInfo();
		stdGUI.resetURLsProperties();
	}

//	private ListCell<String> urlsCellFactory ( final ListView<String> param ) {
//		final ListCell<String> listCell = new UrlListCell();
//		return listCell;
//	}
//
//	private void urlsListViewItem_clicked ( final MouseEvent event ) {
//		if ( event.getClickCount() == 2 ) {
//			final UrlListCell cell = (UrlListCell) event.getSource();
//			final String url = cell.getItem();
//			WebBrowserAccess.openURL(url);
//		}
//	}

	public final class URLs {

		public TableView<URLsSearchResult> control;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (TableView<URLsSearchResult>) root.lookup("#urls_tv");
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends URLsSearchResult> observable, URLsSearchResult oldValue, URLsSearchResult newValue ) -> urls_selected(observable,oldValue,newValue));

			final TableColumn<URLsSearchResult, String> urlColumn = new TableColumn<URLsSearchResult, String>();
			final TableColumn<URLsSearchResult, String> accessedColumn = new TableColumn<URLsSearchResult, String>();
			final TableColumn<URLsSearchResult, String> scoreColumn = new TableColumn<URLsSearchResult, String>();

//			urlColumn.setCellFactory( ( param ) -> tableCellFactory(param));
//			accessedColumn.setCellFactory( ( param ) -> accessedCellFactory(param));
//			scoreColumn.setCellFactory( ( param ) -> scoreCellFactory(param));

			urlColumn.setCellValueFactory( ( param ) -> urlCellValueFactory(param));
			accessedColumn.setCellValueFactory( ( param ) -> accessedCellValueFactory(param));
			scoreColumn.setCellValueFactory( ( param ) -> scoreCellValueFactory(param));

			control.getColumns().clear();
			control.getColumns().add(urlColumn);
			control.getColumns().add(accessedColumn);
			control.getColumns().add(scoreColumn);

//			control.setCellFactory( ( ListView<String> param ) -> urlsCellFactory(param));
			control.setOnKeyPressed(event -> urls_keyPressed(event));
		}

//		private TableCell<URLsSearchResult, String> tableCellFactory ( final TableColumn<URLsSearchResult, String> column ) {
//			final TableCell<URLsSearchResult, String> tableCell = new URLTableCell();
//			return tableCell;
//		}

		private ObservableValue<String> urlCellValueFactory ( final CellDataFeatures<URLsSearchResult, String> param ) {
			final String str = param.getValue().getURL();
			final ObservableValue<String> cellData = new SimpleStringProperty(str);
			return cellData;
		}

		private ObservableValue<String> accessedCellValueFactory ( final CellDataFeatures<URLsSearchResult, String> param ) {
			final String str = param.getValue().getAccessed();
			final ObservableValue<String> cellData = new SimpleStringProperty(str);
			return cellData;
		}

		private ObservableValue<String> scoreCellValueFactory ( final CellDataFeatures<URLsSearchResult, String> param ) {
			final String str = param.getValue().getScore();
			final ObservableValue<String> cellData = new SimpleStringProperty(str);
			return cellData;
		}

		public void showSearchResults ( ) {
			final DBTable record = stdGUI.getCurrentDBRecord();
			final ArrayList<URLsSearchResult> searchResult = record.getURLsSearchResults();
			control.getItems().setAll(searchResult);
			if ( control.getItems().size() > 0 ) {
				control.requestFocus();
				control.getSelectionModel().select(0);
			}
		}

	}

//	private final class URLTableCell extends TableCell<URLsSearchResult, String> {
//
//		@Override
//		protected void updateItem ( final String item, final boolean empty ) {
//			super.updateItem(item,empty);
//			if ( empty ) {
//				setText(null);
//				setOnMouseClicked(null);
//			} else {
//				setText(item);
//				setOnMouseClicked(event -> urlsListViewItem_clicked(event));
//			}
//		}
//
//	}

}

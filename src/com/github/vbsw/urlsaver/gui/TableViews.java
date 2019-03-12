/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.URLsSearchResult;
import com.github.vbsw.urlsaver.utility.Parser;
import com.github.vbsw.urlsaver.utility.WebBrowserAccess;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


/**
 * @author Vitali Baumtrok
 */
public class TableViews {

	protected GUI stdGUI;

	public final URLs urls = new URLs();

	public TableViews ( final GUI stdGUI ) {
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

	private TableRow<URLsSearchResult> urlsRowFactory ( final TableView<URLsSearchResult> table ) {
		final TableRow<URLsSearchResult> row = new TableRow<URLsSearchResult>();
		row.setOnMouseClicked( ( event ) -> urlsListViewItem_clicked(event));
		return row;
	}

	private void urlsListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			@SuppressWarnings ( "unchecked" )
			final TableRow<URLsSearchResult> row = (TableRow<URLsSearchResult>) event.getSource();
			final URLsSearchResult result = row.getItem();
			final String url = result.getURL();
			WebBrowserAccess.openURL(url);
		}
	}

	public final class URLs {

		public TableView<URLsSearchResult> control;

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (TableView<URLsSearchResult>) root.lookup("#urls_tv");
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends URLsSearchResult> observable, URLsSearchResult oldValue, URLsSearchResult newValue ) -> urls_selected(observable,oldValue,newValue));

			final TableColumn<URLsSearchResult, String> urlColumn = new TableColumn<URLsSearchResult, String>();
			final TableColumn<URLsSearchResult, String> accessedColumn = new TableColumn<URLsSearchResult, String>();
			final TableColumn<URLsSearchResult, String> scoreColumn = new TableColumn<URLsSearchResult, String>();

			urlColumn.setText("url");
			urlColumn.setCellValueFactory( ( param ) -> urlCellValueFactory(param));
			accessedColumn.setText("date");
			accessedColumn.setMinWidth(100);
			accessedColumn.setMaxWidth(100);
			accessedColumn.setPrefWidth(100);
			accessedColumn.setCellValueFactory( ( param ) -> accessedCellValueFactory(param));
			scoreColumn.setText("score");
			scoreColumn.setMinWidth(80);
			scoreColumn.setMaxWidth(80);
			scoreColumn.setPrefWidth(80);
			scoreColumn.setCellValueFactory( ( param ) -> scoreCellValueFactory(param));

			control.getColumns().clear();
			control.getColumns().add(urlColumn);
			control.getColumns().add(accessedColumn);
			control.getColumns().add(scoreColumn);

			control.setRowFactory( ( table ) -> urlsRowFactory(table));
			control.setOnKeyPressed(event -> urls_keyPressed(event));
		}

		private ObservableValue<String> urlCellValueFactory ( final CellDataFeatures<URLsSearchResult, String> param ) {
			final String str = param.getValue().getURL();
			final ObservableValue<String> cellData = new SimpleStringProperty(str);
			return cellData;
		}

		private ObservableValue<String> accessedCellValueFactory ( final CellDataFeatures<URLsSearchResult, String> param ) {
			final String str = param.getValue().getDate();
			final ObservableValue<String> cellData = new SimpleStringProperty(str);
			return cellData;
		}

		private ObservableValue<String> scoreCellValueFactory ( final CellDataFeatures<URLsSearchResult, String> param ) {
			final String str = param.getValue().getScore();
			final ObservableValue<String> cellData = new SimpleStringProperty(str);
			return cellData;
		}

		public void showSearchResults ( ) {
			final DBURLs record = Global.db.getSelectedURLs();
			final ArrayList<URLsSearchResult> searchResult = record.getURLsSearchResults();
			control.getItems().setAll(searchResult);
			if ( control.getItems().size() > 0 ) {
				control.requestFocus();
				control.getSelectionModel().select(0);
			}
		}

	}

}

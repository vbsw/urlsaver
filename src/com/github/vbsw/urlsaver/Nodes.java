/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2016 Vitali Baumtrok
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


package com.github.vbsw.urlsaver;


import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 * @author Vitali Baumtrok
 */
public class Nodes {

	public Parent root;
	public Button quitAppBtn;
	public Button reloadFileBtn;
	public Button reloadAllFilesBtn;
	public Button openInBrowserBtn;
	public Button urlsSearchBtn;
	public Button urlsCancelBtn;
	public TextField fileNameTF;
	public TextField urlsSearchTF;
	public TextField urlTF;
	public TextArea tagsTA;
	public ListView<FileData> fileList;
	public ListView<TaggedWords.Word> urlList;
	public TabPane tabPane;
	public Tab filesTab;
	public Tab urlsTab;
	public Tab aboutTab;
	public Tab settingsTab;
	public boolean externalFXML;

	public void recreate ( ) {
		final FXML fxml = new FXML();

		root = fxml.getRoot();
		externalFXML = fxml.isExternal();

		lookupNodes();
		setListeners();
		fillFileList();
	}

	private void fillFileList ( ) {
		final ObservableList<FileData> list = fileList.getItems();
		for ( FileData fileData: App.fileDataList ) {
			list.add(fileData);
		}
		App.nodes.reloadAllFilesBtn.setDisable(App.fileDataList.isEmpty());
	}

	@SuppressWarnings ( "unchecked" )
	private void lookupNodes ( ) {
		final String quitAppBtnSelector = "#quit_app_btn"; //$NON-NLS-1$
		final String reloadFileBtnSelector = "#reload_file_btn"; //$NON-NLS-1$
		final String reloadAllFilesBtnSelector = "#reload_all_files_btn"; //$NON-NLS-1$
		final String openInBrowserBtnSelector = "#open_in_browser_btn"; //$NON-NLS-1$
		final String urlsSearchBtnSelector = "#urls_search_btn"; //$NON-NLS-1$
		final String urlsCancelBtnSelector = "#urls_cancel_btn"; //$NON-NLS-1$
		final String fileListSelector = "#file_list"; //$NON-NLS-1$
		final String urlListSelector = "#url_list"; //$NON-NLS-1$
		final String fileNameTFSelector = "#file_name_tf"; //$NON-NLS-1$
		final String urlsSearchTFSelector = "#urls_search_tf"; //$NON-NLS-1$
		final String urlTFSelector = "#url_tf"; //$NON-NLS-1$
		final String tagsTASelector = "#tags_ta"; //$NON-NLS-1$
		final String tabPaneSelector = "#tab_pane"; //$NON-NLS-1$
		final String filesTabId = "files_tab"; //$NON-NLS-1$
		final String urlsTabId = "urls_tab"; //$NON-NLS-1$
		final String aboutTabId = "about_tab"; //$NON-NLS-1$
		final String settingsTabId = "settings_tab"; //$NON-NLS-1$

		quitAppBtn = (Button) root.lookup(quitAppBtnSelector);
		reloadFileBtn = (Button) root.lookup(reloadFileBtnSelector);
		reloadAllFilesBtn = (Button) root.lookup(reloadAllFilesBtnSelector);
		openInBrowserBtn = (Button) root.lookup(openInBrowserBtnSelector);
		urlsSearchBtn = (Button) root.lookup(urlsSearchBtnSelector);
		urlsCancelBtn = (Button) root.lookup(urlsCancelBtnSelector);
		fileList = (ListView<FileData>) root.lookup(fileListSelector);
		urlList = (ListView<TaggedWords.Word>) root.lookup(urlListSelector);
		fileNameTF = (TextField) root.lookup(fileNameTFSelector);
		urlsSearchTF = (TextField) root.lookup(urlsSearchTFSelector);
		urlTF = (TextField) root.lookup(urlTFSelector);
		tagsTA = (TextArea) root.lookup(tagsTASelector);
		tabPane = (TabPane) root.lookup(tabPaneSelector);
		filesTab = getTab(tabPane,filesTabId);
		urlsTab = getTab(tabPane,urlsTabId);
		aboutTab = getTab(tabPane,aboutTabId);
		settingsTab = getTab(tabPane,settingsTabId);
	}

	private Tab getTab ( final TabPane tabPane, final String urlsTabId ) {
		for ( Tab tab: tabPane.getTabs() ) {
			final String id = tab.getId();
			if ( (id != null) && (id.equals(urlsTabId)) ) {
				return tab;
			}
		}
		return null;
	}

	private void setListeners ( ) {
		quitAppBtn.setOnAction(new Listener.QuitAppBtn());
		quitAppBtn.setOnKeyPressed(new Listener.KeyPressedQuitAppBtn());
		reloadFileBtn.setOnAction(new Listener.ReloadFileBtn());
		reloadAllFilesBtn.setOnAction(new Listener.ReloadAllFilesBtn());
		openInBrowserBtn.setOnAction(new Listener.OpenInBrowserBtn());
		openInBrowserBtn.disableProperty().bind(urlList.getSelectionModel().selectedItemProperty().isNull());
		urlsSearchBtn.setOnAction(new Listener.SearchUrlsBtn());
		fileList.setCellFactory(new Listener.FileListCellFactory());
		fileList.getSelectionModel().selectedItemProperty().addListener(new Listener.SelectFileListItem());
		fileList.setOnKeyPressed(new Listener.KeyPressedFileList());
		urlList.setCellFactory(new Listener.UrlListCellFactory());
		urlList.getSelectionModel().selectedItemProperty().addListener(new Listener.SelectUrlListItem());
		urlList.setOnKeyPressed(new Listener.KeyPressedUrlList());
		urlList.setOnKeyReleased(new Listener.KeyReleasedUrlList(urlList.getOnKeyPressed()));
		urlsSearchTF.textProperty().addListener(new Listener.TypedUrlsSearchTF());
		urlsSearchTF.setOnAction(new Listener.SearchUrlsTF());
		urlsSearchTF.setOnKeyPressed(new Listener.KeyPressedUrlsSearchTF());
		filesTab.disableProperty().bind(urlsCancelBtn.disableProperty().not());
		aboutTab.disableProperty().bind(urlsCancelBtn.disableProperty().not());
		settingsTab.disableProperty().bind(urlsCancelBtn.disableProperty().not());
	}

	/*
	 * public void fillFileTable ( final FileTable fileTable ) {
	 * final ObservableList<FileTableItem> tableItems =
	 * this.fileTable.getItems();
	 * 
	 * for ( FileData fileData: fileTable.data ) {
	 * final FileTableItem fileTableItem = fileData.tableItem;
	 * tableItems.add(fileTableItem);
	 * }
	 * }
	 */

	/*
	 * private void createFileTableColumns ( final Scene scene ) {
	 * final String nameColumnTitle = "File"; //$NON-NLS-1$
	 * final String nameColumnId = "name_column"; //$NON-NLS-1$
	 * final String nameColumnPropField = "name"; //$NON-NLS-1$
	 * final String editedColumnTitle = "Edited"; //$NON-NLS-1$
	 * final String editedColumnId = "edited_column"; //$NON-NLS-1$
	 * final String editedColumnPropField = "edited"; //$NON-NLS-1$
	 * final String loadedColumnTitle = "Loaded %"; //$NON-NLS-1$
	 * final String loadedColumnId = "loaded_column"; //$NON-NLS-1$
	 * final String loadedColumnPropField = "loaded"; //$NON-NLS-1$
	 * final ObservableList<FileTableItem> tableData = fileTable.getItems();
	 * final ObservableList<TableColumn<FileTableItem, ?>> columns =
	 * fileTable.getColumns();
	 * final TableColumn<FileTableItem, String> nameColumn = new
	 * TableColumn<FileTableItem, String>(nameColumnTitle);
	 * final TableColumn<FileTableItem, String> editedColumn = new
	 * TableColumn<FileTableItem, String>(editedColumnTitle);
	 * final TableColumn<FileTableItem, String> loadedColumn = new
	 * TableColumn<FileTableItem, String>(loadedColumnTitle);
	 * // final double nameColumnWidth = scene.getWidth()*70/100;
	 * 
	 * tableData.clear();
	 * columns.clear();
	 * 
	 * editedColumn.setId(editedColumnId);
	 * // editedColumn.setMinWidth(70);
	 * // editedColumn.setMaxWidth(70);
	 * editedColumn.setCellValueFactory(new PropertyValueFactory<FileTableItem,
	 * String>(editedColumnPropField));
	 * 
	 * nameColumn.setId(nameColumnId);
	 * // nameColumn.setMinWidth(nameColumnWidth);
	 * nameColumn.setCellValueFactory(new PropertyValueFactory<FileTableItem,
	 * String>(nameColumnPropField));
	 * 
	 * loadedColumn.setId(loadedColumnId);
	 * // loadedColumn.setMinWidth(80);
	 * // loadedColumn.setMaxWidth(80);
	 * loadedColumn.setCellValueFactory(new PropertyValueFactory<FileTableItem,
	 * String>(loadedColumnPropField));
	 * 
	 * columns.add(editedColumn);
	 * columns.add(nameColumn);
	 * columns.add(loadedColumn);
	 * 
	 * fileTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	 * fileTable.setRowFactory(new Listener.FileTableRowFactory());
	 * }
	 */

}

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
	public Button quitAppSaveBtn;
	public Button quitAppOKBtn;
	public Button reloadFileBtn;
	public Button reloadAllFilesBtn;
	public Button fileSaveBtn;
	public Button openInBrowserBtn;
	public Button urlSearchBtn;
	public Button urlCancelBtn;
	public Button urlDeleteBtn;
	public Button urlDeleteOKBtn;
	public TextField fileNameTF;
	public TextField urlSearchTF;
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
		final String quitAppSaveBtnSelector = "#quit_app_save_btn"; //$NON-NLS-1$
		final String quitAppOKBtnSelector = "#quit_app_ok_btn"; //$NON-NLS-1$
		final String reloadFileBtnSelector = "#reload_file_btn"; //$NON-NLS-1$
		final String reloadAllFilesBtnSelector = "#reload_all_files_btn"; //$NON-NLS-1$
		final String fileSaveBtnSelector = "#file_save_btn"; //$NON-NLS-1$
		final String openInBrowserBtnSelector = "#open_in_browser_btn"; //$NON-NLS-1$
		final String urlsSearchBtnSelector = "#url_search_btn"; //$NON-NLS-1$
		final String urlsCancelBtnSelector = "#url_cancel_btn"; //$NON-NLS-1$
		final String urlsDeleteBtnSelector = "#url_delete_btn"; //$NON-NLS-1$
		final String urlsDeleteOKBtnSelector = "#url_delete_ok_btn"; //$NON-NLS-1$
		final String fileListSelector = "#file_list"; //$NON-NLS-1$
		final String urlListSelector = "#url_list"; //$NON-NLS-1$
		final String fileNameTFSelector = "#file_name_tf"; //$NON-NLS-1$
		final String urlSearchTFSelector = "#url_search_tf"; //$NON-NLS-1$
		final String urlTFSelector = "#url_tf"; //$NON-NLS-1$
		final String tagsTASelector = "#tags_ta"; //$NON-NLS-1$
		final String tabPaneSelector = "#tab_pane"; //$NON-NLS-1$
		final String filesTabId = "files_tab"; //$NON-NLS-1$
		final String urlsTabId = "urls_tab"; //$NON-NLS-1$
		final String aboutTabId = "about_tab"; //$NON-NLS-1$
		final String settingsTabId = "settings_tab"; //$NON-NLS-1$

		quitAppBtn = (Button) root.lookup(quitAppBtnSelector);
		quitAppSaveBtn = (Button) root.lookup(quitAppSaveBtnSelector);
		quitAppOKBtn = (Button) root.lookup(quitAppOKBtnSelector);
		reloadFileBtn = (Button) root.lookup(reloadFileBtnSelector);
		reloadAllFilesBtn = (Button) root.lookup(reloadAllFilesBtnSelector);
		fileSaveBtn = (Button) root.lookup(fileSaveBtnSelector);
		openInBrowserBtn = (Button) root.lookup(openInBrowserBtnSelector);
		urlSearchBtn = (Button) root.lookup(urlsSearchBtnSelector);
		urlCancelBtn = (Button) root.lookup(urlsCancelBtnSelector);
		urlDeleteBtn = (Button) root.lookup(urlsDeleteBtnSelector);
		urlDeleteOKBtn = (Button) root.lookup(urlsDeleteOKBtnSelector);
		fileList = (ListView<FileData>) root.lookup(fileListSelector);
		urlList = (ListView<TaggedWords.Word>) root.lookup(urlListSelector);
		fileNameTF = (TextField) root.lookup(fileNameTFSelector);
		urlSearchTF = (TextField) root.lookup(urlSearchTFSelector);
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
		quitAppSaveBtn.setOnKeyPressed(new Listener.KeyPressedQuitAppSaveBtn());
		quitAppOKBtn.setOnKeyPressed(new Listener.KeyPressedQuitAppOKBtn());
		reloadFileBtn.setOnAction(new Listener.ReloadFileBtn());
		reloadAllFilesBtn.setOnAction(new Listener.ReloadAllFilesBtn());
		openInBrowserBtn.setOnAction(new Listener.OpenInBrowserBtn());
		urlSearchBtn.setOnAction(new Listener.SearchUrlsBtn());
		urlDeleteBtn.setOnAction(new Listener.DeleteUrlBtn());
		urlDeleteOKBtn.setOnAction(new Listener.DeleteUrlOKBtn());
		urlDeleteOKBtn.setOnKeyPressed(new Listener.KeyPressedUrlDeleteOKBtn());
		urlCancelBtn.setOnAction(new Listener.UrlCancelBtn());
		urlCancelBtn.setOnKeyPressed(new Listener.KeyPressedUrlCancelBtn());
		fileList.setCellFactory(new Listener.FileListCellFactory());
		fileList.getSelectionModel().selectedItemProperty().addListener(new Listener.SelectFileListItem());
		fileList.setOnKeyPressed(new Listener.KeyPressedFileList());
		urlList.setCellFactory(new Listener.UrlListCellFactory());
		urlList.getSelectionModel().selectedItemProperty().addListener(new Listener.SelectUrlListItem());
		urlList.setOnKeyPressed(new Listener.KeyPressedUrlList());
		urlSearchTF.textProperty().addListener(new Listener.TypedUrlsSearchTF());
		urlSearchTF.setOnAction(new Listener.SearchUrlsTF());
		urlSearchTF.setOnKeyPressed(new Listener.KeyPressedUrlsSearchTF());
		aboutTab.selectedProperty().addListener(new Listener.SelectAboutTab());
	}

}

/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
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


package com.github.vbsw.urlsaver.scene;


import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.scene.handlers.FileReloadActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileReloadAllActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileReloadAllKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileReloadKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileSaveActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileSaveCancelActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileSaveCancelKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileSaveKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileSaveOKActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.FileSaveOKKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.OpenInBrowserActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.OpenInBrowserKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.QuitAppActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.QuitAppKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.QuitAppOKActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.QuitAppOKKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.QuitAppSaveActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.QuitAppSaveKeyPressedHandler;
import com.github.vbsw.urlsaver.scene.handlers.UrlCancelActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.UrlCreateOKActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.UrlDeleteActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.UrlDeleteOKActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.UrlEditOKActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.UrlsSearchActionHandler;
import com.github.vbsw.urlsaver.scene.handlers.UrlsSearchKeyPressedHandler;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Button;


/**
 * @author Vitali Baumtrok
 */
public final class Buttons {

	public final Button quitApp;
	public final Button quitAppSave;
	public final Button quitAppOK;
	public final Button reloadFile;
	public final Button reloadAllFiles;
	public final Button fileSave;
	public final Button fileCancel;
	public final Button fileSaveOK;
	public final Button openInBrowser;
	public final Button urlSearch;
	public final Button urlCancel;
	public final Button urlDelete;
	public final Button urlDeleteOK;
	public final Button urlCreateOK;
	public final Button urlEditOK;

	Buttons ( final Parent root ) {
		final String quitAppBtnSelector = "#quit_app_btn"; //$NON-NLS-1$
		final String quitAppSaveBtnSelector = "#quit_app_save_btn"; //$NON-NLS-1$
		final String quitAppOKBtnSelector = "#quit_app_ok_btn"; //$NON-NLS-1$
		final String reloadFileBtnSelector = "#reload_file_btn"; //$NON-NLS-1$
		final String reloadAllFilesBtnSelector = "#reload_all_files_btn"; //$NON-NLS-1$
		final String fileSaveBtnSelector = "#file_save_btn"; //$NON-NLS-1$
		final String fileCancelBtnSelector = "#file_cancel_btn"; //$NON-NLS-1$
		final String fileSaveOKBtnSelector = "#file_save_ok_btn"; //$NON-NLS-1$
		final String openInBrowserBtnSelector = "#open_in_browser_btn"; //$NON-NLS-1$
		final String urlsSearchBtnSelector = "#url_search_btn"; //$NON-NLS-1$
		final String urlsCancelBtnSelector = "#url_cancel_btn"; //$NON-NLS-1$
		final String urlsDeleteBtnSelector = "#url_delete_btn"; //$NON-NLS-1$
		final String urlsDeleteOKBtnSelector = "#url_delete_ok_btn"; //$NON-NLS-1$
		final String urlsCreateOKBtnSelector = "#url_create_ok_btn"; //$NON-NLS-1$
		final String urlsEditOKBtnSelector = "#url_edit_ok_btn"; //$NON-NLS-1$

		quitApp = (Button) root.lookup(quitAppBtnSelector);
		quitAppSave = (Button) root.lookup(quitAppSaveBtnSelector);
		quitAppOK = (Button) root.lookup(quitAppOKBtnSelector);
		reloadFile = (Button) root.lookup(reloadFileBtnSelector);
		reloadAllFiles = (Button) root.lookup(reloadAllFilesBtnSelector);
		fileSave = (Button) root.lookup(fileSaveBtnSelector);
		fileCancel = (Button) root.lookup(fileCancelBtnSelector);
		fileSaveOK = (Button) root.lookup(fileSaveOKBtnSelector);
		openInBrowser = (Button) root.lookup(openInBrowserBtnSelector);
		urlSearch = (Button) root.lookup(urlsSearchBtnSelector);
		urlCancel = (Button) root.lookup(urlsCancelBtnSelector);
		urlDelete = (Button) root.lookup(urlsDeleteBtnSelector);
		urlDeleteOK = (Button) root.lookup(urlsDeleteOKBtnSelector);
		urlCreateOK = (Button) root.lookup(urlsCreateOKBtnSelector);
		urlEditOK = (Button) root.lookup(urlsEditOKBtnSelector);
	}

	void configure ( ) {
		quitApp.setOnAction(new QuitAppActionHandler());
		quitApp.setOnKeyPressed(new QuitAppKeyPressedHandler());
		quitAppSave.setOnAction(new QuitAppSaveActionHandler());
		quitAppSave.setOnKeyPressed(new QuitAppSaveKeyPressedHandler());
		quitAppOK.setOnAction(new QuitAppOKActionHandler());
		quitAppOK.setOnKeyPressed(new QuitAppOKKeyPressedHandler());
		reloadFile.disableProperty().bind(Bindings.or(Bindings.not(App.files.selectedProperty()),App.files.confirmingSaveProperty()));
		reloadFile.setOnAction(new FileReloadActionHandler());
		reloadFile.setOnKeyPressed(new FileReloadKeyPressedHandler());
		reloadAllFiles.disableProperty().bind(Bindings.or(App.files.selectedProperty(),App.files.confirmingSaveProperty()));
		reloadAllFiles.setOnAction(new FileReloadAllActionHandler());
		reloadAllFiles.setOnKeyPressed(new FileReloadAllKeyPressedHandler());
		fileSave.disableProperty().bind(Bindings.or(Bindings.not(App.files.selectedFileDirtyProperty()),App.files.confirmingSaveProperty()));
		fileSave.setOnAction(new FileSaveActionHandler());
		fileSave.setOnKeyPressed(new FileSaveKeyPressedHandler());
		fileCancel.disableProperty().bind(Bindings.not(App.files.confirmingSaveProperty()));
		fileCancel.setOnAction(new FileSaveCancelActionHandler());
		fileCancel.setOnKeyPressed(new FileSaveCancelKeyPressedHandler());
		fileSaveOK.disableProperty().bind(Bindings.not(App.files.confirmingSaveProperty()));
		fileSaveOK.setOnAction(new FileSaveOKActionHandler());
		fileSaveOK.setOnKeyPressed(new FileSaveOKKeyPressedHandler());
		openInBrowser.disableProperty().bind(Bindings.not(App.urls.selectedProperty()));
		openInBrowser.setOnAction(new OpenInBrowserActionHandler());
		openInBrowser.setOnKeyPressed(new OpenInBrowserKeyPressedHandler());
		urlSearch.setOnAction(new UrlsSearchActionHandler());
		urlSearch.setOnKeyPressed(new UrlsSearchKeyPressedHandler());
		urlCancel.disableProperty().bind(createUrlCancelDisableBinding());
		urlCancel.setOnAction(new UrlCancelActionHandler());
		urlDelete.disableProperty().bind(createUrlDeleteDisableBinding());
		urlDelete.setOnAction(new UrlDeleteActionHandler());
		urlDeleteOK.disableProperty().bind(Bindings.not(App.urls.deleteRequestedProperty()));
		urlDeleteOK.setOnAction(new UrlDeleteOKActionHandler());
		urlCreateOK.disableProperty().bind(Bindings.not(App.urls.urlModifiedProperty()));
		urlCreateOK.setOnAction(new UrlCreateOKActionHandler());
		urlEditOK.disableProperty().bind(createEditOKDisableBinding());
		urlEditOK.setOnAction(new UrlEditOKActionHandler());
	}

	private ObservableValue<? extends Boolean> createUrlCancelDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.or(App.urls.urlModifiedProperty(),App.urls.tagsModifiedProperty());
		binding = Bindings.or(binding,App.urls.deleteRequestedProperty());
		binding = Bindings.not(binding);

		return binding;
	}

	private ObservableValue<? extends Boolean> createUrlDeleteDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.not(App.urls.selectedProperty());
		binding = Bindings.or(binding,App.urls.deleteRequestedProperty());
		binding = Bindings.or(binding,App.urls.urlModifiedProperty());
		binding = Bindings.or(binding,App.urls.tagsModifiedProperty());

		return binding;
	}

	private ObservableValue<? extends Boolean> createEditOKDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.not(App.urls.urlModifiedProperty());
		binding = Bindings.and(binding,App.urls.tagsModifiedProperty());
		binding = Bindings.not(binding);

		return binding;
	}

}

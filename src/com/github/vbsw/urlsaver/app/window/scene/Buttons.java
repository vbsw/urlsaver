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


package com.github.vbsw.urlsaver.app.window.scene;


import com.github.vbsw.urlsaver.app.App;

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
		quitApp.setOnAction(event -> App.about.mv.button_quitApp_clicked(event));
		quitApp.setOnKeyPressed(event -> App.about.mv.button_quitApp_keyPressed(event));
		quitAppSave.setOnAction(event -> App.about.mv.button_quitAppSave_clicked(event));
		quitAppSave.setOnKeyPressed(event -> App.about.mv.button_quitAppSave_keyPressed(event));
		quitAppOK.setOnAction(event -> App.about.mv.button_quitAppOK_clicked(event));
		quitAppOK.setOnKeyPressed(event -> App.about.mv.button_quitAppOK_keyPressed(event));
		reloadFile.disableProperty().bind(Bindings.or(Bindings.not(App.files.mv.selectedProperty()),App.files.mv.confirmingSaveProperty()));
		reloadFile.setOnAction(event -> App.files.mv.button_reloadFile_clicked(event));
		reloadFile.setOnKeyPressed(event -> App.files.mv.button_reloadFile_keyPressed(event));
		reloadAllFiles.disableProperty().bind(Bindings.or(Bindings.not(App.files.mv.selectedProperty()),App.files.mv.confirmingSaveProperty()));
		reloadAllFiles.setOnAction(event -> App.files.mv.button_reloadAllFiles_clicked(event));
		reloadAllFiles.setOnKeyPressed(event -> App.files.mv.button_reloadAllFiles_keyPressed(event));
		fileSave.disableProperty().bind(Bindings.or(Bindings.not(App.files.mv.selectedFileDirtyProperty()),App.files.mv.confirmingSaveProperty()));
		fileSave.setOnAction(event -> App.files.mv.button_fileSave_clicked(event));
		fileSave.setOnKeyPressed(event -> App.files.mv.button_fileSave_keyPressed(event));
		fileCancel.disableProperty().bind(Bindings.not(App.files.mv.confirmingSaveProperty()));
		fileCancel.setOnAction(event -> App.files.mv.button_fileCancel_clicked(event));
		fileCancel.setOnKeyPressed(event -> App.files.mv.button_fileCancel_keyPressed(event));
		fileSaveOK.disableProperty().bind(Bindings.not(App.files.mv.confirmingSaveProperty()));
		fileSaveOK.setOnAction(event -> App.files.mv.button_fileSaveOK_clicked(event));
		fileSaveOK.setOnKeyPressed(event -> App.files.mv.button_fileSaveOK_keyPressed(event));
		openInBrowser.disableProperty().bind(Bindings.not(App.urls.mv.selectedProperty()));
		openInBrowser.setOnAction(event -> App.urls.mv.button_openInBrowser_clicked(event));
		openInBrowser.setOnKeyPressed(event -> App.urls.mv.button_openInBrowser_keyPressed(event));
		urlSearch.setOnAction(event -> App.urls.mv.button_urlSearch_clicked(event));
		urlSearch.setOnKeyPressed(event -> App.urls.mv.button_urlSearch_keyPressed(event));
		urlCancel.disableProperty().bind(createUrlCancelDisableBinding());
		urlCancel.setOnAction(event -> App.urls.mv.button_urlCancel_clicked(event));
		urlDelete.disableProperty().bind(createUrlDeleteDisableBinding());
		urlDelete.setOnAction(event -> App.urls.mv.button_urlDelete_clicked(event));
		urlDeleteOK.disableProperty().bind(Bindings.not(App.urls.mv.deleteRequestedProperty()));
		urlDeleteOK.setOnAction(event -> App.urls.mv.button_urlDeleteOK_clicked(event));
		urlCreateOK.disableProperty().bind(Bindings.not(App.urls.mv.urlModifiedProperty()));
		urlCreateOK.setOnAction(event -> App.urls.mv.button_urlCreateOK_clicked(event));
		urlEditOK.disableProperty().bind(createEditOKDisableBinding());
		urlEditOK.setOnAction(event -> App.urls.mv.button_urlEditOK_clicked(event));
	}

	private ObservableValue<? extends Boolean> createUrlCancelDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.or(App.urls.mv.urlModifiedProperty(),App.urls.mv.tagsModifiedProperty());
		binding = Bindings.or(binding,App.urls.mv.deleteRequestedProperty());
		binding = Bindings.not(binding);

		return binding;
	}

	private ObservableValue<? extends Boolean> createUrlDeleteDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.not(App.urls.mv.selectedProperty());
		binding = Bindings.or(binding,App.urls.mv.deleteRequestedProperty());
		binding = Bindings.or(binding,App.urls.mv.urlModifiedProperty());
		binding = Bindings.or(binding,App.urls.mv.tagsModifiedProperty());

		return binding;
	}

	private ObservableValue<? extends Boolean> createEditOKDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.not(App.urls.mv.urlModifiedProperty());
		binding = Bindings.and(binding,App.urls.mv.tagsModifiedProperty());
		binding = Bindings.not(binding);

		return binding;
	}

}

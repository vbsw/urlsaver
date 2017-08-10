
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.scene;


import com.github.vbsw.urlsaver.app.App;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;


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
	public final MenuButton reloadSettingsFile;
	public final MenuButton createSettingsFile;
	public final Button createSettingsFileOK;
	public final Button settingsCancel;
	public final Button saveSettings;

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
		final String reloadSettingsFileBtnSelector = "#reload_settings_file_btn"; //$NON-NLS-1$
		final String createSettingsFileBtnSelector = "#create_settings_file_btn"; //$NON-NLS-1$
		final String createSettingsFileOKBtnSelector = "#create_settings_file_ok_btn"; //$NON-NLS-1$
		final String settingsCancelBtnSelector = "#settings_cancel_btn"; //$NON-NLS-1$
		final String saveSettingsBtnSelector = "#save_settings_btn"; //$NON-NLS-1$

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
		reloadSettingsFile = (MenuButton) root.lookup(reloadSettingsFileBtnSelector);
		createSettingsFile = (MenuButton) root.lookup(createSettingsFileBtnSelector);
		createSettingsFileOK = (Button) root.lookup(createSettingsFileOKBtnSelector);
		settingsCancel = (Button) root.lookup(settingsCancelBtnSelector);
		saveSettings = (Button) root.lookup(saveSettingsBtnSelector);
	}

	void configure ( ) {
		quitApp.disableProperty().bind(App.about.mv.confirmQuitAppProperty());
		quitApp.setOnAction(event -> App.about.mv.button_quitApp_clicked(event));
		quitApp.setOnKeyPressed(event -> App.about.mv.button_quitApp_keyPressed(event));
		quitAppSave.disableProperty().bind(Bindings.not(App.about.mv.confirmQuitAppProperty()));
		quitAppSave.setOnAction(event -> App.about.mv.button_quitAppSave_clicked(event));
		quitAppSave.setOnKeyPressed(event -> App.about.mv.button_quitAppSave_keyPressed(event));
		quitAppOK.disableProperty().bind(Bindings.not(App.about.mv.confirmQuitAppProperty()));
		quitAppOK.setOnAction(event -> App.about.mv.button_quitAppOK_clicked(event));
		quitAppOK.setOnKeyPressed(event -> App.about.mv.button_quitAppOK_keyPressed(event));
		reloadFile.disableProperty().bind(Bindings.or(Bindings.not(App.files.vm.selectedProperty()),App.files.vm.confirmingSaveProperty()));
		reloadFile.setOnAction(event -> App.files.vm.button_reloadFile_clicked(event));
		reloadFile.setOnKeyPressed(event -> App.files.vm.button_reloadFile_keyPressed(event));
		reloadAllFiles.disableProperty().bind(Bindings.or(Bindings.not(App.files.vm.selectedProperty()),App.files.vm.confirmingSaveProperty()));
		reloadAllFiles.setOnAction(event -> App.files.vm.button_reloadAllFiles_clicked(event));
		reloadAllFiles.setOnKeyPressed(event -> App.files.vm.button_reloadAllFiles_keyPressed(event));
		fileSave.disableProperty().bind(Bindings.or(Bindings.not(App.files.vm.selectedFileDirtyProperty()),App.files.vm.confirmingSaveProperty()));
		fileSave.setOnAction(event -> App.files.vm.button_fileSave_clicked(event));
		fileSave.setOnKeyPressed(event -> App.files.vm.button_fileSave_keyPressed(event));
		fileCancel.disableProperty().bind(Bindings.not(App.files.vm.confirmingSaveProperty()));
		fileCancel.setOnAction(event -> App.files.vm.button_fileCancel_clicked(event));
		fileCancel.setOnKeyPressed(event -> App.files.vm.button_fileCancel_keyPressed(event));
		fileSaveOK.disableProperty().bind(Bindings.not(App.files.vm.confirmingSaveProperty()));
		fileSaveOK.setOnAction(event -> App.files.vm.button_fileSaveOK_clicked(event));
		fileSaveOK.setOnKeyPressed(event -> App.files.vm.button_fileSaveOK_keyPressed(event));
		openInBrowser.disableProperty().bind(Bindings.not(App.urls.vm.existsProperty()));
		openInBrowser.setOnAction(event -> App.urls.vm.button_openInBrowser_clicked(event));
		openInBrowser.setOnKeyPressed(event -> App.urls.vm.button_openInBrowser_keyPressed(event));
		urlSearch.setOnAction(event -> App.urls.vm.button_urlSearch_clicked(event));
		urlSearch.setOnKeyPressed(event -> App.urls.vm.button_urlSearch_keyPressed(event));
		urlCancel.disableProperty().bind(getUrlCancelDisableBinding());
		urlCancel.setOnAction(event -> App.urls.vm.button_urlCancel_clicked(event));
		urlCancel.setOnKeyPressed(event -> App.urls.vm.button_urlCancel_keyPressed(event));
		urlDelete.disableProperty().bind(getUrlDeleteDisableBinding());
		urlDelete.setOnAction(event -> App.urls.vm.button_urlDelete_clicked(event));
		urlDelete.setOnKeyPressed(event -> App.urls.vm.button_urlDelete_keyPressed(event));
		urlDeleteOK.disableProperty().bind(Bindings.not(App.urls.vm.deleteRequestedProperty()));
		urlDeleteOK.setOnAction(event -> App.urls.vm.button_urlDeleteOK_clicked(event));
		urlDeleteOK.setOnKeyPressed(event -> App.urls.vm.button_urlDeleteOK_keyPressed(event));
		urlCreateOK.disableProperty().bind(Bindings.not(App.urls.vm.urlModifiedProperty()));
		urlCreateOK.setOnAction(event -> App.urls.vm.button_urlCreateOK_clicked(event));
		urlCreateOK.setOnKeyPressed(event -> App.urls.vm.button_urlCreateOK_keyPressed(event));
		urlEditOK.disableProperty().bind(getEditOKDisableBinding());
		urlEditOK.setOnAction(event -> App.urls.vm.button_urlEditOK_clicked(event));
		urlEditOK.setOnKeyPressed(event -> App.urls.vm.button_urlEditOK_keyPressed(event));
		createSettingsFile.disableProperty().bind(getCreateSettingsFileDisableBinding());
		setCallbacks_reloadSettingsFile();
		setCallbacks_createSettingsFile();
		createSettingsFileOK.disableProperty().bind(getCreateSettingsFileDisableBinding().not());
		createSettingsFileOK.setOnAction(event -> App.settings.vm.button_createSettingsFileOK_clicked(event));
		settingsCancel.disableProperty().bind(getCreateSettingsCancelBinding());
		settingsCancel.setOnAction(event -> App.settings.vm.button_settingsCancel_clicked(event));
		saveSettings.disableProperty().bind(getSettingsChangedBinding().not());
		saveSettings.setOnAction(event -> App.settings.vm.button_saveSettings_clicked(event));
	}

	private void setCallbacks_reloadSettingsFile ( ) {
		final ObservableList<MenuItem> items = reloadSettingsFile.getItems();
		final String settingsBtnSelector = "reload_settings_file_btn"; //$NON-NLS-1$
		final String cssBtnSelector = "reload_css_file_btn"; //$NON-NLS-1$
		final String fxmlBtnSelector = "reload_fxml_file_btn"; //$NON-NLS-1$

		for ( final MenuItem item: items ) {
			final String id = item.getId();

			if ( id != null ) {
				if ( id.equals(settingsBtnSelector) ) {
					item.setOnAction(event -> App.settings.vm.button_reloadSettingsFile_clicked(event));

				} else if ( id.equals(cssBtnSelector) ) {
					item.setOnAction(event -> App.settings.vm.button_reloadCSSFile_clicked(event));

				} else if ( id.equals(fxmlBtnSelector) ) {
					item.setOnAction(event -> App.settings.vm.button_reloadFXMLFile_clicked(event));
				}
			}
		}
	}

	private void setCallbacks_createSettingsFile ( ) {
		final ObservableList<MenuItem> items = createSettingsFile.getItems();
		final String settingsBtnSelector = "create_settings_file_btn"; //$NON-NLS-1$
		final String cssBtnSelector = "create_css_file_btn"; //$NON-NLS-1$
		final String fxmlBtnSelector = "create_fxml_file_btn"; //$NON-NLS-1$

		for ( final MenuItem item: items ) {
			final String id = item.getId();

			if ( id != null ) {
				if ( id.equals(settingsBtnSelector) ) {
					item.setOnAction(event -> App.settings.vm.button_createSettingsFile_clicked(event));

				} else if ( id.equals(cssBtnSelector) ) {
					item.setOnAction(event -> App.settings.vm.button_createCSSFile_clicked(event));

				} else if ( id.equals(fxmlBtnSelector) ) {
					item.setOnAction(event -> App.settings.vm.button_createFXMLFile_clicked(event));
				}
			}
		}
	}

	private ObservableValue<? extends Boolean> getUrlCancelDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.or(App.urls.vm.urlModifiedProperty(),App.urls.vm.tagsModifiedProperty());
		binding = Bindings.or(binding,App.urls.vm.deleteRequestedProperty());
		binding = Bindings.not(binding);

		return binding;
	}

	private ObservableValue<? extends Boolean> getUrlDeleteDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.not(App.urls.vm.existsProperty());
		binding = Bindings.or(binding,App.urls.vm.deleteRequestedProperty());
		binding = Bindings.or(binding,App.urls.vm.urlModifiedProperty());
		binding = Bindings.or(binding,App.urls.vm.tagsModifiedProperty());

		return binding;
	}

	private ObservableValue<? extends Boolean> getEditOKDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.not(App.urls.vm.urlModifiedProperty());
		binding = Bindings.and(binding,App.urls.vm.tagsModifiedProperty());
		binding = Bindings.not(binding);

		return binding;
	}

	private BooleanBinding getCreateSettingsFileDisableBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.or(App.settings.vm.confirmingCreateSettingsProperty(),App.settings.vm.confirmingCreateCSSProperty());
		binding = Bindings.or(binding,App.settings.vm.confirmingCreateFXMLProperty());

		return binding;
	}

	private ObservableValue<? extends Boolean> getCreateSettingsCancelBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.or(getCreateSettingsFileDisableBinding(),App.settings.vm.settingsModifiedProperty());
		binding = Bindings.not(binding);

		return binding;
	}

	private BooleanBinding getSettingsChangedBinding ( ) {
		BooleanBinding binding;

		binding = Bindings.or(App.settings.vm.titleChangedProperty(),App.settings.vm.widthChangedProperty());
		binding = Bindings.or(binding,App.settings.vm.heightChangedProperty());
		binding = Bindings.or(binding,App.settings.vm.fileExtensionChangedProperty());
		binding = Bindings.or(binding,App.settings.vm.defaultFileChangedProperty());
		binding = Bindings.or(binding,App.settings.vm.maximizeChangedProperty());
		binding = Bindings.or(binding,App.settings.vm.loadAtStartChangedProperty());
		binding = Bindings.or(binding,App.settings.vm.byPrefixChangedProperty());

		return binding;
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.urlsaver.gui;

import java.nio.file.Path;

import com.github.vbsw.urlsaver.App;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * @author Vitali Baumtrok
 */
public class GUI {

	public static final Buttons btn = new Buttons();
	public static final CheckBoxes cb = new CheckBoxes();
	public static final ListViews lv = new ListViews();
	public static final TabPanes tp = new TabPanes();
	public static final TextAreas ta = new TextAreas();
	public static final TextFields tf = new TextFields();
	public static final TopTabs tt = new TopTabs();

	@SuppressWarnings ( "unchecked" )
	public static void extractControlsFromFXML ( Parent fxmlRoot ) {
		btn.quitApp = (Button) fxmlRoot.lookup("#quit_app_btn");
		btn.quitAppSave = (Button) fxmlRoot.lookup("#quit_app_save_btn");
		btn.quitAppOK = (Button) fxmlRoot.lookup("#quit_app_ok_btn");
		btn.reloadFile = (Button) fxmlRoot.lookup("#reload_file_btn");
		btn.reloadAllFiles = (Button) fxmlRoot.lookup("#reload_all_files_btn");
		btn.fileSave = (Button) fxmlRoot.lookup("#file_save_btn");
		btn.fileCancel = (Button) fxmlRoot.lookup("#file_cancel_btn");
		btn.fileSaveOK = (Button) fxmlRoot.lookup("#file_save_ok_btn");
		btn.openInBrowser = (Button) fxmlRoot.lookup("#open_in_browser_btn");
		btn.urlSearch = (Button) fxmlRoot.lookup("#url_search_btn");
		btn.urlCancel = (Button) fxmlRoot.lookup("#url_cancel_btn");
		btn.urlDelete = (Button) fxmlRoot.lookup("#url_delete_btn");
		btn.urlDeleteOK = (Button) fxmlRoot.lookup("#url_delete_ok_btn");
		btn.urlCreateOK = (Button) fxmlRoot.lookup("#url_create_ok_btn");
		btn.urlEditOK = (Button) fxmlRoot.lookup("#url_edit_ok_btn");
		btn.reloadSettingsFile = (MenuButton) fxmlRoot.lookup("#reload_settings_file_btn");
		btn.createSettingsFile = (MenuButton) fxmlRoot.lookup("#create_settings_file_btn");
		btn.createSettingsFileOK = (Button) fxmlRoot.lookup("#create_settings_file_ok_btn");
		btn.settingsCancel = (Button) fxmlRoot.lookup("#settings_cancel_btn");
		btn.saveSettings = (Button) fxmlRoot.lookup("#save_settings_btn");

		cb.maximize = (CheckBox) fxmlRoot.lookup("#settings_maximize_cb");
		cb.loadAtStart = (CheckBox) fxmlRoot.lookup("#settings_load_at_start_cb");
		cb.byPrefix = (CheckBox) fxmlRoot.lookup("#settings_by_prefix_cb");

		lv.files = (ListView<Path>) fxmlRoot.lookup("#file_list_view");
		lv.urls = (ListView<String>) fxmlRoot.lookup("#url_list_view");

		tp.top = (TabPane) fxmlRoot.lookup("#top_tab_pane");

		ta.tags = (TextArea) fxmlRoot.lookup("#tags_ta");
		ta.log = (TextArea) fxmlRoot.lookup("#log_ta");

		tf.fileName = (TextField) fxmlRoot.lookup("#file_name_tf");
		tf.urlSearch = (TextField) fxmlRoot.lookup("#url_search_tf");
		tf.url = (TextField) fxmlRoot.lookup("#url_tf");
		tf.title = (TextField) fxmlRoot.lookup("#settings_title_tf");
		tf.width = (TextField) fxmlRoot.lookup("#settings_width_tf");
		tf.height = (TextField) fxmlRoot.lookup("#settings_height_tf");
		tf.fileExtension = (TextField) fxmlRoot.lookup("#settings_file_extension_tf");
		tf.defaultFile = (TextField) fxmlRoot.lookup("#settings_default_file_tf");

		tt.files = getTab(tp.top,"files_tab");
		tt.urls = getTab(tp.top,"urls_tab");
		tt.about = getTab(tp.top,"about_tab");
		tt.settings = getTab(tp.top,"settings_tab");
	}

	public void configureControls ( ) {
		btn.quitApp.disableProperty().bind(App.about.mv.confirmQuitAppProperty());
		btn.quitApp.setOnAction(event -> App.about.mv.button_quitApp_clicked(event));
		btn.quitApp.setOnKeyPressed(event -> App.about.mv.button_quitApp_keyPressed(event));
		btn.quitAppSave.disableProperty().bind(Bindings.not(App.about.mv.confirmQuitAppProperty()));
		btn.quitAppSave.setOnAction(event -> App.about.mv.button_quitAppSave_clicked(event));
		btn.quitAppSave.setOnKeyPressed(event -> App.about.mv.button_quitAppSave_keyPressed(event));
		btn.quitAppOK.disableProperty().bind(Bindings.not(App.about.mv.confirmQuitAppProperty()));
		btn.quitAppOK.setOnAction(event -> App.about.mv.button_quitAppOK_clicked(event));
		btn.quitAppOK.setOnKeyPressed(event -> App.about.mv.button_quitAppOK_keyPressed(event));
		btn.reloadFile.disableProperty().bind(Bindings.or(Bindings.not(App.files.vm.selectedProperty()),App.files.vm.confirmingSaveProperty()));
		btn.reloadFile.setOnAction(event -> App.files.vm.button_reloadFile_clicked(event));
		btn.reloadFile.setOnKeyPressed(event -> App.files.vm.button_reloadFile_keyPressed(event));
		btn.reloadAllFiles.disableProperty().bind(Bindings.or(Bindings.not(App.files.vm.selectedProperty()),App.files.vm.confirmingSaveProperty()));
		btn.reloadAllFiles.setOnAction(event -> App.files.vm.button_reloadAllFiles_clicked(event));
		btn.reloadAllFiles.setOnKeyPressed(event -> App.files.vm.button_reloadAllFiles_keyPressed(event));
		btn.fileSave.disableProperty().bind(Bindings.or(Bindings.not(App.files.vm.selectedFileDirtyProperty()),App.files.vm.confirmingSaveProperty()));
		btn.fileSave.setOnAction(event -> App.files.vm.button_fileSave_clicked(event));
		btn.fileSave.setOnKeyPressed(event -> App.files.vm.button_fileSave_keyPressed(event));
		btn.fileCancel.disableProperty().bind(Bindings.not(App.files.vm.confirmingSaveProperty()));
		btn.fileCancel.setOnAction(event -> App.files.vm.button_fileCancel_clicked(event));
		btn.fileCancel.setOnKeyPressed(event -> App.files.vm.button_fileCancel_keyPressed(event));
		btn.fileSaveOK.disableProperty().bind(Bindings.not(App.files.vm.confirmingSaveProperty()));
		btn.fileSaveOK.setOnAction(event -> App.files.vm.button_fileSaveOK_clicked(event));
		btn.fileSaveOK.setOnKeyPressed(event -> App.files.vm.button_fileSaveOK_keyPressed(event));
		btn.openInBrowser.disableProperty().bind(Bindings.not(App.urls.vm.existsProperty()));
		btn.openInBrowser.setOnAction(event -> App.urls.vm.button_openInBrowser_clicked(event));
		btn.openInBrowser.setOnKeyPressed(event -> App.urls.vm.button_openInBrowser_keyPressed(event));
		btn.urlSearch.setOnAction(event -> App.urls.vm.button_urlSearch_clicked(event));
		btn.urlSearch.setOnKeyPressed(event -> App.urls.vm.button_urlSearch_keyPressed(event));
		btn.urlCancel.disableProperty().bind(getUrlCancelDisableBinding());
		btn.urlCancel.setOnAction(event -> App.urls.vm.button_urlCancel_clicked(event));
		btn.urlCancel.setOnKeyPressed(event -> App.urls.vm.button_urlCancel_keyPressed(event));
		btn.urlDelete.disableProperty().bind(getUrlDeleteDisableBinding());
		btn.urlDelete.setOnAction(event -> App.urls.vm.button_urlDelete_clicked(event));
		btn.urlDelete.setOnKeyPressed(event -> App.urls.vm.button_urlDelete_keyPressed(event));
		btn.urlDeleteOK.disableProperty().bind(Bindings.not(App.urls.vm.deleteRequestedProperty()));
		btn.urlDeleteOK.setOnAction(event -> App.urls.vm.button_urlDeleteOK_clicked(event));
		btn.urlDeleteOK.setOnKeyPressed(event -> App.urls.vm.button_urlDeleteOK_keyPressed(event));
		btn.urlCreateOK.disableProperty().bind(Bindings.not(App.urls.vm.urlModifiedProperty()));
		btn.urlCreateOK.setOnAction(event -> App.urls.vm.button_urlCreateOK_clicked(event));
		btn.urlCreateOK.setOnKeyPressed(event -> App.urls.vm.button_urlCreateOK_keyPressed(event));
		btn.urlEditOK.disableProperty().bind(getEditOKDisableBinding());
		btn.urlEditOK.setOnAction(event -> App.urls.vm.button_urlEditOK_clicked(event));
		btn.urlEditOK.setOnKeyPressed(event -> App.urls.vm.button_urlEditOK_keyPressed(event));
		btn.createSettingsFile.disableProperty().bind(getCreateSettingsFileDisableBinding());
		btn.setCallbacks_reloadSettingsFile();
		btn.setCallbacks_createSettingsFile();
		btn.createSettingsFileOK.disableProperty().bind(getCreateSettingsFileDisableBinding().not());
		btn.createSettingsFileOK.setOnAction(event -> App.settings.vm.button_createSettingsFileOK_clicked(event));
		btn.settingsCancel.disableProperty().bind(getCreateSettingsCancelBinding());
		btn.settingsCancel.setOnAction(event -> App.settings.vm.button_settingsCancel_clicked(event));
		btn.saveSettings.disableProperty().bind(getSettingsChangedBinding().not());
		btn.saveSettings.setOnAction(event -> App.settings.vm.button_saveSettings_clicked(event));

		lv.files.setCellFactory( ( ListView<Path> param ) -> App.files.vm.cellFactory(param));
		lv.files.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) -> App.files.vm.listViewItem_selected(observable,oldValue,newValue));
		lv.files.setOnKeyPressed(event -> App.files.vm.listView_keyPressed(event));
		lv.urls.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.listViewItem_selected(observable,oldValue,newValue));
		lv.urls.setCellFactory( ( ListView<String> param ) -> App.urls.vm.cellFactory(param));
		lv.urls.setOnKeyPressed(event -> App.urls.vm.listView_keyPressed(event));

		tp.top.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) -> App.window.mv.topTab_selected(observable,oldValue,newValue));

		ta.tags.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.textField_tags_changed(observable,oldValue,newValue));

		tf.urlSearch.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.textField_urlSearch_changed(observable,oldValue,newValue));
		tf.urlSearch.setOnAction(event -> App.urls.vm.textField_urlSearch_enterPressed(event));
		tf.url.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.urls.vm.textField_url_changed(observable,oldValue,newValue));
		tf.title.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.settings.vm.textField_title_changed(observable,oldValue,newValue));
		tf.width.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.settings.vm.textField_width_changed(observable,oldValue,newValue));
		tf.height.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.settings.vm.textField_height_changed(observable,oldValue,newValue));
		tf.fileExtension.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.settings.vm.textField_fileExtension_changed(observable,oldValue,newValue));
		tf.defaultFile.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> App.settings.vm.textField_defaultFile_changed(observable,oldValue,newValue));

		tt.urls.disableProperty().bind(Bindings.not(App.urls.vm.availableProperty()));
		tt.urls.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> App.urls.vm.topTab_urls_selected(observable,oldValue,newValue));
	}

	private static Tab getTab ( final TabPane tabPane, final String tabId ) {
		for ( Tab tab: tabPane.getTabs() ) {
			final String id = tab.getId();
			if ( (id != null) && (id.equals(tabId)) )
				return tab;
		}
		return null;
	}

	public static final class Buttons {

		public Button quitApp;
		public Button quitAppSave;
		public Button quitAppOK;
		public Button reloadFile;
		public Button reloadAllFiles;
		public Button fileSave;
		public Button fileCancel;
		public Button fileSaveOK;
		public Button openInBrowser;
		public Button urlSearch;
		public Button urlCancel;
		public Button urlDelete;
		public Button urlDeleteOK;
		public Button urlCreateOK;
		public Button urlEditOK;
		public MenuButton reloadSettingsFile;
		public MenuButton createSettingsFile;
		public Button createSettingsFileOK;
		public Button settingsCancel;
		public Button saveSettings;

	}

	public static final class CheckBoxes {

		public CheckBox maximize;
		public CheckBox loadAtStart;
		public CheckBox byPrefix;

	}

	public static final class ListViews {

		public ListView<Path> files;
		public ListView<String> urls;

	}

	public static final class TabPanes {

		public TabPane top;

	}

	public static final class TextAreas {

		public TextArea tags;
		public TextArea log;

	}

	public static final class TextFields {

		public TextField fileName;
		public TextField urlSearch;
		public TextField url;
		public TextField title;
		public TextField width;
		public TextField height;
		public TextField fileExtension;
		public TextField defaultFile;

	}

	public static final class TopTabs {

		public Tab files;
		public Tab urls;
		public Tab about;
		public Tab settings;

	}

}

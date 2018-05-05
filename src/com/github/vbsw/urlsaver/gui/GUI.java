/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.nio.file.Path;

import javax.swing.text.html.CSS;

import com.github.vbsw.urlsaver.logic.Callbacks;
import com.github.vbsw.urlsaver.logic.Factories;
import com.github.vbsw.urlsaver.logic.Properties;
import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


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

	public static Scene scene;
	public static CSS css;

	public static void initialize ( ) {
		final int windowWidth = Preferences.getWindowWidth().getSavedValue();
		final int windowHeight = Preferences.getWindowHeight().getSavedValue();
		final Parent rootStub = new AnchorPane();

		scene = new Scene(rootStub,windowWidth,windowHeight);

		reloadFXML();
		reloadCSS();
	}

	public static void reloadFXML ( ) {
		final Parent root = FXMLReader.getRoot();
		GUI.setElements(root);
		GUI.buildElements();
		scene.setRoot(root);
	}

	public static void reloadCSS ( ) {
		final String cssURI;
		if ( Preferences.isCustomCSSFileAvailable() ) {
			cssURI = Preferences.getCSSPath().getCustomValue().toUri().toString();
			Preferences.setCustomCSSLoaded(true);
		} else {
			cssURI = Preferences.getCSSPath().getDefaultValue().toUri().toString();
			Preferences.setCustomCSSLoaded(false);
		}
		GUI.scene.getStylesheets().clear();
		GUI.scene.getStylesheets().add(cssURI);
	}

	@SuppressWarnings ( "unchecked" )
	public static void setElements ( final Parent root ) {
		btn.quitApp = (Button) root.lookup("#quit_app_btn");
		btn.quitAppSave = (Button) root.lookup("#quit_app_save_btn");
		btn.quitAppOK = (Button) root.lookup("#quit_app_ok_btn");
		btn.reloadFile = (Button) root.lookup("#reload_file_btn");
		btn.reloadAllFiles = (Button) root.lookup("#reload_all_files_btn");
		btn.fileSave = (Button) root.lookup("#file_save_btn");
		btn.fileCancel = (Button) root.lookup("#file_cancel_btn");
		btn.fileSaveOK = (Button) root.lookup("#file_save_ok_btn");
		btn.openInBrowser = (Button) root.lookup("#open_in_browser_btn");
		btn.urlSearch = (Button) root.lookup("#url_search_btn");
		btn.urlCancel = (Button) root.lookup("#url_cancel_btn");
		btn.urlDelete = (Button) root.lookup("#url_delete_btn");
		btn.urlDeleteOK = (Button) root.lookup("#url_delete_ok_btn");
		btn.urlCreateOK = (Button) root.lookup("#url_create_ok_btn");
		btn.urlEditOK = (Button) root.lookup("#url_edit_ok_btn");
		btn.reloadSettingsFile = (MenuButton) root.lookup("#reload_settings_file_btn");
		btn.createSettingsFile = (MenuButton) root.lookup("#create_settings_file_btn");
		btn.createSettingsFileOK = (Button) root.lookup("#create_settings_file_ok_btn");
		btn.settingsCancel = (Button) root.lookup("#settings_cancel_btn");
		btn.saveSettings = (Button) root.lookup("#save_settings_btn");

		cb.maximize = (CheckBox) root.lookup("#settings_maximize_cb");
		cb.loadAtStart = (CheckBox) root.lookup("#settings_load_at_start_cb");
		cb.byPrefix = (CheckBox) root.lookup("#settings_by_prefix_cb");

		lv.files = (ListView<Path>) root.lookup("#file_list_view");
		lv.urls = (ListView<String>) root.lookup("#url_list_view");

		tp.top = (TabPane) root.lookup("#top_tab_pane");

		ta.tags = (TextArea) root.lookup("#tags_ta");
		ta.log = (TextArea) root.lookup("#log_ta");

		tf.fileName = (TextField) root.lookup("#file_name_tf");
		tf.urlSearch = (TextField) root.lookup("#url_search_tf");
		tf.url = (TextField) root.lookup("#url_tf");
		tf.title = (TextField) root.lookup("#settings_title_tf");
		tf.width = (TextField) root.lookup("#settings_width_tf");
		tf.height = (TextField) root.lookup("#settings_height_tf");
		tf.fileExtension = (TextField) root.lookup("#settings_file_extension_tf");
		tf.defaultFile = (TextField) root.lookup("#settings_default_file_tf");

		tt.files = getTab(tp.top,"files_tab");
		tt.urls = getTab(tp.top,"urls_tab");
		tt.about = getTab(tp.top,"about_tab");
		tt.settings = getTab(tp.top,"settings_tab");
	}

	public static void buildElements ( ) {
		btn.quitApp.disableProperty().bind(Properties.confirmQuitAppProperty());
		btn.quitApp.setOnAction(event -> Callbacks.button_quitApp_clicked(event));
		btn.quitApp.setOnKeyPressed(event -> Callbacks.button_quitApp_keyPressed(event));
		btn.quitAppSave.disableProperty().bind(Bindings.not(Properties.confirmQuitAppProperty()));
		btn.quitAppSave.setOnAction(event -> Callbacks.button_quitAppSave_clicked(event));
		btn.quitAppSave.setOnKeyPressed(event -> Callbacks.button_quitAppSave_keyPressed(event));
		btn.quitAppOK.disableProperty().bind(Bindings.not(Properties.confirmQuitAppProperty()));
		btn.quitAppOK.setOnAction(event -> Callbacks.button_quitAppOK_clicked(event));
		btn.quitAppOK.setOnKeyPressed(event -> Callbacks.button_quitAppOK_keyPressed(event));
		btn.reloadFile.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
		btn.reloadFile.setOnAction(event -> Callbacks.button_reloadFile_clicked(event));
		btn.reloadFile.setOnKeyPressed(event -> Callbacks.button_reloadFile_keyPressed(event));
		btn.reloadAllFiles.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
		btn.reloadAllFiles.setOnAction(event -> Callbacks.button_reloadAllFiles_clicked(event));
		btn.reloadAllFiles.setOnKeyPressed(event -> Callbacks.button_reloadAllFiles_keyPressed(event));
		btn.fileSave.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedFileDirtyProperty()),Properties.confirmingSaveProperty()));
		btn.fileSave.setOnAction(event -> Callbacks.button_fileSave_clicked(event));
		btn.fileSave.setOnKeyPressed(event -> Callbacks.button_fileSave_keyPressed(event));
		btn.fileCancel.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
		btn.fileCancel.setOnAction(event -> Callbacks.button_fileCancel_clicked(event));
		btn.fileCancel.setOnKeyPressed(event -> Callbacks.button_fileCancel_keyPressed(event));
		btn.fileSaveOK.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
		btn.fileSaveOK.setOnAction(event -> Callbacks.button_fileSaveOK_clicked(event));
		btn.fileSaveOK.setOnKeyPressed(event -> Callbacks.button_fileSaveOK_keyPressed(event));
		btn.openInBrowser.disableProperty().bind(Bindings.not(Properties.existsProperty()));
		btn.openInBrowser.setOnAction(event -> Callbacks.button_openInBrowser_clicked(event));
		btn.openInBrowser.setOnKeyPressed(event -> Callbacks.button_openInBrowser_keyPressed(event));
		btn.urlSearch.setOnAction(event -> Callbacks.button_urlSearch_clicked(event));
		btn.urlSearch.setOnKeyPressed(event -> Callbacks.button_urlSearch_keyPressed(event));
		btn.urlCancel.disableProperty().bind(getUrlCancelDisableBinding());
		btn.urlCancel.setOnAction(event -> Callbacks.button_urlCancel_clicked(event));
		btn.urlCancel.setOnKeyPressed(event -> Callbacks.button_urlCancel_keyPressed(event));
		btn.urlDelete.disableProperty().bind(getUrlDeleteDisableBinding());
		btn.urlDelete.setOnAction(event -> Callbacks.button_urlDelete_clicked(event));
		btn.urlDelete.setOnKeyPressed(event -> Callbacks.button_urlDelete_keyPressed(event));
		btn.urlDeleteOK.disableProperty().bind(Bindings.not(Properties.urlDeleteRequestedProperty()));
		btn.urlDeleteOK.setOnAction(event -> Callbacks.button_urlDeleteOK_clicked(event));
		btn.urlDeleteOK.setOnKeyPressed(event -> Callbacks.button_urlDeleteOK_keyPressed(event));
		btn.urlCreateOK.disableProperty().bind(Bindings.not(Properties.urlModifiedProperty()));
		btn.urlCreateOK.setOnAction(event -> Callbacks.button_urlCreateOK_clicked(event));
		btn.urlCreateOK.setOnKeyPressed(event -> Callbacks.button_urlCreateOK_keyPressed(event));
		btn.urlEditOK.disableProperty().bind(getEditOKDisableBinding());
		btn.urlEditOK.setOnAction(event -> Callbacks.button_urlEditOK_clicked(event));
		btn.urlEditOK.setOnKeyPressed(event -> Callbacks.button_urlEditOK_keyPressed(event));
		btn.createSettingsFile.disableProperty().bind(getCreateSettingsFileDisableBinding());
		setCallbacks_reloadSettingsFile();
		setCallbacks_createSettingsFile();
		btn.createSettingsFileOK.disableProperty().bind(getCreateSettingsFileDisableBinding().not());
		btn.createSettingsFileOK.setOnAction(event -> Callbacks.button_createSettingsFileOK_clicked(event));
		btn.settingsCancel.disableProperty().bind(getCreateSettingsCancelBinding());
		btn.settingsCancel.setOnAction(event -> Callbacks.button_settingsCancel_clicked(event));
		btn.saveSettings.disableProperty().bind(getSettingsChangedBinding().not());
		btn.saveSettings.setOnAction(event -> Callbacks.button_saveSettings_clicked(event));

		lv.files.setCellFactory( ( ListView<Path> param ) -> Factories.filesCellFactory(param));
		lv.files.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) -> Callbacks.filesListViewItem_selected(observable,oldValue,newValue));
		lv.files.setOnKeyPressed(event -> Callbacks.filesListView_keyPressed(event));
		lv.urls.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.urlsListViewItem_selected(observable,oldValue,newValue));
		lv.urls.setCellFactory( ( ListView<String> param ) -> Factories.urlsCellFactory(param));
		lv.urls.setOnKeyPressed(event -> Callbacks.urlsListView_keyPressed(event));

		tp.top.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) -> Callbacks.topTab_selected(observable,oldValue,newValue));

		ta.tags.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_tags_changed(observable,oldValue,newValue));

		tf.urlSearch.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_urlSearch_changed(observable,oldValue,newValue));
		tf.urlSearch.setOnAction(event -> Callbacks.textField_urlSearch_enterPressed(event));
		tf.url.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_url_changed(observable,oldValue,newValue));
		tf.title.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_title_changed(observable,oldValue,newValue));
		tf.width.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_width_changed(observable,oldValue,newValue));
		tf.height.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_height_changed(observable,oldValue,newValue));
		tf.fileExtension.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_fileExtension_changed(observable,oldValue,newValue));
		tf.defaultFile.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_defaultFile_changed(observable,oldValue,newValue));

		tt.urls.disableProperty().bind(Bindings.not(Properties.availableProperty()));
		tt.urls.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> Callbacks.topTab_urls_selected(observable,oldValue,newValue));
	}

	private static BooleanExpression getSettingsChangedBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.titleChangedProperty(),Properties.widthChangedProperty());
		binding = Bindings.or(binding,Properties.heightChangedProperty());
		binding = Bindings.or(binding,Properties.fileExtensionChangedProperty());
		binding = Bindings.or(binding,Properties.defaultFileChangedProperty());
		binding = Bindings.or(binding,Properties.maximizeChangedProperty());
		binding = Bindings.or(binding,Properties.loadAtStartChangedProperty());
		binding = Bindings.or(binding,Properties.byPrefixChangedProperty());
		return binding;
	}

	private static ObservableValue<? extends Boolean> getCreateSettingsCancelBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(getCreateSettingsFileDisableBinding(),Properties.propertiesModifiedProperty());
		binding = Bindings.not(binding);
		return binding;
	}

	private static void setCallbacks_reloadSettingsFile ( ) {
		final ObservableList<MenuItem> items = btn.reloadSettingsFile.getItems();
		final String settingsBtnSelector = "reload_settings_file_btn";
		final String cssBtnSelector = "reload_css_file_btn";
		final String fxmlBtnSelector = "reload_fxml_file_btn";

		for ( final MenuItem item: items ) {
			final String id = item.getId();

			if ( id != null ) {
				if ( id.equals(settingsBtnSelector) ) {
					item.setOnAction(event -> Callbacks.button_reloadSettingsFile_clicked(event));

				} else if ( id.equals(cssBtnSelector) ) {
					item.setOnAction(event -> Callbacks.button_reloadCSSFile_clicked(event));

				} else if ( id.equals(fxmlBtnSelector) ) {
					item.setOnAction(event -> Callbacks.button_reloadFXMLFile_clicked(event));
				}
			}
		}
	}

	private static void setCallbacks_createSettingsFile ( ) {
		final ObservableList<MenuItem> items = btn.createSettingsFile.getItems();
		final String settingsBtnSelector = "create_settings_file_btn"; //$NON-NLS-1$
		final String cssBtnSelector = "create_css_file_btn"; //$NON-NLS-1$
		final String fxmlBtnSelector = "create_fxml_file_btn"; //$NON-NLS-1$

		for ( final MenuItem item: items ) {
			final String id = item.getId();

			if ( id != null ) {
				if ( id.equals(settingsBtnSelector) ) {
					item.setOnAction(event -> Callbacks.button_createSettingsFile_clicked(event));

				} else if ( id.equals(cssBtnSelector) ) {
					item.setOnAction(event -> Callbacks.button_createCSSFile_clicked(event));

				} else if ( id.equals(fxmlBtnSelector) ) {
					item.setOnAction(event -> Callbacks.button_createFXMLFile_clicked(event));
				}
			}
		}
	}

	private static BooleanBinding getCreateSettingsFileDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.confirmingCreatePreferencesProperty(),Properties.confirmingCreateCSSProperty());
		binding = Bindings.or(binding,Properties.confirmingCreateFXMLProperty());
		return binding;
	}

	private static ObservableValue<? extends Boolean> getEditOKDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.not(Properties.urlModifiedProperty());
		binding = Bindings.and(binding,Properties.urlTagsModifiedProperty());
		binding = Bindings.not(binding);
		return binding;
	}

	private static ObservableValue<? extends Boolean> getUrlDeleteDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.not(Properties.existsProperty());
		binding = Bindings.or(binding,Properties.urlDeleteRequestedProperty());
		binding = Bindings.or(binding,Properties.urlModifiedProperty());
		binding = Bindings.or(binding,Properties.urlTagsModifiedProperty());
		return binding;
	}

	private static ObservableValue<? extends Boolean> getUrlCancelDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.urlModifiedProperty(),Properties.urlTagsModifiedProperty());
		binding = Bindings.or(binding,Properties.urlDeleteRequestedProperty());
		binding = Bindings.not(binding);
		return binding;
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

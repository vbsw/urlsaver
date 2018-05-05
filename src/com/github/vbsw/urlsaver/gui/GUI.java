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
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class GUI {

	public static final Buttons buttons = new Buttons();
	public static final CheckBoxes checkBoxes = new CheckBoxes();
	public static final ListViews listViews = new ListViews();
	public static final TabPanes tabPanes = new TabPanes();
	public static final TextAreas textAreas = new TextAreas();
	public static final TextFields textFields = new TextFields();
	public static final TopTabs topTabs = new TopTabs();

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
		buttons.quitApp = (Button) root.lookup("#quit_app_btn");
		buttons.quitAppSave = (Button) root.lookup("#quit_app_save_btn");
		buttons.quitAppOK = (Button) root.lookup("#quit_app_ok_btn");
		buttons.reloadFile = (Button) root.lookup("#reload_file_btn");
		buttons.reloadAllFiles = (Button) root.lookup("#reload_all_files_btn");
		buttons.fileSave = (Button) root.lookup("#file_save_btn");
		buttons.fileCancel = (Button) root.lookup("#file_cancel_btn");
		buttons.fileSaveOK = (Button) root.lookup("#file_save_ok_btn");
		buttons.openInBrowser = (Button) root.lookup("#open_in_browser_btn");
		buttons.urlSearch = (Button) root.lookup("#url_search_btn");
		buttons.urlCancel = (Button) root.lookup("#url_cancel_btn");
		buttons.urlDelete = (Button) root.lookup("#url_delete_btn");
		buttons.urlDeleteOK = (Button) root.lookup("#url_delete_ok_btn");
		buttons.urlCreateOK = (Button) root.lookup("#url_create_ok_btn");
		buttons.urlEditOK = (Button) root.lookup("#url_edit_ok_btn");
		buttons.reloadSettingsFile = (MenuButton) root.lookup("#reload_settings_file_btn");
		buttons.createSettingsFile = (MenuButton) root.lookup("#create_settings_file_btn");
		buttons.createSettingsFileOK = (Button) root.lookup("#create_settings_file_ok_btn");
		buttons.settingsCancel = (Button) root.lookup("#settings_cancel_btn");
		buttons.saveSettings = (Button) root.lookup("#save_settings_btn");

		checkBoxes.maximize = (CheckBox) root.lookup("#settings_maximize_cb");
		checkBoxes.loadAtStart = (CheckBox) root.lookup("#settings_load_at_start_cb");
		checkBoxes.byPrefix = (CheckBox) root.lookup("#settings_by_prefix_cb");

		listViews.files = (ListView<Path>) root.lookup("#file_list_view");
		listViews.urls = (ListView<String>) root.lookup("#url_list_view");

		tabPanes.top = (TabPane) root.lookup("#top_tab_pane");

		textAreas.tags = (TextArea) root.lookup("#tags_ta");
		textAreas.log = (TextArea) root.lookup("#log_ta");

		textFields.fileName = (TextField) root.lookup("#file_name_tf");
		textFields.urlSearch = (TextField) root.lookup("#url_search_tf");
		textFields.url = (TextField) root.lookup("#url_tf");
		textFields.title = (TextField) root.lookup("#settings_title_tf");
		textFields.width = (TextField) root.lookup("#settings_width_tf");
		textFields.height = (TextField) root.lookup("#settings_height_tf");
		textFields.fileExtension = (TextField) root.lookup("#settings_file_extension_tf");
		textFields.defaultFile = (TextField) root.lookup("#settings_default_file_tf");

		topTabs.files = getTab(tabPanes.top,"files_tab");
		topTabs.urls = getTab(tabPanes.top,"urls_tab");
		topTabs.about = getTab(tabPanes.top,"about_tab");
		topTabs.settings = getTab(tabPanes.top,"settings_tab");
	}

	public static void buildElements ( ) {
		buttons.quitApp.disableProperty().bind(Properties.confirmQuitAppProperty());
		buttons.quitApp.setOnAction(event -> Callbacks.button_quitApp_clicked(event));
		buttons.quitApp.setOnKeyPressed(event -> Callbacks.button_quitApp_keyPressed(event));
		buttons.quitAppSave.disableProperty().bind(Bindings.not(Properties.confirmQuitAppProperty()));
		buttons.quitAppSave.setOnAction(event -> Callbacks.button_quitAppSave_clicked(event));
		buttons.quitAppSave.setOnKeyPressed(event -> Callbacks.button_quitAppSave_keyPressed(event));
		buttons.quitAppOK.disableProperty().bind(Bindings.not(Properties.confirmQuitAppProperty()));
		buttons.quitAppOK.setOnAction(event -> Callbacks.button_quitAppOK_clicked(event));
		buttons.quitAppOK.setOnKeyPressed(event -> Callbacks.button_quitAppOK_keyPressed(event));
		buttons.reloadFile.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
		buttons.reloadFile.setOnAction(event -> Callbacks.button_reloadFile_clicked(event));
		buttons.reloadFile.setOnKeyPressed(event -> Callbacks.button_reloadFile_keyPressed(event));
		buttons.reloadAllFiles.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
		buttons.reloadAllFiles.setOnAction(event -> Callbacks.button_reloadAllFiles_clicked(event));
		buttons.reloadAllFiles.setOnKeyPressed(event -> Callbacks.button_reloadAllFiles_keyPressed(event));
		buttons.fileSave.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedFileDirtyProperty()),Properties.confirmingSaveProperty()));
		buttons.fileSave.setOnAction(event -> Callbacks.button_fileSave_clicked(event));
		buttons.fileSave.setOnKeyPressed(event -> Callbacks.button_fileSave_keyPressed(event));
		buttons.fileCancel.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
		buttons.fileCancel.setOnAction(event -> Callbacks.button_fileCancel_clicked(event));
		buttons.fileCancel.setOnKeyPressed(event -> Callbacks.button_fileCancel_keyPressed(event));
		buttons.fileSaveOK.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
		buttons.fileSaveOK.setOnAction(event -> Callbacks.button_fileSaveOK_clicked(event));
		buttons.fileSaveOK.setOnKeyPressed(event -> Callbacks.button_fileSaveOK_keyPressed(event));
		buttons.openInBrowser.disableProperty().bind(Bindings.not(Properties.existsProperty()));
		buttons.openInBrowser.setOnAction(event -> Callbacks.button_openInBrowser_clicked(event));
		buttons.openInBrowser.setOnKeyPressed(event -> Callbacks.button_openInBrowser_keyPressed(event));
		buttons.urlSearch.setOnAction(event -> Callbacks.button_urlSearch_clicked(event));
		buttons.urlSearch.setOnKeyPressed(event -> Callbacks.button_urlSearch_keyPressed(event));
		buttons.urlCancel.disableProperty().bind(getUrlCancelDisableBinding());
		buttons.urlCancel.setOnAction(event -> Callbacks.button_urlCancel_clicked(event));
		buttons.urlCancel.setOnKeyPressed(event -> Callbacks.button_urlCancel_keyPressed(event));
		buttons.urlDelete.disableProperty().bind(getUrlDeleteDisableBinding());
		buttons.urlDelete.setOnAction(event -> Callbacks.button_urlDelete_clicked(event));
		buttons.urlDelete.setOnKeyPressed(event -> Callbacks.button_urlDelete_keyPressed(event));
		buttons.urlDeleteOK.disableProperty().bind(Bindings.not(Properties.urlDeleteRequestedProperty()));
		buttons.urlDeleteOK.setOnAction(event -> Callbacks.button_urlDeleteOK_clicked(event));
		buttons.urlDeleteOK.setOnKeyPressed(event -> Callbacks.button_urlDeleteOK_keyPressed(event));
		buttons.urlCreateOK.disableProperty().bind(Bindings.not(Properties.urlModifiedProperty()));
		buttons.urlCreateOK.setOnAction(event -> Callbacks.button_urlCreateOK_clicked(event));
		buttons.urlCreateOK.setOnKeyPressed(event -> Callbacks.button_urlCreateOK_keyPressed(event));
		buttons.urlEditOK.disableProperty().bind(getEditOKDisableBinding());
		buttons.urlEditOK.setOnAction(event -> Callbacks.button_urlEditOK_clicked(event));
		buttons.urlEditOK.setOnKeyPressed(event -> Callbacks.button_urlEditOK_keyPressed(event));
		buttons.createSettingsFile.disableProperty().bind(getCreateSettingsFileDisableBinding());
		setCallbacks_reloadSettingsFile();
		setCallbacks_createSettingsFile();
		buttons.createSettingsFileOK.disableProperty().bind(getCreateSettingsFileDisableBinding().not());
		buttons.createSettingsFileOK.setOnAction(event -> Callbacks.button_createSettingsFileOK_clicked(event));
		buttons.settingsCancel.disableProperty().bind(getCreateSettingsCancelBinding());
		buttons.settingsCancel.setOnAction(event -> Callbacks.button_settingsCancel_clicked(event));
		buttons.saveSettings.disableProperty().bind(getSettingsChangedBinding().not());
		buttons.saveSettings.setOnAction(event -> Callbacks.button_saveSettings_clicked(event));

		listViews.files.setCellFactory( ( ListView<Path> param ) -> Factories.filesCellFactory(param));
		listViews.files.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) -> Callbacks.filePathListViewItem_selected(observable,oldValue,newValue));
		listViews.files.setOnKeyPressed(event -> Callbacks.filePathListView_keyPressed(event));
		listViews.urls.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.urlsListViewItem_selected(observable,oldValue,newValue));
		listViews.urls.setCellFactory( ( ListView<String> param ) -> Factories.urlsCellFactory(param));
		listViews.urls.setOnKeyPressed(event -> Callbacks.urlsListView_keyPressed(event));

		tabPanes.top.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) -> Callbacks.topTab_selected(observable,oldValue,newValue));

		textAreas.tags.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_tags_changed(observable,oldValue,newValue));

		textFields.urlSearch.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_urlSearch_changed(observable,oldValue,newValue));
		textFields.urlSearch.setOnAction(event -> Callbacks.textField_urlSearch_enterPressed(event));
		textFields.url.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_url_changed(observable,oldValue,newValue));
		textFields.title.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_title_changed(observable,oldValue,newValue));
		textFields.width.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_width_changed(observable,oldValue,newValue));
		textFields.height.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_height_changed(observable,oldValue,newValue));
		textFields.fileExtension.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_fileExtension_changed(observable,oldValue,newValue));
		textFields.defaultFile.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_defaultFile_changed(observable,oldValue,newValue));

		topTabs.urls.disableProperty().bind(Bindings.not(Properties.availableProperty()));
		topTabs.urls.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> Callbacks.topTab_urls_selected(observable,oldValue,newValue));
	}

	public static void setWindowTitle ( final String title ) {
		final Stage stage = (Stage) GUI.scene.getWindow();
		stage.setTitle(title);
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
		final ObservableList<MenuItem> items = buttons.reloadSettingsFile.getItems();
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
		final ObservableList<MenuItem> items = buttons.createSettingsFile.getItems();
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

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.App;
import com.github.vbsw.urlsaver.Converter;
import com.github.vbsw.urlsaver.JarFile;
import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.WebBrowserAccess;
import com.github.vbsw.urlsaver.db.DB;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.db.DynArrayOfString;
import com.github.vbsw.urlsaver.io.PreferencesIO;
import com.github.vbsw.urlsaver.io.URLsIO;
import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * @author Vitali Baumtrok
 */
public class Buttons {

	public static final QuitApp quitApp = new QuitApp();
	public static final QuitAppSave quitAppSave = new QuitAppSave();
	public static final QuitAppOK quitAppOK = new QuitAppOK();
	public static final CreateDefaultFile createDefaultFile = new CreateDefaultFile();
	public static final ReloadFile reloadFile = new ReloadFile();
	public static final ReloadAllFiles reloadAllFiles = new ReloadAllFiles();
	public static final FileSave fileSave = new FileSave();
	public static final FileCancel fileCancel = new FileCancel();
	public static final FileSaveOK fileSaveOK = new FileSaveOK();
	public static final OpenInBrowser openInBrowser = new OpenInBrowser();
	public static final UrlSearch urlSearch = new UrlSearch();
	public static final URLCancel urlCancel = new URLCancel();
	public static final URLDelete urlDelete = new URLDelete();
	public static final URLDeleteOK urlDeleteOK = new URLDeleteOK();
	public static final URLCreateOK urlCreateOK = new URLCreateOK();
	public static final URLEditOK urlEditOK = new URLEditOK();
	public static final PreferencesCreateOK preferencesCreateOK = new PreferencesCreateOK();
	public static final PreferencesCancel preferencesCancel = new PreferencesCancel();
	public static final PreferencesSave preferencesSave = new PreferencesSave();
	public static final PreferencesReload preferencesReload = new PreferencesReload();
	public static final PreferencesCreate preferencesCreate = new PreferencesCreate();

	public static void build ( final Parent root ) {
		quitApp.build(root);
		quitAppSave.build(root);
		quitAppOK.build(root);
		createDefaultFile.build(root);
		reloadFile.build(root);
		reloadAllFiles.build(root);
		fileSave.build(root);
		fileCancel.build(root);
		fileSaveOK.build(root);
		openInBrowser.build(root);
		urlSearch.build(root);
		urlCancel.build(root);
		urlDelete.build(root);
		urlDeleteOK.build(root);
		urlCreateOK.build(root);
		urlEditOK.build(root);
		preferencesCreateOK.build(root);
		preferencesCancel.build(root);
		preferencesSave.build(root);
		preferencesReload.build(root);
		preferencesCreate.build(root);
	}

	public static void confirmURLEdit ( ) {
		final DBRecord record = GUI.getCurrentDBRecord();
		final String urlTyped = Parser.trim(TextFields.url.control.getText());
		final int urlIndex = record.getURLIndex(urlTyped);
		final DynArrayOfString tags = Converter.toDynArrayListSorted(TextAreas.tags.control.getText());
		record.setTags(urlIndex,tags);

		final String tagsString = record.getTagsAsString(urlIndex);
		TextAreas.tags.control.setText(tagsString);
		ListViews.urls.control.requestFocus();
		GUI.refreshFileSelection();
	}

	public static void confirmURLDelete ( ) {
		final DBRecord record = GUI.getCurrentDBRecord();
		final String url = ListViews.urls.control.getSelectionModel().getSelectedItem();
		final int urlIndex = record.getURLIndex(url);
		final int selectedIndex = ListViews.urls.control.getSelectionModel().getSelectedIndex();
		record.removeURL(urlIndex);
		ListViews.urls.control.getItems().remove(selectedIndex);
		if ( ListViews.urls.control.getItems().size() > selectedIndex )
			ListViews.urls.control.getSelectionModel().select(selectedIndex);
		else
			record.setSelectedURLIndex(-1);
		if ( ListViews.urls.control.getItems().isEmpty() )
			TextFields.urlSearch.control.requestFocus();
		else
			ListViews.urls.control.requestFocus();
		GUI.refreshFileSelection();
	}

	public static void confirmURLCreate ( ) {
		final DBRecord selectedRecord = GUI.getCurrentDBRecord();
		final String url = Parser.trim(TextFields.url.control.getText());
		final int urlIndex = selectedRecord.addUrl(url);
		final ArrayList<String> tags = Converter.toArrayList(TextAreas.tags.control.getText());
		for ( final String tag: tags )
			selectedRecord.addTagToUrl(urlIndex,tag);
		final String tagsString = selectedRecord.getTagsAsString(urlIndex);
		TextAreas.tags.control.setText(tagsString);
		ListViews.urls.control.requestFocus();
		Properties.urlExistsProperty().set(true);
		Properties.urlModifiedProperty().set(false);
		GUI.refreshFileSelection();
	}

	private static void quitApp_clicked ( final ActionEvent event ) {
		App.quit();
	}

	private static void quitApp_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			App.quit();
	}

	private static void quitAppSave_clicked ( final ActionEvent event ) {
		URLsIO.saveAllFiles();
		App.quitUnconditionally();
	}

	private static void quitAppSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsIO.saveAllFiles();
			App.quitUnconditionally();
		}
	}

	private static void quitAppOK_clicked ( final ActionEvent event ) {
		App.quitUnconditionally();
	}

	private static void quitAppOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			App.quitUnconditionally();
	}

	private static void reloadFile_clicked ( final ActionEvent event ) {
		final DBRecord selectedRecord = GUI.getCurrentDBRecord();
		URLsIO.reloadFile(selectedRecord);
	}

	private static void reloadFile_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			final DBRecord selectedRecord = GUI.getCurrentDBRecord();
			URLsIO.reloadFile(selectedRecord);
		}
	}

	private static void reloadAllFiles_clicked ( final ActionEvent event ) {
		URLsIO.reloadAllFiles();
	}

	private static void reloadAllFiles_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			URLsIO.reloadAllFiles();
	}

	private static void fileSave_clicked ( final ActionEvent event ) {
		Properties.confirmingSaveProperty().set(true);
	}

	private static void fileSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			Properties.confirmingSaveProperty().set(true);
		}
	}

	private static void fileCancel_clicked ( final ActionEvent event ) {
		Properties.confirmingSaveProperty().set(false);
		ListViews.files.control.requestFocus();
	}

	private static void fileCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			fileCancel_clicked(null);
	}

	private static void fileSaveOK_clicked ( final ActionEvent event ) {
		final DBRecord selectedRecord = GUI.getCurrentDBRecord();
		URLsIO.saveFile(selectedRecord);
		GUI.refreshFileInfo();
		GUI.refreshTitle();
	}

	private static void fileSaveOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			fileSaveOK_clicked(null);
	}

	private static void openInBrowser_clicked ( final ActionEvent event ) {
		final String url = ListViews.urls.control.getSelectionModel().getSelectedItem();
		WebBrowserAccess.openURL(url);
	}

	private static void openInBrowser_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			final String url = ListViews.urls.control.getSelectionModel().getSelectedItem();
			WebBrowserAccess.openURL(url);
		}
	}

	private static void urlSearch_clicked ( final ActionEvent event ) {
		final DBRecord record = GUI.getCurrentDBRecord();
		final String searchString = record.getURLsSearchString();
		final boolean searchByPrefix = Preferences.getSearchByPrefix().getSavedValue();
		final DynArrayOfString searchTags = Converter.toDynArrayList(searchString);

		record.searchURLs(searchTags,searchByPrefix);
		ListViews.urls.showSearchResults();
		GUI.refreshURLsInfo();
		Properties.resetURLsProperties();
	}

	private static void urlSearch_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			urlSearch_clicked(null);
		}
	}

	private static void urlCancel_clicked ( final ActionEvent event ) {
		GUI.refreshURLsInfo();
		if ( ListViews.urls.control.getItems().isEmpty() )
			TextFields.urlSearch.control.requestFocus();
		else
			ListViews.urls.control.requestFocus();
	}

	private static void urlCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			Buttons.urlCancel_clicked(null);
	}

	private static void urlDelete_clicked ( final ActionEvent event ) {
		Properties.urlDeleteRequestedProperty().set(true);
		Buttons.urlCancel.control.requestFocus();
	}

	private static void urlDelete_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			Properties.urlDeleteRequestedProperty().set(true);
			Buttons.urlCancel.control.requestFocus();
		}
	}

	private static void urlDeleteOK_clicked ( final ActionEvent event ) {
		Buttons.confirmURLDelete();
		Properties.urlDeleteRequestedProperty().set(false);
	}

	private static void urlDeleteOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			Buttons.urlDelete_clicked(null);
	}

	private static void urlCreateOK_clicked ( final ActionEvent event ) {
		Buttons.confirmURLCreate();
		GUI.refreshTitle();
	}

	private static void urlCreateOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			Buttons.confirmURLCreate();
			GUI.refreshTitle();
		}
	}

	private static void urlEditOK_clicked ( final ActionEvent event ) {
		confirmURLEdit();
		Properties.urlExistsProperty().set(true);
		Properties.urlTagsModifiedProperty().set(false);
	}

	private static void urlEditOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			urlEditOK_clicked(null);
		}
	}

	private static void createPreferencesFileOK_clicked ( final ActionEvent event ) {
		if ( Properties.confirmingCreatePreferencesProperty().get() )
			PreferencesIO.overwritePreferencesFile();
		else if ( Properties.confirmingCreateCSSProperty().get() )
			PreferencesIO.overwriteCSSFile();
		else if ( Properties.confirmingCreateFXMLProperty().get() )
			PreferencesIO.overwriteFXMLFile();
	}

	private static void preferencesCancel_clicked ( final ActionEvent event ) {
		Properties.confirmingCreatePreferencesProperty().set(false);
		Properties.confirmingCreateCSSProperty().set(false);
		Properties.confirmingCreateFXMLProperty().set(false);
		Preferences.resetModifiedValuesToSaved();
		GUI.refreshPreferencesView();
	}

	private static void savePreferences_clicked ( final ActionEvent event ) {
		final String fileName = Preferences.getPreferencesPath().getSavedValue().getFileName().toString();
		Preferences.savePreferences();

		if ( Preferences.isCustomPreferencesSaved() ) {
			Preferences.resetSavedValuesToModified();
			Properties.titleChangedProperty().set(false);
			Properties.widthChangedProperty().set(false);
			Properties.heightChangedProperty().set(false);
			Properties.urlsFileExtensionChangedProperty().set(false);
			Properties.urlsFileSelectChangedProperty().set(false);
			Properties.maximizeChangedProperty().set(false);
			Properties.loadAtStartChangedProperty().set(false);
			Properties.byPrefixChangedProperty().set(false);
			TextFields.title.setFontWeight(false);
			TextFields.width.setFontWeight(false);
			TextFields.height.setFontWeight(false);
			TextFields.urlsFileExtension.setFontWeight(false);
			TextFields.urlsFileSelect.setFontWeight(false);
			CheckBoxes.maximize.setFontWeight(false);
			CheckBoxes.urlsFileAutoloadAll.setFontWeight(false);
			CheckBoxes.byPrefix.setFontWeight(false);
			Logger.logSuccess("file saved (" + fileName + ")");
			TextAreas.log.control.requestFocus();
		} else {
			Logger.logFailure("file not saved (" + fileName + ")");
			TextAreas.log.control.requestFocus();
		}
	}

	private static void reloadPreferencesFile_clicked ( final ActionEvent event ) {
		Preferences.loadCustomPreferences();
		GUI.refreshPreferencesView();
		GUI.refreshTitle();
	}

	private static void reloadCSSFile_clicked ( final ActionEvent event ) {
		GUI.reloadCSS();
	}

	private static void reloadFXMLFile_clicked ( final ActionEvent event ) {
		final String logBackup = TextAreas.log.control.getText();
		final int selectedIndex = ListViews.files.control.getSelectionModel().getSelectedIndex();
		GUI.reloadFXML();
		GUI.refreshPreferencesView();
		TabPanes.top.preferences.select();
		TextAreas.log.control.setText(logBackup);
		Platform.runLater(new Runnable() {
			@Override
			public void run ( ) {
				Buttons.preferencesReload.control.requestFocus();
			}
		});
		ListViews.files.control.getItems().addAll(DB.getRecords());
		ListViews.files.control.getSelectionModel().select(selectedIndex);
	}

	private static void createPreferencesFile_clicked ( final ActionEvent event ) {
		PreferencesIO.createPreferencesFile();
	}

	private static void createCSSFile_clicked ( final ActionEvent event ) {
		PreferencesIO.createCSSFile();
	}

	private static void createFXMLFile_clicked ( final ActionEvent event ) {
		PreferencesIO.createFXMLFile();
	}

	private static BooleanBinding getCreatePreferencesFileDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.confirmingCreatePreferencesProperty(),Properties.confirmingCreateCSSProperty());
		binding = Bindings.or(binding,Properties.confirmingCreateFXMLProperty());
		return binding;
	}

	private static ObservableValue<? extends Boolean> getUrlCancelDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.urlModifiedProperty(),Properties.urlTagsModifiedProperty());
		binding = Bindings.or(binding,Properties.urlDeleteRequestedProperty());
		binding = Bindings.not(binding);
		return binding;
	}

	private static ObservableValue<? extends Boolean> getUrlDeleteDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.not(Properties.urlExistsProperty());
		binding = Bindings.or(binding,Properties.urlDeleteRequestedProperty());
		binding = Bindings.or(binding,Properties.urlModifiedProperty());
		binding = Bindings.or(binding,Properties.urlTagsModifiedProperty());
		return binding;
	}

	private static ObservableValue<? extends Boolean> getEditOKDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.not(Properties.urlModifiedProperty());
		binding = Bindings.and(binding,Properties.urlTagsModifiedProperty());
		binding = Bindings.not(binding);
		return binding;
	}

	private static BooleanExpression getPreferencesChangedBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.titleChangedProperty(),Properties.widthChangedProperty());
		binding = Bindings.or(binding,Properties.heightChangedProperty());
		binding = Bindings.or(binding,Properties.urlsFileExtensionChangedProperty());
		binding = Bindings.or(binding,Properties.urlsFileSelectChangedProperty());
		binding = Bindings.or(binding,Properties.maximizeChangedProperty());
		binding = Bindings.or(binding,Properties.loadAtStartChangedProperty());
		binding = Bindings.or(binding,Properties.byPrefixChangedProperty());
		return binding;
	}

	public static void createDefaultFile_clicked ( final ActionEvent event ) {
		final String defaultFileName = Preferences.getURLsFileSelect().getSavedValue();
		final Path defaultFilePath = JarFile.getPath().resolve(defaultFileName);
		try {
			Files.createFile(defaultFilePath);
			Properties.createDefaultFilePossibleProperty().set(false);
			DB.initialize();
			URLsIO.initialize();

			final ArrayList<DBRecord> records = DB.getRecords();
			ListViews.files.control.getItems().addAll(records);
			if ( records.size() > 0 ) {
				ListViews.files.control.requestFocus();
				ListViews.files.control.getSelectionModel().select(records.get(0));
				Logger.logSuccess("default file created (" + defaultFileName + ")");
			}
		} catch ( final IOException e ) {
			Logger.logFailure("could not create file (" + defaultFileName + ")");
			e.printStackTrace();
		}
	}

	public static void createDefaultFile_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			Buttons.createDefaultFile_clicked(null);
	}

	public static class CustomButton {
		public Button control;
	}

	public static class CustomMenuButton {
		public MenuButton control;
	}

	public static final class QuitApp extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_btn");
			control.disableProperty().bind(Properties.confirmingQuitAppProperty());
			control.setOnAction(event -> Buttons.quitApp_clicked(event));
			control.setOnKeyPressed(event -> Buttons.quitApp_keyPressed(event));
		}
	}

	public static final class QuitAppSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_save_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingQuitAppProperty()));
			control.setOnAction(event -> Buttons.quitAppSave_clicked(event));
			control.setOnKeyPressed(event -> Buttons.quitAppSave_keyPressed(event));
		}
	}

	public static final class QuitAppOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingQuitAppProperty()));
			control.setOnAction(event -> Buttons.quitAppOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.quitAppOK_keyPressed(event));
		}
	}

	public static final class CreateDefaultFile extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#create_default_file_btn");
			control.disableProperty().bind(Properties.createDefaultFilePossibleProperty().not());
			control.setOnAction(event -> Buttons.createDefaultFile_clicked(event));
			control.setOnKeyPressed(event -> Buttons.createDefaultFile_keyPressed(event));
		}
	}

	public static final class ReloadFile extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#reload_file_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.reloadFile_clicked(event));
			control.setOnKeyPressed(event -> Buttons.reloadFile_keyPressed(event));
		}
	}

	public static final class ReloadAllFiles extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#reload_all_files_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.reloadAllFiles_clicked(event));
			control.setOnKeyPressed(event -> Buttons.reloadAllFiles_keyPressed(event));
		}
	}

	public static final class FileSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_save_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedFileDirtyProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.fileSave_clicked(event));
			control.setOnKeyPressed(event -> Buttons.fileSave_keyPressed(event));
		}
	}

	public static final class FileCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_cancel_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.fileCancel_clicked(event));
			control.setOnKeyPressed(event -> Buttons.fileCancel_keyPressed(event));
		}
	}

	public static final class FileSaveOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_save_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.fileSaveOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.fileSaveOK_keyPressed(event));
		}
	}

	public static final class OpenInBrowser extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#open_in_browser_btn");
			control.disableProperty().bind(Bindings.not(Properties.urlExistsProperty()));
			control.setOnAction(event -> Buttons.openInBrowser_clicked(event));
			control.setOnKeyPressed(event -> Buttons.openInBrowser_keyPressed(event));
		}
	}

	public static final class UrlSearch extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_search_btn");
			control.setOnAction(event -> Buttons.urlSearch_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlSearch_keyPressed(event));
		}
	}

	public static final class URLCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_cancel_btn");
			control.disableProperty().bind(Buttons.getUrlCancelDisableBinding());
			control.setOnAction(event -> Buttons.urlCancel_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlCancel_keyPressed(event));
		}
	}

	public static final class URLDelete extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_delete_btn");
			control.disableProperty().bind(Buttons.getUrlDeleteDisableBinding());
			control.setOnAction(event -> Buttons.urlDelete_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlDelete_keyPressed(event));
		}
	}

	public static final class URLDeleteOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_delete_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.urlDeleteRequestedProperty()));
			control.setOnAction(event -> Buttons.urlDeleteOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlDeleteOK_keyPressed(event));
		}
	}

	public static final class URLCreateOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_create_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.urlModifiedProperty()));
			control.setOnAction(event -> Buttons.urlCreateOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlCreateOK_keyPressed(event));
		}
	}

	public static final class URLEditOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_edit_ok_btn");
			control.disableProperty().bind(Buttons.getEditOKDisableBinding());
			control.setOnAction(event -> Buttons.urlEditOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlEditOK_keyPressed(event));
		}
	}

	public static final class PreferencesCreateOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#create_preferences_file_ok_btn");
			control.disableProperty().bind(Buttons.getCreatePreferencesFileDisableBinding().not());
			control.setOnAction(event -> Buttons.createPreferencesFileOK_clicked(event));
		}
	}

	public static final class PreferencesCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#preferences_cancel_btn");
			control.disableProperty().bind(Bindings.and(Bindings.not(getCreatePreferencesFileDisableBinding()),Buttons.getPreferencesChangedBinding().not()));
			control.setOnAction(event -> Buttons.preferencesCancel_clicked(event));
		}
	}

	public static final class PreferencesSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#save_preferences_btn");
			control.disableProperty().bind(Buttons.getPreferencesChangedBinding().not());
			control.setOnAction(event -> Buttons.savePreferences_clicked(event));
		}
	}

	public static final class PreferencesReload extends CustomMenuButton {
		private void build ( final Parent root ) {
			control = (MenuButton) root.lookup("#reload_preferences_file_btn");

			final ObservableList<MenuItem> items = control.getItems();
			final String settingsBtnSelector = "reload_preferences_file_menu";
			final String cssBtnSelector = "reload_css_file_menu";
			final String fxmlBtnSelector = "reload_fxml_file_menu";

			for ( final MenuItem item: items ) {
				final String id = item.getId();
				if ( id != null )
					if ( id.equals(settingsBtnSelector) )
						item.setOnAction(event -> Buttons.reloadPreferencesFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> Buttons.reloadCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> Buttons.reloadFXMLFile_clicked(event));
			}
		}
	}

	public static final class PreferencesCreate extends CustomMenuButton {
		private void build ( final Parent root ) {
			control = (MenuButton) root.lookup("#create_preferences_file_btn");
			control.disableProperty().bind(Bindings.or(Buttons.getCreatePreferencesFileDisableBinding(),Buttons.getPreferencesChangedBinding()));

			final ObservableList<MenuItem> items = control.getItems();
			final String preferencesBtnSelector = "create_preferences_file_menu";
			final String cssBtnSelector = "create_css_file_menu";
			final String fxmlBtnSelector = "create_fxml_file_menu";

			for ( final MenuItem item: items ) {
				final String id = item.getId();
				if ( id != null )
					if ( id.equals(preferencesBtnSelector) )
						item.setOnAction(event -> Buttons.createPreferencesFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> Buttons.createCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> Buttons.createFXMLFile_clicked(event));
			}
		}
	}

}

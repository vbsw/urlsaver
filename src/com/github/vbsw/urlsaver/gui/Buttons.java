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

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.Preferences;
import com.github.vbsw.urlsaver.api.URLMeta;
import com.github.vbsw.urlsaver.db.DBTable;
import com.github.vbsw.urlsaver.db.DynArrayOfString;
import com.github.vbsw.urlsaver.pref.PreferencesConfig;
import com.github.vbsw.urlsaver.utility.Converter;
import com.github.vbsw.urlsaver.utility.Parser;
import com.github.vbsw.urlsaver.utility.WebBrowserAccess;

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

	protected StdGUI stdGUI;

	public final QuitApp quitApp = new QuitApp();
	public final QuitAppSave quitAppSave = new QuitAppSave();
	public final QuitAppOK quitAppOK = new QuitAppOK();
	public final CreateDefaultFile createDefaultFile = new CreateDefaultFile();
	public final ReloadFile reloadFile = new ReloadFile();
	public final ReloadAllFiles reloadAllFiles = new ReloadAllFiles();
	public final FileSave fileSave = new FileSave();
	public final FileCancel fileCancel = new FileCancel();
	public final FileSaveOK fileSaveOK = new FileSaveOK();
	public final OpenInBrowser openInBrowser = new OpenInBrowser();
	public final UrlSearch urlSearch = new UrlSearch();
	public final URLCancel urlCancel = new URLCancel();
	public final URLDelete urlDelete = new URLDelete();
	public final URLDeleteOK urlDeleteOK = new URLDeleteOK();
	public final URLCreateOK urlCreateOK = new URLCreateOK();
	public final URLEditOK urlEditOK = new URLEditOK();
	public final PreferencesCancel preferencesCancel = new PreferencesCancel();
	public final PreferencesSave preferencesSave = new PreferencesSave();
	public final PreferencesReload preferencesReload = new PreferencesReload();
	public final PreferencesCreate preferencesCreate = new PreferencesCreate();
	public final PreferencesOverwriteOK preferencesOverwriteOK = new PreferencesOverwriteOK();

	public Buttons ( StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
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
		preferencesCancel.build(root);
		preferencesSave.build(root);
		preferencesReload.build(root);
		preferencesCreate.build(root);
		preferencesOverwriteOK.build(root);
	}

	public void confirmURLEdit ( ) {
		final DBTable selectedDBTable = Global.db.getSelectedDBTable();
		final String urlTyped = Parser.trim(stdGUI.textFields.url.control.getText());
		final int urlIndex = selectedDBTable.getURLIndex(urlTyped);
		final DynArrayOfString tags = Converter.toDynArrayListSorted(stdGUI.textAreas.tags.control.getText());
		final String score = null; // TODO: set score
		final String urlDate = null; // TODO: set url date
		selectedDBTable.setTags(urlIndex,tags);
		selectedDBTable.setMetaData(urlIndex,URLMeta.SCORE,score);
		selectedDBTable.setMetaData(urlIndex,URLMeta.DATE,urlDate);

		final String tagsString = selectedDBTable.getTagsAsString(urlIndex);
		stdGUI.textAreas.tags.control.setText(tagsString);
		stdGUI.tableViews.urls.control.requestFocus();
		stdGUI.refreshFileSelection();
	}

	public void confirmURLDelete ( ) {
		final DBTable record = Global.db.getSelectedDBTable();
		final String url = stdGUI.tableViews.urls.control.getSelectionModel().getSelectedItem().getURL();
		final int urlIndex = record.getURLIndex(url);
		final int selectedIndex = stdGUI.tableViews.urls.control.getSelectionModel().getSelectedIndex();
		record.removeURL(urlIndex);
		stdGUI.tableViews.urls.control.getItems().remove(selectedIndex);
		if ( stdGUI.tableViews.urls.control.getItems().size() > selectedIndex )
			stdGUI.tableViews.urls.control.getSelectionModel().select(selectedIndex);
		else
			record.setSelectedURLIndex(-1);
		if ( stdGUI.tableViews.urls.control.getItems().isEmpty() )
			stdGUI.textFields.urlSearch.control.requestFocus();
		else
			stdGUI.tableViews.urls.control.requestFocus();
		stdGUI.refreshFileSelection();
	}

	public void confirmURLCreate ( ) {
		final DBTable selectedRecord = Global.db.getSelectedDBTable();
		final String url = Parser.trim(stdGUI.textFields.url.control.getText());
		final int urlIndex = selectedRecord.addUrl(url);
		final ArrayList<String> tags = Converter.toArrayList(stdGUI.textAreas.tags.control.getText());
		for ( final String tag: tags )
			selectedRecord.addTagToUrl(urlIndex,tag);
		final String tagsString = selectedRecord.getTagsAsString(urlIndex);
		stdGUI.textAreas.tags.control.setText(tagsString);
		stdGUI.tableViews.urls.control.requestFocus();
		stdGUI.properties.urlExistsProperty().set(true);
		stdGUI.properties.urlModifiedProperty().set(false);
		stdGUI.refreshFileSelection();
	}

	private void quitApp_clicked ( final ActionEvent event ) {
		stdGUI.quit();
	}

	private void quitApp_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			stdGUI.quit();
	}

	private void quitAppSave_clicked ( final ActionEvent event ) {
		stdGUI.urlsIO.saveAllFiles();
		stdGUI.quitUnconditionally();
	}

	private void quitAppSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			stdGUI.urlsIO.saveAllFiles();
			stdGUI.quitUnconditionally();
		}
	}

	private void quitAppOK_clicked ( final ActionEvent event ) {
		stdGUI.quitUnconditionally();
	}

	private void quitAppOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			stdGUI.quitUnconditionally();
	}

	private void reloadFile_clicked ( final ActionEvent event ) {
		final DBTable selectedRecord = Global.db.getSelectedDBTable();
		stdGUI.urlsIO.reloadFile(selectedRecord);
	}

	private void reloadFile_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			final DBTable selectedRecord = Global.db.getSelectedDBTable();
			stdGUI.urlsIO.reloadFile(selectedRecord);
		}
	}

	private void reloadAllFiles_clicked ( final ActionEvent event ) {
		stdGUI.urlsIO.reloadAllFiles();
	}

	private void reloadAllFiles_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			stdGUI.urlsIO.reloadAllFiles();
	}

	private void fileSave_clicked ( final ActionEvent event ) {
		stdGUI.properties.confirmingSaveProperty().set(true);
	}

	private void fileSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			stdGUI.properties.confirmingSaveProperty().set(true);
		}
	}

	private void fileCancel_clicked ( final ActionEvent event ) {
		stdGUI.properties.confirmingSaveProperty().set(false);
		stdGUI.listViews.files.control.requestFocus();
	}

	private void fileCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			fileCancel_clicked(null);
	}

	private void fileSaveOK_clicked ( final ActionEvent event ) {
		final DBTable selectedRecord = Global.db.getSelectedDBTable();
		stdGUI.urlsIO.saveFile(selectedRecord);
		stdGUI.refreshFileInfo();
		stdGUI.refreshTitle();
	}

	private void fileSaveOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			fileSaveOK_clicked(null);
	}

	private void openInBrowser_clicked ( final ActionEvent event ) {
		final String url = stdGUI.tableViews.urls.control.getSelectionModel().getSelectedItem().getURL();
		WebBrowserAccess.openURL(url);
	}

	private void openInBrowser_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			final String url = stdGUI.tableViews.urls.control.getSelectionModel().getSelectedItem().getURL();
			WebBrowserAccess.openURL(url);
		}
	}

	private void urlSearch_clicked ( final ActionEvent event ) {
		final DBTable record = Global.db.getSelectedDBTable();
		final String searchString = record.getURLsSearchString();
		final boolean searchByPrefix = Global.preferences.getBooleanValue(PreferencesConfig.SEARCH_BY_PREFIX_ID).getSaved();
		final DynArrayOfString searchTags = Converter.toDynArrayList(searchString);

		record.searchURLs(searchTags,searchByPrefix);
		stdGUI.tableViews.urls.showSearchResults();
		stdGUI.refreshURLsInfo();
		stdGUI.resetURLsProperties();
	}

	private void urlSearch_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			urlSearch_clicked(null);
		}
	}

	private void urlCancel_clicked ( final ActionEvent event ) {
		stdGUI.refreshURLsInfo();
		if ( stdGUI.tableViews.urls.control.getItems().isEmpty() )
			stdGUI.textFields.urlSearch.control.requestFocus();
		else
			stdGUI.tableViews.urls.control.requestFocus();
	}

	private void urlCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			stdGUI.buttons.urlCancel_clicked(null);
	}

	private void urlDelete_clicked ( final ActionEvent event ) {
		stdGUI.properties.urlDeleteRequestedProperty().set(true);
		stdGUI.buttons.urlCancel.control.requestFocus();
	}

	private void urlDelete_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			stdGUI.properties.urlDeleteRequestedProperty().set(true);
			stdGUI.buttons.urlCancel.control.requestFocus();
		}
	}

	private void urlDeleteOK_clicked ( final ActionEvent event ) {
		stdGUI.buttons.confirmURLDelete();
		stdGUI.properties.urlDeleteRequestedProperty().set(false);
	}

	private void urlDeleteOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			stdGUI.buttons.urlDelete_clicked(null);
	}

	private void urlCreateOK_clicked ( final ActionEvent event ) {
		stdGUI.buttons.confirmURLCreate();
		stdGUI.refreshTitle();
	}

	private void urlCreateOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			stdGUI.buttons.confirmURLCreate();
			stdGUI.refreshTitle();
		}
	}

	private void urlEditOK_clicked ( final ActionEvent event ) {
		confirmURLEdit();
		stdGUI.properties.urlExistsProperty().set(true);
		stdGUI.properties.urlTagsModifiedProperty().set(false);
	}

	private void urlEditOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			urlEditOK_clicked(null);
		}
	}

	private void preferencesOverwriteOK_clicked ( final ActionEvent event ) {
		if ( stdGUI.properties.confirmingCreatePreferencesProperty().get() ) {
			stdGUI.preferencesIO.overwritePreferencesFile();
			stdGUI.properties.confirmingCreatePreferencesProperty().set(false);
		} else if ( stdGUI.properties.confirmingCreateCSSProperty().get() ) {
			stdGUI.preferencesIO.overwriteCSSFile();
			stdGUI.properties.confirmingCreateCSSProperty().set(false);
		} else if ( stdGUI.properties.confirmingCreateFXMLProperty().get() ) {
			stdGUI.preferencesIO.overwriteFXMLFile();
			stdGUI.properties.confirmingCreateFXMLProperty().set(false);
		}
	}

	private void createPreferencesFileOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			preferencesOverwriteOK_clicked(null);
	}

	private void preferencesCancel_clicked ( final ActionEvent event ) {
		stdGUI.properties.confirmingCreatePreferencesProperty().set(false);
		stdGUI.properties.confirmingCreateCSSProperty().set(false);
		stdGUI.properties.confirmingCreateFXMLProperty().set(false);
		Global.preferences.resetModifiedValuesToSaved();
		stdGUI.refreshPreferencesView();
	}

	private void preferencesCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			preferencesCancel_clicked(null);
	}

	private void savePreferences_clicked ( final ActionEvent event ) {
		final Preferences preferences = Global.preferences;
		final String fileName = preferences.getPreferences().getSaved().getFileName().toString();
		preferences.savePreferences();
		if ( preferences.isCustomPreferencesSaved() ) {
			preferences.resetSavedToModified();
			stdGUI.properties.titleChangedProperty().set(false);
			stdGUI.properties.widthChangedProperty().set(false);
			stdGUI.properties.heightChangedProperty().set(false);
			stdGUI.properties.urlsFileExtensionChangedProperty().set(false);
			stdGUI.properties.urlsFileSelectChangedProperty().set(false);
			stdGUI.properties.maximizeChangedProperty().set(false);
			stdGUI.properties.loadAtStartChangedProperty().set(false);
			stdGUI.properties.byPrefixChangedProperty().set(false);
			stdGUI.textFields.title.setFontWeight(false);
			stdGUI.textFields.width.setFontWeight(false);
			stdGUI.textFields.height.setFontWeight(false);
			stdGUI.textFields.urlsFileExtension.setFontWeight(false);
			stdGUI.textFields.urlsFileSelect.setFontWeight(false);
			stdGUI.checkBoxes.maximize.setFontWeight(false);
			stdGUI.checkBoxes.urlsFileAutoloadAll.setFontWeight(false);
			stdGUI.checkBoxes.byPrefix.setFontWeight(false);
			stdGUI.logger.logSuccess("file saved (" + fileName + ")");
			stdGUI.textAreas.log.control.requestFocus();
			stdGUI.refreshCreateDefaultFileButton();
		} else {
			stdGUI.logger.logFailure("file not saved (" + fileName + ")");
			stdGUI.textAreas.log.control.requestFocus();
		}
	}

	private void savePreferences_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			savePreferences_clicked(null);
	}

	private void reloadPreferencesFile_clicked ( final ActionEvent event ) {
		Global.preferences.loadCustomPreferences();
		stdGUI.refreshPreferencesView();
		stdGUI.refreshTitle();
	}

	private void reloadCSSFile_clicked ( final ActionEvent event ) {
		stdGUI.reloadCSS();
	}

	private void reloadFXMLFile_clicked ( final ActionEvent event ) {
		final String logBackup = stdGUI.textAreas.log.control.getText();
		final int selectedIndex = stdGUI.listViews.files.control.getSelectionModel().getSelectedIndex();
		stdGUI.reloadFXML();
		stdGUI.refreshPreferencesView();
		stdGUI.tabPanes.top.preferences.select();
		stdGUI.textAreas.log.control.setText(logBackup);
		Platform.runLater(new Runnable() {
			@Override
			public void run ( ) {
				stdGUI.buttons.preferencesReload.control.requestFocus();
			}
		});
		stdGUI.listViews.files.control.getItems().addAll(Global.db.getTables());
		stdGUI.listViews.files.control.getSelectionModel().select(selectedIndex);
	}

	private void createPreferencesFile_clicked ( final ActionEvent event ) {
		if ( Global.preferences.getPreferences().getSaved().exists() ) {
			stdGUI.properties.confirmingCreatePreferencesProperty().set(true);
			stdGUI.buttons.preferencesCancel.control.requestFocus();
		} else {
			stdGUI.preferencesIO.createPreferencesFile();
		}
	}

	private void createCSSFile_clicked ( final ActionEvent event ) {
		stdGUI.preferencesIO.createCSSFile();
	}

	private void createFXMLFile_clicked ( final ActionEvent event ) {
		stdGUI.preferencesIO.createFXMLFile();
	}

	private BooleanBinding getCreatePreferencesFileDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(stdGUI.properties.confirmingCreatePreferencesProperty(),stdGUI.properties.confirmingCreateCSSProperty());
		binding = Bindings.or(binding,stdGUI.properties.confirmingCreateFXMLProperty());
		return binding;
	}

	private ObservableValue<? extends Boolean> getUrlCancelDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(stdGUI.properties.urlModifiedProperty(),stdGUI.properties.urlTagsModifiedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlDateModifiedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlDeleteRequestedProperty());
		binding = Bindings.not(binding);
		return binding;
	}

	private ObservableValue<? extends Boolean> getUrlDeleteDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.not(stdGUI.properties.urlExistsProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlDeleteRequestedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlModifiedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlTagsModifiedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlDateModifiedProperty());
		return binding;
	}

	private ObservableValue<? extends Boolean> getEditOKDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(stdGUI.properties.urlTagsModifiedProperty(),stdGUI.properties.urlDateModifiedProperty());
		binding = Bindings.and(binding,Bindings.not(stdGUI.properties.urlModifiedProperty()));
		binding = Bindings.not(binding);
		return binding;
	}

	private BooleanExpression getPreferencesChangedBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(stdGUI.properties.titleChangedProperty(),stdGUI.properties.widthChangedProperty());
		binding = Bindings.or(binding,stdGUI.properties.heightChangedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlsFileExtensionChangedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlsFileSelectChangedProperty());
		binding = Bindings.or(binding,stdGUI.properties.maximizeChangedProperty());
		binding = Bindings.or(binding,stdGUI.properties.loadAtStartChangedProperty());
		binding = Bindings.or(binding,stdGUI.properties.byPrefixChangedProperty());
		return binding;
	}

	public void createDefaultFile_clicked ( final ActionEvent event ) {
		final String defaultFileName = Global.preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
		final Path defaultFilePath = Global.resourceLoader.getLaunchSource().getDirectory().resolve(defaultFileName);
		try {
			Files.createFile(defaultFilePath);
			stdGUI.properties.createDefaultFilePossibleProperty().set(false);
			Global.db.initialize();
			stdGUI.urlsIO.initialize();
			final ArrayList<DBTable> records = Global.db.getTables();
			stdGUI.listViews.files.control.getItems().setAll(records);
			if ( records.size() > 0 ) {
				stdGUI.listViews.files.control.requestFocus();
				stdGUI.listViews.files.control.getSelectionModel().select(records.get(0));
				stdGUI.logger.logSuccess("default file created (" + defaultFileName + ")");
			}
		} catch ( final IOException e ) {
			stdGUI.logger.logFailure("could not create file (" + defaultFileName + ")");
			e.printStackTrace();
		}
	}

	public void createDefaultFile_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			stdGUI.buttons.createDefaultFile_clicked(null);
	}

	public class CustomButton {
		public Button control;
	}

	public class CustomMenuButton {
		public MenuButton control;
	}

	public final class QuitApp extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_btn");
			control.disableProperty().bind(stdGUI.properties.confirmingQuitProperty());
			control.setOnAction(event -> stdGUI.buttons.quitApp_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.quitApp_keyPressed(event));
		}
	}

	public final class QuitAppSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_save_btn");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.confirmingQuitProperty()));
			control.setOnAction(event -> stdGUI.buttons.quitAppSave_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.quitAppSave_keyPressed(event));
		}
	}

	public final class QuitAppOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_ok_btn");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.confirmingQuitProperty()));
			control.setOnAction(event -> stdGUI.buttons.quitAppOK_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.quitAppOK_keyPressed(event));
		}
	}

	public final class CreateDefaultFile extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#create_default_file_btn");
			control.disableProperty().bind(stdGUI.properties.createDefaultFilePossibleProperty().not());
			control.setOnAction(event -> stdGUI.buttons.createDefaultFile_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.createDefaultFile_keyPressed(event));
		}
	}

	public final class ReloadFile extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#reload_file_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(stdGUI.properties.selectedProperty()),stdGUI.properties.confirmingSaveProperty()));
			control.setOnAction(event -> stdGUI.buttons.reloadFile_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.reloadFile_keyPressed(event));
		}
	}

	public final class ReloadAllFiles extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#reload_all_files_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(stdGUI.properties.selectedProperty()),stdGUI.properties.confirmingSaveProperty()));
			control.setOnAction(event -> stdGUI.buttons.reloadAllFiles_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.reloadAllFiles_keyPressed(event));
		}
	}

	public final class FileSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_save_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(stdGUI.properties.selectedFileDirtyProperty()),stdGUI.properties.confirmingSaveProperty()));
			control.setOnAction(event -> stdGUI.buttons.fileSave_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.fileSave_keyPressed(event));
		}
	}

	public final class FileCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_cancel_btn");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.confirmingSaveProperty()));
			control.setOnAction(event -> stdGUI.buttons.fileCancel_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.fileCancel_keyPressed(event));
		}
	}

	public final class FileSaveOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_save_ok_btn");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.confirmingSaveProperty()));
			control.setOnAction(event -> stdGUI.buttons.fileSaveOK_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.fileSaveOK_keyPressed(event));
		}
	}

	public final class OpenInBrowser extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#open_in_browser_btn");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.urlExistsProperty()));
			control.setOnAction(event -> stdGUI.buttons.openInBrowser_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.openInBrowser_keyPressed(event));
		}
	}

	public final class UrlSearch extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_search_btn");
			control.setOnAction(event -> stdGUI.buttons.urlSearch_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.urlSearch_keyPressed(event));
		}
	}

	public final class URLCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_cancel_btn");
			control.disableProperty().bind(stdGUI.buttons.getUrlCancelDisableBinding());
			control.setOnAction(event -> stdGUI.buttons.urlCancel_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.urlCancel_keyPressed(event));
		}
	}

	public final class URLDelete extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_delete_btn");
			control.disableProperty().bind(stdGUI.buttons.getUrlDeleteDisableBinding());
			control.setOnAction(event -> stdGUI.buttons.urlDelete_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.urlDelete_keyPressed(event));
		}
	}

	public final class URLDeleteOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_delete_ok_btn");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.urlDeleteRequestedProperty()));
			control.setOnAction(event -> stdGUI.buttons.urlDeleteOK_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.urlDeleteOK_keyPressed(event));
		}
	}

	public final class URLCreateOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_create_ok_btn");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.urlModifiedProperty()));
			control.setOnAction(event -> stdGUI.buttons.urlCreateOK_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.urlCreateOK_keyPressed(event));
		}
	}

	public final class URLEditOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_edit_ok_btn");
			control.disableProperty().bind(stdGUI.buttons.getEditOKDisableBinding());
			control.setOnAction(event -> stdGUI.buttons.urlEditOK_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.urlEditOK_keyPressed(event));
		}
	}

	public final class PreferencesOverwriteOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#preferences_overwrite_ok_btn");
			control.disableProperty().bind(stdGUI.buttons.getCreatePreferencesFileDisableBinding().not());
			control.setOnAction(event -> stdGUI.buttons.preferencesOverwriteOK_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.createPreferencesFileOK_keyPressed(event));
		}
	}

	public final class PreferencesCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#preferences_cancel_btn");
			control.disableProperty().bind(Bindings.and(Bindings.not(getCreatePreferencesFileDisableBinding()),stdGUI.buttons.getPreferencesChangedBinding().not()));
			control.setOnAction(event -> stdGUI.buttons.preferencesCancel_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.preferencesCancel_keyPressed(event));
		}
	}

	public final class PreferencesSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#save_preferences_btn");
			control.disableProperty().bind(stdGUI.buttons.getPreferencesChangedBinding().not());
			control.setOnAction(event -> stdGUI.buttons.savePreferences_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.savePreferences_keyPressed(event));
		}
	}

	public final class PreferencesReload extends CustomMenuButton {
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
						item.setOnAction(event -> stdGUI.buttons.reloadPreferencesFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.reloadCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.reloadFXMLFile_clicked(event));
			}
		}
	}

	public final class PreferencesCreate extends CustomMenuButton {
		private void build ( final Parent root ) {
			control = (MenuButton) root.lookup("#create_preferences_file_btn");
			control.disableProperty().bind(Bindings.or(stdGUI.buttons.getCreatePreferencesFileDisableBinding(),stdGUI.buttons.getPreferencesChangedBinding()));

			final ObservableList<MenuItem> items = control.getItems();
			final String preferencesBtnSelector = "create_preferences_file_menu";
			final String cssBtnSelector = "create_css_file_menu";
			final String fxmlBtnSelector = "create_fxml_file_menu";

			for ( final MenuItem item: items ) {
				final String id = item.getId();
				if ( id != null )
					if ( id.equals(preferencesBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.createPreferencesFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.createCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.createFXMLFile_clicked(event));
			}
		}
	}

}

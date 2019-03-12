/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
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
import com.github.vbsw.urlsaver.api.ISettings;
import com.github.vbsw.urlsaver.api.URLMetaDefinition;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.DynArrayOfString;
import com.github.vbsw.urlsaver.db.URLsSearchResult;
import com.github.vbsw.urlsaver.utility.Converter;
import com.github.vbsw.urlsaver.utility.Parser;
import com.github.vbsw.urlsaver.utility.WebBrowserAccess;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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

	protected GUI stdGUI;

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
	public final SettingsCancel settingsCancel = new SettingsCancel();
	public final SettingsReload settingsReload = new SettingsReload();
	public final SettingsCreate settingsCreate = new SettingsCreate();
	public final SettingsOverwriteOK settingsOverwriteOK = new SettingsOverwriteOK();

	public Buttons ( GUI stdGUI ) {
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
		settingsCancel.build(root);
		settingsReload.build(root);
		settingsCreate.build(root);
		settingsOverwriteOK.build(root);
	}

	public void confirmURLEdit ( ) {
		final DBURLs selectedDBTable = Global.db.getSelectedURLs();
		final String urlTyped = Parser.trim(stdGUI.textFields.url.control.getText());
		final int urlIndex = selectedDBTable.getURLIndex(urlTyped);
		final DynArrayOfString tags = Converter.toDynArrayListSorted(stdGUI.textAreas.tags.control.getText());
		final String score = stdGUI.comboBoxes.score.getScoreAsString();
		final String urlDate = stdGUI.datePickers.urlDate.getDateAsString();
		selectedDBTable.setTags(urlIndex,tags);
		selectedDBTable.setMetaData(urlIndex,URLMetaDefinition.SCORE,score);
		selectedDBTable.setMetaData(urlIndex,URLMetaDefinition.DATE,urlDate);

		final String tagsString = selectedDBTable.getTagsAsString(urlIndex);
		final URLsSearchResult urlsResult = stdGUI.tableViews.urls.control.getSelectionModel().getSelectedItem();
		urlsResult.setScore(score);
		urlsResult.setDate(urlDate);
		stdGUI.textAreas.tags.control.setText(tagsString);
		stdGUI.tableViews.urls.control.requestFocus();
		stdGUI.tableViews.urls.control.refresh();
		stdGUI.refreshFileSelection();
	}

	public void confirmURLDelete ( ) {
		final DBURLs record = Global.db.getSelectedURLs();
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
		final DBURLs selectedRecord = Global.db.getSelectedURLs();
		final String url = Parser.trim(stdGUI.textFields.url.control.getText());
		final String score = stdGUI.comboBoxes.score.getScoreAsString();
		final String urlDate = stdGUI.datePickers.urlDate.getDateAsString();
		final int urlIndex = selectedRecord.addUrl(url);
		final ArrayList<String> tags = Converter.toArrayList(stdGUI.textAreas.tags.control.getText());
		for ( final String tag: tags )
			selectedRecord.addTagToUrl(urlIndex,tag);
		selectedRecord.setMetaData(urlIndex,URLMetaDefinition.SCORE,score);
		selectedRecord.setMetaData(urlIndex,URLMetaDefinition.DATE,urlDate);
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
		Global.urlsIO.saveAllFiles();
		stdGUI.quitUnconditionally();
	}

	private void quitAppSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			Global.urlsIO.saveAllFiles();
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
		Global.urlsIO.reloadSelectedFile();
	}

	private void reloadFile_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			Global.urlsIO.reloadSelectedFile();
		}
	}

	private void reloadAllFiles_clicked ( final ActionEvent event ) {
		Global.urlsIO.reloadAllFiles();
	}

	private void reloadAllFiles_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			Global.urlsIO.reloadAllFiles();
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
		Global.urlsIO.saveSelectedFile();
		if ( !Global.db.getSelectedURLs().isDirty() ) {
			stdGUI.properties.selectedFileDirtyProperty().setValue(false);
			stdGUI.properties.confirmingSaveProperty().setValue(false);
		}
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
		final DBURLs record = Global.db.getSelectedURLs();
		final String searchString = record.getURLsSearchString();
		final boolean searchByPrefix = Global.settings.getBooleanProperty(ISettings.Property.searchByPrefix).savedValue;
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
		stdGUI.properties.urlDateModifiedProperty().set(false);
		stdGUI.properties.urlScoreModifiedProperty().set(false);
	}

	private void urlEditOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			urlEditOK_clicked(null);
		}
	}

	private void settingsOverwriteOK_clicked ( final ActionEvent event ) {
		if ( stdGUI.properties.settingsModifiedProperty().get() ) {
			saveSettings_clicked(null);
		} else if ( stdGUI.properties.confirmingCreateSettingsProperty().get() ) {
			Global.settingsIO.overwriteSettingsFile();
			stdGUI.properties.confirmingCreateSettingsProperty().set(false);
			stdGUI.refreshSettingsView();
		} else if ( stdGUI.properties.confirmingCreateCSSProperty().get() ) {
			Global.settingsIO.overwriteCSSFile();
			stdGUI.properties.confirmingCreateCSSProperty().set(false);
			stdGUI.refreshSettingsView();
		} else if ( stdGUI.properties.confirmingCreateFXMLProperty().get() ) {
			Global.settingsIO.overwriteFXMLFile();
			stdGUI.properties.confirmingCreateFXMLProperty().set(false);
			stdGUI.refreshSettingsView();
		}
	}

	private void createSettingsFileOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			settingsOverwriteOK_clicked(null);
	}

	private void settingsCancel_clicked ( final ActionEvent event ) {
		stdGUI.properties.confirmingCreateSettingsProperty().set(false);
		stdGUI.properties.confirmingCreateCSSProperty().set(false);
		stdGUI.properties.confirmingCreateFXMLProperty().set(false);
		Global.settings.setModifiedValuesToSaved();
		stdGUI.refreshSettingsView();
	}

	private void settingsCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			settingsCancel_clicked(null);
	}

	private void saveSettings_clicked ( final ActionEvent event ) {
		final ISettings settings = Global.settings;
		final String fileName = settings.getSettingsResource().getFileName().toString();
		settings.saveSettings();
		stdGUI.properties.settingsModifiedProperty().set(false);
		if ( Global.properties.customSettingsSavedProperty().get() ) {
			Global.settings.setSavedValuesToModified();
			stdGUI.properties.titleChangedProperty().set(false);
			stdGUI.properties.widthChangedProperty().set(false);
			stdGUI.properties.heightChangedProperty().set(false);
			stdGUI.properties.urlsFileExtensionChangedProperty().set(false);
			stdGUI.properties.urlsFileSelectChangedProperty().set(false);
			stdGUI.properties.maximizeChangedProperty().set(false);
			stdGUI.properties.loadAtStartChangedProperty().set(false);
			stdGUI.properties.byPrefixChangedProperty().set(false);
			stdGUI.properties.settingsModifiedProperty().set(false);
			stdGUI.textFields.title.setFontWeight(false);
			stdGUI.textFields.width.setFontWeight(false);
			stdGUI.textFields.height.setFontWeight(false);
			stdGUI.textFields.urlsFileExtension.setFontWeight(false);
			stdGUI.textFields.urlsFileSelect.setFontWeight(false);
			stdGUI.checkBoxes.maximize.setFontWeight(false);
			stdGUI.checkBoxes.urlsFileAutoloadAll.setFontWeight(false);
			stdGUI.checkBoxes.byPrefix.setFontWeight(false);
			Global.logger.logSuccess("settings saved (" + fileName + ")");
			stdGUI.refreshSettingsView();
			stdGUI.textAreas.log.control.requestFocus();
			stdGUI.refreshCreateDefaultFileButton();
		} else {
			Global.logger.logFailure("settings not saved (" + fileName + ")");
			stdGUI.textAreas.log.control.requestFocus();
		}
	}

	/*
	 * private void saveSettings_keyPressed ( final KeyEvent event ) {
	 * final KeyCode keyCode = event.getCode();
	 * if ( keyCode == KeyCode.ENTER )
	 * saveSettings_clicked(null);
	 * }
	 */

	private void reloadSettingsFile_clicked ( final ActionEvent event ) {
		Global.settings.reloadSettings();
		stdGUI.refreshSettingsView();
		stdGUI.refreshTitle();
	}

	private void reloadCSSFile_clicked ( final ActionEvent event ) {
		stdGUI.reloadCSS();
	}

	private void reloadFXMLFile_clicked ( final ActionEvent event ) {
		final String logBackup = stdGUI.textAreas.log.control.getText();
		final int selectedIndex = stdGUI.listViews.files.control.getSelectionModel().getSelectedIndex();
		stdGUI.reloadFXML();
		stdGUI.refreshSettingsView();
		stdGUI.tabPanes.top.settings.select();
		stdGUI.textAreas.log.control.setText(logBackup);
		Platform.runLater(new Runnable() {
			@Override
			public void run ( ) {
				stdGUI.buttons.settingsReload.control.requestFocus();
			}
		});
		stdGUI.listViews.files.control.getItems().addAll(Global.db.getURLsList());
		stdGUI.listViews.files.control.getSelectionModel().select(selectedIndex);
	}

	private void createSettingsFile_clicked ( final ActionEvent event ) {
		if ( Global.settings.getSettingsResource().exists() ) {
			stdGUI.properties.confirmingCreateSettingsProperty().set(true);
			stdGUI.refreshSettingsView();
			stdGUI.buttons.settingsCancel.control.requestFocus();
		} else {
			Global.settingsIO.createSettingsFile();
		}
	}

	private void createCSSFile_clicked ( final ActionEvent event ) {
		if ( Global.settings.getCSSResource().exists() ) {
			stdGUI.properties.confirmingCreateCSSProperty().set(true);
			stdGUI.refreshSettingsView();
			stdGUI.buttons.settingsCancel.control.requestFocus();
		} else {
			Global.settingsIO.createCSSFile();
		}
	}

	private void createFXMLFile_clicked ( final ActionEvent event ) {
		if ( Global.settings.getFXMLResource().exists() ) {
			stdGUI.properties.confirmingCreateFXMLProperty().set(true);
			stdGUI.refreshSettingsView();
			stdGUI.buttons.settingsCancel.control.requestFocus();
		} else {
			Global.settingsIO.createFXMLFile();
		}
	}

	private BooleanBinding getCreateSettingsFileDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(stdGUI.properties.confirmingCreateSettingsProperty(),stdGUI.properties.confirmingCreateCSSProperty());
		binding = Bindings.or(binding,stdGUI.properties.confirmingCreateFXMLProperty());
		return binding;
	}

	private ObservableValue<? extends Boolean> getUrlCancelDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(stdGUI.properties.urlModifiedProperty(),stdGUI.properties.urlTagsModifiedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlDateModifiedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlScoreModifiedProperty());
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
		binding = Bindings.or(binding,stdGUI.properties.urlScoreModifiedProperty());
		return binding;
	}

	private ObservableValue<? extends Boolean> getEditOKDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(stdGUI.properties.urlTagsModifiedProperty(),stdGUI.properties.urlDateModifiedProperty());
		binding = Bindings.or(binding,stdGUI.properties.urlScoreModifiedProperty());
		binding = Bindings.and(binding,Bindings.not(stdGUI.properties.urlModifiedProperty()));
		binding = Bindings.not(binding);
		return binding;
	}

	public void createDefaultFile_clicked ( final ActionEvent event ) {
		final String urlsDefaultFileName = Global.settings.getStringProperty(ISettings.Property.urlsFileSelect).savedValue;
		final Path urlsDefaultFilePath = Global.pathProvider.getWorkingDirectory().resolve(urlsDefaultFileName);
		try {
			Files.createFile(urlsDefaultFilePath);
			stdGUI.properties.createDefaultFilePossibleProperty().set(false);
			Global.db.reload();
			Global.urlsIO.recreateServices();
			final ArrayList<DBURLs> dbURLsAll = Global.db.getURLsList();
			stdGUI.listViews.files.control.getItems().setAll(dbURLsAll);
			if ( dbURLsAll.size() > 0 ) {
				stdGUI.listViews.files.control.requestFocus();
				stdGUI.listViews.files.control.getSelectionModel().select(dbURLsAll.get(0));
				Global.logger.logSuccess("default file created (" + urlsDefaultFileName + ")");
			}
		} catch ( final IOException e ) {
			Global.logger.logFailure("could not create file (" + urlsDefaultFileName + ")");
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
			control.disableProperty().bind(Bindings.or(Bindings.not(stdGUI.properties.fileSelectedProperty()),stdGUI.properties.confirmingSaveProperty()));
			control.setOnAction(event -> stdGUI.buttons.reloadFile_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.reloadFile_keyPressed(event));
		}
	}

	public final class ReloadAllFiles extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#reload_all_files_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(stdGUI.properties.fileSelectedProperty()),stdGUI.properties.confirmingSaveProperty()));
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

	public final class SettingsOverwriteOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#settings_overwrite_ok_btn");
			control.disableProperty().bind(Bindings.and(getCreateSettingsFileDisableBinding().not(),stdGUI.properties.settingsModifiedProperty().not()));
			control.setOnAction(event -> stdGUI.buttons.settingsOverwriteOK_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.createSettingsFileOK_keyPressed(event));
		}
	}

	public final class SettingsCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#settings_cancel_btn");
			control.disableProperty().bind(Bindings.and(getCreateSettingsFileDisableBinding().not(),stdGUI.properties.settingsModifiedProperty().not()));
			control.setOnAction(event -> stdGUI.buttons.settingsCancel_clicked(event));
			control.setOnKeyPressed(event -> stdGUI.buttons.settingsCancel_keyPressed(event));
		}
	}

	/*
	 * public final class SettingsSave extends CustomButton {
	 * private void build ( final Parent root ) {
	 * control = (Button) root.lookup("#save_settings_btn");
	 * control.disableProperty().bind(stdGUI.buttons.
	 * getSettingsChangedBinding().not());
	 * control.setOnAction(event ->
	 * stdGUI.buttons.saveSettings_clicked(event));
	 * control.setOnKeyPressed(event ->
	 * stdGUI.buttons.saveSettings_keyPressed(event));
	 * }
	 * }
	 */

	public final class SettingsReload extends CustomMenuButton {
		private void build ( final Parent root ) {
			control = (MenuButton) root.lookup("#reload_settings_file_btn");

			final ObservableList<MenuItem> items = control.getItems();
			final String settingsBtnSelector = "reload_settings_file_menu";
			final String cssBtnSelector = "reload_css_file_menu";
			final String fxmlBtnSelector = "reload_fxml_file_menu";

			for ( final MenuItem item: items ) {
				final String id = item.getId();
				if ( id != null )
					if ( id.equals(settingsBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.reloadSettingsFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.reloadCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.reloadFXMLFile_clicked(event));
			}
		}
	}

	public final class SettingsCreate extends CustomMenuButton {
		private void build ( final Parent root ) {
			control = (MenuButton) root.lookup("#create_settings_file_btn");
			control.disableProperty().bind(Bindings.or(stdGUI.buttons.getCreateSettingsFileDisableBinding(),stdGUI.properties.settingsModifiedProperty()));

			final ObservableList<MenuItem> items = control.getItems();
			final String settingsBtnSelector = "create_settings_file_menu";
			final String cssBtnSelector = "create_css_file_menu";
			final String fxmlBtnSelector = "create_fxml_file_menu";

			for ( final MenuItem item: items ) {
				final String id = item.getId();
				if ( id != null )
					if ( id.equals(settingsBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.createSettingsFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.createCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> stdGUI.buttons.createFXMLFile_clicked(event));
			}
		}
	}

}

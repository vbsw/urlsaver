/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.util.ArrayList;

import javax.swing.text.html.CSS;

import com.github.vbsw.urlsaver.db.DB;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.CheckBoxes.CustomCheckBox;
import com.github.vbsw.urlsaver.io.FXMLIO;
import com.github.vbsw.urlsaver.pref.Preferences;
import com.github.vbsw.urlsaver.pref.PreferencesBooleanValue;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class GUI {

	public static final TextFields textFields = new TextFields();

	public static Scene scene;
	public static CSS css;

	private static void setWindowTitle ( final String title ) {
		final Stage stage = (Stage) GUI.scene.getWindow();
		stage.setTitle(title);
	}

	public static void initialize ( ) {
		final int windowWidth = Preferences.getWindowWidth().getSavedValue();
		final int windowHeight = Preferences.getWindowHeight().getSavedValue();
		final String urlsFileToSelect = Preferences.getURLsFileSelect().getSavedValue();
		final Parent rootStub = new AnchorPane();
		scene = new Scene(rootStub,windowWidth,windowHeight);
		scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> HotKeys.keyPressed(event));
		reloadFXML();
		reloadCSS();
		ListViews.files.control.getItems().addAll(DB.getRecords());
		ListViews.files.autoSelectRequested = Preferences.getURLsFileAutoloadAll().getSavedValue() && DB.getRecordByFileName(urlsFileToSelect) != null;
		Properties.createDefaultFilePossibleProperty().set(DB.getRecords().size() == 0);
	}

	public static void reloadFXML ( ) {
		final Parent root = FXMLIO.readFXML();
		Labels.build(root);
		Buttons.build(root);
		ListViews.build(root);
		TabPanes.build(root);
		TextAreas.build(root);
		TextFields.build(root);
		CheckBoxes.build(root);
		scene.setRoot(root);
	}

	public static void reloadCSS ( ) {
		final String cssURI;
		if ( Preferences.isCustomCSSFileAvailable() ) {
			cssURI = Preferences.getCSSPath().getSavedValue().toUri().toString();
			Preferences.setCustomCSSLoaded(true);
		} else {
			cssURI = Preferences.getCSSPath().getDefaultValue().toUri().toString();
			Preferences.setCustomCSSLoaded(false);
		}
		GUI.scene.getStylesheets().clear();
		GUI.scene.getStylesheets().add(cssURI);
	}

	public static void refreshTitle ( ) {
		final DBRecord selectedRecord = GUI.getCurrentDBRecord();
		final String windowTitle = TextGenerator.getWindowTitle(selectedRecord);
		GUI.setWindowTitle(windowTitle);
	}

	public static void refreshPreferencesView ( ) {
		final boolean disable = !Preferences.isCustomPreferencesLoaded();
		TextFields.title.control.setText(Preferences.getWindowTitle().getModifiedValue());
		TextFields.width.control.setText(Integer.toString((int) Preferences.getWindowWidth().getModifiedValue()));
		TextFields.height.control.setText(Integer.toString((int) Preferences.getWindowHeight().getModifiedValue()));
		TextFields.urlsFileExtension.control.setText(Preferences.getURLsFileExtension().getModifiedValue());
		TextFields.urlsFileSelect.control.setText(Preferences.getURLsFileSelect().getModifiedValue());
		refreshCheckBoxView(CheckBoxes.maximize,Preferences.getWindowMaximized());
		refreshCheckBoxView(CheckBoxes.urlsFileAutoloadAll,Preferences.getURLsFileAutoloadAll());
		refreshCheckBoxView(CheckBoxes.byPrefix,Preferences.getSearchByPrefix());
		TextFields.title.control.setDisable(disable);
		TextFields.width.control.setDisable(disable);
		TextFields.height.control.setDisable(disable);
		TextFields.urlsFileExtension.control.setDisable(disable);
		TextFields.urlsFileSelect.control.setDisable(disable);
		CheckBoxes.maximize.control.setDisable(disable);
		CheckBoxes.urlsFileAutoloadAll.control.setDisable(disable);
		CheckBoxes.byPrefix.control.setDisable(disable);
	}

	public static void selectDefaultFile ( ) {
		final String urlsFileSelect = Preferences.getURLsFileSelect().getModifiedValue();
		final DBRecord record = DB.getRecordByFileName(urlsFileSelect);
		if ( record != null ) {
			ListViews.files.control.requestFocus();
			ListViews.files.control.getSelectionModel().select(record);
		}
	}

	public static void refreshFileSelection ( ) {
		Properties.confirmingSaveProperty().set(false);
		GUI.refereshFileState();
		GUI.refreshFileInfo();
		GUI.refreshTitle();
	}

	public static void refereshFileState ( ) {
		final DBRecord record = GUI.getCurrentDBRecord();
		Properties.availableProperty().set(record.isLoaded());
		Properties.selectedFileDirtyProperty().setValue(record.isDirty());
		Properties.selectedProperty().set(record != null);
	}

	public static void refreshFileInfo ( ) {
		final DBRecord selectedRecord = GUI.getCurrentDBRecord();
		final String pathString = selectedRecord != null ? selectedRecord.getPathAsString() : "";
		final String urlsCountString = TextGenerator.getURLsCountLabel(selectedRecord);
		final String tagsCountString = TextGenerator.getTagsCountLabel(selectedRecord);
		final String fileSizeString = TextGenerator.getFileSizeLabel(selectedRecord);
		TextFields.fileName.control.setText(pathString);
		Labels.urlsCount.control.setText(urlsCountString);
		Labels.tagsCount.control.setText(tagsCountString);
		Labels.fileSize.control.setText(fileSizeString);
	}

	public static DBRecord getCurrentDBRecord ( ) {
		final DBRecord record = ListViews.files.control.getSelectionModel().getSelectedItem();
		return record;
	}

	private static void refreshCheckBoxView ( final CustomCheckBox customCheckBox, final PreferencesBooleanValue preferencesValue ) {
		if ( customCheckBox.control.isSelected() ) {
			if ( !preferencesValue.getModifiedValue() )
				customCheckBox.control.setSelected(preferencesValue.getModifiedValue());
			else if ( !preferencesValue.getSavedValue() )
				customCheckBox.setFontWeight(true);
		} else {
			if ( preferencesValue.getModifiedValue() )
				customCheckBox.control.setSelected(preferencesValue.getModifiedValue());
			else if ( preferencesValue.getSavedValue() )
				customCheckBox.setFontWeight(true);
		}
	}

	public static void refreshURLsView ( ) {
		final DBRecord record = GUI.getCurrentDBRecord();
		final String urlSearchString = record.getURLsSearchString();
		final ArrayList<String> urlsSearchResult = record.getURLsSearchResult();
		final int selectedURLIndex = record.getSelectedURLIndex();
		TextFields.urlSearch.control.setText(urlSearchString);
		ListViews.urls.control.getItems().setAll(urlsSearchResult);
		if ( selectedURLIndex >= 0 )
			ListViews.urls.control.getSelectionModel().select(selectedURLIndex);
	}

	public static void refreshURLsInfo ( ) {
		final String selectedURL = ListViews.urls.control.getSelectionModel().getSelectedItem();
		final DBRecord record = GUI.getCurrentDBRecord();
		final int selectedURLIndex;
		final String urlString;
		final String tagsString;

		if ( selectedURL != null ) {
			final int urlIndex = record.getURLIndex(selectedURL);
			selectedURLIndex = ListViews.urls.control.getSelectionModel().getSelectedIndex();
			urlString = selectedURL;
			tagsString = record.getTagsAsString(urlIndex);

		} else {
			selectedURLIndex = -1;
			urlString = "";
			tagsString = "";
		}
		record.setSelectedURLIndex(selectedURLIndex);
		TextFields.url.control.setText(urlString);
		TextAreas.tags.control.setText(tagsString);
	}

}

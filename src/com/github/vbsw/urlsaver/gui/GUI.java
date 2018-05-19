/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import javax.swing.text.html.CSS;

import com.github.vbsw.urlsaver.db.DB;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.CheckBoxes.CustomCheckBox;
import com.github.vbsw.urlsaver.pref.Preferences;
import com.github.vbsw.urlsaver.pref.PreferencesBooleanValue;

import javafx.scene.Parent;
import javafx.scene.Scene;
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
		reloadFXML();
		reloadCSS();
		ListViews.files.control.getItems().addAll(DB.getRecords());
		ListViews.files.autoSelectRequested = DB.getRecordByFileName(urlsFileToSelect) != null;
	}

	public static void reloadFXML ( ) {
		final Parent root = FXMLReader.getRoot();
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
		final DBRecord record = ListViews.files.control.getSelectionModel().getSelectedItem();
		final String windowTitleCustom = Preferences.getWindowTitle().getSavedValue();
		final String windowTitle;
		if ( record != null )
			if ( record.isDirty() )
				windowTitle = windowTitleCustom + " (" + record.getFileName() + " *)";
			else
				windowTitle = windowTitleCustom + " (" + record.getFileName() + ")";
		else
			windowTitle = windowTitleCustom;
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

	public static String getListLabel ( final DBRecord record, final int percentLoaded ) {
		final String listViewText;
		if ( percentLoaded < 0 )
			listViewText = record.getPathAsString() + "  0%";
		else if ( percentLoaded < 100 )
			listViewText = record.getPathAsString() + "  " + percentLoaded + "%";
		else
			listViewText = record.getPathAsString();
		return listViewText;
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

}

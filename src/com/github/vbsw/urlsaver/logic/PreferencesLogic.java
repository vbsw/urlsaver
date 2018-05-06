/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.logic;


import java.nio.file.Files;
import java.nio.file.Path;

import com.github.vbsw.urlsaver.JarFile;
import com.github.vbsw.urlsaver.gui.Buttons;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.gui.Logger;
import com.github.vbsw.urlsaver.pref.Preferences;
import com.github.vbsw.urlsaver.resources.ResourcesConfig;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


/**
 * @author Vitali Baumtrok
 */
public class PreferencesLogic {

	public static void refreshView ( ) {
		final boolean disable = !Preferences.isCustomPreferencesLoaded();
		GUI.textFields.title.setText(Preferences.getWindowTitle().getModifiedValue());
		GUI.textFields.width.setText(Integer.toString((int) Preferences.getWindowWidth().getModifiedValue()));
		GUI.textFields.height.setText(Integer.toString((int) Preferences.getWindowHeight().getModifiedValue()));
		GUI.textFields.fileExtension.setText(Preferences.getURLsFileExtension().getModifiedValue());
		GUI.textFields.defaultFile.setText(Preferences.getURLsFileSelect().getModifiedValue());
		GUI.checkBoxes.maximize.setSelected(Preferences.getWindowMaximized().getModifiedValue());
		GUI.checkBoxes.loadAtStart.setSelected(Preferences.getURLsFileAutoloadAll().getModifiedValue());
		GUI.checkBoxes.byPrefix.setSelected(Preferences.getSearchByPrefix().getModifiedValue());

		GUI.textFields.title.setDisable(disable);
		GUI.textFields.width.setDisable(disable);
		GUI.textFields.height.setDisable(disable);
		GUI.textFields.fileExtension.setDisable(disable);
		GUI.textFields.defaultFile.setDisable(disable);
		GUI.checkBoxes.maximize.setDisable(disable);
		GUI.checkBoxes.loadAtStart.setDisable(disable);
		GUI.checkBoxes.byPrefix.setDisable(disable);
	}

	public static void createPreferencesFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreatePreferencesProperty();
		final Path destFilePath = Preferences.getPreferencesPath().getSavedValue();
		PreferencesLogic.createFile(ResourcesConfig.DEFAULT_PREFERENCES_FILE_PATH,destFilePath,property);
	}

	public static void createCSSFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreateCSSProperty();
		final Path destFilePath = Preferences.getCSSPath().getSavedValue();
		PreferencesLogic.createFile(ResourcesConfig.DEFAULT_CSS_FILE_PATH,destFilePath,property);
	}

	public static void createFXMLFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreateFXMLProperty();
		final Path destFilePath = Preferences.getFXMLPath().getSavedValue();
		PreferencesLogic.createFile(ResourcesConfig.DEFAULT_FXML_FILE_PATH,destFilePath,property);
	}

	public static void overwritePreferencesFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreatePreferencesProperty();
		final Path destFilePath = Preferences.getPreferencesPath().getSavedValue();
		PreferencesLogic.overwriteFile(ResourcesConfig.DEFAULT_PREFERENCES_FILE_PATH,destFilePath,property);
	}

	public static void overwriteCSSFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreateCSSProperty();
		final Path destFilePath = Preferences.getCSSPath().getSavedValue();
		PreferencesLogic.overwriteFile(ResourcesConfig.DEFAULT_CSS_FILE_PATH,destFilePath,property);
	}

	public static void overwriteFXMLFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreateFXMLProperty();
		final Path destFilePath = Preferences.getFXMLPath().getSavedValue();
		PreferencesLogic.overwriteFile(ResourcesConfig.DEFAULT_FXML_FILE_PATH,destFilePath,property);
	}

	private static void createFile ( final String srcFilePathStr, final Path destFilePath, final SimpleBooleanProperty property ) {
		if ( Files.exists(destFilePath) ) {
			property.set(true);
			Buttons.preferencesCancel.control.requestFocus();
		} else {
			JarFile.copyResource(srcFilePathStr,destFilePath);
			if ( Files.exists(destFilePath) )
				Logger.logSuccess("file created (" + destFilePath.getFileName() + ")");
			else
				Logger.logFailure("file not created (" + destFilePath.getFileName() + ")");
		}
	}

	private static void overwriteFile ( final String srcFilePathStr, final Path destFilePath, final BooleanProperty property ) {
		JarFile.copyResource(srcFilePathStr,destFilePath);
		property.set(false);
		Logger.logSuccess("file overwritten (" + destFilePath.getFileName() + ")");
	}

}

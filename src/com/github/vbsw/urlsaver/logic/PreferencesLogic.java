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
import com.github.vbsw.urlsaver.gui.CheckBoxes;
import com.github.vbsw.urlsaver.gui.Logger;
import com.github.vbsw.urlsaver.gui.Properties;
import com.github.vbsw.urlsaver.gui.TextFields;
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
		TextFields.title.control.setText(Preferences.getWindowTitle().getModifiedValue());
		TextFields.width.control.setText(Integer.toString((int) Preferences.getWindowWidth().getModifiedValue()));
		TextFields.height.control.setText(Integer.toString((int) Preferences.getWindowHeight().getModifiedValue()));
		TextFields.fileExtension.control.setText(Preferences.getURLsFileExtension().getModifiedValue());
		TextFields.defaultFile.control.setText(Preferences.getURLsFileSelect().getModifiedValue());
		CheckBoxes.maximize.control.setSelected(Preferences.getWindowMaximized().getModifiedValue());
		CheckBoxes.loadAtStart.control.setSelected(Preferences.getURLsFileAutoloadAll().getModifiedValue());
		CheckBoxes.byPrefix.control.setSelected(Preferences.getSearchByPrefix().getModifiedValue());

		TextFields.title.control.setDisable(disable);
		TextFields.width.control.setDisable(disable);
		TextFields.height.control.setDisable(disable);
		TextFields.fileExtension.control.setDisable(disable);
		TextFields.defaultFile.control.setDisable(disable);
		CheckBoxes.maximize.control.setDisable(disable);
		CheckBoxes.loadAtStart.control.setDisable(disable);
		CheckBoxes.byPrefix.control.setDisable(disable);
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

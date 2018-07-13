/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.gui.Buttons;
import com.github.vbsw.urlsaver.gui.Logger;
import com.github.vbsw.urlsaver.gui.Properties;
import com.github.vbsw.urlsaver.pref.Preferences;
import com.github.vbsw.urlsaver.resources.Resource;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;


/**
 * @author Vitali Baumtrok
 */
public class PreferencesIO {

	public static void createPreferencesFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreatePreferencesProperty();
		final Resource srcResource = Preferences.getPreferencesResource().getDefaultValue();
		final Resource destResource = Preferences.getPreferencesResource().getSavedValue();
		PreferencesIO.createFile(srcResource,destResource,property);
	}

	public static void createCSSFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreateCSSProperty();
		final Resource srcResource = Preferences.getCSSResource().getDefaultValue();
		final Resource destResource = Preferences.getCSSResource().getSavedValue();
		PreferencesIO.createFile(srcResource,destResource,property);
	}

	public static void createFXMLFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreateFXMLProperty();
		final Resource srcResource = Preferences.getFXMLResource().getDefaultValue();
		final Resource destResource = Preferences.getFXMLResource().getSavedValue();
		PreferencesIO.createFile(srcResource,destResource,property);
	}

	public static void overwritePreferencesFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreatePreferencesProperty();
		final Resource srcResource = Preferences.getPreferencesResource().getDefaultValue();
		final Resource destResource = Preferences.getPreferencesResource().getSavedValue();
		PreferencesIO.overwriteFile(srcResource,destResource,property);
	}

	public static void overwriteCSSFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreateCSSProperty();
		final Resource srcResource = Preferences.getCSSResource().getDefaultValue();
		final Resource destResource = Preferences.getCSSResource().getSavedValue();
		PreferencesIO.overwriteFile(srcResource,destResource,property);
	}

	public static void overwriteFXMLFile ( ) {
		final SimpleBooleanProperty property = Properties.confirmingCreateFXMLProperty();
		final Resource srcResource = Preferences.getFXMLResource().getDefaultValue();
		final Resource destResource = Preferences.getFXMLResource().getSavedValue();
		PreferencesIO.overwriteFile(srcResource,destResource,property);
	}

	private static void createFile ( final Resource srcResource, final Resource destResource, final SimpleBooleanProperty property ) {
		if ( destResource.exists() ) {
			property.set(true);
			Buttons.preferencesCancel.control.requestFocus();
		} else {
			final Path destPath = destResource.getPath();
			srcResource.copyTo(destPath);
			if ( destResource.exists() )
				Logger.logSuccess("file created (" + destPath.getFileName() + ")");
			else
				Logger.logFailure("file not created (" + destPath.getFileName() + ")");
		}
	}

	private static void overwriteFile ( final Resource srcResource, final Resource destResource, final BooleanProperty property ) {
		final Path destPath = destResource.getPath();
		srcResource.copyTo(destPath);
		property.set(false);
		Logger.logSuccess("file overwritten (" + destPath.getFileName() + ")");
	}

}

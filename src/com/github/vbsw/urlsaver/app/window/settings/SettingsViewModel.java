
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.settings;


import java.nio.file.Files;
import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.resources.Resources;
import com.github.vbsw.urlsaver.utility.Jar;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;


/**
 * @author Vitali Baumtrok
 */
public class SettingsViewModel {
	final SimpleBooleanProperty confirmingCreateSettings = new SimpleBooleanProperty();
	final SimpleBooleanProperty confirmingCreateCSS = new SimpleBooleanProperty();
	final SimpleBooleanProperty confirmingCreateFXML = new SimpleBooleanProperty();
	final SimpleBooleanProperty settingsModified = new SimpleBooleanProperty();

	public SimpleBooleanProperty confirmingCreateSettingsProperty ( ) {
		return confirmingCreateSettings;
	}

	public SimpleBooleanProperty confirmingCreateCSSProperty ( ) {
		return confirmingCreateCSS;
	}

	public SimpleBooleanProperty confirmingCreateFXMLProperty ( ) {
		return confirmingCreateFXML;
	}

	public SimpleBooleanProperty settingsModifiedProperty ( ) {
		return settingsModified;
	}

	public void button_reloadSettingsFile_clicked ( final ActionEvent event ) {
		if ( App.files.isAnyDirty() == false ) {
			App.settings.loadCustomValues();
			App.settings.setAllToCustom();
			App.settings.updateView();

			App.files.initialize();
			App.scene.lv.files.getItems().clear();
			App.scene.lv.files.getItems().addAll(App.files.getPaths());
			App.files.selectDefault();
			App.files.processAutoLoad();
			App.scene.tp.top.getSelectionModel().select(App.scene.topTab.settings);

			App.window.updateTitle();

			if ( App.window.isMaximized() != App.settings.isCustomWindowMaximized() ) {
				App.window.setMaximized(App.settings.isCustomWindowMaximized());

			} else {
				App.window.setSize(App.settings.getCustomWindowWidth(),App.settings.getCustomWindowHeight());
			}

		} else {
			App.settings.logSuccess("can't load settings; urls not saved");
		}
	}

	public void button_reloadCSSFile_clicked ( final ActionEvent event ) {
		App.scene.loadCSS();
	}

	public void button_reloadFXMLFile_clicked ( final ActionEvent event ) {
		App.scene.loadFXML();
		App.scene.tp.top.getSelectionModel().select(App.scene.topTab.settings);
	}

	public void button_createSettingsFile_clicked ( final ActionEvent event ) {
		createFile(Resources.DEFAULT_SETTINGS_FILE_PATH,Resources.CUSTOM_SETTINGS_FILE_PATH,confirmingCreateSettings);
	}

	public void button_createCSSFile_clicked ( final ActionEvent event ) {
		createFile(Resources.DEFAULT_CSS_FILE_PATH,Resources.CUSTOM_CSS_FILE_PATH,confirmingCreateCSS);
	}

	public void button_createFXMLFile_clicked ( final ActionEvent event ) {
		createFile(Resources.DEFAULT_FXML_FILE_PATH,Resources.CUSTOM_FXML_FILE_PATH,confirmingCreateFXML);
	}

	public void button_createSettingsFileOK_clicked ( final ActionEvent event ) {
		if ( confirmingCreateSettings.get() ) {
			overwriteFile(Resources.DEFAULT_SETTINGS_FILE_PATH,Resources.CUSTOM_SETTINGS_FILE_PATH,confirmingCreateSettings);

		} else if ( confirmingCreateCSS.get() ) {
			overwriteFile(Resources.DEFAULT_CSS_FILE_PATH,Resources.CUSTOM_CSS_FILE_PATH,confirmingCreateCSS);

		} else if ( confirmingCreateFXML.get() ) {
			overwriteFile(Resources.DEFAULT_FXML_FILE_PATH,Resources.CUSTOM_FXML_FILE_PATH,confirmingCreateFXML);
		}
	}

	public void button_settingsCancel_clicked ( final ActionEvent event ) {
		App.settings.vm.confirmingCreateSettings.set(false);
		App.settings.vm.confirmingCreateCSS.set(false);
		App.settings.vm.confirmingCreateFXML.set(false);
		App.settings.vm.settingsModified.set(false);
	}

	private void createFile ( final String defaultFilePath, final String customFilePath, final BooleanProperty property ) {
		final Path filePath = Jar.getPath().resolve(customFilePath);

		if ( Files.exists(filePath) ) {
			property.set(true);
			App.scene.btn.settingsCancel.requestFocus();

		} else {
			Jar.copy(defaultFilePath,filePath);

			if ( Files.exists(filePath) ) {
				App.settings.logSuccess("file created (" + customFilePath + ")");

			} else {
				App.settings.logFailure("file not created (" + customFilePath + ")");
			}
		}
	}

	private void overwriteFile ( final String defaultFilePath, final String customFilePath, final BooleanProperty property ) {
		final Path filePath = Jar.getPath().resolve(customFilePath);

		Jar.copy(defaultFilePath,filePath);
		property.set(false);
		App.settings.logSuccess("file overwritten (" + customFilePath + ")");
	}

}


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
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;


/**
 * @author Vitali Baumtrok
 */
public class SettingsViewModel {
	final SimpleBooleanProperty confirmingCreateSettings = new SimpleBooleanProperty();
	final SimpleBooleanProperty confirmingCreateCSS = new SimpleBooleanProperty();
	final SimpleBooleanProperty confirmingCreateFXML = new SimpleBooleanProperty();
	final SimpleBooleanProperty settingsModified = new SimpleBooleanProperty();
	final SimpleBooleanProperty titleChanged = new SimpleBooleanProperty();
	final SimpleBooleanProperty widthChanged = new SimpleBooleanProperty();
	final SimpleBooleanProperty heightChanged = new SimpleBooleanProperty();
	final SimpleBooleanProperty fileExtensionChanged = new SimpleBooleanProperty();
	final SimpleBooleanProperty defaultFileChanged = new SimpleBooleanProperty();
	final SimpleBooleanProperty maximizeChanged = new SimpleBooleanProperty();
	final SimpleBooleanProperty loadAtStartChanged = new SimpleBooleanProperty();
	final SimpleBooleanProperty byPrefixChanged = new SimpleBooleanProperty();

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

	public SimpleBooleanProperty titleChangedProperty ( ) {
		return titleChanged;
	}

	public SimpleBooleanProperty widthChangedProperty ( ) {
		return widthChanged;
	}

	public SimpleBooleanProperty heightChangedProperty ( ) {
		return heightChanged;
	}

	public SimpleBooleanProperty fileExtensionChangedProperty ( ) {
		return fileExtensionChanged;
	}

	public SimpleBooleanProperty defaultFileChangedProperty ( ) {
		return defaultFileChanged;
	}

	public SimpleBooleanProperty maximizeChangedProperty ( ) {
		return maximizeChanged;
	}

	public SimpleBooleanProperty loadAtStartChangedProperty ( ) {
		return loadAtStartChanged;
	}

	public SimpleBooleanProperty byPrefixChangedProperty ( ) {
		return byPrefixChanged;
	}

	public void button_reloadSettingsFile_clicked ( final ActionEvent event ) {
		App.settings.loadCustomValues();
		App.settings.setAllToCustom();
		App.settings.updateView();

		// App.files.initialize();
		// App.scene.lv.files.getItems().clear();
		// App.scene.lv.files.getItems().addAll(App.files.getPaths());
		// App.files.selectDefault();
		// App.files.processAutoLoad();
		// App.scene.tp.top.getSelectionModel().select(App.scene.topTab.settings);

		App.window.updateTitle();

		if ( App.window.isMaximized() != App.settings.isCustomWindowMaximized() ) {
			App.window.setMaximized(App.settings.isCustomWindowMaximized());

		} else {
			App.window.setSize(App.settings.getCustomWindowWidth(),App.settings.getCustomWindowHeight());
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

	public void textField_title_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final boolean titlesEqual = App.settings.getWindowTitle().equals(newValue);

		App.settings.setCustomWindowTitle(newValue);

		titleChanged.set(!titlesEqual);
	}

	public void textField_width_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int width = (int) App.settings.getWindowWidth();
		final boolean widthsEqual = Integer.toString(width).equals(newValue);
		final int newWidth = Integer.parseInt(newValue);

		App.settings.setCustomWindowWidth(newWidth);

		widthChanged.set(!widthsEqual);
	}

	public void textField_height_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int height = (int) App.settings.getWindowHeight();
		final boolean heightsEqual = Integer.toString(height).equals(newValue);
		final int newHeight = Integer.parseInt(newValue);

		App.settings.setCustomWindowHeight(newHeight);

		heightChanged.set(!heightsEqual);
	}

	public void textField_fileExtension_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final boolean fileExtensionEqual = App.settings.getUrlsFileExtension().equals(newValue);

		App.settings.setCustomUrlsFileExtension(newValue);

		fileExtensionChanged.set(!fileExtensionEqual);
	}

	public void textField_defaultFile_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final boolean defaultFilesEqual = App.settings.getUrlsFileSelect().equals(newValue);

		App.settings.setCustomUrlsFileSelect(newValue);

		defaultFileChanged.set(!defaultFilesEqual);
	}

	public void textField_maximize_changed ( final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue ) {
		final boolean maximizesEqual = App.settings.isWindowMaximized() == newValue;

		App.settings.setCustomWindowMaximized(newValue);

		maximizeChanged.set(!maximizesEqual);
	}

	public void textField_loadAtStart_changed ( final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue ) {
		final boolean loadAtStartsEqual = App.settings.isAutoloadAll() == newValue;

		App.settings.setCustomAutoloadAll(newValue);

		maximizeChanged.set(!loadAtStartsEqual);
	}

	public void textField_byPrefix_changed ( final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue ) {
		final boolean byPrefixsEqual = App.settings.isSearchByPrefix() == newValue;

		App.settings.setCustomSearchByPrefix(newValue);

		maximizeChanged.set(!byPrefixsEqual);
	}

	public void button_saveSettings_clicked ( final ActionEvent event ) {
		final boolean success = App.settings.saveCustom();

		if ( success ) {
			App.settings.setAllToCustom();
			titleChanged.set(false);
			widthChanged.set(false);
			heightChanged.set(false);
			fileExtensionChanged.set(false);
			defaultFileChanged.set(false);
			maximizeChanged.set(false);
			loadAtStartChanged.set(false);
			byPrefixChanged.set(false);
			App.settings.logSuccess("file saved (" + Resources.CUSTOM_SETTINGS_FILE_PATH + ")");
			App.scene.ta.log.requestFocus();

		} else {
			App.settings.logFailure("file not saved (" + Resources.CUSTOM_SETTINGS_FILE_PATH + ")");
			App.scene.ta.log.requestFocus();
		}

	}

}

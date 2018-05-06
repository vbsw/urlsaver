/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.logic;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.App;
import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.db.DBFiles;
import com.github.vbsw.urlsaver.gui.Buttons;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.gui.Logger;
import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


/**
 * @author Vitali Baumtrok
 */
public class Callbacks {

	public static void button_quitApp_clicked ( final ActionEvent event ) {
		App.quit();
	}

	public static void button_quitApp_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			App.quit();
	}

	public static void button_quitAppSave_clicked ( final ActionEvent event ) {
		FilesLogic.saveAllFiles();
		App.quitUnconditionally();
	}

	public static void button_quitAppSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			FilesLogic.saveAllFiles();
			App.quitUnconditionally();
		}
	}

	public static void button_quitAppOK_clicked ( final ActionEvent event ) {
		App.quitUnconditionally();
	}

	public static void button_quitAppOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			App.quitUnconditionally();
	}

	public static void button_reloadFile_clicked ( final ActionEvent event ) {
		FilesLogic.reloadSelectedFile();
	}

	public static void button_reloadFile_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			FilesLogic.reloadSelectedFile();
	}

	public static void button_reloadAllFiles_clicked ( final ActionEvent event ) {
		FilesLogic.reloadAllFiles();
	}

	public static void button_reloadAllFiles_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			FilesLogic.reloadAllFiles();
	}

	public static void button_fileSave_clicked ( final ActionEvent event ) {
		FilesLogic.saveSelectedFile();
	}

	public static void button_fileSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			FilesLogic.saveSelectedFile();
	}

	public static void button_fileCancel_clicked ( final ActionEvent event ) {
		FilesLogic.cancelFileAction();
	}

	public static void button_fileCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			FilesLogic.cancelFileAction();
	}

	public static void button_fileSaveOK_clicked ( final ActionEvent event ) {
		FilesLogic.confirmSaveSelectedFile();
		Properties.selectedFileDirty.setValue(false);
		Properties.confirmingSave.setValue(false);
		WindowLogic.refreshTitle();
	}

	public static void button_fileSaveOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			FilesLogic.confirmSaveSelectedFile();
			Properties.selectedFileDirty.setValue(false);
			Properties.confirmingSave.setValue(false);
			WindowLogic.refreshTitle();
		}
	}

	public static void button_openInBrowser_clicked ( final ActionEvent event ) {
		WebBrowserLogic.openTypedUrl();
	}

	public static void button_openInBrowser_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			WebBrowserLogic.openTypedUrl();
	}

	public static void button_urlSearch_clicked ( final ActionEvent event ) {
		URLsLogic.updateSearchResult();
		URLsLogic.updateSearchResultListView();
		URLsLogic.setSelectedAsInfoView();
		URLsLogic.finalizeURLSearch();
	}

	public static void button_urlSearch_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.updateSearchResult();
			URLsLogic.updateSearchResultListView();
			URLsLogic.setSelectedAsInfoView();
			URLsLogic.finalizeURLSearch();
		}
	}

	public static void button_urlCancel_clicked ( final ActionEvent event ) {
		URLsLogic.setSelectedAsInfoView();
		URLsLogic.focusUrlsListOrSearchView();
		URLsLogic.finalizeURLCancel();
	}

	public static void button_urlCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.setSelectedAsInfoView();
			URLsLogic.focusUrlsListOrSearchView();
			URLsLogic.finalizeURLCancel();
		}
	}

	public static void button_urlDelete_clicked ( final ActionEvent event ) {
		Properties.urlDeleteRequested.set(true);
		Buttons.urlCancel.control.requestFocus();
	}

	public static void button_urlDelete_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			Properties.urlDeleteRequested.set(true);
			Buttons.urlCancel.control.requestFocus();
		}
	}

	public static void button_urlDeleteOK_clicked ( final ActionEvent event ) {
		URLsLogic.confirmURLDelete();
		URLsLogic.setSelectedAsInfoView();
		URLsLogic.finalizeURLDelete();
	}

	public static void button_urlDeleteOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.confirmURLDelete();
			URLsLogic.setSelectedAsInfoView();
			URLsLogic.finalizeURLDelete();
		}
	}

	public static void button_urlCreateOK_clicked ( final ActionEvent event ) {
		URLsLogic.confirmURLCreate();
		URLsLogic.finalizeURLCreate();
	}

	public static void button_urlCreateOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.confirmURLCreate();
			URLsLogic.finalizeURLCreate();
		}
	}

	public static void button_urlEditOK_clicked ( final ActionEvent event ) {
		URLsLogic.confirmEdit();
		Properties.urlExists.set(true);
		Properties.urlTagsModified.set(false);
	}

	public static void button_urlEditOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.confirmEdit();
			Properties.urlExists.set(true);
			Properties.urlTagsModified.set(false);
		}
	}

	public static void button_createPreferencesFileOK_clicked ( final ActionEvent event ) {
		if ( Properties.confirmingCreatePreferencesProperty().get() )
			PreferencesLogic.overwritePreferencesFile();
		else if ( Properties.confirmingCreateCSSProperty().get() )
			PreferencesLogic.overwriteCSSFile();
		else if ( Properties.confirmingCreateFXMLProperty().get() )
			PreferencesLogic.overwriteFXMLFile();
	}

	public static void button_settingsCancel_clicked ( final ActionEvent event ) {
		Properties.confirmingCreatePreferencesProperty().set(false);
		Properties.confirmingCreateCSSProperty().set(false);
		Properties.confirmingCreateFXMLProperty().set(false);
	}

	public static void button_savePreferences_clicked ( final ActionEvent event ) {
		final String fileName = Preferences.getPreferencesPath().getSavedValue().getFileName().toString();
		Preferences.savePreferences();

		if ( Preferences.isCustomPreferencesSaved() ) {
			Preferences.resetSavedValuesToModified();
			Properties.titleChangedProperty().set(false);
			Properties.widthChangedProperty().set(false);
			Properties.heightChangedProperty().set(false);
			Properties.fileExtensionChangedProperty().set(false);
			Properties.defaultFileChangedProperty().set(false);
			Properties.maximizeChangedProperty().set(false);
			Properties.loadAtStartChangedProperty().set(false);
			Properties.byPrefixChangedProperty().set(false);
			GUI.setFontWeight(GUI.textFields.title,false);
			GUI.setFontWeight(GUI.textFields.width,false);
			GUI.setFontWeight(GUI.textFields.height,false);
			GUI.setFontWeight(GUI.textFields.fileExtension,false);
			GUI.setFontWeight(GUI.textFields.defaultFile,false);
			GUI.setFontWeight(GUI.checkBoxes.maximize,false);
			GUI.setFontWeight(GUI.checkBoxes.loadAtStart,false);
			GUI.setFontWeight(GUI.checkBoxes.byPrefix,false);
			Logger.logSuccess("file saved (" + fileName + ")");
			GUI.textAreas.log.requestFocus();
		} else {
			Logger.logFailure("file not saved (" + fileName + ")");
			GUI.textAreas.log.requestFocus();
		}
	}

	public static void filePathListView_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			event.consume();
			/*
			 * TODO
			 * if ( App.scene.topTab.urls.isDisable() == false ) {
			 * App.scene.tp.top.getSelectionModel().select(App.scene.topTab.urls
			 * );
			 * App.scene.tf.urlSearch.requestFocus();
			 * }
			 */
		}
	}

	public static void filePathListViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 )
			FilesLogic.processFileSelection();
	}

	public static void filePathListViewItem_selected ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) {
		FilesLogic.processFileSelection();
		WindowLogic.refreshTitle();
	}

	public static void urlsListViewItem_selected ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO
	}

	public static void urlsListView_keyPressed ( final KeyEvent event ) {
		// TODO Auto-generated method stub
	}

	public static void topTab_selected ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) {
		// TODO Auto-generated method stub
	}

	public static void textField_tags_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO Auto-generated method stub
	}

	public static void textField_urlSearch_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO Auto-generated method stub
	}

	public static void textField_urlSearch_enterPressed ( final ActionEvent event ) {
		// TODO Auto-generated method stub
	}

	public static void textField_url_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO Auto-generated method stub
	}

	public static void textField_title_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final boolean titlesEqual = Preferences.getWindowTitle().getSavedValue().equals(trimmedValue);
		Preferences.getWindowTitle().setModifiedValue(newValue);
		GUI.setFontWeight(GUI.textFields.title,!titlesEqual);
		Properties.titleChangedProperty().set(!titlesEqual);
		Properties.preferencesModifiedProperty().set(!titlesEqual);
	}

	public static void textField_width_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		boolean valueChanged = false;
		try {
			final String trimmedValue = Parser.trim(newValue);
			final int parsedValue = Integer.parseInt(trimmedValue);
			valueChanged = parsedValue != Preferences.getWindowWidth().getModifiedValue();
			Preferences.getWindowWidth().setModifiedValue(parsedValue);
		} catch ( final NumberFormatException e ) {
			Preferences.getWindowWidth().resetModifiedValueToSaved();
		}
		Properties.widthChangedProperty().set(valueChanged);
		GUI.setFontWeight(GUI.textFields.width,valueChanged);
	}

	public static void textField_height_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO Auto-generated method stub
	}

	public static void textField_fileExtension_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO Auto-generated method stub
	}

	public static void textField_defaultFile_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO Auto-generated method stub
	}

	public static void topTab_urls_selected ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		// TODO Auto-generated method stub
	}

	public static void button_reloadPreferencesFile_clicked ( final ActionEvent event ) {
		Preferences.loadCustomPreferences();
		PreferencesLogic.refreshView();
		WindowLogic.refreshTitle();
	}

	public static void button_reloadCSSFile_clicked ( final ActionEvent event ) {
		GUI.reloadCSS();
	}

	public static void button_reloadFXMLFile_clicked ( final ActionEvent event ) {
		final String logBackup = GUI.textAreas.log.getText();
		final int selectedIndex = GUI.listViews.files.getSelectionModel().getSelectedIndex();
		GUI.reloadFXML();
		PreferencesLogic.refreshView();
		WindowLogic.selectPreferencesTab();
		GUI.textAreas.log.setText(logBackup);
		Platform.runLater(new Runnable() {
			@Override
			public void run ( ) {
				Buttons.reloadPreferencesFile.control.requestFocus();
			}
		});
		GUI.listViews.files.getItems().addAll(DBFiles.getPaths());
		GUI.listViews.files.getSelectionModel().select(selectedIndex);
	}

	public static void button_createPreferencesFile_clicked ( final ActionEvent event ) {
		PreferencesLogic.createPreferencesFile();
	}

	public static void button_createCSSFile_clicked ( final ActionEvent event ) {
		PreferencesLogic.createCSSFile();
	}

	public static void button_createFXMLFile_clicked ( final ActionEvent event ) {
		PreferencesLogic.createFXMLFile();
	}

}

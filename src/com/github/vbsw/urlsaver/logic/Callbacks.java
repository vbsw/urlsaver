/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.logic;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.App;
import com.github.vbsw.urlsaver.gui.GUI;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


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
		WindowLogic.updateTitle();
	}

	public static void button_fileSaveOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			FilesLogic.confirmSaveSelectedFile();
			Properties.selectedFileDirty.setValue(false);
			Properties.confirmingSave.setValue(false);
			WindowLogic.updateTitle();
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
		GUI.btn.urlCancel.requestFocus();
	}

	public static void button_urlDelete_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			Properties.urlDeleteRequested.set(true);
			GUI.btn.urlCancel.requestFocus();
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

	public static void button_createSettingsFileOK_clicked ( final ActionEvent event ) {
		// TODO Auto-generated method stub
	}

	public static void button_settingsCancel_clicked ( final ActionEvent event ) {
		// TODO Auto-generated method stub
	}

	public static void button_saveSettings_clicked ( final ActionEvent event ) {
		// TODO Auto-generated method stub
	}

	public static void filesListViewItem_selected ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) {
		// TODO Auto-generated method stub
	}

	public static void filesListView_keyPressed ( final KeyEvent event ) {
		// TODO Auto-generated method stub
	}

	public static void urlsListViewItem_selected ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
	}

	public static void textField_width_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		// TODO Auto-generated method stub
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

	public static void button_reloadSettingsFile_clicked ( final ActionEvent event ) {
		/*
		App.settings.loadCustomValues();
		App.settings.setAllToCustom();
		App.settings.updateView();

		// App.files.initialize();
		// App.scene.lv.files.getItems().clear();
		// App.scene.lv.files.getItems().addAll(App.files.getPaths());
		// App.files.selectDefault();
		// App.files.processAutoLoad();
		// App.scene.tp.top.getSelectionModel().select(App.scene.topTab.settings);

		WindowLogic.updateTitle();

		if ( App.window.isMaximized() != Preferences.getWindowMaximized().getCustomValue() ) {
			App.window.setMaximized(App.settings.isCustomWindowMaximized());

		} else {
			App.window.setSize(App.settings.getCustomWindowWidth(),App.settings.getCustomWindowHeight());
		}
		*/
	}

	public static void button_reloadCSSFile_clicked ( final ActionEvent event ) {
//		App.scene.loadCSS();
	}

	public static void button_reloadFXMLFile_clicked ( final ActionEvent event ) {
//		App.scene.loadFXML();
//		App.scene.tp.top.getSelectionModel().select(App.scene.topTab.settings);
	}

	public static void button_createSettingsFile_clicked ( final ActionEvent event ) {
//		createFile(ResourcesConfig.DEFAULT_SETTINGS_FILE_PATH,ResourcesConfig.CUSTOM_SETTINGS_FILE_PATH,confirmingCreateSettings);
	}

	public static void button_createCSSFile_clicked ( final ActionEvent event ) {
//		createFile(ResourcesConfig.DEFAULT_CSS_FILE_PATH,ResourcesConfig.CUSTOM_CSS_FILE_PATH,confirmingCreateCSS);
	}

	public static void button_createFXMLFile_clicked ( final ActionEvent event ) {
//		createFile(ResourcesConfig.DEFAULT_FXML_FILE_PATH,ResourcesConfig.CUSTOM_FXML_FILE_PATH,confirmingCreateFXML);
	}

}

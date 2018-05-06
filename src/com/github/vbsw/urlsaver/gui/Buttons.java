/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.App;
import com.github.vbsw.urlsaver.db.DBFiles;
import com.github.vbsw.urlsaver.pref.Preferences;
import com.github.vbsw.urlsaver.worker.FilesLogic;
import com.github.vbsw.urlsaver.worker.PreferencesLogic;
import com.github.vbsw.urlsaver.worker.URLsLogic;
import com.github.vbsw.urlsaver.worker.WebBrowserLogic;
import com.github.vbsw.urlsaver.worker.WindowLogic;
import com.github.vbsw.urlsaver.worker.Worker;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
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

	public static final QuitApp quitApp = new QuitApp();
	public static final QuitAppSave quitAppSave = new QuitAppSave();
	public static final QuitAppOK quitAppOK = new QuitAppOK();
	public static final ReloadFile reloadFile = new ReloadFile();
	public static final ReloadAllFiles reloadAllFiles = new ReloadAllFiles();
	public static final FileSave fileSave = new FileSave();
	public static final FileCancel fileCancel = new FileCancel();
	public static final FileSaveOK fileSaveOK = new FileSaveOK();
	public static final OpenInBrowser openInBrowser = new OpenInBrowser();
	public static final UrlSearch urlSearch = new UrlSearch();
	public static final URLCancel urlCancel = new URLCancel();
	public static final URLDelete urlDelete = new URLDelete();
	public static final URLDeleteOK urlDeleteOK = new URLDeleteOK();
	public static final URLCreateOK urlCreateOK = new URLCreateOK();
	public static final URLEditOK urlEditOK = new URLEditOK();
	public static final PreferencesCreateOK preferencesCreateOK = new PreferencesCreateOK();
	public static final PreferencesCancel preferencesCancel = new PreferencesCancel();
	public static final PreferencesSave preferencesSave = new PreferencesSave();
	public static final PreferencesReload preferencesReload = new PreferencesReload();
	public static final PreferencesCreate preferencesCreate = new PreferencesCreate();

	public static void build ( final Parent root ) {
		quitApp.build(root);
		quitAppSave.build(root);
		quitAppOK.build(root);
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
		preferencesCreateOK.build(root);
		preferencesCancel.build(root);
		preferencesSave.build(root);
		preferencesReload.build(root);
		preferencesCreate.build(root);
	}

	private static void quitApp_clicked ( final ActionEvent event ) {
		App.quit();
	}

	private static void quitApp_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			App.quit();
	}

	private static void quitAppSave_clicked ( final ActionEvent event ) {
		FilesLogic.saveAllFiles();
		App.quitUnconditionally();
	}

	private static void quitAppSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			FilesLogic.saveAllFiles();
			App.quitUnconditionally();
		}
	}

	private static void quitAppOK_clicked ( final ActionEvent event ) {
		App.quitUnconditionally();
	}

	private static void quitAppOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			App.quitUnconditionally();
	}

	private static void reloadFile_clicked ( final ActionEvent event ) {
		FilesLogic.reloadSelectedFile();
	}

	private static void reloadFile_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			FilesLogic.reloadSelectedFile();
	}

	private static void reloadAllFiles_clicked ( final ActionEvent event ) {
		Worker.reloadAllFiles();
	}

	private static void reloadAllFiles_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			Worker.reloadAllFiles();
	}

	private static void fileSave_clicked ( final ActionEvent event ) {
		FilesLogic.saveSelectedFile();
	}

	private static void fileSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			FilesLogic.saveSelectedFile();
	}

	private static void fileCancel_clicked ( final ActionEvent event ) {
		FilesLogic.cancelFileAction();
	}

	private static void fileCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			FilesLogic.cancelFileAction();
	}

	private static void fileSaveOK_clicked ( final ActionEvent event ) {
		FilesLogic.confirmSaveSelectedFile();
		Properties.selectedFileDirty.setValue(false);
		Properties.confirmingSave.setValue(false);
		WindowLogic.refreshTitle();
	}

	private static void fileSaveOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			FilesLogic.confirmSaveSelectedFile();
			Properties.selectedFileDirty.setValue(false);
			Properties.confirmingSave.setValue(false);
			WindowLogic.refreshTitle();
		}
	}

	private static void openInBrowser_clicked ( final ActionEvent event ) {
		WebBrowserLogic.openTypedUrl();
	}

	private static void openInBrowser_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER )
			WebBrowserLogic.openTypedUrl();
	}

	private static void urlSearch_clicked ( final ActionEvent event ) {
		URLsLogic.updateSearchResult();
		URLsLogic.updateSearchResultListView();
		URLsLogic.setSelectedAsInfoView();
		URLsLogic.finalizeURLSearch();
	}

	private static void urlSearch_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.updateSearchResult();
			URLsLogic.updateSearchResultListView();
			URLsLogic.setSelectedAsInfoView();
			URLsLogic.finalizeURLSearch();
		}
	}

	private static void urlCancel_clicked ( final ActionEvent event ) {
		URLsLogic.setSelectedAsInfoView();
		URLsLogic.focusUrlsListOrSearchView();
		URLsLogic.finalizeURLCancel();
	}

	private static void urlCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.setSelectedAsInfoView();
			URLsLogic.focusUrlsListOrSearchView();
			URLsLogic.finalizeURLCancel();
		}
	}

	private static void urlDelete_clicked ( final ActionEvent event ) {
		Properties.urlDeleteRequested.set(true);
		Buttons.urlCancel.control.requestFocus();
	}

	private static void urlDelete_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			Properties.urlDeleteRequested.set(true);
			Buttons.urlCancel.control.requestFocus();
		}
	}

	private static void urlDeleteOK_clicked ( final ActionEvent event ) {
		URLsLogic.confirmURLDelete();
		URLsLogic.setSelectedAsInfoView();
		URLsLogic.finalizeURLDelete();
	}

	private static void urlDeleteOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.confirmURLDelete();
			URLsLogic.setSelectedAsInfoView();
			URLsLogic.finalizeURLDelete();
		}
	}

	private static void urlCreateOK_clicked ( final ActionEvent event ) {
		URLsLogic.confirmURLCreate();
		URLsLogic.finalizeURLCreate();
	}

	private static void urlCreateOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.confirmURLCreate();
			URLsLogic.finalizeURLCreate();
		}
	}

	private static void urlEditOK_clicked ( final ActionEvent event ) {
		URLsLogic.confirmEdit();
		Properties.urlExists.set(true);
		Properties.urlTagsModified.set(false);
	}

	private static void urlEditOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();
		if ( keyCode == KeyCode.ENTER ) {
			URLsLogic.confirmEdit();
			Properties.urlExists.set(true);
			Properties.urlTagsModified.set(false);
		}
	}

	private static void createPreferencesFileOK_clicked ( final ActionEvent event ) {
		if ( Properties.confirmingCreatePreferencesProperty().get() )
			PreferencesLogic.overwritePreferencesFile();
		else if ( Properties.confirmingCreateCSSProperty().get() )
			PreferencesLogic.overwriteCSSFile();
		else if ( Properties.confirmingCreateFXMLProperty().get() )
			PreferencesLogic.overwriteFXMLFile();
	}

	private static void preferencesCancel_clicked ( final ActionEvent event ) {
		Properties.confirmingCreatePreferencesProperty().set(false);
		Properties.confirmingCreateCSSProperty().set(false);
		Properties.confirmingCreateFXMLProperty().set(false);
		Preferences.resetModifiedValuesToSaved();
		GUI.refreshPreferencesView();
	}

	private static void savePreferences_clicked ( final ActionEvent event ) {
		final String fileName = Preferences.getPreferencesPath().getSavedValue().getFileName().toString();
		Preferences.savePreferences();

		if ( Preferences.isCustomPreferencesSaved() ) {
			Preferences.resetSavedValuesToModified();
			Properties.titleChangedProperty().set(false);
			Properties.widthChangedProperty().set(false);
			Properties.heightChangedProperty().set(false);
			Properties.urlsFileExtensionChangedProperty().set(false);
			Properties.urlsFileSelectChangedProperty().set(false);
			Properties.maximizeChangedProperty().set(false);
			Properties.loadAtStartChangedProperty().set(false);
			Properties.byPrefixChangedProperty().set(false);
			TextFields.title.setFontWeight(false);
			TextFields.width.setFontWeight(false);
			TextFields.height.setFontWeight(false);
			TextFields.urlsFileExtension.setFontWeight(false);
			TextFields.urlsFileSelect.setFontWeight(false);
			CheckBoxes.maximize.setFontWeight(false);
			CheckBoxes.urlsFileAutoloadAll.setFontWeight(false);
			CheckBoxes.byPrefix.setFontWeight(false);
			Logger.logSuccess("file saved (" + fileName + ")");
			TextAreas.log.control.requestFocus();
		} else {
			Logger.logFailure("file not saved (" + fileName + ")");
			TextAreas.log.control.requestFocus();
		}
	}

	private static void reloadPreferencesFile_clicked ( final ActionEvent event ) {
		Preferences.loadCustomPreferences();
		GUI.refreshPreferencesView();
		WindowLogic.refreshTitle();
	}

	private static void reloadCSSFile_clicked ( final ActionEvent event ) {
		GUI.reloadCSS();
	}

	private static void reloadFXMLFile_clicked ( final ActionEvent event ) {
		final String logBackup = TextAreas.log.control.getText();
		final int selectedIndex = ListViews.files.control.getSelectionModel().getSelectedIndex();
		GUI.reloadFXML();
		GUI.refreshPreferencesView();
		WindowLogic.selectPreferencesTab();
		TextAreas.log.control.setText(logBackup);
		Platform.runLater(new Runnable() {
			@Override
			public void run ( ) {
				Buttons.preferencesReload.control.requestFocus();
			}
		});
		ListViews.files.control.getItems().addAll(DBFiles.getPaths());
		ListViews.files.control.getSelectionModel().select(selectedIndex);
	}

	private static void createPreferencesFile_clicked ( final ActionEvent event ) {
		PreferencesLogic.createPreferencesFile();
	}

	private static void createCSSFile_clicked ( final ActionEvent event ) {
		PreferencesLogic.createCSSFile();
	}

	private static void createFXMLFile_clicked ( final ActionEvent event ) {
		PreferencesLogic.createFXMLFile();
	}

	private static BooleanBinding getCreatePreferencesFileDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.confirmingCreatePreferencesProperty(),Properties.confirmingCreateCSSProperty());
		binding = Bindings.or(binding,Properties.confirmingCreateFXMLProperty());
		return binding;
	}

	private static ObservableValue<? extends Boolean> getUrlCancelDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.urlModifiedProperty(),Properties.urlTagsModifiedProperty());
		binding = Bindings.or(binding,Properties.urlDeleteRequestedProperty());
		binding = Bindings.not(binding);
		return binding;
	}

	private static ObservableValue<? extends Boolean> getUrlDeleteDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.not(Properties.urlExistsProperty());
		binding = Bindings.or(binding,Properties.urlDeleteRequestedProperty());
		binding = Bindings.or(binding,Properties.urlModifiedProperty());
		binding = Bindings.or(binding,Properties.urlTagsModifiedProperty());
		return binding;
	}

	private static ObservableValue<? extends Boolean> getEditOKDisableBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.not(Properties.urlModifiedProperty());
		binding = Bindings.and(binding,Properties.urlTagsModifiedProperty());
		binding = Bindings.not(binding);
		return binding;
	}

	private static BooleanExpression getPreferencesChangedBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.titleChangedProperty(),Properties.widthChangedProperty());
		binding = Bindings.or(binding,Properties.heightChangedProperty());
		binding = Bindings.or(binding,Properties.urlsFileExtensionChangedProperty());
		binding = Bindings.or(binding,Properties.urlsFileSelectChangedProperty());
		binding = Bindings.or(binding,Properties.maximizeChangedProperty());
		binding = Bindings.or(binding,Properties.loadAtStartChangedProperty());
		binding = Bindings.or(binding,Properties.byPrefixChangedProperty());
		return binding;
	}

	public static class CustomButton {
		public Button control;
	}

	public static class CustomMenuButton {
		public MenuButton control;
	}

	public static final class QuitApp extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_btn");
			control.disableProperty().bind(Properties.confirmingQuitAppProperty());
			control.setOnAction(event -> Buttons.quitApp_clicked(event));
			control.setOnKeyPressed(event -> Buttons.quitApp_keyPressed(event));
		}
	}

	public static final class QuitAppSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_save_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingQuitAppProperty()));
			control.setOnAction(event -> Buttons.quitAppSave_clicked(event));
			control.setOnKeyPressed(event -> Buttons.quitAppSave_keyPressed(event));
		}
	}

	public static final class QuitAppOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingQuitAppProperty()));
			control.setOnAction(event -> Buttons.quitAppOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.quitAppOK_keyPressed(event));
		}
	}

	public static final class ReloadFile extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#reload_file_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.reloadFile_clicked(event));
			control.setOnKeyPressed(event -> Buttons.reloadFile_keyPressed(event));
		}
	}

	public static final class ReloadAllFiles extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#reload_all_files_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.reloadAllFiles_clicked(event));
			control.setOnKeyPressed(event -> Buttons.reloadAllFiles_keyPressed(event));
		}
	}

	public static final class FileSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_save_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedFileDirtyProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.fileSave_clicked(event));
			control.setOnKeyPressed(event -> Buttons.fileSave_keyPressed(event));
		}
	}

	public static final class FileCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_cancel_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.fileCancel_clicked(event));
			control.setOnKeyPressed(event -> Buttons.fileCancel_keyPressed(event));
		}
	}

	public static final class FileSaveOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_save_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Buttons.fileSaveOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.fileSaveOK_keyPressed(event));
		}
	}

	public static final class OpenInBrowser extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#open_in_browser_btn");
			control.disableProperty().bind(Bindings.not(Properties.urlExistsProperty()));
			control.setOnAction(event -> Buttons.openInBrowser_clicked(event));
			control.setOnKeyPressed(event -> Buttons.openInBrowser_keyPressed(event));
		}
	}

	public static final class UrlSearch extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_search_btn");
			control.setOnAction(event -> Buttons.urlSearch_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlSearch_keyPressed(event));
		}
	}

	public static final class URLCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_cancel_btn");
			control.disableProperty().bind(Buttons.getUrlCancelDisableBinding());
			control.setOnAction(event -> Buttons.urlCancel_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlCancel_keyPressed(event));
		}
	}

	public static final class URLDelete extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_delete_btn");
			control.disableProperty().bind(Buttons.getUrlDeleteDisableBinding());
			control.setOnAction(event -> Buttons.urlDelete_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlDelete_keyPressed(event));
		}
	}

	public static final class URLDeleteOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_delete_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.urlDeleteRequestedProperty()));
			control.setOnAction(event -> Buttons.urlDeleteOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlDeleteOK_keyPressed(event));
		}
	}

	public static final class URLCreateOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_create_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.urlModifiedProperty()));
			control.setOnAction(event -> Buttons.urlCreateOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlCreateOK_keyPressed(event));
		}
	}

	public static final class URLEditOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_edit_ok_btn");
			control.disableProperty().bind(Buttons.getEditOKDisableBinding());
			control.setOnAction(event -> Buttons.urlEditOK_clicked(event));
			control.setOnKeyPressed(event -> Buttons.urlEditOK_keyPressed(event));
		}
	}

	public static final class PreferencesCreateOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#create_preferences_file_ok_btn");
			control.disableProperty().bind(Buttons.getCreatePreferencesFileDisableBinding().not());
			control.setOnAction(event -> Buttons.createPreferencesFileOK_clicked(event));
		}
	}

	public static final class PreferencesCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#preferences_cancel_btn");
			control.disableProperty().bind(Bindings.and(Bindings.not(getCreatePreferencesFileDisableBinding()),Buttons.getPreferencesChangedBinding().not()));
			control.setOnAction(event -> Buttons.preferencesCancel_clicked(event));
		}
	}

	public static final class PreferencesSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#save_preferences_btn");
			control.disableProperty().bind(Buttons.getPreferencesChangedBinding().not());
			control.setOnAction(event -> Buttons.savePreferences_clicked(event));
		}
	}

	public static final class PreferencesReload extends CustomMenuButton {
		private void build ( final Parent root ) {
			control = (MenuButton) root.lookup("#reload_preferences_file_btn");

			final ObservableList<MenuItem> items = control.getItems();
			final String settingsBtnSelector = "reload_preferences_file_menu";
			final String cssBtnSelector = "reload_css_file_menu";
			final String fxmlBtnSelector = "reload_fxml_file_menu";

			for ( final MenuItem item: items ) {
				final String id = item.getId();
				if ( id != null )
					if ( id.equals(settingsBtnSelector) )
						item.setOnAction(event -> Buttons.reloadPreferencesFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> Buttons.reloadCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> Buttons.reloadFXMLFile_clicked(event));
			}
		}
	}

	public static final class PreferencesCreate extends CustomMenuButton {
		private void build ( final Parent root ) {
			control = (MenuButton) root.lookup("#create_preferences_file_btn");
			control.disableProperty().bind(Bindings.or(Buttons.getCreatePreferencesFileDisableBinding(),Buttons.getPreferencesChangedBinding()));

			final ObservableList<MenuItem> items = control.getItems();
			final String preferencesBtnSelector = "create_preferences_file_menu";
			final String cssBtnSelector = "create_css_file_menu";
			final String fxmlBtnSelector = "create_fxml_file_menu";

			for ( final MenuItem item: items ) {
				final String id = item.getId();
				if ( id != null )
					if ( id.equals(preferencesBtnSelector) )
						item.setOnAction(event -> Buttons.createPreferencesFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> Buttons.createCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> Buttons.createFXMLFile_clicked(event));
			}
		}
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.logic.Callbacks;
import com.github.vbsw.urlsaver.logic.Properties;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;


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
	public static final CreatePreferencesFileOK createPreferencesFileOK = new CreatePreferencesFileOK();
	public static final PreferencesCancel preferencesCancel = new PreferencesCancel();
	public static final SavePreferences savePreferences = new SavePreferences();
	public static final ReloadPreferencesFile reloadPreferencesFile = new ReloadPreferencesFile();
	public static final CreatePreferencesFile createPreferencesFile = new CreatePreferencesFile();

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
		createPreferencesFileOK.build(root);
		preferencesCancel.build(root);
		savePreferences.build(root);
		reloadPreferencesFile.build(root);
		createPreferencesFile.build(root);
	}

	private static BooleanBinding getCreateSettingsFileDisableBinding ( ) {
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
		binding = Bindings.not(Properties.existsProperty());
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

	private static BooleanExpression getSettingsChangedBinding ( ) {
		BooleanBinding binding;
		binding = Bindings.or(Properties.titleChangedProperty(),Properties.widthChangedProperty());
		binding = Bindings.or(binding,Properties.heightChangedProperty());
		binding = Bindings.or(binding,Properties.fileExtensionChangedProperty());
		binding = Bindings.or(binding,Properties.defaultFileChangedProperty());
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
			control.disableProperty().bind(Properties.confirmQuitAppProperty());
			control.setOnAction(event -> Callbacks.button_quitApp_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_quitApp_keyPressed(event));
		}
	}

	public static final class QuitAppSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_save_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmQuitAppProperty()));
			control.setOnAction(event -> Callbacks.button_quitAppSave_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_quitAppSave_keyPressed(event));
		}
	}

	public static final class QuitAppOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmQuitAppProperty()));
			control.setOnAction(event -> Callbacks.button_quitAppOK_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_quitAppOK_keyPressed(event));
		}
	}

	public static final class ReloadFile extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#reload_file_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Callbacks.button_reloadFile_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_reloadFile_keyPressed(event));
		}
	}

	public static final class ReloadAllFiles extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#quit_app_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Callbacks.button_reloadAllFiles_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_reloadAllFiles_keyPressed(event));
		}
	}

	public static final class FileSave extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_save_btn");
			control.disableProperty().bind(Bindings.or(Bindings.not(Properties.selectedFileDirtyProperty()),Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Callbacks.button_fileSave_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_fileSave_keyPressed(event));
		}
	}

	public static final class FileCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_cancel_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Callbacks.button_fileCancel_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_fileCancel_keyPressed(event));
		}
	}

	public static final class FileSaveOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#file_save_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.confirmingSaveProperty()));
			control.setOnAction(event -> Callbacks.button_fileSaveOK_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_fileSaveOK_keyPressed(event));
		}
	}

	public static final class OpenInBrowser extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#open_in_browser_btn");
			control.disableProperty().bind(Bindings.not(Properties.existsProperty()));
			control.setOnAction(event -> Callbacks.button_openInBrowser_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_openInBrowser_keyPressed(event));
		}
	}

	public static final class UrlSearch extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_search_btn");
			control.setOnAction(event -> Callbacks.button_urlSearch_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_urlSearch_keyPressed(event));
		}
	}

	public static final class URLCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_cancel_btn");
			control.disableProperty().bind(Buttons.getUrlCancelDisableBinding());
			control.setOnAction(event -> Callbacks.button_urlCancel_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_urlCancel_keyPressed(event));
		}
	}

	public static final class URLDelete extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_delete_btn");
			control.disableProperty().bind(Buttons.getUrlDeleteDisableBinding());
			control.setOnAction(event -> Callbacks.button_urlDelete_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_urlDelete_keyPressed(event));
		}
	}

	public static final class URLDeleteOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_delete_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.urlDeleteRequestedProperty()));
			control.setOnAction(event -> Callbacks.button_urlDeleteOK_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_urlDeleteOK_keyPressed(event));
		}
	}

	public static final class URLCreateOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_create_ok_btn");
			control.disableProperty().bind(Bindings.not(Properties.urlModifiedProperty()));
			control.setOnAction(event -> Callbacks.button_urlCreateOK_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_urlCreateOK_keyPressed(event));
		}
	}

	public static final class URLEditOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#url_edit_ok_btn");
			control.disableProperty().bind(Buttons.getEditOKDisableBinding());
			control.setOnAction(event -> Callbacks.button_urlEditOK_clicked(event));
			control.setOnKeyPressed(event -> Callbacks.button_urlEditOK_keyPressed(event));
		}
	}

	public static final class CreatePreferencesFileOK extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#create_preferences_file_ok_btn");
			control.disableProperty().bind(Buttons.getCreateSettingsFileDisableBinding().not());
			control.setOnAction(event -> Callbacks.button_createPreferencesFileOK_clicked(event));
		}
	}

	public static final class PreferencesCancel extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#preferences_cancel_btn");
			control.disableProperty().bind(Bindings.not(getCreateSettingsFileDisableBinding ( )));
			control.setOnAction(event -> Callbacks.button_settingsCancel_clicked(event));
		}
	}

	public static final class SavePreferences extends CustomButton {
		private void build ( final Parent root ) {
			control = (Button) root.lookup("#save_preferences_btn");
			control.disableProperty().bind(Buttons.getSettingsChangedBinding().not());
			control.setOnAction(event -> Callbacks.button_savePreferences_clicked(event));
		}
	}

	public static final class ReloadPreferencesFile extends CustomMenuButton {
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
						item.setOnAction(event -> Callbacks.button_reloadPreferencesFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> Callbacks.button_reloadCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> Callbacks.button_reloadFXMLFile_clicked(event));
			}
		}
	}

	public static final class CreatePreferencesFile extends CustomMenuButton {
		private void build ( final Parent root ) {
			control = (MenuButton) root.lookup("#create_preferences_file_btn");
			control.disableProperty().bind(Bindings.or(Buttons.getCreateSettingsFileDisableBinding(),Buttons.getSettingsChangedBinding()));

			final ObservableList<MenuItem> items = control.getItems();
			final String preferencesBtnSelector = "create_preferences_file_menu";
			final String cssBtnSelector = "create_css_file_menu";
			final String fxmlBtnSelector = "create_fxml_file_menu";

			for ( final MenuItem item: items ) {
				final String id = item.getId();
				if ( id != null )
					if ( id.equals(preferencesBtnSelector) )
						item.setOnAction(event -> Callbacks.button_createPreferencesFile_clicked(event));
					else if ( id.equals(cssBtnSelector) )
						item.setOnAction(event -> Callbacks.button_createCSSFile_clicked(event));
					else if ( id.equals(fxmlBtnSelector) )
						item.setOnAction(event -> Callbacks.button_createFXMLFile_clicked(event));
			}
		}
	}

}

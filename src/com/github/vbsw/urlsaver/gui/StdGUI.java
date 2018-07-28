/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.util.ArrayList;

import javax.swing.text.html.CSS;

import com.github.vbsw.urlsaver.api.GUI;
import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.Preferences;
import com.github.vbsw.urlsaver.api.Preferences.PreferencesBooleanValue;
import com.github.vbsw.urlsaver.api.TextGenerator;
import com.github.vbsw.urlsaver.api.URLsIO;
import com.github.vbsw.urlsaver.api.ViewSelector;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.CheckBoxes.CustomCheckBox;
import com.github.vbsw.urlsaver.io.FXMLIO;
import com.github.vbsw.urlsaver.io.PreferencesIO;
import com.github.vbsw.urlsaver.io.StdURLsIO;
import com.github.vbsw.urlsaver.pref.PreferencesConfig;
import com.github.vbsw.urlsaver.utility.Parser;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 * @author Vitali Baumtrok
 */
public class StdGUI extends GUI {

	protected Global global;

	public StdURLsIO urlsIO;
	public FXMLIO fxmlIO;
	public StdLogger logger;
	public StdProperties properties;
	public PreferencesIO preferencesIO;

	public ViewSelector viewSelector;
	public HotKeys hotKeys;
	public Buttons buttons;
	public CheckBoxes checkBoxes;
	public TabPanes tabPanes;
	public ListViews listViews;
	public TextAreas textAreas;
	public TextFields textFields;

	public Scene scene;
	public CSS css;

	private void setWindowTitle ( final String title ) {
		final Stage stage = (Stage) scene.getWindow();
		stage.setTitle(title);
	}

	@Override
	public void initialize ( final Global global, final Stage primaryStage ) {
		this.global = global;
		this.properties = (StdProperties) global.getProperties();
		this.urlsIO = new StdURLsIO();
		this.fxmlIO = new FXMLIO();
		this.logger = new StdLogger();
		this.preferencesIO = new PreferencesIO();
		this.viewSelector = new StdViewSelector(this);
		this.hotKeys = new HotKeys(this);
		this.buttons = new Buttons(this);
		this.checkBoxes = new CheckBoxes(this);
		this.tabPanes = new TabPanes(this);
		this.listViews = new ListViews(this);
		this.textAreas = new TextAreas(this);
		this.textFields = new TextFields(this);

		fxmlIO.initialize(global.getPreferences());
		preferencesIO.initialize(global.getPreferences(),logger);
		logger.initialize(this);

		final int windowWidth = global.getPreferences().getIntValue(PreferencesConfig.WINDOW_WIDTH_ID).getSaved();
		final int windowHeight = global.getPreferences().getIntValue(PreferencesConfig.WINDOW_HEIGHT_ID).getSaved();
		final String urlsFileToSelect = global.getPreferences().getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
		final Parent rootStub = new AnchorPane();
		scene = new Scene(rootStub,windowWidth,windowHeight);
		scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> hotKeys.keyPressed(event));
		reloadFXML();
		reloadCSS();
		listViews.files.control.getItems().addAll(global.getDataBase().getRecords());
		listViews.files.autoSelectRequested = global.getPreferences().getBooleanValue(PreferencesConfig.URLS_FILE_AUTOLOAD_ALL_ID).getSaved() && global.getDataBase().getRecordByFileName(urlsFileToSelect) != null;
		refreshCreateDefaultFileButton();

		primaryStage.setOnCloseRequest(event -> onCloseRequest(event));
		primaryStage.setScene(scene);
		primaryStage.setMaximized(global.getPreferences().getBooleanValue(PreferencesConfig.WINDOW_MAXIMIZED_ID).getSaved());
		primaryStage.show();

		refreshPreferencesView();
		selectDefaultFile();

		urlsIO.initialize(global);
		urlsIO.autoLoad();
	}

	public void onCloseRequest ( final WindowEvent event ) {
		event.consume();
		quit();
	}

	@Override
	public void quit ( ) {
		if ( global.getDataBase().isSaved() ) {
			quitUnconditionally();
		} else {
			tabPanes.top.control.getSelectionModel().select(tabPanes.top.about.control);
			buttons.quitAppSave.control.requestFocus();
		}
	}

	public void quitUnconditionally ( ) {
		Platform.exit();
	}

	@Override
	public void refreshFileSelection ( ) {
		properties.confirmingSaveProperty().set(false);
		refereshFileState();
		refreshFileInfo();
		refreshTitle();
	}

	@Override
	public void refreshTitle ( ) {
		final DBRecord record = getCurrentDBRecord();
		final String windowTitle = createWindowTitle(record);
		setWindowTitle(windowTitle);
	}

	@Override
	public ViewSelector getViewSelector ( ) {
		return viewSelector;
	}

	public String createWindowTitle ( final DBRecord record ) {
		final String windowTitleCustom = global.getPreferences().getStringValue(PreferencesConfig.WINDOW_TITLE_ID).getSaved();
		final String windowTitle;
		if ( record != null )
			if ( record.isDirty() )
				windowTitle = windowTitleCustom + " (" + record.getFileName() + " *)";
			else
				windowTitle = windowTitleCustom + " (" + record.getFileName() + ")";
		else
			windowTitle = windowTitleCustom;
		return windowTitle;
	}

	public void refreshCreateDefaultFileButton ( ) {
		properties.createDefaultFilePossibleProperty().set(isDefaultFileAvailable());
	}

	private boolean isDefaultFileAvailable ( ) {
		final ArrayList<DBRecord> records = global.getDataBase().getRecords();
		final String defaultFileName = global.getPreferences().getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
		for ( final DBRecord record: records )
			if ( record.getFileName().equals(defaultFileName) )
				return false;
		return true;
	}

	public void reloadFXML ( ) {
		final Parent root = fxmlIO.readFXML();
		Labels.build(root);
		buttons.build(root);
		listViews.build(root);
		tabPanes.build(root);
		textAreas.build(root);
		textFields.build(root);
		checkBoxes.build(root);
		scene.setRoot(root);
	}

	public void reloadCSS ( ) {
		final Preferences preferences = global.getPreferences();
		final String cssURI;
		if ( preferences.getCSS().getSaved().exists() ) {
			cssURI = preferences.getCSS().getSaved().getURI().toString();
			preferences.setCustomCSSLoaded(true);
		} else {
			cssURI = preferences.getCSS().getDefault().getURI().toString();
			preferences.setCustomCSSLoaded(false);
		}
		scene.getStylesheets().clear();
		scene.getStylesheets().add(cssURI);
	}

	public void refreshPreferencesView ( ) {
		final Preferences preferences = global.getPreferences();
		final boolean disable = !preferences.isCustomPreferencesLoaded();
		textFields.title.control.setText(preferences.getStringValue(PreferencesConfig.WINDOW_TITLE_ID).getModified());
		textFields.width.control.setText(Integer.toString(preferences.getIntValue(PreferencesConfig.WINDOW_WIDTH_ID).getModified()));
		textFields.height.control.setText(Integer.toString(preferences.getIntValue(PreferencesConfig.WINDOW_HEIGHT_ID).getModified()));
		textFields.urlsFileExtension.control.setText(preferences.getStringValue(PreferencesConfig.URLS_FILE_EXTENSION_ID).getModified());
		textFields.urlsFileSelect.control.setText(preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getModified());
		refreshCheckBoxView(checkBoxes.maximize,preferences.getBooleanValue(PreferencesConfig.WINDOW_MAXIMIZED_ID));
		refreshCheckBoxView(checkBoxes.urlsFileAutoloadAll,preferences.getBooleanValue(PreferencesConfig.URLS_FILE_AUTOLOAD_ALL_ID));
		refreshCheckBoxView(checkBoxes.byPrefix,preferences.getBooleanValue(PreferencesConfig.SEARCH_BY_PREFIX_ID));
		textFields.title.control.setDisable(disable);
		textFields.width.control.setDisable(disable);
		textFields.height.control.setDisable(disable);
		textFields.urlsFileExtension.control.setDisable(disable);
		textFields.urlsFileSelect.control.setDisable(disable);
		checkBoxes.maximize.control.setDisable(disable);
		checkBoxes.urlsFileAutoloadAll.control.setDisable(disable);
		checkBoxes.byPrefix.control.setDisable(disable);
	}

	public void selectDefaultFile ( ) {
		final String urlsFileSelect = global.getPreferences().getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getModified();
		final DBRecord record = global.getDataBase().getRecordByFileName(urlsFileSelect);
		if ( record != null ) {
			listViews.files.control.requestFocus();
			listViews.files.control.getSelectionModel().select(record);
		}
	}

	public void refereshFileState ( ) {
		final DBRecord record = getCurrentDBRecord();
		if ( record != null ) {
			properties.availableProperty().set(record.isLoaded());
			properties.selectedFileDirtyProperty().setValue(record.isDirty());
			properties.selectedProperty().set(true);
		} else {
			properties.availableProperty().set(false);
			properties.selectedFileDirtyProperty().setValue(false);
			properties.selectedProperty().set(false);
		}
	}

	@Override
	public void refreshFileInfo ( ) {
		final DBRecord record = getCurrentDBRecord();
		final String pathString;
		final String urlsCountString;
		final String tagsCountString;
		final String fileSizeString;
		if ( record != null ) {
			final TextGenerator textGenerator = global.getTextGenerator();
			pathString = record.getPathAsString();
			urlsCountString = textGenerator.getURLsCountLabel(record);
			tagsCountString = textGenerator.getTagsCountLabel(record);
			fileSizeString = textGenerator.getFileSizeLabel(record);
		} else {
			pathString = "";
			urlsCountString = "";
			tagsCountString = "";
			fileSizeString = "";
		}
		textFields.fileName.control.setText(pathString);
		Labels.urlsCount.control.setText(urlsCountString);
		Labels.tagsCount.control.setText(tagsCountString);
		Labels.fileSize.control.setText(fileSizeString);
	}

	@Override
	public void refreshFileListView ( ) {
		listViews.files.control.refresh();
	}

	@Override
	public void recordLoaded ( final DBRecord record ) {
		final boolean fileIsAlreadySelected = (global.getDataBase().getSelectedRecord() == record);

		if ( listViews.files.autoSelectRequested ) {
			final String urlsFileSelect = global.getPreferences().getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
			final DBRecord recordToSelect = global.getDataBase().getRecordByFileName(urlsFileSelect);
			if ( recordToSelect == record ) {
				listViews.files.autoSelectRequested = false;
				if ( tabPanes.top.urls.control.disabledProperty().getValue() ) {
					listViews.files.control.getSelectionModel().select(recordToSelect);
					tabPanes.top.control.getSelectionModel().select(tabPanes.top.urls.control);
					textFields.urlSearch.control.requestFocus();
					refreshFileSelection();
				} else if ( fileIsAlreadySelected ) {
					refreshFileSelection();
				}
			} else if ( fileIsAlreadySelected ) {
				refreshFileSelection();
			}
		} else if ( fileIsAlreadySelected ) {
			refreshFileSelection();
		}
	}

	@Override
	public URLsIO getURLsIO ( ) {
		return urlsIO;
	}

	@Override
	public void confirmAny ( ) {
		if ( !buttons.urlDeleteOK.control.isDisable() )
			buttons.confirmURLDelete();
		else if ( !buttons.urlCreateOK.control.isDisable() )
			buttons.confirmURLCreate();
		else if ( !buttons.urlEditOK.control.isDisable() )
			buttons.confirmURLEdit();
		resetURLsProperties();
	}

	public DBRecord getCurrentDBRecord ( ) {
		final DBRecord record = listViews.files.control.getSelectionModel().getSelectedItem();
		return record;
	}

	private void refreshCheckBoxView ( final CustomCheckBox customCheckBox, final PreferencesBooleanValue preferencesValue ) {
		if ( customCheckBox.control.isSelected() ) {
			if ( !preferencesValue.getModified() )
				customCheckBox.control.setSelected(preferencesValue.getModified());
			else if ( !preferencesValue.getSaved() )
				customCheckBox.setFontWeight(true);
		} else {
			if ( preferencesValue.getModified() )
				customCheckBox.control.setSelected(preferencesValue.getModified());
			else if ( preferencesValue.getSaved() )
				customCheckBox.setFontWeight(true);
		}
	}

	public void refreshURLsView ( ) {
		final DBRecord record = getCurrentDBRecord();
		if ( record != null ) {
			final String urlSearchString = record.getURLsSearchString();
			final ArrayList<String> urlsSearchResult = record.getURLsSearchResult();
			final int selectedURLIndex = record.getSelectedURLIndex();
			textFields.urlSearch.control.setText(urlSearchString);
			listViews.urls.control.getItems().setAll(urlsSearchResult);
			if ( selectedURLIndex >= 0 )
				listViews.urls.control.getSelectionModel().select(selectedURLIndex);
		}
	}

	public void resetURLsProperties ( ) {
		final DBRecord selectedRecord = global.getDataBase().getSelectedRecord();
		final String urlTyped = Parser.trim(textFields.url.control.getText());
		final boolean urlExists = urlTyped.length() > 0 && selectedRecord.getURLIndex(urlTyped) >= 0;
		properties.urlExistsProperty().set(urlExists);
		properties.urlDeleteRequestedProperty().set(false);
		properties.urlModifiedProperty().set(false);
		properties.urlTagsModifiedProperty().set(false);
	}

	public void refreshURLsInfo ( ) {
		final DBRecord record = getCurrentDBRecord();
		if ( record != null ) {
			final String selectedURL = listViews.urls.control.getSelectionModel().getSelectedItem();
			final int selectedURLIndex;
			final String urlString;
			final String tagsString;
			if ( selectedURL != null ) {
				final int urlIndex = record.getURLIndex(selectedURL);
				selectedURLIndex = listViews.urls.control.getSelectionModel().getSelectedIndex();
				urlString = selectedURL;
				tagsString = record.getTagsAsString(urlIndex);

			} else {
				selectedURLIndex = -1;
				urlString = "";
				tagsString = "";
			}
			record.setSelectedURLIndex(selectedURLIndex);
			textFields.url.control.setText(urlString);
			textAreas.tags.control.setText(tagsString);
		}
	}

}

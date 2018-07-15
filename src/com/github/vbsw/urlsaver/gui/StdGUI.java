/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.util.ArrayList;

import javax.swing.text.html.CSS;

import com.github.vbsw.urlsaver.api.DataBase;
import com.github.vbsw.urlsaver.api.GUI;
import com.github.vbsw.urlsaver.api.Preferences;
import com.github.vbsw.urlsaver.api.Preferences.PreferencesBooleanValue;
import com.github.vbsw.urlsaver.api.ResourceLoader;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.CheckBoxes.CustomCheckBox;
import com.github.vbsw.urlsaver.io.FXMLIO;
import com.github.vbsw.urlsaver.io.PreferencesIO;
import com.github.vbsw.urlsaver.io.URLsIO;
import com.github.vbsw.urlsaver.pref.PreferencesConfig;
import com.github.vbsw.urlsaver.utility.Parser;
import com.github.vbsw.urlsaver.utility.TextGenerator;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class StdGUI extends GUI {

	protected ResourceLoader resourceLoader;
	protected Preferences preferences;
	protected DataBase db;

	public URLsIO urlsIO;
	public FXMLIO fxmlIO;
	public StdLogger logger;
	public StdProperties properties;
	public PreferencesIO preferencesIO;
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
	public void initialize ( final ResourceLoader resourceLoader, final Preferences preferences, final DataBase db, final Stage primaryStage ) {
		this.resourceLoader = resourceLoader;
		this.preferences = preferences;
		this.db = db;
		this.urlsIO = new URLsIO();
		this.fxmlIO = new FXMLIO();
		this.logger = new StdLogger();
		this.properties = new StdProperties();
		this.preferencesIO = new PreferencesIO();
		this.hotKeys = new HotKeys(this);
		this.buttons = new Buttons(this);
		this.checkBoxes = new CheckBoxes(this);
		this.tabPanes = new TabPanes(this);
		this.listViews = new ListViews(this);
		this.textAreas = new TextAreas(this);
		this.textFields = new TextFields(this);

		fxmlIO.initialize(preferences);
		preferencesIO.initialize(preferences,logger);
		logger.initialize(this);

		final int windowWidth = preferences.getIntValue(PreferencesConfig.WINDOW_WIDTH_ID).getSaved();
		final int windowHeight = preferences.getIntValue(PreferencesConfig.WINDOW_HEIGHT_ID).getSaved();
		final String urlsFileToSelect = preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
		final Parent rootStub = new AnchorPane();
		scene = new Scene(rootStub,windowWidth,windowHeight);
		scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> hotKeys.keyPressed(event));
		reloadFXML();
		reloadCSS();
		listViews.files.control.getItems().addAll(db.getRecords());
		listViews.files.autoSelectRequested = preferences.getBooleanValue(PreferencesConfig.URLS_FILE_AUTOLOAD_ALL_ID).getSaved() && db.getRecordByFileName(urlsFileToSelect) != null;
		refreshCreateDefaultFileButton();
		
		primaryStage.setOnCloseRequest(event -> hotKeys.onCloseRequest(event));
		primaryStage.setScene(scene);
		primaryStage.setMaximized(preferences.getBooleanValue(PreferencesConfig.WINDOW_MAXIMIZED_ID).getSaved());
		primaryStage.show();

		refreshPreferencesView();
		selectDefaultFile();

		urlsIO.initialize(preferences,db,this,properties);
		urlsIO.autoLoad();
	}

	@Override
	public void quit ( ) {
		if ( db.isSaved() ) {
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

	public String createWindowTitle ( final DBRecord record ) {
		final String windowTitleCustom = preferences.getStringValue(PreferencesConfig.WINDOW_TITLE_ID).getSaved();
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
		final ArrayList<DBRecord> records = db.getRecords();
		final String defaultFileName = preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
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
		final String urlsFileSelect = preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getModified();
		final DBRecord record = db.getRecordByFileName(urlsFileSelect);
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
			pathString = record.getPathAsString();
			urlsCountString = TextGenerator.getURLsCountLabel(record);
			tagsCountString = TextGenerator.getTagsCountLabel(record);
			fileSizeString = TextGenerator.getFileSizeLabel(record);
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
		final boolean fileIsAlreadySelected = (db.getSelectedRecord() == record);

		if ( listViews.files.autoSelectRequested ) {
			final String urlsFileSelect = preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
			final DBRecord recordToSelect = db.getRecordByFileName(urlsFileSelect);
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
		final DBRecord selectedRecord = db.getSelectedRecord();
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

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
import com.github.vbsw.urlsaver.db.DBTable;
import com.github.vbsw.urlsaver.db.URLsSearchResult;
import com.github.vbsw.urlsaver.gui.CheckBoxes.CustomCheckBox;
import com.github.vbsw.urlsaver.io.FXMLIO;
import com.github.vbsw.urlsaver.io.PreferencesIO;
import com.github.vbsw.urlsaver.io.StdURLsIO;
import com.github.vbsw.urlsaver.pref.PreferencesConfig;
import com.github.vbsw.urlsaver.utility.Parser;

import javafx.application.Platform;
import javafx.beans.Observable;
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
	public TableViews tableViews;
	public TextAreas textAreas;
	public TextFields textFields;
	public ComboBoxes comboBoxes;
	public DatePickers datePickers;

	public Scene scene;
	public CSS css;

	private void setWindowTitle ( final String title ) {
		final Stage stage = (Stage) scene.getWindow();
		stage.setTitle(title);
	}

	@Override
	public void initialize ( final Stage primaryStage ) {
		this.properties = (StdProperties) Global.properties;
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
		this.tableViews = new TableViews(this);
		this.textAreas = new TextAreas(this);
		this.textFields = new TextFields(this);
		this.comboBoxes = new ComboBoxes(this);
		this.datePickers = new DatePickers(this);

		fxmlIO.initialize(Global.preferences);
		preferencesIO.initialize(Global.preferences,logger);
		logger.initialize(this);

		final int windowWidth = Global.preferences.getIntValue(PreferencesConfig.WINDOW_WIDTH_ID).getSaved();
		final int windowHeight = Global.preferences.getIntValue(PreferencesConfig.WINDOW_HEIGHT_ID).getSaved();
		final String urlsFileToSelect = Global.preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
		final Parent rootStub = new AnchorPane();
		scene = new Scene(rootStub,windowWidth,windowHeight);
		scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> hotKeys.keyPressed(event));
		reloadFXML();
		reloadCSS();
		listViews.files.control.getItems().addAll(Global.db.getTables());
		listViews.files.autoSelectRequested = Global.preferences.getBooleanValue(PreferencesConfig.URLS_FILE_AUTOLOAD_ALL_ID).getSaved() && Global.db.getTableByFileName(urlsFileToSelect) != null;
		refreshCreateDefaultFileButton();

		primaryStage.setOnCloseRequest(event -> onCloseRequest(event));
		primaryStage.setScene(scene);
		primaryStage.setMaximized(Global.preferences.getBooleanValue(PreferencesConfig.WINDOW_MAXIMIZED_ID).getSaved());
		primaryStage.show();

		refreshPreferencesView();
		selectDefaultFile();

		urlsIO.initialize();
		urlsIO.autoLoad();
	}

	public void onCloseRequest ( final WindowEvent event ) {
		event.consume();
		quit();
	}

	@Override
	public void quit ( ) {
		if ( Global.db.isSaved() ) {
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
		final DBTable dbTable = Global.db.getSelectedDBTable();
		final String windowTitle = createWindowTitle(dbTable);
		setWindowTitle(windowTitle);
	}

	@Override
	public ViewSelector getViewSelector ( ) {
		return viewSelector;
	}

	public String createWindowTitle ( final DBTable record ) {
		final String windowTitleCustom = Global.preferences.getStringValue(PreferencesConfig.WINDOW_TITLE_ID).getSaved();
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
		final ArrayList<DBTable> records = Global.db.getTables();
		final String defaultFileName = Global.preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
		for ( final DBTable record: records )
			if ( record.getFileName().equals(defaultFileName) )
				return false;
		return true;
	}

	public void reloadFXML ( ) {
		final Parent root = fxmlIO.readFXML();
		Labels.build(root);
		buttons.build(root);
		listViews.build(root);
		tableViews.build(root);
		tabPanes.build(root);
		textAreas.build(root);
		textFields.build(root);
		comboBoxes.build(root);
		checkBoxes.build(root);
		datePickers.build(root);
		scene.setRoot(root);
	}

	public void reloadCSS ( ) {
		final Preferences preferences = Global.preferences;
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
		final Preferences preferences = Global.preferences;
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
		final String urlsFileSelect = Global.preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getModified();
		final DBTable record = Global.db.getTableByFileName(urlsFileSelect);
		if ( record != null ) {
			listViews.files.control.requestFocus();
			listViews.files.control.getSelectionModel().select(record);
		}
	}

	public void refereshFileState ( ) {
		final DBTable record = Global.db.getSelectedDBTable();
		if ( record != null ) {
			properties.urlsAvailableProperty().set(record.isLoaded());
			properties.selectedFileDirtyProperty().setValue(record.isDirty());
			properties.selectedProperty().set(true);
		} else {
			properties.urlsAvailableProperty().set(false);
			properties.selectedFileDirtyProperty().setValue(false);
			properties.selectedProperty().set(false);
		}
	}

	@Override
	public void refreshFileInfo ( ) {
		final DBTable record = Global.db.getSelectedDBTable();
		final String pathString;
		final String urlsCountString;
		final String tagsCountString;
		final String fileSizeString;
		if ( record != null ) {
			final TextGenerator textGenerator = Global.textGenerator;
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
	public void recordLoaded ( final DBTable record ) {
		final boolean fileIsAlreadySelected = (Global.db.getSelectedDBTable() == record);

		if ( listViews.files.autoSelectRequested ) {
			final String urlsFileSelect = Global.preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID).getSaved();
			final DBTable recordToSelect = Global.db.getTableByFileName(urlsFileSelect);
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

	public void clearURLsSearch ( final Observable observable, final Boolean oldURLsAvailable, final Boolean newURLsAvailable ) {
		if ( !newURLsAvailable ) {
			textFields.urlSearch.control.setText("");
			tableViews.urls.control.getItems().clear();
		}
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
		final DBTable record = Global.db.getSelectedDBTable();
		if ( record != null ) {
			final String urlSearchString = record.getURLsSearchString();
			final ArrayList<URLsSearchResult> urlsSearchResults = record.getURLsSearchResults();
			final int selectedURLIndex = record.getSelectedURLIndex();
			textFields.urlSearch.control.setText(urlSearchString);
			tableViews.urls.control.getItems().setAll(urlsSearchResults);
			if ( selectedURLIndex >= 0 )
				tableViews.urls.control.getSelectionModel().select(selectedURLIndex);
		}
	}

	public void resetURLsProperties ( ) {
		final DBTable selectedRecord = Global.db.getSelectedDBTable();
		final String urlTyped = Parser.trim(textFields.url.control.getText());
		final boolean urlExists = urlTyped.length() > 0 && selectedRecord.getURLIndex(urlTyped) >= 0;
		properties.urlExistsProperty().set(urlExists);
		properties.urlDeleteRequestedProperty().set(false);
		properties.urlModifiedProperty().set(false);
		properties.urlTagsModifiedProperty().set(false);
	}

	public void refreshURLsInfo ( ) {
		final DBTable record = Global.db.getSelectedDBTable();
		if ( record != null ) {
			final URLsSearchResult selectedURL = tableViews.urls.control.getSelectionModel().getSelectedItem();
			final int selectedURLIndex;
			final String urlString;
			final String tagsString;
			final String date;
			final String score;
			if ( selectedURL != null ) {
				final int urlIndex = record.getURLIndex(selectedURL.getURL());
				selectedURLIndex = tableViews.urls.control.getSelectionModel().getSelectedIndex();
				urlString = selectedURL.getURL();
				tagsString = record.getTagsAsString(urlIndex);
				date = selectedURL.getDate();
				score = selectedURL.getScore();
			} else {
				selectedURLIndex = -1;
				urlString = "";
				tagsString = "";
				date = "";
				score = "";
			}
			record.setSelectedURLIndex(selectedURLIndex);
			textFields.url.control.setText(urlString);
			textAreas.tags.control.setText(tagsString);
			datePickers.urlDate.setDate(date);
			comboBoxes.score.selectScore(score);
		}
	}

}

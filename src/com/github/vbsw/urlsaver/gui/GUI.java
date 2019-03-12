/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.api.IGUI;
import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.ILabelProvider;
import com.github.vbsw.urlsaver.api.ISettings;
import com.github.vbsw.urlsaver.api.IViewSelector;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.URLsSearchResult;
import com.github.vbsw.urlsaver.gui.CheckBoxes.CustomCheckBox;
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
public class GUI implements IGUI {

	public Properties properties;

	public IViewSelector viewSelector;
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

	private void setWindowTitle ( final String title ) {
		final Stage stage = (Stage) scene.getWindow();
		stage.setTitle(title);
	}

	public void initialize ( final Stage primaryStage ) {
		this.properties = (Properties) Global.properties;
		this.viewSelector = new ViewSelector(this);
		this.hotKeys = new HotKeys();
		this.buttons = new Buttons(this);
		this.checkBoxes = new CheckBoxes(this);
		this.tabPanes = new TabPanes(this);
		this.listViews = new ListViews(this);
		this.tableViews = new TableViews(this);
		this.textAreas = new TextAreas(this);
		this.textFields = new TextFields(this);
		this.comboBoxes = new ComboBoxes(this);
		this.datePickers = new DatePickers(this);

		final int windowWidth = Global.settings.getIntProperty(ISettings.Property.windowWidth).savedValue;
		final int windowHeight = Global.settings.getIntProperty(ISettings.Property.windowHeight).savedValue;
		final String urlsFileToSelect = Global.settings.getStringProperty(ISettings.Property.urlsFileSelect).savedValue;
		final Parent rootStub = new AnchorPane();
		scene = new Scene(rootStub,windowWidth,windowHeight);
		scene.addEventFilter(KeyEvent.KEY_PRESSED,event -> hotKeys.keyPressed(event));
		reloadFXML();
		reloadCSS();
		listViews.files.control.getItems().addAll(Global.db.getURLsList());
		listViews.files.autoSelectRequested = Global.settings.getBooleanProperty(ISettings.Property.urlsFileAutoLoadAll).savedValue && Global.db.getURLsByFileName(urlsFileToSelect) != null;
		refreshCreateDefaultFileButton();

		primaryStage.setOnCloseRequest(event -> onCloseRequest(event));
		primaryStage.setScene(scene);
		primaryStage.setMaximized(Global.settings.getBooleanProperty(ISettings.Property.windowMaximized).savedValue);
		primaryStage.show();

		refreshSettingsView();
		selectDefaultFile();

		Global.urlsIO.recreateServices();
		Global.urlsIO.loadDefault();
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
		final DBURLs dbTable = Global.db.getSelectedURLs();
		final String windowTitle = createWindowTitle(dbTable);
		setWindowTitle(windowTitle);
	}

	@Override
	public IViewSelector getViewSelector ( ) {
		return viewSelector;
	}

	public String createWindowTitle ( final DBURLs dbURLs ) {
		final String windowTitleCustom = Global.settings.getStringProperty(ISettings.Property.windowTitle).savedValue;
		final String windowTitle;
		if ( dbURLs != null )
			if ( dbURLs.isDirty() )
				windowTitle = windowTitleCustom + " (" + dbURLs.getFileName() + " *)";
			else
				windowTitle = windowTitleCustom + " (" + dbURLs.getFileName() + ")";
		else
			windowTitle = windowTitleCustom;
		return windowTitle;
	}

	public void refreshCreateDefaultFileButton ( ) {
		properties.createDefaultFilePossibleProperty().set(isDefaultFileAvailable());
	}

	private boolean isDefaultFileAvailable ( ) {
		final ArrayList<DBURLs> dbURLsAll = Global.db.getURLsList();
		final String defaultFileName = Global.settings.getStringProperty(ISettings.Property.urlsFileSelect).savedValue;
		for ( final DBURLs dbURLs: dbURLsAll )
			if ( dbURLs.getFileName().equals(defaultFileName) )
				return false;
		return true;
	}

	public void reloadFXML ( ) {
		final Parent root = Global.fxmlIO.readFXML();
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
		final ISettings settings = Global.settings;
		final String cssURI;
		if ( settings.getCSSResource().exists() ) {
			cssURI = settings.getCSSResource().getURI().toString();
			Global.properties.customCSSLoadedProperty().set(true);
		} else {
			cssURI = settings.getDefaultCSSResource().getURI().toString();
			Global.properties.customCSSLoadedProperty().set(false);
		}
		scene.getStylesheets().clear();
		scene.getStylesheets().add(cssURI);
	}

	public void refreshSettingsView ( ) {
		final ISettings settings = Global.settings;
		final boolean customSettingsLoaded = Global.properties.customSettingsLoadedProperty().get();
		final boolean disable = !customSettingsLoaded || properties.confirmingCreateSettingsProperty().get() || properties.confirmingCreateCSSProperty().get() || properties.confirmingCreateFXMLProperty().get();

		textFields.title.control.setText(settings.getStringProperty(ISettings.Property.windowTitle).modifiedValue);
		textFields.width.control.setText(Integer.toString(settings.getIntProperty(ISettings.Property.windowWidth).modifiedValue));
		textFields.height.control.setText(Integer.toString(settings.getIntProperty(ISettings.Property.windowHeight).modifiedValue));
		textFields.urlsFileExtension.control.setText(settings.getStringProperty(ISettings.Property.urlsFileExtension).modifiedValue);
		textFields.urlsFileSelect.control.setText(settings.getStringProperty(ISettings.Property.urlsFileSelect).modifiedValue);
		refreshCheckBoxView(checkBoxes.maximize,settings.getBooleanProperty(ISettings.Property.windowMaximized));
		refreshCheckBoxView(checkBoxes.urlsFileAutoloadAll,settings.getBooleanProperty(ISettings.Property.urlsFileAutoLoadAll));
		refreshCheckBoxView(checkBoxes.byPrefix,settings.getBooleanProperty(ISettings.Property.searchByPrefix));
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
		final String urlsFileSelect = Global.settings.getStringProperty(ISettings.Property.urlsFileSelect).modifiedValue;
		final DBURLs dbURLs = Global.db.getURLsByFileName(urlsFileSelect);
		if ( dbURLs != null ) {
			listViews.files.control.requestFocus();
			listViews.files.control.getSelectionModel().select(dbURLs);
		}
	}

	public void refereshFileState ( ) {
		final DBURLs dbURLs = Global.db.getSelectedURLs();
		if ( dbURLs != null ) {
			properties.urlsAvailableProperty().set(dbURLs.isLoaded());
			properties.selectedFileDirtyProperty().setValue(dbURLs.isDirty());
			properties.fileSelectedProperty().set(true);
		} else {
			properties.urlsAvailableProperty().set(false);
			properties.selectedFileDirtyProperty().setValue(false);
			properties.fileSelectedProperty().set(false);
		}
	}

	@Override
	public void refreshFileInfo ( ) {
		final DBURLs dbURLs = Global.db.getSelectedURLs();
		final String pathString;
		final String urlsCountString;
		final String tagsCountString;
		final String fileSizeString;
		if ( dbURLs != null ) {
			final ILabelProvider labelProvider = Global.labelProvider;
			pathString = dbURLs.getPathAsString();
			urlsCountString = labelProvider.getURLsCountLabel(dbURLs);
			tagsCountString = labelProvider.getTagsCountLabel(dbURLs);
			fileSizeString = labelProvider.getFileSizeLabel(dbURLs);
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
	public void dbURLsLoadingFinished ( final DBURLs dbURLs ) {
		final boolean fileIsAlreadySelected = (Global.db.getSelectedURLs() == dbURLs);

		if ( listViews.files.autoSelectRequested ) {
			final String urlsFileSelect = Global.settings.getStringProperty(ISettings.Property.urlsFileSelect).savedValue;
			final DBURLs dbURLsToSelect = Global.db.getURLsByFileName(urlsFileSelect);
			if ( dbURLsToSelect == dbURLs ) {
				listViews.files.autoSelectRequested = false;
				if ( tabPanes.top.urls.control.disabledProperty().getValue() ) {
					listViews.files.control.getSelectionModel().select(dbURLsToSelect);
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

	private void refreshCheckBoxView ( final CustomCheckBox customCheckBox, final ISettings.BooleanProperty booleanProperty ) {
		if ( customCheckBox.control.isSelected() ) {
			if ( !booleanProperty.modifiedValue )
				customCheckBox.control.setSelected(booleanProperty.modifiedValue);
			else if ( !booleanProperty.savedValue )
				customCheckBox.setFontWeight(true);
		} else {
			if ( booleanProperty.modifiedValue )
				customCheckBox.control.setSelected(booleanProperty.modifiedValue);
			else if ( booleanProperty.savedValue )
				customCheckBox.setFontWeight(true);
		}
	}

	public void refreshURLsView ( ) {
		final DBURLs dbURLs = Global.db.getSelectedURLs();
		if ( dbURLs != null ) {
			final String urlSearchString = dbURLs.getURLsSearchString();
			final ArrayList<URLsSearchResult> urlsSearchResults = dbURLs.getURLsSearchResults();
			final int selectedURLIndex = dbURLs.getSelectedURLIndex();
			textFields.urlSearch.control.setText(urlSearchString);
			tableViews.urls.control.getItems().setAll(urlsSearchResults);
			if ( selectedURLIndex >= 0 )
				tableViews.urls.control.getSelectionModel().select(selectedURLIndex);
		}
	}

	public void resetURLsProperties ( ) {
		final DBURLs selectedDBURLs = Global.db.getSelectedURLs();
		final String urlTyped = Parser.trim(textFields.url.control.getText());
		final boolean urlExists = urlTyped.length() > 0 && selectedDBURLs.getURLIndex(urlTyped) >= 0;
		properties.urlExistsProperty().set(urlExists);
		properties.urlDeleteRequestedProperty().set(false);
		properties.urlModifiedProperty().set(false);
		properties.urlTagsModifiedProperty().set(false);
		properties.urlDateModifiedProperty().set(false);
		properties.urlScoreModifiedProperty().set(false);
	}

	public void refreshURLsInfo ( ) {
		final DBURLs dbURLs = Global.db.getSelectedURLs();
		if ( dbURLs != null ) {
			final URLsSearchResult selectedURL = tableViews.urls.control.getSelectionModel().getSelectedItem();
			final int selectedURLIndex;
			final String urlString;
			final String tagsString;
			final String date;
			final String score;
			if ( selectedURL != null ) {
				final int urlIndex = dbURLs.getURLIndex(selectedURL.getURL());
				selectedURLIndex = tableViews.urls.control.getSelectionModel().getSelectedIndex();
				urlString = selectedURL.getURL();
				tagsString = dbURLs.getTagsAsString(urlIndex);
				date = selectedURL.getDate();
				score = selectedURL.getScore();
			} else {
				selectedURLIndex = -1;
				urlString = "";
				tagsString = "";
				date = "";
				score = "";
			}
			dbURLs.setSelectedURLIndex(selectedURLIndex);
			textFields.url.control.setText(urlString);
			textAreas.tags.control.setText(tagsString);
			datePickers.urlDate.setDate(date);
			comboBoxes.score.selectScore(score);
		}
	}

}

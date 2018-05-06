/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.nio.file.Path;

import javax.swing.text.html.CSS;

import com.github.vbsw.urlsaver.logic.Callbacks;
import com.github.vbsw.urlsaver.logic.Factories;
import com.github.vbsw.urlsaver.logic.Properties;
import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class GUI {

	public static final CheckBoxes checkBoxes = new CheckBoxes();
	public static final ListViews listViews = new ListViews();
	public static final TabPanes tabPanes = new TabPanes();
	public static final TextAreas textAreas = new TextAreas();
	public static final TextFields textFields = new TextFields();
	public static final TopTabs topTabs = new TopTabs();

	public static Scene scene;
	public static CSS css;

	public static void initialize ( ) {
		final int windowWidth = Preferences.getWindowWidth().getSavedValue();
		final int windowHeight = Preferences.getWindowHeight().getSavedValue();
		final Parent rootStub = new AnchorPane();

		scene = new Scene(rootStub,windowWidth,windowHeight);

		reloadFXML();
		reloadCSS();
	}

	public static void reloadFXML ( ) {
		final Parent root = FXMLReader.getRoot();
		Buttons.build(root);
		GUI.setElements(root);
		GUI.buildElements();
		scene.setRoot(root);
	}

	public static void reloadCSS ( ) {
		final String cssURI;
		if ( Preferences.isCustomCSSFileAvailable() ) {
			cssURI = Preferences.getCSSPath().getSavedValue().toUri().toString();
			Preferences.setCustomCSSLoaded(true);
		} else {
			cssURI = Preferences.getCSSPath().getDefaultValue().toUri().toString();
			Preferences.setCustomCSSLoaded(false);
		}
		GUI.scene.getStylesheets().clear();
		GUI.scene.getStylesheets().add(cssURI);
	}

	@SuppressWarnings ( "unchecked" )
	public static void setElements ( final Parent root ) {
		checkBoxes.maximize = (CheckBox) root.lookup("#preferences_maximize_cb");
		checkBoxes.loadAtStart = (CheckBox) root.lookup("#preferences_load_at_start_cb");
		checkBoxes.byPrefix = (CheckBox) root.lookup("#preferences_by_prefix_cb");

		listViews.files = (ListView<Path>) root.lookup("#file_list_view");
		listViews.urls = (ListView<String>) root.lookup("#url_list_view");

		tabPanes.top = (TabPane) root.lookup("#top_tab_pane");

		textAreas.tags = (TextArea) root.lookup("#tags_ta");
		textAreas.log = (TextArea) root.lookup("#log_ta");

		textFields.fileName = (TextField) root.lookup("#file_name_tf");
		textFields.urlSearch = (TextField) root.lookup("#url_search_tf");
		textFields.url = (TextField) root.lookup("#url_tf");
		textFields.title = (TextField) root.lookup("#preferences_title_tf");
		textFields.width = (TextField) root.lookup("#preferences_width_tf");
		textFields.height = (TextField) root.lookup("#preferences_height_tf");
		textFields.fileExtension = (TextField) root.lookup("#preferences_file_extension_tf");
		textFields.defaultFile = (TextField) root.lookup("#preferences_default_file_tf");

		topTabs.files = getTab(tabPanes.top,"files_tab");
		topTabs.urls = getTab(tabPanes.top,"urls_tab");
		topTabs.about = getTab(tabPanes.top,"about_tab");
		topTabs.preferences = getTab(tabPanes.top,"preferences_tab");
	}

	public static void buildElements ( ) {
		listViews.files.setCellFactory( ( ListView<Path> param ) -> Factories.filesCellFactory(param));
		listViews.files.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Path> observable, Path oldValue, Path newValue ) -> Callbacks.filePathListViewItem_selected(observable,oldValue,newValue));
		listViews.files.setOnKeyPressed(event -> Callbacks.filePathListView_keyPressed(event));
		listViews.urls.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.urlsListViewItem_selected(observable,oldValue,newValue));
		listViews.urls.setCellFactory( ( ListView<String> param ) -> Factories.urlsCellFactory(param));
		listViews.urls.setOnKeyPressed(event -> Callbacks.urlsListView_keyPressed(event));

		tabPanes.top.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) -> Callbacks.topTab_selected(observable,oldValue,newValue));

		textAreas.tags.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_tags_changed(observable,oldValue,newValue));

		textFields.urlSearch.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_urlSearch_changed(observable,oldValue,newValue));
		textFields.urlSearch.setOnAction(event -> Callbacks.textField_urlSearch_enterPressed(event));
		textFields.url.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_url_changed(observable,oldValue,newValue));
		textFields.title.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_title_changed(observable,oldValue,newValue));
		textFields.width.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_width_changed(observable,oldValue,newValue));
		textFields.height.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_height_changed(observable,oldValue,newValue));
		textFields.fileExtension.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_fileExtension_changed(observable,oldValue,newValue));
		textFields.defaultFile.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> Callbacks.textField_defaultFile_changed(observable,oldValue,newValue));

		topTabs.urls.disableProperty().bind(Bindings.not(Properties.availableProperty()));
		topTabs.urls.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> Callbacks.topTab_urls_selected(observable,oldValue,newValue));
	}

	public static void setWindowTitle ( final String title ) {
		final Stage stage = (Stage) GUI.scene.getWindow();
		stage.setTitle(title);
	}

	public static void setFontWeight ( final TextField textField, final boolean bold ) {
		final Font font = GUI.textFields.width.getFont();
		final FontWeight fontWeight = bold ? FontWeight.BOLD : FontWeight.NORMAL;
		final Font newFont = Font.font(font.getFamily(),fontWeight,font.getSize());

		textField.setFont(newFont);
	}

	public static void setFontWeight ( final CheckBox checkBox, final boolean bold ) {
		final String newStyle;
		if ( bold ) {
			newStyle = "-fx-font-weight:bold;";
		} else {
			newStyle = "-fx-font-weight:normal;";
		}
		checkBox.setStyle(newStyle);
	}

	private static Tab getTab ( final TabPane tabPane, final String tabId ) {
		for ( Tab tab: tabPane.getTabs() ) {
			final String id = tab.getId();
			if ( (id != null) && (id.equals(tabId)) )
				return tab;
		}
		return null;
	}

	public static final class CheckBoxes {

		public CheckBox maximize;
		public CheckBox loadAtStart;
		public CheckBox byPrefix;

	}

	public static final class ListViews {

		public ListView<Path> files;
		public ListView<String> urls;

	}

	public static final class TabPanes {

		public TabPane top;

	}

	public static final class TextAreas {

		public TextArea tags;
		public TextArea log;

	}

	public static final class TextFields {

		public TextField fileName;
		public TextField urlSearch;
		public TextField url;
		public TextField title;
		public TextField width;
		public TextField height;
		public TextField fileExtension;
		public TextField defaultFile;

	}

	public static final class TopTabs {

		public Tab files;
		public Tab urls;
		public Tab about;
		public Tab preferences;

	}

}

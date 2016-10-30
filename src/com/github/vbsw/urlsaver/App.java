/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2016 Vitali Baumtrok
 * 
 * This file is part of URL Saver.
 * 
 * URL Saver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * URL Saver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with URL Saver.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.vbsw.urlsaver;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.github.vbsw.urlsaver.TaggedWords.Word;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


/**
 * @author Vitali Baumtrok
 */
public class App extends Application {

	public static final Charset STRING_ENCODING = StandardCharsets.UTF_8;

	public static Arguments args;
	public static Settings settings;
	public static FileDataList fileDataList;
	public static Scene scene;
	public static Nodes nodes;
	public static boolean fileHasBeenSelected;

	public static void main ( final String[] args ) {
		App.args = new Arguments(args);
		App.args.printInfo();

		if ( App.args.hasNoInfo() ) {
			Application.launch(App.class,args);

		} else {
			Platform.exit();
		}
	}

	@Override
	public void start ( final Stage stage ) throws Exception {
		final Parent rootDummy = new AnchorPane();

		App.settings = new Settings();
		App.fileDataList = new FileDataList();
		App.scene = new Scene(rootDummy,App.settings.windowWidth,App.settings.windowHeight);
		App.nodes = new Nodes();

		App.fileDataList.readAll();
		App.recreateNodes();
		App.reloadCSS();

		stage.setOnCloseRequest(new Listener.CloseWindow());
		stage.setScene(scene);
		stage.setTitle(settings.windowTitle);
		stage.setMaximized(settings.windowMaximized);
		stage.show();

		if ( App.settings.fileAutoLoad ) {
			App.reloadFiles();
		}

		App.settings.decorationWidth = stage.getWidth() - scene.getWidth();
		App.settings.decorationHeight = stage.getHeight() - scene.getHeight();
	}

	public static void recreateNodes ( ) {
		App.nodes.recreate();
		App.scene.setRoot(App.nodes.root);
	}

	public static void reloadCSS ( ) {
		final String cssURL = CSS.getURL();
		final ObservableList<String> stylesheets = App.scene.getStylesheets();

		stylesheets.clear();
		stylesheets.add(cssURL);
	}

	public static void reloadSelectedFile ( ) {
		final FileData fileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
		if ( fileData != null ) {
			fileData.clearSearch();
			App.nodes.urlList.getItems().clear();
			App.nodes.urlsSearchTF.clear();
			fileData.read();
		}
	}

	public static void reloadFiles ( ) {
		final FileData selectedFileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
		if ( selectedFileData != null ) {
			selectedFileData.clearSearch();
		}
		App.nodes.urlList.getItems().clear();
		App.nodes.urlsSearchTF.clear();
		for ( FileData fileData: App.fileDataList ) {
			fileData.clearSearch();
			fileData.read();
		}
	}

	public static void setDefaultTitle ( ) {
		final Stage stage = (Stage) App.scene.getWindow();
		stage.setTitle(App.settings.windowTitle);
	}

	public static void setFileTitle ( final String fileString ) {
		final Stage stage = (Stage) App.scene.getWindow();
		stage.setTitle(App.settings.windowTitle + " (" + fileString + ")");
	}

	public static void fillUrlList ( ) {
		final FileData selectedFileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
		final ObservableList<Word> urlListItems = App.nodes.urlList.getItems();
		App.nodes.urlsSearchBtn.setDisable(true);
		selectedFileData.updateSearchedUrls();
		urlListItems.clear();
		for ( Word word: selectedFileData.searchedUrls ) {
			urlListItems.add(word);
		}
	}

	public static void fireCloseEvent ( ) {
		final Window window = App.scene.getWindow();
		final WindowEvent closeEvent = new WindowEvent(window,WindowEvent.WINDOW_CLOSE_REQUEST);
		window.fireEvent(closeEvent);
	}

	public static void focusUrlsSearchTF ( ) {
		App.nodes.tabPane.getSelectionModel().select(App.nodes.urlsTab);
		App.nodes.urlsSearchTF.requestFocus();
	}

}

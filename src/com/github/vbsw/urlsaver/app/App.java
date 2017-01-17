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


package com.github.vbsw.urlsaver.app;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.github.vbsw.urlsaver.FileData;
import com.github.vbsw.urlsaver.FileDataList;
import com.github.vbsw.urlsaver.Listener;
import com.github.vbsw.urlsaver.Nodes;
import com.github.vbsw.urlsaver.Settings;
import com.github.vbsw.urlsaver.TaggedWords;
import com.github.vbsw.urlsaver.Listener.CloseWindow;
import com.github.vbsw.urlsaver.Listener.HotKey;
import com.github.vbsw.urlsaver.TaggedWords.Word;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


/**
 * @author Vitali Baumtrok
 */
public class App extends Application {

	public static final Charset STRING_ENCODING = StandardCharsets.UTF_8;

	public static CommandLine commandLine;
	public static Settings settings;
	public static FileDataList fileDataList;
	public static Scene scene;
	public static Nodes nodes;
	public static boolean fileHasBeenSelected;

	public static void main ( final String[] args ) {
		App.commandLine = new CommandLine(args);

		if ( App.commandLine.isEmpty() ) {
			Application.launch(App.class,args);

		} else {
			App.commandLine.print();
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
		App.scene.addEventFilter(KeyEvent.KEY_PRESSED,new Listener.HotKey());
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
			fileData.searchedTags.clear();
			App.nodes.urlList.getItems().clear();
			App.nodes.urlSearchTF.clear();
			fileData.read();
		}
	}

	public static void reloadFiles ( ) {
		final FileData selectedFileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
		if ( selectedFileData != null ) {
			selectedFileData.searchedTags.clear();
		}
		App.nodes.urlList.getItems().clear();
		App.nodes.urlSearchTF.clear();
		for ( FileData fileData: App.fileDataList ) {
			fileData.searchedTags.clear();
			fileData.read();
		}
	}

	public static void setWindowTitle ( ) {
		final Stage stage = (Stage) App.scene.getWindow();
		stage.setTitle(App.settings.windowTitle);
	}

	public static void setWindowTitle ( final String bracketsContent ) {
		final Stage stage = (Stage) App.scene.getWindow();
		stage.setTitle(App.settings.windowTitle + " (" + bracketsContent + ")");
	}

	public static void fillUrlList ( ) {
		final FileData selectedFileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
		final ObservableList<Word> urlListItems = App.nodes.urlList.getItems();
		App.nodes.urlSearchBtn.setDisable(true);
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

	public static void selectFileTab ( ) {
		if ( App.nodes.filesTab.isDisable() == false ) {
			final FileData selectedFileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
			App.nodes.tabPane.getSelectionModel().select(App.nodes.filesTab);
			if ( selectedFileData != null ) {
				App.nodes.fileList.requestFocus();
			}
		}
	}

	public static void selectUrlsTab ( ) {
		if ( App.nodes.urlsTab.isDisable() == false ) {
			App.nodes.tabPane.getSelectionModel().select(App.nodes.urlsTab);
			App.nodes.urlSearchTF.requestFocus();
		}
	}

	public static void selectAboutTab ( ) {
		if ( App.nodes.aboutTab.isDisable() == false ) {
			App.nodes.tabPane.getSelectionModel().select(App.nodes.aboutTab);
			App.nodes.quitAppBtn.requestFocus();
		}
	}

	public static void selectSettingsTab ( ) {
		if ( App.nodes.settingsTab.isDisable() == false ) {
			App.nodes.tabPane.getSelectionModel().select(App.nodes.settingsTab);
		}
	}

	public static void saveFile ( ) {
		final FileData fileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
		if ( fileData.isModified() ) {
			fileData.save();
			if ( fileData.isModified() == false ) {
				App.updateFileModifiedInfo();
				App.nodes.fileSaveBtn.setDisable(true);
			}
		}
	}

	public static void setFileModified ( final FileData fileData ) {
		if ( fileData.isModified() == false ) {
			final FileData selectedFileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
			fileData.setModified(true);
			App.updateFileModifiedInfo();
			if ( fileData == selectedFileData ) {
				App.nodes.fileSaveBtn.setDisable(false);
			}
		}
	}

	private static void updateFileModifiedInfo ( ) {
		final boolean fileDataModified = App.fileDataList.isModified();
		final String modifiedPostfix = " *"; //$NON-NLS-1$
		final String fileTabText = App.nodes.filesTab.getText();
		if ( fileDataModified ) {
			if ( fileTabText.endsWith(modifiedPostfix) == false ) {
				final String modifiedFielTabText = fileTabText + modifiedPostfix;
				App.nodes.filesTab.setText(modifiedFielTabText);
				App.nodes.fileList.refresh();
			}
		} else {
			if ( fileTabText.endsWith(modifiedPostfix) ) {
				final int endIndex = fileTabText.length() - modifiedPostfix.length();
				final String modifiedFielTabText = fileTabText.substring(0,endIndex);
				App.nodes.filesTab.setText(modifiedFielTabText);
				App.nodes.fileList.refresh();
			}
		}
	}

}

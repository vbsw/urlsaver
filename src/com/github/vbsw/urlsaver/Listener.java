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


import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.github.vbsw.urlsaver.TaggedWords.Word;
import com.github.vbsw.urlsaver.app.App;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;
import javafx.util.Callback;


/**
 * @author Vitali Baumtrok
 */
public class Listener {

	private static final StringList tmpTagList = new StringList(20);

	public static class KeyPressedUrlCreateOKBtn implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			Listener.processUrlCreateOKBtn();
		}
	}

	public static class CreateUrlOKBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			Listener.processUrlCreateOKBtn();
		}
	}

	public static class TypedUrlTF implements ChangeListener<String> {
		@Override
		public void changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
			final TaggedWords.Word selectedItem = App.nodes.urlList.getSelectionModel().getSelectedItem();
			if ( selectedItem != null ) {
				final String selectedUrlString = selectedItem.string;
				if ( oldValue.isEmpty() || selectedUrlString.equals(oldValue) ) {
					if ( newValue.isEmpty() == false && selectedUrlString.equals(newValue) == false ) {
						Listener.enableUrlEdit();
					}
				} else {
					if ( newValue.isEmpty() || selectedUrlString.equals(newValue) ) {
						Listener.disableUrlEdit();
					}
				}
			} else {
				if ( oldValue.isEmpty() ) {
					if ( newValue.isEmpty() == false ) {
						Listener.enableUrlEdit();
					}
				} else {
					if ( newValue.isEmpty() ) {
						Listener.disableUrlEdit();
					}
				}
			}
		}
	}

	public static class SelectAboutTab implements ChangeListener<Boolean> {
		@Override
		public void changed ( final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue ) {
			if ( newValue == false && App.nodes.quitAppSaveBtn.isDisable() == false ) {
				App.nodes.quitAppBtn.setDisable(false);
				App.nodes.quitAppSaveBtn.setDisable(true);
				App.nodes.quitAppOKBtn.setDisable(true);
			}
		}
	}

	public static class HotKey implements EventHandler<KeyEvent> {
		private final KeyCombination ctrlS = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);

		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();

			if ( keyCode == KeyCode.F2 ) {
				event.consume();
				App.selectFileTab();

			} else if ( keyCode == KeyCode.F3 ) {
				event.consume();
				App.selectUrlsTab();

			} else if ( keyCode == KeyCode.F4 ) {
				event.consume();
				App.selectSettingsTab();

			} else if ( keyCode == KeyCode.F5 ) {
				event.consume();
				App.selectAboutTab();

			} else if ( ctrlS.match(event) ) {
				App.saveFile();
			}
		}
	}

	public static class FailFileRead implements EventHandler<WorkerStateEvent> {
		@Override
		public void handle ( final WorkerStateEvent event ) {
			event.getSource().getException().printStackTrace();
		}
	}

	public static class TypedUrlsSearchTF implements ChangeListener<String> {
		@Override
		public void changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
			if ( newValue != null ) {
				final FileData fileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
				fileData.setSearchedTags(newValue);
				App.nodes.urlSearchBtn.setDisable(fileData.searchedTagsEqual);
			}
		}
	}

	public static class SearchUrlsTF implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			App.fillUrlList();
			if ( App.nodes.urlList.getItems().size() > 0 ) {
				App.nodes.urlList.requestFocus();
				App.nodes.urlList.getSelectionModel().select(0);
			}
		}
	}

	public static class KeyPressedUrlSearchTF implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			// final KeyCode keyCode = event.getCode();
			// if ( keyCode == KeyCode.ESCAPE ) {
			// App.nodes.tabPane.getSelectionModel().select(App.nodes.aboutTab);
			// App.nodes.quitAppBtn.requestFocus();
			// }
		}
	}

	public static class SuccessFileRead implements EventHandler<WorkerStateEvent> {
		private final FileData fileData;

		public SuccessFileRead ( FileData fileData ) {
			this.fileData = fileData;
		}

		@Override
		public void handle ( final WorkerStateEvent event ) {
			final FileData selectedFileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
			fileData.urls = (TaggedWords) event.getSource().getValue();

			if ( fileData == selectedFileData ) {
				App.nodes.urlsTab.setDisable(false);
			}

			if ( fileData.fileName.equals(App.settings.fileAutoSelect) && (App.fileHasBeenSelected == false) ) {
				App.fileHasBeenSelected = true;
				App.nodes.fileList.getSelectionModel().select(fileData);
				App.nodes.tabPane.getSelectionModel().select(App.nodes.urlsTab);
				App.nodes.urlSearchTF.requestFocus();
			}
		}
	}

	public static class ProgressFileLoading implements ChangeListener<Number> {
		private final FileData fileData;

		public ProgressFileLoading ( final FileData fileData ) {
			this.fileData = fileData;
		}

		@Override
		public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
			fileData.setLoaded(newValue.doubleValue());
			App.nodes.fileList.refresh();
		}
	}

	public static class CloseWindow implements EventHandler<WindowEvent> {
		@Override
		public void handle ( final WindowEvent event ) {
			if ( App.fileDataList.isModified() ) {
				App.nodes.tabPane.getSelectionModel().select(App.nodes.aboutTab);
				App.nodes.quitAppBtn.setDisable(true);
				App.nodes.quitAppSaveBtn.setDisable(false);
				App.nodes.quitAppOKBtn.setDisable(false);
				App.nodes.quitAppSaveBtn.requestFocus();
				event.consume();
			}
		}
	}

	public static class QuitAppBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			App.fireCloseEvent();
		}
	}

	public static class KeyPressedQuitAppBtn implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			if ( keyCode == KeyCode.ENTER ) {
				App.fireCloseEvent();
			} else if ( keyCode == KeyCode.ESCAPE ) {
				// App.focusUrlsSearchTF();
			}
		}
	}

	public static class KeyPressedQuitAppSaveBtn implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			if ( keyCode == KeyCode.ENTER ) {
				for ( FileData fileData: App.fileDataList ) {
					if ( fileData.isModified() ) {
						fileData.save();
					}
				}
				if ( App.fileDataList.isModified() == false ) {
					Platform.exit();
				}
			}
		}
	}

	public static class QuitAppSaveBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			for ( FileData fileData: App.fileDataList ) {
				if ( fileData.isModified() ) {
					fileData.save();
				}
			}
			if ( App.fileDataList.isModified() == false ) {
				Platform.exit();
			}
		}
	}

	public static class KeyPressedQuitAppOKBtn implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			if ( keyCode == KeyCode.ENTER ) {
				Platform.exit();
			}
		}
	}

	public static class QuitAppOKBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			Platform.exit();
		}
	}

	public static class KeyPressedUrlCancelBtn implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			if ( keyCode == KeyCode.ENTER ) {
				Listener.processUrlCancelButton();
			}
		}
	}

	public static class KeyPressedUrlDeleteOKBtn implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			if ( keyCode == KeyCode.ENTER ) {
				Listener.processUrlDeleteOKButton();
			}
		}
	}

	public static class DeleteUrlBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			App.nodes.urlList.requestFocus();
			App.nodes.urlCancelBtn.setDisable(false);
			App.nodes.urlDeleteBtn.setDisable(true);
			App.nodes.urlDeleteOKBtn.setDisable(false);
		}
	}

	public static class DeleteUrlOKBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			Listener.processUrlDeleteOKButton();
		}
	}

	public static class UrlCancelBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			Listener.processUrlCancelButton();
		}
	}

	public static class ReloadFileBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			App.reloadSelectedFile();
		}
	}

	public static class ReloadAllFilesBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			App.reloadFiles();
		}
	}

	public static class OpenInBrowserBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			final Word word = App.nodes.urlList.getSelectionModel().getSelectedItem();
			if ( word != null ) {
				openURLInBrowser(word.string);
			}
		}
	}

	public static class SearchUrlsBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			App.fillUrlList();
		}
	}

	public static class KeyPressedUrlList implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			final TaggedWords.Word selectedUrl = App.nodes.urlList.getSelectionModel().getSelectedItem();

			if ( keyCode == KeyCode.ENTER && selectedUrl != null ) {
				Listener.openURLInBrowser(selectedUrl.string);

			} else if ( keyCode == KeyCode.DELETE ) {
				App.nodes.urlDeleteBtn.setDisable(true);
				App.nodes.urlCancelBtn.setDisable(false);
				App.nodes.urlDeleteOKBtn.setDisable(false);
				App.nodes.urlCancelBtn.requestFocus();
			}
		}
	}

	public static class KeyReleasedUrlList implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
		}
	}

	public static class KeyPressedFileList implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			final FileData selectedFileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
			if ( keyCode == KeyCode.ENTER && selectedFileData != null && selectedFileData.isLoaded() ) {
				event.consume();
				App.nodes.tabPane.getSelectionModel().select(App.nodes.urlsTab);
				App.nodes.urlSearchTF.requestFocus();
			}
		}
	}

	public static class FileListCellFactory implements Callback<ListView<FileData>, ListCell<FileData>> {
		@Override
		public ListCell<FileData> call ( final ListView<FileData> listView ) {
			final ListCell<FileData> listCell = new Listener.FileListCell();
			return listCell;
		}
	}

	public static class SelectFileListItem implements ChangeListener<FileData> {
		@Override
		public void changed ( final ObservableValue<? extends FileData> observable, final FileData oldValue, final FileData newValue ) {
			if ( newValue != null ) {
				final String filePathStr = newValue.filePathStr;
				App.nodes.fileNameTF.setText(filePathStr);
				App.setWindowTitle(newValue.fileName);
				App.nodes.urlSearchTF.clear();
				newValue.searchedTags.clear();

			} else {
				final String defaultText = ""; //$NON-NLS-1$
				App.nodes.fileNameTF.setText(defaultText);
				App.setWindowTitle();
			}
			App.nodes.reloadFileBtn.setDisable(newValue == null);
			App.nodes.urlsTab.setDisable((newValue == null) || (newValue.isLoaded() == false));
		}
	}

	public static class UrlListCellFactory implements Callback<ListView<Word>, ListCell<Word>> {
		@Override
		public ListCell<Word> call ( final ListView<Word> param ) {
			final ListCell<Word> listCell = new Listener.UrlListCell();
			return listCell;
		}
	}

	public static class SelectUrlListItem implements ChangeListener<Word> {
		@Override
		public void changed ( final ObservableValue<? extends Word> observable, final Word oldValue, final Word newValue ) {
			if ( newValue != null ) {
				Listener.processSelectUrlItem(newValue);

			} else {
				final String defaultText = ""; //$NON-NLS-1$
				App.nodes.urlTF.setText(defaultText);
				App.nodes.tagsTA.setText(defaultText);
				App.nodes.openInBrowserBtn.setDisable(true);
				App.nodes.urlCancelBtn.setDisable(true);
				App.nodes.urlDeleteBtn.setDisable(true);
			}
		}
	}

	private static class FileListCell extends ListCell<FileData> {
		private static final EventHandler<MouseEvent> eventHandler = new Listener.DoubleClickFileListItem();

		@Override
		protected void updateItem ( final FileData item, final boolean empty ) {
			super.updateItem(item,empty);

			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);

			} else {
				setText(item.listName);
				setOnMouseClicked(eventHandler);
			}
		}
	}

	private static class DoubleClickFileListItem implements EventHandler<MouseEvent> {
		@Override
		public void handle ( final MouseEvent event ) {
			if ( event.getClickCount() == 2 ) {
				final FileListCell cell = (FileListCell) event.getSource();
				final FileData fileData = cell.getItem();
				final String filePathStr = fileData.filePathStr;
				App.nodes.fileNameTF.setText(filePathStr);
			}
		}
	}

	private static class UrlListCell extends ListCell<Word> {
		private static final EventHandler<MouseEvent> eventHandler = new Listener.DoubleClickUrlListItem();

		@Override
		protected void updateItem ( final Word item, final boolean empty ) {
			super.updateItem(item,empty);

			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);

			} else {
				setText(item.string);
				setOnMouseClicked(eventHandler);
			}
		}
	}

	private static class DoubleClickUrlListItem implements EventHandler<MouseEvent> {
		@Override
		public void handle ( final MouseEvent event ) {
			if ( event.getClickCount() == 2 ) {
				final UrlListCell cell = (UrlListCell) event.getSource();
				final Word url = cell.getItem();
				openURLInBrowser(url.string);
			}
		}
	}

	private static void openURLInBrowser ( final String urlString ) {
		if ( Desktop.isDesktopSupported() ) {
			final Desktop desktop = Desktop.getDesktop();

			if ( desktop.isSupported(Desktop.Action.BROWSE) ) {
				final URI uri = stringToURI(urlString);

				if ( uri != null ) {
					try {
						desktop.browse(uri);

					} catch ( final Exception e ) {
					}
				}
			}
		}
	}

	private static void processUrlCreateOKBtn ( ) {
		final FileData fileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
		final TaggedWords urls = fileData.urls;
		final String wordString = App.nodes.urlTF.getText();
		final TaggedWords.Word word = urls.add(wordString);
		final String tagsTAString = App.nodes.tagsTA.getText();
		final String tagStrings;

		if ( Parser.isWhiteSpace(tagsTAString) == false ) {
			tagStrings = App.nodes.tagsTA.getText();
		} else {
			tagStrings = "any"; //$NON-NLS-1$
		}
		Listener.tmpTagList.set(tagStrings);
		for ( String tagString: Listener.tmpTagList ) {
			word.addTag(tagString);
		}
		processUrlCancelButton();
		App.setFileModified(fileData);
		App.nodes.urlSearchTF.requestFocus();
	}

	private static void processUrlCancelButton ( ) {
		if ( App.nodes.urlDeleteOKBtn.isDisable() == false ) {
			App.nodes.urlCancelBtn.setDisable(true);
			App.nodes.urlDeleteBtn.setDisable(false);
			App.nodes.urlDeleteOKBtn.setDisable(true);
			App.nodes.urlList.requestFocus();

		} else if ( App.nodes.urlCreateOKBtn.isDisable() == false ) {
			final TaggedWords.Word selectedItem = App.nodes.urlList.getSelectionModel().getSelectedItem();
			App.nodes.urlCancelBtn.setDisable(true);
			App.nodes.urlDeleteBtn.setDisable(false);
			App.nodes.urlDeleteOKBtn.setDisable(true);
//			App.nodes.urlCreateOKBtn.setDisable(true);

			if ( selectedItem != null ) {
				final String selectedUrlString = selectedItem.string;
				final String selectedTagsString = selectedItem.tagsString;
				App.nodes.urlTF.setText(selectedUrlString);
				App.nodes.tagsTA.setText(selectedTagsString);
				App.nodes.urlList.requestFocus();

			} else {
				final String defaultString = ""; //$NON-NLS-1$
				App.nodes.urlTF.setText(defaultString);
				App.nodes.tagsTA.setText(defaultString);
				App.nodes.urlSearchTF.requestFocus();
			}
		}
	}

	private static void enableUrlEdit ( ) {
		App.nodes.urlDeleteBtn.setDisable(true);
		App.nodes.urlDeleteOKBtn.setDisable(true);
//		App.nodes.urlCreateOKBtn.setDisable(false);
		App.nodes.urlEditOKBtn.setDisable(true);
		App.nodes.urlCancelBtn.setDisable(false);
	}

	private static void disableUrlEdit ( ) {
		final TaggedWords.Word selectedItem = App.nodes.urlList.getSelectionModel().getSelectedItem();
		App.nodes.urlDeleteBtn.setDisable(selectedItem == null);
//		App.nodes.urlCreateOKBtn.setDisable(true);
		App.nodes.urlEditOKBtn.setDisable(true);
		App.nodes.urlCancelBtn.setDisable(true);
	}

	private static void processUrlDeleteOKButton ( ) {
		final ObservableList<TaggedWords.Word> urlListItems = App.nodes.urlList.getItems();
		final int selectedIndex = App.nodes.urlList.getSelectionModel().getSelectedIndex();
		final TaggedWords.Word selectedWord = urlListItems.get(selectedIndex);
		final FileData fileData = App.nodes.fileList.getSelectionModel().getSelectedItem();

		urlListItems.remove(selectedIndex);
		fileData.urls.remove(selectedWord);
		App.setFileModified(fileData);
		App.nodes.urlCancelBtn.setDisable(true);

		if ( App.nodes.urlList.getItems().size() > selectedIndex ) {
			App.nodes.urlList.getSelectionModel().select(selectedIndex);
			App.nodes.urlList.requestFocus();

		} else if ( App.nodes.urlList.getItems().size() == selectedIndex && selectedIndex > 0 ) {
			final TaggedWords.Word word = App.nodes.urlList.getItems().get(selectedIndex - 1);
			Listener.processSelectUrlItem(word);
			App.nodes.urlList.requestFocus();

		} else {
			App.nodes.urlSearchTF.requestFocus();
		}
		App.nodes.urlDeleteOKBtn.setDisable(true);
	}

	private static URI stringToURI ( final String urlString ) {
		final URL url = stringToURL(urlString);

		if ( url != null ) {
			try {
				return url.toURI();

			} catch ( final Exception e ) {
			}
		}
		return null;
	}

	private static URL stringToURL ( final String urlString ) {
		if ( urlString != null && urlString.length() > 0 ) {
			try {
				return new URL(urlString);

			} catch ( final MalformedURLException e ) {
			}

			try {
				final String httpPrefix = "http://"; //$NON-NLS-1$
				final String httpURLStr = httpPrefix + urlString;
				return new URL(httpURLStr);

			} catch ( final MalformedURLException e ) {
			}
		}

		return null;
	}

	private static void processSelectUrlItem ( final Word word ) {
		App.nodes.urlCancelBtn.setDisable(true);
		App.nodes.urlDeleteBtn.setDisable(false);
		App.nodes.urlDeleteOKBtn.setDisable(true);
		App.nodes.openInBrowserBtn.setDisable(false);
		App.nodes.urlTF.setText(word.string);
		App.nodes.tagsTA.setText(word.tagsString);
	}

	/*
	 * public static class FileTableRowFactory implements
	 * Callback<TableView<FileTableItem>, TableRow<FileTableItem>> {
	 * private final EventHandler<MouseEvent> eventHandler = new
	 * Listener.DoubleClickFileTableItem();
	 * 
	 * @Override
	 * public TableRow<FileTableItem> call ( final TableView<FileTableItem>
	 * tableView ) {
	 * final TableRow<FileTableItem> row = new TableRow<FileTableItem>();
	 * row.setOnMouseClicked(eventHandler);
	 * return row;
	 * }
	 * }
	 */

	/*
	 * public static class DoubleClickFileTableItem implements
	 * EventHandler<MouseEvent> {
	 * 
	 * @Override
	 * public void handle ( final MouseEvent event ) {
	 * if ( event.getClickCount()==2 ) {
	 * 
	 * @SuppressWarnings ( "unchecked" )
	 * final TableRow<FileTableItem> row = (TableRow<FileTableItem>)
	 * event.getSource();
	 * final Scene scene = (Scene) row.getScene();
	 * if ( row.isEmpty()==false ) {
	 * final FileTableItem item = row.getItem();
	 * final String filePath = item.fileData.filePath.toString();
	 * scene.nodes.fileName.setText(filePath);
	 * }
	 * }
	 * }
	 * }
	 */

}

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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.WindowEvent;
import javafx.util.Callback;


/**
 * @author Vitali Baumtrok
 */
public class Listener {

	public static class FailFileRead implements EventHandler<WorkerStateEvent> {
		@Override
		public void handle ( final WorkerStateEvent event ) {
			event.getSource().getException().printStackTrace();
		}
	}

	public static class TypedUrlsSearchTF implements ChangeListener<String> {
		@Override
		public void changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
			if ( newValue!=null ) {
				final FileData fileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
				fileData.setSearchedTags(newValue);
				App.nodes.urlsSearchBtn.setDisable(fileData.searchedTagsEqual);
			}
		}
	}

	public static class SearchUrlsTF implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			App.fillUrlList();
			if ( App.nodes.urlList.getItems().size()>0 ) {
				App.nodes.urlList.requestFocus();
				App.nodes.urlList.getSelectionModel().select(0);
			}
		}
	}

	public static class KeyPressedUrlsSearchTF implements EventHandler<KeyEvent> {
		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			if ( keyCode==KeyCode.ESCAPE ) {
				// App.nodes.tabPane.getSelectionModel().select(App.nodes.aboutTab);
				// App.nodes.quitAppBtn.requestFocus();
			}
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

			if ( fileData==selectedFileData ) {
				App.nodes.urlsTab.setDisable(false);
			}

			if ( fileData.fileName.equals(App.settings.fileAutoSelect)&&( App.fileHasBeenSelected==false ) ) {
				App.fileHasBeenSelected = true;
				App.nodes.fileList.getSelectionModel().select(fileData);
				App.focusUrlsSearchTF();
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
			System.out.println("URL Saver closed");
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
			if ( keyCode==KeyCode.ENTER ) {
				App.fireCloseEvent();
			} else if ( keyCode==KeyCode.ESCAPE ) {
				// App.focusUrlsSearchTF();
			}
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
			if ( word!=null ) {
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
		private boolean enterPressed;

		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();

			if ( keyCode==KeyCode.ENTER ) {
				final TaggedWords.Word selectedUrl = App.nodes.urlList.getSelectionModel().getSelectedItem();
				if ( selectedUrl!=null ) {
					if ( enterPressed==false ) {
						enterPressed = true;
						Listener.openURLInBrowser(selectedUrl.string);
					}
				} else {
					enterPressed = false;
				}

			} else if ( keyCode==KeyCode.ESCAPE ) {
				// final TaggedWords.Word selectedUrl = App.nodes.urlList.getSelectionModel().getSelectedItem();
				// if ( selectedUrl!=null ) {
				// App.nodes.urlsSearchTF.requestFocus();
				// }
			}
		}
	}

	public static class KeyReleasedUrlList implements EventHandler<KeyEvent> {
		private final KeyPressedUrlList keyPressedUrlList;

		public KeyReleasedUrlList ( final EventHandler<? super KeyEvent> keyPressedUrlList ) {
			this.keyPressedUrlList = (KeyPressedUrlList) keyPressedUrlList;
		}

		@Override
		public void handle ( final KeyEvent event ) {
			final KeyCode keyCode = event.getCode();
			if ( keyCode==KeyCode.ENTER ) {
				keyPressedUrlList.enterPressed = false;
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
			if ( newValue!=null ) {
				final String filePathStr = newValue.filePathStr;
				App.nodes.fileNameTF.setText(filePathStr);
				App.setFileTitle(newValue.fileName);

			} else {
				final String defaultText = ""; //$NON-NLS-1$
				App.nodes.fileNameTF.setText(defaultText);
				App.setDefaultTitle();
			}
			App.nodes.reloadFileBtn.setDisable(newValue==null);
			App.nodes.urlsTab.setDisable(( newValue==null )||( newValue.isLoaded()==false ));
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
			if ( newValue!=null ) {
				App.nodes.urlTF.setText(newValue.string);
				App.nodes.tagsTA.setText(newValue.tagsString);

			} else {
				final String defaultText = ""; //$NON-NLS-1$
				App.nodes.urlTF.setText(defaultText);
				App.nodes.tagsTA.setText(defaultText);
			}
			App.nodes.openInBrowserBtn.setDisable(newValue==null);
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
			if ( event.getClickCount()==2 ) {
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
			if ( event.getClickCount()==2 ) {
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

				if ( uri!=null ) {
					try {
						desktop.browse(uri);

					} catch ( final Exception e ) {
					}
				}
			}
		}
	}

	private static URI stringToURI ( final String urlString ) {
		final URL url = stringToURL(urlString);

		if ( url!=null ) {
			try {
				return url.toURI();

			} catch ( final Exception e ) {
			}
		}
		return null;
	}

	private static URL stringToURL ( final String urlString ) {
		if ( urlString!=null&&urlString.length()>0 ) {
			try {
				return new URL(urlString);

			} catch ( final MalformedURLException e ) {
			}

			try {
				final String httpPrefix = "http://"; //$NON-NLS-1$
				final String httpURLStr = httpPrefix+urlString;
				return new URL(httpURLStr);

			} catch ( final MalformedURLException e ) {
			}
		}

		return null;
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

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
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
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

	public static class ProcessUrlsSearchTF implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			final FileData fileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
			App.nodes.urlsSearchBtn.setDisable(true);
			fileData.updateSearchResults();
			fileData.resambleSearchedKeys();
			fillUrlList(fileData);
		}

		private void fillUrlList ( final FileData fileData ) {
			final ObservableList<Word> list = App.nodes.urlList.getItems();
			list.clear();
			for ( Word word: fileData.searchedUrls ) {
				list.add(word);
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
			fileData.urls = (TaggedWords) event.getSource().getValue();

			if ( fileData.fileName.equals(App.settings.fileAutoSelect) ) {
				App.nodes.fileList.getSelectionModel().select(fileData);
				App.nodes.tabPane.getSelectionModel().select(App.nodes.urlsTab);
				App.nodes.urlsSearchTF.requestFocus();

			} else {
				final FileData selectedItem = App.nodes.fileList.getSelectionModel().getSelectedItem();
				if ( fileData==selectedItem ) {
					App.nodes.urlsTab.setDisable(false);
				}
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
			final ObservableList<FileData> fileTableItems = App.nodes.fileList.getItems();
			final int fileDataIndex = fileTableItems.indexOf(fileData);
			fileData.setLoaded(newValue.doubleValue());
			fileTableItems.set(fileDataIndex,fileData);
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
			final Node source = (Node) event.getSource();
			final Window window = source.getScene().getWindow();
			final WindowEvent closeEvent = new WindowEvent(window,WindowEvent.WINDOW_CLOSE_REQUEST);
			window.fireEvent(closeEvent);
		}
	}

	public static class ReloadFileBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			final FileData fileData = App.nodes.fileList.getSelectionModel().getSelectedItem();
			if ( fileData!=null ) {
				fileData.read();
			}
		}
	}

	public static class ReloadAllFilesBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			App.reloadFiles();
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

	public static class FileListCellFactory implements Callback<ListView<FileData>, ListCell<FileData>> {

		@Override
		public ListCell<FileData> call ( final ListView<FileData> listView ) {
			final ListCell<FileData> listCell = new Listener.FileListCell();
			return listCell;
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

	public static class UrlListCellFactory implements Callback<ListView<Word>, ListCell<Word>> {
		@Override
		public ListCell<Word> call ( final ListView<Word> param ) {
			final ListCell<Word> listCell = new Listener.UrlListCell();
			return listCell;
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

	public static class OpenInBrowserBtn implements EventHandler<ActionEvent> {
		@Override
		public void handle ( final ActionEvent event ) {
			final Word word = App.nodes.urlList.getSelectionModel().getSelectedItem();
			if ( word!=null ) {
				openURLInBrowser(word.string);
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

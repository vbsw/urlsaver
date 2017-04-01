/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
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


package com.github.vbsw.urlsaver.app.window.files;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


/**
 * @author Vitali Baumtrok
 */
public class FilesModelView {

	final SimpleBooleanProperty selectedFileDirty = new SimpleBooleanProperty();
	final SimpleBooleanProperty confirmingSave = new SimpleBooleanProperty();
	final SimpleBooleanProperty selected = new SimpleBooleanProperty();

	public SimpleBooleanProperty selectedFileDirtyProperty ( ) {
		return selectedFileDirty;
	}

	public SimpleBooleanProperty confirmingSaveProperty ( ) {
		return confirmingSave;
	}

	public SimpleBooleanProperty selectedProperty ( ) {
		return selected;
	}

	public void button_reloadFile_clicked ( final ActionEvent event ) {
		App.files.reloadSelected();
	}

	public void button_reloadFile_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.files.reloadSelected();
		}
	}

	public void button_reloadAllFiles_clicked ( final ActionEvent event ) {
		App.files.reloadAll();
	}

	public void button_reloadAllFiles_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.files.reloadAll();
		}
	}

	public void button_fileSave_clicked ( final ActionEvent event ) {
		App.files.saveSelected();
	}

	public void button_fileSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.files.saveSelected();
		}
	}

	public void button_fileCancel_clicked ( final ActionEvent event ) {
		App.files.cancel();
	}

	public void button_fileCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.files.cancel();
		}
	}

	public void button_fileSaveOK_clicked ( final ActionEvent event ) {
		App.files.confirmSaveSelected();
	}

	public void button_fileSaveOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.files.confirmSaveSelected();
		}
	}

	public void listViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			App.files.updateSelectedInfo();
		}
	}

	public void listView_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			event.consume();

			if ( App.scene.topTab.urls.isDisable() == false ) {
				App.scene.tp.top.getSelectionModel().select(App.scene.topTab.urls);
				App.scene.tf.urlSearch.requestFocus();
			}
		}
	}

	public void listViewItem_selected ( final ObservableValue<? extends Path> observable, final Path oldValue, final Path newValue ) {
		App.files.updateSelectedInfo();
		App.window.updateTitle();
	}

	public ListCell<Path> cellFactory ( final ListView<Path> param ) {
		final ListCell<Path> listCell = new FileListCell();
		return listCell;
	}

	private static final class FileListCell extends ListCell<Path> {

		@Override
		protected void updateItem ( final Path item, final boolean empty ) {
			super.updateItem(item,empty);

			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);

			} else {
				final int fileDataIndex = App.files.getDataIndex(item);
				final String listViewText = App.files.getDataLabel(fileDataIndex);

				setText(listViewText);
				setOnMouseClicked(event -> App.files.mv.listViewItem_clicked(event));
			}
		}

	}

}

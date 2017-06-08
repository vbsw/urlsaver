
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


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
public class FilesViewModel {

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
		selectedFileDirty.setValue(false);
		confirmingSave.setValue(false);
		App.window.updateTitle();
	}

	public void hotKey_ctrlS ( ) {
		selectedFileDirty.setValue(false);
		confirmingSave.setValue(false);
	}

	public void button_fileSaveOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.files.confirmSaveSelected();
			selectedFileDirty.setValue(false);
			confirmingSave.setValue(false);
			App.window.updateTitle();
		}
	}

	public void listViewItem_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			App.files.updateSelectedInfo();
			updatePropertiesAfterFileSelection();
		}
	}

	public void listViewItem_selected ( final ObservableValue<? extends Path> observable, final Path oldValue, final Path newValue ) {
		App.files.updateSelectedInfo();
		updatePropertiesAfterFileSelection();
		App.window.updateTitle();
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

	public ListCell<Path> cellFactory ( final ListView<Path> param ) {
		final ListCell<Path> listCell = new FileListCell();
		return listCell;
	}

	private void updatePropertiesAfterFileSelection ( ) {
		final Path selectedFilePath = App.scene.lv.files.getSelectionModel().getSelectedItem();
		final int fileDataIndex = App.files.getDataIndex(selectedFilePath);
		final boolean dirty;

		if ( fileDataIndex >= 0 ) {
			dirty = App.files.isDirty(fileDataIndex);
		} else {
			dirty = false;
		}
		selectedFileDirty.set(dirty);
		selected.set(fileDataIndex >= 0);
		confirmingSave.set(false);
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
				setOnMouseClicked(event -> App.files.vm.listViewItem_clicked(event));
			}
		}

	}

}

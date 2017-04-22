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


package com.github.vbsw.urlsaver.app.window.urls;


import com.github.vbsw.urlsaver.WebBrowser;
import com.github.vbsw.urlsaver.app.App;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
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
public class UrlsModelView {

	final SimpleBooleanProperty available = new SimpleBooleanProperty();
	final SimpleBooleanProperty selected = new SimpleBooleanProperty();
	final SimpleBooleanProperty deleteRequested = new SimpleBooleanProperty();
	final SimpleBooleanProperty urlModified = new SimpleBooleanProperty();
	final SimpleBooleanProperty tagsModified = new SimpleBooleanProperty();

	public SimpleBooleanProperty availableProperty ( ) {
		return available;
	}

	public SimpleBooleanProperty selectedProperty ( ) {
		return selected;
	}

	public ObservableBooleanValue deleteRequestedProperty ( ) {
		return deleteRequested;
	}

	public ObservableBooleanValue urlModifiedProperty ( ) {
		return urlModified;
	}

	public ObservableBooleanValue tagsModifiedProperty ( ) {
		return tagsModified;
	}

	public void button_openInBrowser_clicked ( final ActionEvent event ) {
		WebBrowser.openSelectedUrl();
	}

	public void button_openInBrowser_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			WebBrowser.openSelectedUrl();
		}
	}

	public void button_urlSearch_clicked ( final ActionEvent event ) {
		App.urls.search();
	}

	public void button_urlSearch_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.search();
		}
	}

	public void button_urlCancel_clicked ( final ActionEvent event ) {
		App.urls.cancel();
	}

	public void button_urlCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.cancel();
		}
	}

	public void button_urlDelete_clicked ( final ActionEvent event ) {
		App.urls.updateDeleteRequestedProperty(true);
	}

	public void button_urlDelete_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.updateDeleteRequestedProperty(true);
		}
	}

	public void button_urlDeleteOK_clicked ( final ActionEvent event ) {
		App.urls.confirmDelete();
	}

	public void button_urlDeleteOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.confirmDelete();
		}
	}

	public void button_urlCreateOK_clicked ( final ActionEvent event ) {
		App.urls.confirmCreate();
	}

	public void button_urlCreateOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.confirmCreate();
		}
	}

	public void button_urlEditOK_clicked ( final ActionEvent event ) {
		App.urls.confirmEdit();
	}

	public void button_urlEditOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.confirmEdit();
		}
	}

	public void textField_urlSearch_enterPressed ( final ActionEvent event ) {
		App.urls.search();
	}

	public ListCell<String> cellFactory ( final ListView<String> param ) {
		final ListCell<String> listCell = new UrlListCell();
		return listCell;
	}

	public void listView_clicked ( final MouseEvent event ) {
		if ( event.getClickCount() == 2 ) {
			final UrlListCell cell = (UrlListCell) event.getSource();
			final String url = cell.getItem();

			WebBrowser.openURL(url);
		}
	}

	public void listView_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();

			if ( selectedUrl != null ) {
				WebBrowser.openURL(selectedUrl);
			}

		} else if ( keyCode == KeyCode.DELETE ) {
			App.urls.updateDeleteRequestedProperty(true);
		}
	}

	public void textField_url_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		App.urls.updateUrlModifiedProperty();
		App.urls.updateDeleteRequestedProperty(false);
	}

	public void textField_tags_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		App.urls.updateTagsModifiedProperty();
		App.urls.updateDeleteRequestedProperty(false);
	}

	public void listViewItem_selected ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		App.urls.updateSelectedInfo();
	}

	public void textField_urlSearch_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final UrlsViewData urlsViewData = App.urls.getViewData();

		if ( newValue != null ) {
			urlsViewData.searchTagsString = newValue;

		} else {
			urlsViewData.searchTagsString = "";
		}
	}

	public void topTab_urls_selected ( final ObservableValue<? extends Boolean> observable, final Boolean oldValue, final Boolean newValue ) {
		if ( newValue ) {
			if ( App.scene.lv.urls.getItems().isEmpty() == false ) {
				App.scene.lv.urls.requestFocus();

			} else {
				App.scene.tf.urlSearch.requestFocus();
			}
		}
	}

	private static final class UrlListCell extends ListCell<String> {

		@Override
		protected void updateItem ( final String item, final boolean empty ) {
			super.updateItem(item,empty);

			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);

			} else {
				setText(item);
				setOnMouseClicked(event -> App.urls.mv.listView_clicked(event));
			}
		}

	}

}

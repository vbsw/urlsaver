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

	private final UrlsProperties properties = new UrlsProperties();

	public SimpleBooleanProperty availableProperty ( ) {
		return properties.available;
	}

	public SimpleBooleanProperty existsProperty ( ) {
		return properties.exists;
	}

	public ObservableBooleanValue deleteRequestedProperty ( ) {
		return properties.deleteRequested;
	}

	public ObservableBooleanValue urlModifiedProperty ( ) {
		return properties.urlModified;
	}

	public ObservableBooleanValue tagsModifiedProperty ( ) {
		return properties.tagsModified;
	}

	public void button_openInBrowser_clicked ( final ActionEvent event ) {
		WebBrowser.openTypedUrl();
	}

	public void button_openInBrowser_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			WebBrowser.openTypedUrl();
		}
	}

	public void initProperties ( final boolean urlsLoaded ) {
		properties.init(urlsLoaded);
	}

	public void button_urlSearch_clicked ( final ActionEvent event ) {
		App.urls.updateSearchResult();
		App.urls.updateSearchResultListView();
		App.urls.setSelectedAsInfoView();
		properties.button_urlSearch_clicked();
	}

	public void button_urlSearch_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.updateSearchResult();
			App.urls.updateSearchResultListView();
			App.urls.setSelectedAsInfoView();
			properties.button_urlSearch_clicked();
		}
	}

	public void button_urlCancel_clicked ( final ActionEvent event ) {
		App.urls.setSelectedAsInfoView();
		App.urls.focusUrlsListOrSearchView();
		properties.button_urlCancel_clicked();
	}

	public void button_urlCancel_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.setSelectedAsInfoView();
			App.urls.focusUrlsListOrSearchView();
			properties.button_urlCancel_clicked();
		}
	}

	public void button_urlDelete_clicked ( final ActionEvent event ) {
		properties.button_urlDelete_clicked();
		App.urls.focusUrlCancel();
	}

	public void button_urlDelete_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			properties.button_urlDelete_clicked();
			App.urls.focusUrlCancel();
		}
	}

	public void button_urlDeleteOK_clicked ( final ActionEvent event ) {
		App.urls.confirmDelete();
		App.urls.setSelectedAsInfoView();
		properties.button_urlDeleteOK_clicked();
	}

	public void button_urlDeleteOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.confirmDelete();
			App.urls.setSelectedAsInfoView();
			properties.button_urlDeleteOK_clicked();
		}
	}

	public void button_urlCreateOK_clicked ( final ActionEvent event ) {
		App.urls.confirmCreate();
		properties.button_urlCreateOK_clicked();
	}

	public void button_urlCreateOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.confirmCreate();
			properties.button_urlCreateOK_clicked();
		}
	}

	public void button_urlEditOK_clicked ( final ActionEvent event ) {
		App.urls.confirmEdit();
		properties.button_urlEditOK_clicked();
	}

	public void button_urlEditOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.urls.confirmEdit();
			properties.button_urlEditOK_clicked();
		}
	}

	public void textField_urlSearch_enterPressed ( final ActionEvent event ) {
		App.urls.updateSearchResult();
		App.urls.updateSearchResultListView();
		App.urls.setSelectedAsInfoView();
		properties.button_urlSearch_clicked();
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
			properties.button_urlDelete_clicked();
			App.urls.focusUrlCancel();
		}
	}

	public void textField_url_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		properties.textField_url_changed();
	}

	public void textField_tags_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		properties.textField_tags_changed();
	}

	public void listViewItem_selected ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		App.urls.setSelectedAsInfoView();
		properties.listViewItem_selected();
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

	public void allConfirmed ( ) {
		properties.allConfirmed();
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * @author Vitali Baumtrok
 */
public class TextFields {

	public static final FileName fileName = new FileName();
	public static final URLSearch urlSearch = new URLSearch();
	public static final URL url = new URL();
	public static final Title title = new Title();
	public static final Width width = new Width();
	public static final Height height = new Height();
	public static final FileExtension urlsFileExtension = new FileExtension();
	public static final DefaultFile urlsFileSelect = new DefaultFile();

	public static void build ( final Parent root ) {
		fileName.build(root);
		urlSearch.build(root);
		url.build(root);
		title.build(root);
		width.build(root);
		height.build(root);
		urlsFileExtension.build(root);
		urlsFileSelect.build(root);
	}

	public static void urlSearch_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		//		final UrlsViewData urlsViewData = App.urls.getViewData();
		//		if ( newValue != null ) {
		//			urlsViewData.searchTagsString = newValue;
		//		} else {
		//			urlsViewData.searchTagsString = "";
		//		}
	}

	public static void urlSearch_enterPressed ( ActionEvent event ) {
		//		App.urls.updateSearchResult();
		//		App.urls.updateSearchResultListView();
		//		App.urls.setSelectedAsInfoView();
		//		properties.button_urlSearch_clicked();
	}

	public static void url_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String urlTyped = Parser.trim(TextFields.url.control.getText());
		final DBRecord selectedRecord = ListViews.files.control.getSelectionModel().getSelectedItem();
		final boolean urlExists = urlTyped.length() > 0 && selectedRecord.getUrlIndex(urlTyped) >= 0;
		final boolean urlModified = urlTyped.length() > 0 && selectedRecord.getUrlIndex(urlTyped) < 0;
		Properties.urlExistsProperty().set(urlExists);
		Properties.urlModifiedProperty().set(urlModified);
		Properties.urlDeleteRequestedProperty().set(false);
	}

	public static void title_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final boolean titlesEqual = Preferences.getWindowTitle().getSavedValue().equals(trimmedValue);
		Preferences.getWindowTitle().setModifiedValue(newValue);
		TextFields.title.setFontWeight(!titlesEqual);
		Properties.titleChangedProperty().set(!titlesEqual);
		Properties.preferencesModifiedProperty().set(!titlesEqual);
	}

	public static void width_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int parsedValueInt = Parser.toUnsignedInteger(newValue);
		final String parsedValueStr = Integer.toString(parsedValueInt);
		final boolean valueChanged = parsedValueInt != Preferences.getWindowWidth().getSavedValue();
		if ( !parsedValueStr.equals(newValue) ) {
			TextFields.width.control.setText(parsedValueStr);
		} else {
			Preferences.getWindowWidth().setModifiedValue(parsedValueInt);
			Properties.widthChangedProperty().set(valueChanged);
			TextFields.width.setFontWeight(valueChanged);
		}
	}

	public static void height_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int parsedValueInt = Parser.toUnsignedInteger(newValue);
		final String parsedValueStr = Integer.toString(parsedValueInt);
		final boolean valueChanged = parsedValueInt != Preferences.getWindowHeight().getSavedValue();
		if ( !parsedValueStr.equals(newValue) ) {
			TextFields.height.control.setText(parsedValueStr);
		} else {
			Preferences.getWindowHeight().setModifiedValue(parsedValueInt);
			Properties.heightChangedProperty().set(valueChanged);
			TextFields.height.setFontWeight(valueChanged);
		}
	}

	public static void urlsFileExtension_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final boolean valueChanged = !trimmedValue.equals(Preferences.getURLsFileExtension().getSavedValue());
		Preferences.getURLsFileExtension().setModifiedValue(trimmedValue);
		Properties.urlsFileExtensionChangedProperty().set(valueChanged);
		TextFields.urlsFileExtension.setFontWeight(valueChanged);
	}

	public static void urlsFileSelect_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final boolean valueChanged = !trimmedValue.equals(Preferences.getURLsFileSelect().getSavedValue());
		Preferences.getURLsFileSelect().setModifiedValue(trimmedValue);
		Properties.urlsFileSelectChangedProperty().set(valueChanged);
		TextFields.urlsFileSelect.setFontWeight(valueChanged);
	}

	public static class CustomTextField {
		public TextField control;

		public void setFontWeight ( final boolean bold ) {
			final Font font = control.getFont();
			final FontWeight fontWeight = bold ? FontWeight.BOLD : FontWeight.NORMAL;
			final Font newFont = Font.font(font.getFamily(),fontWeight,font.getSize());
			control.setFont(newFont);
		}
	}

	public static class FileName extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#file_name_tf");
		}
	}

	public static class URLSearch extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#url_search_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.urlSearch_changed(observable,oldValue,newValue));
			control.setOnAction(event -> TextFields.urlSearch_enterPressed(event));
		}
	}

	public static class URL extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#url_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.url_changed(observable,oldValue,newValue));
		}
	}

	public static class Title extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_title_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.title_changed(observable,oldValue,newValue));
		}
	}

	public static class Width extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_width_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.width_changed(observable,oldValue,newValue));
		}
	}

	public static class Height extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_height_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.height_changed(observable,oldValue,newValue));
		}
	}

	public static class FileExtension extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_file_extension_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.urlsFileExtension_changed(observable,oldValue,newValue));
		}
	}

	public static class DefaultFile extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_default_file_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.urlsFileSelect_changed(observable,oldValue,newValue));
		}
	}

}

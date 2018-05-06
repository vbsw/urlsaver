/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.Parser;
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
	public static final FileExtension fileExtension = new FileExtension();
	public static final DefaultFile defaultFile = new DefaultFile();

	public static void build ( final Parent root ) {
		fileName.build(root);
		urlSearch.build(root);
		url.build(root);
		title.build(root);
		width.build(root);
		height.build(root);
		fileExtension.build(root);
		defaultFile.build(root);
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

	public static void url_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		//		final String urlTyped = Parser.trim(App.scene.tf.url.getText());
		//		final UrlsData urlsData = App.urls.getData();
		//		final boolean urlExists = urlTyped.length() > 0 && urlsData.getUrlIndex(urlTyped) >= 0;
		//		final boolean urlModified = urlTyped.length() > 0 && urlsData.getUrlIndex(urlTyped) < 0;
		//		this.exists.set(urlExists);
		//		this.urlModified.set(urlModified);
		//		this.deleteRequested.set(false);
	}

	public static void title_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final boolean titlesEqual = Preferences.getWindowTitle().getSavedValue().equals(trimmedValue);
		Preferences.getWindowTitle().setModifiedValue(newValue);
		TextFields.title.setFontWeight(!titlesEqual);
		Properties.titleChangedProperty().set(!titlesEqual);
		Properties.preferencesModifiedProperty().set(!titlesEqual);
	}

	public static void width_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		boolean valueChanged = false;
		try {
			final String trimmedValue = Parser.trim(newValue);
			final int parsedValue = Integer.parseInt(trimmedValue);
			valueChanged = parsedValue != Preferences.getWindowWidth().getModifiedValue();
			Preferences.getWindowWidth().setModifiedValue(parsedValue);
		} catch ( final NumberFormatException e ) {
			Preferences.getWindowWidth().resetModifiedValueToSaved();
		}
		Properties.widthChangedProperty().set(valueChanged);
		TextFields.width.setFontWeight(valueChanged);
	}

	public static void height_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		//		final int height = (int) App.settings.getWindowHeight();
		//		final boolean heightsEqual = Integer.toString(height).equals(newValue);
		//		final int newHeight = Parser.toInt(newValue);
		//		App.settings.setCustomWindowHeight(newHeight);
		//		setFontWeight(App.scene.tf.height,!heightsEqual);
		//		heightChanged.set(!heightsEqual);
	}

	public static void fileExtension_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		//		final boolean fileExtensionEqual = App.settings.getUrlsFileExtension().equals(newValue);
		//		App.settings.setCustomUrlsFileExtension(newValue);
		//		setFontWeight(App.scene.tf.fileExtension,!fileExtensionEqual);
		//		fileExtensionChanged.set(!fileExtensionEqual);
	}

	public static void defaultFile_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		//		final boolean defaultFilesEqual = App.settings.getUrlsFileSelect().equals(newValue);
		//		App.settings.setCustomUrlsFileSelect(newValue);
		//		setFontWeight(App.scene.tf.defaultFile,!defaultFilesEqual);
		//		defaultFileChanged.set(!defaultFilesEqual);
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
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.fileExtension_changed(observable,oldValue,newValue));
		}
	}

	public static class DefaultFile extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_default_file_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextFields.defaultFile_changed(observable,oldValue,newValue));
		}
	}

}

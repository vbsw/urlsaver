/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Preferences.PreferencesIntValue;
import com.github.vbsw.urlsaver.api.Preferences.PreferencesStringValue;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.db.DynArrayOfString;
import com.github.vbsw.urlsaver.pref.PreferencesConfig;
import com.github.vbsw.urlsaver.utility.Converter;
import com.github.vbsw.urlsaver.utility.Parser;

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

	public final FileName fileName = new FileName();
	public final URLSearch urlSearch = new URLSearch();
	public final URL url = new URL();
	public final Title title = new Title();
	public final Width width = new Width();
	public final Height height = new Height();
	public final FileExtension urlsFileExtension = new FileExtension();
	public final DefaultFile urlsFileSelect = new DefaultFile();

	protected StdGUI stdGUI;

	public TextFields ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
		fileName.build(root);
		urlSearch.build(root);
		url.build(root);
		title.build(root);
		width.build(root);
		height.build(root);
		urlsFileExtension.build(root);
		urlsFileSelect.build(root);
	}

	public void urlSearch_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final DBRecord record = stdGUI.db.getSelectedRecord();
		if ( newValue != null )
			record.setURLsSearchString(newValue);
		else
			record.setURLsSearchString("");
	}

	public void urlSearch_enterPressed ( final ActionEvent event ) {
		final DBRecord record = stdGUI.db.getSelectedRecord();
		final String searchString = record.getURLsSearchString();
		final boolean searchByPrefix = stdGUI.preferences.getBooleanValue(PreferencesConfig.SEARCH_BY_PREFIX_ID).getSaved();
		final DynArrayOfString searchTags = Converter.toDynArrayList(searchString);

		record.searchURLs(searchTags,searchByPrefix);
		stdGUI.listViews.urls.showSearchResults();
		stdGUI.refreshURLsInfo();
		stdGUI.resetURLsProperties();
	}

	public void url_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String urlTyped = Parser.trim(url.control.getText());
		final DBRecord selectedRecord = stdGUI.db.getSelectedRecord();
		final boolean urlExists = urlTyped.length() > 0 && selectedRecord.getURLIndex(urlTyped) >= 0;
		final boolean urlModified = urlTyped.length() > 0 && selectedRecord.getURLIndex(urlTyped) < 0;
		stdGUI.properties.urlExistsProperty().set(urlExists);
		stdGUI.properties.urlModifiedProperty().set(urlModified);
		stdGUI.properties.urlDeleteRequestedProperty().set(false);
	}

	public void title_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final PreferencesStringValue windowTitleValue = stdGUI.preferences.getStringValue(PreferencesConfig.WINDOW_TITLE_ID);
		final boolean titlesEqual = windowTitleValue.getSaved().equals(trimmedValue);
		windowTitleValue.setModified(newValue);
		title.setFontWeight(!titlesEqual);
		stdGUI.properties.titleChangedProperty().set(!titlesEqual);
		stdGUI.properties.preferencesModifiedProperty().set(!titlesEqual);
	}

	public void width_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int parsedValueInt = Converter.toUnsignedInteger(newValue);
		final String parsedValueStr = Integer.toString(parsedValueInt);
		final PreferencesIntValue windowWidthValue = stdGUI.preferences.getIntValue(PreferencesConfig.WINDOW_WIDTH_ID);
		final boolean valueChanged = parsedValueInt != windowWidthValue.getSaved();
		if ( !parsedValueStr.equals(newValue) ) {
			width.control.setText(parsedValueStr);
		} else {
			windowWidthValue.setModified(parsedValueInt);
			stdGUI.properties.widthChangedProperty().set(valueChanged);
			width.setFontWeight(valueChanged);
		}
	}

	public void height_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int parsedValueInt = Converter.toUnsignedInteger(newValue);
		final String parsedValueStr = Integer.toString(parsedValueInt);
		final PreferencesIntValue windowHeightValue = stdGUI.preferences.getIntValue(PreferencesConfig.WINDOW_HEIGHT_ID);
		final boolean valueChanged = parsedValueInt != windowHeightValue.getSaved();
		if ( !parsedValueStr.equals(newValue) ) {
			height.control.setText(parsedValueStr);
		} else {
			windowHeightValue.setModified(parsedValueInt);
			stdGUI.properties.heightChangedProperty().set(valueChanged);
			height.setFontWeight(valueChanged);
		}
	}

	public void urlsFileExtension_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final PreferencesStringValue urlsFileExtensionValue = stdGUI.preferences.getStringValue(PreferencesConfig.URLS_FILE_EXTENSION_ID);
		final boolean valueChanged = !trimmedValue.equals(urlsFileExtensionValue.getSaved());
		urlsFileExtensionValue.setModified(trimmedValue);
		stdGUI.properties.urlsFileExtensionChangedProperty().set(valueChanged);
		urlsFileExtension.setFontWeight(valueChanged);
	}

	public void urlsFileSelect_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final PreferencesStringValue urlsFileSelectValue = stdGUI.preferences.getStringValue(PreferencesConfig.URLS_FILE_SELECT_ID);
		final boolean valueChanged = !trimmedValue.equals(urlsFileSelectValue.getSaved());
		urlsFileSelectValue.setModified(trimmedValue);
		stdGUI.properties.urlsFileSelectChangedProperty().set(valueChanged);
		urlsFileSelect.setFontWeight(valueChanged);
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

	public class URLSearch extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#url_search_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> urlSearch_changed(observable,oldValue,newValue));
			control.setOnAction(event -> urlSearch_enterPressed(event));
		}
	}

	public class URL extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#url_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> url_changed(observable,oldValue,newValue));
		}
	}

	public class Title extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_title_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> title_changed(observable,oldValue,newValue));
		}
	}

	public class Width extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_width_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> width_changed(observable,oldValue,newValue));
		}
	}

	public class Height extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_height_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> height_changed(observable,oldValue,newValue));
		}
	}

	public class FileExtension extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_file_extension_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> urlsFileExtension_changed(observable,oldValue,newValue));
		}
	}

	public class DefaultFile extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#preferences_default_file_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> urlsFileSelect_changed(observable,oldValue,newValue));
		}
	}

}

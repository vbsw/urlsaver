/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.Preferences.IntPreference;
import com.github.vbsw.urlsaver.api.Preferences.StringPreference;
import com.github.vbsw.urlsaver.db.DBTable;
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
		final DBTable record = Global.db.getSelectedDBTable();
		if ( newValue != null )
			record.setURLsSearchString(newValue);
		else
			record.setURLsSearchString("");
	}

	public void urlSearch_enterPressed ( final ActionEvent event ) {
		final DBTable record = Global.db.getSelectedDBTable();
		final String searchString = record.getURLsSearchString();
		final boolean searchByPrefix = Global.preferences.getBooleanPreference(PreferencesConfig.SEARCH_BY_PREFIX_ID).getSaved();
		final DynArrayOfString searchTags = Converter.toDynArrayList(searchString);

		record.searchURLs(searchTags,searchByPrefix);
		stdGUI.tableViews.urls.showSearchResults();
		stdGUI.refreshURLsInfo();
		stdGUI.resetURLsProperties();
	}

	public void url_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String urlTyped = Parser.trim(url.control.getText());
		final DBTable selectedDBTable = Global.db.getSelectedDBTable();
		final boolean urlExists = selectedDBTable.getURLIndex(urlTyped) >= 0;
		final boolean urlModified = urlTyped.length() > 0 && !urlExists;
		stdGUI.properties.urlExistsProperty().set(urlExists);
		stdGUI.properties.urlModifiedProperty().set(urlModified);
		stdGUI.properties.urlDeleteRequestedProperty().set(false);
	}

	private void title_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final StringPreference windowTitlePreference = Global.preferences.getStringPereference(PreferencesConfig.WINDOW_TITLE_ID);
		final boolean valueChanged = !windowTitlePreference.getSaved().equals(trimmedValue);
		windowTitlePreference.setModified(newValue);
		title.setFontWeight(valueChanged);
		stdGUI.properties.titleChangedProperty().set(valueChanged);
		stdGUI.properties.refreshPreferencesModifiedProperty();
	}

	private void width_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int newValueInt = Converter.toUnsignedInteger(newValue);
		final String newValueStr = Integer.toString(newValueInt);
		final IntPreference windowWidthPreference = Global.preferences.getIntPreference(PreferencesConfig.WINDOW_WIDTH_ID);
		final boolean valueChanged = newValueInt != windowWidthPreference.getSaved();
		if ( newValueStr.equals(newValue) ) {
			windowWidthPreference.setModified(newValueInt);
			width.setFontWeight(valueChanged);
			stdGUI.properties.widthChangedProperty().set(valueChanged);
			stdGUI.properties.refreshPreferencesModifiedProperty();
		} else {
			// can't remember what case this is... maybe some empty values?
			width.control.setText(newValueStr);
		}
	}

	private void height_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int newValueInt = Converter.toUnsignedInteger(newValue);
		final String newValueStr = Integer.toString(newValueInt);
		final IntPreference windowHeightPreference = Global.preferences.getIntPreference(PreferencesConfig.WINDOW_HEIGHT_ID);
		final boolean valueChanged = newValueInt != windowHeightPreference.getSaved();
		if ( newValueStr.equals(newValue) ) {
			windowHeightPreference.setModified(newValueInt);
			height.setFontWeight(valueChanged);
			stdGUI.properties.heightChangedProperty().set(valueChanged);
			stdGUI.properties.refreshPreferencesModifiedProperty();
		} else {
			// can't remember what case this is... maybe some empty values?
			height.control.setText(newValueStr);
		}
	}

	private void urlsFileExtension_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final StringPreference urlsFileExtensionValue = Global.preferences.getStringPereference(PreferencesConfig.URLS_FILE_EXTENSION_ID);
		final boolean valueChanged = !trimmedValue.equals(urlsFileExtensionValue.getSaved());
		urlsFileExtensionValue.setModified(trimmedValue);
		urlsFileExtension.setFontWeight(valueChanged);
		stdGUI.properties.urlsFileExtensionChangedProperty().set(valueChanged);
		stdGUI.properties.refreshPreferencesModifiedProperty();
	}

	private void urlsFileSelect_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final StringPreference urlsFileSelectValue = Global.preferences.getStringPereference(PreferencesConfig.URLS_FILE_SELECT_ID);
		final boolean valueChanged = !trimmedValue.equals(urlsFileSelectValue.getSaved());
		urlsFileSelectValue.setModified(trimmedValue);
		urlsFileSelect.setFontWeight(valueChanged);
		stdGUI.properties.urlsFileSelectChangedProperty().set(valueChanged);
		stdGUI.properties.refreshPreferencesModifiedProperty();
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

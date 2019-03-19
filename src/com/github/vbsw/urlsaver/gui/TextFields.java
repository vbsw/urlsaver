/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.ISettings;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.DBURLsImport;
import com.github.vbsw.urlsaver.db.DynArrayOfString;
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
	public final ImportKeys importKeys = new ImportKeys();

	protected GUI stdGUI;

	public TextFields ( final GUI stdGUI ) {
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
		importKeys.build(root);
	}

	public void urlSearch_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final DBURLs dbURLs = Global.db.getSelectedURLs();
		if ( newValue != null )
			dbURLs.setURLsSearchString(newValue);
		else
			dbURLs.setURLsSearchString("");
	}

	public void urlSearch_enterPressed ( final ActionEvent event ) {
		final DBURLs record = Global.db.getSelectedURLs();
		final String searchString = record.getURLsSearchString();
		final boolean searchByPrefix = Global.settings.getBooleanProperty(ISettings.Property.searchByPrefix).savedValue;
		final DynArrayOfString searchTags = Converter.toDynArrayList(searchString);

		record.searchURLs(searchTags,searchByPrefix);
		stdGUI.tableViews.urls.showSearchResults();
		stdGUI.refreshURLsInfo();
		stdGUI.resetURLsProperties();
	}

	public void importKeys_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final DBURLsImport dbURLsImport = Global.db.getSelectedURLsImport();
		if (newValue != null)
			dbURLsImport.setURLsImportKeysString(newValue);
		else
			dbURLsImport.setURLsImportKeysString("");
	}

	public void importKeys_enterPressed ( final ActionEvent event ) {
		// TODO importKeys_enterPressed
	}

	public void url_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String urlTyped = Parser.trim(url.control.getText());
		final DBURLs selectedDBTable = Global.db.getSelectedURLs();
		final boolean urlExists = selectedDBTable.getURLIndex(urlTyped) >= 0;
		final boolean urlModified = urlTyped.length() > 0 && !urlExists;
		stdGUI.properties.urlExistsProperty().set(urlExists);
		stdGUI.properties.urlModifiedProperty().set(urlModified);
		stdGUI.properties.urlDeleteRequestedProperty().set(false);
	}

	private void title_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final ISettings.StringProperty windowTitleSetting = Global.settings.getStringProperty(ISettings.Property.windowTitle);
		final boolean valueChanged = !windowTitleSetting.savedValue.equals(trimmedValue);
		windowTitleSetting.modifiedValue = newValue;
		title.setFontWeight(valueChanged);
		stdGUI.properties.titleChangedProperty().set(valueChanged);
		stdGUI.properties.refreshSettingsModifiedProperty();
	}

	private void width_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int newValueInt = Converter.toUnsignedInteger(newValue);
		final String newValueStr = Integer.toString(newValueInt);
		final ISettings.IntProperty windowWidthSetting = Global.settings.getIntProperty(ISettings.Property.windowWidth);
		final boolean valueChanged = newValueInt != windowWidthSetting.savedValue;
		if ( newValueStr.equals(newValue) ) {
			windowWidthSetting.modifiedValue = newValueInt;
			width.setFontWeight(valueChanged);
			stdGUI.properties.widthChangedProperty().set(valueChanged);
			stdGUI.properties.refreshSettingsModifiedProperty();
		} else {
			// can't remember what case this is... maybe some empty values?
			width.control.setText(newValueStr);
		}
	}

	private void height_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final int newValueInt = Converter.toUnsignedInteger(newValue);
		final String newValueStr = Integer.toString(newValueInt);
		final ISettings.IntProperty windowHeightSetting = Global.settings.getIntProperty(ISettings.Property.windowHeight);
		final boolean valueChanged = newValueInt != windowHeightSetting.savedValue;
		if ( newValueStr.equals(newValue) ) {
			windowHeightSetting.modifiedValue = newValueInt;
			height.setFontWeight(valueChanged);
			stdGUI.properties.heightChangedProperty().set(valueChanged);
			stdGUI.properties.refreshSettingsModifiedProperty();
		} else {
			// can't remember what case this is... maybe some empty values?
			height.control.setText(newValueStr);
		}
	}

	private void urlsFileExtension_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final ISettings.StringProperty urlsFileExtensionValue = Global.settings.getStringProperty(ISettings.Property.urlsFileExtension);
		final boolean valueChanged = !trimmedValue.equals(urlsFileExtensionValue.savedValue);
		urlsFileExtensionValue.modifiedValue = trimmedValue;
		urlsFileExtension.setFontWeight(valueChanged);
		stdGUI.properties.urlsFileExtensionChangedProperty().set(valueChanged);
		stdGUI.properties.refreshSettingsModifiedProperty();
	}

	private void urlsFileSelect_changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final String trimmedValue = Parser.trim(newValue);
		final ISettings.StringProperty urlsFileSelectValue = Global.settings.getStringProperty(ISettings.Property.urlsFileSelect);
		final boolean valueChanged = !trimmedValue.equals(urlsFileSelectValue.savedValue);
		urlsFileSelectValue.modifiedValue = trimmedValue;
		urlsFileSelect.setFontWeight(valueChanged);
		stdGUI.properties.urlsFileSelectChangedProperty().set(valueChanged);
		stdGUI.properties.refreshSettingsModifiedProperty();
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
			control = (TextField) root.lookup("#settings_title_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> title_changed(observable,oldValue,newValue));
		}
	}

	public class Width extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#settings_width_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> width_changed(observable,oldValue,newValue));
		}
	}

	public class Height extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#settings_height_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> height_changed(observable,oldValue,newValue));
		}
	}

	public class FileExtension extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#settings_file_extension_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> urlsFileExtension_changed(observable,oldValue,newValue));
		}
	}

	public class DefaultFile extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#settings_default_file_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> urlsFileSelect_changed(observable,oldValue,newValue));
		}
	}

	public class ImportKeys extends CustomTextField {
		private void build ( final Parent root ) {
			control = (TextField) root.lookup("#import_keys_tf");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> importKeys_changed(observable,oldValue,newValue));
			control.setOnAction(event -> importKeys_enterPressed(event));
		}
	}

}

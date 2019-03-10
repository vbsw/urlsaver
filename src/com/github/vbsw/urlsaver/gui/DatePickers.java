/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.URLMetaDefinition;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.utility.Parser;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;


/**
 * @author Vitali Baumtrok
 */
public class DatePickers {

	private static final String datePattern = "yyyy-MM-dd";
	private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DatePickers.datePattern);
	private static final StringConverter<LocalDate> converter = new Converter();

	protected StdGUI stdGUI;

	public final URLDate urlDate = new URLDate();

	public DatePickers ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
		urlDate.build(root);
	}

	private void urlDate_textChanged ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		final DBURLs selectedDBTable = Global.db.getSelectedURLs();
		final String urlTyped = Parser.trim(stdGUI.textFields.url.control.getText());
		final int urlIndex = selectedDBTable.getURLIndex(urlTyped);
		if ( urlIndex >= 0 ) {
			final String urlDateStrOld = selectedDBTable.getMetaData(urlIndex,URLMetaDefinition.DATE);
			final String urlDateStrNew = stdGUI.datePickers.urlDate.getDateAsString();
			final boolean equalDates = urlDateStrOld == urlDateStrNew || urlDateStrOld != null && urlDateStrOld.equals(urlDateStrNew);
			stdGUI.properties.urlDateModifiedProperty().set(!equalDates);
		} else {
			stdGUI.properties.urlDateModifiedProperty().set(false);
		}
		stdGUI.properties.urlDeleteRequestedProperty().set(false);
	}

	public final class URLDate {

		public DatePicker control;

		private void build ( final Parent root ) {
			control = (DatePicker) root.lookup("#url_dp");
			control.setPromptText(DatePickers.datePattern);
			control.setConverter(DatePickers.converter);
			control.getEditor().textProperty().addListener( ( observable, oldValue, newValue ) -> urlDate_textChanged(observable,oldValue,newValue));
		}

		public void setDate ( final String dateStr ) {
			if ( dateStr != null && dateStr.length() > 0 ) {
				try {
					final LocalDate localDate = LocalDate.parse(dateStr);
					control.setValue(localDate);
				} catch ( Exception e ) {
					control.setValue(null);
				}
			} else {
				control.setValue(null);
			}
		}

		public String getDateAsString ( ) {
			final String dateStrRaw = control.getEditor().getText();
			String dateStr = null;
			if ( !dateStrRaw.isEmpty() ) {
				try {
					LocalDate.parse(dateStrRaw,DatePickers.dateFormatter);
					dateStr = dateStrRaw;
				} catch ( DateTimeParseException e ) {
					final LocalDate urlDateOld = control.getValue();
					if ( urlDateOld != null )
						dateStr = DatePickers.dateFormatter.format(urlDateOld);
				}
			}
			return dateStr;
		}

	}

	private static final class Converter extends StringConverter<LocalDate> {

		@Override
		public String toString ( final LocalDate localDate ) {
			if ( localDate != null ) {
				return DatePickers.dateFormatter.format(localDate);
			} else {
				return "";
			}
		}

		@Override
		public LocalDate fromString ( final String dataStr ) {
			if ( dataStr != null && !dataStr.isEmpty() ) {
				return LocalDate.parse(dataStr,DatePickers.dateFormatter);
			} else {
				return null;
			}
		}

	}

}

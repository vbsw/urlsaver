/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.Converter;
import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.db.DynArrayOfString;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;


/**
 * @author Vitali Baumtrok
 */
public class TextAreas {

	public static final Tags tags = new Tags();
	public static final Log log = new Log();

	public static class CustomTextArea {
		public TextArea control;
	}

	public static void build ( final Parent root ) {
		tags.build(root);
		log.build(root);
	}

	public static void tags_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		final DBRecord record = GUI.getCurrentDBRecord();
		final String urlTyped = Parser.trim(TextFields.url.control.getText());
		final int urlIndex = record.getURLIndex(urlTyped);
		if ( urlIndex >= 0 ) {
			final DynArrayOfString tags = Converter.toDynArrayListSorted(TextAreas.tags.control.getText());
			final boolean equalTags = record.isEqualTags(urlIndex,tags);
			Properties.urlTagsModifiedProperty().set(!equalTags);
		} else {
			Properties.urlTagsModifiedProperty().set(false);
		}
		Properties.urlDeleteRequestedProperty().set(false);
	}

	public static final class Tags extends CustomTextArea {
		private void build ( final Parent root ) {
			control = (TextArea) root.lookup("#tags_ta");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> TextAreas.tags_changed(observable,oldValue,newValue));
		}
	}

	public static final class Log extends CustomTextArea {
		private void build ( final Parent root ) {
			control = (TextArea) root.lookup("#log_ta");
		}
	}

}

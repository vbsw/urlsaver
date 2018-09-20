/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.db.DBTable;
import com.github.vbsw.urlsaver.db.DynArrayOfString;
import com.github.vbsw.urlsaver.utility.Converter;
import com.github.vbsw.urlsaver.utility.Parser;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;


/**
 * @author Vitali Baumtrok
 */
public class TextAreas {

	public final Tags tags = new Tags();
	public final Log log = new Log();

	protected StdGUI stdGUI;

	public TextAreas ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
		tags.build(root);
		log.build(root);
	}

	public void tags_changed ( ObservableValue<? extends String> observable, String oldValue, String newValue ) {
		final DBTable selectedDBTable = Global.db.getSelectedDBTable();
		final String urlTyped = Parser.trim(stdGUI.textFields.url.control.getText());
		final int urlIndex = selectedDBTable.getURLIndex(urlTyped);
		if ( urlIndex >= 0 ) {
			final DynArrayOfString tags = Converter.toDynArrayListSorted(this.tags.control.getText());
			final boolean equalTags = selectedDBTable.isEqualTags(urlIndex,tags);
			stdGUI.properties.urlTagsModifiedProperty().set(!equalTags);
		} else {
			stdGUI.properties.urlTagsModifiedProperty().set(false);
		}
		stdGUI.properties.urlDeleteRequestedProperty().set(false);
	}

	public static class CustomTextArea {
		public TextArea control;
	}

	public final class Tags extends CustomTextArea {
		private void build ( final Parent root ) {
			control = (TextArea) root.lookup("#tags_ta");
			control.textProperty().addListener( ( ObservableValue<? extends String> observable, String oldValue, String newValue ) -> tags_changed(observable,oldValue,newValue));
		}
	}

	public static final class Log extends CustomTextArea {
		private void build ( final Parent root ) {
			control = (TextArea) root.lookup("#log_ta");
		}
	}

}

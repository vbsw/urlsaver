/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


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
		//		final UrlsData urlsData = App.urls.getData();
		//		final String urlTyped = Parser.trim(App.scene.tf.url.getText());
		//		final int urlIndex = urlsData.getUrlIndex(urlTyped);
		//		if ( urlIndex >= 0 ) {
		//			final String tagsTyped = Parser.trim(App.scene.ta.tags.getText());
		//			final SortedUniqueStringList urlTags = urlsData.urlTagsList.get(urlIndex);
		//			tagsTmp.addStringsSeparatedByWhiteSpace(tagsTyped);
		//			tagsModified.setValue(tagsTmp.isEqualByStrings(urlTags) == false);
		//			tagsTmp.clear();
		//		} else {
		//			tagsModified.setValue(false);
		//		}
		//		this.deleteRequested.set(false);
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

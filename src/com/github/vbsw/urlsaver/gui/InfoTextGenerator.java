/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.pref.Preferences;


/**
 * @author Vitali Baumtrok
 */
public class InfoTextGenerator {

	public static String getFileListLabel ( final DBRecord record, final int percentLoaded ) {
		final String listViewText;
		if ( percentLoaded < 0 )
			listViewText = record.getPathAsString() + "  0%";
		else if ( percentLoaded < 100 )
			listViewText = record.getPathAsString() + "  " + percentLoaded + "%";
		else
			listViewText = record.getPathAsString();
		return listViewText;
	}

	public static String getWindowTitle ( final DBRecord selectedRecord ) {
		final String windowTitleCustom = Preferences.getWindowTitle().getSavedValue();
		final String windowTitle;
		if ( selectedRecord != null )
			if ( selectedRecord.isDirty() )
				windowTitle = windowTitleCustom + " (" + selectedRecord.getFileName() + " *)";
			else
				windowTitle = windowTitleCustom + " (" + selectedRecord.getFileName() + ")";
		else
			windowTitle = windowTitleCustom;
		return windowTitle;
	}

	public static String getURLsCountLabel ( final DBRecord selectedRecord ) {
		final int diff = selectedRecord.getURLsCountModified() - selectedRecord.getURLsCountSaved();
		final String diffString = diff != 0 ? " (" + (diff > 0 ? "+" : "") + diff + ")" : "";
		final String urlsCountString;
		if ( selectedRecord.isLoaded() )
			urlsCountString = "URLs  " + selectedRecord.getURLsCountModified() + diffString;
		else
			urlsCountString = "URLs  ?";
		return urlsCountString;
	}

	public static String getTagsCountLabel ( final DBRecord selectedRecord ) {
		final int diff = selectedRecord.getTagsCountModified() - selectedRecord.getTagsCountSaved();
		final String diffString = diff != 0 ? " (" + (diff > 0 ? "+" : "") + diff + ")" : "";
		final String tagsCountString;
		if ( selectedRecord.isLoaded() )
			tagsCountString = "Tags  " + selectedRecord.getTagsCountModified() + diffString;
		else
			tagsCountString = "Tags  ?";
		return tagsCountString;
	}

}

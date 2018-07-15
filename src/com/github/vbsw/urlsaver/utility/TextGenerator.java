/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.utility;


import com.github.vbsw.urlsaver.db.DBRecord;


/**
 * @author Vitali Baumtrok
 */
public class TextGenerator {

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

	public static String getURLsCountLabel ( final DBRecord record ) {
		final int diff = record.getURLsCountModified() - record.getURLsCountSaved();
		final String diffString = diff != 0 ? " (" + (diff > 0 ? "+" : "") + diff + ")" : "";
		final String urlsCountString;
		if ( record.isLoaded() )
			urlsCountString = "URLs  " + record.getURLsCountModified() + diffString;
		else
			urlsCountString = "URLs  ?";
		return urlsCountString;
	}

	public static String getTagsCountLabel ( final DBRecord record ) {
		final int diff = record.getTagsCountModified() - record.getTagsCountSaved();
		final String diffString = diff != 0 ? " (" + (diff > 0 ? "+" : "") + diff + ")" : "";
		final String tagsCountString;
		if ( record.isLoaded() )
			tagsCountString = "Tags  " + record.getTagsCountModified() + diffString;
		else
			tagsCountString = "Tags  ?";
		return tagsCountString;
	}

	public static String getFileSizeLabel ( final DBRecord record ) {
		final long fileSize = record.getFileSize();
		final String fileSizeLabel;
		if ( fileSize >= 0 )
			if ( fileSize > 1000 )
				if ( fileSize > 1000 * 1000 )
					if ( fileSize > 1000 * 1000 * 1000 )
						fileSizeLabel = "Size " + fileSize / (1000 * 1000 * 1000) + " GB";
					else
						fileSizeLabel = "Size " + fileSize / (1000 * 1000) + " MB";
				else
					fileSizeLabel = "Size " + fileSize / 1000 + " KB";
			else
				fileSizeLabel = "Size " + fileSize + " B";
		else
			fileSizeLabel = "Size ?";
		return fileSizeLabel;
	}

}

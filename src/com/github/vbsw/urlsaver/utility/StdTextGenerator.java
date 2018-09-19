/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.utility;


import com.github.vbsw.urlsaver.api.TextGenerator;
import com.github.vbsw.urlsaver.db.DBTable;


/**
 * @author Vitali Baumtrok
 */
public class StdTextGenerator extends TextGenerator {

	@Override
	public String getFileListLabel ( final DBTable record, final int percentLoaded ) {
		final String listViewText;
		if ( percentLoaded < 0 )
			listViewText = record.getPathAsString() + "  0%";
		else if ( percentLoaded < 100 )
			listViewText = record.getPathAsString() + "  " + percentLoaded + "%";
		else
			listViewText = record.getPathAsString();
		return listViewText;
	}

	@Override
	public String getURLsCountLabel ( final DBTable record ) {
		final int diff = record.getURLsCountModified() - record.getURLsCountSaved();
		final String diffString = diff != 0 ? " (" + (diff > 0 ? "+" : "") + diff + ")" : "";
		final String urlsCountString;
		if ( record.isLoaded() )
			urlsCountString = "URLs  " + record.getURLsCountModified() + diffString;
		else
			urlsCountString = "URLs  ?";
		return urlsCountString;
	}

	@Override
	public String getTagsCountLabel ( final DBTable record ) {
		final int diff = record.getTagsCountModified() - record.getTagsCountSaved();
		final String diffString = diff != 0 ? " (" + (diff > 0 ? "+" : "") + diff + ")" : "";
		final String tagsCountString;
		if ( record.isLoaded() )
			tagsCountString = "Tags  " + record.getTagsCountModified() + diffString;
		else
			tagsCountString = "Tags  ?";
		return tagsCountString;
	}

	@Override
	public String getFileSizeLabel ( final DBTable record ) {
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

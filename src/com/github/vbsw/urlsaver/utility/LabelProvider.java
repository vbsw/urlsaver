/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.utility;


import com.github.vbsw.urlsaver.api.ILabelProvider;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.DBURLsImport;


/**
 * @author Vitali Baumtrok
 */
public class LabelProvider implements ILabelProvider {

	@Override
	public String getFileListLabel ( final DBURLs dbURLs, final int percentLoaded ) {
		final String label;
		if ( percentLoaded < 0 )
			label = dbURLs.getPathAsString() + "  0%";
		else if ( percentLoaded < 100 )
			label = dbURLs.getPathAsString() + "  " + percentLoaded + "%";
		else
			label = dbURLs.getPathAsString();
		return label;
	}

	@Override
	public String getImportListLabel ( final DBURLsImport dbURLsImport, final int percentLoaded ) {
		final String label;
		if ( percentLoaded < 0 )
			label = dbURLsImport.getPathAsString() + "  0%";
		else if ( percentLoaded < 100 )
			label = dbURLsImport.getPathAsString() + "  " + percentLoaded + "%";
		else
			label = dbURLsImport.getPathAsString();
		return label;
	}

	@Override
	public String getURLsCountLabel ( final DBURLs dbURLs ) {
		final int diff = dbURLs.getURLsCountModified() - dbURLs.getURLsCountSaved();
		final String diffString = diff != 0 ? " (" + (diff > 0 ? "+" : "") + diff + ")" : "";
		final String label;
		if ( dbURLs.isLoaded() )
			label = "URLs  " + dbURLs.getURLsCountModified() + diffString;
		else
			label = "URLs  ?";
		return label;
	}

	@Override
	public String getTagsCountLabel ( final DBURLs dbURLs ) {
		final int diff = dbURLs.getTagsCountModified() - dbURLs.getTagsCountSaved();
		final String diffString = diff != 0 ? " (" + (diff > 0 ? "+" : "") + diff + ")" : "";
		final String tagsCountString;
		if ( dbURLs.isLoaded() )
			tagsCountString = "Tags  " + dbURLs.getTagsCountModified() + diffString;
		else
			tagsCountString = "Tags  ?";
		return tagsCountString;
	}

	@Override
	public String getFileSizeLabel ( final DBURLs dbURLs ) {
		final long fileSize = dbURLs.getFileSize();
		final String label = fileSizeToLabel(fileSize);
		return label;
	}

	private String fileSizeToLabel ( final long fileSize ) {
		final String label;
		if ( fileSize >= 0 )
			if ( fileSize > 1000 )
				if ( fileSize > 1000 * 1000 )
					if ( fileSize > 1000 * 1000 * 1000 )
						label = "Size " + fileSize / (1000 * 1000 * 1000) + " GB";
					else
						label = "Size " + fileSize / (1000 * 1000) + " MB";
				else
					label = "Size " + fileSize / 1000 + " KB";
			else
				label = "Size " + fileSize + " B";
		else
			label = "Size ?";
		return label;
	}

}

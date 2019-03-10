/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.DBURLsImport;


/**
 * @author Vitali Baumtrok
 */
public interface ILabelProvider {

	public String getFileListLabel ( DBURLs dbURLs, int percentLoaded );

	public String getImportListLabel ( DBURLsImport dbURLsImport, int percentLoaded );

	public String getURLsCountLabel ( DBURLs dbURLs );

	public String getTagsCountLabel ( DBURLs dbURLs );

	public String getFileSizeLabel ( DBURLs dbURLs );

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;

import com.github.vbsw.urlsaver.db.DBRecord;

/**
 * @author Vitali Baumtrok
 */
public abstract class TextGenerator {

	public abstract String getFileListLabel ( final DBRecord record, final int percentLoaded );

	public abstract String getURLsCountLabel ( final DBRecord record );

	public abstract String getTagsCountLabel ( final DBRecord record );

	public abstract String getFileSizeLabel ( final DBRecord record );

}

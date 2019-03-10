/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import com.github.vbsw.urlsaver.api.IPathProvider;
import com.github.vbsw.urlsaver.api.URLMetaDefinition;
import com.github.vbsw.urlsaver.db.DBURLsImport;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


/**
 * @author Vitali Baumtrok
 */
public class URLsImportLoadService extends Service<DBURLsImport> {

	public URLsImportLoadService ( final IPathProvider pathProvider, final URLMetaDefinition urlMeta, final DBURLsImport record ) {
	}

	@Override
	protected Task<DBURLsImport> createTask ( ) {
		return null;
	}

}

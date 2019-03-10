/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.DBURLsImport;


/**
 * @author Vitali Baumtrok
 */
public interface IDB {

	public DBURLs getSelectedURLs ( );

	public DBURLsImport getSelectedURLsImport ( );

	public DBURLs getURLsByFileName ( String fileName );

	public ArrayList<DBURLsImport> getURLsImportList ( );

	public ArrayList<DBURLs> getURLsList ( );

	public boolean isSaved ( );

	public void reload ( );

	public void setSelectedURLs ( DBURLs dbURLs );

	public void setSelectedURLsImport ( DBURLsImport dbURLsImport );

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.db.DBRecord;


/**
 * @author Vitali Baumtrok
 */
public abstract class DataBase {

	public abstract void initialize ( ResourceLoader resourceLoader, Preferences preferences, TextGenerator textGenerator );

	public abstract ArrayList<DBRecord> getRecords ( );

	public abstract boolean isSaved ( );

	public abstract DBRecord getRecordByFileName ( String fileName );

	public abstract DBRecord getSelectedRecord ( );

	public abstract void setSelectedRecord ( DBRecord record );

}

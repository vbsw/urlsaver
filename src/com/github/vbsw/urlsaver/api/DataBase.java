/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.db.DBTable;


/**
 * @author Vitali Baumtrok
 */
public abstract class DataBase {

	public abstract void initialize ( );

	public abstract ArrayList<DBTable> getRecords ( );

	public abstract boolean isSaved ( );

	public abstract DBTable getRecordByFileName ( String fileName );

	public abstract DBTable getSelectedRecord ( );

	public abstract void setSelectedRecord ( DBTable record );

}

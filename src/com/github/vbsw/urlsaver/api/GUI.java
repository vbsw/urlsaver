/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import com.github.vbsw.urlsaver.db.DBRecord;

import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public abstract class GUI {

	public abstract void initialize ( Global global, Stage primaryStage );

	public abstract void quit ( );

	public abstract void confirmAny ( );

	public abstract void refreshFileSelection ( );

	public abstract void refreshFileInfo ( );

	public abstract void refreshFileListView ( );

	public abstract void refreshTitle ( );

	public abstract void recordLoaded ( DBRecord record );

	public abstract ViewSelector getViewSelector ( );

	public abstract URLsIO getURLsIO ( );

}

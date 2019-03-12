/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import com.github.vbsw.urlsaver.db.DBURLs;


/**
 * @author Vitali Baumtrok
 */
public interface IGUI {

	public void quit ( );

	public void confirmAny ( );

	public void refreshFileSelection ( );

	public void refreshFileInfo ( );

	public void refreshFileListView ( );

	public void refreshTitle ( );

	public void dbURLsLoadingFinished ( DBURLs record );

	public IViewSelector getViewSelector ( );

}

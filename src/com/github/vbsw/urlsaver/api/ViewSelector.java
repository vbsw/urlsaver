/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


/**
 * @author Vitali Baumtrok
 */
public abstract class ViewSelector {

	public abstract void selectFilesView ( );

	public abstract void selectURLsView ( );

	public abstract void selectSettingsView ( );

	public abstract void selectAboutView ( );

}

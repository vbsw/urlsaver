/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.util.List;


/**
 * @author Vitali Baumtrok
 */
public abstract class Global {

	public abstract ResourceLoader getResourceLoader ( );

	public abstract Preferences getPreferences ( );

	public abstract TextGenerator getTextGenerator ( );

	public abstract DataBase getDataBase ( );

	public abstract GUI getGUI ( );

	public abstract URLMeta getURLMeta ( );

	public abstract List<String> getArguments ( );

	public abstract Properties getProperties ( );

}

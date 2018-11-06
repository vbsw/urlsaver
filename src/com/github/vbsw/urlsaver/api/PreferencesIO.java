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
public abstract class PreferencesIO {

	public abstract void createPreferencesFile ( );

	public abstract void createCSSFile ( );

	public abstract void createFXMLFile ( );

	public abstract void overwritePreferencesFile ( );

	public abstract void overwriteCSSFile ( );

	public abstract void overwriteFXMLFile ( );
}

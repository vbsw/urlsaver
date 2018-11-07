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
public abstract class SettingsIO {

	public abstract void createSettingsFile ( );

	public abstract void createCSSFile ( );

	public abstract void createFXMLFile ( );

	public abstract void overwriteSettingsFile ( );

	public abstract void overwriteCSSFile ( );

	public abstract void overwriteFXMLFile ( );
}

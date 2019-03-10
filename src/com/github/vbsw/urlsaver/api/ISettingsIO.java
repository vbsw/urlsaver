/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


/**
 * @author Vitali Baumtrok
 */
public interface ISettingsIO {

	public void createSettingsFile ( );

	public void createCSSFile ( );

	public void createFXMLFile ( );

	public void overwriteSettingsFile ( );

	public void overwriteCSSFile ( );

	public void overwriteFXMLFile ( );
}

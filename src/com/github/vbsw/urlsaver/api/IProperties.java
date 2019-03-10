/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import javafx.beans.property.SimpleBooleanProperty;


/**
 * @author Vitali Baumtrok
 */
public interface IProperties {

	public SimpleBooleanProperty selectedFileDirtyProperty ( );

	public SimpleBooleanProperty confirmingSaveProperty ( );

	public SimpleBooleanProperty confirmingQuitProperty ( );

	public SimpleBooleanProperty customFXMLLoadedProperty ( );

	public SimpleBooleanProperty customCSSLoadedProperty ( );

	public SimpleBooleanProperty customSettingsLoadedProperty ( );

	public SimpleBooleanProperty customSettingsSavedProperty ( );

}

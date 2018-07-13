/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.pref;


/**
 * @author Vitali Baumtrok
 */
public class PreferencesStringValue {

	private String defaultValue;
	private String savedValue;
	private String modifiedValue;

	public String getDefaultValue ( ) {
		return defaultValue;
	}

	public void setDefaultValue ( final String defaultValue ) {
		this.defaultValue = defaultValue;
	}

	public String getSavedValue ( ) {
		return savedValue;
	}

	public void setSavedValue ( final String savedValue ) {
		this.savedValue = savedValue;
	}

	public String getModifiedValue ( ) {
		return modifiedValue;
	}

	public void setModifiedValue ( final String modifiedValue ) {
		this.modifiedValue = modifiedValue;
	}

	public boolean isSaved ( ) {
		return savedValue.equals(modifiedValue);
	}

	public void resetModifiedValueToSaved ( ) {
		modifiedValue = savedValue;
	}

	public void resetSavedValueToDefault ( ) {
		savedValue = defaultValue;
	}

	public void resetSavedValueToModified ( ) {
		savedValue = modifiedValue;
	}

}

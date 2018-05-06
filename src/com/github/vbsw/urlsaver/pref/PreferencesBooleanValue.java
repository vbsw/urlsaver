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
public class PreferencesBooleanValue {

	private boolean defaultValue;
	private boolean savedValue;
	private boolean modifiedValue;

	public boolean getDefaultValue ( ) {
		return defaultValue;
	}

	public void setDefaultValue ( final boolean defaultValue ) {
		this.defaultValue = defaultValue;
	}

	public boolean getSavedValue ( ) {
		return savedValue;
	}

	public void setSavedValue ( final boolean savedValue ) {
		this.savedValue = savedValue;
	}

	public boolean getModifiedValue ( ) {
		return modifiedValue;
	}

	public void setModifiedValue ( final boolean modifiedValue ) {
		this.modifiedValue = modifiedValue;
	}

	public boolean isSaved ( ) {
		return savedValue == modifiedValue;
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

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
public class PreferencesIntValue {

	private int defaultValue;
	private int savedValue;
	private int modifiedValue;

	public int getDefaultValue ( ) {
		return defaultValue;
	}

	public void setDefaultValue ( final int defaultValue ) {
		this.defaultValue = defaultValue;
	}

	public int getSavedValue ( ) {
		return savedValue;
	}

	public void setSavedValue ( final int savedValue ) {
		this.savedValue = savedValue;
	}

	public int getModifiedValue ( ) {
		return modifiedValue;
	}

	public void setModifiedValue ( final int modifiedValue ) {
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

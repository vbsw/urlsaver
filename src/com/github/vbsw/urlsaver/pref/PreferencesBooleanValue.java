/*
 *    Copyright 2017, Vitali Baumtrok (vbsw@mailbox.org).
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
	private boolean customValue;

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

	public boolean getCustomValue ( ) {
		return customValue;
	}

	public void setCustomValue ( final boolean customValue ) {
		this.customValue = customValue;
	}

	public boolean isSaved ( ) {
		return savedValue == customValue;
	}

	protected void resetCustomValueToDefault ( ) {
		customValue = defaultValue;
	}

	protected void resetSavedValueToDefault ( ) {
		savedValue = defaultValue;
	}

	protected void resetSavedValueToCustom ( ) {
		savedValue = customValue;
	}

}

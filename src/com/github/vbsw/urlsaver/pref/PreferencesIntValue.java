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
public class PreferencesIntValue {

	private int defaultValue;
	private int savedValue;
	private int customValue;

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

	public int getCustomValue ( ) {
		return customValue;
	}

	public void setCustomValue ( final int customValue ) {
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

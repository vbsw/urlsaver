/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.pref;


import com.github.vbsw.urlsaver.resources.Resource;


/**
 * @author Vitali Baumtrok
 */
public class PreferencesResourceValue {

	private Resource defaultValue;
	private Resource savedValue;
	private Resource modifiedValue;

	public Resource getDefaultValue ( ) {
		return defaultValue;
	}

	public void setDefaultValue ( final Resource defaultValue ) {
		this.defaultValue = defaultValue;
	}

	public Resource getSavedValue ( ) {
		return savedValue;
	}

	public void setSavedValue ( final Resource savedValue ) {
		this.savedValue = savedValue;
	}

	public Resource getModifiedValue ( ) {
		return modifiedValue;
	}

	public void setModifiedValue ( final Resource modifiedValue ) {
		this.modifiedValue = modifiedValue;
	}

	public boolean isSaved ( ) {
		return savedValue.equals(modifiedValue);
	}

	public void resetModifiedValueToDefault ( ) {
		modifiedValue = defaultValue;
	}

	public void resetSavedValueToDefault ( ) {
		savedValue = defaultValue;
	}

	public void resetSavedValueToModified ( ) {
		savedValue = modifiedValue;
	}

}

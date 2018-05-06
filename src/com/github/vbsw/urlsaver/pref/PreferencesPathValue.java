/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.urlsaver.pref;

import java.nio.file.Path;

/**
 * @author Vitali Baumtrok
 */
public class PreferencesPathValue {

	private Path defaultValue;
	private Path savedValue;
	private Path modifiedValue;

	public Path getDefaultValue ( ) {
		return defaultValue;
	}

	public void setDefaultValue ( final Path defaultValue ) {
		this.defaultValue = defaultValue;
	}

	public Path getSavedValue ( ) {
		return savedValue;
	}

	public void setSavedValue ( final Path savedValue ) {
		this.savedValue = savedValue;
	}

	public Path getModifiedValue ( ) {
		return modifiedValue;
	}

	public void setModifiedValue ( final Path modifiedValue ) {
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


//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.pref;


/**
 * @author Vitali Baumtrok
 */
public class PreferencesStringValue {

	private String defaultValue;
	private String savedValue;
	private String customValue;

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

	public String getCustomValue ( ) {
		return customValue;
	}

	public void setCustomValue ( final String customValue ) {
		this.customValue = customValue;
	}

	public boolean isSaved ( ) {
		return savedValue.equals(customValue);
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

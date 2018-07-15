/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.util.List;


/**
 * @author Vitali Baumtrok
 */
public abstract class Preferences {

	public abstract void initialize ( ResourceLoader resources, List<String> args );

	public abstract void loadCustomPreferences ( );

	public abstract ResourceVariants getPreferences ( );

	public abstract ResourceVariants getFXML ( );

	public abstract ResourceVariants getCSS ( );

	public abstract PreferencesStringValue getStringValue ( final int id );

	public abstract PreferencesIntValue getIntValue ( final int id );

	public abstract PreferencesBooleanValue getBooleanValue ( final int id );

	public abstract void resetSavedToDefault ( );

	public abstract void resetSavedToModified ( );

	public abstract void resetModifiedValuesToSaved ( );

	public abstract boolean isCustomPreferencesLoaded ( );

	public abstract boolean isCustomPreferencesSaved ( );

	public abstract boolean isCustomFXMLLoaded ( );

	public abstract void setCustomFXMLLoaded ( final boolean loaded );

	public abstract boolean isCustomCSSLoaded ( );

	public abstract void setCustomCSSLoaded ( final boolean loaded );

	public abstract void savePreferences ( );

	public class PreferencesBooleanValue {

		private boolean defaultValue;
		private boolean savedValue;
		private boolean modifiedValue;

		public boolean getDefault ( ) {
			return defaultValue;
		}

		public void setDefault ( final boolean defaultValue ) {
			this.defaultValue = defaultValue;
		}

		public boolean getSaved ( ) {
			return savedValue;
		}

		public void setSaved ( final boolean savedValue ) {
			this.savedValue = savedValue;
		}

		public boolean getModified ( ) {
			return modifiedValue;
		}

		public void setModified ( final boolean modifiedValue ) {
			this.modifiedValue = modifiedValue;
		}

		public boolean isSaved ( ) {
			return savedValue == modifiedValue;
		}

		public void resetModifiedToSaved ( ) {
			modifiedValue = savedValue;
		}

		public void resetSavedToDefault ( ) {
			savedValue = defaultValue;
		}

		public void resetSavedToModified ( ) {
			savedValue = modifiedValue;
		}

	}

	public class PreferencesIntValue {

		private int defaultValue;
		private int savedValue;
		private int modifiedValue;

		public int getDefault ( ) {
			return defaultValue;
		}

		public void setDefault ( final int defaultValue ) {
			this.defaultValue = defaultValue;
		}

		public int getSaved ( ) {
			return savedValue;
		}

		public void setSaved ( final int savedValue ) {
			this.savedValue = savedValue;
		}

		public int getModified ( ) {
			return modifiedValue;
		}

		public void setModified ( final int modifiedValue ) {
			this.modifiedValue = modifiedValue;
		}

		public boolean isSaved ( ) {
			return savedValue == modifiedValue;
		}

		public void resetModifiedValueToSaved ( ) {
			modifiedValue = savedValue;
		}

		public void resetSavedToDefault ( ) {
			savedValue = defaultValue;
		}

		public void resetSavedToModified ( ) {
			savedValue = modifiedValue;
		}

	}

	public class PreferencesStringValue {

		private String defaultValue;
		private String savedValue;
		private String modifiedValue;

		public String getDefault ( ) {
			return defaultValue;
		}

		public void setDefault ( final String defaultValue ) {
			this.defaultValue = defaultValue;
		}

		public String getSaved ( ) {
			return savedValue;
		}

		public void setSaved ( final String savedValue ) {
			this.savedValue = savedValue;
		}

		public String getModified ( ) {
			return modifiedValue;
		}

		public void setModified ( final String modifiedValue ) {
			this.modifiedValue = modifiedValue;
		}

		public boolean isSaved ( ) {
			return savedValue.equals(modifiedValue);
		}

		public void resetModifiedToSaved ( ) {
			modifiedValue = savedValue;
		}

		public void resetSavedToDefault ( ) {
			savedValue = defaultValue;
		}

		public void resetSavedToModified ( ) {
			savedValue = modifiedValue;
		}

	}

}

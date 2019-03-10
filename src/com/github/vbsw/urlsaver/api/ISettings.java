/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.nio.charset.Charset;


/**
 * @author Vitali Baumtrok
 */
public interface ISettings {

	public Charset getDefaultCharset ( );

	public String[] getAppArguments ( );

	public StringProperty getStringProperty ( String property );

	public IntProperty getIntProperty ( String property );

	public BooleanProperty getBooleanProperty ( String property );

	public void reloadSettings ( );

	public void saveSettings ( );

	public void reloadFXMLRessource ( );

	public void reloadCSSRessource ( );

	public void reloadSettingsRessource ( );

	public void setSavedValuesToDefault ( );

	public void setSavedValuesToModified ( );

	public void setModifiedValuesToSaved ( );

	public IResource getDefaultFXMLResource ( );

	public IResource getDefaultCSSResource ( );

	public IResource getDefaultSettingsResource ( );

	public IResource getFXMLResource ( );

	public IResource getCSSResource ( );

	public IResource getSettingsResource ( );

	public static final class Property {

		public static final String windowTitle = "window.title";
		public static final String windowWidth = "window.width";
		public static final String windowHeight = "window.height";
		public static final String windowMaximized = "window.maximized";
		public static final String urlsFileExtension = "urls.file.extension";
		public static final String urlsFileSelect = "urls.file.select";
		public static final String urlsFileAutoLoadAll = "urls.file.autoload.all";
		public static final String searchByPrefix = "search.byprefix";

	}

	public final class BooleanProperty {

		public boolean defaultValue;
		public boolean savedValue;
		public boolean modifiedValue;

		public boolean isSaved ( ) {
			return savedValue == modifiedValue;
		}

		public void setModifiedToSaved ( ) {
			modifiedValue = savedValue;
		}

		public void setSavedToDefault ( ) {
			savedValue = defaultValue;
		}

		public void setSavedToModified ( ) {
			savedValue = modifiedValue;
		}

	}

	public final class IntProperty {

		public int defaultValue;
		public int savedValue;
		public int modifiedValue;

		public boolean isSaved ( ) {
			return savedValue == modifiedValue;
		}

		public void setModifiedToSaved ( ) {
			modifiedValue = savedValue;
		}

		public void setSavedToDefault ( ) {
			savedValue = defaultValue;
		}

		public void setSavedToModified ( ) {
			savedValue = modifiedValue;
		}

	}

	public final class StringProperty {

		public String defaultValue;
		public String savedValue;
		public String modifiedValue;

		public boolean isSaved ( ) {
			return savedValue == modifiedValue;
		}

		public void setModifiedToSaved ( ) {
			modifiedValue = savedValue;
		}

		public void setSavedToDefault ( ) {
			savedValue = defaultValue;
		}

		public void setSavedToModified ( ) {
			savedValue = modifiedValue;
		}

	}

}

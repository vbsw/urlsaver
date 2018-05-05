/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.logic;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;


/**
 * @author Vitali Baumtrok
 */
public class Properties {

	protected static final SimpleBooleanProperty confirmQuitApp = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty selected = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty confirmingSave = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty selectedFileDirty = new SimpleBooleanProperty();

	protected static final SimpleBooleanProperty urlsAvailable = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty urlExists = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty urlDeleteRequested = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty urlModified = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty urlTagsModified = new SimpleBooleanProperty();

	protected static final SimpleBooleanProperty confirmingCreatePreferences = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty confirmingCreateCSS = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty confirmingCreateFXML = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty propertiesModified = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty titleChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty widthChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty heightChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty fileExtensionChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty defaultFileChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty maximizeChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty loadAtStartChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty byPrefixChanged = new SimpleBooleanProperty();

	public static ObservableBooleanValue confirmQuitAppProperty ( ) {
		return confirmQuitApp;
	}

	public static ObservableBooleanValue selectedProperty ( ) {
		return selected;
	}

	public static ObservableBooleanValue confirmingSaveProperty ( ) {
		return confirmingSave;
	}

	public static ObservableBooleanValue selectedFileDirtyProperty ( ) {
		return selectedFileDirty;
	}

	public static ObservableBooleanValue existsProperty ( ) {
		return urlExists;
	}

	public static ObservableBooleanValue urlDeleteRequestedProperty ( ) {
		return urlDeleteRequested;
	}

	public static ObservableBooleanValue urlModifiedProperty ( ) {
		return urlModified;
	}

	public static ObservableBooleanValue availableProperty ( ) {
		return urlsAvailable;
	}

	public static ObservableBooleanValue urlTagsModifiedProperty ( ) {
		return urlTagsModified;
	}

	public static SimpleBooleanProperty confirmingCreatePreferencesProperty ( ) {
		return confirmingCreatePreferences;
	}

	public static SimpleBooleanProperty confirmingCreateCSSProperty ( ) {
		return confirmingCreateCSS;
	}

	public static SimpleBooleanProperty confirmingCreateFXMLProperty ( ) {
		return confirmingCreateFXML;
	}

	public static SimpleBooleanProperty propertiesModifiedProperty ( ) {
		return propertiesModified;
	}

	public static SimpleBooleanProperty titleChangedProperty ( ) {
		return titleChanged;
	}

	public static SimpleBooleanProperty widthChangedProperty ( ) {
		return widthChanged;
	}

	public static SimpleBooleanProperty heightChangedProperty ( ) {
		return heightChanged;
	}

	public static SimpleBooleanProperty fileExtensionChangedProperty ( ) {
		return fileExtensionChanged;
	}

	public static SimpleBooleanProperty defaultFileChangedProperty ( ) {
		return defaultFileChanged;
	}

	public static SimpleBooleanProperty maximizeChangedProperty ( ) {
		return maximizeChanged;
	}

	public static SimpleBooleanProperty loadAtStartChangedProperty ( ) {
		return loadAtStartChanged;
	}

	public static SimpleBooleanProperty byPrefixChangedProperty ( ) {
		return byPrefixChanged;
	}

}

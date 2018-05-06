/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import javafx.beans.property.SimpleBooleanProperty;


/**
 * @author Vitali Baumtrok
 */
public class Properties {

	protected static final SimpleBooleanProperty confirmingQuitApp = new SimpleBooleanProperty();
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
	protected static final SimpleBooleanProperty preferencesModified = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty titleChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty widthChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty heightChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty urlsFileExtensionChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty urlsFileSelectChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty maximizeChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty loadAtStartChanged = new SimpleBooleanProperty();
	protected static final SimpleBooleanProperty byPrefixChanged = new SimpleBooleanProperty();

	public static SimpleBooleanProperty confirmingQuitAppProperty ( ) {
		return confirmingQuitApp;
	}

	public static SimpleBooleanProperty selectedProperty ( ) {
		return selected;
	}

	public static SimpleBooleanProperty confirmingSaveProperty ( ) {
		return confirmingSave;
	}

	public static SimpleBooleanProperty selectedFileDirtyProperty ( ) {
		return selectedFileDirty;
	}

	public static SimpleBooleanProperty urlExistsProperty ( ) {
		return urlExists;
	}

	public static SimpleBooleanProperty urlDeleteRequestedProperty ( ) {
		return urlDeleteRequested;
	}

	public static SimpleBooleanProperty urlModifiedProperty ( ) {
		return urlModified;
	}

	public static SimpleBooleanProperty availableProperty ( ) {
		return urlsAvailable;
	}

	public static SimpleBooleanProperty urlTagsModifiedProperty ( ) {
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

	public static SimpleBooleanProperty preferencesModifiedProperty ( ) {
		return preferencesModified;
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

	public static SimpleBooleanProperty urlsFileExtensionChangedProperty ( ) {
		return urlsFileExtensionChanged;
	}

	public static SimpleBooleanProperty urlsFileSelectChangedProperty ( ) {
		return urlsFileSelectChanged;
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

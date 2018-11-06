/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Properties;

import javafx.beans.property.SimpleBooleanProperty;


/**
 * @author Vitali Baumtrok
 */
public class StdProperties extends Properties {

	private final SimpleBooleanProperty confirmingQuitApp = new SimpleBooleanProperty();
	private final SimpleBooleanProperty selected = new SimpleBooleanProperty();
	private final SimpleBooleanProperty confirmingSave = new SimpleBooleanProperty();
	private final SimpleBooleanProperty selectedFileDirty = new SimpleBooleanProperty();

	private final SimpleBooleanProperty urlsAvailable = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlExists = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlDeleteRequested = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlModified = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlTagsModified = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlScoreModified = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlDateModified = new SimpleBooleanProperty();

	private final SimpleBooleanProperty confirmingCreatePreferences = new SimpleBooleanProperty();
	private final SimpleBooleanProperty confirmingCreateCSS = new SimpleBooleanProperty();
	private final SimpleBooleanProperty confirmingCreateFXML = new SimpleBooleanProperty();
	private final SimpleBooleanProperty preferencesModified = new SimpleBooleanProperty();
	private final SimpleBooleanProperty titleChanged = new SimpleBooleanProperty();
	private final SimpleBooleanProperty widthChanged = new SimpleBooleanProperty();
	private final SimpleBooleanProperty heightChanged = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlsFileExtensionChanged = new SimpleBooleanProperty();
	private final SimpleBooleanProperty urlsFileSelectChanged = new SimpleBooleanProperty();
	private final SimpleBooleanProperty maximizeChanged = new SimpleBooleanProperty();
	private final SimpleBooleanProperty loadAtStartChanged = new SimpleBooleanProperty();
	private final SimpleBooleanProperty byPrefixChanged = new SimpleBooleanProperty();
	private final SimpleBooleanProperty createDefaultFilePossible = new SimpleBooleanProperty();

	public SimpleBooleanProperty selectedProperty ( ) {
		return selected;
	}

	@Override
	public SimpleBooleanProperty confirmingSaveProperty ( ) {
		return confirmingSave;
	}

	@Override
	public SimpleBooleanProperty selectedFileDirtyProperty ( ) {
		return selectedFileDirty;
	}

	@Override
	public SimpleBooleanProperty confirmingQuitProperty ( ) {
		return confirmingQuitApp;
	}

	public SimpleBooleanProperty urlExistsProperty ( ) {
		return urlExists;
	}

	public SimpleBooleanProperty urlDeleteRequestedProperty ( ) {
		return urlDeleteRequested;
	}

	public SimpleBooleanProperty urlModifiedProperty ( ) {
		return urlModified;
	}

	public SimpleBooleanProperty urlsAvailableProperty ( ) {
		return urlsAvailable;
	}

	public SimpleBooleanProperty urlTagsModifiedProperty ( ) {
		return urlTagsModified;
	}

	public SimpleBooleanProperty urlScoreModifiedProperty ( ) {
		return urlScoreModified;
	}

	public SimpleBooleanProperty urlDateModifiedProperty ( ) {
		return urlDateModified;
	}

	public SimpleBooleanProperty confirmingCreatePreferencesProperty ( ) {
		return confirmingCreatePreferences;
	}

	public SimpleBooleanProperty confirmingCreateCSSProperty ( ) {
		return confirmingCreateCSS;
	}

	public SimpleBooleanProperty confirmingCreateFXMLProperty ( ) {
		return confirmingCreateFXML;
	}

	public SimpleBooleanProperty preferencesModifiedProperty ( ) {
		return preferencesModified;
	}

	public SimpleBooleanProperty titleChangedProperty ( ) {
		return titleChanged;
	}

	public SimpleBooleanProperty widthChangedProperty ( ) {
		return widthChanged;
	}

	public SimpleBooleanProperty heightChangedProperty ( ) {
		return heightChanged;
	}

	public SimpleBooleanProperty urlsFileExtensionChangedProperty ( ) {
		return urlsFileExtensionChanged;
	}

	public SimpleBooleanProperty urlsFileSelectChangedProperty ( ) {
		return urlsFileSelectChanged;
	}

	public SimpleBooleanProperty maximizeChangedProperty ( ) {
		return maximizeChanged;
	}

	public SimpleBooleanProperty loadAtStartChangedProperty ( ) {
		return loadAtStartChanged;
	}

	public SimpleBooleanProperty byPrefixChangedProperty ( ) {
		return byPrefixChanged;
	}

	public SimpleBooleanProperty createDefaultFilePossibleProperty ( ) {
		return createDefaultFilePossible;
	}

	public void refreshPreferencesModifiedProperty ( ) {
		final boolean modified = titleChanged.get() || widthChanged.get() || heightChanged.get() || urlsFileExtensionChanged.get() || urlsFileSelectChanged.get() || maximizeChanged.get() || loadAtStartChanged.get() || byPrefixChanged.get();
		preferencesModified.set(modified);
	}

}

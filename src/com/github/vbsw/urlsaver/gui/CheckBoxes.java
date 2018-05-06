/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;


/**
 * @author Vitali Baumtrok
 */
public class CheckBoxes {

	public static final Maximize maximize = new Maximize();
	public static final LoadAtStart urlsFileAutoloadAll = new LoadAtStart();
	public static final ByPrefix byPrefix = new ByPrefix();

	public static void build ( final Parent root ) {
		maximize.build(root);
		urlsFileAutoloadAll.build(root);
		byPrefix.build(root);
	}

	public static class CustomCheckBox {
		public CheckBox control;

		public void setFontWeight ( final boolean bold ) {
			final String newStyle = bold ? "-fx-font-weight:bold;" : "-fx-font-weight:normal;";
			control.setStyle(newStyle);
		}
	}

	private static void maximize_changed ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		final boolean valueChanged = Preferences.getWindowMaximized().getSavedValue() != newValue;
		Preferences.getWindowMaximized().setModifiedValue(newValue);
		CheckBoxes.maximize.setFontWeight(valueChanged);
		Properties.maximizeChangedProperty().set(valueChanged);
	}

	private static void loadAtStart_changed ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		final boolean valueChanged = Preferences.getURLsFileAutoloadAll().getSavedValue() != newValue;
		Preferences.getURLsFileAutoloadAll().setModifiedValue(newValue);
		CheckBoxes.urlsFileAutoloadAll.setFontWeight(valueChanged);
		Properties.loadAtStartChangedProperty().set(valueChanged);
	}

	private static void byPrefix_changed ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		final boolean valueChanged = Preferences.getSearchByPrefix().getSavedValue() != newValue;
		Preferences.getSearchByPrefix().setModifiedValue(newValue);
		CheckBoxes.byPrefix.setFontWeight(valueChanged);
		Properties.byPrefixChangedProperty().set(valueChanged);
	}

	public static final class Maximize extends CustomCheckBox {
		private void build ( final Parent root ) {
			control = (CheckBox) root.lookup("#preferences_maximize_cb");
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> CheckBoxes.maximize_changed(observable,oldValue,newValue));
		}
	}

	public static final class LoadAtStart extends CustomCheckBox {
		private void build ( final Parent root ) {
			control = (CheckBox) root.lookup("#preferences_load_at_start_cb");
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> CheckBoxes.loadAtStart_changed(observable,oldValue,newValue));
		}
	}

	public static final class ByPrefix extends CustomCheckBox {
		private void build ( final Parent root ) {
			control = (CheckBox) root.lookup("#preferences_by_prefix_cb");
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> CheckBoxes.byPrefix_changed(observable,oldValue,newValue));
		}
	}

}

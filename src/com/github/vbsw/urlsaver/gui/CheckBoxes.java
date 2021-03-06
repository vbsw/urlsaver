/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.Settings.BooleanSetting;
import com.github.vbsw.urlsaver.settings.SettingsConfig;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;


/**
 * @author Vitali Baumtrok
 */
public class CheckBoxes {

	public final Maximize maximize = new Maximize();
	public final LoadAtStart urlsFileAutoloadAll = new LoadAtStart();
	public final ByPrefix byPrefix = new ByPrefix();

	protected StdGUI stdGUI;

	public CheckBoxes ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
		maximize.build(root);
		urlsFileAutoloadAll.build(root);
		byPrefix.build(root);
	}

	public class CustomCheckBox {
		public CheckBox control;

		public void setFontWeight ( final boolean bold ) {
			final String newStyle = bold ? "-fx-font-weight:bold;" : "-fx-font-weight:normal;";
			control.setStyle(newStyle);
		}
	}

	private void maximize_changed ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		final BooleanSetting windowMaximizedValue = Global.settings.getBooleanSetting(SettingsConfig.WINDOW_MAXIMIZED_ID);
		final boolean valueChanged = windowMaximizedValue.getSaved() != newValue;
		windowMaximizedValue.setModified(newValue);
		maximize.setFontWeight(valueChanged);
		stdGUI.properties.maximizeChangedProperty().set(valueChanged);
		stdGUI.properties.refreshSettingsModifiedProperty();
	}

	private void loadAtStart_changed ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		final BooleanSetting urlsFileAutoloadAllValue = Global.settings.getBooleanSetting(SettingsConfig.URLS_FILE_AUTOLOAD_ALL_ID);
		final boolean valueChanged = urlsFileAutoloadAllValue.getSaved() != newValue;
		urlsFileAutoloadAllValue.setModified(newValue);
		urlsFileAutoloadAll.setFontWeight(valueChanged);
		stdGUI.properties.loadAtStartChangedProperty().set(valueChanged);
		stdGUI.properties.refreshSettingsModifiedProperty();
	}

	private void byPrefix_changed ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		final BooleanSetting searchByPrefixValue = Global.settings.getBooleanSetting(SettingsConfig.SEARCH_BY_PREFIX_ID);
		final boolean valueChanged = searchByPrefixValue.getSaved() != newValue;
		searchByPrefixValue.setModified(newValue);
		byPrefix.setFontWeight(valueChanged);
		stdGUI.properties.byPrefixChangedProperty().set(valueChanged);
		stdGUI.properties.refreshSettingsModifiedProperty();
	}

	public final class Maximize extends CustomCheckBox {
		private void build ( final Parent root ) {
			control = (CheckBox) root.lookup("#settings_maximize_cb");
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> maximize_changed(observable,oldValue,newValue));
		}
	}

	public final class LoadAtStart extends CustomCheckBox {
		private void build ( final Parent root ) {
			control = (CheckBox) root.lookup("#settings_load_at_start_cb");
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> loadAtStart_changed(observable,oldValue,newValue));
		}
	}

	public final class ByPrefix extends CustomCheckBox {
		private void build ( final Parent root ) {
			control = (CheckBox) root.lookup("#settings_by_prefix_cb");
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> byPrefix_changed(observable,oldValue,newValue));
		}
	}

}

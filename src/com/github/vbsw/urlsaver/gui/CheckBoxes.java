/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.ISettings;

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

	protected GUI stdGUI;

	public CheckBoxes ( final GUI stdGUI ) {
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
		final ISettings.BooleanProperty windowMaximizedValue = Global.settings.getBooleanProperty(ISettings.Property.windowMaximized);
		final boolean valueChanged = windowMaximizedValue.savedValue != newValue;
		windowMaximizedValue.modifiedValue = newValue;
		maximize.setFontWeight(valueChanged);
		stdGUI.properties.maximizeChangedProperty().set(valueChanged);
		stdGUI.properties.refreshSettingsModifiedProperty();
	}

	private void loadAtStart_changed ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		final ISettings.BooleanProperty urlsFileAutoloadAllValue = Global.settings.getBooleanProperty(ISettings.Property.urlsFileAutoLoadAll);
		final boolean valueChanged = urlsFileAutoloadAllValue.savedValue != newValue;
		urlsFileAutoloadAllValue.modifiedValue = newValue;
		urlsFileAutoloadAll.setFontWeight(valueChanged);
		stdGUI.properties.loadAtStartChangedProperty().set(valueChanged);
		stdGUI.properties.refreshSettingsModifiedProperty();
	}

	private void byPrefix_changed ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		final ISettings.BooleanProperty searchByPrefixValue = Global.settings.getBooleanProperty(ISettings.Property.searchByPrefix);
		final boolean valueChanged = searchByPrefixValue.savedValue != newValue;
		searchByPrefixValue.modifiedValue = newValue;
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

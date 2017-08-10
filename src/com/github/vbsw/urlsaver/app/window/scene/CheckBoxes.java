
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.scene;


import com.github.vbsw.urlsaver.app.App;

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;


/**
 * @author Vitali Baumtrok
 */
public class CheckBoxes {

	public final CheckBox maximize;
	public final CheckBox loadAtStart;
	public final CheckBox byPrefix;

	public CheckBoxes ( final Parent root ) {
		final String maximizeSelector = "#settings_maximize_cb"; //$NON-NLS-1$
		final String loadAtStartSelector = "#settings_load_at_start_cb"; //$NON-NLS-1$
		final String byPrefixSelector = "#settings_by_prefix_cb"; //$NON-NLS-1$

		maximize = (CheckBox) root.lookup(maximizeSelector);
		loadAtStart = (CheckBox) root.lookup(loadAtStartSelector);
		byPrefix = (CheckBox) root.lookup(byPrefixSelector);
	}

	public void configure ( ) {
		maximize.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> App.settings.vm.textField_maximize_changed(observable,oldValue,newValue));
		loadAtStart.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> App.settings.vm.textField_loadAtStart_changed(observable,oldValue,newValue));
		byPrefix.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> App.settings.vm.textField_byPrefix_changed(observable,oldValue,newValue));
	}

}

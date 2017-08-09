
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.settings;


import com.github.vbsw.urlsaver.app.App;

import javafx.event.ActionEvent;


/**
 * @author Vitali Baumtrok
 */
public class SettingsViewModel {

	public void button_reloadSettingsFile_clicked ( final ActionEvent event ) {
		if ( App.files.isAnyDirty() == false ) {
			App.settings.loadCustomValues();
			App.settings.setAllToCustom();
			App.settings.updateView();

			App.files.initialize();
			App.scene.lv.files.getItems().clear();
			App.scene.lv.files.getItems().addAll(App.files.getPaths());
			App.files.selectDefault();
			App.files.processAutoLoad();
			App.scene.tp.top.getSelectionModel().select(App.scene.topTab.settings);

			App.window.updateTitle();

			if ( App.window.isMaximized() != App.settings.isCustomWindowMaximized() ) {
				App.window.setMaximized(App.settings.isCustomWindowMaximized());

			} else {
				App.window.setSize(App.settings.getCustomWindowWidth(),App.settings.getCustomWindowHeight());
			}

		} else {
			App.settings.log("can't load settings; urls not saved");
		}
	}

	public void button_reloadCSSFile_clicked ( final ActionEvent event ) {
		App.scene.loadCSS();
	}

	public void button_reloadFXMLFile_clicked ( final ActionEvent event ) {
		App.scene.loadFXML();
		App.scene.tp.top.getSelectionModel().select(App.scene.topTab.settings);
	}

}

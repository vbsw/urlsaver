
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.scene;


import com.github.vbsw.urlsaver.app.App;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Tab;


/**
 * @author Vitali Baumtrok
 */
public final class TopTabs {

	public final Tab files;
	public final Tab urls;
	public final Tab about;
	public final Tab settings;

	TopTabs ( final Parent root, final TabPanes tp ) {
		final String filesTabId = "files_tab"; //$NON-NLS-1$
		final String urlsTabId = "urls_tab"; //$NON-NLS-1$
		final String aboutTabId = "about_tab"; //$NON-NLS-1$
		final String settingsTabId = "settings_tab"; //$NON-NLS-1$

		files = tp.getTopPaneTab(filesTabId);
		urls = tp.getTopPaneTab(urlsTabId);
		about = tp.getTopPaneTab(aboutTabId);
		settings = tp.getTopPaneTab(settingsTabId);
	}

	void configure ( ) {
		urls.disableProperty().bind(Bindings.not(App.urls.vm.availableProperty()));
		urls.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> App.urls.vm.topTab_urls_selected(observable,oldValue,newValue));
	}

}

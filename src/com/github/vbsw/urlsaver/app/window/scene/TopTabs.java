/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
 * 
 * This file is part of URL Saver.
 * 
 * URL Saver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * URL Saver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with URL Saver.  If not, see <http://www.gnu.org/licenses/>.
 */


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
		urls.disableProperty().bind(Bindings.not(App.urls.mv.availableProperty()));
		urls.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> App.urls.mv.listViewItem_selected(observable,oldValue,newValue));
	}

}

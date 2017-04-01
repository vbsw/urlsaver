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

import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


/**
 * @author Vitali Baumtrok
 */
public final class TabPanes {

	public final TabPane top;

	TabPanes ( final Parent root ) {
		final String topTabPaneSelector = "#top_tab_pane"; //$NON-NLS-1$

		top = (TabPane) root.lookup(topTabPaneSelector);
	}

	Tab getTopPaneTab ( final String tabId ) {
		for ( Tab tab: top.getTabs() ) {
			final String id = tab.getId();

			if ( (id != null) && (id.equals(tabId)) ) {
				return tab;
			}
		}
		return null;
	}

	public void configure ( ) {
		top.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) -> App.window.mv.topTab_selected(observable,oldValue,newValue));
	}

}

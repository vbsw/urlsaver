
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


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

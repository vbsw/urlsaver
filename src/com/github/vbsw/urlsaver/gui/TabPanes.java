/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.db.DB;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


/**
 * @author Vitali Baumtrok
 */
public class TabPanes {

	public static final Top top = new Top();

	public static void build ( final Parent root ) {
		top.build(root);
	}

	private static void topTab_selected ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) {
		if ( newValue == top.about.control )
			Properties.confirmingQuitAppProperty().set(!DB.isSaved());
		else
			Properties.confirmingQuitAppProperty().set(false);
	}

	private static void topTab_urls_selected ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		if ( newValue ) {
			if ( ListViews.urls.control.getItems().isEmpty() == false )
				ListViews.urls.control.requestFocus();
			else
				TextFields.urlSearch.control.requestFocus();
		}
	}

	private static Tab getTab ( final TabPane tabPane, final String tabId ) {
		for ( Tab tab: tabPane.getTabs() ) {
			final String id = tab.getId();
			if ( (id != null) && (id.equals(tabId)) )
				return tab;
		}
		return null;
	}

	public static class CustomTab {
		public Tab control;
	}

	public static final class Top {
		public final Files files = new Files();
		public final URLs urls = new URLs();
		public final About about = new About();
		public final Preferences preferences = new Preferences();
		public TabPane control;

		private void build ( final Parent root ) {
			control = (TabPane) root.lookup("#top_tab_pane");
			files.build(control,root);
			urls.build(control,root);
			about.build(control,root);
			preferences.build(control,root);
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) -> TabPanes.topTab_selected(observable,oldValue,newValue));
		}
	}

	public static final class Files extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = TabPanes.getTab(topPane,"files_tab");
		}
	}

	public static final class URLs extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = TabPanes.getTab(topPane,"urls_tab");
			control.disableProperty().bind(Bindings.not(Properties.availableProperty()));
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> TabPanes.topTab_urls_selected(observable,oldValue,newValue));
		}
	}

	public static final class About extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = TabPanes.getTab(topPane,"about_tab");
		}
	}

	public static final class Preferences extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = TabPanes.getTab(topPane,"preferences_tab");
		}

		public void select ( ) {
			TabPanes.top.control.getSelectionModel().select(control);
			TabPanes.top.control.requestFocus();
		}
	}

}

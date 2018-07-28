/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;


/**
 * @author Vitali Baumtrok
 */
public class TabPanes {

	public final Top top = new Top();
	protected StdGUI stdGUI;

	public TabPanes ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void build ( final Parent root ) {
		top.build(root);
	}

	private void topTab_selected ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) {
		if ( newValue == top.about.control )
			stdGUI.properties.confirmingQuitProperty().set(!stdGUI.global.getDataBase().isSaved());
		else
			stdGUI.properties.confirmingQuitProperty().set(false);
	}

	private void topTab_urls_selected ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		if ( newValue ) {
			if ( stdGUI.listViews.urls.control.getItems().isEmpty() == false )
				stdGUI.listViews.urls.control.requestFocus();
			else
				stdGUI.textFields.urlSearch.control.requestFocus();
		}
	}

	private Tab getTab ( final TabPane tabPane, final String tabId ) {
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

	public final class Top {
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
			control.getSelectionModel().selectedItemProperty().addListener( ( ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue ) -> stdGUI.tabPanes.topTab_selected(observable,oldValue,newValue));
		}
	}

	public final class Files extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = stdGUI.tabPanes.getTab(topPane,"files_tab");
		}
	}

	public final class URLs extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = stdGUI.tabPanes.getTab(topPane,"urls_tab");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.availableProperty()));
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> stdGUI.tabPanes.topTab_urls_selected(observable,oldValue,newValue));
		}
	}

	public final class About extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = stdGUI.tabPanes.getTab(topPane,"about_tab");
		}
	}

	public final class Preferences extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = stdGUI.tabPanes.getTab(topPane,"preferences_tab");
		}

		public void select ( ) {
			stdGUI.tabPanes.top.control.getSelectionModel().select(control);
			stdGUI.tabPanes.top.control.requestFocus();
		}
	}

}

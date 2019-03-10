/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;

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
			stdGUI.properties.confirmingQuitProperty().set(!Global.db.isSaved());
		else
			stdGUI.properties.confirmingQuitProperty().set(false);
	}

	private void topTab_urls_selected ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) {
		if ( newValue ) {
			if ( stdGUI.tableViews.urls.control.getItems().isEmpty() == false )
				stdGUI.tableViews.urls.control.requestFocus();
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
		public final ImportUrls importUrls = new ImportUrls(); /* can't use "import" as designator */
		public final Settings settings = new Settings();
		public final About about = new About();
		public TabPane control;

		private void build ( final Parent root ) {
			control = (TabPane) root.lookup("#top_tab_pane");
			files.build(control,root);
			urls.build(control,root);
			importUrls.build(control,root);
			settings.build(control,root);
			about.build(control,root);
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
			control.disableProperty().bind(Bindings.not(stdGUI.properties.urlsAvailableProperty()));
			control.selectedProperty().addListener( ( ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue ) -> stdGUI.tabPanes.topTab_urls_selected(observable,oldValue,newValue));
			control.disableProperty().addListener( ( observable, oldValue, newValue ) -> stdGUI.clearURLsSearch(observable,oldValue,newValue));
		}
	}

	public final class ImportUrls extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = stdGUI.tabPanes.getTab(topPane,"import_tab");
			control.disableProperty().bind(Bindings.not(stdGUI.properties.urlsAvailableProperty()));
		}

		public void select ( ) {
			stdGUI.tabPanes.top.control.getSelectionModel().select(control);
			stdGUI.tabPanes.top.control.requestFocus();
		}
	}

	public final class Settings extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = stdGUI.tabPanes.getTab(topPane,"settings_tab");
		}

		public void select ( ) {
			stdGUI.tabPanes.top.control.getSelectionModel().select(control);
			stdGUI.tabPanes.top.control.requestFocus();
		}
	}

	public final class About extends CustomTab {
		private void build ( final TabPane topPane, final Parent root ) {
			control = stdGUI.tabPanes.getTab(topPane,"about_tab");
		}
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.IViewSelector;


/**
 * @author Vitali Baumtrok
 */
public class ViewSelector implements IViewSelector {

	protected GUI stdGUI;

	public ViewSelector ( final GUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	@Override
	public void selectFilesView ( ) {
		stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.files.control);
		if ( stdGUI.listViews.files.control.getSelectionModel().isEmpty() == false )
			stdGUI.listViews.files.control.requestFocus();
	}

	@Override
	public void selectURLsView ( ) {
		if ( stdGUI.tabPanes.top.urls.control.isDisable() == false )
			stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.urls.control);
	}

	@Override
	public void selectImportView ( ) {
		stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.importUrls.control);
	}

	@Override
	public void selectSettingsView ( ) {
		stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.settings.control);
	}

	@Override
	public void selectAboutView ( ) {
		stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.about.control);
	}

}

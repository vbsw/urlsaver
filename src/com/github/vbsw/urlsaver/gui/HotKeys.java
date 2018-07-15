/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.db.DBRecord;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;


/**
 * @author Vitali Baumtrok
 */
public class HotKeys {

	public static final KeyCombination keyCombination_ctrlS = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);
	public static final KeyCombination keyCombination_ctrlU = new KeyCodeCombination(KeyCode.U,KeyCombination.CONTROL_DOWN);

	protected StdGUI stdGUI;

	public HotKeys ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public void keyPressed ( KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.F1 ) {
			event.consume();
			stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.files.control);
			if ( stdGUI.listViews.files.control.getSelectionModel().isEmpty() == false )
				stdGUI.listViews.files.control.requestFocus();

		} else if ( keyCode == KeyCode.F2 ) {
			event.consume();
			if ( stdGUI.tabPanes.top.urls.control.isDisable() == false )
				stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.urls.control);

		} else if ( keyCode == KeyCode.F3 ) {
			event.consume();
			stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.preferences.control);

		} else if ( keyCode == KeyCode.F4 ) {
			event.consume();
			stdGUI.tabPanes.top.control.getSelectionModel().select(stdGUI.tabPanes.top.about.control);

		} else if ( keyCombination_ctrlS.match(event) ) {
			final DBRecord record = stdGUI.listViews.files.control.getSelectionModel().getSelectedItem();
			event.consume();
			confirmURLs();
			stdGUI.urlsIO.saveFile(record);
			stdGUI.refreshFileInfo();
			stdGUI.refreshTitle();

		} else if ( keyCombination_ctrlU.match(event) ) {
			event.consume();
			confirmURLs();
			stdGUI.refreshTitle();

		} else if ( keyCode == KeyCode.ESCAPE ) {
			event.consume();
			stdGUI.quit();
		}
	}

	protected void confirmURLs ( ) {
		if ( !stdGUI.buttons.urlDeleteOK.control.isDisable() )
			stdGUI.buttons.confirmURLDelete();
		else if ( !stdGUI.buttons.urlCreateOK.control.isDisable() )
			stdGUI.buttons.confirmURLCreate();
		else if ( !stdGUI.buttons.urlEditOK.control.isDisable() )
			stdGUI.buttons.confirmURLEdit();
		stdGUI.resetURLsProperties();
	}

	protected void onCloseRequest ( final WindowEvent event ) {
		event.consume();
		stdGUI.quit();
	}

}

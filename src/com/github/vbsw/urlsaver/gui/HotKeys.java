/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import com.github.vbsw.urlsaver.api.Global;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;


/**
 * @author Vitali Baumtrok
 */
public class HotKeys {

	public static final KeyCombination keyCombination_ctrlS = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);
	public static final KeyCombination keyCombination_ctrlU = new KeyCodeCombination(KeyCode.U,KeyCombination.CONTROL_DOWN);

	public void keyPressed ( KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.F1 ) {
			event.consume();
			Global.gui.getViewSelector().selectFilesView();

		} else if ( keyCode == KeyCode.F2 ) {
			event.consume();
			Global.gui.getViewSelector().selectURLsView();

		} else if ( keyCode == KeyCode.F3 ) {
			event.consume();
			Global.gui.getViewSelector().selectImportView();

		} else if ( keyCode == KeyCode.F4 ) {
			event.consume();
			Global.gui.getViewSelector().selectSettingsView();

		} else if ( keyCode == KeyCode.F5 ) {
			event.consume();
			Global.gui.getViewSelector().selectAboutView();

		} else if ( keyCombination_ctrlS.match(event) ) {
			event.consume();
			Global.gui.confirmAny();
			Global.urlsIO.saveSelectedFile();
			if ( !Global.db.getSelectedURLs().isDirty() ) {
				Global.properties.selectedFileDirtyProperty().setValue(false);
				((StdGUI) Global.gui).properties.confirmingSaveProperty().setValue(false);
			}
			Global.gui.refreshFileInfo();
			Global.gui.refreshTitle();

		} else if ( keyCombination_ctrlU.match(event) ) {
			event.consume();
			Global.gui.confirmAny();
			Global.gui.refreshTitle();

		} else if ( keyCode == KeyCode.ESCAPE ) {
			event.consume();
			Global.gui.quit();
		}
	}

}

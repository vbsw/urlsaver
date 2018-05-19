/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.services;


import com.github.vbsw.urlsaver.App;
import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.Buttons;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.gui.ListViews;
import com.github.vbsw.urlsaver.gui.Properties;
import com.github.vbsw.urlsaver.gui.TabPanes;
import com.github.vbsw.urlsaver.gui.TextFields;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;


/**
 * @author Vitali Baumtrok
 */
public class WindowService {

	private static final KeyCombination keyCombination_ctrlS = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);
	private static final KeyCombination keyCombination_ctrlU = new KeyCodeCombination(KeyCode.U,KeyCombination.CONTROL_DOWN);

	public static void keyPressed ( KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.F1 ) {
			event.consume();
			TabPanes.top.control.getSelectionModel().select(TabPanes.top.files.control);
			if ( ListViews.files.control.getSelectionModel().isEmpty() == false )
				ListViews.files.control.requestFocus();

		} else if ( keyCode == KeyCode.F2 ) {
			event.consume();
			if ( TabPanes.top.urls.control.isDisable() == false )
				TabPanes.top.control.getSelectionModel().select(TabPanes.top.urls.control);

		} else if ( keyCode == KeyCode.F3 ) {
			event.consume();
			TabPanes.top.control.getSelectionModel().select(TabPanes.top.preferences.control);

		} else if ( keyCode == KeyCode.F4 ) {
			event.consume();
			TabPanes.top.control.getSelectionModel().select(TabPanes.top.about.control);

		} else if ( keyCombination_ctrlS.match(event) ) {
			final DBRecord selectedRecord = ListViews.files.control.getSelectionModel().getSelectedItem();
			event.consume();
			WindowService.confirmURLs();
			if ( selectedRecord != null )
				Services.saveFile(selectedRecord);
			GUI.refreshTitle();

		} else if ( keyCombination_ctrlU.match(event) ) {
			event.consume();
			WindowService.confirmURLs();

		} else if ( keyCode == KeyCode.ESCAPE ) {
			event.consume();
			App.quit();
		}
	}

	private static void confirmURLs ( ) {
		final DBRecord selectedRecord = ListViews.files.control.getSelectionModel().getSelectedItem();
		final String urlTyped = Parser.trim(TextFields.url.control.getText());
		final boolean urlExists = urlTyped.length() > 0 && selectedRecord.getUrlIndex(urlTyped) >= 0;

		if ( !Buttons.urlDeleteOK.control.isDisable() )
			URLsService.confirmURLDelete();
		else if ( !Buttons.urlCreateOK.control.isDisable() )
			URLsService.confirmURLCreate();
		else if ( !Buttons.urlEditOK.control.isDisable() )
			URLsService.confirmURLEdit();

		Properties.urlExistsProperty().set(urlExists);
		Properties.urlDeleteRequestedProperty().set(false);
		Properties.urlModifiedProperty().set(false);
		Properties.urlTagsModifiedProperty().set(false);
	}

	public static void onCloseRequest ( final WindowEvent event ) {
		event.consume();
		App.quit();
	}

}

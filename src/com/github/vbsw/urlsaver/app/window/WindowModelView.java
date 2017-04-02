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


package com.github.vbsw.urlsaver.app.window;


import com.github.vbsw.urlsaver.app.App;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;


/**
 * @author Vitali Baumtrok
 */
public class WindowModelView {

	private static final KeyCombination keyCombination_ctrlS = new KeyCodeCombination(KeyCode.S,KeyCombination.CONTROL_DOWN);
	private static final KeyCombination keyCombination_ctrlU = new KeyCodeCombination(KeyCode.U,KeyCombination.CONTROL_DOWN);

	public void keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.F1 ) {
			event.consume();
			App.scene.tp.top.getSelectionModel().select(App.scene.topTab.files);

			if ( App.scene.lv.files.getSelectionModel().isEmpty() == false ) {
				App.scene.lv.files.requestFocus();
			}

		} else if ( keyCode == KeyCode.F2 ) {
			event.consume();

			if ( App.scene.topTab.urls.isDisable() == false ) {
				App.scene.tp.top.getSelectionModel().select(App.scene.topTab.urls);
			}

		} else if ( keyCode == KeyCode.F3 ) {
			event.consume();
			App.scene.tp.top.getSelectionModel().select(App.scene.topTab.settings);

		} else if ( keyCode == KeyCode.F4 ) {
			event.consume();
			App.scene.tp.top.getSelectionModel().select(App.scene.topTab.about);

		} else if ( keyCombination_ctrlS.match(event) ) {
			event.consume();
			App.urls.confirmAny();
			App.files.confirmSaveSelected();

		} else if ( keyCombination_ctrlU.match(event) ) {
			event.consume();
			App.urls.confirmAny();

		} else if ( keyCode == KeyCode.ESCAPE ) {
			event.consume();
			App.close();
		}
	}

	public void onCloseRequest ( final WindowEvent event ) {
		event.consume();
		App.close();
	}

	public void topTab_selected ( final ObservableValue<? extends Tab> observable, final Tab oldValue, final Tab newValue ) {
		App.window.topTabSelected(newValue);
	}

}

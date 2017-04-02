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


package com.github.vbsw.urlsaver.app.window.about;


import com.github.vbsw.urlsaver.app.App;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * @author Vitali Baumtrok
 */
public class AboutModelView {

	final SimpleBooleanProperty confirmQuitApp = new SimpleBooleanProperty();

	public SimpleBooleanProperty confirmQuitAppProperty ( ) {
		return confirmQuitApp;
	}

	public void button_quitApp_clicked ( final ActionEvent event ) {
		App.close();
	}

	public void button_quitApp_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.close();
		}
	}

	public void button_quitAppSave_clicked ( final ActionEvent event ) {
		App.files.saveAll();
		App.exit();
	}

	public void button_quitAppSave_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.files.saveAll();
			App.exit();
		}
	}

	public void button_quitAppOK_clicked ( final ActionEvent event ) {
		App.exit();
	}

	public void button_quitAppOK_keyPressed ( final KeyEvent event ) {
		final KeyCode keyCode = event.getCode();

		if ( keyCode == KeyCode.ENTER ) {
			App.exit();
		}
	}

}

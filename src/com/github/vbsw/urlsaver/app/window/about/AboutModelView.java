
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


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

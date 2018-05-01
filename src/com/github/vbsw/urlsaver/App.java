/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.urlsaver;


import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class App extends Application {

	@Override
	public void start ( Stage primaryStage ) throws Exception {
		Preferences.initialize(getParameters().getRaw());
		Platform.exit();
	}

}

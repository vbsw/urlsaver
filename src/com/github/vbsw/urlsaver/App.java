/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import com.github.vbsw.urlsaver.api.URLMeta;
import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.db.StdDataBase;
import com.github.vbsw.urlsaver.gui.StdGUI;
import com.github.vbsw.urlsaver.gui.StdLogger;
import com.github.vbsw.urlsaver.gui.StdProperties;
import com.github.vbsw.urlsaver.io.StdFXMLIO;
import com.github.vbsw.urlsaver.io.StdPreferencesIO;
import com.github.vbsw.urlsaver.io.StdURLsIO;
import com.github.vbsw.urlsaver.pref.StdPreferences;
import com.github.vbsw.urlsaver.resources.StdResourceLoader;
import com.github.vbsw.urlsaver.utility.StdTextGenerator;

import javafx.application.Application;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class App extends Application {

	@Override
	public void start ( final Stage primaryStage ) throws Exception {
		Global.arguments = getParameters().getRaw();
		Global.resourceLoader = new StdResourceLoader();
		Global.preferences = new StdPreferences();
		Global.textGenerator = new StdTextGenerator();
		Global.db = new StdDataBase();
		Global.logger = new StdLogger();
		Global.preferencesIO = new StdPreferencesIO();
		Global.urlsIO = new StdURLsIO();
		Global.fxmlIO = new StdFXMLIO();
		Global.gui = new StdGUI();
		Global.urlMeta = new URLMeta();
		Global.properties = new StdProperties();

		Global.resourceLoader.initialize();
		Global.preferences.initialize();
		Global.db.initialize();
		Global.logger.initialize();
		Global.gui.initialize(primaryStage);
	}

}

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
import com.github.vbsw.urlsaver.gui.StdProperties;
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
		Global.dataBase = new StdDataBase();
		Global.gui = new StdGUI();
		Global.urlMeta = new URLMeta();
		Global.properties = new StdProperties();

		Global.resourceLoader.initialize();
		Global.preferences.initialize();
		Global.dataBase.initialize();
		Global.gui.initialize(primaryStage);
	}

}

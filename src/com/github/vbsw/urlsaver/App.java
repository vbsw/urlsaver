/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import com.github.vbsw.urlsaver.api.URLMetaDefinition;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.db.DB;
import com.github.vbsw.urlsaver.gui.Properties;
import com.github.vbsw.urlsaver.gui.StdGUI;
import com.github.vbsw.urlsaver.gui.StdLogger;
import com.github.vbsw.urlsaver.io.StdFXMLIO;
import com.github.vbsw.urlsaver.io.SettingsIO;
import com.github.vbsw.urlsaver.io.StdURLsIO;
import com.github.vbsw.urlsaver.settings.Settings;
import com.github.vbsw.urlsaver.utility.LabelProvider;
import com.github.vbsw.urlsaver.utility.PathProvider;

import javafx.application.Application;
import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class App extends Application {

	@Override
	public void start ( final Stage primaryStage ) throws Exception {
		final String[] appArguments = getAppArguements();

		final Properties properties = new Properties();
		final PathProvider pathProvider = new PathProvider();
		final Settings settings = new Settings(appArguments);
		final LabelProvider labelProvider = new LabelProvider();
		final DB db = new DB();
		final StdLogger logger = new StdLogger();
		final SettingsIO settingsIO = new SettingsIO();
		final StdURLsIO urlsIO = new StdURLsIO();
		final StdFXMLIO fxmlIO = new StdFXMLIO();
		final StdGUI gui = new StdGUI();
		final URLMetaDefinition urlMetaDefinition = new URLMetaDefinition();

		Global.properties = properties;
		Global.pathProvider = pathProvider;
		Global.settings = settings;
		Global.labelProvider = labelProvider;
		Global.db = db;
		Global.logger = logger;
		Global.settingsIO = settingsIO;
		Global.urlsIO = urlsIO;
		Global.fxmlIO = fxmlIO;
		Global.gui = gui;
		Global.urlMetaKey = urlMetaDefinition;

		settings.initialize();
		db.initialize();
		logger.initialize();
		gui.initialize(primaryStage);

	}

	private String[] getAppArguements ( ) {
		final String[] appArguments = new String[getParameters().getRaw().size()];
		getParameters().getRaw().toArray(appArguments);
		return appArguments;
	}

}

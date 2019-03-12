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
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.gui.Logger;
import com.github.vbsw.urlsaver.io.FXMLIO;
import com.github.vbsw.urlsaver.io.SettingsIO;
import com.github.vbsw.urlsaver.io.URLsIO;
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
		final Logger logger = new Logger();
		final SettingsIO settingsIO = new SettingsIO();
		final URLsIO urlsIO = new URLsIO();
		final FXMLIO fxmlIO = new FXMLIO();
		final GUI gui = new GUI();
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

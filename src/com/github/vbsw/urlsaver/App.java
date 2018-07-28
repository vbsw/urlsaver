/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import java.util.List;

import com.github.vbsw.urlsaver.api.DataBase;
import com.github.vbsw.urlsaver.api.GUI;
import com.github.vbsw.urlsaver.api.Preferences;
import com.github.vbsw.urlsaver.api.Properties;
import com.github.vbsw.urlsaver.api.ResourceLoader;
import com.github.vbsw.urlsaver.api.TextGenerator;
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
		final List<String> arguments = getParameters().getRaw();
		final StdGlobal global = new StdGlobal(arguments);

		global.getResourceLoader().initialize(global);
		global.getPreferences().initialize(global);
		global.getDataBase().initialize(global);
		global.getGUI().initialize(global,primaryStage);
	}

	private static final class StdGlobal extends Global {

		private final ResourceLoader resources = new StdResourceLoader();
		private final Preferences preferences = new StdPreferences();
		private final TextGenerator textGenerator = new StdTextGenerator();
		private final DataBase db = new StdDataBase();
		private final GUI gui = new StdGUI();
		private final Properties properites = new StdProperties();
		private final URLMeta urlMeta = new URLMeta();
		private final List<String> arguments;

		public StdGlobal ( final List<String> arguments ) {
			this.arguments = arguments;
		}

		@Override
		public ResourceLoader getResourceLoader ( ) {
			return resources;
		}

		@Override
		public Preferences getPreferences ( ) {
			return preferences;
		}

		@Override
		public TextGenerator getTextGenerator ( ) {
			return textGenerator;
		}

		@Override
		public DataBase getDataBase ( ) {
			return db;
		}

		@Override
		public GUI getGUI ( ) {
			return gui;
		}

		@Override
		public Properties getProperties ( ) {
			return properites;
		}

		@Override
		public URLMeta getURLMeta ( ) {
			return urlMeta;
		}

		@Override
		public List<String> getArguments ( ) {
			return arguments;
		}

	}

}

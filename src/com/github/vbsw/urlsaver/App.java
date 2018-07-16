/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import com.github.vbsw.urlsaver.api.DataBase;
import com.github.vbsw.urlsaver.api.Preferences;
import com.github.vbsw.urlsaver.api.ResourceLoader;
import com.github.vbsw.urlsaver.api.TextGenerator;
import com.github.vbsw.urlsaver.api.URLSaver;
import com.github.vbsw.urlsaver.db.StdDataBase;
import com.github.vbsw.urlsaver.gui.StdGUI;
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
		final URLSaver urlSaver = new URLSaver();

		final ResourceLoader resourceLoader = new StdResourceLoader();
		final Preferences preferences = new StdPreferences();
		final TextGenerator textGenerator = new StdTextGenerator();
		final DataBase db = new StdDataBase();
		final StdGUI gui = new StdGUI();

		urlSaver.setResourceLoader(resourceLoader);
		urlSaver.setPreferences(preferences);
		urlSaver.setTextGenerator(textGenerator);
		urlSaver.setDataBase(db);
		urlSaver.setGUI(gui);
		urlSaver.launch(primaryStage,getParameters().getRaw());
	}

}

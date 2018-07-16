/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.util.List;

import javafx.stage.Stage;


/**
 * @author Vitali Baumtrok
 */
public class URLSaver {

	protected ResourceLoader resources;
	protected Preferences preferences;
	protected TextGenerator textGenerator;
	protected DataBase db;
	protected GUI gui;

	public void setResourceLoader ( final ResourceLoader resourceLoader ) {
		this.resources = resourceLoader;
	}

	public void setPreferences ( final Preferences preferences ) {
		this.preferences = preferences;
	}

	public void setTextGenerator ( final TextGenerator textGenerator ) {
		this.textGenerator = textGenerator;
	}

	public void setDataBase ( final DataBase db ) {
		this.db = db;
	}

	public void setGUI ( final GUI gui ) {
		this.gui = gui;
	}

	public void launch ( final Stage primaryStage, final List<String> args ) {
		resources.initialize();
		preferences.initialize(resources,args);
		db.initialize(resources,preferences,textGenerator);
		gui.initialize(resources,preferences,textGenerator,db,primaryStage);
	}

}

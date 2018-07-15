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

	private ResourceLoader resources;
	private Preferences preferences;
	private DataBase db;
	private GUI gui;

	public void setResourceLoader ( final ResourceLoader resourceLoader ) {
		this.resources = resourceLoader;
	}

	public void setPreferences ( final Preferences preferences ) {
		this.preferences = preferences;
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
		db.initialize(resources,preferences);
		gui.initialize(resources,preferences,db,primaryStage);
	}

}

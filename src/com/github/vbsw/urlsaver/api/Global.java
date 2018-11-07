/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.util.List;


/**
 * @author Vitali Baumtrok
 */
public final class Global {

	public static List<String> arguments;
	public static ResourceLoader resourceLoader;
	public static Settings settings;
	public static TextGenerator textGenerator;
	public static DataBase db;
	public static Logger logger;
	public static SettingsIO settingsIO;
	public static URLsIO urlsIO;
	public static FXMLIO fxmlIO;
	public static GUI gui;
	public static URLMeta urlMeta;
	public static Properties properties;

}

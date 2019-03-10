/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


/**
 * @author Vitali Baumtrok
 */
public final class Global {

	public static IProperties properties;
	public static IPathProvider pathProvider;
	public static ISettings settings;
	public static ILabelProvider labelProvider;
	public static IDB db;
	public static ILogger logger;
	public static ISettingsIO settingsIO;
	public static IURLsIO urlsIO;
	public static IFXMLIO fxmlIO;
	public static IGUI gui;
	public static URLMetaDefinition urlMetaKey;

}

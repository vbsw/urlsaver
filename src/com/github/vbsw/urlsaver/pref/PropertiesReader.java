/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.pref;


import java.util.Properties;


/**
 * @author Vitali Baumtrok
 */
public class PropertiesReader {

	public static String getWindowTitle ( final Properties properties ) {
		final String defaultValue = "<window.title>";
		final String value = PropertiesReader.getWindowTitle(properties,defaultValue);
		return value;
	}

	public static String getWindowTitle ( final Properties properties, final String defaultValue ) {
		final String key = "window.title";
		final String value = PropertiesReader.getPropertyValue(properties,key,defaultValue);
		return value;
	}

	public static int getWindowWidth ( final Properties properties ) {
		final int value = PropertiesReader.getWindowWidth(properties,0);
		return value;
	}

	public static int getWindowWidth ( final Properties properties, final int defaultValue ) {
		final String key = "window.width";
		final int value = PropertiesReader.getPropertyValue(properties,key,defaultValue);
		return value;
	}

	public static int getWindowHeight ( final Properties properties ) {
		final int value = PropertiesReader.getWindowHeight(properties,0);
		return value;
	}

	public static int getWindowHeight ( final Properties properties, final int defaultValue ) {
		final String key = "window.height";
		final int value = PropertiesReader.getPropertyValue(properties,key,0);
		return value;
	}

	public static boolean getWindowMaximized ( final Properties properties ) {
		final boolean value = PropertiesReader.getWindowMaximized(properties,false);
		return value;
	}

	public static boolean getWindowMaximized ( final Properties properties, final boolean defaultValue ) {
		final String key = "window.maximized";
		final boolean value = PropertiesReader.getPropertyValue(properties,key,defaultValue);
		return value;
	}

	public static String getURLsFileExtension ( final Properties properties ) {
		final String defaultValue = "<urls.file.extension>";
		final String value = PropertiesReader.getURLsFileExtension(properties,defaultValue);
		return value;
	}

	public static String getURLsFileExtension ( final Properties properties, final String defaultValue ) {
		final String key = "urls.file.extension";
		final String value = PropertiesReader.getPropertyValue(properties,key,defaultValue);
		return value;
	}

	public static String getURLsFileSelect ( final Properties properties ) {
		final String defaultValue = "<urls.file.select>";
		final String value = PropertiesReader.getURLsFileSelect(properties,defaultValue);
		return value;
	}

	public static String getURLsFileSelect ( final Properties properties, final String defaultValue ) {
		final String key = "urls.file.select";
		final String value = PropertiesReader.getPropertyValue(properties,key,defaultValue);
		return value;
	}

	public static boolean getURLsFileAutoLoadAll ( final Properties properties ) {
		final boolean value = PropertiesReader.getURLsFileAutoLoadAll(properties,false);
		return value;
	}

	public static boolean getURLsFileAutoLoadAll ( final Properties properties, final boolean defaultValue ) {
		final String key = "urls.file.autoload.all";
		final boolean value = PropertiesReader.getPropertyValue(properties,key,defaultValue);
		return value;
	}

	public static boolean getSearchByPrefix ( final Properties properties ) {
		final boolean value = PropertiesReader.getSearchByPrefix(properties,true);
		return value;
	}

	public static boolean getSearchByPrefix ( final Properties properties, final boolean defaultValue ) {
		final String key = "search.byprefix";
		final boolean value = PropertiesReader.getPropertyValue(properties,key,defaultValue);
		return value;
	}

	private static String getPropertyValue ( final Properties properties, final String key, final String defaultValue ) {
		final String value = properties.getProperty(key,defaultValue);
		return value;
	}

	private static int getPropertyValue ( final Properties properties, final String key, final int defaultValue ) {
		final String value = properties.getProperty(key);
		if ( value != null )
			try {
				return Integer.parseInt(value);
			} catch ( final NumberFormatException e ) {
				e.printStackTrace();
			}
		return defaultValue;
	}

	private static boolean getPropertyValue ( final Properties properties, final String key, final boolean defaultValue ) {
		final String value = properties.getProperty(key);
		if ( value != null )
			return PropertiesReader.isTrue(value);
		return defaultValue;
	}

	private static boolean isTrue ( final String boolStr ) {
		final String trueStr = "true";
		final String yesStr = "yes";
		final String yStr = "y";
		final String oneStr = "1";
		return boolStr.equals(trueStr) || boolStr.equals(yesStr) || boolStr.equals(yStr) || boolStr.equals(oneStr);
	}

}

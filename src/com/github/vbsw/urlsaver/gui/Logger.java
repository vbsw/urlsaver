/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Vitali Baumtrok
 */
public class Logger {

	public static void logSuccess ( final String message ) {
		Logger.log("Success: ",message);
	}

	public static void logFailure ( final String message ) {
		Logger.log("Failure: ",message);
	}

	private static void log ( final String prefix, final String message ) {
		final String timeStr = Logger.getDayTime();
		if ( GUI.textAreas.log.getText().length() > 0 )
			GUI.textAreas.log.appendText("\n");
		GUI.textAreas.log.appendText(timeStr);
		GUI.textAreas.log.appendText(" ");
		GUI.textAreas.log.appendText(prefix);
		GUI.textAreas.log.appendText(message);
	}

	private static String getDayTime ( ) {
		final String pattern = "HH:mm:ss";
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		final LocalDateTime time = LocalDateTime.now();
		final String timeStr = "[" + formatter.format(time) + "]";

		return timeStr;
	}

}

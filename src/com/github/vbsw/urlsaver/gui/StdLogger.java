/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.ILogger;


/**
 * @author Vitali Baumtrok
 */
public class StdLogger implements ILogger {

	protected StdGUI stdGUI;

	public void initialize ( ) {
		this.stdGUI = (StdGUI) Global.gui;
	}

	@Override
	public void logSuccess ( final String message ) {
		log("",message);
	}

	@Override
	public void logFailure ( final String message ) {
		log("",message);
	}

	private void log ( final String prefix, final String message ) {
		final String timeStr = getDayTime();
		if ( stdGUI.textAreas.log.control.getText().length() > 0 )
			stdGUI.textAreas.log.control.appendText("\n");
		stdGUI.textAreas.log.control.appendText(timeStr);
		stdGUI.textAreas.log.control.appendText(" ");
		stdGUI.textAreas.log.control.appendText(prefix);
		stdGUI.textAreas.log.control.appendText(message);
	}

	private String getDayTime ( ) {
		final String pattern = "HH:mm:ss";
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		final LocalDateTime time = LocalDateTime.now();
		final String timeStr = "[" + formatter.format(time) + "]";

		return timeStr;
	}

}

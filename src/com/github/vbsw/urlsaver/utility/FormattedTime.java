
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.utility;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author Vitali Baumtrok
 */
public class FormattedTime {

	public static String get ( ) {
		final String pattern = "HH:mm:ss"; //$NON-NLS-1$
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		final LocalDateTime time = LocalDateTime.now();
		final String timeStr = "[" + formatter.format(time) + "]";

		return timeStr;
	}

}

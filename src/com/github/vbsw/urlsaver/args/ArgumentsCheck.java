/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.args;


import com.github.vbsw.urlsaver.utility.Parser;


/**
 * @author Vitali Baumtrok
 */
public class ArgumentsCheck {

	public static boolean isValidForApplication ( final String[] arguments ) {
		return arguments.length == 0 || ArgumentsCheck.startsWithConfigOption(arguments[0]);
	}

	private static boolean startsWithConfigOption ( final String argument ) {
		final int offset = Parser.seekContent(argument,0,argument.length(),'-');
		for ( final String key: ArgumentsConfig.SETTINGS_OPTION )
			if ( argument.startsWith(key,offset) )
				return true;
		return false;
	}

}

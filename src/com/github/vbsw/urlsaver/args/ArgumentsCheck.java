
//    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
// Distributed under the Boost Software License, Version 1.0.
//     (See accompanying file BSL-1.0.txt or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.args;


/**
 * @author Vitali Baumtrok
 */
public class ArgumentsCheck {

	public static boolean isValidForApplication ( final String[] args ) {
		return args.length == 0 || ArgumentsCheck.startsWithConfigOption(args[0]);
	}

	private static boolean startsWithConfigOption ( final String arg ) {
		final int offset = ArgumentsParser.getWordOffset(arg);
		for ( final String key: ArgumentsConfig.PREFERENCES_OPTION )
			if ( arg.startsWith(key,offset) )
				return true;
		return false;
	}

}

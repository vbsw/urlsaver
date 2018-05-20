/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.args;


/**
 * @author Vitali Baumtrok
 */
public class ArgumentsParser {

	public static boolean isOption ( final String arg, final String[] optionVariants ) {
		final int offset = ArgumentsParser.getWordOffset(arg);
		for ( final String option: optionVariants )
			if ( ArgumentsParser.isMatch(arg,offset,option) )
				return true;
		return false;
	}

	public static String getValue ( final String arg, final String[] keyVariants, final String[] assignmentOperatorVariants ) {
		final int offset = ArgumentsParser.getWordOffset(arg);
		for ( final String key: keyVariants ) {
			if ( arg.startsWith(key,offset) ) {
				for ( final String assignmentOperator: assignmentOperatorVariants )
					if ( arg.startsWith(assignmentOperator,offset + key.length()) )
						return arg.substring(offset + key.length() + assignmentOperator.length());
				return arg.substring(offset + key.length());
			}
		}
		return "";
	}

	public static int getWordOffset ( final String arg ) {
		if ( arg.length() > 0 )
			if ( arg.charAt(0) == '-' )
				if ( arg.length() > 1 && arg.charAt(1) == '-' )
					return 2;
				else
					return 1;
		return 0;
	}

	private static boolean isMatch ( final String arg, final int offset, final String word ) {
		return arg.length() - offset == word.length() && arg.endsWith(word);
	}

}

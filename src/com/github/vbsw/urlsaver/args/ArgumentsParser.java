
//    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
// Distributed under the Boost Software License, Version 1.0.
//     (See accompanying file BSL-1.0.txt or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


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

	public static String trim ( final String str ) {
		final int beginIndex = ArgumentsParser.seekContent(str,0,str.length());
		if ( beginIndex < str.length() ) {
			final int endIndex = ArgumentsParser.seekContentReverse(str,str.length(),beginIndex);
			if ( str.length() == endIndex - beginIndex )
				return str;
			else
				return str.substring(beginIndex,endIndex);
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

	private static int seekContent ( final String str, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 )
				if ( !ArgumentsParser.isWhitespace(str.charAt(i)) )
					return i;
			return toRight;
		}
		return fromLeft;
	}

	private static int seekContentReverse ( final String str, final int fromRight, final int toLeft ) {
		if ( fromRight > toLeft ) {
			for ( int i = fromRight - 1; i >= toLeft; i -= 1 )
				if ( !isWhitespace(str.charAt(i)) )
					return i + 1;
			return toLeft;
		}
		return fromRight;
	}

	private static boolean isWhitespace ( final char character ) {
		return character >= 0 && character <= 32;
	}

	private static boolean isMatch ( final String arg, final int offset, final String word ) {
		return arg.length() - offset == word.length() && arg.endsWith(word);
	}

}

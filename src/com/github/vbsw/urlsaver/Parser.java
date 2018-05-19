/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


/**
 * @author Vitali Baumtrok
 */
public class Parser {

	public static int toUnsignedInteger ( final String str ) {
		long number = 0;
		for ( int i = 0; i < str.length(); i += 1 ) {
			final char c = str.charAt(i);
			if ( c >= '0' && c <= '9' ) {
				number = number * 10 + c - '0';
				if ( number > Integer.MAX_VALUE ) {
					number = Integer.MAX_VALUE;
					break;
				}
			}
		}
		return (int) number;
	}

	public static String trim ( final String str ) {
		final int beginIndex = Parser.seekContent(str,0,str.length());
		if ( beginIndex < str.length() ) {
			final int endIndex = Parser.seekContentReverse(str,str.length(),beginIndex);
			if ( str.length() == endIndex - beginIndex )
				return str;
			else
				return str.substring(beginIndex,endIndex);
		}
		return "";
	}

	public static int seekContent ( final String str, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 )
				if ( !Parser.isWhitespace(str.charAt(i)) )
					return i;
			return toRight;
		}
		return fromLeft;
	}

	public static int seekContent ( final byte[] bytes, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 )
				if ( !Parser.isWhitespace(bytes[i]) )
					return i;
			return toRight;
		}
		return fromLeft;
	}

	public static int seekContentReverse ( final String str, final int fromRight, final int toLeft ) {
		if ( fromRight > toLeft ) {
			for ( int i = fromRight - 1; i >= toLeft; i -= 1 )
				if ( !isWhitespace(str.charAt(i)) )
					return i + 1;
			return toLeft;
		}
		return fromRight;
	}

	public static int seekContentReverse ( final byte[] bytes, final int fromRight, final int toLeft ) {
		if ( fromRight > toLeft ) {
			for ( int i = fromRight - 1; i >= toLeft; i -= 1 )
				if ( !isWhitespace(bytes[i]) )
					return i + 1;
			return toLeft;
		}
		return fromRight;
	}

	public static int seekWhitespace ( final String str, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 )
				if ( Parser.isWhitespace(str.charAt(i)) )
					return i;
			return toRight;
		}
		return fromLeft;
	}

	public static int seekWhitespace ( final byte[] bytes, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 )
				if ( Parser.isWhitespace(bytes[i]) )
					return i;
			return toRight;
		}
		return fromLeft;
	}

	public static int seekLineEnd ( final byte[] bytes, final int fromLeft, final int toRight ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 )
				if ( bytes[i] == '\n' || bytes[i] == '\r' )
					return i;
			return toRight;
		}
		return fromLeft;
	}

	private static boolean isWhitespace ( final char character ) {
		return character >= 0 && character <= 32;
	}

	private static boolean isWhitespace ( final byte b ) {
		return b >= 0 && b <= 32;
	}

}

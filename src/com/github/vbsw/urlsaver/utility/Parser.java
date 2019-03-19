/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.utility;


/**
 * @author Vitali Baumtrok
 */
public class Parser {

	public static String getArgumentValue ( final String argument, final String[] keyVariants, final String[] assignmentOperators ) {
		final int offset = Parser.seekContent(argument,0,argument.length(),'-');
		for ( final String key: keyVariants ) {
			if ( argument.startsWith(key,offset) ) {
				for ( final String assignmentOperator: assignmentOperators )
					if ( argument.startsWith(assignmentOperator,offset + key.length()) )
						return argument.substring(offset + key.length() + assignmentOperator.length());
				return argument.substring(offset + key.length());
			}
		}
		return "";
	}

	public static boolean endsWith ( final String str, final int offset, final String suffix ) {
		return str.length() - offset == suffix.length() && str.endsWith(suffix);
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

	public static int seekContent ( final String str, final int fromLeft, final int toRight, final char charToSkip ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < str.length() && i < toRight; i += 1 )
				if ( str.charAt(i) != charToSkip )
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

	public static int seekByte ( final byte[] bytes, final int fromLeft, final int toRight, final byte b ) {
		if ( fromLeft < toRight ) {
			for ( int i = fromLeft; i < toRight; i += 1 )
				if ( bytes[i] == b )
					return i;
			return toRight;
		}
		return fromLeft;
	}

	public static boolean isMyAnimeListXML ( final byte[] bytes, final int fromLeft, final int toRight ) {
		final byte[] filter = new byte[] { '<', 'm', 'y', 'a', 'n', 'i', 'm', 'e', 'l', 'i', 's', 't', '>' };
		final int index = Parser.seekMatch(bytes,fromLeft,toRight,filter);
		final boolean match = index < toRight;
		return match;
	}

	public static int seekMatch ( final byte[] bytes, final int fromLeft, final int toRight, final byte[] word ) {
		if ( fromLeft < toRight ) {
			final int rightLimit = toRight - word.length;
			if ( fromLeft < rightLimit )
				for ( int i = fromLeft; i < rightLimit; i += 1 )
					if ( Parser.isMatch(bytes,i,word) )
						return i;
			return toRight;
		}
		return fromLeft;
	}

	/**
	 * Empty word returns always true!
	 */
	private static boolean isMatch ( final byte[] bytes, final int offset, final byte[] word ) {
		for ( int i = 0; i < word.length; i += 1 ) {
			if ( bytes[offset + i] != word[i] ) {
				return false;
			}
		}
		return true;
	}

}

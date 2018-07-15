/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.utility;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.github.vbsw.urlsaver.db.DynArrayOfString;


/**
 * @author Vitali Baumtrok
 */
public final class Converter {

	public static String toString ( final Collection<String> list ) {
		final StringBuilder stringBuilder = new StringBuilder();
		for ( String string: list ) {
			if ( stringBuilder.length() > 0 )
				stringBuilder.append(' ');
			stringBuilder.append(string);
		}
		return stringBuilder.toString();
	}

	public static ArrayList<String> toArrayList ( final String text ) {
		final ArrayList<String> list = new ArrayList<>();
		int beginIndex = Parser.seekContent(text,0,text.length());
		while ( beginIndex < text.length() ) {
			final int endIndex = Parser.seekWhitespace(text,beginIndex,text.length());
			final String key = text.substring(beginIndex,endIndex);
			beginIndex = Parser.seekContent(text,endIndex,text.length());
			list.add(key);
		}
		return list;
	}

	public static DynArrayOfString toDynArrayList ( final String string ) {
		final DynArrayOfString dynArray = new DynArrayOfString();
		int beginIndex = Parser.seekContent(string,0,string.length());
		while ( beginIndex < string.length() ) {
			final int endIndex = Parser.seekWhitespace(string,beginIndex,string.length());
			final String word = string.substring(beginIndex,endIndex);
			beginIndex = Parser.seekContent(string,endIndex,string.length());
			dynArray.add(word);
		}
		return dynArray;
	}

	public static DynArrayOfString toDynArrayListSorted ( final String string ) {
		final DynArrayOfString dynArray = new DynArrayOfString();
		int beginIndex = Parser.seekContent(string,0,string.length());
		while ( beginIndex < string.length() ) {
			final int endIndex = Parser.seekWhitespace(string,beginIndex,string.length());
			final String word = string.substring(beginIndex,endIndex);
			final int index = dynArray.binarySearch(word);
			beginIndex = Parser.seekContent(string,endIndex,string.length());
			if ( index < 0 )
				dynArray.add(-index - 1,word);
		}
		return dynArray;
	}

	public static void toArrayListNoDuplicates ( final ArrayList<String> list, final String string ) {
		int beginIndex = Parser.seekContent(string,0,string.length());
		list.clear();
		while ( beginIndex < string.length() ) {
			final int endIndex = Parser.seekWhitespace(string,beginIndex,string.length());
			final String word = string.substring(beginIndex,endIndex);
			final int searchIndex = Collections.binarySearch(list,word);
			beginIndex = Parser.seekContent(string,endIndex,string.length());
			if ( searchIndex < 0 ) {
				final int insertIndex = -searchIndex - 1;
				list.add(insertIndex,word);
			}
		}
	}

	public static URI toURI ( final String url ) {
		final URL urlURL = toURL(url);
		if ( urlURL != null ) {
			try {
				return urlURL.toURI();
			} catch ( final Exception e ) {
			}
		}
		return null;
	}

	public static URL toURL ( final String url ) {
		if ( url != null && url.length() > 0 ) {
			try {
				return new URL(url);
			} catch ( final MalformedURLException e ) {
			}
			try {
				final String httpPrefix = "http://";
				final String httpURLStr = httpPrefix + url;
				return new URL(httpURLStr);
			} catch ( final MalformedURLException e ) {
			}
		}
		return null;
	}

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

}

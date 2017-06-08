
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.utility;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * @author Vitali Baumtrok
 */
public final class Convert {

	public static String toString ( final Collection<String> list ) {
		final StringBuilder stringBuilder = new StringBuilder();

		for ( String string: list ) {
			if ( stringBuilder.length() > 0 ) {
				stringBuilder.append(' ');
			}
			stringBuilder.append(string);
		}
		return stringBuilder.toString();
	}

	public static ArrayList<String> toStringArrayList ( final String string ) {
		final ArrayList<String> list = new ArrayList<String>();
		int offset = Parser.skipWhiteSpace(string,0);

		while ( offset < string.length() ) {
			final int endIndex = Parser.skipNonWhiteSpace(string,offset);
			final String word = string.substring(offset,endIndex);
			offset = Parser.skipWhiteSpace(string,endIndex);

			list.add(word);
		}
		return list;
	}

	public static void toArrayListNoDuplicates ( final ArrayList<String> list, final String string ) {
		int offset = Parser.skipWhiteSpace(string,0);

		list.clear();

		while ( offset < string.length() ) {
			final int endIndex = Parser.skipNonWhiteSpace(string,offset);
			final String word = string.substring(offset,endIndex);
			final int searchIndex = Collections.binarySearch(list,word);
			offset = Parser.skipWhiteSpace(string,endIndex);

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
				final String httpPrefix = "http://"; //$NON-NLS-1$
				final String httpURLStr = httpPrefix + url;

				return new URL(httpURLStr);

			} catch ( final MalformedURLException e ) {
			}
		}

		return null;
	}

}

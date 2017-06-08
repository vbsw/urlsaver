
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.urls;

/**
 * @author Vitali Baumtrok
 */
final class TagIndices {

	private int[] values = new int[10];

	public int fillByPrefix ( final UrlsData urlsData, final String tagPrefix ) {
		final int tagIndex = urlsData.getTagIndex(tagPrefix);
		final int tagOffsetIndex;
		int indicesCount = 0;

		if ( tagIndex >= 0 ) {
			tagOffsetIndex = tagIndex + 1;
			values[0] = tagIndex;
			indicesCount = 1;

		} else {
			tagOffsetIndex = -tagIndex - 1;
		}

		for ( int i = tagOffsetIndex; i < urlsData.tags.size(); i += 1 ) {
			final String tag = urlsData.tags.get(i);

			if ( tag.startsWith(tagPrefix) ) {
				if ( values.length <= indicesCount ) {
					final int[] newValues = new int[values.length * 2];
					System.arraycopy(values,0,newValues,0,values.length);
					values = newValues;
				}

				values[indicesCount] = i;
				indicesCount += 1;

			} else {
				break;
			}
		}

		return indicesCount;
	}

	public int get ( final int index ) {
		return values[index];
	}

}

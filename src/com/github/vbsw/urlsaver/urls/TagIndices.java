/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
 * 
 * This file is part of URL Saver.
 * 
 * URL Saver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * URL Saver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with URL Saver.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.vbsw.urlsaver.urls;


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

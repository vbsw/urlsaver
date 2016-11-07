/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2016 Vitali Baumtrok
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


package com.github.vbsw.urlsaver;


import java.util.ArrayList;
import java.util.Collections;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
public class StringList extends ArrayList<String> {

	public StringList ( ) {
		super();
	}

	public StringList ( final int initialCapacity ) {
		super(initialCapacity);
	}

	public void set ( final String stringsStr ) {
		clear();

		int offset = skipWhiteSpace(stringsStr,0);
		offset = setAllButLast(stringsStr,offset);
		setLast(stringsStr,offset);
	}

	private int setAllButLast ( final String stringsStr, int offset ) {
		for ( int i = offset; i < stringsStr.length(); i += 1 ) {
			final char character = stringsStr.charAt(i);
			if ( isWhiteSpace(character) ) {
				if ( offset < i ) {
					final String string = stringsStr.substring(offset,i);
					insertInOrder(string);
				}
				offset = i + 1;
			}
		}
		return offset;
	}

	private void setLast ( final String stringsStr, int offset ) {
		if ( offset < stringsStr.length() ) {
			final String string = stringsStr.substring(offset);
			insertInOrder(string);
		}
	}

	private void insertInOrder ( final String string ) {
		final int index = Collections.binarySearch(this,string);
		if ( index < 0 ) {
			final int insertIndex = -index - 1;
			add(insertIndex,string);
		}
	}

	private int skipWhiteSpace ( final String string, int offset ) {
		while ( offset < string.length() && isWhiteSpace(string.charAt(offset)) ) {
			offset += 1;
		}
		return offset;
	}

	private boolean isWhiteSpace ( final char character ) {
		return character >= 0 && character <= 32;
	}

}

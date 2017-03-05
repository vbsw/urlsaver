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


package com.github.vbsw.urlsaver;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
public class SortedUniqueStringList extends ArrayList<String> {

	public void setStrings ( final String strings ) {
		int offset = Parser.skipWhiteSpace(strings,0);

		this.clear();

		while ( offset < strings.length() ) {
			final int endIndex = Parser.skipNonWhiteSpace(strings,offset);
			final String string = strings.substring(offset,endIndex);
			offset = Parser.skipWhiteSpace(strings,endIndex);

			addSorted(string);
		}
	}

	public boolean isEqualByStrings ( final SortedUniqueStringList list ) {
		if ( this != list ) {
			for ( String key: this ) {
				final int index = Collections.binarySearch(list,key);

				if ( index < 0 ) {
					return false;
				}
			}
			for ( String key: list ) {
				final int index = Collections.binarySearch(this,key);

				if ( index < 0 ) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean addSorted ( final String string ) {
		final int stringIndex = Collections.binarySearch(this,string);

		if ( stringIndex < 0 ) {
			final int stringInsertIndex = -stringIndex - 1;

			super.add(stringInsertIndex,string);

			return true;

		} else {
			return false;
		}
	}

	public void addAllSorted ( final Collection<String> strings ) {
		for ( String string: strings ) {
			addSorted(string);
		}
	}

	public void setAllSorted ( final Collection<String> strings ) {
		this.clear();
		addAllSorted(strings);
	}

}


//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.utility;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
public class SortedUniqueStringList extends ArrayList<String> {

	public void addStringsSeparatedByWhiteSpace ( final String strings ) {
		int offset = Parser.skipWhiteSpace(strings,0);

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

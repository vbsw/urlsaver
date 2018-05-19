/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


/**
 * @author Vitali Baumtrok
 */
public class DynArrayOfString {

	public static final int INITIAL_CAPACITY = 10;

	public String[] values;
	public int valuesLength;

	public DynArrayOfString ( ) {
		this(INITIAL_CAPACITY);
	}

	public DynArrayOfString ( final int initialCapacity ) {
		values = new String[initialCapacity];
		valuesLength = 0;
	}

	public void clear ( ) {
		for ( int i = 0; i < valuesLength; i += 1 )
			values[i] = null;
		valuesLength = 0;
	}

	public void add ( final String value ) {
		add(valuesLength,value);
	}

	public void add ( final int index, final String value ) {
		final int copyLength = valuesLength - index;
		// ensure capacity
		if ( valuesLength == values.length ) {
			final String[] valuesNew = new String[values.length * 2];
			System.arraycopy(values,0,valuesNew,0,values.length);
			values = valuesNew;
		}
		// insert
		if ( copyLength > 0 )
			System.arraycopy(values,index,values,index + 1,copyLength);
		values[index] = value;
		valuesLength += 1;
	}

	public int binarySearch ( final String key ) {
		int left = 0;
		int right = valuesLength - 1;
		while ( left <= right ) {
			final int middle = (left + right) / 2;
			final int compareResult = values[middle].compareTo(key);
			if ( compareResult < 0 )
				left = middle + 1;
			else if ( compareResult > 0 )
				right = middle - 1;
			else
				return middle;
		}
		return -left - 1;
	}

	public void remove ( ) {
		remove(valuesLength - 1);
	}

	public void remove ( final int valueIndex ) {
		valuesLength -= 1;
		if ( valueIndex < valuesLength ) {
			final int srcIndex = valueIndex + 1;
			final int copyLength = valuesLength - valueIndex;
			System.arraycopy(values,srcIndex,values,valueIndex,copyLength);
		}
	}

	public String toString ( final String prefix, final String suffix, final String separator ) {
		final StringBuilder builder = new StringBuilder(100);
		builder.append(prefix);
		for ( int i = 0; i < valuesLength; i += 1 ) {
			if ( i > 0 )
				builder.append(separator);
			builder.append(values[i]);
		}
		builder.append(suffix);
		return builder.toString();
	}

}

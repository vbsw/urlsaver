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
public class DynArrayOfInt {

	public static final int INITIAL_CAPACITY = 10;

	public int[] values;
	public int valuesLength;

	public DynArrayOfInt ( ) {
		this(INITIAL_CAPACITY);
	}

	public DynArrayOfInt ( final int initialCapacity ) {
		values = new int[initialCapacity];
		valuesLength = 0;
	}

	public void clear ( ) {
		valuesLength = 0;
	}

	public void add ( final int value ) {
		add(valuesLength,value);
	}

	public void add ( final int index, final int value ) {
		final int copyLength = valuesLength - index;
		if ( valuesLength == values.length ) {
			final int[] valuesNew = new int[values.length * 2];
			System.arraycopy(values,0,valuesNew,0,values.length);
			values = valuesNew;
		}
		if ( copyLength > 0 )
			System.arraycopy(values,index,values,index + 1,copyLength);
		values[index] = value;
		valuesLength += 1;
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
		builder.append(prefix);
		return builder.toString();
	}

}

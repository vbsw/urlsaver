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
public class DynArrayOfString2 {

	public static final int INITIAL_CAPACITY = 10;

	public DynArrayOfString[] values;
	public int valuesLength;

	public DynArrayOfString2 ( ) {
		this(INITIAL_CAPACITY);
	}

	public DynArrayOfString2 ( final int initialCapacity ) {
		values = new DynArrayOfString[initialCapacity];
		valuesLength = 0;
	}

	public void clear ( ) {
		for ( int i = 0; i < valuesLength; i += 1 )
			values[i] = null;
		valuesLength = 0;
	}

	public void add ( final DynArrayOfString value ) {
		add(valuesLength,value);
	}

	public void add ( final int index, final DynArrayOfString value ) {
		final int copyLength = valuesLength - index;
		// ensure capacity
		if ( valuesLength == values.length ) {
			final DynArrayOfString[] valuesNew = new DynArrayOfString[values.length * 2];
			System.arraycopy(values,0,valuesNew,0,values.length);
			values = valuesNew;
		}
		// insert
		if ( copyLength > 0 )
			System.arraycopy(values,index,values,index + 1,copyLength);
		values[index] = value;
		valuesLength += 1;
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
			final DynArrayOfString subArray = values[i];
			if ( i > 0 )
				builder.append(separator);
			builder.append(prefix);
			for ( int j = 0; j < subArray.valuesLength; j += 1 ) {
				if ( j > 0 )
					builder.append(separator);
				builder.append(subArray.values[j]);
			}
			builder.append(suffix);
		}
		builder.append(suffix);
		return builder.toString();
	}

}

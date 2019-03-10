/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.utility;


import java.util.Iterator;


/**
 * @author Vitali Baumtrok
 */
public class KeySet<T> implements Iterable<T> {

	public String[] keys;
	public Object[] values;
	public int size;

	public KeySet ( ) {
		this(7);
	}

	public KeySet ( final int initialCapacity ) {
		keys = new String[initialCapacity];
		values = new Object[initialCapacity];
		size = 0;
	}

	@SuppressWarnings ( "unchecked" )
	public T get ( final String key ) {
		final int index = binarySearch(key);
		if ( index >= 0 ) {
			return (T) values[index];
		}
		return null;
	}

	public void clear ( ) {
		for ( int i = 0; i < size; i += 1 ) {
			keys[i] = null;
			values[i] = null;
		}
		size = 0;
	}

	public void put ( final String key, final T value ) {
		final int index = binarySearch(key);
		if ( index >= 0 ) {
			values[index] = value;
		} else {
			final int indexInsert = -1 * index - 1;
			if ( size > 0 ) {
				final int lengthCopyRest = size - indexInsert;
				if ( keys.length == size ) {
					final int capacityNew = keys.length * 2 + 1;
					final String[] keysNew = new String[capacityNew];
					final Object[] valuesNew = new Object[capacityNew];
					System.arraycopy(keys,0,keysNew,0,indexInsert);
					System.arraycopy(values,0,valuesNew,0,indexInsert);
					keys = keysNew;
					values = valuesNew;
				}
				if ( lengthCopyRest > 0 ) {
					System.arraycopy(keys,indexInsert,keys,indexInsert + 1,lengthCopyRest);
					System.arraycopy(values,indexInsert,values,indexInsert + 1,lengthCopyRest);
				}
			}
			keys[indexInsert] = key;
			values[indexInsert] = value;
			size += 1;
		}
	}

	@SuppressWarnings ( "unchecked" )
	public T remove ( final String key ) {
		final int index = binarySearch(key);
		if ( index >= 0 ) {
			final int indexNext = index + 1;
			final int lengthCopy = size - indexNext;
			final Object value = values[index];
			if ( indexNext < size ) {
				System.arraycopy(keys,indexNext,keys,index,lengthCopy);
				System.arraycopy(values,indexNext,values,index,lengthCopy);
			}
			size -= 1;
			keys[size] = null;
			values[size] = null;
			return (T) value;
		}
		return null;
	}

	public int size ( ) {
		return size;
	}

	private int binarySearch ( final String key ) {
		int left = 0;
		int right = size - 1;
		while ( left <= right ) {
			final int middle = (left + right) / 2;
			final int compareResult = keys[middle].compareTo(key);
			if ( compareResult < 0 )
				left = middle + 1;
			else if ( compareResult > 0 )
				right = middle - 1;
			else
				return middle;
		}
		return -left - 1;
	}

	@Override
	public Iterator<T> iterator ( ) {
		final ValuesIterator iter = new ValuesIterator();
		return iter;
	}

	protected class ValuesIterator implements Iterator<T> {

		protected int index = -1;

		@Override
		public boolean hasNext ( ) {
			index += 1;
			return index < size;
		}

		@SuppressWarnings ( "unchecked" )
		@Override
		public T next ( ) {
			return (T) values[index];
		}

	}

}

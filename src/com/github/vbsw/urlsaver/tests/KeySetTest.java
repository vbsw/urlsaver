/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import com.github.vbsw.urlsaver.utility.KeySet;


class KeySetTest {

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.utility.KeySet#get(String)}.
	 */
	@Test
	void testGet ( ) {
		final KeySet<String> keySet = new KeySet<String>();
		keySet.put("a","-a");
		keySet.put("b","-b");
		keySet.put("c","-c");
		assertEquals(false,keySet.get("a") == null);
		assertEquals(true,keySet.get("a").equals("-a"));
		assertEquals(false,keySet.get("b") == null);
		assertEquals(true,keySet.get("b").equals("-b"));
		assertEquals(false,keySet.get("c") == null);
		assertEquals(true,keySet.get("c").equals("-c"));
		assertEquals(null,keySet.get("d"));
	}

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.utility.KeySet#clear()}.
	 */
	@Test
	void testClear ( ) {
		final KeySet<String> keySet = new KeySet<String>();
		keySet.put("a","-a");
		keySet.put("b","-b");
		keySet.put("c","-c");
		assertEquals(3,keySet.size());
		keySet.clear();
		assertEquals(0,keySet.size());
	}

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.utility.KeySet#put(String,T)}.
	 */
	@Test
	void testPut ( ) {
		final KeySet<String> keySet = new KeySet<String>();
		assertEquals(0,keySet.size());
		keySet.put("a","-a");
		assertEquals(1,keySet.size());
		keySet.put("b","-b");
		assertEquals(2,keySet.size());
		keySet.put("c","-c");
		assertEquals(3,keySet.size());
	}

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.utility.KeySet#remove(String)}.
	 */
	@Test
	void testRemove ( ) {
		final KeySet<String> keySet = new KeySet<String>();
		keySet.put("a","-a");
		keySet.put("b","-b");
		keySet.put("c","-c");
		keySet.remove("b");
		assertEquals(null,keySet.get("b"));
	}

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.utility.KeySet#size()}.
	 */
	@Test
	void testSize ( ) {
		final KeySet<String> keySet = new KeySet<String>();
		assertEquals(0,keySet.size());
		keySet.put("a","-a");
		assertEquals(1,keySet.size());
		keySet.put("b","-b");
		assertEquals(2,keySet.size());
		keySet.put("c","-c");
		assertEquals(3,keySet.size());
	}

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.utility.KeySet#iterator()}.
	 */
	@Test
	void testIterator ( ) {
		final KeySet<String> keySet = new KeySet<String>();
		final Iterator<String> iter = keySet.iterator();
		keySet.put("a","-a");
		keySet.put("b","-b");
		keySet.put("c","-c");
		assertEquals(true,iter.hasNext());
		assertEquals(true,iter.next().equals("-a"));
		assertEquals(true,iter.hasNext());
		assertEquals(true,iter.next().equals("-b"));
		assertEquals(true,iter.hasNext());
		assertEquals(true,iter.next().equals("-c"));
		assertEquals(false,iter.hasNext());
	}

}

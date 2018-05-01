/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.urlsaver.tests;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.github.vbsw.urlsaver.args.ArgumentsConfig;
import com.github.vbsw.urlsaver.args.ArgumentsParser;


/**
 * @author Vitali Baumtrok
 */
class ArgumentsParserTest {

	private static final String path = "C:/tmp";
	private static final String str00 = "";
	private static final String str01 = "asdf";
	private static final String str02 = "-";
	private static final String str03 = "--";
	private static final String str04 = "--asdf";
	private static final String str05 = "-asdf";
	private static final String str06 = ArgumentsConfig.PREFERENCES_OPTION[0];
	private static final String str07 = "-" + ArgumentsConfig.PREFERENCES_OPTION[0];
	private static final String str08 = "--" + ArgumentsConfig.PREFERENCES_OPTION[0];
	private static final String str09 = str06 + ArgumentsConfig.ASSIGINMENT_OPERATOR[0];
	private static final String str10 = str07 + ArgumentsConfig.ASSIGINMENT_OPERATOR[0];
	private static final String str11 = str08 + ArgumentsConfig.ASSIGINMENT_OPERATOR[0];

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.args.ArgumentsParser#isOption(java.lang.String,java.lang.String[])}.
	 */
	@Test
	void testIsOption ( ) {
		assertEquals(false,ArgumentsParser.isOption(str00,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(false,ArgumentsParser.isOption(str01,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(false,ArgumentsParser.isOption(str02,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(false,ArgumentsParser.isOption(str03,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(false,ArgumentsParser.isOption(str04,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(false,ArgumentsParser.isOption(str05,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(true,ArgumentsParser.isOption(str06,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(true,ArgumentsParser.isOption(str07,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(true,ArgumentsParser.isOption(str08,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(false,ArgumentsParser.isOption(str09 + path,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(false,ArgumentsParser.isOption(str10 + path,ArgumentsConfig.PREFERENCES_OPTION));
		assertEquals(false,ArgumentsParser.isOption(str11 + path,ArgumentsConfig.PREFERENCES_OPTION));
	}

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.args.ArgumentsParser#getValue(java.lang.String,java.lang.String[],java.lang.String[])}.
	 */
	@Test
	void testGetValue ( ) {
		assertEquals("",ArgumentsParser.getValue(str00 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",ArgumentsParser.getValue(str01 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",ArgumentsParser.getValue(str02 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",ArgumentsParser.getValue(str03 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",ArgumentsParser.getValue(str04 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",ArgumentsParser.getValue(str05 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,ArgumentsParser.getValue(str06 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,ArgumentsParser.getValue(str07 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,ArgumentsParser.getValue(str08 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,ArgumentsParser.getValue(str09 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,ArgumentsParser.getValue(str10 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,ArgumentsParser.getValue(str11 + path,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
	}

}

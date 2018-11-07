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
import com.github.vbsw.urlsaver.args.CommandLineInfo;
import com.github.vbsw.urlsaver.utility.Parser;


/**
 * @author Vitali Baumtrok
 */
class CommandLineInfoTest {

	private static final String path = "C:/tmp";
	private static final String str00 = "";
	private static final String str01 = "asdf";
	private static final String str02 = "-";
	private static final String str03 = "--";
	private static final String str04 = "--asdf";
	private static final String str05 = "-asdf";
	private static final String str06 = ArgumentsConfig.SETTINGS_OPTION[0];
	private static final String str07 = "-" + ArgumentsConfig.SETTINGS_OPTION[0];
	private static final String str08 = "--" + ArgumentsConfig.SETTINGS_OPTION[0];
	private static final String str09 = str06 + ArgumentsConfig.ASSIGINMENT_OPERATOR[0];
	private static final String str10 = str07 + ArgumentsConfig.ASSIGINMENT_OPERATOR[0];
	private static final String str11 = str08 + ArgumentsConfig.ASSIGINMENT_OPERATOR[0];

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.args.CommandLineInfo#isOption(java.lang.String,java.lang.String[])}.
	 */
	@Test
	void testIsOption ( ) {
		assertEquals(false,CommandLineInfo.isOption(str00,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(false,CommandLineInfo.isOption(str01,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(false,CommandLineInfo.isOption(str02,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(false,CommandLineInfo.isOption(str03,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(false,CommandLineInfo.isOption(str04,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(false,CommandLineInfo.isOption(str05,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(true,CommandLineInfo.isOption(str06,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(true,CommandLineInfo.isOption(str07,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(true,CommandLineInfo.isOption(str08,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(false,CommandLineInfo.isOption(str09 + path,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(false,CommandLineInfo.isOption(str10 + path,ArgumentsConfig.SETTINGS_OPTION));
		assertEquals(false,CommandLineInfo.isOption(str11 + path,ArgumentsConfig.SETTINGS_OPTION));
	}

	/**
	 * Test method for {@link com.github.vbsw.urlsaver.utility.Parser#getArgumentValue(java.lang.String,java.lang.String[],java.lang.String[])}.
	 */
	@Test
	void testGetValue ( ) {
		assertEquals("",Parser.getArgumentValue(str00 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",Parser.getArgumentValue(str01 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",Parser.getArgumentValue(str02 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",Parser.getArgumentValue(str03 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",Parser.getArgumentValue(str04 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals("",Parser.getArgumentValue(str05 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,Parser.getArgumentValue(str06 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,Parser.getArgumentValue(str07 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,Parser.getArgumentValue(str08 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,Parser.getArgumentValue(str09 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,Parser.getArgumentValue(str10 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
		assertEquals(path,Parser.getArgumentValue(str11 + path,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR));
	}

}

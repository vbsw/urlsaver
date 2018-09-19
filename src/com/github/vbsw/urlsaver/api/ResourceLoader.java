/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * @author Vitali Baumtrok
 */
public abstract class ResourceLoader {

	public abstract void initialize ( );

	public abstract LaunchSource getLaunchSource ( );

	public Charset getCharset ( ) {
		return StandardCharsets.UTF_8;
	}

}

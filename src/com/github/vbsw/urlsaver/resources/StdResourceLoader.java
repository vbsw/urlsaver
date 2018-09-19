/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.resources;


import com.github.vbsw.urlsaver.api.ResourceLoader;


/**
 * @author Vitali Baumtrok
 */
public class StdResourceLoader extends ResourceLoader {

	protected StdLaunchSource source;

	@Override
	public void initialize ( ) {
		source = createLaunchSource();
	}

	protected StdLaunchSource createLaunchSource ( ) {
		final StdLaunchSource source = new StdLaunchSource();
		return source;
	}

	@Override
	public StdLaunchSource getLaunchSource ( ) {
		return source;
	}

}

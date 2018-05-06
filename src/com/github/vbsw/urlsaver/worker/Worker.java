/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.worker;


import com.github.vbsw.urlsaver.pref.Preferences;


/**
 * @author Vitali Baumtrok
 */
public class Worker {

	public static void initialize ( ) {
		if ( Preferences.getURLsFileAutoloadAll().getModifiedValue() )
			Worker.reloadAllFiles();
	}

	public static void reloadAllFiles ( ) {
		// TODO Auto-generated method stub
	}

}

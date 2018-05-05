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
public class DB {

	public static boolean isSaved ( ) {
		final int limit = DBFiles.getPaths().size();
		for ( int i = 0; i < limit; i += 1 )
			if ( DBFiles.isDirty(i) )
				return false;
		return true;
	}

}

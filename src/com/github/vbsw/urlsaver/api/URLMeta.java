/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


/**
 * @author Vitali Baumtrok
 */
public class URLMeta {

	public static final int UNKNOWN = 0;
	public static final int NONE = 1;
	public static final int ACCESSED = 2;
	public static final int SCORE = 3;

	public int getMetaKeyID ( final String metaKey ) {
		final int metaKeyID;
		if ( isNoneKey(metaKey) )
			metaKeyID = URLMeta.NONE;
		else if ( isAccessedKey(metaKey) )
			metaKeyID = URLMeta.ACCESSED;
		else if ( isScoreKey(metaKey) )
			metaKeyID = URLMeta.SCORE;
		else
			metaKeyID = URLMeta.UNKNOWN;
		return metaKeyID;
	}

	public boolean isMetaDataSignature ( final byte[] bytes, final int offset ) {
		return offset < bytes.length && bytes[offset] == '\\';
	}

	private boolean isNoneKey ( final String metaKey ) {
		return metaKey.equals("\\none");
	}

	private boolean isAccessedKey ( final String metaKey ) {
		return metaKey.equals("\\accessed");
	}

	private boolean isScoreKey ( final String metaKey ) {
		return metaKey.equals("\\score");
	}

}

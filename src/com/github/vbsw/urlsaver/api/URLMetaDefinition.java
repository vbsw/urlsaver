/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


/**
 * @author Vitali Baumtrok
 */
public class URLMetaDefinition {

	public static final int DATE = 0;
	public static final int SCORE = 1;
	public static final int NONE = 2;
	public static final int UNKNOWN = 3;

	public String metaKey;
	public String metaValue;
	public int metaKeyID;

	public void setMeta ( final String metaKey, final String metaValue ) {
		this.metaKey = metaKey;
		this.metaKeyID = getMetaKeyID(metaKey);
		this.metaValue = metaValue;
	}

	public boolean isMetaDataSignature ( final byte[] bytes, final int offset ) {
		return offset < bytes.length && bytes[offset] == '\\';
	}

	@Override
	public String toString ( ) {
		final String str = "[" + metaKey + "=" + metaValue + "]";
		return str;
	}

	private int getMetaKeyID ( final String metaKey ) {
		final int metaKeyID;
		if ( isNoneKey(metaKey) )
			metaKeyID = URLMetaDefinition.NONE;
		else if ( isDateKey(metaKey) )
			metaKeyID = URLMetaDefinition.DATE;
		else if ( isScoreKey(metaKey) )
			metaKeyID = URLMetaDefinition.SCORE;
		else
			metaKeyID = URLMetaDefinition.UNKNOWN;
		return metaKeyID;
	}

	private boolean isNoneKey ( final String metaKey ) {
		return metaKey.equals("\\none");
	}

	private boolean isDateKey ( final String metaKey ) {
		return metaKey.equals("\\date");
	}

	private boolean isScoreKey ( final String metaKey ) {
		return metaKey.equals("\\score");
	}

}

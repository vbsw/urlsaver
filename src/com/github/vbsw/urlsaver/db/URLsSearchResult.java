/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.util.Comparator;


/**
 * @author Vitali Baumtrok
 */
public class URLsSearchResult {

	public static final Comparator<URLsSearchResult> comparator = new ResultComparator();

	private String url;
	private String score;
	private String accessed;

	public String getURL ( ) {
		return url;
	}

	public void setURL ( final String url ) {
		this.url = url;
	}

	public String getScore ( ) {
		return score;
	}

	public void setScore ( final String score ) {
		this.score = score;
	}

	public String getAccessed ( ) {
		return accessed;
	}

	public void setAccessed ( final String accessed ) {
		this.accessed = accessed;
	}

	private static class ResultComparator implements Comparator<URLsSearchResult> {

		@Override
		public int compare ( final URLsSearchResult a, final URLsSearchResult b ) {
			final String aStr = a.getURL();
			final String bStr = b.getURL();
			return aStr.compareTo(bStr);
		}

	}

}


//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.urls;


/**
 * @author Vitali Baumtrok
 */
public final class UrlsViewData {

	public final UrlsSearchResult searchResult = new UrlsSearchResult();

	public String searchTagsString = ""; //$NON-NLS-1$
	public int selectedIndex = -1;

	public void clear ( ) {
		searchResult.clear();
		searchTagsString = ""; //$NON-NLS-1$
		selectedIndex = -1;
	}

}

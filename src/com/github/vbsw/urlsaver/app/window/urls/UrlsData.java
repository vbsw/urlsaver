
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.urls;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.github.vbsw.urlsaver.utility.SortedUniqueStringList;


/**
 * @author Vitali Baumtrok
 */
public final class UrlsData {

	public static final int INITIAL_CAPACITY = 250;

	public final ArrayList<String> urls = new ArrayList<String>(INITIAL_CAPACITY);
	public final ArrayList<String> tags = new ArrayList<String>(INITIAL_CAPACITY);
	public final ArrayList<SortedUniqueStringList> urlTagsList = new ArrayList<SortedUniqueStringList>(INITIAL_CAPACITY);
	public final ArrayList<SortedUniqueStringList> tagUrlsList = new ArrayList<SortedUniqueStringList>(INITIAL_CAPACITY);

	public final int getUrlIndex ( final String url ) {
		return Collections.binarySearch(urls,url);
	}

	public final int getTagIndex ( final String tag ) {
		return Collections.binarySearch(tags,tag);
	}

	public final int addUrl ( final String url ) {
		final int index = getUrlIndex(url);

		if ( index < 0 ) {
			final int insertIndex = -index - 1;
			final SortedUniqueStringList newUrlTags = new SortedUniqueStringList();

			urls.add(insertIndex,url);
			urlTagsList.add(insertIndex,newUrlTags);

			return insertIndex;

		} else {
			return index;
		}
	}

	public final int addTag ( final String tag ) {
		final int index = getTagIndex(tag);

		if ( index < 0 ) {
			final int insertIndex = -index - 1;
			final SortedUniqueStringList newTagUrls = new SortedUniqueStringList();

			tags.add(insertIndex,tag);
			tagUrlsList.add(insertIndex,newTagUrls);

			return insertIndex;

		} else {
			return index;
		}
	}

	public final boolean addTagToUrl ( final int urlIndex, final String tag ) {
		final ArrayList<String> urlTags = urlTagsList.get(urlIndex);
		final int urlTagsIndex = Collections.binarySearch(urlTags,tag);

		if ( urlTagsIndex < 0 ) {
			final int urlTagsInsertIndex = -urlTagsIndex - 1;
			final int tagIndex = addTag(tag);
			final ArrayList<String> tagUrls = tagUrlsList.get(tagIndex);
			final String url = urls.get(urlIndex);
			final int tagUrlsIndex = Collections.binarySearch(tagUrls,url);

			if ( tagUrlsIndex < 0 ) {
				final int tagUrlsInsertIndex = -tagUrlsIndex - 1;

				tagUrls.add(tagUrlsInsertIndex,url);
			}
			urlTags.add(urlTagsInsertIndex,tag);

			return true;

		} else {
			return false;
		}
	}

	public final boolean removeUrl ( final String url ) {
		final int urlIndex = Collections.binarySearch(urls,url);

		if ( urlIndex >= 0 ) {
			final ArrayList<String> urlTags = urlTagsList.get(urlIndex);

			for ( String tag: urlTags ) {
				final int tagIndex = getTagIndex(tag);
				final ArrayList<String> tagUrls = tagUrlsList.get(tagIndex);
				final int tagUrlsIndex = Collections.binarySearch(tagUrls,url);
				tagUrls.remove(tagUrlsIndex);
			}
			urls.remove(urlIndex);
			urlTagsList.remove(urlIndex);

			return true;

		} else {
			return false;
		}
	}

	public final boolean removeTagFromUrl ( final int urlIndex, final String tag ) {
		final ArrayList<String> urlTags = urlTagsList.get(urlIndex);
		final int urlTagsIndex = Collections.binarySearch(urlTags,tag);

		if ( urlTagsIndex >= 0 ) {
			final int tagIndex = getTagIndex(tag);
			final ArrayList<String> tagUrls = tagUrlsList.get(tagIndex);
			final String url = urls.get(urlIndex);
			final int tagUrlsIndex = Collections.binarySearch(tagUrls,url);

			urlTags.remove(urlTagsIndex);
			tagUrls.remove(tagUrlsIndex);

			return true;

		} else {
			return false;
		}
	}

	public void setTagsOfUrl ( final int urlIndex, final Collection<String> tags ) {
		final ArrayList<String> urlTags = urlTagsList.get(urlIndex);

		while ( urlTags.isEmpty() == false ) {
			final int urlTagsIndex = urlTags.size() - 1;
			final String tag = urlTags.get(urlTagsIndex);
			final int tagIndex = getTagIndex(tag);
			final ArrayList<String> tagUrls = tagUrlsList.get(tagIndex);
			final String url = urls.get(urlIndex);
			final int tagUrlsIndex = Collections.binarySearch(tagUrls,url);

			urlTags.remove(urlTagsIndex);
			tagUrls.remove(tagUrlsIndex);
		}

		for ( String tag: tags ) {
			addTagToUrl(urlIndex,tag);
		}
	}

}

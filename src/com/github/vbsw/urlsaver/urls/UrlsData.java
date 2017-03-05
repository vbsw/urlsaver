/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
 * 
 * This file is part of URL Saver.
 * 
 * URL Saver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * URL Saver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with URL Saver.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.vbsw.urlsaver.urls;


import java.util.ArrayList;
import java.util.Collections;

import com.github.vbsw.urlsaver.SortedUniqueStringList;


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
			final ArrayList<String> urlTagsRelation = urlTagsList.get(urlIndex);

			for ( String tag: urlTagsRelation ) {
				final int tagIndex = getTagIndex(tag);
				final ArrayList<String> tagUrlsRelation = tagUrlsList.get(tagIndex);
				final int tagUrlsRelationIndex = Collections.binarySearch(tagUrlsRelation,url);
				tagUrlsRelation.remove(tagUrlsRelationIndex);
			}
			urls.remove(urlIndex);
			urlTagsList.remove(urlIndex);

			return true;

		} else {
			return false;
		}
	}

	public final boolean removeTagFromUrl ( final int urlIndex, final String tag ) {
		final ArrayList<String> urlTagsRelation = urlTagsList.get(urlIndex);
		final int urlTagsRelationIndex = Collections.binarySearch(urlTagsRelation,tag);

		if ( urlTagsRelationIndex >= 0 ) {
			final int tagIndex = getTagIndex(tag);
			final ArrayList<String> tagUrlsRelation = tagUrlsList.get(tagIndex);
			final String url = urls.get(urlIndex);
			final int tagUrlsRelationIndex = Collections.binarySearch(tagUrlsRelation,url);

			urlTagsRelation.remove(urlTagsRelationIndex);
			tagUrlsRelation.remove(tagUrlsRelationIndex);

			return true;

		} else {
			return false;
		}
	}

}

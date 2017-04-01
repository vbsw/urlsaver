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


package com.github.vbsw.urlsaver.app.window.urls;


import java.util.ArrayList;
import java.util.Collections;

import com.github.vbsw.urlsaver.SortedUniqueStringList;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
public final class UrlsSearchResult extends SortedUniqueStringList {

	private final SortedUniqueStringList tags = new SortedUniqueStringList();
	private final TagIndices tagIndices = new TagIndices();

	public void fillByPrefix ( final UrlsData urlsData, final String tagsString ) {
		super.clear();
		tags.setStrings(tagsString);

		if ( tags.size() > 0 ) {
			final String firstTag = tags.get(0);
			final int firstTagIndicesCount = tagIndices.fillByPrefix(urlsData,firstTag);

			if ( firstTagIndicesCount > 0 ) {
				for ( int i = 0; i < firstTagIndicesCount; i += 1 ) {
					final int tagIndex = tagIndices.get(i);
					final ArrayList<String> tagUrls = urlsData.tagUrlsList.get(tagIndex);

					for ( String tagUrl: tagUrls ) {
						super.addSorted(tagUrl);
					}
				}

				for ( int k = 1; k < tags.size(); k += 1 ) {
					final String tag = tags.get(k);
					final int tagIndicesCount = tagIndices.fillByPrefix(urlsData,tag);

					if ( tagIndicesCount > 0 ) {

						SearchUrls:
						for ( int j = 0; j < super.size(); j += 1 ) {
							final String url = super.get(j);

							for ( int i = 0; i < tagIndicesCount; i += 1 ) {
								final int tagIndex = tagIndices.get(i);
								final ArrayList<String> tagUrls = urlsData.tagUrlsList.get(tagIndex);
								final int urlIndexInTagUrls = Collections.binarySearch(tagUrls,url);

								if ( urlIndexInTagUrls >= 0 ) {
									continue SearchUrls;
								}
							}
							super.remove(j);
							j -= 1;
						}

					} else {
						super.clear();
						break;
					}
				}
			}
		}
	}

	public void fillByWord ( final UrlsData urlsData, final String tagsString ) {
		super.clear();
		tags.setStrings(tagsString);

		if ( tags.size() > 0 ) {
			final String firstTag = tags.get(0);
			final int firstTagIndex = urlsData.getTagIndex(firstTag);

			if ( firstTagIndex >= 0 ) {
				final ArrayList<String> firstTagUrls = urlsData.tagUrlsList.get(firstTagIndex);

				super.addAll(firstTagUrls);

				for ( int k = 1; k < tags.size(); k += 1 ) {
					final String tag = tags.get(k);
					final int tagIndex = urlsData.getTagIndex(tag);

					if ( tagIndex >= 0 ) {
						final ArrayList<String> tagUrls = urlsData.tagUrlsList.get(tagIndex);

						for ( int i = 0; i < super.size(); i += 1 ) {
							final String url = super.get(i);
							final int urlIndexInTagUrls = Collections.binarySearch(tagUrls,url);

							if ( urlIndexInTagUrls < 0 ) {
								super.remove(i);
								i -= 1;
							}
						}

					} else {
						super.clear();
						break;
					}
				}
			}
		}
	}

	public SortedUniqueStringList getTags ( ) {
		return tags;
	}

}

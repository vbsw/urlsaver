/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.util.ArrayList;
import java.util.Collections;

import com.github.vbsw.urlsaver.api.URLMetaDefinition;


/**
 * @author Vitali Baumtrok
 */
public class URLsSearch {

	private final ArrayList<URLsSearchResult> results;
	private final ArrayList<String> resultsStrings;
	private final DynArrayOfString urls;
	private final DynArrayOfString tags;
	private final DynArrayOfString2 urlsOnTags;
	private final DynArrayOfString2 metaDataOnURLs;
	private final DynArrayOfInt tagIndices;

	public URLsSearch ( final DynArrayOfString urls, final DynArrayOfString tags, final DynArrayOfString2 wordsToTags, final DynArrayOfString2 metaDataOnURLs ) {
		this.results = new ArrayList<>();
		this.resultsStrings = new ArrayList<>();
		this.urls = urls;
		this.tags = tags;
		this.urlsOnTags = wordsToTags;
		this.metaDataOnURLs = metaDataOnURLs;
		this.tagIndices = new DynArrayOfInt();
	}

	public ArrayList<URLsSearchResult> getResults ( ) {
		return results;
	}

	public void searchByPrefix ( final DynArrayOfString prefixes ) {
		results.clear();
		resultsStrings.clear();
		if ( prefixes.valuesLength > 0 ) {
			for ( int k = 0; k < prefixes.valuesLength; k += 1 ) {
				fillTagIndicesByPrefix(prefixes.values[k]);
				if ( tagIndices.valuesLength > 0 ) {
					if ( k > 0 ) {
						for ( int j = 0; j < resultsStrings.size(); j += 1 ) {
							final String url = resultsStrings.get(j);
							final boolean tagged = isTagged(url);
							if ( !tagged ) {
								resultsStrings.remove(j);
								j -= 1;
							}
						}
					} else {
						for ( int i = 0; i < tagIndices.valuesLength; i += 1 ) {
							final int tagIndex = tagIndices.values[i];
							final DynArrayOfString wordsOnTag = urlsOnTags.values[tagIndex];
							addURLsStrings(wordsOnTag);
						}
					}
				} else {
					resultsStrings.clear();
				}
				if ( resultsStrings.isEmpty() )
					break;
			}
		}
		fillResults();
	}

	public void searchByWord ( final DynArrayOfString searchTags ) {
		results.clear();
		resultsStrings.clear();
		if ( searchTags.valuesLength > 0 ) {
			for ( int k = 0; k < searchTags.valuesLength; k += 1 ) {
				final String searchTag = searchTags.values[0];
				final int searchTagIndex = tags.binarySearch(searchTag);
				if ( searchTagIndex >= 0 ) {
					final DynArrayOfString urlsOnTag = urlsOnTags.values[searchTagIndex];
					if ( k > 0 ) {
						for ( int j = 0; j < resultsStrings.size(); j += 1 ) {
							final String url = resultsStrings.get(j);
							final boolean contains = urlsOnTag.contains(url);
							if ( !contains ) {
								resultsStrings.remove(j);
								j -= 1;
							}
						}
					} else {
						appendURLsStrings(urlsOnTag);
					}
				} else {
					resultsStrings.clear();
					break;
				}
			}
		}
		fillResults();
	}

	public void removeFromResult ( final String url ) {
		final int urlIndex = Collections.binarySearch(resultsStrings,url);
		if ( urlIndex >= 0 ) {
			results.remove(urlIndex);
			resultsStrings.remove(urlIndex);
		}
	}

	private void fillTagIndicesByPrefix ( final String prefix ) {
		final int tagIndex = tags.binarySearch(prefix);
		final int fromLeft;
		tagIndices.clear();
		if ( tagIndex >= 0 ) {
			fromLeft = tagIndex + 1;
			tagIndices.add(tagIndex);
		} else {
			fromLeft = -tagIndex - 1;
		}
		for ( int i = fromLeft; i < tags.valuesLength; i += 1 )
			if ( tags.values[i].startsWith(prefix) )
				tagIndices.add(i);
			else
				break;
	}

	private boolean isTagged ( final String url ) {
		for ( int i = 0; i < tagIndices.valuesLength; i += 1 ) {
			final int tagIndex = tagIndices.values[i];
			final DynArrayOfString wordsOnTag = urlsOnTags.values[tagIndex];
			final boolean tagged = wordsOnTag.contains(url);
			if ( tagged )
				return true;
		}
		return false;
	}

	private void addURLsStrings ( final DynArrayOfString urls ) {
		for ( int i = 0; i < urls.valuesLength; i += 1 ) {
			final String url = urls.values[i];
			final int index = Collections.binarySearch(resultsStrings,url);
			if ( index < 0 )
				resultsStrings.add(-index - 1,url);
		}
	}

	private void appendURLsStrings ( final DynArrayOfString urls ) {
		for ( int i = 0; i < urls.valuesLength; i += 1 ) {
			final String url = urls.values[i];
			resultsStrings.add(url);
		}
	}

	private void fillResults ( ) {
		for ( final String url: resultsStrings ) {
			final int urlIndex = urls.binarySearch(url);
			final String accessed = metaDataOnURLs.values[urlIndex].values[URLMetaDefinition.DATE];
			final String score = metaDataOnURLs.values[urlIndex].values[URLMetaDefinition.SCORE];
			final URLsSearchResult result = new URLsSearchResult();
			result.setURL(url);
			result.setDate(accessed);
			result.setScore(score);
			results.add(result);
		}
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.util.ArrayList;
import java.util.Collections;


/**
 * @author Vitali Baumtrok
 */
public class WordSearch {

	private final ArrayList<String> result;
	private final DynArrayOfString tags;
	private final DynArrayOfString2 wordsOnTags;
	private final DynArrayOfInt tagIndices;

	public WordSearch ( final DynArrayOfString tags, final DynArrayOfString2 wordsToTags ) {
		this.result = new ArrayList<>();
		this.tags = tags;
		this.wordsOnTags = wordsToTags;
		this.tagIndices = new DynArrayOfInt();
	}

	public ArrayList<String> getResult ( ) {
		return result;
	}

	public void searchByPrefix ( final DynArrayOfString prefixes ) {
		result.clear();
		if ( prefixes.valuesLength > 0 ) {
			for ( int k = 0; k < prefixes.valuesLength; k += 1 ) {
				fillTagIndicesByPrefix(prefixes.values[k]);
				if ( tagIndices.valuesLength > 0 ) {
					if ( k > 0 ) {
						for ( int j = 0; j < result.size(); j += 1 ) {
							final String word = result.get(j);
							final boolean tagged = isTagged(word);
							if ( !tagged ) {
								result.remove(j);
								j -= 1;
							}
						}
					} else {
						for ( int i = 0; i < tagIndices.valuesLength; i += 1 ) {
							final int tagIndex = tagIndices.values[i];
							final DynArrayOfString wordsOnTag = wordsOnTags.values[tagIndex];
							addWordsToResult(wordsOnTag);
						}
					}
				} else {
					result.clear();
				}
				if ( result.isEmpty() )
					break;
			}
		}
	}

	public void searchByWord ( final DynArrayOfString searchTags ) {
		result.clear();
		if ( searchTags.valuesLength > 0 ) {
			for ( int k = 0; k < searchTags.valuesLength; k += 1 ) {
				final String searchTag = searchTags.values[0];
				final int searchTagIndex = tags.binarySearch(searchTag);
				if ( searchTagIndex >= 0 ) {
					final DynArrayOfString wordsOnTag = wordsOnTags.values[searchTagIndex];
					if ( k > 0 ) {
						for ( int j = 0; j < result.size(); j += 1 ) {
							final String word = result.get(j);
							final boolean contains = wordsOnTag.contains(word);
							if ( !contains ) {
								result.remove(j);
								j -= 1;
							}
						}
					} else {
						appendWordsToResult(wordsOnTag);
					}
				} else {
					result.clear();
					break;
				}
			}
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

	private boolean isTagged ( final String word ) {
		for ( int i = 0; i < tagIndices.valuesLength; i += 1 ) {
			final int tagIndex = tagIndices.values[i];
			final DynArrayOfString wordsOnTag = wordsOnTags.values[tagIndex];
			final boolean tagged = wordsOnTag.contains(word);
			if ( tagged )
				return true;
		}
		return false;
	}

	private void addWordsToResult ( final DynArrayOfString words ) {
		for ( int i = 0; i < words.valuesLength; i += 1 ) {
			final String word = words.values[i];
			final int index = Collections.binarySearch(result,word);
			if ( index < 0 )
				result.add(-index - 1,word);
		}
	}

	private void appendWordsToResult ( final DynArrayOfString words ) {
		for ( int i = 0; i < words.valuesLength; i += 1 ) {
			final String word = words.values[i];
			result.add(word);
		}
	}

}

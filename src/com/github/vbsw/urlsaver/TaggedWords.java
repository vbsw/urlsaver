/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2016 Vitali Baumtrok
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


package com.github.vbsw.urlsaver;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.vbsw.urlsaver.TaggedWords.Word;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
public class TaggedWords extends ArrayList<Word> {

	public static final int INITIAL_CAPACITY = 250;

	private final TagList tagList = new TagList();

	public TaggedWords ( ) {
		super(INITIAL_CAPACITY);
	}

	/**
	 * Creates a new {@link Word} and adds it to the list, if it does not exist;
	 * otherwise the object from the list is returned.
	 * 
	 * @param wordString The word string to add as {@link Word}.
	 * @return A {@link Word} with wordStr.
	 */
	public Word add ( final String wordString ) {
		final int wordIndex = binarySearch(wordString);

		if ( wordIndex < 0 ) {
			final int insertIndex = -wordIndex - 1;
			final Word word = addNewWord(wordString,insertIndex);
			return word;

		} else {
			final Word wordData = super.get(wordIndex);
			return wordData;
		}
	}

	public void search ( final ArrayList<Word> words, final String... tagStrings ) {

	}

	public int binarySearch ( final String wordStringSearched ) {
		final int size = super.size();

		if ( size > 1 ) {
			int compareResult = -1;
			int middleIndex = 0;

			for ( int leftIndex = 0, rightIndex = size - 1; leftIndex <= rightIndex; ) {
				middleIndex = leftIndex + ((rightIndex - leftIndex) / 2);
				final Word word = get(middleIndex);
				compareResult = word.string.compareTo(wordStringSearched);

				if ( compareResult < 0 ) {
					leftIndex = middleIndex + 1;

				} else if ( compareResult > 0 ) {
					rightIndex = middleIndex - 1;

				} else {
					return middleIndex;
				}
			}
			return compareResult < 0 ? -middleIndex - 2 : compareResult > 0 ? -middleIndex - 1 : 0;

		} else if ( size == 1 ) {
			final Word word = super.get(0);
			final int compareResult = word.string.compareTo(wordStringSearched);
			return compareResult < 0 ? -2 : compareResult > 0 ? -1 : 0;

		} else {
			return -1;
		}
	}

	public int binarySearch ( final Word word ) {
		return Collections.binarySearch(this,word,Word.COMPARATOR);
	}

	private Word addNewWord ( final String wordString, final int insertIndex ) {
		final Word word = new Word(tagList,wordString,insertIndex);
		super.add(insertIndex,word);
		incrementIndices(insertIndex + 1);
		return word;
	}

	private void incrementIndices ( final int startWordIndex ) {
		for ( int i = startWordIndex; i < super.size(); i += 1 ) {
			final Word wordData = super.get(i);
			wordData.index += 1;
		}
	}

	public static class Word {
		public static final Comparator COMPARATOR = new Comparator();
		public static final int INITIAL_CAPACITY = 10;
		public static final String EMPTY_STRING = ""; //$NON-NLS-1$

		private final StringBuilder stringBuilder = new StringBuilder(250);
		private final TagList tagList;
		public final String string;
		public int index;
		public final ArrayList<Tag> tags;
		public String tagsString;

		public Word ( final TagList tagList, final String wordStr, final int index ) {
			this.tagList = tagList;
			this.string = wordStr;
			this.index = index;
			this.tags = new ArrayList<Tag>(INITIAL_CAPACITY);
			this.tagsString = EMPTY_STRING;
		}

		public Tag addTag ( final String tagString ) {
			final int index = tagList.binarySearch(tagString);

			if ( index < 0 ) {
				final int insertIndex = -index - 1;
				final Tag tag = new Tag(tagString,insertIndex);
				tagList.add(insertIndex,tag);
				return addTag(tag);

			} else {
				final Tag tag = tagList.get(index);
				return addTag(tag);
			}
		}

		/**
		 * Adds a {@link Tag} to {@code tags}, but not to {@code tagList}.
		 * 
		 * @param tag The {@link Tag} to add.
		 * @return The added {@link Tag}.
		 */
		private Tag addTag ( final Tag tag ) {
			final int tagIndex = Collections.binarySearch(tags,tag,Tag.COMPARATOR);

			if ( tagIndex < 0 ) {
				final int tagWordIndex = tag.binarySearch(this);
				final int tagInsertIndex = -tagIndex - 1;
				tags.add(tagInsertIndex,tag);
				tagsToString();

				if ( tagWordIndex < 0 ) {
					final int tagWordInsertIndex = -tagWordIndex - 1;
					tag.words.add(tagWordInsertIndex,this);
				}
			}
			return tag;
		}

		private void tagsToString ( ) {
			stringBuilder.delete(0,stringBuilder.length());

			for ( Tag tag: tags ) {
				if ( stringBuilder.length() > 0 ) {
					stringBuilder.append(' ');
					stringBuilder.append(tag.string);

				} else {
					stringBuilder.append(tag.string);
				}
			}
			tagsString = stringBuilder.toString();
		}

		private static class Comparator implements java.util.Comparator<Word> {
			@Override
			public int compare ( final Word obj1, final Word obj2 ) {
				final String wordString1 = obj1.string;
				final String wordString2 = obj2.string;
				return wordString1.compareTo(wordString2);
			}
		}
	}

	public static class Tag {
		public static final Comparator COMPARATOR = new Comparator();
		public static final int INITIAL_CAPACITY = 10;

		public final String string;
		public int index;
		private final ArrayList<Word> words;

		public Tag ( final String tagStr, final int index ) {
			this.string = tagStr;
			this.index = index;
			this.words = new ArrayList<Word>(INITIAL_CAPACITY);
		}

		public int binarySearch ( final Word word ) {
			final int index = Collections.binarySearch(words,word,Word.COMPARATOR);
			return index;
		}

		private static class Comparator implements java.util.Comparator<Tag> {
			@Override
			public int compare ( final Tag obj1, final Tag obj2 ) {
				final String tagString1 = obj1.string;
				final String tagString2 = obj2.string;
				return tagString1.compareTo(tagString2);
			}
		}

	}

	public static class TagList extends ArrayList<Tag> {

		public static final int INITIAL_CAPACITY = 250;

		public TagList ( ) {
			super(INITIAL_CAPACITY);
		}

		public int binarySearch ( final String tagString ) {
			if ( size() > 1 ) {
				int compareResult = -1;
				int middleIndex = 0;

				for ( int leftIndex = 0, rightIndex = size() - 1; leftIndex <= rightIndex; ) {
					middleIndex = leftIndex + ((rightIndex - leftIndex) / 2);
					final Tag tag = super.get(middleIndex);
					compareResult = tag.string.compareTo(tagString);

					if ( compareResult < 0 ) {
						leftIndex = middleIndex + 1;

					} else if ( compareResult > 0 ) {
						rightIndex = middleIndex - 1;

					} else {
						return middleIndex;
					}
				}
				return compareResult < 0 ? -middleIndex - 2 : compareResult > 0 ? -middleIndex - 1 : 0;

			} else if ( size() == 1 ) {
				final Tag tag = super.get(0);
				final int compareResult = tag.string.compareTo(tagString);
				return compareResult < 0 ? -2 : compareResult > 0 ? -1 : 0;

			} else {
				return -1;
			}
		}

		public int binarySearch ( final Tag tag ) {
			return Collections.binarySearch(this,tag,Tag.COMPARATOR);
		}
	}

	public static class SearchResult extends ArrayList<Word> {
		private static final int INITIAL_WORD_CAPACITY = 250;
		private static final int INITIAL_TAG_CAPACITY = 10;

		private final ArrayList<Tag> searchedTags = new ArrayList<Tag>(INITIAL_TAG_CAPACITY);

		public SearchResult ( ) {
			super(INITIAL_WORD_CAPACITY);
		}

		public void searchAND ( final TaggedWords taggedWords, final List<String> tagStrings ) {
			super.clear();
			searchedTags.clear();

			for ( String tagString: tagStrings ) {
				final int index = taggedWords.tagList.binarySearch(tagString);

				if ( index >= 0 ) {
					final Tag tag = taggedWords.tagList.get(index);
					searchedTags.add(tag);

				} else {
					return;
				}
			}
			searchAND();
		}

		public void searchAND ( final TaggedWords taggedWords, final String... tagStrings ) {
			super.clear();
			searchedTags.clear();

			for ( String tagString: tagStrings ) {
				final int index = taggedWords.tagList.binarySearch(tagString);

				if ( index >= 0 ) {
					final Tag tag = taggedWords.tagList.get(index);
					searchedTags.add(tag);

				} else {
					return;
				}
			}
			searchAND();
		}

		private void searchAND ( ) {
			final int searchedTagsSize = searchedTags.size();
			if ( searchedTagsSize > 1 ) {
				prefill();
				for ( Tag tag: searchedTags ) {
					for ( int i = 0; i < super.size(); i += 1 ) {
						final Word word = super.get(i);
						final int index = tag.binarySearch(word);
						if ( index < 0 ) {
							super.remove(i);
							i -= 1;
						}
					}
				}

			} else if ( searchedTagsSize == 1 ) {
				final Tag tag = searchedTags.get(0);
				addAll(tag.words);
			}
		}

		private void prefill ( ) {
			final int searchedTagsSize = searchedTags.size();
			final Tag tag = searchedTags.remove(searchedTagsSize - 1);
			addAll(tag.words);
		}
	}

}

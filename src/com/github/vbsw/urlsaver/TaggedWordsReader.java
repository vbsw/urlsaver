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


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javafx.concurrent.Service;
import javafx.concurrent.Task;


/**
 * @author Vitali Baumtrok
 */
public class TaggedWordsReader extends Service<TaggedWords> {

	private final Path filePath;

	public TaggedWordsReader ( final Path filePath ) {
		this.filePath = filePath;
	}

	@Override
	protected Task<TaggedWords> createTask ( ) {
		final Task<TaggedWords> task = new ReaderTask();
		return task;
	}

	private class ReaderTask extends Task<TaggedWords> {
		@Override
		protected TaggedWords call ( ) throws Exception {
			updateProgress(0,100);

			final byte[] bytes = readFile();

			if ( bytes!=null ) {
				final TaggedWords taggedWords = new TaggedWords();
				int offset = 0;
				int progressPrev = 0;
				int progressCurr = 0;

				while ( ( offset<bytes.length )&&( isCancelled()==false ) ) {
					offset = Parser.skipWhiteSpace(bytes,offset);
					offset = readValues(bytes,offset,taggedWords);
					progressCurr = offset*100/bytes.length;
					if ( progressCurr>progressPrev ) {
						progressPrev = progressCurr;
						updateProgress(progressCurr,100);
					}
				}
				return taggedWords;

			} else {
				return null;
			}
		}

		private byte[] readFile ( ) {
			try {
				final byte[] bytes = Files.readAllBytes(filePath);
				return bytes;

			} catch ( IOException e ) {
				e.printStackTrace();
				return null;
			}
		}

		private int readValues ( final byte[] bytes, final int offset, final TaggedWords taggedWords ) {
			final int wordLength = Parser.getLengthTillNewLine(bytes,offset);
			int currIndex = offset+wordLength;

			if ( wordLength>0 ) {
				final String wordStr = new String(bytes,offset,wordLength,App.STRING_ENCODING);
				final TaggedWords.Word word = taggedWords.add(wordStr);

				currIndex = Parser.skipWhiteSpace(bytes,currIndex);
				currIndex = readTags(bytes,currIndex,word);
			}
			return currIndex;
		}

		private int readTags ( final byte[] bytes, int offset, final TaggedWords.Word word ) {
			int currIndex;

			for ( currIndex = offset; currIndex<bytes.length; currIndex += 1 ) {
				final byte byteChar = bytes[currIndex];

				if ( Parser.isWhiteSpace(byteChar) ) {
					final int tagLength = currIndex-offset;

					if ( tagLength>0 ) {
						final String tagString = new String(bytes,offset,tagLength,App.STRING_ENCODING);
						word.addTag(tagString);
					}
					offset = currIndex+1;

					if ( Parser.isNewLine(byteChar) ) {
						return currIndex;
					}
				}
			}
			return currIndex;
		}
	}

}

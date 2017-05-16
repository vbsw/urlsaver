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


package com.github.vbsw.urlsaver.app.window.files;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.window.urls.UrlsData;
import com.github.vbsw.urlsaver.resources.Resources;
import com.github.vbsw.urlsaver.utility.Parser;

import javafx.concurrent.Task;


/**
 * @author Vitali Baumtrok
 */
final class FileLoadTask extends Task<UrlsData> {

	private final Path urlsFilePath;

	public FileLoadTask ( final Path urlsFilePath ) {
		this.urlsFilePath = urlsFilePath;
	}

	@Override
	protected UrlsData call ( ) throws Exception {
		updateProgress(0,100);

		final byte[] bytes = readFile(urlsFilePath);

		if ( bytes != null ) {
			final UrlsData urlsData = new UrlsData();

			if ( bytes.length > 0 ) {
				int offset = 0;
				int progressPrev = 0;
				int progressCurr = 0;

				while ( (offset < bytes.length) && (isCancelled() == false) ) {
					offset = Parser.skipWhiteSpace(bytes,offset);
					offset = readValues(urlsData,bytes,offset);
					progressCurr = offset * 100 / bytes.length;

					if ( progressCurr > progressPrev ) {
						progressPrev = progressCurr;

						updateProgress(progressCurr,100);
					}
				}

			} else {
				updateProgress(100,100);
			}
			return urlsData;

		} else {
			return null;
		}
	}

	private byte[] readFile ( final Path path ) {
		try {
			final byte[] bytes = Files.readAllBytes(path);
			return bytes;

		} catch ( IOException e ) {
			e.printStackTrace();
			return null;
		}
	}

	private int readValues ( final UrlsData urlsData, final byte[] bytes, final int offset ) {
		final int wordLength = Parser.getLengthTillNewLine(bytes,offset);
		int currIndex = offset + wordLength;

		if ( wordLength > 0 ) {
			final String url = new String(bytes,offset,wordLength,Resources.ENCODING);
			final int urlIndex = urlsData.addUrl(url);

			currIndex = Parser.skipWhiteSpace(bytes,currIndex);
			currIndex = readTags(urlsData,urlIndex,bytes,currIndex);
		}
		return currIndex;
	}

	private int readTags ( final UrlsData urlsData, final int urlIndex, final byte[] bytes, int offset ) {
		int currIndex;

		for ( currIndex = offset; currIndex < bytes.length; currIndex += 1 ) {
			final byte byteChar = bytes[currIndex];

			if ( Parser.isWhiteSpace(byteChar) ) {
				final int tagLength = currIndex - offset;

				if ( tagLength > 0 ) {
					final String tag = new String(bytes,offset,tagLength,Resources.ENCODING);
					urlsData.addTagToUrl(urlIndex,tag);
				}
				offset = currIndex + 1;

				if ( Parser.isNewLine(byteChar) ) {
					return currIndex;
				}
			}
		}
		return currIndex;
	}

}

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
		super.updateProgress(0,100);

		final UrlsData urlsData = getUrlsDataFromFile(urlsFilePath);

		return urlsData;
	}

	private UrlsData getUrlsDataFromFile ( final Path path ) {
		final byte[] bytes = getBytesFromFile(path);

		if ( bytes != null ) {
			final UrlsData urlsData = getUrlsDataFromBytes(bytes);

			return urlsData;

		} else {
			return null;
		}
	}

	private UrlsData getUrlsDataFromBytes ( final byte[] bytes ) {
		final UrlsData urlsData = new UrlsData();

		if ( bytes.length > 0 ) {
			int offset = 0;
			int progress = 0;

			while ( (offset < bytes.length) && (isCancelled() == false) ) {
				offset = Parser.skipWhiteSpace(bytes,offset);
				offset = parseUrlAndTags(urlsData,bytes,offset);
				progress = updateProgressIfNecessary(offset,bytes.length,progress);
			}

		} else {
			super.updateProgress(100,100);
		}
		return urlsData;
	}

	private int updateProgressIfNecessary ( final int bytesRead, final int bytesTotal, final int progress ) {
		final int progressNew = bytesRead * 100 / bytesTotal;

		if ( progressNew > progress ) {
			super.updateProgress(progressNew,100);

			return progressNew;

		} else {
			return progress;
		}
	}

	private byte[] getBytesFromFile ( final Path path ) {
		try {
			final byte[] bytes = Files.readAllBytes(path);

			return bytes;

		} catch ( IOException e ) {
			e.printStackTrace();

			return null;
		}
	}

	private int parseUrlAndTags ( final UrlsData urlsData, final byte[] bytes, final int offset ) {
		final int insertedUrlIndex = parseUrl(urlsData,bytes,offset);
		final int offsetNew = parseTags(urlsData,bytes,offset,insertedUrlIndex);

		return offsetNew;
	}

	private int parseUrl ( final UrlsData urlsData, final byte[] bytes, final int offset ) {
		final int wordLength = Parser.getLengthTillNewLine(bytes,offset);

		if ( wordLength > 0 ) {
			final String url = new String(bytes,offset,wordLength,Resources.ENCODING);
			final int urlIndex = urlsData.addUrl(url);

			return urlIndex;

		} else {
			return -1;
		}
	}

	private int parseTags ( final UrlsData urlsData, final byte[] bytes, final int offset, final int insertedUrlIndex ) {
		if ( insertedUrlIndex >= 0 ) {
			int offsetNew = offset + urlsData.urls.get(insertedUrlIndex).length();
			offsetNew = parseOneTag(urlsData,bytes,offsetNew,insertedUrlIndex);

			while ( (offsetNew < bytes.length) && (Parser.isNewLine(bytes[offsetNew]) == false) ) {
				offsetNew = parseOneTag(urlsData,bytes,offsetNew,insertedUrlIndex);
			}
			return offsetNew;

		} else {
			return bytes.length;
		}
	}

	private int parseOneTag ( final UrlsData urlsData, final byte[] bytes, final int offset, final int insertedUrlIndex ) {
		final int offsetTag = Parser.skipWhiteSpace(bytes,offset);
		final int tagLength = Parser.getLengthTillWhiteSpace(bytes,offsetTag);
		final int offsetNew;

		if ( tagLength > 0 ) {
			final String tag = new String(bytes,offsetTag,tagLength,Resources.ENCODING);

			urlsData.addTagToUrl(insertedUrlIndex,tag);
		}
		offsetNew = offsetTag + tagLength;

		return offsetNew;
	}

}

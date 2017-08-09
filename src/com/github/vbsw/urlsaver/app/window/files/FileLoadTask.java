
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


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
		final int wordLength = Parser.getLengthTillNewLine(bytes,offset);
		final int insertedUrlIndex = parseUrl(urlsData,bytes,offset,wordLength);
		final int nextLineOffset = offset + wordLength;
		final int newOffset = parseTags(urlsData,bytes,nextLineOffset,insertedUrlIndex);

		return newOffset;
	}

	private int parseUrl ( final UrlsData urlsData, final byte[] bytes, final int offset, final int urlLength ) {
		if ( urlLength > 0 ) {
			final String url = new String(bytes,offset,urlLength,Resources.ENCODING);
			final int urlIndex = urlsData.addUrl(url);

			return urlIndex;

		} else {
			return -1;
		}
	}

	private int parseTags ( final UrlsData urlsData, final byte[] bytes, final int offset, final int insertedUrlIndex ) {
		if ( insertedUrlIndex >= 0 ) {
			int newOffset = parseOneTag(urlsData,bytes,offset,insertedUrlIndex);

			while ( (newOffset < bytes.length) && (Parser.isNewLine(bytes[newOffset]) == false) ) {
				newOffset = parseOneTag(urlsData,bytes,newOffset,insertedUrlIndex);
			}
			return newOffset;

		} else {
			return bytes.length;
		}
	}

	private int parseOneTag ( final UrlsData urlsData, final byte[] bytes, final int offset, final int insertedUrlIndex ) {
		final int tagOffset = Parser.skipWhiteSpace(bytes,offset);
		final int tagLength = Parser.getLengthTillWhiteSpace(bytes,tagOffset);
		final int newOffset;

		if ( tagLength > 0 ) {
			final String tag = new String(bytes,tagOffset,tagLength,Resources.ENCODING);

			urlsData.addTagToUrl(insertedUrlIndex,tag);
		}
		newOffset = tagOffset + tagLength;

		return newOffset;
	}

}

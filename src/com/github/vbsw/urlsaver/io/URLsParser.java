/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.nio.charset.Charset;

import com.github.vbsw.urlsaver.api.URLMetaDefinition;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.utility.Parser;


/**
 * @author Vitali Baumtrok
 */
public class URLsParser {

	private final Charset inputCharset;
	private final URLMetaDefinition urlMeta;
	private final DBURLs dbURLs;
	private final byte[] bytes;

	private int offset = 0;
	private int progressCurr = 0;
	private int progressNext = 0;

	public URLsParser ( final Charset inputCharset, final URLMetaDefinition urlMeta, final DBURLs dbURLs, final byte[] bytes ) {
		this.inputCharset = inputCharset;
		this.urlMeta = urlMeta;
		this.dbURLs = dbURLs;
		this.bytes = bytes;
	}

	public boolean parseContent ( ) {
		final boolean contentParsed;
		while ( (progressCurr == progressNext || progressNext == 100) && offset < bytes.length ) {
			offset = Parser.seekContent(bytes,offset,bytes.length);
			offset = parseURLTagsAndMeta(bytes,offset);
			progressNext = (int) (offset * 100L / bytes.length);
		}
		contentParsed = progressCurr != progressNext;
		progressCurr = progressNext;
		return contentParsed;
	}

	public long getProgress ( ) {
		return progressCurr;
	}

	private int parseURLTagsAndMeta ( final byte[] bytes, final int offset ) {
		final int indexLineEnd = Parser.seekLineEnd(bytes,offset,bytes.length);
		final int indexWordEnd = Parser.seekContentReverse(bytes,indexLineEnd,offset);
		final int wordLength = indexWordEnd - offset;

		/* wordLength == 0 is when file end */
		if ( wordLength > 0 ) {
			final String url = new String(bytes,offset,wordLength,inputCharset);
			final int indexURL = dbURLs.addUrl(url);
			final int indexTagsLineBegin = Parser.seekContent(bytes,indexLineEnd,bytes.length);
			final int indexTagsLineEnd = Parser.seekLineEnd(bytes,indexTagsLineBegin,bytes.length);
			int indexTagBegin = indexTagsLineBegin;
			int indexTagEnd = Parser.seekWhitespace(bytes,indexTagBegin,indexTagsLineEnd);
			int tagLength = indexTagEnd - indexTagBegin;

			while ( tagLength > 0 ) {
				final String tag = new String(bytes,indexTagBegin,tagLength,inputCharset);
				indexTagBegin = Parser.seekContent(bytes,indexTagEnd,indexTagsLineEnd);
				indexTagEnd = Parser.seekWhitespace(bytes,indexTagBegin,indexTagsLineEnd);
				tagLength = indexTagEnd - indexTagBegin;
				dbURLs.addTagToUrl(indexURL,tag);
			}
			final int indexNext = parseURLMetaData(bytes,indexURL,indexTagsLineEnd);
			return indexNext;
		}
		return indexLineEnd;
	}

	private int parseURLMetaData ( final byte[] bytes, final int indexURL, final int offset ) {
		final int indexMetaDataLineBegin = Parser.seekContent(bytes,offset,bytes.length);

		if ( urlMeta.isMetaDataSignature(bytes,indexMetaDataLineBegin) ) {
			final int indexMetaDataLineEnd = Parser.seekLineEnd(bytes,indexMetaDataLineBegin,bytes.length);
			int indexMetaDataBegin = indexMetaDataLineBegin;
			int indexMetaDataEnd = Parser.seekWhitespace(bytes,indexMetaDataBegin,indexMetaDataLineEnd);
			int metaDataLength = indexMetaDataEnd - indexMetaDataBegin;

			while ( metaDataLength > 0 ) {
				final int indexSeparator = Parser.seekByte(bytes,indexMetaDataBegin,indexMetaDataEnd,(byte) '=');
				final int indexMetaValueBegin = indexSeparator + 1;
				final int metaKeyLength = indexSeparator - indexMetaDataBegin;
				final int metaValueLength = indexMetaDataEnd - indexMetaValueBegin;
				final String metaKey = new String(bytes,indexMetaDataBegin,metaKeyLength,inputCharset);
				final String metaValue = metaValueLength > 0 ? new String(bytes,indexMetaValueBegin,metaValueLength,inputCharset) : "";
				urlMeta.setMeta(metaKey,metaValue);
				addURLMetaValue(indexURL);

				indexMetaDataBegin = Parser.seekContent(bytes,indexMetaDataEnd,indexMetaDataLineEnd);
				indexMetaDataEnd = Parser.seekWhitespace(bytes,indexMetaDataBegin,indexMetaDataLineEnd);
				metaDataLength = indexMetaDataEnd - indexMetaDataBegin;
			}
			return indexMetaDataLineEnd;
		}
		return indexMetaDataLineBegin;
	}

	private void addURLMetaValue ( final int indexURL ) {
		final String metaValue = urlMeta.metaValue;
		final int metaKeyID = urlMeta.metaKeyID;
		if ( !metaValue.isEmpty() ) {
			switch ( metaKeyID ) {
				case URLMetaDefinition.DATE:
				case URLMetaDefinition.SCORE:
				dbURLs.setMetaData(indexURL,metaKeyID,metaValue);
				break;
				case URLMetaDefinition.UNKNOWN:
				System.out.println("unsuported key \"" + urlMeta.metaKey + "\" in \"" + dbURLs.getFileName() + "\"");
				break;
			}
		} else {
			switch ( metaKeyID ) {
				case URLMetaDefinition.DATE:
				case URLMetaDefinition.SCORE:
				System.out.println("key \"" + urlMeta.metaKey + "\" has no value in " + dbURLs.getFileName());
				break;

				case URLMetaDefinition.UNKNOWN:
				System.out.println("unsuported key \"" + urlMeta.metaKey + "\" in \"" + dbURLs.getFileName() + "\"");
			}
		}
	}

}

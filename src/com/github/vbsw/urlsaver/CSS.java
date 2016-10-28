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


import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @author Vitali Baumtrok
 */
public class CSS {

	public static final String EXTERNAL_CSS_FILE = "urlsaver.css"; //$NON-NLS-1$
	public static final String INTERNAL_CSS_FILE = "com/github/vbsw/urlsaver/urlsaver.css"; //$NON-NLS-1$

	public static String getURL ( ) {
		final String externalStylesheetURL = getExternalStylesheetURL();

		if ( externalStylesheetURL!=null ) {
			return externalStylesheetURL;

		} else {
			final String internalStylesheetURL = getInternalStylesheetURL();
			return internalStylesheetURL;
		}
	}

	private static String getExternalStylesheetURL ( ) {
		final Path externalCSSPath = Paths.get(JarPath.get().toString(),EXTERNAL_CSS_FILE);

		if ( Files.exists(externalCSSPath) ) {
			try {
				final ClassLoader classLoader = CSS.class.getClassLoader();
				final URL url = classLoader.getResource(EXTERNAL_CSS_FILE);
				final String urlStr = url.toExternalForm();
				return urlStr;

			} catch ( final Exception e ) {
			}
		}
		return null;
	}

	private static String getInternalStylesheetURL ( ) {
		try {
			final ClassLoader classLoader = CSS.class.getClassLoader();
			final URL url = classLoader.getResource(INTERNAL_CSS_FILE);
			final String urlStr = url.toExternalForm();
			return urlStr;

		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
}

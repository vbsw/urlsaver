/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2016, 2017 Vitali Baumtrok
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


package com.github.vbsw.urlsaver.app.window.scene;


import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.vbsw.urlsaver.resources.Resources;
import com.github.vbsw.urlsaver.utility.Jar;


/**
 * @author Vitali Baumtrok
 */
public final class CSS {

	private final String urlString;
	private final boolean custom;

	CSS ( ) {
		final String customStylesheetURL = getCustomStylesheetURL();

		if ( customStylesheetURL != null ) {
			urlString = customStylesheetURL;
			custom = true;

		} else {
			final String defaultStylesheetURL = getDefaultStylesheetURL();

			urlString = defaultStylesheetURL;
			custom = false;
		}
	}

	public boolean isCustom ( ) {
		return custom;
	}

	public String getUrlString ( ) {
		return urlString;
	}

	private String getCustomStylesheetURL ( ) {
		final Path externalCSSPath = Paths.get(Jar.getPathToJar().toString(),Resources.CUSTOM_CSS_FILE_PATH);

		if ( Files.exists(externalCSSPath) ) {
			try {
				final ClassLoader classLoader = CSS.class.getClassLoader();
				final URL url = classLoader.getResource(Resources.CUSTOM_CSS_FILE_PATH);
				final String urlStr = url.toExternalForm();
				return urlStr;

			} catch ( final Exception e ) {
			}
		}
		return null;
	}

	private String getDefaultStylesheetURL ( ) {
		try {
			final ClassLoader classLoader = CSS.class.getClassLoader();
			final URL url = classLoader.getResource(Resources.DEFAULT_CSS_FILE_PATH);
			final String urlStr = url.toExternalForm();
			return urlStr;

		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}
}

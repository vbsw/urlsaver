
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


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
		final Path externalCSSPath = Paths.get(Jar.getPath().toString(),Resources.CUSTOM_CSS_FILE_PATH);

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

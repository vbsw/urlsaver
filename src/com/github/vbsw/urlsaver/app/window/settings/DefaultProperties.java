
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.settings;


import java.io.InputStream;
import java.util.Properties;

import com.github.vbsw.urlsaver.resources.Resources;
import com.github.vbsw.urlsaver.utility.Jar;
import com.github.vbsw.urlsaver.utility.Parser;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
class DefaultProperties extends Properties {

	DefaultProperties ( ) {
		final String fileName = Resources.DEFAULT_PROPERTIES_FILE_PATH;

		try ( final InputStream stream = Jar.getResourceAsStream(fileName) ) {
			load(stream);

		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

	public String getWindowTitle ( ) {
		final String key = "window.title"; //$NON-NLS-1$
		return getProperty(key);
	}

	public double getWindowWidth ( ) {
		final String key = "window.width"; //$NON-NLS-1$
		final String windowWidthStr = getProperty(key);

		if ( windowWidthStr != null ) {
			try {
				final int intValue = Integer.parseInt(windowWidthStr);

				return (double) intValue;

			} catch ( final NumberFormatException e ) {
			}
		}
		return 0.0d;
	}

	public double getWindowHeight ( ) {
		final String key = "window.height"; //$NON-NLS-1$
		final String windowHeightStr = getProperty(key);

		if ( windowHeightStr != null ) {
			try {
				final int intValue = Integer.parseInt(windowHeightStr);

				return (double) intValue;

			} catch ( final NumberFormatException e ) {
			}
		}
		return 0.0d;
	}

	public int getWindowMaximized ( ) {
		final String key = "window.maximized"; //$NON-NLS-1$
		final String windowMaximizedStr = getProperty(key);

		if ( windowMaximizedStr != null ) {
			return Parser.isTrue(windowMaximizedStr) ? 1 : 0;
		}
		return -1;
	}

	public int getAutoloadAll ( ) {
		final String key = "autoload.all"; //$NON-NLS-1$
		final String autoloadAllStr = getProperty(key);

		if ( autoloadAllStr != null ) {
			return Parser.isTrue(autoloadAllStr) ? 1 : 0;
		}
		return -1;
	}

	public String getFileExtension ( ) {
		final String key = "urls.file.extension"; //$NON-NLS-1$
		return getProperty(key);
	}

	public String getFileSelect ( ) {
		final String key = "urls.file.select"; //$NON-NLS-1$
		return getProperty(key);
	}

	public int getSearchByPrefix ( ) {
		final String key = "search.by.prefix"; //$NON-NLS-1$
		final String autoloadAllStr = getProperty(key);

		if ( autoloadAllStr != null ) {
			return Parser.isTrue(autoloadAllStr) ? 1 : 0;
		}
		return -1;
	}

}

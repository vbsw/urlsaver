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


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
public class Properties extends java.util.Properties {

	private static final String EXTERNAL_FILE = "urlsaver.properties"; //$NON-NLS-1$
	private static final String INTERNAL_PATH = "com/github/vbsw/urlsaver/default.properties"; //$NON-NLS-1$

	private static final String WINDOW_TITLE = "window.title"; //$NON-NLS-1$
	private static final String WINDOW_WIDTH = "window.width"; //$NON-NLS-1$
	private static final String WINDOW_HEIGHT = "window.height"; //$NON-NLS-1$
	private static final String WINDOW_MAXIMIZED = "window.maximized"; //$NON-NLS-1$
	private static final String DATA_AUTO_LOAD = "file.auto.load"; //$NON-NLS-1$
	private static final String DATA_FILE_EXTENSION = "file.extension"; //$NON-NLS-1$

	private final java.util.Properties properties;
	private final boolean external;

	public Properties ( ) {
		java.util.Properties properties = getExternalProperties();

		if ( properties==null ) {
			external = false;
			properties = getInternal();

		} else {
			external = true;
		}

		this.properties = properties;
	}

	public boolean isExternal ( ) {
		return external;
	}

	public String getWindowTitle ( final String defaultTitle ) {
		return properties.getProperty(WINDOW_TITLE,defaultTitle);
	}

	public double getWindowWidth ( final double defaultWidth ) {
		try {
			final String windowWidthStr = properties.getProperty(WINDOW_WIDTH);
			final int windowWidth = Integer.parseInt(windowWidthStr);
			return (double) windowWidth;

		} catch ( final Exception e ) {
			return defaultWidth;
		}
	}

	public double getWindowHeight ( final double defaultHeight ) {
		try {
			final String windowHeightStr = properties.getProperty(WINDOW_HEIGHT);
			final int windowHeight = Integer.parseInt(windowHeightStr);
			return (double) windowHeight;

		} catch ( final Exception e ) {
			return defaultHeight;
		}
	}

	public boolean isWindowMaximized ( final boolean defaultMaximized ) {
		try {
			final String windowMaximizedStr = properties.getProperty(WINDOW_MAXIMIZED);
			final boolean windowMaximized = isTrue(windowMaximizedStr);
			return windowMaximized;

		} catch ( final Exception e ) {
			return defaultMaximized;
		}
	}

	/**
	 * Returns true, if the property for data auto load has been set.
	 * 
	 * @param defaultAutoload
	 * @return True, if data.autoload is set to 1, y, yes or true.
	 */
	public boolean isDataAutoLoad ( final boolean defaultAutoload ) {
		try {
			final String autoloadStr = properties.getProperty(DATA_AUTO_LOAD);
			final boolean autoload = isTrue(autoloadStr);
			return autoload;

		} catch ( final Exception e ) {
			return defaultAutoload;
		}
	}

	public String getDataFileExtension ( final String defaultDataFileExtension ) {
		final String dataFileExtension = properties.getProperty(DATA_FILE_EXTENSION,defaultDataFileExtension);
		return dataFileExtension;
	}

	private static java.util.Properties getExternalProperties ( ) {
		final Path filePath = Paths.get(JarPath.get().toString(),EXTERNAL_FILE);

		if ( Files.exists(filePath) ) {

			try ( final InputStream stream = Files.newInputStream(filePath) ) {
				final java.util.Properties properties = new java.util.Properties();
				properties.load(stream);
				return properties;

			} catch ( final Exception e ) {
			}
		}

		return null;
	}

	private static java.util.Properties getInternal ( ) {
		try ( InputStream stream = JarPath.getStream(INTERNAL_PATH) ) {
			final java.util.Properties properties = new java.util.Properties();
			properties.load(stream);
			return properties;

		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean isTrue ( final String boolStr ) {
		final String trueStr = "true"; //$NON-NLS-1$
		final String yesStr = "yes"; //$NON-NLS-1$
		final String yStr = "y"; //$NON-NLS-1$
		final String oneStr = "1"; //$NON-NLS-1$

		return boolStr.equals(trueStr)||boolStr.equals(yesStr)||boolStr.equals(yStr)||boolStr.equals(oneStr);
	}

}

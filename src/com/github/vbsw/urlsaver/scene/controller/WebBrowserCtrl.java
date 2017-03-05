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


package com.github.vbsw.urlsaver.scene.controller;


import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import com.github.vbsw.urlsaver.Convert;
import com.github.vbsw.urlsaver.app.App;


/**
 * @author Vitali Baumtrok
 */
public final class WebBrowserCtrl {

	public static void openSelectedUrl ( ) {
		final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();

		if ( selectedUrl != null ) {
			WebBrowserCtrl.openURL(selectedUrl);
		}
	}

	public static void openURL ( final String url ) {
		final String os = System.getProperty("os.name").toLowerCase();

		if ( os.indexOf("win") >= 0 ) {
			openURLWindows(url);

		} else if ( os.indexOf("mac") >= 0 ) {
			openURLMac(url);

		} else if ( os.indexOf("nux") >= 0 || os.indexOf("nix") >= 0 ) {
			openURLUnix(url);
		}
	}

	private static void openURLUnix ( final String url ) {
		final Runtime runtime = Runtime.getRuntime();

		try {
			runtime.exec("xdg-open " + url);

		} catch ( IOException e ) {
		}
	}

	private static void openURLMac ( final String url ) {
		final Runtime rt = Runtime.getRuntime();

		try {
			rt.exec("open " + url);

		} catch ( IOException e ) {
		}
	}

	private static void openURLWindows ( final String url ) {
		if ( Desktop.isDesktopSupported() ) {
			final Desktop desktop = Desktop.getDesktop();

			if ( desktop.isSupported(Desktop.Action.BROWSE) ) {
				final URI uri = Convert.toURI(url);

				if ( uri != null ) {
					try {
						desktop.browse(uri);

					} catch ( final Exception e ) {
						openURLWindowsRT(url);
					}
				}

			} else {
				openURLWindowsRT(url);
			}

		} else {
			openURLWindowsRT(url);
		}
	}

	private static void openURLWindowsRT ( final String url ) {
		final Runtime rt = Runtime.getRuntime();

		try {
			rt.exec("rundll32 url.dll,FileProtocolHandler " + url);

		} catch ( IOException e ) {
		}
	}

}

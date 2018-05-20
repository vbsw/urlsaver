/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import com.github.vbsw.urlsaver.gui.TextFields;


/**
 * @author Vitali Baumtrok
 */
public class WebBrowserAccess {

	public static void openTypedUrl ( ) {
		final String urlTyped = Parser.trim(TextFields.url.control.getText());
		WebBrowserAccess.openURL(urlTyped);
	}

	public static void openURL ( final String url ) {
		final String os = System.getProperty("os.name").toLowerCase();
		if ( os.indexOf("win") >= 0 )
			openURLWindows(url);
		else if ( os.indexOf("mac") >= 0 )
			openURLMac(url);
		else if ( os.indexOf("nux") >= 0 || os.indexOf("nix") >= 0 )
			openURLUnix(url);
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
				final URI uri = WebBrowserAccess.toURI(url);
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

	private static URI toURI ( final String url ) {
		final URL urlURL = toURL(url);
		if ( urlURL != null )
			try {
				return urlURL.toURI();
			} catch ( final Exception e ) {
			}
		return null;
	}

	private static URL toURL ( final String url ) {
		if ( url != null && url.length() > 0 ) {
			try {
				return new URL(url);
			} catch ( final MalformedURLException e ) {
			}
			try {
				final String httpURLStr = "http://" + url;
				return new URL(httpURLStr);
			} catch ( final MalformedURLException e ) {
			}
		}
		return null;
	}

}

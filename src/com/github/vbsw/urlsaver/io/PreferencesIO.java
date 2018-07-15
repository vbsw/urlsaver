/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.api.Logger;
import com.github.vbsw.urlsaver.api.Preferences;
import com.github.vbsw.urlsaver.api.Resource;


/**
 * @author Vitali Baumtrok
 */
public class PreferencesIO {

	protected Preferences preferences;
	protected Logger logger;

	public void initialize ( final Preferences preferences, final Logger logger ) {
		this.preferences = preferences;
		this.logger = logger;
	}

	public void createPreferencesFile ( ) {
		final Resource srcResource = preferences.getPreferences().getDefault();
		final Resource destResource = preferences.getPreferences().getSaved();
		createFile(srcResource,destResource);
	}

	public void createCSSFile ( ) {
		final Resource srcResource = preferences.getCSS().getDefault();
		final Resource destResource = preferences.getCSS().getSaved();
		createFile(srcResource,destResource);
	}

	public void createFXMLFile ( ) {
		final Resource srcResource = preferences.getFXML().getDefault();
		final Resource destResource = preferences.getFXML().getSaved();
		createFile(srcResource,destResource);
	}

	public void overwritePreferencesFile ( ) {
		final Resource srcResource = preferences.getPreferences().getDefault();
		final Resource destResource = preferences.getPreferences().getSaved();
		overwriteFile(srcResource,destResource);
	}

	public void overwriteCSSFile ( ) {
		final Resource srcResource = preferences.getCSS().getDefault();
		final Resource destResource = preferences.getCSS().getSaved();
		overwriteFile(srcResource,destResource);
	}

	public void overwriteFXMLFile ( ) {
		final Resource srcResource = preferences.getFXML().getDefault();
		final Resource destResource = preferences.getFXML().getSaved();
		overwriteFile(srcResource,destResource);
	}

	protected void createFile ( final Resource srcResource, final Resource destResource ) {
		final Path destPath = destResource.getPath();
		srcResource.copyTo(destPath);
		if ( destResource.exists() )
			logger.logSuccess("file created (" + destPath.getFileName() + ")");
		else
			logger.logFailure("file not created (" + destPath.getFileName() + ")");
	}

	protected void overwriteFile ( final Resource srcResource, final Resource destResource ) {
		final Path destPath = destResource.getPath();
		srcResource.copyTo(destPath);
		logger.logSuccess("file overwritten (" + destPath.getFileName() + ")");
	}

}

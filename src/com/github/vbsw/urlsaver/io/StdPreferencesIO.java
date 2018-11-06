/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.PreferencesIO;
import com.github.vbsw.urlsaver.api.Resource;


/**
 * @author Vitali Baumtrok
 */
public class StdPreferencesIO extends PreferencesIO {

	@Override
	public void createPreferencesFile ( ) {
		final Resource srcResource = Global.preferences.getPreferences().getDefault();
		final Resource destResource = Global.preferences.getPreferences().getSaved();
		createFile(srcResource,destResource,"preferences");
	}

	@Override
	public void createCSSFile ( ) {
		final Resource srcResource = Global.preferences.getCSS().getDefault();
		final Resource destResource = Global.preferences.getCSS().getSaved();
		createFile(srcResource,destResource,"CSS");
	}

	@Override
	public void createFXMLFile ( ) {
		final Resource srcResource = Global.preferences.getFXML().getDefault();
		final Resource destResource = Global.preferences.getFXML().getSaved();
		createFile(srcResource,destResource,"FXML");
	}

	@Override
	public void overwritePreferencesFile ( ) {
		final Resource srcResource = Global.preferences.getPreferences().getDefault();
		final Resource destResource = Global.preferences.getPreferences().getSaved();
		overwriteFile(srcResource,destResource,"preferences");
	}

	@Override
	public void overwriteCSSFile ( ) {
		final Resource srcResource = Global.preferences.getCSS().getDefault();
		final Resource destResource = Global.preferences.getCSS().getSaved();
		overwriteFile(srcResource,destResource,"CSS");
	}

	@Override
	public void overwriteFXMLFile ( ) {
		final Resource srcResource = Global.preferences.getFXML().getDefault();
		final Resource destResource = Global.preferences.getFXML().getSaved();
		overwriteFile(srcResource,destResource,"FXML");
	}

	protected void createFile ( final Resource srcResource, final Resource destResource, final String resourceType ) {
		final Path destPath = destResource.getPath();
		srcResource.copyTo(destPath);
		if ( destResource.exists() )
			Global.logger.logSuccess(resourceType + " created (" + destPath.getFileName() + ")");
		else
			Global.logger.logFailure(resourceType + " not created (" + destPath.getFileName() + ")");
	}

	protected void overwriteFile ( final Resource srcResource, final Resource destResource, final String resourceType ) {
		final Path destPath = destResource.getPath();
		srcResource.copyTo(destPath);
		Global.logger.logSuccess(resourceType + " saved (" + destPath.getFileName() + ")");
	}

}

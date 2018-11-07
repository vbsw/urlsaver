/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.SettingsIO;
import com.github.vbsw.urlsaver.api.Resource;


/**
 * @author Vitali Baumtrok
 */
public class StdSettingsIO extends SettingsIO {

	@Override
	public void createSettingsFile ( ) {
		final Resource srcResource = Global.settings.getSettings().getDefault();
		final Resource destResource = Global.settings.getSettings().getSaved();
		createFile(srcResource,destResource,"settings");
	}

	@Override
	public void createCSSFile ( ) {
		final Resource srcResource = Global.settings.getCSS().getDefault();
		final Resource destResource = Global.settings.getCSS().getSaved();
		createFile(srcResource,destResource,"CSS");
	}

	@Override
	public void createFXMLFile ( ) {
		final Resource srcResource = Global.settings.getFXML().getDefault();
		final Resource destResource = Global.settings.getFXML().getSaved();
		createFile(srcResource,destResource,"FXML");
	}

	@Override
	public void overwriteSettingsFile ( ) {
		final Resource srcResource = Global.settings.getSettings().getDefault();
		final Resource destResource = Global.settings.getSettings().getSaved();
		overwriteFile(srcResource,destResource,"settings");
	}

	@Override
	public void overwriteCSSFile ( ) {
		final Resource srcResource = Global.settings.getCSS().getDefault();
		final Resource destResource = Global.settings.getCSS().getSaved();
		overwriteFile(srcResource,destResource,"CSS");
	}

	@Override
	public void overwriteFXMLFile ( ) {
		final Resource srcResource = Global.settings.getFXML().getDefault();
		final Resource destResource = Global.settings.getFXML().getSaved();
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

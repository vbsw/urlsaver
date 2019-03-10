/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.IResource;
import com.github.vbsw.urlsaver.api.ISettingsIO;


/**
 * @author Vitali Baumtrok
 */
public class SettingsIO implements ISettingsIO {

	@Override
	public void createSettingsFile ( ) {
		final IResource srcResource = Global.settings.getDefaultSettingsResource();
		final IResource destResource = Global.settings.getSettingsResource();
		createFile(srcResource,destResource,"settings");
	}

	@Override
	public void createCSSFile ( ) {
		final IResource srcResource = Global.settings.getDefaultCSSResource();
		final IResource destResource = Global.settings.getCSSResource();
		createFile(srcResource,destResource,"CSS");
	}

	@Override
	public void createFXMLFile ( ) {
		final IResource srcResource = Global.settings.getDefaultFXMLResource();
		final IResource destResource = Global.settings.getFXMLResource();
		createFile(srcResource,destResource,"FXML");
	}

	@Override
	public void overwriteSettingsFile ( ) {
		final IResource srcResource = Global.settings.getDefaultSettingsResource();
		final IResource destResource = Global.settings.getSettingsResource();
		overwriteFile(srcResource,destResource,"settings");
	}

	@Override
	public void overwriteCSSFile ( ) {
		final IResource srcResource = Global.settings.getDefaultCSSResource();
		final IResource destResource = Global.settings.getCSSResource();
		overwriteFile(srcResource,destResource,"CSS");
	}

	@Override
	public void overwriteFXMLFile ( ) {
		final IResource srcResource = Global.settings.getDefaultFXMLResource();
		final IResource destResource = Global.settings.getFXMLResource();
		overwriteFile(srcResource,destResource,"FXML");
	}

	protected void createFile ( final IResource srcResource, final IResource destResource, final String resourceType ) {
		final Path destPath = destResource.getPath();
		srcResource.copyTo(destPath);
		if ( destResource.exists() )
			Global.logger.logSuccess(resourceType + " created (" + destPath.getFileName() + ")");
		else
			Global.logger.logFailure(resourceType + " not created (" + destPath.getFileName() + ")");
	}

	protected void overwriteFile ( final IResource srcResource, final IResource destResource, final String resourceType ) {
		final Path destPath = destResource.getPath();
		srcResource.copyTo(destPath);
		Global.logger.logSuccess(resourceType + " saved (" + destPath.getFileName() + ")");
	}

}

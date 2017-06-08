
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.scene;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.vbsw.urlsaver.resources.Resources;
import com.github.vbsw.urlsaver.utility.Jar;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 * @author Vitali Baumtrok
 */
public final class FXML {

	final boolean customLoaded;
	final Parent root;

	FXML ( ) {
		final Parent root = FXML.loadCustomFXML();

		if ( root == null ) {
			this.customLoaded = false;
			this.root = FXML.loadDefaultFXML();

		} else {
			this.customLoaded = true;
			this.root = root;
		}
	}

	public boolean isCustomLoaded ( ) {
		return customLoaded;
	}

	private static Parent loadCustomFXML ( ) {
		final Path externalFXMLPath = Paths.get(Jar.getPathToJar().toString(),Resources.CUSTOM_FXML_FILE_PATH);

		if ( Files.exists(externalFXMLPath) ) {

			try ( final InputStream stream = Files.newInputStream(externalFXMLPath) ) {
				final FXMLLoader fxmlLoader = new FXMLLoader();
				final Parent fxml = fxmlLoader.load(stream);
				return fxml;

			} catch ( final Exception e ) {
			}
		}

		return null;
	}

	private static Parent loadDefaultFXML ( ) {
		try ( InputStream stream = Jar.getResourceAsStream(Resources.DEFAULT_FXML_FILE_PATH) ) {
			final FXMLLoader fxmlLoader = new FXMLLoader();
			final Parent fxml = fxmlLoader.load(stream);
			return fxml;

		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

}

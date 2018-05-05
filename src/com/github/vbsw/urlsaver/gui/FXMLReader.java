/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.vbsw.urlsaver.JarFile;
import com.github.vbsw.urlsaver.resources.ResourcesConfig;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 * @author Vitali Baumtrok
 */
public class FXMLReader {

	public static boolean customFXMLRead;

	public static Parent getRoot ( ) {
		Parent root = FXMLReader.getCustomRoot();
		if ( root == null ) {
			root = FXMLReader.getDefaultRoot();
			customFXMLRead = false;
		} else {
			customFXMLRead = true;
		}
		return root;
	}

	private static Parent getCustomRoot ( ) {
		final Path externalFXMLPath = Paths.get(JarFile.getPath().toString(),ResourcesConfig.CUSTOM_FXML_FILE_PATH);
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

	private static Parent getDefaultRoot ( ) {
		try ( InputStream stream = JarFile.getResourceAsStream(ResourcesConfig.DEFAULT_FXML_FILE_PATH) ) {
			final FXMLLoader fxmlLoader = new FXMLLoader();
			final Parent fxml = fxmlLoader.load(stream);
			return fxml;
		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

}

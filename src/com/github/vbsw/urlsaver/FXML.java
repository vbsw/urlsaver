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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 * @author Vitali Baumtrok
 */
public class FXML {

	public static final String EXTERNAL_FXML_FILE = "urlsaver.fxml"; //$NON-NLS-1$
	public static final String INTERNAL_FXML_FILE = "com/github/vbsw/urlsaver/urlsaver.fxml"; //$NON-NLS-1$

	final Parent root;
	final boolean external;

	public FXML ( ) {
		final Parent root = FXML.loadExternalFXML();

		if ( root==null ) {
			this.external = false;
			this.root = FXML.loadInternalFXML();

		} else {
			this.external = true;
			this.root = root;
		}
	}

	public Parent getRoot ( ) {
		return root;
	}

	public boolean isExternal ( ) {
		return external;
	}

	private static Parent loadExternalFXML ( ) {
		final Path externalFXMLPath = Paths.get(JarPath.get().toString(),EXTERNAL_FXML_FILE);

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

	private static Parent loadInternalFXML ( ) {
		try ( InputStream stream = JarPath.getStream(INTERNAL_FXML_FILE) ) {
			final FXMLLoader fxmlLoader = new FXMLLoader();
			final Parent fxml = fxmlLoader.load(stream);
			return fxml;

		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

}

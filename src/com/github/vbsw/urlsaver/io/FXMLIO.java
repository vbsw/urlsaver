/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.io.InputStream;

import com.github.vbsw.urlsaver.pref.Preferences;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 * @author Vitali Baumtrok
 */
public class FXMLIO {

	public static Parent readFXML ( ) {
		Parent root = FXMLIO.readCustomFXML();
		if ( root == null ) {
			root = FXMLIO.readDefaultFXML();
			Preferences.setCustomFXMLLoaded(false);
		} else {
			Preferences.setCustomFXMLLoaded(true);
		}
		return root;
	}

	private static Parent readCustomFXML ( ) {
		if ( Preferences.isCustomFXMLFileAvailable() ) {
			try ( final InputStream stream = Preferences.getFXMLResource().getSavedValue().newInputStream() ) {
				final FXMLLoader fxmlLoader = new FXMLLoader();
				final Parent fxml = fxmlLoader.load(stream);
				return fxml;
			} catch ( final Exception e ) {
			}
		}
		return null;
	}

	private static Parent readDefaultFXML ( ) {
		try ( final InputStream stream = Preferences.getFXMLResource().getDefaultValue().newInputStream() ) {
			final FXMLLoader fxmlLoader = new FXMLLoader();
			final Parent fxml = fxmlLoader.load(stream);
			return fxml;
		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

}

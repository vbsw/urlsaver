/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.io.InputStream;

import com.github.vbsw.urlsaver.api.Preferences;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 * @author Vitali Baumtrok
 */
public class FXMLIO {

	protected Preferences preferences;

	public void initialize ( final Preferences preferences ) {
		this.preferences = preferences;
	}

	public Parent readFXML ( ) {
		Parent root = readCustomFXML();
		if ( root == null ) {
			root = readDefaultFXML();
			preferences.setCustomFXMLLoaded(false);
		} else {
			preferences.setCustomFXMLLoaded(true);
		}
		return root;
	}

	protected Parent readCustomFXML ( ) {
		if ( preferences.getFXML().getSaved().exists() ) {
			try ( final InputStream stream = preferences.getFXML().getSaved().newInputStream() ) {
				final FXMLLoader fxmlLoader = new FXMLLoader();
				final Parent fxml = fxmlLoader.load(stream);
				return fxml;
			} catch ( final Exception e ) {
			}
		}
		return null;
	}

	protected Parent readDefaultFXML ( ) {
		try ( final InputStream stream = preferences.getFXML().getDefault().newInputStream() ) {
			final FXMLLoader fxmlLoader = new FXMLLoader();
			final Parent fxml = fxmlLoader.load(stream);
			return fxml;
		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

}

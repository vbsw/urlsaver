/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.io.InputStream;

import com.github.vbsw.urlsaver.api.FXMLIO;
import com.github.vbsw.urlsaver.api.Global;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


/**
 * @author Vitali Baumtrok
 */
public class StdFXMLIO extends FXMLIO {

	@Override
	public Parent readFXML ( ) {
		Parent root = readCustomFXML();
		if ( root == null ) {
			root = readDefaultFXML();
			Global.settings.setCustomFXMLLoaded(false);
		} else {
			Global.settings.setCustomFXMLLoaded(true);
		}
		return root;
	}

	protected Parent readCustomFXML ( ) {
		if ( Global.settings.getFXML().getSaved().exists() ) {
			try ( final InputStream stream = Global.settings.getFXML().getSaved().newInputStream() ) {
				final FXMLLoader fxmlLoader = new FXMLLoader();
				final Parent fxml = fxmlLoader.load(stream);
				return fxml;
			} catch ( final Exception e ) {
			}
		}
		return null;
	}

	protected Parent readDefaultFXML ( ) {
		try ( final InputStream stream = Global.settings.getFXML().getDefault().newInputStream() ) {
			final FXMLLoader fxmlLoader = new FXMLLoader();
			final Parent fxml = fxmlLoader.load(stream);
			return fxml;
		} catch ( final Exception e ) {
			e.printStackTrace();
			return null;
		}
	}

}

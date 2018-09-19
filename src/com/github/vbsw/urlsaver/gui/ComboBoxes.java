/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import javafx.scene.Parent;
import javafx.scene.control.ComboBox;


/**
 * @author Vitali Baumtrok
 */
public class ComboBoxes {

	protected StdGUI stdGUI;

	public final Score score = new Score();

	public ComboBoxes ( final StdGUI stdGUI ) {
		this.stdGUI = stdGUI;
	}

	public class CustomComboBox {
		public ComboBox<String> control;
	}

	public void build ( Parent root ) {
		score.build(root);
	}

	public final class Score extends CustomComboBox {

		@SuppressWarnings ( "unchecked" )
		private void build ( final Parent root ) {
			control = (ComboBox<String>) root.lookup("#score_cb");
		}

	}

}

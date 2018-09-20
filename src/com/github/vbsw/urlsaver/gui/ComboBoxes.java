/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
			final ObservableList<String> items = FXCollections.observableArrayList("no score","(10) Materpiece","(9) Great","(8) Very Good","(7) Good","(6) Fine","(5) Avarage","(4) Bad","(3) Very Bad","(2) Horrible","(1) Appaling");
			control = (ComboBox<String>) root.lookup("#score_cb");
			control.getItems().addAll(items);
		}

		public void selectScore ( final String score ) {
			if ( score != null && !score.equals("") ) {
				if ( score.equals("10") )
					control.getSelectionModel().select(1);
				else if ( score.equals("9") )
					control.getSelectionModel().select(2);
				else if ( score.equals("8") )
					control.getSelectionModel().select(3);
				else if ( score.equals("7") )
					control.getSelectionModel().select(4);
				else if ( score.equals("6") )
					control.getSelectionModel().select(5);
				else if ( score.equals("5") )
					control.getSelectionModel().select(6);
				else if ( score.equals("4") )
					control.getSelectionModel().select(7);
				else if ( score.equals("3") )
					control.getSelectionModel().select(8);
				else if ( score.equals("2") )
					control.getSelectionModel().select(9);
				else if ( score.equals("1") )
					control.getSelectionModel().select(10);
				else
					control.getSelectionModel().select(0);
			} else {
				control.getSelectionModel().select(0);
			}
		}

	}

}

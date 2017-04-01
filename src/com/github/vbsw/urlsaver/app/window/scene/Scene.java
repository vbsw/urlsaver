/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
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


package com.github.vbsw.urlsaver.app.window.scene;


import com.github.vbsw.urlsaver.app.App;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import javafx.stage.WindowEvent;


/**
 * @author Vitali Baumtrok
 */
public final class Scene extends javafx.scene.Scene {

	public FXML fxml;
	public CSS css;
	public Buttons btn;
	public ListViews lv;
	public TabPanes tp;
	public TopTabs topTab;
	public TextAreas ta;
	public TextFields tf;

	public double decorationWidth;
	public double decorationHeight;

	public Scene ( ) {
		super(new AnchorPane(),App.settings.getWindowWidth(),App.settings.getWindowHeight());
	}

	public void loadFXML ( ) {
		fxml = new FXML();
		btn = new Buttons(fxml.root);
		lv = new ListViews(fxml.root);
		tp = new TabPanes(fxml.root);
		topTab = new TopTabs(fxml.root,tp);
		ta = new TextAreas(fxml.root);
		tf = new TextFields(fxml.root);

		setRoot(fxml.root);
		btn.configure();
		lv.configure();
		tp.configure();
		topTab.configure();
		ta.configure();
		tf.configure();
	}

	public void loadCSS ( ) {
		css = new CSS();

		getStylesheets().clear();
		getStylesheets().add(css.getUrlString());
	}

	public void setDecorationSize ( final double windowWidth, final double windowHeight ) {
		decorationWidth = windowWidth - getWidth();
		decorationHeight = windowHeight - getHeight();
	}

	public void fireCloseEvent ( ) {
		final Window window = super.getWindow();
		final WindowEvent closeEvent = new WindowEvent(window,WindowEvent.WINDOW_CLOSE_REQUEST);

		window.fireEvent(closeEvent);
	}

}

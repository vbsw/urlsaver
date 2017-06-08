
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


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

	public void fireCloseEvent ( ) {
		final Window window = super.getWindow();
		final WindowEvent closeEvent = new WindowEvent(window,WindowEvent.WINDOW_CLOSE_REQUEST);

		window.fireEvent(closeEvent);
	}

}

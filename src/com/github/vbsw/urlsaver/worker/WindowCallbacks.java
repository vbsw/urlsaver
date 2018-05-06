/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.worker;


import com.github.vbsw.urlsaver.App;

import javafx.stage.WindowEvent;


/**
 * @author Vitali Baumtrok
 */
public class WindowCallbacks {

	public static void onCloseRequest ( final WindowEvent event ) {
		event.consume();
		App.quit();
	}

}

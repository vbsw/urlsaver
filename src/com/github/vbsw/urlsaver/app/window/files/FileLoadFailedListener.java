
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.files;


import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
final class FileLoadFailedListener implements EventHandler<WorkerStateEvent> {

	@Override
	public void handle ( final WorkerStateEvent event ) {
		event.getSource().getException().printStackTrace();
	}

}

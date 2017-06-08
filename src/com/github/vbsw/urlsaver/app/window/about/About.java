
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.about;


/**
 * @author Vitali Baumtrok
 */
public class About {

	public final AboutModelView mv = new AboutModelView();

	public void setConfirmQuitAppProperty ( final boolean enable ) {
		mv.confirmQuitApp.set(enable);
	}

}

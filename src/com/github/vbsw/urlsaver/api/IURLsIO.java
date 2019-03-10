/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


/**
 * @author Vitali Baumtrok
 */
public interface IURLsIO {

	public void loadDefault ( );

	public void recreateServices ( );

	public void reloadAllFiles ( );

	public void reloadSelectedFile ( );

	public void saveAllFiles ( );

	public void saveSelectedFile ( );

}

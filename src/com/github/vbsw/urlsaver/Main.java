/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import java.net.URISyntaxException;

import com.github.vbsw.urlsaver.args.ArgumentsCheck;
import com.github.vbsw.urlsaver.args.CommandLineInfo;

import javafx.application.Application;


/**
 * @author Vitali Baumtrok
 */
public class Main {

	public static void main ( final String[] args ) throws URISyntaxException {
		if ( ArgumentsCheck.isValidForApplication(args) )
			Application.launch(App.class,args);
		else
			CommandLineInfo.parseAndPrintInfo(args);
	}

}

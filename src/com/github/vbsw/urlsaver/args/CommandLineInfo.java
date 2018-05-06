/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.urlsaver.args;


import com.github.vbsw.urlsaver.MainConfig;


/**
 * @author Vitali Baumtrok
 */
public class CommandLineInfo {

	public static void parseArgsAndPrintInfo ( final String[] args ) {
		if ( args.length == 1 )
			if ( ArgumentsParser.isOption(args[0],ArgumentsConfig.HELP_OPTION) )
				CommandLineInfo.printHelp();
			else if ( ArgumentsParser.isOption(args[0],ArgumentsConfig.VERSION_OPTION) )
				CommandLineInfo.printVersion();
			else if ( ArgumentsParser.isOption(args[0],ArgumentsConfig.COPYRIGHT_OPTION) )
				CommandLineInfo.printCopyright();
			else
				CommandLineInfo.printUnknownArgument(args[0]);
		else if ( args.length > 1 )
			CommandLineInfo.printTooManyArguments();
		else
			CommandLineInfo.printUnexpectedState(0);
	}

	private static void printHelp ( ) {
		System.out.println("USAGE");
		System.out.println("  URLSaver [OPTION|CONFIG]");
		System.out.println("OPTION");
		System.out.println("  --help       prints this help");
		System.out.println("  --version    prints version");
		System.out.println("  --copyright  prints copyright");
		System.out.println("CONFIG");
		System.out.println("  --config=<config-file-path>");
	}

	private static void printVersion ( ) {
		System.out.println(MainConfig.VERSION_STRING);
	}

	private static void printCopyright ( ) {
		System.out.println("Copyright 2017, 2018, Vitali Baumtrok (vbsw@mailbox.org).");
		System.out.println("Distributed under the Boost Software License, Version 1.0.");
	}

	private static void printUnexpectedState ( final int code ) {
		System.out.println("error: unexpected program state (" + code + ")");
	}

	private static void printUnknownArgument ( final String arg ) {
		System.out.println("error: unknown argument " + arg);
	}

	private static void printTooManyArguments ( ) {
		System.out.println("error: too many arguments");
	}

}

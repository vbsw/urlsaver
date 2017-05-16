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


package com.github.vbsw.urlsaver.app;

import com.github.vbsw.urlsaver.utility.Parser;

/**
 * @author Vitali Baumtrok
 */
final class Help {

	private static enum ArgumentType {
		NONE, ERROR_TOO_MANY_ARGS, ERROR_UNKNOWN_ARG, PRINT_HELP, PRINT_VERSION, PRINT_COPYRIGHT
	}

	private ArgumentType argumentType = ArgumentType.NONE;
	private String unknownArg = null;

	Help ( final String[] args ) {
		if ( isTooManyArguments(args) ) {
			argumentType = ArgumentType.ERROR_TOO_MANY_ARGS;

		} else if ( args.length == 1 ) {

			if ( Parser.isHelp(args[0]) ) {
				argumentType = ArgumentType.PRINT_HELP;

			} else if ( Parser.isVersion(args[0]) ) {
				argumentType = ArgumentType.PRINT_VERSION;

			} else if ( Parser.isCopyright(args[0]) ) {
				argumentType = ArgumentType.PRINT_COPYRIGHT;

			} else if ( args[0].length() > 0 ) {
				argumentType = ArgumentType.ERROR_UNKNOWN_ARG;
				unknownArg = args[0];
			}
		}
	}

	void print ( ) {
		switch ( argumentType ) {
			case ERROR_TOO_MANY_ARGS:
			System.out.println("error: too many arguments");
			break;

			case ERROR_UNKNOWN_ARG:
			System.out.println("error: unknown argument \"" + unknownArg + "\"");
			break;

			case PRINT_HELP:
			System.out.println("Usage:");
			System.out.println("   urls            Starts URL Saver.");
			System.out.println("   urls --help     Prints this help.");
			System.out.println("   urls --version  Prints version and copying conditions.");
			break;

			case PRINT_VERSION:
			System.out.println(Version.STRING);
			break;

			case PRINT_COPYRIGHT:
			System.out.println("Copyright 2017 Vitali Baumtrok (vbsw@mailbox.org)");
			System.out.println("URL Saver is free software: you can redistribute it and/or modify");
			System.out.println("it under the terms of the GNU General Public License as published by");
			System.out.println("the Free Software Foundation, either version 3 of the License, or");
			System.out.println("(at your option) any later version.");
			break;

			default:
			break;
		}
	}

	boolean isNone ( ) {
		return argumentType == ArgumentType.NONE;
	}

	private boolean isTooManyArguments ( final String[] args ) {
		return args.length > 1;
	}

}

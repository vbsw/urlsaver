/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2016 Vitali Baumtrok
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


package com.github.vbsw.urlsaver;


/**
 * @author Vitali Baumtrok
 */
public class Arguments {

	private static enum ParseResult {
		NONE, ERROR_TOO_MANY_ARGS, ERROR_UNKNOWN_ARG, PRINT_HELP, PRINT_VERSION
	}

	private ParseResult result = ParseResult.NONE;
	private String unknownArg = null;

	public Arguments ( final String[] args ) {
		if ( tooManyArguments(args) ) {
			result = ParseResult.ERROR_TOO_MANY_ARGS;

		} else if ( args.length == 1 ) {

			if ( args[0].equals("--help") ) {
				result = ParseResult.PRINT_HELP;

			} else if ( args[0].equals("--version") ) {
				result = ParseResult.PRINT_VERSION;

			} else if ( args[0].length() > 0 ) {
				result = ParseResult.ERROR_UNKNOWN_ARG;
				unknownArg = args[0];
			}
		}
	}

	public void printInfo ( ) {
		switch ( result ) {
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
			System.out.println("URL Saver (version 0.1.0)");
			System.out.println("This program is free software: you can redistribute it and/or modify");
			System.out.println("it under the terms of the GNU General Public License as published by");
			System.out.println("the Free Software Foundation, either version 3 of the License, or");
			System.out.println("(at your option) any later version.");
			break;

			default:
			break;
		}
	}

	public boolean hasNoInfo ( ) {
		return result == ParseResult.NONE;
	}

	private boolean tooManyArguments ( final String[] args ) {
		return args.length > 1;
	}

}

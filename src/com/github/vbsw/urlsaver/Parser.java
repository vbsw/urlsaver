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


import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
public class Parser {

	public static ArrayList<String> toStringArray ( final String string ) {
		final ArrayList<String> list = new ArrayList<String>();
		int offset = skipWhiteSpace(string,0);

		while ( offset < string.length() ) {
			final int endIndex = skipNonWhiteSpace(string,offset);
			final String word = string.substring(offset,endIndex);
			offset = skipWhiteSpace(string,endIndex);

			list.add(word);
		}
		return list;
	}

	public static boolean isWhiteSpace ( final String string ) {
		for ( int i = 0; i < string.length(); i += 1 ) {
			final char character = string.charAt(i);
			if ( isWhiteSpace(character) == false ) {
				return false;
			}
		}
		return true;
	}

	public static String getStringTillNewLine ( final byte[] bytes, final int offset, final Charset encoding ) {
		final int stringLength = Parser.getLengthTillNewLine(bytes,offset);
		final String str = new String(bytes,offset,stringLength,encoding);
		return str;
	}

	public static int getLengthTillNewLine ( final byte[] bytes, final int offset ) {
		int currIndex;
		for ( currIndex = offset; currIndex < bytes.length; currIndex += 1 ) {
			final byte byteChar = bytes[currIndex];
			if ( Parser.isNewLine(byteChar) ) {
				return currIndex - offset;
			}
		}
		return currIndex - offset;
	}

	public static boolean isNewLine ( final byte byteChar ) {
		return byteChar == 10 || byteChar == 13;
	}

	private static int skipNonWhiteSpace ( final String string, int offset ) {
		while ( offset < string.length() && !isWhiteSpace(string.charAt(offset)) ) {
			offset += 1;
		}
		return offset;
	}

	public static int skipWhiteSpace ( final String string, int offset ) {
		while ( offset < string.length() && isWhiteSpace(string.charAt(offset)) ) {
			offset += 1;
		}
		return offset;
	}

	public static int skipWhiteSpace ( final byte[] bytes, int offset ) {
		while ( offset < bytes.length && isWhiteSpace(bytes[offset]) ) {
			offset += 1;
		}
		return offset;
	}

	public static boolean isWhiteSpace ( final byte byteChar ) {
		return byteChar >= 0 && byteChar <= 32;
	}

	public static boolean isWhiteSpace ( final char character ) {
		return character >= 0 && character <= 32;
	}

	public static boolean isHelp ( final String param ) {
		return param.equals("--help") || param.equals("-help") || param.equals("help");
	}

	public static boolean isVersion ( final String param ) {
		return param.equals("--version") || param.equals("-version") || param.equals("version");
	}

	public static boolean isCopyright ( final String param ) {
		return param.equals("--copyright") || param.equals("-copyright") || param.equals("copyright");
	}

}

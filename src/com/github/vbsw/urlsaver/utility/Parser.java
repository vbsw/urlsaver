
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.utility;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;


/**
 * @author Vitali Baumtrok
 */
public final class Parser {

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

	public static int getLengthTillWhiteSpace ( final byte[] bytes, final int offset ) {
		int currIndex;

		for ( currIndex = offset; currIndex < bytes.length; currIndex += 1 ) {
			final byte byteChar = bytes[currIndex];

			if ( Parser.isWhiteSpace(byteChar) ) {
				return currIndex - offset;
			}
		}
		return currIndex - offset;
	}

	public static boolean isNewLine ( final byte byteChar ) {
		return byteChar == 10 || byteChar == 13;
	}

	public static int skipNonWhiteSpace ( final String string, int offset ) {
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

	public static int skipWhiteSpaceReverse ( final String string, int endIndexExclusive ) {
		while ( endIndexExclusive > 0 && isWhiteSpace(string.charAt(endIndexExclusive - 1)) ) {
			endIndexExclusive -= 1;
		}
		return endIndexExclusive;
	}

	public static boolean isWhiteSpace ( final byte byteChar ) {
		return byteChar >= 0 && byteChar <= 32;
	}

	public static boolean isWhiteSpace ( final char character ) {
		return character >= 0 && character <= 32;
	}

	public static boolean isHelp ( final String param ) {
		return param.equals("--help") || param.equals("-help") || param.equals("help") || param.equals("-h");
	}

	public static boolean isVersion ( final String param ) {
		return param.equals("--version") || param.equals("-version") || param.equals("version") || param.equals("-v");
	}

	public static boolean isCopyright ( final String param ) {
		return param.equals("--copyright") || param.equals("-copyright") || param.equals("copyright") || param.equals("-c");
	}

	public static boolean isTrue ( final String boolStr ) {
		final String trueStr = "true"; //$NON-NLS-1$
		final String yesStr = "yes"; //$NON-NLS-1$
		final String yStr = "y"; //$NON-NLS-1$
		final String oneStr = "1"; //$NON-NLS-1$

		return boolStr.equals(trueStr) || boolStr.equals(yesStr) || boolStr.equals(yStr) || boolStr.equals(oneStr);
	}

	public static boolean isEqualSortedLists ( final ArrayList<String> leftList, final ArrayList<String> rightList ) {
		for ( String key: rightList ) {
			final int index = Collections.binarySearch(leftList,key);

			if ( index < 0 ) {
				return false;
			}
		}
		return true;
	}

	public static String trim ( final String string ) {
		final int beginIndex = skipWhiteSpace(string,0);

		if ( beginIndex < string.length() ) {
			int endIndex = string.length();

			while ( Parser.isWhiteSpace(string.charAt(endIndex - 1)) ) {
				endIndex -= 1;
			}

			if ( string.length() == endIndex - beginIndex ) {
				return string;

			} else {
				return string.substring(beginIndex,endIndex);
			}

		} else {
			return "";
		}
	}

	public static int toInt ( final String intString ) {
		try {
			return Integer.parseInt(intString);

		} catch ( final NumberFormatException e ) {
			return 0;
		}
	}

}

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


import java.io.File;

import javafx.beans.property.SimpleStringProperty;


/**
 * @author Vitali Baumtrok
 */
public class FileTableItem {

	public static final String EDITED_STR = "*"; //$NON-NLS-1$
	public static final String NOT_EDITED_STR = ""; //$NON-NLS-1$
	public static final String DEFAULT_NAME = "unknown"; //$NON-NLS-1$
	public static final String DEFAULT_EDITED = NOT_EDITED_STR;
	public static final String DEFAULT_LOADED = "0"; //$NON-NLS-1$

	private String fileName;
	private final SimpleStringProperty name = new SimpleStringProperty(DEFAULT_NAME);
	private final SimpleStringProperty edited = new SimpleStringProperty(DEFAULT_EDITED);
	private final SimpleStringProperty loaded = new SimpleStringProperty(DEFAULT_LOADED);
	public final FileData fileData;

	public FileTableItem ( final FileData fileData ) {
		this.fileData = fileData;
		setFileName();
		setName(fileName + "    [ 0 % ]");
	}

	public SimpleStringProperty nameProperty ( ) {
		return name;
	}

	public String getName ( ) {
		return name.get();
	}

	public void setName ( final String nameStr ) {
		name.set(nameStr);
	}

	public SimpleStringProperty editedProperty ( ) {
		return edited;
	}

	public boolean isEdited ( ) {
		final String editedStr = edited.get();
		return (editedStr != null) && (editedStr.equals(NOT_EDITED_STR) == false);
	}

	public void setEdited ( final String editedStr ) {
		name.set(editedStr);
	}

	public void setEdited ( final boolean edited ) {
		if ( edited ) {
			setEdited(EDITED_STR);
		} else {
			setEdited(NOT_EDITED_STR);
		}
	}

	public SimpleStringProperty loadedProperty ( ) {
		return loaded;
	}

	public double getLoaded ( ) {
		try {
			final String loadedStr = loaded.get();
			final int loadedInt = Integer.parseInt(loadedStr);
			return loadedInt / 100d;

		} catch ( final Exception e ) {
			return 0d;
		}
	}

	private void setFileName ( ) {
		final String filePath = fileData.filePath.toString();
		final char fileSeperator = File.separatorChar;
		fileName = filePath;

		for ( int i = filePath.length() - 1; i >= 0; i -= 1 ) {
			if ( filePath.charAt(i) == fileSeperator ) {
				fileName = filePath.substring(i + 1);
				break;
			}
		}
	}

}

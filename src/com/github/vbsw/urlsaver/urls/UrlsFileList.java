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


package com.github.vbsw.urlsaver.urls;


import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.JarPath;
import com.github.vbsw.urlsaver.app.App;

import javafx.beans.property.SimpleBooleanProperty;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
public final class UrlsFileList extends ArrayList<UrlsFile> {

	private final SimpleBooleanProperty loaded = new SimpleBooleanProperty();
	private final SimpleBooleanProperty dirty = new SimpleBooleanProperty();
	private final SimpleBooleanProperty savingMode = new SimpleBooleanProperty();
	private final SimpleBooleanProperty empty = new SimpleBooleanProperty();

	public UrlsFileList ( final String fileExtension ) {
		final ArrayList<Path> filePaths = JarPath.getPaths(fileExtension);

		for ( Path filePath: filePaths ) {
			final UrlsFile urlFile = new UrlsFile(filePath);

			add(urlFile);
		}
		empty.setValue(super.size() == 0);
	}

	public void loadDefault ( ) {
		for ( UrlsFile urlsFile: this ) {
			if ( urlsFile.isDefault() ) {
				urlsFile.load();
			}
		}
	}

	public UrlsFile getByName ( final String fileName ) {
		for ( UrlsFile urlsFile: this ) {
			if ( urlsFile.getFileName().equals(fileName) ) {
				return urlsFile;
			}
		}
		return null;
	}

	public SimpleBooleanProperty loadedProperty ( ) {
		return loaded;
	}

	public SimpleBooleanProperty dirtyProperty ( ) {
		return dirty;
	}

	public SimpleBooleanProperty savingModeProperty ( ) {
		return savingMode;
	}

	public SimpleBooleanProperty emptyProperty ( ) {
		return empty;
	}

	public boolean isAnyFileDirty ( ) {
		for ( UrlsFile urlsFile: App.files ) {
			if ( urlsFile.isDirty() ) {
				return true;
			}
		}
		return false;
	}

}

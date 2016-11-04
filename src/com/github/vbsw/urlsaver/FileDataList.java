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


import java.nio.file.Path;
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
public class FileDataList extends ArrayList<FileData> {

	public static final int INITIAL_DATA_CAPACITY = 10;

	public FileDataList ( ) {
		super(INITIAL_DATA_CAPACITY);
	}

	public void readAll ( ) {
		final ArrayList<Path> filePaths = JarPath.getFiles(App.settings.fileFileExtension);

		super.clear();

		for ( Path filePath: filePaths ) {
			final FileData fileData = new FileData(filePath);
			super.add(fileData);
		}
	}

	public boolean isModified ( ) {
		for ( FileData fileData: this ) {
			if ( fileData.isModified() ) {
				return true;
			}
		}
		return false;
	}

}

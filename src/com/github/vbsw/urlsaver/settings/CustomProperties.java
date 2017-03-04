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


package com.github.vbsw.urlsaver.settings;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.vbsw.urlsaver.JarPath;
import com.github.vbsw.urlsaver.resources.Resources;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
final class CustomProperties extends DefaultProperties {

	CustomProperties ( ) {
		final String FILE_NAME = Resources.CUSTOM_PROPERTIES_FILE_PATH;
		final Path filePath = Paths.get(JarPath.get().toString(),FILE_NAME);

		try ( final InputStream stream = Files.newInputStream(filePath) ) {
			load(stream);
		} catch ( final Exception e ) {
		}
	}

}


//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.settings;


import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.vbsw.urlsaver.resources.Resources;
import com.github.vbsw.urlsaver.utility.Jar;


/**
 * @author Vitali Baumtrok
 */
@SuppressWarnings ( "serial" )
final class CustomProperties extends DefaultProperties {

	CustomProperties ( ) {
		final String FILE_NAME = Resources.CUSTOM_PROPERTIES_FILE_PATH;
		final Path filePath = Paths.get(Jar.getPathToJar().toString(),FILE_NAME);

		try ( final InputStream stream = Files.newInputStream(filePath) ) {
			load(stream);
		} catch ( final Exception e ) {
		}
	}

}

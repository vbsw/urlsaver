/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.utility;


import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
public class OSFiles {

	public static ArrayList<Path> getFilesFromDirectory ( final Path directory, final String... extensions ) {
		final ArrayList<Path> paths = new ArrayList<Path>(10);
		try ( final DirectoryStream<Path> filePathsStream = Files.newDirectoryStream(directory) ) {
			for ( Path filePath: filePathsStream ) {
				if ( Files.isDirectory(filePath) == false ) {
					for ( String fileExtension: extensions ) {
						if ( filePath.getFileName().toString().endsWith(fileExtension) ) {
							paths.add(filePath);
							break;
						}
					}
				}
			}
		} catch ( final IOException e ) {
			e.printStackTrace();
		}
		return paths;
	}

}

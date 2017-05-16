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


package com.github.vbsw.urlsaver.utility;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
public final class Jar {
	public static final String JAR_PREFIX = "jar:"; //$NON-NLS-1$
	public static final String CLASS_FILE_EXTENSION = ".class"; //$NON-NLS-1$
	public static final String BIN_FOLDER = "/bin"; //$NON-NLS-1$
	public static final int INITIAL_ARRAY_CAPACITY = 10;

	public static Path getPathToJar ( ) {
		return getPathToJar(Jar.class);
	}

	public static Path getPathToJar ( final Class<? extends Object> clazz ) {
		try {
			final String className = clazz.getSimpleName() + CLASS_FILE_EXTENSION;
			final URL classUrl = clazz.getResource(className);
			final String classUrlStr = classUrl.toString();
			final String canonicalClassName = clazz.getCanonicalName();
			final int canonicalClassLength = canonicalClassName.length() + CLASS_FILE_EXTENSION.length();

			if ( Jar.isJarFile(classUrlStr) ) {
				final String jarDirStr = classUrlStr.substring(4,classUrlStr.length() - canonicalClassLength - 2);
				final URL jarDirUrl = new URL(jarDirStr);
				final URI jarDirUri = jarDirUrl.toURI();
				final Path path = Paths.get(jarDirUri);

				return path.getParent();

			} else {
				final String projDirStr = classUrlStr.substring(0,classUrlStr.length() - canonicalClassLength - BIN_FOLDER.length());
				final URL projDirUrl = new URL(projDirStr);
				final URI projDirUri = projDirUrl.toURI();
				final Path path = Paths.get(projDirUri);

				return path;
			}

		} catch ( final Exception e ) {
			e.printStackTrace();

			return null;
		}
	}

	public static InputStream getResourceAsStream ( final String pathInsideJar ) {
		final ClassLoader classLoader = Jar.class.getClassLoader();
		final InputStream stream = classLoader.getResourceAsStream(pathInsideJar);

		return stream;
	}

	public static void copy ( final String srcPathInsideJar, final Path dest ) {
		try ( final InputStream srcInStream = Jar.getResourceAsStream(srcPathInsideJar) ) {
			Files.copy(srcInStream,dest);

		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Path> getFilePaths ( final String... extensions ) {
		final Path dir = Jar.getPathToJar();
		final ArrayList<Path> paths = new ArrayList<Path>(INITIAL_ARRAY_CAPACITY);

		try ( final DirectoryStream<Path> filePathsStream = Files.newDirectoryStream(dir) ) {
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

	private static boolean isJarFile ( final String urlStr ) {
		return urlStr.startsWith(JAR_PREFIX);
	}

}

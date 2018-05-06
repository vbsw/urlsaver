/*
 * Copyright 2017, 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
public final class JarFile {

	private static final String JAR_PREFIX = "jar:";
	private static final String CLASS_FILE_EXTENSION = ".class";
	private static final String BIN_FOLDER = "/bin";
	private static final int INITIAL_ARRAY_CAPACITY = 10;

	public static Path getPath ( ) {
		return getPath(JarFile.class);
	}

	public static Path getPath ( final Class<? extends Object> clazz ) {
		try {
			final String className = clazz.getSimpleName() + CLASS_FILE_EXTENSION;
			final URL classUrl = clazz.getResource(className);
			final String classUrlStr = classUrl.toString();
			final String canonicalClassName = clazz.getCanonicalName();
			final int canonicalClassLength = canonicalClassName.length() + CLASS_FILE_EXTENSION.length();
			final Path path;

			if ( JarFile.isJarFile(classUrlStr) )
				path = getPathToJarFile(classUrlStr,canonicalClassLength);
			else
				path = getPathToProject(classUrlStr,canonicalClassLength);

			return path;

		} catch ( final Exception e ) {
			e.printStackTrace();

			return null;
		}
	}

	public static InputStream getStreamOfResource ( final String pathInsideJar ) {
		final ClassLoader classLoader = JarFile.class.getClassLoader();
		final InputStream stream = classLoader.getResourceAsStream(pathInsideJar);

		return stream;
	}

	public static Path getPathToResource ( final String pathInsideJar ) {
		final ClassLoader classLoader = JarFile.class.getClassLoader();
		final URL url = classLoader.getResource(pathInsideJar);
		try {
			final URI uri = url.toURI();
			final Path path = Paths.get(uri);
			return path;
		} catch ( URISyntaxException e ) {
		}
		return null;
	}

	public static void copyResource ( final String srcPathInsideJar, final Path dest ) {
		try ( final InputStream srcInStream = JarFile.getStreamOfResource(srcPathInsideJar) ) {
			Files.copy(srcInStream,dest,StandardCopyOption.REPLACE_EXISTING);
		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Path> getFilePaths ( final String... extensions ) {
		final Path dir = JarFile.getPath();
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

	private static Path getPathToJarFile ( final String pathToClassInsideJar, final int canonicalClassLength ) throws MalformedURLException, URISyntaxException {
		// pathToClassInsideJar example
		// jar:file:/C:/Users/alice/Desktop/test.jar!/com/github/vbsw/urlsaver/resources/JarFile.class
		final String jarDirStr = pathToClassInsideJar.substring(JAR_PREFIX.length(),pathToClassInsideJar.length() - canonicalClassLength - "!/".length());
		final URL jarDirUrl = new URL(jarDirStr);
		final URI jarDirUri = jarDirUrl.toURI();
		final Path path = Paths.get(jarDirUri);

		return path.getParent();
	}

	private static Path getPathToProject ( final String pathToClassInProject, final int canonicalClassLength ) throws MalformedURLException, URISyntaxException {
		final String projDirStr = pathToClassInProject.substring(0,pathToClassInProject.length() - canonicalClassLength - BIN_FOLDER.length());
		final URL projDirUrl = new URL(projDirStr);
		final URI projDirUri = projDirUrl.toURI();
		final Path path = Paths.get(projDirUri);

		return path;
	}

	private static boolean isJarFile ( final String urlStr ) {
		return urlStr.startsWith(JAR_PREFIX);
	}

}

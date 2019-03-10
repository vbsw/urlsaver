/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.utility;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import com.github.vbsw.urlsaver.api.IPathProvider;


/**
 * @author Vitali Baumtrok
 */
public class PathProvider implements IPathProvider {

	@Override
	public Path getWorkingDirectory ( ) {
		final String classURLString = getClassURLString();
		final int canonicalClassNameLength = getCanonicalClassNameLength();
		final boolean classIsInJar = isInJar(classURLString);
		final Path directory;
		if ( classIsInJar ) {
			final Path jarPath = getJarPath(classURLString,canonicalClassNameLength);
			directory = jarPath.getParent();
		} else {
			final Path projectPath = getProjectPath(classURLString,canonicalClassNameLength);
			directory = projectPath;
		}
		return directory;
	}

	@Override
	public ArrayList<Path> getFiles ( final Path directory, final String... fileExtensions ) {
		final ArrayList<Path> paths = new ArrayList<Path>(10);
		if ( fileExtensions.length == 1 ) {
			final ArrayList<String> filePathStringsBuffer = new ArrayList<String>(10);
			final String fileExtension = fileExtensions[0];
			if ( fileExtension != null ) {
				addFilesToListOrdered(paths,filePathStringsBuffer,directory,fileExtension);
			}
		} else if ( fileExtensions.length > 1 ) {
			final ArrayList<String> filePathStringsBuffer = new ArrayList<String>(10);
			final ArrayList<String> fileExtensionsReduced = reduceByEnding(fileExtensions);
			for ( String fileExtension: fileExtensionsReduced ) {
				addFilesToListOrdered(paths,filePathStringsBuffer,directory,fileExtension);
			}
		}
		return paths;
	}

	/**
	 * @return for example jar:file:/C:/Users/alice/Desktop/test.jar!/com/github/vbsw/urlsaver/utility/Jar.class
	 */
	private String getClassURLString ( ) {
		final String className = PathProvider.class.getSimpleName() + ".class";
		final URL classURL = PathProvider.class.getResource(className);
		final String classURLString = classURL.toString();
		return classURLString;
	}

	private int getCanonicalClassNameLength ( ) {
		final String canonicalClassName = PathProvider.class.getCanonicalName();
		final int canonicalClassNameLength = canonicalClassName.length() + ".class".length();
		return canonicalClassNameLength;
	}

	private boolean isInJar ( final String urlStr ) {
		return urlStr.startsWith("jar:");
	}

	private Path getJarPath ( final String classURLString, final int canonicalClassNameLength ) {
		try {
			final String jarDirStr = classURLString.substring("jar:".length(),classURLString.length() - canonicalClassNameLength - "!/".length());
			final URL jarDirUrl = new URL(jarDirStr);
			final URI jarDirUri = jarDirUrl.toURI();
			final Path path = Paths.get(jarDirUri);
			return path;
		} catch ( MalformedURLException | URISyntaxException e ) {
			e.printStackTrace();
		}
		return null;
	}

	private Path getProjectPath ( final String classURLString, final int canonicalClassNameLength ) {
		try {
			final String projDirStr = classURLString.substring(0,classURLString.length() - canonicalClassNameLength - "/bin".length());
			final URL projDirUrl = new URL(projDirStr);
			final URI projDirUri = projDirUrl.toURI();
			final Path path = Paths.get(projDirUri);
			return path;
		} catch ( MalformedURLException | URISyntaxException e ) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * adds paths to <code>paths</code> of files in directory.
	 * @param paths must be in ascending order
	 */
	private void addFilesToListOrdered ( final ArrayList<Path> paths, final ArrayList<String> pathStringsBuffer, final Path directory, final String fileExtension ) {
		try ( final DirectoryStream<Path> filePathsStream = Files.newDirectoryStream(directory) ) {
			for ( Path filePath: filePathsStream ) {
				if ( !Files.isDirectory(filePath) ) {
					if ( filePath.getFileName().toString().endsWith(fileExtension) ) {
						final String filePathString = filePath.toString();
						final int index = Collections.binarySearch(pathStringsBuffer,filePathString);
						if ( index < 0 ) {
							final int insertIndex = -1 * index - 1;
							pathStringsBuffer.add(insertIndex,filePathString);
							paths.add(insertIndex,filePath);
						}
					}
				}
			}
		} catch ( final IOException e ) {
			e.printStackTrace();
		}
	}

	/**
	 * @return if {"aa","a"}, then {"a"} is returned.
	 */
	private ArrayList<String> reduceByEnding ( final String[] strings ) {
		final ArrayList<String> stringsReduced = new ArrayList<String>(strings.length);
		for ( int i = 0; i < strings.length; i += 1 ) {
			final String strA = strings[i];
			if ( strA != null ) {
				for ( int j = 0; j < i; j += 1 ) {
					final String strB = strings[j];
					if ( strB != null && strB.endsWith(strA) ) {
						strings[j] = null;
					}
				}
				for ( int j = i + 1; j < strings.length; j += 1 ) {
					final String strB = strings[j];
					if ( strB != null && strB.endsWith(strA) ) {
						strings[j] = null;
					}
				}
			}
		}
		for ( String str: strings ) {
			if ( str != null ) {
				stringsReduced.add(str);
			}
		}
		return stringsReduced;
	}

}

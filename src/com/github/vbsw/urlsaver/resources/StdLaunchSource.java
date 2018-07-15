/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.resources;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.vbsw.urlsaver.api.LaunchSource;
import com.github.vbsw.urlsaver.api.Resource;


/**
 * @author Vitali Baumtrok
 */
public class StdLaunchSource extends LaunchSource {

	protected boolean classIsInJar;
	protected Path directory;
	protected Path jarName;

	public StdLaunchSource ( ) {
		final String classURLString = getClassURLString();
		final int canonicalClassNameLength = getCanonicalClassNameLength();
		classIsInJar = checkIsInJar(classURLString);
		if ( classIsInJar ) {
			final Path jarPath = getJarPath(classURLString,canonicalClassNameLength);
			directory = jarPath.getParent();
			jarName = jarPath.getFileName();
		} else {
			final Path projectPath = getProjectPath(classURLString,canonicalClassNameLength);
			directory = projectPath;
			jarName = null;
		}
	}

	/**
	 * @return for example jar:file:/C:/Users/alice/Desktop/test.jar!/com/github/vbsw/urlsaver/utility/Jar.class
	 */
	protected String getClassURLString ( ) {
		final String className = StdLaunchSource.class.getSimpleName() + ".class";
		final URL classURL = StdLaunchSource.class.getResource(className);
		final String classURLString = classURL.toString();
		return classURLString;
	}

	protected int getCanonicalClassNameLength ( ) {
		final String canonicalClassName = StdLaunchSource.class.getCanonicalName();
		final int canonicalClassNameLength = canonicalClassName.length() + ".class".length();
		return canonicalClassNameLength;
	}

	protected boolean checkIsInJar ( final String urlStr ) {
		return urlStr.startsWith("jar:");
	}

	protected Path getJarPath ( final String classURLString, final int canonicalClassNameLength ) {
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

	protected Path getProjectPath ( final String classURLString, final int canonicalClassNameLength ) {
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

	@Override
	public boolean isJar ( ) {
		return classIsInJar;
	}

	@Override
	public Path getDirectory ( ) {
		return directory;
	}

	@Override
	public Path getJarName ( ) {
		return jarName;
	}

	@Override
	public Resource getJarFileResource ( final String filePathInsideJar ) {
		final JarFileResource resource = new JarFileResource(filePathInsideJar);
		return resource;
	}

	@Override
	public Resource getOSFileResource ( final Path filePath ) {
		final OSFileResource resource = new OSFileResource(filePath);
		return resource;
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.resources;


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
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
public class Project {

	private static boolean inJar;
	private static Path directory;
	private static Path jarName;

	public static void initialize ( ) {
		final String classURLString = Project.getClassURLString();
		final int canonicalClassNameLength = Project.getCanonicalClassNameLength();
		inJar = Project.checkIsInJar(classURLString);
		if ( inJar ) {
			final Path jarPath = Project.getJarPath(classURLString,canonicalClassNameLength);
			directory = jarPath.getParent();
			jarName = jarPath.getFileName();
		} else {
			final Path projectPath = Project.getProjectPath(classURLString,canonicalClassNameLength);
			directory = projectPath;
			jarName = null;
		}
	}

	public static boolean isInJar ( ) {
		return inJar;
	}

	public static Path getDirectory ( ) {
		return directory;
	}

	public static Path getJarName ( ) {
		return jarName;
	}

	public static Resource getResourceFromJar ( final String pathInsideJar ) {
		final FileInJarResource resource = new FileInJarResource(pathInsideJar);
		return resource;
	}

	public static Resource getResourceFromProjectDirectory ( final String file ) {
		final Path path = directory.resolve(file);
		final FileResource resource = new FileResource(path);
		return resource;
	}

	public static Resource getResourceFromPath ( final Path path ) {
		final FileResource resource = new FileResource(path);
		return resource;
	}

	public static ArrayList<Path> getFilesFromProjectDirectory ( final String... extensions ) {
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

	/**
	 * @return for example jar:file:/C:/Users/alice/Desktop/test.jar!/com/github/vbsw/urlsaver/resources/Project.class
	 */
	private static String getClassURLString ( ) {
		final String className = Project.class.getSimpleName() + ".class";
		final URL classURL = Project.class.getResource(className);
		final String classURLString = classURL.toString();
		return classURLString;
	}

	private static int getCanonicalClassNameLength ( ) {
		final String canonicalClassName = Project.class.getCanonicalName();
		final int canonicalClassNameLength = canonicalClassName.length() + ".class".length();
		return canonicalClassNameLength;
	}

	private static boolean checkIsInJar ( final String urlStr ) {
		return urlStr.startsWith("jar:");
	}

	private static Path getJarPath ( final String classURLString, final int canonicalClassNameLength ) {
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

	private static Path getProjectPath ( final String classURLString, final int canonicalClassNameLength ) {
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

	private static class FileResource extends Resource {

		private final Path path;
		private final Path fileName;
		private final URI uri;

		private FileResource ( final Path path ) {
			this.path = path;
			this.fileName = path.getFileName();
			this.uri = path.toUri();
		}

		@Override
		public boolean exists ( ) {
			return Files.exists(path);
		}

		@Override
		public InputStream newInputStream ( ) throws IOException {
			return Files.newInputStream(path);
		}

		@Override
		public Path getPath ( ) {
			return path;
		}

		@Override
		public URI getURI ( ) {
			return uri;
		}

		@Override
		public Path getFileName ( ) {
			return fileName;
		}

	}

	private static class FileInJarResource extends Resource {

		private final Path fileName;
		private final URI uri;
		private final String pathInsideJar;
		private final boolean exists;

		private FileInJarResource ( final String pathInsideJar ) {
			final ClassLoader classLoader = FileInJarResource.class.getClassLoader();
			final URL url = classLoader.getResource(pathInsideJar);
			this.pathInsideJar = pathInsideJar;
			this.fileName = Paths.get(pathInsideJar).getFileName();
			if ( url != null ) {
				this.exists = true;
				this.uri = toURI(url);
			} else {
				this.exists = false;
				this.uri = null;
			}
		}

		@Override
		public boolean exists ( ) {
			return exists;
		}

		@Override
		public InputStream newInputStream ( ) {
			final ClassLoader classLoader = FileInJarResource.class.getClassLoader();
			final InputStream stream = classLoader.getResourceAsStream(pathInsideJar);
			return stream;
		}

		@Override
		public URI getURI ( ) {
			return uri;
		}

		@Override
		public Path getFileName ( ) {
			return fileName;
		}

		private URI toURI ( final URL url ) {
			try {
				final URI uri = url.toURI();
				return uri;
			} catch ( URISyntaxException e ) {
				e.printStackTrace();
			}
			return null;
		}

	}

}

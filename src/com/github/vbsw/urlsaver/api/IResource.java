/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


/**
 * @author Vitali Baumtrok
 */
public interface IResource {

	public default boolean exists ( ) {
		return false;
	}

	public default InputStream getInputStream ( ) throws IOException {
		throw new UnsupportedOperationException();
	}

	public default Path getPath ( ) {
		throw new UnsupportedOperationException();
	}

	public default URI getURI ( ) {
		throw new UnsupportedOperationException();
	}

	public default Path getFileName ( ) {
		throw new UnsupportedOperationException();
	}

	public default void copyTo ( final Path target ) {
		try ( final InputStream inputStream = getInputStream() ) {
			Files.copy(inputStream,target,StandardCopyOption.REPLACE_EXISTING);
		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

	public class JarFile implements IResource {

		protected final Path fileName;
		protected final URI fileURI;
		protected final String filePathInsideJar;
		protected final boolean fileExists;

		public JarFile ( final String pathInsideJar ) {
			final ClassLoader classLoader = JarFile.class.getClassLoader();
			final URL url = classLoader.getResource(pathInsideJar);
			this.filePathInsideJar = pathInsideJar;
			this.fileName = Paths.get(pathInsideJar).getFileName();
			if ( url != null ) {
				this.fileExists = true;
				this.fileURI = toURI(url);
			} else {
				this.fileExists = false;
				this.fileURI = null;
			}
		}

		@Override
		public boolean exists ( ) {
			return fileExists;
		}

		@Override
		public InputStream getInputStream ( ) throws IOException {
			final ClassLoader classLoader = IResource.JarFile.class.getClassLoader();
			final InputStream stream = classLoader.getResourceAsStream(filePathInsideJar);
			if ( stream == null ) {
				throw new NoSuchFileException(filePathInsideJar);
			}
			return stream;
		}

		@Override
		public URI getURI ( ) {
			return fileURI;
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

	public class OSFile implements IResource {

		protected final Path filePath;
		protected final Path fileName;
		protected final URI fileURI;

		public OSFile ( final Path path ) {
			this.filePath = path;
			this.fileName = path.getFileName();
			this.fileURI = path.toUri();
		}

		@Override
		public boolean exists ( ) {
			return Files.exists(filePath);
		}

		@Override
		public InputStream getInputStream ( ) throws IOException {
			return Files.newInputStream(filePath);
		}

		@Override
		public Path getPath ( ) {
			return filePath;
		}

		@Override
		public URI getURI ( ) {
			return fileURI;
		}

		@Override
		public Path getFileName ( ) {
			return fileName;
		}
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.resources;


import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.vbsw.urlsaver.api.Resource;


/**
 * @author Vitali Baumtrok
 */
public class JarFileResource extends Resource {

	protected final Path fileName;
	protected final URI uri;
	protected final String pathInsideJar;
	protected final boolean exists;

	public JarFileResource ( final String pathInsideJar ) {
		final ClassLoader classLoader = JarFileResource.class.getClassLoader();
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
		final ClassLoader classLoader = JarFileResource.class.getClassLoader();
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

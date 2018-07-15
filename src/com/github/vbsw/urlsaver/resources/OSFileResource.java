/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.resources;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.vbsw.urlsaver.api.Resource;


/**
 * @author Vitali Baumtrok
 */
public class OSFileResource extends Resource {

	protected final Path path;
	protected final Path fileName;
	protected final URI uri;

	public OSFileResource ( final Path path ) {
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

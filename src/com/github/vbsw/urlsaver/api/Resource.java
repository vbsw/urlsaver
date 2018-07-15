/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


/**
 * @author Vitali Baumtrok
 */
public class Resource {

	public boolean exists ( ) {
		return false;
	}

	public InputStream newInputStream ( ) throws IOException {
		throw new UnsupportedOperationException();
	}

	public Path getPath ( ) {
		throw new UnsupportedOperationException();
	}

	public URI getURI ( ) {
		throw new UnsupportedOperationException();
	}

	public Path getFileName ( ) {
		throw new UnsupportedOperationException();
	}

	public void copyTo ( final Path target ) {
		try ( final InputStream inputStream = newInputStream() ) {
			Files.copy(inputStream,target,StandardCopyOption.REPLACE_EXISTING);
		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

}

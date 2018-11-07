/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.nio.file.Path;


/**
 * @author Vitali Baumtrok
 */
public abstract class ProgramFile {

	public abstract boolean isJar ( );

	public abstract Path getName ( );

	public abstract Path getDirectory ( );

	public abstract Resource getJarFileResource ( String filePathInsideJar );

	public abstract Resource getOSFileResource ( Path filePath );

}

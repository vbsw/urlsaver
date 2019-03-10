/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


import java.nio.file.Path;
import java.util.ArrayList;


/**
 * @author Vitali Baumtrok
 */
public interface IPathProvider {

	/**
	 * @return directory of jar or directory of project
	 */
	public Path getWorkingDirectory ( );

	/**
	 * @return file paths in ascending order
	 */
	public ArrayList<Path> getFiles ( Path directory, String... fileExtensions );

}

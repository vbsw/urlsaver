/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */

package com.github.vbsw.urlsaver.resources;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * @author Vitali Baumtrok
 */
public class ResourcesConfig {

	public static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

	public static final String CUSTOM_CSS_FILE_PATH = "urlsaver.css";
	public static final String DEFAULT_CSS_FILE_PATH = "com/github/vbsw/urlsaver/resources/default.css";

	public static final String CUSTOM_FXML_FILE_PATH = "urlsaver.fxml";
	public static final String DEFAULT_FXML_FILE_PATH = "com/github/vbsw/urlsaver/resources/default.fxml";

	public static final String DEFAULT_PREFERENCES_FILE_PATH = "com/github/vbsw/urlsaver/resources/default.pref";
	public static final String CUSTOM_PREFERENCES_FILE_PATH = "urlsaver.pref";

}

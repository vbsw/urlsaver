/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
 * 
 * This file is part of URL Saver.
 * 
 * URL Saver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * URL Saver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with URL Saver.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.vbsw.urlsaver.resources;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


/**
 * @author Vitali Baumtrok
 */
public class Resources {

	public static final Charset ENCODING = StandardCharsets.UTF_8;

	public static final String CUSTOM_CSS_FILE_PATH = "urlsaver.css"; //$NON-NLS-1$
	public static final String DEFAULT_CSS_FILE_PATH = "com/github/vbsw/urlsaver/resources/default.css"; //$NON-NLS-1$

	public static final String CUSTOM_FXML_FILE_PATH = "urlsaver.fxml"; //$NON-NLS-1$
	public static final String DEFAULT_FXML_FILE_PATH = "com/github/vbsw/urlsaver/resources/default.fxml"; //$NON-NLS-1$

	public static final String DEFAULT_PROPERTIES_FILE_PATH = "com/github/vbsw/urlsaver/resources/default.properties"; //$NON-NLS-1$
	public static final String CUSTOM_PROPERTIES_FILE_PATH = "urlsaver.properties"; //$NON-NLS-1$

}

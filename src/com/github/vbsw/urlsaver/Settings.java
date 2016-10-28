/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2016 Vitali Baumtrok
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


package com.github.vbsw.urlsaver;


/**
 * @author Vitali Baumtrok
 */
public class Settings {

	public static final String DEFAULT_WINDOW_TITLE = "URL Saver (0.1.0)"; //$NON-NLS-1$
	public static final double DEFAULT_WINDOW_WIDTH = 720;
	public static final double DEFAULT_WINDOW_HEIGHT = 420;
	public static final boolean DEFAULT_WINDOW_MAXIMIZED = false;
	public static final boolean DEFAULT_FILE_AUTO_LOAD = false;
	public static final String DEFAULT_FILE_EXTENSION = "urls"; //$NON-NLS-1$
	public static final String DEFAULT_FILE_AUTO_SELECT = "all.urls.txt"; //$NON-NLS-1$

	public String windowTitle;
	public double windowWidth;
	public double windowHeight;
	public boolean windowMaximized;
	public boolean fileAutoLoad;
	public String fileFileExtension;
	public String fileAutoSelect;
	public boolean externalProperties;
	public double decorationWidth;
	public double decorationHeight;

	public Settings ( ) {
		read();
	}

	public void read ( ) {
		final Properties properties = new Properties();
		windowTitle = properties.getWindowTitle(DEFAULT_WINDOW_TITLE);
		windowWidth = properties.getWindowWidth(DEFAULT_WINDOW_WIDTH);
		windowHeight = properties.getWindowHeight(DEFAULT_WINDOW_HEIGHT);
		windowMaximized = properties.isWindowMaximized(DEFAULT_WINDOW_MAXIMIZED);
		fileAutoLoad = properties.isFileAutoLoad(DEFAULT_FILE_AUTO_LOAD);
		fileFileExtension = properties.getFileExtension(DEFAULT_FILE_EXTENSION);
		fileAutoSelect = properties.getFileAutoSelect(DEFAULT_FILE_AUTO_SELECT);
		externalProperties = properties.isExternal();
	}

}

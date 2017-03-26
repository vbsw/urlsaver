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


package com.github.vbsw.urlsaver.settings;


/**
 * @author Vitali Baumtrok
 */
public final class Settings {

	private String windowTitle;
	private String customWindowTitle;
	private String defaultWindowTitle;

	private double windowWidth;
	private double customWindowWidth;
	private double defaultWindowWidth;

	private double windowHeight;
	private double customWindowHeight;
	private double defaultWindowHeight;

	private int windowMaximized;
	private int customWindowMaximized;
	private int defaultWindowMaximized;

	private int autoloadAll;
	private int customAutoloadAll;
	private int defaultAutoloadAll;

	private String fileExtension;
	private String customFileExtension;
	private String defaultFileExtension;

	private String fileSelect;
	private String customFileSelect;
	private String defaultFileSelect;

	private int searchByPrefix;
	private int customSearchByPrefix;
	private int defaultSearchByPrefix;

	public Settings ( ) {
		final DefaultProperties defaultProperties = new DefaultProperties();

		defaultWindowTitle = defaultProperties.getWindowTitle();
		defaultWindowWidth = defaultProperties.getWindowWidth();
		defaultWindowHeight = defaultProperties.getWindowHeight();
		defaultWindowMaximized = defaultProperties.getWindowMaximized();
		defaultAutoloadAll = defaultProperties.getAutoloadAll();
		defaultFileExtension = defaultProperties.getFileExtension();
		defaultFileSelect = defaultProperties.getFileSelect();
		defaultSearchByPrefix = defaultProperties.getSearchByPrefix();

		loadCustomValues();
		setAllToCustom();
	}

	public void loadCustomValues ( ) {
		final CustomProperties properties = new CustomProperties();

		customWindowTitle = properties.getWindowTitle();
		customWindowWidth = properties.getWindowWidth();
		customWindowHeight = properties.getWindowHeight();
		customWindowMaximized = properties.getWindowMaximized();
		customAutoloadAll = properties.getAutoloadAll();
		customFileExtension = properties.getFileExtension();
		customFileSelect = properties.getFileSelect();
		customSearchByPrefix = properties.getSearchByPrefix();
	}

	public void setAllToCustom ( ) {
		windowTitle = isCustomWindowTitleAvailable() ? customWindowTitle : defaultWindowTitle;
		windowWidth = isCustomWindowWidthAvailable() ? customWindowWidth : defaultWindowWidth;
		windowHeight = isCustomWindowHeightAvailable() ? customWindowHeight : defaultWindowHeight;
		windowMaximized = isCustomWindowMaximizedAvailable() ? customWindowMaximized : defaultWindowMaximized;
		autoloadAll = isCustomAutoloadAllAvailable() ? customAutoloadAll : defaultAutoloadAll;
		fileExtension = isCustomUrlsFileExtensionAvailable() ? customFileExtension : defaultFileExtension;
		fileSelect = isCustomUrlsFileSelectAvailable() ? customFileSelect : defaultFileSelect;
		searchByPrefix = isCustomSearchByPrefixAvailable() ? customSearchByPrefix : defaultSearchByPrefix;
	}

	public void setAllToDefault ( ) {
		windowTitle = customWindowTitle = defaultWindowTitle;
		windowWidth = customWindowWidth = defaultWindowWidth;
		windowHeight = customWindowHeight = defaultWindowHeight;
		windowMaximized = customWindowMaximized = defaultWindowMaximized;
		autoloadAll = customAutoloadAll = defaultAutoloadAll;
		fileExtension = customFileExtension = defaultFileExtension;
		fileSelect = customFileSelect = defaultFileSelect;
	}

	public String getWindowTitle ( ) {
		return windowTitle;
	}

	public String getCustomWindowTitle ( ) {
		return customWindowTitle;
	}

	public void setWindowTitle ( final String windowTitle ) {
		this.windowTitle = windowTitle;
	}

	public void setCustomWindowTitle ( final String windowTitle ) {
		customWindowTitle = windowTitle;
	}

	public boolean isCustomWindowTitleAvailable ( ) {
		return customWindowTitle != null;
	}

	public double getWindowWidth ( ) {
		return windowWidth;
	}

	public double getCustomWindowWidth ( ) {
		return customWindowWidth;
	}

	public void setWindowWidth ( final double windowWidth ) {
		this.windowWidth = windowWidth;
	}

	public void setCustomWindowWidth ( final double windowWidth ) {
		customWindowWidth = windowWidth;
	}

	public boolean isCustomWindowWidthAvailable ( ) {
		return customWindowWidth > 0.0d;
	}

	public double getWindowHeight ( ) {
		return windowHeight;
	}

	public double getCustomWindowHeight ( ) {
		return customWindowHeight;
	}

	public void setWindowHeight ( final double windowHeight ) {
		this.windowHeight = windowHeight;
	}

	public void setCustomWindowHeight ( final double windowHeight ) {
		customWindowWidth = windowHeight;
	}

	public boolean isCustomWindowHeightAvailable ( ) {
		return customWindowHeight > 0.0d;
	}

	public boolean isWindowMaximized ( ) {
		return windowMaximized > 0;
	}

	public boolean isCustomWindowMaximized ( ) {
		return customWindowMaximized > 0;
	}

	public void isWindowMaximized ( final boolean windowMaximized ) {
		this.windowMaximized = windowMaximized ? 1 : 0;
	}

	public void setCustomWindowMaximized ( final boolean windowMaximized ) {
		customWindowMaximized = windowMaximized ? 1 : 0;
	}

	public boolean isCustomWindowMaximizedAvailable ( ) {
		return customWindowMaximized >= 0;
	}

	public boolean isAutoloadAll ( ) {
		return autoloadAll > 0;
	}

	public boolean isCustomAutoloadAll ( ) {
		return customAutoloadAll > 0;
	}

	public void setAutoloadAll ( final boolean autoloadAll ) {
		this.autoloadAll = autoloadAll ? 1 : 0;
	}

	public void setCustomAutoloadAll ( final boolean autoloadAll ) {
		customAutoloadAll = autoloadAll ? 1 : 0;
	}

	public boolean isCustomAutoloadAllAvailable ( ) {
		return customAutoloadAll >= 0;
	}

	public String getUrlsFileExtension ( ) {
		return fileExtension;
	}

	public String getCustomUrlsFileExtension ( ) {
		return customFileExtension;
	}

	public void setUrlsFileExtension ( final String urlsFileExtension ) {
		this.fileExtension = urlsFileExtension;
	}

	public void setCustomUrlsFileExtension ( final String urlsFileExtension ) {
		customFileExtension = urlsFileExtension;
	}

	public boolean isCustomUrlsFileExtensionAvailable ( ) {
		return customFileExtension != null;
	}

	public String getUrlsFileSelect ( ) {
		return fileSelect;
	}

	public String getCustomUrlsFileSelect ( ) {
		return customFileSelect;
	}

	public void setUrlsFileSelect ( final String urlsFileSelect ) {
		this.fileSelect = urlsFileSelect;
	}

	public void setCustomUrlsFileSelect ( final String urlsFielSelect ) {
		customFileSelect = urlsFielSelect;
	}

	public boolean isCustomUrlsFileSelectAvailable ( ) {
		return customFileSelect != null;
	}

	public boolean isSearchByPrefix ( ) {
		return searchByPrefix > 0;
	}

	public boolean isCustomSearchByPrefix ( ) {
		return customSearchByPrefix > 0;
	}

	public void setSearchByPrefix ( final boolean searchByPrefix ) {
		this.searchByPrefix = searchByPrefix ? 1 : 0;
	}

	public void setCustomSearchByPrefix ( final boolean searchByPrefix ) {
		customSearchByPrefix = searchByPrefix ? 1 : 0;
	}

	public boolean isCustomSearchByPrefixAvailable ( ) {
		return customSearchByPrefix >= 0;
	}

}

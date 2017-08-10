
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.settings;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.resources.Resources;
import com.github.vbsw.urlsaver.utility.FormattedTime;
import com.github.vbsw.urlsaver.utility.Jar;


/**
 * @author Vitali Baumtrok
 */
public final class Settings {

	private boolean customLoaded;

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

	public final SettingsViewModel vm = new SettingsViewModel();

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

		customLoaded = properties.isLoaded();
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

	public boolean isCustomLoaded ( ) {
		return customLoaded;
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
		customWindowHeight = windowHeight;
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

	public void setWindowMaximized ( final boolean windowMaximized ) {
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

	public void updateView ( ) {
		App.scene.tf.title.setText(getCustomWindowTitle());
		App.scene.tf.width.setText(Integer.toString((int) getCustomWindowWidth()));
		App.scene.tf.height.setText(Integer.toString((int) getCustomWindowHeight()));
		App.scene.tf.fileExtension.setText(getCustomUrlsFileExtension());
		App.scene.tf.defaultFile.setText(getCustomUrlsFileSelect());
		App.scene.cb.maximize.setSelected(isCustomWindowMaximized());
		App.scene.cb.loadAtStart.setSelected(isCustomAutoloadAll());
		App.scene.cb.byPrefix.setSelected(isCustomSearchByPrefix());

		setFieldsDisabled(!customLoaded);
	}

	public void setFieldsDisabled ( final boolean disabled ) {
		App.scene.tf.title.setDisable(disabled);
		App.scene.tf.width.setDisable(disabled);
		App.scene.tf.height.setDisable(disabled);
		App.scene.tf.fileExtension.setDisable(disabled);
		App.scene.tf.defaultFile.setDisable(disabled);
		App.scene.cb.maximize.setDisable(disabled);
		App.scene.cb.loadAtStart.setDisable(disabled);
		App.scene.cb.byPrefix.setDisable(disabled);
	}

	public void logSuccess ( final String message ) {
		log("Success: ",message);
	}

	public void logFailure ( final String message ) {
		log("Failure: ",message);
	}

	public boolean saveCustom ( ) {
		final StringBuilder stringBuilder = new StringBuilder(400);
		final String newLine = "\r\n";

		stringBuilder.append("window.title=");
		stringBuilder.append(customWindowTitle);
		stringBuilder.append(newLine);
		stringBuilder.append("window.width=");
		stringBuilder.append(Integer.toString((int) customWindowWidth));
		stringBuilder.append(newLine);
		stringBuilder.append("window.height=");
		stringBuilder.append(Integer.toString((int) customWindowHeight));
		stringBuilder.append(newLine);
		stringBuilder.append("window.maximized=");
		stringBuilder.append(Boolean.toString(customWindowMaximized == 1));
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.extension=");
		stringBuilder.append(customFileExtension);
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.select=");
		stringBuilder.append(customFileSelect);
		stringBuilder.append(newLine);
		stringBuilder.append("autoload.all=");
		stringBuilder.append(Boolean.toString(customAutoloadAll == 1));
		stringBuilder.append(newLine);
		stringBuilder.append("search.by.prefix=");
		stringBuilder.append(Boolean.toString(customSearchByPrefix == 1));
		stringBuilder.append(newLine);

		try {
			final byte[] bytes = stringBuilder.toString().getBytes(Resources.ENCODING);
			final Path path = Jar.getPath().resolve(Resources.CUSTOM_SETTINGS_FILE_PATH);

			Files.write(path,bytes);

			return true;

		} catch ( final IOException e ) {
			e.printStackTrace();

			return false;
		}
	}

	private void log ( final String prefix, final String message ) {
		final String timeStr = FormattedTime.get();

		if ( App.scene.ta.log.getText().length() > 0 ) {
			App.scene.ta.log.appendText("\n");
		}
		App.scene.ta.log.appendText(timeStr);
		App.scene.ta.log.appendText(" ");
		App.scene.ta.log.appendText(prefix);
		App.scene.ta.log.appendText(message);
	}

}

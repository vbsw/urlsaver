/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.settings;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.Settings;
import com.github.vbsw.urlsaver.api.Resource;
import com.github.vbsw.urlsaver.api.ResourceLoader;
import com.github.vbsw.urlsaver.api.ResourceVariants;
import com.github.vbsw.urlsaver.args.ArgumentsConfig;
import com.github.vbsw.urlsaver.utility.Parser;


/**
 * @author Vitali Baumtrok
 */
public class StdSettings extends Settings {

	protected final ResourceVariants settingsVariants = new ResourceVariants();
	protected final ResourceVariants fxmlVariants = new ResourceVariants();
	protected final ResourceVariants cssVariants = new ResourceVariants();

	protected final StringSetting windowTitle = new StringSetting();
	protected final IntSetting windowWidth = new IntSetting();
	protected final IntSetting windowHeight = new IntSetting();
	protected final BooleanSetting windowMaximized = new BooleanSetting();
	protected final StringSetting urlsFileExtension = new StringSetting();
	protected final StringSetting urlsFileSelect = new StringSetting();
	protected final BooleanSetting urlsFileAutoLoadAll = new BooleanSetting();
	protected final BooleanSetting searchByPrefix = new BooleanSetting();

	protected boolean customSettingsLoaded = false;
	protected boolean customSettingsSaved = true;
	protected boolean customFXMLLoaded = false;
	protected boolean customCSSLoaded = false;

	protected Path extractCustomSettingsPath ( final List<String> args ) {
		if ( args.size() > 0 ) {
			final String arg = args.get(0);
			final String pathStrBare = Parser.getArgumentValue(arg,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR);
			final String pathStr = Parser.trim(pathStrBare);
			if ( pathStr.length() > 0 ) {
				final Path path = Paths.get(pathStr);
				final Path dir = path.getParent();
				if ( dir != null && Files.exists(dir) )
					return path;
				else
					System.out.println("warning: settings path does not exist");
			}
		}
		return null;
	}

	protected static Path extractCustomFXMLPath ( final List<String> args ) {
		// not implemented, yet
		return null;
	}

	protected static Path extractCustomCSSPath ( final List<String> args ) {
		// not implemented, yet
		return null;
	}

	protected void setSettingsSavedValue ( final ResourceLoader resourceLoader, final List<String> args, final String filePathStr ) {
		final Path customPath = extractCustomSettingsPath(args);
		final Resource resource;
		if ( customPath == null )
			resource = resourceLoader.getProgramFile().getOSFileResource(Paths.get(filePathStr));
		else
			resource = resourceLoader.getProgramFile().getOSFileResource(customPath);
		settingsVariants.setSaved(resource);
	}

	protected void setFXMLSavedValue ( final ResourceLoader resourceLoader, final List<String> args, final String filePathStr ) {
		final Path customPath = extractCustomFXMLPath(args);
		final Resource resource;
		if ( customPath == null )
			resource = resourceLoader.getProgramFile().getOSFileResource(Paths.get(filePathStr));
		else
			resource = resourceLoader.getProgramFile().getOSFileResource(customPath);
		fxmlVariants.setSaved(resource);
	}

	protected void setCSSSavedValue ( final ResourceLoader resourceLoader, final List<String> args, final String filePathStr ) {
		final Path customPath = extractCustomCSSPath(args);
		final Resource resource;
		if ( customPath == null )
			resource = resourceLoader.getProgramFile().getOSFileResource(Paths.get(filePathStr));
		else
			resource = resourceLoader.getProgramFile().getOSFileResource(customPath);
		cssVariants.setSaved(resource);
	}

	protected void loadDefaultSettings ( ) {
		try ( final InputStream stream = getSettings().getDefault().newInputStream() ) {
			final Properties properties = new Properties();
			properties.load(stream);
			windowTitle.setDefault(SettingsReader.getWindowTitle(properties));
			windowWidth.setDefault(SettingsReader.getWindowWidth(properties));
			windowHeight.setDefault(SettingsReader.getWindowHeight(properties));
			windowMaximized.setDefault(SettingsReader.getWindowMaximized(properties));
			urlsFileExtension.setDefault(SettingsReader.getURLsFileExtension(properties));
			urlsFileSelect.setDefault(SettingsReader.getURLsFileSelect(properties));
			urlsFileAutoLoadAll.setDefault(SettingsReader.getURLsFileAutoLoadAll(properties));
			searchByPrefix.setDefault(SettingsReader.getSearchByPrefix(properties));
		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadCustomSettings ( ) {
		customSettingsLoaded = false;
		if ( getSettings().getSaved().exists() ) {
			try ( final InputStream stream = getSettings().getSaved().newInputStream() ) {
				final Properties properties = new Properties();
				properties.load(stream);
				windowTitle.setSaved(SettingsReader.getWindowTitle(properties,windowTitle.getDefault()));
				windowWidth.setSaved(SettingsReader.getWindowWidth(properties,windowWidth.getDefault()));
				windowHeight.setSaved(SettingsReader.getWindowHeight(properties,windowHeight.getDefault()));
				windowMaximized.setSaved(SettingsReader.getWindowMaximized(properties,windowMaximized.getDefault()));
				urlsFileExtension.setSaved(SettingsReader.getURLsFileExtension(properties,urlsFileExtension.getDefault()));
				urlsFileSelect.setSaved(SettingsReader.getURLsFileSelect(properties,urlsFileSelect.getDefault()));
				urlsFileAutoLoadAll.setSaved(SettingsReader.getURLsFileAutoLoadAll(properties,urlsFileAutoLoadAll.getDefault()));
				searchByPrefix.setSaved(SettingsReader.getSearchByPrefix(properties,searchByPrefix.getDefault()));
				customSettingsLoaded = true;
			} catch ( final Exception e ) {
				e.printStackTrace();
			}
		}
		if ( !isCustomSettingsLoaded() )
			resetSavedToDefault();
		resetModifiedValuesToSaved();
	}

	@Override
	public void initialize ( ) {
		final Resource prefDefaultRes = Global.resourceLoader.getProgramFile().getJarFileResource(Global.resourceLoader.getDefaultSettingsFilePath());
		final Resource fxmlDefaultRes = Global.resourceLoader.getProgramFile().getJarFileResource(Global.resourceLoader.getDefaultFXMLFilePath());
		final Resource cssDefaultRes = Global.resourceLoader.getProgramFile().getJarFileResource(Global.resourceLoader.getDefaultCSSFilePath());

		settingsVariants.setDefault(prefDefaultRes);
		fxmlVariants.setDefault(fxmlDefaultRes);
		cssVariants.setDefault(cssDefaultRes);
		setSettingsSavedValue(Global.resourceLoader,Global.arguments,Global.resourceLoader.getCustomSettingsFilePath());
		setFXMLSavedValue(Global.resourceLoader,Global.arguments,Global.resourceLoader.getCustomFXMLFilePath());
		setCSSSavedValue(Global.resourceLoader,Global.arguments,Global.resourceLoader.getCustomCSSFilePath());

		loadDefaultSettings();
		loadCustomSettings();
	}

	@Override
	public ResourceVariants getSettings ( ) {
		return settingsVariants;
	}

	@Override
	public ResourceVariants getFXML ( ) {
		return fxmlVariants;
	}

	@Override
	public ResourceVariants getCSS ( ) {
		return cssVariants;
	}

	@Override
	public void resetSavedToDefault ( ) {
		windowTitle.resetSavedToDefault();
		windowWidth.resetSavedToDefault();
		windowHeight.resetSavedToDefault();
		windowMaximized.resetSavedToDefault();
		urlsFileExtension.resetSavedToDefault();
		urlsFileSelect.resetSavedToDefault();
		urlsFileAutoLoadAll.resetSavedToDefault();
		searchByPrefix.resetSavedToDefault();
	}

	@Override
	public void resetSavedToModified ( ) {
		windowTitle.resetSavedToModified();
		windowWidth.resetSavedToModified();
		windowHeight.resetSavedToModified();
		windowMaximized.resetSavedToModified();
		urlsFileExtension.resetSavedToModified();
		urlsFileSelect.resetSavedToModified();
		urlsFileAutoLoadAll.resetSavedToModified();
		searchByPrefix.resetSavedToModified();
	}

	@Override
	public void resetModifiedValuesToSaved ( ) {
		windowTitle.resetModifiedToSaved();
		windowWidth.resetModifiedValueToSaved();
		windowHeight.resetModifiedValueToSaved();
		windowMaximized.resetModifiedToSaved();
		urlsFileExtension.resetModifiedToSaved();
		urlsFileSelect.resetModifiedToSaved();
		urlsFileAutoLoadAll.resetModifiedToSaved();
		searchByPrefix.resetModifiedToSaved();
	}

	@Override
	public StringSetting getStringPereference ( final int propertyId ) {
		switch ( propertyId ) {
			case SettingsConfig.WINDOW_TITLE_ID:
			return windowTitle;
			case SettingsConfig.URLS_FILE_EXTENSION_ID:
			return urlsFileExtension;
			case SettingsConfig.URLS_FILE_SELECT_ID:
			return urlsFileSelect;
			default:
			return null;
		}
	}

	@Override
	public IntSetting getIntSetting ( final int propertyId ) {
		switch ( propertyId ) {
			case SettingsConfig.WINDOW_WIDTH_ID:
			return windowWidth;
			case SettingsConfig.WINDOW_HEIGHT_ID:
			return windowHeight;
			default:
			return null;
		}
	}

	@Override
	public BooleanSetting getBooleanSetting ( final int propertyId ) {
		switch ( propertyId ) {
			case SettingsConfig.WINDOW_MAXIMIZED_ID:
			return windowMaximized;
			case SettingsConfig.URLS_FILE_AUTOLOAD_ALL_ID:
			return urlsFileAutoLoadAll;
			case SettingsConfig.SEARCH_BY_PREFIX_ID:
			return searchByPrefix;
			default:
			return null;
		}
	}

	@Override
	public boolean isCustomSettingsLoaded ( ) {
		return customSettingsLoaded;
	}

	@Override
	public boolean isCustomSettingsSaved ( ) {
		return customSettingsSaved;
	}

	@Override
	public boolean isCustomFXMLLoaded ( ) {
		return customFXMLLoaded;
	}

	@Override
	public void setCustomFXMLLoaded ( final boolean loaded ) {
		customFXMLLoaded = loaded;
	}

	@Override
	public boolean isCustomCSSLoaded ( ) {
		return customCSSLoaded;
	}

	@Override
	public void setCustomCSSLoaded ( final boolean loaded ) {
		customCSSLoaded = loaded;
	}

	@Override
	public void saveSettings ( ) {
		final StringBuilder stringBuilder = new StringBuilder(400);
		final String newLine = System.lineSeparator();

		stringBuilder.append("window.title=");
		stringBuilder.append(Parser.trim(windowTitle.getModified()));
		stringBuilder.append(newLine);
		stringBuilder.append("window.width=");
		stringBuilder.append(Integer.toString(windowWidth.getModified()));
		stringBuilder.append(newLine);
		stringBuilder.append("window.height=");
		stringBuilder.append(Integer.toString(windowHeight.getModified()));
		stringBuilder.append(newLine);
		stringBuilder.append("window.maximized=");
		stringBuilder.append(Boolean.toString(windowMaximized.getModified()));
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.extension=");
		stringBuilder.append(Parser.trim(urlsFileExtension.getModified()));
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.select=");
		stringBuilder.append(Parser.trim(urlsFileSelect.getModified()));
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.autoload.all=");
		stringBuilder.append(Boolean.toString(urlsFileAutoLoadAll.getModified()));
		stringBuilder.append(newLine);
		stringBuilder.append("search.byprefix=");
		stringBuilder.append(Boolean.toString(searchByPrefix.getModified()));
		stringBuilder.append(newLine);

		try {
			final byte[] bytes = stringBuilder.toString().getBytes(Global.resourceLoader.getCharset());
			final Path path = getSettings().getSaved().getPath();
			Files.write(path,bytes);
			customSettingsSaved = true;

		} catch ( final IOException e ) {
			customSettingsSaved = false;
			e.printStackTrace();
		}
	}

}

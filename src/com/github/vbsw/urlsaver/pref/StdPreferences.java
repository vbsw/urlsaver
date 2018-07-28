/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.pref;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.Preferences;
import com.github.vbsw.urlsaver.api.Resource;
import com.github.vbsw.urlsaver.api.ResourceLoader;
import com.github.vbsw.urlsaver.api.ResourceVariants;
import com.github.vbsw.urlsaver.args.ArgumentsConfig;
import com.github.vbsw.urlsaver.args.ArgumentsParser;
import com.github.vbsw.urlsaver.resources.ResourcesConfig;
import com.github.vbsw.urlsaver.utility.Parser;


/**
 * @author Vitali Baumtrok
 */
public class StdPreferences extends Preferences {

	protected final ResourceVariants preferencesVariants = new ResourceVariants();
	protected final ResourceVariants fxmlVariants = new ResourceVariants();
	protected final ResourceVariants cssVariants = new ResourceVariants();

	protected final PreferencesStringValue windowTitle = new PreferencesStringValue();
	protected final PreferencesIntValue windowWidth = new PreferencesIntValue();
	protected final PreferencesIntValue windowHeight = new PreferencesIntValue();
	protected final PreferencesBooleanValue windowMaximized = new PreferencesBooleanValue();
	protected final PreferencesStringValue urlsFileExtension = new PreferencesStringValue();
	protected final PreferencesStringValue urlsFileSelect = new PreferencesStringValue();
	protected final PreferencesBooleanValue urlsFileAutoLoadAll = new PreferencesBooleanValue();
	protected final PreferencesBooleanValue searchByPrefix = new PreferencesBooleanValue();

	protected ResourceLoader resourceLoader;

	protected boolean customPreferencesLoaded = false;
	protected boolean customPreferencesSaved = true;
	protected boolean customFXMLLoaded = false;
	protected boolean customCSSLoaded = false;

	protected Path extractCustomPreferencesPath ( final List<String> args ) {
		if ( args.size() > 0 ) {
			final String arg = args.get(0);
			final String pathStrBare = ArgumentsParser.getValue(arg,ArgumentsConfig.PREFERENCES_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR);
			final String pathStr = Parser.trim(pathStrBare);
			if ( pathStr.length() > 0 ) {
				final Path path = Paths.get(pathStr);
				final Path dir = path.getParent();
				if ( dir != null && Files.exists(dir) )
					return path;
				else
					System.out.println("warning: preferences path does not exist");
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

	protected void setPreferencesSavedValue ( final ResourceLoader resourceLoader, final List<String> args ) {
		final Path customPath = extractCustomPreferencesPath(args);
		final Resource resource;
		if ( customPath == null )
			resource = resourceLoader.getLaunchSource().getJarFileResource(ResourcesConfig.CUSTOM_PREFERENCES_FILE_PATH);
		else
			resource = resourceLoader.getLaunchSource().getOSFileResource(customPath);
		preferencesVariants.setSaved(resource);
	}

	protected void setFXMLSavedValue ( final ResourceLoader resourceLoader, final List<String> args ) {
		final Path customPath = extractCustomFXMLPath(args);
		final Resource resource;
		if ( customPath == null )
			resource = resourceLoader.getLaunchSource().getJarFileResource(ResourcesConfig.CUSTOM_FXML_FILE_PATH);
		else
			resource = resourceLoader.getLaunchSource().getOSFileResource(customPath);
		fxmlVariants.setSaved(resource);
	}

	protected void setCSSSavedValue ( final ResourceLoader resourceLoader, final List<String> args ) {
		final Path customPath = extractCustomCSSPath(args);
		final Resource resource;
		if ( customPath == null )
			resource = resourceLoader.getLaunchSource().getJarFileResource(ResourcesConfig.CUSTOM_CSS_FILE_PATH);
		else
			resource = resourceLoader.getLaunchSource().getOSFileResource(customPath);
		cssVariants.setSaved(resource);
	}

	protected void loadDefaultPreferences ( ) {
		try ( final InputStream stream = getPreferences().getDefault().newInputStream() ) {
			final Properties properties = new Properties();
			properties.load(stream);
			windowTitle.setDefault(PropertiesReader.getWindowTitle(properties));
			windowWidth.setDefault(PropertiesReader.getWindowWidth(properties));
			windowHeight.setDefault(PropertiesReader.getWindowHeight(properties));
			windowMaximized.setDefault(PropertiesReader.getWindowMaximized(properties));
			urlsFileExtension.setDefault(PropertiesReader.getURLsFileExtension(properties));
			urlsFileSelect.setDefault(PropertiesReader.getURLsFileSelect(properties));
			urlsFileAutoLoadAll.setDefault(PropertiesReader.getURLsFileAutoLoadAll(properties));
			searchByPrefix.setDefault(PropertiesReader.getSearchByPrefix(properties));
		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void loadCustomPreferences ( ) {
		customPreferencesLoaded = false;
		if ( getPreferences().getSaved().exists() ) {
			try ( final InputStream stream = getPreferences().getSaved().newInputStream() ) {
				final Properties properties = new Properties();
				properties.load(stream);
				windowTitle.setSaved(PropertiesReader.getWindowTitle(properties,windowTitle.getDefault()));
				windowWidth.setSaved(PropertiesReader.getWindowWidth(properties,windowWidth.getDefault()));
				windowHeight.setSaved(PropertiesReader.getWindowHeight(properties,windowHeight.getDefault()));
				windowMaximized.setSaved(PropertiesReader.getWindowMaximized(properties,windowMaximized.getDefault()));
				urlsFileExtension.setSaved(PropertiesReader.getURLsFileExtension(properties,urlsFileExtension.getDefault()));
				urlsFileSelect.setSaved(PropertiesReader.getURLsFileSelect(properties,urlsFileSelect.getDefault()));
				urlsFileAutoLoadAll.setSaved(PropertiesReader.getURLsFileAutoLoadAll(properties,urlsFileAutoLoadAll.getDefault()));
				searchByPrefix.setSaved(PropertiesReader.getSearchByPrefix(properties,searchByPrefix.getDefault()));
				customPreferencesLoaded = true;
			} catch ( final Exception e ) {
				e.printStackTrace();
			}
		}
		if ( !isCustomPreferencesLoaded() )
			resetSavedToDefault();
		resetModifiedValuesToSaved();
	}

	@Override
	public void initialize ( final Global global ) {
		this.resourceLoader = global.getResourceLoader();

		final Resource prefDefaultRes = resourceLoader.getLaunchSource().getJarFileResource(ResourcesConfig.DEFAULT_PREFERENCES_FILE_PATH);
		final Resource fxmlDefaultRes = resourceLoader.getLaunchSource().getJarFileResource(ResourcesConfig.DEFAULT_FXML_FILE_PATH);
		final Resource cssDefaultRes = resourceLoader.getLaunchSource().getJarFileResource(ResourcesConfig.DEFAULT_CSS_FILE_PATH);

		preferencesVariants.setDefault(prefDefaultRes);
		fxmlVariants.setDefault(fxmlDefaultRes);
		cssVariants.setDefault(cssDefaultRes);
		setPreferencesSavedValue(resourceLoader,global.getArguments());
		setFXMLSavedValue(resourceLoader,global.getArguments());
		setCSSSavedValue(resourceLoader,global.getArguments());

		loadDefaultPreferences();
		loadCustomPreferences();
	}

	@Override
	public ResourceVariants getPreferences ( ) {
		return preferencesVariants;
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
	public PreferencesStringValue getStringValue ( final int id ) {
		switch ( id ) {
			case PreferencesConfig.WINDOW_TITLE_ID:
			return windowTitle;
			case PreferencesConfig.URLS_FILE_EXTENSION_ID:
			return urlsFileExtension;
			case PreferencesConfig.URLS_FILE_SELECT_ID:
			return urlsFileSelect;
			default:
			return null;
		}
	}

	@Override
	public PreferencesIntValue getIntValue ( final int id ) {
		switch ( id ) {
			case PreferencesConfig.WINDOW_WIDTH_ID:
			return windowWidth;
			case PreferencesConfig.WINDOW_HEIGHT_ID:
			return windowHeight;
			default:
			return null;
		}
	}

	@Override
	public PreferencesBooleanValue getBooleanValue ( final int id ) {
		switch ( id ) {
			case PreferencesConfig.WINDOW_MAXIMIZED_ID:
			return windowMaximized;
			case PreferencesConfig.URLS_FILE_AUTOLOAD_ALL_ID:
			return urlsFileAutoLoadAll;
			case PreferencesConfig.SEARCH_BY_PREFIX_ID:
			return searchByPrefix;
			default:
			return null;
		}
	}

	@Override
	public boolean isCustomPreferencesLoaded ( ) {
		return customPreferencesLoaded;
	}

	@Override
	public boolean isCustomPreferencesSaved ( ) {
		return customPreferencesSaved;
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
	public void savePreferences ( ) {
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
			final byte[] bytes = stringBuilder.toString().getBytes(resourceLoader.getCharset());
			final Path path = getPreferences().getSaved().getPath();
			Files.write(path,bytes);
			customPreferencesSaved = true;

		} catch ( final IOException e ) {
			customPreferencesSaved = false;
			e.printStackTrace();
		}
	}

}

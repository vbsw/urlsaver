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

import com.github.vbsw.urlsaver.JarFile;
import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.args.ArgumentsConfig;
import com.github.vbsw.urlsaver.args.ArgumentsParser;
import com.github.vbsw.urlsaver.resources.ResourcesConfig;


/**
 * @author Vitali Baumtrok
 */
public class Preferences {

	private static final PreferencesStringValue windowTitle = new PreferencesStringValue();
	private static final PreferencesIntValue windowWidth = new PreferencesIntValue();
	private static final PreferencesIntValue windowHeight = new PreferencesIntValue();
	private static final PreferencesBooleanValue windowMaximized = new PreferencesBooleanValue();
	private static final PreferencesStringValue urlsFileExtension = new PreferencesStringValue();
	private static final PreferencesStringValue urlsFileMain = new PreferencesStringValue();
	private static final PreferencesBooleanValue urlsFileAutoLoad = new PreferencesBooleanValue();
	private static final PreferencesBooleanValue searchByPrefix = new PreferencesBooleanValue();
	private static final PreferencesPathValue preferencesPath = new PreferencesPathValue();
	private static final PreferencesPathValue fxmlPath = new PreferencesPathValue();
	private static final PreferencesPathValue cssPath = new PreferencesPathValue();

	private static boolean customPreferencesLoaded = false;
	private static boolean customFXMLLoaded = false;
	private static boolean customCSSLoaded = false;

	private static boolean customPreferencesSaved = true;

	public static void initialize ( final List<String> args ) {
		Preferences.loadDefaultPreferencesPath();
		Preferences.loadDefaultFXMLPath();
		Preferences.loadDefaultCSSPath();
		Preferences.loadDefaultPreferences();

		Preferences.loadCustomPreferencesPath(args);
		Preferences.loadCustomFXMLPath(args);
		Preferences.loadCustomCSSPath(args);
		Preferences.loadCustomPreferences();

		if ( !Preferences.isCustomPreferencesLoaded() ) {
			Preferences.resetCustomValuesToDefault();
			Preferences.resetSavedValuesToCustom();
		}
	}

	public static PreferencesStringValue getWindowTitle ( ) {
		return windowTitle;
	}

	public static PreferencesIntValue getWindowWidth ( ) {
		return windowWidth;
	}

	public static PreferencesIntValue getWindowHeight ( ) {
		return windowHeight;
	}

	public static PreferencesBooleanValue getWindowMaximized ( ) {
		return windowMaximized;
	}

	public static PreferencesStringValue getURLsFileExtension ( ) {
		return urlsFileExtension;
	}

	public static PreferencesStringValue getURLsFileMain ( ) {
		return urlsFileMain;
	}

	public static PreferencesBooleanValue getURLsFileAutoload ( ) {
		return urlsFileAutoLoad;
	}

	public static PreferencesBooleanValue getSearchByPrefix ( ) {
		return searchByPrefix;
	}

	public static boolean isCustomPreferencesLoaded ( ) {
		return customPreferencesLoaded;
	}

	public static boolean isCustomPreferencesSaved ( ) {
		return customPreferencesSaved;
	}

	public static boolean isCustomFXMLLoaded ( ) {
		return customFXMLLoaded;
	}

	public static void setCustomFXMLLoaded ( final boolean loaded ) {
		customFXMLLoaded = loaded;
	}

	public static boolean isCustomCSSLoaded ( ) {
		return customCSSLoaded;
	}

	public static void setCustomCSSLoaded ( final boolean loaded ) {
		customCSSLoaded = loaded;
	}

	public static PreferencesPathValue getPreferencesPath ( ) {
		return preferencesPath;
	}

	public static PreferencesPathValue getFXMLPath ( ) {
		return fxmlPath;
	}

	public static PreferencesPathValue getCSSPath ( ) {
		return cssPath;
	}

	public static boolean isCustomPreferencesFileAvailable ( ) {
		final Path filePath = getPreferencesPath().getCustomValue();
		final boolean available = filePath != null && Files.exists(filePath);
		return available;
	}

	public static boolean isCustomFXMLFileAvailable ( ) {
		final Path filePath = getFXMLPath().getCustomValue();
		final boolean available = filePath != null && Files.exists(filePath);
		return available;
	}

	public static boolean isCustomCSSFileAvailable ( ) {
		final Path filePath = getCSSPath().getCustomValue();
		final boolean available = filePath != null && Files.exists(filePath);
		return available;
	}

	public static void loadDefaultPreferences ( ) {
		try ( final InputStream stream = Files.newInputStream(Preferences.getPreferencesPath().getDefaultValue()) ) {
			final Properties properties = new Properties();
			properties.load(stream);
			windowTitle.setDefaultValue(PropertiesReader.getWindowTitle(properties));
			windowWidth.setDefaultValue(PropertiesReader.getWindowWidth(properties));
			windowHeight.setDefaultValue(PropertiesReader.getWindowHeight(properties));
			windowMaximized.setDefaultValue(PropertiesReader.getWindowMaximized(properties));
			urlsFileExtension.setDefaultValue(PropertiesReader.getURLsFileExtension(properties));
			urlsFileMain.setDefaultValue(PropertiesReader.getURLsFileMain(properties));
			urlsFileAutoLoad.setDefaultValue(PropertiesReader.getURLsFileAutoLoad(properties));
			searchByPrefix.setDefaultValue(PropertiesReader.getSearchByPrefix(properties));

		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

	public static void loadCustomPreferences ( ) {
		customPreferencesLoaded = false;

		if ( Preferences.isCustomPreferencesFileAvailable() ) {
			try ( final InputStream stream = Files.newInputStream(Preferences.getPreferencesPath().getCustomValue()) ) {
				final Properties properties = new Properties();
				properties.load(stream);
				windowTitle.setCustomValue(PropertiesReader.getWindowTitle(properties,windowTitle.getDefaultValue()));
				windowWidth.setCustomValue(PropertiesReader.getWindowWidth(properties,windowWidth.getDefaultValue()));
				windowHeight.setCustomValue(PropertiesReader.getWindowHeight(properties,windowHeight.getDefaultValue()));
				windowMaximized.setCustomValue(PropertiesReader.getWindowMaximized(properties,windowMaximized.getDefaultValue()));
				urlsFileExtension.setCustomValue(PropertiesReader.getURLsFileExtension(properties,urlsFileExtension.getDefaultValue()));
				urlsFileMain.setCustomValue(PropertiesReader.getURLsFileMain(properties,urlsFileMain.getDefaultValue()));
				urlsFileAutoLoad.setCustomValue(PropertiesReader.getURLsFileAutoLoad(properties,urlsFileAutoLoad.getDefaultValue()));
				searchByPrefix.setCustomValue(PropertiesReader.getSearchByPrefix(properties,searchByPrefix.getDefaultValue()));
				Preferences.resetSavedValuesToCustom();
				customPreferencesLoaded = true;

			} catch ( final Exception e ) {
				e.printStackTrace();
			}
		}
	}

	public static void resetCustomValuesToDefault ( ) {
		windowWidth.resetCustomValueToDefault();
		windowHeight.resetCustomValueToDefault();
		windowTitle.resetCustomValueToDefault();
		urlsFileExtension.resetCustomValueToDefault();
	}

	public static void resetSavedValuesToCustom ( ) {
		windowWidth.resetSavedValueToCustom();
		windowHeight.resetSavedValueToCustom();
		windowTitle.resetSavedValueToCustom();
		urlsFileExtension.resetSavedValueToCustom();
	}

	public static void saveValues ( ) {
		final StringBuilder stringBuilder = new StringBuilder(400);
		final String newLine = System.lineSeparator();

		stringBuilder.append("window.title=");
		stringBuilder.append(windowTitle.getCustomValue());
		stringBuilder.append(newLine);
		stringBuilder.append("window.width=");
		stringBuilder.append(Integer.toString(windowWidth.getCustomValue()));
		stringBuilder.append(newLine);
		stringBuilder.append("window.height=");
		stringBuilder.append(Integer.toString(windowHeight.getCustomValue()));
		stringBuilder.append(newLine);
		stringBuilder.append("window.maximized=");
		stringBuilder.append(Boolean.toString(windowMaximized.getCustomValue()));
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.extension=");
		stringBuilder.append(urlsFileExtension.getCustomValue());
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.main=");
		stringBuilder.append(urlsFileMain.getCustomValue());
		stringBuilder.append(newLine);
		stringBuilder.append("autoload.all=");
		stringBuilder.append(Boolean.toString(urlsFileAutoLoad.getCustomValue()));
		stringBuilder.append(newLine);
		stringBuilder.append("search.byprefix=");
		stringBuilder.append(Boolean.toString(searchByPrefix.getCustomValue()));
		stringBuilder.append(newLine);

		try {
			final byte[] bytes = stringBuilder.toString().getBytes(ResourcesConfig.FILE_CHARSET);
			final Path path = JarFile.getPath().resolve(ResourcesConfig.CUSTOM_PREFERENCES_FILE_PATH);
			Files.write(path,bytes);
			customPreferencesSaved = true;

		} catch ( final IOException e ) {
			customPreferencesSaved = false;
			e.printStackTrace();
		}
	}

	private static void loadDefaultPreferencesPath ( ) {
		final Path path = JarFile.getPathToResource(ResourcesConfig.DEFAULT_PREFERENCES_FILE_PATH);
		preferencesPath.setDefaultValue(path);
	}

	private static void loadDefaultFXMLPath ( ) {
		final Path path = JarFile.getPathToResource(ResourcesConfig.DEFAULT_FXML_FILE_PATH);
		fxmlPath.setDefaultValue(path);
	}

	private static void loadDefaultCSSPath ( ) {
		final Path path = JarFile.getPathToResource(ResourcesConfig.DEFAULT_CSS_FILE_PATH);
		cssPath.setDefaultValue(path);
	}

	private static void loadCustomPreferencesPath ( final List<String> args ) {
		Path customPath = extractCustomPreferencesPath(args);
		if ( customPath == null )
			customPath = JarFile.getPath().resolve(ResourcesConfig.CUSTOM_PREFERENCES_FILE_PATH);
		preferencesPath.setCustomValue(customPath);
	}

	private static void loadCustomFXMLPath ( final List<String> args ) {
		Path customPath = extractCustomFXMLPath(args);
		if ( customPath == null )
			customPath = JarFile.getPath().resolve(ResourcesConfig.CUSTOM_FXML_FILE_PATH);
		preferencesPath.setCustomValue(customPath);
	}

	private static void loadCustomCSSPath ( final List<String> args ) {
		Path customPath = extractCustomCSSPath(args);
		if ( customPath == null )
			customPath = JarFile.getPath().resolve(ResourcesConfig.CUSTOM_CSS_FILE_PATH);
		preferencesPath.setCustomValue(customPath);
	}

	private static Path extractCustomPreferencesPath ( final List<String> args ) {
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

	private static Path extractCustomFXMLPath ( final List<String> args ) {
		// TODO: extractCustomFXMLPath
		return null;
	}

	private static Path extractCustomCSSPath ( final List<String> args ) {
		// TODO: extractCustomCSSPath
		return null;
	}

}

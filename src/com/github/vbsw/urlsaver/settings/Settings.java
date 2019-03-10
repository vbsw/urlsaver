/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.settings;


import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.IResource;
import com.github.vbsw.urlsaver.api.ISettings;
import com.github.vbsw.urlsaver.args.ArgumentsConfig;
import com.github.vbsw.urlsaver.resources.ResourcesConfig;
import com.github.vbsw.urlsaver.utility.KeySet;
import com.github.vbsw.urlsaver.utility.Parser;


/**
 * @author Vitali Baumtrok
 */
public class Settings implements ISettings {

	private final KeySet<ISettings.BooleanProperty> booleanProperties = new KeySet<ISettings.BooleanProperty>();
	private final KeySet<ISettings.IntProperty> intProperties = new KeySet<ISettings.IntProperty>();
	private final KeySet<ISettings.StringProperty> stringProperties = new KeySet<ISettings.StringProperty>();

	private String[] appArguments;
	private IResource defaultFXMLResource;
	private IResource defaultCSSResource;
	private IResource defaultSettingsResource;
	private IResource customFXMLResource;
	private IResource customCSSResource;
	private IResource customSettingsResource;

	public Settings ( final String[] appArguments ) {
		this.appArguments = appArguments;
	}

	public void initialize ( ) {
		defaultSettingsResource = new IResource.JarFile(ResourcesConfig.DEFAULT_SETTINGS_FILE_PATH);
		defaultFXMLResource = new IResource.JarFile(ResourcesConfig.DEFAULT_FXML_FILE_PATH);
		defaultCSSResource = new IResource.JarFile(ResourcesConfig.DEFAULT_CSS_FILE_PATH);
		loadDefaultSettings();
		reloadSettingsRessource();
		reloadFXMLRessource();
		reloadCSSRessource();
		reloadSettings();
	}

	@Override
	public Charset getDefaultCharset ( ) {
		return StandardCharsets.UTF_8;
	}

	@Override
	public IntProperty getIntProperty ( final String property ) {
		return intProperties.get(property);
	}

	@Override
	public BooleanProperty getBooleanProperty ( final String property ) {
		return booleanProperties.get(property);
	}

	@Override
	public StringProperty getStringProperty ( final String property ) {
		return stringProperties.get(property);
	}

	@Override
	public String[] getAppArguments ( ) {
		return appArguments;
	}

	@Override
	public void reloadSettingsRessource ( ) {
		Path customSettingsPath = extractCustomSettingsPath();
		if ( customSettingsPath == null ) {
			customSettingsPath = Global.pathProvider.getWorkingDirectory().resolve(ResourcesConfig.CUSTOM_SETTINGS_FILE_PATH);
		}
		customSettingsResource = new IResource.OSFile(customSettingsPath);
	}

	@Override
	public void reloadFXMLRessource ( ) {
		Path customFXMLPath = extractCustomFXMLPath();
		if ( customFXMLPath == null ) {
			customFXMLPath = Global.pathProvider.getWorkingDirectory().resolve(ResourcesConfig.CUSTOM_FXML_FILE_PATH);
		}
		customFXMLResource = new IResource.OSFile(customFXMLPath);
	}

	@Override
	public void reloadCSSRessource ( ) {
		Path customCSSPath = extractCustomCSSPath();
		if ( customCSSPath == null ) {
			customCSSPath = Global.pathProvider.getWorkingDirectory().resolve(ResourcesConfig.CUSTOM_CSS_FILE_PATH);
		}
		customCSSResource = new IResource.OSFile(customCSSPath);
	}

	@Override
	public void saveSettings ( ) {
		final StringBuilder stringBuilder = new StringBuilder(400);
		final String newLine = System.lineSeparator();
		final ISettings.StringProperty windowTitle = getStringProperty(ISettings.Property.windowTitle);
		final ISettings.StringProperty urlsFileExtension = getStringProperty(ISettings.Property.urlsFileExtension);
		final ISettings.StringProperty urlsFileSelect = getStringProperty(ISettings.Property.urlsFileSelect);
		final ISettings.IntProperty windowWidth = getIntProperty(ISettings.Property.windowWidth);
		final ISettings.IntProperty windowHeight = getIntProperty(ISettings.Property.windowHeight);
		final ISettings.BooleanProperty windowMaximized = getBooleanProperty(ISettings.Property.windowMaximized);
		final ISettings.BooleanProperty urlsFileAutoLoadAll = getBooleanProperty(ISettings.Property.urlsFileAutoLoadAll);
		final ISettings.BooleanProperty searchByPrefix = getBooleanProperty(ISettings.Property.searchByPrefix);

		stringBuilder.append("window.title=");
		stringBuilder.append(Parser.trim(windowTitle.modifiedValue));
		stringBuilder.append(newLine);
		stringBuilder.append("window.width=");
		stringBuilder.append(Integer.toString(windowWidth.modifiedValue));
		stringBuilder.append(newLine);
		stringBuilder.append("window.height=");
		stringBuilder.append(Integer.toString(windowHeight.modifiedValue));
		stringBuilder.append(newLine);
		stringBuilder.append("window.maximized=");
		stringBuilder.append(Boolean.toString(windowMaximized.modifiedValue));
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.extension=");
		stringBuilder.append(Parser.trim(urlsFileExtension.modifiedValue));
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.select=");
		stringBuilder.append(Parser.trim(urlsFileSelect.modifiedValue));
		stringBuilder.append(newLine);
		stringBuilder.append("urls.file.autoload.all=");
		stringBuilder.append(Boolean.toString(urlsFileAutoLoadAll.modifiedValue));
		stringBuilder.append(newLine);
		stringBuilder.append("search.byprefix=");
		stringBuilder.append(Boolean.toString(searchByPrefix.modifiedValue));
		stringBuilder.append(newLine);

		try {
			final byte[] bytes = stringBuilder.toString().getBytes(getDefaultCharset());
			final Path path = getSettingsResource().getPath();
			Files.write(path,bytes);
			Global.properties.customSettingsSavedProperty().set(true);

		} catch ( final IOException e ) {
			Global.properties.customSettingsSavedProperty().set(false);
			e.printStackTrace();
		}
	}

	@Override
	public void reloadSettings ( ) {
		boolean customSettingsLoaded = false;
		if ( getSettingsResource().exists() ) {
			try ( final InputStream stream = getSettingsResource().getInputStream() ) {
				final Properties properties = new Properties();
				final ISettings.StringProperty windowTitle = getStringProperty(ISettings.Property.windowTitle);
				final ISettings.StringProperty urlsFileExtension = getStringProperty(ISettings.Property.urlsFileExtension);
				final ISettings.StringProperty urlsFileSelect = getStringProperty(ISettings.Property.urlsFileSelect);
				final ISettings.IntProperty windowWidth = getIntProperty(ISettings.Property.windowWidth);
				final ISettings.IntProperty windowHeight = getIntProperty(ISettings.Property.windowHeight);
				final ISettings.BooleanProperty windowMaximized = getBooleanProperty(ISettings.Property.windowMaximized);
				final ISettings.BooleanProperty urlsFileAutoLoadAll = getBooleanProperty(ISettings.Property.urlsFileAutoLoadAll);
				final ISettings.BooleanProperty searchByPrefix = getBooleanProperty(ISettings.Property.searchByPrefix);
				properties.load(stream);
				windowTitle.savedValue = SettingsReader.getWindowTitle(properties,windowTitle.defaultValue);
				windowWidth.savedValue = SettingsReader.getWindowWidth(properties,windowWidth.defaultValue);
				windowHeight.savedValue = SettingsReader.getWindowHeight(properties,windowHeight.defaultValue);
				windowMaximized.savedValue = SettingsReader.getWindowMaximized(properties,windowMaximized.defaultValue);
				urlsFileExtension.savedValue = SettingsReader.getURLsFileExtension(properties,urlsFileExtension.defaultValue);
				urlsFileSelect.savedValue = SettingsReader.getURLsFileSelect(properties,urlsFileSelect.defaultValue);
				urlsFileAutoLoadAll.savedValue = SettingsReader.getURLsFileAutoLoadAll(properties,urlsFileAutoLoadAll.defaultValue);
				searchByPrefix.savedValue = SettingsReader.getSearchByPrefix(properties,searchByPrefix.defaultValue);
				customSettingsLoaded = true;
			} catch ( final Exception e ) {
				e.printStackTrace();
			}
		}
		if ( !customSettingsLoaded ) {
			setSavedValuesToDefault();
		}
		setModifiedValuesToSaved();
		Global.properties.customSettingsLoadedProperty().set(customSettingsLoaded);
	}

	@Override
	public void setSavedValuesToDefault ( ) {
		for ( ISettings.BooleanProperty property: booleanProperties )
			property.setSavedToDefault();
		for ( ISettings.IntProperty property: intProperties )
			property.setSavedToDefault();
		for ( ISettings.StringProperty property: stringProperties )
			property.setSavedToDefault();
	}

	@Override
	public void setSavedValuesToModified ( ) {
		for ( ISettings.BooleanProperty property: booleanProperties )
			property.setSavedToModified();
		for ( ISettings.IntProperty property: intProperties )
			property.setSavedToModified();
		for ( ISettings.StringProperty property: stringProperties )
			property.setSavedToModified();
	}

	@Override
	public void setModifiedValuesToSaved ( ) {
		for ( ISettings.BooleanProperty property: booleanProperties )
			property.setModifiedToSaved();
		for ( ISettings.IntProperty property: intProperties )
			property.setModifiedToSaved();
		for ( ISettings.StringProperty property: stringProperties )
			property.setModifiedToSaved();
	}

	@Override
	public IResource getDefaultFXMLResource ( ) {
		return defaultFXMLResource;
	}

	@Override
	public IResource getDefaultCSSResource ( ) {
		return defaultCSSResource;
	}

	@Override
	public IResource getDefaultSettingsResource ( ) {
		return defaultSettingsResource;
	}

	@Override
	public IResource getFXMLResource ( ) {
		return customFXMLResource;
	}

	@Override
	public IResource getCSSResource ( ) {
		return customCSSResource;
	}

	@Override
	public IResource getSettingsResource ( ) {
		return customSettingsResource;
	}

	protected Path extractCustomSettingsPath ( ) {
		for ( String arg: appArguments ) {
			final String pathStrBare = Parser.getArgumentValue(arg,ArgumentsConfig.SETTINGS_OPTION,ArgumentsConfig.ASSIGINMENT_OPERATOR);
			final String pathStr = Parser.trim(pathStrBare);
			if ( pathStr.length() > 0 ) {
				try {
					final Path path = Paths.get(pathStr);
					final Path dir = path.getParent();
					if ( dir != null && Files.exists(dir) ) {
						return path;
					} else {
						System.out.println("warning: settings path does not exist");
						return null;
					}
				} catch ( InvalidPathException e ) {
					System.out.println("warning: settings path does not exist");
					return null;
				}
			}
		}
		return null;
	}

	protected Path extractCustomFXMLPath ( ) {
		// not implemented, yet
		return null;
	}

	protected Path extractCustomCSSPath ( ) {
		// not implemented, yet
		return null;
	}

	protected void loadDefaultSettings ( ) {
		try ( final InputStream stream = getDefaultSettingsResource().getInputStream() ) {
			final Properties properties = new Properties();
			final ISettings.StringProperty windowTitle = new ISettings.StringProperty();
			final ISettings.IntProperty windowWidth = new ISettings.IntProperty();
			final ISettings.IntProperty windowHeight = new ISettings.IntProperty();
			final ISettings.BooleanProperty windowMaximized = new ISettings.BooleanProperty();
			final ISettings.StringProperty urlsFileExtension = new ISettings.StringProperty();
			final ISettings.StringProperty urlsFileSelect = new ISettings.StringProperty();
			final ISettings.BooleanProperty urlsFileAutoLoadAll = new ISettings.BooleanProperty();
			final ISettings.BooleanProperty searchByPrefix = new ISettings.BooleanProperty();
			properties.load(stream);
			windowTitle.defaultValue = SettingsReader.getWindowTitle(properties);
			windowWidth.defaultValue = SettingsReader.getWindowWidth(properties);
			windowHeight.defaultValue = SettingsReader.getWindowHeight(properties);
			windowMaximized.defaultValue = SettingsReader.getWindowMaximized(properties);
			urlsFileExtension.defaultValue = SettingsReader.getURLsFileExtension(properties);
			urlsFileSelect.defaultValue = SettingsReader.getURLsFileSelect(properties);
			urlsFileAutoLoadAll.defaultValue = SettingsReader.getURLsFileAutoLoadAll(properties);
			searchByPrefix.defaultValue = SettingsReader.getSearchByPrefix(properties);
			stringProperties.put(ISettings.Property.windowTitle,windowTitle);
			intProperties.put(ISettings.Property.windowWidth,windowWidth);
			intProperties.put(ISettings.Property.windowHeight,windowHeight);
			booleanProperties.put(ISettings.Property.windowMaximized,windowMaximized);
			stringProperties.put(ISettings.Property.urlsFileExtension,urlsFileExtension);
			stringProperties.put(ISettings.Property.urlsFileSelect,urlsFileSelect);
			booleanProperties.put(ISettings.Property.urlsFileAutoLoadAll,urlsFileAutoLoadAll);
			booleanProperties.put(ISettings.Property.searchByPrefix,searchByPrefix);
		} catch ( final Exception e ) {
			e.printStackTrace();
		}
	}

}

/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.URLsIO;
import com.github.vbsw.urlsaver.db.DBTable;
import com.github.vbsw.urlsaver.gui.StdProperties;
import com.github.vbsw.urlsaver.pref.PreferencesConfig;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
public class StdURLsIO extends URLsIO {

	protected final ArrayList<URLsLoadService> urlsLoadServices = new ArrayList<>();

	public void initialize ( ) {
		urlsLoadServices.clear();
		for ( DBTable record: Global.db.getTables() ) {
			final URLsLoadService service = new URLsLoadService(Global.resourceLoader,Global.urlMeta,record);
			final URLsLoadProgressListener progressListener = new URLsLoadProgressListener(record);
			final ServiceFailedListener failedListener = new ServiceFailedListener();
			final FileLoadSucceededListener succeededListener = new FileLoadSucceededListener(record);
			service.progressProperty().addListener(progressListener);
			service.setOnFailed(failedListener);
			service.setOnSucceeded(succeededListener);
			urlsLoadServices.add(service);
		}
	}

	public void autoLoad ( ) {
		if ( Global.preferences.getBooleanValue(PreferencesConfig.URLS_FILE_AUTOLOAD_ALL_ID).getModified() )
			for ( URLsLoadService service: urlsLoadServices )
				service.start();
	}

	public void reloadAllFiles ( ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			service.dbTable.beginLoading();
			if ( Global.db.getSelectedDBTable() == service.dbTable )
				Global.gui.refreshFileSelection();
			service.restart();
		}
	}

	public void cancelLoadingFile ( final DBTable dbTable ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			if ( service.dbTable == dbTable )
				service.cancel();
		}
	}

	public void reloadFile ( final DBTable dbTable ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			if ( service.dbTable == dbTable ) {
				dbTable.beginLoading();
				if ( Global.db.getSelectedDBTable() == dbTable )
					Global.gui.refreshFileSelection();
				service.restart();
			}
		}
	}

	@Override
	public void saveAllFiles ( ) {
		for ( DBTable record: Global.db.getTables() )
			saveFile(record);
	}

	@Override
	public void saveSelectedFile ( ) {
		saveFile(Global.db.getSelectedDBTable());
	}

	public void saveFile ( final DBTable record ) {
		if ( record != null && record.isDirty() ) {
			final Path filePath = record.getPath();
			boolean success = false;
			try ( final BufferedWriter writer = Files.newBufferedWriter(filePath,Global.resourceLoader.getCharset()) ) {
				record.write(writer);
				success = true;
			} catch ( final IOException e ) {
				e.printStackTrace();
			}
			if ( success ) {
				if ( Global.db.getSelectedDBTable() == record ) {
					final StdProperties properties = (StdProperties) Global.properties;
					properties.selectedFileDirtyProperty().setValue(false);
					properties.confirmingSaveProperty().setValue(false);
					Global.gui.refreshFileInfo();
					Global.gui.refreshTitle();
				}
				record.saveComplete();
			}
		}
	}

	private class URLsLoadProgressListener implements ChangeListener<Number> {
		private final DBTable record;

		public URLsLoadProgressListener ( final DBTable record ) {
			this.record = record;
		}

		@Override
		public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
			final int percentLoaded = (int) (newValue.doubleValue() * 100);
			final String listLabel = Global.textGenerator.getFileListLabel(record,percentLoaded);
			record.setListLabel(listLabel);
			Global.gui.refreshFileListView();
			Global.gui.refreshFileInfo();
		}

	}

	private static final class ServiceFailedListener implements EventHandler<WorkerStateEvent> {

		@Override
		public void handle ( final WorkerStateEvent event ) {
			event.getSource().getException().printStackTrace();
		}

	}

	private final class FileLoadSucceededListener implements EventHandler<WorkerStateEvent> {

		private final DBTable record;

		public FileLoadSucceededListener ( final DBTable record ) {
			this.record = record;
		}

		@Override
		public void handle ( final WorkerStateEvent event ) {
			record.endLoading();
			Global.gui.recordLoaded(record);
		}

	}

}

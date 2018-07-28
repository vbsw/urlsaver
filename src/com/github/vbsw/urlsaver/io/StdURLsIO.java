/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.URLsIO;
import com.github.vbsw.urlsaver.db.DBRecord;
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

	protected Global global;

	public void initialize ( final Global global ) {
		this.global = global;
		urlsLoadServices.clear();
		for ( DBRecord record: global.getDataBase().getRecords() ) {
			final URLsLoadService service = new URLsLoadService(global.getResourceLoader(),global.getURLMeta(),record);
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
		if ( global.getPreferences().getBooleanValue(PreferencesConfig.URLS_FILE_AUTOLOAD_ALL_ID).getModified() )
			for ( URLsLoadService service: urlsLoadServices )
				service.start();
	}

	public void reloadAllFiles ( ) {
		for ( URLsLoadService service: urlsLoadServices )
			service.restart();
	}

	public void cancelLoadingFile ( final DBRecord record ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			if ( service.record == record )
				service.cancel();
		}
	}

	public void reloadFile ( final DBRecord record ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			if ( service.record == record ) {
				record.beginLoading();
				service.restart();
			}
		}
	}

	@Override
	public void saveAllFiles ( ) {
		for ( DBRecord record: global.getDataBase().getRecords() )
			saveFile(record);
	}

	@Override
	public void saveSelectedFile ( ) {
		saveFile(global.getDataBase().getSelectedRecord());
	}

	public void saveFile ( final DBRecord record ) {
		if ( record != null && record.isDirty() ) {
			final Path filePath = record.getPath();
			boolean success = false;
			try ( final BufferedWriter writer = java.nio.file.Files.newBufferedWriter(filePath,global.getResourceLoader().getCharset()) ) {
				record.write(writer);
				success = true;
			} catch ( final IOException e ) {
				e.printStackTrace();
			}
			if ( success ) {
				if ( global.getDataBase().getSelectedRecord() == record ) {
					final StdProperties properties = (StdProperties) global.getProperties();
					properties.selectedFileDirtyProperty().setValue(false);
					properties.confirmingSaveProperty().setValue(false);
					global.getGUI().refreshFileInfo();
					global.getGUI().refreshTitle();
				}
				record.endSave();
			}
		}
	}

	private class URLsLoadProgressListener implements ChangeListener<Number> {
		private final DBRecord record;

		public URLsLoadProgressListener ( final DBRecord record ) {
			this.record = record;
		}

		@Override
		public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
			final int percentLoaded = (int) (newValue.doubleValue() * 100);
			final String listLabel = global.getTextGenerator().getFileListLabel(record,percentLoaded);
			record.setListLabel(listLabel);
			global.getGUI().refreshFileListView();
			global.getGUI().refreshFileInfo();
		}

	}

	private static final class ServiceFailedListener implements EventHandler<WorkerStateEvent> {

		@Override
		public void handle ( final WorkerStateEvent event ) {
			event.getSource().getException().printStackTrace();
		}

	}

	private final class FileLoadSucceededListener implements EventHandler<WorkerStateEvent> {

		private final DBRecord record;

		public FileLoadSucceededListener ( final DBRecord record ) {
			this.record = record;
		}

		@Override
		public void handle ( final WorkerStateEvent event ) {
			record.endLoading();
			global.getGUI().recordLoaded(record);
		}

	}

}

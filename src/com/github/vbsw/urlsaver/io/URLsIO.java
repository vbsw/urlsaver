/*
 *  Copyright 2018, 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.io;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.ISettings;
import com.github.vbsw.urlsaver.api.IURLsIO;
import com.github.vbsw.urlsaver.db.DBURLs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
public class URLsIO implements IURLsIO {

	protected final ArrayList<URLsLoadService> urlsLoadServices = new ArrayList<>();

	@Override
	public void recreateServices ( ) {
		final Charset inputCharset = Global.settings.getDefaultCharset();
		urlsLoadServices.clear();
		for ( DBURLs dbURLs: Global.db.getURLsList() ) {
			final URLsLoadService service = new URLsLoadService(inputCharset,Global.urlMetaKey,dbURLs);
			final URLsLoadProgressListener progressListener = new URLsLoadProgressListener(dbURLs);
			final ServiceFailedListener failedListener = new ServiceFailedListener();
			final FileLoadSucceededListener succeededListener = new FileLoadSucceededListener(dbURLs);
			service.progressProperty().addListener(progressListener);
			service.setOnFailed(failedListener);
			service.setOnSucceeded(succeededListener);
			urlsLoadServices.add(service);
		}
	}

	@Override
	public void loadDefault ( ) {
		if ( Global.settings.getBooleanProperty(ISettings.Property.urlsFileAutoLoadAll).modifiedValue )
			for ( URLsLoadService service: urlsLoadServices )
				service.start();
	}

	@Override
	public void reloadAllFiles ( ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			service.dbTable.beginLoading();
			if ( Global.db.getSelectedURLs() == service.dbTable )
				Global.gui.refreshFileSelection();
			service.restart();
		}
	}

	@Override
	public void reloadSelectedFile ( ) {
		final DBURLs selectedTable = Global.db.getSelectedURLs();
		reloadFile(selectedTable);
	}

	public void cancelLoadingFile ( final DBURLs dbTable ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			if ( service.dbTable == dbTable )
				service.cancel();
		}
	}

	public void reloadFile ( final DBURLs dbTable ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			if ( service.dbTable == dbTable ) {
				dbTable.beginLoading();
				if ( Global.db.getSelectedURLs() == dbTable )
					Global.gui.refreshFileSelection();
				service.restart();
			}
		}
	}

	@Override
	public void saveAllFiles ( ) {
		for ( DBURLs record: Global.db.getURLsList() )
			saveFile(record);
	}

	@Override
	public void saveSelectedFile ( ) {
		saveFile(Global.db.getSelectedURLs());
	}

	public void saveFile ( final DBURLs record ) {
		if ( record != null && record.isDirty() ) {
			final Path filePath = record.getPath();
			boolean success = false;
			try ( final BufferedWriter writer = Files.newBufferedWriter(filePath,Global.settings.getDefaultCharset()) ) {
				record.write(writer);
				success = true;
			} catch ( final IOException e ) {
				e.printStackTrace();
			}
			if ( success )
				record.saveComplete();
		}
	}

	private class URLsLoadProgressListener implements ChangeListener<Number> {
		private final DBURLs record;

		public URLsLoadProgressListener ( final DBURLs record ) {
			this.record = record;
		}

		@Override
		public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
			final int percentLoaded = (int) (newValue.doubleValue() * 100);
			final String listLabel = Global.labelProvider.getFileListLabel(record,percentLoaded);
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

		private final DBURLs dbURLs;

		public FileLoadSucceededListener ( final DBURLs record ) {
			this.dbURLs = record;
		}

		@Override
		public void handle ( final WorkerStateEvent event ) {
			dbURLs.loadingFinished();
			Global.gui.dbURLsLoadingFinished(dbURLs);
		}

	}

}

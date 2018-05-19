/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.services;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.db.DB;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.gui.ListViews;
import com.github.vbsw.urlsaver.gui.Properties;
import com.github.vbsw.urlsaver.gui.TabPanes;
import com.github.vbsw.urlsaver.gui.TextFields;
import com.github.vbsw.urlsaver.pref.Preferences;
import com.github.vbsw.urlsaver.resources.ResourcesConfig;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
public class Services {

	private static final ArrayList<URLsLoadService> urlsLoadServices = new ArrayList<>();

	public static void initialize ( ) {
		for ( DBRecord record: DB.getRecords() ) {
			final URLsLoadService service = new URLsLoadService(record);
			final URLsLoadProgressListener progressListener = new URLsLoadProgressListener(record);
			final ServiceFailedListener failedListener = new ServiceFailedListener();
			final FileLoadSucceededListener succeededListener = new FileLoadSucceededListener(record);
			service.progressProperty().addListener(progressListener);
			service.setOnFailed(failedListener);
			service.setOnSucceeded(succeededListener);
			urlsLoadServices.add(service);
		}
		if ( Preferences.getURLsFileAutoloadAll().getModifiedValue() )
			for ( URLsLoadService service: Services.urlsLoadServices )
				service.start();
	}

	public static void reloadAllFiles ( ) {
		for ( URLsLoadService service: Services.urlsLoadServices )
			service.restart();
	}

	public static void cancelLoadingFile ( final DBRecord record ) {
		for ( URLsLoadService service: Services.urlsLoadServices ) {
			if ( service.record == record )
				service.cancel();
		}
	}

	public static void reloadFile ( final DBRecord record ) {
		for ( URLsLoadService service: Services.urlsLoadServices ) {
			if ( service.record == record ) {
				record.clearURLs();
				record.setDirty(false);
				record.setLoaded(false);
				service.restart();
			}
		}
	}

	public static void saveAllFiles ( ) {
		for ( DBRecord record: DB.getRecords() )
			if ( record.isDirty() )
				Services.saveFile(record);
		GUI.refreshTitle();
	}

	public static void saveFile ( final DBRecord record ) {
		final Path filePath = record.getPath();
		try ( final BufferedWriter writer = java.nio.file.Files.newBufferedWriter(filePath,ResourcesConfig.FILE_CHARSET) ) {
			record.write(writer);
			record.setDirty(false);
			if ( ListViews.files.control.getSelectionModel().getSelectedItem() == record ) {
				Properties.selectedFileDirtyProperty().setValue(false);
				Properties.confirmingSaveProperty().setValue(false);
			}
		} catch ( final IOException e ) {
			e.printStackTrace();
		}
	}

	private static class URLsLoadProgressListener implements ChangeListener<Number> {
		private final DBRecord record;

		public URLsLoadProgressListener ( final DBRecord record ) {
			this.record = record;
		}

		@Override
		public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
			final int percentLoaded = (int) (newValue.doubleValue() * 100);
			final String listLabel = GUI.getListLabel(record,percentLoaded);
			record.setListLabel(listLabel);
			ListViews.files.control.refresh();
		}

	}

	private static final class ServiceFailedListener implements EventHandler<WorkerStateEvent> {

		@Override
		public void handle ( final WorkerStateEvent event ) {
			event.getSource().getException().printStackTrace();
		}

	}

	private static final class FileLoadSucceededListener implements EventHandler<WorkerStateEvent> {

		private final DBRecord record;

		public FileLoadSucceededListener ( final DBRecord record ) {
			this.record = record;
		}

		@Override
		public void handle ( final WorkerStateEvent event ) {
			final boolean fileIsAlreadySelected = (ListViews.files.control.getSelectionModel().getSelectedItem() == record);
			record.setLoaded(true);
			if ( fileIsAlreadySelected ) {
				Properties.selectedFileDirtyProperty().setValue(false);
				Properties.availableProperty().set(true);
				GUI.refreshTitle();
			} else if ( ListViews.files.autoSelectRequested ) {
				final String urlsFileSelect = Preferences.getURLsFileSelect().getSavedValue();
				final DBRecord recordToSelect = DB.getRecordByFileName(urlsFileSelect);
				if ( recordToSelect == record ) {
					ListViews.files.autoSelectRequested = false;
					if ( TabPanes.top.urls.control.disabledProperty().getValue() ) {
						ListViews.files.control.getSelectionModel().select(recordToSelect);
						TabPanes.top.control.getSelectionModel().select(TabPanes.top.urls.control);
						TextFields.urlSearch.control.requestFocus();
					}
				}
			}
		}

	}

}

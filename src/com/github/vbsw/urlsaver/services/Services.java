/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.services;


import java.util.ArrayList;

import com.github.vbsw.urlsaver.db.DB;
import com.github.vbsw.urlsaver.db.DBRecord;
import com.github.vbsw.urlsaver.gui.GUI;
import com.github.vbsw.urlsaver.gui.ListViews;
import com.github.vbsw.urlsaver.gui.Properties;
import com.github.vbsw.urlsaver.gui.TabPanes;
import com.github.vbsw.urlsaver.gui.TextFields;
import com.github.vbsw.urlsaver.pref.Preferences;

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
			if ( service.record == record )
				service.restart();
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
			// select only, if it's initial selection
			if ( ListViews.files.autoSelectRequested ) {
				final String urlsFileSelect = Preferences.getURLsFileSelect().getSavedValue();
				final DBRecord recordToSelect = DB.getRecordByFileName(urlsFileSelect);
				if ( recordToSelect == record ) {
					ListViews.files.autoSelectRequested = false;
					// select only, if URLs-tab is not already selected
					if ( TabPanes.top.urls.control.disabledProperty().getValue() ) {
						if ( fileIsAlreadySelected )
							Properties.availableProperty().set(true);
						else
							ListViews.files.control.getSelectionModel().select(recordToSelect);
						TabPanes.top.control.getSelectionModel().select(TabPanes.top.urls.control);
						TextFields.urlSearch.control.requestFocus();
					}
				}
			}
			if ( fileIsAlreadySelected )
				GUI.refreshTitle();
		}

	}

}

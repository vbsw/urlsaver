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
import com.github.vbsw.urlsaver.api.IURLsIO;
import com.github.vbsw.urlsaver.db.DBURLs;
import com.github.vbsw.urlsaver.db.DBURLsImport;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
public class URLsIO implements IURLsIO {

	protected final ArrayList<URLsLoadService> urlsLoadServices = new ArrayList<>();
	protected final ArrayList<URLsImportLoadService> urlsImportLoadServices = new ArrayList<>();

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
		for ( DBURLsImport dbURLsImport: Global.db.getURLsImportList() ) {
			final URLsImportLoadService service = new URLsImportLoadService(inputCharset,Global.urlMetaKey,dbURLsImport);
			final URLsImportLoadProgressListener progressListener = new URLsImportLoadProgressListener(dbURLsImport);
			final ServiceFailedListener failedListener = new ServiceFailedListener();
			final ImportSucceededListener succeededListener = new ImportSucceededListener(dbURLsImport);
			service.progressProperty().addListener(progressListener);
			service.setOnFailed(failedListener);
			service.setOnSucceeded(succeededListener);
			urlsImportLoadServices.add(service);
		}
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
		final DBURLs dbURLs = Global.db.getSelectedURLs();
		reloadFile(dbURLs);
	}

	@Override
	public void importSelectedFile ( ) {
		final DBURLsImport dbURLsImport = Global.db.getSelectedURLsImport();
		importFile(dbURLsImport);
	}

	public void cancelLoadingFile ( final DBURLs dbTable ) {
		for ( URLsLoadService service: urlsLoadServices ) {
			if ( service.dbTable == dbTable )
				service.cancel();
		}
	}

	public void reloadFile ( final DBURLs dbURLs ) {
		for ( URLsLoadService urlsLoadService: urlsLoadServices ) {
			if ( urlsLoadService.dbTable == dbURLs ) {
				dbURLs.beginLoading();
				if ( Global.db.getSelectedURLs() == dbURLs )
					Global.gui.refreshFileSelection();
				urlsLoadService.restart();
			}
		}
	}

	public void importFile ( final DBURLsImport dbURLsImport ) {
		for ( URLsImportLoadService urLsImportLoadService: urlsImportLoadServices ) {
			if ( urLsImportLoadService.dbURLsImport == dbURLsImport ) {
				dbURLsImport.beginImport();
				if ( Global.db.getSelectedURLsImport() == dbURLsImport )
					Global.gui.refreshImportSelection();
				urLsImportLoadService.restart();
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
		private final DBURLs dbURLs;

		public URLsLoadProgressListener ( final DBURLs dbURLs ) {
			this.dbURLs = dbURLs;
		}

		@Override
		public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
			final int percentLoaded = (int) (newValue.doubleValue() * 100);
			final String listLabel = Global.labelProvider.getFileListLabel(dbURLs,percentLoaded);
			dbURLs.setListLabel(listLabel);
			Global.gui.refreshFileListView();
			Global.gui.refreshFileInfo();
		}

	}

	private class URLsImportLoadProgressListener implements ChangeListener<Number> {
		private final DBURLsImport dbURLsImport;

		public URLsImportLoadProgressListener ( final DBURLsImport dbURLsImport ) {
			this.dbURLsImport = dbURLsImport;
		}

		@Override
		public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
			final int percentLoaded = (int) (newValue.doubleValue() * 100);
			final String listLabel = Global.labelProvider.getImportListLabel(dbURLsImport,percentLoaded);
			dbURLsImport.setListLabel(listLabel);
			Global.gui.refreshImportListView();
			Global.gui.refreshImportInfo();
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

	private final class ImportSucceededListener implements EventHandler<WorkerStateEvent> {

		private final DBURLsImport dbURLsImport;

		public ImportSucceededListener ( final DBURLsImport dbURLsImport ) {
			this.dbURLsImport = dbURLsImport;
		}

		@Override
		public void handle ( final WorkerStateEvent event ) {
			dbURLsImport.importFinished();
			Global.gui.dbURLsImportFinished(dbURLsImport);
		}

	}

}

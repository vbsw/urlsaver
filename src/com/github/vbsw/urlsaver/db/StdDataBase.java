/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.github.vbsw.urlsaver.api.DataBase;
import com.github.vbsw.urlsaver.api.Preferences;
import com.github.vbsw.urlsaver.api.ResourceLoader;
import com.github.vbsw.urlsaver.api.TextGenerator;
import com.github.vbsw.urlsaver.pref.PreferencesConfig;
import com.github.vbsw.urlsaver.utility.OSFiles;


/**
 * @author Vitali Baumtrok
 */
public class StdDataBase extends DataBase {

	protected final PathComparator pathComparator = new PathComparator();
	protected final ArrayList<DBRecord> records = new ArrayList<>();
	protected DBRecord selectedRecord;

	@Override
	public void initialize ( final ResourceLoader resourceLoader, final Preferences preferences, final TextGenerator textGenerator ) {
		final String fileExtension = preferences.getStringValue(PreferencesConfig.URLS_FILE_EXTENSION_ID).getSaved();
		final Path launchDir = resourceLoader.getLaunchSource().getDirectory();
		final ArrayList<Path> files = OSFiles.getFilesFromDirectory(launchDir,fileExtension);
		final ArrayList<Path> filesSorted = getSortedPaths(files);
		records.clear();
		for ( final Path filePath: filesSorted ) {
			final DBRecord record = new DBRecord(filePath);
			final String listLabel = textGenerator.getFileListLabel(record,0);
			record.setListLabel(listLabel);
			records.add(record);
		}
	}

	@Override
	public boolean isSaved ( ) {
		final int size = records.size();
		for ( int i = 0; i < size; i += 1 )
			if ( records.get(i).isDirty() )
				return false;
		return true;
	}

	@Override
	public ArrayList<DBRecord> getRecords ( ) {
		return records;
	}

	@Override
	public DBRecord getRecordByFileName ( final String fileName ) {
		final int size = records.size();
		for ( int i = 0; i < size; i += 1 ) {
			final DBRecord record = records.get(i);
			if ( record.getFileName().equals(fileName) )
				return record;
		}
		return null;
	}

	@Override
	public DBRecord getSelectedRecord ( ) {
		return selectedRecord;
	}

	@Override
	public void setSelectedRecord ( final DBRecord record ) {
		selectedRecord = record;
	}

	private ArrayList<Path> getSortedPaths ( final ArrayList<Path> paths ) {
		Collections.sort(paths,pathComparator);
		return paths;
	}

	private static class PathComparator implements Comparator<Path> {

		@Override
		public int compare ( final Path path1, final Path path2 ) {
			final String path1String = path1.toString();
			final String path2String = path2.toString();
			return path1String.compareTo(path2String);
		}

	}

}

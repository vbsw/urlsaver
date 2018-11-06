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
import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.pref.PreferencesConfig;
import com.github.vbsw.urlsaver.utility.OSFiles;


/**
 * @author Vitali Baumtrok
 */
public class StdDataBase extends DataBase {

	protected final PathComparator pathComparator = new PathComparator();
	protected final ArrayList<DBTable> tables = new ArrayList<>();
	protected DBTable selectedRecord;

	@Override
	public void initialize ( ) {
		final String fileExtension = Global.preferences.getStringPereference(PreferencesConfig.URLS_FILE_EXTENSION_ID).getSaved();
		final Path programDir = Global.resourceLoader.getProgramFile().getDirectory();
		final ArrayList<Path> files = OSFiles.getFilesFromDirectory(programDir,fileExtension);
		final ArrayList<Path> filesSorted = getSortedPaths(files);
		tables.clear();
		for ( final Path filePath: filesSorted ) {
			final DBTable record = new DBTable(filePath);
			final String listLabel = Global.textGenerator.getFileListLabel(record,0);
			record.setListLabel(listLabel);
			tables.add(record);
		}
	}

	@Override
	public boolean isSaved ( ) {
		final int size = tables.size();
		for ( int i = 0; i < size; i += 1 )
			if ( tables.get(i).isDirty() )
				return false;
		return true;
	}

	@Override
	public ArrayList<DBTable> getTables ( ) {
		return tables;
	}

	@Override
	public DBTable getTableByFileName ( final String fileName ) {
		final int size = tables.size();
		for ( int i = 0; i < size; i += 1 ) {
			final DBTable record = tables.get(i);
			if ( record.getFileName().equals(fileName) )
				return record;
		}
		return null;
	}

	@Override
	public DBTable getSelectedDBTable ( ) {
		return selectedRecord;
	}

	@Override
	public void setSelectedTable ( final DBTable record ) {
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

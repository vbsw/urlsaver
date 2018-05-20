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

import com.github.vbsw.urlsaver.JarFile;
import com.github.vbsw.urlsaver.gui.TextGenerator;
import com.github.vbsw.urlsaver.pref.Preferences;


/**
 * @author Vitali Baumtrok
 */
public class DB {

	private static final PathComparator pathComparator = new PathComparator();
	private static final ArrayList<DBRecord> records = new ArrayList<>();

	public static void initialize ( ) {
		final String fileExtension = Preferences.getURLsFileExtension().getSavedValue();
		final ArrayList<Path> filePaths = JarFile.getFilePaths(fileExtension);
		final ArrayList<Path> filePathsSorted = DB.getSortedPaths(filePaths);
		for ( final Path path: filePathsSorted ) {
			final DBRecord record = new DBRecord(path);
			final String listLabel = TextGenerator.getFileListLabel(record,0);
			record.setListLabel(listLabel);
			records.add(record);
		}
	}

	public static boolean isSaved ( ) {
		final int size = records.size();
		for ( int i = 0; i < size; i += 1 )
			if ( records.get(i).isDirty() )
				return false;
		return true;
	}

	public static ArrayList<DBRecord> getRecords ( ) {
		return records;
	}

	public static DBRecord getRecordByFileName ( final String fileName ) {
		final int size = records.size();
		for ( int i = 0; i < size; i += 1 ) {
			final DBRecord record = records.get(i);
			if ( record.getFileName().equals(fileName) )
				return record;
		}
		return null;
	}

	private static ArrayList<Path> getSortedPaths ( final ArrayList<Path> paths ) {
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

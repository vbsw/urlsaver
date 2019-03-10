/*
 *    Copyright 2019 Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.db;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.github.vbsw.urlsaver.api.Global;
import com.github.vbsw.urlsaver.api.IDB;
import com.github.vbsw.urlsaver.api.ISettings;
import com.github.vbsw.urlsaver.utility.OSFiles;


/**
 * @author Vitali Baumtrok
 */
public class DB implements IDB {

	protected final PathComparator pathComparator = new PathComparator();
	protected final ArrayList<DBURLs> dbURLsList = new ArrayList<>();
	protected final ArrayList<DBURLsImport> dbURLsImportList = new ArrayList<>();
	protected DBURLs selectedDBURLs;
	protected DBURLsImport selectedDBURLsImport;

	public void initialize ( ) {
		reload();
	}

	@Override
	public DBURLs getSelectedURLs ( ) {
		return selectedDBURLs;
	}

	@Override
	public DBURLsImport getSelectedURLsImport ( ) {
		return selectedDBURLsImport;
	}

	@Override
	public DBURLs getURLsByFileName ( final String fileName ) {
		for ( DBURLs dburLs: dbURLsList )
			if ( dburLs.getFileName().equals(fileName) )
				return dburLs;
		return null;
	}

	@Override
	public ArrayList<DBURLsImport> getURLsImportList ( ) {
		return dbURLsImportList;
	}

	@Override
	public ArrayList<DBURLs> getURLsList ( ) {
		return dbURLsList;
	}

	@Override
	public boolean isSaved ( ) {
		for ( DBURLs dburLs: dbURLsList )
			if ( dburLs.isDirty() )
				return false;
		return true;
	}

	@Override
	public void reload ( ) {
		final String fileExtension = Global.settings.getStringProperty(ISettings.Property.urlsFileExtension).savedValue;
		final Path programDir = Global.pathProvider.getWorkingDirectory();
		final ArrayList<Path> urlsFiles = OSFiles.getFilesFromDirectory(programDir,fileExtension);
		final ArrayList<Path> urlsFilesSorted = getSortedPaths(urlsFiles);
		final ArrayList<Path> xmlFiles = OSFiles.getFilesFromDirectory(programDir,"xml");
		final ArrayList<Path> xmlFilesFiltered = filterMyAnimeList(xmlFiles);
		final ArrayList<Path> xmlFilesSorted = getSortedPaths(xmlFilesFiltered);
		final ArrayList<Path> urlsImportFilesSorted = mergeLists(urlsFilesSorted,xmlFilesSorted);
		dbURLsList.clear();
		dbURLsImportList.clear();
		for ( final Path filePath: urlsFilesSorted ) {
			final DBURLs dbURLs = new DBURLs(filePath);
			final String listLabel = Global.labelProvider.getFileListLabel(dbURLs,0);
			dbURLs.setListLabel(listLabel);
			dbURLsList.add(dbURLs);
		}
		for ( final Path importPath: urlsImportFilesSorted ) {
			final DBURLsImport dbURLsImport = new DBURLsImport(importPath);
			final String listLabel = Global.labelProvider.getImportListLabel(dbURLsImport,0);
			dbURLsImport.setListLabel(listLabel);
			dbURLsImportList.add(dbURLsImport);
		}
	}

	@Override
	public void setSelectedURLs ( final DBURLs dbURLs ) {
		selectedDBURLs = dbURLs;
	}

	@Override
	public void setSelectedURLsImport ( final DBURLsImport dbURLsImport ) {
		selectedDBURLsImport = dbURLsImport;
	}

	private ArrayList<Path> mergeLists ( final ArrayList<Path> sortedListA, final ArrayList<Path> sortedListB ) {
		final int sizeA = sortedListA.size();
		final int sizeB = sortedListB.size();
		final ArrayList<Path> paths = new ArrayList<Path>(sizeA + sizeB);
		int i = 0, j = 0;
		while ( i < sizeA && j < sizeB ) {
			final Path pathA = sortedListA.get(i);
			final Path pathB = sortedListB.get(j);
			final int comparison = pathA.compareTo(pathB);
			if ( comparison < 0 ) {
				paths.add(pathA);
				i += 1;
			} else if ( comparison > 0 ) {
				paths.add(pathB);
				j += 1;
			} else {
				paths.add(pathA);
				i += 1;
				j += 1;
			}
		}
		for ( ; i < sizeA; i += 1 ) {
			final Path pathA = sortedListA.get(i);
			paths.add(pathA);
		}
		for ( ; j < sizeB; j += 1 ) {
			final Path pathB = sortedListB.get(j);
			paths.add(pathB);
		}
		return paths;
	}

	private ArrayList<Path> filterMyAnimeList ( final ArrayList<Path> xmlFiles ) {
		if ( xmlFiles.size() > 0 ) {
			final ArrayList<Path> list = new ArrayList<Path>(xmlFiles.size());
			final byte[] filter = new byte[] { '<', 'm', 'y', 'a', 'n', 'i', 'm', 'e', 'l', 'i', 's', 't', '>' };
			for ( Path path: xmlFiles ) {
				final byte[] buffer = new byte[1000];
				int bytesRead = 0;
				try ( InputStream inputStream = Files.newInputStream(path) ) {
					bytesRead = inputStream.read(buffer);
				} catch ( IOException e ) {
					e.printStackTrace();
				}
				for ( int i = 0; i < bytesRead - filter.length; i += 1 ) {
					if ( matchBytes(buffer,filter,i) ) {
						list.add(path);
					}
				}
			}
			return list;
		}
		return xmlFiles;
	}

	private boolean matchBytes ( final byte[] buffer, final byte[] filter, final int offset ) {
		for ( int i = 0; i < filter.length; i += 1 ) {
			if ( buffer[offset + i] != filter[i] ) {
				return false;
			}
		}
		return true;
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

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


/**
 * @author Vitali Baumtrok
 */
public class DBFiles {

	private static final PathComparator comparator = new PathComparator();
	private static final ArrayList<Path> paths = new ArrayList<>();
	private static final DynArrayOfString fileNames = new DynArrayOfString();
	private static final DynArrayOfString labes = new DynArrayOfString();
	private static final DynArrayOfBoolean dirtyFlags = new DynArrayOfBoolean();

	public static void setPathList ( final ArrayList<Path> paths ) {
		DBFiles.paths.clear();
		DBFiles.fileNames.clear();
		DBFiles.dirtyFlags.clear();

		DBFiles.paths.addAll(paths);
		for ( Path path: paths ) {
			final String fileName = path.getFileName().toString();
			DBFiles.fileNames.add(fileName);
			DBFiles.dirtyFlags.add(false);
		}
	}

	public static void setLabelList ( final ArrayList<String> labels ) {
		DBFiles.labes.clear();
		for ( String label: labels )
			DBFiles.labes.add(label);
	}

	public static int getIndex ( final Path path ) {
		for ( int i = 0; i < paths.size(); i += 1 ) {
			final Path pathCurrent = paths.get(i);
			if ( pathCurrent == path )
				return i;
		}
		final int index = Collections.binarySearch(paths,path,comparator);
		return index;
	}

	public static int getIndexByFileName ( final String fileName ) {
		for ( int i = 0; i < fileNames.valuesLength; i += 1 )
			if ( fileNames.values[i].equals(fileName) )
				return i;
		return -1;
	}

	public static void insertPath ( final int index, final Path path ) {
		final String fileName = path.getFileName().toString();
		paths.add(index,path);
		fileNames.add(index,fileName);
	}

	public static void insertLabel ( final int index, final String label ) {
		labes.add(index,label);
	}

	public static void setDirty ( final int index, final boolean dirty ) {
		dirtyFlags.values[index] = dirty;
	}

	public static ArrayList<Path> getPaths ( ) {
		return paths;
	}

	public static String getLabel ( final int index ) {
		return labes.values[index];
	}

	public static ArrayList<Path> getSortedPaths ( final ArrayList<Path> paths ) {
		Collections.sort(paths,comparator);
		return paths;
	}

	public static boolean isDirty ( final int index ) {
		return dirtyFlags.values[index];
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

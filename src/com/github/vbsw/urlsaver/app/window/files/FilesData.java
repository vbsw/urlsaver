/**
 * URL Saver (a tool to manage URLs by keywords)
 * Copyright 2017 Vitali Baumtrok
 * 
 * This file is part of URL Saver.
 * 
 * URL Saver is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * URL Saver is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with URL Saver.  If not, see <http://www.gnu.org/licenses/>.
 */


package com.github.vbsw.urlsaver.app.window.files;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.github.vbsw.urlsaver.SortedUniqueStringList;
import com.github.vbsw.urlsaver.app.window.urls.UrlsData;
import com.github.vbsw.urlsaver.app.window.urls.UrlsSearchResult;
import com.github.vbsw.urlsaver.app.window.urls.UrlsViewData;
import com.github.vbsw.urlsaver.resources.Resources;


/**
 * @author Vitali Baumtrok
 */
public class FilesData {

	public static final int INITIAL_CAPACITY = 10;

	public final ArrayList<Boolean> dirtyFlags = new ArrayList<Boolean>();
	public final ArrayList<Path> paths = new ArrayList<Path>(INITIAL_CAPACITY);
	public final ArrayList<Boolean> loaded = new ArrayList<Boolean>(INITIAL_CAPACITY);
	public final ArrayList<FileLoadService> loadServices = new ArrayList<FileLoadService>(INITIAL_CAPACITY);
	public final ArrayList<UrlsData> urlsData = new ArrayList<UrlsData>(INITIAL_CAPACITY);
	public final ArrayList<UrlsSearchResult> searchUrls = new ArrayList<UrlsSearchResult>(INITIAL_CAPACITY);
	public final ArrayList<String> labels = new ArrayList<String>(INITIAL_CAPACITY);
	public final ArrayList<UrlsViewData> urlsViewData = new ArrayList<UrlsViewData>(INITIAL_CAPACITY);

	public void initialize ( final ArrayList<Path> filePaths ) {
		final PathComparator comparator = new PathComparator();

		dirtyFlags.clear();
		paths.clear();
		loaded.clear();
		loadServices.clear();
		urlsData.clear();
		searchUrls.clear();
		urlsViewData.clear();

		for ( Path filePath: filePaths ) {
			final int index = Collections.binarySearch(paths,filePath,comparator);

			if ( index < 0 ) {
				final int insertIndex = -index - 1;
				final FileLoadService loadService = new FileLoadService(filePath);
				final String label = Files.getListViewText(filePath,0);

				loadService.progressProperty().addListener(new FileLoadProgressListener(filePath));
				loadService.setOnFailed(new FileLoadFailedListener());
				loadService.setOnSucceeded(new FileLoadSucceededListener(filePath));

				dirtyFlags.add(insertIndex,false);
				paths.add(insertIndex,filePath);
				loaded.add(insertIndex,false);
				loadServices.add(insertIndex,loadService);
				urlsData.add(insertIndex,null);
				searchUrls.add(insertIndex,new UrlsSearchResult());
				labels.add(insertIndex,label);
				urlsViewData.add(insertIndex,new UrlsViewData());
			}
		}
	}

	public int getIndex ( final Path filePath ) {
		for ( int i = 0; i < paths.size(); i += 1 ) {
			final Path currentPath = paths.get(i);

			if ( currentPath == filePath ) {
				return i;
			}
		}
		return -1;
	}

	public int getIndexByFileName ( final String fileName ) {
		for ( int i = 0; i < paths.size(); i += 1 ) {
			if ( paths.get(i).getFileName().toString().equals(fileName) ) {
				return i;
			}
		}
		return -1;
	}

	public boolean isAnyDirty ( ) {
		for ( Boolean dirty: dirtyFlags ) {
			if ( dirty ) {
				return true;
			}
		}
		return false;
	}

	public void save ( final int index ) {
		final Path filePath = paths.get(index);
		final UrlsData data = urlsData.get(index);
		final int space = ' ';

		try ( final BufferedWriter writer = java.nio.file.Files.newBufferedWriter(filePath,Resources.ENCODING) ) {
			for ( int i = 0; i < data.urls.size(); i += 1 ) {
				final String url = data.urls.get(i);
				final SortedUniqueStringList urlTags = data.urlTagsList.get(i);

				writer.write(url);
				writer.newLine();

				if ( urlTags.size() > 0 ) {
					writer.write(urlTags.get(0));
				}
				for ( int j = 1; j < urlTags.size(); j += 1 ) {
					writer.write(space);
					writer.write(urlTags.get(j));
				}
				writer.newLine();
			}
			dirtyFlags.set(index,false);

		} catch ( final IOException e ) {
			e.printStackTrace();
		}
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

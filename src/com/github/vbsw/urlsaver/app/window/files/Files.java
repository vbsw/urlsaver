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


import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.JarPath;
import com.github.vbsw.urlsaver.SortedUniqueStringList;
import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.app.window.urls.UrlsData;
import com.github.vbsw.urlsaver.app.window.urls.UrlsViewData;

import javafx.beans.property.SimpleBooleanProperty;


/**
 * @author Vitali Baumtrok
 */
public class Files {

	private final SimpleBooleanProperty selectedFileDirty = new SimpleBooleanProperty();
	private final SimpleBooleanProperty confirmingSave = new SimpleBooleanProperty();
	private final SimpleBooleanProperty selected = new SimpleBooleanProperty();
	private final FilesData data = new FilesData();

	private boolean autoSelectRequested;

	public final FilesModelView mv = new FilesModelView();

	public void initialize ( ) {
		final String fileExtension = App.settings.getUrlsFileExtension();
		final ArrayList<Path> filePaths = JarPath.getPaths(fileExtension);
		final String defaultFileName = App.settings.getUrlsFileSelect();

		data.initialize(filePaths);

		autoSelectRequested = (data.getIndexByFileName(defaultFileName) >= 0);
	}

	public void setUrlsData ( final Path filePath, final UrlsData urlsData ) {
		final int dataIndex = data.getIndex(filePath);

		if ( dataIndex >= 0 ) {
			final UrlsViewData urlsViewData = data.urlsViewData.get(dataIndex);

			urlsViewData.clear();
			data.urlsData.set(dataIndex,urlsData);
			App.urls.setData(urlsData,urlsViewData);

			if ( autoSelectRequested ) {
				final String defaultFileName = App.settings.getUrlsFileSelect();
				final int defaultFileDataIndex = getDataIndexByFileName(defaultFileName);

				if ( dataIndex == defaultFileDataIndex ) {
					autoSelectRequested = false;

					selectDefault();
					App.scene.tp.top.getSelectionModel().select(App.scene.topTab.urls);
					App.scene.tf.urlSearch.requestFocus();
				}
			}
		}
	}

	public void setFileLoadProgress ( final Path filePath, final int percentLoaded ) {
		final int fileDataIndex = data.getIndex(filePath);
		final String listViewText = Files.getListViewText(filePath,percentLoaded);

		data.labels.set(fileDataIndex,listViewText);
		App.scene.lv.files.refresh();
	}

	public void updateSelectedInfo ( ) {
		final Path selectedFilePath = App.scene.lv.files.getSelectionModel().getSelectedItem();
		final int fileDataIndex = data.getIndex(selectedFilePath);
		final String filePathString;
		final UrlsData urlsData;
		final UrlsViewData urlsViewData;
		final boolean dirty;

		if ( fileDataIndex >= 0 ) {
			filePathString = selectedFilePath.toString();
			urlsData = getUrlsData(fileDataIndex);
			urlsViewData = getUrlsViewData(fileDataIndex);
			dirty = data.dirtyFlags.get(fileDataIndex);

		} else {
			filePathString = ""; //$NON-NLS-1$
			urlsData = null;
			urlsViewData = null;
			dirty = false;
		}
		App.scene.tf.fileName.setText(filePathString);
		App.urls.setData(urlsData,urlsViewData);
		selectedFileDirty.set(dirty);
		selected.set(fileDataIndex >= 0);
		confirmingSave.set(false);
	}

	public int getDataIndex ( final Path filePath ) {
		return data.getIndex(filePath);
	}

	public int getDataIndexByFileName ( final String fileName ) {
		return data.getIndexByFileName(fileName);
	}

	public int getSelectedFileIndex ( ) {
		final Path filePath = App.scene.lv.files.getSelectionModel().getSelectedItem();
		final int selectedFileIndex = data.getIndex(filePath);

		return selectedFileIndex;
	}

	public static String getListViewText ( final Path filePath, final int percentLoaded ) {
		final String listViewText;

		if ( percentLoaded < 0 ) {
			listViewText = filePath.toString() + "  0%"; //$NON-NLS-1$

		} else if ( percentLoaded < 100 ) {
			listViewText = filePath.toString() + "  " + percentLoaded + "%"; //$NON-NLS-1$

		} else {
			listViewText = filePath.toString();
		}
		return listViewText;
	}

	public void processAutoLoad ( ) {
		if ( App.settings.isAutoloadAll() ) {
			reloadAll();

		} else {
			loadDefault();
		}
	}

	public void reload ( final int dataIndex ) {
		final UrlsData urlsData = data.urlsData.get(dataIndex);

		if ( App.urls.getData() == urlsData ) {
			App.urls.setData(null,null);
		}
		data.urlsData.add(dataIndex,null);
		data.loadServices.get(dataIndex).restart();
	}

	public void setSearchTagsString ( final Path filePath, final String newValue ) {
		// TODO Auto-generated method stub
	}

	public UrlsData getUrlsData ( final int index ) {
		return data.urlsData.get(index);
	}

	public UrlsViewData getUrlsViewData ( final int index ) {
		return data.urlsViewData.get(index);
	}

	public boolean isAnyDirty ( ) {
		return data.isAnyDirty();
	}

	public SortedUniqueStringList getSearchTags ( final int index ) {
		return data.searchUrls.get(index).getTags();
	}

	public ArrayList<Path> getPaths ( ) {
		return data.paths;
	}

	public void selectDefault ( ) {
		final String autoSelectFileName = App.settings.getUrlsFileSelect();
		final int autoSelectFileIndex = data.getIndexByFileName(autoSelectFileName);

		if ( autoSelectFileIndex >= 0 ) {
			App.scene.lv.files.requestFocus();
			App.scene.lv.files.getSelectionModel().select(autoSelectFileIndex);
		}
	}

	public void reloadAll ( ) {
		for ( int i = 0; i < data.paths.size(); i += 1 ) {
			reload(i);
		}
	}

	private void loadDefault ( ) {
		final String autoSelectFileName = App.settings.getUrlsFileSelect();
		final int autoSelectFileIndex = data.getIndexByFileName(autoSelectFileName);

		if ( autoSelectFileIndex >= 0 ) {
			reload(autoSelectFileIndex);
		}
	}

	public SimpleBooleanProperty selectedFileDirtyProperty ( ) {
		return selectedFileDirty;
	}

	public SimpleBooleanProperty confirmingSaveProperty ( ) {
		return confirmingSave;
	}

	public SimpleBooleanProperty selectedProperty ( ) {
		return selected;
	}

	public boolean isDirty ( final int index ) {
		return data.dirtyFlags.get(index);
	}

	public String getDataLabel ( final int index ) {
		return data.labels.get(index);
	}

	public void reloadSelected ( ) {
		final int selectedFileIndex = getSelectedFileIndex();

		reload(selectedFileIndex);
	}

	public void confirmSaveSelected ( ) {
		final int fileIndex = getSelectedFileIndex();

		data.save(fileIndex);
		selectedFileDirty.setValue(false);
		confirmingSave.setValue(false);
		App.window.updateTitle();
	}

	public void saveSelected ( ) {
		confirmingSave.setValue(true);
	}

	public void setDirty ( final int index ) {
		data.dirtyFlags.set(index,true);

		if ( getSelectedFileIndex() == index ) {
			App.window.updateTitle();
			selectedFileDirty.setValue(true);
		}
	}

	public void saveAll ( ) {
		for ( int i = 0; i < data.dirtyFlags.size(); i += 1 ) {
			final boolean dirty = data.dirtyFlags.get(i);

			if ( dirty ) {
				data.save(i);
			}
		}
		App.window.updateTitle();
	}

	public void cancel ( ) {
		confirmingSave.setValue(false);
		App.scene.lv.files.requestFocus();
	}

}

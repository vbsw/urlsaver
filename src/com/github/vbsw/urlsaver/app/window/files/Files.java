
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.files;


import java.nio.file.Path;
import java.util.ArrayList;

import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.app.window.urls.UrlsData;
import com.github.vbsw.urlsaver.app.window.urls.UrlsViewData;
import com.github.vbsw.urlsaver.utility.Jar;
import com.github.vbsw.urlsaver.utility.SortedUniqueStringList;


/**
 * @author Vitali Baumtrok
 */
public class Files {

	private final FilesData data = new FilesData();

	private boolean autoSelectRequested;

	public final FilesViewModel vm = new FilesViewModel();

	public void initialize ( ) {
		final String fileExtension = App.settings.getUrlsFileExtension();
		final ArrayList<Path> filePaths = Jar.getFilePaths(fileExtension);
		final String defaultFileName = App.settings.getUrlsFileSelect();

		data.initialize(filePaths);

		autoSelectRequested = (data.getIndexByFileName(defaultFileName) >= 0);
	}

	public void setUrlsData ( final Path filePath, final UrlsData urlsData ) {
		final int dataIndex = data.getIndex(filePath);

		if ( dataIndex >= 0 ) {
			final UrlsViewData urlsViewData = getUrlsViewData(dataIndex);

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

		if ( fileDataIndex >= 0 ) {
			filePathString = selectedFilePath.toString();
			urlsData = getUrlsData(fileDataIndex);
			urlsViewData = getUrlsViewData(fileDataIndex);

		} else {
			filePathString = ""; //$NON-NLS-1$
			urlsData = null;
			urlsViewData = null;
		}
		App.scene.tf.fileName.setText(filePathString);
		App.urls.setData(urlsData,urlsViewData);
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
	}

	public void saveSelected ( ) {
		vm.confirmingSave.setValue(true);
	}

	public void setDirty ( final int index ) {
		data.dirtyFlags.set(index,true);

		if ( getSelectedFileIndex() == index ) {
			App.window.updateTitle();
			vm.selectedFileDirty.setValue(true);
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
		vm.confirmingSave.setValue(false);
		App.scene.lv.files.requestFocus();
	}

}

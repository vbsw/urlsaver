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


package com.github.vbsw.urlsaver.scene.factories;


import com.github.vbsw.urlsaver.urls.UrlsFile;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;


/**
 * @author Vitali Baumtrok
 */
public final class FilesListViewCellFactory implements Callback<ListView<UrlsFile>, ListCell<UrlsFile>> {

	@Override
	public ListCell<UrlsFile> call ( ListView<UrlsFile> param ) {
		final ListCell<UrlsFile> listCell = new FilesListViewCell();
		return listCell;
	}

}

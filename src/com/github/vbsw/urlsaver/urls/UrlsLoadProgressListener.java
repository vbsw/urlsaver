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


package com.github.vbsw.urlsaver.urls;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * @author Vitali Baumtrok
 */
final class UrlsLoadProgressListener implements ChangeListener<Number> {

	private final UrlsFile urlsFile;

	public UrlsLoadProgressListener ( final UrlsFile urlsFile ) {
		this.urlsFile = urlsFile;
	}

	@Override
	public void changed ( final ObservableValue<? extends Number> observable, final Number oldValue, final Number newValue ) {
		final int percent = (int) (newValue.doubleValue() * 100);
		final String listViewText = urlsFile.getLoadingListViewText(percent);

		urlsFile.setItemText(listViewText);
	}

}

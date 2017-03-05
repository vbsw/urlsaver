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


package com.github.vbsw.urlsaver.scene.listeners;


import com.github.vbsw.urlsaver.Convert;
import com.github.vbsw.urlsaver.SortedUniqueStringList;
import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.urls.UrlsData;
import com.github.vbsw.urlsaver.urls.UrlsFile;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


/**
 * @author Vitali Baumtrok
 */
public final class UrlItemSelectionListener implements ChangeListener<String> {

	@Override
	public void changed ( final ObservableValue<? extends String> observable, final String oldValue, final String newValue ) {
		if ( newValue != null ) {
			final int selectedUrlIndex = App.scene.lv.urls.getSelectionModel().getSelectedIndex();
			final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();
			final UrlsData urlsData = selectedUrlsFile.getData();
			final int urlIndex = urlsData.getUrlIndex(newValue);
			final SortedUniqueStringList urlTags = urlsData.urlTagsList.get(urlIndex);
			final String tagsString = Convert.toString(urlTags);

			App.scene.tf.url.setText(newValue);
			App.scene.ta.tags.setText(tagsString);
			selectedUrlsFile.setUrlIndex(selectedUrlIndex);

		} else {
			final String emptyString = ""; //$NON-NLS-1$

			App.scene.tf.url.setText(emptyString);
			App.scene.ta.tags.setText(emptyString);
		}
	}

}

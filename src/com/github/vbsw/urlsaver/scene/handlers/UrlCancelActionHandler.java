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


package com.github.vbsw.urlsaver.scene.handlers;


import com.github.vbsw.urlsaver.Converter;
import com.github.vbsw.urlsaver.SortedUniqueStringList;
import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.urls.UrlsData;
import com.github.vbsw.urlsaver.urls.UrlsFile;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;


/**
 * @author Vitali Baumtrok
 */
public class UrlCancelActionHandler implements EventHandler<ActionEvent> {

	@Override
	public void handle ( final ActionEvent event ) {
		final String selectedUrl = App.scene.lv.urls.getSelectionModel().getSelectedItem();

		if ( selectedUrl != null ) {
			final UrlsFile selectedUrlsFile = App.scene.lv.files.getSelectionModel().getSelectedItem();
			final UrlsData urlsData = selectedUrlsFile.getData();
			final int selectedUrlIndex = urlsData.getUrlIndex(selectedUrl);
			final SortedUniqueStringList selectedTags = urlsData.urlTagsList.get(selectedUrlIndex);
			final String tags = Converter.toString(selectedTags);

			App.scene.tf.url.setText(selectedUrl);
			App.scene.ta.tags.setText(tags);
			App.scene.lv.urls.requestFocus();

		} else {
			App.scene.tf.url.setText("");
			App.scene.ta.tags.setText("");
		}
	}

}

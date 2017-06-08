
//          Copyright 2017, Vitali Baumtrok.
// Distributed under the Boost Software License, Version 1.0.
//      (See accompanying file LICENSE or copy at
//        http://www.boost.org/LICENSE_1_0.txt)


package com.github.vbsw.urlsaver.app.window.urls;


import com.github.vbsw.urlsaver.app.App;
import com.github.vbsw.urlsaver.utility.Parser;
import com.github.vbsw.urlsaver.utility.SortedUniqueStringList;

import javafx.beans.property.SimpleBooleanProperty;


/**
 * @author Vitali Baumtrok
 */
final class UrlsProperties {

	private final SortedUniqueStringList tagsTmp = new SortedUniqueStringList();

	public final SimpleBooleanProperty available = new SimpleBooleanProperty();
	public final SimpleBooleanProperty exists = new SimpleBooleanProperty();
	public final SimpleBooleanProperty deleteRequested = new SimpleBooleanProperty();
	public final SimpleBooleanProperty urlModified = new SimpleBooleanProperty();
	public final SimpleBooleanProperty tagsModified = new SimpleBooleanProperty();

	public void init ( final boolean urlsLoaded ) {
		available.set(urlsLoaded);
		exists.set(false);
		deleteRequested.set(false);
		urlModified.set(false);
		tagsModified.set(false);
	}

	public void button_urlSearch_clicked ( ) {
		exists.set(App.scene.tf.url.getText().length() > 0);
		deleteRequested.set(false);
		urlModified.set(false);
		tagsModified.set(false);
	}

	public void button_urlCancel_clicked ( ) {
		exists.set(App.scene.tf.url.getText().length() > 0);
		deleteRequested.set(false);
		urlModified.set(false);
		tagsModified.set(false);
	}

	public void button_urlDelete_clicked ( ) {
		deleteRequested.set(true);
	}

	public void textField_url_changed ( ) {
		final String urlTyped = Parser.trim(App.scene.tf.url.getText());
		final UrlsData urlsData = App.urls.getData();
		final boolean urlExists = urlTyped.length() > 0 && urlsData.getUrlIndex(urlTyped) >= 0;
		final boolean urlModified = urlTyped.length() > 0 && urlsData.getUrlIndex(urlTyped) < 0;

		this.exists.set(urlExists);
		this.urlModified.set(urlModified);
		this.deleteRequested.set(false);
	}

	public void textField_tags_changed ( ) {
		final UrlsData urlsData = App.urls.getData();
		final String urlTyped = Parser.trim(App.scene.tf.url.getText());
		final int urlIndex = urlsData.getUrlIndex(urlTyped);

		if ( urlIndex >= 0 ) {
			final String tagsTyped = Parser.trim(App.scene.ta.tags.getText());
			final SortedUniqueStringList urlTags = urlsData.urlTagsList.get(urlIndex);

			tagsTmp.addStringsSeparatedByWhiteSpace(tagsTyped);
			tagsModified.setValue(tagsTmp.isEqualByStrings(urlTags) == false);
			tagsTmp.clear();

		} else {
			tagsModified.setValue(false);
		}

		this.deleteRequested.set(false);
	}

	public void listViewItem_selected ( ) {
		exists.set(App.scene.tf.url.getText().length() > 0);
	}

	public void button_urlDeleteOK_clicked ( ) {
		deleteRequested.set(false);
	}

	public void allConfirmed ( ) {
		final UrlsData urlsData = App.urls.getData();
		final String urlTyped = Parser.trim(App.scene.tf.url.getText());
		final boolean urlExists = urlTyped.length() > 0 && urlsData.getUrlIndex(urlTyped) >= 0;

		exists.set(urlExists);
		deleteRequested.set(false);
		urlModified.set(false);
		tagsModified.set(false);
	}

	public void button_urlEditOK_clicked ( ) {
		exists.set(true);
		tagsModified.set(false);
	}

	public void button_urlCreateOK_clicked ( ) {
		exists.set(true);
		urlModified.set(false);
	}

}

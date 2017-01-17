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


package com.github.vbsw.urlsaver.binding;


import com.github.vbsw.urlsaver.TaggedWords.Word;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ListView;


/**
 * @author Vitali Baumtrok
 */
public class UrlSelectedBinding extends BooleanBinding {

	private final UrlEnteredBinding urlEntered;
	private final UrlModifiedBinding urlModified;
	private final ListView<Word> urlList;

	public UrlSelectedBinding ( final UrlEnteredBinding urlEntered, final UrlModifiedBinding urlModified, final ListView<Word> urlList ) {
		this.urlEntered = urlEntered;
		this.urlModified = urlModified;
		this.urlList = urlList;

		bind(urlEntered,urlModified);
	}

	@Override
	protected boolean computeValue ( ) {
		final boolean urlItemSelected = urlList.getSelectionModel().getSelectedItem() != null;

		return !urlEntered.getValue() && !urlModified.getValue() && urlItemSelected;
	}

}

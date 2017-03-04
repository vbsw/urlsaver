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


package com.github.vbsw.urlsaver.scene.bindings;


import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.app.App;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectProperty;


/**
 * @author Vitali Baumtrok
 */
public class UrlSelectedBinding extends BooleanBinding {

	public UrlSelectedBinding ( ) {
		final ReadOnlyObjectProperty<String> selectedUrlProperty = App.scene.lv.urls.getSelectionModel().selectedItemProperty();

		bind(selectedUrlProperty);
	}

	@Override
	protected boolean computeValue ( ) {
		final String url = App.scene.lv.urls.getSelectionModel().getSelectedItem();

		return url != null && Parser.isWhiteSpace(url) == false;
	}

}

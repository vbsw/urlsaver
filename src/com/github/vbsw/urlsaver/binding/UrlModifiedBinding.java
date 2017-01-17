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


import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


/**
 * @author Vitali Baumtrok
 */
public class UrlModifiedBinding extends BooleanBinding {

	private final UrlEnteredBinding newUrlEntered;
	private final TagsEnteredBinding tagsEntered;
	private final TextField urlTF;
	private final TextArea tagsTA;

	public UrlModifiedBinding ( final UrlEnteredBinding urlEntered, final TagsEnteredBinding tagsEntered, final TextField urlTF, final TextArea tagsTA ) {
		this.newUrlEntered = urlEntered;
		this.tagsEntered = tagsEntered;
		this.urlTF = urlTF;
		this.tagsTA = tagsTA;

		bind(urlEntered,tagsEntered);
	}

	@Override
	protected boolean computeValue ( ) {
		final boolean urlTextAvailable = !urlTF.getText().isEmpty();
		final boolean tagsTextAvailable = !tagsTA.getText().isEmpty();

		return !newUrlEntered.getValue() && tagsEntered.getValue() && urlTextAvailable && tagsTextAvailable;
	}

}

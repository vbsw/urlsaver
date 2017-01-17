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


import java.util.ArrayList;

import com.github.vbsw.urlsaver.Parser;
import com.github.vbsw.urlsaver.TaggedWords;

import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;


/**
 * @author Vitali Baumtrok
 */
public class TagsEnteredBinding extends BooleanBinding {

	private final ListView<TaggedWords.Word> urlList;
	private final TextArea tagsTA;

	public TagsEnteredBinding ( final ListView<TaggedWords.Word> urlList, final TextArea tagsTA ) {
		this.urlList = urlList;
		this.tagsTA = tagsTA;

		bind(tagsTA.textProperty());
	}

	@Override
	protected boolean computeValue ( ) {
		final String tagsAsOneString = tagsTA.getText();
		final ArrayList<String> tagStringList = Parser.toStringArray(tagsAsOneString);
		final boolean newTags = hasNewTags(tagStringList);
		
		return newTags;
	}

	private boolean hasNewTags ( final ArrayList<String> tagStringList ) {
		final TaggedWords.Word selectedItem = urlList.getSelectionModel().getSelectedItem();

		if ( selectedItem == null ) {
			return !tagStringList.isEmpty();

		} else {
			for ( String tagString: tagStringList ) {
				if ( !selectedItem.hasTag(tagString) ) {
					return true;
				}
			}
			return false;
		}
	}

}

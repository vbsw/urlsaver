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


package com.github.vbsw.urlsaver.scene;


import javafx.scene.Parent;
import javafx.scene.control.TextArea;


/**
 * @author Vitali Baumtrok
 */
public final class TextAreas {

	public final TextArea tags;

	TextAreas ( final Parent root ) {
		final String tagsTextAreaSelector = "#tags_ta"; //$NON-NLS-1$

		tags = (TextArea) root.lookup(tagsTextAreaSelector);
	}

}
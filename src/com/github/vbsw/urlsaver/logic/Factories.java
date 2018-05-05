/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.logic;


import java.nio.file.Path;

import com.github.vbsw.urlsaver.db.DBFiles;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;


/**
 * @author Vitali Baumtrok
 */
public class Factories {

	public static ListCell<Path> filesCellFactory ( ListView<Path> param ) {
		final ListCell<Path> listCell = new FilePathListCell();
		return listCell;
	}

	public static ListCell<String> urlsCellFactory ( ListView<String> param ) {
		// TODO Auto-generated method stub
		return null;
	}

	private static final class FilePathListCell extends ListCell<Path> {

		@Override
		protected void updateItem ( final Path item, final boolean empty ) {
			super.updateItem(item,empty);
			if ( empty ) {
				setText(null);
				setOnMouseClicked(null);
			} else {
				final int listIndex = DBFiles.getIndex(item);
				final String text = DBFiles.getLabel(listIndex);
				setText(text);
				setOnMouseClicked(event -> Callbacks.filePathListViewItem_clicked(event));
			}
		}

	}

}

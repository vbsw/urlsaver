/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.gui;


import javafx.scene.Parent;
import javafx.scene.control.Label;


/**
 * @author Vitali Baumtrok
 */
public class Labels {

	public static final URLsCount urlsCount = new URLsCount();
	public static final TagsCount tagsCount = new TagsCount();
	public static final FileSize fileSize = new FileSize();

	public static class CustomLabel {
		public Label control;
	}

	public static void build ( final Parent root ) {
		urlsCount.build(root);
		tagsCount.build(root);
		fileSize.build(root);
	}

	public static final class URLsCount extends CustomLabel {
		private void build ( final Parent root ) {
			control = (Label) root.lookup("#files_urls_count_label");
		}
	}

	public static final class TagsCount extends CustomLabel {
		private void build ( final Parent root ) {
			control = (Label) root.lookup("#files_tags_count_label");
		}
	}

	public static final class FileSize extends CustomLabel {
		private void build ( final Parent root ) {
			control = (Label) root.lookup("#files_size_label");
		}
	}
}

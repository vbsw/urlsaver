/*
 *    Copyright 2018, Vitali Baumtrok (vbsw@mailbox.org).
 * Distributed under the Boost Software License, Version 1.0.
 *      (See accompanying file LICENSE or copy at
 *        http://www.boost.org/LICENSE_1_0.txt)
 */


package com.github.vbsw.urlsaver.api;


/**
 * @author Vitali Baumtrok
 */
public class ResourceVariants {

	private Resource defaultResource;
	private Resource savedResource;
	private Resource modifiedResource;

	public Resource getDefault ( ) {
		return defaultResource;
	}

	public void setDefault ( final Resource defaultResource ) {
		this.defaultResource = defaultResource;
	}

	public Resource getSaved ( ) {
		return savedResource;
	}

	public void setSaved ( final Resource savedResource ) {
		this.savedResource = savedResource;
	}

	public Resource getModified ( ) {
		return modifiedResource;
	}

	public void setModified ( final Resource modifiedResource ) {
		this.modifiedResource = modifiedResource;
	}

	public boolean isSaved ( ) {
		return savedResource.equals(modifiedResource);
	}

	public void resetModifiedToDefault ( ) {
		modifiedResource = defaultResource;
	}

	public void resetSavedToDefault ( ) {
		savedResource = defaultResource;
	}

	public void resetSavedToModified ( ) {
		savedResource = modifiedResource;
	}

}

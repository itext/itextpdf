/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */
package com.lowagie.tools.arguments;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filters images in a FileChooser.
 */
public class ImageFilter extends FileFilter {

	/** Array with all kinds of image extensions. */
	public static final String[] IMAGES = new String[8];
	static {
		IMAGES[0] = ".jpg";
		IMAGES[1] = ".jpeg";
		IMAGES[2] = ".png";
		IMAGES[3] = ".gif";
		IMAGES[4] = ".bmp";
		IMAGES[5] = ".wmf";
		IMAGES[6] = ".tif";
		IMAGES[7] = ".tiff";
	}
	
	/** array that enables you to filter on imagetypes. */
	public boolean[] filter = new boolean[8];

	/**
	 * Constructs an ImageFilter allowing all images.
	 */
	public ImageFilter() {
		for (int i = 0; i < filter.length; i++) {
			filter[i] = true;
		}
	}
	
	/**
	 * Constructs an ImageFilter allowing some images.
	 * @param jpeg indicates if jpegs are allowed
	 * @param png indicates if pngs are allowed
	 * @param gif indicates if gifs are allowed
	 * @param bmp indicates if bmps are allowed
	 * @param wmf indicates if wmfs are allowed
	 * @param tiff indicates if tiffs are allowed
	 */
	public ImageFilter(boolean jpeg, boolean png, boolean gif, boolean bmp, boolean wmf, boolean tiff) {
		if (jpeg) {
			filter[0] = true;
			filter[1] = true;
		}
		if (png) {
			filter[2] = true;
		}
		if (gif) {
			filter[3] = true;
		}
		if (bmp) {
			filter[4] = true;
		}
		if (wmf) {
			filter[5] = true;
		}
		if (tiff) {
			filter[6] = true;
			filter[7] = true;
		}
	}
	
	/**
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File f) {
		if (f.isDirectory()) return true;
		for (int i = 0; i < IMAGES.length; i++) {
			if (filter[i] && f.getName().endsWith(IMAGES[i])) return true;
		}
		return false;
	}

	/**
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	public String getDescription() {
		return "Image files";
	}
}
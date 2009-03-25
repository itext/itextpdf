/*
 * $Id$
 *
 * Copyright 2009 by Bruno Lowagie.
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
 * the Initial Developer are Copyright (C) 1999-2009 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2009 by Paulo Soares. All Rights Reserved.
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

package com.lowagie.text.pdf.richmedia;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;

/**
 * The RichMediaWindow dictionary stores the dimensions and position of the
 * floating window presented to the user. It is used only if Style is set
 * to Windowed.
 * See ExtensionLevel 3 p84
 * @see	RichMediaPresentation
 * @since	2.1.6
 */
public class RichMediaWindow extends PdfDictionary {

	/**
	 * Creates a RichMediaWindow dictionary.
	 */
	public RichMediaWindow() {
		super(PdfName.RICHMEDIAWINDOW);
	}
	
	/**
	 * Sets a dictionary with keys Default, Max, and Min describing values for
	 * the width of the Window in default user space units.
	 * @param	defaultWidth	the default width
	 * @param	maxWidth		the maximum width
	 * @param	minWidth		the minimum width
	 */
	public void setWidth(float defaultWidth, float maxWidth, float minWidth) {
		put(PdfName.WIDTH, createDimensionDictionary(defaultWidth, maxWidth, minWidth));
	}

	/**
	 * Sets a dictionary with keys Default, Max, and Min describing values for
	 * the height of the Window in default user space units.
	 * @param	defaultHeight	the default height
	 * @param	maxHeight		the maximum height
	 * @param	minHeight		the minimum height
	 */
	public void setHeight(float defaultHeight, float maxHeight, float minHeight) {
		put(PdfName.HEIGHT, createDimensionDictionary(defaultHeight, maxHeight, minHeight));
	}
	
	/**
	 * Creates a dictionary that can be used for the HEIGHT and WIDTH entries
	 * of the RichMediaWindow dictionary.
	 * @param	d		the default
	 * @param	max		the maximum
	 * @param	min		the minimum
	 */
	private PdfDictionary createDimensionDictionary(float d, float max, float min) {
		PdfDictionary dict = new PdfDictionary();
		dict.put(PdfName.DEFAULT, new PdfNumber(d));
		dict.put(PdfName.MAX_CAMEL_CASE, new PdfNumber(max));
		dict.put(PdfName.MIN_CAMEL_CASE, new PdfNumber(min));
		return dict;
	}
	
	/**
	 * Sets a RichMediaPosition dictionary describing the position of the RichMediaWindow.
	 * @param	position	a RichMediaPosition object
	 */
	public void setPosition(RichMediaPosition position) {
		put(PdfName.POSITION, position);
	}
}

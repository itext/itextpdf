/*
 * @(#)PdfFontDictionary.java		0.22 2000/02/02
 *       release rugPdf0.10:		0.03 99/03/31
 *               rugPdf0.20:		0.12 99/11/30
 *               iText0.3:			0.22 2000/02/14
 *               iText0.35:         0.22 2000/08/11
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *  
 */

package com.lowagie.text.pdf;

import java.util.Iterator;

/**
 * <CODE>PdfFontDictionary</CODE> is a <CODE>PdfResource</CODE>, containing a dictionary of <CODE>PdfFont</CODE>s.
 *
 * @see		PdfResource
 * @see		PdfResources
 *
 * @author  bruno@lowagie.com
 * @version 0.22 2000/02/02
 * @since   rugPdf0.10
 */

class PdfFontDictionary extends PdfDictionary implements PdfResource {

// constructors

	/**
	 * Constructs a new <CODE>PdfFontDictionary</CODE>.
	 *
	 * @since		iText0.30
	 */

	PdfFontDictionary() { 
		super();
	}

// methods

	/**
	 * Returns the name of a resource.
	 *
	 * @return		a <CODE>PdfName</CODE>.
	 *
	 * @since		rugPdf0.10
	 */

	public PdfName key() {
		return PdfName.FONT;
	}

	/**
	 * Returns the object that represents the resource.
	 *
	 * @return		a <CODE>PdfObject</CODE>
	 *
	 * @since		rugPdf0.10
	 */

	public PdfObject value() {
		return this;
	}

	/**
	 * Checks if the <CODE>FontDictionary</CODE> allready contains a
	 * <CODE>PdfFont</CODE> with this name.
	 *
	 * @return		<CODE>true</CODE> if a font with this name allready exists,
	 *				<CODE>false</CODE> otherwise
	 *
	 * @since		iText0.30
	 */

	boolean contains(PdfFont font) {
		return treeMap.containsKey(font.getName());
	}

	/**
	 * Checks if the <CODE>PdfFontDictionary</CODE> contains at least
	 * one object.
	 *
	 * @return		<CODE>true</CODE> if an object was found
	 *				<CODE>false</CODE> otherwise
	 *
	 * @since		iText0.38
	 */

	boolean containsFont() {
		return treeMap.size() > 0;
	}
}
/*
 * $Id$
 * $Name$
 * 
 * Copyright 2001 by Bruno Lowagie.
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
 */

package com.lowagie.text.pdf;

/**
 * <CODE>PdfFormObject</CODE> is a type of XObject containing a <CODE>PdfContents</CODE>-object.
 *
 * @author  bruno@lowagie.com
 */

public class PdfFormXObject extends PdfStream {

// public static final variables

	/** This is a PdfNumber representing 0. */
	public static final PdfNumber ZERO = new PdfNumber(0);

	/** This is a PdfNumber representing 1. */
	public static final PdfNumber ONE = new PdfNumber(1);

	/** This is the 1 - matrix. */
	public static final PdfArray MATRIX = new PdfArray();

	static {
		MATRIX.add(ONE);
		MATRIX.add(ZERO);
		MATRIX.add(ZERO);
		MATRIX.add(ONE);
		MATRIX.add(ZERO);
		MATRIX.add(ZERO);
	}

// membervariables

	/** This is the <CODE>PdfName</CODE> of the XObject. */
	protected PdfName name = null;

// constructor

	/**
	 * Constructs a <CODE>PdfFormXObject</CODE>-object.
	 *
	 * @param		name		the name of the XObject
	 * @param		contents	the contents of this XObject
	 * @param		resources	the resources used in the contents
	 * @param		llx			lower left x
	 * @param		lly			lower left y
	 * @param		urx			upper right x
	 * @param		ury			upper right y
	 */

	protected PdfFormXObject(String name, PdfContents contents, PdfDictionary resources, int llx, int lly, int urx, int ury) throws BadPdfFormatException {
		super();
		this.name = new PdfName(name);
		dictionary.put(PdfName.TYPE, PdfName.XOBJECT);
		dictionary.put(PdfName.SUBTYPE, PdfName.FORM);
		dictionary.put(PdfName.NAME, this.name);
		dictionary.put(PdfName.RESOURCES, resources);
		dictionary.put(PdfName.BBOX, new PdfRectangle(llx, lly, urx, ury));
		dictionary.put(PdfName.FORMTYPE, ONE);
		dictionary.put(PdfName.MATRIX, MATRIX);
	}

	/**
	 * Returns the <CODE>PdfName</CODE> of the image.
	 *
	 * @return		the name
	 */

	public final PdfName name() {
		return name;
	}
}
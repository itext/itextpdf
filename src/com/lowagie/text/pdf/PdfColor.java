/*
 * @(#)PdfColor.java				0.37 2000/10/05
 *       release iText0.37:         0.37 2000/10/05
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

/**
 * A <CODE>PdfColor</CODE> defines a Color (it's a <CODE>PdfArray</CODE> containing 3 values).
 *
 * @see		PdfDictionary
 *
 * @author  bruno@lowagie.com
 * @version 0.37 2000/10/05
 * @since   rugPdf0.37
 */

class PdfColor extends PdfArray {

// constructors

	/**
	 * Constructs a new <CODE>PdfColor</CODE>.
	 *
	 * @param		red			a value between 0 and 255
	 * @param		green		a value between 0 and 255
	 * @param		blue		a value between 0 and 255
	 *
	 * @since		iText0.37
	 */

	PdfColor(int red, int green, int blue) { 
		super(new PdfNumber((double)(red & 0xFF) / 0xFF));
		add(new PdfNumber((double)(green & 0xFF) / 0xFF));
		add(new PdfNumber((double)(blue & 0xFF) / 0xFF));
	}
}
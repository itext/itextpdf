/*
 * @(#)PdfTextArray.java			0.22 2000/02/02
 *       release rugPdf0.10:		0.01 99/03/31
 *               rugPdf0.20:		0.13 99/11/30
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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <CODE>PdfTextArray</CODE> defines an array with displacements and <CODE>PdfString</CODE>-objects.
 * <P>
 * A <CODE>TextArray</CODE> is used with the operator <VAR>TJ</VAR> in <CODE>PdfText</CODE>.
 * The first object in this array has to be a <CODE>PdfString</CODE>;
 * see reference manual version 1.3 section 8.7.5, pages 346-347.
 *
 * @author  bruno@lowagie.com
 * @version 0.22 2000/02/02
 * @since   rugPdf0.10
 */

class PdfTextArray extends PdfArray {

// constructors
	

	/**
	 * Constructs an <CODE>PdfArray</CODE>-object, containing 1 <CODE>PdfObject</CODE>.
	 *
	 * @param	object		a <CODE>PdfObject</CODE> that has to be added to the array
	 *
	 * @since   rugPdf0.20
	 */

	PdfTextArray(PdfObject object) throws PdfException {
		super(object);
		if (! object.isString()) {
			throw new PdfException("The first object in a TextArray has to be a PdfString.");
		}
	}

	/**
	 * This constructor may never be used.
	 *
	 * @param	object		a <CODE>PdfArray</CODE> that has to be added to the array
	 *
	 * @since   rugPdf0.20
	 */

	private PdfTextArray(PdfArray array) {
	}

// methods

	/**
	 * Adds a <CODE>PdfObject</CODE> to the <CODE>PdfArray</CODE>.
	 *
	 * @param		object			<CODE>PdfObject</CODE> to add
	 * @return		<CODE>true</CODE> if the object is a <CODE>PdfString</CODE>; <CODE>false</CODE> otherwise
	 *
	 * @since		rugPdf0.10
	 */

	final boolean add(PdfObject object) {
		if (! object.isString()) return false;
		return arrayList.add(object);
	}

	/**
	 * Adds a <CODE>PdfNumber</CODE> and a <CODE>PdfPrintable</CODE> to the <CODE>PdfArray</CODE>.
	 *
	 * @param		number			displacement of the string
	 * @param		text			the text
	 *
	 * @return		<CODE>true</CODE> if there is allready a <CODE>PdfString</CODE> present in the array,
	 *				<CODE>false</CODE> otherwise
	 *
	 * @since		rugPdf0.10
	 */

	final boolean add(PdfNumber number, PdfPrintable text) {
		if (arrayList.size() == 0) {
			return false;
		}
		arrayList.add(number);
		arrayList.add(new PdfString(text));
		return true;
	}
    
    final boolean add(PdfNumber number)
    {
		arrayList.add(number);
		return true;
    }
}
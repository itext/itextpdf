/*
 * @(#)PdfNull.java					0.22 2000/02/02
 *       release rugPdf0.10:		0.04 99/03/30
 *               rugPdf0.20:		0.14 99/11/30
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
 *     
 * Very special thanks to Troy Harrison, Systems Consultant
 * of CNA Life Department-Information Technology
 * Troy.Harrison@cnalife.com <mailto:Troy.Harrison@cnalife.com>
 * His input concerning the changes in version rugPdf0.20 was
 * really very important.
 */

package com.lowagie.text.pdf;

/**
 * <CODE>PdfNull</CODE> is the Null object represented by the keyword <VAR>null</VAR>.
 * <P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.9 (page 53).
 *
 * @see		PdfObject
 *
 * @author  bruno@lowagie.com
 * @version 0.22 2000/02/02
 *
 * @since   rugPdf0.10
 */

class PdfNull extends PdfObject implements PdfPrintable {

// static membervariables

	/** This is an instance of the <CODE>PdfNull</CODE>-object. */
	public static final PdfNull	PDFNULL = new PdfNull();

	/** This is the content of a <CODE>PdfNull</CODE>-object. */
	private static final String CONTENT = "null";

// constructors

	/**
	 * Constructs a <CODE>PdfNull</CODE>-object.
	 * <P>
	 * You never need to do this yourself, you can always use the static final object <VAR>PDFNULL</VAR>.
	 *
	 * @since		rugPdf0.10
	 */

	private PdfNull() {
		super(NULL, CONTENT);
	}

// implementation of the PdfPrintable method(s)

	/**
     * Returns the <CODE>String</CODE>-representation of this <CODE>PdfObject</CODE>.
	 *
	 * @return		a <CODE>String</CODE>
     *
	 * @since		rugPdf0.10
     */

    public final String toString() {
		return NOTHING;
    }
}
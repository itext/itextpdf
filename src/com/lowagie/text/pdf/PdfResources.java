/*
 * @(#)PdfResources.java			0.38 2000/10/06
 *       release rugPdf0.10:		0.03 99/03/30
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

/**
 * <CODE>PdfResources</CODE> is the PDF Resources-object.
 * <P>
 * The marking operations for drawing a page are stored in a stream that is the value of the
 * <B>Contents</B> key in the Page object's dictionary. Each marking context includes a list
 * of the named resources it uses. This resource list is stored as a dictionary that is the
 * value of the context's <B>Resources</B> key, and it serves two functions: it enumerates
 * the named resources in the contents stream, and it established the mapping from the names
 * to the objects used by the marking operations.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 7.5 (page 195-197).
 *
 * @see		PdfResource
 * @see		PdfProcSet
 * @see		PdfFontDictionary
 * @see		PdfPage
 *
 * @author  bruno@lowagie.com
 * @version 0.38 2000/10/06
 * @since   rugPdf0.10
 */

class PdfResources extends PdfDictionary {

// constructor

	/**
	 * Constructs a PDF ResourcesDictionary.
	 *
	 * @since		rugPdf0.10
	 */

	PdfResources() {
		super();
	}

// methods

	/**
	 * Adds a <CODE>PdfResource</CODE> to the ResourcesDictionary.
	 *
	 * @param		resource		a <CODE>PdfResource</CODE>
	 * @return		<CODE>true</CODE>
	 *
	 * @since		rugPdf0.10
	 */

	PdfObject add(PdfResource resource) {
		return put(resource.key(), resource.value());
	}
}
/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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
 * <CODE>PdfNull</CODE> is the Null object represented by the keyword <VAR>null</VAR>.
 * <P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.9 (page 53).
 *
 * @see		PdfObject
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
 */
    
    private PdfNull() {
        super(NULL, CONTENT);
    }
    
    // implementation of the PdfPrintable method(s)
    
/**
 * Returns the <CODE>String</CODE>-representation of this <CODE>PdfObject</CODE>.
 *
 * @return		a <CODE>String</CODE>
 */
    
    public final String toString() {
        return NOTHING;
    }
}
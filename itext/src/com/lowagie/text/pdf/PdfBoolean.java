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
 * <CODE>PdfBoolean</CODE> is the boolean object represented by the keywords <VAR>true</VAR> or <VAR>false</VAR>.
 * <P>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.2 (page 37).
 *
 * @see		PdfObject
 * @see		BadPdfFormatException
 */

class PdfBoolean extends PdfObject implements PdfPrintable {
    
    // static membervariables (possible values of a boolean object)
    static final PdfBoolean PDFTRUE = new PdfBoolean(true);
    static final PdfBoolean PDFFALSE = new PdfBoolean(false);
/** A possible value of <CODE>PdfBoolean</CODE> */
    public static final String TRUE = "true";
    
/** A possible value of <CODE>PdfBoolean</CODE> */
    public static final String FALSE = "false";
    
    // membervariables
    
/** the boolean value of this object */
    private boolean value;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfBoolean</CODE>-object.
 *
 * @param		value			the value of the new <CODE>PdfObject</CODE>
 */
    
    PdfBoolean(boolean value) {
        super(BOOLEAN);
        if (value) {
            setContent(TRUE);
        }
        else {
            setContent(FALSE);
        }
        this.value = value;
    }
    
/**
 * Constructs a <CODE>PdfBoolean</CODE>-object.
 *
 * @param		value			the value of the new <CODE>PdfObject</CODE>, represented as a <CODE>String</CODE>
 *
 * @throws		BadPdfFormatException	thrown if the <VAR>value</VAR> isn't '<CODE>true</CODE>' or '<CODE>false</CODE>'
 */
    
    PdfBoolean(String value) throws BadPdfFormatException {
        super(BOOLEAN, value);
        if (value.equals(TRUE)) {
            this.value = true;
        }
        else if (value.equals(FALSE)) {
            this.value = false;
        }
        else {
            throw new BadPdfFormatException("The value has to be 'true' of 'false', instead of '" + value + "'.");
        }
    }
    
    // methods returning the value of this object
    
/**
 * Returns the <CODE>String</CODE> value of the <CODE>PdfBoolean</CODE>-object.
 *
 * @return		a <CODE>String</CODE> value "true" or "false"
 */
    
    public String toString() {
        if (value) {
            return TRUE;
        }
        else {
            return FALSE;
        }
    }
    
    
/**
 * Returns the primitive value of the <CODE>PdfBoolean</CODE>-object.
 *
 * @return		the actual value of the object.
 */
    
    final boolean booleanValue() {
        return value;
    }
}
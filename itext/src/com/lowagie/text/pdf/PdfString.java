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
 * Very special thanks to Troy Harrison, Systems Consultant
 * of CNA Life Department-Information Technology
 * Troy.Harrison@cnalife.com <mailto:Troy.Harrison@cnalife.com>
 * His input concerning the changes in version rugPdf0.20 was
 * really very important.
 */

package com.lowagie.text.pdf;

import java.io.UnsupportedEncodingException;

/**
 * A <CODE>PdfString</CODE>-class is the PDF-equivalent of a JAVA-<CODE>String</CODE>-object.
 * <P>
 * A string is a sequence of characters delimited by parenthesis. If a string is too long
 * to be conveniently placed on a single line, it may be split across multiple lines by using
 * the backslash character (\) at the end of a line to indicate that the string continues
 * on the following line. Within a string, the backslash character is used as an escape to
 * specify unbalanced parenthesis, non-printing ASCII characters, and the backslash character
 * itself. Use of the \<I>ddd</I> escape sequence is the preferred way to represent characters
 * outside the printable ASCII character set.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.4 (page 37-39).
 *
 * @see		PdfObject
 * @see		BadPdfFormatException
 */

class PdfString extends PdfObject implements PdfPrintable {
    
    // membervariables
    
/** The value of this object. */
    protected String value = NOTHING;
    
/** The encoding. */
    protected String encoding = ENCODING;
    
    // constructors
    
/**
 * Constructs an empty <CODE>PdfString</CODE>-object.
 */
    
    PdfString() {
        super(STRING, NOTHING);
    }
    
/**
 * Constructs a <CODE>PdfString</CODE>-object.
 *
 * @param		content		the content of the string
 */
    
    PdfString(String value) {
        super(STRING, value);
        this.value = value;
    }
    
/**
 * Constructs a <CODE>PdfString</CODE>-object.
 *
 * @param		content		the content of the string
 * @param		encoding	an encoding
 */
    
    PdfString(String value, String encoding) {
        super(STRING, value);
        this.value = value;
        this.encoding = encoding;
    }
    
/**
 * Constructs a <CODE>PdfString</CODE>-object.
 *
 * @param		bytes	an array of <CODE>byte</CODE>
 */
    
    PdfString(byte[] bytes) {
        super(STRING, bytes);
        try {
            this.value = new String(bytes, ENCODING);
        }
        catch(UnsupportedEncodingException uee) {
            this.value = new String(bytes);
        }
    }
    
    // methods overriding some methods in PdfObject
    
/**
 * Returns the PDF representation of this <CODE>PdfString</CODE>.
 *
 * @return		an array of <CODE>byte</CODE>s
 */
    
    final public byte[] toPdf(PdfEncryption crypto) {
        byte b[];
        try {
            b = value.getBytes(encoding);
        }
        catch(UnsupportedEncodingException uee) {
            b = value.getBytes();
        }
        if (crypto != null) {
            crypto.prepareKey();
            crypto.encryptRC4(b);
        }
        return PdfContentByte.escapeString(b);
    }
    
/**
 * Returns the <CODE>String</CODE> value of the <CODE>PdfString</CODE>-object.
 *
 * @return		a <CODE>String</CODE>
 */
    
    public String toString() {
        return value;
    }
    
    // other methods
    
/**
 * Gets the PDF representation of this <CODE>String</CODE> as a <CODE>String</CODE>
 *
 * @return		a <CODE>String</CODE>
 */
    
    byte[] get(PdfEncryption crypto) {
        return toPdf(crypto);
        // we create the StringBuffer that will be the PDF representation of the content
    }
    
/**
 * Tells you if this string is in Chinese, Japanese or Korean.
 */
    
    boolean isCJKEncoding()
    {
        return encoding.equals(CJKFont.CJK_ENCODING);
    }
    
/**
 * Gets the encoding of this string.
 *
 * @return		a <CODE>String</CODE>
 */
    
    String getEncoding()
    {
        return encoding;
    }
}

/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
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

package com.lowagie.text.pdf;

import com.lowagie.text.ExceptionConverter;
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
            throw new ExceptionConverter(uee);
        }
    }
    
    // methods overriding some methods in PdfObject
    
/**
 * Returns the PDF representation of this <CODE>PdfString</CODE>.
 *
 * @return		an array of <CODE>byte</CODE>s
 */
    
    final public byte[] toPdf(PdfWriter writer) {
        byte b[];
        try {
            b = value.getBytes(encoding);
        }
        catch(UnsupportedEncodingException uee) {
            throw new ExceptionConverter(uee);
        }
        PdfEncryption crypto = writer.getEncryption();
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
    
    byte[] get(PdfWriter writer) {
        return toPdf(writer);
        // we create the StringBuffer that will be the PDF representation of the content
    }
    
/**
 * Tells you if this string is in Chinese, Japanese, Korean or Identity-H.
 */
    
    boolean isSpecialEncoding()
    {
        return encoding.equals(CJKFont.CJK_ENCODING) || encoding.equals(BaseFont.IDENTITY_H);
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

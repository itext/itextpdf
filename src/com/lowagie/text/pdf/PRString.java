/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Paulo Soares.
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

class PRString extends PRObject {
    
    // membervariables
    
    // constructors
    
/**
 * Constructs an empty <CODE>PdfString</CODE>-object.
 */
    
    PRString() {
        super(STRING);
    }
    
/**
 * Constructs a <CODE>PdfString</CODE>-object.
 *
 * @param		content		the content of the string
 */
    
    PRString(String value) {
        super(STRING, value);
    }
    
/**
 * Constructs a <CODE>PdfString</CODE>-object.
 *
 * @param		bytes	an array of <CODE>byte</CODE>
 */
    
    PRString(byte[] bytes) {
        super(STRING, bytes);
    }
    
    // methods overriding some methods in PdfObject
    
/**
 * Returns the PDF representation of this <CODE>PdfString</CODE>.
 *
 * @return		an array of <CODE>byte</CODE>s
 */
    
    final public byte[] toPdf(PdfWriter writer) {
        PdfEncryption crypto = writer.getEncryption();
        if (crypto != null) {
            byte b[] = new byte[bytes.length];
            System.arraycopy(bytes, 0, b, 0, bytes.length);
            crypto.prepareKey();
            crypto.encryptRC4(b);
            return PdfContentByte.escapeString(b);
        }
        return PdfContentByte.escapeString(bytes);
    }
}
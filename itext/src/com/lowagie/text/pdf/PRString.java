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
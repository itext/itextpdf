package com.lowagie.text.pdf;

import com.lowagie.text.pdf.ByteBuffer;
/**
 * <CODE>PdfNumber</CODE> provides two types of numbers, integer and real.
 * <P>
 * Integers may be specified by signed or unsigned constants. Reals may only be
 * in decimal format.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.3 (page 37).
 *
 * @see		PdfObject
 * @see		BadPdfFormatException
 */

class PRNumber extends PRObject{
    
    // static membervariables (possible types of a number object)
    
    
    // constructors
    
    /**
     * Constructs a <CODE>PdfNumber</CODE>-object.
     *
     * @param		type			one of the following types: <CODE>INTEGER</CODE> or <CODE>REAL</CODE>
     * @param		content			value of the new <CODE>PdfNumber</CODE>-object
     *
     * @exception	BadPdfFormatException	Signals that a given type doesn't exist of that a given value isn't a number.
     */
    
    PRNumber(String content) {
        super(NUMBER, content);
    }
    
    /**
     * Constructs a new INTEGER <CODE>PdfNumber</CODE>-object.
     *
     * @param		value				value of the new <CODE>PdfNumber</CODE>-object
     */
    
    PRNumber(int value) {
        super(NUMBER, String.valueOf(value));
    }
    
    /**
     * Constructs a new REAL <CODE>PdfNumber</CODE>-object.
     *
     * @param		value				value of the new <CODE>PdfNumber</CODE>-object
     */
    
    PRNumber(float value) {
        super(NUMBER, ByteBuffer.formatDouble(value));
    }
    
    // methods returning the value of this object
    
    
    /**
     * Returns the primitive <CODE>int</CODE> value of this object.
     *
     * @return		a value
     */
    
    final int intValue() {
        return (int)floatValue();
    }
    
    /**
     * Returns the primitive <CODE>double</CODE> value of this object.
     *
     * @return		a value
     */
    
    final float floatValue() {
        return Float.valueOf(toString()).floatValue();
    }
}
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

class PdfNumber extends PdfObject implements PdfPrintable {
    
    // static membervariables (possible types of a number object)
    
/** a possible type of <CODE>PdfNumber</CODE> */
    public static final int INTEGER = 0;
    
/** a possible type of <CODE>PdfNumber</CODE> */
    public static final int REAL = 1;
    
    // membervariables
    
/** type of this <CODE>PdfNumber</CODE> */
    private int numberType;
    
/** actual value of this <CODE>PdfNumber</CODE>, represented as a <CODE>double</CODE> */
    private double value;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfNumber</CODE>-object.
 *
 * @param		type			one of the following types: <CODE>INTEGER</CODE> or <CODE>REAL</CODE>
 * @param		content			value of the new <CODE>PdfNumber</CODE>-object
 *
 * @exception	BadPdfFormatException	Signals that a given type doesn't exist of that a given value isn't a number.
 */
    
    PdfNumber(int type, String content) throws BadPdfFormatException {
        super(NUMBER);
        numberType = type;
        switch (numberType) {
            case INTEGER:
                try {
                    int i = Integer.parseInt(content.trim());
                    setContent(String.valueOf(i));
                    value = 1.0 * i;
                }
                catch (NumberFormatException nfe){
                    throw new BadPdfFormatException(content + " is not a valid integer.");
                }
                break;
            case REAL:
                try {
                    double d = Double.valueOf(content.trim()).doubleValue();
                    setContent(ByteBuffer.formatDouble(d));
                    value = d;
                }
                catch (NumberFormatException nfe){
                    throw new BadPdfFormatException(content + " is not a valid real.");
                }
                break;
                default:
                    throw new BadPdfFormatException("Unknown type of number: " + type);
        }
    }
    
/**
 * Constructs a new INTEGER <CODE>PdfNumber</CODE>-object.
 *
 * @param		value				value of the new <CODE>PdfNumber</CODE>-object
 */
    
    PdfNumber(int value) {
        super(NUMBER, String.valueOf(value));
        this.value = 1.0 * value;
        numberType = INTEGER;
    }
    
/**
 * Constructs a new REAL <CODE>PdfNumber</CODE>-object.
 *
 * @param		value				value of the new <CODE>PdfNumber</CODE>-object
 */
    
    PdfNumber(double value) {
        super(NUMBER, ByteBuffer.formatDouble(value));
        this.value = value;
        numberType = REAL;
    }
    
/**
 * Constructs a new REAL <CODE>PdfNumber</CODE>-object.
 *
 * @param		value				value of the new <CODE>PdfNumber</CODE>-object
 */
    
    PdfNumber(float value) {
        this((double)value);
    }
    
    // methods returning the value of this object
    
/**
 * Returns the <CODE>String</CODE> value of the <CODE>PdfNumber</CODE>-object.
 *
 * @return		a <CODE>String</CODE> value "true" or "false"
 */
    
    public String toString() {
        if (numberType == INTEGER) {
            return String.valueOf((int) value);
        }
        else {
            return ByteBuffer.formatDouble(value);
        }
    }
    
/**
 * Returns the primitive <CODE>int</CODE> value of this object.
 *
 * @return		a value
 */
    
    final int intValue() {
        return (int) value;
    }
    
/**
 * Returns the primitive <CODE>double</CODE> value of this object.
 *
 * @return		a value
 */
    
    final double doubleValue() {
        return value;
    }
    
    // other methods
    
/**
 * Increments the value of the <CODE>PdfNumber</CODE>-object with 1.
 *
 * @return		<CODE>void</CODE>
 */
    
    void increment() {
        value += 1.0;
        setContent(ByteBuffer.formatDouble(value));
    }
}
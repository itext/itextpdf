/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import com.itextpdf.text.error_messages.MessageLocalization;

/**
 * <CODE>PdfNumber</CODE> provides two types of numbers, integer and real.
 * <P>
 * Integers may be specified by signed or unsigned constants. Reals may only be
 * in decimal format.<BR>
 * This object is described in the 'Portable Document Format Reference Manual
 * version 1.7' section 3.3.2 (page 52-53).
 *
 * @see		PdfObject
 * @see		BadPdfFormatException
 */
public class PdfNumber extends PdfObject {

    // CLASS VARIABLES
    
    /**
     * actual value of this <CODE>PdfNumber</CODE>, represented as a
     * <CODE>double</CODE>
     */
    private double value;
    
    // CONSTRUCTORS
    
    /**
     * Constructs a <CODE>PdfNumber</CODE>-object.
     *
     * @param content    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(String content) {
        super(NUMBER);
        try {
            value = Double.parseDouble(content.trim());
            setContent(content);
        }
        catch (NumberFormatException nfe){
            throw new RuntimeException(MessageLocalization.getComposedMessage("1.is.not.a.valid.number.2", content, nfe.toString()));
        }
    }
    
    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type integer.
     *
     * @param value    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(int value) {
        super(NUMBER);
        this.value = value;
        setContent(String.valueOf(value));
    }
    
    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type long.
     *
     * @param value    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(long value) {
        super(NUMBER);
        this.value = value;
        setContent(String.valueOf(value));
    }
    
    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type real.
     *
     * @param value    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(double value) {
        super(NUMBER);
        this.value = value;
        setContent(ByteBuffer.formatDouble(value));
    }
    
    /**
     * Constructs a new <CODE>PdfNumber</CODE>-object of type real.
     *
     * @param value    value of the new <CODE>PdfNumber</CODE>-object
     */
    public PdfNumber(float value) {
        this((double)value);
    }
    
    // methods returning the value of this object
    
    /**
     * Returns the primitive <CODE>int</CODE> value of this object.
     *
     * @return The value as <CODE>int</CODE>
     */
    public int intValue() {
        return (int) value;
    }
    
    /**
     * Returns the primitive <CODE>long</CODE> value of this object.
     *
     * @return The value as <CODE>long</CODE>
     */
    public long longValue() {
        return (long) value;
    }
    
    /**
     * Returns the primitive <CODE>double</CODE> value of this object.
     *
     * @return The value as <CODE>double</CODE>
     */
    public double doubleValue() {
        return value;
    }
    
    /**
     * Returns the primitive <CODE>float</CODE> value of this object.
     *
     * @return The value as <CODE>float</CODE>
     */
    public float floatValue() {
        return (float)value;
    }
    
    // other methods
    
    /**
     * Increments the value of the <CODE>PdfNumber</CODE>-object by 1.
     */
    public void increment() {
        value += 1.0;
        setContent(ByteBuffer.formatDouble(value));
    }
}

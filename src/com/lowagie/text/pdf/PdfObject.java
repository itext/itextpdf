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
 * <CODE>PdfObject</CODE> is the abstract superclass of all PDF objects.
 * <P>
 * PDF supports seven basic types of objects: Booleans, numbers, strings, names,
 * arrays, dictionaries and streams. In addition, PDF provides a null object.
 * Objects may be labeled so that they can be referred to by other objects.<BR>
 * All these basic PDF objects are described in the 'Portable Document Format
 * Reference Manual version 1.3' Chapter 4 (pages 37-54).
 *
 * @see		PdfNull
 * @see		PdfBoolean
 * @see		PdfNumber
 * @see		PdfString
 * @see		PdfName
 * @see		PdfArray
 * @see		PdfDictionary
 * @see		PdfStream
 * @see		PdfIndirectReference
 */

abstract class PdfObject {
    
    // static membervariables (all the possible types of a PdfObject)
    
/** a possible type of <CODE>PdfObject</CODE> */
    static final int NULL = 0;
    
/** a possible type of <CODE>PdfObject</CODE> */
    static final int BOOLEAN = 1;
    
/** a possible type of <CODE>PdfObject</CODE> */
    static final int NUMBER = 2;
    
/** a possible type of <CODE>PdfObject</CODE> */
    static final int STRING = 3;
    
/** a possible type of <CODE>PdfObject</CODE> */
    static final int NAME = 4;
    
/** a possible type of <CODE>PdfObject</CODE> */
    static final int ARRAY = 5;
    
/** a possible type of <CODE>PdfObject</CODE> */
    static final int DICTIONARY = 6;
    
/** a possible type of <CODE>PdfObject</CODE> */
    static final int STREAM = 7;
    
/** This is an empty string used for the <CODE>PdfNull</CODE>-object and for an empty <CODE>PdfString</CODE>-object. */
    public static final String NOTHING = "";
    
/** This is the default encoding to be used for converting Strings into bytes and vice versa. */
    public static final String ENCODING = "Cp1252";
    
/** This is the encoding to be used to output text in Unicode. */
    public static final String TEXT_UNICODE = "UnicodeBig";
    
    // membervariables
    
/** the content of this <CODE>PdfObject</CODE> */
    protected byte[] bytes = new byte[0];
    
/** the type of this <CODE>PdfObject</CODE> */
    protected int type;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> without any <VAR>content</VAR>.
 *
 * @param		type			type of the new <CODE>PdfObject</CODE>
 */
    
    protected PdfObject(int type) {
        this.type = type;
    }
    
/**
 * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> with a certain <VAR>content</VAR>.
 *
 * @param		type			type of the new <CODE>PdfObject</CODE>
 * @param		content			content of the new <CODE>PdfObject</CODE> as a <CODE>String</CODE>.
 */
    
    protected PdfObject(int type, String content) {
        try {
            bytes = content.getBytes(ENCODING);
        }
        catch(UnsupportedEncodingException uee) {
            bytes = content.getBytes();
        }
        this.type = type;
    }
    
/**
 * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR> with a certain <VAR>content</VAR>.
 *
 * @param		type			type of the new <CODE>PdfObject</CODE>
 * @param		bytes			content of the new <CODE>PdfObject</CODE> as an array of <CODE>byte</CODE>.
 */
    
    protected PdfObject(int type, byte[] bytes) {
        this.bytes = bytes;
        this.type = type;
    }
    
    // methods dealing with the content of this object
    
/**
 * Returns the PDF representation of this <CODE>PdfObject</CODE> as an array of <CODE>byte</CODE>s.
 *
 * @return		an array of <CODE>byte</CODE>
 */
    
    public byte[] toPdf(PdfEncryption crypto) {
        return bytes;
    }
    
/**
 * Returns the length of the PDF representation of the <CODE>PdfObject</CODE>.
 * <P>
 * In some cases, namely for <CODE>PdfString</CODE> and <CODE>PdfStream</CODE>,
 * this method differs from the method <CODE>length</CODE> because <CODE>length</CODE>
 * returns the length of the actual content of the <CODE>PdfObject</CODE>.</P>
 * <P>
 * Remark: the actual content of an object is in most cases identical to its representation.
 * The following statement is always true: length() &gt;= pdfLength().</P>
 *
 * @return		a length
 */
    
    final int pdfLength() {
        return toPdf(null).length;
    }
    
/**
 * Returns the <CODE>String</CODE>-representation of this <CODE>PdfObject</CODE>.
 *
 * @return		a <CODE>String</CODE>
 */
    
    public String toString() {
        try {
            return new String(toPdf(null), ENCODING);
        }
        catch (Exception e) {
            return new String(toPdf(null));
        }
    }
    
/**
 * Returns the length of the actual content of the <CODE>PdfObject</CODE>.
 * <P>
 * In some cases, namely for <CODE>PdfString</CODE> and <CODE>PdfStream</CODE>,
 * this method differs from the method <CODE>pdfLength</CODE> because <CODE>pdfLength</CODE>
 * returns the length of the PDF representation of the object, not of the actual content
 * as does the method <CODE>length</CODE>.</P>
 * <P>
 * Remark: the actual content of an object is in some cases identical to its representation.
 * The following statement is always true: length() &gt;= pdfLength().</P>
 *
 * @return		a length
 */
    
    public int length() {
        return toString().length();
    }
    
/**
 * Changes the content of this <CODE>PdfObject</CODE>.
 *
 * @param		content			the new content of this <CODE>PdfObject</CODE>
 * @return		<CODE>void</CODE>
 */
    
    protected void setContent(String content) {
        try {
            bytes = content.getBytes(ENCODING);
        }
        catch(UnsupportedEncodingException uee) {
            bytes = content.getBytes();
        }
    }
    
    // methods dealing with the type of this object
    
/**
 * Returns the type of this <CODE>PdfObject</CODE>.
 *
 * @return		a type
 */
    
    final int type() {
        return type;
    }
    
/**
 * Checks if this <CODE>PdfObject</CODE> is of the type <CODE>PdfNull</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public final boolean isNull() {
        return (this.type == NULL);
    }
    
/**
 * Checks if this <CODE>PdfObject</CODE> is of the type <CODE>PdfBoolean</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public final boolean isBoolean() {
        return (this.type == BOOLEAN);
    }
    
/**
 * Checks if this <CODE>PdfObject</CODE> is of the type <CODE>PdfNumber</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public final boolean isNumber() {
        return (this.type == NUMBER);
    }
    
/**
 * Checks if this <CODE>PdfObject</CODE> is of the type <CODE>PdfString</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public final boolean isString() {
        return (this.type == STRING);
    }
    
/**
 * Checks if this <CODE>PdfObject</CODE> is of the type <CODE>PdfName</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    final boolean isName() {
        return (this.type == NAME);
    }
    
/**
 * Checks if this <CODE>PdfObject</CODE> is of the type <CODE>PdfArray</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    final boolean isArray() {
        return (this.type == ARRAY);
    }
    
/**
 * Checks if this <CODE>PdfObject</CODE> is of the type <CODE>PdfDictionary</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    final boolean isDictionary() {
        return (this.type == DICTIONARY);
    }
    
/**
 * Checks if this <CODE>PdfObject</CODE> is of the type <CODE>PdfStream</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    final boolean isStream() {
        return (this.type == STREAM);
    }
}

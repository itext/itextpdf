/*
 * $Id: PdfObject.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
import com.itextpdf.text.pdf.internal.PdfIsoKeys;

import java.io.IOException;
import java.io.OutputStream;

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
public abstract class PdfObject {

    // CONSTANTS

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int BOOLEAN = 1;

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int NUMBER = 2;

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int STRING = 3;

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int NAME = 4;

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int ARRAY = 5;

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int DICTIONARY = 6;

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int STREAM = 7;

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int NULL = 8;

    /** A possible type of <CODE>PdfObject</CODE> */
    public static final int INDIRECT = 10;

    /** An empty string used for the <CODE>PdfNull</CODE>-object and for an empty <CODE>PdfString</CODE>-object. */
    public static final String NOTHING = "";

    /**
     * This is the default encoding to be used for converting Strings into
     * bytes and vice versa. The default encoding is PdfDocEncoding.
     */
    public static final String TEXT_PDFDOCENCODING = "PDF";

    /** This is the encoding to be used to output text in Unicode. */
    public static final String TEXT_UNICODE = "UnicodeBig";

    // CLASS VARIABLES

    /** The content of this <CODE>PdfObject</CODE> */
    protected byte[] bytes;

    /** The type of this <CODE>PdfObject</CODE> */
    protected int type;

    /** Holds the indirect reference. */
    protected PRIndirectReference indRef;

    // CONSTRUCTORS

    /**
     * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR>
     * without any <VAR>content</VAR>.
     *
     * @param type    type of the new <CODE>PdfObject</CODE>
     */
    protected PdfObject(int type) {
        this.type = type;
    }

    /**
     * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR>
     * with a certain <VAR>content</VAR>.
     *
     * @param type     type of the new <CODE>PdfObject</CODE>
     * @param content  content of the new <CODE>PdfObject</CODE> as a
     *   <CODE>String</CODE>.
     */
    protected PdfObject(int type, String content) {
        this.type = type;
        bytes = PdfEncodings.convertToBytes(content, null);
    }

    /**
     * Constructs a <CODE>PdfObject</CODE> of a certain <VAR>type</VAR>
     * with a certain <VAR>content</VAR>.
     *
     * @param type   type of the new <CODE>PdfObject</CODE>
     * @param bytes  content of the new <CODE>PdfObject</CODE> as an array of
     *   <CODE>byte</CODE>.
     */
    protected PdfObject(int type, byte[] bytes) {
        this.bytes = bytes;
        this.type = type;
    }

    // methods dealing with the content of this object

    /**
     * Writes the PDF representation of this <CODE>PdfObject</CODE> as an
     * array of <CODE>byte</CODE>s to the writer.
     * 
     * @param writer for backwards compatibility
     * @param os     The <CODE>OutputStream</CODE> to write the bytes to.
     * @throws IOException
     */
    public void toPdf(PdfWriter writer, OutputStream os) throws IOException {
        if (bytes != null) {
            PdfWriter.checkPdfIsoConformance(writer, PdfIsoKeys.PDFISOKEY_OBJECT, this);
            os.write(bytes);
        }
    }

    /**
     * Returns the <CODE>String</CODE>-representation of this
     * <CODE>PdfObject</CODE>.
     *
     * @return    a <CODE>String</CODE>
     */
    public String toString() {
        if (bytes == null)
            return super.toString();
        return PdfEncodings.convertToString(bytes, null);
    }

    /**
     * Gets the presentation of this object in a byte array
     * 
     * @return a byte array
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Whether this object can be contained in an object stream.
     * 
     * PdfObjects of type STREAM OR INDIRECT can not be contained in an
     * object stream.
     * 
     * @return <CODE>true</CODE> if this object can be in an object stream.
     *   Otherwise <CODE>false</CODE>
     */
    public boolean canBeInObjStm() {
        switch (type) {
            case NULL:
            case BOOLEAN:
            case NUMBER:
            case STRING:
            case NAME:
            case ARRAY:
            case DICTIONARY:
                return true;
            case STREAM:
            case INDIRECT:
            default:
                return false;
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
     * @return The length as <CODE>int</CODE>
     */
    public int length() {
        return toString().length();
    }

    /**
     * Changes the content of this <CODE>PdfObject</CODE>.
     *
     * @param content    the new content of this <CODE>PdfObject</CODE>
     */
    protected void setContent(String content) {
        bytes = PdfEncodings.convertToBytes(content, null);
    }

    // methods dealing with the type of this object

    /**
     * Returns the type of this <CODE>PdfObject</CODE>.
     * 
     * May be either of:
     * - <VAR>NULL</VAR>: A <CODE>PdfNull</CODE>
     * - <VAR>BOOLEAN</VAR>: A <CODE>PdfBoolean</CODE>
     * - <VAR>NUMBER</VAR>: A <CODE>PdfNumber</CODE>
     * - <VAR>STRING</VAR>: A <CODE>PdfString</CODE>
     * - <VAR>NAME</VAR>: A <CODE>PdfName</CODE>
     * - <VAR>ARRAY</VAR>: A <CODE>PdfArray</CODE>
     * - <VAR>DICTIONARY</VAR>: A <CODE>PdfDictionary</CODE>
     * - <VAR>STREAM</VAR>: A <CODE>PdfStream</CODE>
     * - <VAR>INDIRECT</VAR>: ><CODE>PdfIndirectObject</CODE>
     *
     * @return The type
     */
    public int type() {
        return type;
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfNull</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isNull() {
        return (type == NULL);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfBoolean</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isBoolean() {
        return (type == BOOLEAN);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfNumber</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isNumber() {
        return (type == NUMBER);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfString</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isString() {
        return (type == STRING);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfName</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isName() {
        return (type == NAME);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfArray</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isArray() {
        return (type == ARRAY);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfDictionary</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isDictionary() {
        return (type == DICTIONARY);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfStream</CODE>.
     *
     * @return <CODE>true</CODE> or <CODE>false</CODE>
     */
    public boolean isStream() {
        return (type == STREAM);
    }

    /**
     * Checks if this <CODE>PdfObject</CODE> is of the type
     * <CODE>PdfIndirectObject</CODE>.
     * 
     * @return <CODE>true</CODE> if this is an indirect object,
     *   otherwise <CODE>false</CODE>
     */
    public boolean isIndirect() {
        return (type == INDIRECT);
    }

    /**
     * Get the indirect reference
     * 
     * @return A <CODE>PdfIndirectReference</CODE>
     */
    public PRIndirectReference getIndRef() {
        return indRef;
    }

    /**
     * Set the indirect reference
     * 
     * @param indRef New value as a <CODE>PdfIndirectReference</CODE>
     */
    public void setIndRef(PRIndirectReference indRef) {
        this.indRef = indRef;
    }
}

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

import java.io.IOException;
import java.io.OutputStream;

import com.itextpdf.text.DocWriter;

/**
 * <CODE>PdfIndirectObject</CODE> is the Pdf indirect object.
 * <P>
 * An <I>indirect object</I> is an object that has been labeled so that it can be referenced by
 * other objects. Any type of <CODE>PdfObject</CODE> may be labeled as an indirect object.<BR>
 * An indirect object consists of an object identifier, a direct object, and the <B>endobj</B>
 * keyword. The <I>object identifier</I> consists of an integer <I>object number</I>, an integer
 * <I>generation number</I>, and the <B>obj</B> keyword.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.7'
 * section 3.2.9 (page 63-65).
 *
 * @see		PdfObject
 * @see		PdfIndirectReference
 */

public class PdfIndirectObject {
    
    // membervariables
    
/** The object number */
    protected int number;
    
/** the generation number */
    protected int generation = 0;
    
    static final byte STARTOBJ[] = DocWriter.getISOBytes(" obj\n");
    static final byte ENDOBJ[] = DocWriter.getISOBytes("\nendobj\n");
    static final int SIZEOBJ = STARTOBJ.length + ENDOBJ.length;
    protected PdfObject object;
    protected PdfWriter writer;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfIndirectObject</CODE>.
 *
 * @param		number			the object number
 * @param		object			the direct object
 */
    
    protected PdfIndirectObject(int number, PdfObject object, PdfWriter writer) {
        this(number, 0, object, writer);
    }
    
    PdfIndirectObject(PdfIndirectReference ref, PdfObject object, PdfWriter writer) {
        this(ref.getNumber(),ref.getGeneration(),object,writer);
    }
/**
 * Constructs a <CODE>PdfIndirectObject</CODE>.
 *
 * @param		number			the object number
 * @param		generation		the generation number
 * @param		object			the direct object
 */
    
    PdfIndirectObject(int number, int generation, PdfObject object, PdfWriter writer) {
        this.writer = writer;
        this.number = number;
        this.generation = generation;
        this.object = object;
        PdfEncryption crypto = null;
        if (writer != null)
            crypto = writer.getEncryption();
        if (crypto != null) {
            crypto.setHashKey(number, generation);
        }
    }
    
    // methods
    
/**
 * Return the length of this <CODE>PdfIndirectObject</CODE>.
 *
 * @return		the length of the PDF-representation of this indirect object.
 */
    
//    public int length() {
//        if (isStream)
//            return bytes.size() + SIZEOBJ + stream.getStreamLength(writer);
//        else
//            return bytes.size();
//    }
    
    
/**
 * Returns a <CODE>PdfIndirectReference</CODE> to this <CODE>PdfIndirectObject</CODE>.
 *
 * @return		a <CODE>PdfIndirectReference</CODE>
 */
    
    public PdfIndirectReference getIndirectReference() {
        return new PdfIndirectReference(object.type(), number, generation);
    }
    
/**
 * Writes efficiently to a stream
 *
 * @param os the stream to write to
 * @throws IOException on write error
 */
    protected void writeTo(OutputStream os) throws IOException
    {
        os.write(DocWriter.getISOBytes(String.valueOf(number)));
        os.write(' ');
        os.write(DocWriter.getISOBytes(String.valueOf(generation)));
        os.write(STARTOBJ);
        object.toPdf(writer, os);
        os.write(ENDOBJ);
    }

    @Override
    public String toString() {
        return new StringBuffer().append(number).append(' ').append(generation).append(" R: ").append(object != null ? object.toString(): "null").toString();
    }
}

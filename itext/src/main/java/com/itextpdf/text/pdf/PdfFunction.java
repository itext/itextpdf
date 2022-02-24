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

import com.itextpdf.text.ExceptionConverter;
/** Implements PDF functions.
 *
 * @author Paulo Soares
 */
public class PdfFunction {
    
    protected PdfWriter writer;
    
    protected PdfIndirectReference reference;
    
    protected PdfDictionary dictionary;
    
    /** Creates new PdfFunction */
    protected PdfFunction(PdfWriter writer) {
        this.writer = writer;
    }
    
    PdfIndirectReference getReference() {
        try {
            if (reference == null) {
                reference = writer.addToBody(dictionary).getIndirectReference();
            }
        }
        catch (IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
        return reference;
    }
        
    public static PdfFunction type0(PdfWriter writer, float domain[], float range[], int size[],
        int bitsPerSample, int order, float encode[], float decode[], byte stream[]) {
        PdfFunction func = new PdfFunction(writer);
        func.dictionary = new PdfStream(stream);
        ((PdfStream)func.dictionary).flateCompress(writer.getCompressionLevel());
        func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(0));
        func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
        func.dictionary.put(PdfName.RANGE, new PdfArray(range));
        func.dictionary.put(PdfName.SIZE, new PdfArray(size));
        func.dictionary.put(PdfName.BITSPERSAMPLE, new PdfNumber(bitsPerSample));
        if (order != 1)
            func.dictionary.put(PdfName.ORDER, new PdfNumber(order));
        if (encode != null)
            func.dictionary.put(PdfName.ENCODE, new PdfArray(encode));
        if (decode != null)
            func.dictionary.put(PdfName.DECODE, new PdfArray(decode));
        return func;
    }

    public static PdfFunction type2(PdfWriter writer, float domain[], float range[], float c0[], float c1[], float n) {
        PdfFunction func = new PdfFunction(writer);
        func.dictionary = new PdfDictionary();
        func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(2));
        func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
        if (range != null)
            func.dictionary.put(PdfName.RANGE, new PdfArray(range));
        if (c0 != null)
            func.dictionary.put(PdfName.C0, new PdfArray(c0));
        if (c1 != null)
            func.dictionary.put(PdfName.C1, new PdfArray(c1));
        func.dictionary.put(PdfName.N, new PdfNumber(n));
        return func;
    }

    public static PdfFunction type3(PdfWriter writer, float domain[], float range[], PdfFunction functions[], float bounds[], float encode[]) {
        PdfFunction func = new PdfFunction(writer);
        func.dictionary = new PdfDictionary();
        func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(3));
        func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
        if (range != null)
            func.dictionary.put(PdfName.RANGE, new PdfArray(range));
        PdfArray array = new PdfArray();
        for (int k = 0; k < functions.length; ++k)
            array.add(functions[k].getReference());
        func.dictionary.put(PdfName.FUNCTIONS, array);
        func.dictionary.put(PdfName.BOUNDS, new PdfArray(bounds));
        func.dictionary.put(PdfName.ENCODE, new PdfArray(encode));
        return func;
    }
    
    public static PdfFunction type4(PdfWriter writer, float domain[], float range[], String postscript) {
        byte b[] = new byte[postscript.length()];
        for (int k = 0; k < b.length; ++k)
            b[k] = (byte)postscript.charAt(k);
        PdfFunction func = new PdfFunction(writer);
        func.dictionary = new PdfStream(b);
        ((PdfStream)func.dictionary).flateCompress(writer.getCompressionLevel());
        func.dictionary.put(PdfName.FUNCTIONTYPE, new PdfNumber(4));
        func.dictionary.put(PdfName.DOMAIN, new PdfArray(domain));
        func.dictionary.put(PdfName.RANGE, new PdfArray(range));
        return func;
    }
}

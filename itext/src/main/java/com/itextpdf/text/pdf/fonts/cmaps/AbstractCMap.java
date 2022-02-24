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
package com.itextpdf.text.pdf.fonts.cmaps;

import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfEncodings;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfString;

/**
 *
 * @author psoares
 */
public abstract class AbstractCMap {

    private String cmapName;
    private String registry;
    private String ordering;
    private int supplement;
    
    public String getName() {
        return cmapName;
    }

    void setName(String cmapName) {
        this.cmapName = cmapName;
    }
    
    public String getOrdering() {
        return ordering;
    }

    void setOrdering(String ordering) {
        this.ordering = ordering;
    }
    
    public String getRegistry() {
        return registry;
    }

    void setRegistry(String registry) {
        this.registry = registry;
    }
    
    public int getSupplement() {
        return supplement;
    }
    
    void setSupplement(int supplement) {
        this.supplement = supplement;
    }

    abstract void addChar(PdfString mark, PdfObject code);
    
    void addRange(PdfString from, PdfString to, PdfObject code) {
        byte[] a1 = decodeStringToByte(from);
        byte[] a2 = decodeStringToByte(to);
        if (a1.length != a2.length || a1.length == 0)
            throw new IllegalArgumentException("Invalid map.");
        byte[] sout = null;
        if (code instanceof PdfString)
            sout = decodeStringToByte((PdfString)code);
        int start = byteArrayToInt(a1);
        int end = byteArrayToInt(a2);
        for (int k = start; k <= end; ++k) {
            intToByteArray(k, a1);
            PdfString s = new PdfString(a1);
            s.setHexWriting(true);
            if (code instanceof PdfArray) {
                addChar(s, ((PdfArray)code).getPdfObject(k - start));
            }
            else if (code instanceof PdfNumber) {
                int nn = ((PdfNumber)code).intValue() + k - start;
                addChar(s, new PdfNumber(nn));
            }
            else if (code instanceof PdfString) {
                PdfString s1 = new PdfString(sout);
                s1.setHexWriting(true);
                ++sout[sout.length - 1];
                addChar(s, s1);
            }
        }
    }
    
    private static void intToByteArray(int v, byte[] b) {
        for (int k = b.length - 1; k >= 0; --k) {
            b[k] = (byte)v;
            v = v >>> 8;
        }
    }
    
    private static int byteArrayToInt(byte[] b) {
        int v = 0;
        for (int k = 0; k < b.length; ++k) {
            v = v << 8;
            v |= b[k] & 0xff;
        }
        return v;
    }
    
    public static byte[] decodeStringToByte(PdfString s) {
        byte[] b = s.getBytes();
        byte[] br = new byte[b.length];
        System.arraycopy(b, 0, br, 0, b.length);
        return br;
    }

    public String decodeStringToUnicode(PdfString ps) {
        if (ps.isHexWriting())
            return PdfEncodings.convertToString(ps.getBytes(), "UnicodeBigUnmarked");
        else
            return ps.toUnicodeString();
    }
}

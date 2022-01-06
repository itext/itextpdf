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
import java.util.Arrays;
import java.util.HashMap;

/**
 * Creates a name tree.
 * @author Paulo Soares
 */
public class PdfNameTree {

    private static final int leafSize = 64;

    /**
     * Writes a name tree to a PdfWriter.
     * @param items the item of the name tree. The key is a <CODE>String</CODE>
     * and the value is a <CODE>PdfObject</CODE>. Note that although the
     * keys are strings only the lower byte is used and no check is made for chars
     * with the same lower byte and different upper byte. This will generate a wrong
     * tree name.
     * @param writer the writer
     * @throws IOException on error
     * @return the dictionary with the name tree. This dictionary is the one
     * generally pointed to by the key /Dests, for example
     */
    public static PdfDictionary writeTree(HashMap<String, ? extends PdfObject> items, PdfWriter writer) throws IOException {
        if (items.isEmpty())
            return null;
        String names[] = new String[items.size()];
        names = items.keySet().toArray(names);
        Arrays.sort(names);
        if (names.length <= leafSize) {
            PdfDictionary dic = new PdfDictionary();
            PdfArray ar = new PdfArray();
            for (int k = 0; k < names.length; ++k) {
                ar.add(new PdfString(names[k], null));
                ar.add(items.get(names[k]));
            }
            dic.put(PdfName.NAMES, ar);
            return dic;
        }
        int skip = leafSize;
        PdfIndirectReference kids[] = new PdfIndirectReference[(names.length + leafSize - 1) / leafSize];
        for (int k = 0; k < kids.length; ++k) {
            int offset = k * leafSize;
            int end = Math.min(offset + leafSize, names.length);
            PdfDictionary dic = new PdfDictionary();
            PdfArray arr = new PdfArray();
            arr.add(new PdfString(names[offset], null));
            arr.add(new PdfString(names[end - 1], null));
            dic.put(PdfName.LIMITS, arr);
            arr = new PdfArray();
            for (; offset < end; ++offset) {
                arr.add(new PdfString(names[offset], null));
                arr.add(items.get(names[offset]));
            }
            dic.put(PdfName.NAMES, arr);
            kids[k] = writer.addToBody(dic).getIndirectReference();
        }
        int top = kids.length;
        while (true) {
            if (top <= leafSize) {
                PdfArray arr = new PdfArray();
                for (int k = 0; k < top; ++k)
                    arr.add(kids[k]);
                PdfDictionary dic = new PdfDictionary();
                dic.put(PdfName.KIDS, arr);
                return dic;
            }
            skip *= leafSize;
            int tt = (names.length + skip - 1 )/ skip;
            for (int k = 0; k < tt; ++k) {
                int offset = k * leafSize;
                int end = Math.min(offset + leafSize, top);
                PdfDictionary dic = new PdfDictionary();
                PdfArray arr = new PdfArray();
                arr.add(new PdfString(names[k * skip], null));
                arr.add(new PdfString(names[Math.min((k + 1) * skip, names.length) - 1], null));
                dic.put(PdfName.LIMITS, arr);
                arr = new PdfArray();
                for (; offset < end; ++offset) {
                    arr.add(kids[offset]);
                }
                dic.put(PdfName.KIDS, arr);
                kids[k] = writer.addToBody(dic).getIndirectReference();
            }
            top = tt;
        }
    }

    private static PdfString iterateItems(PdfDictionary dic, HashMap<String, PdfObject> items, PdfString leftOverString) {
        PdfArray nn = (PdfArray)PdfReader.getPdfObjectRelease(dic.get(PdfName.NAMES));
        if (nn != null) {
            for (int k = 0; k < nn.size(); ++k) {
                PdfString s;
                if (leftOverString == null)
                    s = (PdfString)PdfReader.getPdfObjectRelease(nn.getPdfObject(k++));
                else {
                    // this is the leftover string from the previous loop
                    s = leftOverString;
                    leftOverString = null;
                }
                if (k < nn.size()) // could have a mistake int the pdf file
                    items.put(PdfEncodings.convertToString(s.getBytes(), null), nn.getPdfObject(k));
                else
                    return s;
            }
        } else if ((nn = (PdfArray)PdfReader.getPdfObjectRelease(dic.get(PdfName.KIDS))) != null) {
            for (int k = 0; k < nn.size(); ++k) {
                PdfDictionary kid = (PdfDictionary)PdfReader.getPdfObjectRelease(nn.getPdfObject(k));
                leftOverString = iterateItems(kid, items, leftOverString);
            }
        }
        return null;
    }

    public static HashMap<String, PdfObject> readTree(PdfDictionary dic) {
        HashMap<String, PdfObject> items = new HashMap<String, PdfObject>();
        if (dic != null)
            iterateItems(dic, items, null);
        return items;
    }
}

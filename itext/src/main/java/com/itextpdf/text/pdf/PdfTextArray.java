/*
 * $Id: PdfTextArray.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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

import java.util.ArrayList;

/**
 * <CODE>PdfTextArray</CODE> defines an array with displacements and <CODE>PdfString</CODE>-objects.
 * <P>
 * A <CODE>TextArray</CODE> is used with the operator <VAR>TJ</VAR> in <CODE>PdfText</CODE>.
 * The first object in this array has to be a <CODE>PdfString</CODE>;
 * see reference manual version 1.3 section 8.7.5, pages 346-347.
 *       OR
 * see reference manual version 1.6 section 5.3.2, pages 378-379.
 */

public class PdfTextArray{
    ArrayList<Object> arrayList = new ArrayList<Object>();

    // To emit a more efficient array, we consolidate
    // repeated numbers or strings into single array entries.
    // "add( 50 ); add( -50 );" will REMOVE the combined zero from the array.
    // the alternative (leaving a zero in there) was Just Weird.
    // --Mark Storer, May 12, 2008
    private String lastStr;
    private Float lastNum;

    // constructors
    public PdfTextArray(String str) {
        add(str);
    }

    public PdfTextArray() {
    }

    /**
     * Adds a <CODE>PdfNumber</CODE> to the <CODE>PdfArray</CODE>.
     *
     * @param  number   displacement of the string
     */
    public void add(PdfNumber number) {
        add((float) number.doubleValue());
    }

    public void add(float number) {
        if (number != 0) {
            if (lastNum != null) {
                lastNum = new Float(number + lastNum.floatValue());
                if (lastNum.floatValue() != 0) {
                    replaceLast(lastNum);
                } else {
                    arrayList.remove(arrayList.size() - 1);
                }
            } else {
                lastNum = new Float(number);
                arrayList.add(lastNum);
            }

            lastStr = null;
        }
        // adding zero doesn't modify the TextArray at all
    }

    public void add(String str) {
        if (str.length() > 0) {
            if (lastStr != null) {
                lastStr = lastStr + str;
                replaceLast(lastStr);
            } else {
                lastStr = str;
                arrayList.add(lastStr);
            }
            lastNum = null;
        }
        // adding an empty string doesn't modify the TextArray at all
    }

    ArrayList<Object> getArrayList() {
        return arrayList;
    }

    private void replaceLast(Object obj) {
        // deliberately throw the IndexOutOfBoundsException if we screw up.
        arrayList.set(arrayList.size() - 1, obj);
    }
}

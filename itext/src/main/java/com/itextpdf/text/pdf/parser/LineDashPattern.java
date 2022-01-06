/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Pavel Mitrofanov, Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General License for more
 * details. You should have received a copy of the GNU Affero General License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General License.
 *
 * In accordance with Section 7(b) of the GNU Affero General License, a covered
 * work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfArray;

/**
 * Represents the line dash pattern. The line dash pattern shall control the pattern
 * of dashes and gaps used to stroke paths. It shall be specified by a dash array and
 * a dash phase.
 *
 * @since 5.5.6
 */
public class LineDashPattern {

    private PdfArray dashArray;
    private float dashPhase;

    private int currentIndex;
    private int elemOrdinalNumber = 1;
    private DashArrayElem currentElem;

    /**
     * Creates new {@link LineDashPattern} object.
     * @param dashArray The dash array. See {@link #getDashArray()}
     * @param dashPhase The dash phase. See {@link #getDashPhase()}
     */
    public LineDashPattern(PdfArray dashArray, float dashPhase) {
        this.dashArray = new PdfArray(dashArray);
        this.dashPhase = dashPhase;
        initFirst(dashPhase);
    }

    /**
     * Getter for the dash array.
     *
     * The dash array’s elements is number that specify the lengths of
     * alternating dashes and gaps; the numbers are nonnegative. The
     * elements are expressed in user space units.
     *
     * @return The dash array.
     */
    public PdfArray getDashArray() {
        return dashArray;
    }

    /**
     * Setter for the dash array. See {@link #getDashArray()}
     * @param dashArray New dash array.
     */
    public void setDashArray(PdfArray dashArray) {
        this.dashArray = dashArray;
    }

    /**
     * Getter for the dash phase.
     *
     * The dash phase shall specify the distance into the dash pattern at which
     * to start the dash. The elements are expressed in user space units.
     *
     * @return The dash phase.
     */
    public float getDashPhase() {
        return dashPhase;
    }

    /**
     * Setter for the dash phase. See {@link #getDashArray()}
     * @param dashPhase New dash phase.
     */
    public void setDashPhase(float dashPhase) {
        this.dashPhase = dashPhase;
    }

    /**
     * Calculates and returns the next element which is either gap or dash.
     * @return The next dash array's element.
     */
    public DashArrayElem next() {
        DashArrayElem ret = currentElem;

        if (dashArray.size() > 0) {
            currentIndex = (currentIndex + 1) % dashArray.size();
            currentElem = new DashArrayElem(dashArray.getAsNumber(currentIndex).floatValue(),
                    isEven(++elemOrdinalNumber));
        }

        return ret;
    }

    /**
     * Resets the dash array so that the {@link #next()} method will start
     * from the beginning of the dash array.
     */
    public void reset() {
        currentIndex = 0;
        elemOrdinalNumber = 1;
        initFirst(dashPhase);
    }

    /**
     * Checks whether the dashed pattern is solid or not. It's solid when the
     * size of a dash array is even and sum of all the units off in the array
     * is 0.<br/>
     * For example: [3 0 4 0 5 0 6 0] (sum is 0), [3 0 4 0 5 1] (sum is 1).
     */
    public boolean isSolid() {
        if (dashArray.size() % 2 != 0) {
            return false;
        }

        float unitsOffSum = 0;

        for (int i = 1; i < dashArray.size(); i += 2) {
            unitsOffSum += dashArray.getAsNumber(i).floatValue();
        }

        return Float.compare(unitsOffSum, 0) == 0;
    }

    private void initFirst(float phase) {
        if (dashArray.size() > 0) {
            while (phase > 0) {
                phase -= dashArray.getAsNumber(currentIndex).floatValue();
                currentIndex = (currentIndex + 1) % dashArray.size();
                elemOrdinalNumber++;
            }

            if (phase < 0) {
                --elemOrdinalNumber;
                --currentIndex;
                currentElem = new DashArrayElem(-phase, isEven(elemOrdinalNumber));
            } else {
                currentElem = new DashArrayElem(dashArray.getAsNumber(currentIndex).floatValue(),
                        isEven(elemOrdinalNumber));
            }
        }
    }

    private boolean isEven(int num) {
        return (num % 2) == 0;
    }

    public class DashArrayElem {

        private float val;
        private boolean isGap;

        public DashArrayElem(float val, boolean isGap) {
            this.val = val;
            this.isGap = isGap;
        }

        public float getVal() {
            return val;
        }

        public void setVal(float val) {
            this.val = val;
        }

        public boolean isGap() {
            return isGap;
        }

        public void setGap(boolean isGap) {
            this.isGap = isGap;
        }
    }
}

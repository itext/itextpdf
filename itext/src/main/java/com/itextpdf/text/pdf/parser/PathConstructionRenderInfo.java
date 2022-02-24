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
package com.itextpdf.text.pdf.parser;

import java.util.List;

/**
 * Contains information relating to construction the current path.
 *
 * @since 5.5.6
 */
public class PathConstructionRenderInfo {

    /**
     * See {@link com.itextpdf.text.pdf.parser.Path#moveTo(float, float)}
     */
    public static final int MOVETO = 1;

    /**
     * See {@link com.itextpdf.text.pdf.parser.Path#lineTo(float, float)}
     */
    public static final int LINETO = 2;

    /**
     * See {@link com.itextpdf.text.pdf.parser.Path#curveTo(float, float, float, float, float, float)}
     */
    public static final int CURVE_123 = 3;

    /**
     * See {@link com.itextpdf.text.pdf.parser.Path#curveTo(float, float, float, float)}
     */
    public static final int CURVE_23 = 4;

    /**
     * See {@link com.itextpdf.text.pdf.parser.Path#curveFromTo(float, float, float, float)}
     */
    public static final int CURVE_13 = 5;

    /**
     * See {@link com.itextpdf.text.pdf.parser.Path#closeSubpath()}
     */
    public static final int CLOSE = 6;

    /**
     * See {@link com.itextpdf.text.pdf.parser.Path#rectangle(float, float, float, float)}
     */
    public static final int RECT = 7;

    private int operation;
    private List<Float> segmentData;
    private Matrix ctm;

    /**
     * @param operation   Indicates which path-construction operation should be performed.
     * @param segmentData Contains data of a new segment being added to the current path.
     *                    E.g. x, y, w, h for rectangle; x, y for line etc.
     * @param ctm         Current transformation matrix.
     */
    public PathConstructionRenderInfo(int operation, List<Float> segmentData, Matrix ctm) {
        this.operation = operation;
        this.segmentData = segmentData;
        this.ctm = ctm;
    }

    /**
     * See {@link #PathConstructionRenderInfo(int, java.util.List, Matrix)}
     */
    public PathConstructionRenderInfo(int operation, Matrix ctm) {
        this(operation, null, ctm);
    }

    /**
     * @return construction operation should be performed on the current path.
     */
    public int getOperation() {
        return operation;
    }

    /**
     * @return {@link java.util.List} containing data of a new segment (E.g. x, y, w, h for rectangle;
     *         x, y for line etc.) if the specified operation relates to adding the segment to the
     *         current path, <CODE>null</CODE> otherwise.
     */
    public List<Float> getSegmentData() {
        return segmentData;
    }

    /**
     * @return Current transformation matrix.
     */
    public Matrix getCtm() {
        return ctm;
    }
}

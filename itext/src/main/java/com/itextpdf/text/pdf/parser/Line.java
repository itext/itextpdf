/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
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

import com.itextpdf.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a line.
 * @since 5.5.6
 * @deprecated For internal use only. If you want to use iText, please use a dependency on iText 7.
 */
@Deprecated
public class Line implements Shape {

    private final Point2D p1;
    private final Point2D p2;

    /**
     * Constructs a new zero-length line starting at zero.
     */
    public Line() {
        this(0, 0, 0, 0);
    }

    /**
     * Constructs a new line based on the given coordinates.
     */
    public Line(float x1, float y1, float x2, float y2) {
        p1 = new Point2D.Float(x1, y1);
        p2 = new Point2D.Float(x2, y2);
    }

    /**
     * Constructs a new line based on the given coordinates.
     */
    public Line(Point2D p1, Point2D p2) {
        this((float) p1.getX(), (float) p1.getY(), (float) p2.getX(), (float) p2.getY());
    }

    public List<Point2D> getBasePoints() {
        List<Point2D> basePoints = new ArrayList<Point2D>(2);
        basePoints.add(p1);
        basePoints.add(p2);

        return basePoints;
    }
}

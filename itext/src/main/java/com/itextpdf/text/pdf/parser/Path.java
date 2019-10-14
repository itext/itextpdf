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
import java.util.Arrays;
import java.util.List;

/**
 * Paths define shapes, trajectories, and regions of all sorts. They shall be used
 * to draw lines, define the shapes of filled areas, and specify boundaries for clipping
 * other graphics. A path shall be composed of straight and curved line segments, which
 * may connect to one another or may be disconnected.
 * @since 5.5.6
 * @deprecated For internal use only. If you want to use iText, please use a dependency on iText 7.
 */
@Deprecated
public class Path {

    private static final String START_PATH_ERR_MSG = "Path shall start with \"re\" or \"m\" operator";

    private List<Subpath> subpaths = new ArrayList<Subpath>();
    private Point2D currentPoint;

    public Path() {
    }

    public Path(List<? extends Subpath> subpaths) {
        addSubpaths(subpaths);
    }

    /**
     * @return A {@link java.util.List} of subpaths forming this path.
     */
    public List<Subpath> getSubpaths() {
        return subpaths;
    }

    /**
     * Adds the subpath to this path.
     *
     * @param subpath The subpath to be added to this path.
     */
    public void addSubpath(Subpath subpath) {
        subpaths.add(subpath);
        currentPoint = subpath.getLastPoint();
    }

    /**
     * Adds the subpaths to this path.
     *
     * @param subpaths {@link java.util.List} of subpaths to be added to this path.
     */
    public void addSubpaths(List<? extends Subpath> subpaths) {
        if (subpaths.size() > 0) {
            this.subpaths.addAll(subpaths);
            currentPoint = this.subpaths.get(subpaths.size() - 1).getLastPoint();
        }
    }

    /**
     * The current point is the trailing endpoint of the segment most recently added to the current path.
     *
     * @return The current point.
     */
    public Point2D getCurrentPoint() {
        return currentPoint;
    }

    /**
     * Begins a new subpath by moving the current point to coordinates <CODE>(x, y)</CODE>.
     */
    public void moveTo(final float x, final float y) {
        currentPoint = new Point2D.Float(x, y);
        Subpath lastSubpath = getLastSubpath();

        if (lastSubpath != null && lastSubpath.isSinglePointOpen()) {
            lastSubpath.setStartPoint(currentPoint);
        } else {
            subpaths.add(new Subpath(currentPoint));
        }
    }

    /**
     * Appends a straight line segment from the current point to the point <CODE>(x, y)</CODE>.
     */
    public void lineTo(final float x, final float y) {
        if (currentPoint == null) {
            throw new RuntimeException(START_PATH_ERR_MSG);
        }

        Point2D targetPoint = new Point2D.Float(x, y);
        getLastSubpath().addSegment(new Line(currentPoint, targetPoint));
        currentPoint = targetPoint;
    }

    /**
     * Appends a cubic Bezier curve to the current path. The curve shall extend from
     * the current point to the point <CODE>(x3, y3)</CODE>.
     */
    public void curveTo(final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        if (currentPoint == null) {
            throw new RuntimeException(START_PATH_ERR_MSG);
        }
        // Numbered in natural order
        Point2D secondPoint = new Point2D.Float(x1, y1);
        Point2D thirdPoint = new Point2D.Float(x2, y2);
        Point2D fourthPoint = new Point2D.Float(x3, y3);

        List<Point2D> controlPoints = new ArrayList(Arrays.asList(currentPoint, secondPoint, thirdPoint, fourthPoint));
        getLastSubpath().addSegment(new BezierCurve(controlPoints));

        currentPoint = fourthPoint;
    }

    /**
     * Appends a cubic Bezier curve to the current path. The curve shall extend from
     * the current point to the point <CODE>(x3, y3)</CODE> with the note that the current
     * point represents two control points.
     */
    public void curveTo(final float x2, final float y2, final float x3, final float y3) {
        if (currentPoint == null) {
            throw new RuntimeException(START_PATH_ERR_MSG);
        }

        curveTo((float) currentPoint.getX(), (float) currentPoint.getY(), x2, y2, x3, y3);
    }

    /**
     * Appends a cubic Bezier curve to the current path. The curve shall extend from
     * the current point to the point <CODE>(x3, y3)</CODE> with the note that the (x3, y3)
     * point represents two control points.
     */
    public void curveFromTo(final float x1, final float y1, final float x3, final float y3) {
        if (currentPoint == null) {
            throw new RuntimeException(START_PATH_ERR_MSG);
        }

        curveTo(x1, y1, x3, y3, x3, y3);
    }

    /**
     * Appends a rectangle to the current path as a complete subpath.
     */
    public void rectangle(final float x, final float y, final float w, final float h) {
        moveTo(x, y);
        lineTo(x + w, y);
        lineTo(x + w, y + h);
        lineTo(x, y + h);
        closeSubpath();
    }

    /**
     * Closes the current subpath.
     */
    public void closeSubpath() {
        Subpath lastSubpath = getLastSubpath();
        lastSubpath.setClosed(true);

        Point2D startPoint = lastSubpath.getStartPoint();
        moveTo((float) startPoint.getX(), (float) startPoint.getY());
    }

    /**
     * Closes all subpathes contained in this path.
     */
    public void closeAllSubpaths() {
        for (Subpath subpath : subpaths) {
            subpath.setClosed(true);
        }
    }

    /**
     * Adds additional line to each closed subpath and makes the subpath unclosed.
     * The line connects the last and the first points of the subpaths.
     *
     * @returns Indices of modified subpaths.
     */
    public List<Integer> replaceCloseWithLine() {
        List<Integer> modifiedSubpathsIndices = new ArrayList<Integer>();
        int i = 0;

            /* It could be replaced with "for" cycle, because IList in C# provides effective
             * access by index. In Java List interface has at least one implementation (LinkedList)
             * which is "bad" for access elements by index.
             */
        for (Subpath subpath : subpaths) {
            if (subpath.isClosed()) {
                subpath.setClosed(false);
                subpath.addSegment(new Line(subpath.getLastPoint(), subpath.getStartPoint()));
                modifiedSubpathsIndices.add(i);
            }

            ++i;
        }

        return modifiedSubpathsIndices;
    }

    /**
     * Path is empty if it contains no subpaths.
     */
    public boolean isEmpty() {
        return subpaths.size() == 0;
    }

    private Subpath getLastSubpath() {
        return subpaths.size() > 0 ? subpaths.get(subpaths.size() - 1) : null;
    }
}

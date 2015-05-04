/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
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
package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.awt.geom.NoninvertibleTransformException;
import com.itextpdf.awt.geom.Point;
import com.itextpdf.awt.geom.Point2D;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.parser.*;
import com.itextpdf.text.pdf.parser.clipper.*;
import com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpGraphicsState.LineDashPattern;

import java.util.*;

class PdfCleanUpRegionFilter extends RenderFilter {

    private Rectangle rectangle;

    public PdfCleanUpRegionFilter(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Checks if the text is inside render filter region.
     */
    @Override
    public boolean allowText(TextRenderInfo renderInfo) {
        LineSegment ascent = renderInfo.getAscentLine();
        LineSegment descent = renderInfo.getDescentLine();

        Rectangle r1 = new Rectangle(Math.min(descent.getStartPoint().get(0), descent.getEndPoint().get(0)),
                                     descent.getStartPoint().get(1),
                                     Math.max(descent.getStartPoint().get(0), descent.getEndPoint().get(0)),
                                     ascent.getEndPoint().get(1));
        Rectangle r2 = rectangle;

        return intersect(r1, r2);
    }

    @Override
    public boolean allowImage(ImageRenderInfo renderInfo) {
        throw new UnsupportedOperationException();
    }

    /**
     * Calculates intersection of the image and the render filter region in the coordinate system relative to the image.
     */
    protected PdfCleanUpCoveredArea intersection(ImageRenderInfo renderInfo) {
        Rectangle imageRect = calcImageRect(renderInfo);

        if (imageRect == null) {
            return null;
        }

        Rectangle intersectionRect = intersection(imageRect, rectangle);
        Rectangle transformedIntersection = null;

        if (intersectionRect != null) {
            transformedIntersection = transformIntersection(renderInfo.getImageCTM(), intersectionRect);
        }

        return new PdfCleanUpCoveredArea(transformedIntersection, imageRect.equals(intersectionRect));
    }

    protected Path filterStrokePath(Path path, Matrix ctm, float lineWidth, int lineCapStyle,
                                    int lineJoinStyle, float miterLimit, LineDashPattern lineDashPattern) {
        JoinType joinType = getJoinType(lineJoinStyle);
        EndType endType = getEndType(lineCapStyle);

        ClipperOffset offset = new ClipperOffset(miterLimit, PdfCleanUpProcessor.arcTolerance * PdfCleanUpProcessor.floatMultiplier);
        addPath(offset, path, joinType, endType);

        PolyTree resultTree = new PolyTree();
        offset.Execute(resultTree, lineWidth * PdfCleanUpProcessor.floatMultiplier / 2);

        return filterFillPath(convertToPath(resultTree), ctm, PathPaintingRenderInfo.NONZERO_WINDING_RULE);
    }

    /**
     * @param fillingRule If the subpath is contour, pass any value.
     */
    protected Path filterFillPath(Path path, Matrix ctm, int fillingRule) {
        Point2D[] transfRectVertices = transformPoints(ctm, false, getVertices(rectangle));
        PolyFillType fillType = PolyFillType.pftNonZero;

        if (fillingRule == PathPaintingRenderInfo.EVEN_ODD_RULE) {
            fillType = PolyFillType.pftEvenOdd;
        }

        Clipper clipper = new Clipper();
        addPath(clipper, path);
        addRect(clipper, transfRectVertices);

        PolyTree resultTree = new PolyTree();
        clipper.execute(ClipType.ctDifference, resultTree, fillType, PolyFillType.pftNonZero);

        return convertToPath(resultTree);
    }

    private static JoinType getJoinType(int lineJoinStyle) {
        switch (lineJoinStyle) {
            case PdfContentByte.LINE_JOIN_BEVEL:
                return JoinType.jtSquare;

            case PdfContentByte.LINE_JOIN_MITER:
                return JoinType.jtMiter;
        }

        return JoinType.jtRound;
    }

    private static EndType getEndType(int lineCapStyle) {
        switch (lineCapStyle) {
            case PdfContentByte.LINE_CAP_BUTT:
                return EndType.etOpenButt;

            case PdfContentByte.LINE_CAP_PROJECTING_SQUARE:
                return EndType.etOpenSquare;
        }

        return EndType.etOpenRound;
    }

    private static void addPath(ClipperOffset offset, Path path, JoinType joinType, EndType endType) {
        for (Subpath subpath : path.getSubpaths()) {
            if (!subpath.isSinglePointClosed() && !subpath.isSinglePointOpen()) {
                // Offsetting is never used for path to be filled
                if (subpath.isClosed()) {
                    endType = EndType.etClosedLine;
                }

                List<Point2D> linearApproxPoints = subpath.getPiecewiseLinearApproximation();
                offset.AddPath(convertToIntPoints(linearApproxPoints), joinType, endType);
            }
        }
    }


    private static void addPath(Clipper clipper, Path path) {
        for (Subpath subpath : path.getSubpaths()) {
            if (!subpath.isSinglePointClosed() && !subpath.isSinglePointOpen()) {
                List<Point2D> linearApproxPoints = subpath.getPiecewiseLinearApproximation();
                clipper.addPath(convertToIntPoints(linearApproxPoints), PolyType.ptSubject, subpath.isClosed());
            }
        }
    }

    private static void addRect(Clipper clipper, Point2D[] rectVertices) {
        clipper.addPath(convertToIntPoints(new ArrayList<Point2D>(Arrays.asList(rectVertices))), PolyType.ptClip, true);
    }

    private static List<IntPoint> convertToIntPoints(List<Point2D> points) {
        List<IntPoint> convertedPoints = new ArrayList<IntPoint>(points.size());

        for (Point2D point : points) {
            convertedPoints.add(new IntPoint(PdfCleanUpProcessor.floatMultiplier * point.getX(),
                                             PdfCleanUpProcessor.floatMultiplier * point.getY()));
        }

        return convertedPoints;
    }

    private static List<Point2D> convertToFloatPoints(List<IntPoint> points) {
        List<Point2D> convertedPoints = new ArrayList<Point2D>(points.size());

        for (IntPoint point : points) {
            convertedPoints.add(new Point2D.Double(point.X / PdfCleanUpProcessor.floatMultiplier,
                                                   point.Y / PdfCleanUpProcessor.floatMultiplier));
        }

        return convertedPoints;
    }

    private static Path convertToPath(PolyTree result) {
        Path path = new Path();
        PolyNode node = result.getFirst();

        while (node != null) {
            addContour(path, node.getContour(), !node.isOpen());
            node = node.GetNext();
        }

        return path;
    }

    private static void addContour(Path path, List<IntPoint> contour, Boolean close) {
        List<Point2D> floatContour = convertToFloatPoints(contour);
        Iterator<Point2D> iter = floatContour.iterator();

        Point2D point = iter.next();
        path.moveTo((float) point.getX(), (float) point.getY());

        while (iter.hasNext()) {
            point = iter.next();
            path.lineTo((float) point.getX(), (float) point.getY());
        }

        if (close) {
            path.closeSubpath();
        }
    }

    private Point2D[] getVertices(Rectangle rect) {
        Point2D[] points = {
                new Point2D.Double(rect.getLeft(), rect.getBottom()),
                new Point2D.Double(rect.getRight(), rect.getBottom()),
                new Point2D.Double(rect.getRight(), rect.getTop()),
                new Point2D.Double(rect.getLeft(), rect.getTop())
        };

        return points;
    }

    private boolean intersect(Rectangle r1, Rectangle r2) {
        return (r1.getLeft() < r2.getRight() && r1.getRight() > r2.getLeft() &&
                r1.getBottom() < r2.getTop() && r1.getTop() > r2.getBottom());
    }

    /**
     * @return Image boundary rectangle in device space.
     */
    private Rectangle calcImageRect(ImageRenderInfo renderInfo) {
        Matrix ctm = renderInfo.getImageCTM();

        if (ctm == null) {
            return null;
        }

        Point2D[] points = transformPoints(ctm, false, new Point(0, 0), new Point(0, 1),
                                                       new Point(1, 0), new Point(1, 1));
        return getRectangle(points[0], points[1], points[2], points[3]);
    }

    /**
     * @return null if the intersection is empty, {@link com.itextpdf.text.Rectangle} representing intersection otherwise
     */
    private Rectangle intersection(Rectangle rect1, Rectangle rect2) {
        com.itextpdf.awt.geom.Rectangle awtRect1 = new com.itextpdf.awt.geom.Rectangle(rect1);
        com.itextpdf.awt.geom.Rectangle awtRect2 = new com.itextpdf.awt.geom.Rectangle(rect2);
        com.itextpdf.awt.geom.Rectangle awtIntersection = awtRect1.intersection(awtRect2);

        return awtIntersection.isEmpty() ? null : new Rectangle(awtIntersection);
    }

    /**
     * Transforms the given Rectangle into the image coordinate system which is [0,1]x[0,1] by default
     */
    private Rectangle transformIntersection(Matrix imageCTM, Rectangle rect) {
        Point2D[] points = transformPoints(imageCTM, true, new Point(rect.getLeft(), rect.getBottom()),
                                                           new Point(rect.getLeft(), rect.getTop()),
                                                           new Point(rect.getRight(), rect.getBottom()),
                                                           new Point(rect.getRight(), rect.getTop()));
        return getRectangle(points[0], points[1], points[2], points[3]);
    }

    /**
     * Constructs Rectangle object on the given points
     */
    private Rectangle getRectangle(Point2D p1, Point2D p2, Point2D p3, Point2D p4) {
        List<Double> xs = Arrays.asList(p1.getX(), p2.getX(), p3.getX(), p4.getX());
        List<Double> ys = Arrays.asList(p1.getY(), p2.getY(), p3.getY(), p4.getY());

        double left = Collections.min(xs);
        double bottom = Collections.min(ys);
        double right = Collections.max(xs);
        double top = Collections.max(ys);

        return new Rectangle((float) left, (float) bottom, (float) right, (float) top);
    }

    private static boolean containsAll(com.itextpdf.awt.geom.Rectangle rect, Point2D... points) {
        for (Point2D point : points) {
            if (!rect.contains(point)) {
                return false;
            }
        }

        return true;
    }

    private Point2D[] transformPoints(Matrix transormationMatrix, boolean inverse, Collection<? extends Point2D> points) {
        Point2D[] pointsArr = new Point2D[points.size()];
        points.toArray(pointsArr);

        return transformPoints(transormationMatrix, inverse, pointsArr);
    }

    private Point2D[] transformPoints(Matrix transormationMatrix, boolean inverse, Point2D... points) {
        AffineTransform t = new AffineTransform(transormationMatrix.get(Matrix.I11), transormationMatrix.get(Matrix.I12),
                transormationMatrix.get(Matrix.I21), transormationMatrix.get(Matrix.I22),
                transormationMatrix.get(Matrix.I31), transormationMatrix.get(Matrix.I32));
        Point2D[] transformed = new Point2D[points.length];

        if (inverse) {
            try {
                t = t.createInverse();
            } catch (NoninvertibleTransformException e) {
                throw new RuntimeException(e);
            }
        }

        t.transform(points, 0, transformed, 0, points.length);

        return transformed;
    }
}

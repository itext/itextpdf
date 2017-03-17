/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
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
import com.itextpdf.text.pdf.parser.Path;
import com.itextpdf.text.pdf.parser.clipper.*;
import com.itextpdf.text.pdf.parser.clipper.Clipper.*;
import com.itextpdf.text.pdf.parser.clipper.Point.LongPoint;

import java.util.*;

class PdfCleanUpRegionFilter extends RenderFilter {

    private List<Rectangle> rectangles;

    private static final double circleApproximationConst = 0.55191502449;

    public PdfCleanUpRegionFilter(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    /**
     * Checks if the text is inside render filter region.
     */
    @Override
    public boolean allowText(TextRenderInfo renderInfo) {
        LineSegment ascent = renderInfo.getAscentLine();
        LineSegment descent = renderInfo.getDescentLine();

        Point2D[] glyphRect = new Point2D[] {
                new Point2D.Float(ascent.getStartPoint().get(0), ascent.getStartPoint().get(1)),
                new Point2D.Float(ascent.getEndPoint().get(0), ascent.getEndPoint().get(1)),
                new Point2D.Float(descent.getEndPoint().get(0), descent.getEndPoint().get(1)),
                new Point2D.Float(descent.getStartPoint().get(0), descent.getStartPoint().get(1)),
        };

        for (Rectangle rectangle : rectangles) {
            Point2D[] redactRect = getVertices(rectangle);

            if (intersect(glyphRect, redactRect)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean allowImage(ImageRenderInfo renderInfo) {
        throw new UnsupportedOperationException();
    }

    /**
     * Calculates intersection of the image and the render filter region in the coordinate system relative to the image.
     *
     * @return <code>null</code> if the image is not allowed, {@link java.util.List} of
     * {@link com.itextpdf.text.Rectangle} objects otherwise.
     */
    protected List<Rectangle> getCoveredAreas(ImageRenderInfo renderInfo) {
        Rectangle imageRect = calcImageRect(renderInfo);
        List<Rectangle> coveredAreas = new ArrayList<Rectangle>();

        if (imageRect == null) {
            return null;
        }

        for (Rectangle rectangle : rectangles) {
            Rectangle intersectionRect = intersection(imageRect, rectangle);

            if (intersectionRect != null) {
                // True if the image is completely covered
                if (imageRect.equals(intersectionRect)) {
                    return null;
                }

                coveredAreas.add(transformIntersection(renderInfo.getImageCTM(), intersectionRect));
            }
        }

        return coveredAreas;
    }

    protected Path filterStrokePath(Path sourcePath, Matrix ctm, float lineWidth, int lineCapStyle,
                                    int lineJoinStyle, float miterLimit, LineDashPattern lineDashPattern) {
        Path path = sourcePath;
        JoinType joinType = getJoinType(lineJoinStyle);
        EndType endType = getEndType(lineCapStyle);

        if (lineDashPattern != null) {
            if (!lineDashPattern.isSolid()) {
                path = applyDashPattern(path, lineDashPattern);
            }
        }

        ClipperOffset offset = new ClipperOffset(miterLimit, PdfCleanUpProcessor.arcTolerance * PdfCleanUpProcessor.floatMultiplier);
        List<Subpath> degenerateSubpaths = addPath(offset, path, joinType, endType);

        PolyTree resultTree = new PolyTree();
        offset.execute(resultTree, lineWidth * PdfCleanUpProcessor.floatMultiplier / 2);
        Path offsetedPath = convertToPath(resultTree);

        if (degenerateSubpaths.size() > 0) {
            if (endType == EndType.OPEN_ROUND) {
                List<Subpath> circles = convertToCircles(degenerateSubpaths, lineWidth / 2);
                offsetedPath.addSubpaths(circles);
            } else if (endType == EndType.OPEN_SQUARE && lineDashPattern != null) {
                List<Subpath> squares = convertToSquares(degenerateSubpaths, lineWidth, sourcePath);
                offsetedPath.addSubpaths(squares);
            }
        }

        return filterFillPath(offsetedPath, ctm, PathPaintingRenderInfo.NONZERO_WINDING_RULE);
    }

    /**
     * Note: this method will close all unclosed subpaths of the passed path.
     *
     * @param fillingRule If the subpath is contour, pass any value.
     */
    protected Path filterFillPath(Path path, Matrix ctm, int fillingRule) {
        path.closeAllSubpaths();

        Clipper clipper = new DefaultClipper();
        addPath(clipper, path);

        for (Rectangle rectangle : rectangles) {
            Point2D[] transfRectVertices = transformPoints(ctm, true, getVertices(rectangle));
            addRect(clipper, transfRectVertices, PolyType.CLIP);
        }

        PolyFillType fillType = PolyFillType.NON_ZERO;

        if (fillingRule == PathPaintingRenderInfo.EVEN_ODD_RULE) {
            fillType = PolyFillType.EVEN_ODD;
        }

        PolyTree resultTree = new PolyTree();
        clipper.execute(ClipType.DIFFERENCE, resultTree, fillType, PolyFillType.NON_ZERO);

        return convertToPath(resultTree);
    }

    private static JoinType getJoinType(int lineJoinStyle) {
        switch (lineJoinStyle) {
            case PdfContentByte.LINE_JOIN_BEVEL:
                return JoinType.BEVEL;

            case PdfContentByte.LINE_JOIN_MITER:
                return JoinType.MITER;
        }

        return JoinType.ROUND;
    }

    private static EndType getEndType(int lineCapStyle) {
        switch (lineCapStyle) {
            case PdfContentByte.LINE_CAP_BUTT:
                return EndType.OPEN_BUTT;

            case PdfContentByte.LINE_CAP_PROJECTING_SQUARE:
                return EndType.OPEN_SQUARE;
        }

        return EndType.OPEN_ROUND;
    }

    /**
     * Converts specified degenerate subpaths to circles.
     * Note: actually the resultant subpaths are not real circles but approximated.
     *
     * @param radius Radius of each constructed circle.
     * @return {@link java.util.List} consisting of circles constructed on given degenerated subpaths.
     */
    private static List<Subpath> convertToCircles(List<Subpath> degenerateSubpaths, double radius) {
        List<Subpath> circles = new ArrayList<Subpath>(degenerateSubpaths.size());

        for (Subpath subpath : degenerateSubpaths) {
            BezierCurve[] circleSectors = approximateCircle(subpath.getStartPoint(), radius);

            Subpath circle = new Subpath();
            circle.addSegment(circleSectors[0]);
            circle.addSegment(circleSectors[1]);
            circle.addSegment(circleSectors[2]);
            circle.addSegment(circleSectors[3]);

            circles.add(circle);
        }

        return circles;
    }

    /**
     * Converts specified degenerate subpaths to squares.
     * Note: the list of degenerate subpaths should contain at least 2 elements. Otherwise
     * we can't determine the direction which the rotation of each square depends on.
     *
     * @param squareWidth Width of each constructed square.
     * @param sourcePath The path which dash pattern applied to. Needed to calc rotation angle of each square.
     * @return {@link java.util.List} consisting of squares constructed on given degenerated subpaths.
     */
    private static List<Subpath> convertToSquares(List<Subpath> degenerateSubpaths, double squareWidth, Path sourcePath) {
        List<Point2D> pathApprox = getPathApproximation(sourcePath);

        if (pathApprox.size() < 2) {
            return Collections.EMPTY_LIST;
        }

        Iterator<Point2D> approxIter = pathApprox.iterator();
        Point2D approxPt1 = approxIter.next();
        Point2D approxPt2 = approxIter.next();
        StandardLine line = new StandardLine(approxPt1, approxPt2);

        List<Subpath> squares = new ArrayList<Subpath>(degenerateSubpaths.size());
        float widthHalf = (float) squareWidth / 2;

        for (int i = 0; i < degenerateSubpaths.size(); ++i) {
            Point2D point = degenerateSubpaths.get(i).getStartPoint();

            while (!line.contains(point)) {
                approxPt1 = approxPt2;
                approxPt2 = approxIter.next();
                line = new StandardLine(approxPt1, approxPt2);
            }

            double slope = line.getSlope();
            double angle;

            if (slope != Float.POSITIVE_INFINITY) {
                angle = Math.atan(slope);
            } else {
                angle = Math.PI / 2;
            }

            squares.add(constructSquare(point, widthHalf, angle));
        }

        return squares;
    }

    private static List<Point2D> getPathApproximation(Path path) {
        List<Point2D> approx = new ArrayList<Point2D>() {
            @Override
            public boolean addAll(Collection<? extends Point2D> c) {
                Point2D prevPoint = (size() - 1 < 0 ? null : get(size() - 1));
                boolean ret = false;

                for (Point2D pt : c) {
                    if (!pt.equals(prevPoint)) {
                        add(pt);
                        prevPoint = pt;
                        ret = true;
                    }
                }

                return true;
            }
        };

        for (Subpath subpath : path.getSubpaths()) {
            approx.addAll(subpath.getPiecewiseLinearApproximation());
        }

        return approx;
    }

    private static Subpath constructSquare(Point2D squareCenter, double widthHalf, double rotationAngle) {
        // Orthogonal square is the square with sides parallel to one of the axes.
        Point2D[] ortogonalSquareVertices = {
                new Point2D.Double(-widthHalf, -widthHalf),
                new Point2D.Double(-widthHalf, widthHalf),
                new Point2D.Double(widthHalf, widthHalf),
                new Point2D.Double(widthHalf, -widthHalf)
        };

        Point2D[] rotatedSquareVertices = getRotatedSquareVertices(ortogonalSquareVertices, rotationAngle, squareCenter);

        Subpath square = new Subpath();
        square.addSegment(new Line(rotatedSquareVertices[0], rotatedSquareVertices[1]));
        square.addSegment(new Line(rotatedSquareVertices[1], rotatedSquareVertices[2]));
        square.addSegment(new Line(rotatedSquareVertices[2], rotatedSquareVertices[3]));
        square.addSegment(new Line(rotatedSquareVertices[3], rotatedSquareVertices[0]));

        return square;
    }

    private static Point2D[] getRotatedSquareVertices(Point2D[] orthogonalSquareVertices, double angle, Point2D squareCenter) {
        Point2D[] rotatedSquareVertices = new Point2D[orthogonalSquareVertices.length];

        AffineTransform.getRotateInstance(angle).
                transform(orthogonalSquareVertices, 0, rotatedSquareVertices, 0, rotatedSquareVertices.length);
        AffineTransform.getTranslateInstance(squareCenter.getX(), squareCenter.getY()).
                transform(rotatedSquareVertices, 0, rotatedSquareVertices, 0, orthogonalSquareVertices.length);

        return rotatedSquareVertices;
    }

    /**
     * Adds all subpaths of the path to the {@link ClipperOffset} object with one
     * note: it doesn't add degenerate subpaths.
     *
     * @return {@link java.util.List} consisting of all degenerate subpaths of the path.
     */
    private static List<Subpath> addPath(ClipperOffset offset, Path path, JoinType joinType, EndType endType) {
        List<Subpath> degenerateSubpaths = new ArrayList<Subpath>();

        for (Subpath subpath : path.getSubpaths()) {
            if (subpath.isDegenerate()) {
                degenerateSubpaths.add(subpath);
                continue;
            }

            if (!subpath.isSinglePointClosed() && !subpath.isSinglePointOpen()) {
                EndType et;

                if (subpath.isClosed()) {
                    // Offsetting is never used for path being filled
                    et = EndType.CLOSED_LINE;
                } else {
                    et = endType;
                }

                List<Point2D> linearApproxPoints = subpath.getPiecewiseLinearApproximation();
                offset.addPath(convertToIntPoints(linearApproxPoints), joinType, et);
            }
        }

        return degenerateSubpaths;
    }

    private static BezierCurve[] approximateCircle(Point2D center, double radius) {
        // The circle is split into 4 sectors. Arc of each sector
        // is approximated  with bezier curve separately.
        BezierCurve[] approximation = new BezierCurve[4];
        double x = center.getX();
        double y = center.getY();

        approximation[0] = new BezierCurve(Arrays.asList(
                (Point2D) new Point2D.Double(x, y + radius),
                new Point2D.Double(x + radius * circleApproximationConst, y + radius),
                new Point2D.Double(x + radius, y + radius * circleApproximationConst),
                new Point2D.Double(x + radius, y)));

        approximation[1] = new BezierCurve(Arrays.asList(
                (Point2D) new Point2D.Double(x + radius, y),
                new Point2D.Double(x + radius, y - radius * circleApproximationConst),
                new Point2D.Double(x + radius * circleApproximationConst, y - radius),
                new Point2D.Double(x, y - radius)));

        approximation[2] = new BezierCurve(Arrays.asList(
                (Point2D) new Point2D.Double(x, y - radius),
                new Point2D.Double(x - radius * circleApproximationConst, y - radius),
                new Point2D.Double(x - radius, y - radius * circleApproximationConst),
                new Point2D.Double(x - radius, y)));

        approximation[3] = new BezierCurve(Arrays.asList(
                (Point2D) new Point2D.Double(x - radius, y),
                new Point2D.Double(x - radius, y + radius * circleApproximationConst),
                new Point2D.Double(x - radius * circleApproximationConst, y + radius),
                new Point2D.Double(x, y + radius)));

        return approximation;
    }


    private static void addPath(Clipper clipper, Path path) {
        for (Subpath subpath : path.getSubpaths()) {
            if (!subpath.isSinglePointClosed() && !subpath.isSinglePointOpen()) {
                List<Point2D> linearApproxPoints = subpath.getPiecewiseLinearApproximation();
                clipper.addPath(convertToIntPoints(linearApproxPoints), PolyType.SUBJECT, subpath.isClosed());
            }
        }
    }

    private static void addRect(Clipper clipper, Point2D[] rectVertices, PolyType polyType) {
        clipper.addPath(convertToIntPoints(new ArrayList<Point2D>(Arrays.asList(rectVertices))), polyType, true);
    }

    private static com.itextpdf.text.pdf.parser.clipper.Path convertToIntPoints(List<Point2D> points) {
        List<LongPoint> convertedPoints = new ArrayList<LongPoint>(points.size());

        for (Point2D point : points) {
            convertedPoints.add(new LongPoint(PdfCleanUpProcessor.floatMultiplier * point.getX(),
                                              PdfCleanUpProcessor.floatMultiplier * point.getY()));
        }

        return new com.itextpdf.text.pdf.parser.clipper.Path(convertedPoints);
    }

    private static List<Point2D> convertToFloatPoints(List<LongPoint> points) {
        List<Point2D> convertedPoints = new ArrayList<Point2D>(points.size());

        for (LongPoint point : points) {
            convertedPoints.add(new Point2D.Float((float) (point.getX() / PdfCleanUpProcessor.floatMultiplier),
                                                  (float) (point.getY() / PdfCleanUpProcessor.floatMultiplier)));
        }

        return convertedPoints;
    }

    private static Path convertToPath(PolyTree result) {
        Path path = new Path();
        PolyNode node = result.getFirst();

        while (node != null) {
            addContour(path, node.getContour(), !node.isOpen());
            node = node.getNext();
        }

        return path;
    }

    private static void addContour(Path path, List<LongPoint> contour, Boolean close) {
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

    private boolean intersect(Point2D[] rect1, Point2D[] rect2) {
        Clipper clipper = new DefaultClipper();
        addRect(clipper, rect1, PolyType.SUBJECT);
        addRect(clipper, rect2, PolyType.CLIP);

        Paths paths = new Paths();
        clipper.execute(ClipType.INTERSECTION, paths, PolyFillType.NON_ZERO, PolyFillType.NON_ZERO);

        return !paths.isEmpty();
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

    private static Path applyDashPattern(Path path, LineDashPattern lineDashPattern) {
        Set<Integer> modifiedSubpaths = new HashSet<Integer>(path.replaceCloseWithLine());
        Path dashedPath = new Path();
        int currentSubpath = 0;

        for (Subpath subpath : path.getSubpaths()) {
            List<Point2D> subpathApprox = subpath.getPiecewiseLinearApproximation();

            if (subpathApprox.size() > 1) {
                dashedPath.moveTo((float) subpathApprox.get(0).getX(), (float) subpathApprox.get(0).getY());
                float remainingDist = 0;
                boolean remainingIsGap = false;

                for (int i = 1; i < subpathApprox.size(); ++i) {
                    Point2D nextPoint = null;

                    if (remainingDist != 0) {
                        nextPoint = getNextPoint(subpathApprox.get(i - 1), subpathApprox.get(i), remainingDist);
                        remainingDist = applyDash(dashedPath, subpathApprox.get(i - 1), subpathApprox.get(i), nextPoint, remainingIsGap);
                    }

                    while (Float.compare(remainingDist, 0) == 0 && !dashedPath.getCurrentPoint().equals(subpathApprox.get(i))) {
                        LineDashPattern.DashArrayElem currentElem = lineDashPattern.next();
                        nextPoint = getNextPoint(nextPoint != null ? nextPoint : subpathApprox.get(i - 1), subpathApprox.get(i), currentElem.getVal());
                        remainingDist = applyDash(dashedPath, subpathApprox.get(i - 1), subpathApprox.get(i), nextPoint, currentElem.isGap());
                        remainingIsGap = currentElem.isGap();
                    }
                }
                
                // If true, then the line closing the subpath was explicitly added (see Path.ReplaceCloseWithLine).
                // This causes a loss of a visual effect of line join style parameter, so in this clause
                // we simply add overlapping dash (or gap, no matter), which continues the last dash and equals to
                // the first dash (or gap) of the path.
                if (modifiedSubpaths.contains(currentSubpath)) {
                    lineDashPattern.reset();
                    LineDashPattern.DashArrayElem currentElem = lineDashPattern.next();
                    Point2D nextPoint = getNextPoint(subpathApprox.get(0), subpathApprox.get(1), currentElem.getVal());
                    applyDash(dashedPath, subpathApprox.get(0), subpathApprox.get(1), nextPoint, currentElem.isGap());
                }
            }

            // According to PDF spec. line dash pattern should be restarted for each new subpath.
            lineDashPattern.reset();
            ++currentSubpath;
        }

        return dashedPath;
    }

    private static Point2D getNextPoint(Point2D segStart, Point2D segEnd, float dist) {
        Point2D vector = componentwiseDiff(segEnd, segStart);
        Point2D unitVector = getUnitVector(vector);

        return new Point2D.Float((float) (segStart.getX() + dist * unitVector.getX()),
                                 (float) (segStart.getY() + dist * unitVector.getY()));
    }

    private static Point2D componentwiseDiff(Point2D minuend, Point2D subtrahend) {
        return new Point2D.Float((float) (minuend.getX() - subtrahend.getX()),
                                 (float) (minuend.getY() - subtrahend.getY()));
    }

    private static Point2D getUnitVector(Point2D vector) {
        double vectorLength = getVectorEuclideanNorm(vector);
        return new Point2D.Float((float) (vector.getX() / vectorLength),
                                 (float) (vector.getY() / vectorLength));
    }

    private static double getVectorEuclideanNorm(Point2D vector) {
        return vector.distance(0, 0);
    }

    private static float applyDash(Path dashedPath, Point2D segStart, Point2D segEnd, Point2D dashTo, boolean isGap) {
        float remainingDist = 0;

        if (!liesOnSegment(segStart, segEnd, dashTo)) {
            remainingDist = (float) dashTo.distance(segEnd);
            dashTo = segEnd;
        }

        if (isGap) {
            dashedPath.moveTo((float) dashTo.getX(), (float) dashTo.getY());
        } else {
            dashedPath.lineTo((float) dashTo.getX(), (float) dashTo.getY());
        }

        return remainingDist;
    }

    private static boolean liesOnSegment(Point2D segStart, Point2D segEnd, Point2D point) {
        return point.getX() >= Math.min(segStart.getX(), segEnd.getX()) &&
               point.getX() <= Math.max(segStart.getX(), segEnd.getX()) &&
               point.getY() >= Math.min(segStart.getY(), segEnd.getY()) &&
               point.getY() <= Math.max(segStart.getY(), segEnd.getY());
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

    // Constants from the standard line representation: Ax+By+C
    private static class StandardLine {

        float A;
        float B;
        float C;

        StandardLine(Point2D p1, Point2D p2) {
            A = (float) (p2.getY() - p1.getY());
            B = (float) (p1.getX() - p2.getX());
            C = (float) (p1.getY() * (-B) - p1.getX() * A);
        }

        float getSlope() {
            if (B == 0) {
                return Float.POSITIVE_INFINITY;
            }

            return -A / B;
        }

        boolean contains(Point2D point) {
            return Float.compare(Math.abs(A * (float) point.getX() + B * (float) point.getY() + C), 0.1f) < 0;
        }
    }
}

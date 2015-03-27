package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.awt.geom.NoninvertibleTransformException;
import com.itextpdf.awt.geom.Point;
import com.itextpdf.awt.geom.Point2D;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.*;

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

    // Current implementation is for completely covered line arts only.
    protected List<Subpath> filterSubpath(Subpath subpath, Matrix ctm, boolean isContour) {
        List<Subpath> filteredSubpaths = new ArrayList<Subpath>();
        com.itextpdf.awt.geom.Rectangle rect = new com.itextpdf.awt.geom.Rectangle(rectangle);

        if (subpath.isSinglePointClosed()) {
            Point2D transformedStartPoint = transformPoints(ctm, false, subpath.getStartPoint())[0];

            if (!containsAll(rect, transformedStartPoint)) {
                filteredSubpaths.add(subpath);
            }

            return filteredSubpaths;
        } else if (subpath.isSinglePointOpen()) {
            return filteredSubpaths;
        }

        Subpath newSubpath = new Subpath();
        List<Shape> subpathSegments = new ArrayList<Shape>(subpath.getSegments());

        // Create the line which is implicitly added by PDF, when you use 'h' operator
        if (subpath.isClosed()) {
            Shape lastSegment = subpathSegments.get(subpathSegments.size() - 1);
            List<Point2D> segmentBasePoints = lastSegment.getBasePoints();
            subpathSegments.add(new Line(segmentBasePoints.get(segmentBasePoints.size() - 1), subpath.getStartPoint()));
        }

        for (Shape segment : subpathSegments) {
            Point2D[] transformedSegBasePoints = transformPoints(ctm, false, segment.getBasePoints());

            if (containsAll(rect, transformedSegBasePoints)) {
                if (!newSubpath.isEmpty()) {
                    filteredSubpaths.add(newSubpath);
                    newSubpath = new Subpath();
                }
            } else {
                newSubpath.addSegment(segment);

                // if it's filled area and we have got here, then it isn't completely covered
                if (!isContour) {
                    break;
                }
            }
        }

        if (!newSubpath.isEmpty()) {
            filteredSubpaths.add( !isContour ? subpath : newSubpath );
        }

        return filteredSubpaths;
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

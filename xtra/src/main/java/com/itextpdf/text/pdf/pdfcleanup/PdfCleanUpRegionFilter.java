package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.awt.geom.NoninvertibleTransformException;
import com.itextpdf.awt.geom.Point;
import com.itextpdf.awt.geom.Point2D;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        throw new NotImplementedException();
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

        AffineTransform t = new AffineTransform(ctm.get(Matrix.I11), ctm.get(Matrix.I12),
                                                ctm.get(Matrix.I21), ctm.get(Matrix.I22),
                                                ctm.get(Matrix.I31), ctm.get(Matrix.I32));
        Point2D p1 = t.transform(new Point(0, 0), null);
        Point2D p2 = t.transform(new Point(0, 1), null);
        Point2D p3 = t.transform(new Point(1, 0), null);
        Point2D p4 = t.transform(new Point(1, 1), null);

        return getRectangle(p1, p2, p3, p4);
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
        AffineTransform t = new AffineTransform(imageCTM.get(Matrix.I11), imageCTM.get(Matrix.I12),
                                                imageCTM.get(Matrix.I21), imageCTM.get(Matrix.I22),
                                                imageCTM.get(Matrix.I31), imageCTM.get(Matrix.I32));
        Point2D p1;
        Point2D p2;
        Point2D p3;
        Point2D p4;

        try {
            p1 = t.inverseTransform(new Point(rect.getLeft(), rect.getBottom()), null);
            p2 = t.inverseTransform(new Point(rect.getLeft(), rect.getTop()), null);
            p3 = t.inverseTransform(new Point(rect.getRight(), rect.getBottom()), null);
            p4 = t.inverseTransform(new Point(rect.getRight(), rect.getTop()), null);
        } catch (NoninvertibleTransformException e) {
            throw new RuntimeException(e);
        }

        return getRectangle(p1, p2, p3, p4);
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
}

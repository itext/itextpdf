package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.awt.geom.Point;
import com.itextpdf.awt.geom.Point2D;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class PdfCleanUpRegionFilter extends RenderFilter {

    private Rectangle rectangle;

    public PdfCleanUpRegionFilter(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    /**
     * Checks if the text is inside render filter region.
     *
     * @param renderInfo
     * @return
     */
    @Override
    public boolean allowText(TextRenderInfo renderInfo) { // TODO: a little bit confusing name regarding to logic, isn't it?
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
     * Calculates intersection of the image and the render filter region in the coordinate system relative to image.
     * The transformed coordinate system here is the coordinate system center of which is in (image.leftX, image.topY) and
     * y' = -y
     *
     * @return intersection
     */
    public PdfCleanUpCoveredArea intersection(ImageRenderInfo renderInfo) {
        Rectangle imageRect = calcImageRect(renderInfo);

        if (imageRect == null) {
            return null;
        }

        Rectangle intersectionRect = intersection(imageRect, rectangle);
        Rectangle transformedIntersection = null;

        if (intersectionRect != null) {
            transformedIntersection = shearCoordinatesAndInverseY(imageRect.getLeft(), imageRect.getTop(), intersectionRect);
        }

        return new PdfCleanUpCoveredArea(transformedIntersection, imageRect.equals(intersectionRect));
    }

    private boolean intersect(Rectangle r1, Rectangle r2) {
        return (r1.getLeft() < r2.getRight() && r1.getRight() > r2.getLeft() &&
                r1.getBottom() < r2.getTop() && r1.getTop() > r2.getBottom());
    }

    private Rectangle calcImageRect(ImageRenderInfo renderInfo) {
        Matrix ctm = renderInfo.getImageCTM();
        if (ctm == null)
            return null;
        AffineTransform t = new AffineTransform(ctm.get(0), ctm.get(1), ctm.get(3), ctm.get(4), ctm.get(6), ctm.get(7));
        Point2D p1 = t.transform(new Point(0, 0), null);
        Point2D p2 = t.transform(new Point(0, 1), null);
        Point2D p3 = t.transform(new Point(1, 0), null);
        Point2D p4 = t.transform(new Point(1, 1), null);
        double[] xs = new double[]{p1.getX(), p2.getX(), p3.getX(), p4.getX()};
        double[] ys = new double[]{p1.getY(), p2.getY(), p3.getY(), p4.getY()};
        double left = min(xs);
        double bottom = min(ys);
        double right = max(xs);
        double top = max(ys);

        return new Rectangle((float) left, (float) bottom, (float) right, (float) top);
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

    private Rectangle shearCoordinatesAndInverseY(float dx, float dy, Rectangle rect) {
        AffineTransform affineTransform = new AffineTransform(1, 0, 0, -1, -dx, dy);

        Point2D leftBottom = affineTransform.transform(new Point(rect.getLeft(), rect.getBottom()), null);
        Point2D rightTop = affineTransform.transform(new Point(rect.getRight(), rect.getTop()), null);

        return new Rectangle((float) leftBottom.getX(), (float) leftBottom.getY(),
                             (float) rightTop.getX(), (float) rightTop.getY());
    }

    private double min(double[] numbers) {
        double min = Float.MAX_VALUE;
        for (double num : numbers)
            if (num < min)
                min = num;
        return min;
    }

    private double max(double[] numbers) {
        double max = -Float.MAX_VALUE;
        for (double num : numbers)
            if (num > max)
                max = num;
        return max;
    }
}

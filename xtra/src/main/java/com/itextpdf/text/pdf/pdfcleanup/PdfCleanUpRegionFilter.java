package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.awt.geom.Point;
import com.itextpdf.awt.geom.Point2D;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.*;

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
    public boolean allowText(TextRenderInfo renderInfo) {
        LineSegment ascent = renderInfo.getAscentLine();
        LineSegment descent = renderInfo.getDescentLine();
        Vector startPoint = new Vector((ascent.getStartPoint().get(0) + descent.getStartPoint().get(0)) / 2,
                (ascent.getStartPoint().get(1) + descent.getStartPoint().get(1)) / 2, 0);
        Vector endPoint = new Vector((ascent.getEndPoint().get(0) + descent.getEndPoint().get(0)) / 2,
                (ascent.getEndPoint().get(1) + descent.getEndPoint().get(1)) / 2, 0);
        float x1 = startPoint.get(Vector.I1);
        float y1 = startPoint.get(Vector.I2);
        float x2 = endPoint.get(Vector.I1);
        float y2 = endPoint.get(Vector.I2);
        Rectangle r1 = new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2));
        Rectangle r2 = rectangle;
        return b(r1, r2);
    }

    /**
     * Checks if the image is inside render filter region.
     *
     * @param renderInfo
     * @return
     */
    @Override
    public boolean allowImage(ImageRenderInfo renderInfo) {
        Matrix ctm = renderInfo.getImageCTM();
        if (ctm == null)
            return false;
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
        Rectangle r1 = new Rectangle((float) left, (float) bottom, (float) right, (float) top);
        Rectangle r2 = rectangle;
        return b(r1, r2);
    }

    public boolean allowObject(Object renderInfo) {
        if (renderInfo instanceof TextRenderInfo)
            return allowText((TextRenderInfo)renderInfo);
        else if (renderInfo instanceof ImageRenderInfo)
            return allowImage((ImageRenderInfo)renderInfo);
        else
            return false;
    }

    /**
     * Checks if r1 is completely inside r2.
     *
     * @param r1
     * @param r2
     * @return
     */
    private boolean b(Rectangle r1, Rectangle r2) {
        return r1.getLeft() >= r2.getLeft() && r1.getBottom() >= r2.getBottom() && r1.getRight() <= r2.getRight() &&
                r1.getTop() <= r2.getTop();
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

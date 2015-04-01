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
 *
 * @since 5.5.6
 */
public class Path {

    private static final String START_PATH_ERR_MSG = "Path shall start with \"re\" or \"m\" operator";

    private List<Subpath> subpaths = new ArrayList<Subpath>();
    private Point2D currentPoint;

    public Path() {
    }

    public Path(List<? extends Subpath> subpaths) {
        if (subpaths.size() > 0) {
            this.subpaths.addAll(subpaths);
            currentPoint = this.subpaths.get(subpaths.size() - 1).getLastPoint();
        }
    }

    /**
     * @return A {@link java.util.List} of subpaths forming this path.
     */
    public List<Subpath> getSubpaths() {
        return subpaths;
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
        // numbered in natural order
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
     * Close the current subpath by appending a straight line segment from the current
     * point to the starting point of the subpath.
     */
    public void closeSubpath() {
        Subpath lastSubpath = getLastSubpath();
        lastSubpath.setClosed(true);

        Point2D startPoint = lastSubpath.getStartPoint();
        moveTo((float) startPoint.getX(), (float) startPoint.getY());
    }

    private Subpath getLastSubpath() {
        return subpaths.size() > 0 ? subpaths.get(subpaths.size() - 1) : null;
    }
}

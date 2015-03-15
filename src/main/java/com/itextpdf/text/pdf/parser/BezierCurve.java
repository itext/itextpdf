package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.awt.geom.PathIterator;
import com.itextpdf.awt.geom.Point2D;
import com.itextpdf.awt.geom.Rectangle2D;

import java.util.List;

/**
 * Represents a Bezier curve.
 *
 * @since 5.5.6
 */
public class BezierCurve implements Shape {

    private List<Point2D> controlPoints;

    public BezierCurve(List<Point2D> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public List<Point2D> getBasePoints() {
        return controlPoints;
    }

    public com.itextpdf.awt.geom.Rectangle getBounds() {
        throw new UnsupportedOperationException();
    }

    public Rectangle2D getBounds2D() {
        throw new UnsupportedOperationException();
    }

    public boolean contains(double x, double y) {
        return false;
    }

    public boolean contains(Point2D p) {
        return false;
    }

    public boolean intersects(double x, double y, double w, double h) {
        throw new UnsupportedOperationException();
    }

    public boolean intersects(Rectangle2D r) {
        throw new UnsupportedOperationException();
    }

    public boolean contains(double x, double y, double w, double h) {
        return false;
    }

    public boolean contains(Rectangle2D r) {
        return false;
    }

    public PathIterator getPathIterator(AffineTransform at) {
        throw new UnsupportedOperationException();
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        throw new UnsupportedOperationException();
    }
}

package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.Line2D;
import com.itextpdf.awt.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * {@inheritDoc}
 */
public class Line extends Line2D.Float implements Shape {

    /**
     * Constructs a new zero-length line starting at zero.
     */
    public Line() {
    }

    /**
     * Constructs a new line based on the given coordinates.
     */
    public Line(float x1, float y1, float x2, float y2) {
        super(x1, y1, x2, y2);
    }

    /**
     * Constructs a new line based on the given coordinates.
     */
    public Line(Point2D p1, Point2D p2) {
        super(p1, p2);
    }

    public List<Point2D> getBasePoints() {
        List<Point2D> basePoints = new ArrayList<Point2D>(2);
        basePoints.add(getP1());
        basePoints.add(getP2());

        return basePoints;
    }
}

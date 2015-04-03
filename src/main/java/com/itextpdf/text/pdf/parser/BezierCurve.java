package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.Point2D;

import java.util.List;

/**
 * Represents a Bezier curve.
 *
 * @since 5.5.6
 */
public class BezierCurve implements Shape {

    private final List<Point2D> controlPoints;

    public BezierCurve(List<Point2D> controlPoints) {
        this.controlPoints = controlPoints;
    }

    public List<Point2D> getBasePoints() {
        return controlPoints;
    }
}

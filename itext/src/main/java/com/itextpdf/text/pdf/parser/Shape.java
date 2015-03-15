package com.itextpdf.text.pdf.parser;

import com.itextpdf.awt.geom.Point2D;

import java.util.List;

/**
 * Simply extends the {@link com.itextpdf.text.pdf.parser.Shape} interface to provide
 * additional method.
 *
 * {@inheritDoc}
 */
public interface Shape extends com.itextpdf.awt.geom.Shape {

    /**
     * Treat base points as the points which are enough to construct a shape.
     * E.g. for a bezier curve they are control points, for a line segment - the start and the end points
     * of the segment.
     *
     * @return Ordered {@link java.util.List} consisting of shape's base points.
     */
    List<Point2D> getBasePoints();
}

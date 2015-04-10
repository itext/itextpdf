/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part4.helper;

import com.itextpdf.awt.geom.Point;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.Matrix;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * Abstract class that is used as a superclass for specific item classes
 * such as TextItem, ImageItem, Line and Structure.
 */
public abstract class MyItem implements Comparable<MyItem> {

    /**
     * If we want to compare item positions we should add some tolerance.
     */
    public static final float itemPositionTolerance = 3f;

    /** Rectangle that defines the coordinates of an item. */
    protected Rectangle rectangle;
    /** Color that will be used to mark the item. */
    protected BaseColor color;

    /**
     * Creates an instance of the MyItem object
     */
    protected MyItem() {
        this.rectangle = rectangle;
        this.color = color;
    }

    /**
     * Getter for the coordinates.
     * @return coordinates in the form of a Rectangle object
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Getter for the color.
     * @return a BaseColor object
     */
    public BaseColor getColor() {
        return color;
    }

    /**
     * Gets the lower left corner of the item.
     * For image items it returns lower left corner of bounding box.
     * For text items it returns start point of a baseline.
     * @return point of the lower left corner.
     */
    public Point getLL() {
        return new Point(getRectangle().getLeft(), getRectangle().getBottom());
    }

    /**
     * @see java.lang.Comparable#compareTo(Object)
     */
    public int compareTo(MyItem o) {
        double left = getLL().getX();
        double bottom = getLL().getY();
        double oLeft = o.getLL().getX();
        double oBottom = o.getLL().getY();
        if (bottom - oBottom > itemPositionTolerance)
            return -1;
        else if (oBottom - bottom > itemPositionTolerance)
            return 1;
        else
            return Double.compare(left, oLeft);
    }

}
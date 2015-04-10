/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part4.helper;

import com.itextpdf.text.Rectangle;
import java.util.List;

/**
 * MyItem implementation that gets its coordinates and color from a list
 * of items that are all on the same line.
 */
public class Line extends MyItem {
    /**
     * Creates a Line object based on a list of items that have the same
     * offset of their baseline.
     * @param items a list of MyItem objects
     */
    public Line(List<MyItem> items) {
        super();
        rectangle = getItemsRect(items);
        color = items.get(0).getColor();
    }

    /**
     * Creates a rectangle that encompasses all the coordinate rectangles
     * of the items that belong to this line.
     * @param items the items that belong to a line
     * @return a rectangle that encompasses all items belonging to a line
     */
    private static Rectangle getItemsRect(List<MyItem> items) {
        float left = Float.MAX_VALUE;
        float right = 0;
        float top = 0;
        float bottom = Float.MAX_VALUE;
        for (MyItem item : items) {
            if (item.getRectangle().getLeft() < left)
                left = item.getRectangle().getLeft();
            if (item.getRectangle().getRight() > right)
                right = item.getRectangle().getRight();
            if (item.getRectangle().getTop() > top)
                top = item.getRectangle().getTop();
            if (item.getRectangle().getBottom() < bottom)
                bottom = item.getRectangle().getBottom();
        }
        return new Rectangle(left, bottom, right, top);
    }
}

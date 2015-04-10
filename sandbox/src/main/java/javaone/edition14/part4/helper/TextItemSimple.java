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
import com.itextpdf.text.pdf.parser.LineSegment;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * Subclass of the MyItem class that is used to store the coordinates
 * of a text snippet.
 */
public class TextItemSimple extends MyItem {
    /** Position of the baseline of the text. */
    float baseline;

    /**
     * Creates a TextItem based on a TextRenderInfo object.
     * @param textRenderInfo    the TextRenderInfo object
     */
    public TextItemSimple(TextRenderInfo textRenderInfo) {
        baseline = textRenderInfo.getBaseline().getStartPoint().get(1);
        rectangle = getRectangle(textRenderInfo);
        color = BaseColor.BLUE;
    }

    /**
     * @see MyItem#getLL()
     */
    @Override
    public Point getLL() {
        return new Point(getRectangle().getLeft(), baseline);
    }

    /**
     * Stores the start and end points and the ascent and descent info from
     * a text snippet into a Rectangle object.
     * @param textRenderInfo    Object that contains info about a text snippet
     * @return coordinates in the form of a Rectangle object
     */
    static Rectangle getRectangle(TextRenderInfo textRenderInfo) {
        LineSegment descentLine = textRenderInfo.getDescentLine();
        LineSegment ascentLine = textRenderInfo.getAscentLine();
        float x0 = descentLine.getStartPoint().get(0);
        float x1 = descentLine.getEndPoint().get(0);
        float y0 = descentLine.getStartPoint().get(1);
        float y1 = ascentLine.getEndPoint().get(1);
        return new Rectangle(x0, y0, x1, y1);
    }

}

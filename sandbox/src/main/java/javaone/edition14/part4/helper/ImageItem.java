/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part4.helper;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.Matrix;

/**
 * Subclass of the MyItem class that is used to store the coordinates
 * of an image.
 */
public class ImageItem extends MyItem {
    /** Images will be marked in this color. */
    public static final BaseColor IMG_COLOR = BaseColor.RED;
    
    /**
     * Creates an ImageItem based on an ImageRenderInfo object.
     * @param imageRenderInfo Object containing info about an image
     */
    public ImageItem(ImageRenderInfo imageRenderInfo) {
        super();
        rectangle = getRectangle(imageRenderInfo);
        color = IMG_COLOR;
    }

    /**
     * Converts the Matrix containing the coordinates of an image as stored
     * in an ImageRenderInfo object into a Rectangle.
     * @param imageRenderInfo   Object that contains info about an image
     * @return coordinates in the form of a Rectangle object
     */
    private static Rectangle getRectangle(ImageRenderInfo imageRenderInfo) {
        Matrix ctm = imageRenderInfo.getImageCTM();
        return new Rectangle(ctm.get(6), ctm.get(7), ctm.get(6) + ctm.get(0), ctm.get(7) + ctm.get(4));
    }

}

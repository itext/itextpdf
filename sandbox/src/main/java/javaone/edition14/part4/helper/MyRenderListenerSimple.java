/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part4.helper;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;

/**
 * Implementation of the RenderListener interface that generates a list
 * of MyItem objects while parsing a PDF page.
 */
public class MyRenderListenerSimple implements RenderListener {
    /** The resulting list of items after parsing. */
    List<MyItem> items = new ArrayList<MyItem>();

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
     */
    public void beginTextBlock() { }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    public void renderText(TextRenderInfo textRenderInfo) {
        items.add(new TextItemSimple(textRenderInfo));
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
     */
    public void endTextBlock() {
    }

     /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(com.itextpdf.text.pdf.parser.ImageRenderInfo)
     */
    public void renderImage(ImageRenderInfo imageRenderInfo) {
        items.add(new ImageItem(imageRenderInfo));
    }

    /**
     * Getter for the items that were encountered.
     * @return a list of MyItem objects
     */
    public List<MyItem> getItems() {
        return items;
    }
}

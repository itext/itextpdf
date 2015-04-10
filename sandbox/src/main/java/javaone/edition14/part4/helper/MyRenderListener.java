/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part4.helper;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the RenderListener interface that generates a list
 * of MyItem objects while parsing a PDF page.
 */
public class MyRenderListener implements RenderListener {
    /** The Y coordinate of the top margin. */
    float top;
    /** The resulting list of items after parsing. */
    List<MyItem> items = new ArrayList<MyItem>();

    /**
     * Creates an instance of this RenderListener.
     * @param top the Y coordinate of the top margin.
     */
    public MyRenderListener(float top) {
        this.top = top;
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
     */
    public void beginTextBlock() { }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    public void renderText(TextRenderInfo textRenderInfo) {
        items.add(new TextItem(textRenderInfo, top));
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

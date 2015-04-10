/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part2;

import com.itextpdf.text.pdf.parser.ImageRenderInfo;
import com.itextpdf.text.pdf.parser.RenderListener;
import com.itextpdf.text.pdf.parser.TextRenderInfo;
import com.itextpdf.text.pdf.parser.Vector;
import java.io.PrintWriter;

/**
 * A very simple text render listener that writes snippets of text to a PrintWriter.
 */
public class MyTextRenderListener implements RenderListener {
    /** The print writer to which the information will be written. */
    protected PrintWriter out;

    /**
     * Creates a RenderListener that will look for text.
     * @param out
     */
    public MyTextRenderListener(PrintWriter out) {
        this.out = out;
    }
    
    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#beginTextBlock()
     */
    public void beginTextBlock() {
        out.println("<");
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#endTextBlock()
     */
    public void endTextBlock() {
        out.println(">");
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderImage(
     *     com.itextpdf.text.pdf.parser.ImageRenderInfo)
     */
    public void renderImage(ImageRenderInfo renderInfo) {
    }

    /**
     * @see com.itextpdf.text.pdf.parser.RenderListener#renderText(
     *     com.itextpdf.text.pdf.parser.TextRenderInfo)
     */
    public void renderText(TextRenderInfo renderInfo) {
        out.println("    <");
        Vector start = renderInfo.getBaseline().getStartPoint();
        out.println(String.format("        x: %s y: %s length: %s \n        Text: %s",
                start.get(Vector.I1), start.get(Vector.I2),
                renderInfo.getBaseline().getLength(),
                renderInfo.getText()));
        out.println("    >");
    }
}

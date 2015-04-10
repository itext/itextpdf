/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javaone.edition14.part4.helper.Line;
import javaone.edition14.part4.helper.MyItem;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;

/**
 * In this second example that parses a PDF to discover its structure,
 * we'll highlight all the text lines and images that are encountered.
 */
public class S09_FindLines extends S08_FindItems {
    /** The resulting PDF after parsing for structure. */
    public static final String DEST = "results/javaone/edition2014/09_page229_lines.pdf";
    /**
     * Reads the first page of a document of which the top margin is 48pt heigh
     * and highlights text lines and images.
     * @param args No arguments needed
     * @throws IOException
     * @throws DocumentException 
     */
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        S09_FindLines app = new S09_FindLines();
        PdfReader reader = new PdfReader(SRC);
        List<MyItem> items = app.getContentItems(reader, 1, 48);
        Collections.sort(items);
        List<Line> lines = app.getLines(items);
        app.highlight(lines, reader, 1, DEST);
    }

    /**
     * Converts a list of items into a list of lines. This method assumes
     * that all the items are sorted in the logical reading order.
     * @param items a list of text and image items
     * @return a list of line items
     */
    public List<Line> getLines(List<MyItem> items) {
        List<Line> lines = new ArrayList<Line>();
        List<MyItem> line = new ArrayList<MyItem>();
        for (MyItem item : items) {
            if (line.isEmpty()) {
                line.add(item);
                continue;
            }
            if (areOnSameLine(line.get(line.size() - 1), item)) {
                line.add(item);
            } else {
                lines.add(new Line(line));
                line = new ArrayList<MyItem>();
                line.add(item);
            }
        }
        if (!line.isEmpty())
            lines.add(new Line(line));
        return lines;
    }
    
    /**
     * Checks if 2 items are on the same line.
     * @param i1 first item
     * @param i2 second item
     * @return true if items are on the same line, otherwise false
     */
    static boolean areOnSameLine(MyItem i1, MyItem i2) {
        return Math.abs(i1.getLL().getY() - i2.getLL().getY()) <= MyItem.itemPositionTolerance;
    }
}

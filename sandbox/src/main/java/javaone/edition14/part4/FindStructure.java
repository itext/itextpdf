/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part4;

import javaone.edition14.part4.helper.Line;
import javaone.edition14.part4.helper.MyItem;
import javaone.edition14.part4.helper.Structure;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In this second example that parses a PDF to discover its structure,
 * we'll highlight all the structures that are encountered.
 */
public class FindStructure extends FindLines {
    /** The resulting PDF after parsing for structure. */
    public static final String DEST = "results/javaone/edition2014/part4/page229_structure.pdf";
    /**
     * Reads the first page of a document of which the top margin is 48pt heigh
     * and highlights structures.
     * @param args No arguments needed
     * @throws IOException
     * @throws DocumentException 
     */
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        FindStructure app = new FindStructure();
        PdfReader reader = new PdfReader(SRC);
        List<MyItem> items = app.getContentItems(reader, 1, 48);
        Collections.sort(items);
        List<Line> lines = app.getLines(items);
        List<Structure> structures = app.getStructures(lines);
        app.highlight(structures, reader, 1, DEST);
    }

    /**
     * Combines lines into structures
     * @param lines a list of lines
     * @return list of structures
     */
    public List<Structure> getStructures(List<Line> lines) {
        List<Structure> structures = new ArrayList<Structure>();
        List<MyItem> structure = new ArrayList<MyItem>();
        for (Line line : lines) {
            if (structure.isEmpty()) {
                structure.add(line);
                continue;
            }
            if (areInSameStructure((Line)structure.get(structure.size() - 1), line)) {
                structure.add(line);
            } else {
                structures.add(new Structure(structure));
                structure = new ArrayList<MyItem>();
                structure.add(line);
            }
        }
        if (!structure.isEmpty())
            structures.add(new Structure(structure));
        return structures;
    }
    
    /**
     * Checks if two adjacent lines belong to the same structure
     * @param i1
     * @param i2
     * @return
     */
    static boolean areInSameStructure(Line i1, Line i2) {
        if (!i1.getColor().equals(i2.getColor()))
            return false;
        else if (i2.getRectangle().getLeft() - i1.getRectangle().getLeft() >= MyItem.itemPositionTolerance)
            return false;
        return true;
    }
}

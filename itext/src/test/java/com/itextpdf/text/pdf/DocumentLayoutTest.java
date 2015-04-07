package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DocumentLayoutTest {
    private static String CMP_FOLDER ="./src/test/resources/com/itextpdf/text/pdf/DocumentLayoutTest/";
    private static String OUTPUT_FOLDER = "./target/com/itextpdf/test/pdf/DocumentLayoutTest/";

    @BeforeClass
    public static void init() {
        new File(OUTPUT_FOLDER).mkdirs();
    }

    @Test
    public void textLeadingTest() throws IOException, DocumentException, InterruptedException {
        String file = "phrases.pdf";

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(OUTPUT_FOLDER + file));
        document.open();

        Phrase p1 = new Phrase("first, leading of 150");
        p1.setLeading(150);
        document.add(p1);
        document.add(Chunk.NEWLINE);

        Phrase p2 = new Phrase("second, leading of 500");
        p2.setLeading(500);
        document.add(p2);
        document.add(Chunk.NEWLINE);

        Phrase p3 = new Phrase();
        p3.add(new Chunk("third, leading of 20"));
        p3.setLeading(20);
        document.add(p3);

        document.close();

        // compare
        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent(OUTPUT_FOLDER + file, CMP_FOLDER + file, OUTPUT_FOLDER, "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}

package com.itextpdf.text.pdf;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class TextExpansionTest {

    public static final String DEST_FOLDER = "./target/com/itextpdf/test/pdf/TextExpansionTest/";
    public static final String SOURCE_FOLDER = "./src/test/resources/com/itextpdf/text/pdf/TextExpansionTest/";


    @Before
    public void setUp() throws Exception {
        new File(DEST_FOLDER).mkdirs();
    }


    @Test
    public void imageTaggingExpansionTest() throws IOException, DocumentException, InterruptedException {
        String filename = "TextExpansionTest.pdf";
        Document doc = new Document(PageSize.LETTER, 72, 72, 72, 72);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(DEST_FOLDER+filename));
        writer.setTagged();
        doc.open();

        Chunk c1 = new Chunk("ABC");
        c1.setTextExpansion("the alphabet");
        Paragraph p1 = new Paragraph();
        p1.add(c1);
        doc.add(p1);

        PdfTemplate t = writer.getDirectContent().createTemplate(6, 6);
        t.setLineWidth(1f);
        t.circle(3f, 3f, 1.5f);
        t.setGrayFill(0);
        t.fillStroke();
        Image i = Image.getInstance(t);
        Chunk c2 = new Chunk(i, 100, -100);
        doc.add(c2);

        Chunk c3 = new Chunk("foobar");
        c3.setTextExpansion("bar bar bar");
        Paragraph p3 = new Paragraph();
        p3.add(c3);
        doc.add(p3);

        doc.close();


        CompareTool compareTool = new CompareTool();
        String error = compareTool.compareByContent(DEST_FOLDER + filename, SOURCE_FOLDER + "cmp_" + filename, DEST_FOLDER, "diff_");
        if (error != null) {
            Assert.fail(error);
        }
    }
}

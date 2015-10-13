package com.itextpdf.text.pdf.fonts;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;

public class SymbolTest {

    @BeforeClass
    public static void setUp() throws Exception {
        new File("./target/com/itextpdf/text/pdf/fonts/SymbolFontTest").mkdirs();
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void textWithSymbolEncoding() throws Exception {
        BaseFont f = BaseFont.createFont(BaseFont.SYMBOL, BaseFont.SYMBOL, false);
        FileOutputStream fs = new FileOutputStream("./target/com/itextpdf/text/pdf/fonts/SymbolFontTest/textWithSymbolEncoding.pdf");
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, fs);
        Paragraph p;
        writer.setCompressionLevel(0);
        doc.open();

        String origText = "ΑΒΓΗ€\u2022\u2663\u22c5";
        p = new Paragraph(new Chunk(origText, new Font(f, 16)));
        doc.add(p);
        doc.close();

        PdfReader reader = new PdfReader("./target/com/itextpdf/text/pdf/fonts/SymbolFontTest/textWithSymbolEncoding.pdf");
        String text = PdfTextExtractor.getTextFromPage(reader, 1, new SimpleTextExtractionStrategy());
        reader.close();
        Assert.assertEquals(origText, text);

        CompareTool compareTool = new CompareTool();
        String errorMessage = compareTool.compareByContent("./target/com/itextpdf/text/pdf/fonts/SymbolFontTest/textWithSymbolEncoding.pdf", "./src/test/resources/com/itextpdf/text/pdf/fonts/SymbolFontTest/cmp_textWithSymbolEncoding.pdf", "./target/com/itextpdf/text/pdf/fonts/SymbolFontTest/", "diff");
        if (errorMessage != null) {
            Assert.fail(errorMessage);
        }
    }
}

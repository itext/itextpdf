package com.itextpdf.text.pdf.fonts;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;

import java.io.File;
import java.io.FileOutputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SymbolTest {
    @Before
    public void setUp() throws Exception {
        new File("./target/com/itextpdf/text/pdf/fonts/SymbolFontTest").mkdirs();
    }

    @After
    public void tearDown() throws Exception {
    }
    @Test
    @Ignore
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

package com.itextpdf.text.pdf.fonts;

import com.itextpdf.testutils.CompareTool;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncodingTest {
    public final static String sourceFolder = "./src/test/resources/com/itextpdf/text/pdf/fonts/EncodingTest/";
    private static String outFolder = "./target/com/itextpdf/text/pdf/fonts/EncodingTest/";

    @BeforeClass
    public static void setUp() throws Exception {
        new File(outFolder).mkdirs();
    }

    @Test
    public void exoticCharsTimesRomanTest() throws IOException, DocumentException, InterruptedException {
        String filename = "exoticCharsTimesRomanTest.pdf";
        BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.WINANSI, true);
        Document doc = new Document();

        PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        doc.newPage();
        Paragraph p = new Paragraph();
        p.setFont(new Font(bf));
        p.add("\u0188");
        doc.add(p);
        doc.close();

        new CompareTool().compareByContent(outFolder + filename, sourceFolder + "cmp_" + filename, outFolder, "diff");
    }

    @Test
    public void exoticCharsWithDifferencesTimesRomanTest() throws IOException, DocumentException, InterruptedException {
        String filename = "exoticCharsWithDifferencesTimesRomanTest.pdf";
        BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, "# simple 32 0020 0188", true);
        Document doc = new Document();

        PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        doc.newPage();
        Paragraph p = new Paragraph();
        p.setFont(new Font(bf));
        p.add("\u0188");
        doc.add(p);
        doc.close();

        new CompareTool().compareByContent(outFolder + filename, sourceFolder + "cmp_" + filename, outFolder, "diff");
    }

    @Test
    public void exoticCharsCourierTest() throws IOException, DocumentException, InterruptedException {
        String filename = "exoticCharsCourierTest.pdf";
        BaseFont bf = BaseFont.createFont(BaseFont.COURIER, BaseFont.WINANSI, true);
        Document doc = new Document();

        PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        doc.newPage();
        Paragraph p = new Paragraph();
        p.setFont(new Font(bf));
        p.add("\u0188");
        doc.add(p);
        doc.close();

        new CompareTool().compareByContent(outFolder + filename, sourceFolder + "cmp_" + filename, outFolder, "diff");
    }

    @Test
    public void exoticCharsWithDifferencesCourierTest() throws IOException, DocumentException, InterruptedException {
        String filename = "exoticCharsWithDifferencesCourierTest.pdf";
        BaseFont bf = BaseFont.createFont(BaseFont.COURIER, "# simple 32 0020 0188", true);
        Document doc = new Document();

        PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        doc.newPage();
        Paragraph p = new Paragraph();
        p.setFont(new Font(bf));
        p.add("\u0188");
        doc.add(p);
        doc.close();

        new CompareTool().compareByContent(outFolder + filename, sourceFolder + "cmp_" + filename, outFolder, "diff");
    }

    @Test
    public void surrogatePairTest() throws IOException, DocumentException, InterruptedException {
        String filename = "surrogatePairTest.pdf";
        BaseFont bf = BaseFont.createFont(sourceFolder + "DejaVuSans.ttf", BaseFont.IDENTITY_H, true);
        Document doc = new Document();

        PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        doc.newPage();
        Paragraph p = new Paragraph();
        p.setFont(new Font(bf));
        p.add("\uD800\uDF1D");
        doc.add(p);
        doc.close();

        new CompareTool().compareByContent(outFolder + filename, sourceFolder + "cmp_" + filename, outFolder, "diff");
    }

    @Test
    public void exoticCharsFreeSansTest() throws IOException, DocumentException, InterruptedException {
        String filename = "exoticCharsFreeSansTest.pdf";
        BaseFont bf = BaseFont.createFont(sourceFolder + "FreeSans.ttf", BaseFont.WINANSI, true);
        Document doc = new Document();

        PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        doc.newPage();
        Paragraph p = new Paragraph();
        p.setFont(new Font(bf));
        p.add("\u0188");
        doc.add(p);
        doc.close();

        new CompareTool().compareByContent(outFolder + filename, sourceFolder + "cmp_" + filename, outFolder, "diff");
    }

    @Test
    public void exoticCharsFreeSansWithDifferencesTest() throws IOException, DocumentException, InterruptedException {
        String filename = "exoticCharsFreeSansWithDifferencesTest.pdf";
        BaseFont bf = BaseFont.createFont(sourceFolder + "FreeSans.ttf", "# simple 32 0020 1031D", true);
        Document doc = new Document();

        PdfWriter.getInstance(doc, new FileOutputStream(outFolder + filename));
        doc.open();
        doc.newPage();
        Paragraph p = new Paragraph();
        p.setFont(new Font(bf));
        p.add("\u0188");
        doc.add(p);
        doc.close();

        new CompareTool().compareByContent(outFolder + filename, sourceFolder + "cmp_" + filename, outFolder, "diff");
    }
}

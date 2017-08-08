package com.itextpdf.text.pdf.BarcodeDatamatrixTest;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.testutils.CompareTool;
import com.itextpdf.testutils.ITextTest;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BarcodeDatamatrix;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class BarcodeDatamatrixTest extends ITextTest {
    public static final String sourceFolder = "./src/test/resources/com/itextpdf/text/pdf/BarcodeDatamatrix/";
    public static final String destinationFolder = "./target/com/itextpdf/test/BarcodeDatamatrix/";

    @Before
    public void setUp() {
        new File(destinationFolder).mkdirs();
    }


    protected void makePdf(String outPdf) throws Exception {

    }

    protected String getOutPdf() {
        return null;
    }

    @Test
    public void barcode01Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix01.pdf";
        String code = "AAAAAAAAAA;BBBBAAAA3;00028;BBBAA05;AAAA;AAAAAA;1234567;AQWXSZ;JEAN;;;;7894561;AQWXSZ;GEO;;;;1;1;1;1;0;0;1;0;1;0;0;0;1;0;1;0;0;0;0;0;0;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 01"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void barcode02Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix02.pdf";
        String code = "дима";
        String encoding = "UTF-8";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 02"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix(code, encoding);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void barcode03Test() throws DocumentException, IOException, InterruptedException {
        String filename = "barcodeDataMatrix03.pdf";
        String code = "AbcdFFghijklmnopqrstuWXSQ";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 03"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setWidth(36);
        barcode.setHeight(12);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.BLACK,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void barcode04Test() throws DocumentException, IOException, InterruptedException{
        String filename = "barcodeDataMatrix04.pdf";
        String code = "01AbcdefgAbcdefg123451231231234";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 04"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setWidth(36);
        barcode.setHeight(12);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.BLACK,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));

    }

    @Test
    public void barcode05Test()throws DocumentException, IOException, InterruptedException {
        String filename = "barcodeDataMatrix05.pdf";
        String code = "aaabbbcccdddAAABBBAAABBaaabbbcccdddaaa";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 05"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setWidth(40);
        barcode.setHeight(40);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.BLACK,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));


    }

    @Test
    public void barcode06Test()throws DocumentException, IOException, InterruptedException {
        String filename = "barcodeDataMatrix06.pdf";
        String code = ">>>\r>>>THIS VERY TEXT>>\r>";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 06"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setWidth(36);
        barcode.setHeight(12);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.BLACK,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void barcode07Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix07.pdf";
        String code = "AAAAAAAAAA;BBBBAAAA3;00028;BBBAA05;AAAA;AAAAAA;1234567;AQWXSZ;JEAN;;;;7894561;AQWXSZ;GEO;;;;1;1;1;1;0;0;1;0;1;0;0;0;1;0;1;0;0;0;0;0;0;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 07"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setOptions(BarcodeDatamatrix.DM_ASCII);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void barcode08Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix08.pdf";
        String code = "AAAAAAAAAA;BBBBAAAA3;00028;BBBAA05;AAAA;AAAAAA;1234567;AQWXSZ;JEAN;;;;7894561;AQWXSZ;GEO;;;;1;1;1;1;0;0;1;0;1;0;0;0;1;0;1;0;0;0;0;0;0;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 08"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setOptions(BarcodeDatamatrix.DM_C40);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void barcode09Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix09.pdf";
        String code = "AAAAAAAAAA;BBBBAAAA3;00028;BBBAA05;AAAA;AAAAAA;1234567;AQWXSZ;JEAN;;;;7894561;AQWXSZ;GEO;;;;1;1;1;1;0;0;1;0;1;0;0;0;1;0;1;0;0;0;0;0;0;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 09"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setOptions(BarcodeDatamatrix.DM_TEXT);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void barcode10Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix10.pdf";
        String code = "AAAAAAAAAA;BBBBAAAA3;00028;BBBAA05;AAAA;AAAAAA;1234567;AQWXSZ;JEAN;;;;7894561;AQWXSZ;GEO;;;;1;1;1;1;0;0;1;0;1;0;0;0;1;0;1;0;0;0;0;0;0;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 10"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setOptions(BarcodeDatamatrix.DM_B256);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }

    @Test
    public void barcode11Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix11.pdf";
        String code = "AAAAAAAAAA;BBBBAAAA3;00028;BBBAA05;AAAA;AAAAAA;1234567;AQWXSZ;JEAN;;;;7894561;AQWXSZ;GEO;;;;1;1;1;1;0;0;1;0;1;0;0;0;1;0;1;0;0;0;0;0;0;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 11"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setOptions(BarcodeDatamatrix.DM_X12);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }
    @Test
    public void barcode12Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix12.pdf";
        String code = "AAAAAAAAAA;BBBBAAAA3;00028;BBBAA05;AAAA;AAAAAA;1234567;AQWXSZ;JEAN;;;;7894561;AQWXSZ;GEO;;;;1;1;1;1;0;0;1;0;1;0;0;0;1;0;1;0;0;0;0;0;0;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 12"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setOptions(BarcodeDatamatrix.DM_EDIFACT);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }
    @Test
    public void barcode13Test() throws IOException, DocumentException, InterruptedException {
        String filename = "barcodeDataMatrix13.pdf";
        String code = "AAAAAAAAAA;BBBBAAAA3;00028;BBBAA05;AAAA;AAAAAA;1234567;AQWXSZ;JEAN;;;;7894561;AQWXSZ;GEO;;;;1;1;1;1;0;0;1;0;1;0;0;0;1;0;1;0;0;0;0;0;0;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1;1";

        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destinationFolder + filename));
        document.open();
        document.add(new Paragraph("Datamatrix test 13"));
        PdfContentByte cb = writer.getDirectContent();
        cb.concatCTM(AffineTransform.getTranslateInstance(PageSize.A4.getWidth()/2-100,PageSize.A4.getHeight()/2-100));
        BarcodeDatamatrix barcode = new BarcodeDatamatrix();
        barcode.setOptions(BarcodeDatamatrix.DM_RAW);
        barcode.generate(code);
        barcode.placeBarcode(cb, BaseColor.GREEN,5,5);
        document.close();

        Assert.assertNull(new CompareTool().compareByContent(destinationFolder + filename, sourceFolder + "cmp_" + filename, destinationFolder, "diff_"));
    }
}

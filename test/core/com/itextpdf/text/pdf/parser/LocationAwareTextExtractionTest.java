/*
 * Created on Dec 21, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;


import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author kevin
 */
public class LocationAwareTextExtractionTest extends SimpleTextExtractionTest{

    @Override
    @Before
    public void setUp() throws Exception {
    }

    @Override
    @After
    public void tearDown() throws Exception {
    }

    @Override
    public TextProvidingRenderListener createRenderListenerForTest() {
        return new LocationAwareTextExtractingPdfContentRenderListener();
    }

    @Test
    public void testYPosition() throws Exception{
        PdfReader r = createPdfWithOverlappingTextVertical(new String[]{"A", "B", "C", "D"}, new String[]{"AA", "BB", "CC", "DD"});

        PdfTextExtractor ex = new PdfTextExtractor(r, createRenderListenerForTest());
        String text = ex.getTextFromPage(1);

        Assert.assertEquals("A\nAA\nB\nBB\nC\nCC\nD\nDD", text);
    }

    @Test
    public void testXPosition() throws Exception{
        PdfReader r = createPdfWithOverlappingTextHorizontal(new String[]{"A", "B", "C", "D"}, new String[]{"AA", "BB", "CC", "DD"});

        PdfTextExtractor ex = new PdfTextExtractor(r, createRenderListenerForTest());
        String text = ex.getTextFromPage(1);

//        Assert.assertEquals("A AA B BB C CC D DD", text);
        Assert.assertEquals("A\tAA\tB\tBB\tC\tCC\tD\tDD", text);
    }


    private PdfReader createPdfWithOverlappingTextHorizontal(String[] text1, String[] text2) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        float ystart = 500;
        float xstart = 50;

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        float x = xstart;
        float y = ystart;
        for(String text : text1){
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            x += 70.0;
        }

        x = xstart + 12;
        y = ystart;
        for(String text : text2){
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            x += 70.0;
        }
        canvas.stroke();
        canvas.endText();

        doc.close();

        //saveBytesToFile(baos.toByteArray(), new File("C:/temp/out.pdf"));

        return new PdfReader(baos.toByteArray());

    }

    private PdfReader createPdfWithOverlappingTextVertical(String[] text1, String[] text2) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        PdfContentByte canvas = writer.getDirectContent();
        float ystart = 500;

        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        float x = 50;
        float y = ystart;
        for(String text : text1){
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            y -= 25.0;
        }

        y = ystart - 13;
        for(String text : text2){
            canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
            y -= 25.0;
        }
        canvas.stroke();
        canvas.endText();

        doc.close();

        return new PdfReader(baos.toByteArray());

    }
}

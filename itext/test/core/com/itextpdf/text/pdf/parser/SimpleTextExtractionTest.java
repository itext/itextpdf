/*
 * Created on Nov 5, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;


import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;
import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author kevin
 */
public class SimpleTextExtractionTest {

    String TEXT1 = "TEXT1 TEXT1";
    String TEXT2 = "TEXT2 TEXT2";
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    public TextProvidingRenderListener createRenderListenerForTest(){
        return new SimpleTextExtractingPdfContentRenderListener();
    }
    
    @Test
    public void testCoLinnearText() throws Exception{
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 0, false, 0);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        Assert.assertEquals(TEXT1 + TEXT2, ex.getTextFromPage(1));
    }
    
    @Test
    public void testCoLinnearTextWithSpace() throws Exception{
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 0, false, 2);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        //saveBytesToFile(bytes, new File("c:/temp/test.pdf"));
        
        Assert.assertEquals(TEXT1 + " " + TEXT2, ex.getTextFromPage(1));
    }
    
    @Test
    public void testCoLinnearTextEndingWithSpaceCharacter() throws Exception{
        // in this case, we shouldn't be inserting an extra space
        TEXT1 = TEXT1 + " ";
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 0, false, 2);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        //saveBytesToFile(bytes, new File("c:/temp/test.pdf"));
        
        Assert.assertEquals(TEXT1 + TEXT2, ex.getTextFromPage(1));
        
    }    
    @Test
    public void testUnRotatedText() throws Exception{
        
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 0, true, -20);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        Assert.assertEquals(TEXT1 + "\n" + TEXT2, ex.getTextFromPage(1));
        
    }

    
    @Test
    public void testRotatedText() throws Exception{
        
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, -90, true, -20);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        Assert.assertEquals(TEXT1 + "\n" + TEXT2, ex.getTextFromPage(1));

    }
    
    @Test
    public void testRotatedText2() throws Exception{
        
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 90, true, -20);
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        Assert.assertEquals(TEXT1 + "\n" + TEXT2, ex.getTextFromPage(1));

    }

    @Test
    public void testPartiallyRotatedText() throws Exception{
        
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 33, true, -20);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        Assert.assertEquals(TEXT1 + "\n" + TEXT2, ex.getTextFromPage(1));
        
    }
    
    @Test
    public void testWordSpacingCausedByExplicitGlyphPositioning() throws Exception{
        byte[] bytes = createPdfWithArrayText(TEXT1, TEXT2, 250);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        Assert.assertEquals(TEXT1 + " " + TEXT2, ex.getTextFromPage(1));
    }
    
    
    @Test
    public void testWordSpacingCausedByExplicitGlyphPositioning2() throws Exception{
        
        byte[] bytes = createPdfWithArrayText("[(S)3.2(an)-255.0(D)13.0(i)8.3(e)-10.1(g)1.6(o)-247.5(C)2.4(h)5.8(ap)3.0(t)10.7(er)]TJ");
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        Assert.assertEquals("San Diego Chapter", ex.getTextFromPage(1));
    }
    
    
    @Test
    public void testTrailingSpace() throws Exception{
        byte[] bytes = createPdfWithRotatedText(TEXT1 + " ", TEXT2, 0, false, 6);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());

        Assert.assertEquals(TEXT1 + " " + TEXT2, ex.getTextFromPage(1));
    }

    @Test
    public void testLeadingSpace() throws Exception{
        byte[] bytes = createPdfWithRotatedText(TEXT1, " " + TEXT2, 0, false, 6);
        PdfTextExtractor ex = new PdfTextExtractor(new PdfReader(bytes), createRenderListenerForTest());
        
        Assert.assertEquals(TEXT1 + " " + TEXT2, ex.getTextFromPage(1));
    }
    
    @Test
    public void testExtractXObjectText() throws Exception {
        String text1 = "X";
        byte[] content = createPdfWithXObject(text1);
        PdfReader r = new PdfReader(content);
        
        PdfTextExtractor ex = new PdfTextExtractor(r, createRenderListenerForTest());
        String text = ex.getTextFromPage(1);
        Assert.assertTrue("extracted text (" + text + ") must contain '" + text1 + "'", text.indexOf(text1) >= 0);
    }
    

    
    byte[] createPdfWithXObject(String xobjectText) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        doc.add(new Paragraph("A"));
        doc.add(new Paragraph("B"));
        
        PdfTemplate template = writer.getDirectContent().createTemplate(100, 100);
        
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        template.moveText(5, template.getHeight()-5);
        template.showText(xobjectText);
        template.stroke();
        template.endText();
        
        Image xobjectImage = Image.getInstance(template);
        
        doc.add(xobjectImage);
        
        doc.add(new Paragraph("C"));
        
        doc.close();
        
        return baos.toByteArray();
    }    
    
    private static byte[] createPdfWithArrayText(String directContentTj) throws Exception{
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);

        document.open();

        PdfContentByte cb = writer.getDirectContent();

        BaseFont font = BaseFont.createFont();
        
        cb.transform(AffineTransform.getTranslateInstance(100, 500));
        cb.beginText();
        cb.setFontAndSize(font, 12);

        cb.getInternalBuffer().append(directContentTj + "\n");
        
        cb.endText();
        
        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;

    }    
    
    private static byte[] createPdfWithArrayText(String text1, String text2, int spaceInPoints) throws Exception{
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);

        document.open();

        PdfContentByte cb = writer.getDirectContent();

        BaseFont font = BaseFont.createFont();
        
        
        cb.beginText();
        cb.setFontAndSize(font, 12);

        cb.getInternalBuffer().append("[(" + text1 + ")" + (-spaceInPoints) + "(" + text2 + ")]TJ\n");
        
        cb.endText();
        
        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;

    }
    
    private static byte[] createPdfWithRotatedText(String text1, String text2, float rotation, boolean moveTextToNextLine, float moveTextDelta) throws Exception {

        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);

        document.open();

        PdfContentByte cb = writer.getDirectContent();

        BaseFont font = BaseFont.createFont();

        float x = document.getPageSize().getWidth()/2;
        float y = document.getPageSize().getHeight()/2;
        
        cb.transform(AffineTransform.getTranslateInstance(x, y));

        cb.moveTo(-10, 0);
        cb.lineTo(10, 0);
        cb.moveTo(0, -10);
        cb.lineTo(0, 10);
        cb.stroke();
        
        cb.beginText();
        cb.setFontAndSize(font, 12);
        cb.transform(AffineTransform.getRotateInstance(rotation/180f*Math.PI));
        cb.showText(text1);
        if (moveTextToNextLine)
            cb.moveText(0, moveTextDelta);
        else
            cb.transform(AffineTransform.getTranslateInstance(moveTextDelta, 0));
        cb.showText(text2);
        cb.endText();

        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;
    }
  
}

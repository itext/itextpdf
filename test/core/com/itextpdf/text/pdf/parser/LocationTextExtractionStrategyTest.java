/*
 * Created on Dec 21, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;


import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfTextArray;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author kevin
 */
public class LocationTextExtractionStrategyTest extends SimpleTextExtractionStrategyTest{

    @Override
    @Before
    public void setUp() throws Exception {
    }

    @Override
    @After
    public void tearDown() throws Exception {
    }

    @Override
    public TextExtractionStrategy createRenderListenerForTest() {
        return new LocationTextExtractionStrategy();
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

    @Test
    public void testRotatedPage() throws Exception{
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate(), "A\nB\nC\nD");
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));

        PdfReader r = new PdfReader(bytes);
        
        PdfTextExtractor ex = new PdfTextExtractor(r, createRenderListenerForTest());
        String text = ex.getTextFromPage(1);
        
        Assert.assertEquals("A\nB\nC\nD", text);
    }
    
    @Test
    public void testRotatedPage2() throws Exception{
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate().rotate(), "A\nB\nC\nD");
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));

        PdfReader r = new PdfReader(bytes);
        
        PdfTextExtractor ex = new PdfTextExtractor(r, createRenderListenerForTest());
        String text = ex.getTextFromPage(1);
        
        Assert.assertEquals("A\nB\nC\nD", text);
    }

    @Test
    public void testRotatedPage3() throws Exception{
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate().rotate().rotate(), "A\nB\nC\nD");
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));

        PdfReader r = new PdfReader(bytes);
        
        PdfTextExtractor ex = new PdfTextExtractor(r, createRenderListenerForTest());
        String text = ex.getTextFromPage(1);
        
        Assert.assertEquals("A\nB\nC\nD", text);
    }

    @Test
    public void testExtractXObjectTextWithRotation() throws Exception {
        //LocationAwareTextExtractingPdfContentRenderListener.DUMP_STATE = true;
        String text1 = "X";
        byte[] content = createPdfWithRotatedXObject(text1);
        //TestResourceUtils.saveBytesToFile(content, new File("C:/temp/out.pdf"));
        PdfReader r = new PdfReader(content);
        
        PdfTextExtractor ex = new PdfTextExtractor(r, createRenderListenerForTest());
        String text = ex.getTextFromPage(1);
        Assert.assertEquals("A\nB\nX\nC", text);
    }

    @Test
    public void testNegativeCharacterSpacing() throws Exception{
        byte[] content = createPdfWithNegativeCharSpacing("W", 200, "A");
        //TestResourceUtils.openBytesAsPdf(content);
        PdfReader r= new PdfReader(content);
        PdfTextExtractor ex = new PdfTextExtractor(r, createRenderListenerForTest());
        String text = ex.getTextFromPage(1);
        Assert.assertEquals("WA", text);
    }
    
    @Test
    public void testSanityCheckOnVectorMath(){
        Vector start = new Vector(0, 0, 1);
        Vector end = new Vector(1, 0, 1);
        Vector antiparallelStart = new Vector(0.9f, 0, 1);
        Vector parallelStart = new Vector(1.1f, 0, 1);
        
        float rsltAntiParallel = antiparallelStart.subtract(end).dot(end.subtract(start).normalize());
        Assert.assertEquals(-0.1f, rsltAntiParallel, 0.0001);
        
        float rsltParallel = parallelStart.subtract(end).dot(end.subtract(start).normalize());
        Assert.assertEquals(0.1f, rsltParallel, 0.0001);

    }
    
    private byte[] createPdfWithNegativeCharSpacing(String str1, float charSpacing, String str2) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        canvas.setFontAndSize(BaseFont.createFont(), 12);
        canvas.moveText(45, doc.getPageSize().getHeight() - 45);
        PdfTextArray ta = new PdfTextArray();
        ta.add(str1);
        ta.add(charSpacing);
        ta.add(str2);
        canvas.showText(ta);
        canvas.endText();
        
        doc.close();
        
        return baos.toByteArray();
    }
    
    private byte[] createPdfWithRotatedXObject(String xobjectText) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();

        doc.add(new Paragraph("A"));
        doc.add(new Paragraph("B"));
        
        boolean rotate = true;
        
        PdfTemplate template = writer.getDirectContent().createTemplate(20, 100);
        template.setColorStroke(BaseColor.GREEN);
        template.rectangle(0, 0, template.getWidth(), template.getHeight());
        template.stroke();
        AffineTransform tx = new AffineTransform();
        if (rotate){
            tx.translate(0, template.getHeight());
            tx.rotate(-90/180f*Math.PI);
        }
        template.transform(tx);
        template.beginText();
        template.setFontAndSize(BaseFont.createFont(), 12);
        if (rotate)
            template.moveText(0, template.getWidth()-12);
        else
            template.moveText(0, template.getHeight()-12);
        template.showText(xobjectText);

        template.endText();
        
        Image xobjectImage = Image.getInstance(template);
        if (rotate)
            xobjectImage.setRotationDegrees(90);
        doc.add(xobjectImage);
        
        doc.add(new Paragraph("C"));
        
        doc.close();
        
        return baos.toByteArray();
    }    
    
    private byte[] createSimplePdf(Rectangle pageSize, final String... text) throws Exception{
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            final Document document = new Document(pageSize);
            PdfWriter.getInstance(document, byteStream);
            document.open();
            for (String string : text) {
                document.add(new Paragraph(string));
                document.newPage();
            }

            document.close();

            final byte[] pdfBytes = byteStream.toByteArray();

            return pdfBytes;
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

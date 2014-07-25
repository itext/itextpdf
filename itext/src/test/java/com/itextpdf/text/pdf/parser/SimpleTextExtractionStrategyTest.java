/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.parser;

import java.io.ByteArrayOutputStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.awt.geom.AffineTransform;
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
public class SimpleTextExtractionStrategyTest {

    String TEXT1 = "TEXT1 TEXT1";
    String TEXT2 = "TEXT2 TEXT2";
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
    
    public TextExtractionStrategy createRenderListenerForTest(){
        return new SimpleTextExtractionStrategy();
    }
    
    @Test
    public void testCoLinnearText() throws Exception{
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 0, false, 0);
        
        Assert.assertEquals(TEXT1 + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
    }
    
    @Test
    public void testCoLinnearTextWithSpace() throws Exception{
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 0, false, 2);
        //saveBytesToFile(bytes, new File("c:/temp/test.pdf"));
        
        Assert.assertEquals(TEXT1 + " " + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
    }
    
    @Test
    public void testCoLinnearTextEndingWithSpaceCharacter() throws Exception{
        // in this case, we shouldn't be inserting an extra space
        TEXT1 = TEXT1 + " ";
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 0, false, 2);

        //TestResourceUtils.openBytesAsPdf(bytes);
        
        Assert.assertEquals(TEXT1 + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
        
    }    
    @Test
    public void testUnRotatedText() throws Exception{
        
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 0, true, -20);

        Assert.assertEquals(TEXT1 + "\n" + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
        
    }

    
    @Test
    public void testRotatedText() throws Exception{
        
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, -90, true, -20);

        Assert.assertEquals(TEXT1 + "\n" + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));

    }
    
    @Test
    public void testRotatedText2() throws Exception{
        
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 90, true, -20);
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));

        Assert.assertEquals(TEXT1 + "\n" + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));

    }

    @Test
    public void testPartiallyRotatedText() throws Exception{
        
        byte[] bytes = createPdfWithRotatedText(TEXT1, TEXT2, 33, true, -20);

        Assert.assertEquals(TEXT1 + "\n" + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
        
    }
    
    @Test
    public void testWordSpacingCausedByExplicitGlyphPositioning() throws Exception{
        byte[] bytes = createPdfWithArrayText(TEXT1, TEXT2, 250);

        Assert.assertEquals(TEXT1 + " " + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
    }
    
    
    @Test
    public void testWordSpacingCausedByExplicitGlyphPositioning2() throws Exception{
        
        byte[] bytes = createPdfWithArrayText("[(S)3.2(an)-255.0(D)13.0(i)8.3(e)-10.1(g)1.6(o)-247.5(C)2.4(h)5.8(ap)3.0(t)10.7(er)]TJ");

        Assert.assertEquals("San Diego Chapter", PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
    }
    
    
    @Test
    public void testTrailingSpace() throws Exception{
        byte[] bytes = createPdfWithRotatedText(TEXT1 + " ", TEXT2, 0, false, 6);

        Assert.assertEquals(TEXT1 + " " + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
    }

    @Test
    public void testLeadingSpace() throws Exception{
        byte[] bytes = createPdfWithRotatedText(TEXT1, " " + TEXT2, 0, false, 6);
        
        Assert.assertEquals(TEXT1 + " " + TEXT2, PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest()));
    }
    
    @Test
    public void testExtractXObjectText() throws Exception {
        String text1 = "X";
        byte[] bytes = createPdfWithXObject(text1);
        String text = PdfTextExtractor.getTextFromPage(new PdfReader(bytes), 1, createRenderListenerForTest());
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

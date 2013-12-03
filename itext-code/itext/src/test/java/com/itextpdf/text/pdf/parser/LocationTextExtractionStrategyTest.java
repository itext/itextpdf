/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, Kevin Day, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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

        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
        
        Assert.assertEquals("A\nAA\nB\nBB\nC\nCC\nD\nDD", text);
    }
    
    @Test
    public void testXPosition() throws Exception{
        byte[] content = createPdfWithOverlappingTextHorizontal(new String[]{"A", "B", "C", "D"}, new String[]{"AA", "BB", "CC", "DD"});
        PdfReader r = new PdfReader(content);

        //TestResourceUtils.openBytesAsPdf(content);
        
        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
        
        Assert.assertEquals("A AA B BB C CC D DD", text);
//        Assert.assertEquals("A\tAA\tB\tBB\tC\tCC\tD\tDD", text);
    }

    @Test
    public void testRotatedPage() throws Exception{
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate(), "A\nB\nC\nD");

        PdfReader r = new PdfReader(bytes);
        
        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
        
        Assert.assertEquals("A\nB\nC\nD", text);
    }
    
    @Test
    public void testRotatedPage2() throws Exception{
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate().rotate(), "A\nB\nC\nD");
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));

        PdfReader r = new PdfReader(bytes);
        
        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
        
        Assert.assertEquals("A\nB\nC\nD", text);
    }

    @Test
    public void testRotatedPage3() throws Exception{
        byte[] bytes = createSimplePdf(PageSize.LETTER.rotate().rotate().rotate(), "A\nB\nC\nD");
        //TestResourceUtils.saveBytesToFile(bytes, new File("C:/temp/out.pdf"));

        PdfReader r = new PdfReader(bytes);
        
        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
        
        Assert.assertEquals("A\nB\nC\nD", text);
    }

    @Test
    public void testExtractXObjectTextWithRotation() throws Exception {
        //LocationAwareTextExtractingPdfContentRenderListener.DUMP_STATE = true;
        String text1 = "X";
        byte[] content = createPdfWithRotatedXObject(text1);
        //TestResourceUtils.saveBytesToFile(content, new File("C:/temp/out.pdf"));
        PdfReader r = new PdfReader(content);
        
        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
        Assert.assertEquals("A\nB\nX\nC", text);
    }

    @Test
    public void testNegativeCharacterSpacing() throws Exception{
        byte[] content = createPdfWithNegativeCharSpacing("W", 200, "A");
        //TestResourceUtils.openBytesAsPdf(content);
        PdfReader r= new PdfReader(content);
        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
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
    
    @Test
    public void testSuperscript() throws Exception {
        byte[] content = createPdfWithSupescript("Hel", "lo");
        //TestResourceUtils.openBytesAsPdf(content);
        PdfReader r= new PdfReader(content);
        String text = PdfTextExtractor.getTextFromPage(r, 1, createRenderListenerForTest());
        Assert.assertEquals("Hello", text);
    	

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
    
    protected byte[] createPdfWithOverlappingTextHorizontal(String[] text1, String[] text2) throws Exception{
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
        canvas.endText();
        
        doc.close();
        
        
        return baos.toByteArray();
        
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
        canvas.endText();
        
        doc.close();
        
        return new PdfReader(baos.toByteArray());
        
    }    
    
    private byte[] createPdfWithSupescript(String regularText, String superscriptText) throws Exception{
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document();
        PdfWriter.getInstance(document, byteStream);
        document.open();
        document.add(new Chunk(regularText));
        Chunk c2 = new Chunk(superscriptText);
        c2.setTextRise(7.0f);
        document.add(c2);

        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;
    }
}

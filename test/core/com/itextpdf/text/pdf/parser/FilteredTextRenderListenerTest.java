/*
 * Created on Jan 19, 2010
 * (c) 2010 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;


import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author kevin
 */
public class FilteredTextRenderListenerTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRegion() throws Exception {
        byte[] pdf = createPdfWithCornerText();
        
        PdfReader reader = new PdfReader(pdf);
        float pageHeight = reader.getPageSize(1).getHeight();
        Rectangle upperLeft = new Rectangle(0, (int)pageHeight-30, 250, 30);
        
        Assert.assertTrue(textIsInRectangle(reader, "Upper Left", upperLeft));
        Assert.assertFalse(textIsInRectangle(reader, "Upper Right", upperLeft));
    }
    
    private boolean textIsInRectangle(PdfReader reader, String text, Rectangle rect) throws Exception{
        
        FilteredTextRenderListener filterListener = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), new RegionTextRenderFilter(rect) );
        
        PdfTextExtractor e = new PdfTextExtractor(reader, filterListener);
        
        String extractedText = e.getTextFromPage(1);
        
        return extractedText.equals(text);
    }
    
    private byte[] createPdfWithCornerText() throws Exception{
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document(PageSize.LETTER);
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.open();
        
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();

        canvas.setFontAndSize(BaseFont.createFont(), 12);
        
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "Upper Left", 10, document.getPageSize().getHeight() - 10 - 12, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Upper Right", document.getPageSize().getWidth() - 10, document.getPageSize().getHeight() - 10 - 12, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "Lower Left", 10, 10, 0);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Lower Right", document.getPageSize().getWidth() - 10, 10, 0);
        
        canvas.endText();

        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;

    }
}

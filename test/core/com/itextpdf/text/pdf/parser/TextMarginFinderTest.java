/*
 * Created on Mar 24, 2010
 * (c) 2010 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;


import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author kevin
 */
public class TextMarginFinderTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBasics() throws Exception{
        Rectangle rToDraw = new Rectangle(1.42f*72f, 2.42f*72f, 7.42f*72f, 10.42f*72f);
        rToDraw.setBorder(Rectangle.BOX);
        rToDraw.setBorderWidth(1.0f);
        
        byte[] content = createPdf(rToDraw);
        //TestResourceUtils.openBytesAsPdf(content);
        
        PdfReader r= new PdfReader(content);
        TextMarginFinder finder = new TextMarginFinder();
        PdfDictionary pageDic = r.getPageN(1);
        PdfDictionary resourcesDic = pageDic.getAsDict(PdfName.RESOURCES);
        finder.reset();
        PdfContentStreamProcessor processor = new PdfContentStreamProcessor(finder);
        processor.processContent(ContentByteUtils.getContentBytesForPage(r, 1), resourcesDic);

        Assert.assertEquals(1.42f*72f, finder.getLlx(), 0.01f);
        Assert.assertEquals(7.42f*72f, finder.getUrx(), 0.01f);
        Assert.assertEquals(2.42f*72f, finder.getLly(), 0.01f);
        Assert.assertEquals(10.42f*72f, finder.getUry(), 0.01f);

    }
    
    private byte[] createPdf(Rectangle recToDraw) throws DocumentException, IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        
        
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        float fontsiz = 12;
        
        float llx = 1.42f * 72f;
        float lly = 2.42f * 72f;
        float urx = 7.42f * 72f;
        float ury = 10.42f * 72f;

        BaseFont font = BaseFont.createFont();
        canvas.setFontAndSize(font, fontsiz);

        float ascent = font.getFontDescriptor(BaseFont.ASCENT, fontsiz);
        float descent = font.getFontDescriptor(BaseFont.DESCENT, fontsiz);

        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "LowerLeft", llx, lly-descent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "LowerRight", urx, lly-descent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "UpperLeft", llx, ury - ascent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "UpperRight", urx, ury - ascent, 0.0f);
        canvas.endText();
        
        if (recToDraw != null){
            doc.add(recToDraw);
        }
        
        doc.close();
        
        return baos.toByteArray();
    }
}

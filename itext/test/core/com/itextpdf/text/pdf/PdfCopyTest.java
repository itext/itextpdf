/*
 * Created on Aug 6, 2010
 * (c) 2010 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;

/**
 * @author kevin
 */
public class PdfCopyTest {

    @Before
    public void setUp() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @After
    public void tearDown() throws Exception {
        TestResourceUtils.purgeTempFiles();
    }

    @Test
    /**
     * Test to demonstrate issue https://sourceforge.net/tracker/?func=detail&aid=3013642&group_id=15255&atid=115255
     */
    public void testExtraXObjects() throws Exception {
        PdfReader sourceR = new PdfReader(createImagePdf());
        int sourceXRefCount = sourceR.getXrefSize();
        
        final Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfCopy copy = new PdfCopy(document, out);
        document.open();
        PdfImportedPage importedPage = copy.getImportedPage(sourceR, 1);
        copy.addPage(importedPage);
        document.close();
        
        PdfReader targetR = new PdfReader(out.toByteArray());
        int destinationXRefCount = targetR.getXrefSize();
        
//        TestResourceUtils.saveBytesToFile(createImagePdf(), new File("./source.pdf"));
//        TestResourceUtils.saveBytesToFile(out.toByteArray(), new File("./result.pdf"));
        
        Assert.assertEquals(sourceXRefCount, destinationXRefCount);
        
    }
    
    private static byte[] createImagePdf() throws Exception {

        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        final Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, byteStream);
        document.setPageSize(PageSize.LETTER);

        document.open();
        
        BufferedImage awtImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB); 
        Graphics2D g2d = awtImg.createGraphics();
        g2d.setColor(Color.green);
        g2d.fillRect(10, 10, 80, 80);
        g2d.dispose();
        
        com.itextpdf.text.Image itextImg = com.itextpdf.text.Image.getInstance(awtImg, null);
        document.add(itextImg);               

        document.close();

        final byte[] pdfBytes = byteStream.toByteArray();

        return pdfBytes;
    }
}

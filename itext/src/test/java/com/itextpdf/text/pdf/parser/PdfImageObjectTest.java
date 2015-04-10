/*
 * Created on Sep 2, 2011
 * (c) 2011 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author kevin
 */
public class PdfImageObjectTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    private void testFile(String filename, int page, String objectid) throws Exception{
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, filename);
        try{
            PdfDictionary resources = pdfReader.getPageResources(page);
            PdfDictionary xobjects = resources.getAsDict(PdfName.XOBJECT);
            PdfIndirectReference objRef = xobjects.getAsIndirectObject(new PdfName(objectid));
            if (objRef == null)
                throw new NullPointerException("Reference " + objectid + " not found - Available keys are " + xobjects.getKeys());
            PRStream stream = (PRStream)PdfReader.getPdfObject(objRef);
            PdfDictionary colorSpaceDic = resources != null ? resources.getAsDict(PdfName.COLORSPACE) : null;
            PdfImageObject img = new PdfImageObject(stream, colorSpaceDic);
            byte[] result = img.getImageAsBytes();
            Assert.assertNotNull(result);
            int zeroCount = 0;
            for (byte b : result) {
                if (b == 0) zeroCount++;
            }
            Assert.assertTrue(zeroCount > 0);
        } finally {
            pdfReader.close();
        }
    }
    
    @Test
    public void testMultiStageFilters() throws Exception{
        testFile("multistagefilter1.pdf", 1, "Obj13");
    }

    @Test
    public void testAscii85Filters() throws Exception{
        testFile("ASCII85_RunLengthDecode.pdf", 1, "Im9");
    }

    @Test
    public void testCcittFilters() throws Exception{
        testFile("ccittfaxdecode.pdf", 1, "background0");
    }

    @Test
    public void testFlateDecodeFilters() throws Exception{
        testFile("flatedecode_runlengthdecode.pdf", 1, "Im9");
    }

    @Test
    public void testDctDecodeFilters() throws Exception{
        testFile("dctdecode.pdf", 1, "im1");
    }
    
    @Test
    public void testjbig2Filters() throws Exception{
        testFile("jbig2decode.pdf", 1, "2");
    }
    
}

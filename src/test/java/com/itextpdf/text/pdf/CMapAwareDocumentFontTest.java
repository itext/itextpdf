/*
 * Created on Nov 2, 2011
 * (c) 2011 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;

/**
 * @author Kevin
 */
public class CMapAwareDocumentFontTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWidths() throws Exception{
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "fontwithwidthissue.pdf");

        try {
            PdfDictionary fontsDic = pdfReader.getPageN(1).getAsDict(PdfName.RESOURCES).getAsDict(PdfName.FONT);
            PRIndirectReference fontDicIndirect = (PRIndirectReference)fontsDic.get(new PdfName("F1"));
            
            CMapAwareDocumentFont f = new CMapAwareDocumentFont(fontDicIndirect);
            Assert.assertTrue("Width should not be 0", f.getWidth('h') != 0);
        } finally {
            pdfReader.close();
        }
    }

}

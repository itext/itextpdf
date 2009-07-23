/*
 * Created on Jul 9, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.lowagie.text.pdf;


import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author kevin
 */
public class DocumentFontTest {

    private static File resourceRoot;
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        resourceRoot = new File("test/core/com/lowagie/text/pdf");
    }
    
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConstructionForType0WithoutToUnicodeMap() throws Exception{
        int pageNum = 2;
        PdfName fontIdName = new PdfName("TT9");
        
        RandomAccessFileOrArray f = new RandomAccessFileOrArray("type0FontWithoutToUnicodeMap.pdf");
        PdfReader reader = new PdfReader(f, null);
        
        PdfDictionary fontsDic = reader.getPageN(pageNum).getAsDict(PdfName.RESOURCES).getAsDict(PdfName.FONT);
        PdfDictionary fontDicDirect = fontsDic.getAsDict(fontIdName);
        PRIndirectReference fontDicIndirect = (PRIndirectReference)fontsDic.get(fontIdName);
        
        Assert.assertEquals(PdfName.TYPE0, fontDicDirect.getAsName(PdfName.SUBTYPE));
        Assert.assertEquals("/Identity-H", fontDicDirect.getAsName(PdfName.ENCODING).toString());
        Assert.assertNull("This font should not have a ToUnicode map", fontDicDirect.get(PdfName.TOUNICODE));
        
        new DocumentFont(fontDicIndirect); // this used to throw an NPE
    }
}

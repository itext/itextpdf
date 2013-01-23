/*
 * Created on Feb 20, 2012
 * (c) 2012 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.fonts.cmaps;

import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;

/**
 * @author kevin
 */
public class CMapParserExTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCMapWithDefDictionaryKey() throws Exception{
        byte[] touni = TestResourceUtils.getResourceAsByteArray(this, "cmap_with_def_dictionary_key.txt");
        CidLocationFromByte lb = new CidLocationFromByte(touni);
        CMapToUnicode cmapRet = new CMapToUnicode();
        CMapParserEx.parseCid("", cmapRet, lb);
    }
    
    @Test
    public void testCMapThatResultsInEmptyMap() throws Exception{
        byte[] touni = TestResourceUtils.getResourceAsByteArray(this, "cmap_results_in_empty_map.cmap");
        CidLocationFromByte lb = new CidLocationFromByte(touni);
        CMapToUnicode cmapRet = new CMapToUnicode();
        CMapParserEx.parseCid("", cmapRet, lb);
        
        byte[] in = new byte[]{80, 111, 114}; // should translate to "Por"
        String rslt = cmapRet.lookup(in, 0, 1);
        Assert.assertEquals("P", cmapRet.lookup(in, 0, 1));
        Assert.assertEquals("P", cmapRet.lookup(in, 1, 1));
        Assert.assertEquals("P", cmapRet.lookup(in, 2, 1));
        
    }

}

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

}

/*
 * Created on Feb 9, 2011
 * (c) 2011 Trumpet, Inc.
 *
 */
package com.itextpdf.text.error_messages;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kevin
 */
public class MessageLocalizationTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testBackslashes() throws Exception{
        String testPath = "C:\\test\\file.txt";
        String rslt = MessageLocalization.getComposedMessage("1.not.found.as.file.or.resource", testPath);
        Assert.assertTrue("Result doesn't contain the test path", rslt.contains(testPath));
    }
}
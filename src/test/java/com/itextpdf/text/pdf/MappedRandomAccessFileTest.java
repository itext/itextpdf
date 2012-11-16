/*
 * Created on Feb 10, 2012
 * (c) 2012 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf;

import java.io.File;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.testutils.TestResourceUtils;

/**
 * @author Kevin
 */
public class MappedRandomAccessFileTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testZeroSize() throws Exception {
        File pdf = TestResourceUtils.getResourceAsTempFile(getClass(), "zerosizedfile.pdf");
        MappedRandomAccessFile f = new MappedRandomAccessFile(pdf.getCanonicalPath(), "rw");
        Assert.assertEquals(-1, f.read());
    }

}

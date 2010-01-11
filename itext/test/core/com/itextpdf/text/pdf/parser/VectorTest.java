/*
 * Created on Jan 10, 2010
 * (c) 2010 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kevin
 */
public class VectorTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testCrossVector() {
        Vector v = new Vector(2, 3, 4);
        Matrix m = new Matrix(5, 6, 7, 8, 9, 10);
        Vector shouldBe = new Vector(67, 76, 4);
        
        Vector rslt = v.cross(m);
        Assert.assertEquals(shouldBe, rslt);
    }

}

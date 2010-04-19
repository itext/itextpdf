/*
 * Created on Jan 10, 2010
 * (c) 2010 Trumpet, Inc.
 *
 */
package com.itextpdf.text.pdf.parser;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author kevin
 */
public class MatrixTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMultiply() throws Exception{
        Matrix m1 = new Matrix(2, 3, 4, 5, 6, 7);
        Matrix m2 = new Matrix(8, 9, 10, 11, 12, 13);
        Matrix shouldBe = new Matrix(46, 51, 82, 91, 130, 144);
        
        Matrix rslt = m1.multiply(m2);
        Assert.assertEquals(shouldBe, rslt);
    }
    
    @Test
    public void testDeterminant(){
        Matrix m = new Matrix(2, 3, 4, 5, 6, 7);
        Assert.assertEquals(-2f, m.getDeterminant(), .001f);
    }
}

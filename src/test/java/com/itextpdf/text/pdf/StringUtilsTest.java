/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.text.pdf;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Parameterized unit tests for the method StringUtils::convertCharsToBytes
 *
 * @author benoit
 */
@RunWith(Parameterized.class)
public class StringUtilsTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {'\u0000', (byte) 0x0, (byte) 0x0},
            {'\b', (byte) 0x0, (byte) 0x08},
            {'a', (byte) 0x0, (byte) 0x61},
            {'ة', (byte) 0x06, (byte) 0x29}, // Arabic characters
            {'\ud7ff', (byte) 0xd7, (byte) 0xff}, // just outside of a special Unicode range
            {'\ud800', (byte) 0xd8, (byte) 0x0}, // in a special Unicode range
            {'\uda82', (byte) 0xda, (byte) 0x82}, // in a special Unicode range
            {'\udbb0', (byte) 0xdb, (byte) 0xb0}, // in a special Unicode range
            {'\udfff', (byte) 0xdf, (byte) 0xff}, // in a special Unicode range
            {'\ue000', (byte) 0xe0, (byte) 0x0}, // just outside of a special Unicode range
            {'\ufffd', (byte) 0xff, (byte) 0xfd},
            {'\uffff', (byte) 0xff, (byte) 0xff},});
    }

    private final char input;
    private final byte check1, check2;

    public StringUtilsTest(char in, byte c1, byte c2) {
        input = in;
        check1 = c1;
        check2 = c2;
    }

    @Test
    public void convertCharsToBytesTest() throws UnsupportedEncodingException {
        byte[] check = {check1, check2};
        char[] vals = {input};
        byte[] result = StringUtils.convertCharsToBytes(vals, CJKFont.CJK_ENCODING);

        Assert.assertArrayEquals(check, result);
    }
}

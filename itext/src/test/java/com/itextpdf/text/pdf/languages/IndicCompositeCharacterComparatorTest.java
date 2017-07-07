package com.itextpdf.text.pdf.languages;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class IndicCompositeCharacterComparatorTest {

    @Test
    public void testLengthIsEqualAndStringsAreEqual() {
        String oneString = "\u0938\u0924";
        String twoString = "\u0938\u0924";
        int result = new IndicCompositeCharacterComparator().compare(oneString, twoString);
        assertTrue("expected to be equal", result == 0);
    }

    @Test
    public void testLengthIsEqualAndStringsAreNotEqual() {
        String oneString = "\u0938\u0924";
        String twoString = "\u0924\u0938";
        int result = new IndicCompositeCharacterComparator().compare(oneString, twoString);
        assertTrue("expected not to be equal", result != 0);
    }

    @Test
    public void testFirstStringIsShorter() {
        String oneString = "\u0938\u0924";
        String twoString = "\u0938\u0924\u0938\u0924";
        int result = new IndicCompositeCharacterComparator().compare(oneString, twoString);
        assertTrue("expected to be greater than", result >= 1);
    }

    @Test
    public void testFirstStringIsLonger() {
        String oneString = "\u0938\u0924\u0938\u0924";
        String twoString = "\u0938\u0924";
        int result = new IndicCompositeCharacterComparator().compare(oneString, twoString);
        assertTrue("expected to be less than", result <= -1);
    }

}
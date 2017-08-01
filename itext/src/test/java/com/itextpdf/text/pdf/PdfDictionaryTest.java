package com.itextpdf.text.pdf;

import org.junit.Assert;
import org.junit.Test;

public class PdfDictionaryTest {
    @Test
    public void pdfDictionaryGetReturnsNullIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();

        PdfObject value = dictionary.get(null);

        Assert.assertNull(value);
    }

    @Test
    public void pdfDictionaryContainsReturnsFalseIfKeyIsNull() {
        PdfDictionary dictionary = new PdfDictionary();

        boolean contained = dictionary.contains(null);

        Assert.assertFalse(contained);
    }
}

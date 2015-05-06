package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class PdfTextExtractorUnicodeIdentityTest {

    @Test
    public void test() throws IOException {
        PdfReader reader = TestResourceUtils.getResourceAsPdfReader(this, "user10.pdf");

        Rectangle rectangle = new Rectangle(71, 792 - 84, 225, 792 - 75);
        RenderFilter filter = new RegionTextRenderFilter(rectangle);
        String txt = PdfTextExtractor.getTextFromPage(reader, 1, new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter));
        Assert.assertEquals("Pname Dname Email Address", txt);
    }

}

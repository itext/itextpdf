package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.pdf.PdfReader;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;


public class GlyphTextRenderListenerTest{
    @Test
    public void test1() throws IOException {
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "test.pdf");
        PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);

        float x1, y1, x2, y2;

        x1 = 203; x2 = 224; y1 = 842 - 44; y2 = 842 - 93;
        String extractedText = parser.processContent(1, new GlyphTextRenderListener(new FilteredTextRenderListener(new LocationTextExtractionStrategy(), new RegionTextRenderFilter(new com.itextpdf.text.Rectangle(x1, y1, x2, y2))))).getResultantText();
        Assert.assertEquals("1234\nt5678", extractedText);
    }

    @Test
    public void test2() throws IOException {
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "Sample.pdf");

        PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);
        String extractedText = parser.processContent(1, new GlyphTextRenderListener(new FilteredTextRenderListener(new LocationTextExtractionStrategy(), new RegionTextRenderFilter(new com.itextpdf.text.Rectangle(111,855,136,867))))).getResultantText();

        Assert.assertEquals("Your ", extractedText);
    }

    @Test
    public void testWithMultiFilteredRenderListener() throws IOException {
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "test.pdf");
        PdfReaderContentParser parser = new PdfReaderContentParser(pdfReader);

        float x1, y1, x2, y2;

        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        x1 = 122; x2 = 144; y1 = 841.9f - 151; y2 = 841.9f - 163;
        TextExtractionStrategy region1Listener = listener.attachRenderListener(new LocationTextExtractionStrategy(), new RegionTextRenderFilter(new com.itextpdf.text.Rectangle(x1, y1, x2, y2)));

        x1 = 156; x2 = 169; y1 = 841.9f - 151; y2 = 841.9f - 163;
        TextExtractionStrategy region2Listener = listener.attachRenderListener(new LocationTextExtractionStrategy(), new RegionTextRenderFilter(new com.itextpdf.text.Rectangle(x1, y1, x2, y2)));

        parser.processContent(1, new GlyphRenderListener(listener));
        Assert.assertEquals("Your", region1Listener.getResultantText());
        Assert.assertEquals("dju", region2Listener.getResultantText());
    }
}

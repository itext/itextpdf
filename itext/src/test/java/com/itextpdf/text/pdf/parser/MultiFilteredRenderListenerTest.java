package com.itextpdf.text.pdf.parser;

import com.itextpdf.testutils.TestResourceUtils;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import junit.framework.Assert;
import org.junit.Test;

import java.io.IOException;

public class MultiFilteredRenderListenerTest {

    @Test
    public void test() throws IOException {
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "test.pdf");

        final String expectedText[] = new String[] {
                "PostScript Compatibility",
                "Because the PostScript language does not support the transparent imaging \n" +
                        "model, PDF 1.4 consumer applications must have some means for converting the \n" +
                        "appearance of a document that uses transparency to a purely opaque description \n" +
                        "for printing on PostScript output devices. Similar techniques can also be used to \n" +
                        "convert such documents to a form that can be correctly viewed by PDF 1.3 and \n" +
                        "earlier consumers. ",
                "Otherwise, flatten the colors to some assumed device color space with pre-\n" +
                        "determined calibration. In the generated PostScript output, paint the flattened \n" +
                        "colors in a CIE-based color space having that calibration. "};

        final Rectangle[] regions = new Rectangle[] {new Rectangle(90, 605, 220, 581),
                new Rectangle(80, 578, 450, 486), new Rectangle(103, 196, 460, 143)};

        final RegionTextRenderFilter[] regionFilters = new RegionTextRenderFilter[regions.length];
        for (int i = 0; i < regions.length; i++)
            regionFilters[i] = new RegionTextRenderFilter(regions[i]);


        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy[] extractionStrategies = new LocationTextExtractionStrategy[regions.length];
        for (int i = 0; i < regions.length; i++)
            extractionStrategies[i] = (LocationTextExtractionStrategy)listener.attachRenderListener(new LocationTextExtractionStrategy(), regionFilters[i]);

        new PdfReaderContentParser(pdfReader).processContent(1, listener);

        for (int i = 0; i < regions.length; i++)
        {
            String actualText = extractionStrategies[i].getResultantText() ;
            Assert.assertEquals(expectedText[i], actualText);
        }
    }

    @Test
    public void multipleFiltersForOneRegionTest() throws IOException {
        final PdfReader pdfReader = TestResourceUtils.getResourceAsPdfReader(this, "test.pdf");

        final Rectangle[] regions = new Rectangle[] {new Rectangle(0, 0, 500, 650),
                new Rectangle(0, 0, 400, 400), new Rectangle(200, 200, 500, 600), new Rectangle(100, 100, 450, 400)};

        final RegionTextRenderFilter[] regionFilters = new RegionTextRenderFilter[regions.length];
        for (int i = 0; i < regions.length; i++)
            regionFilters[i] = new RegionTextRenderFilter(regions[i]);

        MultiFilteredRenderListener listener = new MultiFilteredRenderListener();
        LocationTextExtractionStrategy extractionStrategy = (LocationTextExtractionStrategy)listener.attachRenderListener(new LocationTextExtractionStrategy(), regionFilters);
        new PdfReaderContentParser(pdfReader).processContent(1, listener);
        String actualText = extractionStrategy.getResultantText();

        String expectedText = PdfTextExtractor.getTextFromPage(pdfReader, 1, new FilteredTextRenderListener(new LocationTextExtractionStrategy(), regionFilters));

        Assert.assertEquals(expectedText, actualText);
    }
}

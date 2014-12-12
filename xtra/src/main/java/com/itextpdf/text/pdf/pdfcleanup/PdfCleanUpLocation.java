package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;

/**
 * Defines the region in PDF document to be erased.
 */
public class PdfCleanUpLocation {

    private int page;
    private Rectangle region;
    private BaseColor cleanUpColor;

    public PdfCleanUpLocation(int page, Rectangle region) {
        this.page = page;
        this.region = region;
    }

    /**
     *
     * @param page
     * @param region
     * @param cleanUpColor color used to fill cleaned up area.
     */
    public PdfCleanUpLocation(int page, Rectangle region, BaseColor cleanUpColor) {
        this(page, region);
        this.cleanUpColor = cleanUpColor;
    }

    public int getPage() {
        return page;
    }

    public Rectangle getRegion() {
        return region;
    }

    public BaseColor getCleanUpColor() {
        return cleanUpColor;
    }
}

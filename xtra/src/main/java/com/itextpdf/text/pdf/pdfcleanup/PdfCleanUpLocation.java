package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Rectangle;

/**
 * Defines the region to be erased in a PDF document.
 *
 * @since 5.5.4
 */
public class PdfCleanUpLocation {

    private int page;
    private Rectangle region;
    private BaseColor cleanUpColor;

    /**
     * Constructs a {@link com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpLocation} object.
     *
     * @param page   specifies the number of the page which the region belongs to.
     * @param region represents the boundaries of the area to be erased.
     */
    public PdfCleanUpLocation(int page, Rectangle region) {
        this.page = page;
        this.region = region;
    }

    /**
     * Constructs a {@link com.itextpdf.text.pdf.pdfcleanup.PdfCleanUpLocation} object.
     *
     * @param page         specifies the number of the page which the region belongs to.
     * @param region       represents the boundaries of the area to be erased.
     * @param cleanUpColor a color used to fill the area after erasing it. If <CODE>null</CODE>
     *                     the erased area left uncolored.
     */
    public PdfCleanUpLocation(int page, Rectangle region, BaseColor cleanUpColor) {
        this(page, region);
        this.cleanUpColor = cleanUpColor;
    }

    /**
     * @return the number of the page which the region belongs to.
     */
    public int getPage() {
        return page;
    }

    /**
     * @return A {@link com.itextpdf.text.Rectangle} representing the boundaries of the area to be erased.
     */
    public Rectangle getRegion() {
        return region;
    }

    /**
     * Returns a color used to fill the area after erasing it. If <CODE>null</CODE> the erased area left uncolored.
     *
     * @return a color used to fill the area after erasing it.
     */
    public BaseColor getCleanUpColor() {
        return cleanUpColor;
    }
}

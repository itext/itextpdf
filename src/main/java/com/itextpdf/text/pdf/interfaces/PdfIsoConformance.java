package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfDictionary;

public interface PdfIsoConformance {

    /**
     * Checks if any PDF ISO conformance is necessary.
     * @return <code>true</code> if the PDF has to be in conformance with any of the PDF ISO specifications
     */
    boolean isPdfIso();
}

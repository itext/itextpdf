package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.Rectangle;

public class PdfCleanUpCoveredArea {

    private Rectangle rect;
    private boolean matchesObjRect; // true if object's (which is under cleaning) rect is the same as filter rect

    public PdfCleanUpCoveredArea(Rectangle rect, boolean matchesObjRect) {
        this.rect = rect;
        this.matchesObjRect = matchesObjRect;
    }

    public Rectangle getRect() {
        return rect;
    }

    public boolean matchesObjRect() {
        return matchesObjRect;
    }
}

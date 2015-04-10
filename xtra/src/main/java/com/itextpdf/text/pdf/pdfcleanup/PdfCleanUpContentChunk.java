package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.pdf.DocumentFont;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.Vector;

public class PdfCleanUpContentChunk {

    private float size;
    private PdfString string;
    private boolean visible;
    private boolean image = false;
    private float startX = 0;
    private float endX = 0;

    public PdfCleanUpContentChunk(PdfString string, Vector startLocation, Vector endLocation, float size, boolean visible) {
        this.string = string;
        this.size = size;
        this.startX = startLocation.get(0);
        this.endX = endLocation.get(0);
        this.visible = visible;
    }

    public PdfCleanUpContentChunk(boolean visible) {
        image = true;
        this.visible = visible;
    }

    public float getSize() {
        return size;
    }

    public PdfString getString() {
        return string;
    }

    public boolean isVisible() {
        return visible;
    }

    public boolean isImage() {
        return image;
    }

    public float getStartX() {
        return startX;
    }

    public float getEndX() {
        return endX;
    }
}

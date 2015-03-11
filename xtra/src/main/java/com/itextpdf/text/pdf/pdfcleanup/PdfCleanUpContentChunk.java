package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.Vector;

/**
 * Represents an image or text data.
 */
class PdfCleanUpContentChunk {

    private boolean visible;

    private PdfString text;
    private float startX ;
    private float endX;
    private int numOfStrChunkBelongsTo;

    private boolean image;
    private byte[] newImageData;

    public PdfCleanUpContentChunk(PdfString text, Vector startLocation, Vector endLocation, boolean visible, int numOfStrChunkBelongsTo) {
        this.text = text;
        this.startX = startLocation.get(0);
        this.endX = endLocation.get(0);
        this.visible = visible;
        this.numOfStrChunkBelongsTo = numOfStrChunkBelongsTo;
    }

    public PdfCleanUpContentChunk(boolean visible, byte[] newImageData) {
        this.image = true;
        this.visible = visible;
        this.newImageData = newImageData;
    }

    public PdfString getText() {
        return text;
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

    public byte[] getNewImageData() {
        return newImageData;
    }

    public int getNumOfStrChunkBelongsTo() {
        return numOfStrChunkBelongsTo;
    }
}

package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.Vector;

public class PdfCleanUpContentChunk {

    private PdfString string;
    private boolean visible;
    private boolean image = false;
    private float startX = 0;
    private float endX = 0;

    private byte[] newImageData;

    private int numOfStrChunkBelongsTo;

    public PdfCleanUpContentChunk(PdfString string, Vector startLocation, Vector endLocation, boolean visible, int numOfStrChunkBelongsTo) {
        this.string = string;
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

    public byte[] getNewImageData() {
        return newImageData;
    }

    public int getNumOfStrChunkBelongsTo() {
        return numOfStrChunkBelongsTo;
    }
}

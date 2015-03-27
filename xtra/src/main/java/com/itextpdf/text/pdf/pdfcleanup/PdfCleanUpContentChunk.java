package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.Vector;

/**
 * Represents a chunk of a pdf content stream which is under cleanup processing. E.g. image, text.
 */
abstract class PdfCleanUpContentChunk {

    private boolean visible;

    public PdfCleanUpContentChunk(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Represents a text fragment from a pdf content stream.
     */
    public static class Text extends PdfCleanUpContentChunk {

        private PdfString text;
        private float startX;
        private float endX;
        private int numOfStrTextBelongsTo;

        public Text(PdfString text, Vector startLocation, Vector endLocation, boolean visible, int numOfStrTextBelongsTo) {
            super(visible);
            this.text = text;
            this.startX = startLocation.get(0);
            this.endX = endLocation.get(0);
            this.numOfStrTextBelongsTo = numOfStrTextBelongsTo;
        }

        public PdfString getText() {
            return text;
        }

        public float getStartX() {
            return startX;
        }

        public float getEndX() {
            return endX;
        }

        public int getNumOfStrTextBelongsTo() {
            return numOfStrTextBelongsTo;
        }
    }

    /**
     * Represents an image used in a pdf content stream.
     */
    public static class Image extends PdfCleanUpContentChunk {

        private byte[] newImageData;

        public Image(boolean visible, byte[] newImageData) {
            super(visible);
            this.newImageData = newImageData;
        }

        public byte[] getNewImageData() {
            return newImageData;
        }
    }
}

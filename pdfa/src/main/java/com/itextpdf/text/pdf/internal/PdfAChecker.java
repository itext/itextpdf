package com.itextpdf.text.pdf.internal;

import com.itextpdf.text.pdf.PdfWriter;

abstract public class PdfAChecker {
    abstract protected void checkFont(PdfWriter writer, int key, Object obj1);

    abstract protected void checkImage(PdfWriter writer, int key, Object obj1);

    abstract protected void checkGState(PdfWriter writer, int key, Object obj1);

    abstract protected void checkLayer(PdfWriter writer, int key, Object obj1);

    void checkPdfAConformance(PdfWriter writer, int key, Object obj1) {
        if (writer == null || !writer.isPdfIso())
            return;
        switch (key) {
            case PdfIsoKeys.PDFISOKEY_FONT:
                checkFont(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_IMAGE:
                checkImage(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_GSTATE:
                checkGState(writer, key, obj1);
                break;
            case PdfIsoKeys.PDFISOKEY_LAYER:
                checkLayer(writer, key, obj1);
            default:
                break;
        }
    }

}

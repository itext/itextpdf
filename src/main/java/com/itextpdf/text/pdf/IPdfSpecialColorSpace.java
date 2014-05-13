package com.itextpdf.text.pdf;

public interface IPdfSpecialColorSpace {
    public PdfObject getPdfObject(PdfWriter writer);
    public boolean equals(Object obj);
}

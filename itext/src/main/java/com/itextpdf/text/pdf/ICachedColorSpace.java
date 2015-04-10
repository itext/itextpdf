package com.itextpdf.text.pdf;

public interface ICachedColorSpace {
    public PdfObject getPdfObject(PdfWriter writer);
    public boolean equals(Object obj);
    public int hashCode();
}

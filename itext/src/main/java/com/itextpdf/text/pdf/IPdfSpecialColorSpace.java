package com.itextpdf.text.pdf;

public interface IPdfSpecialColorSpace {
    public ColorDetails[] getColorantDetails(PdfWriter writer);
}

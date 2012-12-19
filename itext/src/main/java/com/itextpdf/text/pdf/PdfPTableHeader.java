package com.itextpdf.text.pdf;

public class PdfPTableHeader extends PdfPTableBody {

    protected PdfName role = PdfName.THEAD;

    public PdfPTableHeader() {
        super();
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

}

package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.HashMap;

public class PdfListBody implements IAccessibleElement {

    protected PdfName role = PdfName.LBODY;

    public PdfObject getAccessibleProperty(final PdfName key) {
        return null;
    }

    public void setAccessibleProperty(final PdfName key, final PdfObject value) {

    }

    public HashMap<PdfName, PdfObject> getAccessibleProperties() {
        return null;
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

}

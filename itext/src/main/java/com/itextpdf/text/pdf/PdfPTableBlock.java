package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PdfPTableBlock implements IAccessibleElement {

    protected UUID id = UUID.randomUUID();
    protected ArrayList<PdfPRow> rows = null;

    public PdfPTableBlock() {

    }

    public PdfObject getAccessibleAttribute(final PdfName key) {
        return null;
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {

    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return null;
    }

    public PdfName getRole() {
        return null;
    }

    public void setRole(final PdfName role) {
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

}

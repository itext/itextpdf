package com.itextpdf.text.pdf;

import com.itextpdf.text.ListItem;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.HashMap;
import java.util.UUID;

public class PdfListBody implements IAccessibleElement {

    protected PdfName role = PdfName.LBODY;
    protected UUID id = UUID.randomUUID();
    protected ListItem parentItem = null;
    protected float indentation = 0;

    public PdfListBody() {

    }

    public PdfListBody(final ListItem parentItem) {
        this();
        this.parentItem = parentItem;
    }

    public PdfListBody(final ListItem parentItem, float indentation) {
        this(parentItem);
        this.indentation = indentation;
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
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

}

package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PdfPTableBody implements IAccessibleElement {

    protected UUID id = UUID.randomUUID();
    protected ArrayList<PdfPRow> rows = null;
    protected PdfName role = PdfName.TBODY;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;

    public PdfPTableBody() {

    }

    public PdfObject getAccessibleAttribute(final PdfName key) {
        if (accessibleAttributes != null)
            return accessibleAttributes.get(key);
        else
            return null;
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {
        if (accessibleAttributes == null)
            accessibleAttributes = new HashMap<PdfName, PdfObject>();
        accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return accessibleAttributes;
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

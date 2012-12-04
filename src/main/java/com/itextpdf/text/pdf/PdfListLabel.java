package com.itextpdf.text.pdf;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.HashMap;
import java.util.UUID;

public class PdfListLabel implements IAccessibleElement {

    protected PdfName role = PdfName.LBL;
    protected UUID id = UUID.randomUUID();

    public PdfObject getAccessibleProperty(final PdfName key) {
        return null;
    }

    public void setAccessibleProperty(final PdfName key, final PdfObject value) {

    }

    public HashMap<PdfName, PdfObject> getAccessibleProperties() {
        return null;
    }

    public AccessibleUserProperty getUserProperty(final PdfName key) {
        return null;
    }

    public void setUserProperty(final PdfName key, final AccessibleUserProperty value) {
    }

    public HashMap<PdfName, AccessibleUserProperty> getUserProperties() {
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

}

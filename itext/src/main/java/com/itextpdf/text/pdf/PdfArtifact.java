package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.HashMap;
import java.util.UUID;

public class PdfArtifact implements IAccessibleElement {

    protected PdfName role = PdfName.ARTIFACT;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected UUID id = UUID.randomUUID();

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
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public PdfString getType() {
        return accessibleAttributes == null ? null : (PdfString)accessibleAttributes.get(PdfName.TYPE);
    }

    public void setType(PdfString type) {
        setAccessibleAttribute(PdfName.TYPE, type);
    }

    public PdfArray getBBox() {
        return accessibleAttributes == null ? null : (PdfArray)accessibleAttributes.get(PdfName.BBOX);
    }

    public void setBBox(PdfArray bbox) {
        setAccessibleAttribute(PdfName.BBOX, bbox);
    }

    public PdfArray getAttached() {
        return accessibleAttributes == null ? null : (PdfArray)accessibleAttributes.get(PdfName.ATTACHED);
    }

    public void setAttached(PdfArray attached) {
        setAccessibleAttribute(PdfName.ATTACHED, attached);
    }



}

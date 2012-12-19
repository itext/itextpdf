package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;

import java.util.HashMap;
import java.util.UUID;

/**
 * Describes accessible element.
 */
public interface IAccessibleElement {

    /**
     * Get the attribute of accessible element (everything in <code>A</code> dictionary + <code>Lang</code>, <code>Alt</code>, <code>ActualText</code>, <code>E</code>).
     * @param key
     * @return
     */
    PdfObject getAccessibleAttribute(final PdfName key);

    /**
     * Set the attribute of accessible element (everything in <code>A</code> dictionary + <code>Lang</code>, <code>Alt</code>, <code>ActualText</code>, <code>E</code>).
     * @param key
     * @param value
     */
    void setAccessibleAttribute(final PdfName key, final PdfObject value);

    /**
     * Gets all the properties of accessible element.
     * @return
     */
    HashMap<PdfName, PdfObject> getAccessibleAttributes();

    /**
     * Gets the role of the accessible element.
     * @return
     */
    PdfName getRole();

    /**
     * Sets the role of the accessiblee element.
     * Set role to <code>null</code> if you don't want to tag this element.
     * Note that all child elements won't also be tagged.
     * @param role
     */
    void setRole(final PdfName role);

    UUID getId();

    void setId(final UUID id);

}

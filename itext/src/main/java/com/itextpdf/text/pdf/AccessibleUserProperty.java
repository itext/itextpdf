package com.itextpdf.text.pdf;

/**
 * Describes user property of a pdf structure element as described in 14.7.5.4 of PDF 1.7 reference.
 */
public class AccessibleUserProperty extends PdfDictionary {

    public AccessibleUserProperty(final PdfName name, final PdfObject value) {
        super();
        put(PdfName.N, name);
        put(PdfName.V, value);
    }

    public AccessibleUserProperty(final PdfName name, final PdfObject value, final PdfString format) {
        this(name, value);
        put(PdfName.F, format);
    }

    public AccessibleUserProperty(final PdfName name, final PdfObject value, final PdfBoolean hidden) {
        this(name, value);
        put(PdfName.H, hidden);
    }

    public AccessibleUserProperty(final PdfName name, final PdfObject value, final PdfString format, final PdfBoolean hidden) {
        this(name, value);
        put(PdfName.F, format);
        put(PdfName.H, hidden);
    }

    public PdfName getName() {
        return getAsName(PdfName.N);
    }

    public PdfObject getValue() {
        return get(PdfName.V);
    }

    public PdfString getFormat() {
        return getAsString(PdfName.F);
    }

    public PdfBoolean getHidden() {
        return getAsBoolean(PdfName.H);
    }

}

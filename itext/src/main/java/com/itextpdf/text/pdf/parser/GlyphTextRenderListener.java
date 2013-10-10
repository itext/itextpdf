package com.itextpdf.text.pdf.parser;

public class GlyphTextRenderListener extends GlyphRenderListener implements TextExtractionStrategy {

    private final TextExtractionStrategy delegate;

    public GlyphTextRenderListener(TextExtractionStrategy delegate) {
        super(delegate);
        this.delegate = delegate;
    }

    public String getResultantText() {
        return delegate.getResultantText();
    }
}

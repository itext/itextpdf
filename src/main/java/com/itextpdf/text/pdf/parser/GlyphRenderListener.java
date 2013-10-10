package com.itextpdf.text.pdf.parser;


public class GlyphRenderListener implements RenderListener {

    private final RenderListener delegate;

    public GlyphRenderListener(RenderListener delegate) {
        this.delegate = delegate;
    }

    public void beginTextBlock() {
        delegate.beginTextBlock();
    }

    public void renderText(TextRenderInfo renderInfo) {
        for (TextRenderInfo glyphInfo : renderInfo.getCharacterRenderInfos())
            delegate.renderText(glyphInfo);
    }

    public void endTextBlock() {
        delegate.endTextBlock();
    }

    public void renderImage(ImageRenderInfo renderInfo) {
        delegate.renderImage(renderInfo);
    }
}

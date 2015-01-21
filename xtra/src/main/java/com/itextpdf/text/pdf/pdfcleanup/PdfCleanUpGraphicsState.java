package com.itextpdf.text.pdf.pdfcleanup;

/**
 * Represents subset of graphics state parameters
 */
public class PdfCleanUpGraphicsState {

    private float fontSize;
    private float characterSpacing;

    public PdfCleanUpGraphicsState() {
    }

    public PdfCleanUpGraphicsState(float fontSize, float characterSpacing) {
        this.fontSize = fontSize;
        this.characterSpacing = characterSpacing;
    }

    public PdfCleanUpGraphicsState(PdfCleanUpGraphicsState graphicsState) {
        this.fontSize = graphicsState.fontSize;
        this.characterSpacing = graphicsState.characterSpacing;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public float getCharacterSpacing() {
        return characterSpacing;
    }

    public void setCharacterSpacing(float characterSpacing) {
        this.characterSpacing = characterSpacing;
    }
}

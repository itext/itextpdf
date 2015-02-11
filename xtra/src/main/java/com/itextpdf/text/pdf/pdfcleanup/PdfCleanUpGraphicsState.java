package com.itextpdf.text.pdf.pdfcleanup;

/**
 * Represents subset of graphics state parameters
 */
class PdfCleanUpGraphicsState {

    private float fontSize = 1;
    private float horizontalScaling = 100; // in percents
    private float characterSpacing;
    private float wordSpacing;

    public PdfCleanUpGraphicsState() {
    }

    public PdfCleanUpGraphicsState(float fontSize, float horizontalScaling, float characterSpacing, float wordSpacing) {
        this.fontSize = fontSize;
        this.horizontalScaling = horizontalScaling;
        this.characterSpacing = characterSpacing;
        this.wordSpacing = wordSpacing;
    }

    public PdfCleanUpGraphicsState(PdfCleanUpGraphicsState graphicsState) {
        this.fontSize = graphicsState.fontSize;
        this.horizontalScaling = graphicsState.horizontalScaling;
        this.characterSpacing = graphicsState.characterSpacing;
        this.wordSpacing = graphicsState.wordSpacing;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public float getHorizontalScaling() {
        return horizontalScaling;
    }

    public void setHorizontalScaling(float horizontalScaling) {
        this.horizontalScaling = horizontalScaling;
    }

    public float getCharacterSpacing() {
        return characterSpacing;
    }

    public void setCharacterSpacing(float characterSpacing) {
        this.characterSpacing = characterSpacing;
    }

    public float getWordSpacing() {
        return wordSpacing;
    }

    public void setWordSpacing(float wordSpacing) {
        this.wordSpacing = wordSpacing;
    }
}

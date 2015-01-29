package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;

import java.util.Stack;

public class PdfCleanUpContext {

    private PdfDictionary resources = null;
    private PdfContentByte canvas = null;

    private Stack<PdfCleanUpGraphicsState> graphicsStateStack = new Stack<PdfCleanUpGraphicsState>();

    public PdfCleanUpContext(PdfDictionary resources, PdfContentByte canvas) {
        this.resources = resources;
        this.canvas = canvas;

        graphicsStateStack.push(new PdfCleanUpGraphicsState());
    }

    public PdfDictionary getResources() {
        return resources;
    }

    public void setResources(PdfDictionary resources) {
        this.resources = resources;
    }

    public PdfContentByte getCanvas() {
        return canvas;
    }

    public void setCanvas(PdfContentByte canvas) {
        this.canvas = canvas;
    }

    public float getFontSize() {
        return graphicsStateStack.peek().getFontSize();
    }

    public void setFontSize(float fontSize) {
        graphicsStateStack.peek().setFontSize(fontSize);
    }

    public float getHorizontalScaling() {
        return graphicsStateStack.peek().getHorizontalScaling();
    }

    public void setHorizontalScaling(float horizontalScaling) {
        graphicsStateStack.peek().setHorizontalScaling(horizontalScaling);
    }

    public float getCharacterSpacing() {
        return graphicsStateStack.peek().getCharacterSpacing();
    }

    public void setCharacterSpacing(float characterSpacing) {
        graphicsStateStack.peek().setCharacterSpacing(characterSpacing);
    }

    public float getWordSpacing() {
        return graphicsStateStack.peek().getWordSpacing();
    }

    public void setWordSpacing(float wordSpacing) {
        graphicsStateStack.peek().setWordSpacing(wordSpacing);
    }

    public void saveGraphicsState() {
        graphicsStateStack.push( new PdfCleanUpGraphicsState(graphicsStateStack.peek()) );
    }

    public void restoreGraphicsState() {
        graphicsStateStack.pop();
    }
}
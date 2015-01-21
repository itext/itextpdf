package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDictionary;

import java.util.Stack;

public class PdfCleanUpContext {

    private PdfDictionary resources = null;
    private PdfContentByte canvas = null;

    private Stack<PdfCleanUpGraphicsState> graphicsStateStack = new Stack<PdfCleanUpGraphicsState>();
    private float textMatrixElement22; // represents 'd' component of text matrix vector [a b c d e f]

    public PdfCleanUpContext(PdfDictionary resources, PdfContentByte canvas) {
        this.resources = resources;
        this.canvas = canvas;

        graphicsStateStack.push(new PdfCleanUpGraphicsState());
        resetTextMatrixElement22();
    }

    public PdfDictionary getResources() {
        return resources;
    }

    public PdfContentByte getCanvas() {
        return canvas;
    }

    public float getTextMatrixElement22() {
        return textMatrixElement22;
    }

    public float getFontSize() {
        return graphicsStateStack.peek().getFontSize();
    }

    public float getCharacterSpacing() {
        return graphicsStateStack.peek().getCharacterSpacing();
    }

    public void setResources(PdfDictionary resources) {
        this.resources = resources;
    }

    public void setCanvas(PdfContentByte canvas) {
        this.canvas = canvas;
    }

    public void setTextMatrixElement22(float textMatrixElement22) {
        this.textMatrixElement22 = textMatrixElement22;
    }

    public void setFontSize(float fontSize) {
        graphicsStateStack.peek().setFontSize(fontSize);
    }

    public void setCharacterSpacing(float characterSpacing) {
        graphicsStateStack.peek().setCharacterSpacing(characterSpacing);
    }

    public void saveGraphicsState() {
        graphicsStateStack.push( new PdfCleanUpGraphicsState(graphicsStateStack.peek()) );
    }

    public void restoreGraphicsState() {
        graphicsStateStack.pop();
    }

    public void resetTextMatrixElement22() {
        textMatrixElement22 = 1;
    }
}
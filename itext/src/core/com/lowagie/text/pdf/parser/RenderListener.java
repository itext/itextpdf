/*
 * Created on Oct 6, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.lowagie.text.pdf.parser;


/**
 * Defines the callback method(s) that {@link PdfContentStreamProcessor} will make as
 * various render operations are encountered during processing of a context stream
 * @since 2.1.5
 */
public abstract class RenderListener {
    /**
     * Called when text should be rendered
     * @param text the text to render
     * @param gs the current graphics state
     * @param textMatrix the text matrix at the beginning of the operation
     * @param endingTextMatrix the text matrix that will exist at the end of the operation.  This is useful for determining the width of the string without having to re-compute it.
     */
    abstract public void renderText(String text, GraphicsState gs, Matrix textMatrix, Matrix endingTextMatrix); 
}

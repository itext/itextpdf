/*
 * Created on Oct 28, 2009
 * (c) 2009 Trumpet, Inc.
 *
 */
package com.lowagie.text.pdf.parser;

/**
 * @author kevin
 */
public interface RenderListener {

    /**
     * Called when text should be rendered
     * @param text the text to render
     * @param gs the current graphics state
     * @param textMatrix the text matrix at the beginning of the operation
     * @param endingTextMatrix the text matrix that will exist at the end of the operation.  This is useful for determining the width of the string without having to re-compute it.
     */
    public void renderText(String text, GraphicsState gs, Matrix textMatrix,
            Matrix endingTextMatrix);

    /**
     * Resets the internal state of the RenderListener
     */
    public void reset();
}
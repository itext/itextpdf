/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;
import java.util.ArrayList;
import java.util.Iterator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.error_messages.MessageLocalization;

/** Writes text vertically. Note that the naming is done according
 * to horizontal text although it refers to vertical text.
 * A line with the alignment Element.LEFT_ALIGN will actually
 * be top aligned.
 */
public class VerticalText {

/** Signals that there are no more text available. */
    public static final int NO_MORE_TEXT = 1;

/** Signals that there is no more column. */
    public static final int NO_MORE_COLUMN = 2;

/** The chunks that form the text. */
    protected ArrayList<PdfChunk> chunks = new ArrayList<PdfChunk>();

    /** The <CODE>PdfContent</CODE> where the text will be written to. */
    protected PdfContentByte text;

    /** The column alignment. Default is left alignment. */
    protected int alignment = Element.ALIGN_LEFT;

    /** Marks the chunks to be eliminated when the line is written. */
    protected int currentChunkMarker = -1;

    /** The chunk created by the splitting. */
    protected PdfChunk currentStandbyChunk;

    /** The chunk created by the splitting. */
    protected String splittedChunkText;

    /** The leading
     */
    protected float leading;

    /** The X coordinate.
     */
    protected float startX;

    /** The Y coordinate.
     */
    protected float startY;

    /** The maximum number of vertical lines.
     */
    protected int maxLines;

    /** The height of the text.
     */
    protected float height;

    /** Creates new VerticalText
     * @param text the place where the text will be written to. Can
     * be a template.
     */
    public VerticalText(PdfContentByte text) {
        this.text = text;
    }

    /**
     * Adds a <CODE>Phrase</CODE> to the current text array.
     * @param phrase the text
     */
    public void addText(Phrase phrase) {
        for (Chunk c: phrase.getChunks()) {
            chunks.add(new PdfChunk(c, null));
        }
    }

    /**
     * Adds a <CODE>Chunk</CODE> to the current text array.
     * @param chunk the text
     */
    public void addText(Chunk chunk) {
        chunks.add(new PdfChunk(chunk, null));
    }

    /** Sets the layout.
     * @param startX the top right X line position
     * @param startY the top right Y line position
     * @param height the height of the lines
     * @param maxLines the maximum number of lines
     * @param leading the separation between the lines
     */
    public void setVerticalLayout(float startX, float startY, float height, int maxLines, float leading) {
        this.startX = startX;
        this.startY = startY;
        this.height = height;
        this.maxLines = maxLines;
        setLeading(leading);
    }

    /** Sets the separation between the vertical lines.
     * @param leading the vertical line separation
     */
    public void setLeading(float leading) {
        this.leading = leading;
    }

    /** Gets the separation between the vertical lines.
     * @return the vertical line separation
     */
    public float getLeading() {
        return leading;
    }

    /**
     * Creates a line from the chunk array.
     * @param width the width of the line
     * @return the line or null if no more chunks
     */
    protected PdfLine createLine(float width) {
        if (chunks.isEmpty())
            return null;
        splittedChunkText = null;
        currentStandbyChunk = null;
        PdfLine line = new PdfLine(0, width, alignment, 0);
        String total;
        for (currentChunkMarker = 0; currentChunkMarker < chunks.size(); ++currentChunkMarker) {
            PdfChunk original = chunks.get(currentChunkMarker);
            total = original.toString();
            currentStandbyChunk = line.add(original);
            if (currentStandbyChunk != null) {
                splittedChunkText = original.toString();
                original.setValue(total);
                return line;
            }
        }
        return line;
    }

    /**
     * Normalizes the list of chunks when the line is accepted.
     */
    protected void shortenChunkArray() {
        if (currentChunkMarker < 0)
            return;
        if (currentChunkMarker >= chunks.size()) {
            chunks.clear();
            return;
        }
        PdfChunk split = chunks.get(currentChunkMarker);
        split.setValue(splittedChunkText);
        chunks.set(currentChunkMarker, currentStandbyChunk);
        for (int j = currentChunkMarker - 1; j >= 0; --j)
            chunks.remove(j);
    }

    /**
     * Outputs the lines to the document. It is equivalent to <CODE>go(false)</CODE>.
     * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
     * and/or <CODE>NO_MORE_COLUMN</CODE>
     */
    public int go() {
        return go(false);
    }

    /**
     * Outputs the lines to the document. The output can be simulated.
     * @param simulate <CODE>true</CODE> to simulate the writing to the document
     * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
     * and/or <CODE>NO_MORE_COLUMN</CODE>
     */
    public int go(boolean simulate) {
        boolean dirty = false;
        PdfContentByte graphics = null;
        if (text != null) {
            graphics = text.getDuplicate();
        }
        else if (!simulate)
            throw new NullPointerException(MessageLocalization.getComposedMessage("verticaltext.go.with.simulate.eq.eq.false.and.text.eq.eq.null"));
        int status = 0;
        for (;;) {
            if (maxLines <= 0) {
                status = NO_MORE_COLUMN;
                if (chunks.isEmpty())
                    status |= NO_MORE_TEXT;
                break;
            }
            if (chunks.isEmpty()) {
                status = NO_MORE_TEXT;
                break;
            }
            PdfLine line = createLine(height);
            if (!simulate && !dirty) {
                text.beginText();
                dirty = true;
            }
            shortenChunkArray();
            if (!simulate) {
                text.setTextMatrix(startX, startY - line.indentLeft());
                writeLine(line, text, graphics);
            }
            --maxLines;
            startX -= leading;
        }
        if (dirty) {
            text.endText();
            text.add(graphics);
        }
        return status;
    }

    private Float curCharSpace = 0f;

    void writeLine(PdfLine line, PdfContentByte text, PdfContentByte graphics) {
        PdfFont currentFont = null;
        PdfChunk chunk;
        for (Iterator<PdfChunk> j = line.iterator(); j.hasNext(); ) {
            chunk = j.next();

            if (!chunk.isImage() && chunk.font().compareTo(currentFont) != 0) {
                currentFont = chunk.font();
                text.setFontAndSize(currentFont.getFont(), currentFont.size());
            }
            Object textRender[] = (Object[])chunk.getAttribute(Chunk.TEXTRENDERMODE);
            int tr = 0;
            float strokeWidth = 1;
            BaseColor color = chunk.color();
            BaseColor strokeColor = null;
            if (textRender != null) {
                tr = ((Integer)textRender[0]).intValue() & 3;
                if (tr != PdfContentByte.TEXT_RENDER_MODE_FILL)
                    text.setTextRenderingMode(tr);
                if (tr == PdfContentByte.TEXT_RENDER_MODE_STROKE || tr == PdfContentByte.TEXT_RENDER_MODE_FILL_STROKE) {
                    strokeWidth = ((Float)textRender[1]).floatValue();
                    if (strokeWidth != 1)
                        text.setLineWidth(strokeWidth);
                    strokeColor = (BaseColor)textRender[2];
                    if (strokeColor == null)
                        strokeColor = color;
                    if (strokeColor != null)
                        text.setColorStroke(strokeColor);
                }
            }

            Float charSpace = (Float)chunk.getAttribute(Chunk.CHAR_SPACING);
            // no char space setting means "leave it as is".
            if (charSpace != null && !curCharSpace.equals(charSpace)) {
            	curCharSpace = charSpace.floatValue();
            	text.setCharacterSpacing(curCharSpace);
            }
            if (color != null)
                text.setColorFill(color);
            
            text.showText(chunk.toString());
            
            if (color != null)
                text.resetRGBColorFill();
            if (tr != PdfContentByte.TEXT_RENDER_MODE_FILL)
                text.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
            if (strokeColor != null)
                text.resetRGBColorStroke();
            if (strokeWidth != 1)
                text.setLineWidth(1);
        }
    }

    /** Sets the new text origin.
     * @param startX the X coordinate
     * @param startY the Y coordinate
     */
    public void setOrigin(float startX, float startY) {
        this.startX = startX;
        this.startY = startY;
    }

    /** Gets the X coordinate where the next line will be written. This value will change
     * after each call to <code>go()</code>.
     * @return  the X coordinate
     */
    public float getOriginX() {
        return startX;
    }

    /** Gets the Y coordinate where the next line will be written.
     * @return  the Y coordinate
     */
    public float getOriginY() {
        return startY;
    }

    /** Gets the maximum number of available lines. This value will change
     * after each call to <code>go()</code>.
     * @return Value of property maxLines.
     */
    public int getMaxLines() {
        return maxLines;
    }

    /** Sets the maximum number of lines.
     * @param maxLines the maximum number of lines
     */
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    /** Gets the height of the line
     * @return the height
     */
    public float getHeight() {
        return height;
    }

    /** Sets the height of the line
     * @param height the new height
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Sets the alignment.
     * @param alignment the alignment
     */
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    /**
     * Gets the alignment.
     * @return the alignment
     */
    public int getAlignment() {
        return alignment;
    }
}

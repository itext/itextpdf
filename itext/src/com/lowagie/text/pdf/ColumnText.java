/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Paulo Soares.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.pdf;
import java.util.ArrayList;
import java.util.Iterator;
import com.lowagie.text.Phrase;
import com.lowagie.text.Chunk;
import com.lowagie.text.Element;
import com.lowagie.text.DocumentException;

/**
 * Formats text in a columnwise form. The text is bound
 * on the left and on the right by a sequence of lines. This allows the column
 * to have any shape, not only rectangular.
 * <P>
 * Several parameters can be set like the first paragraph line indent and
 * extra space between paragraphs.
 * <P>
 * A call to the method <CODE>go</CODE> will return one of the following
 * situations: the column ended or the text ended.
 * <P>
 * I the column ended, a new column definition can be loaded with the method
 * <CODE>setColumns</CODE> and the method <CODE>go</CODE> can be called again.
 * <P>
 * If the text ended, more text can be loaded with <CODE>addText</CODE>
 * and the method <CODE>go</CODE> can be called again.<BR>
 * The only limitation is that one or more complete paragraphs must be loaded
 * each time.
 * @author Paulo Soares (psoares@consiste.pt)
 */

public class ColumnText {
    
    public static final float GLOBAL_SPACE_CHAR_RATIO = 0;
    
    /** Signals that there is no more text available. */
    public static final int NO_MORE_TEXT = 1;
    
    /** Signals that there is no more column. */
    public static final int NO_MORE_COLUMN = 2;
    
    /** The column is valid. */
    protected static final int LINE_STATUS_OK = 0;
    
    /** The line is out the column limits. */
    protected static final int LINE_STATUS_OFFLIMITS = 1;
    
    /** The line cannot fit this column position. */
    protected static final int LINE_STATUS_NOLINE = 2;
    
    /** Upper bound of the column. */
    protected float maxY;
    
    /** Lower bound of the column. */
    protected float minY;
    
    /** The column alignment. Default is left alignment. */
    protected int alignment = Element.ALIGN_LEFT;
    
    /** The left column bound. */
    protected ArrayList leftWall;
    
    /** The right column bound. */
    protected ArrayList rightWall;
    
    /** The chunks that form the text. */
    protected ArrayList chunks = new ArrayList();
    
    /** The current y line location. Text will be written at this line minus the leading. */
    protected float yLine;
    
    /** The leading for the current line. */
    protected float currentLeading = 16;
    
    /** The fixed text leading. */
    protected float fixedLeading = 16;
    
    /** The text leading that is multiplied by the biggest font size in the line. */
    protected float multipliedLeading = 0;
    
    /** The <CODE>PdfContent</CODE> where the text will be written to. */
    protected PdfContentByte text;
    
    /** The line status when trying to fit a line to a column. */
    protected int lineStatus;
    
    /** The first paragraph line indent. */
    protected float indent = 0;
    
    /** The following paragraph lines indent. */
    protected float followingIndent = 0;
    
    /** The right paragraph lines indent. */
    protected float rightIndent = 0;
    
    /** The extra space between paragraphs. */
    protected float extraParagraphSpace = 0;
    
    /** Marks the chunks to be eliminated when the line is written. */
    protected int currentChunkMarker = -1;
    
    /** The chunk created by the splitting. */
    protected PdfChunk currentStandbyChunk;
    
    /** The chunk created by the splitting. */
    protected String splittedChunkText;
    
    /** The width of the line when the column is defined as a simple rectangle. */
    protected float rectangularWidth = -1;
    
    /** Holds value of property spaceCharRatio. */
    private float spaceCharRatio = GLOBAL_SPACE_CHAR_RATIO;
    
    /**
     * Creates a <CODE>ColumnText</CODE>.
     * @param text the place where the text will be written to. Can
     * be a template.
     */
    public ColumnText(PdfContentByte text) {
        this.text = text;
    }
    
    /**
     * Adds a <CODE>Phrase</CODE> to the current text array.
     * @param phrase the text
     */
    public void addText(Phrase phrase) {
        for (Iterator j = phrase.getChunks().iterator(); j.hasNext();) {
            chunks.add(new PdfChunk((Chunk)j.next(), null));
        }
    }
    
    /**
     * Adds a <CODE>Chunk</CODE> to the current text array.
     * @param chunk the text
     */
    public void addText(Chunk chunk) {
        chunks.add(new PdfChunk(chunk, null));
    }
    
    /**
     * Converts a sequence of lines representing one of the column bounds into
     * an internal format.
     * <p>
     * Each array element will contain a <CODE>float[4]</CODE> representing
     * the line x = ax + b.
     * @param cLine the column array
     * @return the converted array
     */
    protected ArrayList convertColumn(float cLine[]) {
        if (cLine.length < 4)
            throw new RuntimeException("No valid column line found.");
        ArrayList cc = new ArrayList();
        for (int k = 0; k < cLine.length - 2; k += 2) {
            float x1 = cLine[k];
            float y1 = cLine[k + 1];
            float x2 = cLine[k + 2];
            float y2 = cLine[k + 3];
            if (y1 == y2)
                continue;
            // x = ay + b
            float a = (x1 - x2) / (y1 - y2);
            float b = x1 - a * y1;
            float r[] = new float[4];
            r[0] = Math.min(y1, y2);
            r[1] = Math.max(y1, y2);
            r[2] = a;
            r[3] = b;
            cc.add(r);
            maxY = Math.max(maxY, r[1]);
            minY = Math.min(minY, r[0]);
        }
        if (cc.size() == 0)
            throw new RuntimeException("No valid column line found.");
        return cc;
    }
    
    /**
     * Finds the intersection between the <CODE>yLine</CODE> and the column. It will
     * set the <CODE>lineStatus</CODE> apropriatly.
     * @param wall the column to intersect
     * @return the x coordinate of the intersection
     */
    protected float findLimitsPoint(ArrayList wall) {
        lineStatus = LINE_STATUS_OK;
        if (yLine < minY || yLine > maxY) {
            lineStatus = LINE_STATUS_OFFLIMITS;
            return 0;
        }
        for (int k = 0; k < wall.size(); ++k) {
            float r[] = (float[])wall.get(k);
            if (yLine < r[0] || yLine > r[1])
                continue;
            return r[2] * yLine + r[3];
        }
        lineStatus = LINE_STATUS_NOLINE;
        return 0;
    }
    
    /**
     * Finds the intersection between the <CODE>yLine</CODE> and the two
     * column bounds. It will set the <CODE>lineStatus</CODE> apropriatly.
     * @return a <CODE>float[2]</CODE>with the x coordinates of the intersection
     */
    protected float[] findLimitsOneLine() {
        for (;;) {
            float x1 = findLimitsPoint(leftWall);
            if (lineStatus == LINE_STATUS_OFFLIMITS || lineStatus == LINE_STATUS_NOLINE)
                return null;
            float x2 = findLimitsPoint(rightWall);
            if (lineStatus == LINE_STATUS_NOLINE)
                return null;
            return new float[]{x1, x2};
        }
    }
    
    /**
     * Finds the intersection between the <CODE>yLine</CODE>,
     * the <CODE>yLine-leading</CODE>and the two
     * column bounds. It will set the <CODE>lineStatus</CODE> apropriatly.
     * @return a <CODE>float[4]</CODE>with the x coordinates of the intersection
     */
    protected float[] findLimitsTwoLines() {
        for (;;) {
            float x1[] = findLimitsOneLine();
            if (lineStatus == LINE_STATUS_OFFLIMITS)
                return null;
            yLine -= currentLeading;
            if (lineStatus == LINE_STATUS_NOLINE) {
                continue;
            }
            float x2[] = findLimitsOneLine();
            if (lineStatus == LINE_STATUS_OFFLIMITS)
                return null;
            if (lineStatus == LINE_STATUS_NOLINE) {
                yLine -= currentLeading;
                continue;
            }
            if (x1[0] >= x2[1] || x2[0] >= x1[1])
                continue;
            return new float[]{x1[0], x1[1], x2[0], x2[1]};
        }
    }
    
    /**
     * Sets the columns bounds. Each column bound is described by a
     * <CODE>float[]</CODE> with the line points [x1,y1,x2,y2,...].
     * The array must have at least 4 elements.
     * @param leftLine the left column bound
     * @param rightLine the right column bound
     */
    public void setColumns(float leftLine[], float rightLine[]) {
        rightWall = convertColumn(rightLine);
        leftWall = convertColumn(leftLine);
        rectangularWidth = -1;
    }
    
    /**
     * Simplified method for rectangular columns.
     * @param phrase a <CODE>Phrase</CODE>
     * @param llx the lower left x corner
     * @param lly the lower left y corner
     * @param urx the upper right x corner
     * @param ury the upper right y corner
     * @param leading the leading
     * @param alignment the column alignment
     */
    public void setSimpleColumn(Phrase phrase, float llx, float lly, float urx, float ury, float leading, int alignment) {
        addText(phrase);
        setSimpleColumn(llx, lly, urx, ury, leading, alignment);
    }
    
    /**
     * Simplified method for rectangular columns.
     * @param llx the lower left x corner
     * @param lly the lower left y corner
     * @param urx the upper right x corner
     * @param ury the upper right y corner
     * @param leading the leading
     * @param alignment the column alignment
     */
    public void setSimpleColumn(float llx, float lly, float urx, float ury, float leading, int alignment) {
        float leftLine[] = new float[4];
        float rightLine[] = new float[4];
        leftLine[0] = Math.min(llx, urx);
        leftLine[1] = Math.max(lly, ury);
        leftLine[2] = leftLine[0];
        leftLine[3] = Math.min(lly, ury);
        rightLine[0] = Math.max(llx, urx);
        rightLine[1] = leftLine[1];
        rightLine[2] = rightLine[0];
        rightLine[3] = leftLine[3];
        setColumns(leftLine, rightLine);
        setLeading(leading);
        this.alignment = alignment;
        yLine = leftLine[1];
        rectangularWidth = Math.abs(llx - urx);
    }
    
    /**
     * Sets the leading to fixed
     * @param leading the leading
     */
    public void setLeading(float leading) {
        fixedLeading = leading;
        multipliedLeading = 0;
    }
    
    /**
     * Sets the leading fixed and variable. The resultant leading will be
     * fixedLeading+multipliedLeading*maxFontSize where maxFontSize is the
     * size of the bigest font in the line.
     * @param fixedLeading the fixed leading
     * @param multipliedLeading the variable leading
     */
    public void setLeading(float fixedLeading, float multipliedLeading) {
        this.fixedLeading = fixedLeading;
        this.multipliedLeading = multipliedLeading;
    }
    
    /**
     * Gets the fixed leading
     * @return the leading
     */
    public float getLeading() {
        return fixedLeading;
    }
    
    /**
     * Gets the variable leading
     * @return the leading
     */
    public float getMultipliedLeading() {
        return multipliedLeading;
    }
    
    /**
     * Sets the yLine. The line will be written to yLine-leading.
     * @param yLine the yLine
     */
    public void setYLine(float yLine) {
        this.yLine = yLine;
    }
    
    /**
     * Gets the yLine.
     * @return the yLine
     */
    public float getYLine() {
        return yLine;
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
    
    /**
     * Sets the first paragraph line indent.
     * @param indent the indent
     */
    public void setIndent(float indent) {
        this.indent = indent;
    }
    
    /**
     * Gets the first paragraph line indent.
     * @return the indent
     */
    public float getIndent() {
        return indent;
    }
    
    /**
     * Sets the following paragraph lines indent.
     * @param indent the indent
     */
    public void setFollowingIndent(float indent) {
        this.followingIndent = indent;
    }
    
    /**
     * Gets the following paragraph lines indent.
     * @return the indent
     */
    public float getFollowingIndent() {
        return followingIndent;
    }
    
    /**
     * Sets the right paragraph lines indent.
     * @param indent the indent
     */
    public void setRightIndent(float indent) {
        this.rightIndent = indent;
    }
    
    /**
     * Gets the right paragraph lines indent.
     * @return the indent
     */
    public float getRightIndent() {
        return rightIndent;
    }
    
    /**
     * Creates a line from the chunk array.
     * @param width the width of the line
     * @return the line or null if no more chunks
     */
    protected PdfLine createLine(float width) {
        if (chunks.size() == 0)
            return null;
        splittedChunkText = null;
        currentStandbyChunk = null;
        PdfLine line = new PdfLine(0, width, alignment, 0);
        String total;
        for (currentChunkMarker = 0; currentChunkMarker < chunks.size(); ++currentChunkMarker) {
            PdfChunk original = (PdfChunk)(chunks.get(currentChunkMarker));
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
        PdfChunk split = (PdfChunk)(chunks.get(currentChunkMarker));
        split.setValue(splittedChunkText);
        chunks.set(currentChunkMarker, currentStandbyChunk);
        for (int j = currentChunkMarker - 1; j >= 0; --j)
            chunks.remove(j);
    }
    
    /**
     * Outputs the lines to the document. It is equivalent to <CODE>go(false)</CODE>.
     * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
     * and/or <CODE>NO_MORE_COLUMN</CODE>
     * @throws DocumentException on error
     */
    public int go() throws DocumentException {
        return go(false);
    }
    
    /**
     * Outputs the lines to the document. The output can be simulated.
     * @param simulate <CODE>true</CODE> to simulate the writting to the document
     * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
     * and/or <CODE>NO_MORE_COLUMN</CODE>
     * @throws DocumentException on error
     */
    public int go(boolean simulate) throws DocumentException {
        boolean dirty = false;
        float ratio = spaceCharRatio;
        Object currentValues[] = new Object[2];
        PdfFont currentFont = null;
        Float lastBaseFactor = new Float(0);
        currentValues[1] = lastBaseFactor;
        PdfDocument pdf = null;
        PdfContentByte graphics = null;
        if (text != null) {
            pdf = text.getPdfDocument();
            graphics = text.getDuplicate();
        }
        else if (!simulate)
            throw new NullPointerException("ColumnText.go with simulate==false and text==null.");
        if (!simulate) {
            if (ratio == GLOBAL_SPACE_CHAR_RATIO)
                ratio = text.getPdfWriter().getSpaceCharRatio();
            else if (ratio < 0.001f)
                ratio = 0.001f;
        }
        float firstIndent = indent;
        
        int status = 0;
        if (rectangularWidth > 0) {
            for (;;) {
                if (rectangularWidth <= firstIndent + rightIndent) {
                    status = NO_MORE_COLUMN;
                    if (chunks.size() == 0)
                        status |= NO_MORE_TEXT;
                    break;
                }
                if (chunks.size() == 0) {
                    status = NO_MORE_TEXT;
                    break;
                }
                float yTemp = yLine;
                PdfLine line = createLine(rectangularWidth - firstIndent - rightIndent);
                float maxSize = line.getMaxSizeSimple();
                currentLeading = fixedLeading + maxSize * multipliedLeading;
                float xx[] = findLimitsTwoLines();
                if (xx == null) {
                    status = NO_MORE_COLUMN;
                    yLine = yTemp;
                    break;
                }
                float x1 = Math.max(xx[0], xx[2]);
                if (!simulate && !dirty) {
                    text.beginText();
                    dirty = true;
                }
                shortenChunkArray();
                if (!simulate) {
                    currentValues[0] = currentFont;
                    text.setTextMatrix(x1 + firstIndent + line.indentLeft(), yLine);
                    pdf.writeLineToContent(line, text, graphics, currentValues, ratio);
                    currentFont = (PdfFont)currentValues[0];
                }
                firstIndent = line.isNewlineSplit() ? indent : followingIndent;
                yLine -= line.isNewlineSplit() ? extraParagraphSpace : 0;
            }
        }
        else {
            currentLeading = fixedLeading;
            for (;;) {
                float yTemp = yLine;
                float xx[] = findLimitsTwoLines();
                if (xx == null) {
                    status = NO_MORE_COLUMN;
                    if (chunks.size() == 0)
                        status |= NO_MORE_TEXT;
                    yLine = yTemp;
                    break;
                }
                if (chunks.size() == 0) {
                    status = NO_MORE_TEXT;
                    yLine = yTemp;
                    break;
                }
                float x1 = Math.max(xx[0], xx[2]);
                float x2 = Math.min(xx[1], xx[3]);
                if (x2 - x1 <= firstIndent + rightIndent)
                    continue;
                if (!simulate && !dirty) {
                    text.beginText();
                    dirty = true;
                }
                PdfLine line = createLine(x2 - x1 - firstIndent - rightIndent);
                shortenChunkArray();
                if (!simulate) {
                    currentValues[0] = currentFont;
                    text.setTextMatrix(x1 + firstIndent + line.indentLeft(), yLine);
                    pdf.writeLineToContent(line, text, graphics, currentValues, ratio);
                    currentFont = (PdfFont)currentValues[0];
                }
                firstIndent = line.isNewlineSplit() ? indent : followingIndent;
                yLine -= line.isNewlineSplit() ? extraParagraphSpace : 0;
            }
        }
        if (dirty) {
            text.endText();
            text.add(graphics);
        }
        return status;
    }
    
    /**
     * Sets the extra space between paragraphs.
     * @return the extra space between paragraphs
     */
    public float getExtraParagraphSpace() {
        return extraParagraphSpace;
    }
    
    /**
     * Sets the extra space between paragraphs.
     * @param extraParagraphSpace the extra space between paragraphs
     */
    public void setExtraParagraphSpace(float extraParagraphSpace) {
        this.extraParagraphSpace = extraParagraphSpace;
    }
    
    /**
     * Clears the chunk array. A call to <CODE>go()</CODE> will always return
     * NO_MORE_TEXT.
     */
    public void clearChunks() {
        chunks.clear();
    }
    
    /** Gets the space/character extra spacing ratio for
     * fully justified text.
     * @return the space/character extra spacing ratio
     */    
    public float getSpaceCharRatio() {
        return spaceCharRatio;
    }
    
    /** Sets the ratio between the extra word spacing and the extra character spacing
     * when the text is fully justified.
     * Extra word spacing will grow <CODE>spaceCharRatio</CODE> times more than extra character spacing.
     * If the ratio is <CODE>PdfWriter.NO_SPACE_CHAR_RATIO</CODE> then the extra character spacing
     * will be zero.
     * @param spaceCharRatio the ratio between the extra word spacing and the extra character spacing
     */
    public void setSpaceCharRatio(float spaceCharRatio) {
        this.spaceCharRatio = spaceCharRatio;
    }
    
}
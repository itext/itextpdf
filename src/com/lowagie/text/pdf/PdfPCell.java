/*
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Paulo Soares.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.pdf;

import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Image;
import com.lowagie.text.Chunk;

/**
 * a cell in a PdfPTable
 */

public class PdfPCell extends Rectangle{
    
/** Holds value of property horizontalAlignment. */
    private int horizontalAlignment = Element.ALIGN_LEFT;
    
/** Holds value of property verticalAlignment. */
    private int verticalAlignment = Element.ALIGN_TOP;
    
/** Holds value of property paddingLeft. */
    private float paddingLeft = 2;
    
/** Holds value of property paddingLeft. */
    private float paddingRight = 2;
    
/** Holds value of property paddingTop. */
    private float paddingTop = 2;
    
/** Holds value of property paddingBottom. */
    private float paddingBottom = 2;
    
/** The fixed text leading. */
    protected float fixedLeading = 0;
    
/** The text leading that is multiplied by the biggest font size in the line. */
    protected float multipliedLeading = 1;
    
/** The extra space between paragraphs. */
    protected float extraParagraphSpace = 0;
    
/** The first paragraph line indent. */
    protected float indent = 0;
    
    protected Phrase phrase;
    
/** Holds value of property fixedHeight. */
    private float fixedHeight = 0;
    
/** Holds value of property noWrap. */
    private boolean noWrap = false;
    
/** Holds value of property table. */
    private PdfPTable table;
    
    public PdfPCell(Phrase phrase)
    {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        this.phrase = phrase;
    }
    
    public PdfPCell(Image image)
    {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        phrase = new Phrase(new Chunk(image, 0, 0));
        fixedLeading = 0;
        multipliedLeading = 1;
        setPadding(0);
    }
    
    PdfPCell(PdfPTable table)
    {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        fixedLeading = 0;
        multipliedLeading = 1;
        this.table = table;
    }
    
    public PdfPCell(PdfPCell cell)
    {
        super(cell.llx, cell.lly, cell.urx, cell.ury);
        border = cell.border;
        borderWidth = cell.borderWidth;
        color = cell.color;
        background = cell.background;
        grayFill = cell.grayFill;
        horizontalAlignment = cell.horizontalAlignment;
        verticalAlignment = cell.verticalAlignment;
        paddingLeft = cell.paddingLeft;
        paddingRight = cell.paddingRight;
        paddingTop = cell.paddingTop;
        paddingBottom = cell.paddingBottom;
        fixedLeading = cell.fixedLeading;
        multipliedLeading = cell.multipliedLeading;
        extraParagraphSpace = cell.extraParagraphSpace;
        indent = cell.indent;
        phrase = cell.phrase;
        fixedHeight = cell.fixedHeight;
        noWrap = cell.noWrap;
        if (cell.table != null)
            table = new PdfPTable(cell.table);
    }
    
    public Phrase getPhrase()
    {
        return phrase;
    }
    
    public void setPhrase(Phrase phrase)
    {
        this.phrase = phrase;
    }
    
/**
 * Getter for property horizontalAlignment.
 * @return Value of property horizontalAlignment.
 */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }
    
/**
 * Setter for property horizontalAlignment.
 * @param horizontalAlignment New value of property horizontalAlignment.
 */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }
    
/**
 * Getter for property verticalAlignment.
 * @return Value of property verticalAlignment.
 */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }
    
/**
 * Setter for property verticalAlignment.
 * @param verticalAlignment New value of property verticalAlignment.
 */
    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }
    
/**
 * Getter for property paddingLeft.
 * @return Value of property paddingLeft.
 */
    public float getPaddingLeft() {
        return paddingLeft;
    }
    
/**
 * Setter for property paddingLeft.
 * @param paddingLeft New value of property paddingLeft.
 */
    public void setPaddingLeft(float paddingLeft) {
        this.paddingLeft = paddingLeft;
    }
    
/**
 * Getter for property paddingRight.
 * @return Value of property paddingRight.
 */
    public float getPaddingRight() {
        return paddingRight;
    }
    
/**
 * Setter for property paddingRight.
 * @param paddingRight New value of property paddingRight.
 */
    public void setPaddingRight(float paddingRight) {
        this.paddingRight = paddingRight;
    }
    
/**
 * Getter for property paddingTop.
 * @return Value of property paddingTop.
 */
    public float getPaddingTop() {
        return paddingTop;
    }
    
/**
 * Setter for property paddingTop.
 * @param paddingTop New value of property paddingTop.
 */
    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }
    
/**
 * Getter for property paddingBottom.
 * @return Value of property paddingBottom.
 */
    public float getPaddingBottom() {
        return paddingBottom;
    }
    
/**
 * Setter for property paddingBottom.
 * @param paddingBottom New value of property paddingBottom.
 */
    public void setPaddingBottom(float paddingBottom) {
        this.paddingBottom = paddingBottom;
    }
    
    public void setPadding(float padding) {
        paddingBottom = padding;
        paddingTop = padding;
        paddingLeft = padding;
        paddingRight = padding;
    }
    
/**
 * Sets the leading fixed and variable. The resultant leading will be
 * fixedLeading+multipliedLeading*maxFontSize where maxFontSize is the
 * size of the bigest font in the line.
 * @param fixedLeading the fixed leading
 * @param multipliedLeading the variable leading
 */
    public void setLeading(float fixedLeading, float multipliedLeading)
    {
        this.fixedLeading = fixedLeading;
        this.multipliedLeading = multipliedLeading;
    }
    
/**
 * Gets the fixed leading
 * @return the leading
 */
    public float getLeading()
    {
        return fixedLeading;
    }
    
/**
 * Gets the variable leading
 * @return the leading
 */
    public float getMultipliedLeading()
    {
        return multipliedLeading;
    }
    
/**
 * Sets the first paragraph line indent.
 * @param indent the indent
 */
    public void setIndent(float indent)
    {
        this.indent = indent;
    }
    
/**
 * Gets the first paragraph line indent.
 * @return the indent
 */
    public float getIndent()
    {
        return indent;
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
 * Getter for property fixedHeight.
 * @return Value of property fixedHeight.
 */
    public float getFixedHeight() {
        return fixedHeight;
    }
    
/**
 * Setter for property fixedHeight.
 * @param fixedHeight New value of property fixedHeight.
 */
    public void setFixedHeight(float fixedHeight) {
        this.fixedHeight = fixedHeight;
    }
    
/**
 * Getter for property noWrap.
 * @return Value of property noWrap.
 */
    public boolean isNoWrap() {
        return noWrap;
    }
    
/**
 * Setter for property noWrap.
 * @param noWrap New value of property noWrap.
 */
    public void setNoWrap(boolean noWrap) {
        this.noWrap = noWrap;
    }
    
/**
 * Getter for property table.
 * @return Value of property table.
 */
    PdfPTable getTable() {
        return table;
    }
    
    void setTable(PdfPTable table) {
        this.table = table;
    }
    
}

/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 Paulo Soares
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

import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Image;
import com.lowagie.text.Chunk;

/** A cell in a PdfPTable.
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
    
    /** The text leading that is multiplied by the biggest font size in the line.
     */
    protected float multipliedLeading = 1;
    
/** The extra space between paragraphs. */
    protected float extraParagraphSpace = 0;
    
/** The first paragraph line indent. */
    protected float indent = 0;
    
    /** The following paragraph lines indent. */
    protected float followingIndent = 0;
    
    /** The right paragraph lines indent. */
    protected float rightIndent = 0;

    /** The text in the cell.
     */    
    protected Phrase phrase;
    
/** Holds value of property fixedHeight. */
    private float fixedHeight = 0;
    
/** Holds value of property noWrap. */
    private boolean noWrap = false;
    
/** Holds value of property table. */
    private PdfPTable table;
    
    /** Holds value of property minimumHeight. */
    private float minimumHeight;
    
    /** Holds value of property colspan. */
    private int colspan = 1;
    private float spaceCharRatio = ColumnText.GLOBAL_SPACE_CHAR_RATIO;
    protected int runDirection = PdfWriter.RUN_DIRECTION_DEFAULT;
    
    /** Holds value of property image. */
    private Image image;
    
    /** Holds value of property cellEvent. */
    private PdfPCellEvent cellEvent;
    private int arabicOptions = 0;
    
    /** Holds value of property useDescender. */
    private boolean useDescender;
    
    /** Constructs a <CODE>PdfPCell</CODE> with a <CODE>Phrase</CODE>.
     * The default padding is 2.
     * @param phrase the text
     */    
    public PdfPCell(Phrase phrase)
    {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        this.phrase = phrase;
    }
    
    /** Constructs a <CODE>PdfPCell</CODE> with an <CODE>Image</CODE>.
     * The default padding is 0.
     * @param image the <CODE>Image</CODE>
     */    
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
    
    /** Constructs a <CODE>PdfPCell</CODE> with an <CODE>Image</CODE>.
     * The default padding is 0.25 for a border width of 0.5.
     * @param image the <CODE>Image</CODE>
     * @param fit <CODE>true</CODE> to fit the image to the cell
     */    
    public PdfPCell(Image image, boolean fit)
    {
        super(0, 0, 0, 0);
        if (fit) {
            borderWidth = 0.5f;
            border = BOX;
            this.image = image;
            fixedLeading = 0;
            multipliedLeading = 1;
            setPadding(borderWidth / 2);
        }
        else {
            borderWidth = 0.5f;
            border = BOX;
            phrase = new Phrase(new Chunk(image, 0, 0));
            fixedLeading = 0;
            multipliedLeading = 1;
            setPadding(0);
        }
    }
    
    /** Constructs a <CODE>PdfPCell</CODE> with a <CODE>PdfPtable</CODE>.
     * This constructor allows nested tables.
     * The default padding is 0.
     * @param table The <CODE>PdfPTable</CODE>
     */    
    public PdfPCell(PdfPTable table)
    {
        super(0, 0, 0, 0);
        borderWidth = 0.5f;
        border = BOX;
        fixedLeading = 0;
        multipliedLeading = 1;
        setPadding(0);
        this.table = table;
    }
    
    /** Constructs a deep copy of a <CODE>PdfPCell</CODE>.
     * @param cell the <CODE>PdfPCell</CODE> to duplicate
     */    
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
        followingIndent = cell.followingIndent;
        rightIndent = cell.rightIndent;
        phrase = cell.phrase;
        fixedHeight = cell.fixedHeight;
        minimumHeight = cell.minimumHeight;
        noWrap = cell.noWrap;
        colspan = cell.colspan;
        spaceCharRatio = cell.spaceCharRatio;
        runDirection = cell.runDirection;
        if (cell.table != null)
            table = new PdfPTable(cell.table);
        image = Image.getInstance(cell.image);
        cellEvent = cell.cellEvent;
        arabicOptions = cell.arabicOptions;
        useDescender = cell.useDescender;
    }
    
    /** Gets the <CODE>Phrase</CODE> from this cell.
     * @return the <CODE>Phrase</CODE>
     */    
    public Phrase getPhrase()
    {
        return phrase;
    }
    
    /** Sets the <CODE>Phrase</CODE> for this cell.
     * @param phrase the <CODE>Phrase</CODE>
     */    
    public void setPhrase(Phrase phrase)
    {
        this.phrase = phrase;
    }
    
    /** Gets the horizontal alignment for the cell.
     * @return the horizontal alignment for the cell
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }
    
    /** Sets the horizontal alignment for the cell. It could be
     * <CODE>Element.ALIGN_CENTER</CODE> for example.
     * @param horizontalAlignment The horizontal alignment
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }
    
    /** Gets the vertical alignment for the cell.
     * @return the vertical alignment for the cell
     */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }
    
    /** Sets the vertical alignment for the cell. It could be
     * <CODE>Element.ALIGN_MIDDLE</CODE> for example.
     * @param verticalAlignment The vertical alignment
     */
    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }
    
    /**
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
        minimumHeight = 0;
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
    
    /** Getter for property minimumHeight.
     * @return Value of property minimumHeight.
     */
    public float getMinimumHeight() {
        return minimumHeight;
    }
    
    /** Setter for property minimumHeight.
     * @param minimumHeight New value of property minimumHeight.
     */
    public void setMinimumHeight(float minimumHeight) {
        this.minimumHeight = minimumHeight;
        fixedHeight = 0;
    }
    
    /** Getter for property colspan.
     * @return Value of property colspan.
     */
    public int getColspan() {
        return colspan;
    }
    
    /** Setter for property colspan.
     * @param colspan New value of property colspan.
     */
    public void setColspan(int colspan) {
        this.colspan = colspan;
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

    public void setRunDirection(int runDirection) {
        if (runDirection < PdfWriter.RUN_DIRECTION_DEFAULT || runDirection > PdfWriter.RUN_DIRECTION_RTL)
            throw new RuntimeException("Invalid run direction: " + runDirection);
        this.runDirection = runDirection;
    }
    
    public int getRunDirection() {
        return runDirection;
    }
    
    /** Getter for property image.
     * @return Value of property image.
     *
     */
    public Image getImage() {
        return this.image;
    }
    
    /** Setter for property image.
     * @param image New value of property image.
     *
     */
    public void setImage(Image image) {
        this.image = image;
    }
    
    /** Gets the cell event for this cell.
     * @return the cell event
     *
     */
    public PdfPCellEvent getCellEvent() {
        return this.cellEvent;
    }
    
    /** Sets the cell event for this cell.
     * @param cellEvent the cell event
     *
     */
    public void setCellEvent(PdfPCellEvent cellEvent) {
        this.cellEvent = cellEvent;
    }
    
    /** Gets the arabic shaping options.
     * @return the arabic shaping options
     */
    public int getArabicOptions() {
        return this.arabicOptions;
    }
    
    /** Sets the arabic shaping options. The option can be AR_NOVOWEL,
     * AR_COMPOSEDTASHKEEL and AR_LIG.
     * @param arabicOptions the arabic shaping options
     */
    public void setArabicOptions(int arabicOptions) {
        this.arabicOptions = arabicOptions;
    }
    
    /** Getter for property useDescender.
     * @return Value of property useDescender.
     *
     */
    public boolean isUseDescender() {
        return this.useDescender;
    }
    
    /** Setter for property useDescender.
     * @param useDescender New value of property useDescender.
     *
     */
    public void setUseDescender(boolean useDescender) {
        this.useDescender = useDescender;
    }
    
}

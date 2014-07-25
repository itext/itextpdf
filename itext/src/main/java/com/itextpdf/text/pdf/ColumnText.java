/*
 * $Id: ColumnText.java 6192 2014-01-29 14:37:53Z eugenemark $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.itextpdf.text.*;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfPTable.FittingRows;
import com.itextpdf.text.pdf.draw.DrawInterface;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;

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
 * If the column ended, a new column definition can be loaded with the method
 * <CODE>setColumns</CODE> and the method <CODE>go</CODE> can be called again.
 * <P>
 * If the text ended, more text can be loaded with <CODE>addText</CODE>
 * and the method <CODE>go</CODE> can be called again.<BR>
 * The only limitation is that one or more complete paragraphs must be loaded
 * each time.
 * <P>
 * Full bidirectional reordering is supported. If the run direction is
 * <CODE>PdfWriter.RUN_DIRECTION_RTL</CODE> the meaning of the horizontal
 * alignments and margins is mirrored.
 * @author Paulo Soares
 */

public class ColumnText {
	
	private final Logger LOGGER = LoggerFactory.getLogger(ColumnText.class);
	
    /** Eliminate the arabic vowels */
    public static final int AR_NOVOWEL = ArabicLigaturizer.ar_novowel;
    /** Compose the tashkeel in the ligatures. */
    public static final int AR_COMPOSEDTASHKEEL = ArabicLigaturizer.ar_composedtashkeel;
    /** Do some extra double ligatures. */
    public static final int AR_LIG = ArabicLigaturizer.ar_lig;
    /**
     * Digit shaping option: Replace European digits (U+0030...U+0039) by Arabic-Indic digits.
     */
    public static final int DIGITS_EN2AN = ArabicLigaturizer.DIGITS_EN2AN;

    /**
     * Digit shaping option: Replace Arabic-Indic digits by European digits (U+0030...U+0039).
     */
    public static final int DIGITS_AN2EN = ArabicLigaturizer.DIGITS_AN2EN;

    /**
     * Digit shaping option:
     * Replace European digits (U+0030...U+0039) by Arabic-Indic digits
     * if the most recent strongly directional character
     * is an Arabic letter (its Bidi direction value is RIGHT_TO_LEFT_ARABIC).
     * The initial state at the start of the text is assumed to be not an Arabic,
     * letter, so European digits at the start of the text will not change.
     * Compare to DIGITS_ALEN2AN_INIT_AL.
     */
    public static final int DIGITS_EN2AN_INIT_LR = ArabicLigaturizer.DIGITS_EN2AN_INIT_LR;

    /**
     * Digit shaping option:
     * Replace European digits (U+0030...U+0039) by Arabic-Indic digits
     * if the most recent strongly directional character
     * is an Arabic letter (its Bidi direction value is RIGHT_TO_LEFT_ARABIC).
     * The initial state at the start of the text is assumed to be an Arabic,
     * letter, so European digits at the start of the text will change.
     * Compare to DIGITS_ALEN2AN_INT_LR.
     */
    public static final int DIGITS_EN2AN_INIT_AL = ArabicLigaturizer.DIGITS_EN2AN_INIT_AL;

    /**
     * Digit type option: Use Arabic-Indic digits (U+0660...U+0669).
     */
    public static final int DIGIT_TYPE_AN = ArabicLigaturizer.DIGIT_TYPE_AN;

    /**
     * Digit type option: Use Eastern (Extended) Arabic-Indic digits (U+06f0...U+06f9).
     */
    public static final int DIGIT_TYPE_AN_EXTENDED = ArabicLigaturizer.DIGIT_TYPE_AN_EXTENDED;

    protected int runDirection = PdfWriter.RUN_DIRECTION_DEFAULT;

    /** the space char ratio */
    public static final float GLOBAL_SPACE_CHAR_RATIO = 0;

    /** Initial value of the status. */
    public static final int START_COLUMN = 0;

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

    protected float leftX;

    protected float rightX;

    /** The column alignment. Default is left alignment. */
    protected int alignment = Element.ALIGN_LEFT;

    /** The left column bound. */
    protected ArrayList<float[]> leftWall;

    /** The right column bound. */
    protected ArrayList<float[]> rightWall;

    /** The chunks that form the text. */
//    protected ArrayList chunks = new ArrayList();
    protected BidiLine bidiLine;

    /** The current y line location. Text will be written at this line minus the leading. */
    protected float yLine;

    /**
     * The X position after the last line that has been written.
     * @since 5.0.3
     */
    protected float lastX;

    /** The leading for the current line. */
    protected float currentLeading = 16;

    /** The fixed text leading. */
    protected float fixedLeading = 16;

    /** The text leading that is multiplied by the biggest font size in the line. */
    protected float multipliedLeading = 0;

    /** The <CODE>PdfContent</CODE> where the text will be written to. */
    protected PdfContentByte canvas;

    protected PdfContentByte[] canvases;

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

    /** The width of the line when the column is defined as a simple rectangle. */
    protected float rectangularWidth = -1;

    protected boolean rectangularMode = false;
    /** Holds value of property spaceCharRatio. */
    private float spaceCharRatio = GLOBAL_SPACE_CHAR_RATIO;

    private boolean lastWasNewline = true;
    private boolean repeatFirstLineIndent = true;

    /** Holds value of property linesWritten. */
    private int linesWritten;

    private float firstLineY;
    private boolean firstLineYDone = false;

    /** Holds value of property arabicOptions. */
    private int arabicOptions = 0;

    protected float descender;

    protected boolean composite = false;

    protected ColumnText compositeColumn;

    protected LinkedList<Element> compositeElements;

    protected int listIdx = 0;
    /**
     * Pointer for the row in a table that is being dealt with
     * @since 5.1.0
     */
    protected int rowIdx = 0;

    /**
     * The index of the last row that needed to be splitted.
     * @since 5.0.1 changed a boolean into an int
     */
    private int splittedRow = -1;

    protected Phrase waitPhrase;

    /** if true, first line height is adjusted so that the max ascender touches the top */
    private boolean useAscender = false;

    /** Holds value of property filledWidth. */
    private float filledWidth;

    private boolean adjustFirstLine = true;

    /**
     * @since 5.4.2
     */
    private boolean inheritGraphicState = false;

    /**
     * Creates a <CODE>ColumnText</CODE>.
     *
     * @param canvas the place where the text will be written to. Can
     * be a template.
     */
    public ColumnText(final PdfContentByte canvas) {
        this.canvas = canvas;
    }

    /**
     * Creates an independent duplicated of the instance <CODE>org</CODE>.
     *
     * @param org the original <CODE>ColumnText</CODE>
     * @return the duplicated
     */
    public static ColumnText duplicate(final ColumnText org) {
        ColumnText ct = new ColumnText(null);
        ct.setACopy(org);
        return ct;
    }

    /**
     * Makes this instance an independent copy of <CODE>org</CODE>.
     *
     * @param org the original <CODE>ColumnText</CODE>
     * @return itself
     */
    public ColumnText setACopy(final ColumnText org) {
        setSimpleVars(org);
        if (org.bidiLine != null)
            bidiLine = new BidiLine(org.bidiLine);
        return this;
    }

    protected void setSimpleVars(final ColumnText org) {
        maxY = org.maxY;
        minY = org.minY;
        alignment = org.alignment;
        leftWall = null;
        if (org.leftWall != null)
            leftWall = new ArrayList<float[]>(org.leftWall);
        rightWall = null;
        if (org.rightWall != null)
            rightWall = new ArrayList<float[]>(org.rightWall);
        yLine = org.yLine;
        currentLeading = org.currentLeading;
        fixedLeading = org.fixedLeading;
        multipliedLeading = org.multipliedLeading;
        canvas = org.canvas;
        canvases = org.canvases;
        lineStatus = org.lineStatus;
        indent = org.indent;
        followingIndent = org.followingIndent;
        rightIndent = org.rightIndent;
        extraParagraphSpace = org.extraParagraphSpace;
        rectangularWidth = org.rectangularWidth;
        rectangularMode = org.rectangularMode;
        spaceCharRatio = org.spaceCharRatio;
        lastWasNewline = org.lastWasNewline;
        repeatFirstLineIndent = org.repeatFirstLineIndent;
        linesWritten = org.linesWritten;
        arabicOptions = org.arabicOptions;
        runDirection = org.runDirection;
        descender = org.descender;
        composite = org.composite;
        splittedRow = org.splittedRow;
        if (org.composite) {
            compositeElements = new LinkedList<Element>();
            for (Element element : org.compositeElements) {
                if (element instanceof PdfPTable) {
                    compositeElements.add(new PdfPTable((PdfPTable)element));
                } else {
                    compositeElements.add(element);
                }
            }
            if (org.compositeColumn != null)
                compositeColumn = duplicate(org.compositeColumn);
        }
        listIdx = org.listIdx;
        rowIdx = org.rowIdx;
        firstLineY = org.firstLineY;
        leftX = org.leftX;
        rightX = org.rightX;
        firstLineYDone = org.firstLineYDone;
        waitPhrase = org.waitPhrase;
        useAscender = org.useAscender;
        filledWidth = org.filledWidth;
        adjustFirstLine = org.adjustFirstLine;
        inheritGraphicState = org.inheritGraphicState;
    }

    private void addWaitingPhrase() {
        if (bidiLine == null && waitPhrase != null) {
            bidiLine = new BidiLine();
            for (Chunk c: waitPhrase.getChunks()) {
                bidiLine.addChunk(new PdfChunk(c, null, waitPhrase.getTabSettings()));
            }
            waitPhrase = null;
        }
    }

    /**
     * Adds a <CODE>Phrase</CODE> to the current text array.
     * Will not have any effect if addElement() was called before.
     *
     * @param phrase the text
     */
    public void addText(final Phrase phrase) {
        if (phrase == null || composite)
            return;
        addWaitingPhrase();
        if (bidiLine == null) {
            waitPhrase = phrase;
            return;
        }
        for (Object element : phrase.getChunks()) {
            bidiLine.addChunk(new PdfChunk((Chunk)element, null, phrase.getTabSettings()));
        }
    }

    /**
     * Replaces the current text array with this <CODE>Phrase</CODE>.
     * Anything added previously with addElement() is lost.
     *
     * @param phrase the text
     */
    public void setText(final Phrase phrase) {
        bidiLine = null;
        composite = false;
        compositeColumn = null;
        compositeElements = null;
        listIdx = 0;
        rowIdx = 0;
        splittedRow = -1;
        waitPhrase = phrase;
    }

    /**
     * Adds a <CODE>Chunk</CODE> to the current text array.
     * Will not have any effect if addElement() was called before.
     *
     * @param chunk the text
     */
    public void addText(final Chunk chunk) {
        if (chunk == null || composite)
            return;
        addText(new Phrase(chunk));
    }

	/**
	 * Adds an element. Elements supported are <CODE>Paragraph</CODE>,
	 * <CODE>List</CODE>, <CODE>PdfPTable</CODE> and <CODE>Image</CODE>.
	 * Also accepts a <code>Chunk</code> and a
	 * <code>Phrase</code>, they are placed in a new <code>Paragraph<code>.
	 * <p>
	 * It removes all the text placed with <CODE>addText()</CODE>.
	 *
	 * @param element the <CODE>Element</CODE>
	 */
    public void addElement(Element element) {
        if (element == null)
            return;
        if (element instanceof Image) {
            Image img = (Image)element;
            PdfPTable t = new PdfPTable(1);
            float w = img.getWidthPercentage();
            if (w == 0) {
                t.setTotalWidth(img.getScaledWidth());
                t.setLockedWidth(true);
            }
            else
                t.setWidthPercentage(w);
            t.setSpacingAfter(img.getSpacingAfter());
            t.setSpacingBefore(img.getSpacingBefore());
            switch (img.getAlignment()) {
                case Image.LEFT:
                    t.setHorizontalAlignment(Element.ALIGN_LEFT);
                    break;
                case Image.RIGHT:
                    t.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    break;
                default:
                    t.setHorizontalAlignment(Element.ALIGN_CENTER);
                    break;
            }
            PdfPCell c = new PdfPCell(img, true);
            c.setPadding(0);
            c.setBorder(img.getBorder());
            c.setBorderColor(img.getBorderColor());
            c.setBorderWidth(img.getBorderWidth());
            c.setBackgroundColor(img.getBackgroundColor());
            t.addCell(c);
            element = t;
        }
        if (element.type() == Element.CHUNK) {
        	element = new Paragraph((Chunk)element);
        }
        else if (element.type() == Element.PHRASE) {
        	element = new Paragraph((Phrase)element);
        }
        if (element.type() != Element.PARAGRAPH && element.type() != Element.LIST && element.type() != Element.PTABLE && element.type() != Element.YMARK && element.type() != Element.DIV)
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("element.not.allowed"));
        if (!composite) {
            composite = true;
            compositeElements = new LinkedList<Element>();
            bidiLine = null;
            waitPhrase = null;
        }
        if (element.type() == Element.PARAGRAPH) {
        	Paragraph p = (Paragraph)element;
        	compositeElements.addAll(p.breakUp());
        	return;
        }
        compositeElements.add(element);
    }

    public static boolean isAllowedElement(Element element) {
    	int type = element.type();
    	if (type == Element.CHUNK || type == Element.PHRASE || type == Element.DIV
    			|| type == Element.PARAGRAPH || type == Element.LIST
    			|| type == Element.YMARK || type == Element.PTABLE) return true;
    	if (element instanceof Image) return true;
		return false;
    }
    
    /**
     * Converts a sequence of lines representing one of the column bounds into
     * an internal format.
     * <p>
     * Each array element will contain a <CODE>float[4]</CODE> representing
     * the line x = ax + b.
     *
     * @param cLine the column array
     * @return the converted array
     */
    protected ArrayList<float []> convertColumn(final float cLine[]) {
        if (cLine.length < 4)
            throw new RuntimeException(MessageLocalization.getComposedMessage("no.valid.column.line.found"));
        ArrayList<float []> cc = new ArrayList<float []>();
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
        if (cc.isEmpty())
            throw new RuntimeException(MessageLocalization.getComposedMessage("no.valid.column.line.found"));
        return cc;
    }

    /**
     * Finds the intersection between the <CODE>yLine</CODE> and the column. It will
     * set the <CODE>lineStatus</CODE> appropriately.
     *
     * @param wall the column to intersect
     * @return the x coordinate of the intersection
     */
    protected float findLimitsPoint(final ArrayList<float []> wall) {
        lineStatus = LINE_STATUS_OK;
        if (yLine < minY || yLine > maxY) {
            lineStatus = LINE_STATUS_OFFLIMITS;
            return 0;
        }
        for (int k = 0; k < wall.size(); ++k) {
            float r[] = wall.get(k);
            if (yLine < r[0] || yLine > r[1])
                continue;
            return r[2] * yLine + r[3];
        }
        lineStatus = LINE_STATUS_NOLINE;
        return 0;
    }

    /**
     * Finds the intersection between the <CODE>yLine</CODE> and the two
     * column bounds. It will set the <CODE>lineStatus</CODE> appropriately.
     *
     * @return a <CODE>float[2]</CODE>with the x coordinates of the intersection
     */
    protected float[] findLimitsOneLine() {
        float x1 = findLimitsPoint(leftWall);
        if (lineStatus == LINE_STATUS_OFFLIMITS || lineStatus == LINE_STATUS_NOLINE)
            return null;
        float x2 = findLimitsPoint(rightWall);
        if (lineStatus == LINE_STATUS_NOLINE)
            return null;
        return new float[]{x1, x2};
    }

    /**
     * Finds the intersection between the <CODE>yLine</CODE>,
     * the <CODE>yLine-leading</CODE>and the two column bounds.
     * It will set the <CODE>lineStatus</CODE> appropriately.
     *
     * @return a <CODE>float[4]</CODE>with the x coordinates of the intersection
     */
    protected float[] findLimitsTwoLines() {
        boolean repeat = false;
        for (;;) {
            if (repeat && currentLeading == 0)
                return null;
            repeat = true;
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
     *
     * @param leftLine the left column bound
     * @param rightLine the right column bound
     */
    public void setColumns(final float leftLine[], final float rightLine[]) {
        maxY = -10e20f;
        minY = 10e20f;
        setYLine(Math.max(leftLine[1], leftLine[leftLine.length - 1]));
        rightWall = convertColumn(rightLine);
        leftWall = convertColumn(leftLine);
        rectangularWidth = -1;
        rectangularMode = false;
    }

    /**
     * Simplified method for rectangular columns.
     *
     * @param phrase a <CODE>Phrase</CODE>
     * @param llx the lower left x corner
     * @param lly the lower left y corner
     * @param urx the upper right x corner
     * @param ury the upper right y corner
     * @param leading the leading
     * @param alignment the column alignment
     */
    public void setSimpleColumn(final Phrase phrase, final float llx, final float lly, final float urx, final float ury, final float leading, final int alignment) {
        addText(phrase);
        setSimpleColumn(llx, lly, urx, ury, leading, alignment);
    }

    /**
     * Simplified method for rectangular columns.
     *
     * @param llx the lower left x corner
     * @param lly the lower left y corner
     * @param urx the upper right x corner
     * @param ury the upper right y corner
     * @param leading the leading
     * @param alignment the column alignment
     */
    public void setSimpleColumn(final float llx, final float lly, final float urx, final float ury, final float leading, final int alignment) {
        setLeading(leading);
        this.alignment = alignment;
        setSimpleColumn(llx, lly, urx, ury);
    }

    /**
     * Simplified method for rectangular columns.
     *
     * @param llx
     * @param lly
     * @param urx
     * @param ury
     */
    public void setSimpleColumn(final float llx, final float lly, final float urx, final float ury) {
        leftX = Math.min(llx, urx);
        maxY = Math.max(lly, ury);
        minY = Math.min(lly, ury);
        rightX = Math.max(llx, urx);
        yLine = maxY;
        rectangularWidth = rightX - leftX;
        if (rectangularWidth < 0)
            rectangularWidth = 0;
        rectangularMode = true;
    }
    
    /**
     * Simplified method for rectangular columns.
     * @param rect	the rectangle for the column
     */
    public void setSimpleColumn(Rectangle rect) {
    	setSimpleColumn(rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop());
    }

    /**
     * Sets the leading to fixed.
     *
     * @param leading the leading
     */
    public void setLeading(final float leading) {
        fixedLeading = leading;
        multipliedLeading = 0;
    }

    /**
     * Sets the leading fixed and variable. The resultant leading will be
     * fixedLeading+multipliedLeading*maxFontSize where maxFontSize is the
     * size of the biggest font in the line.
     *
     * @param fixedLeading the fixed leading
     * @param multipliedLeading the variable leading
     */
    public void setLeading(final float fixedLeading, final float multipliedLeading) {
        this.fixedLeading = fixedLeading;
        this.multipliedLeading = multipliedLeading;
    }

    /**
     * Gets the fixed leading.
     *
     * @return the leading
     */
    public float getLeading() {
        return fixedLeading;
    }

    /**
     * Gets the variable leading.
     *
     * @return the leading
     */
    public float getMultipliedLeading() {
        return multipliedLeading;
    }

    /**
     * Sets the yLine. The line will be written to yLine-leading.
     *
     * @param yLine the yLine
     */
    public void setYLine(final float yLine) {
        this.yLine = yLine;
    }

    /**
     * Gets the yLine.
     *
     * @return the yLine
     */
    public float getYLine() {
        return yLine;
    }
    
    /**
     * Gets the number of rows that were drawn when a table is involved.
     */
    public int getRowsDrawn() {
    	return rowIdx;
    }

    /**
     * Sets the alignment.
     *
     * @param alignment the alignment
     */
    public void setAlignment(final int alignment) {
        this.alignment = alignment;
    }

    /**
     * Gets the alignment.
     *
     * @return the alignment
     */
    public int getAlignment() {
        return alignment;
    }

    /**
     * Sets the first paragraph line indent.
     *
     * @param indent the indent
     */
    public void setIndent(final float indent) {
    	setIndent(indent, true);
    }
    /**
     * Sets the first paragraph line indent.
     *
     * @param indent the indent
     * @param	repeatFirstLineIndent	do we need to repeat the indentation of the first line after a newline?
     */
    public void setIndent(final float indent, final boolean repeatFirstLineIndent) {
        this.indent = indent;
        lastWasNewline = true;
        this.repeatFirstLineIndent = repeatFirstLineIndent;
    }

    /**
     * Gets the first paragraph line indent.
     *
     * @return the indent
     */
    public float getIndent() {
        return indent;
    }

    /**
     * Sets the following paragraph lines indent.
     *
     * @param indent the indent
     */
    public void setFollowingIndent(final float indent) {
        this.followingIndent = indent;
        lastWasNewline = true;
    }

    /**
     * Gets the following paragraph lines indent.
     *
     * @return the indent
     */
    public float getFollowingIndent() {
        return followingIndent;
    }

    /**
     * Sets the right paragraph lines indent.
     *
     * @param indent the indent
     */
    public void setRightIndent(final float indent) {
        this.rightIndent = indent;
        lastWasNewline = true;
    }

    /**
     * Gets the right paragraph lines indent.
     *
     * @return the indent
     */
    public float getRightIndent() {
        return rightIndent;
    }

    /**
     * Gets the currentLeading.
     *
     * @return the currentLeading
     */
    public float getCurrentLeading() {
        return currentLeading;
    }

    public boolean getInheritGraphicState() {
        return inheritGraphicState;
    }

    public void setInheritGraphicState(boolean inheritGraphicState) {
        this.inheritGraphicState = inheritGraphicState;
    }

    /**
     * Outputs the lines to the document. It is equivalent to <CODE>go(false)</CODE>.
     *
     * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
     * and/or <CODE>NO_MORE_COLUMN</CODE>
     * @throws DocumentException on error
     */
    public int go() throws DocumentException {
        return go(false);
    }

    /**
     * Outputs the lines to the document. The output can be simulated.
     * @param simulate <CODE>true</CODE> to simulate the writing to the document
     * @return returns the result of the operation. It can be <CODE>NO_MORE_TEXT</CODE>
     * and/or <CODE>NO_MORE_COLUMN</CODE>
     * @throws DocumentException on error
     */
    public int go(final boolean simulate) throws DocumentException {
        return go(simulate, null);
    }

    public int go(final boolean simulate, final IAccessibleElement elementToGo) throws DocumentException {
        if (composite)
            return goComposite(simulate);

        ListBody lBody = null;
        if (isTagged(canvas) && elementToGo instanceof ListItem) {
            lBody = ((ListItem)elementToGo).getListBody();
        }

        addWaitingPhrase();
        if (bidiLine == null)
            return NO_MORE_TEXT;
        descender = 0;
        linesWritten = 0;
        lastX = 0;
        boolean dirty = false;
        float ratio = spaceCharRatio;
        Object currentValues[] = new Object[2];
        PdfFont currentFont = null;
        Float lastBaseFactor = new Float(0);
        currentValues[1] = lastBaseFactor;
        PdfDocument pdf = null;
        PdfContentByte graphics = null;
        PdfContentByte text = null;
        firstLineY = Float.NaN;
        int localRunDirection = PdfWriter.RUN_DIRECTION_NO_BIDI;
        if (runDirection != PdfWriter.RUN_DIRECTION_DEFAULT)
            localRunDirection = runDirection;
        if (canvas != null) {
            graphics = canvas;
            pdf = canvas.getPdfDocument();
            if (!isTagged(canvas))
                text = canvas.getDuplicate(inheritGraphicState);
            else
                text = canvas;
        }
        else if (!simulate)
            throw new NullPointerException(MessageLocalization.getComposedMessage("columntext.go.with.simulate.eq.eq.false.and.text.eq.eq.null"));
        if (!simulate) {
            if (ratio == GLOBAL_SPACE_CHAR_RATIO)
                ratio = text.getPdfWriter().getSpaceCharRatio();
            else if (ratio < 0.001f)
                ratio = 0.001f;
        }
        if (!rectangularMode) {
        	float max = 0;
        	for (PdfChunk c : bidiLine.chunks) {
        		max = Math.max(max, c.height());
        	}
        	currentLeading = fixedLeading + max * multipliedLeading;
        }
        float firstIndent = 0;
        PdfLine line;
        float x1;
        int status = 0;
        while(true) {
        	firstIndent = lastWasNewline ? indent : followingIndent; //
        	if (rectangularMode) {
        		if (rectangularWidth <= firstIndent + rightIndent) {
        			status = NO_MORE_COLUMN;
        			if (bidiLine.isEmpty())
        				status |= NO_MORE_TEXT;
        			break;
        		}
        		if (bidiLine.isEmpty()) {
        			status = NO_MORE_TEXT;
        			break;
        		}
                line = bidiLine.processLine(leftX, rectangularWidth - firstIndent - rightIndent, alignment, localRunDirection, arabicOptions, minY, yLine, descender);
                if (line == null) {
                	status = NO_MORE_TEXT;
                	break;
                }
                float[] maxSize = line.getMaxSize(fixedLeading, multipliedLeading);
                if (isUseAscender() && Float.isNaN(firstLineY))
                	currentLeading = line.getAscender();
                else
                	currentLeading = Math.max(maxSize[0], maxSize[1] - descender);
                if (yLine > maxY || yLine - currentLeading < minY ) {
                	status = NO_MORE_COLUMN;
                	bidiLine.restore();
                	break;
                }
                yLine -= currentLeading;
                if (!simulate && !dirty) {
                    text.beginText();
                	dirty = true;
                }
                if (Float.isNaN(firstLineY))
                	firstLineY = yLine;
                updateFilledWidth(rectangularWidth - line.widthLeft());
                x1 = leftX;
        	}
            else {
               	float yTemp = yLine - currentLeading;
               	float xx[] = findLimitsTwoLines();
               	if (xx == null) {
               		status = NO_MORE_COLUMN;
               		if (bidiLine.isEmpty())
               			status |= NO_MORE_TEXT;
               		yLine = yTemp;
               		break;
               	}
               	if (bidiLine.isEmpty()) {
               		status = NO_MORE_TEXT;
               		yLine = yTemp;
               		break;
               	}
               	x1 = Math.max(xx[0], xx[2]);
                float x2 = Math.min(xx[1], xx[3]);
                if (x2 - x1 <= firstIndent + rightIndent)
                    continue;
                if (!simulate && !dirty) {
                    text.beginText();
                    dirty = true;
                }
                line = bidiLine.processLine(x1, x2 - x1 - firstIndent - rightIndent, alignment, localRunDirection, arabicOptions, minY, yLine, descender);
                if (line == null) {
                    status = NO_MORE_TEXT;
                    yLine = yTemp;
                    break;
                }
            }
            if (isTagged(canvas) && elementToGo instanceof ListItem) {
                if (!Float.isNaN(firstLineY) && !firstLineYDone) {
                    if (!simulate) {
                        ListLabel lbl = ((ListItem)elementToGo).getListLabel();
                        canvas.openMCBlock(lbl);
                        Chunk symbol = new Chunk(((ListItem)elementToGo).getListSymbol());
                        symbol.setRole(null);
                        ColumnText.showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(symbol), leftX + lbl.getIndentation(), firstLineY, 0);
                        canvas.closeMCBlock(lbl);
                    }
                    firstLineYDone = true;
                }
            }
            if (!simulate) {
                if (lBody != null) {
                    canvas.openMCBlock(lBody);
                    lBody = null;
                }
                currentValues[0] = currentFont;
                text.setTextMatrix(x1 + (line.isRTL() ? rightIndent : firstIndent) + line.indentLeft(), yLine);
                lastX = pdf.writeLineToContent(line, text, graphics, currentValues, ratio);
                currentFont = (PdfFont)currentValues[0];
            }
            lastWasNewline = repeatFirstLineIndent && line.isNewlineSplit();
            yLine -= line.isNewlineSplit() ? extraParagraphSpace : 0;
            ++linesWritten;
            descender = line.getDescender();
        }
        if (dirty) {
            text.endText();
            if (canvas != text)
                canvas.add(text);
        }
        return status;
    }

    /**
     * Sets the extra space between paragraphs.
     *
     * @return the extra space between paragraphs
     */
    public float getExtraParagraphSpace() {
        return extraParagraphSpace;
    }

    /**
     * Sets the extra space between paragraphs.
     *
     * @param extraParagraphSpace the extra space between paragraphs
     */
    public void setExtraParagraphSpace(final float extraParagraphSpace) {
        this.extraParagraphSpace = extraParagraphSpace;
    }

    /**
     * Clears the chunk array.
     * A call to <CODE>go()</CODE> will always return NO_MORE_TEXT.
     */
    public void clearChunks() {
        if (bidiLine != null)
            bidiLine.clearChunks();
    }

    /**
     * Gets the space/character extra spacing ratio for fully justified text.
     *
     * @return the space/character extra spacing ratio
     */
    public float getSpaceCharRatio() {
        return spaceCharRatio;
    }

    /**
     * Sets the ratio between the extra word spacing and the extra character
     * spacing when the text is fully justified.
     * Extra word spacing will grow <CODE>spaceCharRatio</CODE> times more
     * than extra character spacing.
     * If the ratio is <CODE>PdfWriter.NO_SPACE_CHAR_RATIO</CODE> then the
     * extra character spacing will be zero.
     *
     * @param spaceCharRatio the ratio between the extra word spacing and the extra character spacing
     */
    public void setSpaceCharRatio(final float spaceCharRatio) {
        this.spaceCharRatio = spaceCharRatio;
    }

    /**
     * Sets the run direction.
     *
     * @param runDirection the run direction
     */
    public void setRunDirection(final int runDirection) {
        if (runDirection < PdfWriter.RUN_DIRECTION_DEFAULT || runDirection > PdfWriter.RUN_DIRECTION_RTL)
            throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
        this.runDirection = runDirection;
    }

    /**
     * Gets the run direction.
     *
     * @return the run direction
     */
    public int getRunDirection() {
        return runDirection;
    }

    /**
     * Gets the number of lines written.
     *
     * @return the number of lines written
     */
    public int getLinesWritten() {
        return this.linesWritten;
    }

    /**
     * Gets the X position of the end of the last line that has been written
     * (will not work in simulation mode!).
     * @since 5.0.3
     */
    public float getLastX() {
    	return lastX;
    }

    /**
     * Gets the arabic shaping options.
     *
     * @return the arabic shaping options
     */
    public int getArabicOptions() {
        return this.arabicOptions;
    }

    /**
     * Sets the arabic shaping options. The option can be AR_NOVOWEL,
     * AR_COMPOSEDTASHKEEL and AR_LIG.
     *
     * @param arabicOptions the arabic shaping options
     */
    public void setArabicOptions(final int arabicOptions) {
        this.arabicOptions = arabicOptions;
    }

    /**
     * Gets the biggest descender value of the last line written.
     *
     * @return the biggest descender value of the last line written
     */
    public float getDescender() {
        return descender;
    }

    /**
     * Gets the width that the line will occupy after writing.
     * Only the width of the first line is returned.
     *
     * @param phrase the <CODE>Phrase</CODE> containing the line
     * @param runDirection the run direction
     * @param arabicOptions the options for the arabic shaping
     * @return the width of the line
     */
    public static float getWidth(final Phrase phrase, final int runDirection, final int arabicOptions) {
        ColumnText ct = new ColumnText(null);
        ct.addText(phrase);
        ct.addWaitingPhrase();
        PdfLine line = ct.bidiLine.processLine(0, 20000, Element.ALIGN_LEFT, runDirection, arabicOptions, 0, 0, 0);
        if (line == null)
            return 0;
        else
            return 20000 - line.widthLeft();
    }

    /**
     * Gets the width that the line will occupy after writing.
     * Only the width of the first line is returned.
     *
     * @param phrase the <CODE>Phrase</CODE> containing the line
     * @return the width of the line
     */
    public static float getWidth(final Phrase phrase) {
        return getWidth(phrase, PdfWriter.RUN_DIRECTION_NO_BIDI, 0);
    }

    /**
     * Shows a line of text. Only the first line is written.
     *
     * @param canvas where the text is to be written to
     * @param alignment the alignment. It is not influenced by the run direction
     * @param phrase the <CODE>Phrase</CODE> with the text
     * @param x the x reference position
     * @param y the y reference position
     * @param rotation the rotation to be applied in degrees counterclockwise
     * @param runDirection the run direction
     * @param arabicOptions the options for the arabic shaping
     */
    public static void showTextAligned(final PdfContentByte canvas, int alignment, final Phrase phrase, final float x, final float y, final float rotation, final int runDirection, final int arabicOptions) {
        if (alignment != Element.ALIGN_LEFT && alignment != Element.ALIGN_CENTER
            && alignment != Element.ALIGN_RIGHT)
            alignment = Element.ALIGN_LEFT;
        canvas.saveState();
        ColumnText ct = new ColumnText(canvas);
        float lly = -1;
        float ury = 2;
        float llx;
        float urx;
        switch (alignment) {
        	case Element.ALIGN_LEFT:
        		llx = 0;
        		urx = 20000;
        		break;
        	case Element.ALIGN_RIGHT:
        		llx = -20000;
        		urx = 0;
        		break;
        	default:
        		llx = -20000;
        		urx = 20000;
        		break;
        }
        if (rotation == 0) {
        	llx += x;
        	lly += y;
        	urx += x;
        	ury += y;
        }
        else {
            double alpha = rotation * Math.PI / 180.0;
            float cos = (float)Math.cos(alpha);
            float sin = (float)Math.sin(alpha);
            canvas.concatCTM(cos, sin, -sin, cos, x, y);
        }
        ct.setSimpleColumn(phrase, llx, lly, urx, ury, 2, alignment);
        if (runDirection == PdfWriter.RUN_DIRECTION_RTL) {
            if (alignment == Element.ALIGN_LEFT)
                alignment = Element.ALIGN_RIGHT;
            else if (alignment == Element.ALIGN_RIGHT)
                alignment = Element.ALIGN_LEFT;
        }
        ct.setAlignment(alignment);
        ct.setArabicOptions(arabicOptions);
        ct.setRunDirection(runDirection);
        try {
            ct.go();
        }
        catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
        canvas.restoreState();
    }

    /**
     * Shows a line of text. Only the first line is written.
     *
     * @param canvas where the text is to be written to
     * @param alignment the alignment
     * @param phrase the <CODE>Phrase</CODE> with the text
     * @param x the x reference position
     * @param y the y reference position
     * @param rotation the rotation to be applied in degrees counterclockwise
     */
    public static void showTextAligned(final PdfContentByte canvas, final int alignment, final Phrase phrase, final float x, final float y, final float rotation) {
        showTextAligned(canvas, alignment, phrase, x, y, rotation, PdfWriter.RUN_DIRECTION_NO_BIDI, 0);
    }

	/**
	 * Fits the text to some rectangle adjusting the font size as needed.
	 * @param font the font to use
	 * @param text the text
	 * @param rect the rectangle where the text must fit
	 * @param maxFontSize the maximum font size
	 * @param runDirection the run direction
	 * @return the calculated font size that makes the text fit
	 */
	public static float fitText(Font font, String text, Rectangle rect, float maxFontSize, int runDirection) {
	    try {
	        ColumnText ct = null;
	        int status = 0;
	        if (maxFontSize <= 0) {
	            int cr = 0;
	            int lf = 0;
	            char t[] = text.toCharArray();
	            for (int k = 0; k < t.length; ++k) {
	                if (t[k] == '\n')
	                    ++lf;
	                else if (t[k] == '\r')
	                    ++cr;
	            }
	            int minLines = Math.max(cr, lf) + 1;
	            maxFontSize = Math.abs(rect.getHeight()) / minLines - 0.001f;
	        }
	        font.setSize(maxFontSize);
	        Phrase ph = new Phrase(text, font);
	        ct = new ColumnText(null);
	        ct.setSimpleColumn(ph, rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), maxFontSize, Element.ALIGN_LEFT);
	        ct.setRunDirection(runDirection);
	        status = ct.go(true);
	        if ((status & NO_MORE_TEXT) != 0)
	            return maxFontSize;
	        float precision = 0.1f;
	        float min = 0;
	        float max = maxFontSize;
	        float size = maxFontSize;
	        for (int k = 0; k < 50; ++k) { //just in case it doesn't converge
	            size = (min + max) / 2;
	            ct = new ColumnText(null);
	            font.setSize(size);
	            ct.setSimpleColumn(new Phrase(text, font), rect.getLeft(), rect.getBottom(), rect.getRight(), rect.getTop(), size, Element.ALIGN_LEFT);
	            ct.setRunDirection(runDirection);
	            status = ct.go(true);
	            if ((status & NO_MORE_TEXT) != 0) {
	                if (max - min < size * precision)
	                    return size;
	                min = size;
	            }
	            else
	                max = size;
	        }
	        return size;
	    }
	    catch (Exception e) {
	        throw new ExceptionConverter(e);
	    }
	}
    
    
    protected int goComposite(final boolean simulate) throws DocumentException {
    	PdfDocument pdf = null;
        if (canvas != null)
            pdf = canvas.pdf;
        if (!rectangularMode)
            throw new DocumentException(MessageLocalization.getComposedMessage("irregular.columns.are.not.supported.in.composite.mode"));
    	linesWritten = 0;
        descender = 0;
        boolean firstPass = true;
        main_loop:
        while (true) {
            if (compositeElements.isEmpty())
                return NO_MORE_TEXT;
            Element element = compositeElements.getFirst();
            if (element.type() == Element.PARAGRAPH) {
                Paragraph para = (Paragraph)element;
                int status = 0;
                for (int keep = 0; keep < 2; ++keep) {
                    float lastY = yLine;
                    boolean createHere = false;
                    if (compositeColumn == null) {
                        compositeColumn = new ColumnText(canvas);
                        compositeColumn.setAlignment(para.getAlignment());
                        compositeColumn.setIndent(para.getIndentationLeft() + para.getFirstLineIndent(), false);
                        compositeColumn.setExtraParagraphSpace(para.getExtraParagraphSpace());
                        compositeColumn.setFollowingIndent(para.getIndentationLeft());
                        compositeColumn.setRightIndent(para.getIndentationRight());
                        compositeColumn.setLeading(para.getLeading(), para.getMultipliedLeading());
                        compositeColumn.setRunDirection(runDirection);
                        compositeColumn.setArabicOptions(arabicOptions);
                        compositeColumn.setSpaceCharRatio(spaceCharRatio);
                        compositeColumn.addText(para);
                        if (!(firstPass && adjustFirstLine)) {
                            yLine -= para.getSpacingBefore();
                        }
                        createHere = true;
                    }
                    compositeColumn.setUseAscender((firstPass || descender == 0) && adjustFirstLine ? useAscender : false);
                    compositeColumn.setInheritGraphicState(inheritGraphicState);
                    compositeColumn.leftX = leftX;
                    compositeColumn.rightX = rightX;
                    compositeColumn.yLine = yLine;
                    compositeColumn.rectangularWidth = rectangularWidth;
                    compositeColumn.rectangularMode = rectangularMode;
                    compositeColumn.minY = minY;
                    compositeColumn.maxY = maxY;
                    boolean keepCandidate = para.getKeepTogether() && createHere && !(firstPass && adjustFirstLine);
                    boolean s = simulate || keepCandidate && keep == 0;
                    if (isTagged(canvas) && !s) {
                        canvas.openMCBlock(para);
                    }
                    status = compositeColumn.go(s);
                    if (isTagged(canvas) && !s) {
                        canvas.closeMCBlock(para);
                    }
                    lastX = compositeColumn.getLastX();
                    updateFilledWidth(compositeColumn.filledWidth);
                    if ((status & NO_MORE_TEXT) == 0 && keepCandidate) {
                        compositeColumn = null;
                        yLine = lastY;
                        return NO_MORE_COLUMN;
                    }
                    if (simulate || !keepCandidate)
                        break;
                    if (keep == 0) {
                        compositeColumn = null;
                        yLine = lastY;
                    }
                }
                firstPass = false;
                if (compositeColumn.getLinesWritten() > 0) {
                    yLine = compositeColumn.yLine;
                    linesWritten += compositeColumn.linesWritten;
                    descender = compositeColumn.descender;
                }
                currentLeading = compositeColumn.currentLeading;
                if ((status & NO_MORE_TEXT) != 0) {
                    compositeColumn = null;
                    compositeElements.removeFirst();
                    yLine -= para.getSpacingAfter();
                }
                if ((status & NO_MORE_COLUMN) != 0) {
                    return NO_MORE_COLUMN;
                }
            }
            else if (element.type() == Element.LIST) {
                com.itextpdf.text.List list = (com.itextpdf.text.List)element;
                ArrayList<Element> items = list.getItems();
                ListItem item = null;
                float listIndentation = list.getIndentationLeft();
                int count = 0;
                Stack<Object[]> stack = new Stack<Object[]>();
                for (int k = 0; k < items.size(); ++k) {
                    Object obj = items.get(k);
                    if (obj instanceof ListItem) {
                        if (count == listIdx) {
                            item = (ListItem)obj;
                            break;
                        }
                        else ++count;
                    }
                    else if (obj instanceof com.itextpdf.text.List) {
                        stack.push(new Object[]{list, Integer.valueOf(k), new Float(listIndentation)});
                        list = (com.itextpdf.text.List)obj;
                        items = list.getItems();
                        listIndentation += list.getIndentationLeft();
                        k = -1;
                        continue;
                    }
                    if (k == items.size() - 1) {
                        if (!stack.isEmpty()) {
                            Object objs[] = stack.pop();
                            list = (com.itextpdf.text.List)objs[0];
                            items = list.getItems();
                            k = ((Integer)objs[1]).intValue();
                            listIndentation = ((Float)objs[2]).floatValue();
                        }
                    }
                }
                int status = 0;
                for (int keep = 0; keep < 2; ++keep) {
                    float lastY = yLine;
                    boolean createHere = false;
                    if (compositeColumn == null) {
                        if (item == null) {
                            listIdx = 0;
                            compositeElements.removeFirst();
                            continue main_loop;
                        }
                        compositeColumn = new ColumnText(canvas);
                        compositeColumn.setUseAscender((firstPass || descender == 0) && adjustFirstLine ? useAscender : false);
                        compositeColumn.setInheritGraphicState(inheritGraphicState);
                        compositeColumn.setAlignment(item.getAlignment());
                        compositeColumn.setIndent(item.getIndentationLeft() + listIndentation + item.getFirstLineIndent(), false);
                        compositeColumn.setExtraParagraphSpace(item.getExtraParagraphSpace());
                        compositeColumn.setFollowingIndent(compositeColumn.getIndent());
                        compositeColumn.setRightIndent(item.getIndentationRight() + list.getIndentationRight());
                        compositeColumn.setLeading(item.getLeading(), item.getMultipliedLeading());
                        compositeColumn.setRunDirection(runDirection);
                        compositeColumn.setArabicOptions(arabicOptions);
                        compositeColumn.setSpaceCharRatio(spaceCharRatio);
                        compositeColumn.addText(item);
                        if (!(firstPass && adjustFirstLine)) {
                            yLine -= item.getSpacingBefore();
                        }
                        createHere = true;
                    }
                    compositeColumn.leftX = leftX;
                    compositeColumn.rightX = rightX;
                    compositeColumn.yLine = yLine;
                    compositeColumn.rectangularWidth = rectangularWidth;
                    compositeColumn.rectangularMode = rectangularMode;
                    compositeColumn.minY = minY;
                    compositeColumn.maxY = maxY;
                    boolean keepCandidate = item.getKeepTogether() && createHere && !(firstPass && adjustFirstLine);
                    boolean s = simulate || keepCandidate && keep == 0;
                    if (isTagged(canvas) && !s) {
                        item.getListLabel().setIndentation(listIndentation);
                        if (list.getFirstItem() == item || (compositeColumn != null && compositeColumn.bidiLine != null))
                            canvas.openMCBlock(list);
                        canvas.openMCBlock(item);
                    }
					status = compositeColumn.go(simulate || keepCandidate && keep == 0, item);
                    if (isTagged(canvas) && !s) {
                        canvas.closeMCBlock(item.getListBody());
                        canvas.closeMCBlock(item);
                        if ((list.getLastItem() == item && (status & NO_MORE_TEXT) != 0) || (status & NO_MORE_COLUMN) != 0)
                            canvas.closeMCBlock(list);
                    }
                    lastX = compositeColumn.getLastX();
                    updateFilledWidth(compositeColumn.filledWidth);
                    if ((status & NO_MORE_TEXT) == 0 && keepCandidate) {
                        compositeColumn = null;
                        yLine = lastY;
                        return NO_MORE_COLUMN;
                    }
                    if (simulate || !keepCandidate)
                        break;
                    if (keep == 0) {
                        compositeColumn = null;
                        yLine = lastY;
                    }
                }
                firstPass = false;
                yLine = compositeColumn.yLine;
                linesWritten += compositeColumn.linesWritten;
                descender = compositeColumn.descender;
                currentLeading = compositeColumn.currentLeading;
                if (!isTagged(canvas)) {
                    if (!Float.isNaN(compositeColumn.firstLineY) && !compositeColumn.firstLineYDone) {
                        if (!simulate) {
                            showTextAligned(canvas, Element.ALIGN_LEFT, new Phrase(item.getListSymbol()), compositeColumn.leftX + listIndentation, compositeColumn.firstLineY, 0);
                        }
                        compositeColumn.firstLineYDone = true;
                    }
                }
                if ((status & NO_MORE_TEXT) != 0) {
                    compositeColumn = null;
                    ++listIdx;
                    yLine -= item.getSpacingAfter();
                }
                if ((status & NO_MORE_COLUMN) != 0)
                    return NO_MORE_COLUMN;
            }
            else if (element.type() == Element.PTABLE) {

            	// INITIALISATIONS

                // get the PdfPTable element
                PdfPTable table = (PdfPTable)element;

                // tables without a body are dismissed
                if (table.size() <= table.getHeaderRows()) {
                    compositeElements.removeFirst();
                    continue;
                }

                // Y-offset
                float yTemp = yLine;
                yTemp += descender;
                if (rowIdx == 0 && adjustFirstLine)
                    yTemp -= table.spacingBefore();

                // if there's no space left, ask for new column
                if (yTemp < minY || yTemp > maxY)
                    return NO_MORE_COLUMN;

                // mark start of table
                float yLineWrite = yTemp;
                float x1 = leftX;
                currentLeading = 0;
                // get the width of the table
                float tableWidth;
                if (table.isLockedWidth()) {
                    tableWidth = table.getTotalWidth();
                    updateFilledWidth(tableWidth);
                }
                else {
                    tableWidth = rectangularWidth * table.getWidthPercentage() / 100f;
                    table.setTotalWidth(tableWidth);
                }

                // HEADERS / FOOTERS
                // how many header rows are real header rows; how many are footer rows?
                table.normalizeHeadersFooters();
                int headerRows = table.getHeaderRows();
                int footerRows = table.getFooterRows();
                int realHeaderRows = headerRows - footerRows;
                float headerHeight = table.getHeaderHeight();
                float footerHeight = table.getFooterHeight();

                // do we need to skip the header?
                boolean skipHeader = table.isSkipFirstHeader() && rowIdx <= realHeaderRows && (table.isComplete() || rowIdx != realHeaderRows);
                // if not, we wan't to be able to add more than just a header and a footer
                if (!skipHeader) {
                    yTemp -= headerHeight;
                    if (yTemp < minY || yTemp > maxY) {
                        return NO_MORE_COLUMN;
                    }
                }

                // MEASURE NECESSARY SPACE

                // how many real rows (not header or footer rows) fit on a page?
                int k = 0;
                if (rowIdx < headerRows) {
                    rowIdx = headerRows;
                }
                // if the table isn't complete, we need to be able to add a footer
                if (!table.isComplete())
                	yTemp -= footerHeight;
                // k will be the first row that doesn't fit
                // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
                FittingRows fittingRows = table.getFittingRows(yTemp - minY, rowIdx);
                k = fittingRows.lastRow + 1;
                yTemp -= fittingRows.height;
                // splitting row spans

                LOGGER.info("Want to split at row " + k);
                int kTemp = k;
                while (kTemp > rowIdx && kTemp < table.size() && table.getRow(kTemp).isMayNotBreak()) {
                    kTemp--;
                }
                if ((kTemp > rowIdx && kTemp < k) || (kTemp == 0 && table.getRow(0).isMayNotBreak() && table.isLoopCheck())) {
                	yTemp = minY;
                	k = kTemp;
                	table.setLoopCheck(false);
                }
                LOGGER.info("Will split at row " + k);

                // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
                if (table.isSplitLate() && k > 0) {
                    fittingRows.correctLastRowChosen(table, k - 1);
                }
                // splitting row spans

                // only for incomplete tables:
                if (!table.isComplete())
                	yTemp += footerHeight;

                // IF ROWS MAY NOT BE SPLIT
                if (!table.isSplitRows()) {
        			splittedRow = -1;
                	if (k == rowIdx) {
                		// drop the whole table
                		if (k == table.size()) {
                			compositeElements.removeFirst();
                			continue;
                		}
                		// or drop the row
                		else {
                			table.getRows().remove(k);
                			return NO_MORE_COLUMN;
                		}
                	}
                }
                // IF ROWS SHOULD NOT BE SPLIT
                // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
                //else if (table.isSplitLate() && !table.hasRowspan(k) && rowIdx < k) {
                else if (table.isSplitLate() && rowIdx < k) {
                	splittedRow = -1;
                }
                // SPLIT ROWS (IF WANTED AND NECESSARY)
                else if (k < table.size()) {
                	// we calculate the remaining vertical space
                    // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
                    // correct yTemp to only take completed rows into account
                    yTemp -= fittingRows.completedRowsHeight - fittingRows.height;
                    // splitting row spans

                    float h = yTemp - minY;
                    // we create a new row with the remaining content
                    PdfPRow newRow = table.getRow(k).splitRow(table, k, h);
                    // if the row isn't null add it as an extra row
                    if (newRow == null) {
                        LOGGER.info("Didn't split row!");
                    	splittedRow = -1;
                    	if (rowIdx == k)
                    		return NO_MORE_COLUMN;
                    }
                    else {
                        // if the row hasn't been split before, we duplicate (part of) the table
                        if (k != splittedRow) {
                            splittedRow = k + 1;
                            table = new PdfPTable(table);
                            compositeElements.set(0, table);
                            ArrayList<PdfPRow> rows = table.getRows();
                            for (int i = headerRows; i < rowIdx; ++i)
                                rows.set(i, null);
                        }
                        yTemp = minY;
                        table.getRows().add(++k, newRow);
                        LOGGER.info("Inserting row at position " + k);
                    }
                }

                // We're no longer in the first pass
                firstPass = false;

                // if not in simulation mode, draw the table
                if (!simulate) {
                	// set the alignment
                    switch (table.getHorizontalAlignment()) {
                        case Element.ALIGN_LEFT:
                            break;
                        case Element.ALIGN_RIGHT:
                            x1 += rectangularWidth - tableWidth;
                            break;
                        default:
                            x1 += (rectangularWidth - tableWidth) / 2f;
                    }
                    // copy the rows that fit on the page in a new table nt
                    PdfPTable nt = PdfPTable.shallowCopy(table);
                    ArrayList<PdfPRow> sub = nt.getRows();
                    // first we add the real header rows (if necessary)
                    if (!skipHeader && realHeaderRows > 0) {
                        ArrayList<PdfPRow> rows = table.getRows(0, realHeaderRows);
                        if (isTagged(canvas))
                            nt.getHeader().rows = rows;
                        sub.addAll(rows);
                    }
                    else
                        nt.setHeaderRows(footerRows);
                    // then we add the real content

                    {
                        ArrayList<PdfPRow> rows = table.getRows(rowIdx, k);
                        if (isTagged(canvas)) {
                            nt.getBody().rows = rows;
                        }
                        sub.addAll(rows);
                    }
                    // do we need to show a footer?
                    boolean showFooter = !table.isSkipLastFooter();
                    boolean newPageFollows = false;
                    if (k < table.size()) {
                    	nt.setComplete(true);
                    	showFooter = true;
                    	newPageFollows = true;
                    }
                    // we add the footer rows if necessary (not for incomplete tables)
                    if (footerRows > 0 && nt.isComplete() && showFooter) {
                        ArrayList<PdfPRow> rows = table.getRows(realHeaderRows, realHeaderRows + footerRows);
                        if (isTagged(canvas)) {
                            nt.getFooter().rows = rows;
                        }
                    	sub.addAll(rows);
                    }
                    else {
                    	footerRows = 0;
                    }

                    // we need a correction if the last row needs to be extended
                    float rowHeight = 0;
                    int lastIdx = sub.size() - 1 - footerRows;
                    PdfPRow last = sub.get(lastIdx);
                    if (table.isExtendLastRow(newPageFollows)) {
                        rowHeight = last.getMaxHeights();
                        last.setMaxHeights(yTemp - minY + rowHeight);
                        yTemp = minY;
                    }

                    // newPageFollows indicates that this table is being split
                    if (newPageFollows) {
                        PdfPTableEvent tableEvent = table.getTableEvent();
                        if (tableEvent instanceof PdfPTableEventSplit) {
                            ((PdfPTableEventSplit)tableEvent).splitTable(table);
                        }
                    }

                    // now we render the rows of the new table
                    if (canvases != null) {
                        if (isTagged(canvases[PdfPTable.TEXTCANVAS])) {
                            canvases[PdfPTable.TEXTCANVAS].openMCBlock(table);
                        }
                        nt.writeSelectedRows(0, -1, 0, -1, x1, yLineWrite, canvases, false);
                        if (isTagged(canvases[PdfPTable.TEXTCANVAS])) {
                            canvases[PdfPTable.TEXTCANVAS].closeMCBlock(table);
                        }
                    }
                    else {
                        if (isTagged(canvas)) {
                            canvas.openMCBlock(table);
                        }
                        nt.writeSelectedRows(0, -1, 0, -1, x1, yLineWrite, canvas, false);
                        if (isTagged(canvas)) {
                            canvas.closeMCBlock(table);
                        }
                    }

                    // if the row was split, we copy the content of the last row
                    // that was consumed into the first row shown on the next page
                    if (splittedRow == k && k < table.size()) {
                       	PdfPRow splitted = table.getRows().get(k);
                    	splitted.copyRowContent(nt, lastIdx);
                    }
                    // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
                    else if (k > 0 && k < table.size()) {
                        // continue rowspans on next page
                        // (as the row was not split there is no content to copy)
                        PdfPRow row = table.getRow(k);
                        row.splitRowspans(table, k - 1, nt, lastIdx);
                    }
                    // splitting row spans

                    // reset the row height of the last row
                    if (table.isExtendLastRow(newPageFollows)) {
                        last.setMaxHeights(rowHeight);
                    }

                    // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz)
                    // newPageFollows indicates that this table is being split
                    if (newPageFollows) {
                        PdfPTableEvent tableEvent = table.getTableEvent();
                        if (tableEvent instanceof PdfPTableEventAfterSplit) {
                            PdfPRow row = table.getRow(k);
                            ((PdfPTableEventAfterSplit)tableEvent).afterSplitTable(table, row, k);
                        }
                    }
                }
                // in simulation mode, we need to take extendLastRow into account
                else if (table.isExtendLastRow() && minY > PdfPRow.BOTTOM_LIMIT) {
                    yTemp = minY;
                }

                yLine = yTemp;
                descender = 0;
                currentLeading = 0;
                if (!(skipHeader || table.isComplete()))
                	yLine += footerHeight;
                while (k < table.size()) {
                	if (table.getRowHeight(k) > 0 || table.hasRowspan(k)) {
                		break;
                	}
                	k++;
                }
                if (k >= table.size()) {
                	// Use up space no more than left
                	if(yLine - table.spacingAfter() < minY) {
                		yLine = minY;
                	}
                	else {
                		yLine -= table.spacingAfter();
                	}
                    compositeElements.removeFirst();
                    splittedRow = -1;
                    rowIdx = 0;
                }
                else {
                    if (splittedRow != -1) {
                        ArrayList<PdfPRow> rows = table.getRows();
                        for (int i = rowIdx; i < k; ++i)
                            rows.set(i, null);
                    }
                    rowIdx = k;
                    return NO_MORE_COLUMN;
                }
            }
            else if (element.type() == Element.YMARK) {
                if (!simulate) {
                    DrawInterface zh = (DrawInterface)element;
                    zh.draw(canvas, leftX, minY, rightX, maxY, yLine);
                }
                compositeElements.removeFirst();
            } else if (element.type() == Element.DIV) {
                ArrayList<Element> floatingElements = new ArrayList<Element>();
                do {
                    floatingElements.add(element);
                    compositeElements.removeFirst();
                    element = !compositeElements.isEmpty() ? compositeElements.getFirst() : null;
                } while (element != null && element.type() == Element.DIV);

                FloatLayout fl = new FloatLayout(floatingElements, useAscender);
                fl.setSimpleColumn(leftX, minY, rightX, yLine);
                int status = fl.layout(canvas, simulate);

                //firstPass = false;
                yLine = fl.getYLine();
                descender = 0;
                if ((status & NO_MORE_TEXT) == 0) {
                    compositeElements.addAll(floatingElements);
                    return status;
                }
            } else
                compositeElements.removeFirst();
        }
    }

    /**
     * Gets the canvas.
     * If a set of four canvases exists, the TEXTCANVAS is returned.
     *
     * @return a PdfContentByte.
     */
    public PdfContentByte getCanvas() {
        return canvas;
    }

    /**
     * Sets the canvas.
     * If before a set of four canvases was set, it is being unset.
     *
     * @param canvas
     */
    public void setCanvas(final PdfContentByte canvas) {
        this.canvas = canvas;
        this.canvases = null;
        if (compositeColumn != null)
            compositeColumn.setCanvas(canvas);
    }

    /**
     * Sets the canvases.
     *
     * @param canvases
     */
    public void setCanvases(final PdfContentByte[] canvases) {
        this.canvases = canvases;
        this.canvas = canvases[PdfPTable.TEXTCANVAS];
        if (compositeColumn != null)
            compositeColumn.setCanvases(canvases);
    }

    /**
     * Gets the canvases.
     *
     * @return an array of PdfContentByte
     */
    public PdfContentByte[] getCanvases() {
        return canvases;
    }

    /**
     * Checks if the element has a height of 0.
     *
     * @return true or false
     * @since 2.1.2
     */
    public boolean zeroHeightElement() {
        return composite && !compositeElements.isEmpty() && compositeElements.getFirst().type() == Element.YMARK;
    }

    public List<Element> getCompositeElements() {
    	return compositeElements;
    }
    
    /**
     * Checks if UseAscender is enabled/disabled.
     *
     * @return true is the adjustment of the first line height is based on max ascender.
     */
    public boolean isUseAscender() {
        return useAscender;
    }

    /**
     * Enables/Disables adjustment of first line height based on max ascender.
     *
     * @param useAscender	enable adjustment if true
     */
    public void setUseAscender(final boolean useAscender) {
        this.useAscender = useAscender;
    }

    /**
     * Checks the status variable and looks if there's still some text.
     */
    public static boolean hasMoreText(final int status) {
    	return (status & ColumnText.NO_MORE_TEXT) == 0;
    }

    /**
     * Gets the real width used by the largest line.
     *
     * @return the real width used by the largest line
     */
    public float getFilledWidth() {
        return filledWidth;
    }

    /**
     * Sets the real width used by the largest line.
     * Only used to set it to zero to start another measurement.
     *
     * @param filledWidth the real width used by the largest line
     */
    public void setFilledWidth(final float filledWidth) {
        this.filledWidth = filledWidth;
    }

    /**
     * Replaces the <CODE>filledWidth</CODE> if greater than the existing one.
     *
     * @param w the new <CODE>filledWidth</CODE> if greater than the existing one
     */
    public void updateFilledWidth(final float w) {
        if (w > filledWidth)
            filledWidth = w;
    }


    /**
     * Gets the first line adjustment property.
     *
     * @return the first line adjustment property.
     */
    public boolean isAdjustFirstLine() {
        return adjustFirstLine;
    }

    /**
     * Sets the first line adjustment.
     * Some objects have properties, like spacing before, that behave
     * differently if the object is the first to be written after go() or not.
     * The first line adjustment is <CODE>true</CODE> by default but can be
     * changed if several objects are to be placed one after the other in the
     * same column calling go() several times.
     *
     * @param adjustFirstLine <CODE>true</CODE> to adjust the first line, <CODE>false</CODE> otherwise
     */
    public void setAdjustFirstLine(final boolean adjustFirstLine) {
        this.adjustFirstLine = adjustFirstLine;
    }

    private static boolean isTagged(final PdfContentByte canvas) {
        return (canvas != null) && (canvas.pdf != null) && (canvas.writer != null) && canvas.writer.isTagged();
    }

}

/**
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Mark Hall
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
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
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

package com.lowagie.text.rtf;

import com.lowagie.text.*;

import java.util.*;
import java.io.*;
import java.awt.Color;

/**
 * A Helper Class for the <CODE>RtfWriter</CODE>.
 * <P>
 * Do not use it directly
 *
 * Parts of this Class were contributed by Steffen Stundzig. Many thanks for the
 * improvements.
 */
public class RtfCell {
    /** Constants for merging Cells */

    /** A possible value for merging */
    private static final int MERGE_HORIZ_FIRST = 1;
    /** A possible value for merging */
    private static final int MERGE_VERT_FIRST = 2;
    /** A possible value for merging */
    private static final int MERGE_BOTH_FIRST = 3;
    /** A possible value for merging */
    private static final int MERGE_HORIZ_PREV = 4;
    /** A possible value for merging */
    private static final int MERGE_VERT_PREV = 5;
    /** A possible value for merging */
    private static final int MERGE_BOTH_PREV = 6;

    /**
     * RTF Tags
     */

    /** First cell to merge with - Horizontal */
    private static final byte[] cellMergeFirst = "clmgf".getBytes();
    /** First cell to merge with - Vertical */
    private static final byte[] cellVMergeFirst = "clvmgf".getBytes();
    /** Merge cell with previous horizontal cell */
    private static final byte[] cellMergePrev = "clmrg".getBytes();
    /** Merge cell with previous vertical cell */
    private static final byte[] cellVMergePrev = "clvmrg".getBytes();
    /** Cell content vertical alignment bottom */
    private static final byte[] cellVerticalAlignBottom = "clvertalb".getBytes();
    /** Cell content vertical alignment center */
    private static final byte[] cellVerticalAlignCenter = "clvertalc".getBytes();
    /** Cell content vertical alignment top */
    private static final byte[] cellVerticalAlignTop = "clvertalt".getBytes();
    /** Cell border left */
    private static final byte[] cellBorderLeft = "clbrdrl".getBytes();
    /** Cell border right */
    private static final byte[] cellBorderRight = "clbrdrr".getBytes();
    /** Cell border top */
    private static final byte[] cellBorderTop = "clbrdrt".getBytes();
    /** Cell border bottom */
    private static final byte[] cellBorderBottom = "clbrdrb".getBytes();
    /** Cell background color */
    private static final byte[] cellBackgroundColor = "clcbpat".getBytes();
    /** Cell width format */
    private static final byte[] cellWidthStyle = "clftsWidth3".getBytes();
    /** Cell width */
    private static final byte[] cellWidthTag = "clwWidth".getBytes();
    /** Cell right border position */
    private static final byte[] cellRightBorder = "cellx".getBytes();
    /** Cell is part of table */
    protected static final byte[] cellInTable = "intbl".getBytes();
    /** End of cell */
    private static final byte[] cellEnd = "cell".getBytes();

    /** padding top */
    private static final byte[] cellPaddingTop = "clpadt".getBytes();
    /** padding top unit */
    private static final byte[] cellPaddingTopUnit = "clpadft3".getBytes();
    /** padding bottom */
    private static final byte[] cellPaddingBottom = "clpadb".getBytes();
    /** padding bottom unit */
    private static final byte[] cellPaddingBottomUnit = "clpadfb3".getBytes();
    /** padding left */
    private static final byte[] cellPaddingLeft = "clpadl".getBytes();
    /** padding left unit */
    private static final byte[] cellPaddingLeftUnit = "clpadfl3".getBytes();
    /** padding right */
    private static final byte[] cellPaddingRight = "clpadr".getBytes();
    /** padding right unit */
    private static final byte[] cellPaddingRightUnit = "clpadfr3".getBytes();

    /** The <code>RtfWriter</code> to which this <code>RtfCell</code> belongs. */
    private RtfWriter writer = null;
    /** The <code>RtfTable</code> to which this <code>RtfCell</code> belongs. */
    private RtfTable mainTable = null;

    /** Cell width */
    private int cellWidth = 0;
    /** Cell right border position */
    private int cellRight = 0;
    /** <code>Cell</code> containing the actual data */
    private Cell store = null;
    /** Is this an empty cell */
    private boolean emptyCell = true;
    /** Type of merging to do */
    private int mergeType = 0;
    /** cell padding, because the table only renders the left and right cell padding
     * and not the top and bottom one
     */
    private int cellpadding = 0;

    /**
     * Create a new <code>RtfCell</code>.
     *
     * @param writer The <code>RtfWriter</code> that this <code>RtfCell</code> belongs to
     * @param mainTable The <code>RtfTable</code> that created the
     * <code>RtfRow</code> that created the <code>RtfCell</code> :-)
     */
    public RtfCell(RtfWriter writer, RtfTable mainTable) {
        super();
        this.writer = writer;
        this.mainTable = mainTable;
    }

    /**
     * Import a <code>Cell</code>.
     * <P>
     * @param cell The <code>Cell</code> containing the data for this
     * <code>RtfCell</code>
     * @param cellLeft The position of the left border
     * @param cellWidth The default width of a cell
     * @param x The column index of this <code>RtfCell</code>
     * @param y The row index of this <code>RtfCell</code>
     */
    public int importCell(Cell cell, int cellLeft, int cellWidth, int x, int y, int cellpadding) {
        this.cellpadding = cellpadding;

        // set this value in any case
        this.cellWidth = cellWidth;
        if (cell == null) {
            cellRight = cellLeft + cellWidth;
            return cellRight;
        }
        if (cell.cellWidth() != null && !cell.cellWidth().equals("")) {

            this.cellWidth = (int) (Integer.parseInt(cell.cellWidth()) * RtfWriter.TWIPSFACTOR);
        }
        cellRight = cellLeft + this.cellWidth;
        store = cell;
        emptyCell = false;
        if (cell.colspan() > 1) {
            if (cell.rowspan() > 1) {
                mergeType = MERGE_BOTH_FIRST;
                for (int i = y; i < y + cell.rowspan(); i++) {
                    if (i > y) mainTable.setMerge(x, i, MERGE_VERT_PREV, this);
                    for (int j = x + 1; j < x + cell.colspan(); j++) {
                        mainTable.setMerge(j, i, MERGE_BOTH_PREV, this);
                    }
                }
            } else {
                mergeType = MERGE_HORIZ_FIRST;
                for (int i = x + 1; i < x + cell.colspan(); i++) {
                    mainTable.setMerge(i, y, MERGE_HORIZ_PREV, this);
                }
            }
        } else if (cell.rowspan() > 1) {
            mergeType = MERGE_VERT_FIRST;
            for (int i = y + 1; i < y + cell.rowspan(); i++) {
                mainTable.setMerge(x, i, MERGE_VERT_PREV, this);
            }
        }
        return cellRight;
    }

    /**
     * Write the properties of the <code>RtfCell</code>.
     *
     * @param os The <code>OutputStream</code> to which to write the properties
     * of the <code>RtfCell</code> to.
     */
    public boolean writeCellSettings(ByteArrayOutputStream os) throws DocumentException {
        try {
            float lWidth, tWidth, rWidth, bWidth;
            byte[] lStyle, tStyle, rStyle, bStyle;

            if (store instanceof RtfTableCell) {
                RtfTableCell c = (RtfTableCell) store;
                lWidth = c.leftBorderWidth();
                tWidth = c.topBorderWidth();
                rWidth = c.rightBorderWidth();
                bWidth = c.bottomBorderWidth();
                lStyle = RtfTableCell.getStyleControlWord(c.leftBorderStyle());
                tStyle = RtfTableCell.getStyleControlWord(c.topBorderStyle());
                rStyle = RtfTableCell.getStyleControlWord(c.rightBorderStyle());
                bStyle = RtfTableCell.getStyleControlWord(c.bottomBorderStyle());
            } else {
                lWidth = tWidth = rWidth = bWidth = store.borderWidth();
                lStyle = tStyle = rStyle = bStyle = RtfRow.tableBorder;
            }

            if (mergeType == MERGE_HORIZ_PREV || mergeType == MERGE_BOTH_PREV) {
                return true;
            }
            switch (mergeType) {
                case MERGE_VERT_FIRST:
                    os.write(RtfWriter.escape);
                    os.write(cellVMergeFirst);
                    break;
                case MERGE_BOTH_FIRST:
                    os.write(RtfWriter.escape);
                    os.write(cellVMergeFirst);
                    break;
                case MERGE_HORIZ_PREV:
                    os.write(RtfWriter.escape);
                    os.write(cellMergePrev);
                    break;
                case MERGE_VERT_PREV:
                    os.write(RtfWriter.escape);
                    os.write(cellVMergePrev);
                    break;
                case MERGE_BOTH_PREV:
                    os.write(RtfWriter.escape);
                    os.write(cellMergeFirst);
                    break;
            }
            switch (store.verticalAlignment()) {
                case Element.ALIGN_BOTTOM:
                    os.write(RtfWriter.escape);
                    os.write(cellVerticalAlignBottom);
                    break;
                case Element.ALIGN_CENTER:
                case Element.ALIGN_MIDDLE:
                    os.write(RtfWriter.escape);
                    os.write(cellVerticalAlignCenter);
                    break;
                case Element.ALIGN_TOP:
                    os.write(RtfWriter.escape);
                    os.write(cellVerticalAlignTop);
                    break;
            }

            if (((store.border() & Rectangle.LEFT) == Rectangle.LEFT) &&
                    (lWidth > 0)) {
                os.write(RtfWriter.escape);
                os.write(cellBorderLeft);
                os.write(RtfWriter.escape);
                os.write(lStyle);
                os.write(RtfWriter.escape);
                os.write(RtfRow.tableBorderWidth);
                writeInt(os, (int) (lWidth * RtfWriter.TWIPSFACTOR));
                os.write(RtfWriter.escape);
                os.write(RtfRow.tableBorderColor);
                if (store.borderColor() == null)
                    writeInt(os, writer.addColor(new
                            Color(0, 0, 0)));
                else
                    writeInt(os, writer.addColor(store.borderColor()));
                os.write((byte) '\n');
            }
            if (((store.border() & Rectangle.TOP) == Rectangle.TOP) && (tWidth > 0)) {
                os.write(RtfWriter.escape);
                os.write(cellBorderTop);
                os.write(RtfWriter.escape);
                os.write(tStyle);
                os.write(RtfWriter.escape);
                os.write(RtfRow.tableBorderWidth);
                writeInt(os, (int) (tWidth * RtfWriter.TWIPSFACTOR));
                os.write(RtfWriter.escape);
                os.write(RtfRow.tableBorderColor);
                if (store.borderColor() == null)
                    writeInt(os, writer.addColor(new
                            Color(0, 0, 0)));
                else
                    writeInt(os, writer.addColor(store.borderColor()));
                os.write((byte) '\n');
            }
            if (((store.border() & Rectangle.BOTTOM) == Rectangle.BOTTOM) &&
                    (bWidth > 0)) {
                os.write(RtfWriter.escape);
                os.write(cellBorderBottom);
                os.write(RtfWriter.escape);
                os.write(bStyle);
                os.write(RtfWriter.escape);
                os.write(RtfRow.tableBorderWidth);
                writeInt(os, (int) (bWidth * RtfWriter.TWIPSFACTOR));
                os.write(RtfWriter.escape);
                os.write(RtfRow.tableBorderColor);
                if (store.borderColor() == null)
                    writeInt(os, writer.addColor(new
                            Color(0, 0, 0)));
                else
                    writeInt(os, writer.addColor(store.borderColor()));
                os.write((byte) '\n');
            }
            if (((store.border() & Rectangle.RIGHT) == Rectangle.RIGHT) &&
                    (rWidth > 0)) {
                os.write(RtfWriter.escape);
                os.write(cellBorderRight);
                os.write(RtfWriter.escape);
                os.write(rStyle);
                os.write(RtfWriter.escape);
                os.write(RtfRow.tableBorderWidth);
                writeInt(os, (int) (rWidth * RtfWriter.TWIPSFACTOR));
                os.write(RtfWriter.escape);
                os.write(RtfRow.tableBorderColor);
                if (store.borderColor() == null)
                    writeInt(os, writer.addColor(new
                            Color(0, 0, 0)));
                else
                    writeInt(os, writer.addColor(store.borderColor()));
                os.write((byte) '\n');
            }
            os.write(RtfWriter.escape);
            os.write(cellBackgroundColor);
            if ((store.backgroundColor() == null) && (store.grayFill() == 0)) {
                writeInt(os, writer.addColor(new Color(255, 255, 255)));
            } else if (store.backgroundColor() != null) {
                writeInt(os, writer.addColor(store.backgroundColor()));
            } else {
                int shadeColor = (int) (store.grayFill() * 255);
                writeInt(os, writer.addColor(new Color(shadeColor, shadeColor, shadeColor)));
            }
            os.write((byte) '\n');
            os.write(RtfWriter.escape);
            os.write(cellWidthStyle);
            os.write((byte) '\n');
            os.write(RtfWriter.escape);
            os.write(cellWidthTag);
            writeInt(os, cellWidth);
            os.write((byte) '\n');
            if (cellpadding > 0) {
                // values
                os.write(RtfWriter.escape);
                os.write(cellPaddingLeft);
                writeInt(os, cellpadding / 2);
                os.write(RtfWriter.escape);
                os.write(cellPaddingTop);
                writeInt(os, cellpadding / 2);
                os.write(RtfWriter.escape);
                os.write(cellPaddingRight);
                writeInt(os, cellpadding / 2);
                os.write(RtfWriter.escape);
                os.write(cellPaddingBottom);
                writeInt(os, cellpadding / 2);
                // unit
                os.write(RtfWriter.escape);
                os.write(cellPaddingLeftUnit);
                os.write(RtfWriter.escape);
                os.write(cellPaddingTopUnit);
                os.write(RtfWriter.escape);
                os.write(cellPaddingRightUnit);
                os.write(RtfWriter.escape);
                os.write(cellPaddingBottomUnit);
            }
            os.write(RtfWriter.escape);
            os.write(cellRightBorder);
            writeInt(os, cellRight);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Write the content of the <code>RtfCell</code>.
     *
     * @param os The <code>OutputStream</code> to which to write the content of
     * the <code>RtfCell</code> to.
     */
    public boolean writeCellContent(ByteArrayOutputStream os) throws DocumentException {
        try {
            if (mergeType == MERGE_HORIZ_PREV || mergeType == MERGE_BOTH_PREV) {
                return true;
            }

            if (!emptyCell) {
                Iterator cellIterator = store.getElements();
                while (cellIterator.hasNext()) {
                    Element element = (Element) cellIterator.next();

                    // if horizontal alignment is undefined overwrite
                    // with that of enclosing cell
                    if (element instanceof Paragraph && ((Paragraph) element).alignment() == Element.ALIGN_UNDEFINED) {
                        ((Paragraph) element).setAlignment(store.horizontalAlignment());
                    }
                    writer.addElement(element, os);
                    if (element.type() == Element.PARAGRAPH && cellIterator.hasNext()) {
                        os.write(RtfWriter.escape);
                        os.write(RtfWriter.paragraph);
                    }
                }
            } else {
                os.write(RtfWriter.escape);
                os.write(RtfWriter.paragraphDefaults);
                os.write(RtfWriter.escape);
                os.write(cellInTable);
            }
            os.write(RtfWriter.escape);
            os.write(cellEnd);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Sets the merge type and the <code>RtfCell</code> with which this
     * <code>RtfCell</code> is to be merged.
     *
     * @param mergeType The merge type specifies the kind of merge to be applied
     * (MERGE_HORIZ_PREV, MERGE_VERT_PREV, MERGE_BOTH_PREV)
     * @param mergeCell The <code>RtfCell</code> that the cell at x and y is to
     * be merged with
     */
    public void setMerge(int mergeType, RtfCell mergeCell) {
        this.mergeType = mergeType;
        store = mergeCell.getStore();
    }

    /**
     * Get the <code>Cell</code> with the actual content.
     *
     * @return <code>Cell</code> which is contained in the <code>RtfCell</code>
     */
    public Cell getStore() {
        return store;
    }

    /**
     * Get the with of this <code>RtfCell</code>
     *
     * @return Width of the current <code>RtfCell</code>
     */
    public int getCellWidth() {
        return cellWidth;
    }


    public void setCellWidth(int value) {
        cellWidth = value;
    }

    /**
     * Get the position of the right border of this <code>RtfCell</code>.
     */
    public int getCellRight() {
        return cellRight;
    }


    public void setCellRight(int value) {
        cellRight = value;
    }

    /*
     * Write an Integer to the Outputstream.
     *
     * @param out The <code>OutputStream</code> to be written to.
     * @param i The int to be written.
     */
    private void writeInt(ByteArrayOutputStream out, int i) throws IOException {
        out.write(Integer.toString(i).getBytes());
    }
}

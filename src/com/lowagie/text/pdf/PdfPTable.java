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

import java.util.ArrayList;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.ElementListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.BadElementException;

/**
 * This is a table that can be put at an absolute position.
 *
 * @author Paulo Soares (psoares@consiste.pt)
 */

public class PdfPTable implements Element{
    
    public static final int BASECANVAS = 0;
    public static final int BACKGROUNDCANVAS = 1;
    public static final int LINECANVAS = 2;
    public static final int TEXTCANVAS = 3;
    
    protected ArrayList rows = new ArrayList();
    protected float totalHeight = 0;
    protected PdfPCell currentRow[];
    protected int currentRowIdx = 0;
    protected PdfPCell defaultCell = new PdfPCell((Phrase)null);
    protected float totalWidth = 0;
    protected float relativeWidths[];
    protected float absoluteWidths[];
    protected PdfPTableEvent tableEvent;
    
/** Holds value of property headerRows. */
    protected int headerRows;
    
/** Holds value of property widthPercentage. */
    protected float widthPercentage = 80;
    
/** Holds value of property horizontalAlignment. */
    private int horizontalAlignment = Element.ALIGN_CENTER;
    
    public PdfPTable(float relativeWidths[]) {
        if (relativeWidths == null)
            throw new NullPointerException("The widths array in PdfPTable constructor can not be null.");
        if (relativeWidths.length == 0)
            throw new IllegalArgumentException("The widths array in PdfPTable constructor can not have zero length.");
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
    }
    
    public PdfPTable(int numColumns) {
        if (numColumns <= 0)
            throw new IllegalArgumentException("The number of columns in PdfPTable constructor must be greater than zero.");
        relativeWidths = new float[numColumns];
        for (int k = 0; k < numColumns; ++k)
            relativeWidths[k] = 1;
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
    }
    
    public PdfPTable(PdfPTable table) {
        relativeWidths = new float[table.relativeWidths.length];
        absoluteWidths = new float[table.relativeWidths.length];
        System.arraycopy(table.relativeWidths, 0, relativeWidths, 0, relativeWidths.length);
        System.arraycopy(table.absoluteWidths, 0, absoluteWidths, 0, relativeWidths.length);
        totalWidth = table.totalWidth;
        totalHeight = table.totalHeight;
        currentRowIdx = table.currentRowIdx;
        tableEvent = table.tableEvent;
        defaultCell = new PdfPCell(table.defaultCell);
        currentRow = new PdfPCell[table.currentRow.length];
        for (int k = 0; k < currentRow.length; ++k) {
            if (table.currentRow[k] == null)
                break;
            currentRow[k] = new PdfPCell(table.currentRow[k]);
        }
        for (int k = 0; k < table.rows.size(); ++k) {
            rows.add(new PdfPRow((PdfPRow)(table.rows.get(k))));
        }
    }
    
    public void setWidths(float relativeWidths[]) throws DocumentException {
        if (relativeWidths.length != this.relativeWidths.length)
            throw new DocumentException("Wrong number of columns.");
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        absoluteWidths = new float[relativeWidths.length];
        totalHeight = 0;
        calculateWidths();
        calculateHeights();
    }

    public void setWidths(int relativeWidths[]) throws DocumentException {
        float tb[] = new float[relativeWidths.length];
        for (int k = 0; k < relativeWidths.length; ++k)
            tb[k] = relativeWidths[k];
        setWidths(tb);
    }

    private void calculateWidths() {
        if (totalWidth <= 0)
            return;
        float total = 0;
        for (int k = 0; k < absoluteWidths.length; ++k) {
            total += relativeWidths[k];
        }
        for (int k = 0; k < absoluteWidths.length; ++k) {
            absoluteWidths[k] = totalWidth * relativeWidths[k] / total;
        }
    }
    
    public void setTotalWidth(float totalWidth) {
        if (this.totalWidth == totalWidth)
            return;
        this.totalWidth = totalWidth;
        totalHeight = 0;
        calculateWidths();
        calculateHeights();
    }

    public float getTotalWidth() {
        return totalWidth;
    }

    public void calculateHeights() {
        if (totalWidth <= 0)
            return;
        totalHeight = 0;
        for (int k = 0; k < rows.size(); ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            row.setWidths(absoluteWidths);
            totalHeight += row.getMaxHeights();
        }
    }
    
    public PdfPCell getDefaultCell() {
        return defaultCell;
    }
    
    public void addCell(PdfPCell cell) {
        PdfPCell ncell = new PdfPCell(cell);
        currentRow[currentRowIdx++] = ncell;
        if (currentRowIdx >= currentRow.length) {
            PdfPRow row = new PdfPRow(currentRow);
            if (totalWidth > 0) {
                row.setWidths(absoluteWidths);
                totalHeight += row.getMaxHeights();
            }
            rows.add(row);
            currentRow = new PdfPCell[absoluteWidths.length];
            currentRowIdx = 0;
        }
    }
    
    public void addCell(String text) {
        addCell(new Phrase(text));
    }
    
    public void addCell(PdfPTable table) {
        defaultCell.setTable(table);
        addCell(defaultCell);
        defaultCell.setTable(null);
    }
    
    public void addCell(Phrase phrase) {
        defaultCell.setPhrase(phrase);
        addCell(defaultCell);
        defaultCell.setPhrase(null);
    }
    
    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte[] canvases) {
        if (totalWidth <= 0)
            throw new RuntimeException("The width must be greater than zero.");
        int size = rows.size();
        if (rowEnd < 0)
            rowEnd = size;
        if (rowStart >= size || rowStart >= rowEnd)
            return yPos;
        rowEnd = Math.min(rowEnd, size);
        float yPosStart = yPos;
        for (int k = rowStart; k < rowEnd; ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            row.writeCells(xPos, yPos, canvases);
            yPos -= row.getMaxHeights();
        }
        if (tableEvent != null) {
            float heights[] = new float[rowEnd - rowStart + 1];
            heights[0] = yPosStart;
            for (int k = rowStart; k < rowEnd; ++k) {
                PdfPRow row = (PdfPRow)rows.get(k);
                heights[k - rowStart + 1] = heights[k - rowStart] - row.getMaxHeights();
            }
            float widths[] = new float[absoluteWidths.length + 1];
            widths[0] = xPos;
            for (int k = 0; k < absoluteWidths.length; ++k)
                widths[k + 1] = widths[k] + absoluteWidths[k];
            tableEvent.tableLayout(this, widths, heights, 0, rowStart, canvases);
        }
        return yPos;
    }
    
    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        PdfContentByte[] canvases = beginWritingRows(canvas);
        float y = writeSelectedRows(rowStart, rowEnd, xPos, yPos, canvases);
        endWritingRows(canvases);
        return y;
    }
    
    public static PdfContentByte[] beginWritingRows(PdfContentByte canvas) {
        return new PdfContentByte[]{
            canvas,
            canvas.getDuplicate(),
            canvas.getDuplicate(),
            canvas.getDuplicate(),
        };
    }
    
    public static void endWritingRows(PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[BASECANVAS];
        canvas.saveState();
        canvas.add(canvases[BACKGROUNDCANVAS]);
        canvas.restoreState();
        canvas.saveState();
        canvas.setLineCap(2);
        canvas.resetRGBColorStroke();
        canvas.add(canvases[LINECANVAS]);
        canvas.restoreState();
        canvas.add(canvases[TEXTCANVAS]);
    }
    
    public int size() {
        return rows.size();
    }
    
    public float getTotalHeight() {
        return totalHeight;
    }
    
    public float getRowHeight(int idx) {
        if (totalWidth <= 0 || idx < 0 || idx >= rows.size())
            return 0;
        PdfPRow row = (PdfPRow)rows.get(idx);
        return row.getMaxHeights();
    }
    
    public float getHeaderHeight() {
        float total = 0;
        int size = Math.min(rows.size(), headerRows);
        for (int k = 0; k < size; ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            total += row.getMaxHeights();
        }
        return total;
    }
    
    public boolean deleteRow(int rowNumber) {
        if (rowNumber < 0 || rowNumber >= rows.size()) {
            return false;
        }
        if (totalWidth > 0) {
            PdfPRow row = (PdfPRow)rows.get(rowNumber);
            totalHeight -= row.getMaxHeights();
        }
        rows.remove(rowNumber);
        return true;
    }
    
    public boolean deleteLastRow() {
        return deleteRow(rows.size() - 1);
    }
    
    /** Getter for property headerRows.
     * @return Value of property headerRows.
     */
    public int getHeaderRows() {
        return headerRows;
    }
    
    /** Setter for property headerRows.
     * @param headerRows New value of property headerRows.
     */
    public void setHeaderRows(int headerRows) {
        if (headerRows < 0)
            headerRows = 0;
        this.headerRows = headerRows;
    }
    
    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public ArrayList getChunks() {
        return new ArrayList();
    }
    
    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    public int type() {
        return Element.PTABLE;
    }
    
    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener	an <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
    /** Getter for property widthPercentage.
     * @return Value of property widthPercentage.
     */
    public float getWidthPercentage() {
        return widthPercentage;
    }
    
    /** Setter for property widthPercentage.
     * @param widthPercentage New value of property widthPercentage.
     */
    public void setWidthPercentage(float widthPercentage) {
        this.widthPercentage = widthPercentage;
    }
    
    /** Getter for property horizontalAlignment.
     * @return Value of property horizontalAlignment.
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }
    
    /** Setter for property horizontalAlignment.
     * @param horizontalAlignment New value of property horizontalAlignment.
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }
    
    public void setTableEvent(PdfPTableEvent event) {
        tableEvent = event;
    }
    
    public PdfPTableEvent getTableEvent() {
        return tableEvent;
    }
    
    public float[] getAbsoluteWidths() {
        return absoluteWidths;
    }
}

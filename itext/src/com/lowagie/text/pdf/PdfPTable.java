/*
 * PdfPTable.java
 *
 * Created on June 30, 2001, 6:44 PM
 */

package com.lowagie.text.pdf;

import java.util.ArrayList;
import com.lowagie.text.Phrase;
import com.lowagie.text.Element;
import com.lowagie.text.ElementListener;
import com.lowagie.text.DocumentException;
/**
 *
 * @author  Administrator
 * @version
 */
public class PdfPTable implements Element{
    
    protected ArrayList rows = new ArrayList();
    protected float totalHeight = 0;
    protected PdfPCell currentRow[];
    protected int currentRowIdx = 0;
    protected PdfPCell defaultCell = new PdfPCell((Phrase)null);
    protected float totalWidth = 0;
    protected float relativeWidths[];
    protected float absoluteWidths[];
    
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
    
    public void add(PdfPCell cell) {
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
    
    public float writeRows(float xPos, float yPos, PdfContentByte canvas) {
        if (totalWidth <= 0)
            throw new RuntimeException("The width must be greater than zero.");
        return writeSelectedRows(0, -1, xPos, yPos, canvas);
    }
    
    public float writeRows(float xPos, float yPos, PdfContentByte lines, PdfContentByte backgr, PdfContentByte text) {
        if (totalWidth <= 0)
            throw new RuntimeException("The width must be greater than zero.");
        return writeSelectedRows(0, -1, xPos, yPos, lines, backgr, text);
    }
    
    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte lines, PdfContentByte backgr, PdfContentByte text) {
        if (totalWidth <= 0)
            throw new RuntimeException("The width must be greater than zero.");
        int size = rows.size();
        if (rowEnd < 0)
            rowEnd = size;
        if (rowStart >= size || rowStart >= rowEnd)
            return yPos;
        rowEnd = Math.min(rowEnd, size);
        for (int k = rowStart; k < rowEnd; ++k) {
            PdfPRow row = (PdfPRow)rows.get(k);
            row.writeCells(xPos, yPos, lines, backgr, text);
            yPos -= row.getMaxHeights();
        }
        return yPos;
    }
    
    public float writeSelectedRows(int rowStart, int rowEnd, float xPos, float yPos, PdfContentByte canvas) {
        PdfContentByte lines = canvas.getDuplicate();
        PdfContentByte backgr = canvas.getDuplicate();
        PdfContentByte text = canvas.getDuplicate();
        float y = writeSelectedRows(rowStart, rowEnd, xPos, yPos, lines, backgr, text);
        canvas.saveState();
        canvas.add(backgr);
        canvas.restoreState();
        canvas.saveState();
        canvas.setLineCap(2);
        canvas.resetRGBColorStroke();
        canvas.add(lines);
        canvas.restoreState();
        canvas.add(text);
        return y;
    }
    
    public void add(String text) {
        add(new Phrase(text));
    }
    
    public void add(PdfPTable table) {
        defaultCell.setTable(table);
        add(defaultCell);
        defaultCell.setTable(null);
    }
    
    public void add(Phrase phrase) {
        defaultCell.setPhrase(phrase);
        add(defaultCell);
        defaultCell.setPhrase(null);
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
    
    public void deleteLastRow() {
        int size = rows.size();
        if (size > 0) {
            --size;
            if (totalWidth > 0) {
                PdfPRow row = (PdfPRow)rows.get(size);
                totalHeight -= row.getMaxHeights();
            }
            rows.remove(size);
        }
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
     * Gets the content of the text element.
     *
     * @return	a type
     */
    public String toString() {
        return "PdfPTable instance";
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
    
}

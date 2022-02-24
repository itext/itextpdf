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

import com.itextpdf.text.AccessibleElementId;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.Image;
import com.itextpdf.text.LargeElement;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.api.Spaceable;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.events.PdfPTableEventForwarder;
import com.itextpdf.text.pdf.interfaces.IAccessibleElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a table that can be put at an absolute position but can also be added
 * to the document as the class <CODE>Table</CODE>.
 * <p/>
 * A PdfPTableEvent can be associated to the table to do custom drawing when the
 * table is rendered.
 *
 * @author Paulo Soares
 */
public class PdfPTable implements LargeElement, Spaceable, IAccessibleElement {

    private final Logger LOGGER = LoggerFactory.getLogger(PdfPTable.class);
    /**
     * The index of the original <CODE>PdfcontentByte</CODE>.
     */
    public static final int BASECANVAS = 0;

    /**
     * The index of the duplicate <CODE>PdfContentByte</CODE> where the
     * background will be drawn.
     */
    public static final int BACKGROUNDCANVAS = 1;

    /**
     * The index of the duplicate <CODE>PdfContentByte</CODE> where the border
     * lines will be drawn.
     */
    public static final int LINECANVAS = 2;

    /**
     * The index of the duplicate <CODE>PdfContentByte</CODE> where the text
     * will be drawn.
     */
    public static final int TEXTCANVAS = 3;

    protected ArrayList<PdfPRow> rows = new ArrayList<PdfPRow>();
    protected float totalHeight = 0;
    protected PdfPCell currentRow[];
    /**
     * The current column index.
     *
     * @since 5.1.0 renamed from currentRowIdx
     */
    protected int currentColIdx = 0;
    protected PdfPCell defaultCell = new PdfPCell((Phrase) null);
    protected float totalWidth = 0;
    protected float relativeWidths[];
    protected float absoluteWidths[];
    protected PdfPTableEvent tableEvent;

    /**
     * Holds value of property headerRows.
     */
    protected int headerRows;

    /**
     * Holds value of property widthPercentage.
     */
    protected float widthPercentage = 80;

    /**
     * Holds value of property horizontalAlignment.
     */
    private int horizontalAlignment = Element.ALIGN_CENTER;

    /**
     * Holds value of property skipFirstHeader.
     */
    private boolean skipFirstHeader = false;
    /**
     * Holds value of property skipLastFooter.
     *
     * @since 2.1.6
     */
    private boolean skipLastFooter = false;

    protected boolean isColspan = false;

    protected int runDirection = PdfWriter.RUN_DIRECTION_NO_BIDI;

    /**
     * Holds value of property lockedWidth.
     */
    private boolean lockedWidth = false;

    /**
     * Holds value of property splitRows.
     */
    private boolean splitRows = true;

    /**
     * The spacing before the table.
     */
    protected float spacingBefore;

    /**
     * The spacing after the table.
     */
    protected float spacingAfter;

    protected float paddingTop;

    /**
     * Holds value of property extendLastRow.
     */
    private boolean[] extendLastRow = {false, false};

    /**
     * Holds value of property headersInEvent.
     */
    private boolean headersInEvent;

    /**
     * Holds value of property splitLate.
     */
    private boolean splitLate = true;

    /**
     * Defines if the table should be kept on one page if possible
     */
    private boolean keepTogether;

    /**
     * Indicates if the PdfPTable is complete once added to the document.
     *
     * @since iText 2.0.8
     */
    protected boolean complete = true;

    /**
     * Holds value of property footerRows.
     */
    private int footerRows;

    /**
     * Keeps track of the completeness of the current row.
     *
     * @since 2.1.6
     */
    protected boolean rowCompleted = true;

    protected boolean loopCheck = true;
    protected boolean rowsNotChecked = true;

    protected PdfName role = PdfName.TABLE;
    protected HashMap<PdfName, PdfObject> accessibleAttributes = null;
    protected AccessibleElementId id = new AccessibleElementId();
    private PdfPTableHeader header = null;
    private PdfPTableBody body = null;
    private PdfPTableFooter footer = null;

    private int numberOfWrittenRows;

    protected PdfPTable() {
    }

    /**
     * Constructs a <CODE>PdfPTable</CODE> with the relative column widths.
     *
     * @param relativeWidths the relative column widths
     */
    public PdfPTable(final float relativeWidths[]) {
        if (relativeWidths == null) {
            throw new NullPointerException(MessageLocalization.getComposedMessage("the.widths.array.in.pdfptable.constructor.can.not.be.null"));
        }
        if (relativeWidths.length == 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.widths.array.in.pdfptable.constructor.can.not.have.zero.length"));
        }
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
        keepTogether = false;
    }

    /**
     * Constructs a <CODE>PdfPTable</CODE> with <CODE>numColumns</CODE> columns.
     *
     * @param numColumns the number of columns
     */
    public PdfPTable(final int numColumns) {
        if (numColumns <= 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.columns.in.pdfptable.constructor.must.be.greater.than.zero"));
        }
        relativeWidths = new float[numColumns];
        for (int k = 0; k < numColumns; ++k) {
            relativeWidths[k] = 1;
        }
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
        keepTogether = false;
    }

    /**
     * Constructs a copy of a <CODE>PdfPTable</CODE>.
     *
     * @param table the <CODE>PdfPTable</CODE> to be copied
     */
    public PdfPTable(final PdfPTable table) {
        copyFormat(table);
        for (int k = 0; k < currentRow.length; ++k) {
            if (table.currentRow[k] == null) {
                break;
            }
            currentRow[k] = new PdfPCell(table.currentRow[k]);
        }
        for (int k = 0; k < table.rows.size(); ++k) {
            PdfPRow row = table.rows.get(k);
            if (row != null) {
                row = new PdfPRow(row);
            }
            rows.add(row);
        }
    }

    public void init() {
        LOGGER.info("Initialize row and cell heights");

        for (PdfPRow row : getRows()) {
            if (row == null) continue;
            row.calculated = false;
            for (PdfPCell cell : row.getCells()) {
                if (cell == null) continue;
                cell.setCalculatedHeight(0);
            }
        }
        
    }
    
    /**
     * Makes a shallow copy of a table (format without content).
     *
     * @param table
     * @return a shallow copy of the table
     */
    public static PdfPTable shallowCopy(final PdfPTable table) {
        PdfPTable nt = new PdfPTable();
        nt.copyFormat(table);
        return nt;
    }

    /**
     * Copies the format of the sourceTable without copying the content.
     *
     * @param sourceTable
     * @since 2.1.6 private is now protected
     */
    protected void copyFormat(final PdfPTable sourceTable) {
        rowsNotChecked = sourceTable.rowsNotChecked;
        relativeWidths = new float[sourceTable.getNumberOfColumns()];
        absoluteWidths = new float[sourceTable.getNumberOfColumns()];
        System.arraycopy(sourceTable.relativeWidths, 0, relativeWidths, 0, getNumberOfColumns());
        System.arraycopy(sourceTable.absoluteWidths, 0, absoluteWidths, 0, getNumberOfColumns());
        totalWidth = sourceTable.totalWidth;
        totalHeight = sourceTable.totalHeight;
        currentColIdx = 0;
        tableEvent = sourceTable.tableEvent;
        runDirection = sourceTable.runDirection;
        if (sourceTable.defaultCell instanceof PdfPHeaderCell) {
            defaultCell = new PdfPHeaderCell((PdfPHeaderCell) sourceTable.defaultCell);
        } else {
            defaultCell = new PdfPCell(sourceTable.defaultCell);
        }
        currentRow = new PdfPCell[sourceTable.currentRow.length];
        isColspan = sourceTable.isColspan;
        splitRows = sourceTable.splitRows;
        spacingAfter = sourceTable.spacingAfter;
        spacingBefore = sourceTable.spacingBefore;
        headerRows = sourceTable.headerRows;
        footerRows = sourceTable.footerRows;
        lockedWidth = sourceTable.lockedWidth;
        extendLastRow = sourceTable.extendLastRow;
        headersInEvent = sourceTable.headersInEvent;
        widthPercentage = sourceTable.widthPercentage;
        splitLate = sourceTable.splitLate;
        skipFirstHeader = sourceTable.skipFirstHeader;
        skipLastFooter = sourceTable.skipLastFooter;
        horizontalAlignment = sourceTable.horizontalAlignment;
        keepTogether = sourceTable.keepTogether;
        complete = sourceTable.complete;
        loopCheck = sourceTable.loopCheck;
        id = sourceTable.id;
        role = sourceTable.role;
        if (sourceTable.accessibleAttributes != null) {
            accessibleAttributes = new HashMap<PdfName, PdfObject>(sourceTable.accessibleAttributes);
        }
        header = sourceTable.getHeader();
        body = sourceTable.getBody();
        footer = sourceTable.getFooter();
    }

    /**
     * Sets the relative widths of the table.
     *
     * @param relativeWidths the relative widths of the table.
     * @throws DocumentException if the number of widths is different than the
     * number of columns
     */
    public void setWidths(final float relativeWidths[]) throws DocumentException {
        if (relativeWidths.length != getNumberOfColumns()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns"));
        }
        this.relativeWidths = new float[relativeWidths.length];
        System.arraycopy(relativeWidths, 0, this.relativeWidths, 0, relativeWidths.length);
        absoluteWidths = new float[relativeWidths.length];
        totalHeight = 0;
        calculateWidths();
        calculateHeights();
    }

    /**
     * Sets the relative widths of the table.
     *
     * @param relativeWidths the relative widths of the table.
     * @throws DocumentException if the number of widths is different than the
     * number of columns
     */
    public void setWidths(final int relativeWidths[]) throws DocumentException {
        float tb[] = new float[relativeWidths.length];
        for (int k = 0; k < relativeWidths.length; ++k) {
            tb[k] = relativeWidths[k];
        }
        setWidths(tb);
    }

    /**
     * @since 2.1.6 private is now protected
     */
    protected void calculateWidths() {
        if (totalWidth <= 0) {
            return;
        }
        float total = 0;
        int numCols = getNumberOfColumns();
        for (int k = 0; k < numCols; ++k) {
            total += relativeWidths[k];
        }
        for (int k = 0; k < numCols; ++k) {
            absoluteWidths[k] = totalWidth * relativeWidths[k] / total;
        }
    }

    /**
     * Sets the full width of the table.
     *
     * @param totalWidth the full width of the table.
     */
    public void setTotalWidth(final float totalWidth) {
        if (this.totalWidth == totalWidth) {
            return;
        }
        this.totalWidth = totalWidth;
        totalHeight = 0;
        calculateWidths();
        calculateHeights();
    }

    /**
     * Sets the full width of the table from the absolute column width.
     *
     * @param columnWidth the absolute width of each column
     * @throws DocumentException if the number of widths is different than the
     * number of columns
     */
    public void setTotalWidth(final float columnWidth[]) throws DocumentException {
        if (columnWidth.length != getNumberOfColumns()) {
            throw new DocumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns"));
        }
        totalWidth = 0;
        for (int k = 0; k < columnWidth.length; ++k) {
            totalWidth += columnWidth[k];
        }
        setWidths(columnWidth);
    }

    /**
     * Sets the percentage width of the table from the absolute column width. Warning: Don't use this with setLockedWidth(true). These two settings don't mix.
     *
     * @param columnWidth the absolute width of each column
     * @param pageSize the page size
     * @throws DocumentException
     */
    public void setWidthPercentage(final float columnWidth[], final Rectangle pageSize) throws DocumentException {
        if (columnWidth.length != getNumberOfColumns()) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("wrong.number.of.columns"));
        }
        setTotalWidth(columnWidth);
        widthPercentage = totalWidth / (pageSize.getRight() - pageSize.getLeft()) * 100f;
    }

    /**
     * Gets the full width of the table.
     *
     * @return the full width of the table
     */
    public float getTotalWidth() {
        return totalWidth;
    }

    /**
     * Calculates the heights of the table.
     *
     * @return the total height of the table. Note that it will be 0 if you
     * didn't specify the width of the table with setTotalWidth(). and made it
     * public
     */
    public float calculateHeights() {
        if (totalWidth <= 0) {
            return 0;
        }
        totalHeight = 0;
        for (int k = 0; k < rows.size(); ++k) {
            totalHeight += getRowHeight(k, true);
        }
        return totalHeight;
    }

    /**
     * Changes the number of columns. Any existing rows will be deleted.
     *
     * @param newColCount the new number of columns
     * @since 5.0.2
     */
    public void resetColumnCount(final int newColCount) {
        if (newColCount <= 0) {
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("the.number.of.columns.in.pdfptable.constructor.must.be.greater.than.zero"));
        }
        relativeWidths = new float[newColCount];
        for (int k = 0; k < newColCount; ++k) {
            relativeWidths[k] = 1;
        }
        absoluteWidths = new float[relativeWidths.length];
        calculateWidths();
        currentRow = new PdfPCell[absoluteWidths.length];
        totalHeight = 0;
    }

    /**
     * Gets the default <CODE>PdfPCell</CODE> that will be used as reference for
     * all the <CODE>addCell</CODE> methods except
     * <CODE>addCell(PdfPCell)</CODE>.
     *
     * @return default <CODE>PdfPCell</CODE>
     */
    public PdfPCell getDefaultCell() {
        return defaultCell;
    }

    /**
     * Adds a cell element.
     *
     * @param cell the cell element
     */
    public PdfPCell addCell(final PdfPCell cell) {
        rowCompleted = false;
        PdfPCell ncell;
        if (cell instanceof PdfPHeaderCell) {
            ncell = new PdfPHeaderCell((PdfPHeaderCell) cell);
        } else {
            ncell = new PdfPCell(cell);
        }

        int colspan = ncell.getColspan();
        colspan = Math.max(colspan, 1);
        colspan = Math.min(colspan, currentRow.length - currentColIdx);
        ncell.setColspan(colspan);

        if (colspan != 1) {
            isColspan = true;
        }
        int rdir = ncell.getRunDirection();
        if (rdir == PdfWriter.RUN_DIRECTION_NO_BIDI) {
            ncell.setRunDirection(runDirection);
        }

        skipColsWithRowspanAbove();

        boolean cellAdded = false;
        if (currentColIdx < currentRow.length) {
            currentRow[currentColIdx] = ncell;
            currentColIdx += colspan;
            cellAdded = true;
        }

        skipColsWithRowspanAbove();

        while (currentColIdx >= currentRow.length) {
            int numCols = getNumberOfColumns();
            if (runDirection == PdfWriter.RUN_DIRECTION_RTL) {
                PdfPCell rtlRow[] = new PdfPCell[numCols];
                int rev = currentRow.length;
                for (int k = 0; k < currentRow.length; ++k) {
                    PdfPCell rcell = currentRow[k];
                    int cspan = rcell.getColspan();
                    rev -= cspan;
                    rtlRow[rev] = rcell;
                    k += cspan - 1;
                }
                currentRow = rtlRow;
            }
            PdfPRow row = new PdfPRow(currentRow);
            if (totalWidth > 0) {
                row.setWidths(absoluteWidths);
                totalHeight += row.getMaxHeights();
            }
            rows.add(row);
            currentRow = new PdfPCell[numCols];
            currentColIdx = 0;
            skipColsWithRowspanAbove();
            rowCompleted = true;
        }

        if (!cellAdded) {
            currentRow[currentColIdx] = ncell;
            currentColIdx += colspan;
        }
        return ncell;
    }

    /**
     * When updating the row index, cells with rowspan should be taken into
     * account. This is what happens in this method.
     *
     * @since 2.1.6
     */
    private void skipColsWithRowspanAbove() {
        int direction = 1;
        if (runDirection == PdfWriter.RUN_DIRECTION_RTL) {
            direction = -1;
        }
        while (rowSpanAbove(rows.size(), currentColIdx)) {
            currentColIdx += direction;
        }
    }

    /**
     * Added by timmo3. This will return the correct cell taking it's cellspan
     * into account
     *
     * @param row the row index
     * @param col the column index
     * @return PdfPCell at the given row and position or null otherwise
     */
    PdfPCell cellAt(final int row, final int col) {
        PdfPCell[] cells = rows.get(row).getCells();
        for (int i = 0; i < cells.length; i++) {
            if (cells[i] != null) {
                if (col >= i && col < (i + cells[i].getColspan())) {
                    return cells[i];
                }
            }
        }
        return null;
    }

    /**
     * Checks if there are rows above belonging to a rowspan.
     *
     * @param currRow the current row to check
     * @param currCol the current column to check
     * @return true if there's a cell above that belongs to a rowspan
     * @since 2.1.6
     */
    boolean rowSpanAbove(final int currRow, final int currCol) {
        if (currCol >= getNumberOfColumns()
                || currCol < 0
                || currRow < 1) {
            return false;
        }
        int row = currRow - 1;
        PdfPRow aboveRow = rows.get(row);
        if (aboveRow == null) {
            return false;
        }
        PdfPCell aboveCell = cellAt(row, currCol);
        while (aboveCell == null && row > 0) {
            aboveRow = rows.get(--row);
            if (aboveRow == null) {
                return false;
            }
            aboveCell = cellAt(row, currCol);
        }

        int distance = currRow - row;

        if (aboveCell.getRowspan() == 1 && distance > 1) {
            int col = currCol - 1;
            aboveRow = rows.get(row + 1);
            distance--;
            aboveCell = aboveRow.getCells()[col];
            while (aboveCell == null && col > 0) {
                aboveCell = aboveRow.getCells()[--col];
            }
        }

        return aboveCell != null && aboveCell.getRowspan() > distance;
    }

    /**
     * Adds a cell element.
     *
     * @param text the text for the cell
     */
    public void addCell(final String text) {
        addCell(new Phrase(text));
    }

    /**
     * Adds a nested table.
     *
     * @param table the table to be added to the cell
     */
    public void addCell(final PdfPTable table) {
        defaultCell.setTable(table);
        PdfPCell newCell = addCell(defaultCell);
        newCell.id = new AccessibleElementId();
        defaultCell.setTable(null);
    }

    /**
     * Adds an Image as Cell.
     *
     * @param image the <CODE>Image</CODE> to add to the table. This image will
     * fit in the cell
     */
    public void addCell(final Image image) {
        defaultCell.setImage(image);
        PdfPCell newCell = addCell(defaultCell);
        newCell.id = new AccessibleElementId();
        defaultCell.setImage(null);
    }

    /**
     * Adds a cell element.
     *
     * @param phrase the <CODE>Phrase</CODE> to be added to the cell
     */
    public void addCell(final Phrase phrase) {
        defaultCell.setPhrase(phrase);
        PdfPCell newCell = addCell(defaultCell);
        newCell.id = new AccessibleElementId();
        defaultCell.setPhrase(null);
    }

    /**
     * Writes the selected rows to the document. <CODE>canvases</CODE> is
     * obtained from <CODE>beginWritingRows()</CODE>.
     *
     * @param rowStart the first row to be written, zero index
     * @param rowEnd the last row to be written + 1. If it is -1 all the rows to
     * the end are written
     * @param xPos the x write coordinate
     * @param yPos the y write coordinate
     * @param canvases an array of 4 <CODE>PdfContentByte</CODE> obtained from
     * <CODE>beginWrittingRows()</CODE>
     * @return the y coordinate position of the bottom of the last row
     * @see #beginWritingRows(com.itextpdf.text.pdf.PdfContentByte)
     */
    public float writeSelectedRows(final int rowStart, final int rowEnd, final float xPos, final float yPos, final PdfContentByte[] canvases) {
        return writeSelectedRows(0, -1, rowStart, rowEnd, xPos, yPos, canvases);
    }

    /**
     * Writes the selected rows and columns to the document. This method does
     * not clip the columns; this is only important if there are columns with
     * colspan at boundaries. <CODE>canvases</CODE> is obtained from
     * <CODE>beginWritingRows()</CODE>. The table event is only fired for
     * complete rows.
     *
     * @param colStart the first column to be written, zero index
     * @param colEnd the last column to be written + 1. If it is -1 all the
     * columns to the end are written
     * @param rowStart the first row to be written, zero index
     * @param rowEnd the last row to be written + 1. If it is -1 all the rows to
     * the end are written
     * @param xPos the x write coordinate
     * @param yPos the y write coordinate
     * @param canvases an array of 4 <CODE>PdfContentByte</CODE> obtained from
     * <CODE>beginWritingRows()</CODE>
     * @return the y coordinate position of the bottom of the last row
     * @see #beginWritingRows(com.itextpdf.text.pdf.PdfContentByte)
     */
    public float writeSelectedRows(final int colStart, final int colEnd, final int rowStart, final int rowEnd, final float xPos, final float yPos, final PdfContentByte[] canvases) {
        return writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases, true);
    }

    /**
     * Writes the selected rows and columns to the document. This method does
     * not clip the columns; this is only important if there are columns with
     * colspan at boundaries. <CODE>canvases</CODE> is obtained from
     * <CODE>beginWritingRows()</CODE>. The table event is only fired for
     * complete rows.
     *
     * @param colStart the first column to be written, zero index
     * @param colEnd the last column to be written + 1. If it is -1 all the
     * columns to the end are written
     * @param rowStart the first row to be written, zero index
     * @param rowEnd the last row to be written + 1. If it is -1 all the rows to
     * the end are written
     * @param xPos the x write coordinate
     * @param yPos the y write coordinate
     * @param canvases an array of 4 <CODE>PdfContentByte</CODE> obtained from
     * <CODE>beginWritingRows()</CODE>
     * @return the y coordinate position of the bottom of the last row
     * @param reusable if set to false, the content in the cells is "consumed";
     * if true, you can reuse the cells, the row, the parent table as many times
     * you want.
     * @see #beginWritingRows(com.itextpdf.text.pdf.PdfContentByte)
     * @since 5.1.0 added the reusable parameter
     */
    public float writeSelectedRows(int colStart, int colEnd, int rowStart, int rowEnd, final float xPos, float yPos, final PdfContentByte[] canvases, final boolean reusable) {
        if (totalWidth <= 0) {
            throw new RuntimeException(MessageLocalization.getComposedMessage("the.table.width.must.be.greater.than.zero"));
        }

        int totalRows = rows.size();
        if (rowStart < 0) {
            rowStart = 0;
        }
        if (rowEnd < 0) {
            rowEnd = totalRows;
        } else {
            rowEnd = Math.min(rowEnd, totalRows);
        }
        if (rowStart >= rowEnd) {
            return yPos;
        }

        int totalCols = getNumberOfColumns();
        if (colStart < 0) {
            colStart = 0;
        } else {
            colStart = Math.min(colStart, totalCols);
        }
        if (colEnd < 0) {
            colEnd = totalCols;
        } else {
            colEnd = Math.min(colEnd, totalCols);
        }

        if (LOGGER.isLogging(Level.INFO)) {
            LOGGER.info(String.format("Writing row %s to %s; column %s to %s", rowStart, rowEnd, colStart, colEnd));
        }

        float yPosStart = yPos;

        PdfPTableBody currentBlock = null;
        if (rowsNotChecked) {
            getFittingRows(Float.MAX_VALUE, rowStart);
        }
        List<PdfPRow> rows = getRows(rowStart, rowEnd);
        int k = rowStart;
        for (PdfPRow row : rows) {
            if (getHeader().rows != null && getHeader().rows.contains(row) && currentBlock == null) {
                currentBlock = openTableBlock(getHeader(), canvases[TEXTCANVAS]);
            } else if (getBody().rows != null && getBody().rows.contains(row) && currentBlock == null) {
                currentBlock = openTableBlock(getBody(), canvases[TEXTCANVAS]);
            } else if (getFooter().rows != null && getFooter().rows.contains(row) && currentBlock == null) {
                currentBlock = openTableBlock(getFooter(), canvases[TEXTCANVAS]);
            }
            if (row != null) {
                row.writeCells(colStart, colEnd, xPos, yPos, canvases, reusable);
                yPos -= row.getMaxHeights();
            }
            if (getHeader().rows != null && getHeader().rows.contains(row) && (k == rowEnd - 1 || !getHeader().rows.contains(rows.get(k + 1)))) {
                currentBlock = closeTableBlock(getHeader(), canvases[TEXTCANVAS]);
            } else if (getBody().rows != null && getBody().rows.contains(row) && (k == rowEnd - 1 || !getBody().rows.contains(rows.get(k + 1)))) {
                currentBlock = closeTableBlock(getBody(), canvases[TEXTCANVAS]);
            } else if (getFooter().rows != null && getFooter().rows.contains(row) && (k == rowEnd - 1 || !getFooter().rows.contains(rows.get(k + 1)))) {
                currentBlock = closeTableBlock(getFooter(), canvases[TEXTCANVAS]);
            }
            k++;
        }

        if (tableEvent != null && colStart == 0 && colEnd == totalCols) {
            float heights[] = new float[rowEnd - rowStart + 1];
            heights[0] = yPosStart;
            for (k = 0; k < rowEnd-rowStart; ++k) {
                PdfPRow row = rows.get(k);
                float hr = 0;
                if (row != null) {
                    hr = row.getMaxHeights();
                }
                heights[k + 1] = heights[k] - hr;
            }
            tableEvent.tableLayout(this, getEventWidths(xPos, rowStart, rowEnd, headersInEvent), heights, headersInEvent ? headerRows : 0, rowStart, canvases);
        }

        return yPos;
    }

    private PdfPTableBody openTableBlock(PdfPTableBody block, PdfContentByte canvas) {
        if (canvas.writer.getStandardStructElems().contains(block.getRole())) {
            canvas.openMCBlock(block);
            return block;
        }
        return null;
    }

    private PdfPTableBody closeTableBlock(PdfPTableBody block, PdfContentByte canvas) {
        if (canvas.writer.getStandardStructElems().contains(block.getRole())) {
            canvas.closeMCBlock(block);
        }
        return null;
    }

    /**
     * Writes the selected rows to the document.
     *
     * @param rowStart the first row to be written, zero index
     * @param rowEnd the last row to be written + 1. If it is -1 all the rows to
     * the end are written
     * @param xPos the x write coordinate
     * @param yPos the y write coordinate
     * @param canvas the <CODE>PdfContentByte</CODE> where the rows will be
     * written to
     * @return the y coordinate position of the bottom of the last row
     */
    public float writeSelectedRows(final int rowStart, final int rowEnd, final float xPos, final float yPos, final PdfContentByte canvas) {
        return writeSelectedRows(0, -1, rowStart, rowEnd, xPos, yPos, canvas);
    }

    /**
     * Writes the selected rows and columns to the document. This method clips
     * the columns; this is only important if there are columns with colspan at
     * boundaries. The table event is only fired for complete rows.
     *
     * @param colStart the first column to be written, zero index
     * @param colEnd the last column to be written + 1. If it is -1 all the
     * columns to the end are written
     * @param rowStart the first row to be written, zero index
     * @param rowEnd the last row to be written + 1. If it is -1 all the rows to
     * the end are written
     * @param xPos the x write coordinate
     * @param yPos the y write coordinate
     * @param canvas the <CODE>PdfContentByte</CODE> where the rows will be
     * written to
     * @return the y coordinate position of the bottom of the last row
     */
    public float writeSelectedRows(final int colStart, final int colEnd, final int rowStart, final int rowEnd, final float xPos, final float yPos, final PdfContentByte canvas) {
        return writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvas, true);
    }

    /**
     * Writes the selected rows and columns to the document. This method clips
     * the columns; this is only important if there are columns with colspan at
     * boundaries. The table event is only fired for complete rows.
     *
     * @param colStart the first column to be written, zero index
     * @param colEnd the last column to be written + 1. If it is -1 all the
     * columns to the end are written
     * @param rowStart the first row to be written, zero index
     * @param rowEnd the last row to be written + 1. If it is -1 all the rows to
     * the end are written
     * @param xPos the x write coordinate
     * @param yPos the y write coordinate
     * @param canvas the <CODE>PdfContentByte</CODE> where the rows will be
     * written to
     * @return the y coordinate position of the bottom of the last row
     * @param reusable if set to false, the content in the cells is "consumed";
     * if true, you can reuse the cells, the row, the parent table as many times
     * you want.
     * @since 5.1.0 added the reusable parameter
     */
    public float writeSelectedRows(int colStart, int colEnd, final int rowStart, final int rowEnd, final float xPos, final float yPos, final PdfContentByte canvas, final boolean reusable) {
        int totalCols = getNumberOfColumns();
        if (colStart < 0) {
            colStart = 0;
        } else {
            colStart = Math.min(colStart, totalCols);
        }

        if (colEnd < 0) {
            colEnd = totalCols;
        } else {
            colEnd = Math.min(colEnd, totalCols);
        }

        boolean clip = colStart != 0 || colEnd != totalCols;

        if (clip) {
            float w = 0;
            for (int k = colStart; k < colEnd; ++k) {
                w += absoluteWidths[k];
            }
            canvas.saveState();
            float lx = colStart == 0 ? 10000 : 0;
            float rx = colEnd == totalCols ? 10000 : 0;
            canvas.rectangle(xPos - lx, -10000, w + lx + rx, PdfPRow.RIGHT_LIMIT);
            canvas.clip();
            canvas.newPath();
        }

        PdfContentByte[] canvases = beginWritingRows(canvas);
        float y = writeSelectedRows(colStart, colEnd, rowStart, rowEnd, xPos, yPos, canvases, reusable);
        endWritingRows(canvases);

        if (clip) {
            canvas.restoreState();
        }

        return y;
    }

    /**
     * Gets and initializes the 4 layers where the table is written to. The text
     * or graphics are added to one of the 4 <CODE>PdfContentByte</CODE>
     * returned with the following order:
     * <p>
     * <ul>
     * <li><CODE>PdfPtable.BASECANVAS</CODE> - the original
     * <CODE>PdfContentByte</CODE>. Anything placed here will be under the
     * table.
     * <li><CODE>PdfPtable.BACKGROUNDCANVAS</CODE> - the layer where the
     * background goes to.
     * <li><CODE>PdfPtable.LINECANVAS</CODE> - the layer where the lines go to.
     * <li><CODE>PdfPtable.TEXTCANVAS</CODE> - the layer where the text go to.
     * Anything placed here will be over the table.
     * </ul><p>
     * The layers are placed in sequence on top of each other.
     *
     * @param canvas the <CODE>PdfContentByte</CODE> where the rows will be
     * written to
     * @return an array of 4 <CODE>PdfContentByte</CODE>
     * @see #writeSelectedRows(int, int, float, float, PdfContentByte[])
     */
    public static PdfContentByte[] beginWritingRows(final PdfContentByte canvas) {
        return new PdfContentByte[]{
            canvas,
            canvas.getDuplicate(),
            canvas.getDuplicate(),
            canvas.getDuplicate(),};
    }

    /**
     * Finishes writing the table.
     *
     * @param canvases the array returned by <CODE>beginWritingRows()</CODE>
     */
    public static void endWritingRows(final PdfContentByte[] canvases) {
        PdfContentByte canvas = canvases[BASECANVAS];
        PdfArtifact artifact = new PdfArtifact();
        canvas.openMCBlock(artifact);
        canvas.saveState();
        canvas.add(canvases[BACKGROUNDCANVAS]);
        canvas.restoreState();
        canvas.saveState();
        canvas.setLineCap(2);
        canvas.resetRGBColorStroke();
        canvas.add(canvases[LINECANVAS]);
        canvas.restoreState();
        canvas.closeMCBlock(artifact);
        canvas.add(canvases[TEXTCANVAS]);
    }

    /**
     * Gets the number of rows in this table.
     *
     * @return the number of rows in this table
     */
    public int size() {
        return rows.size();
    }

    /**
     * Gets the total height of the table.
     *
     * @return the total height of the table
     */
    public float getTotalHeight() {
        return totalHeight;
    }

    /**
     * Gets the height of a particular row.
     *
     * @param idx the row index (starts at 0)
     * @return the height of a particular row
     */
    public float getRowHeight(final int idx) {
        return getRowHeight(idx, false);
    }

    /**
     * Gets the height of a particular row.
     *
     * @param idx the row index (starts at 0)
     * @param firsttime is this the first time the row heigh is calculated?
     * @return the height of a particular row
     * @since 5.0.0
     */
    protected float getRowHeight(final int idx, final boolean firsttime) {
        if (totalWidth <= 0 || idx < 0 || idx >= rows.size()) {
            return 0;
        }
        PdfPRow row = rows.get(idx);
        if (row == null) {
            return 0;
        }
        if (firsttime) {
            row.setWidths(absoluteWidths);
        }
        float height = row.getMaxHeights();
        PdfPCell cell;
        PdfPRow tmprow;
        for (int i = 0; i < relativeWidths.length; i++) {
            if (!rowSpanAbove(idx, i)) {
                continue;
            }
            int rs = 1;
            while (rowSpanAbove(idx - rs, i)) {
                rs++;
            }
            tmprow = rows.get(idx - rs);
            cell = tmprow.getCells()[i];
            float tmp = 0;
            if (cell != null && cell.getRowspan() == rs + 1) {
                tmp = cell.getMaxHeight();
                while (rs > 0) {
                    tmp -= getRowHeight(idx - rs);
                    rs--;
                }
            }
            if (tmp > height) {
                height = tmp;
            }
        }
        row.setMaxHeights(height);
        return height;
    }

    /**
     * Gets the maximum height of a cell in a particular row (will only be
     * different from getRowHeight is one of the cells in the row has a rowspan
     * > 1).
     *
     * @return the height of a particular row including rowspan
     * @param rowIndex the row index
     * @param cellIndex the cell index
     * @since 2.1.6
     */
    public float getRowspanHeight(final int rowIndex, final int cellIndex) {
        if (totalWidth <= 0 || rowIndex < 0 || rowIndex >= rows.size()) {
            return 0;
        }
        PdfPRow row = rows.get(rowIndex);
        if (row == null || cellIndex >= row.getCells().length) {
            return 0;
        }
        PdfPCell cell = row.getCells()[cellIndex];
        if (cell == null) {
            return 0;
        }
        float rowspanHeight = 0;
        for (int j = 0; j < cell.getRowspan(); j++) {
            rowspanHeight += getRowHeight(rowIndex + j);
        }
        return rowspanHeight;
    }

    /**
     * Checks if a cell in a row has a rowspan greater than 1.
     *
     * @since 5.1.0
     */
    public boolean hasRowspan(final int rowIdx) {
        if (rowIdx < rows.size() && getRow(rowIdx).hasRowspan()) {
            return true;
        }
        PdfPRow previousRow = rowIdx > 0 ? getRow(rowIdx - 1) : null;
        if (previousRow != null && previousRow.hasRowspan()) {
            return true;
        }
        for (int i = 0; i < getNumberOfColumns(); i++) {
            if (rowSpanAbove(rowIdx - 1, i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes sure the footers value is lower than the headers value.
     *
     * @since 5.0.1
     */
    public void normalizeHeadersFooters() {
        if (footerRows > headerRows) {
            footerRows = headerRows;
        }
    }

    /**
     * Gets the height of the rows that constitute the header as defined by
     * <CODE>setHeaderRows()</CODE>.
     *
     * @return the height of the rows that constitute the header and footer
     */
    public float getHeaderHeight() {
        float total = 0;
        int size = Math.min(rows.size(), headerRows);
        for (int k = 0; k < size; ++k) {
            PdfPRow row = rows.get(k);
            if (row != null) {
                total += row.getMaxHeights();
            }
        }
        return total;
    }

    /**
     * Gets the height of the rows that constitute the footer as defined by
     * <CODE>setFooterRows()</CODE>.
     *
     * @return the height of the rows that constitute the footer
     * @since 2.1.1
     */
    public float getFooterHeight() {
        float total = 0;
        int start = Math.max(0, headerRows - footerRows);
        int size = Math.min(rows.size(), headerRows);
        for (int k = start; k < size; ++k) {
            PdfPRow row = rows.get(k);
            if (row != null) {
                total += row.getMaxHeights();
            }
        }
        return total;
    }

    /**
     * Deletes a row from the table.
     *
     * @param rowNumber the row to be deleted
     * @return <CODE>true</CODE> if the row was deleted
     */
    public boolean deleteRow(final int rowNumber) {
        if (rowNumber < 0 || rowNumber >= rows.size()) {
            return false;
        }
        if (totalWidth > 0) {
            PdfPRow row = rows.get(rowNumber);
            if (row != null) {
                totalHeight -= row.getMaxHeights();
            }
        }
        rows.remove(rowNumber);
        if (rowNumber < headerRows) {
            --headerRows;
            if (rowNumber >= headerRows - footerRows) {
                --footerRows;
            }
        }
        return true;
    }

    /**
     * Deletes the last row in the table.
     *
     * @return <CODE>true</CODE> if the last row was deleted
     */
    public boolean deleteLastRow() {
        return deleteRow(rows.size() - 1);
    }

    /**
     * Removes all of the rows except headers
     */
    public void deleteBodyRows() {
        ArrayList<PdfPRow> rows2 = new ArrayList<PdfPRow>();
        for (int k = 0; k < headerRows; ++k) {
            rows2.add(rows.get(k));
        }
        rows = rows2;
        totalHeight = 0;
        if (totalWidth > 0) {
            totalHeight = getHeaderHeight();
        }
    }

    /**
     * Returns the number of columns.
     *
     * @return the number of columns.
     * @since 2.1.1
     */
    public int getNumberOfColumns() {
        return relativeWidths.length;
    }

    /**
     * Gets the number of the rows that constitute the header.
     *
     * @return the number of the rows that constitute the header
     */
    public int getHeaderRows() {
        return headerRows;
    }

    /**
     * Sets the number of the top rows that constitute the header. This header
     * has only meaning if the table is added to <CODE>Document</CODE> and the
     * table crosses pages.
     *
     * @param headerRows the number of the top rows that constitute the header
     */
    public void setHeaderRows(int headerRows) {
        if (headerRows < 0) {
            headerRows = 0;
        }
        this.headerRows = headerRows;
    }

    /**
     * Gets all the chunks in this element.
     *
     * @return an <CODE>ArrayList</CODE>
     */
    public List<Chunk> getChunks() {
        return new ArrayList<Chunk>();
    }

    /**
     * Gets the type of the text element.
     *
     * @return a type
     */
    public int type() {
        return Element.PTABLE;
    }

    /**
     * @since iText 2.0.8
     * @see com.itextpdf.text.Element#isContent()
     */
    public boolean isContent() {
        return true;
    }

    /**
     * @since iText 2.0.8
     * @see com.itextpdf.text.Element#isNestable()
     */
    public boolean isNestable() {
        return true;
    }

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param listener an <CODE>ElementListener</CODE>
     * @return    <CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(final ElementListener listener) {
        try {
            return listener.add(this);
        } catch (DocumentException de) {
            return false;
        }
    }

    public String getSummary() {
        return getAccessibleAttribute(PdfName.SUMMARY).toString();
    }
    
    public void setSummary(final String summary) {
        setAccessibleAttribute(PdfName.SUMMARY, new PdfString(summary));
    }

    /**
     * Gets the width percentage that the table will occupy in the page.
     *
     * @return the width percentage that the table will occupy in the page
     */
    public float getWidthPercentage() {
        return widthPercentage;
    }

    /**
     * Sets the width percentage that the table will occupy in the page.
     *
     * @param widthPercentage the width percentage that the table will occupy in
     * the page
     */
    public void setWidthPercentage(final float widthPercentage) {
        this.widthPercentage = widthPercentage;
    }

    /**
     * Gets the horizontal alignment of the table relative to the page.
     *
     * @return the horizontal alignment of the table relative to the page
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * Sets the horizontal alignment of the table relative to the page. It only
     * has meaning if the width percentage is less than 100%.
     *
     * @param horizontalAlignment the horizontal alignment of the table relative
     * to the page
     */
    public void setHorizontalAlignment(final int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    /**
     * Gets a row with a given index.
     *
     * @param idx
     * @return the row at position idx
     */
    public PdfPRow getRow(final int idx) {
        return rows.get(idx);
    }

    /**
     * Gets an arraylist with all the rows in the table.
     *
     * @return an arraylist
     */
    public ArrayList<PdfPRow> getRows() {
        return rows;
    }

    /**
     * Returns the index of the last completed row.
     *
     * @return the index of a row
     */
    public int getLastCompletedRowIndex() {
        return rows.size() - 1;
    }

    /**
     * Defines where the table may be broken (if necessary).
     *
     * @param breakPoints int[]
     * @throws java.lang.IndexOutOfBoundsException if a row index is passed that
     * is out of bounds
     */
    public void setBreakPoints(int... breakPoints) {
        keepRowsTogether(0, rows.size()); // sets all rows as unbreakable

        for (int i = 0; i < breakPoints.length; i++) {
            getRow(breakPoints[i]).setMayNotBreak(false);
        }
    }

    /**
     * Defines which rows should not allow a page break (if possible).
     *
     * @param rows int[]
     * @throws java.lang.IndexOutOfBoundsException if a row index is passed that
     * is out of bounds
     */
    public void keepRowsTogether(int[] rows) {
        for (int i = 0; i < rows.length; i++) {
            getRow(rows[i]).setMayNotBreak(true);
        }
    }

    /**
     * Defines a range of rows that should not allow a page break (if possible).
     *
     * @param start int
     * @param end int
     * @throws java.lang.IndexOutOfBoundsException if a row index is passed that
     * is out of bounds
     */
    public void keepRowsTogether(int start, int end) {
        if (start < end) {
            while (start < end) {
                getRow(start).setMayNotBreak(true);
                start++;
            }
        }
    }

    /**
     * Defines a range of rows (from the parameter to the last row) that should
     * not allow a page break (if possible). The equivalent of calling
     * {@link #keepRowsTogether(int,int) keepRowsTogether(start, rows.size()}.
     *
     * @param start int
     * @throws java.lang.IndexOutOfBoundsException if a row index is passed that
     * is out of bounds
     */
    public void keepRowsTogether(int start) {
        keepRowsTogether(start, rows.size());
    }

    /**
     * Gets an arraylist with a selection of rows.
     *
     * @param start the first row in the selection
     * @param end the first row that isn't part of the selection
     * @return a selection of rows
     * @since 2.1.6
     */
    public ArrayList<PdfPRow> getRows(final int start, final int end) {
        ArrayList<PdfPRow> list = new ArrayList<PdfPRow>();
        if (start < 0 || end > size()) {
            return list;
        }
        for (int i = start; i < end; i++) {
            list.add(adjustCellsInRow(i, end));
        }
        return list;
    }

    /**
     * Calculates the extra height needed in a row because of rowspans.
     *
     * @param start the index of the start row (the one to adjust)
     * @param end the index of the end row on the page
     * @since 2.1.6
     */
    protected PdfPRow adjustCellsInRow(final int start, final int end) {
        PdfPRow row = getRow(start);
        if (row.isAdjusted()) {
            return row;
        }
        row = new PdfPRow(row);
        PdfPCell cell;
        PdfPCell[] cells = row.getCells();
        for (int i = 0; i < cells.length; i++) {
            cell = cells[i];
            if (cell == null || cell.getRowspan() == 1) {
                continue;
            }
            int stop = Math.min(end, start + cell.getRowspan());
            float extra = 0;
            for (int k = start + 1; k < stop; k++) {
                extra += getRow(k).getMaxHeights();
            }
            row.setExtraHeight(i, extra);
        }
        row.setAdjusted(true);
        return row;
    }

    /**
     * Sets the table event for this table.
     *
     * @param event the table event for this table
     */
    public void setTableEvent(final PdfPTableEvent event) {
        if (event == null) {
            this.tableEvent = null;
        } else if (this.tableEvent == null) {
            this.tableEvent = event;
        } else if (this.tableEvent instanceof PdfPTableEventForwarder) {
            ((PdfPTableEventForwarder) this.tableEvent).addTableEvent(event);
        } else {
            PdfPTableEventForwarder forward = new PdfPTableEventForwarder();
            forward.addTableEvent(this.tableEvent);
            forward.addTableEvent(event);
            this.tableEvent = forward;
        }
    }

    /**
     * Gets the table event for this page.
     *
     * @return the table event for this page
     */
    public PdfPTableEvent getTableEvent() {
        return tableEvent;
    }

    /**
     * Gets the absolute sizes of each column width.
     *
     * @return he absolute sizes of each column width
     */
    public float[] getAbsoluteWidths() {
        return absoluteWidths;
    }

    float[][] getEventWidths(final float xPos, int firstRow, int lastRow, final boolean includeHeaders) {
        if (includeHeaders) {
            firstRow = Math.max(firstRow, headerRows);
            lastRow = Math.max(lastRow, headerRows);
        }
        float widths[][] = new float[(includeHeaders ? headerRows : 0) + lastRow - firstRow][];
        if (isColspan) {
            int n = 0;
            if (includeHeaders) {
                for (int k = 0; k < headerRows; ++k) {
                    PdfPRow row = rows.get(k);
                    if (row == null) {
                        ++n;
                    } else {
                        widths[n++] = row.getEventWidth(xPos, absoluteWidths);
                    }
                }
            }
            for (; firstRow < lastRow; ++firstRow) {
                PdfPRow row = rows.get(firstRow);
                if (row == null) {
                    ++n;
                } else {
                    widths[n++] = row.getEventWidth(xPos, absoluteWidths);
                }
            }
        } else {
            int numCols = getNumberOfColumns();
            float width[] = new float[numCols + 1];
            width[0] = xPos;
            for (int k = 0; k < numCols; ++k) {
                width[k + 1] = width[k] + absoluteWidths[k];
            }
            for (int k = 0; k < widths.length; ++k) {
                widths[k] = width;
            }
        }
        return widths;
    }

    /**
     * Tells you if the first header needs to be skipped (for instance if the
     * header says "continued from the previous page").
     *
     * @return Value of property skipFirstHeader.
     */
    public boolean isSkipFirstHeader() {
        return skipFirstHeader;
    }

    /**
     * Tells you if the last footer needs to be skipped (for instance if the
     * footer says "continued on the next page")
     *
     * @return Value of property skipLastFooter.
     * @since 2.1.6
     */
    public boolean isSkipLastFooter() {
        return skipLastFooter;
    }

    /**
     * Skips the printing of the first header. Used when printing tables in
     * succession belonging to the same printed table aspect.
     *
     * @param skipFirstHeader New value of property skipFirstHeader.
     */
    public void setSkipFirstHeader(final boolean skipFirstHeader) {
        this.skipFirstHeader = skipFirstHeader;
    }

    /**
     * Skips the printing of the last footer. Used when printing tables in
     * succession belonging to the same printed table aspect.
     *
     * @param skipLastFooter New value of property skipLastFooter.
     * @since 2.1.6
     */
    public void setSkipLastFooter(final boolean skipLastFooter) {
        this.skipLastFooter = skipLastFooter;
    }

    /**
     * Sets the run direction of the contents of the table.
     *
     * @param runDirection One of the following values:
     * PdfWriter.RUN_DIRECTION_DEFAULT, PdfWriter.RUN_DIRECTION_NO_BIDI,
     * PdfWriter.RUN_DIRECTION_LTR or PdfWriter.RUN_DIRECTION_RTL.
     */
    public void setRunDirection(final int runDirection) {
        switch (runDirection) {
            case PdfWriter.RUN_DIRECTION_DEFAULT:
            case PdfWriter.RUN_DIRECTION_NO_BIDI:
            case PdfWriter.RUN_DIRECTION_LTR:
            case PdfWriter.RUN_DIRECTION_RTL:
                this.runDirection = runDirection;
                break;
            default:
                throw new RuntimeException(MessageLocalization.getComposedMessage("invalid.run.direction.1", runDirection));
        }
    }

    /**
     * Returns the run direction of the contents in the table.
     *
     * @return One of the following values: PdfWriter.RUN_DIRECTION_DEFAULT,
     * PdfWriter.RUN_DIRECTION_NO_BIDI, PdfWriter.RUN_DIRECTION_LTR or
     * PdfWriter.RUN_DIRECTION_RTL.
     */
    public int getRunDirection() {
        return runDirection;
    }

    /**
     * Getter for property lockedWidth.
     *
     * @return Value of property lockedWidth.
     */
    public boolean isLockedWidth() {
        return this.lockedWidth;
    }

    /**
     * Uses the value in <CODE>setTotalWidth()</CODE> in
     * <CODE>Document.add()</CODE>.
     *
     * @param lockedWidth <CODE>true</CODE> to use the value in
     * <CODE>setTotalWidth()</CODE> in <CODE>Document.add()</CODE>
     */
    public void setLockedWidth(final boolean lockedWidth) {
        this.lockedWidth = lockedWidth;
    }

    /**
     * Gets the split value.
     *
     * @return true to split; false otherwise
     */
    public boolean isSplitRows() {
        return this.splitRows;
    }

    /**
     * When set the rows that won't fit in the page will be split. Note that it
     * takes at least twice the memory to handle a split table row than a normal
     * table. <CODE>true</CODE> by default.
     *
     * @param splitRows true to split; false otherwise
     */
    public void setSplitRows(final boolean splitRows) {
        this.splitRows = splitRows;
    }

    /**
     * Sets the spacing before this table.
     *
     * @param spacing the new spacing
     */
    public void setSpacingBefore(final float spacing) {
        this.spacingBefore = spacing;
    }

    /**
     * Sets the spacing after this table.
     *
     * @param spacing the new spacing
     */
    public void setSpacingAfter(final float spacing) {
        this.spacingAfter = spacing;
    }

    /**
     * Gets the spacing before this table.
     *
     * @return the spacing
     */
    public float spacingBefore() {
        return spacingBefore;
    }

    /**
     * Gets the spacing after this table.
     *
     * @return the spacing
     */
    public float spacingAfter() {
        return spacingAfter;
    }

    public float getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(float paddingTop) {
        this.paddingTop = paddingTop;
    }

    /**
     * Gets the value of the last row extension.
     *
     * @return true if the last row will extend; false otherwise
     */
    public boolean isExtendLastRow() {
        return extendLastRow[0];
    }

    /**
     * When set the last row on every page will be extended to fill all the
     * remaining space to the bottom boundary.
     *
     * @param extendLastRows true to extend the last row; false otherwise
     */
    public void setExtendLastRow(final boolean extendLastRows) {
        extendLastRow[0] = extendLastRows;
        extendLastRow[1] = extendLastRows;
    }

    /**
     * When set the last row on every page will be extended to fill all the
     * remaining space to the bottom boundary; except maybe the final row.
     *
     * @param extendLastRows true to extend the last row on each page; false
     * otherwise
     * @param extendFinalRow false if you don't want to extend the final row of
     * the complete table
     * @since iText 5.0.0
     */
    public void setExtendLastRow(final boolean extendLastRows, final boolean extendFinalRow) {
        extendLastRow[0] = extendLastRows;
        extendLastRow[1] = extendFinalRow;
    }

    /**
     * Gets the value of the last row extension, taking into account if the
     * final row is reached or not.
     *
     * @return true if the last row will extend; false otherwise
     * @since iText 5.0.0
     */
    public boolean isExtendLastRow(final boolean newPageFollows) {
        if (newPageFollows) {
            return extendLastRow[0];
        }
        return extendLastRow[1];
    }

    /**
     * Gets the header status inclusion in PdfPTableEvent.
     *
     * @return true if the headers are included; false otherwise
     */
    public boolean isHeadersInEvent() {
        return headersInEvent;
    }

    /**
     * When set the PdfPTableEvent will include the headers.
     *
     * @param headersInEvent true to include the headers; false otherwise
     */
    public void setHeadersInEvent(final boolean headersInEvent) {
        this.headersInEvent = headersInEvent;
    }

    /**
     * Gets the property splitLate.
     *
     * @return the property splitLate
     */
    public boolean isSplitLate() {
        return splitLate;
    }

    /**
     * If true the row will only split if it's the first one in an empty page.
     * It's true by default. It's only meaningful if setSplitRows(true).
     *
     * @param splitLate the property value
     */
    public void setSplitLate(final boolean splitLate) {
        this.splitLate = splitLate;
    }

    /**
     * If true the table will be kept on one page if it fits, by forcing a new
     * page if it doesn't fit on the current page. The default is to split the
     * table over multiple pages.
     *
     * @param keepTogether whether to try to keep the table on one page
     */
    public void setKeepTogether(final boolean keepTogether) {
        this.keepTogether = keepTogether;
    }

    /**
     * Getter for property keepTogether
     *
     * @return true if it is tried to keep the table on one page; false
     * otherwise
     */
    public boolean getKeepTogether() {
        return keepTogether;
    }

    /**
     * Gets the number of rows in the footer.
     *
     * @return the number of rows in the footer
     */
    public int getFooterRows() {
        return this.footerRows;
    }

    /**
     * Sets the number of rows to be used for the footer. The number of footer
     * rows are subtracted from the header rows. For example, for a table with
     * two header rows and one footer row the code would be:
     * <pre>
     * table.setHeaderRows(3);
     * table.setFooterRows(1);
     * </pre> Row 0 and 1 will be the header rows and row 2 will be the footer
     * row.
     *
     * @param footerRows the number of rows to be used for the footer
     */
    public void setFooterRows(int footerRows) {
        if (footerRows < 0) {
            footerRows = 0;
        }
        this.footerRows = footerRows;
    }

    /**
     * Completes the current row with the default cell. An incomplete row will
     * be dropped but calling this method will make sure that it will be present
     * in the table.
     */
    public void completeRow() {
        while (!rowCompleted) {
            addCell(defaultCell);
        }
    }

    /**
     * @since iText 2.0.8
     * @see com.itextpdf.text.LargeElement#flushContent()
     */
    public void flushContent() {
        deleteBodyRows();

        // setSkipFirstHeader(boolean) shouldn't be set to true if the table hasn't been added yet.
        if (this.numberOfWrittenRows > 0) {
            setSkipFirstHeader(true);
        }
    }

    /**
     * Adds the number of written rows to the counter.
     *
     * @param numberOfWrittenRows number of newly written rows
     * @since 5.5.4
     */
    void addNumberOfRowsWritten(final int numberOfWrittenRows) {
        this.numberOfWrittenRows += numberOfWrittenRows;
    }

    /**
     * @since iText 2.0.8
     * @see com.itextpdf.text.LargeElement#isComplete()
     */
    public boolean isComplete() {
        return complete;
    }

    /**
     * @since iText 2.0.8
     * @see com.itextpdf.text.LargeElement#setComplete(boolean)
     */
    public void setComplete(final boolean complete) {
        this.complete = complete;
    }

    /* (non-Javadoc)
     * @see com.itextpdf.text.api.Spaceable#getSpacingBefore()
     */
    public float getSpacingBefore() {
        return spacingBefore;
    }

    /* (non-Javadoc)
     * @see com.itextpdf.text.api.Spaceable#getSpacingAfter()
     */
    public float getSpacingAfter() {
        return spacingAfter;
    }

    public boolean isLoopCheck() {
        return loopCheck;
    }

    public void setLoopCheck(boolean loopCheck) {
        this.loopCheck = loopCheck;
    }

    public PdfObject getAccessibleAttribute(final PdfName key) {
        if (accessibleAttributes != null) {
            return accessibleAttributes.get(key);
        } else {
            return null;
        }
    }

    public void setAccessibleAttribute(final PdfName key, final PdfObject value) {
        if (accessibleAttributes == null) {
            accessibleAttributes = new HashMap<PdfName, PdfObject>();
        }
        accessibleAttributes.put(key, value);
    }

    public HashMap<PdfName, PdfObject> getAccessibleAttributes() {
        return accessibleAttributes;
    }

    public PdfName getRole() {
        return role;
    }

    public void setRole(final PdfName role) {
        this.role = role;
    }

    public AccessibleElementId getId() {
        return id;
    }

    public void setId(final AccessibleElementId id) {
        this.id = id;
    }

    public boolean isInline() {
        return false;
    }

    public PdfPTableHeader getHeader() {
        if (header == null) {
            header = new PdfPTableHeader();
        }
        return header;
    }

    public PdfPTableBody getBody() {
        if (body == null) {
            body = new PdfPTableBody();
        }
        return body;
    }

    public PdfPTableFooter getFooter() {
        if (footer == null) {
            footer = new PdfPTableFooter();
        }
        return footer;
    }

    // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
    /**
     * Gets row index where cell overlapping (rowIdx, colIdx) starts
     *
     * @param rowIdx
     * @param colIdx
     * @return row index
     * @since iText 5.4.3
     */
    public int getCellStartRowIndex(int rowIdx, int colIdx) {
        int lastRow = rowIdx;
        while (getRow(lastRow).getCells()[colIdx] == null && lastRow > 0) {
            --lastRow;
        }
        return lastRow;
    }

    // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
    /**
     *
     * @since iText 5.4.3
     */
    public static class FittingRows {

        public final int firstRow, lastRow;

        public final float height, completedRowsHeight;

        private final Map<Integer, Float> correctedHeightsForLastRow;

        public FittingRows(int firstRow, int lastRow, float height, float completedRowsHeight,
                Map<Integer, Float> correctedHeightsForLastRow) {
            this.firstRow = firstRow;
            this.lastRow = lastRow;
            this.height = height;
            this.completedRowsHeight = completedRowsHeight;
            this.correctedHeightsForLastRow = correctedHeightsForLastRow;
        }

        /**
         * Correct chosen last fitting row so that the content of all cells with
         * open rowspans will fit on the page, i.e. the cell content won't be
         * split. (Only to be used with splitLate == true)
         */
        public void correctLastRowChosen(PdfPTable table, int k) {
            PdfPRow row = table.getRow(k);
            Float value = correctedHeightsForLastRow.get(k);
            if (value != null) {
                row.setFinalMaxHeights(value);
                //System.out.printf("corrected chosen last fitting row: %6.0f\n\n", row.getMaxHeights());
            }
        }
    }

    // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
    /**
     *
     * @since iText 5.4.3
     */
    public static class ColumnMeasurementState {

        public float height = 0;

        public int rowspan = 1, colspan = 1;

        public void beginCell(PdfPCell cell, float completedRowsHeight, float rowHeight) {
            rowspan = cell.getRowspan();
            colspan = cell.getColspan();
            height = completedRowsHeight + Math.max(cell.hasCachedMaxHeight() ? cell.getCachedMaxHeight() : cell.getMaxHeight(), rowHeight);
        }

        public void consumeRowspan(float completedRowsHeight, float rowHeight) {
            --rowspan;
        }

        public boolean cellEnds() {
            return rowspan == 1;
        }
    }

    // Contributed by Deutsche Bahn Systel GmbH (Thorsten Seitz), splitting row spans
    /**
     * Determine which rows fit on the page, respecting isSplitLate(). Note:
     * sets max heights of the inspected rows as a side effect, just like
     * PdfPTable.getRowHeight(int, boolean) does. Respect row.getMaxHeights() if
     * it has been previously set (which might be independent of the height of
     * individual cells). The last row written on the page will be chosen by the
     * caller who might choose not the calculated one but an earlier one (due to
     * mayNotBreak settings on the rows). The height of the chosen last row has
     * to be corrected if splitLate == true by calling
     * FittingRows.correctLastRowChosen() by the caller to avoid splitting the
     * content of cells with open rowspans.
     *
     * @since iText 5.4.3
     */
    public FittingRows getFittingRows(float availableHeight, int startIdx) {
        if (LOGGER.isLogging(Level.INFO)) {
            LOGGER.info(String.format("getFittingRows(%s, %s)", availableHeight, startIdx));
        }
        if ( startIdx > 0 && startIdx < rows.size() ) {
            assert (getRow(startIdx).getCells()[0] != null); // top left cell of current page may not be null
        }
        int cols = getNumberOfColumns();
        ColumnMeasurementState states[] = new ColumnMeasurementState[cols];
        for (int i = 0; i < cols; ++i) {
            states[i] = new ColumnMeasurementState();
        }
        float completedRowsHeight = 0; // total height of all rows up to k only counting completed cells (with no open
        // rowspans)
        float totalHeight = 0; // total height needed to display all rows up to k, respecting rowspans
        Map<Integer, Float> correctedHeightsForLastRow = new HashMap<Integer, Float>();
        int k;
        for (k = startIdx; k < size(); ++k) {
            PdfPRow row = getRow(k);
            float rowHeight = row.getMaxRowHeightsWithoutCalculating();
            float maxCompletedRowsHeight = 0;
            int i = 0;
            while (i < cols) {
                PdfPCell cell = row.getCells()[i];
                ColumnMeasurementState state = states[i];
                if (cell == null) {
                    state.consumeRowspan(completedRowsHeight, rowHeight);
                } else {
                    state.beginCell(cell, completedRowsHeight, rowHeight);
                    if (LOGGER.isLogging(Level.INFO)) {
                        LOGGER.info(String.format("Height after beginCell: %s (cell: %s)", state.height, cell.getCachedMaxHeight()));
                    }
                }
                if (state.cellEnds() && state.height > maxCompletedRowsHeight) {
                    maxCompletedRowsHeight = state.height;
                }
                for (int j = 1; j < state.colspan; ++j) {
                    states[i + j].height = state.height;
                }
                i += state.colspan;
                //System.out.printf("%6.0f", state.height);
            }
            float maxTotalHeight = 0;
            for (ColumnMeasurementState state : states) {
                if (state.height > maxTotalHeight) {
                    maxTotalHeight = state.height;
                }
            }
            row.setFinalMaxHeights(maxCompletedRowsHeight - completedRowsHeight);
            //System.out.printf(" | %6.0f | %6.0f %6.0f | row: %6.0f\n", rowHeight, maxCompletedRowsHeight, maxTotalHeight, row.getMaxHeights());
            float remainingHeight = availableHeight - (isSplitLate() ? maxTotalHeight : maxCompletedRowsHeight);
            if (remainingHeight < 0) {
                break;
            }
            correctedHeightsForLastRow.put(k, maxTotalHeight - completedRowsHeight);
            completedRowsHeight = maxCompletedRowsHeight;
            totalHeight = maxTotalHeight;
        }
        rowsNotChecked = false;
        return new FittingRows(startIdx, k - 1, totalHeight, completedRowsHeight, correctedHeightsForLastRow);
    }
}

/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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

package com.lowagie.text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import com.lowagie.text.pdf.PdfWriter;

/**
 * A <CODE>Table</CODE> is a <CODE>Rectangle</CODE> that contains <CODE>Cell</CODE>s,
 * ordered in some kind of matrix.
 * <P>
 * Tables that span multiple pages are cut into different parts automatically.
 * If you want a table header to be repeated on every page, you may not forget to
 * mark the end of the header section by using the method <CODE>endHeaders()</CODE>.
 * <P>
 * The matrix of a table is not necessarily an m x n-matrix. It can contain holes
 * or cells that are bigger than the unit. Believe me or not, but it took some serious
 * thinking to make this as userfriendly as possible. I hope you wil find the result
 * quite simple (I love simple solutions, especially for complex problems).
 * I didn't want it to be something as complex as the Java <CODE>GridBagLayout</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * // Remark: You MUST know the number of columns when constructing a Table.
 * //         The number of rows is not important.
 * <STRONG>Table table = new Table(3);</STRONG>
 * <STRONG>table.setBorderWidth(1);</STRONG>
 * <STRONG>table.setBorderColor(new Color(0, 0, 255));</STRONG>
 * <STRONG>table.setCellpadding(5);</STRONG>
 * <STRONG>table.setCellspacing(5);</STRONG>
 * Cell cell = new Cell("header");
 * cell.setHeader(true);
 * cell.setColspan(3);
 * <STRONG>table.addCell(cell);</STRONG>
 * <STRONG>table.endHeaders();</STRONG>
 * cell = new Cell("example cell with colspan 1 and rowspan 2");
 * cell.setRowspan(2);
 * cell.setBorderColor(new Color(255, 0, 0));
 * <STRONG>table.addCell(cell);</STRONG>
 * <STRONG>table.addCell("1.1");</STRONG>
 * <STRONG>table.addCell("2.1");</STRONG>
 * <STRONG>table.addCell("1.2");</STRONG>
 * <STRONG>table.addCell("2.2");</STRONG>
 * <STRONG>table.addCell("cell test1");</STRONG>
 * cell = new Cell("big cell");
 * cell.setRowspan(2);
 * cell.setColspan(2);
 * <STRONG>table.addCell(cell);</STRONG>
 * <STRONG>table.addCell("cell test2");</STRONG>
 * </PRE></BLOCKQUOTE>
 * The result of this code is a table:
 *      <TABLE ALIGN="Center" BORDER="1" BORDERCOLOR="#0000ff" CELLPADDING="5" CELLSPACING="5">
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TH ALIGN="Left" COLSPAN="3" VALIGN="Left">
 *                              header
 *                      </TH>
 *              </TR>
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TD ALIGN="Left" BORDERCOLOR="#ff0000" ROWSPAN="2" VALIGN="Left">
 *                              example cell with colspan 1 and rowspan 2
 *                      </TD>
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              1.1
 *                      </TD>
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              2.1
 *                      </TD>
 *              </TR>
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              1.2
 *                      </TD>
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              2.2
 *                      </TD>
 *              </TR>
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              cell test1
 *                      </TD>
 *                      <TD ALIGN="Left" COLSPAN="2" ROWSPAN="2" VALIGN="Left">
 *                              big cell
 *                      </TD>
 *              </TR>
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              cell test2
 *                      </TD>
 *              </TR>
 *      </TABLE>
 *
 * @see         Rectangle
 * @see         Element
 * @see         Row
 * @see         Cell
 *
 * @author  bruno@lowagie.com
 */

public class Table extends Rectangle implements Element {
    
    // membervariables
    
    // these variables contain the data of the table
    
/** This is the number of columns in the <CODE>Table</CODE>. */
    private int columns;
    
/** This is the currentRow. */
    private int currentRow;
    
/** This is the currentColumn. */
    private int currentColumn;
    
/** This is the list of <CODE>Row</CODE>s. */
    private ArrayList rows = new ArrayList();
    
    // these variables contain the layout of the table
    
/** This Empty Cell contains the DEFAULT layout of each Cell added with the method addCell(String content). */
    private Cell defaultLayout = new Cell();
    
/** This is the number of the last row of the table headers. */
    private int lastHeaderRow = -1;
    
/** This is the horizontal alignment. */
    private int alignment = Element.ALIGN_CENTER;
    
/** This is cellpadding. */
    private float cellpadding;
    
/** This is cellspacing. */
    private float cellspacing;
    
/** This is the width of the table (in percent of the available space). */
    private float widthPercentage = 80;
    
    // member variable added by Evelyne De Cordier
/** This is the width of the table (in pixels). */
    private String absWidth = "";
    
/** This is an array containing the widths (in percentages) of every column. */
    private float[] widths;
    
    // constructors
    
/**
 * Constructs a <CODE>Table</CODE> with a certain number of columns.
 *
 * @param       columns         The number of columns in the table
 * @throws      BadElementException if the creator was called with less than 1 column
 */
    
    public Table(int columns) throws BadElementException {
        this(columns, 1);
    }
    
/**
 * Constructs a <CODE>Table</CODE> with a certain number of columns
 * and a certain number of <CODE>Row</CODE>s.
 *
 * @param       columns         The number of columns in the table
 * @param       rows            The number of rows
 * @throws      BadElementException if the creator was called with less than 1 column
 */
    
    public Table(int columns, int rows) throws BadElementException {
        // a Rectangle is create with BY DEFAULT a border with a width of 1
        super(0, 0, 0, 0);
        setBorder(BOX);
        setBorderWidth(1);
        
        // a table should have at least 1 column
        if (columns <= 0) {
            throw new BadElementException("A table should have at least 1 column.");
        }
        this.columns = columns;
        
        // a certain number of rows are created
        for (int i = 0; i < rows; i++) {
            this.rows.add(new Row(columns));
        }
        currentRow = 0;
        currentColumn = 0;
        
        // the DEFAULT widths are calculated
        widths = new float[columns];
        float width = 100f / columns;
        for (int i = 0; i < columns; i++) {
            widths[i] = width;
        }
    }
    
/**
 * Returns a <CODE>Table</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 * @return	a <CODE>Table</CODE>
 */
    
    public Table(Properties attributes) throws BadElementException {
        // a Rectangle is create with BY DEFAULT a border with a width of 1
        super(0, 0, 0, 0);
        setBorder(BOX);
        setBorderWidth(1);
        
        String value = attributes.getProperty(ElementTags.COLUMNS);
        if (value == null) {
            throw new BadElementException("You have to tell the table how many columns you need.");
        }
        
        columns = Integer.parseInt(value);
        if (columns <= 0) {
            throw new BadElementException("A table should have at least 1 column.");
        }
        
        rows.add(new Row(columns));
        currentRow = 0;
        
        if ((value = attributes.getProperty(ElementTags.LASTHEADERROW)) != null) {
            setLastHeaderRow(Integer.parseInt(value));
        }
        if ((value = attributes.getProperty(ElementTags.ALIGN)) != null) {
            setAlignment(value);
        }
        if ((value = attributes.getProperty(ElementTags.CELLSPACING)) != null) {
            setCellspacing(Float.parseFloat(value + "f"));
        }
        if ((value = attributes.getProperty(ElementTags.CELLPADDING)) != null) {
            setCellpadding(Float.parseFloat(value + "f"));
        }
        if ((value = attributes.getProperty(ElementTags.WIDTH)) != null) {
            if (value.endsWith("%"))
                setWidth(Float.parseFloat(value.substring(0, value.length() - 1) + "f"));
            else
                setAbsWidth(value);
        }
        widths = new float[columns];
        for (int i = 0; i < columns; i++) {
            widths[i] = 1;
        }
        if ((value = attributes.getProperty(ElementTags.WIDTHS)) != null) {
            StringTokenizer widthTokens = new StringTokenizer(value, ";");
            int i = 0;
            while (widthTokens.hasMoreTokens() && i < columns) {
                value = (String) widthTokens.nextToken();
                widths[i] = Float.parseFloat(value + "f");
                i++;
            }
        }
        if ((value = attributes.getProperty(ElementTags.BORDERWIDTH)) != null) {
            setBorderWidth(Float.parseFloat(value + "f"));
        }
        int border = 0;
        if ((value = attributes.getProperty(ElementTags.LEFT)) != null) {
            if (new Boolean(value).booleanValue()) border |= Rectangle.LEFT;
        }
        if ((value = attributes.getProperty(ElementTags.RIGHT)) != null) {
            if (new Boolean(value).booleanValue()) border |= Rectangle.RIGHT;
        }
        if ((value = attributes.getProperty(ElementTags.TOP)) != null) {
            if (new Boolean(value).booleanValue()) border |= Rectangle.TOP;
        }
        if ((value = attributes.getProperty(ElementTags.BOTTOM)) != null) {
            if (new Boolean(value).booleanValue()) border |= Rectangle.BOTTOM;
        }
        setBorder(border);
        if (attributes.getProperty(ElementTags.RED) != null &&
        attributes.getProperty(ElementTags.GREEN) != null &&
        attributes.getProperty(ElementTags.BLUE) != null) {
            setBorderColor(new Color(Integer.parseInt(attributes.getProperty(ElementTags.RED)),
            Integer.parseInt(attributes.getProperty(ElementTags.GREEN)),
            Integer.parseInt(attributes.getProperty(ElementTags.BLUE))));
        }
        if (attributes.getProperty(ElementTags.BGRED) != null &&
        attributes.getProperty(ElementTags.BGGREEN) != null &&
        attributes.getProperty(ElementTags.BGBLUE) != null) {
            setBackgroundColor(new Color(Integer.parseInt(attributes.getProperty(ElementTags.BGRED)),
            Integer.parseInt(attributes.getProperty(ElementTags.BGGREEN)),
            Integer.parseInt(attributes.getProperty(ElementTags.BGBLUE))));
        }
        if ((value = attributes.getProperty(ElementTags.GRAYFILL)) != null) {
            setGrayFill(Float.parseFloat(value + "f"));
        }
    }
    
    // implementation of the Element-methods
    
/**
 * Processes the element by adding it (or the different parts) to an
 * <CODE>ElementListener</CODE>.
 *
 * @param       listener        an <CODE>ElementListener</CODE>
 * @return <CODE>true</CODE> if the element was processed successfully
 */
    
    public final boolean process(ElementListener listener) {
        try {
            return listener.add(this);
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
/**
 * Gets the type of the text element.
 *
 * @return  a type
 */
    
    public final int type() {
        return Element.TABLE;
    }
    
/**
 * Gets all the chunks in this element.
 *
 * @return  an <CODE>ArrayList</CODE>
 */
    
    public final ArrayList getChunks() {
        return new ArrayList();
    }
    
    // methods to add content to the table
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE> at a certain row and column.
 *
 * @param       cell    The <CODE>Cell</CODE> to add
 * @param       row     The row where the <CODE>Cell</CODE> will be added
 * @param       column  The column where the <CODE>Cell</CODE> will be added
 * @author Geert Poels  -  Geert.Poels@skynet.be
 */
    
    public void addCell(Cell aCell, int row, int column) throws BadElementException {
        addCell(aCell, new Point(row,column));
    }
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE> at a certain location.
 *
 * @param       cell        The <CODE>Cell</CODE> to add
 * @param       location    The location where the <CODE>Cell</CODE> will be added
 * @author Geert Poels  -  Geert.Poels@skynet.be
 */
    
    public void addCell(Cell aCell, Point aLocation) throws BadElementException {
        if (aCell == null) throw new NullPointerException("addCell - cell has null-value");
        if (aLocation == null) throw new NullPointerException("addCell - point has null-value");
        Assert.assert(aLocation.x >= 0,"row coordinate of location must be >= 0)");
        Assert.assert((aLocation.y > 0) || (aLocation.y <= columns),"column coordinate of location must be >= 0 and < nr of columns)");
        Assert.assert(isValidLocation(aCell, aLocation) == true,"Adding a cell at the location (" + aLocation.x + "," + aLocation.y + ") with a colspan of " + aCell.colspan() + " and a rowspan of " + aCell.rowspan() + " is illegal (beyond boundaries/overlapping).");
        
        placeCell(rows, aCell, aLocation);
        
        // set latest location to next valid position
        int i, j;
        i = aLocation.x;
        j = aLocation.y;
        do {
            if ( (j + 1)  == columns ) // goto next row
            {
                i++;
                j = 0;
            }
            else {
                j++;
            }
        }
        while (
        (i < rows.size()) && (j < columns) && (((Row) rows.get(i)).isReserved(j) == true)
        );
        currentRow              = i;
        currentColumn   = j;
    }
    
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>.
 *
 * @param       cell         a <CODE>Cell</CODE>
 * @throws      BadElementException this should never happen
 */
    
    public final void addCell(Cell cell) throws BadElementException {
        addCell(cell, new Point(currentRow, currentColumn));
    }
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>.
 * <P>
 * This is a shortcut for <CODE>addCell(Cell cell)</CODE>.
 * The <CODE>Phrase</CODE> will be converted to a <CODE>Cell</CODE>.
 *
 * @param       content         a <CODE>Phrase</CODE>
 * @throws      BadElementException this should never happen
 */
    
    public final void addCell(Phrase content) throws BadElementException {
        addCell(content, new Point(currentRow, currentColumn));
    }
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>.
 * <P>
 * This is a shortcut for <CODE>addCell(Cell cell, Point location)</CODE>.
 * The <CODE>Phrase</CODE> will be converted to a <CODE>Cell</CODE>.
 *
 * @param       content         a <CODE>Phrase</CODE>
 * @param       location        a <CODE>Point</CODE>
 * @throws      BadElementException this should never happen
 */
    
    public final void addCell(Phrase content, Point location) throws BadElementException {
        Cell cell = new Cell(content);
        cell.setBorder(defaultLayout.border());
        cell.setBorderWidth(defaultLayout.borderWidth());
        cell.setBorderColor(defaultLayout.borderColor());
        cell.setBackgroundColor(defaultLayout.backgroundColor());
        cell.setGrayFill(defaultLayout.grayFill());
        cell.setHorizontalAlignment(defaultLayout.horizontalAlignment());
        cell.setVerticalAlignment(defaultLayout.verticalAlignment());
        cell.setColspan(defaultLayout.colspan());
        cell.setRowspan(defaultLayout.rowspan());
        addCell(cell, location);
    }
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>.
 * <P>
 * This is a shortcut for <CODE>addCell(Cell cell)</CODE>.
 * The <CODE>String</CODE> will be converted to a <CODE>Cell</CODE>.
 *
 * @param       content         a <CODE>String</CODE>
 * @throws      BadElementException this should never happen
 */
    
    public final void addCell(String content) throws BadElementException {
        addCell(new Phrase(content), new Point(currentRow,currentColumn));
    }
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>.
 * <P>
 * This is a shortcut for <CODE>addCell(Cell cell, Point location)</CODE>.
 * The <CODE>String</CODE> will be converted to a <CODE>Cell</CODE>.
 *
 * @param       content         a <CODE>String</CODE>
 * @param       location        a <CODE>Point</CODE>
 * @throws      BadElementException this should never happen
 */
    
    public final void addCell(String content, Point location) throws BadElementException {
        addCell(new Phrase(content), location);
    }
    
/**
 * To put a table within the existing table at the current position
 * generateTable will of course re-arrange the widths of the columns
 *
 * @param   aTable      the table you want to insert
 * @author Geert Poels  -  Geert.Poels@skynet.be
 */
    
    public void insertTable(Table aTable) {
        if (aTable == null) throw new NullPointerException("insertTable - table has null-value");
        insertTable(aTable, new Point(currentRow,currentColumn));
    }
    
/**
 * To put a table within the existing table at the given position
 * generateTable will of course re-arrange the widths of the columns
 *
 * @param   aTable      the table you want to insert
 * @param   aLocation   a <CODE>Point</CODE>
 * @author Geert Poels  -  Geert.Poels@skynet.be
 */
    
    public void insertTable(Table aTable, Point aLocation) {
        if (aTable == null) throw new NullPointerException("insertTable - table has null-value");
        if (aLocation == null) throw new NullPointerException("insertTable - point has null-value");
        
        Assert.assert(aLocation.y <= columns,"insertTable -- wrong columnposition("+ aLocation.y + ") of location; max =" + columns);
        ((Row) rows.get(aLocation.x)).setElement(aTable,aLocation.y);
    }
    
/*
 * Will fill empty cells with valid blank <CODE>Cell</CODE>s
 * @author Geert Poels  -  Geert.Poels@skynet.be
 */
    
    public final void complete() throws BadElementException, DocumentException {
        mergeInsertedTables();  // integrate tables in the table
        fillEmptyMatrixCells();
        checkIllegalRowspan();
    }
    
/**
 * Changes the border in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       value   the new border value
 */
    
    public final void setDefaultCellBorder(int value) {
        defaultLayout.setBorder(value);
    }
    
/**
 * Changes the width of the borders in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       value   the new width
 */
    
    public final void setDefaultCellBorderWidth(float value) {
        defaultLayout.setBorderWidth(value);
    }
    
/**
 * Changes the bordercolor in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       color   the new color
 */
    
    public final void setDefaultCellBorderColor(Color color) {
        defaultLayout.setBorderColor(color);
    }
    
/**
 * Changes the backgroundcolor in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       color   the new color
 */
    
    public final void setDefaultCellBackgroundColor(Color color) {
        defaultLayout.setBackgroundColor(color);
    }
    
/**
 * Changes the grayfill in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       value   the new value
 */
    
    public final void setDefaultCellGrayFill(float value) {
        if (value >= 0 && value <= 1) {
            defaultLayout.setGrayFill(value);
        }
    }
    
/**
 * Changes the horizontalAlignment in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       value   the new alignment value
 */
    
    public final void setDefaultHorizontalAlignment(int value) {
        defaultLayout.setHorizontalAlignment(value);
    }
    
/**
 * Changes the verticalAlignment in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       value   the new alignment value
 */
    
    public final void setDefaultVerticalAlignment(int value) {
        defaultLayout.setVerticalAlignment(value);
    }
    
/**
 * Changes the rowspan in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       value   the new rowspan value
 */
    
    public final void setDefaultRowspan(int value) {
        defaultLayout.setRowspan(value);
    }
    
/**
 * Changes the colspan in the default layout of the <CODE>Cell</CODE>s
 * added with method <CODE>addCell(String content)</CODE>.
 *
 * @param       value   the new colspan value
 */
    
    public final void setDefaultColspan(int value) {
        defaultLayout.setColspan(value);
    }
    
    // methods
    
/**
 * Deletes a column in this table.
 *
 * @param       column  the number of the column that has to be deleted
 */
    
    public final void deleteColumn(int column) throws BadElementException {
        float newWidths[] = new float[--columns];
        for (int i = 0; i < column; i++) {
            newWidths[i] = widths[i];
        }
        for (int i = column; i < columns; i++) {
            newWidths[i] = widths[i + 1];
        }
        setWidths(newWidths);
        for (int i = 0; i < columns; i++) {
            newWidths[i] = widths[i];
        }
        widths = newWidths;
        Row row;
        int size = rows.size();
        for (int i = 0; i < size; i++) {
            row = (Row) rows.get(i);
            row.deleteColumn(column);
            rows.set(i, row);
        }
    }
    
/**
 * Deletes a row.
 *
 * @param       row             the number of the row to delete
 * @return      boolean <CODE>true</CODE> if the row was deleted; <CODE>false</CODE> if not
 */
    
    public final boolean deleteRow(int row) {
        if (row < 0 || row >= rows.size()) {
            return false;
        }
        rows.remove(row);
        //Added by CWE
        --currentRow;
        return true;
    }
    
/**
 * Deletes the last row in this table.
 *
 * @return      boolean <CODE>true</CODE> if the row was deleted; <CODE>false</CODE> if not
 */
    
    public final boolean deleteLastRow() {
        return deleteRow(rows.size() - 1);
    }
    
/**
 * Marks the last row of the table headers.
 *
 * @return      the number of the last row of the table headers
 */
    
    public int endHeaders() {
        lastHeaderRow = currentRow;
        return lastHeaderRow;
    }
    
    // methods to set the membervariables
    
/**
 * Sets the horizontal alignment.
 *
 * @param       value   the new value
 */
    
    public final void setLastHeaderRow(int value) {
        lastHeaderRow = value;
    }
    
/**
 * Sets the horizontal alignment.
 *
 * @param       value   the new value
 */
    
    public final void setAlignment(int value) {
        alignment = value;
    }
    
/**
 * Sets the alignment of this paragraph.
 *
 * @param	alignment		the new alignment as a <CODE>String</CODE>
 */
    
    public final void setAlignment(String alignment) {
        if (ElementTags.ALIGN_LEFT.equals(alignment)) {
            this.alignment = Element.ALIGN_LEFT;
            return;
        }
        if (ElementTags.RIGHT.equals(alignment)) {
            this.alignment = Element.ALIGN_RIGHT;
            return;
        }
        this.alignment = Element.ALIGN_CENTER;
    }
    
/**
 * Sets the cellpadding.
 *
 * @param       value   the new value
 */
    
    public final void setCellpadding(float value) {
        cellpadding = value;
    }
    
/**
 * Sets the cellspacing.
 *
 * @param       value   the new value
 */
    
    public final void setCellspacing(float value) {
        cellspacing = value;
    }
    
/**
 * Sets the width of this table (in percentage of the available space).
 *
 * @param       width           the width
 */
    
    public final void setWidth(float width) {
        this.widthPercentage = width;
    }
    
/**
 * Sets the width of this table (in percentage of the available space).
 *
 * @param   width           the width
 * @author  Evelyne De Cordier
 */
    
    public final void setAbsWidth(String width) {
        this.absWidth = width;
    }
    
/**
 * Sets the widths of the different columns (percentages).
 * <P>
 * You can give up relative values of borderwidths.
 * The sum of these values will be considered 100%.
 * The values will be recalculated as percentages of this sum.
 * <P>
 * example:
 * <BLOCKQUOTE><PRE>
 * float[] widths = {2, 1, 1};
 * <STRONG>table.setWidths(widths)</STRONG>
 * </PRE></BLOCKQUOTE>
 * The widths will be: a width of 50% for the first column,
 * 25% for the second and third column.
 *
 * @param       an array with values
 */
    
    public final void setWidths(float[] widths) throws BadElementException {
        if (widths.length != columns) {
            throw new BadElementException("Wrong number of columns.");
        }
        
        // The sum of all values is 100%
        float hundredPercent = 0;
        for (int i = 0; i < columns; i++) {
            hundredPercent += widths[i];
        }
        
        // The different percentages are calculated
        float width;
        this.widths[columns - 1] = 100;
        for (int i = 0; i < columns - 1; i++) {
            width = (100.0f * widths[i]) / hundredPercent;
            this.widths[i] = width;
            this.widths[columns - 1] -= width;
        }
    }
    
/**
 * Sets the widths of the different columns (percentages).
 * <P>
 * You can give up relative values of borderwidths.
 * The sum of these values will be considered 100%.
 * The values will be recalculated as percentages of this sum.
 *
 * @param       an array with values
 */
    
    public final void setWidths(int[] widths) throws DocumentException {
        float tb[] = new float[widths.length];
        for (int k = 0; k < widths.length; ++k)
            tb[k] = widths[k];
        setWidths(tb);
    }
    // methods to retrieve the membervariables
    
/**
 * Gets the number of rows in this <CODE>Table</CODE>.
 *
 * @return      the number of rows in this <CODE>Table</CODE>
 */
    
    public final int size() {
        return rows.size();
    }
    
/**
 * Gets an <CODE>Iterator</CODE> of all the <CODE>Row</CODE>s.
 *
 * @return      an <CODE>Iterator</CODE>
 */
    
    public Iterator iterator() {
        return rows.iterator();
    }
    
/**
 * Gets the horizontal alignment.
 *
 * @return  a value
 */
    
    public int alignment() {
        return alignment;
    }
    
/**
 * Gets the cellpadding.
 *
 * @return  a value
 */
    
    public float cellpadding() {
        return cellpadding;
    }
    
/**
 * Gets the cellspacing.
 *
 * @return  a value
 */
    
    public float cellspacing() {
        return cellspacing;
    }
    
/**
 * Gets the table width (a percentage).
 *
 * @return      the table width
 * @author      Leslie Baski
 */
    
    public float widthPercentage() {
        return widthPercentage;
    }
    
/**
 * Gets the table width (in pixels).
 *
 * @return  the table width
 * @author  Evelyne De Cordier
 */
    
    public String absWidth() {
        return absWidth;
    }
    
/**
 * Gets the first number of the row that doesn't contain headers.
 *
 * @return      a rownumber
 */
    
    public int firstDataRow() {
        return lastHeaderRow + 1;
    }
    
/**
 * Gets the dimension of this table
 *
 * @return  dimension
 * @author  Geert Poels
 */
    
    public Dimension getDimension() {
        return new Dimension(columns, rows.size());
    }
    
/**
 * returns the element at the position row, column
 *          (Cast to Cell or Table)
 *
 * @return  dimension
 * @author  Geert Poels
 */
    
    public Object getElement(int row, int column) {
        return ((Row) rows.get(row)).getCell(column);
    }
    
/**
 * Integrates all added tables and recalculates column widths.
 *
 * @author  Geert Poels
 */
    
    private void mergeInsertedTables() throws DocumentException {
        int i=0, j=0;
        float [] lNewWidths = null;
        int [] lDummyWidths = new int[columns];     // to keep track in how many new cols this one will be split
        int [] lDummyHeights = new int[rows.size()]; // to keep track in how many new rows this one will be split
        ArrayList newRows = null;
        
        int lTotalRows  = 0, lTotalColumns      = 0;
        int lNewMaxRows = 0, lNewMaxColumns     = 0;
        
        Table lDummyTable = null;
        
        // first we'll add new columns when needed
        // check one column at a time, find maximum needed nr of cols
        for (j=0; j < columns; j++) {
            lNewMaxColumns = 1; // value to hold in how many columns the current one will be split
            for (i=0; i < rows.size(); i++) {
                if ( Table.class.isInstance(((Row) rows.get(i)).getCell(j)) ) {
                    lDummyTable = ((Table) ((Row) rows.get(i)).getCell(j));
                    if ( lDummyTable.getDimension().width > lNewMaxColumns ) {
                        lNewMaxColumns = lDummyTable.getDimension().width;
                    }
                }
            }
            lTotalColumns += lNewMaxColumns;
            lDummyWidths [j] = lNewMaxColumns;
        }
        
        // next we'll add new rows when needed
        for (i=0; i < rows.size(); i++) {
            lNewMaxRows = 1;    // holds value in how many rows the current one will be split
            for (j=0; j < columns; j++) {
                if ( Table.class.isInstance(((Row) rows.get(i)).getCell(j)) ) {
                    lDummyTable = (Table) ((Row) rows.get(i)).getCell(j);
                    if ( lDummyTable.getDimension().height > lNewMaxRows ) {
                        lNewMaxRows = lDummyTable.getDimension().height;
                    }
                }
            }
            lTotalRows += lNewMaxRows;
            lDummyHeights [i] = lNewMaxRows;
        }
        
        if ( (lTotalColumns != columns) || (lTotalRows != rows.size()) ) {       // NO ADJUSTMENT
             // ** WIDTH
            // set correct width for new columns
            // divide width over new nr of columns
            lNewWidths = new float [lTotalColumns];
            int lDummy = 0;
            for (int tel=0; tel < widths.length;tel++) {
                if ( lDummyWidths[tel] != 1) {
                    // divide
                    for (int tel2 = 0; tel2 < lDummyWidths[tel]; tel2++) {
                        lNewWidths[lDummy] = widths[tel] / lDummyWidths[tel];
                        lDummy++;
                    }
                }
                else {
                    lNewWidths[lDummy] = widths[tel];
                    lDummy++;
                }
            }
            
            // ** FILL OUR NEW TABLE
            // generate new table
            // set new widths
            // copy old values
            newRows = new ArrayList(lTotalRows);
            for (i = 0; i < lTotalRows; i++) {
                newRows.add(new Row(lTotalColumns));
            }
            int lDummyRow = 0, lDummyColumn = 0;        // to remember where we are in the new, larger table
            Object lDummyElement = null;
            for (i=0; i < rows.size(); i++) {
                lDummyColumn = 0;
                lNewMaxRows = 1;
                for (j=0; j < columns; j++) {
                    if ( Table.class.isInstance(((Row) rows.get(i)).getCell(j)) )       // copy values from embedded table
                    {
                        lDummyTable = (Table) ((Row) rows.get(i)).getCell(j);
                        
                        for (int k=0; k < lDummyTable.getDimension().height; k++) {
                            for (int l=0; l < lDummyTable.getDimension().width; l++) {
                                lDummyElement = lDummyTable.getElement(k,l);
                                if (lDummyElement != null) {
                                    ((Row) newRows.get(k + lDummyRow)).addElement(lDummyElement,l + lDummyColumn);  // use addElement to set reserved status ok in row
                                }
                            }
                        }
                    }
                    else        // copy others values
                    {
                        Object aElement = getElement(i,j);
                        
                        if ( Cell.class.isInstance(aElement) ) {
                            // adjust spans for cell
                            ((Cell) aElement).setRowspan(((Cell) ((Row) rows.get(i)).getCell(j)).rowspan() + lDummyHeights[i] - 1);
                            ((Cell) aElement).setColspan(((Cell) ((Row) rows.get(i)).getCell(j)).colspan() + lDummyWidths[j] - 1);
                            
                            // most likely this cell covers a larger area because of the row/cols splits : define not-to-be-filled cells
                            placeCell(newRows,((Cell) aElement), new Point(lDummyRow,lDummyColumn));
                        }
                    }
                    lDummyColumn += lDummyWidths[j];
                }
                lDummyRow += lDummyHeights[i];
            }
            
            // Set our new matrix
            columns     = lTotalColumns;
            rows = newRows;
            this.widths = lNewWidths;
        }
        
    }
    
/**
 * adds new<CODE>Cell</CODE>'s to empty/null spaces
 * @author  Geert Poels
 */
    
    private void fillEmptyMatrixCells() throws BadElementException {
        Cell lDummyCell = null;
        int  lTel = -1;
        Object obj = new Object();
        
        for (int i=0; i < rows.size(); i++) {
            for (int j=0; j < columns; j++) {
                if ( ((Row) rows.get(i)).isReserved(j) == false) {
                    addCell(new Cell(new Paragraph(" ")), new Point(i, j));
                }
            }
        }
    }
    
/**
 * check if <CODE>Cell</CODE> 'fits' the table.
 * <P>
 * <UL><LI>rowspan/colspan not beyond borders
 *     <LI>spanned cell don't overlap existing cells</UL>
 *
 * @param   aCell       the cell that has to be checked
 * @param   aLocation   the location where the cell has to be placed
 * @author  Geert Poels
 */
    
    private boolean isValidLocation(Cell aCell, Point aLocation) {
        // rowspan not beyond last column
        if ( aLocation.x < rows.size() )        // if false : new location is already at new, not-yet-created area so no check
        {
            if ((aLocation.y + aCell.colspan()) > columns) {
                return false;
            }
            
            int difx = ((rows.size() - aLocation.x) >  aCell.rowspan()) ? aCell.rowspan() : rows.size() - aLocation.x;
            int dify = ((columns - aLocation.y) >  aCell.colspan()) ? aCell.colspan() : columns - aLocation.y;
            // no other content at cells targetted by rowspan/colspan
            for (int i=aLocation.x; i < (aLocation.x + difx); i++) {
                for (int j=aLocation.y; j < (aLocation.y + dify); j++) {
                    if ( ((Row) rows.get(i)).isReserved(j) == true ) {
                        return false;
                    }
                }
            }
        }
        else {
            if ((aLocation.y + aCell.colspan()) > columns) {
                return false;
            }
        }
        return true;
    }
    
/**
 * Inserts a Cell in a cell-array and reserves cells defined by row-/colspan.
 *
 * @param   someRows    some rows
 * @param   aCell       the cell that has to be inserted
 * @param   aPosition   the position where the cell has to be placed
 * @author  Geert Poels
 */
    
    private void placeCell(ArrayList someRows, Cell aCell, Point aPosition) {
        int i,j;
        Row row = null;
        int lColumns = ((Row) someRows.get(0)).columns();
        int rowCount = aPosition.x + aCell.rowspan() - someRows.size();
        
        if ( (aPosition.x + aCell.rowspan()) > someRows.size() )        //create new rows ?
        {
            for (i = 0; i < rowCount; i++) {
                row = new Row(lColumns);
                someRows.add(row);
            }
        }
        
        // reserve cell in rows below
        for (i = aPosition.x + 1; i < (aPosition.x  + aCell.rowspan()); i++) {
            if ( !((Row) someRows.get(i)).reserve(aPosition.y, aCell.colspan())) {
                // should be impossible to come here :-)
                throw new UnsupportedOperationException("addCell - error in reserve");
            }
        }
        
        row = (Row) someRows.get(aPosition.x);
        row.addElement(aCell, aPosition.y);
        
    }

/**
 * Checks if there are no rowspan difficulties in the table.
 * <P>
 * Using rowspan to cover a number of cells may have unexpected consequences!!
 *  fe. : a table with two columns, each spanning multiple rows may result in
 *        a table with only one visible row  !!
 *        (because the rows to be spanned actually don't exist)<BR>
 *
 * Lowagie's library, like HTML, requires an element at the lowest point or below
 * the lowest row, targetted by the rowspan.<BR>
 *
 * This library will throw a warning in case such a situation is detected.
 * It will replace the rowspan with several empty cells in case of an empty row with rowspan.
 *
 * @author  Kris Jespers
 */
    protected void checkIllegalRowspan() throws BadElementException {
        // find lowest cell
        int i = rows.size() - 1, j = columns - 1;
        
        // check rowspans
        i = rows.size() - 1;
        int lIllegalRowCount = 0;
        while ( i >= 0 ) {
            j = 0;
            while ( (j < columns) && (((Row) rows.get(i)).getCell(j) != null) && (!Cell.class.isInstance(((Row) rows.get(i)).getCell(j))) &&
            (Object.class.isInstance(((Row) rows.get(i)).getCell(j))) ) {
                j++;
            }
            
            if ( j == columns ) // found a row with only 'spanned' cells
            {
                lIllegalRowCount++;
                i--;
            }
            else {
                break;
            }
        }
        if ( lIllegalRowCount > 0 ) {
            //int lNrOfReplacedColumns = replaceEmptyAreasWithEmptyCells();
            System.err.println("*************************************************** !!!!!!! ***************************************************");
            System.err.println("WARNING: This table contains " + lIllegalRowCount + " rows with rowspans without valid cells.");
            System.err.println("WARNING: An empty column has been added to solve the problem...");
            System.err.println("*************************************************** !!!!!!! ***************************************************");
            addColumn(1);
            for (i = 0; i < rows.size(); i++) {
                addCell(new Cell(new Paragraph(" ")), new Point(i, columns-1));
            }
        }
    }
    
/**
 * Gives you the posibility to add columns.
 *
 * @param   aColumns    the number of columns to add
 */
    
    private void addColumn(int aColumns) {
        ArrayList newRows = new ArrayList(rows.size());
        int length = ((Row) rows.get(0)).columns();             // old nr of cols
        
        for (int i = 0; i < rows.size(); i++) {
            this.rows.add(new Row(length + aColumns));
            for (int j = 0; j < length; j++) {
                ((Row) rows.get(i)).setElement( ((Row) rows.get(i)).getCell(j) ,j);
            }
        }
        
        columns += aColumns;
        
        // applied 1 column-fix; last column needs to have a width of 0
        float [] newWidths = new float[columns];
        System.arraycopy(widths, 0, newWidths, 0, columns-1);
        for (int k = columns - aColumns;k < columns ; k++) {
            newWidths[k] = 0;
        }
        widths = newWidths;
        rows = newRows;
    }
    
/**
 * returns the element at a given location
 *
 * @author  Geert Poels
 */
    
    private Object getElement(ArrayList al, int row, int column) {
        return ((Row) al.get(row)).getCell(column);
    }
    
/**
 * returns the element type name (Cell, Tabl, Objt)
 * @author  Geert Poels
 */
    
    private String getElementType(Object aElement) {
        String aReturnValue = null;
        if ( Cell.class.isInstance(aElement) ) {
            aReturnValue = "Cell";
        }
        else if ( Table.class.isInstance(aElement) ) {
            aReturnValue = "Tabl";
        }
        else if ( Object.class.isInstance(aElement) ) {
            aReturnValue = "Objt";
        }
        else {
            aReturnValue = "null";
        }
        return aReturnValue;
    }
    
/**
 * returns the element type name (Cell, Tabl, Objt) of
 * an element in the table at point(aRow, aColumn)
 *
 * @author  Geert Poels
 */
    
    private String getElementType(int aRow, int aColumn) {
        return getElementType(rows,aRow,aColumn);
    }
    
/**
 * returns the element type name (Cell, Tabl, Objt) of
 * an element in the table at point(aRow, aColumn)
 *
 * @author  Geert Poels
 */
    
    private String getElementType(ArrayList al, int aRow, int aColumn) {
        return getElementType(getElement(al,aRow, aColumn));
    }
    
/**
 * Gets an array with the positions of the borders between every column.
 * <P>
 * This method translates the widths expressed in percentages into the
 * x-coordinate of the borders of the columns on a real document.
 *
 * @param       left            this is the position of the first border at the left (cellpadding not included)
 * @param       totalWidth      this is the space between the first border at the left
 *                                              and the last border at the right (cellpadding not included)
 * @return      an array with borderpositions
 */
    
    public float[] getWidths(float left, float totalWidth) {
        // for x columns, there are x+1 borders
        float[] w = new float[columns + 1];
        // the border at the left is calculated
        switch(alignment) {
            case Element.ALIGN_LEFT:
                w[0] = left;
                break;
            case Element.ALIGN_RIGHT:
                w[0] = left + (totalWidth * (100 - widthPercentage)) / 100;
                break;
            case Element.ALIGN_CENTER:
                default:
                    w[0] = left + (totalWidth * (100 - widthPercentage)) / 200;
        }
        // the total available width is changed
        totalWidth = (totalWidth * widthPercentage) / 100;
        // the inner borders are calculated
        for (int i = 1; i < columns; i++) {
            w[i] = w[i - 1] + (widths[i - 1] * totalWidth / 100);
        }
        // the border at the right is calculated
        w[columns] = w[0] + totalWidth;
        return w;
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.TABLE.equals(tag);
    }
    
/**
 * Returns a representation of this <CODE>Row</CODE>.
 *
 * @return      a <CODE>String</CODE>
 */
    
    public String toString() {
        StringBuffer buf = new StringBuffer("<").append(ElementTags.TABLE);
        buf.append(" ").append(ElementTags.COLUMNS).append("=\"").append(columns).append("\"");
        buf.append(" ").append(ElementTags.WIDTH).append("=\"");
        if (!absWidth.equals("")){
            buf.append(absWidth);
        }
        else{
            buf.append(widthPercentage).append("%");
        }
        buf.append("\" ").append(ElementTags.ALIGN).append("=\"");
        buf.append(ElementTags.getAlignment(alignment));
        buf.append("\" ").append(ElementTags.CELLPADDING).append("=\"");
        buf.append(cellpadding);
        buf.append("\" ").append(ElementTags.CELLSPACING).append("=\"");
        buf.append(cellspacing);
        buf.append("\" ").append(ElementTags.WIDTHS).append("s=\"");
        buf.append(widths[0]);
        for (int i = 1; i < widths.length; i++) {
            buf.append(";");
            buf.append(widths[i]);
        }
        buf.append("\"");
        buf.append(super.toString());
        buf.append(">\n");
        Row row;
        for (Iterator iterator = rows.iterator(); iterator.hasNext(); ) {
            row = (Row) iterator.next();
            buf.append(row.toString());
        }
        buf.append("</").append(ElementTags.TABLE).append(">\n");
        return buf.toString();
    }
}
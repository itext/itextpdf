/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
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
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU LIBRARY GENERAL PUBLIC LICENSE for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * Some methods in this class were contributed by Geert Poels, Kris Jespers and
 * Steve Ogryzek. Check the CVS repository.
 */

package com.lowagie.text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
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
 * <STRONG>table.setPadding(5);</STRONG>
 * <STRONG>table.setSpacing(5);</STRONG>
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
 */

public class Table extends Rectangle implements Element, MarkupAttributes {
    
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
    private Cell defaultLayout = new Cell(true);
    
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
    
/** Boolean to track errors (some checks will be performed) */
    boolean mDebug = false;
    
/** Boolean to track if a table was inserted (to avoid unnecessary computations afterwards) */
    boolean mTableInserted = false;
    
/**
 * Boolean to automatically fill empty cells before a table is rendered
 *  (takes CPU so may be set to false in case of certainty)
 */
    boolean mAutoFillEmptyCells = false;
    
/** If true this table may not be split over two pages. */
    boolean tableFitsPage = false;
    
/** If true cells may not be split over two pages. */
    boolean cellsFitPage = false;
    
/** This is the offset of the table. */
    float offset = Float.MIN_VALUE;
    
/** contains the attributes that are added to each odd (or even) row */
    protected Hashtable alternatingRowAttributes;
    
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
 * @param    attributes        Some attributes
 */
    
    public Table(Properties attributes) {
        // a Rectangle is create with BY DEFAULT a border with a width of 1
        super(0, 0, 0, 0);
        setBorder(BOX);
        setBorderWidth(1);
        
        String value = (String)attributes.remove(ElementTags.COLUMNS);
        if (value == null) {
            columns = 1;
        }
        else {
            columns = Integer.parseInt(value);
            if (columns <= 0) {
                columns = 1;
            }
        }
        
        rows.add(new Row(columns));
        currentRow = 0;
        
        if ((value = (String)attributes.remove(ElementTags.LASTHEADERROW)) != null) {
            setLastHeaderRow(Integer.parseInt(value));
        }
        if ((value = (String)attributes.remove(ElementTags.ALIGN)) != null) {
            setAlignment(value);
        }
        if ((value = (String)attributes.remove(ElementTags.CELLSPACING)) != null) {
            setSpacing(Float.valueOf(value + "f").floatValue());
        }
        if ((value = (String)attributes.remove(ElementTags.CELLPADDING)) != null) {
            setPadding(Float.valueOf(value + "f").floatValue());
        }
        if ((value = (String)attributes.remove(ElementTags.OFFSET)) != null) {
            setOffset(Float.valueOf(value + "f").floatValue());
        }
        if ((value = (String)attributes.remove(ElementTags.WIDTH)) != null) {
            if (value.endsWith("%"))
                setWidth(Float.valueOf(value.substring(0, value.length() - 1) + "f").floatValue());
            else
                setAbsWidth(value);
        }
        widths = new float[columns];
        for (int i = 0; i < columns; i++) {
            widths[i] = 0;
        }
        if ((value = (String)attributes.remove(ElementTags.WIDTHS)) != null) {
            StringTokenizer widthTokens = new StringTokenizer(value, ";");
            int i = 0;
            while (widthTokens.hasMoreTokens()) {
                value = (String) widthTokens.nextToken();
                widths[i] = Float.valueOf(value + "f").floatValue();
                i++;
            }
            columns = i;
        }
        if ((value = (String)attributes.remove(ElementTags.TABLEFITSPAGE)) != null) {
            tableFitsPage = new Boolean(value).booleanValue();
        }
        if ((value = (String)attributes.remove(ElementTags.CELLSFITPAGE)) != null) {
            cellsFitPage = new Boolean(value).booleanValue();
        }
        if ((value = (String)attributes.remove(ElementTags.BORDERWIDTH)) != null) {
            setBorderWidth(Float.valueOf(value + "f").floatValue());
        }
        int border = 0;
        if ((value = (String)attributes.remove(ElementTags.LEFT)) != null) {
            if (new Boolean(value).booleanValue()) border |= Rectangle.LEFT;
        }
        if ((value = (String)attributes.remove(ElementTags.RIGHT)) != null) {
            if (new Boolean(value).booleanValue()) border |= Rectangle.RIGHT;
        }
        if ((value = (String)attributes.remove(ElementTags.TOP)) != null) {
            if (new Boolean(value).booleanValue()) border |= Rectangle.TOP;
        }
        if ((value = (String)attributes.remove(ElementTags.BOTTOM)) != null) {
            if (new Boolean(value).booleanValue()) border |= Rectangle.BOTTOM;
        }
        setBorder(border);
        String r = null;
        String g = null;
        String b = null;
        if ((r = (String)attributes.remove(ElementTags.RED)) != null ||
        (g = (String)attributes.remove(ElementTags.GREEN)) != null ||
        (b = (String)attributes.remove(ElementTags.BLUE)) != null) {
            int red = 0;
            int green = 0;
            int blue = 0;
            if (r != null) red = Integer.parseInt(r);
            if (g != null) green = Integer.parseInt(g);
            if (b != null) blue = Integer.parseInt(b);
            setBorderColor(new Color(red, green, blue));
        }
        else if ((value = attributes.getProperty(ElementTags.BORDERCOLOR)) != null) {
            setBorderColor(ElementTags.decodeColor(value));
        }
        if ((r = (String)attributes.remove(ElementTags.BGRED)) != null ||
        (g = (String)attributes.remove(ElementTags.BGGREEN)) != null ||
        (b = (String)attributes.remove(ElementTags.BGBLUE)) != null) {
            int red = 0;
            int green = 0;
            int blue = 0;
            if (r != null) red = Integer.parseInt(r);
            if (g != null) green = Integer.parseInt(g);
            if (b != null) blue = Integer.parseInt(b);
            setBackgroundColor(new Color(red, green, blue));
        }
        else if ((value = (String)attributes.remove(ElementTags.BACKGROUNDCOLOR)) != null) {
            setBackgroundColor(ElementTags.decodeColor(value));
        }
        if ((value = (String)attributes.remove(ElementTags.GRAYFILL)) != null) {
            setGrayFill(Float.valueOf(value + "f").floatValue());
        }
        if (attributes.size() > 0) setMarkupAttributes(attributes);
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
 * Performs extra checks when executing table code (currently only when cells are added).
 */
    public void setDebug(boolean aDebug) {
        mDebug = aDebug;
    }
    
/**
 * Enables/disables automatic insertion of empty cells before table is rendered. (default = false)
 * As some people may want to create a table, fill only a couple of the cells and don't bother with
 * investigating which empty ones need to be added, this default behaviour may be very welcome.
 * Disabling is recommended to increase speed. (empty cells should be added through extra code then)
 *
 * @param       aDoAutoFill   enable/disable autofill
 */
    
    public void setAutoFillEmptyCells(boolean aDoAutoFill) {
        mAutoFillEmptyCells = aDoAutoFill;
    }
   
/**
 * Allows you to control when a page break occurs.
 * <P>
 * When a table doesn't fit a page, it is split in two parts.
 * If you want to avoid this, you should set the <VAR>tableFitsPage</VAR> value to true.
 *
 * @param   fitPage    enter true if you don't want to split cells
 */
 
    public void setTableFitsPage(boolean fitPage) {
        this.tableFitsPage = fitPage;
        if (fitPage) setCellsFitPage(true);
    }

/**
 * Allows you to control when a page break occurs.
 * <P>
 * When a cell doesn't fit a page, it is split in two parts.
 * If you want to avoid this, you should set the <VAR>cellsFitPage</VAR> value to true.
 *
 * @param   fitPage    enter true if you don't want to split cells
 */
 
    public void setCellsFitPage(boolean fitPage) {
        this.cellsFitPage = fitPage;
    }
    
/**
 * Checks if this <CODE>Table</CODE> has to fit a page.
 *
 * @return  true if the table may not be split
 */
 
    public boolean hasToFitPageTable() {
        return tableFitsPage;
    }
    
/**
 * Checks if the cells of this <CODE>Table</CODE> have to fit a page.
 *
 * @return  true if the cells may not be split
 */
 
    public boolean hasToFitPageCells() {
        return cellsFitPage;
    }
    
/**
 * Sets the offset of this table.
 *
 * Normally a newline is added before you add a Table object.
 * This newline uses the current leading.
 * If you want to control the space between the table and the previous
 * element yourself, you have to set the offset of this table.
 *
 * @param   offset  the space between this table and the previous object.
 */
    
    public void setOffset(float offset) {
        this.offset = offset;
    }
    
/**
 * Gets the offset of this table.
 *
 * @return  the space between this table and the previous element.
 */
    
    public float getOffset() {
        return offset;
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
 */
    
    public void addCell(Cell aCell, int row, int column) throws BadElementException {
        addCell(aCell, new Point(row,column));
    }
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE> at a certain location.
 *
 * @param       cell        The <CODE>Cell</CODE> to add
 * @param       location    The location where the <CODE>Cell</CODE> will be added
 */
    
    public void addCell(Cell aCell, Point aLocation) throws BadElementException {
        if (aCell == null) throw new NullPointerException("addCell - cell has null-value");
        if (aLocation == null) throw new NullPointerException("addCell - point has null-value");
        if (aCell.isTable()) insertTable((Table)aCell.getElements().next(), aLocation);
        if (mDebug == true) {
            if (aLocation.x < 0) throw new BadElementException("row coordinate of location must be >= 0");
            if ((aLocation.y <= 0) && (aLocation.y > columns)) throw new BadElementException("column coordinate of location must be >= 0 and < nr of columns");
            if (!isValidLocation(aCell, aLocation)) throw new BadElementException("Adding a cell at the location (" + aLocation.x + "," + aLocation.y + ") with a colspan of " + aCell.colspan() + " and a rowspan of " + aCell.rowspan() + " is illegal (beyond boundaries/overlapping).");
        }
        aCell.fill();
        placeCell(rows, aCell, aLocation);
        setCurrentLocationToNextValidPosition(aLocation);
    }
    
    
/**
 * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>.
 *
 * @param       cell         a <CODE>Cell</CODE>
 */
    
    public final void addCell(Cell cell) {
        try {
            addCell(cell, new Point(currentRow, currentColumn));
        }
        catch(BadElementException bee) {
            // don't add the cell
        }
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
 * generateTable will of course re-arrange the widths of the columns.
 *
 * @param   aTable      the table you want to insert
 */
    
    public void insertTable(Table aTable) {
        if (aTable == null) throw new NullPointerException("insertTable - table has null-value");
        insertTable(aTable, new Point(currentRow, currentColumn));
    }
    
/**
 * To put a table within the existing table at the given position
 * generateTable will of course re-arrange the widths of the columns.
 *
 * @param       aTable  The <CODE>Table</CODE> to add
 * @param       row     The row where the <CODE>Cell</CODE> will be added
 * @param       column  The column where the <CODE>Cell</CODE> will be added
 */
    
    public void insertTable(Table aTable, int row, int column) {
        if (aTable == null) throw new NullPointerException("insertTable - table has null-value");
        insertTable(aTable, new Point(row, column));
    }
    
/**
 * To put a table within the existing table at the given position
 * generateTable will of course re-arrange the widths of the columns.
 *
 * @param   aTable      the table you want to insert
 * @param   aLocation   a <CODE>Point</CODE>
 */
    public void insertTable(Table aTable, Point aLocation) {
        
        if (aTable == null) throw new NullPointerException("insertTable - table has null-value");
        if (aLocation == null) throw new NullPointerException("insertTable - point has null-value");
        mTableInserted = true;
        aTable.complete();
        if (mDebug == true) {
            if (aLocation.y > columns) System.err.println("insertTable -- wrong columnposition("+ aLocation.y + ") of location; max =" + columns);
        }
        int rowCount = aLocation.x + 1 - rows.size();
        int i = 0;
        if ( rowCount > 0 ) {   //create new rows ?
            for (; i < rowCount; i++) {
                rows.add(new Row(columns));
            }
        }
        
        ((Row) rows.get(aLocation.x)).setElement(aTable,aLocation.y);
        
        setCurrentLocationToNextValidPosition(aLocation);
    }
    
/*
 * Will fill empty cells with valid blank <CODE>Cell</CODE>s
 */
    
    public final void complete() {
        try {
            if (mTableInserted == true) {
                mergeInsertedTables();  // integrate tables in the table
                mTableInserted = false;
            }
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
        if (mAutoFillEmptyCells == true) {
            fillEmptyMatrixCells();
        }
        if (alternatingRowAttributes != null) {
            Properties even = new Properties();
            Properties odd = new Properties();
            String name;
            String[] value;
            for (Iterator iterator = alternatingRowAttributes.keySet().iterator(); iterator.hasNext(); ) {
                name = String.valueOf(iterator.next());
                value = (String[])alternatingRowAttributes.get(name);
                even.setProperty(name, value[0]);
                odd.setProperty(name, value[1]);
            }
            Row row;
            for (int i = lastHeaderRow + 1; i < rows.size(); i++) {
                row = (Row)rows.get(i);
                row.setMarkupAttributes(i % 2 == 0 ? even : odd);
            }
        }
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
 * Sets the unset cell properties to be the table defaults.
 *
 * @param aCell The cell to set to table defaults as necessary.
 */
    
    private void assumeTableDefaults(Cell aCell) {
        
        if (aCell.border() == Rectangle.UNDEFINED) {
            aCell.setBorder(defaultLayout.border());
        }
        if (aCell.borderWidth() == Rectangle.UNDEFINED) {
            aCell.setBorderWidth(defaultLayout.borderWidth());
        }
        if (aCell.borderColor() == null) {
            aCell.setBorderColor(defaultLayout.borderColor());
        }
        if (aCell.backgroundColor() == null) {
            aCell.setBackgroundColor(defaultLayout.backgroundColor());
        }
        if (aCell.grayFill() == Rectangle.UNDEFINED) {
            aCell.setGrayFill(defaultLayout.grayFill());
        }
        if (aCell.horizontalAlignment() == Element.ALIGN_UNDEFINED) {
            aCell.setHorizontalAlignment(defaultLayout.horizontalAlignment());
        }
        if (aCell.verticalAlignment() == Element.ALIGN_UNDEFINED) {
            aCell.setVerticalAlignment(defaultLayout.verticalAlignment());
        }
    }
    
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
        if (column == columns) {
            currentRow++;
            currentColumn = 0;
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
        /* patch sep 8 2001 Francesco De Milato */
        lastHeaderRow = currentRow - 1;
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
 * @param    alignment        the new alignment as a <CODE>String</CODE>
 */
    
    public final void setAlignment(String alignment) {
        if (ElementTags.ALIGN_LEFT.equalsIgnoreCase(alignment)) {
            this.alignment = Element.ALIGN_LEFT;
            return;
        }
        if (ElementTags.RIGHT.equalsIgnoreCase(alignment)) {
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
    
    public final void setSpaceInsideCell(float value) {
        cellpadding = value;
    }
    
/**
 * Sets the cellspacing.
 *
 * @param       value   the new value
 */
    
    public final void setSpaceBetweenCells(float value) {
        cellspacing = value;
    }
    
/**
 * Sets the cellpadding.
 *
 * @param       value   the new value
 */
    
    public final void setPadding(float value) {
        cellpadding = value;
    }
    
/**
 * Sets the cellspacing.
 *
 * @param       value   the new value
 */
    
    public final void setSpacing(float value) {
        cellspacing = value;
    }
    
/**
 * Sets the cellspacing (the meaning of cellpadding and cellspacing was inverted by mistake).
 *
 * @param       value   the new value
 * @deprecated  use setSpacing instead
 */
    
    public final void setCellpadding(float value) {
        cellspacing = value;
    }
    
/**
 * Sets the cellpadding (the meaning of cellpadding and cellspacing was inverted by mistake).
 *
 * @param       value   the new value
 * @deprecated  use setPadding instead
 */
    
    public final void setCellspacing(float value) {
        cellpadding = value;
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
 * Gets the number of columns.
 *
 * @return    a value
 */
    
    public final int columns() {
        return columns;
    }
    
/**
 * Gets the number of rows in this <CODE>Table</CODE>.
 *
 * @return      the number of rows in this <CODE>Table</CODE>
 */
    
    public final int size() {
        return rows.size();
    }
    
/**
 * Gets the proportional widths of the columns in this <CODE>Table</CODE>.
 *
 * @return      the proportional widths of the columns in this <CODE>Table</CODE>
 */
    
    public final float[] getProportionalWidths() {
        return widths;
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
 */
    
    public float widthPercentage() {
        return widthPercentage;
    }
    
/**
 * Gets the table width (in pixels).
 *
 * @return  the table width
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
 */
    
    public Dimension getDimension() {
        return new Dimension(columns, rows.size());
    }
    
/**
 * returns the element at the position row, column
 *          (Cast to Cell or Table)
 *
 * @return  dimension
 */
    
    public Object getElement(int row, int column) {
        return ((Row) rows.get(row)).getCell(column);
    }
    
/**
 * Integrates all added tables and recalculates column widths.
 */
    
    private void mergeInsertedTables() throws DocumentException {
        int i=0, j=0;
        float [] lNewWidths = null;
        int [] lDummyWidths = new int[columns];     // to keep track in how many new cols this one will be split
        float[][] lDummyColumnWidths = new float[columns][]; // bugfix Tony Copping
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
                        lDummyColumnWidths[j] = lDummyTable.widths; // bugfix Tony Copping
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
        
        if ( (lTotalColumns != columns) || (lTotalRows != rows.size()) )    // NO ADJUSTMENT
        {
            // ** WIDTH
            // set correct width for new columns
            // divide width over new nr of columns
            lNewWidths = new float [lTotalColumns];
            int lDummy = 0;
            for (int tel=0; tel < widths.length;tel++) {
                if ( lDummyWidths[tel] != 1) {
                    // divide
                    for (int tel2 = 0; tel2 < lDummyWidths[tel]; tel2++) {
                        // lNewWidths[lDummy] = widths[tel] / lDummyWidths[tel];
                        lNewWidths[lDummy] = widths[tel] * lDummyColumnWidths[tel][tel2] / 100f; // bugfix Tony Copping
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
 * adds new<CODE>Cell</CODE>'s to empty/null spaces.
 */
    
    private void fillEmptyMatrixCells() {
        Cell lDummyCell = null;
        int  lTel = -1;
        Object obj = new Object();
        try {
            for (int i=0; i < rows.size(); i++) {
                for (int j=0; j < columns; j++) {
                    if ( ((Row) rows.get(i)).isReserved(j) == false) {
                        addCell(defaultLayout, new Point(i, j));
                    }
                }
            }
        }
        catch(BadElementException bee) {
            throw new ExceptionConverter(bee);
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
 */
    private boolean isValidLocation(Cell aCell, Point aLocation)
    {
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
 */
    
    private void placeCell(ArrayList someRows, Cell aCell, Point aPosition) {
        int i,j;
        Row row = null;
        int lColumns = ((Row) someRows.get(0)).columns();
        int rowCount = aPosition.x + aCell.rowspan() - someRows.size();
        assumeTableDefaults(aCell);
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
                throw new RuntimeException("addCell - error in reserve");
            }
        }
        row = (Row) someRows.get(aPosition.x);
        row.addElement(aCell, aPosition.y);
        
    }
    
/**
 * Gives you the posibility to add columns.
 *
 * @param   aColumns    the number of columns to add
 */
    
    public void addColumns(int aColumns) {
        ArrayList newRows = new ArrayList(rows.size());
        
        int newColumns = columns + aColumns;
        Row row;
        for (int i = 0; i < rows.size(); i++) {
            row = new Row(newColumns);
            for (int j = 0; j < columns; j++) {
                row.setElement(((Row) rows.get(i)).getCell(j) ,j);
            }
            for (int j = columns; j < newColumns && i < currentRow; j++) {
                row.setElement(defaultLayout, j);
            }
            newRows.add(row);
        }
        
        // applied 1 column-fix; last column needs to have a width of 0
        float [] newWidths = new float[newColumns];
        for (int j = 0; j < columns; j++) {
            newWidths[j] = widths[j];
        }
        for (int j = columns; j < newColumns ; j++) {
            newWidths[j] = 0;
        }
        columns = newColumns;
        widths = newWidths;
        rows = newRows;
    }
    
/**
 * Gives you the possibility to add columns.
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
 * returns the element at a given location.
 */
    
    private Object getElement(ArrayList al, int row, int column) {
        return ((Row) al.get(row)).getCell(column);
    }
    
/**
 * returns the element type name (Cell, Tabl, Objt).
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
 * an element in the table at point(aRow, aColumn).
 */
    
    private String getElementType(int aRow, int aColumn) {
        return getElementType(rows,aRow,aColumn);
    }
    
/**
 * returns the element type name (Cell, Tabl, Objt) of
 * an element in the table at point(aRow, aColumn).
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
 *  Sets current col/row to valid(empty) pos after addCell/Table
 */
    private void setCurrentLocationToNextValidPosition(Point aLocation)    {
        // set latest location to next valid position
        int i, j;
        i = aLocation.x;
        j = aLocation.y;
        do {
            if ( (j + 1)  == columns ) {    // goto next row
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
        currentRow        = i;
        currentColumn    = j;
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
 *    Only for debugging purposes: printing table's structure and contents.
 */
    
    private void printTableMatrix()
    {
        printTableMatrix(rows);
    }
    
/**
 *    Only for debugging purposes: printing table's structure and contents.
 */
    
    private void printTableMatrix(ArrayList aAl)
    {
        printReserved(aAl);
        StringBuffer lLine = null;
        int lLineNumberCount = -1;
        int lColumns = ((Row) aAl.get(0)).columns();
        for (int i=0; i < aAl.size();i++)
        {
            // rownumber
            lLine = new StringBuffer("Row ");
            lLineNumberCount = new Integer(i).toString().length(); // alignment for three chars
            for(int s=0; s < (3 - lLineNumberCount);s++)
            {
                lLine.append(" ");
            }
            lLine.append(i + ": ");
            for (int j=0; j < lColumns;j++)
            {
                lLine.append(" " + getElementType(aAl,i,j));
            }
        }
        printTableMatrixContents();
    }
    
    private void printReserved(ArrayList aAl) {
        Row lRow = null;
        String lStatus = null;
        for (int i=0; i < aAl.size();i++)
        {
            lStatus = new String();
            lRow = (Row) aAl.get(i);
            for (int j=0; j < lRow.columns();j++)
            {
                lStatus += lRow.isReserved(j) + "  ";
            }
        }
    }
    
/**
 * Method to briefly print all cell contents, an aid when debugging.
 */
    
    private void printTableMatrixContents()
    {
        int lineNumberCount = -1;
        Object lElement = null;
        StringBuffer lLine = null;
        for (int i=0; i < rows.size();i++)
        {
            // rownumber
            lLine = new StringBuffer("Row ");
            lineNumberCount = new Integer(i).toString().length(); // alignment for three chars
            for(int s=0; s < (3 - lineNumberCount);s++)
            {
                lLine.append(" ");
            }
            lLine.append(i + ": ");
            for (int j=0; j < columns;j++)
            {
                lElement = ((Row) rows.get(i)).getCell(j);
                if ( Cell.class.isInstance(lElement) )
                {
                    ArrayList al = ((Cell) lElement).getChunks();
                    if (al.size() > 0)
                    {
                        if (((Chunk) al.get(0)).content().length() >= 6)
                        {
                            lLine.append(" - " + ((Chunk) al.get(0)).content().substring(0,6) + " - ");
                        }
                        else
                        {
                            lLine.append(" - " +((Chunk) al.get(0)).content().substring(0,((Chunk) al.get(0)).content().length()) + " - ");
                        }
                    }
                }
                else if ( Table.class.isInstance(lElement) )
                {
                    lLine.append(" - Table  - ");
                }
                else if ( Object.class.isInstance(lElement) )
                {
                    lLine.append(" - Object - ");
                }
                else if (lElement == null)
                {
                    lLine.append(" - null   - ");
                }
                else
                {
                    lLine.append("- other  -");
                }
            }
        }
    }
    
/**
 * Allows clients to set up alternating attributes for each Row in the Table.
 * <P>
 * This code was contributed by Matt Benson.
 *
 * @param   name    the name of the attribute
 * @param   value0  the value of the attribute for even rows
 * @param   value1  the value of the attribute for odd rows
 */
    public void setAlternatingRowAttribute(String name, String value0, String value1) {
        if (value0 == null || value1 == null) {
            throw new NullPointerException("MarkupTable#setAlternatingRowAttribute(): null values are not permitted.");
        }
        alternatingRowAttributes = (alternatingRowAttributes == null) ?  new Hashtable() : alternatingRowAttributes;
        
        // we could always use new Arrays but this is big enough
        String[] value = (String[])(alternatingRowAttributes.get(name));
        value = (value == null) ? new String[2] : value;
        value[0] = value0;
        value[1] = value1;
        alternatingRowAttributes.put(name, value);
    }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public float top() {
       throw new UnsupportedOperationException("Dimensions of a Table can't be calculated. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public float bottom() {
       throw new UnsupportedOperationException("Dimensions of a Table can't be calculated. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public float left() {
       throw new UnsupportedOperationException("Dimensions of a Table can't be calculated. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public float right() {
       throw new UnsupportedOperationException("Dimensions of a Table can't be calculated. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public float top(int margin) {
       throw new UnsupportedOperationException("Dimensions of a Table can't be calculated. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public float bottom(int margin) {
       throw new UnsupportedOperationException("Dimensions of a Table can't be calculated. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public float left(int margin) {
       throw new UnsupportedOperationException("Dimensions of a Table can't be calculated. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public float right(int margin) {
       throw new UnsupportedOperationException("Dimensions of a Table can't be calculated. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public void setTop(int value) {
       throw new UnsupportedOperationException("Dimensions of a Table are attributed automagically. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public void setBottom(int value) {
       throw new UnsupportedOperationException("Dimensions of a Table are attributed automagically. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public void setLeft(int value) {
       throw new UnsupportedOperationException("Dimensions of a Table are attributed automagically. See the FAQ.");
   }
    
/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 */
   public void setRight(int value) {
       throw new UnsupportedOperationException("Dimensions of a Table are attributed automagically. See the FAQ.");
   }
}
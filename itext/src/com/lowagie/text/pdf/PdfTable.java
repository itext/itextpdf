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

package com.lowagie.text.pdf;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.lowagie.text.Rectangle;
import com.lowagie.text.Cell;
import com.lowagie.text.Row;
import com.lowagie.text.Table;

/**
 * <CODE>PdfTable</CODE> is an object that contains the graphics and text of a table.
 *
 * @see		com.lowagie.text.Table
 * @see		com.lowagie.text.Row
 * @see		com.lowagie.text.Cell
 * @see		PdfCell
 */

class PdfTable extends Rectangle {
    
    // membervariables
    
/** this is the ArrayList with all the cell of the table header. */
    private ArrayList headercells;
    
/** this is the ArrayList with all the cells in the table. */
    private ArrayList cells;
    
/** this is the cellpadding of the table. */
    private float cellspacing;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfTable</CODE>-object.
 *
 * @param	table	a <CODE>Table</CODE>
 * @param	left	the left border on the page
 * @param	right	the right border on the page
 * @param	top		the start position of the top of the table
 */
    
    PdfTable(Table table, float left, float right, float top) {
        
        // constructs a Rectangle (the bottomvalue will be changed afterwards)
        super(left, top, right, top);
        setBorder(table.border());
        setBorderWidth(table.borderWidth());
        setBorderColor(table.borderColor());
        setBackgroundColor(table.backgroundColor());
        setGrayFill(table.grayFill());
        
        // initialisation of some parameters
        this.cellspacing = table.cellspacing();
        float[] positions = table.getWidths(left, right - left);
        setLeft(positions[0]);
        setRight(positions[positions.length - 1]);
        
        Row row;
        int rowNumber = 0;
        int firstDataRow = table.firstDataRow();
        Cell cell;
        PdfCell currentCell;
        headercells = new ArrayList();
        cells = new ArrayList();
        int rows = table.size() + 1;
        float[] offsets = new float[rows];
        for (int i = 0; i < rows; i++) {
            offsets[i] = top - cellspacing;
        }
        
        // loop over all the rows
        for (Iterator rowIterator = table.iterator(); rowIterator.hasNext(); ) {
            row = (Row) rowIterator.next();
            if (! row.isEmpty()) {
                for(int i = 0; i < row.columns(); i++) {
                    cell = (Cell) row.getCell(i);
                    if (cell != null) {
                        currentCell = new PdfCell(cell, rowNumber, positions[i], positions[i + cell.colspan()], offsets[rowNumber], table.cellpadding(), cellspacing);
                        try {
                            if (offsets[rowNumber] - currentCell.height() - cellspacing < offsets[rowNumber + currentCell.rowspan()]) {
                                offsets[rowNumber + currentCell.rowspan()] = offsets[rowNumber] - currentCell.height() - cellspacing;
                            }
                        }
                        catch(ArrayIndexOutOfBoundsException aioobe) {
                            if (offsets[rowNumber] - currentCell.height() < offsets[offsets.length - 1]) {
                                offsets[offsets.length - 1] = offsets[rowNumber] - currentCell.height();
                            }
                        }
                        if (rowNumber < firstDataRow) {
                            currentCell.setHeader();
                            headercells.add(currentCell);
                        }
                        cells.add(currentCell);
                    }
                }
            }
            rowNumber++;
        }
        
        // loop over all the cells
        int n = cells.size();
        for (int i = 0; i < n; i++) {
            currentCell = (PdfCell) cells.get(i);
            try {
                currentCell.setBottom(offsets[currentCell.rownumber() + currentCell.rowspan()]);
            }
            catch(ArrayIndexOutOfBoundsException aioobe) {
                currentCell.setBottom(offsets[offsets.length - 1]);
            }
        }
        setBottom(offsets[offsets.length - 1] - cellspacing);
    }
    
    // methods
    
/**
 * Returns the arraylist with the cells of the table header.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    ArrayList getHeaderCells() {
        return headercells;
    }
    
/**
 * Checks if there is a table header.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    boolean hasHeader() {
        return headercells.size() > 0;
    }
    
/**
 * Returns the arraylist with the cells of the table.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    ArrayList getCells() {
        return cells;
    }
    
/**
 * Returns the cellpadding of the table.
 *
 * @return	the cellpadding
 */
    
    float cellspacing() {
        return cellspacing;
    }
}
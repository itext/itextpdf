/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 Bruno Lowagie
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
    
/** this is the number of columns in the table. */
    private int columns;
    
/** this is the ArrayList with all the cell of the table header. */
    private ArrayList headercells;
    
/** this is the ArrayList with all the cells in the table. */
    private ArrayList cells;
    
/** this is the cellpadding of the table. */
    private float cellpadding;
    
/** this is the cellspacing of the table. */
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
        
        // copying the attributes from class Table
        setBorder(table.border());
        setBorderWidth(table.borderWidth());
        setBorderColor(table.borderColor());
        setBackgroundColor(table.backgroundColor());
        setGrayFill(table.grayFill());
        this.columns = table.columns();
        this.cellpadding = table.cellpadding();
        this.cellspacing = table.cellspacing();
        float[] positions = table.getWidths(left, right - left);
        
        // initialisation of some parameters
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
            offsets[i] = top;
        }
        
        // loop over all the rows
        for (Iterator rowIterator = table.iterator(); rowIterator.hasNext(); ) {
            row = (Row) rowIterator.next();
            if (row.isEmpty()) {
                if (rowNumber < rows - 1 && offsets[rowNumber + 1] > offsets[rowNumber]) offsets[rowNumber + 1] = offsets[rowNumber];
            }
            else {
                for(int i = 0; i < row.columns(); i++) {
                    cell = (Cell) row.getCell(i);
                    if (cell != null) {
                        currentCell = new PdfCell(cell, rowNumber, positions[i], positions[i + cell.colspan()], offsets[rowNumber], cellspacing, cellpadding);                        
                        try {
                            if (offsets[rowNumber] - currentCell.height() - cellpadding < offsets[rowNumber + currentCell.rowspan()]) {
                                offsets[rowNumber + currentCell.rowspan()] = offsets[rowNumber] - currentCell.height() - cellpadding;
                            }
                        }
                        catch(ArrayIndexOutOfBoundsException aioobe) {
                            if (offsets[rowNumber] - currentCell.height() < offsets[rows - 1]) {
                                offsets[rows - 1] = offsets[rowNumber] - currentCell.height();
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
                currentCell.setBottom(offsets[rows - 1]);
            }
        }
        setBottom(offsets[rows - 1]);
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
 * Returns the number of columns of the table.
 *
 * @return	the number of columns
 */
    
    int columns() {
        return columns;
    }
    
/**
 * Returns the cellpadding of the table.
 *
 * @return	the cellpadding
 */
    
    float cellpadding() {
        return cellpadding;
    }
    
/**
 * Returns the cellspacing of the table.
 *
 * @return	the cellspacing
 */
    
    float cellspacing() {
        return cellspacing;
    }
}
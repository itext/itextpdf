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
import java.util.ArrayList;

/**
 * A <CODE>Row</CODE> is part of a <CODE>Table</CODE>
 * and contains some <CODE>Cells</CODE>.
 * <P>
 * All <CODE>Row</CODE>s are constructed by a <CODE>Table</CODE>-object.
 * You don't have to construct any <CODE>Row</CODE> yourself.
 * In fact you can't construct a <CODE>Row</CODE> outside the package.
 * <P>
 * Since a <CODE>Cell</CODE> can span several rows and/or columns
 * a row can contain reserved space without any content.
 *
 * @see		Element
 * @see		Cell
 * @see		Table
 *
 * @author  bruno@lowagie.com
 */

public class Row implements Element {

// membervariables

	/** This is the number of columns in the <CODE>Row</CODE>. */
	private int columns;
	
	/** This is the array that keeps track of reserved cells. */
	private boolean[] reserved;

	/** This is the array of <CODE>Cell</CODE>s. */
	private Cell[] cells;

	/** This is the vertical alignment. */
	private int horizontalAlignment;

	/** This is the vertical alignment. */
	private int verticalAlignment;

// constructors

	/**
	 * Constructs a <CODE>Row</CODE> with a certain number of <VAR>columns</VAR>.
	 *
	 * @param	columns		a number of columns
	 */

	Row(int columns) {
		this.columns = columns;
		reserved = new boolean[columns];
		cells = new Cell[columns];
	}

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to a
	 * <CODE>ElementListener</CODE>. 
     *
	 * @param	listener	an <CODE>ElementListener</CODE>
	 * @return	<CODE>true</CODE> if the element was processed successfully
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
     * @return	a type
     */

    public final int type() {
		return Element.ROW;
	}		

    /**
     * Gets all the chunks in this element. 
     *
     * @return	an <CODE>ArrayList</CODE>
     */

    public final ArrayList getChunks() {
		 return new ArrayList();
	}

	/**
	 * Returns a <CODE>Row</CODE> that is a copy of this <CODE>Row</CODE>
	 * in which a certain column has been deleted.
	 *
	 * @param	column	the number of the column to delete
	 */

	final void deleteColumn(int column) {
		columns--;				  
		boolean newReserved[] = new boolean[columns];
		Cell newCells[] = new Cell[columns];

		for (int i = 0; i < column; i++) {
			newReserved[i] = reserved[i];
			newCells[i] = cells[i];
			if (newCells[i] != null && (i + newCells[i].colspan() > column)) {
				newCells[i].setColspan(cells[i].colspan() - 1);
			}
		}
		for (int i = column; i < columns; i++) {
			newReserved[i] = reserved[i + 1];
			newCells[i] = cells[i + 1];
		}
		if (cells[column] != null && cells[column].colspan() > 1) {
			newCells[column] = cells[column];
			newCells[column].setColspan(newCells[column].colspan() - 1);
		}
		reserved = newReserved;
		cells = newCells;
	}

// methods

	/**
	 * Adds a <CODE>Cell</CODE> to the <CODE>Row</CODE>.
	 * 
	 * @param	cell	the cell to add.
	 * @return	the column position the <CODE>Cell</CODE> was added,
	 *			or <CODE>-1</CODE> if the <CODE>Cell</CODE> couldn't be added.
	 */

	final int addCell(Cell cell) {
		// loops over the columns untill a free column is found
		for (int i = 0; i < columns; i++) {
			// if the column is free, the cell is added
			if (!reserved[i]) {
				reserved[i] = true;
				cells[i] = cell;
				return i;
			}
		}
		// the cell couldn't be added
		return -1;
	}

	/**
	 * Reserves a <CODE>Cell</CODE> in the <CODE>Row</CODE>.
	 *
	 * @param	column	the column that has to be reserved.
	 * @return	<CODE>true</CODE> if the column was reserved, <CODE>false</CODE> if not.
	 */

	final boolean reserve(int column) {
		// if the column couldn't be reserved, we return false;
		if (column >= columns || reserved[column]) {
			return false;
		}
		// if the reservation succeeds, we return true
		reserved[column] = true;
		return true;
	}

    /**
     * Sets the horizontal alignment. 
     *
	 * @param	value	the new value
     */

    public void setHorizontalAlignment(int value) {
		horizontalAlignment = value;
	}

    /**
     * Sets the vertical alignment. 
     *
	 * @param	value	the new value
     */

    public void setVerticalAlignment(int value) {
		verticalAlignment = value;
	}

// methods to retrieve information

	/**
	 * Gets a <CODE>Cell</CODE> from a certain column.
	 *
	 * @param	column	the column the <CODE>Cell</CODE> is in.
	 * @return	the <CODE>Cell</CODE> or <VAR>null</VAR> if the column was
	 *			reserved or empty.
	 */

	public final Cell getCell(int column) {
		if (column >= columns) {
			return null;
		}
		return cells[column];
	}

	/**
	 * Checks if the row is empty.
	 *
	 * @return	<CODE>true</CODE> if none of the columns is reserved.
	 */

	public final boolean isEmpty() {
		for (int i = 0; i < columns; i++) {
			if (reserved[i]) {
				return false;
			}
		}
		return true;
	}

    /**
     * Gets the number of columns. 
     *
     * @return	a value
     */

    public final int columns() {
		return columns;
	}

    /**
     * Gets the horizontal alignment. 
     *
     * @return	a value
     */

    public int horizontalAlignment() {
		return horizontalAlignment;
	}

    /**
     * Gets the vertical alignment. 
     *
     * @return	a value
     */

    public int verticalAlignment() {
		return verticalAlignment;
	}

	/**
	 * Returns a representation of this <CODE>Row</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 */

	public String toString() {
		if (isEmpty()) {
			return "";
		}
		StringBuffer buf = new StringBuffer("\t<ROW COLUMNS=\"");
		buf.append(columns);
		buf.append(">\n");
		buf.append("\n\t\t<HORIZONTAL_ALIGNMENT>");
		switch(horizontalAlignment) {
		case Element.ALIGN_LEFT:
			buf.append("Left");
			break;
		case Element.ALIGN_CENTER:
			buf.append("Center");
			break;
		case Element.ALIGN_RIGHT:
			buf.append("Right");
			break;
		case Element.ALIGN_JUSTIFIED:
			buf.append("Justify");
			break;
		default:
			buf.append("Default");
		}
		buf.append("</HORIZONTAL_ALIGNMENT>\n");
		buf.append("\t\t<VERTICAL_ALIGNMENT>");
		switch(verticalAlignment) {
		case Element.ALIGN_TOP:
			buf.append("Top");
			break;
		case Element.ALIGN_MIDDLE:
			buf.append("Middle");
			break;
		case Element.ALIGN_BOTTOM:
			buf.append("Bottom");
			break;
		case Element.ALIGN_BASELINE:
			buf.append("Baseline");
			break;
		default:
			buf.append("Default");
		}
		buf.append("</VERTICAL_ALIGNMENT>\n");
		Cell cell;
		for (int i = 0; i < columns; i++) {
			cell = cells[i];
			if (cell != null) {
				buf.append(cell.toString());
			}
		}
		buf.append("\n\t</ROW>\n");								
		return buf.toString();
	}
}

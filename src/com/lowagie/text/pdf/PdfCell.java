/*
 * @(#)PdfCell.java				0.29 2000/02/13
 *       release iText0.3:		0.28 2000/02/14
 *       release iText0.35:		0.28 2000/08/11
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
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

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;						 
import com.lowagie.text.Element;						 
import com.lowagie.text.List;						 
import com.lowagie.text.ListItem;						 
import com.lowagie.text.Rectangle;

/**
 * A <CODE>PdfCell</CODE> is the PDF translation of a <CODE>Cell</CODE>.
 * <P>
 * A <CODE>PdfCell</CODE> is an <CODE>ArrayList</CODE> of <CODE>PdfLine</CODE>s.
 *
 * @see		com.lowagie.text.Rectangle
 * @see		com.lowagie.text.Cell
 * @see		PdfLine
 * @see		PdfTable
 *
 * @author  bruno@lowagie.com
 * @version 0.29 2000/02/13
 * @since   iText0.30
 */

public class PdfCell extends Rectangle {

// membervariables

	/** These are the PdfLines in the Cell. */
	private ArrayList lines;

	/** This is the leading of the lines. */
	private int leading;

	/** This is the number of the row the cell is in. */
	private int rownumber;

	/** This is the rowspan of the cell. */
	private int rowspan;

	/** This is the cellspacing of the cell. */
	private int cellpadding;

	/** This is the cellpadding of the cell. */
	private int cellspacing;

	/** Indicates if this cell belongs to the header of a <CODE>PdfTable</CODE> */
	private boolean header = false;

// constructors

	/**
	 * Constructs a <CODE>PdfCell</CODE>-object.
	 *
	 * @param	cell		the original <CODE>Cell</CODE>
	 * @param	rownumber	the number of the <CODE>Row</CODE> the <CODE>Cell</CODE> was in.
	 * @param	left		the left border of the <CODE>PdfCell</CODE>
	 * @param	right		the right border of the <CODE>PdfCell</CODE>
	 * @param	top			the top border of the <CODE>PdfCell</CODE>
	 * @param	cellspacing	the cellspacing of the <CODE>Table</CODE>
	 * @param	cellpadding	the cellpadding	of the <CODE>Table</CODE>
	 *
	 * @since	iText0.30
	 */

	public PdfCell(Cell cell, int rownumber, int left, int right, int top, int cellspacing, int cellpadding) {
		// initialisation of the Rectangle
		super(left, top, right, top);
		setBorder(cell.border());
		setBorderWidth(cell.borderWidth());
		setBorderColor(cell.borderColor());
		setBackgroundColor(cell.backgroundColor());
		setGrayFill(cell.grayFill());

		// initialisation of the lines
		PdfChunk chunk;
		Element element;
		PdfChunk overflow;
		lines = new ArrayList();
		leading = cell.leading();
		int alignment = cell.horizontalAlignment();
		left += cellspacing + cellpadding;
		right -= cellspacing + cellpadding;
		PdfLine line = new PdfLine(left, right, alignment, leading + cellspacing);
		// we loop over all the elements of the cell
		for (Iterator i = cell.getElements(); i.hasNext(); ) {
			element = (Element) i.next();

			switch(element.type()) {
			// if the element is a list
			case Element.LIST:
				if (line.size() > 0) {
					line.resetAlignment();
					lines.add(line);
				}
				ListItem item;
				// we loop over all the listitems
				for (Iterator items = ((List)element).getItems().iterator(); items.hasNext(); ) {
					item = (ListItem) items.next();
					line = new PdfLine(left + item.indentationLeft(), right, alignment, leading);
					line.setListItem(item);
					for (Iterator j = item.getChunks().iterator(); j.hasNext(); ) {
						chunk = new PdfChunk((Chunk) j.next());
						while ((overflow = line.add(chunk)) != null) {
							lines.add(line);
							line = new PdfLine(left + item.indentationLeft(), right, alignment, leading);
							chunk = overflow;
						}
						line.resetAlignment();
						lines.add(line);
						line = new PdfLine(left + item.indentationLeft(), right, alignment, leading);
					}
				}
				line = new PdfLine(left, right, alignment, leading);
				break;
			// if the element is something else
			default:
				// we loop over the chunks
				for (Iterator j = element.getChunks().iterator(); j.hasNext(); ) {
					chunk = new PdfChunk((Chunk) j.next());
					while ((overflow = line.add(chunk)) != null) {
						lines.add(line);
						line = new PdfLine(left, right, alignment, leading);
						chunk = overflow;
					}
				}
				// if the element is a paragraph, section or chapter, we reset the alignment and add the line
				switch (element.type()) {
				case Element.PARAGRAPH:
				case Element.SECTION:
				case Element.CHAPTER:
					line.resetAlignment();
					lines.add(line);
					line = new PdfLine(left, right, alignment, leading);
				}
			}
		}
		if (line.size() > 0) {
			lines.add(line);
		} 
		if (lines.size() > 0) {
			((PdfLine)lines.get(lines.size() - 1)).resetAlignment();
		}

		// we set some additional parameters
		setBottom(top - leading * lines.size() - 5 * cellspacing / 2);
		this.cellpadding = cellpadding;
		this.cellspacing = cellspacing;

		rowspan = cell.rowspan();
		this.rownumber = rownumber;
	}
	
// overriding of the Rectangle methods

	/**
	 * Returns the lower left x-coordinaat.
	 *
	 * @return		the lower left x-coordinaat
	 *
	 * @since		iText0.30
	 */

	public int left() {
		return super.left(cellpadding);
	}

	/**
	 * Returns the upper right x-coordinate.
	 *
	 * @return		the upper right x-coordinate
	 *
	 * @since		iText0.30
	 */

	public int right() {
		return super.right(cellpadding);
	}

	/**
	 * Returns the upper right y-coordinate.
	 *
	 * @return		the upper right y-coordinate
	 *
	 * @since		iText0.30
	 */

	public int top() {
		return super.top(cellpadding);
	}

	/**
	 * Returns the lower left y-coordinate.
	 *
	 * @return		the lower left y-coordinate
	 *
	 * @since		iText0.30
	 */

	public int bottom() {
		return super.bottom(cellpadding);
	}

// methods

	/**
	 * Gets the lines of a cell that can be drawn between certain limits.
	 * <P>
	 * Remark: all the lines that can be drawn are removed from the object!
	 *
	 * @param	top		the top of the part of the table that can be drawn
	 * @param	bottom	the bottom of the part of the table that can be drawn
	 * @return	an <CODE>ArrayList</CODE> of <CODE>PdfLine</CODE>s
	 *
	 * @since	iText0.30
	 */

	public ArrayList getLines(int top, int bottom) {
		// if the bottom of the page is higher than the top of the cell: do nothing
		if (top() < bottom) {
			return null;
		}

		// initialisations
		PdfLine line;
		int lineHeight;
		int currentPosition = Math.min(top(), top);
		ArrayList result = new ArrayList();

		// we loop over the lines
		int size = lines.size();
		for (int i = 0; i < size; i++) {
			line = (PdfLine) lines.get(i);
			lineHeight = line.height();
			currentPosition -= lineHeight;
			// if the currentPosition is higher than the bottom, we add the line to the result
			if (currentPosition > bottom) {
				result.add(line);
				// as soon as a line is part of the result, we blank it out, except for table headers
				if (!header) {
					lines.set(i, new PdfLine(left(-cellpadding - cellspacing), right(-cellpadding - cellspacing), Element.ALIGN_LEFT, leading));
				}
			}
		}
		// if the bottom of the cell is higher than the bottom of the page, the cell is written, so we can remove all lines
		// bugfix solving an endless loop problem by Leslie Baski
		if (!header && bottom <= bottom()) {
			lines = new ArrayList();
		}
		return result;
	}

	/**
	 * Indicates that this cell belongs to the header of a <CODE>PdfTable</CODE>.
	 *
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	final void setHeader() {
		header = true;
	}

	/**
	 * Checks if the cell may be removed.
	 * <P>
	 * Headers may allways be removed, even if they are drawn only partially:
	 * they will be repeated on each following page anyway!
	 *
	 * @return	<CODE>true</CODE> if all the lines are allready drawn; <CODE>false</CODE> otherwise.
	 *
	 * @since	iText0.30
	 */

	final boolean mayBeRemoved() {
		return (header || lines.size() < 1);
	}

	/**
	 * Returns the number of lines in the cell.
	 *
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	public int size() {
		return lines.size();
	}

// methods to retrieve membervariables							   

	/**
	 * Gets the leading of a cell. 
	 *
	 * @return	the leading of the lines is the cell.
	 *
	 * @since	iText0.30
	 */

	public int leading() {
		return leading;
	}

	/**
	 * Gets the number of the row this cell is in.. 
	 *
	 * @return	a number
	 *
	 * @since	iText0.30
	 */

	public int rownumber() {
		return rownumber;
	}							   

	/**
	 * Gets the rowspan of a cell. 
	 *
	 * @return	the rowspan of the cell
	 *
	 * @since	iText0.30
	 */

	public int rowspan() {
		return rowspan;
	}

	/**
	 * Gets the cellspacing of a cell. . 
	 *
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	public int cellspacing() {
		return cellspacing;
	}

	/**
	 * Gets the cellpadding of a cell.. 
	 *
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	public int cellpadding() {
		return cellpadding;
	}
}
/*
 * @(#)PdfLine.java					0.24 2000/02/08
 *               iText0.3:			0.24 2000/02/14
 *               iText0.35:			0.24 2000/08/11
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

import java.util.ArrayList;
import java.util.Iterator;

import com.lowagie.text.Element;
import com.lowagie.text.Chunk;
import com.lowagie.text.ListItem;

/**
 * <CODE>PdfLine</CODE> defines an array with <CODE>PdfChunk</CODE>-objects
 * that fit into 1 line.
 *
 * @author  bruno@lowagie.com
 * @version 0.24 2000/02/08
 * @since   rugPdf0.10
 */

public class PdfLine {

// membervariables

	/** The arraylist containing the chunks. */
	protected ArrayList line = new ArrayList();
								
	/** The left indentation of the line. */
	protected int left;

	/** The width of the line. */
	protected int width;

	/** The alignment of the line. */
	protected int alignment;

	/** The heigth of the line. */
	protected int height;

	/** The listsymbol (if necessary). */
	protected PdfChunk listSymbol = null;

	/** The listsymbol (if necessary). */
	protected int symbolIndent;

// constructors

	/**
	 * Constructs a new <CODE>PdfLine</CODE>-object.
	 *
	 * @param	left		the limit of the line at the left  
	 * @param	right		the limit of the line at the right
	 * @param	alignment	the alignment of the line
	 * @param	height		the height of the line
	 *
	 * @since   iText0.30
	 */

	PdfLine(int left, int right, int alignment, int height) {
		this.left = left;
		this.width = right - left;
		this.alignment = alignment;
		this.height = height;
	}

// methods

	/**
	 * Adds a <CODE>PdfChunk</CODE> to the <CODE>PdfLine</CODE>.
	 *
	 * @param		chunk		the <CODE>PdfChunk</CODE> to add
	 * @return		<CODE>null</CODE> if the chunk could be added completely; if not
	 *				a <CODE>PdfChunk</CODE> containing the part of the chunk that could
	 *				not be added is returned
	 *
	 * @since		iText0.30
	 */

	final PdfChunk add(PdfChunk chunk) {
		// nothing happens if the chunk is null.
		if (chunk == null || chunk.toString().equals("")) {
			return null;
		}

		// we split the chunk to be added
		PdfChunk overflow = chunk.split(width);


		// if the length of the chunk > 0 we add it to the line
		if (chunk.length() > 0) {
			width -= chunk.width();
			line.add(chunk);
		}

		// if the length == 0 and there were no other chunks added to the line yet,
		// we risk to end up in an endless loop trying endlessly to add the same chunk
		else if (line.size() < 1) {
			chunk = overflow;
			overflow = chunk.truncate(width);
			width -= chunk.width();
			if (chunk.length() > 0) {
				line.add(chunk);
				return overflow;
			}
			// if the chunck couldn't even be truncated, we add everything, so be it
			else {
				line.add(overflow);
				return null;
			}
		}
		return overflow;
	}

// methods to retrieve information

	/**
	 * Returns the number of chunks in the line.
	 *
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	public int size() {
		return line.size();
	}

	/**
	 * Returns an iterator of <CODE>PdfChunk</CODE>s.
	 *	 
	 * @return	an <CODE>Iterator</CODE>
	 *
	 * @since	iText0.30
	 */

	public Iterator iterator() {
		return line.iterator();
	}

	/**
	 * Returns the height of the line.
	 * 
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	final int height() {
		return height;
	}

	/**
	 * Returns the left indentation of the line taking the alignment of the line into account.
	 *
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	final int indentLeft() {
		switch (alignment) {
		case Element.ALIGN_RIGHT:
			return left + width;
		case Element.ALIGN_CENTER:
			return left + (width / 2);
		default:
			return left;
		}
	}

	/**
	 * Checks if this line has to be justified.
	 *
	 * @return	<CODE>true</CODE> if the alignment equals <VAR>ALIGN_JUSTIFIED</VAR> and there is some width left.
	 *
	 * @since	iText0.30
	 */

	public boolean hasToBeJustified() {
		return (alignment == Element.ALIGN_JUSTIFIED && width != 0);
	}

	/**
	 * Resets the alignment of this line.
	 * <P>
	 * The alignment of the last line of for instance a <CODE>Paragraph</CODE>
	 * that has to be justified, has to be reset to <VAR>ALIGN_LEFT</VAR>. 
	 *
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public void resetAlignment() {
		if (alignment == Element.ALIGN_JUSTIFIED) {
			alignment = Element.ALIGN_LEFT;
		}
	}

	/**
	 * Returns the width that is left, after a maximum of characters is added to the line.
	 *
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	final int widthLeft() {
		return width;
	}

	/**
	 * Returns the number of space-characters in this line.
	 *
	 * @return	a value
	 * 
	 * @since	iText0.30
	 */

	final int numberOfSpaces() {
		String string = toString();
		int length = string.length();
		int numberOfSpaces = 0;
		for (int i = 0; i < length; i++) {
			if (string.charAt(i) == ' ') {
				numberOfSpaces++;
			}
		}
		return numberOfSpaces;
	}

	/**
	 * Sets the listsymbol of this line.
	 * <P>
	 * This is only necessary for the first line of a <CODE>ListItem</CODE>.
	 * 
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30 
	 */

	public void setListItem(ListItem listItem) {
		this.listSymbol = new PdfChunk(listItem.listSymbol());
		this.symbolIndent = listItem.indentationLeft();
	}

	/**
	 * Returns the listsymbol of this line.
	 *
	 * @return	a <CODE>PdfChunk</CODE> if the line has a listsymbol; <CODE>null</CODE> otherwise
	 *
	 * @since	iText0.30
	 */

	public PdfChunk listSymbol() {
		return listSymbol;
	}

	/**
	 * Return the indentation needed to show the listsymbol.
	 *
	 * @return	a value
	 *
	 * @since	iText0.30
	 */

	public int listIndent() {
		return symbolIndent;
	}

	/**
	 * Get the string representation of what is in this line.
	 *
	 * @return	a <CODE>String</CODE>
	 *
	 * @since	iText0.30
	 */

	public String toString() {
		StringBuffer tmp = new StringBuffer();
		for (Iterator i = line.iterator(); i.hasNext(); ) {
			tmp.append(((PdfChunk) i.next()).toString());
		}
		return tmp.toString();
	}
}
/*
 * @(#)Cell.java					0.36 2000/09/08
 *       release iText0.3:			0.29 2000/02/14
 *       release iText0.35:			0.29 2000/08/11
 *       release iText0.36:			0.36 2000/09/08
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

package com.lowagie.text;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A <CODE>Cell</CODE> is a <CODE>Rectangle</CODE> containing other
 * <CODE>Element</CODE>s.
 * <P>
 * A <CODE>Cell</CODE> must be added to a <CODE>Table</CODE>.
 * The <CODE>Table</CODE> will place the <CODE>Cell</CODE> in
 * a <CODE>Row</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * Table table = new Table(3);
 * table.setBorderWidth(1);
 * table.setBorderColor(new Color(0, 0, 255));
 * table.setCellpadding(5);
 * table.setCellspacing(5);
 * <STRONG>Cell cell = new Cell("header");</STRONG>
 * <STRONG>cell.setHeader(true);</STRONG>
 * <STRONG>cell.setColspan(3);</STRONG>
 * table.addCell(cell);
 * <STRONG>cell = new Cell("example cell with colspan 1 and rowspan 2");</STRONG>
 * <STRONG>cell.setRowspan(2);</STRONG>
 * <STRONG>cell.setBorderColor(new Color(255, 0, 0));</STRONG>
 * table.addCell(cell);
 * table.addCell("1.1");
 * table.addCell("2.1");
 * table.addCell("1.2");
 * table.addCell("2.2");
 * </PRE></BLOCKQUOTE>
 *
 * @see		Rectangle
 * @see		Element
 * @see		Table
 * @see		Row
 *
 * @author  bruno@lowagie.com
 * @version 0.36, 2000/09/08
 *
 * @since   iText0.30
 */

public class Cell extends Rectangle implements TextElementArray {

// static final membervariable
	
	/** This constant can be used as empty cell. */
	public static final Cell EMPTY_CELL = new Cell();

// membervariables

	/** This is the <CODE>ArrayList</CODE> of <CODE>Element</CODE>s. */
	private ArrayList arrayList;

	/** This is the horizontal alignment. */
	private int horizontalAlignment;

	/** This is the vertical alignment. */
	private int verticalAlignment;

	/** This is the colspan. */
	private int colspan = 1;

	/** This is the rowspan. */
	private int rowspan = 1;

	/** This is the leading. */
	int leading = -1;

	/** Is this <CODE>Cell</CODE> a header? */
	private boolean header;

	/** Will the element have to be wrapped? */
	private boolean noWrap;

// constructors

	/**
	 * Constructs an empty <CODE>Cell</CODE>.
	 * 
	 * @since	iText0.30
	 */

	public Cell() {
		// creates a Rectangle with BY DEFAULT a border of 0.5
		super(0, 0, 0, 0);
		setBorder(BOX);
		setBorderWidth(0.5);

		// initializes the arraylist and adds an element
		arrayList = new ArrayList();
		try {
			addElement(new Paragraph(0));
		}
		catch(BadElementException bee) {
			// this will never happen
		}
	}

	/**
	 * Constructs a <CODE>Cell</CODE> with a certain content.
	 * <P>
	 * The <CODE>String</CODE> will be converted into a <CODE>Paragraph</CODE>.
	 *
	 * @param	content		a <CODE>String</CODE>
	 * @throws	BadElementException this can never happen with this creator
	 * 
	 * @since	iText0.30
	 */

	public Cell(String content) throws BadElementException {
		this(new Paragraph(content));
	}

	/**
	 * Constructs a <CODE>Cell</CODE> with a certain <CODE>Element</CODE>.
	 * <P>
	 * if the element is a <CODE>ListItem</CODE>, <CODE>Row</CODE> or
	 * <CODE>Cell</CODE>, an exception will be thrown.
	 *
	 * @param	element		the element
	 * @throws	BadElementException when the creator was called with a <CODE>ListItem</CODE>, <CODE>Row</CODE> or <CODE>Cell</CODE>
	 * 
	 * @since	iText0.30
	 */

	public Cell(Element element) throws BadElementException {
		// creates a Rectangle with BY DEFAULT a border of 0.5
		super(0, 0, 0, 0);
		setBorder(BOX);
		setBorderWidth(0.5);

		// initializes the arraylist and adds an element
		arrayList = new ArrayList();
		addElement(element);
	}

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to a
	 * <CODE>DocListener</CODE>. 
     *
	 * <CODE>true</CODE> if the element was processed successfully
	 *
     * @since   iText0.30
     */

    public final boolean process(DocListener listener) {
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
	 *
     * @since	iText0.30
     */

    public final int type() {
		return Element.CELL;
	}		

    /**
     * Gets all the chunks in this element. 
     *
     * @return	an <CODE>ArrayList</CODE>
	 *
     * @since	iText0.30
     */

    public final ArrayList getChunks() {
		 ArrayList tmp = new ArrayList();
		 for (Iterator i = arrayList.iterator(); i.hasNext(); ) {
			 tmp.addAll(((Element) i.next()).getChunks());
		 }
		 return tmp;
	}

// methods to set the membervariables

	/**
	 * Adds an element to this <CODE>Cell</CODE>. 
     * <P>
	 * Remark: you can't add <CODE>ListItem</CODE>s, <CODE>Row</CODE>s, <CODE>Cell</CODE>s,
	 * <CODE>JPEG</CODE>s, <CODE>GIF</CODE>s or <CODE>PNG</CODE>s to a <CODE>Cell</CODE>.
	 *
     * @return	<CODE>void</CODE>
	 * @throws	BadElementException if the method was called with a <CODE>ListItem</CODE>, <CODE>Row</CODE> or <CODE>Cell</CODE>
	 *
     * @since	iText0.30
	 */

	public final void addElement(Element element) throws BadElementException {
		switch(element.type()) {
		case Element.LISTITEM:
		case Element.ROW:
		case Element.CELL:
		case Element.TABLE:
		case Element.JPEG:
		case Element.GIF:
		case Element.PNG:
			throw new BadElementException("You can't add listitems, rows, tables, cells, jpgs, gifs or pngs to a cell.");
		case Element.PARAGRAPH:
		case Element.PHRASE:
			if (leading < 0) {
				leading = ((Phrase) element).leading();
			}
		case Element.LIST:
			if (leading < 0) {
				leading = ((List) element).leading();
			}
		default:
			arrayList.add(element);
		}
	}

	/**
	 * Add an <CODE>Object</CODE> to this cell.
	 *
	 * @param	o		the object to add
	 */

	public boolean add(Object o) {
		try {
			this.addElement((Element) o);
			return true;
		}
		catch(ClassCastException cce) {
			throw new ClassCastException("You can only add objects that implement the Element interface.");
		}
		catch(BadElementException bee) {
			throw new ClassCastException(bee.getMessage());
		}
	}

    /**
     * Sets the leading. 
     *
	 * @param	value	the new value
     * @return	<CODE>void</CODE>
	 *
     * @since	iText0.30
     */

    public final void setLeading(int value) {
		leading = value;
	}

    /**
     * Sets the horizontal alignment. 
     *
	 * @param	value	the new value
     * @return	<CODE>void</CODE>
	 *
     * @since	iText0.30
     */

    public final void setHorizontalAlignment(int value) {
		horizontalAlignment = value;
	}

    /**
     * Sets the vertical alignment. 
     *
	 * @param	value	the new value
     * @return	<CODE>void</CODE>
	 *
     * @since	iText0.30
     */

    public final void setVerticalAlignment(int value) {
		verticalAlignment = value;
	}					  

    /**
     * Sets the colspan. 
     *
	 * @param	value	the new value
     * @return	<CODE>void</CODE>
	 *
     * @since	iText0.30
     */

    public final void setColspan(int value) {
		colspan = value;
	}					  

    /**
     * Sets the rowspan. 
     *
	 * @param	value	the new value
     * @return	<CODE>void</CODE>
	 *
     * @since	iText0.30
     */

    public final void setRowspan(int value) {
		rowspan = value;
	}			  					  

    /**
     * Sets header. 
     *
	 * @param	value	the new value
     * @return	<CODE>void</CODE>
	 *
     * @since	iText0.30
     */

    public final void setHeader(boolean value) {
		header = value;
	}			  					  

    /**
     * Set nowrap. 
     *
	 * @param	value	the new value
     * @return	<CODE>void</CODE>
	 *
     * @since	iText0.30
     */

    public final void setNoWrap(boolean value) {
		noWrap = value;
	}

// methods to retrieve information

	/**
	 * Gets the number of <CODE>Element</CODE>s in the Cell. 
     *
     * @return	a <CODE>size</CODE>.
	 *
     * @since	iText0.30
	 */

	public final int size() {
		return arrayList.size();
	}

	/**
	 * Checks if the <CODE>Cell</CODE> is empty. 
     *
     * @return	<CODE>false</CODE> if there are non-empty <CODE>Element</CODE>s in the <CODE>Cell</CODE>.
	 *
     * @since	iText0.30
	 */

	public final boolean isEmpty() {
		switch(size()) {
		case 0:
			return true;
		case 1:
			Element element = (Element) arrayList.get(0);		
			switch (element.type()) {
			case Element.CHUNK:
				return ((Chunk) element).isEmpty();
			case Element.PHRASE:
			case Element.PARAGRAPH:
				return ((Phrase) element).isEmpty();
			}
			return false;
		default:
			return false;
		}
	}

	/**
	 * Gets an iterator of <CODE>Element</CODE>s. 
     *
     * @return	an <CODE>Iterator</CODE>.
	 *
     * @since	iText0.30
	 */

	public final Iterator getElements() {
		return arrayList.iterator();
	}

    /**
     * Gets the horizontal alignment. 
     *
     * @return	a value
	 *
     * @since	iText0.30
     */

    public final int horizontalAlignment() {
		return horizontalAlignment;
	}

    /**
     * Gets the vertical alignment. 
     *
     * @return	a value
	 *
     * @since	iText0.30
     */

    public final int verticalAlignment() {
		return verticalAlignment;
	}					  

    /**
     * Gets the colspan. 
     *
     * @return	a value
	 *
     * @since	iText0.30
     */

    public final int colspan() {
		return colspan;
	}					  

    /**
     * Gets the rowspan. 
     *
     * @return	a value
	 *
     * @since	iText0.30
     */

    public final int rowspan() {
		return rowspan;
	}

    /**
     * Gets the leading. 
     *
     * @return	a value
	 *
     * @since	iText0.30
     */

    public final int leading() {
		if (leading < 0) {
			return 16;
		}
		return leading;
	}			  					  

    /**
     * Is this <CODE>Cell</CODE> a header? 
     *
     * @return	a value
	 *
     * @since	iText0.30
     */

    public boolean header() {
		return header;
	}			  					  

    /**
     * Get nowrap. 
     *
     * @return	a value
	 *
     * @since	iText0.30
     */

    public boolean noWrap() {
		return noWrap;
	}

	/**
	 * Returns a representation of this <CODE>Cell</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 *
     * @since	iText0.30
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("\t\t<CELL HEADER=\"");
		buf.append(header);
		buf.append("\" NOWRAP=\"");
		buf.append(noWrap);
		buf.append(">\n");
		buf.append("\n\t\t\t<COLSPAN>");
		buf.append(colspan);
		buf.append("</COLSPAN>\n");
		buf.append("\n\t\t\t<ROWSPAN>");
		buf.append(rowspan);
		buf.append("</ROWSPAN>\n");
		buf.append("\n\t\t\t<HORIZONTAL_ALIGNMENT>");
		switch(horizontalAlignment()) {
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
		buf.append("\n\t\t\t<VERTICAL_ALIGNMENT>");
		switch(verticalAlignment()) {
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
		if (borderWidth > 0 && border != NO_BORDER) {
			buf.append("\t\t\t<BORDER WIDTH=\"");
			buf.append(borderWidth);
			buf.append("\">");
			if (hasBorder(TOP)) {
				buf.append("\t\t\t\t<SIDE>");
				buf.append("top");
				buf.append("</SIDE>\n");
			}
			if (hasBorder(BOTTOM)) {
				buf.append("\t\t\t\t<SIDE>");
				buf.append("bottom");
				buf.append("</SIDE>\n");
			}										
			if (hasBorder(LEFT)) {
				buf.append("\t\t\t\t<SIDE>");
				buf.append("left");
				buf.append("</SIDE>\n");
			}									  
			if (hasBorder(RIGHT)) {
				buf.append("\t\t\t\t<SIDE>");
				buf.append("right");
				buf.append("</SIDE>\n");
			}
			if (color != null) {
				buf.append("\t\t\t\t<COLOR>\n");
				buf.append("\t\t\t\t\t<RED>");
				buf.append(color.getRed());
				buf.append("</RED>\n");;
				buf.append("\t\t\t\t\t<GREEN>");
				buf.append(color.getGreen());
				buf.append("</GREEN>\n"); ;
				buf.append("\t\t\t\t\t<BLUE>");
				buf.append(color.getBlue());
				buf.append("</BLUE>\n");
				buf.append("\t\t\t\t</COLOR>\n");
			}
			buf.append("\t\t\t</BORDER>\n");
		}
		for (Iterator i = arrayList.iterator(); i.hasNext(); ) {
			buf.append(((Element) i.next()).toString());
		}
		buf.append("\n\t\t</CELL>\n");								
		return buf.toString();
	}
}
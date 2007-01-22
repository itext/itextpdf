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

package com.lowagie.text;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import com.lowagie.text.html.Markup;
import com.lowagie.text.pdf.PdfPCell;

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
 */

public class Cell extends Rectangle implements TextElementArray {

	// static final membervariable

    // This accessor replaces the dangerous static member DUMMY_CELL
    /**
     * Get dummy cell used when merging inner tables. 
     * @return a cell with colspan 3 and no border
     */
    public static Cell getDummyCell() {
        Cell cell = new Cell(true);
        cell.setColspan(3);
        cell.setBorder(NO_BORDER);
        return cell;
	}

	// membervariables

/** This is the <CODE>ArrayList</CODE> of <CODE>Element</CODE>s. */
	protected ArrayList arrayList = null;

/** This is the horizontal alignment. */
	protected int horizontalAlignment = Element.ALIGN_UNDEFINED;

/** This is the vertical alignment. */
	protected int verticalAlignment = Element.ALIGN_UNDEFINED;

/** This is the vertical alignment. */
	protected String width;

/** This is the colspan. */
	protected int colspan = 1;

/** This is the rowspan. */
	protected int rowspan = 1;

/** This is the leading. */
	float leading = Float.NaN;

/** Is this <CODE>Cell</CODE> a header? */
	protected boolean header;

    /** Indicates that the largest ascender height should be used to determine the
     * height of the first line.  Note that this only has an effect when rendered
     * to PDF.  Setting this to true can help with vertical alignment problems. */
    protected boolean useAscender = false;

    /** Indicates that the largest descender height should be added to the height of
     * the last line (so characters like y don't dip into the border).   Note that
     * this only has an effect when rendered to PDF. */
    protected boolean useDescender = false;

    /**
     * Adjusts the cell contents to compensate for border widths.  Note that
     * this only has an effect when rendered to PDF.
     */
    protected boolean useBorderPadding;

	// constructors

/**
 * Constructs an empty <CODE>Cell</CODE>.
 */

	public Cell() {
		// creates a Rectangle with BY DEFAULT a border of 0.5
		super(0, 0, 0, 0);
		setBorder(UNDEFINED);
		setBorderWidth(0.5f);

		// initializes the arraylist and adds an element
		arrayList = new ArrayList();
	}

/**
 * Constructs an empty <CODE>Cell</CODE> (for internal use only).
 *
 * @param   dummy   a dummy value
 */

	public Cell(boolean dummy) {
		this();
		arrayList.add(new Paragraph(0));
	}

/**
 * Constructs a <CODE>Cell</CODE> with a certain content.
 * <P>
 * The <CODE>String</CODE> will be converted into a <CODE>Paragraph</CODE>.
 *
 * @param	content		a <CODE>String</CODE>
 */

	public Cell(String content) {
		// creates a Rectangle with BY DEFAULT a border of 0.5
		super(0, 0, 0, 0);
		setBorder(UNDEFINED);
		setBorderWidth(0.5f);

		// initializes the arraylist and adds an element
		arrayList = new ArrayList();
		try {
			addElement(new Paragraph(content));
		}
		catch(BadElementException bee) {
		}
	}

/**
 * Constructs a <CODE>Cell</CODE> with a certain <CODE>Element</CODE>.
 * <P>
 * if the element is a <CODE>ListItem</CODE>, <CODE>Row</CODE> or
 * <CODE>Cell</CODE>, an exception will be thrown.
 *
 * @param	element		the element
 * @throws	BadElementException when the creator was called with a <CODE>ListItem</CODE>, <CODE>Row</CODE> or <CODE>Cell</CODE>
 */

	public Cell(Element element) throws BadElementException {
		// creates a Rectangle with BY DEFAULT a border of 0.5
		super(0, 0, 0, 0);
		setBorder(UNDEFINED);
		setBorderWidth(0.5f);

 		// Update by Benoit WIART <b.wiart@proxiad.com>
 		if(element instanceof Phrase) {
			Phrase p = (Phrase)element;
			leading = p.leading();
		}

		// initializes the arraylist and adds an element
		arrayList = new ArrayList();
		addElement(element);
	}

/**
 * Returns a <CODE>Cell</CODE> that has been constructed taking in account
 * the value of some <VAR>attributes</VAR>.
 *
 * @param	attributes		Some attributes
 */

	public Cell(Properties attributes) {
		this();
		String value;
		if ((value = (String)attributes.remove(ElementTags.HORIZONTALALIGN)) != null) {
			setHorizontalAlignment(value);
		}
		if ((value = (String)attributes.remove(ElementTags.VERTICALALIGN)) != null) {
			setVerticalAlignment(value);
		}
		if ((value = (String)attributes.remove(ElementTags.WIDTH)) != null) {
			setWidth(value);
		}
		if ((value = (String)attributes.remove(ElementTags.COLSPAN)) != null) {
			setColspan(Integer.parseInt(value));
		}
		if ((value = (String)attributes.remove(ElementTags.ROWSPAN)) != null) {
			setRowspan(Integer.parseInt(value));
		}
		if ((value = (String)attributes.remove(ElementTags.LEADING)) != null) {
			setLeading(Float.parseFloat(value + "f"));
		}
		if ((value = (String)attributes.remove(ElementTags.HEADER)) != null) {
			setHeader(Boolean.valueOf(value).booleanValue());
		}
		if ((value = (String)attributes.remove(ElementTags.NOWRAP)) != null) {
			setNoWrap(Boolean.valueOf(value).booleanValue());
		}
		if ((value = (String)attributes.remove(ElementTags.BORDERWIDTH)) != null) {
			setBorderWidth(Float.parseFloat(value + "f"));
		}
		int border = 0;
		if ((value = (String)attributes.remove(ElementTags.LEFT)) != null) {
			if (Boolean.valueOf(value).booleanValue()) border |= Rectangle.LEFT;
		}
		if ((value = (String)attributes.remove(ElementTags.RIGHT)) != null) {
			if (Boolean.valueOf(value).booleanValue()) border |= Rectangle.RIGHT;
		}
		if ((value = (String)attributes.remove(ElementTags.TOP)) != null) {
			if (Boolean.valueOf(value).booleanValue()) border |= Rectangle.TOP;
		}
		if ((value = (String)attributes.remove(ElementTags.BOTTOM)) != null) {
			if (Boolean.valueOf(value).booleanValue()) border |= Rectangle.BOTTOM;
		}
		setBorder(border);
		String r = (String)attributes.remove(ElementTags.RED);
		String g = (String)attributes.remove(ElementTags.GREEN);
		String b = (String)attributes.remove(ElementTags.BLUE);
		if (r != null || g != null || b != null) {
			int red = 0;
			int green = 0;
			int blue = 0;
			if (r != null) red = Integer.parseInt(r);
			if (g != null) green = Integer.parseInt(g);
			if (b != null) blue = Integer.parseInt(b);
			setBorderColor(new Color(red, green, blue));
		}
		else if ((value = (String)attributes.remove(ElementTags.BORDERCOLOR)) != null) {
			setBorderColor(Markup.decodeColor(value));
		}
		r = (String)attributes.remove(ElementTags.BGRED);
		g = (String)attributes.remove(ElementTags.BGGREEN);
		b = (String)attributes.remove(ElementTags.BGBLUE);
		if (r != null || g != null || b != null) {
			int red = 0;
			int green = 0;
			int blue = 0;
			if (r != null) red = Integer.parseInt(r);
			if (g != null) green = Integer.parseInt(g);
			if (b != null) blue = Integer.parseInt(b);
			setBackgroundColor(new Color(red, green, blue));
		}
		else if ((value = (String)attributes.remove(ElementTags.BACKGROUNDCOLOR)) != null) {
			setBackgroundColor(Markup.decodeColor(value));
		}
		if ((value = (String)attributes.remove(ElementTags.GRAYFILL)) != null) {
			setGrayFill(Float.parseFloat(value + "f"));
		}
	}

	// implementation of the Element-methods

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

/**
 * Gets the type of the text element.
 *
 * @return	a type
 */

	public int type() {
		return Element.CELL;
	}

/**
 * Gets all the chunks in this element.
 *
 * @return	an <CODE>ArrayList</CODE>
 */

	public ArrayList getChunks() {
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
 * @param element The <CODE>Element</CODE> to add
 * @throws BadElementException if the method was called with a <CODE>ListItem</CODE>, <CODE>Row</CODE> or <CODE>Cell</CODE>
 */

	public void addElement(Element element) throws BadElementException {
		if (isTable()) {
			Table table = (Table) arrayList.get(0);
			Cell tmp = new Cell(element);
			tmp.setBorder(NO_BORDER);
			tmp.setColspan(table.columns());
			table.addCell(tmp);
			return;
		}
		switch(element.type()) {
			case Element.LISTITEM:
			case Element.ROW:
			case Element.CELL:
				throw new BadElementException("You can't add listitems, rows or cells to a cell.");
			case Element.LIST:
				if (Float.isNaN(leading)) {
					leading = ((List) element).leading();
				}
				if (((List) element).size() == 0) return;
				arrayList.add(element);
				return;
			case Element.ANCHOR:
			case Element.PARAGRAPH:
			case Element.PHRASE:
				if (Float.isNaN(leading)) {
					leading = ((Phrase) element).leading();
				}
				if (((Phrase) element).isEmpty()) return;
				arrayList.add(element);
				return;
			case Element.CHUNK:
				if (((Chunk) element).isEmpty()) return;
				arrayList.add(element);
				return;
			case Element.TABLE:
				Table table = new Table(3);
				float[] widths = new float[3];
				widths[1] = ((Table)element).widthPercentage();

				switch(((Table)element).alignment()) {
					case Element.ALIGN_LEFT:
						widths[0] = 0f;
						widths[2] = 100f - widths[1];
						break;
					case Element.ALIGN_CENTER:
						widths[0] = (100f - widths[1]) / 2f;
						widths[2] = widths[0];
						break;
					case Element.ALIGN_RIGHT:
						widths[0] = 100f - widths[1];
						widths[2] = 0f;
				}
				table.setWidths(widths);
				Cell tmp;
				if (arrayList.isEmpty()) {
					table.addCell(getDummyCell());
				}
				else {
					tmp = new Cell();
					tmp.setBorder(NO_BORDER);
					tmp.setColspan(3);
					for (Iterator i = arrayList.iterator(); i.hasNext(); ) {
						tmp.add(i.next());
					}
					table.addCell(tmp);
				}
				tmp = new Cell();
				tmp.setBorder(NO_BORDER);
				table.addCell(tmp);
				table.insertTable((Table)element);
				tmp = new Cell();
				tmp.setBorder(NO_BORDER);
				table.addCell(tmp);
				table.addCell(getDummyCell());
				clear();
				arrayList.add(table);
				return;
				default:
					arrayList.add(element);
		}
	}

/**
 * Add an <CODE>Object</CODE> to this cell.
 *
 * @param o the object to add
 * @return always <CODE>true</CODE>
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
 */

	public void setLeading(float value) {
		leading = value;
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
 * Sets the alignment of this cell.
 *
 * @param	alignment		the new alignment as a <CODE>String</CODE>
 */

	public void setHorizontalAlignment(String alignment) {
		if (ElementTags.ALIGN_CENTER.equalsIgnoreCase(alignment)) {
			this.horizontalAlignment = Element.ALIGN_CENTER;
			return;
		}
		if (ElementTags.ALIGN_RIGHT.equalsIgnoreCase(alignment)) {
			this.horizontalAlignment = Element.ALIGN_RIGHT;
			return;
		}
		if (ElementTags.ALIGN_JUSTIFIED.equalsIgnoreCase(alignment)) {
			this.horizontalAlignment = Element.ALIGN_JUSTIFIED;
			return;
		}
		if (ElementTags.ALIGN_JUSTIFIED_ALL.equalsIgnoreCase(alignment)) {
			this.horizontalAlignment = Element.ALIGN_JUSTIFIED_ALL;
			return;
		}
		this.horizontalAlignment = Element.ALIGN_LEFT;
	}

/**
 * Sets the vertical alignment.
 *
 * @param	value	the new value
 */

	public void setVerticalAlignment(int value) {
		verticalAlignment = value;
	}

/**
 * Sets the alignment of this paragraph.
 *
 * @param	alignment		the new alignment as a <CODE>String</CODE>
 */

	public void setVerticalAlignment(String alignment) {
		if (ElementTags.ALIGN_MIDDLE.equalsIgnoreCase(alignment)) {
			this.verticalAlignment = Element.ALIGN_MIDDLE;
			return;
		}
		if (ElementTags.ALIGN_BOTTOM.equalsIgnoreCase(alignment)) {
			this.verticalAlignment = Element.ALIGN_BOTTOM;
			return;
		}
		if (ElementTags.ALIGN_BASELINE.equalsIgnoreCase(alignment)) {
			this.verticalAlignment = Element.ALIGN_BASELINE;
			return;
		}
		this.verticalAlignment = Element.ALIGN_TOP;
	}

/**
 * Sets the width.
 *
 * @param	value	the new value
 */

	public void setWidth(String value) {
		width = value;
	}

/**
 * Sets the colspan.
 *
 * @param	value	the new value
 */

	public void setColspan(int value) {
		colspan = value;
	}

/**
 * Sets the rowspan.
 *
 * @param	value	the new value
 */

	public void setRowspan(int value) {
		rowspan = value;
	}

/**
 * Sets header.
 *
 * @param	value	the new value
 */

	public void setHeader(boolean value) {
		header = value;
	}

/**
 * Set nowrap.
 *
 * @param	value	the new value
 */

	public void setNoWrap(boolean value) {
		maxLines = 1;
	}

	// methods to retrieve information

/**
 * Gets the number of <CODE>Element</CODE>s in the Cell.
 *
 * @return	a <CODE>size</CODE>.
 */

	public int size() {
		return arrayList.size();
	}

/**
 * Checks if the <CODE>Cell</CODE> is empty.
 *
 * @return	<CODE>false</CODE> if there are non-empty <CODE>Element</CODE>s in the <CODE>Cell</CODE>.
 */

	public boolean isEmpty() {
		switch(size()) {
			case 0:
				return true;
			case 1:
				Element element = (Element) arrayList.get(0);
				switch (element.type()) {
					case Element.CHUNK:
						return ((Chunk) element).isEmpty();
					case Element.ANCHOR:
					case Element.PHRASE:
					case Element.PARAGRAPH:
						return ((Phrase) element).isEmpty();
					case Element.LIST:
						return ((List) element).size() == 0;
				}
				return false;
				default:
					return false;
		}
	}

/**
 * Makes sure there is at least 1 object in the Cell.
 *
 * Otherwise it might not be shown in the table.
 */

	void fill() {
		if (size() == 0) arrayList.add(new Paragraph(0));
	}

/**
 * Checks if the <CODE>Cell</CODE> is empty.
 *
 * @return	<CODE>false</CODE> if there are non-empty <CODE>Element</CODE>s in the <CODE>Cell</CODE>.
 */

	public boolean isTable() {
		return (size() == 1) && (((Element)arrayList.get(0)).type() == Element.TABLE);
	}

/**
 * Gets an iterator of <CODE>Element</CODE>s.
 *
 * @return	an <CODE>Iterator</CODE>.
 */

	public Iterator getElements() {
		return arrayList.iterator();
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
 * Gets the width.
 *
 * @return	a value
 */

	public String cellWidth() {
		return width;
	}

/**
 * Gets the colspan.
 *
 * @return	a value
 */

	public int colspan() {
		return colspan;
	}

/**
 * Gets the rowspan.
 *
 * @return	a value
 */

	public int rowspan() {
		return rowspan;
	}

/**
 * Gets the leading.
 *
 * @return	a value
 */

	public float leading() {
		if (Float.isNaN(leading)) {
			return 16;
		}
		return leading;
	}

/**
 * Is this <CODE>Cell</CODE> a header?
 *
 * @return	a value
 */

	public boolean header() {
		return header;
	}

/**
 * Get nowrap.
 *
 * @return	a value
 */

	public boolean noWrap() {
		return maxLines == 1;
	}

/**
 * Clears all the <CODE>Element</CODE>s of this <CODE>Cell</CODE>.
 */
	public void clear() {
		arrayList.clear();
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @return NA
 */
	public float top() {
		throw new UnsupportedOperationException("Dimensions of a Cell can't be calculated. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @return NA
 */
	public float bottom() {
		throw new UnsupportedOperationException("Dimensions of a Cell can't be calculated. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @return NA
 */
	public float left() {
		throw new UnsupportedOperationException("Dimensions of a Cell can't be calculated. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @return NA
 */
	public float right() {
		throw new UnsupportedOperationException("Dimensions of a Cell can't be calculated. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @param margin
 * @return NA
 */
	public float top(int margin) {
		throw new UnsupportedOperationException("Dimensions of a Cell can't be calculated. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @param margin
 * @return NA
 */
	public float bottom(int margin) {
		throw new UnsupportedOperationException("Dimensions of a Cell can't be calculated. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @param margin
 * @return NA
 */
	public float left(int margin) {
		throw new UnsupportedOperationException("Dimensions of a Cell can't be calculated. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @param margin NA
 * @return NA
 */
	public float right(int margin) {
		throw new UnsupportedOperationException("Dimensions of a Cell can't be calculated. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @param value NA
 */
	public void setTop(int value) {
		throw new UnsupportedOperationException("Dimensions of a Cell are attributed automagically. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @param value NA
 */
	public void setBottom(int value) {
		throw new UnsupportedOperationException("Dimensions of a Cell are attributed automagically. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @param value NA
 */
	public void setLeft(int value) {
		throw new UnsupportedOperationException("Dimensions of a Cell are attributed automagically. See the FAQ.");
	}

/**
 * This method throws an <CODE>UnsupportedOperationException</CODE>.
 * @param value NA
 */
	public void setRight(int value) {
		throw new UnsupportedOperationException("Dimensions of a Cell are attributed automagically. See the FAQ.");
	}

/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */

	public static boolean isTag(String tag) {
		return ElementTags.CELL.equals(tag);
	}

/** Does this <CODE>Cell</CODE> force a group change? */
	protected boolean groupChange = true;

/**
 * Does this <CODE>Cell</CODE> force a group change?
 *
 * @return	a value
 */

	public boolean getGroupChange() {
		return groupChange;
	}

/**
 * Sets group change.
 *
 * @param	value	the new value
 */

	public void setGroupChange(boolean value) {
		groupChange = value;
	}
	
	/**
	 * Getter for {@link #maxLines}
	 * @return the maxLines value
	 */
	public int getMaxLines() {
		return maxLines;
	}
	/**
	 * Setter for {@link #maxLines}
	 * @param value the maximum number of lines
	 */
	public void setMaxLines(int value) {
		maxLines = value;
	}
	/**
	 * Maximum number of lines allowed in the cell.  
	 * The default value of this property is not to limit the maximum number of lines
	 * (contributed by dperezcar@fcc.es)
	 */
	protected int maxLines = Integer.MAX_VALUE;
	/**Setter for {@link #showTruncation}
	 * @param value	Can be null for avoiding marking the truncation.*/
	public void setShowTruncation(String value) {
		showTruncation = value;
	}
	/**
	 * Getter for {@link #showTruncation}
	 * @return the showTruncation value
	 */
	public String getShowTruncation() {
		return showTruncation;
	}
	/**
	 * If a truncation happens due to the {@link #maxLines} property, then this text will 
	 * be added to indicate a truncation has happened.
	 * Default value is null, and means avoiding marking the truncation.  
	 * A useful value of this property could be e.g. "..."
	 * (contributed by dperezcar@fcc.es)
	 */
	String showTruncation;


    /**
     * Sets the value of {@link #useAscender}.
     * @param use use ascender height if true
     */
    public void setUseAscender(boolean use) {
        useAscender = use;
    }

    /**
     * Gets the value of {@link #useAscender}
     * @return useAscender
     */
    public boolean isUseAscender() {
        return useAscender;
    }

    /**
     * Sets the value of {@link #useDescender}.
     * @param use use descender height if true
     */
    public void setUseDescender(boolean use) {
        useDescender = use;
    }

    /**
     * gets the value of {@link #useDescender }
     * @return useDescender
     */
    public boolean isUseDescender() {
        return useDescender;
    }

    /**
     * Sets the value of {@link #useBorderPadding}.
     * @param use adjust layour for borders if true
     */
    public void setUseBorderPadding(boolean use) {
        useBorderPadding = use;
    }

    /**
     * Gets the value of {@link #useBorderPadding}.
     * @return useBorderPadding
     */
    public boolean isUseBorderPadding() {
        return useBorderPadding;
    }

	/**
	 * Creates a PdfPCell based on this Cell object.
	 * @return a PdfPCell
	 * @throws BadElementException
	 */
	public PdfPCell createPdfPCell() throws BadElementException {
		if (rowspan > 1) throw new BadElementException("PdfPCells can't have a rowspan > 1");
		if (isTable()) return new PdfPCell(((Table)arrayList.get(0)).createPdfPTable());
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(verticalAlignment);
		cell.setHorizontalAlignment(horizontalAlignment);
		cell.setColspan(colspan);
		cell.setUseBorderPadding(useBorderPadding);
		cell.setUseDescender(useDescender);
		cell.setLeading(leading(), 0);
		cell.cloneNonPositionParameters(this);
		cell.setNoWrap(noWrap());
		for (Iterator i = getElements(); i.hasNext(); ) {
            Element e = (Element)i.next();
            if (e.type() == Element.PHRASE || e.type() == Element.PARAGRAPH) {
                Paragraph p = new Paragraph((Phrase)e);
                p.setAlignment(horizontalAlignment);
                e = p;
            }
			cell.addElement(e);
		}
		return cell;
	}
}
/*
 * $Id$
 * $Name$
 *
 * Copyright 1999-2005 by Bruno Lowagie.
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
 */
package com.lowagie.text;

import java.util.ArrayList;
import java.util.Iterator;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPCellEvent;
import com.lowagie.text.pdf.PdfPTable;

/**
 * Rectangle that can be used for Cells.
 * This Rectangle is padded and knows how to draw itself in a PdfPTable or PdfPcellEvent.
 */
public class SimpleCell extends Rectangle implements PdfPCellEvent, Element {

	/** the CellAttributes object represents a row. */
	public static final boolean ROW = true;
	/** the CellAttributes object represents a cell. */
	public static final boolean CELL = false;
	/** the content of the Cell. */
	private ArrayList content = new ArrayList();
	/** the width of the Cell. */
	private float width = 0f;
	/** the widthpercentage of the Cell. */
	private float widthpercentage = 0f;
	/** an extra spacing variable */
	private float spacing = Float.NaN;
	/** an extra padding variable */
	private float padding_left = Float.NaN;
	/** an extra padding variable */
	private float padding_right = Float.NaN;
	/** an extra padding variable */
	private float padding_top = Float.NaN;
	/** an extra padding variable */
	private float padding_bottom = Float.NaN;
	/** the colspan of a Cell */
	private int colspan = 1;
	/** horizontal alignment inside the Cell. */
	private int horizontalAlignment = Element.ALIGN_UNDEFINED;
	/** vertical alignment inside the Cell. */
	private int verticalAlignment = Element.ALIGN_UNDEFINED;
	/** indicates if these are the attributes of a single Cell (false) or a group of Cells (true). */
	private boolean cellgroup = false;
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
	
	/**
	 * A CellAttributes object is always constructed without any dimensions.
	 * Dimensions are defined after creation.
	 * @param row only true if the CellAttributes object represents a row.
	 */
	public SimpleCell(boolean row) {
		super(0f, 0f, 0f, 0f);
		cellgroup = row;
		setBorder(BOX);
		setBorderWidth(1);
	}
	
	/**
	 * Adds content to this object.
	 * @param element
	 * @throws BadElementException
	 */
	public void addElement(Element element) throws BadElementException {
		if (cellgroup) {
			if (element instanceof SimpleCell) {
				if(((SimpleCell)element).isCellgroup()) {
					throw new BadElementException("You can't add one row to another row.");
				}
			}
			else {
				throw new BadElementException("You can only add cells to rows, no objects of type " + element.getClass().getName());
			}
		}
		if (element.type() == Element.PARAGRAPH
				|| element.type() == Element.PHRASE
				|| element.type() == Element.ANCHOR
				|| element.type() == Element.CHUNK
				|| element.type() == Element.LIST) {
			content.add(element);
		}
		else {
			throw new BadElementException("You can't add an element of type " + element.getClass().getName() + " to a SimpleCell.");
		}
	}
	
	/**
	 * Creates a Cell with these attributes.
	 * @param rowAttributes
	 * @return a cell based on these attributes.
	 * @throws BadElementException
	 */
	public Cell createCell(SimpleCell rowAttributes) throws BadElementException {
		Cell cell = new Cell();
		cell.cloneNonPositionParameters(rowAttributes);
		cell.softCloneNonPositionParameters(this);
		cell.setColspan(colspan);
		cell.setHorizontalAlignment(horizontalAlignment);
		cell.setVerticalAlignment(verticalAlignment);
		cell.setUseAscender(useAscender);
		cell.setUseBorderPadding(useBorderPadding);
		cell.setUseDescender(useDescender);
		Element element;
		for (Iterator i = content.iterator(); i.hasNext(); ) {
			element = (Element)i.next();
			cell.addElement(element);
		}
		return cell;
	}
	
	/**
	 * Creates a PdfPCell with these attributes.
	 * @param rowAttributes
	 * @return a PdfPCell based on these attributes.
	 */
	public PdfPCell createPdfPCell(SimpleCell rowAttributes) {
		PdfPCell cell = new PdfPCell();
		cell.setBorder(NO_BORDER);
		SimpleCell tmp = new SimpleCell(CELL);
		tmp.setSpacing(spacing);
		tmp.cloneNonPositionParameters(rowAttributes);
		tmp.softCloneNonPositionParameters(this);
		cell.setCellEvent(tmp);
		cell.setHorizontalAlignment(rowAttributes.horizontalAlignment);
		cell.setVerticalAlignment(rowAttributes.verticalAlignment);
		cell.setUseAscender(rowAttributes.useAscender);
		cell.setUseBorderPadding(rowAttributes.useBorderPadding);
		cell.setUseDescender(rowAttributes.useDescender);
		cell.setColspan(colspan);
		if (horizontalAlignment != Element.ALIGN_UNDEFINED)
			cell.setHorizontalAlignment(horizontalAlignment);
		if (verticalAlignment != Element.ALIGN_UNDEFINED)
			cell.setVerticalAlignment(verticalAlignment);
		if (useAscender)
			cell.setUseAscender(useAscender);
		if (useBorderPadding)
			cell.setUseBorderPadding(useBorderPadding);
		if (useDescender)
			cell.setUseDescender(useDescender);
		float p;
		float sp = spacing;
		if (Float.isNaN(sp)) sp = 0f;
		p = padding_left;
		if (Float.isNaN(p)) p = 0f; 
		cell.setPaddingLeft(p + sp / 2f);
		p = padding_right;
		if (Float.isNaN(p)) p = 0f; 
		cell.setPaddingRight(p + sp / 2f);
		p = padding_top;
		if (Float.isNaN(p)) p = 0f; 
		cell.setPaddingTop(p + sp / 2f);
		p = padding_bottom;
		if (Float.isNaN(p)) p = 0f; 
		cell.setPaddingBottom(p + sp / 2f);
		Element element;
		for (Iterator i = content.iterator(); i.hasNext(); ) {
			element = (Element)i.next();
			cell.addElement(element);
		}
		return cell;
	}
	
	/**
	 * @param rectangle
	 * @param spacing
	 * @return a rectangle
	 */
	public static SimpleCell getDimensionlessInstance(Rectangle rectangle, float spacing) {
		SimpleCell event = new SimpleCell(CELL);
		event.cloneNonPositionParameters(rectangle);
		event.setSpacing(spacing);
		return event;
	}

	/**
	 * @see com.lowagie.text.pdf.PdfPCellEvent#cellLayout(com.lowagie.text.pdf.PdfPCell, com.lowagie.text.Rectangle, com.lowagie.text.pdf.PdfContentByte[])
	 */
	public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
		float sp = spacing;
		if (Float.isNaN(sp)) sp = 0f;
		sp = sp / 2f;
		Rectangle rect = new Rectangle(position.left(sp), position.bottom(sp), position.right(sp), position.top(sp));
		rect.cloneNonPositionParameters(this);
		canvases[PdfPTable.BACKGROUNDCANVAS].rectangle(rect);
		rect.setBackgroundColor(null);
		canvases[PdfPTable.LINECANVAS].rectangle(rect);
	}
	
	/** Sets the padding parameters if they are undefined. 
	 * @param padding*/
	public void setPadding(float padding) {
		if (Float.isNaN(padding_right)) {
			setPadding_right(padding);
		}
		if (Float.isNaN(padding_left)) {
			setPadding_left(padding);
		}
		if (Float.isNaN(padding_top)) {
			setPadding_top(padding);
		}
		if (Float.isNaN(padding_bottom)) {
			setPadding_bottom(padding);
		}
	}
	
	/**
	 * @return Returns the colspan.
	 */
	public int getColspan() {
		return colspan;
	}
	/**
	 * @param colspan The colspan to set.
	 */
	public void setColspan(int colspan) {
		if (colspan > 0) this.colspan = colspan;
	}
	/**
	 * @return Returns the padding_bottom.
	 */
	public float getPadding_bottom() {
		return padding_bottom;
	}
	/**
	 * @param padding_bottom The padding_bottom to set.
	 */
	public void setPadding_bottom(float padding_bottom) {
		this.padding_bottom = padding_bottom;
	}
	/**
	 * @return Returns the padding_left.
	 */
	public float getPadding_left() {
		return padding_left;
	}
	/**
	 * @param padding_left The padding_left to set.
	 */
	public void setPadding_left(float padding_left) {
		this.padding_left = padding_left;
	}
	/**
	 * @return Returns the padding_right.
	 */
	public float getPadding_right() {
		return padding_right;
	}
	/**
	 * @param padding_right The padding_right to set.
	 */
	public void setPadding_right(float padding_right) {
		this.padding_right = padding_right;
	}
	/**
	 * @return Returns the padding_top.
	 */
	public float getPadding_top() {
		return padding_top;
	}
	/**
	 * @param padding_top The padding_top to set.
	 */
	public void setPadding_top(float padding_top) {
		this.padding_top = padding_top;
	}
	/**
	 * @return Returns the spacing.
	 */
	public float getSpacing() {
		return spacing;
	}
	
	/**
	 * @param spacing The spacing to set.
	 */
	public void setSpacing(float spacing) {
		this.spacing = spacing;
	}
	
	/**
	 * @return Returns the cellgroup.
	 */
	public boolean isCellgroup() {
		return cellgroup;
	}
	/**
	 * @param cellgroup The cellgroup to set.
	 */
	public void setCellgroup(boolean cellgroup) {
		this.cellgroup = cellgroup;
	}
	/**
	 * @return Returns the horizontal alignment.
	 */
	public int getHorizontalAlignment() {
		return horizontalAlignment;
	}
	/**
	 * @param horizontalAlignment The horizontalAlignment to set.
	 */
	public void setHorizontalAlignment(int horizontalAlignment) {
		this.horizontalAlignment = horizontalAlignment;
	}
	/**
	 * @return Returns the vertical alignment.
	 */
	public int getVerticalAlignment() {
		return verticalAlignment;
	}
	/**
	 * @param verticalAlignment The verticalAligment to set.
	 */
	public void setVerticalAlignment(int verticalAlignment) {
		this.verticalAlignment = verticalAlignment;
	}
	/**
	 * @return Returns the width.
	 */
	public float getWidth() {
		return width;
	}
	/**
	 * @param width The width to set.
	 */
	public void setWidth(float width) {
		this.width = width;
	}
	/**
	 * @return Returns the widthpercentage.
	 */
	public float getWidthpercentage() {
		return widthpercentage;
	}
	/**
	 * @param widthpercentage The widthpercentage to set.
	 */
	public void setWidthpercentage(float widthpercentage) {
		this.widthpercentage = widthpercentage;
	}
	/**
	 * @return Returns the useAscender.
	 */
	public boolean isUseAscender() {
		return useAscender;
	}
	/**
	 * @param useAscender The useAscender to set.
	 */
	public void setUseAscender(boolean useAscender) {
		this.useAscender = useAscender;
	}
	/**
	 * @return Returns the useBorderPadding.
	 */
	public boolean isUseBorderPadding() {
		return useBorderPadding;
	}
	/**
	 * @param useBorderPadding The useBorderPadding to set.
	 */
	public void setUseBorderPadding(boolean useBorderPadding) {
		this.useBorderPadding = useBorderPadding;
	}
	/**
	 * @return Returns the useDescender.
	 */
	public boolean isUseDescender() {
		return useDescender;
	}
	/**
	 * @param useDescender The useDescender to set.
	 */
	public void setUseDescender(boolean useDescender) {
		this.useDescender = useDescender;
	}
	
	/**
	 * @return Returns the content.
	 */
	ArrayList getContent() {
		return content;
	}
}
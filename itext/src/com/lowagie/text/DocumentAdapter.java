/*
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Bruno Lowagie.
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
 * Special thanks to David Freels for writing this class
 */

package com.lowagie.text;

/**
 * This class allows you to obtain a listener without
 * adding all of the methods of the DocListener interface.
 * 
 * @author       David Freels
 */

public abstract class DocumentAdapter implements DocListener {

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 *
	 * @return	false
	 */

	public boolean newPage() throws DocumentException {
		return false;
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 */

	public void close() {
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 */

	public void finalize() {
	}
	
	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 *
	 * @param	element		a text element
	 * @return	false
	 */

	public boolean add(Element element) throws DocumentException {
		return false;
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 */

	public void open() {
	}								   

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 *
	 * @param	pageSize	the size of a page
	 */
	
	public boolean setPageSize(Rectangle pageSize) {
		return false;
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 *
	 * @param	watermark	a watermark
	 */

	public boolean add(Watermark watermark) {
		return false;
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 */

	public void removeWatermark() {
	}							

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 *
	 * @param	marginLeft		the width of the margin at the left
	 * @param	marginRight		the width of the margin at the right
	 * @param	marginTop		the height of the margin at the top
	 * @param	marginBottom	the height of the margin at the bottom
	 */

	public boolean setMargins(int marginLeft, int marginRight, int marginTop, int marginBottom) {
		return false;
	}							

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 *
	 * @param	header	a header
	 */

	public void setHeader(HeaderFooter header) {
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 */

	public void resetHeader() {
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 *
	 * @param	footer	a footer
	 */

	public void setFooter(HeaderFooter footer) {
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 */

	public void resetFooter() {
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 */

	public void resetPageCount() {
	}

	/**
	 * This is an empty implementation of the corresponding method in the DocListener interface.
	 *
	 * @param	pageN	a page number
	 */

	public void setPageCount(int pageN) {
	}
}
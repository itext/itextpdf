/*
 * $Id$
 * $Name$
 * 
 * Copyright (c) 1999, 2000, 2001 Bruno Lowagie.
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

/**
 * A class that implements <CODE>DocListener</CODE> will perform some
 * actions when some actions are performed on a <CODE>Document</CODE>.
 *
 * @see		ElementListener
 * @see		Document
 * @see		DocWriter
 *
 * @author  bruno@lowagie.com
 */

public interface DocListener extends ElementListener {

// methods

	/**
	 * Closes the <CODE>DocListener</CODE> when gc is invoked.
	 */

	public void finalize();

    /**
     * Signals that the <CODE>Document</CODE> has been opened and that
	 * <CODE>Elements</CODE> can be added.
     */

    public void open();

	/**
	 * Sets the pagesize.
	 *
	 * @param	pageSize	the new pagesize
	 * @return	a <CODE>boolean</CODE>
	 */

	public boolean setPageSize(Rectangle pageSize);

    /**
     * Signals that a <CODE>Watermark</CODE> was added to the <CODE>Document</CODE>. 
     *
	 * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
     */

    public boolean add(Watermark watermark);

    /**
     * Signals that a <CODE>Watermark</CODE> was removed from the <CODE>Document</CODE>.
     */

    public void removeWatermark();

	/**
	 * Sets the margins.
	 *			   							
	 * @param	marginLeft		the margin on the left
	 * @param	marginRight		the margin on the right
	 * @param	marginTop		the margin on the top
	 * @param	marginBottom	the margin on the bottom
	 * @return	a <CODE>boolean</CODE>
	 */

	public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom);	

    /**
     * Signals that an new page has to be started. 
     *
	 * @return	<CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
	 * @throws	DocumentException	when a document isn't open yet, or has been closed
     */

    public boolean newPage() throws DocumentException;

	/**
	 * Changes the header of this document.
	 * 
	 * @param	header		the new header
	 */

	public void setHeader(HeaderFooter header);

	/**
	 * Resets the header of this document.
	 * 
	 * @param	header		the new header
	 */

	public void resetHeader();

	/**
	 * Changes the footer of this document.
	 * 
	 * @param	footer		the new footer
	 */

	public void setFooter(HeaderFooter footer);

	/**
	 * Resets the footer of this document.
	 */

	public void resetFooter();
	
	/**
	 * Sets the page number to 0.
	 */

	public void resetPageCount();

	/**
	 * Sets the page number.
	 *
	 * @param	pageN		the new page number
	 */

	public void setPageCount(int pageN);

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
	 * <CODE>Elements</CODE> will be added.
	 * <P>
	 * The outputstream of every writer implementing <CODE>DocListener</CODE> will be closed.
     */

    public void close();
}
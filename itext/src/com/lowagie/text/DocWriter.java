/*
 * @(#)DocWriter.java				0.36 2000/09/08
 *       release iText0.3:			0.25 2000/02/14
 *       release iText0.35:			0.31 2000/08/11
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

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.IOException;

import com.lowagie.text.DocListener;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Watermark;

/**
 * An abstract <CODE>Writer</CODE> class for documents.
 * <P>
 * <CODE>DocWriter</CODE> is the abstract class of several writers such
 * as <CODE>PdfWriter</CODE> and <CODE>HtmlWriter</CODE>.
 * A <CODE>DocWriter</CODE> can be added as a <CODE>DocListener</CODE>
 * to a certain <CODE>Document</CODE> by getting an instance (see method
 * <CODE>getInstance()</CODE> in the specific writer-classes).
 * Every <CODE>Element</CODE> added to the original <CODE>Document</CODE>
 * will be written to the <CODE>OutputStream</CODE> of the listening
 * <CODE>DocWriter</CODE>.
 * 
 * @see		Document
 * @see		DocListener
 *
 * @author  bruno@lowagie.com
 * @version 0.31, 2000/08/11
 *
 * @since   iText0.30
 */

public abstract class DocWriter implements DocListener {

// membervariables

	/** The pageSize. */
	protected Rectangle pageSize;

	/** This is the document that has to be written. */
	protected Document document;

	/** The outputstream of this writer. */
	protected BufferedOutputStream os;

	/** Is the writer open for writing? */
	protected boolean open;

// constructor

	/**
	 * Constructs a <CODE>DocWriter</CODE>.
	 *				
	 * @param	document	The <CODE>Document</CODE> that has to be written
	 * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
	 * 
	 * @since	iText0.30
	 */

	protected DocWriter(Document document, OutputStream os)  {
		this.document = document;
		this.os = new BufferedOutputStream(os);
		open = true;
	}

// destructor

	/**
	 * Closes the <CODE>DocWriter</CODE> when gc is invoked.
	 *
     * @since   iText0.30
	 */

	public void finalize() {
		close();
	}

// implementation of the DocListener methods

    /**
     * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>. 
     *
	 * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
	 * @throws	DocumentException	when a document isn't open yet, or has been closed
	 *
     * @since   iText0.30
     */

    public boolean add(Element element) throws DocumentException {
		 return false;
	}

    /**
     * Signals that the <CODE>Document</CODE> was opened. 
     *
	 * @return	<CODE>void</CODE>
	 *
     * @since   iText0.30
     */

    public void open() {
		open = true;
	}			  

	/**
	 * Sets the pagesize.
	 * <P>
	 * This does nothing. Has to be overridden if needed.
	 *
	 * @param	pageSize	the new pagesize
	 * @return	a <CODE>boolean</CODE>
	 *
	 * @since	iText0.30
	 */

	public boolean setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
		return true;
	}

    /**
     * Sets the <CODE>Watermark</CODE>. 
     *
	 * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
	 *
     * @since   iText0.31
     */

    public boolean add(Watermark watermark) {
		return false;
	}

	/**
	 * Removes the <CODE>Watermark</CODE>.
	 *
	 * @since	iText0.31;
	 */

	public void removeWatermark() {
	}

	/**
	 * Sets the margins.
	 * <P>
	 * This does nothing. Has to be overridden if needed.
	 *			   							
	 * @param	marginLeft		the margin on the left
	 * @param	marginRight		the margin on the right
	 * @param	marginTop		the margin on the top
	 * @param	marginBottom	the margin on the bottom
	 * @return	a <CODE>boolean</CODE>
	 *
	 * @since	iText0.30
	 */

	public boolean setMargins(int marginLeft, int marginRight, int marginTop, int marginBottom) {
		return false;
	}

    /**
     * Signals that an new page has to be started.
	 * <P>
	 * This does nothing. Has to be overridden if needed. 
     *
	 * @return	<CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
	 * @throws	DocumentException	when a document isn't open yet, or has been closed
	 *
     * @since   iText0.30
     */

    public boolean newPage() throws DocumentException {
		if (!open) {
			return false;
		}
		return true;
	}

	/**
	 * Changes the header of this document.
	 * <P>
	 * This does nothing. Has to be overridden if needed.
	 * 
	 * @param	header		the new header
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public void setHeader(HeaderFooter header) {
	}

	/**
	 * Resets the header of this document.
	 * <P>
	 * This does nothing. Has to be overridden if needed.
	 * 
	 * @param	header		the new header
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public void resetHeader() {
	}

	/**
	 * Changes the footer of this document.
	 * <P>
	 * This does nothing. Has to be overridden if needed.
	 * 
	 * @param	footer		the new footer
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public void setFooter(HeaderFooter footer) {
	}

	/**
	 * Resets the footer of this document.
	 * <P>
	 * This does nothing. Has to be overridden if needed.
	 *
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public void resetFooter() {
	}
	
	/**
	 * Sets the page number to 0.
	 * <P>
	 * This does nothing. Has to be overridden if needed.
	 *
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public void resetPageCount() {
	}

	/**
	 * Sets the page number.							 
	 * <P>
	 * This does nothing. Has to be overridden if needed.
	 *
	 * @param	pageN		the new page number
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public void setPageCount(int pageN) {
	}

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
	 * <CODE>Elements</CODE> will be added. 
     *
	 * @return	<CODE>void</CODE>
	 *
     * @since   iText0.30
     */

    public void close() {
		open = false;
		try {
			os.flush();
			os.close();
		}
		catch(IOException ioe) {
		//	System.err.println("Error: " + ioe.getMessage());
		}
	}

// methods

	/**
	 * Flushes the <CODE>BufferedOutputStream</CODE>.
	 *
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public void flush() {
		try {
			os.flush();
		}
		catch(IOException ioe) {
		}
	}

	/**
	 * Writes a <CODE>String</CODE> to the <CODE>OutputStream</CODE>.
	 *
	 * @param	string		the <CODE>String</CODE> to write
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	public final void write(String string) throws IOException {
		os.write(string.getBytes());
	}
}
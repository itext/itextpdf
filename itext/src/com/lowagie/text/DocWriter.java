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
	protected boolean open = false;		

	/** Do we have to pause all writing actions? */
	protected boolean pause = false;

// constructor

	/**
	 * Constructs a <CODE>DocWriter</CODE>.
	 *				
	 * @param	document	The <CODE>Document</CODE> that has to be written
	 * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
	 */

	protected DocWriter(Document document, OutputStream os)  {
		this.document = document;
		this.os = new BufferedOutputStream(os);
		open = true;
	}

// destructor

	/**
	 * Closes the <CODE>DocWriter</CODE> when gc is invoked.
	 */

	public void finalize() {
		close();
	}

// implementation of the DocListener methods

    /**
     * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>. 
	 * <P>
	 * This method should be overriden in the specific <CODE>DocWriter<CODE> classes
	 * derived from this abstract class.
     *
	 * @return	<CODE>false</CODE>
	 * @throws	DocumentException	when a document isn't open yet, or has been closed
     */

    public boolean add(Element element) throws DocumentException {
		 return false;
	}

    /**
     * Signals that the <CODE>Document</CODE> was opened.
     */

    public void open() {
		open = true;
	}			  

	/**
	 * Sets the pagesize.
	 *
	 * @param	pageSize	the new pagesize
	 * @return	a <CODE>boolean</CODE>
	 */

	public boolean setPageSize(Rectangle pageSize) {
		this.pageSize = pageSize;
		return true;
	}

    /**
     * Sets the <CODE>Watermark</CODE>. 
	 * <P>
	 * This method should be overriden in the specific <CODE>DocWriter<CODE> classes
	 * derived from this abstract class if they actually support the use of
	 * a <CODE>Watermark</CODE>. 
     *
	 * @return	<CODE>false</CODE> (because watermarks aren't supported by default).
     */

    public boolean add(Watermark watermark) {
		return false;
	}

	/**
	 * Removes the <CODE>Watermark</CODE> (if there is one).
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
	 * @return	<CODE>false</CODE>
	 */

	public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
		return false;
	}

    /**
     * Signals that an new page has to be started.
	 * <P>
	 * This does nothing. Has to be overridden if needed. 
     *
	 * @return	<CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
	 * @throws	DocumentException	when a document isn't open yet, or has been closed
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
	 * This method should be overriden in the specific <CODE>DocWriter<CODE> classes
	 * derived from this abstract class if they actually support the use of
	 * headers.
	 * 
	 * @param	header		the new header
	 */

	public void setHeader(HeaderFooter header) {
	}

	/**
	 * Resets the header of this document. 
	 * <P>
	 * This method should be overriden in the specific <CODE>DocWriter<CODE> classes
	 * derived from this abstract class if they actually support the use of
	 * headers.
	 */

	public void resetHeader() {
	}

	/**
	 * Changes the footer of this document. 
	 * <P>
	 * This method should be overriden in the specific <CODE>DocWriter<CODE> classes
	 * derived from this abstract class if they actually support the use of
	 * footers.
	 * 
	 * @param	footer		the new footer
	 */

	public void setFooter(HeaderFooter footer) {
	}

	/**
	 * Resets the footer of this document. 
	 * <P>
	 * This method should be overriden in the specific <CODE>DocWriter<CODE> classes
	 * derived from this abstract class if they actually support the use of
	 * footers.
	 */

	public void resetFooter() {
	}
	
	/**
	 * Sets the page number to 0. 
	 * <P>
	 * This method should be overriden in the specific <CODE>DocWriter<CODE> classes
	 * derived from this abstract class if they actually support the use of
	 * pagenumbers.
	 */

	public void resetPageCount() {
	}

	/**
	 * Sets the page number. 
	 * <P>
	 * This method should be overriden in the specific <CODE>DocWriter<CODE> classes
	 * derived from this abstract class if they actually support the use of
	 * pagenumbers.
	 *
	 * @param	pageN		the new page number
	 */

	public void setPageCount(int pageN) {
	}

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
	 * <CODE>Elements</CODE> will be added. 
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
	 * Let the writer know that all writing has to be paused.
	 */

	public void pause() {
		pause = true;
	}

	/**
	 * Let the writer know that writing may be resumed.
	 */

	public void resume() {
		pause = false;
	}

	/**
	 * Flushes the <CODE>BufferedOutputStream</CODE>.
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
	 */

	public final void write(String string) throws IOException {
		os.write(string.getBytes());
	}
}
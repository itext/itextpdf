/*
 * @(#)PdfOutline.java				0.39 2000/11/21
 *       release iText0.39:         0.39 2000/11/22
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
				  
import java.util.Iterator;

import com.lowagie.text.Chunk;
import com.lowagie.text.Paragraph;

/**
 * <CODE>PdfOutline</CODE> is an object that represents a PDF outline entry.
 * <P>
 * An outline allows a user to access views of a document by name.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 6.7 (page 104-106)
 * 
 * @see		PdfDictionary
 *
 * @author  bruno@lowagie.com
 * @version 0.39 2000/11/21
 * @since   iText0.39
 */

public class PdfOutline extends PdfDictionary {

// membervariables

	/** the <CODE>PdfIndirectReference</CODE> of this object */
	private PdfIndirectReference reference;

	/** value of the <B>Count</B>-key */
	private PdfNumber count;

	/** value of the <B>Parent</B>-key */
	private PdfOutline parent;

	/** value of the <B>Parent</B>-key */
	private PdfDestination destination;

// constructors

	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for the <CODE>outlines object</CODE>.
	 *
	 * @since		iText0.39
	 */

	PdfOutline() {
		super(OUTLINES);
		count = new PdfNumber(0);
		put(PdfName.COUNT, count);
		parent = null;
	}

	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for an <CODE>outline entry</CODE>.
	 *
	 * @since		iText0.39
	 */

	PdfOutline(PdfOutline parent, PdfDestination destination, PdfString title) {
		super();
		count = new PdfNumber(0);
		put(PdfName.COUNT, count);
		this.parent = parent;
		parent.add();
		this.destination = destination;
		put(PdfName.TITLE, title);
	}

	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for an <CODE>outline entry</CODE>.
	 *
	 * @since		iText0.39
	 */

	PdfOutline(PdfOutline parent, PdfDestination destination, Paragraph title) {
		super();
		count = new PdfNumber(0);
		put(PdfName.COUNT, count);
		this.parent = parent;
		parent.add();
		this.destination = destination;
		StringBuffer buf = new StringBuffer();
		for (Iterator i = title.getChunks().iterator(); i.hasNext(); ) {
			Chunk chunk = (Chunk) i.next();
			buf.append(chunk.content());
		}
		put(PdfName.TITLE, new PdfString(buf.toString()));
	}

// methods

	/**
	 * Sets the indirect reference of this <CODE>PdfOutline</CODE>.
	 *
	 * @param		the <CODE>PdfIndirectReference</CODE> to this outline.
	 *
	 * @since		iText0.39
	 */

	void setIndirectReference(PdfIndirectReference reference) {
		this.reference = reference;
	}

	/**
	 * Gets the indirect reference of this <CODE>PdfOutline</CODE>.
	 *
	 * @return		the <CODE>PdfIndirectReference</CODE> to this outline.
	 *
	 * @since		iText0.39
	 */

	PdfIndirectReference indirectReference() {
		return reference;
	}

	/**
	 * Gets the parent of this <CODE>PdfOutline</CODE>.
	 *
	 * @return		the <CODE>PdfOutline</CODE> that is the parent of this outline.
	 *
	 * @since		iText0.39
	 */

	PdfOutline parent() {
		return parent;
	}

	/**	 
	 * Set the page of the <CODE>PdfDestination</CODE>-object.
	 *
	 * @param		the indirect reference to the page
	 * @return		<CODE>true</CODE> if this page was set as the <CODE>PdfDestination</CODE>-page.
	 *
	 * @since		iText0.39
	 */

	boolean setDestinationPage(PdfIndirectReference pageReference) {
		if (destination == null) {
			return false;
		}
		return destination.addPage(pageReference);
	}

	/**
	 * Checks if this page element is a tree of pages.
	 * <P>
	 * This method allways returns <CODE>true</CODE>.
	 *
	 * @return	<CODE>true</CODE> because this object is a tree of pages
	 *
	 * @since		iText0.39
	 */

	boolean isParent() {
		return (count.intValue() > 0);
	}

	/**
	 * Increments the count.
	 *
	 * @return		<CODE>void</CODE>
	 *
	 * @since		iText0.39
	 */

	void add() {
		if (parent != null) {
			parent.add();
		}
		count.increment();
		put(PdfName.COUNT, count);
	}

	/**
	 * returns the level of this outline.
	 *
	 * @return		a level
	 *
	 * @since		iText0.39
	 */

	int level() {
		if (parent == null) {
			return 0;
		}
		return (parent.level() + 1);
	}

	/**
     * Returns the PDF representation of this <CODE>PdfOutline</CODE>.
	 *
	 * @return		an array of <CODE>byte</CODE>
     *
	 * @since		rugPdf0.39
     */

    final byte[] toPdf() {
		if (parent != null) {
			put(PdfName.PARENT, parent.indirectReference());
		}
		if (destination != null && destination.hasPage()) {
			put(PdfName.DEST, destination);
		}
		if (!isParent()) {
			remove(PdfName.COUNT);
		}
		return super.toPdf();
	}
}
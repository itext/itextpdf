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
	private int count = 0;

	/** value of the <B>Parent</B>-key */
	private PdfOutline parent;

	/** value of the <B>Destination</B>-key */
	private PdfDestination destination;
    
    /** <CODE>true</CODE> if it's children are visible */
    private boolean isOpen;

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
        isOpen = true;
        parent = null;
    }

	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
	 *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
	 * @since		iText0.39
	 */

	public PdfOutline(PdfOutline parent, PdfDestination destination, PdfString title) {
        this(parent, destination, title, true);
	}

	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
     *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
	 * @since		iText0.39
	 */

	public PdfOutline(PdfOutline parent, PdfDestination destination, String title) {
        this(parent, destination, title, true);
	}

	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for an <CODE>outline entry</CODE>. The open mode is
     * <CODE>true</CODE>.
	 *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
	 * @since		iText0.39
	 */

	public PdfOutline(PdfOutline parent, PdfDestination destination, Paragraph title) {
        this(parent, destination, title, true);
	}

	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for an <CODE>outline entry</CODE>.
	 *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
	 */
    public PdfOutline(PdfOutline parent, PdfDestination destination, PdfString title, boolean open)
    {
        this(parent, destination, title.toString(), true);
    }
    
	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for an <CODE>outline entry</CODE>.
	 *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
	 */
    public PdfOutline(PdfOutline parent, PdfDestination destination, String title, boolean open) {
        super();
        isOpen = open && parent.isOpen;
        if (isOpen || parent.isOpen) {
            parent.add();
        }
        this.parent = parent;
        this.destination = destination;
        put(PdfName.TITLE, new PdfString(title, "UnicodeBig"));
    }
    
	/**
	 * Constructs a <CODE>PdfOutline</CODE>.
	 * <P>
	 * This is the constructor for an <CODE>outline entry</CODE>.
	 *
     * @param parent the parent of this outline item
     * @param destination the destination for this outline item
     * @param title the title of this outline item
     * @param open <CODE>true</CODE> if the children are visible
	 */
    public PdfOutline(PdfOutline parent, PdfDestination destination, Paragraph title, boolean open) {
        super();
        isOpen = open && parent.isOpen;
        if (isOpen || parent.isOpen) {
            parent.add();
        }
        this.parent = parent;
        this.destination = destination;
        StringBuffer buf = new StringBuffer();
        for (Iterator i = title.getChunks().iterator(); i.hasNext(); ) {
            Chunk chunk = (Chunk) i.next();
            buf.append(chunk.content());
        }
        put(PdfName.TITLE, new PdfString(buf.toString(), "UnicodeBig"));
    }
    
    
// methods

	/**
     * Sets the indirect reference of this <CODE>PdfOutline</CODE>.
     *
     * @param reference the <CODE>PdfIndirectReference</CODE> to this outline.
     * @since iText0.39
 */

	public void setIndirectReference(PdfIndirectReference reference) {
		this.reference = reference;
	}

	/**
	 * Gets the indirect reference of this <CODE>PdfOutline</CODE>.
	 *
	 * @return		the <CODE>PdfIndirectReference</CODE> to this outline.
	 *
	 * @since		iText0.39
	 */

	public PdfIndirectReference indirectReference() {
		return reference;
	}

	/**
	 * Gets the parent of this <CODE>PdfOutline</CODE>.
	 *
	 * @return		the <CODE>PdfOutline</CODE> that is the parent of this outline.
	 *
	 * @since		iText0.39
	 */

	public PdfOutline parent() {
		return parent;
	}

	/** Set the page of the <CODE>PdfDestination</CODE>-object.
     *
     * @param pageReference indirect reference to the page
     * @return <CODE>true</CODE> if this page was set as the <CODE>PdfDestination</CODE>-page.
     * @since iText0.39
 */

	public boolean setDestinationPage(PdfIndirectReference pageReference) {
		if (destination == null) {
			return false;
		}
		return destination.addPage(pageReference);
	}
    
/** Gets the destination for this outline.
 * @return the destination
 */    
    public PdfDestination getPdfDestination()
    {
        return destination;
    }

	/** Increments the count.
     *
     * @since iText0.39
 */

	public void add() {
		if (parent != null) {
			parent.add();
		}
		++count;
	}

	/**
	 * returns the level of this outline.
	 *
	 * @return		a level
	 *
	 * @since		iText0.39
	 */

	public int level() {
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

    final public byte[] toPdf() {
		if (parent != null) {
			put(PdfName.PARENT, parent.indirectReference());
		}
		if (destination != null && destination.hasPage()) {
			put(PdfName.DEST, destination);
		}
		if ((isOpen || parent.isOpen) && count > 0) {
			put(PdfName.COUNT, new PdfNumber(count));
		}
		return super.toPdf();
	}
}
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

package com.lowagie.text.pdf;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <CODE>PdfPages</CODE> is the PDF Pages-object.
 * <P>
 * The Pages of a document are accessible through a tree of nodes known as the Pages tree.
 * This tree defines the ordering of the pages in the document.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 6.3 (page 71-73)
 *
 * @see		PdfPageElement
 * @see		PdfPage
 */

public class PdfPages extends PdfDictionary implements PdfPageElement {
    
    // membervariables
    
/** value of the <B>Count</B>-key */
    private PdfNumber count;
    
/** value of the <B>Kids</B>-key */
    private PdfArray kids;
    
/** array of objects of the type <CODE>PdfPageElement</CODE> (contains the actual Pages tree) */
    private ArrayList pages = new ArrayList();
    
    // constructors
    
/**
 * Constructs a <CODE>PdfPages</CODE>-object.
 */
    
    PdfPages() {
        super(PAGES);
        count = new PdfNumber(0);
        kids = new PdfArray();
        put(PdfName.COUNT, count);
        put(PdfName.KIDS, kids);
    }
    
    // implementation of the PdfPageElement interface
    
/**
 * Set the value for the <B>Parent</B> key in the Page or Pages Dictionary.
 *
 * @param		parent			an indirect reference to a <CODE>PdfPages</CODE>-object
 * @return		<CODE>void</CODE>
 */
    
    public void setParent(PdfIndirectReference reference) {
        put(PdfName.PARENT, reference);
    }
    
/**
 * Checks if this page element is a tree of pages.
 * <P>
 * This method allways returns <CODE>true</CODE>.
 *
 * @return	<CODE>true</CODE> because this object is a tree of pages
 */
    
    public boolean isParent() {
        return true;
    }
    
    // methods
    
/**
 * Adds a <CODE>PdfPages</CODE>-object.
 *
 * @param		pages		a <CODE>PdfPages</CODE>-object
 * @return		<CODE>void</CODE>
 */
    
    void add(PdfPages pages) {
        pages.add(pages);
    }
    
/**
 * Adds a <CODE>PdfPage</CODE>-object.
 *
 * @param		page		a <CODE>PdfPage</CODE>-object
 * @return		<CODE>void</CODE>
 */
    
    void add(PdfPage page) {
        pages.add(page);
    }
    
/**
 * Updates the array of kids.
 *
 * @param		kid			an indirect reference to a <CODE>PdfPageElement</CODE>-object
 * @return		<CODE>void</CODE>
 */
    
    void add(PdfIndirectReference kid) {
        count.increment();
        kids.add(kid);
        put(PdfName.COUNT, count);
        put(PdfName.KIDS, kids);
    }
    
/**
 * Returns an <CODE>Iterator</CODE> with all the leafs of this Pages-object.
 *
 * @return		an <CODE>Iterator</CODE>
 */
    
    Iterator iterator() {
        return pages.iterator();
    }
}
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

/**
 * <CODE>PdfPage</CODE> is the PDF Page-object.
 * <P>
 * A Page object is a dictionary whose keys describe a single page containing text,
 * graphics, and images. A Page onjects is a leaf of the Pages tree.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 6.4 (page 73-81)
 *
 * @see		PdfPageElement
 * @see		PdfPages
 */

public class PdfPage extends PdfDictionary implements PdfPageElement {
    
    // membervariables
    
/** value of the <B>Rotate</B> key for a page in PORTRAIT */
    public static final PdfNumber PORTRAIT = new PdfNumber(0);
    
/** value of the <B>Rotate</B> key for a page in LANDSCAPE */
    public static final PdfNumber LANDSCAPE = new PdfNumber(90);
    
/** value of the <B>Rotate</B> key for a page in INVERTEDPORTRAIT */
    public static final PdfNumber INVERTEDPORTRAIT = new PdfNumber(180);
    
/**	value of the <B>Rotate</B> key for a page in SEASCAPE */
    public static final PdfNumber SEASCAPE = new PdfNumber(270);
    
/** value of the <B>MediaBox</B> key */
    PdfRectangle mediaBox;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfPage</CODE>.
 *
 * @param		mediaBox		a value for the <B>MediaBox</B> key
 * @param		resources		an indirect reference to a <CODE>PdfResources</CODE>-object
 * @param		rotate			a value for the <B>Rotate</B> key
 */
    
    PdfPage(PdfRectangle mediaBox, PdfIndirectReference resources, PdfNumber rotate) {
        super(PAGE);
        this.mediaBox = mediaBox;
        put(PdfName.MEDIABOX, mediaBox);
        put(PdfName.RESOURCES, resources);
        if (rotate != null) {
            put(PdfName.ROTATE, rotate);
        }
    }
    
/**
 * Constructs a <CODE>PdfPage</CODE>.
 *
 * @param		mediaBox		a value for the <B>MediaBox</B> key
 * @param		resources		an indirect reference to a <CODE>PdfResources</CODE>-object
 * @param		rotate			a value for the <B>Rotate</B> key
 */
    
    PdfPage(PdfRectangle mediaBox, PdfResources resources, PdfNumber rotate) {
        super(PAGE);
        this.mediaBox = mediaBox;
        put(PdfName.MEDIABOX, mediaBox);
        put(PdfName.RESOURCES, resources);
        if (rotate != null) {
            put(PdfName.ROTATE, rotate);
        }
    }
    
/**
 * Constructs a <CODE>PdfPage</CODE>.
 *
 * @param		mediaBox		a value for the <B>MediaBox</B> key
 * @param		resources		an indirect reference to a <CODE>PdfResources</CODE>-object
 */
    
    PdfPage(PdfRectangle mediaBox, PdfIndirectReference resources) {
        this(mediaBox, resources, null);
    }
    
/**
 * Constructs a <CODE>PdfPage</CODE>.
 *
 * @param		mediaBox		a value for the <B>MediaBox</B> key
 * @param		resources		an indirect reference to a <CODE>PdfResources</CODE>-object
 */
    
    PdfPage(PdfRectangle mediaBox, PdfResources resources) {
        this(mediaBox, resources, null);
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
 * This method allways returns <CODE>false</CODE>.
 *
 * @return	<CODE>false</CODE> because this is a single page
 */
    
    public boolean isParent() {
        return false;
    }
    
    // methods
    
/**
 * Adds an indirect reference pointing to a <CODE>PdfContents</CODE>-object.
 *
 * @param		contents		an indirect reference to a <CODE>PdfContents</CODE>-object
 * @return		<CODE>void</CODE>
 */
    
    void add(PdfIndirectReference contents) {
        put(PdfName.CONTENTS, contents);
    }
    
/**
 * Rotates the mediabox, but not the text in it.
 *
 * @return		a <CODE>PdfRectangle</CODE>
 */
    
    PdfRectangle rotateMediaBox() {
        this.mediaBox =  mediaBox.rotate();
        put(PdfName.MEDIABOX, this.mediaBox);
        return this.mediaBox;
    }
    
/**
 * Returns the MediaBox of this Page.
 *
 * @return		a <CODE>PdfRectangle</CODE>
 */
    
    PdfRectangle getMediaBox() {
        return mediaBox;
    }
}
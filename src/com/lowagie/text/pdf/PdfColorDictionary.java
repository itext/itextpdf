package com.lowagie.text.pdf;

import java.util.Iterator;

/**
 * $Id$ 
 * <CODE>PdfColorDictionary</CODE> is a <CODE>PdfResource</CODE>, containing a dictionary of <CODE>PdfSpotColor</CODE>s.
 *	at the moment
 *
 * @see		PdfResource
 * @see		PdfResources
 */

class PdfColorDictionary extends PdfDictionary implements PdfResource {

    // constructors

/**
 * Constructs a new <CODE>PdfColorDictionary</CODE>.
 */

    PdfColorDictionary() {
        super();
    }

    // methods

/**
 * Returns the name of a resource.
 *
 * @return		a <CODE>PdfName</CODE>.
 */

    public PdfName key() {
        return PdfName.COLORSPACE;
    }

/**
 * Returns the object that represents the resource.
 *
 * @return		a <CODE>PdfObject</CODE>
 */

    public PdfObject value() {
        return this;
    }

/**
 * Checks if the <CODE>PdfColorDictionary</CODE> contains at least
 * one object.
 *
 * @return		<CODE>true</CODE> if an object was found
 *				<CODE>false</CODE> otherwise
 */

    boolean containsColorSpace() {
        return hashMap.size() > 0;
    }
}

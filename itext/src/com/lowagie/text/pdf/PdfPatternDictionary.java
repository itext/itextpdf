package com.lowagie.text.pdf;

import java.util.Iterator;

/**
 * $Id$ 
 * <CODE>PdfPatternDictionary</CODE> is a <CODE>PdfResource</CODE>, containing a dictionary of <CODE>PdfSpotPattern</CODE>s.
 *	at the moment
 *
 * @see		PdfResource
 * @see		PdfResources
 */

class PdfPatternDictionary extends PdfDictionary implements PdfResource {

    // constructors

/**
 * Constructs a new <CODE>PdfPatternDictionary</CODE>.
 */

    PdfPatternDictionary() {
        super();
    }

    // methods

/**
 * Returns the name of a resource.
 *
 * @return		a <CODE>PdfName</CODE>.
 */

    public PdfName key() {
        return PdfName.PATTERN;
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
 * Checks if the <CODE>PdfPatternDictionary</CODE> contains at least
 * one object.
 *
 * @return		<CODE>true</CODE> if an object was found
 *				<CODE>false</CODE> otherwise
 */

    boolean containsPattern() {
        return hashMap.size() > 0;
    }
}

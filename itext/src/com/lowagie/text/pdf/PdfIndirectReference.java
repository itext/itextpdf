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
 * <CODE>PdfIndirectReference</CODE> contains a reference to a <CODE>PdfIndirectObject</CODE>.
 * <P>
 * Any object used as an element of an array or as a value in a dictionary may be specified
 * by either a direct object of an indirect reference. An <I>indirect reference</I> is a reference
 * to an indirect object, and consists of the indirect object's object number, generation number
 * and the <B>R</B> keyword.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.11 (page 54).
 *
 * @see		PdfObject
 * @see		PdfIndirectObject
 */

class PdfIndirectReference extends PdfObject {
    
    // membervariables
    
/** the object number */
    protected int number;
    
/** the generation number */
    protected int generation = 0;
    
    // constructors
    
/**
 * Constructs a <CODE>PdfIndirectReference</CODE>.
 *
 * @param		type			the type of the <CODE>PdfObject</CODE> that is referenced to
 * @param		number			the object number.
 * @param		generation		the generation number.
 */
    
    PdfIndirectReference(int type, int number, int generation) {
        super(type, new StringBuffer().append(number).append(" ").append(generation).append(" R").toString());
        this.number = number;
        this.generation = generation;
    }
    
/**
 * Constructs a <CODE>PdfIndirectReference</CODE>.
 *
 * @param		type			the type of the <CODE>PdfObject</CODE> that is referenced to
 * @param		number			the object number.
 */
    
    PdfIndirectReference(int type, int number) {
        this(type, number, 0);
    }
    
    // methods
    
/**
 * Returns the number of the object.
 *
 * @return		a number.
 */
    
    final int getNumber() {
        return number;
    }
    
/**
 * Returns the generation of the object.
 *
 * @return		a number.
 */
    
    final int getGeneration() {
        return generation;
    }
}
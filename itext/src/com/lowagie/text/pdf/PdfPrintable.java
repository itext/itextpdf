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
 * Classes that implement the <CODE>PdfPrintable</CODE>-interface can be printed as text on a page.
 *
 * @see		PdfNull
 * @see		PdfBoolean
 * @see		PdfNumber
 * @see		PdfString
 */

public interface PdfPrintable {
    
    // static membervariables
    
/** possible align value of a printable object */
    public static final int DEFAULT = 0;
    
/** possible align value of a printable object */
    public static final int RIGHT = 1;
    
/** possible align value of a printable object */
    public static final int LEFT = 2;
    
/** possible align value of a printable object */
    public static final int CENTER = 3;
    
    // basic methods
    
/**
 * Every printable object should have a <CODE>toString</CODE>-method.
 *
 * @return		a <CODE>String</CODE>
 */
    
    public String toString();
    
/**
 * Returns the length of the actual content of the <CODE>PdfObject</CODE>.
 *
 * @return		a length
 */
    
    public int length();
    
    // methods concerning the actual type of a PdfPrintable object
    
/**
 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfNull</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public boolean isNull();
    
/**
 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfBoolean</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public boolean isBoolean();
    
/**
 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfNumber</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public boolean isNumber();
    
/**
 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfString</CODE>.
 *
 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    public boolean isString();
    
    public byte[] toPdf(PdfWriter writer);
}
/*
 * @(#)PdfPrintable.java			0.22 2000/02/02
 *       release rugPdf0.10:		0.03 99/04/01
 *               rugPdf0.20:		0.15 99/11/30
 *               iText0.3:			0.22 2000/02/14
 *               iText0.35:         0.22 2000/08/11
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
 *     
 * Very special thanks to Troy Harrison, Systems Consultant
 * of CNA Life Department-Information Technology
 * Troy.Harrison@cnalife.com <mailto:Troy.Harrison@cnalife.com>
 * His input concerning the changes in version rugPdf0.20 was
 * really very important.
 */

package com.lowagie.text.pdf;

/**
 * Classes that implement the <CODE>PdfPrintable</CODE>-interface can be printed as text on a page.
 *
 * @see		PdfNull
 * @see		PdfBoolean
 * @see		PdfNumber
 * @see		PdfString
 *
 * @author  bruno@lowagie.com
 * @version 0.22 2000/02/02
 * @since   rugPdf0.10
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
	 *
	 * @since		rugPdf0.10
	 */

	public String toString();

	/**
	 * Returns the length of the actual content of the <CODE>PdfObject</CODE>.
	 *
	 * @return		a length
	 *
	 * @since		rugPdf0.10
	 */
		   
	public int length();

// methods concerning the actual type of a PdfPrintable object

	/**
	 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfNull</CODE>.
	 *
	 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
	 *
	 * @since		rugPdf0.20
	 */

	public boolean isNull();

	/**
	 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfBoolean</CODE>.
	 *
	 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
	 *
	 * @since		rugPdf0.20
	 */

	public boolean isBoolean();																		

	/**
	 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfNumber</CODE>.
	 *
	 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
	 *
	 * @since		rugPdf0.20
	 */

	public boolean isNumber();

	/**
	 * Checks if this <CODE>PdfPrintable</CODE> is of the type <CODE>PdfString</CODE>.
	 *
	 * @returns		<CODE>true</CODE> or <CODE>false</CODE>
	 *
	 * @since		rugPdf0.20
	 */

	public boolean isString();
}
/*
 * @(#)PdfNumber.java				0.31 2000/08/16
 *       release rugPdf0.10:		0.04 99/03/30
 *               rugPdf0.20:		0.14 99/11/30
 *               iText0.3:			0.22 2000/02/14
 *               iText0.35*:        0.31 2000/08/16
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
 * <CODE>PdfNumber</CODE> provides two types of numbers, integer and real.
 * <P>
 * Integers may be specified by signed or unsigned constants. Reals may only be
 * in decimal format.<BR>
 * This object is described in the 'Portable Document Format Reference Manual version 1.3'
 * section 4.3 (page 37).
 *
 * @see		PdfObject
 * @see		BadPdfFormatException
 *
 * @author  bruno@lowagie.com
 * @version 0.22 2000/02/02
 *
 * @since   rugPdf0.10
 */

class PdfNumber extends PdfObject implements PdfPrintable {

// static membervariables (possible types of a number object)

	/** a possible type of <CODE>PdfNumber</CODE> */
	public static final int INTEGER = 0; 

	/** a possible type of <CODE>PdfNumber</CODE> */
	public static final int REAL = 1;

// membervariables

	/** type of this <CODE>PdfNumber</CODE> */
	private int numberType;

	/** actual value of this <CODE>PdfNumber</CODE>, represented as a <CODE>double</CODE> */
	private double value;

// constructors

	/**
	 * Constructs a <CODE>PdfNumber</CODE>-object.
	 *
	 * @param		type			one of the following types: <CODE>INTEGER</CODE> or <CODE>REAL</CODE>
	 * @param		content			value of the new <CODE>PdfNumber</CODE>-object
	 *
	 * @exception	BadPdfFormatException	Signals that a given type doesn't exist of that a given value isn't a number.
	 *
	 * @since	rugPdf0.10
	 */

	PdfNumber(int type, String content) throws BadPdfFormatException {
		super(NUMBER);
		numberType = type;
		switch (numberType) {
		case INTEGER:
			try {
				int i = Integer.parseInt(content.trim());
				setContent(String.valueOf(i));
				value = 1.0 * i;
			}
			catch (NumberFormatException nfe){
				throw new BadPdfFormatException(content + " is not a valid integer.");
			}
			break;
		case REAL:
			try {
				double d = Double.valueOf(content.trim()).doubleValue();
				setContent(ByteBuffer.formatDouble(d));
				value = d;
			}
			catch (NumberFormatException nfe){
				throw new BadPdfFormatException(content + " is not a valid real.");
			}
			break;
		default:
			throw new BadPdfFormatException("Unknown type of number: " + type);
		}
	}

	/**
	 * Constructs a new INTEGER <CODE>PdfNumber</CODE>-object.
	 *
	 * @param		value				value of the new <CODE>PdfNumber</CODE>-object
	 *
	 * @since		rugPdf0.10
	 */

	PdfNumber(int value) {
		super(NUMBER, String.valueOf(value));
		this.value = 1.0 * value;
		numberType = INTEGER;
	}

	/**
	 * Constructs a new REAL <CODE>PdfNumber</CODE>-object.
	 *
	 * @param		value				value of the new <CODE>PdfNumber</CODE>-object
	 *
	 * @since		rugPdf0.10
	 */

	PdfNumber(double value) {
		super(NUMBER, ByteBuffer.formatDouble(value));
		this.value = value;
		numberType = REAL;
	}
	
	/**
	 * Constructs a new REAL <CODE>PdfNumber</CODE>-object.
	 *
	 * @param		value				value of the new <CODE>PdfNumber</CODE>-object
	 *
	 * @since		rugPdf0.10
	 */

	PdfNumber(float value) {
        this((double)value);
	}
	
// methods returning the value of this object

	/**
	 * Returns the <CODE>String</CODE> value of the <CODE>PdfNumber</CODE>-object.
	 *
	 * @return		a <CODE>String</CODE> value "true" or "false"
	 *
	 * @since		rugPdf0.20
	 */

	public String toString() {
		if (numberType == INTEGER) {
			return String.valueOf((int) value);
		}
		else {
			return ByteBuffer.formatDouble(value);
		}
	}

	/**
	 * Returns the primitive <CODE>int</CODE> value of this object.
	 *
	 * @return		a value
	 *
	 * @since		rugPdf0.10
	 */

	final int intValue() {
		return (int) value;
	}

	/**
	 * Returns the primitive <CODE>double</CODE> value of this object.
	 *
	 * @return		a value
	 *
	 * @since		rugPdf0.10
	 */

	final double doubleValue() {
		return value;
	}

// other methods

	/**
	 * Increments the value of the <CODE>PdfNumber</CODE>-object with 1.
	 *
	 * @return		<CODE>void</CODE>	
	 *
	 * @since		rugPdf0.10
	 */

	void increment() {
		value += 1.0;
		setContent(ByteBuffer.formatDouble(value));
	}
}
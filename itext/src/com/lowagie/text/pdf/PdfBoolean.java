/*

 * @(#)PdfBoolean.java				0.22 2000/02/02

 *       release rugPdf0.10:		0.03 99/03/30

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

 * Very special thanks to Troy Harrison, Systems Consultant

 * of CNA Life Department-Information Technology

 * Troy.Harrison@cnalife.com <mailto:Troy.Harrison@cnalife.com>

 * His input concerning the changes in version rugPdf0.20 was

 * really very important.

 */



package com.lowagie.text.pdf;



/**

 * <CODE>PdfBoolean</CODE> is the boolean object represented by the keywords <VAR>true</VAR> or <VAR>false</VAR>.

 * <P>

 * This object is described in the 'Portable Document Format Reference Manual version 1.3'

 * section 4.2 (page 37).

 *

 * @see		PdfObject

 * @see		BadPdfFormatException

 *

 * @author  bruno@lowagie.com

 * @version 0.22 2000/02/02

 *

 * @since   rugPdf0.10

 */



class PdfBoolean extends PdfObject implements PdfPrintable {



// static membervariables (possible values of a boolean object)



	/** A possible value of <CODE>PdfBoolean</CODE> */

	public static final String TRUE = "true";



	/** A possible value of <CODE>PdfBoolean</CODE> */

	public static final String FALSE = "false";



// membervariables



	/** the boolean value of this object */

	private boolean value;



// constructors



	/**

	 * Constructs a <CODE>PdfBoolean</CODE>-object.

	 *

	 * @param		value			the value of the new <CODE>PdfObject</CODE>

	 *

	 * @since		rugPdf0.10

	 */



	PdfBoolean(boolean value) {

		super(BOOLEAN);

		if (value) {

			setContent(TRUE);

		}

		else {

			setContent(FALSE);

		}

		this.value = value;

	}



	/**

	 * Constructs a <CODE>PdfBoolean</CODE>-object.

	 *

	 * @param		value			the value of the new <CODE>PdfObject</CODE>, represented as a <CODE>String</CODE>

	 *

	 * @throws		BadPdfFormatException	thrown if the <VAR>value</VAR> isn't '<CODE>true</CODE>' or '<CODE>false</CODE>'

	 *

	 * @since		rugPdf0.10

	 */



	PdfBoolean(String value) throws BadPdfFormatException {

		super(BOOLEAN, value);

		if (value.equals(TRUE)) {

			this.value = true;

		}

		else if (value.equals(FALSE)) {

			this.value = false;

		}

		else {

			throw new BadPdfFormatException("The value has to be 'true' of 'false', instead of '" + value + "'.");

		}

	}

	

// methods returning the value of this object



	/**

	 * Returns the <CODE>String</CODE> value of the <CODE>PdfBoolean</CODE>-object.

	 *

	 * @return		a <CODE>String</CODE> value "true" or "false"

	 *

	 * @since		rugPdf0.20

	 */



	public String toString() {

		if (value) {

			return TRUE;

		}

		else {

			return FALSE;

		}

	}





	/**

	 * Returns the primitive value of the <CODE>PdfBoolean</CODE>-object.

	 *

	 * @return		the actual value of the object.

	 *

	 * @since		rugPdf0.10

	 */



	final boolean booleanValue() {

		return value;

	}

}
/*

 * @(#)PdfException.java			0.22 2000/02/02

 *               rugPdf0.20:		0.13 99/11/29

 *               iText0.3:			0.22 2000/02/14

 *       release iText0.35:         0.22 2000/08/11

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



import com.lowagie.text.DocumentException;



/**

 * Signals that an unspecified problem while constructing a PDF document.

 *

 * @see		BadPdfFormatException

 *

 * @author  bruno@lowagie.com

 * @version 0.22 2000/02/02

 *

 * @since   rugPdf0.20

 */



public class PdfException extends DocumentException {



// constructors



    /**

     * Constructs a <CODE>PdfException</CODE> whithout a message. 

     *

     * @since   rugPdf0.20

     */



    PdfException() {

		super();

    }



    /**

     * Constructs a <code>PdfException</code> with a message. 

     *

     * @param		message			a message describing the exception

     * @since		rugPdf0.20

     */



    PdfException(String message) {

		super(message);

    }

}
/*

 * @(#)PdfBorderDictionary.java		0.37 2000/10/05

 *       release iText0.37:         0.37 2000/10/05

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



/**

 * A <CODE>PdfBorderDictionary</CODE> define the appearance of a Border (Annotations).

 *

 * @see		PdfDictionary

 *

 * @author  bruno@lowagie.com

 * @version 0.37 2000/10/05

 * @since   rugPdf0.37

 */



class PdfBorderDictionary extends PdfDictionary {



// constructors



	/**

	 * Constructs a default <CODE>PdfBorderDictionary</CODE>.

	 *

	 * @since		iText0.37

	 */



	PdfBorderDictionary() { 

		super(PdfName.BORDER);

		put(PdfName.S, PdfName.U);

		put(PdfName.W, new PdfNumber(2));

	}

}
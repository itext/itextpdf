/*
 * @(#)PdfGifColorTable.java		0.35 2000/08/21
 *               iText0.35*:		0.35 2000/08/21
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
 */

package com.lowagie.text.pdf;

/**
 * <CODE>PdfGifColor</CODE> is a <CODE>PdfString</CODE> containing a Gif Color Table.
 *
 * @author  bruno@lowagie.com
 * @version 0.35 2000/08/21
 * @since   iText0.35*
 */

class PdfGifColorTable extends PdfString {

// constructor

	/**
	 * Constructs a <CODE>PdfString</CODE>-object.
	 *
	 * @param		bytes	an array of <CODE>byte</CODE>
	 *
	 * @since		iText0.30
	 */

	PdfGifColorTable(byte[] bytes) {
		super(bytes);
	}

// methods
	
	/**
	 * Gets the PDF representation of this <CODE>String</CODE> as a <CODE>String</CODE>
	 *
	 * @return		a <CODE>String</CODE>
	 *
	 * @since		rugPdf0.35*
	 */

	 String get() {
		// we create the StringBuffer that will be the PDF representation of the content
		StringBuffer pdfString = new StringBuffer("(");

		// we have to control all the characters in the content
		int length = value.length();
		char character;
		// loop over all the characters
		for (int i = 0; i < length; i++) {
			character = value.charAt(i);
			// setting the characters outside the representable ASCII characters set to FF
			if (character > 0xFF) {
				character = (char) 0xFF;
			}
			// special characters are escaped (reference manual: p38 Table 4.1)
			switch (character) {
			case '\\':
				pdfString.append("\\\\");
				break;
			case '(':
				pdfString.append("\\(");
				break;
			case ')':
				pdfString.append("\\)");
				break;
			default:
				pdfString.append(character);
			}
		}
		pdfString.append(')');
		// de StringBuffer is completed, we can return the String
		return pdfString.toString();
	}
}
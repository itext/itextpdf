/*
 * @(#)PdfPageElement.java			0.23 2000/02/02
 *       release rugPdf0.10:		0.02 99/03/30
 *               rugPdf0.20:		0.12 99/11/26
 *               iText0.3:			0.23 2000/02/14
 *               iText0.35:         0.23 2000/08/11
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
 * The <CODE>PdfPageElement</CODE> interface has to be implemented by <CODE>PdfPage</CODE> and <CODE>PdfPages</CODE>.
 *
 * @see		PdfPage
 * @see		PdfPages
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 * @since   rugPdf0.10
 */

interface PdfPageElement {

	/**
	 * Set the value for the <B>Parent</B> key in the Page or Pages Dictionary.
	 *
	 * @param	parent			an indirect reference to a <CODE>PdfPages</CODE>-object
	 * @return	<CODE>void</CODE>
	 *
	 * @since	rugPdf0.10
	 */

	public void setParent(PdfIndirectReference reference);

	/**
	 * Checks if this page element is a tree of pages.
	 *
	 * @return	<CODE>true</CODE> if it's a tree of pages;
	 *			<CODE>false</CODE> if it's a single page
	 *
	 * @since	iText0.30
	 */

	public boolean isParent();
}
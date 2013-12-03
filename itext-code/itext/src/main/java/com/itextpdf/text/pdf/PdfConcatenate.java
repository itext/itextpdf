/*
 * $Id: PdfConcatenate.java 5914 2013-07-28 14:18:11Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf;

import java.io.IOException;
import java.io.OutputStream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

/**
 * Wrapper class for PdfCopy and PdfSmartCopy.
 * Allows you to concatenate existing PDF documents with much less code.
 */
public class PdfConcatenate {
	/** The Document object for PdfCopy. */
	protected Document document;
	/** The actual PdfWriter */
	protected PdfCopy copy;
	
	/**
	 * Creates an instance of the concatenation class.
	 * @param os	the OutputStream for the PDF document
	 */
	public PdfConcatenate(OutputStream os) throws DocumentException {
		this(os, false);
	}

	/**
	 * Creates an instance of the concatenation class.
	 * @param os	the OutputStream for the PDF document
	 * @param smart	do we want PdfCopy to detect redundant content?
	 */
	public PdfConcatenate(OutputStream os, boolean smart) throws DocumentException {
		document = new Document();
		if (smart)
			copy = new PdfSmartCopy(document, os);
		else
			copy = new PdfCopy(document, os);	
	}
	
	/**
	 * Adds the pages from an existing PDF document.
	 * @param reader	the reader for the existing PDF document
	 * @return			the number of pages that were added
	 * @throws DocumentException
	 * @throws IOException
	 */
	public int addPages(PdfReader reader) throws DocumentException, IOException {
		open();
		int n = reader. getNumberOfPages();
	    for (int i = 1; i <= n; i++) {
	        copy.addPage(copy.getImportedPage(reader, i));
	    }
	    copy.freeReader(reader);
	    reader.close();
	    return n;
	}
	
	/**
	 * Gets the PdfCopy instance so that you can add bookmarks or change preferences before you close PdfConcatenate.
	 */
	public PdfCopy getWriter() {
		return copy;
	}
	
	/**
	 * Opens the document (if it isn't open already).
	 * Opening the document is done implicitly.
	 */
	public void open() {
		if (!document.isOpen()) {
			document.open();
		}
	}
	
	/**
	 * We've finished writing the concatenated document.
	 */
	public void close() {
		document.close();
	}
}

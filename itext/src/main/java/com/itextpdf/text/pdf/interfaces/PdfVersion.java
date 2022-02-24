/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
package com.itextpdf.text.pdf.interfaces;

import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfName;

/**
 * The PDF version is described in the PDF Reference 1.7 p92
 * (about the PDF Header) and page 139 (the version entry in
 * the Catalog). You'll also find info about setting the version
 * in the book 'iText in Action' sections 2.1.3 (PDF Header)
 * and 3.3 (Version history).
 */

public interface PdfVersion {
    
    /**
	 * If the PDF Header hasn't been written yet,
	 * this changes the version as it will appear in the PDF Header.
	 * If the PDF header was already written to the OutputStream,
	 * this changes the version as it will appear in the Catalog.
	 * @param version	a character representing the PDF version
	 */
	public void setPdfVersion(char version);
    /**
	 * If the PDF Header hasn't been written yet,
	 * this changes the version as it will appear in the PDF Header,
	 * but only if the parameter refers to a higher version.
	 * If the PDF header was already written to the OutputStream,
	 * this changes the version as it will appear in the Catalog.
	 * @param version	a character representing the PDF version
	 */
	public void setAtLeastPdfVersion(char version);
	/**
	 * Sets the PDF version as it will appear in the Catalog.
	 * Note that this only has effect if you use a later version
	 * than the one that appears in the header. This method
	 * ignores the parameter if you try to set a lower version
	 * than the one currently set in the Catalog.
	 * @param version	the PDF name that will be used for the Version key in the catalog
	 */
	public void setPdfVersion(PdfName version);
	/**
	 * Adds a developer extension to the Extensions dictionary
	 * in the Catalog.
	 * @param de	an object that contains the extensions prefix and dictionary
	 * @since	2.1.6
	 */
	public void addDeveloperExtension(PdfDeveloperExtension de);
}

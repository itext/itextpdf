/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, et al.
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
package com.itextpdf.text.pdf.mc;

import java.io.IOException;
import java.io.OutputStream;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * Removes all interactivity from an AcroForm, maintaining the
 * structure tree.
 * 
 * DISCLAIMER:
 * - Use this class only if the form is properly tagged.
 * - This class won't work with pages in which the CTM is changed
 * - This class may not work for form fields with more than one widget annotation
 */
public class MCFieldFlattener {

	/**
	 * Processes a properly tagged PDF form.
	 * @param reader the PdfReader instance holding the PDF
	 * @param os	the OutputStream to which the flattened file will be written
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public void process(PdfReader reader, OutputStream os) throws IOException, DocumentException {
		int n = reader.getNumberOfPages();
		// getting the root dictionary
		PdfDictionary catalog = reader.getCatalog();
		// flattening means: remove AcroForm
		catalog.remove(PdfName.ACROFORM);
		// read the structure and create a parser
		StructureItems items = new StructureItems(reader);
		MCParser parser = new MCParser(items);
		// loop over all pages
		for (int i = 1; i <= n; i++) {
			// make one stream of a content stream array
			reader.setPageContent(i, reader.getPageContent(i));
			// parse page
			parser.parse(reader.getPageN(i), reader.getPageOrigRef(i));
		}
		reader.removeUnusedObjects();
		// create flattened file
		PdfStamper stamper = new PdfStamper(reader, os);
		items.writeParentTree(stamper.getWriter());
		stamper.close();
	}
}

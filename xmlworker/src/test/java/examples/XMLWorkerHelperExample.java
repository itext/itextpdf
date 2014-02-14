/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;

/**
 * @author itextpdf.com
 *
 */
public class XMLWorkerHelperExample extends Setup {

	/**
	 * Parse html to a PDF.
	 * With the XMLWorkerHelper this is done in no time. Create a Document and a
	 * PdfWriter. Don't forget to open the document and call
	 * <code>XMLWorkerHelper.getInstance().parseXHtml()</code>. This test takes
	 * html from <code>columbus.html</code>. This document contains &lt;img&gt;
	 * tags with relative src attributes. You'll see that the images are not
	 * added, unless they are absolute url's or file paths.
	 *
	 * @throws DocumentException
	 * @throws IOException
	 */
	@Test
	public void defaultSetup() throws DocumentException, IOException {
		Document doc = new Document(PageSize.A4);
		PdfWriter instance = PdfWriter.getInstance(doc, new FileOutputStream(new File(
				"./src/test/resources/examples/columbus.pdf")));
		doc.open();
		XMLWorkerHelper.getInstance().parseXHtml(instance, doc,
				XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"), Charset.defaultCharset());
		doc.close();
	}

	/**
	 * Create lists of {@link Element} instead of a document
	 * @throws IOException
	 */
	@Test
	public void defaultSetupWithoutDocument() throws IOException {
		XMLWorkerHelper.getInstance().parseXHtml(new ElementHandler() {

			public void add(final Writable w) {
				if (w instanceof WritableElement) {
					List<Element> elements = ((WritableElement)w).elements();
					// do something with the lists of elements
				}

			}
		}, XMLWorkerHelperExample.class.getResourceAsStream("columbus.html"), Charset.defaultCharset());
	}
}

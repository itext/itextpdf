/*
 * $Id:  $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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
package com.itextpdf.text.html.parser;

import static junit.framework.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Balder
 *
 */
public class HtmlXFAWorkerTest {

	private StringBuilder snippet;
	private BufferedReader inreader;

	/**
	 * @throws IOException
	 * @since 5.0.6
	 */
	@Before
	public void setup() throws IOException {
		this.snippet = new StringBuilder();
		InputStream in = HtmlXFAWorkerTest.class.getResourceAsStream("/html/snippet/xfa-support-snippet.html");
		inreader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while (null != (line = inreader.readLine())) {
			this.snippet.append(line);
		}
		in.close();
	}

	/**
	 * @throws IOException
	 * @throws DocumentException
	 * @since 5.0.6
	 */

	public void simpleSnippet() throws IOException, DocumentException {
			HashMap<String, Object> providers = new HashMap<String, Object>();
			List<Element> parseToList = HTMLWorker.parseToList(
					new StringReader(snippet.toString()), null, providers);
			Document d = new Document(PageSize.A4);
			PdfWriter
					.getInstance(d, new FileOutputStream(new File("./test.pdf")));
			d.open();
			for (Element e : parseToList) {
				try {
					d.add(e);
				} catch (DocumentException e1) {
					e1.printStackTrace();
					fail("unable to add element " + e);
				}
			}
			d.close();

	}

	@Test
	public void dummy() {

	}

}

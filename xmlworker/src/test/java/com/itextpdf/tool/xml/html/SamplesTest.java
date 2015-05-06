/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
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
package com.itextpdf.tool.xml.html;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

/**
 * Added to visually check all snippets. (see target/text-classes/*.pdf )
 */
public class SamplesTest {
    public static final String OUT = "./target/test-classes/com/itextpdf/tool/xml/html/";
	private final List<String> list = new ArrayList<String>();

	static {
	//	FontFactory.registerDirectories();
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
	}

	@Before
	public void setup() {
		list.add("widthTable_");
		list.add("test-table-a_");
		list.add("test-table-b_");
		list.add("test-table-c_");
		list.add("test-table-d_");
		list.add("xfa-support_");
		list.add("position_");
		list.add("b-p_");
		list.add("br-sub-sup_");
		list.add("div_");
		list.add("font_color_");
		list.add("lineheight_");
//		list.add("index_");
		list.add("img_");
		list.add("h_");
		list.add("fontSizes_");
		list.add("line-height_letter-spacing_");
		list.add("longtext_");
		list.add("margin-align_");
		list.add("xfa-hor-vert_");
		list.add("text-indent_text-decoration_");
		list.add("comment-double-print_");
		list.add("tab_");
		list.add("table_");
		list.add("tableInTable_");
		list.add("lists_");
		list.add("headers_");
	}

	@Test
	public void createAllSamples() throws IOException {
		boolean success = true;
		for (String str : list) {
			try {
			System.out.println(str);
			final Document doc = new Document();
			PdfWriter writer = null;
			try {
				writer = PdfWriter.getInstance(doc, new FileOutputStream(String.format("%s/%sTest.pdf", OUT, str)));
			} catch (DocumentException e) {
				e.printStackTrace();
			}
			doc.open();
			BufferedInputStream bis = new BufferedInputStream(SamplesTest.class.getResourceAsStream(String.format("/snippets/%ssnippet.html", str)));
			XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
			helper.parseXHtml(writer, doc, new InputStreamReader(bis));
			doc.close();
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				success = false;
			}

		}
		if (!success) {
			Assert.fail();
		}
	}
}

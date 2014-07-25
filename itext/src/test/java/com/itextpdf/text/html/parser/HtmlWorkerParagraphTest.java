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

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.HTMLWorker;

/**
 * @author Balder
 *
 */
public class HtmlWorkerParagraphTest {

	@Test
	public void htmlP2ParagraphNormal() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p>This is a Simple Snippet</p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(0, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphBold() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p><b>This is a Simple Snippet</b></p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(1, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphStrong() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p><strong>This is a Simple Snippet</strong></p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(1, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphItalic() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p><i>This is a Simple Snippet</i></p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(2, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphItalicByStyle() throws IOException {
		List<Element> parseToList = HTMLWorker
				.parseToList(
						new StringReader(
								"<p style=\"font-style: italic;\">This is a Simple Snippet</i></p>"),
						null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(2, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphObliqueByStyle() throws IOException {
		List<Element> parseToList = HTMLWorker
				.parseToList(
						new StringReader(
								"<p style=\"font-style: italic;\">This is a Simple Snippet</i></p>"),
						null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(2, c.getFont().getStyle());
	}
	@Test
	public void htmlP2ParagraphBoldByStyle() throws IOException {
		List<Element> parseToList = HTMLWorker
		.parseToList(
				new StringReader(
						"<p style=\"font-weight: bold;\">This is a Simple Snippet</i></p>"),
						null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(1, c.getFont().getStyle());
	}
}

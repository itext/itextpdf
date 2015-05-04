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
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.tool.xml.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.parser.io.Appender;
import com.itextpdf.tool.xml.parser.io.ParserListenerWriter;

/**
 * @author redlab_b
 *
 */
public class ParserTest {

	/**
	 *
	 */
	@Before
	public void setup() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
	}

	/**
	 * Validate comment whitespace handling .
	 *
	 * @throws IOException
	 */
	@Test
	public void stickyComment() throws IOException {
		String html = "<p><!--stickycomment-->sometext  moretext</p>";
		String expected = "<p><!--stickycomment-->sometext  moretext</p>";
		XMLParser p = new XMLParser(false, Charset.defaultCharset());
		final StringBuilder b = init(html, p);
		p.parse(new ByteArrayInputStream(html.getBytes()));
		Assert.assertEquals(expected, b.toString());
	}

	/**
	 * Validate attribute handling of &lt;?abc defg hijklm ?&gt;.
	 *
	 * @throws IOException
	 */
	@Test
	public void specialTag() throws IOException {
		String html = "<p><?formServer acrobat8.1dynamic defaultPDFRenderFormat?>ohoh</p>";
		XMLParser p = new XMLParser(false, Charset.forName("UTF-8"));
		final StringBuilder b = init(html, p);
		p.parse(new ByteArrayInputStream(html.getBytes()));
		String str = b.toString();
		Assert.assertTrue(str.contains("acrobat8.1dynamic") && str.contains("defaultPDFRenderFormat"));
	}

	@Test
	public void specialChars() throws IOException {
		final List<String> list = new ArrayList<String>();
		XMLParser p = new XMLParser(false, new XMLParserListener() {
			public void unknownText(final String text) {
			}


			public void startElement(final String tag, final Map<String, String> attributes, final String ns) {
			}

			public void init() {
			}

			public void endElement(final String tag, final String ns) {
			}

			public void comment(final String comment) {
			}

			public void close() {
			}

			public void text(final String text) {
					list.add(text);
				

			}
		});
		p.parse(ParserTest.class.getResourceAsStream("parser.xml"), Charset.forName("UTF-8"));
//		org.junit.Assert.assertEquals(new String("e\u00e9\u00e8\u00e7\u00e0\u00f5".getBytes("UTF-8"), "UTF-8"), list.get(0));
//		org.junit.Assert.assertEquals(new String("e\u00e9\u00e8\u00e7\u00e0\u00f5".getBytes("UTF-8"), "UTF-8"), list.get(1));
		org.junit.Assert.assertEquals("e\u00e9\u00e8\u00e7\u00e0\u00f5", list.get(0));
		org.junit.Assert.assertEquals("e\u00e9\u00e8\u00e7\u00e0\u00f5", list.get(1));
	}

	public void readBare() throws UnsupportedEncodingException {
		InputStreamReader inputStreamReader = new InputStreamReader(ParserTest.class.getResourceAsStream("parser.xml"),
				"UTF-8");
	}

	/**
	 * @param html
	 * @param p
	 * @return
	 */
	private StringBuilder init(final String html, final XMLParser p) {
		final StringBuilder writer = new StringBuilder(html.length());
		p.addListener(new ParserListenerWriter(new Appender() {

			public Appender append(final char c) {
				writer.append(c);
				return this;
			}

			public Appender append(final String str) {
				writer.append(str);
				return this;
			}
		}, false));
		return writer;
	}
}

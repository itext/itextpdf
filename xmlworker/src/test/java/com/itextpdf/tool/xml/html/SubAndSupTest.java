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
package com.itextpdf.tool.xml.html;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Balder
 *
 */
public class SubAndSupTest {

	private List<Element> elementList;

	@Before
	public void setup() throws IOException {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		BufferedInputStream bis = new BufferedInputStream(SubAndSupTest.class.getResourceAsStream("/snippets/br-sub-sup_snippet.html"));
		XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
		elementList = new ArrayList<Element>();
		helper.parseXHtml(new ElementHandler() {

			public void add(final Writable w) {
				elementList.addAll(((WritableElement) w).elements());

			}
        }, new InputStreamReader(bis));

	}
	@Test
	public void resolveNumberOfElements() throws IOException {
		assertEquals(8, elementList.size()); // Br's count for one element(Chunk.NEWLINE).
	}
	@Test
	public void resolveNewLines() throws IOException {
		assertEquals(Chunk.NEWLINE.getContent(), elementList.get(1).getChunks().get(0).getContent());
        assertEquals(8f, elementList.get(1).getChunks().get(0).getFont().getSize(), 0);
		assertEquals(Chunk.NEWLINE.getContent(), elementList.get(4).getChunks().get(1).getContent());
	}
	@Test
	public void resolveFontSize() throws IOException {
		assertEquals(12, elementList.get(5).getChunks().get(0).getFont().getSize(), 0);
		assertEquals(9.75f, elementList.get(5).getChunks().get(2).getFont().getSize(), 0);
		assertEquals(24, elementList.get(6).getChunks().get(0).getFont().getSize(), 0);
		assertEquals(18f, elementList.get(6).getChunks().get(2).getFont().getSize(), 0);
	}
	@Test
	public void resolveTextRise() throws IOException {
		assertEquals(-9.75f/2, elementList.get(5).getChunks().get(2).getTextRise(), 0);
		assertEquals(-9.75f/2, elementList.get(5).getChunks().get(4).getTextRise(), 0);
		assertEquals(18/2+0.5, elementList.get(6).getChunks().get(2).getTextRise(), 0);
		assertEquals(-18/2, elementList.get(6).getChunks().get(6).getTextRise(), 0);
		assertEquals(0, elementList.get(7).getChunks().get(0).getTextRise(), 0);
		assertEquals(-3, elementList.get(7).getChunks().get(2).getTextRise(), 0);
		assertEquals(4, elementList.get(7).getChunks().get(14).getTextRise(), 0);
		assertEquals(3, elementList.get(7).getChunks().get(22).getTextRise(), 0);
	}
	@Test
	public void resolvePhraseLeading() throws IOException {
		assertTrue(Math.abs(1.2f - ((Paragraph) elementList.get(5)).getMultipliedLeading()) < 0.0001);
		assertTrue(Math.abs(1.2f - ((Paragraph) elementList.get(6)).getMultipliedLeading()) < 0.0001);
	}

}

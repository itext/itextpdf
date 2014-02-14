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
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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
public class LineHeightLetterSpacingTest {

	private TagProcessorFactory factory;
	private XMLWorker worker;
	private ByteArrayOutputStream baos;
	private XMLWorkerHelper workerFactory;
	private List<Element> elementList;

	@Before
	public void setup() throws IOException {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		BufferedInputStream bis = new BufferedInputStream(LineHeightLetterSpacingTest.class.getResourceAsStream("/snippets/line-height_letter-spacing_snippet.html"));
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
		assertEquals(7, elementList.size());
	}
	@Test
	public void resolveFontSize() throws IOException {
		assertEquals(16, elementList.get(2).getChunks().get(0).getFont().getSize(), 0);
		assertEquals(15, elementList.get(4).getChunks().get(0).getFont().getSize(), 0);
	}
	@Test
	public void resolveLeading() throws IOException {
		assertTrue(Math.abs(1.2f - ((Paragraph)elementList.get(0)).getMultipliedLeading()) < 0.0001f);
		assertEquals(8, ((Paragraph)elementList.get(1)).getLeading(), 0); // leading laten bepalen door inner line-height setting?
		assertEquals(160, ((Paragraph)elementList.get(2)).getLeading(), 0);
		assertEquals(21, ((Paragraph)elementList.get(3)).getLeading(), 0); //1.75em
		assertEquals(45, ((Paragraph)elementList.get(4)).getLeading(), 0);
	}
	@Test
	public void resolveCharSpacing() throws IOException {
		assertEquals(CssUtils.getInstance().parsePxInCmMmPcToPt("1.6pc"), elementList.get(5).getChunks().get(0).getCharacterSpacing(), 0);
		assertEquals(CssUtils.getInstance().parseRelativeValue("0.83em", elementList.get(6).getChunks().get(2).getFont().getSize()), elementList.get(6).getChunks().get(2).getCharacterSpacing(), 0);
	}
}

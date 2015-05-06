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
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * @author Balder
 *
 */
public class AlignAndMarginTest {

	private List<Element> elementList;

	@Before
	public void setup() throws IOException {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		BufferedInputStream bis = new BufferedInputStream(AlignAndMarginTest.class.getResourceAsStream("/snippets/margin-align_snippet.html"));
		XMLWorkerHelper helper = XMLWorkerHelper.getInstance();
		elementList = new ArrayList<Element>();
		helper.parseXHtml(new ElementHandler() {

			public void add(final Writable w) {
				elementList.addAll(((WritableElement) w).elements());

			}
        }, new InputStreamReader(bis));
	}
	/*
	@Test
	public void resolveNumberOfElements() throws IOException {
		assertEquals(5, elementList.size());
	}

	@Test
	public void resolveAlignment() throws IOException {
		assertEquals(Element.ALIGN_CENTER,((Paragraph)elementList.get(0)).getAlignment());
		assertEquals(Element.ALIGN_LEFT, ((Paragraph)elementList.get(1)).getAlignment());
		assertEquals(Element.ALIGN_RIGHT, ((Paragraph)elementList.get(2)).getAlignment());
		assertEquals(Element.ALIGN_LEFT, ((Paragraph)elementList.get(3)).getAlignment());
	}
	*/
	@Test
	public void resolveIndentations() throws IOException {
		CssUtils cssUtils = CssUtils.getInstance();
		assertEquals(cssUtils.parseRelativeValue("1em", 12), ((Paragraph)elementList.get(0)).getSpacingBefore(), 0);
		assertEquals(cssUtils.parsePxInCmMmPcToPt("1.5in"), ((Paragraph)elementList.get(1)).getIndentationLeft(), 0);
		assertEquals(cssUtils.parsePxInCmMmPcToPt("3cm"), ((Paragraph)elementList.get(2)).getIndentationRight(), 0);
		assertEquals(cssUtils.parsePxInCmMmPcToPt("5pc")-12, ((Paragraph)elementList.get(3)).getSpacingBefore(), 0);
		assertEquals(cssUtils.parsePxInCmMmPcToPt("4pc"), ((Paragraph)elementList.get(3)).getSpacingAfter(), 0);
		assertEquals(cssUtils.parsePxInCmMmPcToPt("80px")-cssUtils.parsePxInCmMmPcToPt("4pc"), ((Paragraph)elementList.get(4)).getSpacingBefore(), 0);
		assertEquals(cssUtils.parsePxInCmMmPcToPt("80px"), ((Paragraph)elementList.get(4)).getSpacingAfter(), 0);
	}
	@After
	public void cleanup() throws IOException {
	}
}

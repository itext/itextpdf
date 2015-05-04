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
import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Element;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.pipeline.WritableElement;


/**
 * @author Balder
 *
 */
public class HorAndVertScalingTest {

	private List<Element> elementList;

	@Before
	public void setup() throws IOException {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		BufferedInputStream bis = new BufferedInputStream(HorAndVertScalingTest.class.getResourceAsStream("/snippets/xfa-hor-vert_snippet.html"));
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
		assertEquals(4, elementList.size());
	}
	@Test
	public void resolveFontSize() throws IOException {
		assertEquals(12, elementList.get(0).getChunks().get(0).getFont().getSize(), 0);
		assertEquals(16, elementList.get(1).getChunks().get(0).getFont().getSize(), 0);
		assertEquals(16*1.5, elementList.get(1).getChunks().get(2).getFont().getSize(), 0);
		assertEquals(15, elementList.get(2).getChunks().get(0).getFont().getSize(), 0);
		assertEquals(7.5, elementList.get(2).getChunks().get(2).getFont().getSize(), 0);
		assertEquals(6.375, elementList.get(2).getChunks().get(4).getFont().getSize(), 0);
	}
	@Test
	public void resolveScaling() throws IOException {
		assertEquals(1, elementList.get(1).getChunks().get(0).getHorizontalScaling(), 0);
		assertEquals(1/1.5f, elementList.get(1).getChunks().get(2).getHorizontalScaling(), 0);
		assertEquals(1, elementList.get(2).getChunks().get(0).getHorizontalScaling(), 0);
		assertEquals(1/0.5f, elementList.get(2).getChunks().get(2).getHorizontalScaling(), 0);
		assertEquals(1/0.5f, elementList.get(2).getChunks().get(4).getHorizontalScaling(), 0);
		assertEquals(1, elementList.get(3).getChunks().get(0).getHorizontalScaling(), 0);
		assertEquals(1.5, elementList.get(3).getChunks().get(2).getHorizontalScaling(), 0);
	}
}

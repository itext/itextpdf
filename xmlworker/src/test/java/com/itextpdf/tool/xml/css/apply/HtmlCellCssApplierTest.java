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
package com.itextpdf.tool.xml.css.apply;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.html.table.TableRowElement;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author Emiel Ackermann
 *
 */
public class HtmlCellCssApplierTest {

	private final List<Element> cells = new ArrayList<Element>();
	Tag tag = new Tag("td", new HashMap<String, String>());
	private final NoNewLineParagraph basicPara = new NoNewLineParagraph();
	private final Chunk basic = new Chunk("content");

	private TableRowElement row1;
	private final HtmlCell cell = new HtmlCell();
	private final HtmlCellCssApplier applier = new HtmlCellCssApplier();
	private HtmlPipelineContext config;

	@Before
	public void setup() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		Tag parent = new Tag("tr");
		parent.setParent(new Tag("table"));
		tag.setParent(parent);
		basicPara.add(basic);
		cell.addElement(basicPara);
		cells.add(cell);
		config = new HtmlPipelineContext(null);
	}

	/*Disabled as long as the default borders are enabled*/
	public void resolveNoBorder() {
		applier.apply(cell, tag, config,config);
		assertEquals(Rectangle.NO_BORDER, cell.getBorder());
	}

	@Test
	public void resolveColspan() {
		assertEquals(1, cell.getColspan(), 0);
		tag.getAttributes().put("colspan", "2");
		applier.apply(cell, tag, config,config);
		assertEquals(2, cell.getColspan());
	}

	@Test
	public void resolveRowspan() {
		assertEquals(1, cell.getRowspan(), 0);
		tag.getAttributes().put("rowspan", "3");
		applier.apply(cell, tag, config,config);
		assertEquals(3, cell.getRowspan());
	}

	@Test
	public void resolveFixedWidth() {
		HtmlCell fixed = new HtmlCell();
		tag.getAttributes().put("width", "90pt");
		fixed = applier.apply(fixed, tag, config,config);
		assertEquals(90, (fixed).getFixedWidth(), 0);
	}

	@Test
	public void resolveBorderWidth() {
		assertEquals(0.5, cell.getBorderWidthTop(), 0);
		tag.getCSS().put("border-width-top", "5pt");
		tag.getCSS().put("border-width-left", "6pt");
		tag.getCSS().put("border-width-right", "7pt");
		tag.getCSS().put("border-width-bottom", "8pt");
		applier.apply(cell, tag, config,config);
		assertEquals(5, cell.getCellValues().getBorderWidthTop(), 0);
		assertEquals(6, cell.getCellValues().getBorderWidthLeft(), 0);
		assertEquals(7, cell.getCellValues().getBorderWidthRight(), 0);
		assertEquals(8, cell.getCellValues().getBorderWidthBottom(), 0);
	}
	@Test
	public void resolveBorderColor() {
		assertEquals(null, cell.getBorderColorTop());
		tag.getCSS().put("border-color-top", "red");
		tag.getCSS().put("border-color-left", "#0f0");
		tag.getCSS().put("border-color-right", "#0000ff");
		tag.getCSS().put("border-color-bottom", "rgb(000,111,222)");
		applier.apply(cell, tag, config,config);
		assertEquals(BaseColor.RED, cell.getCellValues().getBorderColorTop());
		assertEquals(BaseColor.GREEN, cell.getCellValues().getBorderColorLeft());
		assertEquals(BaseColor.BLUE, cell.getCellValues().getBorderColorRight());
		assertEquals(new BaseColor(000,111,222), cell.getCellValues().getBorderColorBottom());
	}
}

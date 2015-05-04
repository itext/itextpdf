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
package com.itextpdf.tool.xml.html.table;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.tool.xml.html.CssAppliersImpl;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.AbstractTagProcessor;
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.html.table.TableRowElement.Place;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

public class TableTest {
	private final List<Element> cells1 = new ArrayList<Element>();
	private final List<Element> cells2 = new ArrayList<Element>();
	private final List<Element> rows = new ArrayList<Element>();
	Tag tag = new Tag(null, new HashMap<String, String>());
	private final NoNewLineParagraph basicPara = new NoNewLineParagraph();
	private final NoNewLineParagraph extraPara = new NoNewLineParagraph();
	private final Chunk basic = new Chunk("content");
	private final Chunk extra = new Chunk("extra content");

	private TableRowElement row1;
	private final HtmlCell cell1Row1 = new HtmlCell();
	private final HtmlCell cell2Row1 = new HtmlCell();
	private final HtmlCell cell3Row1 = new HtmlCell();
	private final HtmlCell cell4Row1 = new HtmlCell();
	private TableRowElement row2;
	private final HtmlCell cell1Row2 = new HtmlCell();
	private final HtmlCell cell2Row2 = new HtmlCell();
	private final HtmlCell cell3Row2 = new HtmlCell();

	@Before
	public void setup() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		tag.setParent(new Tag("defaultRoot"));
		basicPara.add(basic);
		extraPara.add(extra);
		cell1Row1.addElement(basicPara);
		cell2Row1.addElement(extraPara);
		cell3Row1.addElement(basicPara);
		cell4Row1.addElement(extraPara);
		cell4Row1.setRowspan(2);
		cells1.add(cell1Row1);
		cells1.add(cell2Row1);
		cells1.add(cell3Row1);
		cells1.add(cell4Row1);
		row1 = new TableRowElement(cells1, Place.BODY);

		cell1Row2.addElement(extraPara);
		cell2Row2.addElement(basicPara);
//		Tag t = new Tag(null, new HashMap<String, String>());
//		t.getAttributes().put("col-span", "2");
		cell2Row2.setColspan(2);
		cell3Row2.addElement(extraPara);
		cells2.add(cell1Row2);
		cells2.add(cell2Row2);
		//cells2.add(cell3Row2);
		row2 = new TableRowElement(cells2, Place.BODY);

		rows.add(row1);
		rows.add(row2);
	}

	@Test
	public void resolveBuild() {
		AbstractTagProcessor table2 = new Table();
        table2.setCssAppliers(new CssAppliersImpl());
		WorkerContextImpl context = new WorkerContextImpl();
		context.put(HtmlPipeline.class.getName(), new HtmlPipelineContext(null));
		PdfPTable table = (PdfPTable) (table2.end(context, tag, rows).get(0));
		assertEquals(4, table.getRow(0).getCells().length);
		assertEquals(4, table.getNumberOfColumns());
	}

	@Test
	public void resolveNumberOfCells() {
		assertEquals(4, ((TableRowElement) rows.get(0)).getContent().size());
		assertEquals(2, ((TableRowElement) rows.get(1)).getContent().size());
	}
	@Test
	public void resolveColspan() {
		assertEquals(2, (((TableRowElement) rows.get(1)).getContent().get(1)).getColspan());
	}
	@Test
	public void resolveRowspan() {
		assertEquals(2, (((TableRowElement) rows.get(0)).getContent().get(3)).getRowspan());
	}
}

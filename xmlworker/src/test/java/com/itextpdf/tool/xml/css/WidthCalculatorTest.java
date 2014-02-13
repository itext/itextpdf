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
package com.itextpdf.tool.xml.css;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author Emiel Ackermann
 *
 */
public class WidthCalculatorTest {

	Tag body = new Tag("body", new HashMap<String,String>());
	Tag table = new Tag("table", new HashMap<String,String>());
	Tag row = new Tag("tr", new HashMap<String,String>());
	Tag cell = new Tag("td", new HashMap<String,String>());
	private final HtmlPipelineContext config = new HtmlPipelineContext(null);
	private final WidthCalculator calc = new WidthCalculator();

	@Before
	public void before() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		body.addChild(table);
		table.setParent(body);
		table.addChild(row);
		row.setParent(table);
		row.addChild(cell);
		cell.setParent(row);
	}

	@Test
	public void resolveBodyWidth80() throws IOException {
		body.getAttributes().put(HTML.Attribute.WIDTH, "80%");
		assertEquals(0.8*config.getPageSize().getWidth(), calc.getWidth(body, config.getRootTags(), config.getPageSize().getWidth()), 0);
	}
	@Test
	public void resolveTableWidth80() throws IOException {
		table.getAttributes().put(HTML.Attribute.WIDTH, "80%");
		assertEquals(0.8*config.getPageSize().getWidth(), calc.getWidth(table, config.getRootTags(), config.getPageSize().getWidth()), 0);
	}
	@Test
	public void resolveCellWidth20of100() throws IOException {
		cell.getAttributes().put(HTML.Attribute.WIDTH, "20%");
		assertEquals(config.getPageSize().getWidth()*0.2f, calc.getWidth(cell, config.getRootTags(), config.getPageSize().getWidth()),0.01f);
	}
	@Test
	public void resolveCellWidth20of80() throws IOException {
		table.getAttributes().put(HTML.Attribute.WIDTH, "80%");
		cell.getAttributes().put(HTML.Attribute.WIDTH, "20%");
		assertEquals(0.8f*config.getPageSize().getWidth()*0.2f, calc.getWidth(cell, config.getRootTags(), config.getPageSize().getWidth()),0.01f);
	}
}

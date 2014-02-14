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
package com.itextpdf.tool.xml.html.tps;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfDiv;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Div;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author itextpdf.com
 *
 */
public class DivTest {
	final Div d = new Div();
	List<Element> currentContent = new ArrayList<Element>();
	private WorkerContextImpl workerContextImpl;

	@Before
	public void init() {
		workerContextImpl = new WorkerContextImpl();
		workerContextImpl.put(HtmlPipeline.class.getName(), new HtmlPipelineContext(null));
		currentContent.add(new Paragraph("titel paragraph"));
		currentContent.add(Chunk.NEWLINE);
		currentContent.add(new NoNewLineParagraph("first content text"));
		currentContent.add(new Paragraph("footer text"));
		d.setCssAppliers(new CssAppliersImpl());
	}

	/**
	 * Verifies that the call to content of {@link Div} returns a NoNewLineParagraph.
	 */
	@Test
	public void verifyContent() {
		final List<Element> content = d.content(workerContextImpl, new Tag("div"), "text inside a div tag");
		Assert.assertTrue(content.get(0) instanceof NoNewLineParagraph);
	}

	/**
	 * Verifies that the numbers of paragraphs returned by {@link Div#end}.
	 */
	@Test
	public void verifyNumberOfParagraphs() {
		final List<Element> endContent = d.end(workerContextImpl, new Tag("div"), currentContent);
		Assert.assertEquals(1, endContent.size());
	}

	/**
	 * Verifies that the class of the elements returned by {@link Div#end}.
	 */
	@Test
	public void verifyIfPdfDiv() {
		final List<Element> endContent = d.end(workerContextImpl, new Tag("div"), currentContent);
		Assert.assertTrue(endContent.get(0) instanceof PdfDiv);
	}

	/**
	 * Verifies if {@link Div} is a stack owner. Should be true.
	 */
	@Test
	public void verifyIfStackOwner() {
		Assert.assertTrue(d.isStackOwner());
	}
}

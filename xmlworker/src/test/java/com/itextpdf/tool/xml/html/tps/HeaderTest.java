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
package com.itextpdf.tool.xml.html.tps;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.WritableDirectElement;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Header;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author itextpdf.com
 *
 */
public class HeaderTest {
	/**
	 *
	 */
	private static final Tag H2 = new Tag("h2");
	final Header h = new Header();
	private List<Element> content = null;
	private WorkerContextImpl workerContextImpl;

	@Before
	public void init() {
		h.setCssAppliers(new CssAppliersImpl());
		workerContextImpl = new WorkerContextImpl();
		workerContextImpl.put(HtmlPipeline.class.getName(), new HtmlPipelineContext(null).autoBookmark(true));
		content = h.content(workerContextImpl, H2, "text inside a header tag");
	}

	/**
	 * Verifies that the call to content of {@link Header} returns a Chunk.
	 */
	@Test
	public void verifyContent() {
		Assert.assertTrue(content.get(0) instanceof Chunk);
	}

	/**
	 * Verifies if {@link Header#end} returns both a WritableDirectElement and a Paragraph.
	 */
	@Test
	public void verifyEnd() {
		List<Element> end = h.end(workerContextImpl, H2, content);
		Assert.assertTrue(end.get(0) instanceof WritableDirectElement);
		Assert.assertTrue(end.get(1) instanceof Paragraph);
	}
	/**
	 * Verifies if {@link Header} is a stack owner. Should be true.
	 */
	@Test
	public void verifyIfStackOwner() {
		Assert.assertTrue(h.isStackOwner());
	}
}

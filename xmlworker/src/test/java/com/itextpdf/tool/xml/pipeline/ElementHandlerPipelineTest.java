/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
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
package com.itextpdf.tool.xml.pipeline;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.PipelineException;
import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;

/**
 * @author itextpdf.com
 *
 * @deprecated For internal use only. If you want to use iText, please use a dependency on iText 7.
 */
@Deprecated
public class ElementHandlerPipelineTest {


	private List<Writable> lst;
	private ProcessObject po;
	private ElementHandlerPipeline p;
	private WritableElement writable;
	private WorkerContextImpl context;

	@Before
	public void setup() throws PipelineException {
		lst = new ArrayList<Writable>();
		ElementHandler elemH = new ElementHandler() {

			public void add(final Writable w) {
				lst.add(w);
			}
		};
		p = new ElementHandlerPipeline(elemH, null);
		po = new ProcessObject();
		writable = new WritableElement(new Chunk("aaaaa"));
		po.add(writable);
		context = new WorkerContextImpl();
		p.init(context);
	}

	/**
	 * Verifies that the content of the ProcessObject is processed on open.
	 * @throws PipelineException
	 */
	@Test
	public void runOpen() throws PipelineException {
		p.open(context, null, po);
		Assert.assertEquals(writable, lst.get(0));
	}
	/**
	 * Verifies that the content of the ProcessObject is processed on content.
	 * @throws PipelineException
	 */
	@Test
	public void runContent() throws PipelineException {
		p.content(context, null, null, po);
		Assert.assertEquals(writable, lst.get(0));
	}
	/**
	 * Verifies that the content of the ProcessObject is processed on close.
	 * @throws PipelineException
	 */
	@Test
	public void runClose() throws PipelineException {
		p.close(context, null, po);
		Assert.assertEquals(writable, lst.get(0));
	}
}

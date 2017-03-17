/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS
    
    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/
    
    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.
    
    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.
    
    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.
    
    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.PipelineException;
import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * @author Balder Van Camp
 *
 */
public class HtmlPipelineTest {

	private HtmlPipeline p;
	private WorkerContextImpl wc;
	private HtmlPipelineContext hpc;
	/**
	 * @throws PipelineException
	 *
	 */
	@Before
	public void setup() throws PipelineException {
		hpc = new HtmlPipelineContext(null);
		p = new HtmlPipeline(hpc, null);
		wc = new WorkerContextImpl();
		p.init(wc);
	}

	@Test
	public void init() throws PipelineException {
		Assert.assertNotNull(p.getLocalContext(wc));
	}
	@Test
	public void text() throws PipelineException, UnsupportedEncodingException {
		final String b = new String("aeéèàçï".getBytes(), "ISO-8859-1");
		 TagProcessorFactory tagFactory = new TagProcessorFactory() {

				public void removeProcessor(final String tag) {
				}

				public TagProcessor getProcessor(final String tag, final String nameSpace) throws NoTagProcessorException {
					if (tag.equalsIgnoreCase("tag"));
					return new TagProcessor() {

						public List<Element> startElement(final WorkerContext ctx, final Tag tag) {
							return new ArrayList<Element>(0);
						}

						public boolean isStackOwner() {
							return false;
						}

						public List<Element> endElement(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
							return new ArrayList<Element>(0);
						}

						public List<Element> content(final WorkerContext ctx, final Tag tag, final String content) {
								Assert.assertEquals(b, content);
							return new ArrayList<Element>(0);
						}
					};
				}

				public void addProcessor(final TagProcessor processor, final String... tags) {
				}
			};;
		p.getLocalContext(wc).setTagFactory(tagFactory ).charSet(Charset.forName("ISO-8859-1"));
		p.content(wc, new Tag("tag"), b , new ProcessObject());
	}
}

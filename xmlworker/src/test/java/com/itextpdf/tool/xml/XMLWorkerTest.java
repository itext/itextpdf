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
package com.itextpdf.tool.xml;

import java.util.HashMap;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Balder Van Camp
 *
 */
public class XMLWorkerTest {


	private XMLWorker worker;
	protected boolean called = false;

	@Before
	public void setup() {
		worker = new XMLWorker(new Pipeline() {
			public Pipeline<?> open(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
				called = true;
				return null;
			}

			public Pipeline init(final WorkerContext context) throws PipelineException {
				called = true;
				return null;
			}

			public Pipeline content(final WorkerContext context, final Tag t, final String content, final ProcessObject po)
					throws PipelineException {
				called = true;
				return null;
			}

			public Pipeline close(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
				called = true;
				return null;
			}

			public Pipeline getNext() {
				return null;
			}


		}, false);
	}

	@Test
	public void verifyPipelineInitCalled() {
		worker.init();
		Assert.assertTrue(called);
	}
	@Test
	public void verifyPipelineOpenCalled() {
		worker.startElement("test", new HashMap<String, String>() , "ns");
		Assert.assertTrue(called);
	}
	@Test
	public void verifyPipelineContentCalled() {
		worker.startElement("test", new HashMap<String, String>() , "ns");
		worker.text("test");
		Assert.assertTrue(called);
	}
	@Test
	public void verifyPipelineContentNotCalledOnNoTag() {
		worker.text("test");
		Assert.assertFalse(called);
	}
	@Test
	public void verifyPipelineCloseCalled() {
		worker.endElement("test", "ns");
		Assert.assertTrue(called);
	}

	@Test
	public void verifyNoCurrentTag() {
		worker.init();
		Assert.assertNull(worker.getCurrentTag());
	}
	@Test
	public void verifyCurrentTag() {
		worker.startElement("test", new HashMap<String, String>() , "ns");
		Assert.assertNotNull(worker.getCurrentTag());
	}

	@After
	public  void clean() {
		worker.close();
	}
}

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

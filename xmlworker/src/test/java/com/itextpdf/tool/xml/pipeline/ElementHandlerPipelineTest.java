/**
 *
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
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;

/**
 * @author itextpdf.com
 *
 */
public class ElementHandlerPipelineTest {


	private List<Writable> lst;
	private ProcessObject po;
	private ElementHandlerPipeline p;
	private WritableElement writable;

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
	}

	/**
	 * Verifies that the content of the ProcessObject is processed on open.
	 * @throws PipelineException
	 */
	@Test
	public void runOpen() throws PipelineException {
		p.close(null, po);
		Assert.assertEquals(writable, lst.get(0));
	}
	/**
	 * Verifies that the content of the ProcessObject is processed on content.
	 * @throws PipelineException
	 */
	@Test
	public void runContent() throws PipelineException {
		p.close(null, po);
		Assert.assertEquals(writable, lst.get(0));
	}
	/**
	 * Verifies that the content of the ProcessObject is processed on close.
	 * @throws PipelineException
	 */
	@Test
	public void runClose() throws PipelineException {
		p.close(null, po);
		Assert.assertEquals(writable, lst.get(0));
	}
}

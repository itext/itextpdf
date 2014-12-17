/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.NoImageProviderException;

/**
 * @author Balder Van Camp
 *
 */
public class HtmlPipelineCloneTest {

	private HtmlPipelineContext clone;
	private HtmlPipelineContext ctx;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		ctx = new HtmlPipelineContext(null);
		ctx.setImageProvider(new AbstractImageProvider() {

			public String getImageRootPath() {
				return "42 is the answer";
			}

		});
		clone = ctx.clone();
	}

	@Test
	public void verifyNewImageProvider() throws NoImageProviderException {
		Assert.assertSame(ctx.getImageProvider(), clone.getImageProvider());
	}
	@Test
	public void verifyNewRoottags() throws NoImageProviderException {
		Assert.assertNotSame(ctx.getRootTags(), clone.getRootTags());
	}
	@Test
	public void verifyNewPageSize() throws NoImageProviderException {
		Assert.assertNotSame(ctx.getPageSize(), clone.getPageSize());
	}
	@Test
	public void verifyNewMemory() throws NoImageProviderException {
		Assert.assertNotSame(ctx.getMemory(), clone.getMemory());
	}
	@Test
	public void verifySameLinkProvider() throws NoImageProviderException {
		Assert.assertEquals(ctx.getLinkProvider(), clone.getLinkProvider());
	}
}

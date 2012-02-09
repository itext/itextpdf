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

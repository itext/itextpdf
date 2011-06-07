/**
 *
 */
package com.itextpdf.tool.xml.pipeline;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.PipelineException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.CssResolverException;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;

/**
 * Verifies that the CssResolving process is executed by the CssResolverPipeline.
 *
 * @author itextpdf.com
 *
 */
public class CssResolverPipelineTest {

	private Map<String, String> css2;

	@Before
	public void setup() throws CssResolverException, PipelineException {
		StyleAttrCSSResolver css = new StyleAttrCSSResolver();
		css.addCss("dummy { key1: value1; key2: value2 } .aklass { key3: value3;} #dumid { key4: value4}");
		CssResolverPipeline p = new CssResolverPipeline(css, null);
		Tag t = new Tag("dummy");
		t.getAttributes().put("id", "dumid");
		t.getAttributes().put("class", "aklass");
		Pipeline<?> open = p.open(t, null);
		css2 = t.getCSS();
	}

	/**
	 * Verify that pipeline resolves css on tag.
	 *
	 * @throws CssResolverException
	 * @throws PipelineException
	 */
	@Test
	public void verifyCssResolvedTag() throws CssResolverException, PipelineException {
		Assert.assertEquals("value1", css2.get("key1"));
	}

	/**
	 * Verify that pipeline resolves css on tag2.
	 *
	 * @throws CssResolverException
	 * @throws PipelineException
	 */
	@Test
	public void verifyCssResolvedTag2() throws CssResolverException, PipelineException {
		Assert.assertEquals("value2", css2.get("key2"));
	}

	/**
	 * Verify that pipeline resolves css class.
	 *
	 * @throws CssResolverException
	 * @throws PipelineException
	 */
	@Test
	public void verifyCssResolvedClass() throws CssResolverException, PipelineException {
		Assert.assertEquals("value3", css2.get("key3"));
	}

	/**
	 * Verify that pipeline resolves css id.
	 *
	 * @throws CssResolverException
	 * @throws PipelineException
	 */
	@Test
	public void verifyCssResolvedId() throws CssResolverException, PipelineException {
		Assert.assertEquals("value4", css2.get("key4"));
	}

}

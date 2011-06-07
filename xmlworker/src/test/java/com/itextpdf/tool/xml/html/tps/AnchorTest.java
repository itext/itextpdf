/**
 *
 */
package com.itextpdf.tool.xml.html.tps;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.Anchor;

/**
 * @author itextpdf.com
 *
 */
public class AnchorTest {



	/**
	 * Validates that the content of an &lt;a&gt; is transformed to a Chunk.
	 */
	@Test
	public void testContentToChunk() {
		Anchor a = new Anchor();
		Tag t = new Tag("dummy");
		String content2 = "some content";
		List<Element> ct = a.content(t , content2);
		Assert.assertEquals(content2, ct.get(0).getChunks().get(0).getContent());
	}

	/**
	 * Verifies if {@link Achor} is a stack owner. Should be true.
	 */
	@Test
	public void verifyIfStackOwner() {
		Assert.assertTrue(new Anchor().isStackOwner());
	}
}

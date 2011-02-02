package com.itextpdf.text.html.parser;

import static junit.framework.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.HTMLWorker;

/**
 * @author Balder
 *
 */
public class HtmlWorkerWhitSpaceTest {

	@Test
	public void htmlPcheckWhiteSpaceRemoved() throws IOException {
		List<Element> parseToList = HTMLWorker
		.parseToList(
				new StringReader(
						"<p> This   is a  Simple Snippet  </i></p>"),
						null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals("This is a Simple Snippet ", c.getContent());
	}
}

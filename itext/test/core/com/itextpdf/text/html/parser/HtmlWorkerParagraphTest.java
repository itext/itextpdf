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
public class HtmlWorkerParagraphTest {

	@Test
	public void htmlP2ParagraphNormal() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p>This is a Simple Snippet</p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(0, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphBold() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p><b>This is a Simple Snippet</b></p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(1, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphStrong() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p><strong>This is a Simple Snippet</strong></p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(1, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphItalic() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p><i>This is a Simple Snippet</i></p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(2, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphItalicByStyle() throws IOException {
		List<Element> parseToList = HTMLWorker
				.parseToList(
						new StringReader(
								"<p style=\"font-style: italic;\">This is a Simple Snippet</i></p>"),
						null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(2, c.getFont().getStyle());
	}

	@Test
	public void htmlP2ParagraphObliqueByStyle() throws IOException {
		List<Element> parseToList = HTMLWorker
				.parseToList(
						new StringReader(
								"<p style=\"font-style: italic;\">This is a Simple Snippet</i></p>"),
						null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(2, c.getFont().getStyle());
	}
	@Test
	public void htmlP2ParagraphBoldByStyle() throws IOException {
		List<Element> parseToList = HTMLWorker
		.parseToList(
				new StringReader(
						"<p style=\"font-weight: bold;\">This is a Simple Snippet</i></p>"),
						null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		assertEquals(1, c.getFont().getStyle());
	}
}

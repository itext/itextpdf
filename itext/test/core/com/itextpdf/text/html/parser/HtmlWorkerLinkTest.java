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
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfName;

/**
 * @author Balder
 *
 */
public class HtmlWorkerLinkTest {

	@Test
	public void htmlAtagAsFirstElement() throws IOException {
		List<Element> parseToList = HTMLWorker.parseToList(new StringReader(
				"<p><a href=\"http://www.itextpdf.com/\">a link</a></p>"), null);
		Element e = parseToList.get(0);
		assertEquals(e.getClass(), Paragraph.class);
		Paragraph p = (Paragraph) e;
		Element ce = p.get(0);
		assertEquals(ce.getClass(), Chunk.class);
		Chunk c = (Chunk) ce;
		PdfAction action = ((PdfAction) c.getAttributes().get("ACTION"));
		assertEquals("http://www.itextpdf.com/",action.get(PdfName.URI).toString());
	}

}

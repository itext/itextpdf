package com.itextpdf.text.html.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;

import static junit.framework.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.ChainedProperties;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.simpleparser.LinkProcessor;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Balder
 *
 */
public class HtmlXFAWorkerTest {

	private StringBuilder snippet;
	private BufferedReader inreader;

	/**
	 * @throws IOException
	 * @since 5.0.6
	 */
	@Before
	public void setup() throws IOException {
		this.snippet = new StringBuilder();
		InputStream in = HtmlXFAWorkerTest.class.getResourceAsStream("/html/snippet/xfa-support-snippet.html");
		inreader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while (null != (line = inreader.readLine())) {
			this.snippet.append(line);
		}
		in.close();
	}

	/**
	 * @throws IOException
	 * @throws DocumentException
	 * @since 5.0.6
	 */
	@Test
	public void simpleSnippet() throws IOException, DocumentException {
			HashMap<String, Object> providers = new HashMap<String, Object>();
//			providers.put(HTMLWorker.LINK_PROVIDER, new LinkProcessor() {
//
//				public boolean process(Paragraph current,
//						ChainedProperties attrs) {
//					// TODO Auto-generated method stub
//					return false;
//				}
//			});
			List<Element> parseToList = HTMLWorker.parseToList(
					new StringReader(snippet.toString()), null, providers);
			Document d = new Document(PageSize.A4);
			PdfWriter
					.getInstance(d, new FileOutputStream(new File("test.pdf")));
			d.open();
			for (Element e : parseToList) {
				try {
					d.add(e);
				} catch (DocumentException e1) {
					e1.printStackTrace();
					fail("unable to add element " + e);
				}
			}
			d.close();

	}

	@Test
	public void dummy() {

	}

}

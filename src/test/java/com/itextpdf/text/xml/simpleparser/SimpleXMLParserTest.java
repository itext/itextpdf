/**
 * 
 */
package com.itextpdf.text.xml.simpleparser;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;

/**
 * 
 * @author Balder Van Camp
 * 
 */
public class SimpleXMLParserTest {

	/**
	 * Validate correct whitespace handling of SimpleXMLHandler.
	 * Carriage return received as text instead of space.
	 * 
	 * @throws IOException
	 */
	@Test
	public void whitespaceHtml() throws IOException {
		String whitespace = "<p>sometext\r moretext</p>";
		String expected = "sometext moretext";
		final StringBuilder b = new StringBuilder();
		SimpleXMLParser.parse(new SimpleXMLDocHandler() {

			public void text(String str) {
				b.append(str);

			}

			public void startElement(String tag, Map<String, String> h) {
				// TODO Auto-generated method stub

			}

			public void startDocument() {
				// TODO Auto-generated method stub

			}

			public void endElement(String tag) {
				// TODO Auto-generated method stub

			}

			public void endDocument() {
				// TODO Auto-generated method stub

			}
		}, null, new StringReader(whitespace), true);
		Assert.assertEquals(expected, b.toString());
	}
}

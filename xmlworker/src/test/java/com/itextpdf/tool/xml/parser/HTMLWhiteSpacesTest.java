/**
 *
 */
package com.itextpdf.tool.xml.parser;



import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

/**
 * @author itextpdf.com
 *
 */
public class HTMLWhiteSpacesTest {

	private String str;

	@Before
	public void setUp() {
		str = "<body><b>&euro;<b> 124</body>";
	}

	/**
	 * See that a space is not removed after a special char.
	 * @throws IOException
	 */
	@Test
	public void checkIfSpaceIsStillThere() throws IOException {
		final StringBuilder b = new StringBuilder();
		XMLParser p = new XMLParser(true, new XMLParserListener() {
			public void unknownText(final String text) {
			}
			public void text(final String text) {
				b.append(text);
			}
			public void startElement(final String tag, final Map<String, String> attributes, final String ns) {
			}
			public void init() {
			}
			public void endElement(final String tag, final String ns) {
			}
			public void comment(final String comment) {
			}
			public void close() {
			}
		});
		p.parse(new StringReader(str));
		Assert.assertEquals("\u20ac 124", b.toString());
	}
}

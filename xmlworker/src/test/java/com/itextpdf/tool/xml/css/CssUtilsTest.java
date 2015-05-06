/*
 * $Id$
 *
 * This file is part of the iText (R) project. Copyright (c) 1998-2015 iText Group NV Authors: Balder Van Camp, Emiel
 * Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public License along with this program; if not,
 * see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions of this program must display Appropriate
 * Legal Notices, as required under Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a covered work must retain the producer
 * line in every PDF that is created or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the iText software without disclosing the source
 * code of your own applications. These activities include: offering paid services to customers as an ASP, serving PDFs
 * on the fly in a web application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.css;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;

/**
 * @author redlab_b
 *
 */
public class CssUtilsTest {

	private static final int MAX = 10000;
	private CssUtils css;
	private String string;

	@Before
	public void setup() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		css = CssUtils.getInstance();
		string = "  een  twee   drie    vier    een  twee   drie    vier";
	}

	@Test
	public void calculateHorizontalMargin() {
		Tag t = new Tag(string);
		t.getCSS().put("margin-left", "15pt");
		t.getCSS().put("margin-right", "15pt");
		Assert.assertEquals(30, css.getLeftAndRightMargin(t, 0f), 0);
	}

	@Test
	public void validateMetricValue() {
		Assert.assertEquals(true, css.isMetricValue("px"));
		Assert.assertEquals(true, css.isMetricValue("in"));
		Assert.assertEquals(true, css.isMetricValue("cm"));
		Assert.assertEquals(true, css.isMetricValue("mm"));
		Assert.assertEquals(true, css.isMetricValue("pc"));
		Assert.assertEquals(false, css.isMetricValue("em"));
		Assert.assertEquals(false, css.isMetricValue("ex"));
		Assert.assertEquals(true, css.isMetricValue("pt"));
		Assert.assertEquals(true, css.isMetricValue("inch"));
		Assert.assertEquals(false, css.isMetricValue("m"));
	}

	@Test
	public void validateNumericValue() {
		Assert.assertEquals(true, css.isNumericValue("1"));
		Assert.assertEquals(true, css.isNumericValue("12"));
		Assert.assertEquals(true, css.isNumericValue("1.2"));
		Assert.assertEquals(true, css.isNumericValue(".12"));
		Assert.assertEquals(false, css.isNumericValue("12f"));
		Assert.assertEquals(false, css.isNumericValue("f1.2"));
		Assert.assertEquals(false, css.isNumericValue(".12f"));
	}

	@Test
	public void parseLength() {
		Assert.assertEquals(9, css.parsePxInCmMmPcToPt("12"), 0);
		Assert.assertEquals(576, css.parsePxInCmMmPcToPt("8inch"), 0);
		Assert.assertEquals(576, css.parsePxInCmMmPcToPt("8", CSS.Value.IN), 0);
	}

	@Test
	public void splitFont() {
		Map<String, String> processFont = css.processFont("bold italic 16pt/3px Verdana");
		Assert.assertEquals("bold", processFont.get("font-weight"));
		Assert.assertEquals("italic", processFont.get("font-style"));
		Assert.assertEquals("16pt", processFont.get("font-size"));
		Assert.assertEquals("3px", processFont.get("line-height"));
		Assert.assertEquals("Verdana", processFont.get("font-family"));
	}

	@Test
	public void splitBackgroundOne() {
		Map<String, String> background = css.processBackground("#00ff00 url('smiley.gif') no-repeat fixed center top");
		Assert.assertEquals("#00ff00", background.get("background-color"));
		Assert.assertEquals("url('smiley.gif')", background.get("background-image"));
		Assert.assertEquals("no-repeat", background.get("background-repeat"));
		Assert.assertEquals("fixed", background.get("background-attachment"));
		Assert.assertEquals("top center", background.get("background-position"));
	}

	@Test
	public void splitBackgroundTwo() {
		Map<String, String> background = css
				.processBackground("rgdbq(150, 90, 60) url'smiley.gif') repeat-x scroll 20 60%");
		Assert.assertEquals(null, background.get("background-color"));
		Assert.assertEquals(null, background.get("background-image"));
		Assert.assertEquals("repeat-x", background.get("background-repeat"));
		Assert.assertEquals("scroll", background.get("background-attachment"));
		Assert.assertEquals("60% 20", background.get("background-position"));
	}

	@Test
	public void splitBackgroundThree() {
		Map<String, String> background = css.processBackground("DarkOliveGreen fixed center");
		Assert.assertEquals("DarkOliveGreen", background.get("background-color"));
		Assert.assertEquals(null, background.get("background-image"));
		Assert.assertEquals(null, background.get("background-repeat"));
		Assert.assertEquals("fixed", background.get("background-attachment"));
		Assert.assertEquals("center", background.get("background-position"));
	}

	@Test
	public void replaceDoubleSpaces() {
		String stripDoubleSpacesAndTrim = css.stripDoubleSpacesAndTrim(string);
		Assert.assertTrue("double spaces [  ] detected", !(stripDoubleSpacesAndTrim.contains("  ")));
	}

	@Test
	public void parse1BoxValuesTest() {
		String box = "2px";
		Map<String, String> values = css.parseBoxValues(box, "pre-", "-post");
		validateKeys(values);
		Assert.assertEquals(box, values.get("pre-right-post"));
		Assert.assertEquals(box, values.get("pre-left-post"));
		Assert.assertEquals(box, values.get("pre-bottom-post"));
		Assert.assertEquals(box, values.get("pre-top-post"));
	}

	/**
	 * @param values
	 */
	private void validateKeys(final Map<String, String> values) {
		Assert.assertTrue("key not found top", values.containsKey("pre-top-post"));
		Assert.assertTrue("key not found bottom", values.containsKey("pre-bottom-post"));
		Assert.assertTrue("key not found left", values.containsKey("pre-left-post"));
		Assert.assertTrue("key not found right", values.containsKey("pre-right-post"));
	}

	@Test
	public void parse2BoxValuesTest() {
		String box = "2px 5px";
		Map<String, String> values = css.parseBoxValues(box, "pre-", "-post");
		validateKeys(values);
		Assert.assertEquals("5px", values.get("pre-right-post"));
		Assert.assertEquals("5px", values.get("pre-left-post"));
		Assert.assertEquals("2px", values.get("pre-bottom-post"));
		Assert.assertEquals("2px", values.get("pre-top-post"));
	}

	@Test
	public void parse3BoxValuesTest() {
		String box = "2px 3px 4px";
		Map<String, String> values = css.parseBoxValues(box, "pre-", "-post");
		validateKeys(values);
		Assert.assertEquals("3px", values.get("pre-right-post"));
		Assert.assertEquals("3px", values.get("pre-left-post"));
		Assert.assertEquals("4px", values.get("pre-bottom-post"));
		Assert.assertEquals("2px", values.get("pre-top-post"));
	}

	@Test
	public void parse4BoxValuesTest() {
		String box = "2px 3px 4px 5px";
		Map<String, String> values = css.parseBoxValues(box, "pre-", "-post");
		validateKeys(values);
		Assert.assertEquals("3px", values.get("pre-right-post"));
		Assert.assertEquals("5px", values.get("pre-left-post"));
		Assert.assertEquals("4px", values.get("pre-bottom-post"));
		Assert.assertEquals("2px", values.get("pre-top-post"));
	}

	@Test
	public void parseBorder() {
		String border = "dashed";
		Map<String, String> map = css.parseBorder(border);
		Assert.assertTrue(map.containsKey("border-left-style"));
		Assert.assertEquals("dashed", map.get("border-left-style"));
		Assert.assertTrue(map.containsKey("border-top-style"));
		Assert.assertEquals("dashed", map.get("border-top-style"));
		Assert.assertTrue(map.containsKey("border-bottom-style"));
		Assert.assertEquals("dashed", map.get("border-bottom-style"));
		Assert.assertTrue(map.containsKey("border-right-style"));
		Assert.assertEquals("dashed", map.get("border-right-style"));
	}

	@Test
	public void parseBorder2() {
		String border = "dashed green";
		Map<String, String> map = css.parseBorder(border);
		Assert.assertTrue(map.containsKey("border-left-style"));
		Assert.assertEquals("dashed", map.get("border-left-style"));
		Assert.assertTrue(map.containsKey("border-top-style"));
		Assert.assertEquals("dashed", map.get("border-top-style"));
		Assert.assertTrue(map.containsKey("border-bottom-style"));
		Assert.assertEquals("dashed", map.get("border-bottom-style"));
		Assert.assertTrue(map.containsKey("border-right-style"));
		Assert.assertEquals("dashed", map.get("border-right-style"));
		Assert.assertTrue(map.containsKey("border-left-color"));
		Assert.assertEquals("green", map.get("border-left-color"));
		Assert.assertTrue(map.containsKey("border-top-color"));
		Assert.assertEquals("green", map.get("border-top-color"));
		Assert.assertTrue(map.containsKey("border-bottom-color"));
		Assert.assertEquals("green", map.get("border-bottom-color"));
		Assert.assertTrue(map.containsKey("border-right-color"));
		Assert.assertEquals("green", map.get("border-right-color"));
	}

	@Test
	public void parseBorder3() {
		String border = "1px dashed";
		Map<String, String> map = css.parseBorder(border);
		Assert.assertTrue(map.containsKey("border-left-style"));
		Assert.assertEquals("dashed", map.get("border-left-style"));
		Assert.assertTrue(map.containsKey("border-top-style"));
		Assert.assertEquals("dashed", map.get("border-top-style"));
		Assert.assertTrue(map.containsKey("border-bottom-style"));
		Assert.assertEquals("dashed", map.get("border-bottom-style"));
		Assert.assertTrue(map.containsKey("border-right-style"));
		Assert.assertEquals("dashed", map.get("border-right-style"));
		Assert.assertTrue(map.containsKey("border-left-width"));
		Assert.assertEquals("1px", map.get("border-left-width"));
		Assert.assertTrue(map.containsKey("border-top-width"));
		Assert.assertEquals("1px", map.get("border-top-width"));
		Assert.assertTrue(map.containsKey("border-bottom-width"));
		Assert.assertEquals("1px", map.get("border-bottom-width"));
		Assert.assertTrue(map.containsKey("border-right-width"));
		Assert.assertEquals("1px", map.get("border-right-width"));
	}

	@Test
	public void parseBorder4() {
		String border = "1px dashed green";
		Map<String, String> map = css.parseBorder(border);
		Assert.assertTrue(map.containsKey("border-left-style"));
		Assert.assertEquals("dashed", map.get("border-left-style"));
		Assert.assertTrue(map.containsKey("border-top-style"));
		Assert.assertEquals("dashed", map.get("border-top-style"));
		Assert.assertTrue(map.containsKey("border-bottom-style"));
		Assert.assertEquals("dashed", map.get("border-bottom-style"));
		Assert.assertTrue(map.containsKey("border-right-style"));
		Assert.assertEquals("dashed", map.get("border-right-style"));
		Assert.assertTrue(map.containsKey("border-left-color"));
		Assert.assertEquals("green", map.get("border-left-color"));
		Assert.assertTrue(map.containsKey("border-top-color"));
		Assert.assertEquals("green", map.get("border-top-color"));
		Assert.assertTrue(map.containsKey("border-bottom-color"));
		Assert.assertEquals("green", map.get("border-bottom-color"));
		Assert.assertTrue(map.containsKey("border-right-color"));
		Assert.assertEquals("green", map.get("border-right-color"));
		Assert.assertTrue(map.containsKey("border-left-width"));
		Assert.assertEquals("1px", map.get("border-left-width"));
		Assert.assertTrue(map.containsKey("border-top-width"));
		Assert.assertEquals("1px", map.get("border-top-width"));
		Assert.assertTrue(map.containsKey("border-bottom-width"));
		Assert.assertEquals("1px", map.get("border-bottom-width"));
		Assert.assertTrue(map.containsKey("border-right-width"));
		Assert.assertEquals("1px", map.get("border-right-width"));
		Assert.assertTrue(map.containsKey("border-left-width"));
		Assert.assertEquals("1px", map.get("border-left-width"));
		Assert.assertTrue(map.containsKey("border-top-width"));
		Assert.assertEquals("1px", map.get("border-top-width"));
		Assert.assertTrue(map.containsKey("border-bottom-width"));
		Assert.assertEquals("1px", map.get("border-bottom-width"));
		Assert.assertTrue(map.containsKey("border-right-width"));
		Assert.assertEquals("1px", map.get("border-right-width"));
	}

	@Test
	public void parseUrlSingleQuoted() {
		Assert.assertEquals("file.jpg", css.extractUrl("url( 'file.jpg')"));
	}

	@Test
	public void parseUrlDoubleQuoted() {
		Assert.assertEquals("file.jpg", css.extractUrl("url ( \"file.jpg\" )"));
	}

	@Test
	public void parseUnparsableUrl() {
		Assert.assertEquals("('file.jpg')", css.extractUrl("('file.jpg')"));
	}

}

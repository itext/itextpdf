/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.css;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CssInheritanceRules;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;

/**
 * @author redlab_b
 *
 */
public class DefaultCSSResolverTest {

	private StyleAttrCSSResolver css;
	private Tag parent;
	private Tag child;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		css = new StyleAttrCSSResolver();
		HashMap<String, String> pAttr = new HashMap<String, String>();
        pAttr.put("style", "fontk: Verdana; color: blue; test: a");
		parent = new Tag("parent", pAttr);
		HashMap<String, String> cAttr = new HashMap<String, String>();
        cAttr.put("style", "fontk: Arial; font-size: large; test: b");
		child = new Tag("child", cAttr);
		child.setParent(parent);
	}
	/**
	 * Resolves the style attribute to css for 1 tag.
	 */
	@Test
	public void resolveTagCss() {
		css.resolveStyles(parent);
		Map<String, String> css2 = parent.getCSS();
        assertTrue("font not found", css2.containsKey("fontk"));
		assertTrue("color not found", css2.containsKey("color"));
        assertEquals("Verdana", css2.get("fontk"));
		assertEquals("blue", css2.get("color"));
	}
	/**
	 * Resolves the style attribute to css for parent and child.
	 */
	@Test
	public void resolveChildTagCss() {
		css.resolveStyles(parent);
		css.resolveStyles(child);
		Map<String, String> css2 = child.getCSS();
        assertTrue("font not found", css2.containsKey("fontk"));
		assertTrue("color not found", css2.containsKey("color"));
		assertTrue("font-size not found", css2.containsKey("font-size"));
        assertEquals("Arial", css2.get("fontk"));
		assertEquals("blue", css2.get("color"));
		assertEquals("large", css2.get("font-size"));
	}

	/**
	 * checks CSSInheritance between parent and child on tag level
	 */
	@Test
	public void cssTagLevelInheritance() {
		css.setCssInheritance(new CssInheritanceRules() {

			public boolean inheritCssTag(final String tag) {
				return !"child".equals(tag);
			}

			public boolean inheritCssSelector(final Tag tag, final String key) {
				return true;
			}
		});
		css.resolveStyles(parent);
		css.resolveStyles(child);
		Map<String, String> css2 = child.getCSS();
        assertTrue("font not found", css2.containsKey("fontk"));
		assertTrue("font-size not found", css2.containsKey("font-size"));
		assertFalse("color found while not expected", css2.containsKey("color"));
        assertEquals("Arial", css2.get("fontk"));
		assertEquals("large", css2.get("font-size"));
	}
	/**
	 * checks CSSInheritance between parent and child on css element level
	 */
	@Test
	public void cssTagStyleLevelInheritance() {
		css.setCssInheritance(new CssInheritanceRules() {

			public boolean inheritCssTag(final String tag) {
				return true;
			}

			public boolean inheritCssSelector(final Tag tag, final String key) {
				return "child".equals(tag.getName()) && "color".equals(key);
			}
		});
		css.resolveStyles(parent);
		css.resolveStyles(child);
		Map<String, String> css2 = child.getCSS();
        assertTrue("font not found", css2.containsKey("fontk"));
		assertTrue("font-size not found", css2.containsKey("font-size"));
		assertTrue("color not found", css2.containsKey("color"));
        assertEquals("Arial", css2.get("fontk"));
		assertEquals("large", css2.get("font-size"));
		assertEquals("blue", css2.get("color"));
	}
}

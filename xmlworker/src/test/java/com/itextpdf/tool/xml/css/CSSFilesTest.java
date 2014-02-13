/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.exceptions.CssResolverException;

/**
 * @author redlab_b
 *
 */
public class CSSFilesTest {


	private CssFilesImpl files;
	private Tag t;

	@Before
	public void setup() throws CssResolverException {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		files = new CssFilesImpl();
		final StyleAttrCSSResolver resolver = new StyleAttrCSSResolver(files);
		final URL u = CSSFilesTest.class.getResource("/css/style.css");
		resolver.addCssFile(u.getPath().replace("%20", " "), false); // fix url conversion of space (%20) for File
		final Map<String, String> attr = new HashMap<String, String>();
		t = new Tag("body", attr);
	}
	@Test
	public void getStyle() throws  CssResolverException {
		final Map<String, String> css = files.getCSS(t);
		Assert.assertTrue(css.containsKey("font-size"));
		Assert.assertTrue(css.containsKey("color"));
	}

	@Test
	public void clear() {
		files.clear();
		Assert.assertFalse("files detected", files.hasFiles());
	}
	@Test
	public void clearWithPersistent() {
		CssFileImpl css = new CssFileImpl();
		css.isPersistent(true);
		files.add(css);
		files.clear();
		Assert.assertTrue("no files detected", files.hasFiles());
	}
}

/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.tool.xml.html;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.DefaultProvider;
import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerConfigurationImpl;
import com.itextpdf.tool.xml.XMLWorkerImpl;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.parser.XMLParser;

/**
 * @author Balder Van Camp
 *
 */
public class LoadCssThroughLinkStyleTagTest {

	private static final String HTML1 = "<html><head><link type='text/css' rel='stylesheet' href='style.css'/></head><body><p>Import css files test</p></body></html>";
	private static final String HTML2 = "<html><head><link type='text/css' rel='stylesheet' href='style.css'/><link type='text/css' rel='stylesheet' href='test.css'/></head><body><p>Import css files test</p></body></html>";
	private static final String HTML3 = "<html><head><link type='text/css' rel='stylesheet' href='style.css'/><link type='text/css' rel='stylesheet' href='test.css'/><style type='text/css'>body {padding: 5px;}</style></head><body><p>Import css files test</p></body></html>";
	private XMLParser p;
	private CssFilesImpl cssFiles;

	@Before
	public void setup() {

		XMLWorkerConfigurationImpl config = new XMLWorkerConfigurationImpl();
		cssFiles = new CssFilesImpl();
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		DefaultProvider provider = new DefaultProvider();
		String path = LoadCssThroughLinkStyleTagTest.class.getResource("/css/test.css").getPath();
		provider.setGlobalCssRoot(path.substring(0, path.lastIndexOf("test.css")));
		config.tagProcessorFactory(new Tags().getHtmlTagProcessorFactory()).cssResolver(cssResolver)
		.acceptUnknown(false).provider(provider);
		final XMLWorker worker = new XMLWorkerImpl(config);
		worker.setDocumentListener(new ElementHandler() {

			public void addAll(final List<Element> currentContent) throws DocumentException {

			}

			public void add(final Element e) throws DocumentException {

			}
		});
		p = new XMLParser(worker);
	}

	@Test
	public void parse1CssFileAndValidate() throws IOException {
		p.parse(new StringReader(HTML1));
		Map<String, String> props = new HashMap<String, String>();
		cssFiles.populateCss(props, "body");
		Assert.assertTrue(props.containsKey("font-size"));
		Assert.assertTrue(props.containsKey("color"));
	}
	@Test
	public void parse2CsszFileAndValidate() throws IOException {
		p.parse(new StringReader(HTML2));
		Map<String, String> props = new HashMap<String, String>();
		cssFiles.populateCss(props, "body");
		Assert.assertTrue(props.containsKey("font-size"));
		Assert.assertTrue(props.containsKey("color"));
		Assert.assertTrue(props.containsKey("margin-left"));
		Assert.assertTrue(props.containsKey("margin-right"));
		Assert.assertTrue(props.containsKey("margin-top"));
		Assert.assertTrue(props.containsKey("margin-bottom"));
	}
	@Test
	public void parse2CsszFilePluseStyleTagAndValidate() throws IOException {
		p.parse(new StringReader(HTML3));
		Map<String, String> props = new HashMap<String, String>();
		cssFiles.populateCss(props, "body");
		Assert.assertTrue(props.containsKey("font-size"));
		Assert.assertTrue(props.containsKey("color"));
		Assert.assertTrue(props.containsKey("margin-left"));
		Assert.assertTrue(props.containsKey("margin-right"));
		Assert.assertTrue(props.containsKey("margin-top"));
		Assert.assertTrue(props.containsKey("margin-bottom"));
		Assert.assertTrue(props.containsKey("padding-left"));
		Assert.assertTrue(props.containsKey("padding-right"));
		Assert.assertTrue(props.containsKey("padding-top"));
		Assert.assertTrue(props.containsKey("padding-bottom"));
	}
}

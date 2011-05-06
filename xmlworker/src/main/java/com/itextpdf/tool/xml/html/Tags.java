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

import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.DefaultTagProcessorFactory;
import com.itextpdf.tool.xml.TagProcessorFactory;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;

/**
 * @author redlab_b
 *
 */
public class Tags {

	private static String defaultpackage = "com.itextpdf.tool.xml.html.";
	private static String dummyTagProcessor = defaultpackage + "DummyTagProcessor";
	private static String headers = defaultpackage + "Header";
	private static String span = defaultpackage + "Span";
	private static String nonSanitized = defaultpackage + "NonSanitizedTag";
	private static String paragraph = defaultpackage + "ParaGraph";

	/**
	 * @return a default XHTML {@link TagProcessorFactory}
	 */
	public final TagProcessorFactory getHtmlTagProcessorFactory() {
		DefaultTagProcessorFactory factory = new DefaultTagProcessorFactory();
		factory.addProcessor("xml", defaultpackage+"head.XML");
		factory.addProcessor("!doctype", dummyTagProcessor);
		factory.addProcessor("html", dummyTagProcessor);
		factory.addProcessor("head", dummyTagProcessor);
		factory.addProcessor("meta", defaultpackage+"head.Meta");
		factory.addProcessor("object", dummyTagProcessor);
		factory.addProcessor("title", defaultpackage + "head.Title");
		factory.addProcessor("link", defaultpackage + "head.Link");
		factory.addProcessor("style", defaultpackage + "head.Style");
		factory.addProcessor("body", defaultpackage + "Body");
		factory.addProcessor("div", defaultpackage + "Div");
		factory.addProcessor("a", defaultpackage + "Anchor");
		factory.addProcessor("table", defaultpackage + "table.Table");
		factory.addProcessor("tr", defaultpackage + "table.TableRow");
		factory.addProcessor("td", defaultpackage + "table.TableData");
		factory.addProcessor("th", defaultpackage + "table.TableData");
		factory.addProcessor("caption", paragraph);
		factory.addProcessor("p", paragraph);
		factory.addProcessor("dt", paragraph);
		factory.addProcessor("dd", paragraph);
		factory.addProcessor("br", defaultpackage + "Break");
		factory.addProcessor("span", span);
		factory.addProcessor("small", span);
		factory.addProcessor("big", span);
		factory.addProcessor("s", span);
		factory.addProcessor("strike", span);
		factory.addProcessor("del", span);
		factory.addProcessor("sub", span);
		factory.addProcessor("sup", span);
		factory.addProcessor("b", span);
		factory.addProcessor("strong", span);
		factory.addProcessor("i", span);
		factory.addProcessor("cite", span);
		factory.addProcessor("em", span);
		factory.addProcessor("address", span);
		factory.addProcessor("dfn", span);
		factory.addProcessor("var", span);
		factory.addProcessor("pre", nonSanitized);
		factory.addProcessor("tt", nonSanitized);
		factory.addProcessor("code", nonSanitized);
		factory.addProcessor("kbd", nonSanitized);
		factory.addProcessor("samp", nonSanitized);
		factory.addProcessor("u", span);
		factory.addProcessor("ins", span);
		factory.addProcessor("img", defaultpackage + "Image");
		factory.addProcessor("ul", defaultpackage + "OrderedUnorderedList");
		factory.addProcessor("ol", defaultpackage + "OrderedUnorderedList");
		factory.addProcessor("li", defaultpackage + "OrderedUnorderedListItem");
		factory.addProcessor("h1", headers);
		factory.addProcessor("h2", headers);
		factory.addProcessor("h3", headers);
		factory.addProcessor("h4", headers);
		factory.addProcessor("h5", headers);
		factory.addProcessor("h6", headers);
		factory.addProcessor("hr", defaultpackage + "HorizontalRule");
		return factory;
	}

	/**
	 * Adds currentContent list to a paragraph element. If addNewLines is true a Paragraph object is returned, else a
	 * NoNewLineParagraph object is returned.
	 *
	 * @param currentContent List<Element> of the current elements to be added.
	 * @param addNewLines boolean to declare which paragraph element should be returned, true if new line should be added or not.
	 * @return Element either a Paragraph object or a NoNewLineParagraph object. Or null if current content was empty
	 */
	public final static Element currentContentToParagraph(final List<Element> currentContent, final boolean addNewLines) {
		if (currentContent.size() > 0) {
			if (addNewLines) {
				Paragraph p = new Paragraph();
				for (Element e : currentContent) {
					p.add(e);
				}
				return p;
			} else {
				NoNewLineParagraph p = new NoNewLineParagraph();
				for (Element e : currentContent) {
					p.add(e);
				}
				return p;
			}
		}
		throw new IllegalArgumentException("currentContent cannot be null");
	}
}

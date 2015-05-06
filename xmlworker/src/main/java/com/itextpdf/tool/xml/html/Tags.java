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
package com.itextpdf.tool.xml.html;

import com.itextpdf.tool.xml.html.HTML.Tag;



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
	 * Returns a new {@link DefaultTagProcessorFactory}
	 * @return a default XHTML {@link TagProcessorFactory}
	 */
	public static final TagProcessorFactory getHtmlTagProcessorFactory() {
		DefaultTagProcessorFactory factory = new DefaultTagProcessorFactory();
		factory.addProcessor(Tag.XML, dummyTagProcessor);
		factory.addProcessor("!doctype", dummyTagProcessor);
		factory.addProcessor(Tag.HTML, dummyTagProcessor);
		factory.addProcessor(Tag.HEAD, dummyTagProcessor);
		factory.addProcessor(Tag.META, dummyTagProcessor);
		factory.addProcessor(Tag.OBJECT, dummyTagProcessor);
		factory.addProcessor(Tag.TITLE, defaultpackage + "head.Title");
		factory.addProcessor(Tag.LINK, defaultpackage + "head.Link");
		factory.addProcessor(Tag.STYLE, defaultpackage + "head.Style");
		factory.addProcessor(Tag.BODY, defaultpackage + "Body");
		factory.addProcessor(Tag.DIV, defaultpackage + "Div");
		factory.addProcessor(Tag.A, defaultpackage + "Anchor");
		factory.addProcessor(Tag.TABLE, defaultpackage + "table.Table");
		factory.addProcessor(Tag.TR, defaultpackage + "table.TableRow");
		factory.addProcessor(Tag.TD, defaultpackage + "table.TableData");
		factory.addProcessor(Tag.TH, defaultpackage + "table.TableData");
		factory.addProcessor(Tag.CAPTION, paragraph);
		factory.addProcessor(Tag.P, paragraph);
		factory.addProcessor(Tag.DT, paragraph);
		factory.addProcessor(Tag.DD, paragraph);
        factory.addProcessor(Tag.BLOCKQUOTE, paragraph);
		factory.addProcessor(Tag.BR, defaultpackage + "Break");
		factory.addProcessor(Tag.SPAN, span);
		factory.addProcessor(Tag.SMALL, span);
		factory.addProcessor(Tag.BIG, span);
		factory.addProcessor(Tag.S, span);
		factory.addProcessor(Tag.STRIKE, span);
		factory.addProcessor(Tag.DEL, span);
		factory.addProcessor(Tag.SUB, span);
		factory.addProcessor(Tag.SUP, span);
		factory.addProcessor(Tag.B, span);
		factory.addProcessor(Tag.STRONG, span);
        factory.addProcessor(Tag.FONT, span);
		factory.addProcessor(Tag.I, span);
		factory.addProcessor(Tag.CITE, span);
		factory.addProcessor(Tag.EM, span);
		factory.addProcessor(Tag.ADDRESS, span);
		factory.addProcessor(Tag.DFN, span);
		factory.addProcessor(Tag.VAR, span);
		factory.addProcessor(Tag.PRE, nonSanitized);
		factory.addProcessor(Tag.TT, nonSanitized);
		factory.addProcessor(Tag.CODE, nonSanitized);
		factory.addProcessor(Tag.KBD, nonSanitized);
		factory.addProcessor(Tag.SAMP, nonSanitized);
		factory.addProcessor(Tag.U, span);
		factory.addProcessor(Tag.INS, span);
		factory.addProcessor(Tag.IMG, defaultpackage + "Image");
		factory.addProcessor(Tag.UL, defaultpackage + "OrderedUnorderedList");
		factory.addProcessor(Tag.OL, defaultpackage + "OrderedUnorderedList");
		factory.addProcessor(Tag.LI, defaultpackage + "OrderedUnorderedListItem");
		factory.addProcessor(Tag.H1, headers);
		factory.addProcessor(Tag.H2, headers);
		factory.addProcessor(Tag.H3, headers);
		factory.addProcessor(Tag.H4, headers);
		factory.addProcessor(Tag.H5, headers);
		factory.addProcessor(Tag.H6, headers);
		factory.addProcessor(Tag.HR, defaultpackage + "HorizontalRule");
		//added by Jeroen Nouws
		factory.addProcessor(Tag.LABEL, span);
		return factory;
	}
}

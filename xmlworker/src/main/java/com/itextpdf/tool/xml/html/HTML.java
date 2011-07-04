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

/**
 * Contains Strings of all used HTML tags and attributes.
 * @author redlab_b
 *
 */
public final class HTML {

	/**
	 *
	 */
	private HTML() {
	}
	/**
	 *
	 * All Tags.
	 *
	 */
	public final static class Tag {
		/**
		 *
		 */
		private Tag() {
		}

		public static final String THEAD = "thead";
		public static final String TBODY = "thead";
		public static final String TFOOT = "tfoot";
		public static final String OL = "ol";
		public static final String UL = "ul";
		public static final String CAPTION = "caption";
		public static final String PRE = "pre";
		public static final String P = "p";
		public static final String DIV = "div";
		public static final String H1 = "h1";
		public static final String H2 = "h2";
		public static final String H3 = "h3";
		public static final String H4 = "h4";
		public static final String H5 = "h5";
		public static final String H6 = "h6";
		public static final String TD = "td";
		public static final String BR = "br";
		public static final String LI = "li";
		public static final String DD = "dd";
		public static final String DT = "dt";
		public static final String TH = "th";
		public static final String HR = "hr";
		public static final String BODY = "body";
		public static final String HTML = "html";
		public static final String TABLE = "table";
		public static final String SCRIPT = "script";
		public static final String HEAD = "head";
		public static final String LINK = "link";
		public static final String META = "meta";

	}
	/**
	 * All attributes
	 */
	public final static class Attribute {

		/**
		 *
		 */
		private Attribute() {
		}

		public static final String CELLPADDING = "cellpadding";
		public static final String CELLSPACING = "cellspacing";
		public static final String STYLE = "style";
		public static final String CLASS = "class";
		public static final String ID = "id";
		public static final String HREF = "href";
		public static final String NAME = "name";
		public static final String SRC = "src";
		public static final String WIDTH = "width";
		public static final String HEIGHT = "height";
		public static final String TYPE = "type";
		public static final String COLSPAN = "colspan";
		public static final String ROWSPAN = "rowspan";

	}
}

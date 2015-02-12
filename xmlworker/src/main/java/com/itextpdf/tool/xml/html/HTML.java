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
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.tool.xml.html;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Contains Strings of all used HTML tags and attributes.
 *
 * @author redlab_b
 *
 */
@SuppressWarnings("javadoc")
public final class HTML {

	/**
	 *
	 */
	private HTML() {
	}

	/**
	 *
	 * All Tags used in HTML.
	 *
	 */
	public final static class Tag {

		private Tag() {
		}

		public static final String XML = "xml";
		public static final String THEAD = "thead";
		public static final String TBODY = "tbody";
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
		public static final String STYLE = "style";
		public static final String ADDRESS = "address";
		public static final String ARTICLE = "article";
		public static final String ASIDE = "aside";
		public static final String AUDIO = "audio";
		public static final String BLOCKQUOTE = "blockquote";
		public static final String CANVAS = "canvas";
		public static final String FIELDSET = "fieldset";
		public static final String FIGCAPTION = "figcaption";
		public static final String FIGURE = "figure";
		public static final String FOOTER = "footer";
		public static final String FONT = "font";
		public static final String FORM = "form";
		public static final String HEADER = "header";
		public static final String HGROUP = "hgroup";
		public static final String NOSCRIPT = "noscript";
		public static final String OUTPUT = "output";
		public static final String SECTION = "section";
		public static final String VIDEO = "video";
		public static final String BASE = "base";
		public static final String COMMAND = "command";
		public static final String TITLE = "title";
		public static final String A = "a";
		public static final String ABBR = "abbr";
		public static final String B = "b";
		public static final String BDO = "bdo";
		public static final String BUTTON = "button";
		public static final String DETAILS = "details";
		public static final String CODE = "code";
		public static final String DEL = "del";
		public static final String DATALIST = "datalist";
		public static final String DFN = "dfn";
		public static final String EMBED = "embed";
		public static final String CITE = "cite";
		public static final String DL = "dl";
		public static final String EM = "em";
		public static final String I = "i";
		public static final String IFRAME = "iframe";
		public static final String INPUT = "input";
		public static final String IMG = "img";
		public static final String INS = "ins";
		public static final String MAP = "map";
		public static final String KEYGEN = "keygen";
		public static final String METER = "meter";
		public static final String MENU = "menu";
		public static final String NAV = "nav";
		public static final String KBD = "kbd";
		public static final String MATH = "math";
		public static final String MARK = "mark";
		public static final String LABEL = "label";
		public static final String Q = "q";
		public static final String SAMP = "samp";
		public static final String PROGRESS = "progress";
		public static final String RUBY = "ruby";
		public static final String OBJECT = "object";
		public static final String SMALL = "small";
		public static final String SUB = "sub";
		public static final String SUP = "sup";
		public static final String STRONG = "strong";
		public static final String SELECT = "select";
		public static final String SPAN = "span";
		public static final String SVG = "svg";
		public static final String WBR = "wbr";
		public static final String TIME = "time";
		public static final String TEXTAREA = "textarea";
		public static final String VAR = "var";
		public static final String TR = "tr";
		public static final String BIG = "big";
		public static final String S = "s";
		public static final String STRIKE = "strike";
		public static final String TT = "tt";
		public static final String U = "u";

	}

	/**
	 * Collections that combine different tags.
	 *
	 */
	public static final class Category {

		private Category() {
		}

		/**
		 * List with the tags that are not visible in the browser.<br />
		 * Tag.HTML, Tag.HEAD, Tag.META, Tag.SCRIPT, Tag.LINK, Tag.STYLE,
		 * Tag.TITLE
		 */
		public static final Set<String> NOT_VISIBLE = new HashSet<String>(Arrays.asList(new String[] { Tag.HTML,
				Tag.HEAD, Tag.META, Tag.SCRIPT, Tag.LINK, Tag.STYLE, Tag.TITLE }));
		/**
		 * Groups all tags that are block level tags by default.<br />
		 * Tag.ADDRESS, Tag.ARTICLE, Tag.ASIDE, Tag.AUDIO, Tag.BLOCKQUOTE,
		 * Tag.CANVAS, Tag.DD, Tag.DIV, Tag.FIELDSET, Tag.FIGCAPTION,
		 * Tag.FIGURE, Tag.FOOTER, Tag.FORM, Tag.H1, Tag.H2, Tag.H3, Tag.H4,
		 * Tag.H5, Tag.H6, Tag.HEADER, Tag.HGROUP, Tag.HR, Tag.NOSCRIPT, Tag.OL,
		 * Tag.OUTPUT, Tag.P, Tag.PRE, Tag.SECTION, Tag.TABLE, Tag.UL, Tag.VIDEO
		 */
		public static final Set<String> BLOCK_LEVEL = new HashSet<String>(Arrays.asList(new String[] { Tag.ADDRESS,
				Tag.ARTICLE, Tag.ASIDE, Tag.AUDIO, Tag.BLOCKQUOTE, Tag.CANVAS, Tag.DD, Tag.DIV, Tag.FIELDSET,
				Tag.FIGCAPTION, Tag.FIGURE, Tag.FOOTER, Tag.FORM, Tag.H1, Tag.H2, Tag.H3, Tag.H4, Tag.H5, Tag.H6,
				Tag.HEADER, Tag.HGROUP, Tag.HR, Tag.NOSCRIPT, Tag.OL, Tag.OUTPUT, Tag.P, Tag.PRE, Tag.SECTION,
				Tag.TABLE, Tag.UL, Tag.VIDEO }));

		/**
		 * HTML5<br />
		 * Elements belonging to the <em>metadata content</em> category modify
		 * the presentation or the behavior of the rest of the document, set up
		 * links to others documents, or convey other <em>out of band</em>
		 * information.<br />
		 * Tag.BASE, Tag.COMMAND, Tag.LINK, Tag.META, Tag.NOSCRIPT, Tag.STYLE,
		 * Tag.TITLE
		 */
		public static final Set<String> METADATA = new HashSet<String>(Arrays.asList(new String[] { Tag.BASE,
				Tag.COMMAND, Tag.LINK, Tag.META, Tag.NOSCRIPT, Tag.STYLE, Tag.TITLE }));

		/**
		 * HTML5<br />
		 * Elements belonging to the flow content category typically contain
		 * text or embedded content.<br />
		 * Tag.A, Tag.ABBR, Tag.ADDRESS, Tag.ARTICLE, Tag.ASIDE, Tag.AUDIO,
		 * Tag.B, Tag.BDO, Tag.BLOCKQUOTE, Tag.BR, Tag.BUTTON, Tag.CANVAS,
		 * Tag.CITE, Tag.CODE, Tag.COMMAND, Tag.DATALIST, Tag.DEL, Tag.DETAILS,
		 * Tag.DFN, Tag.DIV, Tag.DL, Tag.EM, Tag.EMBED, Tag.FIELDSET,
		 * Tag.FIGURE, Tag.FOOTER, Tag.FORM, Tag.H1, Tag.H2, Tag.H3, Tag.H4,
		 * Tag.H5, Tag.H6, Tag.HEADER, Tag.HGROUP, Tag.HR, Tag.I, Tag.IFRAME,
		 * Tag.IMG, Tag.INPUT, Tag.INS, Tag.KBD, Tag.KEYGEN, Tag.LABEL, Tag.MAP,
		 * Tag.MARK, Tag.MATH, Tag.MENU, Tag.METER, Tag.NAV, Tag.NOSCRIPT,
		 * Tag.OBJECT, Tag.OL, Tag.OUTPUT, Tag.P, Tag.PRE, Tag.PROGRESS, Tag.Q,
		 * Tag.RUBY, Tag.SAMP, Tag.SCRIPT, Tag.SECTION, Tag.SELECT, Tag.SMALL,
		 * Tag.SPAN, Tag.STRONG, Tag.SUB, Tag.SUP, Tag.SVG, Tag.TABLE,
		 * Tag.TEXTAREA, Tag.TIME, Tag.UL, Tag.VAR, Tag.VIDEO, Tag.WBR
		 */
		public static final Set<String> FLOW_CONTENT = new HashSet<String>(Arrays.asList(new String[] { Tag.A,
				Tag.ABBR, Tag.ADDRESS, Tag.ARTICLE, Tag.ASIDE, Tag.AUDIO, Tag.B, Tag.BDO, Tag.BLOCKQUOTE, Tag.BR,
				Tag.BUTTON, Tag.CANVAS, Tag.CITE, Tag.CODE, Tag.COMMAND, Tag.DATALIST, Tag.DEL, Tag.DETAILS, Tag.DFN,
				Tag.DIV, Tag.DL, Tag.EM, Tag.EMBED, Tag.FIELDSET, Tag.FIGURE, Tag.FOOTER, Tag.FORM, Tag.H1, Tag.H2,
				Tag.H3, Tag.H4, Tag.H5, Tag.H6, Tag.HEADER, Tag.HGROUP, Tag.HR, Tag.I, Tag.IFRAME, Tag.IMG, Tag.INPUT,
				Tag.INS, Tag.KBD, Tag.KEYGEN, Tag.LABEL, Tag.MAP, Tag.MARK, Tag.MATH, Tag.MENU, Tag.METER, Tag.NAV,
				Tag.NOSCRIPT, Tag.OBJECT, Tag.OL, Tag.OUTPUT, Tag.P, Tag.PRE, Tag.PROGRESS, Tag.Q, Tag.RUBY, Tag.SAMP,
				Tag.SCRIPT, Tag.SECTION, Tag.SELECT, Tag.SMALL, Tag.SPAN, Tag.FONT, Tag.STRONG, Tag.SUB, Tag.SUP, Tag.SVG,
				Tag.TABLE, Tag.TEXTAREA, Tag.TIME, Tag.UL, Tag.VAR, Tag.VIDEO, Tag.WBR }));
		/**
		 * HTML5<br />
		 * Elements belonging to the sectioning content model create a <a
		 * rel="internal" href=
		 * "https://developer.mozilla.org/en/Sections_and_Outlines_of_an_HTML5_document"
		 * >section in the current outline</a> that defines the scope of&nbsp;
		 * <code><a rel="custom" href="https://developer.mozilla.org/en/HTML/Element/header">&lt;header&gt;</a></code>
		 * elements,
		 * <code><a rel="custom" href="https://developer.mozilla.org/en/HTML/Element/footer">&lt;footer&gt;</a></code>
		 * elements, and <a rel="internal"
		 * href="https://developer.mozilla.org/#heading_content">heading
		 * content</a>. <footer> elements, and heading content.<br />
		 * Tag.ARTICLE, Tag.ASIDE, Tag.NAV, Tag.SECTION
		 */
		public static final Set<String> SECTIONING_CONTENT = new HashSet<String>(Arrays.asList(new String[] {
				Tag.ARTICLE, Tag.ASIDE, Tag.NAV, Tag.SECTION }));
		/**
		 * HTML5<br />
		 * Heading content defines the title of a section, whether marked by an
		 * explicit <a
		 * href="https://developer.mozilla.org/#sectioning_content">sectioning
		 * content</a> element or implicitly defined by the heading content
		 * itself.<br />
		 * Tag.H1, Tag.H2, Tag.H3, Tag.H4, Tag.H5, Tag.H6, Tag.HGROUP
		 */
		public static final Set<String> HEADING_CONTENT = new HashSet<String>(Arrays.asList(new String[] {
				Tag.H1, Tag.H2, Tag.H3, Tag.H4, Tag.H5, Tag.H6, Tag.HGROUP
		}));
		/**
		 * HTML5<br />
		 * Phrasing content defines the text and the mark-up it contains. Runs
		 * of phrasing content make up paragraphs.<br />
		 * Tag.ABBR, Tag.AUDIO, Tag.B, Tag.BDO, Tag.BR, Tag.BUTTON, Tag.CANVAS,
		 * Tag.CITE, Tag.CODE, Tag.COMMAND, Tag.DATALIST, Tag.DFN, Tag.EM,
		 * Tag.EMBED, Tag.I, Tag.IFRAME, Tag.IMG, Tag.INPUT, Tag.KBD,
		 * Tag.KEYGEN, Tag.LABEL, Tag.MARK, Tag.MATH, Tag.METER, Tag.NOSCRIPT,
		 * Tag.OBJECT, Tag.OUTPUT, Tag.PROGRESS, Tag.Q, Tag.RUBY, Tag.SAMP,
		 * Tag.SCRIPT, Tag.SELECT, Tag.SMALL, Tag.SPAN, Tag.STRONG, Tag.SUB,
		 * Tag.SUP, Tag.SVG, Tag.TEXTAREA, Tag.TIME, Tag.VAR, Tag.VIDEO, Tag.WBR
		 */
		public static final Set<String> PHRASING_CONTENT = new HashSet<String>(Arrays.asList(new String[] {
				Tag.ABBR, Tag.AUDIO, Tag.B, Tag.BDO, Tag.BR, Tag.BUTTON, Tag.CANVAS, Tag.CITE, Tag.CODE, Tag.COMMAND,
				Tag.DATALIST, Tag.DFN, Tag.EM, Tag.EMBED, Tag.I, Tag.IFRAME, Tag.IMG, Tag.INPUT, Tag.KBD, Tag.KEYGEN,
				Tag.LABEL, Tag.MARK, Tag.MATH, Tag.METER, Tag.NOSCRIPT, Tag.OBJECT, Tag.OUTPUT, Tag.PROGRESS, Tag.Q,
				Tag.RUBY, Tag.SAMP, Tag.SCRIPT, Tag.SELECT, Tag.SMALL, Tag.SPAN, Tag.FONT, Tag.STRONG, Tag.SUB, Tag.SUP, Tag.SVG,
				Tag.TEXTAREA, Tag.TIME, Tag.VAR, Tag.VIDEO, Tag.WBR
		}));
		/**
		 * HTML5<br />
		 * Embedded content imports another resource or inserts content from
		 * another mark-up language or namespace into the document.<br />
		 * Tag.AUDIO, Tag.CANVAS, Tag.EMBED, Tag.IFRAME, Tag.IMG, Tag.MATH,
		 * Tag.OBJECT, Tag.SVG, Tag.VIDEO
		 */
		public static final Set<String> EMBEDDED_CONTENT = new HashSet<String>(Arrays.asList(new String[] {
				Tag.AUDIO, Tag.CANVAS, Tag.EMBED, Tag.IFRAME, Tag.IMG, Tag.MATH, Tag.OBJECT, Tag.SVG, Tag.VIDEO
		}));
	}

	/**
	 * Attributes used in HTML tags.
	 */
	public final static class Attribute {

		/**
		 *
		 */
		private Attribute() {
		}

        public static final String ALT = "alt";
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
        public static final String VALIGN  = "valign";
        public static final String ALIGN  = "align";
        public static final String FACE  = "face";
        public static final String SIZE  = "size";
        public static final String COLOR  = "color";
        public static final String START  = "start";
                public static final String DIR = "dir";

		/**
		 * Possible attribute values.
		 *
		 * @author itextpdf.com
		 *
		 */
		public static final class Value {
			public static final String TEXTCSS = "text/css";

			private Value() {
			};

		}

	}
}

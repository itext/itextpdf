/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text.html;

import java.util.HashSet;
import java.util.Set;

/**
 * A class that contains all the possible tagnames and their attributes.
 */

public class HtmlTags {

	/** the root tag. */
	public static final String HTML = "html";

	/** the head tag */
	public static final String HEAD = "head";

	/** This is a possible HTML attribute for the HEAD tag. */
	public static final String CONTENT = "content";

	/** the meta tag */
	public static final String META = "meta";

	/** attribute of the root tag */
	public static final String SUBJECT = "subject";

	/** attribute of the root tag */
	public static final String KEYWORDS = "keywords";

	/** attribute of the root tag */
	public static final String AUTHOR = "author";

	/** the title tag. */
	public static final String TITLE = "title";

	/** the script tag. */
	public static final String SCRIPT = "script";

	/** This is a possible HTML attribute for the SCRIPT tag. */
	public static final String LANGUAGE = "language";

	/** This is a possible value for the LANGUAGE attribute. */
	public static final String JAVASCRIPT = "JavaScript";

	/** the body tag. */
	public static final String BODY = "body";

	/** This is a possible HTML attribute for the BODY tag */
	public static final String JAVASCRIPT_ONLOAD = "onLoad";

	/** This is a possible HTML attribute for the BODY tag */
	public static final String JAVASCRIPT_ONUNLOAD = "onUnLoad";

	/** This is a possible HTML attribute for the BODY tag. */
	public static final String TOPMARGIN = "topmargin";

	/** This is a possible HTML attribute for the BODY tag. */
	public static final String BOTTOMMARGIN = "bottommargin";

	/** This is a possible HTML attribute for the BODY tag. */
	public static final String LEFTMARGIN = "leftmargin";

	/** This is a possible HTML attribute for the BODY tag. */
	public static final String RIGHTMARGIN = "rightmargin";

	// Phrases, Anchors, Lists and Paragraphs

	/** the chunk tag */
	public static final String CHUNK = "font";

	/** the phrase tag */
	public static final String CODE = "code";

	/** the phrase tag */
	public static final String VAR = "var";

	/** the anchor tag */
	public static final String ANCHOR = "a";

	/** the list tag */
	public static final String ORDEREDLIST = "ol";

	/** the list tag */
	public static final String UNORDEREDLIST = "ul";

	/** the listitem tag */
	public static final String LISTITEM = "li";

	/** the paragraph tag */
	public static final String PARAGRAPH = "p";

	/** attribute of anchor tag */
	public static final String NAME = "name";

	/** attribute of anchor tag */
	public static final String REFERENCE = "href";

	/** attribute of anchor tag */
	public static final String[] H = new String[6];
	static {
		H[0] = "h1";
		H[1] = "h2";
		H[2] = "h3";
		H[3] = "h4";
		H[4] = "h5";
		H[5] = "h6";
	}

	// Chunks

	/** attribute of the chunk tag */
	public static final String FONT = "face";

	/** attribute of the chunk tag */
	public static final String SIZE = "point-size";

	/** attribute of the chunk/table/cell tag */
	public static final String COLOR = "color";

	/** some phrase tag */
	public static final String EM = "em";

	/** some phrase tag */
	public static final String I = "i";

	/** some phrase tag */
	public static final String STRONG = "strong";

	/** some phrase tag */
	public static final String B = "b";

	/** some phrase tag */
	public static final String S = "s";

	/** some phrase tag */
	public static final String U = "u";

	/** some phrase tag */
	public static final String SUB = "sub";

	/** some phrase tag */
	public static final String SUP = "sup";

	/** the possible value of a tag */
	public static final String HORIZONTALRULE = "hr";

	// tables/cells

	/** the table tag */
	public static final String TABLE = "table";

	/** the cell tag */
	public static final String ROW = "tr";

	/** the cell tag */
	public static final String CELL = "td";

	/** attribute of the cell tag */
	public static final String HEADERCELL = "th";

	/** attribute of the table tag */
	public static final String COLUMNS = "cols";

	/** attribute of the table tag */
	public static final String CELLPADDING = "cellpadding";

	/** attribute of the table tag */
	public static final String CELLSPACING = "cellspacing";

	/** attribute of the cell tag */
	public static final String COLSPAN = "colspan";

	/** attribute of the cell tag */
	public static final String ROWSPAN = "rowspan";

	/** attribute of the cell tag */
	public static final String NOWRAP = "nowrap";

	/** attribute of the table/cell tag */
	public static final String BORDERWIDTH = "border";

	/** attribute of the table/cell tag */
	public static final String WIDTH = "width";

	/** attribute of the table/cell tag */
	public static final String BACKGROUNDCOLOR = "bgcolor";

	/** attribute of the table/cell tag */
	public static final String BORDERCOLOR = "bordercolor";

	/** attribute of paragraph/image/table tag */
	public static final String ALIGN = "align";

	/** attribute of chapter/section/paragraph/table/cell tag */
	public static final String LEFT = "left";

	/** attribute of chapter/section/paragraph/table/cell tag */
	public static final String RIGHT = "right";

	/** attribute of the cell tag */
	public static final String HORIZONTALALIGN = "align";

	/** attribute of the cell tag */
	public static final String VERTICALALIGN = "valign";

	/** attribute of the table/cell tag */
	public static final String TOP = "top";

	/** attribute of the table/cell tag */
	public static final String BOTTOM = "bottom";

	// Misc

	/** the image tag */
	public static final String IMAGE = "img";

	/** attribute of the image tag 
	 * @see com.itextpdf.text.ElementTags#SRC
	 */
	public static final String URL = "src";

	/** attribute of the image tag */
	public static final String ALT = "alt";

	/** attribute of the image tag */
	public static final String PLAINWIDTH = "width";

	/** attribute of the image tag */
	public static final String PLAINHEIGHT = "height";

	/** the newpage tag */
	public static final String NEWLINE = "br";

	// alignment attribute values

	/** the possible value of an alignment attribute */
	public static final String ALIGN_LEFT = "Left";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_CENTER = "Center";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_RIGHT = "Right";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_JUSTIFIED = "Justify";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_TOP = "Top";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_MIDDLE = "Middle";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_BOTTOM = "Bottom";

	/** the possible value of an alignment attribute */
	public static final String ALIGN_BASELINE = "Baseline";

	/** the possible value of an alignment attribute */
	public static final String DEFAULT = "Default";

	/** The DIV tag. */
	public static final String DIV = "div";

	/** The SPAN tag. */
	public static final String SPAN = "span";

	/** The LINK tag. */
	public static final String LINK = "link";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String TEXT_CSS = "text/css";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String REL = "rel";

	/** This is used for inline css style information */
	public static final String STYLE = "style";

	/** This is a possible HTML attribute for the LINK tag. */
	public static final String TYPE = "type";

	/** This is a possible HTML attribute. */
	public static final String STYLESHEET = "stylesheet";

	/** This is a possible HTML attribute for auto-formated 
     * @since 2.1.3
     */
	public static final String PRE = "pre";
	
	/**
	 * Set containing tags that trigger a new line.
	 */
	private static final Set<String> newLineTags = new HashSet<String>();
	static {
		// Following list are the basic html tags that force new lines
		// List may be extended as we discover them
		newLineTags.add(PARAGRAPH);
		newLineTags.add("blockquote");
		newLineTags.add("br");
	}	
	
	/**
	 * Returns true if the tag causes a new line like p, br etc.
	 */
	public static boolean isNewLineTag(String tag) {
		return newLineTags.contains(tag);
	}
}
/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.html;

/**
 * Static final values of supported HTML tags and attributes.
 * @since 5.0.6
 * @deprecated since 5.5.2
 */
@Deprecated
public class HtmlTags {

	// tag names
	
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String A = "a";
	/** name of a tag */
	public static final String B = "b";
	/** name of a tag */
	public static final String BODY = "body";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String BLOCKQUOTE = "blockquote";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String BR = "br";
	/** name of a tag */
	public static final String DIV = "div";
	/** name of a tag */
	public static final String EM = "em";
	/** name of a tag */
	public static final String FONT = "font";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String H1 = "h1";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String H2 = "h2";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String H3 = "h3";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String H4 = "h4";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String H5 = "h5";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String H6 = "h6";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String HR = "hr";
	/** name of a tag */
	public static final String I = "i";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String IMG = "img";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String LI = "li";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String OL = "ol";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String P = "p";
	/** name of a tag */
	public static final String PRE = "pre";
	/** name of a tag */
	public static final String S = "s";
	/** name of a tag */
	public static final String SPAN = "span";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String STRIKE = "strike";
	/** name of a tag */
	public static final String STRONG = "strong";
	/** name of a tag */
	public static final String SUB = "sub";
	/** name of a tag */
	public static final String SUP = "sup";
	/** name of a tag */
	public static final String TABLE = "table";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String TD = "td";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String TH = "th";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String TR = "tr";
	/** name of a tag */
	public static final String U = "u";
	/**
	 * name of a tag.
	 * @since 5.0.6 (reorganized all constants)
	 */
	public static final String UL = "ul";

	// attributes (some are not real HTML attributes!)

	/** name of an attribute */
	public static final String ALIGN = "align";
	/**
	 * name of an attribute
	 * @since 5.0.6
	 */
	public static final String BGCOLOR = "bgcolor";
	/**
	 * name of an attribute
	 * @since 5.0.6
	 */
	public static final String BORDER = "border";
	/** name of an attribute */
	public static final String CELLPADDING = "cellpadding";
	/** name of an attribute */
	public static final String COLSPAN = "colspan";
	/**
	 * name of an attribute
	 * @since 5.0.6
	 */
	public static final String EXTRAPARASPACE = "extraparaspace";
	/**
	 * name of an attribute
	 * @since 5.0.6
	 */
	public static final String ENCODING = "encoding";
	/**
	 * name of an attribute
	 * @since 5.0.6
	 */
	public static final String FACE = "face";
	/**
	 * Name of an attribute.
	 * @since 5.0.6
	 */
	public static final String HEIGHT = "height";
	/**
	 * Name of an attribute.
	 * @since 5.0.6
	 */
	public static final String HREF = "href";
	/**
	 * Name of an attribute.
	 * @since 5.0.6
	 */
	public static final String HYPHENATION = "hyphenation";
	/**
	 * Name of an attribute.
	 * @since 5.0.6
	 */
	public static final String IMAGEPATH = "image_path";
	/**
	 * Name of an attribute.
	 * @since 5.0.6
	 */
	public static final String INDENT = "indent";
	/**
	 * Name of an attribute.
	 * @since 5.0.6
	 */
	public static final String LEADING = "leading";
	/** name of an attribute */
	public static final String ROWSPAN = "rowspan";
	/** name of an attribute */
	public static final String SIZE = "size";
	/**
	 * Name of an attribute.
	 * @since 5.0.6
	 */
	public static final String SRC = "src";
	/**
	 * Name of an attribute.
	 * @since 5.0.6
	 */
	public static final String VALIGN = "valign";
	/** name of an attribute */
	public static final String WIDTH = "width";
	
	// attribute values

	/** the possible value of an alignment attribute */
	public static final String ALIGN_LEFT = "left";
	/** the possible value of an alignment attribute */
	public static final String ALIGN_CENTER = "center";
	/** the possible value of an alignment attribute */
	public static final String ALIGN_RIGHT = "right";
	/** 
	 * The possible value of an alignment attribute.
	 * @since 5.0.6
	 */
	public static final String ALIGN_JUSTIFY = "justify";
	/** 
	 * The possible value of an alignment attribute.
	 * @since 5.0.6
	 */
    public static final String ALIGN_JUSTIFIED_ALL = "JustifyAll";
	/** the possible value of an alignment attribute */
	public static final String ALIGN_TOP = "top";
	/** the possible value of an alignment attribute */
	public static final String ALIGN_MIDDLE = "middle";
	/** the possible value of an alignment attribute */
	public static final String ALIGN_BOTTOM = "bottom";
	/** the possible value of an alignment attribute */
	public static final String ALIGN_BASELINE = "baseline";
	
	// CSS
	
	/** This is used for inline css style information */
	public static final String STYLE = "style";
	/**
	 * Attribute for specifying externally defined CSS class.
	 * @since 5.0.6
	 */
	public static final String CLASS = "class";
	/** the CSS tag for text color */
	public static final String COLOR = "color";
	/**
	 * The CSS tag for the font size.
	 * @since 5.0.6
	 */
	public static final String FONTFAMILY = "font-family";
	/**
	 * The CSS tag for the font size.
	 * @since 5.0.6
	 */
	public static final String FONTSIZE = "font-size";
	/**
	 * The CSS tag for the font size.
	 * @since 5.0.6
	 */
	public static final String FONTSTYLE = "font-style";
	/**
	 * The CSS tag for the font size.
	 * @since 5.0.6
	 */
	public static final String FONTWEIGHT = "font-weight";
	/**
	 * The CSS tag for the font size.
	 * @since 5.0.6
	 */
	public static final String LINEHEIGHT = "line-height";
	/**
	 * The CSS tag for the font size.
	 * @since 5.0.6
	 */
	public static final String PADDINGLEFT = "padding-left";
	/**
	 * The CSS tag for the font size.
	 * @since 5.0.6
	 */
	public static final String TEXTALIGN = "text-align";
	/**
	 * The CSS tag for the font size.
	 * @since 5.0.6
	 */
	public static final String TEXTDECORATION = "text-decoration";
	/** the CSS tag for text decorations */
	public static final String VERTICALALIGN = "vertical-align";
	/**
	 * a CSS value for text decoration
	 * @since 5.0.6
	 */
	public static final String BOLD = "bold";
	/**
	 * a CSS value for text decoration
	 * @since 5.0.6
	 */
	public static final String ITALIC = "italic";
	/**
	 * a CSS value for text decoration
	 * @since 5.0.6
	 */
	public static final String LINETHROUGH = "line-through";
	/**
	 * a CSS value for text decoration
	 * @since 5.0.6
	 */
	public static final String NORMAL = "normal";
	/**
	 * a CSS value for text decoration
	 * @since 5.0.6
	 */
	public static final String OBLIQUE = "oblique";
	/**
	 * a CSS value for text decoration
	 * @since 5.0.6
	 */
	public static final String UNDERLINE = "underline";

	/**
	 * A possible attribute.
	 * @since 5.0.6
	 */
	public static final String AFTER = "after";
	/**
	 * A possible attribute.
	 * @since 5.0.6
	 */
	public static final String BEFORE = "before";
}

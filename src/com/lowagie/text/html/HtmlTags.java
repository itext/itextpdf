/*
 * $Id$
 * $Name$
 *
 * Copyright (c) 2001 Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *
 */

package com.lowagie.text.html;

import java.awt.Color;

/**
 * A class that contains all the possible tagnames and their attributes.
 *
 * @author  bruno@lowagie.com
 */

public class HtmlTags {
    
/** the root tag. */
    public static final String HTML = "html";
    
/** the head tag */
    public static final String HEAD = "head";
    
/** This is a possible HTML attribute for the HEAD tag. */
    public static final String CONTENT = "content";
    
/** This is a possible HTML-tag. */
    public static final String LINK = "link";
    
/** This is a possible HTML attribute for the LINK tag. */
    public static final String CSS = "text/css";
    
/** This is a possible HTML attribute for the LINK tag. */
    public static final String REL = "rel";
    
/** This is a possible HTML attribute for the TD tag. */
    public static final String STYLESHEET = "stylesheet";

/** This is used for inline css style information */
    public static final String STYLE = "style";
    
/** This is a possible HTML attribute for the LINK tag. */
    public static final String TYPE = "type";
    
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
    
/** the body tag. */
    public static final String BODY = "body";
    
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
    public static final String CHUNK = "span";
    
/** the phrase tag */
    public static final String PHRASE = "span";
    
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
    
/** the paragraph tag */
    public static final String DIV = "div";
    
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
    
/** attribute of the image tag */
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

    // CSS related

/** the CSS tag for the font size */
    public static final String CSS_FONTSIZE = "font-size";

/** the CSS tag for the font style */
    public static final String CSS_FONTSTYLE = "font-style";

/** the CSS tag for the font weight */
    public static final String CSS_FONTWEIGHT = "font-weight";
    
/** the CSS tag for the font family */
    public static final String CSS_FONTFAMILY = "font-family";

/** the CSS tag for text decorations */
    public static final String CSS_TEXTDECORATION = "text-decoration";

/** the CSS tag for text color */
    public static final String CSS_COLOR = "color";

/** the CSS tag for background color */
    public static final String CSS_BGCOLOR = "background-color";

/** the CSS tag for text color */
    public static final String CSS_UNDERLINE = "underline";

/** the CSS tag for background color */
    public static final String CSS_LINETHROUGH = "line-through";
}
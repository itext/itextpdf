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

package com.lowagie.text;

/**
 * A class that contains all the possible tagnames and their attributes.
 *
 * @author  bruno@lowagie.com
 */

public class ElementTags {
    
/** This is the name of a tag. */
    public static final String TAGNAME = "tagname";
    
/** the root tag. */
    public static final String ITEXT = "itext";
    
/** attribute of the root and annotation tag (also a special tag within a chapter or section) */
    public static final String TITLE = "title";
    
/** attribute of the root tag */
    public static final String SUBJECT = "subject";
    
/** attribute of the root tag */
    public static final String KEYWORDS = "keywords";
    
/** attribute of the root tag */
    public static final String AUTHOR = "author";
    
/** attribute of the root tag */
    public static final String CREATIONDATE = "creationdate";
    
/** attribute of the root tag */
    public static final String PRODUCER = "producer";
    
    // Chapters and Sections
    
/** the chapter tag */
    public static final String CHAPTER = "chapter";
    
/** the section tag */
    public static final String SECTION = "section";
    
/** attribute of section/chapter tag */
    public static final String DEPTH = "depth";
    
/** attribute of section/chapter tag */
    public static final String INDENT = "indent";
    
/** attribute of chapter/section/paragraph/table/cell tag */
    public static final String LEFT = "left";
    
/** attribute of chapter/section/paragraph/table/cell tag */
    public static final String RIGHT = "right";
    
    // Phrases, Anchors, Lists and Paragraphs
    
/** the phrase tag */
    public static final String PHRASE = "phrase";
    
/** the anchor tag */
    public static final String ANCHOR = "anchor";
    
/** the list tag */
    public static final String LIST = "list";
    
/** the listitem tag */
    public static final String LISTITEM = "listitem";
    
/** the paragraph tag */
    public static final String PARAGRAPH = "paragraph";
    
/** attribute of phrase/paragraph/cell tag */
    public static final String LEADING = "leading";
    
/** attribute of paragraph/image/table tag */
    public static final String ALIGN = "align";
    
/** attribute of anchor tag */
    public static final String NAME = "name";
    
/** attribute of anchor tag */
    public static final String REFERENCE = "reference";
    
/** attribute of list tag */
    public static final String LISTSYMBOL = "listsymbol";
    
/** attribute of list tag */
    public static final String NUMBERED = "numbered";
    
/** attribute of list tag */
    public static final String FIRST = "first";
    
/** attribute of list tag */
    public static final String SYMBOLINDENT = "symbolindent";
    
/** attribute of list tag */
    public static final String INDENTATIONLEFT = "indentationleft";
    
/** attribute of list tag */
    public static final String INDENTATIONRIGHT = "indentationright";
    
    // Chunks
    
/** the chunk tag */
    public static final String CHUNK = "chunk";
    
/** attribute of the chunk tag */
    public static final String FONT = "font";
    
/** attribute of the chunk tag */
    public static final String SIZE = "size";
    
/** attribute of the chunk tag */
    public static final String STYLE = "style";
    
/** attribute of the chunk/table/cell tag */
    public static final String RED = "red";
    
/** attribute of the chunk/table/cell tag */
    public static final String GREEN = "green";
    
/** attribute of the chunk/table/cell tag */
    public static final String BLUE = "blue";
    
/** attribute of the chunk tag */
    public static final String LOCALGOTO = "localgoto";
    
/** attribute of the chunk tag */
    public static final String LOCALDESTINATION = "localdestination";
    
    // tables/cells
    
/** the table tag */
    public static final String TABLE = "table";
    
/** the cell tag */
    public static final String CELL = "cell";
    
/** attribute of the table tag */
    public static final String COLUMNS = "columns";
    
/** attribute of the table tag */
    public static final String LASTHEADERROW = "lastHeaderRow";
    
/** attribute of the table tag */
    public static final String CELLPADDING = "cellpadding";
    
/** attribute of the table tag */
    public static final String CELLSPACING = "cellspacing";
    
/** attribute of the table tag */
    public static final String WIDTH = "width";
    
/** attribute of the table tag */
    public static final String WIDTHS = "widths";
    
/** attribute of the cell tag */
    public static final String HORIZONTALALIGN = "horizontalalign";
    
/** attribute of the cell tag */
    public static final String VERTICALALIGN = "verticalalign";
    
/** attribute of the cell tag */
    public static final String COLSPAN = "colspan";
    
/** attribute of the cell tag */
    public static final String ROWSPAN = "rowspan";
    
/** attribute of the cell tag */
    public static final String HEADER = "header";
    
/** attribute of the cell tag */
    public static final String NOWRAP = "nowrap";
    
/** attribute of the table/cell tag */
    public static final String BORDERWIDTH = "borderwidth";
    
/** attribute of the table/cell tag */
    public static final String TOP = "top";
    
/** attribute of the table/cell tag */
    public static final String BOTTOM = "bottom";
    
/** attribute of the table/cell tag */
    public static final String BGRED = "bgred";
    
/** attribute of the table/cell tag */
    public static final String BGGREEN = "bggreen";
    
/** attribute of the table/cell tag */
    public static final String BGBLUE = "bgblue";
    
/** attribute of the table/cell tag */
    public static final String GRAYFILL = "grayfill";
    
    // Misc
    
/** the image tag */
    public static final String IMAGE = "image";
    
/** attribute of the image tag */
    public static final String URL = "url";
    
/** attribute of the image tag */
    public static final String UNDERLYING = "underlying";
    
/** attribute of the image tag */
    public static final String TEXTWRAP = "textwrap";
    
/** attribute of the image tag */
    public static final String ALT = "alt";
    
/** attribute of the image tag */
    public static final String ABSOLUTEX = "absolutex";
    
/** attribute of the image tag */
    public static final String ABSOLUTEY = "absolutey";
    
/** attribute of the image tag */
    public static final String PLAINWIDTH = "plainwidth";
    
/** attribute of the image tag */
    public static final String PLAINHEIGHT = "plainheight";
    
/** attribute of the image tag */
    public static final String SCALEDWIDTH = "scaledwidth";
    
/** attribute of the image tag */
    public static final String SCALEDHEIGHT = "scaledheight";
    
/** attribute of the image tag */
    public static final String  ROTATION = "rotation";
    
/** the newpage tag */
    public static final String NEWPAGE = "newpage";
    
/** the newpage tag */
    public static final String NEWLINE = "newline";
    
/** the annotation tag */
    public static final String ANNOTATION = "annotation";
    
/** attribute of the annotation tag */
    public static final String CONTENT = "content";
    
    // alignment attribute values
    
/** the possible value of an alignment attribute */
    public static final String ALIGN_LEFT = "Left";
    
/** the possible value of an alignment attribute */
    public static final String ALIGN_CENTER = "Center";
    
/** the possible value of an alignment attribute */
    public static final String ALIGN_RIGHT = "Right";
    
/** the possible value of an alignment attribute */
    public static final String ALIGN_JUSTIFIED = "Justified";
    
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
    
/** the possible value of an alignment attribute */
    public static final String UNKNOWN = "unknown";
    
    // methods
    
/**
 * Translates the alignment value.
 *
 * @param   alignment   the alignment value
 * @return  the translated value
 */
    
    public static String getAlignment(int alignment) {
        switch(alignment) {
        case Element.ALIGN_LEFT:
            return ALIGN_LEFT;
        case Element.ALIGN_CENTER:
            return ALIGN_CENTER;
        case Element.ALIGN_RIGHT:
            return ALIGN_RIGHT;
        case Element.ALIGN_JUSTIFIED:
            return ALIGN_JUSTIFIED;
        case Element.ALIGN_TOP:
            return ALIGN_TOP;
        case Element.ALIGN_MIDDLE:
            return ALIGN_MIDDLE;
        case Element.ALIGN_BOTTOM:
            return ALIGN_BOTTOM;
        case Element.ALIGN_BASELINE:
            return ALIGN_BASELINE;
        default:
            return DEFAULT;
        }
    }
        
}
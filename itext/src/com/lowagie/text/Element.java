/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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

import java.util.ArrayList;

/**
 * Interface for a text element.
 * <P>
 * Remark: I looked at the interface javax.swing.text.Element, but
 * I decided to write my own text-classes for two reasons:
 * <OL>
 * <LI>The javax.swing.text-classes may be very generic, I think
 * they are overkill: they are to heavy for what they have to do.
 * <LI>A lot of people using iText (formerly known as rugPdf), still use JDK1.1.x.
 * I try to keep the Java2 requirements limited to the Collection classes
 * (I think they're really great). However, if I use the javax.swing.text
 * classes, it will become very difficult to downgrade rugPdf.
 * </OL>
 *
 * @see		Anchor
 * @see		Cell
 * @see		Chapter
 * @see		Chunk
 * @see		Gif
 * @see		Graphic
 * @see		Header
 * @see		Image
 * @see		Jpeg
 * @see		List
 * @see		ListItem
 * @see		Meta
 * @see		Paragraph
 * @see		Phrase
 * @see		Rectangle
 * @see		Row
 * @see		Section
 * @see		Table
 *
 * @author  bruno@lowagie.com
 */

public interface Element {
    
    // static membervariables (meta information)
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int	HEADER = 0;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int TITLE = 1;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int SUBJECT = 2;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int KEYWORDS = 3;
    
/** This is a possible type of <CODE>Element</CIDE>. */
    public static final int AUTHOR = 4;
    
/** This is a possible type of <CODE>Element</CIDE>. */
    public static final int PRODUCER = 5;
    
/** This is a possible type of <CODE>Element</CIDE>. */
    public static final int CREATIONDATE = 6;
    
    // static membervariables (content)
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int CHUNK = 10;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int PHRASE = 11;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int PARAGRAPH = 12;
    
/** This is a possible type of <CODE>Element</CODE> */
    public static final int SECTION = 13;
    
/** This is a possible type of <CODE>Element</CODE> */
    public static final int LIST = 14;
    
/** This is a possible type of <CODE>Element</CODE> */
    public static final int LISTITEM = 15;
    
/** This is a possible type of <CODE>Element</CODE> */
    public static final int CHAPTER = 16;
    
/** This is a possible type of <CODE>Element</CODE> */
    public static final int ANCHOR = 17;
    
    // static membervariables (tables)
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int CELL = 20;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int ROW = 21;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int TABLE = 22;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int PTABLE = 23;
    
    // static membervariables (annotations)
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int ANNOTATION = 29;
    
    // static membervariables (geometric figures)
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int RECTANGLE = 30;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int GIF = 31;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int JPEG = 32;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int PNG = 33;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int IMGRAW = 34;
    
/** This is a possible type of <CODE>Element</CODE>. */
    public static final int GRAPHIC = 39;
    
    // static membervariables (alignment)
    
/**
 * A possible value for paragraph alignment.  This
 * specifies that the text is aligned to the left
 * indent and extra whitespace should be placed on
 * the right.
 */
    public static final int ALIGN_LEFT = 0;
    
/**
 * A possible value for paragraph alignment.  This
 * specifies that the text is aligned to the center
 * and extra whitespace should be placed equally on
 * the left and right.
 */
    public static final int ALIGN_CENTER = 1;
    
/**
 * A possible value for paragraph alignment.  This
 * specifies that the text is aligned to the right
 * indent and extra whitespace should be placed on
 * the left.
 */
    public static final int ALIGN_RIGHT = 2;
    
/**
 * A possible value for paragraph alignment.  This
 * specifies that extra whitespace should be spread
 * out through the rows of the paragraph with the
 * text lined up with the left and right indent
 * except on the last line which should be aligned
 * to the left.
 */
    public static final int ALIGN_JUSTIFIED = 3;
    
/**
 * A possible value for vertical alignment.
 */
    
    public static final int ALIGN_TOP = 4;
    
/**
 * A possible value for vertical alignment.
 */
    
    public static final int ALIGN_MIDDLE = 5;
    
/**
 * A possible value for vertical alignment.
 */
    
    public static final int ALIGN_BOTTOM = 6;
    
/**
 * A possible value for vertical alignment.
 */
    public static final int ALIGN_BASELINE = 7;
    
    // methods
    
/**
 * Processes the element by adding it (or the different parts) to an
 * <CODE>ElementListener</CODE>.
 *
 * @param	listener	an <CODE>ElementListener</CODE>
 * @return	<CODE>true</CODE> if the element was processed successfully
 */
    
    public boolean process(ElementListener listener);
    
/**
 * Gets the type of the text element.
 *
 * @return	a type
 */
    
    public int type();
    
/**
 * Gets all the chunks in this element.
 *
 * @return	an <CODE>ArrayList</CODE>
 */
    
    public ArrayList getChunks();
    
/**
 * Gets the content of the text element in XML form.
 *
 * @return	a type
 */
    
    public String toXml(int indent);
    
/**
 * Gets the content of the text element.
 *
 * @return	a type
 */
    
    public String toString();
}

/*
 * @(#)Header.java					0.23 2000/02/02
 *       release iText0.3:			0.23 2000/02/14
 *       release iText0.35:         0.23 2000/08/11
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
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

import java.awt.Color;

import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

/**
 * A <CODE>HeaderFooter</CODE>-object is a <CODE>Rectangle</CODe> with text
 * that can be put above and/or below every page.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * <STRONG>HeaderFooter header = new HeaderFooter(new Phrase("This is a header."), false);</STRONG>
 * <STRONG>HeaderFooter footer = new HeaderFooter(new Phrase("This is page "), new Phrase("."));</STRONG>
 * document.setHeader(header);
 * document.setFooter(footer);
 * </PRE></BLOCKQUOTE>
 *
 * @author  bruno@lowagie.com
 * @version 0.23 2000/02/02
 *
 * @since   iText0.30
 */

public class HeaderFooter extends Rectangle {

// membervariables

	/** Does the page contain a pagenumber? */
	private boolean numbered;

	/** This is the <CODE>Phrase</CODE> that comes before the pagenumber. */
	private Phrase before = null;

	/** This is number of the page. */
	private int pageN;

	/** This is the <CODE>Phrase</CODE> that comes after the pagenumber. */
	private Phrase after = null;

	/** This is alignment of the header/footer. */
	private int alignment; 

// constructors

	/**
	 * Constructs a <CODE>HeaderFooter</CODE>-object.
	 *
	 * @param	before		the <CODE>Phrase</CODE> before the pagenumber
	 * @param	after		the <CODE>Phrase</CODE> before the pagenumber
	 *
	 * @since	iText0.30
	 */

	public HeaderFooter(Phrase before, Phrase after) {
		super(0, 0, 0, 0);
		setBorder(TOP + BOTTOM);
		setBorderWidth(1.0);

		numbered = true;
		this.before = before;
		this.after = after;
	}

	/**
	 * Constructs a <CODE>Header</CODE>-object with a pagenumber at the end.
	 *
	 * @param	before		the <CODE>Phrase</CODE> before the pagenumber
	 * @param	numbered	<CODE>true</CODE> if the page has to be numbered
	 *
	 * @since	iText0.30
	 */

	public HeaderFooter(Phrase before, boolean numbered) {
		super(0, 0, 0, 0);
		setBorder(TOP + BOTTOM);
		setBorderWidth(1.0);

		this.numbered = numbered;
		this.before = before;
	}

// methods

	/**
	 * Sets the page number.
	 *
	 * @param		pageN		the new page number
	 * @return		<CODE>void</CODE>
	 *
	 * @since		iText0.30
	 */

	public final void setPageNumber(int pageN) {
		this.pageN = pageN;
	}

	/**
	 * Sets the alignment.
	 *
	 * @param		alignment	the new alignment
	 * @return		<CODE>void</CODE>
	 *
	 * @since		iText0.30
	 */

	public final void setAlignment(int alignment) {
		this.alignment = alignment;
	}

// methods to retrieve the membervariables

	/**
	 * Gets the <CODE>Paragraph</CODE> that can be used as header or footer.
	 *
	 * @return		a <CODE>Paragraph</CODE>
	 *
	 * @since		iText0.30
	 */

	public final Paragraph paragraph() {
		Paragraph paragraph = new Paragraph(before);
		if (numbered) {
			paragraph.add(String.valueOf(pageN));
		}
		if (after != null) {
			paragraph.add(after);
		} 
		paragraph.setAlignment(alignment);
		return paragraph;
	}

	/**
	 * Returns a representation of this <CODE>Section</CODE>.
	 *
	 * @return	a <CODE>String</CODE>
	 * 
	 * @since	iText0.30
	 */

	public String toString() {
		StringBuffer buf = new StringBuffer("<HEADERFOOTER NUMBERED=\"");
		buf.append(numbered);
		buf.append("\" ALIGNMENT=\"");
		buf.append(alignment);
		buf.append("\">\n");
		buf.append(super.toString());
		buf.append("\t<BEFORE>\n");
		buf.append(before.toString());
		buf.append("\t</BEFORE>");
		buf.append("\t<AFTER>\n");
		if (after != null) {
			buf.append(after.toString());
		}
		buf.append("\t</AFTER>");
		return buf.toString();
	}
}
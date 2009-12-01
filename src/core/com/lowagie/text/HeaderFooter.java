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
package com.lowagie.text;


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
 */
    
    public HeaderFooter(Phrase before, Phrase after) {
        super(0, 0, 0, 0);
        setBorder(TOP + BOTTOM);
        setBorderWidth(1);
        
        numbered = true;
        this.before = before;
        this.after = after;
    }
    
/**
 * Constructs a <CODE>Header</CODE>-object with a pagenumber at the end.
 *
 * @param	before		the <CODE>Phrase</CODE> before the pagenumber
 * @param	numbered	<CODE>true</CODE> if the page has to be numbered
 */
    
    public HeaderFooter(Phrase before, boolean numbered) {
        super(0, 0, 0, 0);
        setBorder(TOP + BOTTOM);
        setBorderWidth(1);
        
        this.numbered = numbered;
        this.before = before;
    }
    
    // methods
    
/**
 * Checks if the HeaderFooter contains a page number.
 *
 * @return  true if the page has to be numbered
 */
    
    public boolean isNumbered() {
        return numbered;
    }
    
/**
 * Gets the part that comes before the pageNumber.
 *
 * @return  a Phrase
 */
    
    public Phrase getBefore() {
        return before;
    }
    
/**
 * Gets the part that comes after the pageNumber.
 *
 * @return  a Phrase
 */
    
    public Phrase getAfter() {
        return after;
    }
    
/**
 * Sets the page number.
 *
 * @param		pageN		the new page number
 */
    
    public void setPageNumber(int pageN) {
        this.pageN = pageN;
    }
    
/**
 * Sets the alignment.
 *
 * @param		alignment	the new alignment
 */
    
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    // methods to retrieve the membervariables
    
/**
 * Gets the <CODE>Paragraph</CODE> that can be used as header or footer.
 *
 * @return		a <CODE>Paragraph</CODE>
 */
    
    public Paragraph paragraph() {
        Paragraph paragraph = new Paragraph(before.getLeading());
        paragraph.add(before);
        if (numbered) {
            paragraph.addSpecial(new Chunk(String.valueOf(pageN), before.getFont()));
        }
        if (after != null) {
            paragraph.addSpecial(after);
        }
        paragraph.setAlignment(alignment);
        return paragraph;
    }

    /**
     * Gets the alignment of this HeaderFooter.
     *
     * @return	alignment
     */

        public int alignment() {
            return alignment;
        }

}
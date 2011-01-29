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
package com.itextpdf.text;

/**
 * A <CODE>ListItem</CODE> is a <CODE>Paragraph</CODE>
 * that can be added to a <CODE>List</CODE>.
 * <P>
 * <B>Example 1:</B>
 * <BLOCKQUOTE><PRE>
 * List list = new List(true, 20);
 * list.add(<STRONG>new ListItem("First line")</STRONG>);
 * list.add(<STRONG>new ListItem("The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?")</STRONG>);
 * list.add(<STRONG>new ListItem("Third line")</STRONG>);
 * </PRE></BLOCKQUOTE>
 *
 * The result of this code looks like this:
 *	<OL>
 *		<LI>
 *			First line
 *		</LI>
 *		<LI>
 *			The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?
 *		</LI>
 *		<LI>
 *			Third line
 *		</LI>
 *	</OL>
 *
 * <B>Example 2:</B>
 * <BLOCKQUOTE><PRE>
 * List overview = new List(false, 10);
 * overview.add(<STRONG>new ListItem("This is an item")</STRONG>);
 * overview.add("This is another item");
 * </PRE></BLOCKQUOTE>
 *
 * The result of this code looks like this:
 *	<UL>
 *		<LI>
 *			This is an item
 *		</LI>
 *		<LI>
 *			This is another item
 *		</LI>
 *	</UL>
 *
 * @see	Element
 * @see List
 * @see	Paragraph
 */

public class ListItem extends Paragraph {
    
    // constants
	private static final long serialVersionUID = 1970670787169329006L;
	
	// member variables
	
	/**
	 * this is the symbol that will precede the listitem.
	 * @since	5.0	used to be private
	 */
    protected Chunk symbol;
    
    // constructors
    
    /**
     * Constructs a <CODE>ListItem</CODE>.
     */
    public ListItem() {
        super();
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain leading.
     *
     * @param	leading		the leading
     */    
    public ListItem(float leading) {
        super(leading);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Chunk</CODE>.
     *
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public ListItem(Chunk chunk) {
        super(chunk);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     */
    public ListItem(String string) {
        super(string);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>
     * and a certain <CODE>Font</CODE>.
     *
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>String</CODE>
     */
    public ListItem(String string, Font font) {
        super(string, font);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Chunk</CODE>
     * and a certain leading.
     *
     * @param	leading		the leading
     * @param	chunk		a <CODE>Chunk</CODE>
     */
    public ListItem(float leading, Chunk chunk) {
        super(leading, chunk);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>String</CODE>
     * and a certain leading.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     */
    public ListItem(float leading, String string) {
        super(leading, string);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain leading, <CODE>String</CODE>
     * and <CODE>Font</CODE>.
     *
     * @param	leading		the leading
     * @param	string		a <CODE>String</CODE>
     * @param	font		a <CODE>Font</CODE>
     */
    public ListItem(float leading, String string, Font font) {
        super(leading, string, font);
    }
    
    /**
     * Constructs a <CODE>ListItem</CODE> with a certain <CODE>Phrase</CODE>.
     *
     * @param	phrase		a <CODE>Phrase</CODE>
     */
    public ListItem(Phrase phrase) {
        super(phrase);
    }
    
    // implementation of the Element-methods
    
    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    public int type() {
        return Element.LISTITEM;
    }
    
    // methods
    
    /**
     * Sets the listsymbol.
     *
     * @param	symbol	a <CODE>Chunk</CODE>
     */
    public void setListSymbol(Chunk symbol) {
    	if (this.symbol == null) {
    		this.symbol = symbol;
    		if (this.symbol.getFont().isStandardFont()) {
    			this.symbol.setFont(font);
    		}
    	}
    }
    
    /**
     * Sets the indentation of this paragraph on the left side.
     *
     * @param	indentation		the new indentation
     */
    public void setIndentationLeft(float indentation, boolean autoindent) {
    	if (autoindent) {
    		setIndentationLeft(getListSymbol().getWidthPoint());
    	}
    	else {
    		setIndentationLeft(indentation);
    	}
    }

    /**
     * Changes the font of the list symbol to the font of the first chunk
     * in the list item.
     * @since 5.0.6
     */
    public void adjustListSymbolFont() {
		java.util.List<Chunk> cks = getChunks();
		if (!cks.isEmpty() && symbol != null)
			symbol.setFont(cks.get(0).getFont());
    }
    
    // methods to retrieve information

	/**
     * Returns the listsymbol.
     *
     * @return	a <CODE>Chunk</CODE>
     */
    public Chunk getListSymbol() {
        return symbol;
    }

}

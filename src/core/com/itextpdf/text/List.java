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

import java.util.ArrayList;
import java.util.Iterator;

import com.itextpdf.text.factories.RomanAlphabetFactory;

/**
 * A <CODE>List</CODE> contains several <CODE>ListItem</CODE>s.
 * <P>
 * <B>Example 1:</B>
 * <BLOCKQUOTE><PRE>
 * <STRONG>List list = new List(true, 20);</STRONG>
 * <STRONG>list.add(new ListItem("First line"));</STRONG>
 * <STRONG>list.add(new ListItem("The second line is longer to see what happens once the end of the line is reached. Will it start on a new line?"));</STRONG>
 * <STRONG>list.add(new ListItem("Third line"));</STRONG>
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
 * <STRONG>List overview = new List(false, 10);</STRONG>
 * <STRONG>overview.add(new ListItem("This is an item"));</STRONG>
 * <STRONG>overview.add("This is another item");</STRONG>
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
 * @see		Element
 * @see		ListItem
 */

public class List implements TextElementArray {
    
    // constants
	
	/** a possible value for the numbered parameter */
	public static final boolean ORDERED = true;
	/** a possible value for the numbered parameter */
	public static final boolean UNORDERED = false;
	/** a possible value for the lettered parameter */
	public static final boolean NUMERICAL = false;
	/** a possible value for the lettered parameter */
	public static final boolean ALPHABETICAL = true;
	/** a possible value for the lettered parameter */
	public static final boolean UPPERCASE = false;
	/** a possible value for the lettered parameter */
	public static final boolean LOWERCASE = true;
	
    // member variables
	
	/** This is the <CODE>ArrayList</CODE> containing the different <CODE>ListItem</CODE>s. */
    protected ArrayList list = new ArrayList();
    
    /** Indicates if the list has to be numbered. */
    protected boolean numbered = false;
    /** Indicates if the listsymbols are numerical or alphabetical. */
    protected boolean lettered = false;
    /** Indicates if the listsymbols are lowercase or uppercase. */
    protected boolean lowercase = false;
    /** Indicates if the indentation has to be set automatically. */
    protected boolean autoindent = false;
    /** Indicates if the indentation of all the items has to be aligned. */
    protected boolean alignindent = false;
    
    /** This variable indicates the first number of a numbered list. */
    protected int first = 1;
    /** This is the listsymbol of a list that is not numbered. */
    protected Chunk symbol = new Chunk("- ");
    /**
     * In case you are using numbered/lettered lists, this String is added before the number/letter.
     * @since	iText 2.1.1
     */
    protected String preSymbol = "";
    /**
     * In case you are using numbered/lettered lists, this String is added after the number/letter.	
     * @since	iText 2.1.1
     */
    protected String postSymbol = ". ";
    
    /** The indentation of this list on the left side. */
    protected float indentationLeft = 0;
    /** The indentation of this list on the right side. */
    protected float indentationRight = 0;
    /** The indentation of the listitems. */
    protected float symbolIndent = 0;
    
    // constructors

    /** Constructs a <CODE>List</CODE>. */
    public List() {
        this(false, false);
    }
    
    /**
     * Constructs a <CODE>List</CODE> with a specific symbol indentation.
     * @param	symbolIndent	the symbol indentation
     * @since	iText 2.0.8
     */
    public List(float symbolIndent) {
    	this.symbolIndent = symbolIndent;
    }
    
    /**
     * Constructs a <CODE>List</CODE>.
     * @param	numbered		a boolean
     */
    public List(boolean numbered) {
      	this(numbered, false);
    }
        
    /**
     * Constructs a <CODE>List</CODE>.
     * @param	numbered		a boolean
     * @param lettered has the list to be 'numbered' with letters
     */
    public List(boolean numbered, boolean lettered) {
    	this.numbered = numbered;
        this.lettered = lettered;
        this.autoindent = true;
        this.alignindent = true;
    }
    
    /**
     * Constructs a <CODE>List</CODE>.
     * <P>
     * Remark: the parameter <VAR>symbolIndent</VAR> is important for instance when
     * generating PDF-documents; it indicates the indentation of the listsymbol.
     * It is not important for HTML-documents.
     *
     * @param	numbered		a boolean
     * @param	symbolIndent	the indentation that has to be used for the listsymbol
     */
    public List(boolean numbered, float symbolIndent) {
        this(numbered, false, symbolIndent);
    }
    
    /**
     * Creates a list
     * @param numbered has the list to be numbered?
     * @param lettered has the list to be 'numbered' with letters
     * @param symbolIndent the indentation of the symbol
     */
    public List(boolean numbered, boolean lettered, float symbolIndent) {
        this.numbered = numbered;
        this.lettered = lettered;
        this.symbolIndent = symbolIndent;
    }
    
    // implementation of the Element-methods
    
    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener	an <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(ElementListener listener) {
        try {
            for (Iterator i = list.iterator(); i.hasNext(); ) {
                listener.add((Element) i.next());
            }
            return true;
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */
    public int type() {
        return Element.LIST;
    }
    
    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public ArrayList getChunks() {
        ArrayList tmp = new ArrayList();
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            tmp.addAll(((Element) i.next()).getChunks());
        }
        return tmp;
    }
    
    // methods to set the membervariables
    
    /**
     * Adds an <CODE>Object</CODE> to the <CODE>List</CODE>.
     *
     * @param	o		the object to add.
     * @return true if adding the object succeeded
     */
    public boolean add(Object o) {
        if (o instanceof ListItem) {
            ListItem item = (ListItem) o;
            if (numbered || lettered) {
                Chunk chunk = new Chunk(preSymbol, symbol.getFont());
                int index = first + list.size();
                if ( lettered )
                    chunk.append(RomanAlphabetFactory.getString(index, lowercase));
                else
                    chunk.append(String.valueOf(index));
                chunk.append(postSymbol);
                item.setListSymbol(chunk);
            }
            else {
                item.setListSymbol(symbol);
            }
            item.setIndentationLeft(symbolIndent, autoindent);
            item.setIndentationRight(0);
            return list.add(item);
        }
        else if (o instanceof List) {
            List nested = (List) o;
            nested.setIndentationLeft(nested.getIndentationLeft() + symbolIndent);
            first--;
            return list.add(nested);
        }
        else if (o instanceof String) {
            return this.add(new ListItem((String) o));
        }
        return false;
    }
    
    // extra methods
	
	/** Makes sure all the items in the list have the same indentation. */
    public void normalizeIndentation() {
        float max = 0;
    	Element o;
        for (Iterator i = list.iterator(); i.hasNext(); ) {
        	o = (Element)i.next();
            if (o instanceof ListItem) {
            	max = Math.max(max, ((ListItem)o).getIndentationLeft());
            }
        }
        for (Iterator i = list.iterator(); i.hasNext(); ) {
        	o = (Element)i.next();
            if (o instanceof ListItem) {
            	((ListItem)o).setIndentationLeft(max);
            }
        }
    }
    
    // setters

	/**
	 * @param numbered the numbered to set
	 */
	public void setNumbered(boolean numbered) {
		this.numbered = numbered;
	}

	/**
	 * @param lettered the lettered to set
	 */
	public void setLettered(boolean lettered) {
		this.lettered = lettered;
	}

	/**
	 * @param uppercase the uppercase to set
	 */
	public void setLowercase(boolean uppercase) {
		this.lowercase = uppercase;
	}

	/**
	 * @param autoindent the autoindent to set
	 */
	public void setAutoindent(boolean autoindent) {
		this.autoindent = autoindent;
	}
	/**
	 * @param alignindent the alignindent to set
	 */
	public void setAlignindent(boolean alignindent) {
		this.alignindent = alignindent;
	}
    
    /**
     * Sets the number that has to come first in the list.
     *
     * @param	first		a number
     */
    public void setFirst(int first) {
        this.first = first;
    }
    
    /**
     * Sets the listsymbol.
     *
     * @param	symbol		a <CODE>Chunk</CODE>
     */
    public void setListSymbol(Chunk symbol) {
        this.symbol = symbol;
    }
    
    /**
     * Sets the listsymbol.
     * <P>
     * This is a shortcut for <CODE>setListSymbol(Chunk symbol)</CODE>.
     *
     * @param	symbol		a <CODE>String</CODE>
     */
    public void setListSymbol(String symbol) {
        this.symbol = new Chunk(symbol);
    }
    
    /**
     * Sets the indentation of this paragraph on the left side.
     *
     * @param	indentation		the new indentation
     */
    public void setIndentationLeft(float indentation) {
        this.indentationLeft = indentation;
    }
    
    /**
     * Sets the indentation of this paragraph on the right side.
     *
     * @param	indentation		the new indentation
     */
    public void setIndentationRight(float indentation) {
        this.indentationRight = indentation;
    }

	/**
	 * @param symbolIndent the symbolIndent to set
	 */
	public void setSymbolIndent(float symbolIndent) {
		this.symbolIndent = symbolIndent;
	}
    
    // methods to retrieve information
    
    /**
     * Gets all the items in the list.
     *
     * @return	an <CODE>ArrayList</CODE> containing <CODE>ListItem</CODE>s.
     */
    public ArrayList getItems() {
        return list;
    }
    
    /**
     * Gets the size of the list.
     *
     * @return	a <CODE>size</CODE>
     */
    public int size() {
        return list.size();
    }

    /**
     * Returns <CODE>true</CODE> if the list is empty.
     * 
     * @return <CODE>true</CODE> if the list is empty
     */
    public boolean isEmpty() {
    	return list.isEmpty();
    }

    /**
     * Gets the leading of the first listitem.
     *
     * @return	a <CODE>leading</CODE>
     */
    public float getTotalLeading() {
        if (list.size() < 1) {
            return -1;
        }
        ListItem item = (ListItem) list.get(0);
        return item.getTotalLeading();
    }
    
    // getters
    
    /**
     * Checks if the list is numbered.
     * @return	<CODE>true</CODE> if the list is numbered, <CODE>false</CODE> otherwise.
     */
    
    public boolean isNumbered() {
        return numbered;
    }

    /**
     * Checks if the list is lettered.
     * @return  <CODE>true</CODE> if the list is lettered, <CODE>false</CODE> otherwise.
     */
    public boolean isLettered() {
        return lettered;
    }

    /**
     * Checks if the list lettering is lowercase.
     * @return  <CODE>true</CODE> if it is lowercase, <CODE>false</CODE> otherwise.
     */
    public boolean isLowercase() {
        return lowercase;
    }
    
    /**
     * Checks if the indentation of list items is done automatically.
	 * @return the autoindent
	 */
	public boolean isAutoindent() {
		return autoindent;
	}
	
	/**
	 * Checks if all the listitems should be aligned.
	 * @return the alignindent
	 */
	public boolean isAlignindent() {
		return alignindent;
	}

	/**
     * Gets the first number        .
     * @return a number
	 */
	public int getFirst() {
		return first;
	}

	/**
     * Gets the Chunk containing the symbol.
     * @return a Chunk with a symbol
	 */
	public Chunk getSymbol() {
		return symbol;
	}

	/**
     * Gets the indentation of this paragraph on the left side.
     * @return	the indentation
	 */
	public float getIndentationLeft() {
		return indentationLeft;
	}

	/**
     * Gets the indentation of this paragraph on the right side.
     * @return	the indentation
	 */
	public float getIndentationRight() {
		return indentationRight;
	}

	/**
     * Gets the symbol indentation.
     * @return the symbol indentation
	 */
	public float getSymbolIndent() {
		return symbolIndent;
	}
	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return true;
	}

	/**
	 * Returns the String that is after a number or letter in the list symbol.
	 * @return	the String that is after a number or letter in the list symbol
	 * @since	iText 2.1.1
	 */
	public String getPostSymbol() {
		return postSymbol;
	}

	/**
	 * Sets the String that has to be added after a number or letter in the list symbol.
	 * @since	iText 2.1.1
	 * @param	postSymbol the String that has to be added after a number or letter in the list symbol.
	 */
	public void setPostSymbol(String postSymbol) {
		this.postSymbol = postSymbol;
	}

	/**
	 * Returns the String that is before a number or letter in the list symbol.
	 * @return	the String that is before a number or letter in the list symbol
	 * @since	iText 2.1.1
	 */
	public String getPreSymbol() {
		return preSymbol;
	}

	/**
	 * Sets the String that has to be added before a number or letter in the list symbol.
	 * @since	iText 2.1.1
	 * @param	preSymbol the String that has to be added before a number or letter in the list symbol.
	 */
	public void setPreSymbol(String preSymbol) {
		this.preSymbol = preSymbol;
	}

}
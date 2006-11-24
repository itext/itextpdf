/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

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

public class List implements TextElementArray, MarkupAttributes {
    
    // membervariables
	/** a possible value for the numbered parameter */
	public static final boolean ORDERED = true;
	/** a possible value for the numbered parameter */
	public static final boolean UNORDERED = false;
	/** a possible value for the lettered parameter */
	public static final boolean NUMBERICAL = false;
	/** a possible value for the lettered parameter */
	public static final boolean ALPHABETICAL = true;
	
    
/** This is the <CODE>ArrayList</CODE> containing the different <CODE>ListItem</CODE>s. */
    protected ArrayList list = new ArrayList();
    
/** This variable indicates if the list has to be numbered. */
    protected boolean numbered;
    protected boolean lettered;
    
/** This variable indicates the first number of a numbered list. */
    protected int first = 1;
    protected char firstCh = 'A';
    protected char lastCh  = 'Z';
    
/** This is the listsymbol of a list that is not numbered. */
    protected Chunk symbol = new Chunk("-");
    
/** The indentation of this list on the left side. */
    protected float indentationLeft = 0;
    
/** The indentation of this list on the right side. */
    protected float indentationRight = 0;
    
/** The indentation of the listitems. */
    protected float symbolIndent;

/** Contains extra markupAttributes */
    protected Properties markupAttributes;
    
    // constructors
    
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
        this.numbered = numbered;
        this.lettered = false;
        this.symbolIndent = symbolIndent;
    }
    
    /**
     * Creates a list
     * @param numbered has the list to be numbered?
     * @param lettered has the list to be 'numbered' with letters
     * @param symbolIndent the indentation of the symbol
     */
    public List(boolean numbered, boolean lettered, float symbolIndent ) {
        this.numbered = numbered;
        this.lettered = lettered;
        this.symbolIndent = symbolIndent;
    }
    
        /**
         * Returns a <CODE>List</CODE> that has been constructed taking in account
         * the value of some <VAR>attributes</VAR>.
         *
         * @param	attributes		Some attributes
         */
    
    public List(Properties attributes) {
        String value= (String)attributes.remove(ElementTags.LISTSYMBOL);
        if (value == null) {
            value = "-";
        }
        symbol = new Chunk(value, FontFactory.getFont(attributes));
        
        if ((value = (String)attributes.remove(ElementTags.NUMBERED)) != null) {
            this.numbered = Boolean.valueOf(value).booleanValue();
        }
        if ((value = (String)attributes.remove(ElementTags.LETTERED)) != null) {
            this.lettered = Boolean.valueOf(value).booleanValue();
            if ( this.numbered && this.lettered )
                this.numbered = false;
        }
        if ((value = (String)attributes.remove(ElementTags.SYMBOLINDENT)) != null) {
            this.symbolIndent = Float.parseFloat(value);
        }
        
        if ((value = (String)attributes.remove(ElementTags.FIRST)) != null) {
            char khar = value.charAt(0);
            if ( Character.isLetter( khar ) ) {
                setFirst( khar );
            }
            else {
                setFirst(Integer.parseInt(value));
            }
        }
        if ((value = (String)attributes.remove(ElementTags.INDENTATIONLEFT)) != null) {
            setIndentationLeft(Float.parseFloat(value + "f"));
        }
        if ((value = (String)attributes.remove(ElementTags.INDENTATIONRIGHT)) != null) {
            setIndentationRight(Float.parseFloat(value + "f"));
        }
        if (attributes.size() > 0) setMarkupAttributes(attributes);
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
                Chunk chunk;
                if ( lettered )
                    chunk = new Chunk(nextLetter(), symbol.font());
                else
                    chunk = new Chunk(String.valueOf(first + list.size()), symbol.font());
                chunk.append(".");
                item.setListSymbol(chunk);
            }
            else {
                item.setListSymbol(symbol);
            }
            item.setIndentationLeft(symbolIndent);
            item.setIndentationRight(0);
            list.add(item);
        }
        else if (o instanceof List) {
            List nested = (List) o;
            nested.setIndentationLeft(nested.indentationLeft() + symbolIndent);
            first--;
            return list.add(nested);
        }
        else if (o instanceof String) {
            return this.add(new ListItem((String) o));
        }
        return false;
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
 * Sets the number that has to come first in the list.
 *
 * @param	first		a number
 */
    
    public void setFirst(int first) {
        this.first = first;
    }
    
    
/**
 * Sets the Letter that has to come first in the list.
 *
 * @param	first		a letter
 */
    
    public void setFirst(char first) {
        this.firstCh = first;
        if ( Character.isLowerCase( this.firstCh )) {
            this.lastCh = 'z';
        }
        else {
            this.lastCh = 'Z';
        }
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
 * Gets the leading of the first listitem.
 *
 * @return	a <CODE>leading</CODE>
 */
    
    public float leading() {
        if (list.size() < 1) {
            return -1;
        }
        ListItem item = (ListItem) list.get(0);
        return item.leading();
    }
    
/**
 * Checks if the list is numbered.
 *
 * @return	<CODE>true</CODE> if the list is numbered, <CODE>false</CODE> otherwise.
 */
    
    public boolean isNumbered() {
        return numbered;
    }
    
/**
 * Gets the symbol indentation.
 * @return the symbol indentation
 */
    
    public float symbolIndent() {
        return symbolIndent;
    }
    
/**
 * Gets the Chunk containing the symbol.
 * @return a Chunk with a symbol
 */
    
    public Chunk symbol() {
        return symbol;
    }
    
/**
 * Gets the first number        .
 * @return a number
 */
    
    public int first() {
        return first;
    }
    
/**
 * Gets the indentation of this paragraph on the left side.
 *
 * @return	the indentation
 */
    
    public float indentationLeft() {
        return indentationLeft;
    }
    
/**
 * Gets the indentation of this paragraph on the right side.
 *
 * @return	the indentation
 */
    
    public float indentationRight() {
        return indentationRight;
    }
    
/**
 * Checks if a given tag corresponds with the listsymbol tag of this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isSymbol(String tag) {
        return ElementTags.LISTSYMBOL.equals(tag);
    }
    
/**
 * Checks if a given tag corresponds with this object.
 *
 * @param   tag     the given tag
 * @return  true if the tag corresponds
 */
    
    public static boolean isTag(String tag) {
        return ElementTags.LIST.equals(tag);
    }

/**
 * Retrieves the next letter in the sequence
 *
 * @return  String contains the next character (A-Z or a-z)
 */
    private String nextLetter() {
        int num_in_list = listItemsInList(); //list.size();
        int max_ival = (lastCh + 0);
        int ival = (firstCh + num_in_list);
        while ( ival > max_ival ) {
            ival -= 26;
        }
        char[] new_char = new char[1];
        new_char[0] = (char) ival;
        String ret = new String( new_char );
        return ret;
    }
    
    /**
     * Counts the number of ListItems in the list ommiting nested lists
     *
     * @return  Integer number of ListItems in the list
     */
    private int listItemsInList() {
        int result = 0;
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            if (!(i.next() instanceof List)) result++;
        }
        return result;
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#setMarkupAttribute(java.lang.String, java.lang.String)
 */
    public void setMarkupAttribute(String name, String value) {
		if (markupAttributes == null) markupAttributes = new Properties();
        markupAttributes.put(name, value);
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#setMarkupAttributes(java.util.Properties)
 */
    public void setMarkupAttributes(Properties markupAttributes) {
        this.markupAttributes = markupAttributes;
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#getMarkupAttribute(java.lang.String)
 */
    public String getMarkupAttribute(String name) {
        return (markupAttributes == null) ? null : String.valueOf(markupAttributes.get(name));
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributeNames()
 */
    public Set getMarkupAttributeNames() {
        return Chunk.getKeySet(markupAttributes);
    }
    
/**
 * @see com.lowagie.text.MarkupAttributes#getMarkupAttributes()
 */
    public Properties getMarkupAttributes() {
        return markupAttributes;
    }
}

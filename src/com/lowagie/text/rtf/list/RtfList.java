/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002, 2003, 2004, 2005 by Mark Hall
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
 * LGPL license (the ?GNU LIBRARY GENERAL PUBLIC LICENSE?), in which case the
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

package com.lowagie.text.rtf.list;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.RomanList;
import com.lowagie.text.factories.RomanAlphabetFactory;
import com.lowagie.text.factories.RomanNumberFactory;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.RtfExtendedElement;
import com.lowagie.text.rtf.document.RtfDocument;
import com.lowagie.text.rtf.style.RtfFont;
import com.lowagie.text.rtf.style.RtfFontList;
import com.lowagie.text.rtf.style.RtfParagraphStyle;
import com.lowagie.text.rtf.text.RtfParagraph;


/**
 * The RtfList stores one List. It also provides the methods to write the
 * list declaration and the list data.
 *  
 * @version $Id$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
 * @author Thomas Bickel (tmb99@inode.at)
 */
public class RtfList extends RtfElement implements RtfExtendedElement {

    /**
     * Constant for list level
     */
    private static final byte[] LIST_LEVEL = "\\listlevel".getBytes();
    /**
     * Constant for list level style old
     */
    private static final byte[] LIST_LEVEL_TYPE = "\\levelnfc".getBytes();
    /**
     * Constant for list level style new
     */
    private static final byte[] LIST_LEVEL_TYPE_NEW = "\\levelnfcn".getBytes();
    /**
     * Constant for list level alignment old
     */
    private static final byte[] LIST_LEVEL_ALIGNMENT = "\\leveljc".getBytes();
    /**
     * Constant for list level alignment new
     */
    private static final byte[] LIST_LEVEL_ALIGNMENT_NEW = "\\leveljcn".getBytes();
    /**
     * Constant for list level start at
     */
    private static final byte[] LIST_LEVEL_START_AT = "\\levelstartat".getBytes();
    /**
     * Constant for list level text
     */
    private static final byte[] LIST_LEVEL_TEXT = "\\leveltext".getBytes();
    /**
     * Constant for the beginning of the list level numbered style
     */
    private static final byte[] LIST_LEVEL_STYLE_NUMBERED_BEGIN = "\\\'02\\\'".getBytes();
    /**
     * Constant for the end of the list level numbered style
     */
    private static final byte[] LIST_LEVEL_STYLE_NUMBERED_END = ".;".getBytes();
    /**
     * Constant for the list level bulleted style
     */
    private static final byte[] LIST_LEVEL_STYLE_BULLETED = "\\\'01\\u-3913 ?;".getBytes();
    /**
     * Constant for the beginning of the list level numbers
     */
    private static final byte[] LIST_LEVEL_NUMBERS_BEGIN = "\\levelnumbers".getBytes();
    /**
     * Constant for the list level numbers
     */
    private static final byte[] LIST_LEVEL_NUMBERS_NUMBERED = "\\\'01".getBytes();
    /**
     * Constant for the end of the list level numbers
     */
    private static final byte[] LIST_LEVEL_NUMBERS_END = ";".getBytes();
    /**
     * Constant for the first indentation
     */
    private static final byte[] LIST_LEVEL_FIRST_INDENT = "\\fi".getBytes();
    /**
     * Constant for the symbol indentation
     */
    private static final byte[] LIST_LEVEL_SYMBOL_INDENT = "\\tx".getBytes();
    /**
     * Constant for the list level value
     */
    private static final byte[] LIST_LEVEL_NUMBER = "\\ilvl".getBytes();
    /**
     * Constant for a tab character
     */
    private static final byte[] TAB = "\\tab".getBytes();
    /**
     * Constant for the old list text
     */
    private static final byte[] LIST_TEXT = "\\listtext".getBytes();
    /**
     * Constant for the old list number end
     */
    private static final byte[] LIST_NUMBER_END = ".".getBytes();
    /**
     * Constant for the old bulleted list 
     */
    private static final byte[] LIST_BULLET = "\\\'b7".getBytes();
    
    private static final int LIST_TYPE_BULLET = 0;
    private static final int LIST_TYPE_NUMBERED = 1;
    private static final int LIST_TYPE_UPPER_LETTERS = 2;
    private static final int LIST_TYPE_LOWER_LETTERS = 3;
    private static final int LIST_TYPE_UPPER_ROMAN = 4;
    private static final int LIST_TYPE_LOWER_ROMAN = 5;
    
    /**
     * The subitems of this RtfList
     */
    private ArrayList items;
    /**
     * The level of this RtfList
     */
    private int listLevel = 0;
    /**
     * The first indentation of this RtfList
     */
    private int firstIndent = 0;
    /**
     * The left indentation of this RtfList
     */
    private int leftIndent = 0;
    /**
     * The right indentation of this RtfList
     */
    private int rightIndent = 0;
    /**
     * The symbol indentation of this RtfList
     */
    private int symbolIndent = 0;
    /**
     * The list number of this RtfList
     */
    private int listNumber = 1;
    /**
     * Whether this RtfList is numbered
     */
    private int listType = LIST_TYPE_BULLET;
    /**
     * The RtfFont for numbered lists
     */
    private RtfFont fontNumber;
    /**
     * The RtfFont for bulleted lists
     */
    private RtfFont fontBullet;
    /**
     * The alignment of this RtfList
     */
    private int alignment = Element.ALIGN_LEFT;
    /**
     * The parent List in multi-level lists.
     */
    private RtfList parentList = null;
    
    /**
     * Constructs a new RtfList for the specified List.
     * 
     * @param doc The RtfDocument this RtfList belongs to
     * @param list The List this RtfList is based on
     */
    public RtfList(RtfDocument doc, List list) {
        super(doc);
        
        this.listNumber = document.getDocumentHeader().getListNumber(this);
        
        this.items = new ArrayList();
        if(list.getSymbolIndent() > 0 && list.getIndentationLeft() > 0) {
            this.firstIndent = (int) (list.getSymbolIndent() * RtfElement.TWIPS_FACTOR * -1);
            this.leftIndent = (int) ((list.getIndentationLeft() + list.getSymbolIndent()) * RtfElement.TWIPS_FACTOR);
        } else if(list.getSymbolIndent() > 0) {
            this.firstIndent = (int) (list.getSymbolIndent() * RtfElement.TWIPS_FACTOR * -1);
            this.leftIndent = (int) (list.getSymbolIndent() * RtfElement.TWIPS_FACTOR);
        } else if(list.getIndentationLeft() > 0) {
            this.firstIndent = 0;
            this.leftIndent = (int) (list.getIndentationLeft() * RtfElement.TWIPS_FACTOR);
        } else {
            this.firstIndent = 0;
            this.leftIndent = 0;
        }
        this.rightIndent = (int) (list.getIndentationRight() * RtfElement.TWIPS_FACTOR);
        this.symbolIndent = (int) ((list.getSymbolIndent() + list.getIndentationLeft()) * RtfElement.TWIPS_FACTOR);
        
        if(list instanceof RomanList) {
            if(list.isLowercase()) {
                this.listType = LIST_TYPE_LOWER_ROMAN;
            } else {
                this.listType = LIST_TYPE_UPPER_ROMAN;
            }
        } else if(list.isNumbered()) {
            this.listType = LIST_TYPE_NUMBERED;
        } else if(list.isLettered()) {
            if(list.isLowercase()) {
                this.listType = LIST_TYPE_LOWER_LETTERS;
            } else {
                this.listType = LIST_TYPE_UPPER_LETTERS;
            }
        }
        
        for(int i = 0; i < list.getItems().size(); i++) {
            try {
                Element element = (Element) list.getItems().get(i);
                if(element.type() == Element.CHUNK) {
                    element = new ListItem((Chunk) element);
                }
                if(element instanceof ListItem) {
                    this.alignment = ((ListItem) element).getAlignment();
                }
                RtfBasicElement rtfElement = doc.getMapper().mapElement(element);
                if(rtfElement instanceof RtfList) {
                    ((RtfList) rtfElement).setListNumber(listNumber);
                    ((RtfList) rtfElement).setListLevel(listLevel + 1);
                    ((RtfList) rtfElement).setParent(this);
                } else if(rtfElement instanceof RtfListItem) {
                    ((RtfListItem) rtfElement).setParent(this);
                    ((RtfListItem) rtfElement).inheritListSettings(listNumber, listLevel + 1);
                }
                items.add(rtfElement);
            } catch(DocumentException de) {
                de.printStackTrace();
            }
        }
        
        if(this.listLevel == 0) {
            correctIndentation();
        }
        
        fontNumber = new RtfFont(document, new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, new Color(0, 0, 0)));
        fontBullet = new RtfFont(document, new Font(Font.SYMBOL, 10, Font.NORMAL, new Color(0, 0, 0)));
    }
    
    private void writeIndentations(final OutputStream result) throws IOException
    {
        result.write(LIST_LEVEL_FIRST_INDENT);
        result.write(intToByteArray(firstIndent));
        result.write(RtfParagraphStyle.INDENT_LEFT);
        result.write(intToByteArray(leftIndent));
        result.write(RtfParagraphStyle.INDENT_RIGHT);
        result.write(intToByteArray(rightIndent));    	
    }
    
    /**
     * Writes the definition part of this list level
     * 
     * @return A byte array containing the definition of this list level
     * @deprecated replaced by {@link #writeDefinition(OutputStream)}
     */
    public byte[] writeDefinition()
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
        	writeDefinition(result);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    /**
     * Writes the definition part of this list level
     */
    public void writeDefinition(final OutputStream result) throws IOException
    {
        result.write(OPEN_GROUP);
        result.write(LIST_LEVEL);
        result.write(LIST_LEVEL_TYPE);
        switch(this.listType) {
            case LIST_TYPE_BULLET        : result.write(intToByteArray(23)); break;
            case LIST_TYPE_NUMBERED      : result.write(intToByteArray(0)); break;
            case LIST_TYPE_UPPER_LETTERS : result.write(intToByteArray(3)); break;
            case LIST_TYPE_LOWER_LETTERS : result.write(intToByteArray(4)); break;
            case LIST_TYPE_UPPER_ROMAN   : result.write(intToByteArray(1)); break;
            case LIST_TYPE_LOWER_ROMAN   : result.write(intToByteArray(2)); break;
        }
        result.write(LIST_LEVEL_TYPE_NEW);
        switch(this.listType) {
            case LIST_TYPE_BULLET        : result.write(intToByteArray(23)); break;
            case LIST_TYPE_NUMBERED      : result.write(intToByteArray(0)); break;
            case LIST_TYPE_UPPER_LETTERS : result.write(intToByteArray(3)); break;
            case LIST_TYPE_LOWER_LETTERS : result.write(intToByteArray(4)); break;
            case LIST_TYPE_UPPER_ROMAN   : result.write(intToByteArray(1)); break;
            case LIST_TYPE_LOWER_ROMAN   : result.write(intToByteArray(2)); break;
        }
        result.write(LIST_LEVEL_ALIGNMENT);
        result.write(intToByteArray(0));
        result.write(LIST_LEVEL_ALIGNMENT_NEW);
        result.write(intToByteArray(0));
        result.write(LIST_LEVEL_START_AT);
        result.write(intToByteArray(1));
        result.write(OPEN_GROUP);
        result.write(LIST_LEVEL_TEXT);
        if(this.listType != LIST_TYPE_BULLET) {
            result.write(LIST_LEVEL_STYLE_NUMBERED_BEGIN);
            if(listLevel < 10) {
                result.write(intToByteArray(0));
            }
            result.write(intToByteArray(listLevel));
            result.write(LIST_LEVEL_STYLE_NUMBERED_END);
        } else {
            result.write(LIST_LEVEL_STYLE_BULLETED);
        }
        result.write(CLOSE_GROUP);
        result.write(OPEN_GROUP);
        result.write(LIST_LEVEL_NUMBERS_BEGIN);
        if(this.listType != LIST_TYPE_BULLET) {
            result.write(LIST_LEVEL_NUMBERS_NUMBERED);
        }
        result.write(LIST_LEVEL_NUMBERS_END);
        result.write(CLOSE_GROUP);
        result.write(RtfFontList.FONT_NUMBER);
        if(this.listType != LIST_TYPE_BULLET) {
            result.write(intToByteArray(fontNumber.getFontNumber()));
        } else {
            result.write(intToByteArray(fontBullet.getFontNumber()));
        }
        //.result.write(writeIndentations());
        writeIndentations(result);
        result.write(LIST_LEVEL_SYMBOL_INDENT);
        result.write(intToByteArray(this.leftIndent));
        result.write(CLOSE_GROUP);
        result.write("\n".getBytes());
        for(int i = 0; i < items.size(); i++) {
            RtfElement rtfElement = (RtfElement) items.get(i);
            if(rtfElement instanceof RtfList) {
            	RtfList rl = (RtfList)rtfElement;
                //.result.write(((RtfList) rtfElement).writeDefinition());
            	rl.writeDefinition(result);
                break;
            } else if(rtfElement instanceof RtfListItem) {
            	RtfListItem rli = (RtfListItem) rtfElement;
                //.byte[] data = rli.writeDefinition();
                //.if(data.length > 0) {
                //.    result.write(data);
                //.    break;
                //.}
            	if(rli.writeDefinition(result)) break;
            }
        }    	
    }

    
    /**
     * Writes the initialisation part of the RtfList
     * 
     * @return A byte array containing the initialisation part
     */
    protected byte[] writeListBeginning() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(RtfParagraph.PARAGRAPH_DEFAULTS);
            if(this.inTable) {
                result.write(RtfParagraph.IN_TABLE);
            }
            switch (this.alignment) {
                case Element.ALIGN_LEFT:
                    result.write(RtfParagraphStyle.ALIGN_LEFT);
                    break;
                case Element.ALIGN_RIGHT:
                    result.write(RtfParagraphStyle.ALIGN_RIGHT);
                    break;
                case Element.ALIGN_CENTER:
                    result.write(RtfParagraphStyle.ALIGN_CENTER);
                    break;
                case Element.ALIGN_JUSTIFIED:
                case Element.ALIGN_JUSTIFIED_ALL:
                    result.write(RtfParagraphStyle.ALIGN_JUSTIFY);
                    break;
            }
            //.result.write(writeIndentations());
            writeIndentations(result);
            result.write(RtfFont.FONT_SIZE);
            result.write(intToByteArray(fontNumber.getFontSize() * 2));
            if(this.symbolIndent > 0) {
                result.write("\\tx".getBytes());
                result.write(intToByteArray(this.leftIndent));
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }

    /**
     * Writes only the list number and list level number.
     * 
     * @return The list number and list level number of this RtfList.
     */
    protected byte[] writeListNumbers() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(RtfListTable.LIST_NUMBER);
            result.write(intToByteArray(listNumber));
            if(listLevel > 0) {
                result.write(LIST_LEVEL_NUMBER);
                result.write(intToByteArray(listLevel));
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    
    /**
     * Writes the content of the RtfList
     * 
     * @return A byte array containing the actual content of the RtfList
     * @deprecated replaced by {@link #writeContent(OutputStream)}
     */
    public byte[] write()  
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
        	writeContent(result);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    
    
    /**
     * Writes the content of the RtfList
     */    
    public void writeContent(final OutputStream result) throws IOException
    {
		result.write(writeListBeginning());
        result.write(writeListNumbers());
        result.write(OPEN_GROUP);
        int itemNr = 0;
        for(int i = 0; i < items.size(); i++) {
            RtfElement rtfElement = (RtfElement) items.get(i);
            if(rtfElement instanceof RtfListItem) {
                itemNr++;
                result.write(OPEN_GROUP);
                result.write(LIST_TEXT);
                result.write(RtfParagraph.PARAGRAPH_DEFAULTS);
                if(this.inTable) {
                    result.write(RtfParagraph.IN_TABLE);
                }
                result.write(RtfFontList.FONT_NUMBER);
                if(this.listType != LIST_TYPE_BULLET) {
                    result.write(intToByteArray(fontNumber.getFontNumber()));
                } else {
                    result.write(intToByteArray(fontBullet.getFontNumber()));
                }
                //.result.write(writeIndentations());
                writeIndentations(result);
                result.write(DELIMITER);
                if(this.listType != LIST_TYPE_BULLET) {
                    switch(this.listType) {
                        case LIST_TYPE_NUMBERED      : result.write(intToByteArray(itemNr)); break;
                        case LIST_TYPE_UPPER_LETTERS : result.write(RomanAlphabetFactory.getUpperCaseString(itemNr).getBytes()); break;
                        case LIST_TYPE_LOWER_LETTERS : result.write(RomanAlphabetFactory.getLowerCaseString(itemNr).getBytes()); break;
                        case LIST_TYPE_UPPER_ROMAN   : result.write(RomanNumberFactory.getUpperCaseString(itemNr).getBytes()); break;
                        case LIST_TYPE_LOWER_ROMAN   : result.write(RomanNumberFactory.getLowerCaseString(itemNr).getBytes()); break;
                    }
                    result.write(LIST_NUMBER_END);
                } else {
                    result.write(LIST_BULLET);
                }
                result.write(TAB);
                result.write(CLOSE_GROUP);                
                //.result.write(rtfElement.write());
                rtfElement.writeContent(result);
                if(i < (items.size() - 1) || !this.inTable || this.listLevel > 0) {
                    result.write(RtfParagraph.PARAGRAPH);
                }
                if(((RtfListItem) rtfElement).isContainsInnerList()) {
                    result.write(writeListNumbers());
                }
                result.write("\n".getBytes());
            } else if(rtfElement instanceof RtfList) {
                //.result.write(rtfElement.write());
            	rtfElement.writeContent(result);
        		result.write(writeListBeginning());
                result.write(writeListNumbers());
                result.write("\n".getBytes());
            }
        }
        result.write(CLOSE_GROUP);
        
        if(!this.inTable) {
            result.write(RtfParagraph.PARAGRAPH_DEFAULTS);
        }
    }        
    
    /**
     * Gets the list level of this RtfList
     * 
     * @return Returns the list level.
     */
    public int getListLevel() {
        return listLevel;
    }
    
    /**
     * Sets the list level of this RtfList. A list level > 0 will
     * unregister this RtfList from the RtfListTable
     * 
     * @param listLevel The list level to set.
     */
    public void setListLevel(int listLevel) {
        this.listLevel = listLevel;
        if(this.listLevel != 0) {
            document.getDocumentHeader().freeListNumber(this);
            for(int i = 0; i < this.items.size(); i++) {
                if(this.items.get(i) instanceof RtfList) {
                    ((RtfList) this.items.get(i)).setListNumber(this.listNumber);
                    ((RtfList) this.items.get(i)).setListLevel(this.listLevel + 1);
                }
            }
        } else {
            this.listNumber = document.getDocumentHeader().getListNumber(this);
        }
    }
    
    /**
     * Sets the parent RtfList of this RtfList
     * 
     * @param parent The parent RtfList to use.
     */
    protected void setParent(RtfList parent) {
        this.parentList = parent;
    }
    
    /**
     * Gets the id of this list
     * 
     * @return Returns the list number.
     */
    public int getListNumber() {
        return listNumber;
    }
    
    /**
     * Sets the id of this list
     * 
     * @param listNumber The list number to set.
     */
    public void setListNumber(int listNumber) {
        this.listNumber = listNumber;
    }
    
    /**
     * Sets whether this RtfList is in a table. Sets the correct inTable setting for all
     * child elements.
     * 
     * @param inTable <code>True</code> if this RtfList is in a table, <code>false</code> otherwise
     */
    public void setInTable(boolean inTable) {
        super.setInTable(inTable);
        for(int i = 0; i < this.items.size(); i++) {
            ((RtfBasicElement) this.items.get(i)).setInTable(inTable);
        }
    }
    
    /**
     * Sets whether this RtfList is in a header. Sets the correct inTable setting for all
     * child elements.
     * 
     * @param inHeader <code>True</code> if this RtfList is in a header, <code>false</code> otherwise
     */
    public void setInHeader(boolean inHeader) {
        super.setInHeader(inHeader);
        for(int i = 0; i < this.items.size(); i++) {
            ((RtfBasicElement) this.items.get(i)).setInHeader(inHeader);
        }
    }

    /**
     * Correct the indentation of this RtfList by adding left/first line indentation
     * from the parent RtfList. Also calls correctIndentation on all child RtfLists.
     */
    protected void correctIndentation() {
        if(this.parentList != null) {
            this.leftIndent = this.leftIndent + this.parentList.getLeftIndent() + this.parentList.getFirstIndent();
        }
        for(int i = 0; i < this.items.size(); i++) {
            if(this.items.get(i) instanceof RtfList) {
                ((RtfList) this.items.get(i)).correctIndentation();
            } else if(this.items.get(i) instanceof RtfListItem) {
                ((RtfListItem) this.items.get(i)).correctIndentation();
            }
        }
    }

    /**
     * Get the left indentation of this RtfList.
     * 
     * @return The left indentation.
     */
    private int getLeftIndent() {
        return this.leftIndent;
    }
    
    /**
     * Get the first line indentation of this RtfList.
     * 
     * @return The first line indentation.
     */
    private int getFirstIndent() {
        return this.firstIndent;
    }
}

/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002, 2003, 2004 by Mark Hall
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
import java.util.Vector;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.List;
import com.lowagie.text.ListItem;
import com.lowagie.text.rtf.RtfBasicElement;
import com.lowagie.text.rtf.RtfElement;
import com.lowagie.text.rtf.RtfExtendedElement;
import com.lowagie.text.rtf.style.RtfFont;
import com.lowagie.text.rtf.style.RtfFontList;
import com.lowagie.text.rtf.text.RtfParagraph;
import com.lowagie.text.rtf.document.RtfDocument;


/**
 * The RtfList stores one List. It also provides the methods to write the
 * list declaration and the list data.
 *  
 * @version $Version:$
 * @author Mark Hall (mhall@edu.uni-klu.ac.at)
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
    
    /**
     * The subitems of this RtfList
     */
    private Vector items;
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
    private boolean numbered = true;
    /**
     * The RtfFont for numbered lists
     */
    private RtfFont fontNumber;
    /**
     * The RtfFont for bulleted lists
     */
    private RtfFont fontBullet;
    
    /**
     * Constructs a new RtfList for the specified List.
     * 
     * @param doc The RtfDocument this RtfList belongs to
     * @param list The List this RtfList is based on
     */
    public RtfList(RtfDocument doc, List list) {
        super(doc);
        
        listNumber = document.getDocumentHeader().getListNumber(this);
        
        items = new Vector();
        firstIndent = (int) (list.indentationLeft() * RtfElement.TWIPS_FACTOR * -1);
        leftIndent = (int) ((list.indentationLeft() + list.symbolIndent()) * RtfElement.TWIPS_FACTOR);
        rightIndent = (int) (list.indentationRight() * RtfElement.TWIPS_FACTOR);
        this.symbolIndent = (int) (list.symbolIndent() * RtfElement.TWIPS_FACTOR);
        numbered = list.isNumbered();
        
        for(int i = 0; i < list.getItems().size(); i++) {
            try {
                Element element = (Element) list.getItems().get(i);
                if(element.type() == Element.CHUNK) {
                    element = new ListItem((Chunk) element);
                }
                RtfBasicElement rtfElement = doc.getMapper().mapElement(element);
                if(rtfElement instanceof RtfList) {
                    ((RtfList) rtfElement).setListNumber(listNumber);
                    ((RtfList) rtfElement).setListLevel(listLevel + 1);
                }
                items.add(rtfElement);
            } catch(DocumentException de) {
                de.printStackTrace();
            }
        }
        
        fontNumber = new RtfFont(document, new Font(Font.TIMES_ROMAN, 10, Font.NORMAL, new Color(0, 0, 0)), 0);
        fontBullet = new RtfFont(document, new Font(Font.SYMBOL, 10, Font.NORMAL, new Color(0, 0, 0)), 0);
    }
    
    private byte[] writeIndentations() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(LIST_LEVEL_FIRST_INDENT);
            result.write(intToByteArray(firstIndent));
            result.write(RtfParagraph.INDENT_LEFT);
            result.write(intToByteArray(leftIndent));
            result.write(RtfParagraph.INDENT_RIGHT);
            result.write(intToByteArray(rightIndent));
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }
    
    /**
     * Writes the definition part of this list level
     * 
     * @return A byte array containing the definition of this list level
     */
    public byte[] writeDefinition() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(OPEN_GROUP);
            result.write(LIST_LEVEL);
            result.write(LIST_LEVEL_TYPE);
            if(numbered) {
                result.write(intToByteArray(0));
            } else {
                result.write(intToByteArray(23));
            }
            result.write(LIST_LEVEL_TYPE_NEW);
            if(numbered) {
                result.write(intToByteArray(0));
            } else {
                result.write(intToByteArray(23));
            }
            result.write(LIST_LEVEL_ALIGNMENT);
            result.write(intToByteArray(0));
            result.write(LIST_LEVEL_ALIGNMENT_NEW);
            result.write(intToByteArray(0));
            result.write(LIST_LEVEL_START_AT);
            result.write(intToByteArray(1));
            result.write(OPEN_GROUP);
            result.write(LIST_LEVEL_TEXT);
            if(numbered) {
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
            if(numbered) {
                result.write(LIST_LEVEL_NUMBERS_NUMBERED);
            }
            result.write(LIST_LEVEL_NUMBERS_END);
            result.write(CLOSE_GROUP);
            result.write(RtfFontList.FONT_NUMBER);
            if(numbered) {
                result.write(intToByteArray(fontNumber.getFontNumber()));
            } else {
                result.write(intToByteArray(fontBullet.getFontNumber()));
            }
            result.write(writeIndentations());
            result.write(LIST_LEVEL_SYMBOL_INDENT);
            result.write(intToByteArray(symbolIndent));
            result.write(CLOSE_GROUP);
            result.write("\n".getBytes());
            for(int i = 0; i < items.size(); i++) {
                RtfElement rtfElement = (RtfElement) items.get(i);
                if(rtfElement instanceof RtfList) {
                    result.write(((RtfList) rtfElement).writeDefinition());
                    break;
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
    }

    /**
     * Writes the initialisation part of the RtfList
     * 
     * @return A byte array containing the initialisation part
     */
    private byte[] writeListBeginning() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(RtfParagraph.PARAGRAPH_DEFAULTS);
            result.write(RtfParagraph.ALIGN_LEFT);
            result.write(writeIndentations());
            result.write(RtfFont.FONT_SIZE);
            result.write(intToByteArray(fontNumber.getFontSize() * 2));
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
     */
    public byte[] write()  {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            result.write(writeListBeginning());
            result.write(OPEN_GROUP);
            int itemNr = 0;
            for(int i = 0; i < items.size(); i++) {
                RtfElement rtfElement = (RtfElement) items.get(i);
                if(rtfElement instanceof RtfListItem) {
                    itemNr++;
                    result.write(OPEN_GROUP);
                    result.write(LIST_TEXT);
                    result.write(RtfParagraph.PARAGRAPH_DEFAULTS);
                    result.write(RtfFontList.FONT_NUMBER);
                    if(numbered) {
                        result.write(intToByteArray(fontNumber.getFontNumber()));
                    } else {
                        result.write(intToByteArray(fontBullet.getFontNumber()));
                    }
                    result.write(writeIndentations());
                    result.write(DELIMITER);
                    if(numbered) {
                        result.write(this.intToByteArray(itemNr));
                        result.write(LIST_NUMBER_END);
                    } else {
                        result.write(LIST_BULLET);
                    }
                    result.write(TAB);
                    result.write(CLOSE_GROUP);
                    result.write(rtfElement.write());
                    result.write("\n".getBytes());
                } else if(rtfElement instanceof RtfList) {
                    result.write(rtfElement.write());
                    result.write(writeListBeginning());
                    result.write("\n".getBytes());
                }
            }
            result.write(CLOSE_GROUP);
            result.write(RtfParagraph.PARAGRAPH_DEFAULTS);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return result.toByteArray();
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
        } else {
            this.listNumber = document.getDocumentHeader().getListNumber(this);
        }
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
     * @param isInTable <code>True</code> if this RtfList is in a table, <code>false</code> otherwise
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
     * @param isInHeader <code>True</code> if this RtfList is in a header, <code>false</code> otherwise
     */
    public void setInHeader(boolean inHeader) {
        super.setInHeader(inHeader);
        for(int i = 0; i < this.items.size(); i++) {
            ((RtfBasicElement) this.items.get(i)).setInHeader(inHeader);
        }
    }
}

/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Bruno Lowagie.
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

package com.lowagie.text.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Stack;

import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;

import com.lowagie.text.*;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 */

public class SAXiTextHandler extends HandlerBase {
    
/** This is the resulting document. */
    protected DocListener document;
    
/** This is a <CODE>Stack</CODE> of objects, waiting to be added to the document. */
    protected Stack stack;
    
/** Counts the number of chapters in this document. */
    protected int chapters = 0;
    
/** This is the current chunk to which characters can be added. */
    protected Chunk currentChunk = null;
    
/** This is the current chunk to which characters can be added. */
    protected boolean ignore = false;
    
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 */
    
    public SAXiTextHandler(DocListener document) {
        super();
        this.document = document;
        stack = new Stack();
    }
    
/**
 * This method gets called when a start tag is encountered.
 *
 * @param	name		the name of the tag that is encountered
 * @param	attrs		the list of attributes
 */
    
    public void startElement(String name, AttributeList attrs) {
        
        Properties attributes = new Properties();
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String attribute = attrs.getName(i);
                attributes.setProperty(attribute, attrs.getValue(i));
            }
        }
        handleStartingTags(name, attributes);
    }
    
/**
 * This method deals with the starting tags.
 *
 * @param       name        the name of the tag
 * @param       attributes  the list of attributes
 */
    
    public void handleStartingTags(String name, Properties attributes) {
        //System.err.println("Start: " + name);
        
        if (ignore || ElementTags.IGNORE.equals(name)) {
            ignore = true;
            return;
        }
        
        // maybe there is some meaningful data that wasn't between tags
        if (currentChunk != null) {
            TextElementArray current;
            try {
                current = (TextElementArray) stack.pop();
            }
            catch(EmptyStackException ese) {
                current = new Paragraph();
            }
            current.add(currentChunk);
            stack.push(current);
            currentChunk = null;
        }
        
        // chunks
        if (Chunk.isTag(name)) {
            currentChunk = new Chunk(attributes);
            return;
        }
        
        // symbols
        if (Entities.isTag(name)) {
            Font f = new Font();
            if (currentChunk != null) {
                handleEndingTags(ElementTags.CHUNK);
                f = currentChunk.font();
            }
            currentChunk = Entities.get(attributes.getProperty(ElementTags.ID), f);
            return;
        }

        // phrases
        if (Phrase.isTag(name)) {
            stack.push(new Phrase(attributes));
            return;
        }
        
        // anchors
        if (Anchor.isTag(name)) {
            stack.push(new Anchor(attributes));
            return;
        }
        
        // paragraphs and titles
        if (Paragraph.isTag(name) || Section.isTitle(name)) {
            stack.push(new Paragraph(attributes));
            return;
        }
        
        // lists
        if (List.isTag(name)) {
            stack.push(new List(attributes));
            return;
        }
        
        // listitems
        if (ListItem.isTag(name)) {
            stack.push(new ListItem(attributes));
            return;
        }
        
        // cells
        if (Cell.isTag(name)) {
            stack.push(new Cell(attributes));
            return;
        }
        
        // tables
        if (Table.isTag(name)) {
            Table table = new Table(attributes);
            float widths[] = table.getProportionalWidths();
            for (int i = 0; i < widths.length; i++) {
                if (widths[i] == 0) {
                    widths[i] = 100.0f / (float)widths.length;
                }
            }
            try {
                table.setWidths(widths);
            }
            catch(BadElementException bee) {
                // this shouldn't happen
                throw new ExceptionConverter(bee);
            }
            stack.push(table);
            return;
        }
        
        // sections
        if (Section.isTag(name)) {
            Element previous = (Element) stack.pop();
            Section section;
            try {
                section = ((Section)previous).addSection(attributes);
            }
            catch(ClassCastException cce) {
                throw new ExceptionConverter(cce);
            }
            stack.push(previous);
            stack.push(section);
            return;
        }
        
        // chapters
        if (Chapter.isTag(name)) {
            String value; // changed after a suggestion by Serge S. Vasiljev
            if ((value = (String)attributes.remove(ElementTags.NUMBER)) != null){
                 chapters = Integer.parseInt(value);
             }
            else {
               chapters++;
            }
            Chapter chapter = new Chapter(attributes,chapters);
            stack.push(chapter);
            return;
        }
        
        // images
        if (Image.isTag(name)) {
            try {
                Image img = Image.getInstance(attributes);
                TextElementArray current;
                try {
                    // if there is an element on the stack...
                    current = (TextElementArray) stack.pop();
                    // ...and it's a Chapter or a Section, the Image can be added directly
                    if ((current instanceof Chapter) || (current instanceof Section) || (current instanceof Cell)) {
                        current.add(img);
                        stack.push(current);
                        return;
                    }
                    // ...if not, the Image is wrapped in a Chunk before it's added
                    else {
                        Chunk chunk = new Chunk(img, 0, 0);
                        current.add(chunk);
                        stack.push(current);
                        return;
                    }
                }
                catch(EmptyStackException ese) {
                    // if there is no element on the stack, the Image is added to the document
                    try {
                        document.add(img);
                    }
                    catch(DocumentException de) {
                        throw new ExceptionConverter(de);
                    }
                    return;
                }
            }
            catch(Exception e) {
                throw new ExceptionConverter(e);
            }
        }
        
        // annotations
        if (Annotation.isTag(name)) {
            Annotation annotation = new Annotation(attributes);
            TextElementArray current;
            try {
                try {
                    current = (TextElementArray) stack.pop();
                    try {
                        current.add(annotation);
                    }
                    catch(Exception e) {
                        document.add(annotation);
                    }
                    stack.push(current);
                }
                catch(EmptyStackException ese) {
                    document.add(annotation);
                }
                return;
            }
            catch(DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }
        
        // newlines
        if (isNewline(name)) {
            TextElementArray current;
            try {
                current = (TextElementArray) stack.pop();
                current.add(Chunk.NEWLINE);
                stack.push(current);
            }
            catch(EmptyStackException ese) {
                if (currentChunk == null) {
                    try {
                        document.add(Chunk.NEWLINE);
                    }
                    catch(DocumentException de) {
                        throw new ExceptionConverter(de);
                    }
                }
                else {
                    currentChunk.append("\n");
                }
            }
            return;
        }
        
        // newpage
        if (isNewpage(name)) {
            TextElementArray current;
            try {
                current = (TextElementArray) stack.pop();
                Chunk newPage = new Chunk("");
                newPage.setNewPage();
                current.add(newPage);
                stack.push(current);
            }
            catch(EmptyStackException ese) {
                try {
                    document.newPage();
                }
                catch(DocumentException de) {
                    throw new ExceptionConverter(de);
                }
            }
            return;
        }
        
        // newpage
        if (ElementTags.HORIZONTALRULE.equals(name)) {
            TextElementArray current;
            Graphic hr = new Graphic();
            hr.setHorizontalLine(1.0f, 100.0f);
            try {
                current = (TextElementArray) stack.pop();
                current.add(hr);
                stack.push(current);
            }
            catch(EmptyStackException ese) {
                try {
                    document.add(hr);
                }
                catch(DocumentException de) {
                    throw new ExceptionConverter(de);
                }
            }
            return;
        }
        
        // documentroot
        if (isDocumentRoot(name)) {
            String key;
            String value;
            for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
                key = (String) i.next();
                value = attributes.getProperty(key);
                try {
                    document.add(new Meta(key, value));
                }
                catch(DocumentException de) {
                    throw new ExceptionConverter(de);
                }
            }
            document.open();
        }
    }
    
/**
 * This method gets called when ignorable white space encountered.
 *
 * @param	ch		an array of characters
 * @param	start	the start position in the array
 * @param	length	the number of characters to read from the array
 */
    
    public void ignorableWhitespace(char[] ch, int start, int length) {
        // do nothing: we handle white space ourselves in the characters method
    }
    
/**
 * This method gets called when characters are encountered.
 *
 * @param	ch		an array of characters
 * @param	start	the start position in the array
 * @param	length	the number of characters to read from the array
 */
    
    public void characters(char[] ch, int start, int length) {
        
        if (ignore) return;
        
        String content = new String(ch, start, length);
        //System.err.println("'" + content + "'");
        
        if (content.trim().length() == 0) {
            return;
        }
        
        StringBuffer buf = new StringBuffer();
        int len = content.length();
        char character;
        boolean newline = false;
        for (int i = 0; i < len; i++) {
            switch(character = content.charAt(i)) {
                case ' ':
                    if (!newline) {
                        buf.append(character);
                    }
                    break;
                case '\n':
                    if (i > 0) {
                        newline = true;
                        buf.append(' ');
                    }
                    break;
                case '\r':
                    break;
                case '\t':
                    break;
                    default:
                        newline = false;
                        buf.append(character);
            }
        }
        if (currentChunk == null) {
            currentChunk = new Chunk(buf.toString());
        }
        else {
            currentChunk.append(buf.toString());
        }
    }
    
/**
 * This method gets called when an end tag is encountered.
 *
 * @param	name		the name of the tag that ends
 */
    
    public void endElement(String name) {
        handleEndingTags(name);
    }
    
/**
 * This method deals with the starting tags.
 *
 * @param       name        the name of the tag
 * @param       attributes  the list of attributes
 */
    
    public void handleEndingTags(String name) {
        
        //System.err.println("Stop: " + name);
        
        if (ElementTags.IGNORE.equals(name)) {
            ignore = false;
            return;
        }
        if (ignore) return;
        // tags that don't have any content
        if (isNewpage(name) || Annotation.isTag(name) || Image.isTag(name) || isNewline(name)) {
            return;
        }
        
        try {
            // titles of sections and chapters
            if (Section.isTitle(name)) {
                Paragraph current = (Paragraph) stack.pop();
                if (currentChunk != null) {
                    current.add(currentChunk);
                    currentChunk = null;
                }
                Section previous = (Section) stack.pop();
                previous.setTitle(current);
                stack.push(previous);
                return;
            }
            
            // all other endtags
            if (currentChunk != null) {
                TextElementArray current;
                try {
                    current = (TextElementArray) stack.pop();
                }
                catch(EmptyStackException ese) {
                    current = new Paragraph();
                }
                current.add(currentChunk);
                stack.push(current);
                currentChunk = null;
            }
            
            // chunks
            if (Chunk.isTag(name)) {
                return;
            }
            
            // phrases, anchors, lists, tables
            if (Phrase.isTag(name) || Anchor.isTag(name) || List.isTag(name) || Paragraph.isTag(name)) {
                Element current = (Element) stack.pop();
                try {
                    TextElementArray previous = (TextElementArray) stack.pop();
                    previous.add(current);
                    stack.push(previous);
                }
                catch(EmptyStackException ese) {
                    document.add(current);
                }
                return;
            }
            
            // listitems
            if (ListItem.isTag(name)) {
                ListItem listItem = (ListItem) stack.pop();
                List list = (List) stack.pop();
                list.add(listItem);
                stack.push(list);
            }
            
            // tables
            if (Table.isTag(name)) {
                Table table = (Table) stack.pop();           
                try {
                    TextElementArray previous = (TextElementArray) stack.pop(); 
                    previous.add(table);
                    stack.push(previous);
                }
                catch(EmptyStackException ese) {
                    document.add(table);
                }
                return;
            }
            
            // rows
            if (Row.isTag(name)) {
                ArrayList cells = new ArrayList();
                int columns = 0;
                Table table;
                Cell cell;
                while (true) {
                    Element element = (Element) stack.pop();
                    if (element.type() == Element.CELL) {
                        cell = (Cell) element;
                        columns += cell.colspan();
                        cells.add(cell);
                    }
                    else {
                        table = (Table) element;
                        break;
                    }
                }
                if (table.columns() < columns) {
                    table.addColumns(columns - table.columns());
                }
                Collections.reverse(cells);
                String width;
                float[] cellWidths = new float[columns];
                boolean[] cellNulls = new boolean[columns];
                for (int i = 0; i < columns; i++) {
                    cellWidths[i] = 0;
                    cellNulls[i] = true;
                }
                float total = 0;
                int j = 0;
                for (Iterator i = cells.iterator(); i.hasNext(); ) {
                    cell = (Cell) i.next();
                    if ((width = cell.cellWidth()) == null) {
                        if (cell.colspan() == 1 && cellWidths[j] == 0) {
                            try {
                                cellWidths[j] = 100f / columns;
                                total += cellWidths[j];
                            }
                            catch(Exception e) {
                                // empty on purpose
                            }
                        }
                        else if (cell.colspan() == 1) {
                            cellNulls[j] = false;
                        }
                    }
                    else if (cell.colspan() == 1 && width.endsWith("%")) {
                        try {
                            cellWidths[j] = Float.valueOf(width.substring(0, width.length() - 1) + "f").floatValue();
                            total += cellWidths[j];
                        }
                        catch(Exception e) {
                            // empty on purpose
                        }
                    }
                    j += cell.colspan();
                    table.addCell(cell);
                }
                float widths[] = table.getProportionalWidths();
                if (widths.length == columns) {
                    float left = 0.0f;
                    for (int i = 0; i < columns; i++) {
                        if (cellNulls[i] && widths[i] != 0) {
                            left += widths[i];
                            cellWidths[i] = widths[i];
                        }
                    }
                    if (100.0 >= total) {
                        for (int i = 0; i < widths.length; i++) {
                            if (cellWidths[i] == 0 && widths[i] != 0) {
                                cellWidths[i] = (widths[i] / left) * (100.0f - total);
                            }
                        }
                    }
                    table.setWidths(cellWidths);
                }
                stack.push(table);
            }
            
            // cells
            if (Cell.isTag(name)) {
                return;
            }
            
            // sections
            if (Section.isTag(name)) {
                stack.pop();
                return;
            }
            
            // chapters
            if (Chapter.isTag(name)) {
                document.add((Element) stack.pop());
                return;
            }
            
            // the documentroot
            if (isDocumentRoot(name)) {
                try {
                    while (true) {
                        Element element = (Element) stack.pop();
                        try {
                            TextElementArray previous = (TextElementArray) stack.pop();
                            previous.add(element);
                            stack.push(previous);
                        }
                        catch(EmptyStackException es) {
                            document.add(element);
                        }
                    }
                }
                catch(EmptyStackException ese) {
                    // empty on purpose
                }
                document.close();
                return;
            }
        }
        catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }
    
/**
 * Checks if a certain tag corresponds with the newpage-tag.
 *
 * @param	tag			a presumed tagname
 * @return	<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    private boolean isNewpage(String tag) {
        return ElementTags.NEWPAGE.equals(tag);
    }
    
/**
 * Checks if a certain tag corresponds with the newpage-tag.
 *
 * @param	tag			a presumed tagname
 * @return	<CODE>true</CODE> or <CODE>false</CODE>
 */
    
    private boolean isNewline(String tag) {
        return ElementTags.NEWLINE.equals(tag);
    }
    
/**
 * Checks if a certain tag corresponds with the roottag.
 *
 * @param	tag			a presumed tagname
 * @return	<CODE>true</CODE> if <VAR>tag</VAR> equals <CODE>itext</CODE>, <CODE>false</CODE> otherwise.
 */
    
    protected boolean isDocumentRoot(String tag) {
        return ElementTags.ITEXT.equals(tag);
    }
}
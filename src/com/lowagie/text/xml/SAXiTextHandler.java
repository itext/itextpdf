/*
 * $Id$
 * $Name$
 *
 * Copyright (c) 2000, 2001 Bruno Lowagie.
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

package com.lowagie.text.xml;

import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Properties;

import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;

import com.lowagie.text.*;

/**
 * The <CODE>Tags</CODE>-class maps several XHTML-tags to iText-objects.
 *
 * @author  bruno@lowagie.com
 */

public class SAXiTextHandler extends HandlerBase {
    
/** This is the resulting document. */
    private Document document;
    
/** This is a <CODE>Stack</CODE> of objects, waiting to be added to the document. */
    private Stack stack;
    
/** Counts the number of chapters in this document. */
    private int chapters = 0;
    
/** This is the current chunk to which characters can be added. */
    private Chunk currentChunk = null;
    
/**
 * Constructs a new SAXiTextHandler that will translate all the events
 * triggered by the parser to actions on the <CODE>Document</CODE>-object.
 *
 * @param	document	this is the document on which events must be triggered
 */
    
    public SAXiTextHandler(Document document) {
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
        attributes.setProperty(ElementTags.TAGNAME, name);
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
        
        // tables
        if (Table.isTag(name)) {
            try {
                stack.push(new Table(attributes));
                return;
            }
            catch(BadElementException bee) {
                throw new RuntimeException(bee.getMessage());
            }
        }
        
        // cells
        if (Cell.isTag(name)) {
            stack.push(new Cell(attributes));
            return;
        }
        
        // sections
        if (Section.isTag(name)) {
            Element previous = (Element) stack.pop();
            Section section = ((Section)previous).addSection(attributes);
            stack.push(previous);
            stack.push(section);
            return;
        }
        
        // chapters
        if (Chapter.isTag(name)) {
            Chapter chapter = new Chapter(attributes, ++chapters);
            stack.push(chapter);
            return;
        }
        
        // images
        if (Image.isTag(name)) {
            try {
                Image img = Image.getInstance(attributes);
                Chunk chunk = new Chunk(img, 0, 0);
                TextElementArray current;
                try {
                    current = (TextElementArray) stack.pop();
                }
                catch(EmptyStackException ese) {
                    current = new Paragraph();
                }
                current.add(chunk);
                stack.push(current);
            }
            catch(Exception e) {
                System.err.println(e.toString());
            }
            return;
        }
        
        // annotations
        if (Annotation.isTag(name)) {
            TextElementArray current;
            try {
                current = (TextElementArray) stack.pop();
                current.add(new Annotation(attributes));
                stack.push(current);
            }
            catch(EmptyStackException ese) {
                try {
                    document.add(new Annotation(attributes));
                }
                catch(DocumentException de) {
                }
            }
            return;
        }
        
        // newlines
        if (isNewline(name)) {
            TextElementArray current;
            try {
                current = (TextElementArray) stack.pop();
                current.add(new Chunk("\n"));
                stack.push(current);
            }
            catch(EmptyStackException ese) {
                if (currentChunk == null) {
                    currentChunk = new Chunk("\n");
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
                }
            }
            return;
        }
        
        // documentroot
        if (isDocumentRoot(name)) {
            String value;
            if ((value = attributes.getProperty(ElementTags.TITLE)) != null) {
                document.addTitle(value);
            }
            if ((value = attributes.getProperty(ElementTags.SUBJECT)) != null) {
                document.addSubject(value);
            }
            if ((value = attributes.getProperty(ElementTags.KEYWORDS)) != null) {
                document.addKeywords(value);
            }
            if ((value = attributes.getProperty(ElementTags.AUTHOR)) != null) {
                document.addAuthor(value);
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
        // do nothing
    }
    
/**
 * This method gets called when characters are encountered.
 *
 * @param	ch		an array of characters
 * @param	start	the start position in the array
 * @param	length	the number of characters to read from the array
 */
    
    public void characters(char[] ch, int start, int length) {
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
                    newline = true;
                    buf.append(' ');
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
        
        //System.err.println("End: " + name);
        
        try {
            // tags that don't have any content
            if (isNewpage(name) || Annotation.isTag(name) || Image.isTag(name) || isNewline(name)) {
                return;
            }
            
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
            
            // chunks or other endtags
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
            
            // phrases, anchors, lists, tables
            if (Phrase.isTag(name) || Anchor.isTag(name) || List.isTag(name) || Table.isTag(name) || Paragraph.isTag(name)) {
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
            
            // cells
            if (Cell.isTag(name)) {
                Cell cell = (Cell) stack.pop();
                Table table = (Table) stack.pop();
                table.addCell(cell);
                stack.push(table);
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
                }
                document.close();
                return;
            }
        }
        catch(DocumentException de) {
            de.printStackTrace();
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
    
    private boolean isDocumentRoot(String tag) {
        return ElementTags.ITEXT.equals(tag);
    }
}
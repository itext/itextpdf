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
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE 
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU LIBRARY GENERAL PUBLIC LICENSE for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.xml;

import java.awt.Color;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.HashMap;

import com.lowagie.text.*;
import com.lowagie.text.MarkupAttributes;

/**
 * A <CODE>DocWriter</CODE> class for XML (Remark: this class is not finished yet!).
 * <P>
 * An <CODE>XmlWriter</CODE> can be added as a <CODE>DocListener</CODE>
 * to a certain <CODE>Document</CODE> by getting an instance.
 * Every <CODE>Element</CODE> added to the original <CODE>Document</CODE>
 * will be written to the <CODE>OutputStream</CODE> of this <CODE>XmlWriter</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * // creation of the document with a certain size and certain margins
 * Document document = new Document(PageSize.A4, 50, 50, 50, 50);
 * try {
 *    // this will write XML to the Standard OutputStream
 *    <STRONG>XmlWriter.getInstance(document, System.out);</STRONG>
 *    // this will write XML to a file called text.html
 *    <STRONG>XmlWriter.getInstance(document, new FileOutputStream("text.xml"));</STRONG>
 *    // this will write XML to for instance the OutputStream of a HttpServletResponse-object
 *    <STRONG>XmlWriter.getInstance(document, response.getOutputStream());</STRONG>
 * }
 * catch(DocumentException de) {
 *    System.err.println(de.getMessage());
 * }
 * // this will close the document and all the OutputStreams listening to it
 * <STRONG>document.close();</CODE>
 * </PRE></BLOCKQUOTE>
 */

public class XmlWriter extends DocWriter implements DocListener {
    
    // static membervariables (tags)
    
/** This is the first line of the XML page. */
    public static final byte[] PROLOG = getISOBytes("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    
/** This is the reference to the DTD. */
    public static final byte[] DOCTYPE = getISOBytes("<!DOCTYPE ITEXT SYSTEM \"");
    
/** This is the place where the DTD is located. */
    public final static byte[] DTD = getISOBytes("http://www.lowagie.com/iText/itext.dtd");
    
/** This is an array containing character to XML translations. */
    private static final String[] xmlCode = new String[256];
    
    static {
        for (int i = 0; i < 10; i++) {
            xmlCode[i] = "&#00" + i + ";";
        }
        
        for (int i = 10; i < 32; i++) {
            xmlCode[i] = "&#0" + i + ";";
        }
        
        for (int i = 32; i < 128; i++) {
            xmlCode[i] = String.valueOf((char)i);
        }
        
        // Special characters
        xmlCode['\n'] = "<" + ElementTags.NEWLINE + " />\n";
        xmlCode['\"'] = "&quot;"; // double quote
        xmlCode['\''] = "&apos;"; // single quote
        xmlCode['&'] = "&amp;"; // ampersand
        xmlCode['<'] = "&lt;"; // lower than
        xmlCode['>'] = "&gt;"; // greater than
        
        for (int i = 128; i < 256; i++) {
            xmlCode[i] = "&#" + i + ";";
        }
    }
    // membervariables
    
/** This is the meta information of the document. */
    private TreeMap itext = new TreeMap(new com.lowagie.text.StringCompare());
    
    // constructors
    
/**
 * Constructs an <CODE>XmlWriter</CODE>.
 *
 * @param document  The <CODE>Document</CODE> that has to be written as XML
 * @param os      The <CODE>OutputStream</CODE> the writer has to write to.
 */
    
    protected XmlWriter(Document doc, OutputStream os) {
        super(doc, os);
        
        document.addDocListener(this);
        try {
            os.write(PROLOG);
            os.write(DOCTYPE);
            os.write(DTD);
            os.write(QUOTE);
            os.write(GT);
            os.write(NEWLINE);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Constructs an <CODE>XmlWriter</CODE>.
 *
 * @param document  The <CODE>Document</CODE> that has to be written as XML
 * @param os      The <CODE>OutputStream</CODE> the writer has to write to.
 * @param   dtd         The DTD to use
 */
    
    protected XmlWriter(Document doc, OutputStream os, String dtd) {
        super(doc, os);
        
        document.addDocListener(this);
        try {
            os.write(PROLOG);
            os.write(DOCTYPE);
            os.write(getISOBytes(dtd));
            os.write(QUOTE);
            os.write(GT);
            os.write(NEWLINE);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    // get an instance of the XmlWriter
    
/**
 * Gets an instance of the <CODE>XmlWriter</CODE>.
 *
 * @param document  The <CODE>Document</CODE> that has to be written
 * @param os  The <CODE>OutputStream</CODE> the writer has to write to.
 * @return  a new <CODE>XmlWriter</CODE>
 */
    
    public static XmlWriter getInstance(Document document, OutputStream os) {
        return new XmlWriter(document, os);
    }
    
/**
 * Gets an instance of the <CODE>XmlWriter</CODE>.
 *
 * @param document  The <CODE>Document</CODE> that has to be written
 * @param os  The <CODE>OutputStream</CODE> the writer has to write to.
 * @param   dtd         The DTD to use
 * @return  a new <CODE>XmlWriter</CODE>
 */
    
    public static XmlWriter getInstance(Document document, OutputStream os, String dtd) {
        return new XmlWriter(document, os, dtd);
    }
    
    // implementation of the DocListener methods
    
/**
 * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
 *
 * @return  <CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
 * @throws  DocumentException when a document isn't open yet, or has been closed
 */
    
    public boolean add(Element element) throws DocumentException {
        if (pause) {
            return false;
        }
        try {
            switch(element.type()) {
                case Element.TITLE:
                    itext.put(ElementTags.TITLE, ((Meta)element).content());
                    return true;
                case Element.SUBJECT:
                    itext.put(ElementTags.SUBJECT, ((Meta)element).content());
                    return true;
                case Element.KEYWORDS:
                    itext.put(ElementTags.KEYWORDS, ((Meta)element).content());
                    return true;
                case Element.AUTHOR:
                    itext.put(ElementTags.AUTHOR, ((Meta)element).content());
                    return true;
                    default:
                        write(element, 1);
                        return true;
            }
        }
        catch(IOException ioe) {
            return false;
        }
    }
    
/**
 * Signals that the <CODE>Document</CODE> has been opened and that
 * <CODE>Elements</CODE> can be added.
 */
    
    public void open() {
        super.open();
        try {
            itext.put(ElementTags.PRODUCER, "iTextXML by lowagie.com");
            itext.put(ElementTags.CREATIONDATE, new Date().toString());
            writeStart(ElementTags.ITEXT);
            String key;
            for (java.util.Iterator i = itext.keySet().iterator(); i.hasNext(); ) {
                key = (String) i.next();
                write(key, (String) itext.get(key));
            }
            os.write(GT);
            os.write(NEWLINE);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Signals that an new page has to be LTed.
 *
 * @return  <CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
 * @throws  DocumentException when a document isn't open yet, or has been closed
 */
    
    public boolean newPage() throws DocumentException {
        if (pause || !open) {
            return false;
        }
        try {
            writeStart(ElementTags.NEWPAGE);
            writeEnd();
            return true;
        }
        catch(IOException ioe) {
            return false;
        }
    }
    
/**
 * Signals that the <CODE>Document</CODE> was closed and that no other
 * <CODE>Elements</CODE> will be added.
 */
    
    public void close() {
        try {
            writeEnd(ElementTags.ITEXT);
            super.close();
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    // methods
    
/**
 * Writes the XML representation of an element.
 *
 * @param   element     the element
 * @param   indent      the indentation
 */
    
    private void write(Element element, int indent) throws IOException {
        switch(element.type()) {
            case Element.CHUNK:
            {
                Chunk chunk = (Chunk) element;
                
                // if the chunk contains an image, return the image representation
                try {
                    Image image = chunk.getImage();
                    write(image, indent);
                    return;
                }
                catch(NullPointerException npe) {
                    // empty on purpose
                }
                
                addTabs(indent);
                HashMap attributes = chunk.getAttributes();
// patch by Matt Benson 02/21/2002
                if (chunk.font().isStandardFont() && attributes == null
                 && !(hasMarkupAttributes(chunk)))
                {
// end patch by Matt Benson 02/21/2002
                    write(encode(chunk.content(), indent));
                    os.write(NEWLINE);
                    return;
                }
                else {
                    if (attributes !=  null && attributes.get(Chunk.NEWPAGE) != null) {
                        writeStart(ElementTags.NEWPAGE);
                        writeEnd();
                        return;
                    }
                    writeStart(ElementTags.CHUNK);
                    if (! chunk.font().isStandardFont()) {
                        write(chunk.font());
                    }
                    if (attributes != null) {
                        for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
                            String key = (String) i.next();
                            if (key.equals(Chunk.LOCALGOTO)
                            || key.equals(Chunk.LOCALDESTINATION)
                            || key.equals(Chunk.GENERICTAG)) {
                                String value = (String) attributes.get(key);
                                write(key.toLowerCase(), value);
                            }
                            if (key.equals(Chunk.SUBSUPSCRIPT)) {
                                write(key.toLowerCase(), String.valueOf((Float) attributes.get(key)));
                            }
                        }
                    }
// patch by Matt Benson 02/21/2002
                    if (hasMarkupAttributes(chunk))
                    {
                      writeMarkupAttributes((MarkupAttributes)chunk);
                    }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                    os.write(GT);
                    write(encode(chunk.content(), indent));
                    writeEnd(ElementTags.CHUNK);
                }
                return;
            }
            case Element.PHRASE:
            {
                Phrase phrase = (Phrase) element;
                
                addTabs(indent);
                writeStart(ElementTags.PHRASE);
                
                write(ElementTags.LEADING, String.valueOf(phrase.leading()));
                write(phrase.font());
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(phrase))
                {
                  writeMarkupAttributes((MarkupAttributes)phrase);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                os.write(GT);
                os.write(NEWLINE);
                
                for (Iterator i = phrase.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                
                addTabs(indent);
                writeEnd(ElementTags.PHRASE);
                return;
            }
            case Element.ANCHOR:
            {
                Anchor anchor = (Anchor) element;
                
                addTabs(indent);
                writeStart(ElementTags.ANCHOR);
                
                write(ElementTags.LEADING, String.valueOf(anchor.leading()));
                write(anchor.font());
                if (anchor.name() != null) {
                    write(ElementTags.NAME, anchor.name());
                }
                if (anchor.reference() != null) {
                    write(ElementTags.REFERENCE, anchor.reference());
                }
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(anchor))
                {
                  writeMarkupAttributes((MarkupAttributes)anchor);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                os.write(GT);
                os.write(NEWLINE);
                for (Iterator i = anchor.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                addTabs(indent);
                writeEnd(ElementTags.ANCHOR);
                return;
            }
            case Element.PARAGRAPH:
            {
                Paragraph paragraph = (Paragraph) element;
                
                addTabs(indent);
                writeStart(ElementTags.PARAGRAPH);
                
                write(ElementTags.LEADING, String.valueOf(paragraph.leading()));
                write(paragraph.font());
                write(ElementTags.ALIGN, ElementTags.getAlignment(paragraph.alignment()));
                if (paragraph.indentationLeft() != 0) {
                    write(ElementTags.INDENTATIONLEFT, String.valueOf(paragraph.indentationLeft()));
                }
                if (paragraph.indentationRight() != 0) {
                    write(ElementTags.INDENTATIONRIGHT, String.valueOf(paragraph.indentationRight()));
                }
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(paragraph))
                {
                  writeMarkupAttributes((MarkupAttributes)paragraph);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                os.write(GT);
                os.write(NEWLINE);
                for (Iterator i = paragraph.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                addTabs(indent);
                writeEnd(ElementTags.PARAGRAPH);
                return;
            }
            case Element.SECTION:
            {
                Section section = (Section) element;
                
                addTabs(indent);
                writeStart(ElementTags.SECTION);
                writeSection(section, indent);
                writeEnd(ElementTags.SECTION);
                return;
            }
            case Element.CHAPTER:
            {
                Chapter chapter = (Chapter) element;
                
                addTabs(indent);
                writeStart(ElementTags.CHAPTER);
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(chapter))
                {
                  writeMarkupAttributes((MarkupAttributes)chapter);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                writeSection(chapter, indent);
                writeEnd(ElementTags.CHAPTER);
                return;
                
            }
            case Element.LIST:
            {
                List list = (List) element;
                
                addTabs(indent);
                writeStart(ElementTags.LIST);
                write(ElementTags.NUMBERED, String.valueOf(list.isNumbered()));
                write(ElementTags.SYMBOLINDENT, String.valueOf(list.symbolIndent()));
                if (list.first() != 1) {
                    write(ElementTags.FIRST, String.valueOf(list.first()));
                }
                if (list.indentationLeft() != 0) {
                    write(ElementTags.INDENTATIONLEFT, String.valueOf(list.indentationLeft()));
                }
                if (list.indentationRight() != 0) {
                    write(ElementTags.INDENTATIONRIGHT, String.valueOf(list.indentationRight()));
                }
                if (!list.isNumbered()) {
                    write(ElementTags.LISTSYMBOL, list.symbol().content());
                }
                write(list.symbol().font());
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(list))
                {
                  writeMarkupAttributes((MarkupAttributes)list);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                os.write(GT);
                os.write(NEWLINE);
                for (Iterator i = list.getItems().iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                addTabs(indent);
                writeEnd(ElementTags.LIST);
                return;
            }
            case Element.LISTITEM:
            {
                ListItem listItem = (ListItem) element;
                
                addTabs(indent);
                writeStart(ElementTags.LISTITEM);
                write(ElementTags.LEADING, String.valueOf(listItem.leading()));
                write(listItem.font());
                write(ElementTags.ALIGN, ElementTags.getAlignment(listItem.alignment()));
                if (listItem.indentationLeft() != 0) {
                    write(ElementTags.INDENTATIONLEFT, String.valueOf(listItem.indentationLeft()));
                }
                if (listItem.indentationRight() != 0) {
                    write(ElementTags.INDENTATIONRIGHT, String.valueOf(listItem.indentationRight()));
                }
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(listItem))
                {
                  writeMarkupAttributes((MarkupAttributes)listItem);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                os.write(GT);
                os.write(NEWLINE);
                for (Iterator i = listItem.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                addTabs(indent);
                writeEnd(ElementTags.LISTITEM);
                return;
            }
            case Element.CELL:
            {
                Cell cell = (Cell) element;
                
                addTabs(indent);
                writeStart(ElementTags.CELL);
                write((Rectangle) cell);
                write(ElementTags.HORIZONTALALIGN, ElementTags.getAlignment(cell.horizontalAlignment()));
                write(ElementTags.VERTICALALIGN, ElementTags.getAlignment(cell.verticalAlignment()));
                if (cell.cellWidth() != null) {
                    write(ElementTags.WIDTH, cell.cellWidth());
                }
                if (cell.colspan() != 1) {
                    write(ElementTags.COLSPAN, String.valueOf(cell.colspan()));
                }
                if (cell.rowspan() != 1) {
                    write(ElementTags.ROWSPAN, String.valueOf(cell.rowspan()));
                }
                if (cell.header()) {
                    write(ElementTags.HEADER, String.valueOf(true));
                }
                if (cell.noWrap()) {
                    write(ElementTags.NOWRAP, String.valueOf(true));
                }
                if (cell.leading() != -1) {
                    write(ElementTags.LEADING, String.valueOf(cell.leading()));
                }
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(cell))
                {
                  writeMarkupAttributes((MarkupAttributes)cell);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                os.write(GT);
                os.write(NEWLINE);
                for (Iterator i = cell.getElements(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                addTabs(indent);
                writeEnd(ElementTags.CELL);
                return;
            }
            case Element.ROW:
            {
                Row row = (Row) element;
                
                addTabs(indent);
                writeStart(ElementTags.ROW);
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(row))
                {
                  writeMarkupAttributes((MarkupAttributes)row);
                }//end if this Element has MarkupAttributes
                os.write(GT);
                os.write(NEWLINE);
                Element cell;
                for (int i = 0; i < row.columns(); i++) {
                    if ((cell = (Element)row.getCell(i)) != null) {
                        write(cell, indent + 1);
                    }
                }
                addTabs(indent);
                writeEnd(ElementTags.ROW);
                return;
            }
            case Element.TABLE:
            {
                Table table = (Table) element;
                
                addTabs(indent);
                writeStart(ElementTags.TABLE);
                write(ElementTags.COLUMNS, String.valueOf(table.columns()));
                os.write(SPACE);
                write(ElementTags.WIDTH);
                os.write(EQUALS);
                os.write(QUOTE);
                if (! "".equals(table.absWidth())){
                    write(table.absWidth());
                }
                else{
                    write(String.valueOf(table.widthPercentage()));
                    write("%");
                }
                os.write(QUOTE);
                write(ElementTags.ALIGN, ElementTags.getAlignment(table.alignment()));
                write(ElementTags.CELLPADDING, String.valueOf(table.cellpadding()));
                write(ElementTags.CELLSPACING, String.valueOf(table.cellspacing()));
                os.write(SPACE);
                write(ElementTags.WIDTHS);
                os.write(EQUALS);
                os.write(QUOTE);
                float[] widths = table.getProportionalWidths();
                write(String.valueOf(widths[0]));
                for (int i = 1; i < widths.length; i++) {
                    write(";");
                    write(String.valueOf(widths[i]));
                }
                os.write(QUOTE);
                write((Rectangle) table);
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(table))
                {
                  writeMarkupAttributes((MarkupAttributes)table);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                os.write(GT);
                os.write(NEWLINE);
                Row row;
                for (Iterator iterator = table.iterator(); iterator.hasNext(); ) {
                    row = (Row) iterator.next();
                    write(row, indent + 1);
                }
                addTabs(indent);
                writeEnd(ElementTags.TABLE);
                return;
            }
            case Element.ANNOTATION:
            {
                Annotation annotation = (Annotation) element;
                
                addTabs(indent);
                writeStart(ElementTags.ANNOTATION);
                if (annotation.title() != null) {
                    write(ElementTags.TITLE, annotation.title());
                }
                if (annotation.content() != null) {
                    write(ElementTags.CONTENT, annotation.content());
                }
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(annotation))
                {
                  writeMarkupAttributes((MarkupAttributes)annotation);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                writeEnd();
                return;
            }
            case Element.GIF:
            case Element.JPEG:
            case Element.PNG:
            {
                Image image = (Image) element;
                if (image.url() == null) {
                    return;
                }
                
                addTabs(indent);
                writeStart(ElementTags.IMAGE);
                write(ElementTags.URL, image.url().toString());
                if ((image.alignment() & Image.LEFT) > 0) {
                    write(ElementTags.ALIGN, ElementTags.ALIGN_LEFT);
                }
                else if ((image.alignment() & Image.RIGHT) > 0) {
                    write(ElementTags.ALIGN, ElementTags.ALIGN_RIGHT);
                }
                else if ((image.alignment() & Image.MIDDLE) > 0) {
                    write(ElementTags.ALIGN, ElementTags.ALIGN_MIDDLE);
                }
                if ((image.alignment() & Image.UNDERLYING) > 0) {
                    write(ElementTags.UNDERLYING, String.valueOf(true));
                }
                if ((image.alignment() & Image.TEXTWRAP) > 0) {
                    write(ElementTags.TEXTWRAP, String.valueOf(true));
                }
                if (image.alt() != null) {
                    write(ElementTags.ALT, image.alt());
                }
                if (image.hasAbsolutePosition()) {
                    write(ElementTags.ABSOLUTEX, String.valueOf(image.absoluteX()));
                    write(ElementTags.ABSOLUTEY, String.valueOf(image.absoluteY()));
                }
                write(ElementTags.PLAINWIDTH, String.valueOf(image.plainWidth()));
                write(ElementTags.PLAINHEIGHT, String.valueOf(image.plainHeight()));
// patch by Matt Benson 02/21/2002
                if (hasMarkupAttributes(image))
                {
                  writeMarkupAttributes((MarkupAttributes)image);
                }//end if this Element has MarkupAttributes
// end patch by Matt Benson 02/21/2002
                writeEnd();
                return;
            }
            default:
                return;
        }
    }
    
/**
 * Writes the XML representation of a section.
 *
 * @param   section     the section to write
 * @param   indent      the indentation
 */
    
    private void writeSection(Section section, int indent) throws IOException {
        write(ElementTags.NUMBERDEPTH, String.valueOf(section.numberDepth()));
        write(ElementTags.DEPTH, String.valueOf(section.depth()));
        write(ElementTags.INDENT, String.valueOf(section.indentation()));
        if (section.indentationLeft() != 0) {
            write(ElementTags.INDENTATIONLEFT, String.valueOf(section.indentationLeft()));
        }
        if (section.indentationRight() != 0) {
            write(ElementTags.INDENTATIONRIGHT, String.valueOf(section.indentationRight()));
        }
        os.write(GT);
        os.write(NEWLINE);
        
        if (section.title() != null) {
            addTabs(indent + 1);
            writeStart(ElementTags.TITLE);
            write(ElementTags.LEADING, String.valueOf(section.title().leading()));
            write(ElementTags.ALIGN, ElementTags.getAlignment(section.title().alignment()));
            if (section.title().indentationLeft() != 0) {
                write(ElementTags.INDENTATIONLEFT, String.valueOf(section.title().indentationLeft()));
            }
            if (section.title().indentationRight() != 0) {
                write(ElementTags.INDENTATIONRIGHT, String.valueOf(section.title().indentationRight()));
            }
            write(section.title().font());
            os.write(GT);
            os.write(NEWLINE);
            Iterator i = section.title().iterator();
            if (section.depth() > 0) {
                i.next();
            }
            while (i.hasNext()) {
                write((Element) i.next(), indent + 2);
            }
            addTabs(indent + 1);
            writeEnd(ElementTags.TITLE);
        }
        for (Iterator i = section.iterator(); i.hasNext(); ) {
            write((Element) i.next(), indent + 1);
        }
        addTabs(indent);
    }
    
/**
 * Writes the XML representation of a <CODE>Font</CODE>.
 *
 * @param a <CODE>Font</CODE>
 */
    
    private void write(Font font) throws IOException {
        switch(font.family()) {
            case Font.COURIER:
                write(ElementTags.FONT, "Courier");
                break;
            case Font.HELVETICA:
                write(ElementTags.FONT, "Helvetica");
                break;
            case Font.TIMES_NEW_ROMAN:
                write(ElementTags.FONT, "Times New Roman");
                break;
            case Font.SYMBOL:
                write(ElementTags.FONT, "Symbol");
                break;
            case Font.ZAPFDINGBATS:
                write(ElementTags.FONT, "ZapfDingbats");
                break;
                default:
        }
        if (font.size() != Font.UNDEFINED) {
            write(ElementTags.SIZE, String.valueOf(font.size()));
        }
        if (font.style() != Font.UNDEFINED) {
            os.write(SPACE);
            write(ElementTags.STYLE);
            os.write(EQUALS);
            os.write(QUOTE);
            switch(font.style() & Font.BOLDITALIC) {
                case Font.NORMAL:
                    write(ElementTags.NORMAL);
                    break;
                case Font.BOLD:
                    write(ElementTags.BOLD);
                    break;
                case Font.ITALIC:
                    write(ElementTags.ITALIC);
                    break;
                case Font.BOLDITALIC:
                    write(ElementTags.BOLD);
                    write(", ");
                    write(ElementTags.ITALIC);
                    break;
            }
            if ((font.style() & Font.UNDERLINE) > 0) {
                write(", ");
                write(ElementTags.UNDERLINE);
            }
            if ((font.style() & Font.STRIKETHRU) > 0) {
                write(", ");
                write(ElementTags.STRIKETHRU);
            }
            os.write(QUOTE);
        }
        if (font.color() != null) {
            write(ElementTags.RED, String.valueOf(font.color().getRed()));
            write(ElementTags.GREEN, String.valueOf(font.color().getGreen()));
            write(ElementTags.BLUE, String.valueOf(font.color().getBlue()));
        }
    }
    
/**
 * Writes the XML representation of this <CODE>Rectangle</CODE>.
 *
 * @param a <CODE>Rectangle</CODE>
 */
    
    private void write(Rectangle rectangle) throws IOException {
        if (rectangle.borderWidth() != Rectangle.UNDEFINED) {
            write(ElementTags.BORDERWIDTH, String.valueOf(rectangle.borderWidth()));
            if (rectangle.hasBorder(Rectangle.LEFT)) {
                write(ElementTags.LEFT, String.valueOf(true));
            }
            if (rectangle.hasBorder(Rectangle.RIGHT)) {
                write(ElementTags.RIGHT, String.valueOf(true));
            }
            if (rectangle.hasBorder(Rectangle.TOP)) {
                write(ElementTags.TOP, String.valueOf(true));
            }
            if (rectangle.hasBorder(Rectangle.BOTTOM)) {
                write(ElementTags.BOTTOM, String.valueOf(true));
            }
        }
        if (rectangle.borderColor() != null) {
            write(ElementTags.RED, String.valueOf(rectangle.borderColor().getRed()));
            write(ElementTags.GREEN, String.valueOf(rectangle.borderColor().getGreen()));
            write(ElementTags.BLUE, String.valueOf(rectangle.borderColor().getBlue()));
        }
        if (rectangle.backgroundColor() != null) {
            write(ElementTags.BGRED, String.valueOf(rectangle.backgroundColor().getRed()));
            write(ElementTags.BGGREEN, String.valueOf(rectangle.backgroundColor().getGreen()));
            write(ElementTags.BGBLUE, String.valueOf(rectangle.backgroundColor().getBlue()));
        }
        if (rectangle.grayFill() > 0) {
            write(ElementTags.GRAYFILL, String.valueOf(rectangle.grayFill()));
        }
    }
    
/**
 * Encodes a <CODE>String</CODE>.
 *
 * @param   string     the <CODE>String</CODE> to encode
 * @return  the encoded <CODE>String</CODE>
 */
    
    static final String encode(String string, int indent) {
        int n = string.length();
        int pos = 0;
        char character;
        StringBuffer buf = new StringBuffer();
        // loop over all the characters of the String.
        for (int i = 0; i < n; i++) {
            character = string.charAt(i);
            // the Xmlcode of these characters are added to a StringBuffer one by one
            switch(character) {
                case ' ':
                    if ((i - pos) > 60) {
                        pos = i;
                        buf.append("\n");
                        addTabs(buf, indent);
                        break;
                    }
                    default:
                        buf.append(xmlCode[(int) character]);
            }
        }
        return buf.toString();
    }
    
/**
 * Adds a number of tabs to a <CODE>StringBuffer</CODE>.
 *
 * @param   buf     the stringbuffer
 * @param   indent  the number of tabs to add
 */
    
    static final void addTabs(StringBuffer buf, int indent) {
        for (int i = 0; i < indent; i++) {
            buf.append("\t");
        }
    }
}

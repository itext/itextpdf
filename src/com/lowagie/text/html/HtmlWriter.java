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

package com.lowagie.text.html;

import java.io.OutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.util.Stack;
import java.util.EmptyStackException;

import com.lowagie.text.*;
import com.lowagie.text.markup.MarkupTags;

/**
 * A <CODE>DocWriter</CODE> class for HTML.
 * <P>
 * An <CODE>HtmlWriter</CODE> can be added as a <CODE>DocListener</CODE>
 * to a certain <CODE>Document</CODE> by getting an instance.
 * Every <CODE>Element</CODE> added to the original <CODE>Document</CODE>
 * will be written to the <CODE>OutputStream</CODE> of this <CODE>HtmlWriter</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * // creation of the document with a certain size and certain margins
 * Document document = new Document(PageSize.A4, 50, 50, 50, 50);
 * try {
 *    // this will write HTML to the Standard OutputStream
 *    <STRONG>HtmlWriter.getInstance(document, System.out);</STRONG>
 *    // this will write HTML to a file called text.html
 *    <STRONG>HtmlWriter.getInstance(document, new FileOutputStream("text.html"));</STRONG>
 *    // this will write HTML to for instance the OutputStream of a HttpServletResponse-object
 *    <STRONG>HtmlWriter.getInstance(document, response.getOutputStream());</STRONG>
 * }
 * catch(DocumentException de) {
 *    System.err.println(de.getMessage());
 * }
 * // this will close the document and all the OutputStreams listening to it
 * <STRONG>document.close();</CODE>
 * </PRE></BLOCKQUOTE>
 */

public class HtmlWriter extends DocWriter implements DocListener {
    
    // static membervariables (tags)
    
/** This is a possible HTML-tag. */
    public static final byte[] BEGINCOMMENT = getISOBytes("<!-- ");
    
/** This is a possible HTML-tag. */
    public static final byte[] ENDCOMMENT = getISOBytes(" -->");
    
/** This is a possible HTML-tag. */
    public static final String NBSP = "&nbsp;";
    
    // membervariables
    
/** This is the current font of the HTML. */
    protected Stack currentfont = new Stack();
    
/** This is the standard font of the HTML. */
    protected Font standardfont = new Font();
    
/** This is a path for images. */
    protected String imagepath = null;
    
/** Stores the page number. */
    protected int pageN = 0;
    
/** This is the textual part of a header */
    protected HeaderFooter header = null;
    
/** This is the textual part of the footer */
    protected HeaderFooter footer = null;
    
    // constructor
    
/**
 * Constructs a <CODE>HtmlWriter</CODE>.
 *
 * @param doc     The <CODE>Document</CODE> that has to be written as HTML
 * @param os      The <CODE>OutputStream</CODE> the writer has to write to.
 */
    
    protected HtmlWriter(Document doc, OutputStream os) {
        super(doc, os);
        
        document.addDocListener(this);
        this.pageN = document.getPageNumber();
        try {
            os.write(LT);
            os.write(getISOBytes(HtmlTags.HTML));
            os.write(GT);
            os.write(NEWLINE);
            os.write(TAB);
            os.write(LT);
            os.write(getISOBytes(HtmlTags.HEAD));
            os.write(GT);
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    // get an instance of the HtmlWriter
    
/**
 * Gets an instance of the <CODE>HtmlWriter</CODE>.
 *
 * @param document  The <CODE>Document</CODE> that has to be written
 * @param os  The <CODE>OutputStream</CODE> the writer has to write to.
 * @return  a new <CODE>HtmlWriter</CODE>
 */
    
    public static HtmlWriter getInstance(Document document, OutputStream os) {
        return new HtmlWriter(document, os);
    }
    
    // implementation of the DocListener methods
    
/**
 * Signals that an new page has to be started.
 *
 * @return  <CODE>true</CODE> if this action succeeded, <CODE>false</CODE> if not.
 * @throws  DocumentException when a document isn't open yet, or has been closed
 */
    
    public boolean newPage() throws DocumentException {
        try {
            writeStart(MarkupTags.DIV);
            write(" ");
            write(MarkupTags.STYLE);
            write("=\"");
            writeCssProperty(MarkupTags.PAGE_BREAK_BEFORE, MarkupTags.ALWAYS);
            write("\" /");
            os.write(GT);
        }
        catch(IOException ioe) {
            throw new DocumentException(ioe.getMessage());
        }
        return true;
    }
    
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
                case Element.HEADER:
                    try {
                        Header h = (Header) element;
                        if (MarkupTags.STYLESHEET.equals(h.name())) {
                            writeLink(h);
                        }
                        else if (HtmlTags.JAVASCRIPT.equals(h.name())) {
                            writeJavaScript(h);
                        }
                        else {
                            writeHeader(h);
                        }
                    }
                    catch(ClassCastException cce) {
                    }
                    return true;
                case Element.SUBJECT:
                case Element.KEYWORDS:
                case Element.AUTHOR:
                    Meta meta = (Meta) element;
                    writeHeader(meta);
                    return true;
                case Element.TITLE:
                    addTabs(2);
                    writeStart(HtmlTags.TITLE);
                    os.write(GT);
                    addTabs(3);
                    write(HtmlEncoder.encode(((Meta)element).content()));
                    addTabs(2);
                    writeEnd(HtmlTags.TITLE);
                    return true;
                case Element.CREATOR:
                    writeComment("Creator: " + HtmlEncoder.encode(((Meta)element).content()));
                    return true;
                case Element.PRODUCER:
                    writeComment("Producer: " + HtmlEncoder.encode(((Meta)element).content()));
                    return true;
                case Element.CREATIONDATE:
                    writeComment("Creationdate: " + HtmlEncoder.encode(((Meta)element).content()));
                    return true;
                    default:
                        write(element, 2);
                        return true;
            }
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Signals that the <CODE>Document</CODE> has been opened and that
 * <CODE>Elements</CODE> can be added.
 * <P>
 * The <CODE>HEAD</CODE>-section of the HTML-document is written.
 */
    
    public void open() {
        super.open();
        try {
            writeComment("Producer: iTextXML by lowagie.com");
            writeComment("CreationDate: " + new Date().toString());
            addTabs(1);
            writeEnd(HtmlTags.HEAD);
            addTabs(1);
            writeStart(HtmlTags.BODY);
            if (document.leftMargin() > 0) {
                write(HtmlTags.LEFTMARGIN, String.valueOf(document.leftMargin()));
            }
            if (document.rightMargin() > 0) {
                write(HtmlTags.RIGHTMARGIN, String.valueOf(document.rightMargin()));
            }
            if (document.topMargin() > 0) {
                write(HtmlTags.TOPMARGIN, String.valueOf(document.topMargin()));
            }
            if (document.bottomMargin() > 0) {
                write(HtmlTags.BOTTOMMARGIN, String.valueOf(document.bottomMargin()));
            }
            if (pageSize.backgroundColor() != null) {
                write(HtmlTags.BACKGROUNDCOLOR, HtmlEncoder.encode(pageSize.backgroundColor()));
            }
            if (document.getJavaScript_onLoad() != null) {
                write(HtmlTags.JAVASCRIPT_ONLOAD, HtmlEncoder.encode(document.getJavaScript_onLoad()));
            }
            if (document.getJavaScript_onUnLoad() != null) {
                write(HtmlTags.JAVASCRIPT_ONUNLOAD, HtmlEncoder.encode(document.getJavaScript_onUnLoad()));
            }
            if (document.getHtmlStyleClass() != null) {
                write(MarkupTags.CLASS, document.getHtmlStyleClass());
            }
            os.write(GT);
            initHeader(); // line added by David Freels
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Signals that the <CODE>Document</CODE> was closed and that no other
 * <CODE>Elements</CODE> will be added.
 */
    
    public void close() {
        try {
            initFooter(); // line added by David Freels
            addTabs(1);
            writeEnd(HtmlTags.BODY);
            os.write(NEWLINE);
            writeEnd(HtmlTags.HTML);
            super.close();
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
    // some protected methods
    
/**
 * Adds the header to the top of the </CODE>Document</CODE>
 */
    
    protected void initHeader() {
        if (header != null) {
            try {
                add(header.paragraph());
            }
            catch(Exception e) {
                throw new ExceptionConverter(e);
            }
        }
    }
    
/**
 *  Adds the header to the top of the </CODE>Document</CODE>
 */
    
    protected void initFooter() {
        if (footer != null) {
            try {
                // Set the page number. HTML has no notion of a page, so it should always
                // add up to 1
                footer.setPageNumber(pageN + 1);
                add(footer.paragraph());
            }
            catch(Exception e) {
                throw new ExceptionConverter(e);
            }
        }
    }
    
/**
 * Writes a Metatag in the header.
 *
 * @param   meta   the element that has to be written
 * @throws  IOException
 */
    
    protected void writeHeader(Meta meta) throws IOException {
        addTabs(2);
        writeStart(HtmlTags.META);
        switch(meta.type()) {
            case Element.HEADER:
                write(HtmlTags.NAME, ((Header) meta).name());
                break;
            case Element.SUBJECT:
                write(HtmlTags.NAME, HtmlTags.SUBJECT);
                break;
            case Element.KEYWORDS:
                write(HtmlTags.NAME, HtmlTags.KEYWORDS);
                break;
            case Element.AUTHOR:
                write(HtmlTags.NAME, HtmlTags.AUTHOR);
                break;
        }
        write(HtmlTags.CONTENT, HtmlEncoder.encode(meta.content()));
        writeEnd();
    }
    
/**
 * Writes a link in the header.
 *
 * @param   header   the element that has to be written
 * @throws  IOException
 */
    
    protected void writeLink(Header header) throws IOException {
        addTabs(2);
        writeStart(MarkupTags.LINK);
        write(MarkupTags.REL, header.name());
        write(MarkupTags.TYPE, MarkupTags.CSS);
        write(HtmlTags.REFERENCE, header.content());
        writeEnd();
    }
    
/**
 * Writes a JavaScript section or, if the markup attribute HtmlTags.URL is set, a JavaScript reference in the header.
 *
 * @param   header   the element that has to be written
 * @throws  IOException
 */
    
    protected void writeJavaScript(Header header) throws IOException {
        addTabs(2);
        writeStart(HtmlTags.SCRIPT);
        write(HtmlTags.LANGUAGE, HtmlTags.JAVASCRIPT);
        if (header.getMarkupAttribute(HtmlTags.URL) != null) {
          /* JavaScript reference example:
           *
           * <script language="JavaScript" src="/myPath/MyFunctions.js"/>
           */ 
          write(HtmlTags.URL, header.getMarkupAttribute(HtmlTags.URL));
          os.write(GT);
          writeEnd(HtmlTags.SCRIPT);
        }
        else {
          /* JavaScript coding convention:
           *
           * <script language="JavaScript" type="text/javascript">
           * <!--
           * // ... JavaScript methods ...
           * //-->
           * </script>
           */ 
          write(MarkupTags.TYPE, MarkupTags.JAVASCRIPT);
          os.write(GT);
          addTabs(2);
          write(new String(BEGINCOMMENT) + "\n");
          write(header.content());
          addTabs(2);
          write("//" + new String(ENDCOMMENT));
          addTabs(2);
          writeEnd(HtmlTags.SCRIPT);
        }
    }
    
/**
 * Writes some comment.
 * <P>
 * This method writes some comment.
 *
 * @param comment   the comment that has to be written
 * @throws  IOException
 */
    
    protected void writeComment(String comment) throws IOException {
        addTabs(2);
        os.write(BEGINCOMMENT);
        write(comment);
        os.write(ENDCOMMENT);
    }
    
    // public methods
    
/**
 * Changes the standardfont.
 *
 * @param standardfont  The font
 */
    
    public void setStandardFont(Font standardfont) {
        this.standardfont = standardfont;
    }
    
/**
 * Checks if a given font is the same as the font that was last used.
 *
 * @param   font    the font of an object
 * @return  true if the font differs
 */
    
    public boolean isOtherFont(Font font) {
        try {
            Font cFont = (Font) currentfont.peek();
            if (cFont.compareTo(font) == 0) return false;
            return true;
        }
        catch(EmptyStackException ese) {
            if (standardfont.compareTo(font) == 0) return false;
            return true;
        }
    }
    
/**
 * Sets the basepath for images.
 * <P>
 * This is especially useful if you add images using a file,
 * rather than an URL. In PDF there is no problem, since
 * the images are added inline, but in HTML it is sometimes
 * necessary to use a relative path or a special path to some
 * images directory.
 *
 * @param imagepath the new imagepath
 */
    
    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
    
/**
 * Resets the imagepath.
 */
    
    public void resetImagepath() {
        imagepath = null;
    }
    
/**
 * Changes the header of this document.
 *
 * @param header    the new header
 */
    
    public void setHeader(HeaderFooter header) {
        this.header = header;
    }
    
/**
 * Changes the footer of this document.
 *
 * @param footer    the new footer
 */
    
    public void setFooter(HeaderFooter footer) {
        this.footer = footer;
    }
    
/**
 * Signals that a <CODE>String</CODE> was added to the <CODE>Document</CODE>.
 *
 * @return  <CODE>true</CODE> if the string was added, <CODE>false</CODE> if not.
 * @throws  DocumentException when a document isn't open yet, or has been closed
 */
    
    public boolean add(String string) throws DocumentException{
        if (pause) {
            return false;
        }
        try
        {
            write(string);
            return true;
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }
    
/**
 * Writes the HTML representation of an element.
 *
 * @param   element     the element
 * @param   indent      the indentation
 */
    
    protected void write(Element element, int indent) throws IOException {
        Properties styleAttributes = null;
        switch(element.type()) {
            case Element.CHUNK:
            {
                Chunk chunk = (Chunk) element;
                // if the chunk contains an image, return the image representation
                Image image = chunk.getImage();
                if (image != null) {
                    write(image, indent);
                    return;
                }
                
                if (chunk.isEmpty()) return;
                HashMap attributes = chunk.getAttributes();
                if (attributes != null && attributes.get(Chunk.NEWPAGE) != null) {
                    return;
                }
                // This doesn't seem to work:
                //if (attributes != null && attributes.get(Chunk.SUBSUPSCRIPT) != null) {
                //    float p = (((Float)attributes.get(Chunk.SUBSUPSCRIPT)).floatValue() * 100f) / chunk.font().size();
                //    styleAttributes = new Properties();
                //    styleAttributes.setProperty(MarkupTags.CSS_VERTICALALIGN, "" + p + "%");
                //}
                boolean tag = isOtherFont(chunk.font()) || hasMarkupAttributes(chunk) || styleAttributes != null;
                if (tag) {
                    // start span tag
                    addTabs(indent);
                    writeStart(MarkupTags.SPAN);
                    if (isOtherFont(chunk.font())) {
                        write(chunk.font(), styleAttributes);
                    }
                    if (hasMarkupAttributes(chunk)) {
                        writeMarkupAttributes((MarkupAttributes)chunk);
                    }
                    os.write(GT);
                }
                if (attributes != null && attributes.get(Chunk.SUBSUPSCRIPT) != null) {
                    // start sup or sub tag
                    if (((Float)attributes.get(Chunk.SUBSUPSCRIPT)).floatValue() > 0) {
                        writeStart(HtmlTags.SUP);
                    }
                    else {
                        writeStart(HtmlTags.SUB);
                    }
                    os.write(GT);
                }
                // contents
                write(HtmlEncoder.encode(chunk.content()));
                if (attributes != null && attributes.get(Chunk.SUBSUPSCRIPT) != null) {
                    // end sup or sub tag
                    os.write(LT);
                    os.write(FORWARD);
                    if (((Float)attributes.get(Chunk.SUBSUPSCRIPT)).floatValue() > 0) {
                        write(HtmlTags.SUP);
                    }
                    else {
                        write(HtmlTags.SUB);
                    }
                    os.write(GT);
                }
                if (tag) {
                    // end tag
                    writeEnd(MarkupTags.SPAN);
                }
                return;
            }
            case Element.PHRASE:
            {
                Phrase phrase = (Phrase) element;
                styleAttributes = new Properties();
                if (phrase.leadingDefined()) styleAttributes.setProperty(MarkupTags.CSS_LINEHEIGHT, String.valueOf(phrase.leading()) + "pt");
                
                // start tag
                addTabs(indent);
                writeStart(MarkupTags.SPAN);
                if (hasMarkupAttributes(phrase)) {
                    writeMarkupAttributes((MarkupAttributes)phrase);
                }
                write(phrase.font(), styleAttributes);
                os.write(GT);
                currentfont.push(phrase.font());
                // contents
                for (Iterator i = phrase.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                // end tag
                addTabs(indent);
                writeEnd(MarkupTags.SPAN);
                currentfont.pop();
                return;
            }
            case Element.ANCHOR:
            {
                Anchor anchor = (Anchor) element;
                styleAttributes = new Properties();
                if (anchor.leadingDefined()) styleAttributes.setProperty(MarkupTags.CSS_LINEHEIGHT, String.valueOf(anchor.leading()) + "pt");
                
                // start tag
                addTabs(indent);
                writeStart(HtmlTags.ANCHOR);
                if (anchor.name() != null) {
                    write(HtmlTags.NAME, anchor.name());
                }
                if (anchor.reference() != null) {
                    write(HtmlTags.REFERENCE, anchor.reference());
                }
                if (hasMarkupAttributes(anchor)) {
                    writeMarkupAttributes((MarkupAttributes)anchor);
                }
                write(anchor.font(), styleAttributes);
                os.write(GT);
                currentfont.push(anchor.font());
                // contents
                for (Iterator i = anchor.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                // end tag
                addTabs(indent);
                writeEnd(HtmlTags.ANCHOR);
                currentfont.pop();
                return;
            }
            case Element.PARAGRAPH:
            {
                Paragraph paragraph = (Paragraph) element;
                styleAttributes = new Properties();
                if (paragraph.leadingDefined()) styleAttributes.setProperty(MarkupTags.CSS_LINEHEIGHT, String.valueOf(paragraph.leading()) + "pt");
                
                // start tag
                addTabs(indent);
                writeStart(MarkupTags.DIV);
                if (hasMarkupAttributes(paragraph)) {
                    writeMarkupAttributes((MarkupAttributes)paragraph);
                }
                String alignment = HtmlEncoder.getAlignment(paragraph.alignment());
                if (!"".equals(alignment)) {
                    write(HtmlTags.ALIGN, alignment);
                }
                write(paragraph.font(), styleAttributes);
                os.write(GT);
                currentfont.push(paragraph.font());
                // contents
                for (Iterator i = paragraph.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                // end tag
                addTabs(indent);
                writeEnd(MarkupTags.DIV);
                currentfont.pop();
                return;
            }
            case Element.SECTION:
            case Element.CHAPTER:
            {
                // part of the start tag + contents
                writeSection((Section) element, indent);
                return;
            }
            case Element.LIST:
            {
                List list = (List) element;
                // start tag
                addTabs(indent);
                if (list.isNumbered()) {
                    writeStart(HtmlTags.ORDEREDLIST);
                }
                else {
                    writeStart(HtmlTags.UNORDEREDLIST);
                }
                if (hasMarkupAttributes(list)) {
                    writeMarkupAttributes((MarkupAttributes)list);
                }
                os.write(GT);
                // contents
                for (Iterator i = list.getItems().iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                // end tag
                addTabs(indent);
                if (list.isNumbered()) {
                    writeEnd(HtmlTags.ORDEREDLIST);
                }
                else {
                    writeEnd(HtmlTags.UNORDEREDLIST);
                }
                return;
            }
            case Element.LISTITEM:
            {
                ListItem listItem = (ListItem) element;
                styleAttributes = new Properties();
                if (listItem.leadingDefined()) styleAttributes.setProperty(MarkupTags.CSS_LINEHEIGHT, String.valueOf(listItem.leading()) + "pt");
                
                // start tag
                addTabs(indent);
                writeStart(HtmlTags.LISTITEM);
                if (hasMarkupAttributes(listItem)) {
                    writeMarkupAttributes((MarkupAttributes)listItem);
                }
                write(listItem.font(), styleAttributes);
                os.write(GT);
                currentfont.push(listItem.font());
                // contents
                for (Iterator i = listItem.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                // end tag
                addTabs(indent);
                writeEnd(HtmlTags.LISTITEM);
                currentfont.pop();
                return;
            }
            case Element.CELL:
            {
                Cell cell = (Cell) element;
                
                // start tag
                addTabs(indent);
                if (cell.header()) {
                    writeStart(HtmlTags.HEADERCELL);
                }
                else {
                    writeStart(HtmlTags.CELL);
                }
                if (hasMarkupAttributes(cell)) {
                    writeMarkupAttributes((MarkupAttributes)cell);
                }
                if (cell.borderWidth() != Rectangle.UNDEFINED) {
                    write(HtmlTags.BORDERWIDTH, String.valueOf(cell.borderWidth()));
                }
                if (cell.borderColor() != null) {
                    write(HtmlTags.BORDERCOLOR, HtmlEncoder.encode(cell.borderColor()));
                }
                if (cell.backgroundColor() != null) {
                    write(HtmlTags.BACKGROUNDCOLOR, HtmlEncoder.encode(cell.backgroundColor()));
                }
                String alignment = HtmlEncoder.getAlignment(cell.horizontalAlignment());
                if (!"".equals(alignment)) {
                    write(HtmlTags.HORIZONTALALIGN, alignment);
                }
                alignment = HtmlEncoder.getAlignment(cell.verticalAlignment());
                if (!"".equals(alignment)) {
                    write(HtmlTags.VERTICALALIGN, alignment);
                }
                if (cell.cellWidth() != null) {
                    write(HtmlTags.WIDTH, cell.cellWidth());
                }
                if (cell.colspan() != 1) {
                    write(HtmlTags.COLSPAN, String.valueOf(cell.colspan()));
                }
                if (cell.rowspan() != 1) {
                    write(HtmlTags.ROWSPAN, String.valueOf(cell.rowspan()));
                }
                if (cell.noWrap()) {
                    write(HtmlTags.NOWRAP, String.valueOf(true));
                }
                os.write(GT);
                // contents
                if (cell.isEmpty()) {
                    write(NBSP);
                } else {
                    for (Iterator i = cell.getElements(); i.hasNext(); ) {
                        write((Element) i.next(), indent + 1);
                    }
                }
                // end tag
                addTabs(indent);
                if (cell.header()) {
                    writeEnd(HtmlTags.HEADERCELL);
                }
                else {
                    writeEnd(HtmlTags.CELL);
                }
                return;
            }
            case Element.ROW:
            {
                Row row = (Row) element;
                
                // start tag
                addTabs(indent);
                writeStart(HtmlTags.ROW);
                if (hasMarkupAttributes(row)) {
                    writeMarkupAttributes((MarkupAttributes)row);
                }
                os.write(GT);
                // contents
                Element cell;
                for (int i = 0; i < row.columns(); i++) {
                    if ((cell = (Element)row.getCell(i)) != null) {
                        write(cell, indent + 1);
                    }
                }
                // end tag
                addTabs(indent);
                writeEnd(HtmlTags.ROW);
                return;
            }
            case Element.TABLE:
            {
                Table table = (Table) element;
                table.complete();
                // start tag
                addTabs(indent);
                writeStart(HtmlTags.TABLE);
                if (hasMarkupAttributes(table)) {
                    writeMarkupAttributes((MarkupAttributes)table);
                }
                os.write(SPACE);
                write(HtmlTags.WIDTH);
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
                String alignment = HtmlEncoder.getAlignment(table.alignment());
                if (!"".equals(alignment)) {
                    write(HtmlTags.ALIGN, alignment);
                }
                write(HtmlTags.CELLPADDING, String.valueOf(table.cellpadding()));
                write(HtmlTags.CELLSPACING, String.valueOf(table.cellspacing()));
                if (table.borderWidth() != Rectangle.UNDEFINED) {
                    write(HtmlTags.BORDERWIDTH, String.valueOf(table.borderWidth()));
                }
                if (table.borderColor() != null) {
                    write(HtmlTags.BORDERCOLOR, HtmlEncoder.encode(table.borderColor()));
                }
                if (table.backgroundColor() != null) {
                    write(HtmlTags.BACKGROUNDCOLOR, HtmlEncoder.encode(table.backgroundColor()));
                }
                os.write(GT);
                // contents
                Row row;
                for (Iterator iterator = table.iterator(); iterator.hasNext(); ) {
                    row = (Row) iterator.next();
                    write(row, indent + 1);
                }
                // end tag
                addTabs(indent);
                writeEnd(HtmlTags.TABLE);
                return;
            }
            case Element.ANNOTATION:
            {
                Annotation annotation = (Annotation) element;
                writeComment(annotation.title() + ": " + annotation.content());
                if (hasMarkupAttributes(annotation)) {
                    os.write(BEGINCOMMENT);
                    writeMarkupAttributes((MarkupAttributes)annotation);
                    os.write(ENDCOMMENT);
                }
                return;
            }
            case Element.IMGRAW:
            case Element.JPEG:
            case Element.IMGTEMPLATE:
            {
                Image image = (Image) element;
                if (image.url() == null) {
                    return;
                }
                
                // start tag
                addTabs(indent);
                writeStart(HtmlTags.IMAGE);
                String path = image.url().toString();
                if (imagepath != null) {
                    if (path.indexOf("/") > 0) {
                        path = imagepath + path.substring(path.lastIndexOf("/") + 1);
                    }
                    else {
                        path = imagepath + path;
                    }
                }
                write(HtmlTags.URL, path);
                if ((image.alignment() & Image.LEFT) > 0) {
                    write(HtmlTags.ALIGN, HtmlTags.ALIGN_LEFT);
                }
                else if ((image.alignment() & Image.RIGHT) > 0) {
                    write(HtmlTags.ALIGN, HtmlTags.ALIGN_RIGHT);
                }
                else if ((image.alignment() & Image.MIDDLE) > 0) {
                    write(HtmlTags.ALIGN, HtmlTags.ALIGN_MIDDLE);
                }
                if (image.alt() != null) {
                    write(HtmlTags.ALT, image.alt());
                }
                write(HtmlTags.PLAINWIDTH, String.valueOf(image.scaledWidth()));
                write(HtmlTags.PLAINHEIGHT, String.valueOf(image.scaledHeight()));
                if (hasMarkupAttributes(image)){
                    writeMarkupAttributes((MarkupAttributes)image);
                }
                writeEnd();
                return;
            }
            
            default:
                return;
        }
    }
    
/**
 * Writes the HTML representation of a section.
 *
 * @param   section     the section to write
 * @param   indent      the indentation
 */
    
    protected void writeSection(Section section, int indent) throws IOException {
        if (section.title() != null) {
            int depth = section.depth() - 1;
            if (depth > 5) {
                depth = 5;
            }
            Properties styleAttributes = new Properties();
            if (section.title().leadingDefined()) styleAttributes.setProperty(MarkupTags.CSS_LINEHEIGHT, String.valueOf(section.title().leading()) + "pt");
            // start tag
            addTabs(indent);
            writeStart(HtmlTags.H[depth]);
            write(section.title().font(), styleAttributes);
            String alignment = HtmlEncoder.getAlignment(section.title().alignment());
            if (!"".equals(alignment)) {
                write(HtmlTags.ALIGN, alignment);
            }
            if (hasMarkupAttributes(section.title())) {
                writeMarkupAttributes((MarkupAttributes)section.title());
            }
            os.write(GT);
            currentfont.push(section.title().font());
            // contents
            for (Iterator i = section.title().iterator(); i.hasNext(); ) {
                write((Element)i.next(), indent + 1);
            }
            // end tag
            addTabs(indent);
            writeEnd(HtmlTags.H[depth]);
            currentfont.pop();
        }
        for (Iterator i = section.iterator(); i.hasNext(); ) {
            write((Element) i.next(), indent);
        }
    }
    
    /**
     * Writes the representation of a <CODE>Font</CODE>.
     *
     * @param font              a <CODE>Font</CODE>
     * @param styleAttributes   the style of the font
     */
    
    protected void write(Font font, Properties styleAttributes) throws IOException {
        if (font == null || !isOtherFont(font) || styleAttributes == null) return;
        write(" ");
        write(MarkupTags.STYLE);
        write("=\"");
        if (styleAttributes != null) {
            String key;
            for (Enumeration e = styleAttributes.propertyNames(); e.hasMoreElements(); ) {
                key = (String)e.nextElement();
                writeCssProperty(key, styleAttributes.getProperty(key));
            }
        }
        if (isOtherFont(font)) {
            writeCssProperty(MarkupTags.CSS_FONTFAMILY, font.getFamilyname());
            
            if (font.size() != Font.UNDEFINED) {
                writeCssProperty(MarkupTags.CSS_FONTSIZE, String.valueOf(font.size()) + "pt");
            }
            if (font.color() != null) {
                writeCssProperty(MarkupTags.CSS_COLOR, HtmlEncoder.encode(font.color()));
            }
            
            int fontstyle = font.style();
            if (fontstyle != Font.UNDEFINED && fontstyle != Font.NORMAL) {
                switch (fontstyle & Font.BOLDITALIC) {
                    case Font.BOLD:
                        writeCssProperty(MarkupTags.CSS_FONTWEIGHT, MarkupTags.CSS_BOLD);
                        break;
                    case Font.ITALIC:
                        writeCssProperty(MarkupTags.CSS_FONTSTYLE, MarkupTags.CSS_ITALIC);
                        break;
                    case Font.BOLDITALIC:
                        writeCssProperty(MarkupTags.CSS_FONTWEIGHT, MarkupTags.CSS_BOLD);
                        writeCssProperty(MarkupTags.CSS_FONTSTYLE, MarkupTags.CSS_ITALIC);
                        break;
                }
                
                // CSS only supports one decoration tag so if both are specified
                // only one of the two will display
                if ((fontstyle & Font.UNDERLINE) > 0) {
                    writeCssProperty(MarkupTags.CSS_TEXTDECORATION, MarkupTags.CSS_UNDERLINE);
                }
                if ((fontstyle & Font.STRIKETHRU) > 0) {
                    writeCssProperty(MarkupTags.CSS_TEXTDECORATION, MarkupTags.CSS_LINETHROUGH);
                }
            }
        }
        write("\"");
    }
    
    /**
     * Writes out a CSS property.
     */
    protected void writeCssProperty(String prop, String value) throws IOException {
        write(new StringBuffer(prop).append(": ").append(value).append("; ").toString());
    }
}
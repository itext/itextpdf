/**
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Mark Hall
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
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
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

package com.lowagie.text.rtf;

import com.lowagie.text.*;

import java.io.*;
import java.util.*;
import java.awt.Color;
import java.text.DateFormat;

  /**
   * A <CODE>DocWriter</CODE> class for Rich Text Files (RTF).
   * <P>
   * A <CODE>RtfWriter</CODE> can be added as a <CODE>DocListener</CODE>
   * to a certain <CODE>Document</CODE> by getting an instance.
   * Every <CODE>Element</CODE> added to the original <CODE>Document</CODE>
   * will be written to the <CODE>OutputStream</CODE> of this
   * <CODE>RtfWriter</CODE>.
   * <P>
   * Example:
   * <BLOCKQUOTE><PRE>
   * // creation of the document with a certain size and certain margins
   * Document document = new Document(PageSize.A4, 50, 50, 50, 50);
   * try {
   *    // this will write RTF to the Standard OutputStream
   *    <STRONG>RtfWriter.getInstance(document, System.out);</STRONG>
   *    // this will write Rtf to a file called text.rtf
   *    <STRONG>RtfWriter.getInstance(document, new
   * FileOutputStream("text.rtf"));</STRONG>
   *    // this will write Rtf to for instance the OutputStream of a
   * HttpServletResponse-object
   *    <STRONG>RtfWriter.getInstance(document,
   * response.getOutputStream());</STRONG>
   * }
   * catch(DocumentException de) {
   *    System.err.println(de.getMessage());
   * }
   * // this will close the document and all the OutputStreams listening to it
   * <STRONG>document.close();</CODE>
   * </PRE></BLOCKQUOTE>
   */

public class RtfWriter extends DocWriter implements DocListener
{
  /**
   * Static Constants
   */
    
  /**
   * General
   */
    
  /** This is the escape character which introduces RTF tags. */
    private static final byte escape = (byte) '\\';
    
  /** This is another escape character which introduces RTF tags. */
    private static final byte[] extendedEscape = "\\*\\".getBytes();
    
  /** This is the delimiter between RTF tags and normal text. */
    private static final byte delimiter = (byte) ' ';
    
  /** This is another delimiter between RTF tags and normal text. */
    private static final byte commaDelimiter = (byte) ';';
    
  /** This is the character for beginning a new group. */
    private static final byte openGroup = (byte) '{';
    
  /** This is the character for closing a group. */
    private static final byte closeGroup = (byte) '}';
    
    
  /**
   * RTF Information
   */
    
  /** RTF begin and version. */
    private static final byte[] docBegin = "rtf1".getBytes();
    
  /** RTF encoding. */
    private static final byte[] ansi = "ansi".getBytes();
    
  /** RTF encoding codepage. */
    private static final byte[] ansiCodepage = "ansicpg".getBytes();
    
    
  /**
   *Font Data
   */
    
  /** Begin the font table tag. */
    private static final byte[] fontTable = "fonttbl".getBytes();
    
  /** Font number tag. */
    private static final byte fontNumber = (byte) 'f';
    
  /** Font size tag. */
    private static final byte[] fontSize = "fs".getBytes();
    
  /** Font color tag. */
    private static final byte[] fontColor = "cf".getBytes();
    
  /** Modern font tag. */
    private static final byte[] fontModern = "fmodern".getBytes();
    
  /** Swiss font tag. */
    private static final byte[] fontSwiss = "fswiss".getBytes();
    
  /** Roman font tag. */
    private static final byte[] fontRoman = "froman".getBytes();
    
  /** Tech font tag. */
    private static final byte[] fontTech = "ftech".getBytes();
    
  /** Font charset tag. */
    private static final byte[] fontCharset = "fcharset".getBytes();
    
  /** Font Courier tag. */
    private static final byte[] fontCourier = "Courier".getBytes();
    
  /** Font Arial tag. */
    private static final byte[] fontArial = "Arial".getBytes();
    
  /** Font Symbol tag. */
    private static final byte[] fontSymbol = "Symbol".getBytes();
    
  /** Font Times New Roman tag. */
    private static final byte[] fontTimesNewRoman = "Times New Roman".getBytes();
    
  /** Font Windings tag. */
    private static final byte[] fontWindings = "Windings".getBytes();
    
    
  /**
   *Sections / Paragraphs
   */
    
  /** Reset section defaults tag. */
    private static final byte[] sectionDefaults = "sectd".getBytes();
    
  /** Begin new section tag. */
    private static final byte[] section = "sect".getBytes();
    
  /** Reset paragraph defaults tag. */
    private static final byte[] paragraphDefaults = "pard".getBytes();
    
  /** Begin new paragraph tag. */
    private static final byte[] paragraph = "par".getBytes();
    
    
  /**
   *Lists
   */
    
  /** List style group tag. */
    private static final byte[] listText = "pntext".getBytes();
    
  /** List bullet character tag. */
    private static final byte[] bulletCharacter = "'B7".getBytes();
    
  /** Tabulator tag. */
    private static final byte[] tab = "tab".getBytes();
    
  /** Begin new list tag. */
    private static final byte[] listBegin = "pn".getBytes();
    
  /** List numbering  tag. */
    private static final byte[] lvlbodyNumbering = "pnlvlbody".getBytes();
    
  /** List decimal numbering tag. */
    private static final byte[] decimalNumbering = "pndec".getBytes();
    
  /** Bulleted list tag. */
    private static final byte[] listBullets = "pnlvlblt".getBytes();
    
  /** List font number tag. */
    private static final byte[] listFontNumber = "pnf".getBytes();
    
  /** List first indentation tag. */
    private static final byte[] listFirstIndent  = "pnindent".getBytes();
    
  /** Begin bullet group tag. */
    private static final byte[] listBulletDefine  = "pntextb".getBytes();
    
  /** First indent tag. */
    private static final byte[] firstIndent = "fi-".getBytes();
    
  /** List indent tag. */
    private static final byte[] listIndent = "li".getBytes();
    
    
  /**
   *Text Style
   */
    
  /** Bold tag. */
    private static final byte bold = (byte) 'b';
    
  /** Italic tag. */
    private static final byte italic = (byte) 'i';
    
  /** Underline tag. */
    private static final byte[] underline = "ul".getBytes();
    
  /** Strikethrough tag. */
    private static final byte[] strikethrough = "strike".getBytes();
    
  /** Text alignment left tag. */
    private static final byte[] alignLeft = "ql".getBytes();
    
  /** Text alignment center tag. */
    private static final byte[] alignCenter = "qc".getBytes();
    
  /** Text alignment right tag. */
    private static final byte[] alignRight = "qr".getBytes();
    
    
  /**
   *Colors
   */
    
  /** Begin colour table tag. */
    private static final byte[] colorTable = "colortbl".getBytes();
    
  /** Red value tag. */
    private static final byte[] colorRed = "red".getBytes();
    
  /** Green value tag. */
    private static final byte[] colorGreen = "green".getBytes();
    
  /** Blue value tag. */
    private static final byte[] colorBlue = "blue".getBytes();
    
    
  /**
   *Information Group
   */
    
  /** Begin the info group tag.*/
    private static final byte[] infoBegin = "info".getBytes();
    
  /** Author tag. */
    private static final byte[] metaAuthor = "author".getBytes();
    
  /** Subject tag. */
    private static final byte[] metaSubject = "subject".getBytes();
    
  /** Keywords tag. */
    private static final byte[] metaKeywords = "keywords".getBytes();
    
  /** Title tag. */
    private static final byte[] metaTitle = "title".getBytes();
    
  /** Producer tag. */
    private static final byte[] metaProducer = "operator".getBytes();
    
  /** Creation Date tag. */
    private static final byte[] metaCreationDate = "creationdate".getBytes();
    
  /** Year tag. */
    private static final byte[] year = "yr".getBytes();
    
  /** Month tag. */
    private static final byte[] month = "mo".getBytes();
    
  /** Day tag. */
    private static final byte[] day = "dy".getBytes();
    
  /** Hour tag. */
    private static final byte[] hour = "hr".getBytes();
    
  /** Minute tag. */
    private static final byte[] minute = "min".getBytes();
    
  /** Second tag. */
    private static final byte[] second = "sec".getBytes();
    
    
  /**
   *Tables
   */
    
  /** Begin table row tag. */
    private static final byte[] tableRowBegin = "trowd".getBytes();
    
  /** End row tag. */
    private static final byte[] tableRowEnd = "row".getBytes();
    
  /** Cell right position tag. */
    private static final byte[] cellRightPosition = "cellx".getBytes();
    
  /** End cell tag. */
    private static final byte[] cellEnd = "cell".getBytes();
    
  /** In table tag. */
    private static final byte[] inTableBegin = "intbl".getBytes();
    
  /** Table Row alignment left tag. */
    private static final byte[] tableAlignLeft = "trql".getBytes();
    
  /** Table Row alignment center tag. */
    private static final byte[] tableAlignCenter = "trqc".getBytes();
    
  /** Table Row alignment right tag. */
    private static final byte[] tableAlignRight = "trqr".getBytes();
    
  /** Horizontal merge begin tag. */
    private static final byte[] mergeHorizontalBegin = "clmgf".getBytes();
    
  /** Horizontal merge cell tag. */
    private static final byte[] mergeHorizontal = "clmrg".getBytes();
    
  /** Vertical merge begin tag. */
    private static final byte[] mergeVerticalBegin = "clvmgf".getBytes();
    
  /** Vertical merge cell tag. */
    private static final byte[] mergeVertical = "clvmrg".getBytes();
    
  /** Left cell padding tag. */
    private static final byte[] tablePaddingLeft = "trpaddl".getBytes();
    
  /** Right cell padding tag. */
    private static final byte[] tablePaddingRight = "trpaddr".getBytes();
    
  /** Vertical cell alignment bottom tag. */
    private static final byte[] cellVertAlignBottom = "clvertalt".getBytes();
    
  /** Vertical cell alignment middle tag. */
    private static final byte[] cellVertAlignMiddle = "clvertalc".getBytes();
    
  /** Vertical cell alignment top tag. */
    private static final byte[] cellVertAlignTop = "clvertalb".getBytes();
    
    
  /**
   * Header / Footer
   */
    
  /** Begin header group tag. */
    private static final byte[] headerBegin = "header".getBytes();
    
  /** Begin footer group tag. */
    private static final byte[] footerBegin = "footer".getBytes();
    
    
  /**
   * Paper Properties
   */
    
  /** Paper width tag. */
    private static final byte[] rtfPaperWidth = "paperw".getBytes();
    
  /** Paper height tag. */
    private static final byte[] rtfPaperHeight = "paperh".getBytes();
    
  /** Margin left tag. */
    private static final byte[] rtfMarginLeft = "margl".getBytes();
    
  /** Margin right tag. */
    private static final byte[] rtfMarginRight = "margr".getBytes();
    
  /** Margin top tag. */
    private static final byte[] rtfMarginTop = "margt".getBytes();
    
  /** Margin bottom tag. */
    private static final byte[] rtfMarginBottom = "margb".getBytes();
    
    
  /**
   * Annotations
   */
    
  /** Annotation ID tag. */
    private static final byte[] annotationID = "atnid".getBytes();
    
  /** Annotation Author tag. */
    private static final byte[] annotationAuthor = "atnauthor".getBytes();
    
  /** Annotation text tag. */
    private static final byte[] annotation = "annotation".getBytes();
    
    
  /**
   * Images
   */
    
  /** Begin GIF / PNG image tag. */
    private static final byte[] imageGifPng = "pngblibp".getBytes();
    
  /** Begin JPEG image tag. */
    private static final byte[] imageJpeg = "jpegblibp".getBytes();
    
  /** Begin picture group tag. */
    private static final byte[] pictGroupBegin = "shppict".getBytes();
    
  /** Picture tag. */
    private static final byte[] picture = "pict".getBytes();
    
  /** Picture width tag. */
    private static final byte[] pictureWidth = "picwgoal".getBytes();
    
  /** Picture height tag. */
    private static final byte[] pictureHeight = "pichgoal".getBytes();
    
  /** Begin binary data tag. */
    private static final byte[] binaryData = "bin".getBytes();
    
    
    
  /** Class variables */
    
  /**
   * Because of the way RTF works and the way itext works, the text has to be
   * stored and is only written to the actual OutputStream at the end.
   */
    
  /** This <code>Vector</code> contains all fonts used in the document. */
    private static Vector fontList = new Vector();
    
  /** This <code>Vector</code> contains all colours used in the document. */
    private static Vector colorList = new Vector();
    
  /** This <code>ByteArrayOutputStream</code> contains the main body of the
   * document. */
    private static ByteArrayOutputStream content = null;
    
  /** This <code>ByteArrayOutputStream</code> contains the information group.
   */
    private static ByteArrayOutputStream info = null;
    
  /** This <code>ByteArrayOutputStream</code> is used when tables are created.
   */
    private static ByteArrayOutputStream[] rows = null;
    
  /** Which font is currently being used. */
    private static int listFont = 0;
    
  /** The current row number when processing tables. */
    private static int rowNr = 0;
    
  /** Flag for processing tables. */
    private static boolean tableProcessing = false;
    
  /** Document header. */
    private static HeaderFooter header = null;
    
  /** Document footer. */
    private static HeaderFooter footer = null;
    
  /** Left margin. */
    private static int marginLeft = 1800;
    
  /** Right margin. */
    private static int marginRight = 1800;
    
  /** Top margin. */
    private static int marginTop = 1440;
    
  /** Bottom margin. */
    private static int marginBottom = 1440;
    
  /** Page width. */
    private static int pageWidth = 12240;
    
  /** Page height. */
    private static int pageHeight = 15840;
    
  /** Factor to use when converting. */
    private static double twipsFactor = 20.5714;
    
  /** Current annotation ID. */
    private static int currentAnnotationID = 0;
    
    
  /** Protected Constructor */
    
/**
 * Constructs a <CODE>RtfWriter</CODE>.
 *
 * @param       document        The <CODE>Document</CODE> that has to be written as RTF
 * @param       os                      The <CODE>OutputStream</CODE> the writer has to write to.
 */
    
    protected RtfWriter(Document doc, OutputStream os) {
        super(doc, os);
        document.addDocListener(this);
        writeDocumentBegin();
        fontList.clear();
        colorList.clear();
        info = new ByteArrayOutputStream();
        content = new ByteArrayOutputStream();
        document.addProducer();
        document.addCreationDate();
    }
    
    
  /** Public functions from the DocWriter Interface */
    
/**
 * Gets an instance of the <CODE>RtfWriter</CODE>.
 *
 * @param       document        The <CODE>Document</CODE> that has to be written
 * @param       os      The <CODE>OutputStream</CODE> the writer has to write to.
 * @return      a new <CODE>RtfWriter</CODE>
 */
    
    public static RtfWriter getInstance(Document document, OutputStream os) {
        return(new RtfWriter(document, os));
    }
    
/**
 * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
 *
 * @return      <CODE>true</CODE> if the element was added, <CODE>false</CODE> if
 * not.
 * @throws      DocumentException       if a document isn't open yet, or has been closed
 */
    public boolean add(Element element) throws DocumentException
    {
        ByteArrayOutputStream cOut = null;
        if(tableProcessing) {
            cOut = rows[rowNr];
        }
        else {
            cOut = content;
        }
        try {
            switch(element.type()) {
                case Element.LISTITEM   : writeListElement((ListItem) element, cOut);
                break;
                case Element.LIST       : writeList((com.lowagie.text.List) element,
                cOut); break;
                case Element.CHAPTER    :
                case Element.SECTION    : writeSection((Section) element, cOut);
                break;
                case Element.PARAGRAPH  : writeParagraph((Paragraph) element, cOut);
                break;
                case Element.CHUNK      : writeChunk((Chunk) element, cOut);
                break;
                case Element.ANCHOR     :
                case Element.PHRASE     : element.process(this);
                break;
                case Element.TABLE      : writeTable((Table) element);
                break;
                case Element.ANNOTATION : writeAnnotation((Annotation) element, cOut);
                break;
                case Element.GIF        :
                case Element.PNG        : writeImage((Image) element, imageGifPng,
                cOut);   break;
                case Element.JPEG       : writeImage((Image) element, imageJpeg, cOut);
                break;
                
                case Element.AUTHOR       : writeMeta(metaAuthor, (Meta) element);
                break;
                case Element.SUBJECT      : writeMeta(metaSubject, (Meta) element);
                break;
                case Element.KEYWORDS     : writeMeta(metaKeywords, (Meta) element);
                break;
                case Element.TITLE        : writeMeta(metaTitle, (Meta) element);
                break;
                case Element.PRODUCER     : writeMeta(metaProducer, (Meta) element);
                break;
                case Element.CREATIONDATE : writeMeta(metaCreationDate, (Meta) element);
                break;
            }
        }
        catch(IOException e) {
            throw new ExceptionConverter(e);
        }
        return true;
    }
    
/**
 * Signals that the <CODE>Document</CODE> has been opened and that
 * <CODE>Elements</CODE> can be added.
 */
    public void open() {
        super.open();
    }
    
/**
 * Signals that the <CODE>Document</CODE> was closed and that no other
 * <CODE>Elements</CODE> will be added.
 * <p>
 * The content of the font table, color table, information group, content,
 * header, footer are merged into the final
 * <code>OutputStream</code>
 */
    public void close() {
        writeDocumentEnd();
        super.close();
    }
    
  /**
   * Adds the footer to the bottom of the <CODE>Document</CODE>.
   */
    public void setFooter(HeaderFooter footer) {
        this.footer = footer;
    }
    
  /**
   * Adds the header to the top of the <CODE>Document</CODE>.
   */
    public void setHeader(HeaderFooter header) {
        this.header = header;
    }
    
  /**
   * Resets the footer.
   */
    public void resetFooter() {
        this.footer = null;
    }
    
  /**
   * Resets the header.
   */
    public void resetHeader() {
        this.header = null;
    }
    
  /**
   * Tells the <code>RtfWriter</code> that a new page is to be begun.
   *
   * @return <code>true</code> if a new Page was begun.
   * @throws DocumentException if the Document was not open or had been closed.
   */
    public boolean newPage() throws DocumentException {
        try {
            content.write(escape);
            content.write(section);
            content.write(escape);
            content.write(sectionDefaults);
        }
        catch(IOException e) {
            throw new ExceptionConverter(e);
        }
        return true;
    }
    
  /**
   * Sets the page margins
   *
   * @param marginLeft The left margin
   * @param marginRight The right margin
   * @param marginTop The top margin
   * @param marginBottom The bottom margin
   *
   * @return <code>true</code> if the page margins were set.
   */
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        this.marginLeft = (int) (marginLeft * twipsFactor);
        this.marginRight = (int) (marginRight * twipsFactor);
        this.marginTop = (int) (marginTop * twipsFactor);
        this.marginBottom = (int) (marginBottom * twipsFactor);
        return true;
    }
    
  /**
   * Sets the page size
   *
   * @param pageSize A <code>Rectangle</code> specifying the page size
   *
   * @return <code>true</code> if the page size was set
   */
    public boolean setPageSize(Rectangle pageSize) {
        pageWidth = (int) (pageSize.width() * twipsFactor);
        pageHeight = (int) (pageSize.height() * twipsFactor);
        return true;
    }
    
  /** Private functions */
    
  /**
   * Write a <code>ListItem</code>
   *
   * @param listItem The <code>ListItem</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeListElement(ListItem listItem, ByteArrayOutputStream out) throws IOException {
        out.write(openGroup);
        out.write(escape);
        out.write(listText);
        out.write(escape);
        out.write(fontNumber);
        writeInt(out, listFont);
        out.write(escape);
        out.write(bulletCharacter);
        out.write(escape);
        out.write(tab);
        out.write(closeGroup);
        listItem.process(this);
        out.write(escape);
        out.write(paragraph);
    }
    
  /**
   * Write a <code>List</code>
   *
   * @param list The <code>List</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeList(com.lowagie.text.List list, ByteArrayOutputStream out) throws IOException {
        out.write(openGroup);
        out.write(extendedEscape);
        out.write(listBegin);
        out.write(escape);
        if(list.isNumbered()) {
            Font bulletFont = new Font(Font.COURIER, 10, Font.NORMAL, new
            Color(0, 0, 0));
            listFont = addFont(bulletFont);
            out.write(lvlbodyNumbering);
            out.write(escape);
            out.write(decimalNumbering);
        }
        else {
            Font bulletFont = new Font(Font.SYMBOL, 10, Font.NORMAL, new
            Color(0, 0, 0));
            listFont = addFont(bulletFont);
            out.write(listBullets);
        }
        out.write(escape);
        out.write(listFontNumber);
        writeInt(out, listFont);
        out.write(escape);
        out.write(listFirstIndent);
        writeInt(out, 0);
        out.write(closeGroup);
        out.write(escape);
        out.write(firstIndent);
        writeInt(out, 220);
        out.write(escape);
        out.write(listIndent);
        writeInt(out, 320+(int)(list.indentationLeft()*twipsFactor));
        list.process(this);
        out.write(escape);
        out.write(paragraphDefaults);
        out.write(escape);
        out.write(paragraph);
    }
    
  /**
   * Write the beginning of a new <code>Section</code>
   *
   * @param sectionElement The <code>Section</code> be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeSection(Section sectionElement, ByteArrayOutputStream out) throws IOException
    {
        if(sectionElement.type() == Element.CHAPTER) {
            out.write(escape);
            out.write(sectionDefaults);
        }
        if(sectionElement.title() != null) {
            out.write(escape);
            out.write(paragraphDefaults);
            sectionElement.title().process(this);
            out.write(escape);
            out.write(paragraph);
            out.write(escape);
            out.write(paragraph);
        }
        sectionElement.process(this);
        out.write(escape);
        out.write(paragraph);
        out.write(escape);
        out.write(paragraph);
        if(sectionElement.type() == Element.CHAPTER) {
            out.write(escape);
            out.write(section);
        }
    }
    
  /**
   * Write the beginning of a new <code>Paragraph</code>
   *
   * @param paragraphElement The <code>Paragraph</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeParagraph(Paragraph paragraphElement, ByteArrayOutputStream out) throws IOException {
        out.write(escape);
        out.write(paragraphDefaults);
        switch(paragraphElement.alignment()) {
            case Element.ALIGN_LEFT   : out.write(escape);
            out.write(alignLeft);   break;
            case Element.ALIGN_CENTER : out.write(escape);
            out.write(alignCenter); break;
            case Element.ALIGN_RIGHT  : out.write(escape);
            out.write(alignRight);  break;
        }
        out.write(escape);
        out.write(listIndent);
        writeInt(out, (int) (paragraphElement.indentationLeft()*twipsFactor));
        paragraphElement.process(this);
        out.write(escape);
        out.write(paragraph);
    }
    
  /**
   * Write a <code>Chunk</code> and all its font properties.
   *
   * @param chunk The <code>Chunk</code> item to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeChunk(Chunk chunk, ByteArrayOutputStream out) throws IOException {
        out.write(escape);
        out.write(fontNumber);
        if(chunk.font().family() != -1) {
            writeInt(out, addFont(chunk.font()));
        }
        else {
            writeInt(out, 0);
        }
        out.write(escape);
        out.write(fontSize);
        if(chunk.font().size() > 0) {
            writeInt(out, (int) chunk.font().size() * 2);
        }
        else {
            writeInt(out, 20);
        }
        out.write(escape);
        out.write(fontColor);
        writeInt(out, addColor(chunk.font().color()));
        switch(chunk.font().style()) {
            case Font.BOLD       :
                out.write(escape);
                out.write(bold);
                break;
            case Font.ITALIC     :
                out.write(escape);
                out.write(italic);
                break;
            case Font.UNDERLINE  :
                out.write(escape);
                out.write(underline);
                break;
            case Font.STRIKETHRU :
                out.write(escape);
                out.write(strikethrough);
                break;
            case Font.BOLDITALIC :
                out.write(escape);
                out.write(bold);
                out.write(escape);
                out.write(italic);
                break;
        }
        out.write(delimiter);
        out.write(chunk.content().getBytes());
        switch(chunk.font().style()) {
            case Font.BOLD :
                out.write(escape);
                out.write(bold);
                writeInt(out, 0);
                break;
            case Font.ITALIC :
                out.write(escape);
                out.write(italic);
                writeInt(out, 0);
                break;
            case Font.UNDERLINE :
                out.write(escape);
                out.write(underline);
                writeInt(out, 0);
                break;
            case Font.STRIKETHRU :
                out.write(escape);
                out.write(strikethrough);
                writeInt(out, 0);
                break;
            case Font.BOLDITALIC :
                out.write(escape);
                out.write(bold);
                writeInt(out,0);
                out.write(escape);
                out.write(italic);
                writeInt(out, 0);
                break;
        }
    }
    
    
  /**
   * Add a <code>Meta</code> element. It is written to the Inforamtion Group
   * and merged with the main <code>ByteArrayOutputStream</code> when the
   * Document is closed.
   *
   * @param metaName The type of <code>Meta</code> element to be added
   * @param meta The <code>Meta</code> element to be added
   *
   * @throws IOException
   */
    private void writeMeta(byte[] metaName, Meta meta) throws IOException {
        info.write(openGroup);
        info.write(escape);
        info.write(metaName);
        info.write(delimiter);
        if(meta.type() != Element.CREATIONDATE) {
            info.write(meta.content().getBytes());
        }
        else {
            writeFormatedDateTime(meta.content());
        }
        info.write(closeGroup);
    }
    
  /**
   * Writes a date. The date is formated <strong>Year, Month, Day, Hour,
   * Minute, Second</strong>
   *
   * @param date The date to be written
   *
   * @throws IOException
   */
    private void writeFormatedDateTime(String date) throws IOException {
        Calendar cal = Calendar.getInstance();
        DateFormat df = DateFormat.getInstance();
        try {
            cal.setTime(df.parse(date));
            info.write(escape);
            info.write(year);
            writeInt(info, cal.get(Calendar.YEAR));
            info.write(escape);
            info.write(month);
            writeInt(info, cal.get(Calendar.MONTH));
            info.write(escape);
            info.write(day);
            writeInt(info, cal.get(Calendar.DAY_OF_MONTH));
            info.write(escape);
            info.write(hour);
            writeInt(info, cal.get(Calendar.HOUR));
            info.write(escape);
            info.write(minute);
            writeInt(info, cal.get(Calendar.MINUTE));
            info.write(escape);
            info.write(second);
            writeInt(info, cal.get(Calendar.SECOND));
        }
        catch(java.text.ParseException e) {
            throw new ExceptionConverter(e);
        }
    }
    
  /**
   * Write a <code>Table</code>.
   *
   * @param table The <code>table</code> to be written
   *
   * @throws IOException
   */
    
    //  ATTENTION. THIS IS A KLUDGE
    //  It's ugly and it's slow and it works.
    //  The problem being that the structure of the iText library and
    //  the structure of tables in RTF are not too compatible
    private void writeTable(Table table) throws IOException {
        rows = new ByteArrayOutputStream[table.size()];
        Row currentRow = null;
        Element element = null;
        Iterator rowIterator = table.iterator();
        Iterator cellIterator = null;
        Vector emptyCells = new Vector();
        int tableWidth = 8500;
        String dummyStr = null;
        
        if(!table.absWidth().equals("")) { tableWidth =
        Integer.parseInt(table.absWidth()) * 29; }
        for(int i = 0; i < table.size(); i++) {
            rows[i] = new ByteArrayOutputStream();
        }
        rowNr = 0;
        tableProcessing = true;
        
        for(int i = 0; i < table.size(); i++) {
            currentRow = (Row) rowIterator.next();
            rowNr = i;
            rows[i].write(escape);
            rows[i].write(tableRowBegin);
            rows[i].write(escape);
            rows[i].write(inTableBegin);
            rows[i].write(escape);
            switch(table.alignment()) {
                case Element.ALIGN_LEFT   : rows[i].write(tableAlignLeft);
                break;
                case Element.ALIGN_CENTER : rows[i].write(tableAlignCenter);
                break;
                case Element.ALIGN_RIGHT  : rows[i].write(tableAlignRight);
                break;
            }
            rows[i].write(escape);
            rows[i].write(tablePaddingLeft);
            writeInt(rows[i], 10);
            rows[i].write(escape);
            rows[i].write(tablePaddingRight);
            writeInt(rows[i], 10);
            for(int j = 0; j < currentRow.columns(); j++) {
                Cell currentCell = null;
                try {
                    currentCell = (Cell)currentRow.getCell(j);
                    if (currentCell == null) {
                        continue;
                    }
                }
                catch(ClassCastException cce) {
                    continue;
                }
                if(emptyCells.indexOf(new String(j+"-"+i+"h")) == -1) {
                    if(currentCell.colspan() != 1) {
                        rows[i].write(escape);
                        rows[i].write(mergeHorizontalBegin);
                        for(int k = j + 1; k <= j + currentCell.colspan()-1; k++) {
                            emptyCells.add(new String(k+"-"+i+"h"));
                        }
                    }
                }
                else {
                    rows[i].write(escape);
                    rows[i].write(mergeHorizontal);
                }
                if(emptyCells.indexOf(new String(j+"-"+i+"v")) == -1) {
                    if(currentCell.rowspan() != 1) {
                        rows[i].write(escape);
                        rows[i].write(mergeVerticalBegin);
                        for(int k = i + 1; k <= i + currentCell.rowspan()-1;  k++) {
                            emptyCells.add(new String(j+"-"+k+"v"));
                        }
                    }
                }
                else {
                    rows[i].write(escape);
                    rows[i].write(mergeVertical);
                }
                if(currentRow.getCell(j) != null) {
                    rows[i].write(escape);
                    switch(currentCell.verticalAlignment()) {
                        case Element.ALIGN_BASELINE :
                        case Element.ALIGN_BOTTOM   :
                            rows[i].write(cellVertAlignBottom); break;
                        case Element.ALIGN_MIDDLE   :
                            rows[i].write(cellVertAlignMiddle); break;
                        case Element.ALIGN_TOP      :
                            rows[i].write(cellVertAlignTop);    break;
                            default                     :
                                rows[i].write(cellVertAlignBottom);
                    }
                }
                rows[i].write(escape);
                rows[i].write(cellRightPosition);
                writeInt(rows[i], (int) tableWidth / currentRow.columns() *
                (j+1) );
            }
            rows[i].write(openGroup);
            for(int j = 0; j < currentRow.columns(); j++) {
                if((emptyCells.indexOf(new String(j+"-"+i+"h")) == -1) &&
                (emptyCells.indexOf(new String(j+"-"+i+"v")) == -1)) {
                    Cell currentCell = null;
                    try {
                        currentCell = (Cell)currentRow.getCell(j);
                        if (currentCell == null) {
                            continue;
                        }
                    }
                    catch(ClassCastException cce) {
                        continue;
                    }
                    for(cellIterator = currentCell.getElements(); cellIterator.hasNext();) {
                        element = (Element) cellIterator.next();
                        rows[i].write(escape);
                        rows[i].write(paragraphDefaults);
                        rows[i].write(escape);
                        rows[i].write(inTableBegin);
                        rows[i].write(escape);
                        switch(currentCell.horizontalAlignment()) {
                            case Element.ALIGN_LEFT   :
                                rows[i].write(alignLeft);   break;
                            case Element.ALIGN_CENTER :
                                rows[i].write(alignCenter); break;
                            case Element.ALIGN_RIGHT  :
                                rows[i].write(alignRight);  break;
                        }
                        element.process(this);
                        rows[i].write(escape);
                        rows[i].write(paragraph);
                    }
                }
                rows[i].write(escape);
                rows[i].write(cellEnd);
            }
            rows[i].write(closeGroup);
            rows[i].write(escape);
            rows[i].write(tableRowEnd);
        }
        tableProcessing = false;
        for(int i = 0; i < rows.length; i++) {
            rows[i].writeTo(content);
        }
        content.write(escape);
        content.write(paragraphDefaults);
        content.write(escape);
        content.write(paragraph);
    }
    
  /**
   * Write an <code>Annotation</code>
   *
   * @param annotationElement The <code>Annotation</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeAnnotation(Annotation annotationElement, ByteArrayOutputStream out) throws IOException {
        out.write(openGroup);
        out.write(escape);
        out.write(annotationID);
        writeInt(out, currentAnnotationID);
        out.write(closeGroup);
        out.write(openGroup);
        out.write(escape);
        out.write(annotationAuthor);
        out.write("itext".getBytes());
        out.write(closeGroup);
        out.write(openGroup);
        out.write(escape);
        out.write(annotation);
        out.write(delimiter);
        annotationElement.process(this);
        out.write(closeGroup);
    }
    
  /**
   * Write am <code>Image</code> and all its font properties.
   *
   * @param image The <code>Image</code> to be written
   * @param imageType The type of image to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    
    //  I'm having problems with this, because image.rawData() always return null
    //  Don't know why
    private void writeImage(Image image, byte[] imageType, ByteArrayOutputStream out) throws IOException {
        if(image.rawData() != null) {
            out.write(escape);
            switch(image.alignment()) {
                case Element.ALIGN_LEFT   : out.write(alignLeft);   break;
                case Element.ALIGN_CENTER : out.write(alignCenter); break;
                case Element.ALIGN_RIGHT  : out.write(alignRight);  break;
                default                   : out.write(alignLeft);
            }
            out.write(openGroup);
            out.write(extendedEscape);
            out.write(pictGroupBegin);
            out.write(openGroup);
            out.write(escape);
            out.write(picture);
            out.write(escape);
            out.write(imageType);
            out.write(escape);
            out.write(pictureWidth);
            writeInt(out, (int) image.scaledWidth());
            out.write(escape);
            out.write(pictureHeight);
            writeInt(out, (int) image.scaledHeight());
            out.write(escape);
            out.write(binaryData);
            out.write(delimiter);
            out.write(image.rawData());
            out.write(closeGroup);
            out.write(closeGroup);
        }
    }
    
  /**
   * Write the initialisation and basic RTF information.
   *
   * @return <code>true</code> if the initialisation was sucessfully written
   */
    private boolean writeDocumentBegin() {
        try {
            os.write(openGroup);
            os.write(escape);
            os.write(docBegin);
            os.write(escape);
            os.write(ansi);
            os.write(escape);
            os.write(ansiCodepage);
            writeInt(os, 1252);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
  /**
   * Add a new <code>Font</code> to the list of fonts. If the <code>Font</code>
   * already exists in the list of fonts, then it is not added again.
   *
   * @param newFont The <code>Font</code> to be added
   *
   * @return The index of the <code>Font</code> in the font list
   */
    private int addFont(Font newFont) {
        int fn = -1;
        
        for(int i = 0; i < fontList.size(); i++) {
            if(newFont.family() == ((Font)fontList.get(i)).family()) {
                fn = i;
            }
        }
        if(fn == -1) {
            fontList.add(newFont);
            return fontList.size()-1;
        }
        return fn;
    }
    
  /**
   * Add a new <code>Color</code> to the list of colours. If the
   * <code>Color</code>
   * already exists in the list of colours, then it is not added again.
   *
   * @param newColor The <code>Color</code> to be added
   *
   * @return The index of the <code>color</code> in the colour list
   */
    private int addColor(Color newColor) {
        int cn = 0;
        if(newColor == null) { return cn; }
        cn = colorList.indexOf(newColor);
        if(cn == -1)
        {
            colorList.add(newColor);
            return colorList.size()-1;
        }
        return cn;
    }
    
  /**
   * Merge all the different <code>Vector</code>s and
   * <code>ByteArrayOutputStream</code>s
   * to the final <code>ByteArrayOutputStream</code>
   *
   * @return <code>true</code> if all information was sucessfully written to
   * the <code>ByteArrayOutputStream</code>
   */
    private boolean writeDocumentEnd() {
        try {
            writeFontList();
            writeColorList();
            writeDocumentFormat();
            writeInfoList();
            writeHeader();
            writeFooter();
            content.writeTo(os);
            os.write(closeGroup);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }
    
  /**
   * Write the font list to the final <code>ByteArrayOutputStream</code>
   */
    private void writeFontList() throws IOException {
        Font fnt;
        
        os.write(openGroup);
        os.write(escape);
        os.write(fontTable);
        os.write(escape);
        os.write("deff0".getBytes());
        for(int i = 0; i < fontList.size(); i++) {
            fnt = (Font) fontList.get(i);
            os.write(openGroup);
            os.write(escape);
            os.write((byte) 'f');
            writeInt(os, i);
            os.write(escape);
            switch(fnt.family()) {
                case Font.COURIER:
                    os.write(fontModern);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 0);
                    os.write(delimiter);
                    os.write(fontCourier);
                    break;
                case Font.HELVETICA:
                    os.write(fontSwiss);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 0);
                    os.write(delimiter);
                    os.write(fontArial);
                    break;
                case Font.SYMBOL:
                    os.write(fontRoman);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 2);
                    os.write(delimiter);
                    os.write(fontSymbol);
                    break;
                case Font.TIMES_NEW_ROMAN:
                    os.write(fontRoman);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 0);
                    os.write(delimiter);
                    os.write(fontTimesNewRoman);
                    break;
                case Font.ZAPFDINGBATS:
                    os.write(fontTech);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 0);
                    os.write(delimiter);
                    os.write(fontWindings);
                    break;
            }
            os.write(commaDelimiter);
            os.write(closeGroup);
        }
        os.write(closeGroup);
    }
    
  /**
   * Write the colour list to the final <code>ByteArrayOutputStream</code>
   */
    private void writeColorList() throws IOException {
        Color color = null;
        
        os.write(openGroup);
        os.write(escape);
        os.write(colorTable);
        for(int i = 0; i < colorList.size(); i++) {
            color = (Color) colorList.get(i);
            os.write(escape);
            os.write(colorRed);
            writeInt(os, color.getRed());
            os.write(escape);
            os.write(colorGreen);
            writeInt(os, color.getGreen());
            os.write(escape);
            os.write(colorBlue);
            writeInt(os, color.getBlue());
            os.write(commaDelimiter);
        }
        os.write(closeGroup);
    }
    
  /**
   * Write the Information Group to the final
   * <code>ByteArrayOutputStream</code>
   */
    private void writeInfoList() throws IOException {
        try {
            os.write(openGroup);
            os.write(escape);
            os.write(infoBegin);
            info.writeTo(os);
            os.write(closeGroup);
        }
        catch(IOException e) {
            os.write(closeGroup);
            throw new ExceptionConverter(e);
        }
    }
    
  /**
   * Write an integer
   *
   * @param out The <code>OuputStream</code> to which the <code>int</code>
   * value is to be written
   * @param i The <code>int</code> value to be written
   */
    private void writeInt(OutputStream out, int i) throws IOException {
        out.write(Integer.toString(i).getBytes());
    }
    
    
  /**
   * Write the header to the final <code>ByteArrayOutputStream</code>
   */
    private void writeHeader() throws IOException {
        if(header != null) {
            os.write(openGroup);
            os.write(escape);
            os.write(headerBegin);
            rows = new ByteArrayOutputStream[1];
            rows[0] = new ByteArrayOutputStream();
            rowNr = 0;
            tableProcessing = true;
            header.process(this);
            tableProcessing = false;
            rows[0].writeTo(os);
            os.write(closeGroup);
        }
    }
    
  /**
   * Write the footer to the final <code>ByteArrayOutputStream</code>
   */
    private void writeFooter() throws IOException {
        if(footer != null) {
            os.write(openGroup);
            os.write(escape);
            os.write(footerBegin);
            rows = new ByteArrayOutputStream[1];
            rows[0] = new ByteArrayOutputStream();
            rowNr = 0;
            tableProcessing = true;
            footer.process(this);
            tableProcessing = false;
            rows[0].writeTo(os);
            os.write(closeGroup);
        }
    }
    
  /**
   * Write the <code>Document</code>'s Paper and Margin Size
   *  to the final <code>ByteArrayOutputStream</code>
   */
    private void writeDocumentFormat() throws IOException {
        os.write(openGroup);
        os.write(escape);
        os.write(rtfPaperWidth);
        writeInt(os, pageWidth);
        os.write(escape);
        os.write(rtfPaperHeight);
        writeInt(os, pageHeight);
        os.write(escape);
        os.write(rtfMarginLeft);
        writeInt(os, marginLeft);
        os.write(escape);
        os.write(rtfMarginRight);
        writeInt(os, marginRight);
        os.write(escape);
        os.write(rtfMarginTop);
        writeInt(os, marginTop);
        os.write(escape);
        os.write(rtfMarginBottom);
        writeInt(os, marginBottom);
        os.write(closeGroup);
    }
    
}
/*
 * $Id$
 * $Name$
 *
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
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
package com.lowagie.text.html;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Iterator;
import com.lowagie.text.Anchor;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Header;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.List;
import com.lowagie.text.Meta;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Row;
import com.lowagie.text.Section;
import com.lowagie.text.Table;

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
 *
 * @author  bruno@lowagie.com
 */

public class HtmlWriter extends DocWriter implements DocListener {
    
    // static membervariables (tags)
    
/** This is some byte that is often used. */
    public static final byte NEWLINE = (byte)'\n';
    
/** This is some byte that is often used. */
    public static final byte TAB = (byte)'\t';
    
/** This is some byte that is often used. */
    public static final byte START = (byte)'<';
    
/** This is some byte that is often used. */
    public static final byte END = (byte)'>';
    
/** This is some byte that is often used. */
    public static final byte ENDTAG = (byte)'/';
    
/** This is a possible HTML-tag. */
    public static final byte[] BEGINCOMMENT = getISOBytes("<!--");
    
/** This is a possible HTML-tag. */
    public static final byte[] ENDCOMMENT = getISOBytes("-->");
    
/** This is a possible HTML-tag. */
    public static final byte[] A = getISOBytes("A");
    
/** This is a possible HTML-tag. */
    public static final byte[] B = getISOBytes("B");
    
/** This is a possible HTML-tag. */
    public static final byte[] BR = getISOBytes("BR");
    
/** This is a possible HTML-tag. */
    public static final byte[] BODY = getISOBytes("BODY");
    
/** This is a possible HTML-tag. */
    public static final byte[] FONT = getISOBytes("FONT");
    
/** This is a possible HTML-tag. */
    public static final byte[] HEAD = getISOBytes("HEAD");
    
/** This is a possible HTML-tag. */
    public static final byte[] HTML = getISOBytes("HTML");
    
/** This is a possible HTML-tag. */
    public static final byte[] I = getISOBytes("I");
    
/** This is a possible HTML-tag. */
    public static final byte[] IMG = getISOBytes("IMG");
    
/** This is a possible HTML-tag. */
    public static final byte[] LI = getISOBytes("LI");
    
/** This is a possible HTML-tag. */
    public static final byte[] LINK = getISOBytes("LINK");
    
/** This is a possible HTML-tag. */
    public static final byte[] META = getISOBytes("META");
    
/** This is a non breaking space. */
    public static final byte[] NBSP = getISOBytes("&nbsp;");
    
/** This is a possible HTML-tag. */
    public static final byte[] OL = getISOBytes("OL");
    
/** This is a possible HTML-tag. */
    public static final byte[] PARAGRAPH = getISOBytes("P");
    
/** This is a possible HTML-tag. */
    public static final byte[] S = getISOBytes("S");
    
/** This is a possible HTML-tag. */
    public static final byte[] TABLE = getISOBytes("TABLE");
    
/** This is a possible HTML-tag. */
    public static final byte[] TD = getISOBytes("TD");
    
/** This is a possible HTML-tag. */
    public static final byte[] TH = getISOBytes("TH");
    
/** This is a possible HTML-tag. */
    public static final byte[] TITLE = getISOBytes("TITLE");
    
/** This is a possible HTML-tag. */
    public static final byte[] TR = getISOBytes("TR");
    
/** This is a possible HTML-tag. */
    public static final byte[] U = getISOBytes("U");
    
/** This is a possible HTML-tag. */
    public static final byte[] UL = getISOBytes("UL");
    
    // static membervariables (attributes)
    
/** This is a possible HTML attribute for the HEAD tag. */
    public static final String FONTS[] = new String[5];
    
    static {
        FONTS[0] = "Courier";
        FONTS[1] = "Helvetica, Arial";
        FONTS[2] = "Times New Roman, Times";
        FONTS[3] = "Symbol";
        FONTS[4] = "ZapfDingbats, WingDings";
    }
    
/** This is a possible HTML attribute for the P, TABLE tag. */
    public static final String ALIGN = "ALIGN";
    
/** This is a possible HTML attribute for the P, TABLE tag. */
    public static final String ALT = "ALT";
    
/** This is a possible HTML attribute for the TABLE tag. */
    public static final String BGCOLOR = "BGCOLOR";
    
/** This is a possible HTML attribute for the BODY tag. */
    public static final String BOTTOMMARGIN = "BOTTOMMARGIN";
    
/** This is a possible HTML attribute for the TABLE tag. */
    public static final String BORDER = "BORDER";
    
/** This is a possible HTML attribute for the TABLE tag. */
    public static final String BORDERCOLOR = "BORDERCOLOR";
    
/** This is a possible HTML attribute for the TABLE tag. */
    public static final String CELLPADDING = "CELLPADDING";
    
/** This is a possible HTML attribute for the TABLE tag. */
    public static final String CELLSPACING = "CELLSPACING";
    
/** This is a possible HTML attribute for the TD tag. */
    public static final String COLSPAN = "COLSPAN";
    
/** This is a possible HTML attribute for the HEAD tag. */
    public static final String CONTENT = "CONTENT";
    
/** This is a possible HTML attribute for the HEAD tag. */
    public static final String COLOR = "COLOR";
    
/** This is a possible HTML attribute for the LINK tag. */
    public static final String CSS = "text/css";
    
/** This is a possible HTML attribute for the HEAD tag. */
    public static final String FACE = "FACE";
    
/** This is a possible HTML attribute for the A tag. */
    public static final String HEIGHT = "HEIGHT";
    
/** This is a possible HTML attribute for the A tag. */
    public static final String HREF = "HREF";
    
/** This is a possible HTML attribute for the BODY tag. */
    public static final String LEFTMARGIN = "LEFTMARGIN";
    
/** This is a possible HTML attribute for the A, HEAD tag. */
    public static final String NAME = "NAME";
    
/** This is a possible HTML attribute for the NOWRAP tag. */
    public static final String NOWRAP = "NOWRAP";
    
/** This is a possible HTML attribute for the HEAD tag. */
    public static final String POINTSIZE = "POINT-SIZE";
    
/** This is a possible HTML attribute for the LINK tag. */
    public static final String REL = "rel";
    
/** This is a possible HTML attribute for the BODY tag. */
    public static final String RIGHTMARGIN = "RIGHTMARGIN";
    
/** This is a possible HTML attribute for the TD tag. */
    public static final String ROWSPAN = "ROWSPAN";
    
/** This is a possible HTML attribute for the TD tag. */
    public static final String SRC = "SRC";
    
/** This is a possible HTML attribute for the TD tag. */
    public static final String STYLESHEET = "STYLESHEET";
    
/** This is a possible HTML attribute for the BODY tag. */
    public static final String TEXT = "TEXT";
    
/** This is a possible HTML attribute for the BODY tag. */
    public static final String TOPMARGIN = "TOPMARGIN";
    
/** This is a possible HTML attribute for the LINK tag. */
    public static final String TYPE = "TYPE";
    
/** This is a possible HTML attribute for the TD tag. */
    public static final String VALIGN = "VALIGN";
    
/** This is a possible HTML attribute for the A tag. */
    public static final String WIDTH = "WIDTH";
    
    // membervariables
    
/** This represents the indentation of the HTML. */
    private int indent = 0;
    
/** This is the current font of the HTML. */
    private Font font = new Font();
    
/** This is the standard font of the HTML. */
    private Font standardFont = new Font();
    
/** This is a path for images. */
    private String imagepath = null;
    
/** Stores the page number. */
    private static int pageN = 0;
    
/** This is the textual part of a header */
    private HeaderFooter header = null;
    
/** This is the textual part of the footer */
    private HeaderFooter footer = null;
    
    // constructor
    
/**
 * Constructs a <CODE>HtmlWriter</CODE>.
 *
 * @param	document	The <CODE>Document</CODE> that has to be written as HTML
 * @param	os			The <CODE>OutputStream</CODE> the writer has to write to.
 */
    
    protected HtmlWriter(Document doc, OutputStream os) {
        super(doc, os);
        document.addDocListener(this);
        this.pageN = document.getPageNumber();
        try {
            writeBeginTag(HTML);
            writeBeginTag(HEAD);
            document.addProducer();
            document.addCreationDate();
        }
        catch(IOException ioe) {
        }
    }
    
    // get an instance of the HtmlWriter
    
/**
 * Gets an instance of the <CODE>HtmlWriter</CODE>.
 *
 * @param	document	The <CODE>Document</CODE> that has to be written
 * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
 * @return	a new <CODE>HtmlWriter</CODE>
 */
    
    public static HtmlWriter getInstance(Document document, OutputStream os) {
        return new HtmlWriter(document, os);
    }
    
    // implementation of the DocListener methods
    
/**
 * Signals that an new page has to be started.
 * <P>
 * Writes a horizontal rule.
 *
 * @return	<CODE>true</CODE> if this action succeeded, <CODE>false</CODE> if not.
 * @throws	DocumentException	when a document isn't open yet, or has been closed
 */
    
    public boolean newPage() throws DocumentException {
        if (pause || !open) {
            return false;
        }
        try {
            os.write(getISOBytes("<HR>\n"));
            return true;
        }
        catch(IOException ioe) {
            return false;
        }
    }
    
/**
 * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
 *
 * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
 * @throws	DocumentException	when a document isn't open yet, or has been closed
 */
    
    public boolean add(Element element) throws DocumentException {
        if (pause) {
            return false;
        }
        Font difference = null;
        if (element.type() == Element.CHUNK) {
            Chunk chunk = (Chunk) element;
            if (chunk.font() != null && chunk.font().compareTo(standardFont) != 0) {
                difference = chunk.font().difference(font);
                try {
                    setFont(difference);
                }
                catch(IOException ioe) {
                    throw new DocumentException(ioe.getMessage());
                }
            }
        }
        HtmlAttributes attributes = new HtmlAttributes();
        try {
            switch(element.type()) {
                case Element.HEADER:
                    Header h = (Header) element;
                    if (!STYLESHEET.equals(h.name())) {
                        writeHeader(h);
                    }
                    else {
                        writeLink(h);
                    }
                    break;
                case Element.SUBJECT:
                case Element.KEYWORDS:
                case Element.AUTHOR:
                    Meta meta = (Meta) element;
                    writeHeader(meta);
                    break;
                case Element.TITLE:
                    writeBeginTag(TITLE);
                    tab();
                    write(((Meta)element).content());
                    newLine();
                    writeEndTag(TITLE);
                    break;
                case Element.PRODUCER:
                    writeComment("Producer: " + ((Meta)element).content());
                    break;
                case Element.CREATIONDATE:
                    writeComment("Creationdate: " + ((Meta)element).content());
                    break;
                case Element.CHAPTER:
                case Element.SECTION:
                    Section section = (Section) element;
                    if (section.title() != null) {
                        section.title().process(this);
                        newLine();
                        writeBreak();
                        writeBreak();
                    }
                    section.process(this);
                    writeBreak();
                    break;
                case Element.CHUNK:
                    tab();
                    write(HtmlEncoder.encode(((Chunk)element).content()));
                    newLine();
                    break;
                case Element.PHRASE:
                    element.process(this);
                    break;
                case Element.LISTITEM:
                    writeBeginTag(LI);
                    element.process(this);
                    writeEndTag(LI);
                    break;
                case Element.LIST:
                    List list = (List) element;
                    if (list.isNumbered()) {
                        writeBeginTag(OL);
                    }
                    else {
                        writeBeginTag(UL);
                    }
                    element.process(this);
                    if (list.isNumbered()) {
                        writeEndTag(OL);
                    }
                    else {
                        writeEndTag(UL);
                    }
                    break;
                case Element.PARAGRAPH:
                    attributes.put(ALIGN, HtmlEncoder.getAlignment(((Paragraph) element).alignment()));
                    writeBeginTag(PARAGRAPH, attributes);
                    element.process(this);
                    writeEndTag(PARAGRAPH);
                    break;
                case Element.ANCHOR:
                    Anchor anchor = (Anchor) element;
                    if (anchor.reference() != null) {
                        attributes.put(HREF, anchor.reference());
                    }
                    if (anchor.name() != null) {
                        attributes.put(NAME, anchor.name());
                    }
                    writeBeginTag(A, attributes);
                    element.process(this);
                    writeEndTag(A);
                    break;
                case Element.GIF:
                case Element.JPEG:
                case Element.PNG:
                    Image image = (Image) element;
                    String path = image.url().toString();
                    // if an imagepath is defined, the path is changed
                    if (imagepath != null) {
                        if (path.indexOf("/") > 0) {
                            path = imagepath + path.substring(path.lastIndexOf("/") + 1);
                        }
                        else {
                            path = imagepath + path;
                        }
                    }
                    attributes.put(SRC, path);
                    if ((image.alignment() & Image.MIDDLE) == Image.MIDDLE) {
                        attributes.put(ALIGN, "Middle");
                    }
                    else if ((image.alignment() & Image.LEFT) == Image.LEFT) {
                        attributes.put(ALIGN, "Left");
                    }
                    else if ((image.alignment() & Image.MIDDLE) == Image.MIDDLE) {
                        attributes.put(ALIGN, "Right");
                    }
                    if (image.alt() != null) {
                        attributes.put(ALT, image.alt());
                    }
                    if (!image.hasBorders()) {
                        attributes.put(BORDER, "0");
                    }
                    if (image.scaledWidth() != 0) {
                        attributes.put(WIDTH, String.valueOf(image.scaledWidth()));
                    }
                    if (image.scaledHeight() != 0) {
                        attributes.put(HEIGHT, String.valueOf(image.scaledHeight()));
                    }
                    writeBeginTag(IMG, attributes);
                    indent--;
                    break;
                case Element.TABLE:
                    Table table = (Table) element;
                    table.complete();   // correct table : fill empty cells/ parse table in table
                    attributes.put(ALIGN, HtmlEncoder.getAlignment(table.alignment()));
                    if (table.cellpadding() > 0) {
                        attributes.put(CELLPADDING, String.valueOf(table.cellpadding()));
                    }
                    //Added 03/01/2001 David Freels
                    //Adds the width attribute to the table tag
                    //Changed by Evelyne De Cordier
                    if (!table.absWidth().equals("")) {
                        attributes.put(WIDTH, String.valueOf(table.absWidth()));
                    } else if (table.widthPercentage() > 0) {
                        attributes.put(WIDTH, String.valueOf(table.widthPercentage())+"%");
                    }
                    if (table.cellspacing() > 0) {
                        attributes.put(CELLSPACING, String.valueOf(table.cellspacing()));
                    }
                    if (table.borderWidth() > 0) {
                        attributes.put(BORDER, String.valueOf((int) table.borderWidth()));
                    }
                    if (table.backgroundColor() != null) {
                        attributes.put(BGCOLOR, HtmlEncoder.encode(table.backgroundColor()));
                    }
                    if (table.borderColor() != null) {
                        attributes.put(BORDERCOLOR, HtmlEncoder.encode(table.borderColor()));
                    }
                    writeBeginTag(TABLE, attributes);
                    for (Iterator rowIterator = table.iterator(); rowIterator.hasNext(); ) {
                        ((Row) rowIterator.next()).process(this);
                    }
                    writeEndTag(TABLE);
                    break;
                case Element.ROW:
                    Row row = (Row) element;
                    if (! row.isEmpty()) {
                        attributes.put(ALIGN, HtmlEncoder.getAlignment(row.horizontalAlignment()));
                        attributes.put(VALIGN, HtmlEncoder.getAlignment(row.verticalAlignment()));
                        writeBeginTag(TR, attributes);
                        for(int i = 0; i < row.columns(); i++) {
                            if (row.getCell(i) != null) {
                                ((Element)(row.getCell(i))).process(this);
                            }
                        }
                        writeEndTag(TR);
                    }
                    break;
                case Element.CELL:
                    Cell cell = (Cell) element;
                    attributes.put(ALIGN, HtmlEncoder.getAlignment(cell.horizontalAlignment()));
                    attributes.put(VALIGN, HtmlEncoder.getAlignment(cell.verticalAlignment()));
                    if (cell.backgroundColor() != null) {
                        attributes.put(BGCOLOR, HtmlEncoder.encode(cell.backgroundColor()));
                    }
                    if (cell.borderColor() != null) {
                        attributes.put(BORDERCOLOR, HtmlEncoder.encode(cell.borderColor()));
                    }
                    if (cell.rowspan() > 1) {
                        attributes.put(ROWSPAN, String.valueOf(cell.rowspan()));
                    }
                    if (cell.colspan() > 1) {
                        attributes.put(COLSPAN, String.valueOf(cell.colspan()));
                    }
                    if (cell.noWrap()) {
                        attributes.put(NOWRAP, null);
                    }
                    if (cell.header()) {
                        writeBeginTag(TH, attributes);
                    }
                    else {
                        writeBeginTag(TD, attributes);
                    }
                    if (cell.isEmpty()) {
                        os.write(NBSP);
                    }
                    else {
                        for (Iterator i = cell.getElements(); i.hasNext(); ) {
                            Element el = (Element)i.next();
                            // this is a hack!!! (LWG 7 june 2000)
                            if (el.type() == Element.PARAGRAPH) {
                                HtmlAttributes att = new HtmlAttributes();
                                att.put(ALIGN, HtmlEncoder.getAlignment(cell.horizontalAlignment()));
                                writeBeginTag(PARAGRAPH, att);
                                el.process(this);
                                writeEndTag(PARAGRAPH);
                            }
                            // bugfix by Paulo (july 2001)
                            else if (el.type() == Element.ANCHOR) {
                                add(el);
                            }
                            else {
                                el.process(this);
                            }
                        }
                    }
                    if (cell.header()) {
                        writeEndTag(TH);
                    }
                    else {
                        writeEndTag(TD);
                    }
                    break;
                    default:
                        return false;
            }
            if (difference != null) {
                resetFont(difference);
            }
            return true;
        }
        catch(IOException ioe) {
            throw new DocumentException(ioe.getMessage());
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
            writeEndTag(HEAD);
            HtmlAttributes attributes = new HtmlAttributes();
            if (font != null && font.color() != null) {
                attributes.put(TEXT, HtmlEncoder.encode(font.color()));
            }
            if (document.leftMargin() > 0) {
                attributes.put(LEFTMARGIN, String.valueOf(document.leftMargin()));
            }
            if (document.rightMargin() > 0) {
                attributes.put(RIGHTMARGIN, String.valueOf(document.rightMargin()));
            }
            if (document.topMargin() > 0) {
                attributes.put(TOPMARGIN, String.valueOf(document.topMargin()));
            }
            if (document.bottomMargin() > 0) {
                attributes.put(BOTTOMMARGIN, String.valueOf(document.bottomMargin()));
            }
            if (pageSize.backgroundColor() != null) {
                attributes.put(BGCOLOR, HtmlEncoder.encode(pageSize.backgroundColor()));
            }
            writeBeginTag(BODY, attributes);
            
            //Add a header if available (added by David Freels)
            initHeader();
        }
        catch(IOException ioe) {
        }
    }
    
/**
 * Signals that the <CODE>Document</CODE> was closed and that no other
 * <CODE>Elements</CODE> will be added.
 */
    
    public void close() {
        try {
            initFooter(); // line added by David Freels
            writeEndTag(BODY);
            writeEndTag(HTML);
            super.close();
        }
        catch(IOException ioe) {
        }
    }
    
    // some private methods
    
/**
 * Adds the header to the top of the </CODE>Document</CODE>
 *
 * @author	David Freels
 */
    
    private void initHeader() {
        if (header != null) {
            try {
                add(header.paragraph());
                newLine();
            }
            catch(Exception e) {
            }
        }
    }
    
/**
 *  Adds the header to the top of the </CODE>Document</CODE>
 *
 * @author	David Freels
 */
    
    private void initFooter() {
        if (footer != null)	{
            try {
                // Set the page number. HTML has no notion of a page, so it should always
                // add up to 1
                footer.setPageNumber(HtmlWriter.pageN + 1);
                add(footer.paragraph());
            }
            catch(Exception e) {
            }
        }
    }
    
/**
 * Writes a new line to the outputstream.
 *
 * @throws	IOException
 */
    
    private void newLine() throws IOException {
        os.write(NEWLINE);
    }
    
/**
 * Writes a number of tabs to the outputstream.
 *
 * @throws	IOException
 */
    
    private void tab() throws IOException {
        for (int i = 0; i < indent; i++) {
            os.write(TAB);
        }
    }
    
/**
 * Writes a linebreak.
 *
 * @throws	IOException
 */
    
    private void writeBreak() throws IOException {
        tab();
        os.write(START);
        os.write(BR);
        os.write(END);
        newLine();
    }
    
/**
 * Writes a begin tag.
 * <P>
 * This method writes a given tag between brackets.
 *
 * @param	tag		the tag that has to be written
 * @throws	IOException
 */
    
    private void writeBeginTag(byte[] tag) throws IOException {
        tab();
        os.write(START);
        os.write(tag);
        os.write(END);
        indent++;
        newLine();
    }
    
/**
 * Writes a begin tag.
 * <P>
 * This method writes a given tag between brackets.
 *
 * @param	tag			the tag that has to be written
 * @param	attributes	the attributes of the tag
 * @throws	IOException
 */
    
    private void writeBeginTag(byte[] tag, HtmlAttributes attributes) throws IOException {
        tab();
        os.write(START);
        os.write(tag);
        write(attributes.toString());
        os.write(END);
        indent++;
        newLine();
    }
    
/**
 * Writes an end tag.
 * <P>
 * This method writes a given tag between brackets.
 *
 * @param	tag		the tag that has to be written
 * @throws	IOException
 */
    
    private void writeEndTag(byte[] tag) throws IOException {
        indent--;
        tab();
        os.write(START);
        os.write(ENDTAG);
        os.write(tag);
        os.write(END);
        newLine();
    }
    
/**
 * Writes a Metatag in the header.
 *
 * @param	element		the element that has to be written
 * @throws	IOException
 */
    
    private void writeHeader(Meta meta) throws IOException {
        HtmlAttributes attributes = new HtmlAttributes();
        attributes.put(CONTENT, meta.content());
        switch(meta.type()) {
            case Element.HEADER:
                attributes.put(NAME, ((Header) meta).name());
                break;
            case Element.SUBJECT:
                attributes.put(NAME, "subject");
                break;
            case Element.KEYWORDS:
                attributes.put(NAME, "keywords");
                break;
            case Element.AUTHOR:
                attributes.put(NAME, "author");
                break;
        }
        writeBeginTag(META, attributes);
        indent--;
    }
    
/**
 * Writes a link in the header.
 *
 * @param	element		the element that has to be written
 * @throws	IOException
 */
    
    private void writeLink(Header header) throws IOException {
        HtmlAttributes attributes = new HtmlAttributes();
        attributes.put(REL, header.name());
        attributes.put(TYPE, CSS);
        attributes.put(HREF, header.content());
        writeBeginTag(LINK, attributes);
        indent--;
    }
    
/**
 * Writes some comment.
 * <P>
 * This method writes some comment.
 *
 * @param	comment		the comment that has to be written
 * @throws	IOException
 */
    
    private void writeComment(String comment) throws IOException {
        tab();
        os.write(BEGINCOMMENT);
        write(comment);
        os.write(ENDCOMMENT);
        newLine();
    }
    
/**
 * Changes the font.
 *
 * @param	font	The font
 *
 * @throws	IOException
 */
    
    private void setFont(Font difference) throws IOException {
        if (! difference.isStandardFont()) {
            HtmlAttributes attributes = new HtmlAttributes();
            if (!(difference.family() == Font.UNDEFINED)) {
                attributes.put(FACE, FONTS[difference.family()]);
            }
            if (!(difference.size() == Font.UNDEFINED)) {
                attributes.put(POINTSIZE, String.valueOf(difference.size()));
            }
            if (difference.color() != null) {
                attributes.put(COLOR, HtmlEncoder.encode(difference.color()));
            }
            writeBeginTag(FONT, attributes);
        }
        if (!font.isBold() && difference.isBold()) {
            writeBeginTag(B);
        }
        if (!font.isItalic() && difference.isItalic()) {
            writeBeginTag(I);
        }
        if (!font.isStrikethru() && difference.isStrikethru()) {
            writeBeginTag(S);
        }
        if (!font.isUnderlined() && difference.isUnderlined()) {
            writeBeginTag(U);
        }
    }
    
/**
 * Closes the tags that changed the font.
 *
 * @param	font	The font
 */
    
    private void resetFont(Font difference) throws IOException {
        if (difference.isItalic()) {
            writeEndTag(I);
        }
        if (difference.isBold()) {
            writeEndTag(B);
        }
        if (difference.isUnderlined()) {
            writeEndTag(U);
        }
        if (difference.isStrikethru()) {
            writeEndTag(S);
        }
        if (!difference.isStandardFont()) {
            writeEndTag(FONT);
        }
    }
    
    // public methods
    
/**
 * Changes the standardfont.
 *
 * @param	standardfont	The font
 */
    
    public void setStandardFont(Font standardFont) {
        this.standardFont = standardFont;
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
 * @param	the new imagepath
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
 * @param	header		the new header
 *
 * @author	David Freels
 */
    
    public void setHeader(HeaderFooter header) {
        this.header = header;
    }
    
/**
 * Changes the footer of this document.
 *
 * @param	footer		the new footer
 *
 * @author	David Freels
 */
    
    public void setFooter(HeaderFooter footer) {
        this.footer = footer;
    }
    
/**
 * Signals that a <CODE>String</CODE> was added to the <CODE>Document</CODE>.
 *
 * @return	<CODE>true</CODE> if the string was added, <CODE>false</CODE> if not.
 * @throws	DocumentException	when a document isn't open yet, or has been closed
 * @author      Evelyne De Cordier
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
            throw new DocumentException(ioe.getMessage());
        }
    }
}

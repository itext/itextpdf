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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.*;

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
    public static final byte[] BEGINCOMMENT = getISOBytes("\t<!-- ");
    
/** This is a possible HTML-tag. */
    public static final byte[] ENDCOMMENT = getISOBytes(" -->\n");    
    
/** This is a possible HTML-tag. */
    public static final String NBSP = "&nbsp;";
    
    // membervariables
    
/** This is the current font of the HTML. */
    protected Font font = new Font();
    
/** This is the standard font of the HTML. */
    protected Font standardFont = new Font();
    
/** This is a path for images. */
    protected String imagepath = null;
    
/** Stores the page number. */
    protected static int pageN = 0;
    
/** This is the textual part of a header */
    protected HeaderFooter header = null;
    
/** This is the textual part of the footer */
    protected HeaderFooter footer = null;
    
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
            os.write(LT);
            os.write(getISOBytes(HtmlTags.HTML));
            os.write(GT);
            os.write(NEWLINE);
            os.write(TAB);
            os.write(LT);
            os.write(getISOBytes(HtmlTags.HEAD));
            os.write(GT);
            os.write(NEWLINE);
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
 *
 * @return	<CODE>true</CODE> if this action succeeded, <CODE>false</CODE> if not.
 * @throws	DocumentException	when a document isn't open yet, or has been closed
 */
    
    public boolean newPage() throws DocumentException {
        try {
          writeStart(HtmlTags.DIV);
          write(" ");
          write(HtmlTags.STYLE);
          write("=\"");
          writeCssProperty(HtmlTags.PAGE_BREAK_BEFORE, ElementTags.ALWAYS);
          write("\" /");        
          os.write(GT);
          os.write(NEWLINE);
        }
        catch(IOException ioe) {
            throw new DocumentException(ioe.getMessage());
        }
        return true;
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
        try {
            if (element.type() == Element.CHUNK && !standardFont.isStandardFont()) {
                Chunk chunk = new Chunk(((Chunk) element).content(), standardFont.difference(((Chunk) element).font()));
                write(chunk, 2);
                return true;
            }
            
            switch(element.type()) {
                case Element.HEADER:
                    try {
                        Header h = (Header) element;
                        if (!HtmlTags.STYLESHEET.equals(h.name())) {
                            writeHeader(h);
                        }
                        else {
                            writeLink(h);
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
                    os.write(NEWLINE);
                    addTabs(3);
                    write(((Meta)element).content());
                    os.write(NEWLINE);
                    addTabs(2);
                    writeEnd(HtmlTags.TITLE);
                    return true;
                case Element.CREATOR:
                    writeComment("Creator: " + ((Meta)element).content());
                    return true;
                case Element.PRODUCER:
                    writeComment("Producer: " + ((Meta)element).content());
                    return true;
                case Element.CREATIONDATE:
                    writeComment("Creationdate: " + ((Meta)element).content());
                    return true;
                    default:
                        write(element, 2);
                        return true;
            }
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
            os.write(GT);
            os.write(NEWLINE);
            initHeader(); // line added by David Freels
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
            addTabs(1);
            writeEnd(HtmlTags.BODY);
            writeEnd(HtmlTags.HTML);
            super.close();
        }
        catch(IOException ioe) {
        }
    }
    
    // some protected methods
    
/**
 * Adds the header to the top of the </CODE>Document</CODE>
 *
 * @author	David Freels
 */
    
    protected void initHeader() {
        if (header != null) {
            try {
                add(header.paragraph());
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
    
    protected void initFooter() {
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
 * Writes a Metatag in the header.
 *
 * @param	element		the element that has to be written
 * @throws	IOException
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
        write(HtmlTags.CONTENT, meta.content());
        writeEnd();
    }
    
/**
 * Writes a link in the header.
 *
 * @param	element		the element that has to be written
 * @throws	IOException
 */
    
    protected void writeLink(Header header) throws IOException {
        addTabs(2);
        writeStart(HtmlTags.LINK);
        write(HtmlTags.REL, header.name());
        write(HtmlTags.TYPE, HtmlTags.CSS);
        write(HtmlTags.REFERENCE, header.content());
        writeEnd();
    }
    
/**
 * Writes some comment.
 * <P>
 * This method writes some comment.
 *
 * @param	comment		the comment that has to be written
 * @throws	IOException
 */
    
    protected void writeComment(String comment) throws IOException {
        os.write(BEGINCOMMENT);
        write(comment);
        os.write(ENDCOMMENT);
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
    
/**
 * Writes the HTML representation of an element.
 *
 * @param   element     the element
 * @param   indent      the indentation
 */
    
    protected void write(Element element, int indent) throws IOException {
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
                }
                
                //if (chunk.isEmpty()) return;
                HashMap attributes = chunk.getAttributes();
                if (chunk.font().isStandardFont() && attributes == null) {
                    addTabs(indent);
                    write(HtmlEncoder.encode(chunk.content()));
                    os.write(NEWLINE);
                    return;
                }
                else {
                    if (attributes != null && attributes.get(Chunk.NEWPAGE) != null) {
                        return;
                    }
                    addTabs(indent);
                    writeStart(HtmlTags.CHUNK);
                    if (! chunk.font().isStandardFont()) {
                        write(chunk.font());
                    }
                    os.write(GT);
                    if (attributes != null && attributes.get(Chunk.SUBSUPSCRIPT) != null) {
                        if (((Float)attributes.get(Chunk.SUBSUPSCRIPT)).floatValue() > 0) {
                            writeStart(HtmlTags.SUP);
                        }
                        else {
                            writeStart(HtmlTags.SUB);
                        }
                        os.write(GT);
                    }
                    write(HtmlEncoder.encode(chunk.content()));
                    
                    if (attributes != null && attributes.get(Chunk.SUBSUPSCRIPT) != null) {
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
                    writeEnd(HtmlTags.CHUNK);
                }
                return;
            }
            case Element.PHRASE:
            {
                Phrase phrase = (Phrase) element;
                
                addTabs(indent);
                writeStart(HtmlTags.PHRASE);
                write(phrase.font());
                os.write(GT);
                os.write(NEWLINE);
                for (Iterator i = phrase.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                addTabs(indent);
                writeEnd(HtmlTags.PHRASE);
                return;
            }
            case Element.ANCHOR:
            {
                Anchor anchor = (Anchor) element;
                
                if (!anchor.font().isStandardFont()) {
                    addTabs(indent);
                    writeStart(HtmlTags.PHRASE);
                    write(anchor.font());
                    os.write(GT);
                    os.write(NEWLINE);
                }
                
                addTabs(indent);
                writeStart(HtmlTags.ANCHOR);
                if (anchor.name() != null) {
                    write(HtmlTags.NAME, anchor.name());
                }
                if (anchor.reference() != null) {
                    write(HtmlTags.REFERENCE, anchor.reference());
                }
                os.write(GT);
                os.write(NEWLINE);
                
                for (Iterator i = anchor.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                
                addTabs(indent);
                writeEnd(HtmlTags.ANCHOR);
                
                if (!anchor.font().isStandardFont()) {
                    addTabs(indent);
                    writeEnd(HtmlTags.PHRASE);
                }
                return;
            }
            case Element.PARAGRAPH:
            {
                Paragraph paragraph = (Paragraph) element;
                
                addTabs(indent);
                writeStart(HtmlTags.PARAGRAPH);
                String alignment = HtmlEncoder.getAlignment(paragraph.alignment());
                if (!"".equals(alignment)) {
                    write(HtmlTags.ALIGN, alignment);
                }
                os.write(GT);
                os.write(NEWLINE);
                
                if (!paragraph.font().isStandardFont()) {
                    addTabs(indent);
                    writeStart(HtmlTags.PHRASE);
                    write(paragraph.font());
                    os.write(GT);
                    os.write(NEWLINE);
                }
                
                for (Iterator i = paragraph.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                if (!paragraph.font().isStandardFont()) {
                    addTabs(indent);
                    writeEnd(HtmlTags.PHRASE);
                }
                
                addTabs(indent);
                writeEnd(HtmlTags.PARAGRAPH);
                return;
            }
            case Element.SECTION:
            case Element.CHAPTER:
            {
                Section section = (Section) element;
                
                addTabs(indent);   
                /*
                try {
                  newPage();
                } catch (DocumentException e) {
                }
                */
                writeStart(HtmlTags.DIV);
                writeSection(section, indent);
                writeEnd(HtmlTags.DIV);
                return;
                
            }
            case Element.LIST:
            {
                List list = (List) element;
                
                addTabs(indent);
                if (list.isNumbered()) {
                    writeStart(HtmlTags.ORDEREDLIST);
                }
                else {
                    writeStart(HtmlTags.UNORDEREDLIST);
                }
                os.write(GT);
                os.write(NEWLINE);
                for (Iterator i = list.getItems().iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
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
                
                addTabs(indent);
                writeStart(HtmlTags.LISTITEM);
                os.write(GT);
                os.write(NEWLINE);
                
                if (!listItem.font().isStandardFont()) {
                    addTabs(indent);
                    writeStart(HtmlTags.PHRASE);
                    write(listItem.font());
                    os.write(GT);
                    os.write(NEWLINE);
                }
                for (Iterator i = listItem.iterator(); i.hasNext(); ) {
                    write((Element) i.next(), indent + 1);
                }
                if (!listItem.font().isStandardFont()) {
                    addTabs(indent);
                    writeEnd(HtmlTags.PHRASE);
                }
                addTabs(indent);
                writeEnd(HtmlTags.LISTITEM);
                return;
            }
            case Element.CELL:
            {
                Cell cell = (Cell) element;
                
                addTabs(indent);
                if (cell.header()) {
                    writeStart(HtmlTags.HEADERCELL);
                }
                else {
                    writeStart(HtmlTags.CELL);
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
                os.write(NEWLINE);
                if (cell.isEmpty()) {
                    write(NBSP);
                } else {
                    for (Iterator i = cell.getElements(); i.hasNext(); ) {
                        write((Element) i.next(), indent + 1);
                    }
                }
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
                
                addTabs(indent);
                writeStart(HtmlTags.ROW);
                os.write(GT);
                os.write(NEWLINE);
                Element cell;
                for (int i = 0; i < row.columns(); i++) {
                    if ((cell = (Element)row.getCell(i)) != null) {
                        write(cell, indent + 1);
                    }
                }
                addTabs(indent);
                writeEnd(HtmlTags.ROW);
                return;
            }
            case Element.TABLE:
            {
                Table table = (Table) element;
                
                addTabs(indent);
                writeStart(HtmlTags.TABLE);
                //write(HtmlTags.COLUMNS, String.valueOf(table.columns()));
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
                os.write(NEWLINE);
                Row row;
                for (Iterator iterator = table.iterator(); iterator.hasNext(); ) {
                    row = (Row) iterator.next();
                    write(row, indent + 1);
                }
                addTabs(indent);
                writeEnd(HtmlTags.TABLE);
                return;
            }
            case Element.ANNOTATION:
            {
                Annotation annotation = (Annotation) element;
                writeComment(annotation.title() + ": " + annotation.content());
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
        os.write(GT);
        os.write(NEWLINE);
        
        if (section.title() != null) {
            int depth = section.depth() - 1;
            if (depth > 5) {
                depth = 5;
            }
            addTabs(indent + 1);
            writeStart(HtmlTags.H[depth]);
            String alignment = HtmlEncoder.getAlignment(section.title().alignment());
            if (!"".equals(alignment)) {
                write(HtmlTags.ALIGN, alignment);
            }
            os.write(GT);
            os.write(NEWLINE);
            for (Iterator i = section.title().iterator(); i.hasNext(); ) {
                write((Element)i.next(), indent + 2);
            }
            addTabs(indent + 1);
            writeEnd(HtmlTags.H[depth]);
        }
        for (Iterator i = section.iterator(); i.hasNext(); ) {
            write((Element) i.next(), indent + 1);
        }
        addTabs(indent);
    }
        
    /**
     * Writes the HTML representation of a <CODE>Font</CODE>.
     *
     * @param	a <CODE>Font</CODE>
     * @author  "Steve Ogryzek" <steve@ogryzek.com>
     */
    
    private void write(Font font) throws IOException {
        write(" ");
        write(HtmlTags.STYLE);
        write("=\"");
        switch (font.family()) {
        case Font.COURIER:
            writeCssProperty(HtmlTags.CSS_FONTFAMILY, ElementTags.COURIER);
            break;
        case Font.HELVETICA:
            writeCssProperty(HtmlTags.CSS_FONTFAMILY, ElementTags.HELVETICA);
            break;
        case Font.TIMES_NEW_ROMAN:
            writeCssProperty(HtmlTags.CSS_FONTFAMILY, ElementTags.TIMES_NEW_ROMAN);
            break;
        case Font.SYMBOL:
            writeCssProperty(HtmlTags.CSS_FONTFAMILY, ElementTags.SYMBOL);
            break;
        case Font.ZAPFDINGBATS:
            writeCssProperty(HtmlTags.CSS_FONTFAMILY, ElementTags.ZAPFDINGBATS);
            break;
        default:
        }

        if (font.size() != Font.UNDEFINED) {
            writeCssProperty(HtmlTags.CSS_FONTSIZE, String.valueOf(font.size()) + "px");
        }
        if (font.color() != null) {
            writeCssProperty(HtmlTags.CSS_COLOR, HtmlEncoder.encode(font.color()));
        }
    
        int fontstyle = font.style();
        if (fontstyle != Font.UNDEFINED && fontstyle != Font.NORMAL) {
            switch (fontstyle & Font.BOLDITALIC) {
            case Font.BOLD:
                writeCssProperty(HtmlTags.CSS_FONTWEIGHT, ElementTags.BOLD);
                break;
            case Font.ITALIC:
                writeCssProperty(HtmlTags.CSS_FONTSTYLE, ElementTags.ITALIC);
                break;
            case Font.BOLDITALIC:
                writeCssProperty(HtmlTags.CSS_FONTWEIGHT, ElementTags.BOLD);
                writeCssProperty(HtmlTags.CSS_FONTSTYLE, ElementTags.ITALIC);
                break;
            }

            // CSS only supports one decoration tag so if both are specified
            // only one of the two will display
            if ((fontstyle & Font.UNDERLINE) > 0) {
                writeCssProperty(HtmlTags.CSS_TEXTDECORATION, HtmlTags.CSS_UNDERLINE);
            }
            if ((fontstyle & Font.STRIKETHRU) > 0) {
                writeCssProperty(HtmlTags.CSS_TEXTDECORATION, HtmlTags.CSS_LINETHROUGH);
            }
        }

        write("\"");
    }
    
    /**
     * Writes out a CSS property.
     */
    private void writeCssProperty(String prop, String value) throws IOException {
        write(new StringBuffer(prop).append(": ").append(value).append("; ").toString());
    }
}
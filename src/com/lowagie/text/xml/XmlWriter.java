/*
 * $Id$
 * $Name$
 *
 * Copyright (c) 1999, 2000, 2001 Bruno Lowagie.
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

import java.awt.Color;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.HashMap;

import com.lowagie.text.*;

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
 *
 * @author  bruno@lowagie.com
 */

public class XmlWriter extends DocWriter implements DocListener {
    
    // static membervariables (tags)
    
/** This is the first line of the XML page. */
    public static final byte[] PROLOG = getISOBytes("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    
/** This is the reference to the DTD. */
    public static final byte[] DOCTYPE = getISOBytes("<!DOCTYPE ITEXT SYSTEM \"");
    
/** This is the place where the DTD is located. */
    public final static byte[] DTD = getISOBytes("http://www.lowagie.com/iText/itext.dtd");
    
/** This is the name of the root element. */
    public final static byte[] ITEXT = getISOBytes(ElementTags.ITEXT);
    
/** This is the newpage tag. */
    public final static byte[] NEWPAGE = getISOBytes("\n<" + ElementTags.NEWPAGE + " />");
    
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
    
    // membervariables
    
/** This is the meta information of the document. */
    private TreeMap itext = new TreeMap(new com.lowagie.text.StringCompare());
    
    // constructors
    
/**
 * Constructs an <CODE>XmlWriter</CODE>.
 *
 * @param	document	The <CODE>Document</CODE> that has to be written as XML
 * @param	os			The <CODE>OutputStream</CODE> the writer has to write to.
 */
    
    protected XmlWriter(Document doc, OutputStream os) {
        super(doc, os);
        
        document.addDocListener(this);
        try {
            os.write(PROLOG);
            os.write(DOCTYPE);
            os.write(DTD);
            os.write(getISOBytes("\">\n"));
        }
        catch(IOException ioe) {
        }
    }
    
/**
 * Constructs an <CODE>XmlWriter</CODE>.
 *
 * @param	document	The <CODE>Document</CODE> that has to be written as XML
 * @param	os			The <CODE>OutputStream</CODE> the writer has to write to.
 * @param   dtd         The DTD to use
 */
    
    protected XmlWriter(Document doc, OutputStream os, String dtd) {
        super(doc, os);
        
        document.addDocListener(this);
        try {
            os.write(PROLOG);
            os.write(DOCTYPE);
            os.write(getISOBytes(dtd));
            os.write(getISOBytes("\">\n"));
        }
        catch(IOException ioe) {
        }
    }
    
    // get an instance of the XmlWriter
    
/**
 * Gets an instance of the <CODE>XmlWriter</CODE>.
 *
 * @param	document	The <CODE>Document</CODE> that has to be written
 * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
 * @return	a new <CODE>XmlWriter</CODE>
 */
    
    public static XmlWriter getInstance(Document document, OutputStream os) {
        return new XmlWriter(document, os);
    }
    
/**
 * Gets an instance of the <CODE>XmlWriter</CODE>.
 *
 * @param	document	The <CODE>Document</CODE> that has to be written
 * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
 * @param   dtd         The DTD to use
 * @return	a new <CODE>XmlWriter</CODE>
 */
    
    public static XmlWriter getInstance(Document document, OutputStream os, String dtd) {
        return new XmlWriter(document, os, dtd);
    }
    
    // implementation of the DocListener methods
    
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
                        os.write(getISOBytes(toXml(element, 1)));
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
            writeBeginTag(ITEXT, itext);
        }
        catch(IOException ioe) {
        }
    }
    
/**
 * Signals that an new page has to be started.
 *
 * @return	<CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
 * @throws	DocumentException	when a document isn't open yet, or has been closed
 */
    
    public boolean newPage() throws DocumentException {
        if (pause || !open) {
            return false;
        }
        try {
            os.write(NEWPAGE);
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
            writeEndTag(ITEXT);
            super.close();
        }
        catch(IOException ioe) {
        }
    }
    
    // methods
    
/**
 * Writes a begin tag.
 * <P>
 * This method writes a given tag between brackets.
 *
 * @param	tag		the tag that has to be written
 * @return	<CODE>void</CODE>
 * @throws	IOException
 */
    
    private void writeBeginTag(byte[] tag, TreeMap map) throws IOException {
        os.write(START);
        os.write(tag);
        String key;
        for (java.util.Iterator i = map.keySet().iterator(); i.hasNext(); ) {
            key = (String) i.next();
            os.write(getISOBytes(" "));
            os.write(getISOBytes(key));
            os.write(getISOBytes("=\""));
            os.write(getISOBytes((String) map.get(key)));
            os.write(getISOBytes("\""));
        }
        os.write(END);
        newLine();
    }
    
/**
 * Writes an end tag.
 * <P>
 * This method writes a given tag between brackets.
 *
 * @param	tag		the tag that has to be written
 * @return	<CODE>void</CODE>
 * @throws	IOException
 */
    
    private void writeEndTag(byte[] tag) throws IOException {
        os.write(START);
        os.write(ENDTAG);
        os.write(tag);
        os.write(END);
        newLine();
    }
    
/**
 * Writes a new line to the outputstream.
 */
    
    private void newLine() throws IOException {
        os.write(NEWLINE);
    }
    
/**
 * Returns the XML representation of an element.
 *
 * @param   element     the element
 * @param   indent      the indentation
 * @return  an XML representation
 */
    
    private String toXml(Element element, int indent) {
        switch(element.type()) {
            case Element.CHUNK:
            {
                Chunk chunk = (Chunk) element;
                
                // if the chunk contains an image, return the image representation
                try {
                    Image image = chunk.getImage();
                    return toXml(image, indent);
                }
                catch(NullPointerException npe) {
                }
                
                boolean tag = false;
                // start composing the chunk tag
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.CHUNK);
                if (! chunk.font().isStandardFont()) {
                    buf.append(toXml(chunk.font()));
                    tag = true;
                }
                HashMap attributes = chunk.getAttributes();
                if (attributes != null) {
                    for (Iterator i = attributes.keySet().iterator(); i.hasNext(); ) {
                        String key = (String) i.next();
                        if (key.equals(Chunk.LOCALGOTO)
                        || key.equals(Chunk.LOCALDESTINATION)
                        || key.equals(Chunk.GENERICTAG)
                        || key.equals(Chunk.SUBSUPSCRIPT)) {
                            String value = (String) attributes.get(key);
                            buf.append(" ").append(key.toLowerCase()).append("=\"").append(value).append("\"");
                            tag = true;
                        }
                        if (key.equals(Chunk.NEWPAGE)) {
                            return "<" + ElementTags.NEWPAGE + " />";
                        }
                    }
                }
                buf.append(">");
                if (tag) {
                    buf.append(encode(chunk.content(), indent));
                    buf.append("</").append(ElementTags.CHUNK).append(">\n");
                    return buf.toString();
                }
                buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append(encode(chunk.content(), indent));
                buf.append("\n");
                return buf.toString();
            }
            case Element.PHRASE:
            {
                Phrase phrase = (Phrase) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.PHRASE).append(" ");
                
                buf.append(ElementTags.LEADING).append("=\"").append(phrase.leading());
                buf.append("\"").append(toXml(phrase.font())).append(">\n");
                
                for (Iterator i = phrase.iterator(); i.hasNext(); ) {
                    buf.append(toXml((Element) i.next(), indent + 1));
                }
                addTabs(buf, indent);
                buf.append("</").append(ElementTags.PHRASE).append(">\n");
                return buf.toString();
            }
            case Element.ANCHOR:
            {
                Anchor anchor = (Anchor) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.ANCHOR);
                buf.append(" ").append(ElementTags.LEADING).append("=\"").append(anchor.leading());
                buf.append("\"").append(toXml(anchor.font()));
                if (anchor.name() != null) {
                    buf.append(" ").append(ElementTags.NAME).append("=\"").append(anchor.name()).append("\"");
                }
                if (anchor.reference() != null) {
                    buf.append(" ").append(ElementTags.REFERENCE).append("=\"").append(anchor.reference()).append("\"");
                }
                buf.append(">\n");
                for (Iterator i = anchor.iterator(); i.hasNext(); ) {
                    buf.append(toXml((Element) i.next(), indent + 1));
                }
                addTabs(buf, indent);
                buf.append("</").append(ElementTags.ANCHOR).append(">\n");
                return buf.toString();
            }
            case Element.PARAGRAPH:
            {
                Paragraph paragraph = (Paragraph) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.PARAGRAPH);
                
                buf.append(" ").append(ElementTags.LEADING).append("=\"").append(paragraph.leading());
                buf.append("\"").append(toXml(paragraph.font()));
                buf.append(" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.getAlignment(paragraph.alignment()));
                if (paragraph.indentationLeft() != 0) {
                    buf.append("\" ").append(ElementTags.INDENTATIONLEFT).append("=\"").append(paragraph.indentationLeft());
                }
                if (paragraph.indentationRight() != 0) {
                    buf.append("\" ").append(ElementTags.INDENTATIONRIGHT).append("=\"").append(paragraph.indentationRight());
                }
                buf.append("\">\n");
                for (Iterator i = paragraph.iterator(); i.hasNext(); ) {
                    buf.append(toXml((Element) i.next(), indent + 1));
                }
                addTabs(buf, indent);
                buf.append("</").append(ElementTags.PARAGRAPH).append(">\n");
                return buf.toString();
            }
            case Element.SECTION:
            {
                Section section = (Section) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.SECTION);
                addXml(buf, section, indent);
                buf.append("</").append(ElementTags.SECTION).append(">\n");
                return buf.toString();
            }
            case Element.CHAPTER:
            {
                Chapter chapter = (Chapter) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.CHAPTER);
                addXml(buf, chapter, indent);
                buf.append("</").append(ElementTags.CHAPTER).append(">\n");
                return buf.toString();
                
            }
            case Element.LIST:
            {
                List list = (List) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.LIST);
                buf.append(" ").append(ElementTags.NUMBERED).append("=\"").append(list.isNumbered());
                buf.append("\" ").append(ElementTags.SYMBOLINDENT).append("=\"").append(list.symbolIndent()).append("\"");
                if (list.first() != 1) {
                    buf.append(" ").append(ElementTags.FIRST).append("=\"").append(list.first()).append("\"");
                }
                if (list.indentationLeft() != 0) {
                    buf.append(" ").append(ElementTags.INDENTATIONLEFT).append("=\"").append(list.indentationLeft()).append("\"");
                }
                if (list.indentationRight() != 0) {
                    buf.append(" ").append(ElementTags.INDENTATIONRIGHT).append("=\"").append(list.indentationRight()).append("\"");
                }
                if (!list.isNumbered()) {
                    buf.append(" ").append(ElementTags.LISTSYMBOL).append("=\"").append(list.symbol().content()).append("\"");
                }
                buf.append(toXml(list.symbol().font())).append(">\n");
                for (Iterator i = list.getItems().iterator(); i.hasNext(); ) {
                    buf.append(toXml((Element) i.next(), indent + 1));
                }
                addTabs(buf, indent);
                buf.append("</").append(ElementTags.LIST).append(">\n");
                return buf.toString();
            }
            case Element.LISTITEM:
            {
                ListItem listItem = (ListItem) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.LISTITEM);
                buf.append(" ").append(ElementTags.LEADING).append("=\"").append(listItem.leading());
                buf.append("\"").append(toXml(listItem.font()));
                buf.append(" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.getAlignment(listItem.alignment()));
                if (listItem.indentationLeft() != 0) {
                    buf.append("\" ").append(ElementTags.INDENTATIONLEFT).append("=\"").append(listItem.indentationLeft());
                }
                if (listItem.indentationRight() != 0) {
                    buf.append("\" ").append(ElementTags.INDENTATIONRIGHT).append("=\"").append(listItem.indentationRight());
                }
                buf.append("\">\n");
                for (Iterator i = listItem.iterator(); i.hasNext(); ) {
                    buf.append(toXml((Element) i.next(), indent + 1));
                }
                addTabs(buf, indent);
                buf.append("</").append(ElementTags.LISTITEM).append(">\n");
                return buf.toString();
            }
            case Element.CELL:
            {
                Cell cell = (Cell) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.CELL);
                buf.append(toXml((Rectangle) cell));
                buf.append(" ").append(ElementTags.HORIZONTALALIGN).append("=\"").append(ElementTags.getAlignment(cell.horizontalAlignment())).append("\"");
                buf.append(" ").append(ElementTags.VERTICALALIGN).append("=\"").append(ElementTags.getAlignment(cell.verticalAlignment())).append("\"");
                if (cell.cellWidth() != null) {
                    buf.append(" ").append(ElementTags.WIDTH).append("=\"").append(cell.cellWidth()).append("\"");
                }
                if (cell.colspan() != 1) {
                    buf.append(" ").append(ElementTags.COLSPAN).append("=\"").append(cell.colspan()).append("\"");
                }
                if (cell.rowspan() != 1) {
                    buf.append(" ").append(ElementTags.ROWSPAN).append("=\"").append(cell.rowspan()).append("\"");
                }
                if (cell.header()) {
                    buf.append(" ").append(ElementTags.HEADER).append("=\"").append(true).append("\"");
                }
                if (cell.noWrap()) {
                    buf.append(" ").append(ElementTags.NOWRAP).append("=\"").append(true).append("\"");
                }
                if (cell.leading() != -1) {
                    buf.append(" ").append(ElementTags.LEADING).append("=\"").append(cell.leading()).append("\"");
                }
                buf.append(">\n");
                for (Iterator i = cell.getElements(); i.hasNext(); ) {
                    buf.append(toXml((Element) i.next(), indent + 1));
                }
                addTabs(buf, indent);
                buf.append("</").append(ElementTags.CELL).append(">\n");
                return buf.toString();
            }
            case Element.ROW:
            {
                Row row = (Row) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.ROW).append(">\n");
                Element cell;
                for (int i = 0; i < row.columns(); i++) {
                    if ((cell = (Element)row.getCell(i)) != null) {
                        buf.append(toXml(cell, indent + 1));
                    }
                }
                addTabs(buf, indent);
                buf.append("</").append(ElementTags.ROW).append(">\n");
                return buf.toString();
            }
            case Element.TABLE:
            {
                Table table = (Table) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.TABLE);
                buf.append(" ").append(ElementTags.COLUMNS).append("=\"").append(table.columns()).append("\"");
                buf.append(" ").append(ElementTags.WIDTH).append("=\"");
                if (!table.absWidth().equals("")){
                    buf.append(table.absWidth());
                }
                else{
                    buf.append(table.widthPercentage()).append("%");
                }
                buf.append("\" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.getAlignment(table.alignment()));
                buf.append("\" ").append(ElementTags.CELLPADDING).append("=\"").append(table.cellpadding());
                buf.append("\" ").append(ElementTags.CELLSPACING).append("=\"").append(table.cellspacing());
                buf.append("\" ").append(ElementTags.WIDTHS).append("=\"");
                float[] widths = table.getProportionalWidths();
                buf.append(widths[0]);
                for (int i = 1; i < widths.length; i++) {
                    buf.append(";");
                    buf.append(widths[i]);
                }
                buf.append("\"").append(toXml((Rectangle) table)).append(">\n");
                Row row;
                for (Iterator iterator = table.iterator(); iterator.hasNext(); ) {
                    row = (Row) iterator.next();
                    buf.append(toXml(row, indent + 1));
                }
                addTabs(buf, indent);
                buf.append("</").append(ElementTags.TABLE).append(">\n");
                return buf.toString();
            }
            case Element.ANNOTATION:
            {
                Annotation annotation = (Annotation) element;
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.ANNOTATION).append("");
                if (annotation.title() != null) {
                    buf.append(" ").append(ElementTags.TITLE).append("=\"");
                    buf.append(annotation.title());
                }
                if (annotation.content() != null) {
                    buf.append("\" ").append(ElementTags.CONTENT).append("=\"");
                    buf.append(annotation.content());
                }
                buf.append("\" />\n");
                return buf.toString();
            }
            case Element.GIF:
            case Element.JPEG:
            case Element.PNG:
            {
                Image image = (Image) element;
                if (image.url() == null) {
                    return "";
                }
                StringBuffer buf = new StringBuffer();
                addTabs(buf, indent);
                buf.append("<").append(ElementTags.IMAGE).append(" ").append(ElementTags.URL).append("=\"");
                buf.append(image.url().toString()).append("\"");
                if ((image.alignment() & Image.LEFT) > 0) {
                    buf.append(" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.ALIGN_LEFT).append("\"");
                }
                else if ((image.alignment() & Image.RIGHT) > 0) {
                    buf.append(" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.ALIGN_RIGHT).append("\"");
                }
                else if ((image.alignment() & Image.MIDDLE) > 0) {
                    buf.append(" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.ALIGN_MIDDLE).append("\"");
                }
                if ((image.alignment() & Image.UNDERLYING) > 0) {
                    buf.append(" ").append(ElementTags.UNDERLYING).append("=\"").append(true).append("\"");
                }
                if ((image.alignment() & Image.TEXTWRAP) > 0) {
                    buf.append(" ").append(ElementTags.TEXTWRAP).append("=\"").append(true).append("\"");
                }
                if (image.alt() != null) {
                    buf.append(" ").append(ElementTags.ALT).append("=\"").append(image.alt()).append("\"");
                }
                if (image.hasAbsolutePosition()) {
                    buf.append(" ").append(ElementTags.ABSOLUTEX).append("=\"").append(image.absoluteX()).append("\"");
                    buf.append(" ").append(ElementTags.ABSOLUTEY).append("=\"").append(image.absoluteY()).append("\"");
                }
                buf.append(" ").append(ElementTags.PLAINWIDTH).append("=\"").append(image.plainWidth()).append("\"");
                buf.append(" ").append(ElementTags.PLAINHEIGHT).append("=\"").append(image.plainHeight()).append("\"");
                buf.append(" />\n");
                return buf.toString();
            }
            default:
                return "";
        }
    }
    
    /**
     * Adds XML to a given StringBuffer.
     */
    
    public void addXml(StringBuffer buf, Section section, int indent){
        buf.append(" ").append(ElementTags.DEPTH).append("=\"").append(section.numberDepth());
        buf.append("\" ").append(ElementTags.INDENT).append("=\"").append(section.indentation());
        if (section.indentationLeft() != 0) {
            buf.append("\" ").append(ElementTags.INDENTATIONLEFT).append("=\"").append(section.indentationLeft());
        }
        if (section.indentationRight() != 0) {
            buf.append("\" ").append(ElementTags.INDENTATIONRIGHT).append("=\"").append(section.indentationRight());
        }
        buf.append("\">\n");
        
        if (section.title() != null) {
            addTabs(buf, indent + 1);
            buf.append("<").append(ElementTags.TITLE).append(" ");
            buf.append(ElementTags.LEADING).append("=\"").append(section.title().leading());
            buf.append("\" ").append(ElementTags.ALIGN).append("=\"").append(ElementTags.getAlignment(section.title().alignment()));
            if (section.title().indentationLeft() != 0) {
                buf.append("\" ").append(ElementTags.INDENTATIONLEFT).append("=\"");
                buf.append(section.title().indentationLeft());
            }
            if (section.title().indentationRight() != 0) {
                buf.append("\" ").append(ElementTags.INDENTATIONRIGHT).append("=\"");
                buf.append(section.title().indentationRight());
            }
            buf.append("\">\n");
            for (Iterator i = section.title().iterator(); i.hasNext(); ) {
                buf.append(toXml((Element)i.next(), indent + 2));
            }
            addTabs(buf, indent + 1);
            buf.append("</").append(ElementTags.TITLE).append(">\n");
        }
        for (Iterator i = section.iterator(); i.hasNext(); ) {
            buf.append(toXml((Element) i.next(), indent + 1));
        }
        addTabs(buf, indent);
    }
    
/**
 * Gives the String representation of a <CODE>Font</CODE>.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String toXml(Font font) {
        StringBuffer buffer = new StringBuffer(" ").append(ElementTags.FONT).append("=\"");
        switch(font.family()) {
            case Font.COURIER:
                buffer.append("Courier");
                break;
            case Font.HELVETICA:
                buffer.append("Helvetica");
                break;
            case Font.TIMES_NEW_ROMAN:
                buffer.append("Times New Roman");
                break;
            case Font.SYMBOL:
                buffer.append("Symbol");
                break;
            case Font.ZAPFDINGBATS:
                buffer.append("ZapfDingbats");
                break;
                default:
                    buffer.append("default");
        }
        if (font.size() != Font.UNDEFINED) {
            buffer.append("\" ").append(ElementTags.SIZE).append("=\"");
            buffer.append(font.size());
        }
        if (font.style() != Font.UNDEFINED) {
            buffer.append("\" ").append(ElementTags.STYLE).append("=\"");
            switch(font.style() & Font.BOLDITALIC) {
                case Font.NORMAL:
                    buffer.append("normal");
                    break;
                case Font.BOLD:
                    buffer.append("bold");
                    break;
                case Font.ITALIC:
                    buffer.append("italic");
                    break;
                case Font.BOLDITALIC:
                    buffer.append("bold, italic");
                    break;
            }
            if ((font.style() & Font.UNDERLINE) > 0) {
                buffer.append(", underline");
            }
            if ((font.style() & Font.STRIKETHRU) > 0) {
                buffer.append(", strike");
            }
        }
        if (font.color() != null) {
            buffer.append("\" ").append(ElementTags.RED).append("=\"");
            buffer.append(font.color().getRed());
            buffer.append("\" ").append(ElementTags.GREEN).append("=\"");
            buffer.append(font.color().getGreen());
            buffer.append("\" ").append(ElementTags.BLUE).append("=\"");
            buffer.append(font.color().getBlue());
        }
        return buffer.append("\"").toString();
    }
    
/**
 * Returns a representation of this <CODE>Rectangle</CODE>.
 *
 * @return	a <CODE>String</CODE>
 */
    
    public String toXml(Rectangle rectangle) {
        StringBuffer buf = new StringBuffer();
        if (rectangle.borderWidth() != Rectangle.UNDEFINED) {
            buf.append(" ").append(ElementTags.BORDERWIDTH).append("=\"");
            buf.append(rectangle.borderWidth());
            buf.append("\"");
            if (rectangle.hasBorder(Rectangle.LEFT)) {
                buf.append(" ").append(ElementTags.LEFT).append("=\"").append(true).append("\"");
            }
            if (rectangle.hasBorder(Rectangle.RIGHT)) {
                buf.append(" ").append(ElementTags.RIGHT).append("=\"").append(true).append("\"");
            }
            if (rectangle.hasBorder(Rectangle.TOP)) {
                buf.append(" ").append(ElementTags.TOP).append("=\"").append(true).append("\"");
            }
            if (rectangle.hasBorder(Rectangle.BOTTOM)) {
                buf.append(" ").append(ElementTags.BOTTOM).append("=\"").append(true).append("\"");
            }
        }
        if (rectangle.borderColor() != null) {
            buf.append(" ").append(ElementTags.RED).append("=\"");
            buf.append(rectangle.borderColor().getRed());
            buf.append("\" ").append(ElementTags.GREEN).append("=\"");
            buf.append(rectangle.borderColor().getGreen());
            buf.append("\" ").append(ElementTags.BLUE).append("=\"");
            buf.append(rectangle.borderColor().getBlue());
            buf.append("\"");
        }
        if (rectangle.backgroundColor() != null) {
            buf.append(" ").append(ElementTags.BGRED).append("=\"");
            buf.append(rectangle.backgroundColor().getRed());
            buf.append("\" ").append(ElementTags.BGGREEN).append("=\"");
            buf.append(rectangle.backgroundColor().getGreen());
            buf.append("\" ").append(ElementTags.BGBLUE).append("=\"");
            buf.append(rectangle.backgroundColor().getBlue());
            buf.append("\"");
        }
        if (rectangle.grayFill() > 0) {
            buf.append(" ").append(ElementTags.GRAYFILL).append("=\"");
            buf.append(rectangle.grayFill());
            buf.append("\"");
        }
        return buf.toString();
    }
    
/**
 * Adds a number of tabs to a <CODE>StringBuffer</CODE>.
 *
 * @param   buf     the <CODE>StringBuffer</CODE>
 * @param   indent  the number of tabs to add
 */
    
    static final void addTabs(StringBuffer buf, int indent) {
        for (int i = 0; i < indent; i++) {
            buf.append("\t");
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
            // the Htmlcode of these characters are added to a StringBuffer one by one
            switch(character) {
                case '\n':
                    buf.append("<").append(ElementTags.NEWLINE).append(" />\n");
                    addTabs(buf, indent);
                    break;
                case '"':
                    buf.append("&quot;");
                    break;
                case '\'':
                    buf.append("&apos;");
                    break;
                case '<':
                    buf.append("&lt;");
                    break;
                case '>':
                    buf.append("&gt;");
                    break;
                case '&':
                    buf.append("&amp;");
                    break;
                case ' ':
                    if ((i - pos) > 60) {
                        pos = i;
                        buf.append("\n");
                        addTabs(buf, indent);
                        break;
                    }
                    default:
                        buf.append(character);
            }
        }
        return buf.toString();
    }
}

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

import java.io.OutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

import com.lowagie.text.DocListener;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.Meta;
import com.lowagie.text.Rectangle;

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
                case Element.RECTANGLE:
                    return false;
                    default:
                        os.write(getISOBytes(element.toString()));
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
}

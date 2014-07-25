/*
 * $Id: DocWriter.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;

import com.itextpdf.text.pdf.OutputStreamCounter;

/**
 * An abstract <CODE>Writer</CODE> class for documents.
 * <P>
 * <CODE>DocWriter</CODE> is the abstract class of several writers such
 * as <CODE>PdfWriter</CODE> and <CODE>HtmlWriter</CODE>.
 * A <CODE>DocWriter</CODE> can be added as a <CODE>DocListener</CODE>
 * to a certain <CODE>Document</CODE> by getting an instance (see method
 * <CODE>getInstance()</CODE> in the specific writer-classes).
 * Every <CODE>Element</CODE> added to the original <CODE>Document</CODE>
 * will be written to the <CODE>OutputStream</CODE> of the listening
 * <CODE>DocWriter</CODE>.
 *
 * @see   Document
 * @see   DocListener
 */

public abstract class DocWriter implements DocListener {

/** This is some byte that is often used. */
    public static final byte NEWLINE = (byte)'\n';

/** This is some byte that is often used. */
    public static final byte TAB = (byte)'\t';

/** This is some byte that is often used. */
    public static final byte LT = (byte)'<';

/** This is some byte that is often used. */
    public static final byte SPACE = (byte)' ';

/** This is some byte that is often used. */
    public static final byte EQUALS = (byte)'=';

/** This is some byte that is often used. */
    public static final byte QUOTE = (byte)'\"';

/** This is some byte that is often used. */
    public static final byte GT = (byte)'>';

/** This is some byte that is often used. */
    public static final byte FORWARD = (byte)'/';

    // membervariables

/** The pageSize. */
    protected Rectangle pageSize;

/** This is the document that has to be written. */
    protected Document document;

/** The outputstream of this writer. */
    protected OutputStreamCounter os;

/** Is the writer open for writing? */
    protected boolean open = false;

/** Do we have to pause all writing actions? */
    protected boolean pause = false;

/** Closes the stream on document close */
    protected boolean closeStream = true;

    // constructor

    protected DocWriter()  {
    }

/**
 * Constructs a <CODE>DocWriter</CODE>.
 *
 * @param document  The <CODE>Document</CODE> that has to be written
 * @param os  The <CODE>OutputStream</CODE> the writer has to write to.
 */

    protected DocWriter(Document document, OutputStream os)  {
        this.document = document;
        this.os = new OutputStreamCounter(new BufferedOutputStream(os));
    }

    // implementation of the DocListener methods

/**
 * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class.
 *
 * @param element A high level object to add
 * @return  <CODE>false</CODE>
 * @throws  DocumentException when a document isn't open yet, or has been closed
 */

    public boolean add(Element element) throws DocumentException {
        return false;
    }

/**
 * Signals that the <CODE>Document</CODE> was opened.
 */

    public void open() {
        open = true;
    }

/**
 * Sets the pagesize.
 *
 * @param pageSize  the new pagesize
 * @return  a <CODE>boolean</CODE>
 */

    public boolean setPageSize(Rectangle pageSize) {
        this.pageSize = pageSize;
        return true;
    }

/**
 * Sets the margins.
 * <P>
 * This does nothing. Has to be overridden if needed.
 *
 * @param marginLeft    the margin on the left
 * @param marginRight   the margin on the right
 * @param marginTop   the margin on the top
 * @param marginBottom  the margin on the bottom
 * @return  <CODE>false</CODE>
 */

    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom) {
        return false;
    }

/**
 * Signals that an new page has to be started.
 * <P>
 * This does nothing. Has to be overridden if needed.
 *
 * @return  <CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
 */

    public boolean newPage() {
        if (!open) {
            return false;
        }
        return true;
    }

/**
 * Sets the page number to 0.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class if they actually support the use of
 * pagenumbers.
 */

    public void resetPageCount() {
    }

/**
 * Sets the page number.
 * <P>
 * This method should be overridden in the specific <CODE>DocWriter<CODE> classes
 * derived from this abstract class if they actually support the use of
 * pagenumbers.
 *
 * @param pageN   the new page number
 */

    public void setPageCount(int pageN) {
    }

/**
 * Signals that the <CODE>Document</CODE> was closed and that no other
 * <CODE>Elements</CODE> will be added.
 */

    public void close() {
        open = false;
        try {
            os.flush();
            if (closeStream)
                os.close();
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

    // methods

/** Converts a <CODE>String</CODE> into a <CODE>Byte</CODE> array
 * according to the ISO-8859-1 codepage.
 * @param text the text to be converted
 * @return the conversion result
 */

    public static final byte[] getISOBytes(String text)
    {
        if (text == null)
            return null;
        int len = text.length();
        byte b[] = new byte[len];
        for (int k = 0; k < len; ++k)
            b[k] = (byte)text.charAt(k);
        return b;
    }

/**
 * Let the writer know that all writing has to be paused.
 */

    public void pause() {
        pause = true;
    }

    /**
     * Checks if writing is paused.
     *
     * @return		<CODE>true</CODE> if writing temporarily has to be paused, <CODE>false</CODE> otherwise.
     */

    public boolean isPaused() {
        return pause;
    }

/**
 * Let the writer know that writing may be resumed.
 */

    public void resume() {
        pause = false;
    }

/**
 * Flushes the <CODE>BufferedOutputStream</CODE>.
 */

    public void flush() {
        try {
            os.flush();
        }
        catch(IOException ioe) {
            throw new ExceptionConverter(ioe);
        }
    }

/**
 * Writes a <CODE>String</CODE> to the <CODE>OutputStream</CODE>.
 *
 * @param string    the <CODE>String</CODE> to write
 * @throws IOException
 */

    protected void write(String string) throws IOException {
        os.write(getISOBytes(string));
    }

/**
 * Writes a number of tabs.
 *
 * @param   indent  the number of tabs to add
 * @throws IOException
 */

    protected void addTabs(int indent) throws IOException {
        os.write(NEWLINE);
        for (int i = 0; i < indent; i++) {
            os.write(TAB);
        }
    }

/**
 * Writes a key-value pair to the outputstream.
 *
 * @param   key     the name of an attribute
 * @param   value   the value of an attribute
 * @throws IOException
 */

    protected void write(String key, String value)
    throws IOException {
        os.write(SPACE);
        write(key);
        os.write(EQUALS);
        os.write(QUOTE);
        write(value);
        os.write(QUOTE);
    }

/**
 * Writes a starttag to the outputstream.
 *
 * @param   tag     the name of the tag
 * @throws IOException
 */

    protected void writeStart(String tag)
    throws IOException {
        os.write(LT);
        write(tag);
    }

/**
 * Writes an endtag to the outputstream.
 *
 * @param   tag     the name of the tag
 * @throws IOException
 */

    protected void writeEnd(String tag)
    throws IOException {
        os.write(LT);
        os.write(FORWARD);
        write(tag);
        os.write(GT);
    }

/**
 * Writes an endtag to the outputstream.
 * @throws IOException
 */

    protected void writeEnd()
    throws IOException {
        os.write(SPACE);
        os.write(FORWARD);
        os.write(GT);
    }

/**
 * Writes the markup attributes of the specified <CODE>MarkupAttributes</CODE>
 * object to the <CODE>OutputStream</CODE>.
 * @param markup   a <CODE>Properties</CODE> collection to write.
 * @return true, if writing the markup attributes succeeded
 * @throws IOException
 */
    protected boolean writeMarkupAttributes(Properties markup)
    throws IOException {
    	if (markup == null) return false;
    	Iterator<Object> attributeIterator = markup.keySet().iterator();
    	String name;
    	while (attributeIterator.hasNext()) {
    		name = String.valueOf(attributeIterator.next());
    		write(name, markup.getProperty(name));
    	}
    	markup.clear();
    	return true;
    }

    /** Checks if the stream is to be closed on document close
     * @return true if the stream is closed on document close
     *
     */
    public boolean isCloseStream() {
        return closeStream;
    }

    /** Sets the close state of the stream after document close
     * @param closeStream true if the stream is closed on document close
     *
     */
    public void setCloseStream(boolean closeStream) {
        this.closeStream = closeStream;
    }

    /**
     * @see com.itextpdf.text.DocListener#setMarginMirroring(boolean)
     */
    public boolean setMarginMirroring(boolean MarginMirroring) {
        return false;
    }

    /**
     * @see com.itextpdf.text.DocListener#setMarginMirroring(boolean)
     * @since	2.1.6
     */
    public boolean setMarginMirroringTopBottom(boolean MarginMirroring) {
        return false;
    }

}

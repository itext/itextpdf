/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with this program; if not, see http://www.gnu.org/licenses or
 * write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a
 * covered work must retain the producer line in every PDF that is created or
 * manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a
 * commercial license. Buying such a license is mandatory as soon as you develop
 * commercial activities involving the iText software without disclosing the
 * source code of your own applications. These activities include: offering paid
 * services to customers as an ASP, serving PDFs on the fly in a web
 * application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address:
 * sales@itextpdf.com
 */
package com.itextpdf.tool.xml.parser;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.text.xml.simpleparser.IanaEncodings;
import com.itextpdf.tool.xml.parser.io.EncodingUtil;
import com.itextpdf.tool.xml.parser.io.MonitorInputReader;
import com.itextpdf.tool.xml.parser.io.ParserMonitor;

/**
 * Reads an XML file. Attach a {@link XMLParserListener} for receiving events.
 *
 * @author redlab_b
 */
public class XMLParser {

    private State state;
    private final StateController controller;
    private final List<XMLParserListener> listeners;
    private final XMLParserMemory memory;
    private ParserMonitor monitor;
    private String text = null;
    private TagState tagState;
    private Charset charset;

    /**
     * Constructs a default XMLParser ready for HTML/XHTML processing.
     */
    public XMLParser() {
        this(true, Charset.defaultCharset());
    }

    /**
     * Constructs a XMLParser.
     *
     * @param isHtml  false if this parser is not going to parse HTML and
     *                whitespace should be submitted as text too.
     * @param charset charset
     */
    public XMLParser(final boolean isHtml, final Charset charset) {
        this.charset = charset;
        this.controller = new StateController(this, isHtml);
        controller.unknown();
        memory = new XMLParserMemory(isHtml);
        listeners = new CopyOnWriteArrayList<XMLParserListener>();
    }

    /**
     * Construct an XMLParser with the given XMLParserConfig ready for
     * HTML/XHTML processing..
     *
     * @param listener the listener
     * @param charset  the Charset
     */
    public XMLParser(final XMLParserListener listener, final Charset charset) {
        this(true, charset);
        listeners.add(listener);
    }

    /**
     * Construct a XMLParser with the given XMLParserConfig.
     *
     * @param isHtml   false if this parser is not going to parse HTML and
     *                 whitespace should be submitted as text too.
     * @param listener the listener
     * @param charset  the Charset to use
     */
    public XMLParser(final boolean isHtml, final XMLParserListener listener, final Charset charset) {
        this(isHtml, charset);
        listeners.add(listener);
    }

    /**
     * Constructs a new Parser with the default jvm charset.
     *
     * @param b        true if HTML is being parsed
     * @param listener the XMLParserListener
     */
    public XMLParser(final boolean b, final XMLParserListener listener) {
        this(b, Charset.defaultCharset());
        listeners.add(listener);
    }

    /**
     * Constructs a new Parser with HTML parsing set to true and the default jvm charset.
     *
     * @param listener the XMLParserListener
     */
    public XMLParser(final XMLParserListener listener) {
        this(true, Charset.defaultCharset());
        listeners.add(listener);
    }

    /**
     * If no <code>ParserListener</code> is added, parsing with the parser seems
     * useless no?
     *
     * @param pl the {@link XMLParserListener}
     * @return the parser
     */
    public XMLParser addListener(final XMLParserListener pl) {
        listeners.add(pl);
        return this;
    }

    /**
     * Removes a Listener from the list of listeners.
     *
     * @param pl the {@link XMLParserListener} to remove
     * @return the parser
     */
    public XMLParser removeListener(final XMLParserListener pl) {
        listeners.remove(pl);
        return this;
    }

    /**
     * Parse an InputStream with default encoding set
     *
     * @param in the InputStream to parse
     * @throws IOException if IO went wrong
     */
    public void parse(final InputStream in) throws IOException {
        parse(new InputStreamReader(in));
    }

    /**
     * Parse an InputStream that optionally detects encoding from the stream
     *
     * @param in             the InputStream to parse
     * @param detectEncoding true if encoding should be detected from the stream
     * @throws IOException if IO went wrong
     */
    public void parse(final InputStream in, final boolean detectEncoding) throws IOException {
        if (detectEncoding) {
            parse(detectEncoding(new BufferedInputStream(in)));
        } else {
            parse(in);
        }
    }

    /**
     * Parses an InputStream using the given encoding
     *
     * @param in      the stream to read
     * @param charSet to use for the constructed reader.
     * @throws IOException if reading fails
     */
    public void parse(final InputStream in, final Charset charSet) throws IOException {
        this.charset = charSet;
        InputStreamReader reader = new InputStreamReader(in, charSet);
        parse(reader);

    }

    /**
     * Parse an Reader
     *
     * @param reader the reader
     * @throws IOException if IO went wrong
     */
    public void parse(final Reader reader) throws IOException {
        parseWithReader(reader);
    }

    /**
     * The actual parse method
     *
     * @param r
     * @throws IOException
     */
    private void parseWithReader(final Reader reader) throws IOException {
        for (XMLParserListener l : listeners) {
            l.init();
        }
        Reader r;
        if (monitor != null) {
            r = new MonitorInputReader(reader, monitor);
        } else {
            r = reader;
        }
        char read[] = new char[1];
        try {
            while (-1 != (r.read(read))) {
                state.process(read[0]);
            }
        } finally {
            for (XMLParserListener l : listeners) {
                l.close();
            }
            r.close();
        }
    }

    /**
     * Detects encoding from a stream.
     *
     * @param in the stream
     * @return a Reader with the deduced encoding.
     * @throws IOException                  if IO went wrong
     * @throws UnsupportedEncodingException if unsupported encoding was detected
     */
    public InputStreamReader detectEncoding(final InputStream in) throws IOException, UnsupportedEncodingException {
        // we expect a '>' in the first 100 characters
    	in.mark(1028);
    	byte b4[] = new byte[4];
        int count = in.read(b4);
        if (count != 4)
            throw new IOException("Insufficient length");
        String encoding = XMLUtil.getEncodingName(b4);
        String decl = null;
        if (encoding.equals("UTF-8")) {
            StringBuffer sb = new StringBuffer();
            int c;
            while ((c = in.read()) != -1) {
                if (c == '>')
                    break;
                sb.append((char) c);
            }
            decl = sb.toString();
        } else if (encoding.equals("CP037")) {
            ByteArrayOutputStream bi = new ByteArrayOutputStream();
            int c;
            while ((c = in.read()) != -1) {
                if (c == 0x6e) // that's '>' in ebcdic
                    break;
                bi.write(c);
            }
            decl = new String(bi.toByteArray(), "CP037");
        }
        if (decl != null) {
            decl = EncodingUtil.getDeclaredEncoding(decl);
            if (decl != null)
                encoding = decl;
        }
        in.reset();
        return new InputStreamReader(in, IanaEncodings.getJavaEncoding(encoding));
    }

    /**
     * Set the current state.
     *
     * @param state the current state
     */
    protected void setState(final State state) {
        this.state = state;
    }

    /**
     * @param character the character to append
     * @return the parser
     */
    public XMLParser append(final char character) {
        this.memory.current().append(character);
        return this;

    }

    // /**
    // * @param str the String to append
    // * @return the parser
    // */
    // public XMLParser append(final String str) {
    // this.memory.current().write(str.getBytes());
    // return this;
    //
    // }

    /**
     * The state controller of the parser
     *
     * @return {@link StateController}
     */
    public StateController selectState() {
        return this.controller;
    }

    /**
     * Triggered when the UnknownState encountered anything before encountering
     * a tag.
     */
    public void unknownData() {
        for (XMLParserListener l : listeners) {
            l.unknownText(this.memory.current().toString());
        }
    }

    /**
     * Flushes the currently stored data in the buffer.
     */
    public void flush() {
        this.memory.resetBuffer();
    }

    /**
     * Returns the current content of the text buffer.
     *
     * @return current buffer content
     */
    public String current() {
        return this.memory.current().toString();
    }

    /**
     * Returns the XMLParserMemory.
     *
     * @return the memory
     */
    public XMLParserMemory memory() {
        return memory;
    }

    /**
     * Triggered when an opening tag has been encountered.
     */
    public void startElement() {
        currentTagState(TagState.OPEN);
        callText();
        for (XMLParserListener l : listeners) {
            l.startElement(this.memory.getCurrentTag(), this.memory.getAttributes(), this.memory.getNameSpace());
        }
        this.memory().flushNameSpace();
    }

    /**
     * Call this method to submit the text to listeners.
     */
    private void callText() {
        if (null != text && text.length() > 0) {
            // LOGGER .log(text);
            for (XMLParserListener l : listeners) {
                l.text(text);
            }
            text = null;
        }
    }

    /**
     * Triggered when a closing tag has been encountered.
     */
    public void endElement() {
        currentTagState(TagState.CLOSE);
        callText();
        for (XMLParserListener l : listeners) {
            l.endElement(this.memory.getCurrentTag(), this.memory.getNameSpace());
        }
    }

    /**
     * Triggered when content has been encountered.
     *
     * @param bs the content
     */
    public void text(final String bs) {
        text = bs;
    }

    /**
     * Triggered for comments.
     */
    public void comment() {
        callText();
        for (XMLParserListener l : listeners) {
            l.comment(this.memory.current().toString());
        }
    }

    /**
     * @return the current last character of the buffer or ' ' if none.
     */
    public char currentLastChar() {
        StringBuilder current2 = this.memory.current();
        int length = current2.length();
        CharSequence current = current2.subSequence(length - 2, length - 1);
        if (current.length() > 0) {
            return (char) (current.length() - 1);
        }
        return ' ';
    }

    /**
     * Get the current tag
     *
     * @return the current tag.
     */
    public String currentTag() {
        return this.memory.getCurrentTag();
    }

    /**
     * Get the state of the current tag
     *
     * @return the state of the current tag
     */
    public TagState currentTagState() {
        return this.tagState;
    }

    /**
     * Set the state of the current tag
     *
     * @param state the state of the current tag
     */
    private void currentTagState(final TagState state) {
        this.tagState = state;
    }

    /**
     * @param monitor the monitor to set
     */
    public void setMonitor(final ParserMonitor monitor) {
        this.monitor = monitor;
    }

    /**
     * @return the current buffer as a String
     */
    public String bufferToString() {
        return this.memory.current().toString();
    }

    /**
     * @param bytes the byte array to append
     * @return this instance of the XMLParser
     */
    public XMLParser append(final char[] bytes) {
        this.memory.current().append(bytes);
        return this;
    }

    /**
     * @return the size of the buffer
     */
    public int bufferSize() {
        return (null != this.memory.current()) ? this.memory.current().length() : 0;
    }

    /**
     * Appends the given string to the buffer.
     *
     * @param string the String to append
     * @return this instance of the XMLParser
     */
    public XMLParser append(final String string) {
        this.memory.current().append(string);
        return this;

    }

    /**
     * Returns the current used character set.
     *
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

}

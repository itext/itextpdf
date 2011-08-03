/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
package com.itextpdf.tool.xml.parser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.itextpdf.text.xml.XMLUtil;
import com.itextpdf.text.xml.simpleparser.IanaEncodings;
import com.itextpdf.tool.xml.parser.io.MonitorInputReader;
import com.itextpdf.tool.xml.parser.io.ParserMonitor;

/**
 * Reads an XML file. Attach a {@link XMLParserListener} for receiving events.
 *
 * @author redlab_b
 *
 */
public class XMLParser {

	private State state;
	private final StateController controller;
	private final List<XMLParserListener> listeners;
	private final XMLParserMemory memory;
	private ParserMonitor monitor;
	private byte[] text = null;
	private TagState tagState;

	/**
	 * Constructs a default XMLParser ready for HTML/XHTML processing.
	 */
	public XMLParser() {
		this(true);
	}
	/**
	 * Constructs a XMLParser.
	 * @param isHtml false if this parser is not going to parse HTML and whitespace should be submitted as text too.
	 */
	public XMLParser(final boolean isHtml) {
		this.controller = new StateController(this, isHtml);
		controller.unknown();
		memory = new XMLParserMemory();
		listeners = new CopyOnWriteArrayList<XMLParserListener>();
	}

	/**
	 * Construct an HTML XMLParser with the given XMLParserConfig.
	 * @param listener the listener
	 */
	public XMLParser(final XMLParserListener listener) {
		this(true);
		listeners.add(listener);
	}
	/**
	 * Construct an HTML XMLParser with the given XMLParserConfig.
	 * @param isHtml false if this parser is not going to parse HTML and whitespace should be submitted as text too.
	 * @param listener the listener
	 */
	public XMLParser(final boolean isHtml, final XMLParserListener listener) {
		this(isHtml);
		listeners.add(listener);
	}

	/**
	 * If no <code>ParserListener</code> is added, parsing with the parser seems useless no?
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
	 * @param pl the {@link XMLParserListener} to remove
	 * @return the parser
	 */
	public XMLParser removeListener(final XMLParserListener pl) {
		listeners.remove(pl);
		return this;
	}

	/**
	 * Parse an InputStream.
	 * @param in the InputStream to parse
	 * @throws IOException if IO went wrong
	 */
	public void parse(final InputStream in) throws IOException {
		parseStream(in);
	}

	/**
	 * Parse an InputStream.
	 * @param in the InputStream to parse
	 * @param detectEncoding true if encoding should be detected from the stream
	 * @throws IOException if IO went wrong
	 */
	public void parse(final InputStream in, final boolean detectEncoding) throws IOException {
		if (detectEncoding) {
			parse(detectEncoding(in));
		} else {
			parse(in);
		}
	}

	/**
	 * Parse an Reader
	 * @param reader the reader
	 * @throws IOException if IO went wrong
	 */
	public void parse(final Reader reader) throws IOException {
		for (XMLParserListener l : listeners) {
			l.init();
		}
		int read = -1;
		Reader r;
		if (monitor != null) {
			r = new MonitorInputReader(reader, monitor);
		} else {
			r = reader;
		}
		try {
		while (-1 != (read = r.read())) {
			state.process(read);
		}
		} finally {
			for (XMLParserListener l : listeners) {
				l.close();
			}
			r.close();
		}
	}
	/**
	 * @param r
	 * @throws IOException
	 */
	private void parseStream(final InputStream r) throws IOException {
		for (XMLParserListener l : listeners) {
			l.init();
		}
		int read = -1;
		try {
			while (-1 != (read = r.read())) {
				state.process(read);
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
	 * @throws IOException if IO went wrong
	 * @throws UnsupportedEncodingException if unsupported encoding was detected
	 */
	public InputStreamReader detectEncoding(final InputStream in) throws IOException, UnsupportedEncodingException {
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
			decl = getDeclaredEncoding(decl);
			if (decl != null)
				encoding = decl;
		}
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
	 * @param character the int that will be appended to the buffer.
	 * @return the parser
	 */
	public XMLParser append(final int character) {
		this.memory.current().write(character);
		return this;

	}

	/**
	 * @param character the character to append
	 * @return the parser
	 */
	public XMLParser append(final char character) {
		this.memory.current().write(character);
		return this;

	}

//	/**
//	 * @param str the String to append
//	 * @return the parser
//	 */
//	public XMLParser append(final String str) {
//		this.memory.current().write(str.getBytes());
//		return this;
//
//	}

	/**
	 * The state controller of the parser
	 * @return {@link StateController}
	 */
	public StateController selectState() {
		return this.controller;
	}

	/**
	 * Triggered when the UnknownState encountered anything before encountering a tag.
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
	 * @return current buffer content
	 */
	public byte[] current() {
		return this.memory.current().toByteArray();
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
		if (null != text && text.length > 0) {
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
	public void text(final byte[] bs) {
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

	private static String getDeclaredEncoding(final String decl) {
		if (decl == null)
			return null;
		int idx = decl.indexOf("encoding");
		if (idx < 0)
			return null;
		int idx1 = decl.indexOf('"', idx);
		int idx2 = decl.indexOf('\'', idx);
		if (idx1 == idx2)
			return null;
		if (idx1 < 0 && idx2 > 0 || idx2 > 0 && idx2 < idx1) {
			int idx3 = decl.indexOf('\'', idx2 + 1);
			if (idx3 < 0)
				return null;
			return decl.substring(idx2 + 1, idx3);
		}
		if (idx2 < 0 && idx1 > 0 || idx1 > 0 && idx1 < idx2) {
			int idx3 = decl.indexOf('"', idx1 + 1);
			if (idx3 < 0)
				return null;
			return decl.substring(idx1 + 1, idx3);
		}
		return null;
	}

	/**
	 * @return the current last character of the buffer or ' ' if none.
	 */
	public char currentLastChar() {
		byte[] current = this.memory.current().toByteArray();
		if (current.length > 0) {
			return (char)(current.length -1);
		}
		return ' ';
	}

	/**
	 * Get the current tag
	 * @return the current tag.
	 */
	public String currentTag() {
		return this.memory.getCurrentTag();
	}
	/**
	 * Get the state of the current tag
	 * @return the state of the current tag
	 */
	public TagState currentTagState() {
		return this.tagState;
	}

	/**
	 *  Set the state of the current tag
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
	 * @return this XMLParser
	 */
	public XMLParser append(final byte[] bytes) {
		for (byte b : bytes) {
			this.memory.current().write(b);
		}
		return this;
	}
	/**
	 * @return the size of the buffer
	 */
	public int bufferSize() {
		return (null != this.memory.current())?this.memory.current().size():0;
	}

}

/*
 * @(#)XmlWriter.java				0.22 2000/02/02
 *       release iText0.3:			0.22 2000/02/14
 *       release iText0.35:			0.22 2000/08/11
 * 
 * Copyright (c) 1999, 2000 Bruno Lowagie.
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
					
import com.lowagie.text.DocListener;						
import com.lowagie.text.Document;				
import com.lowagie.text.DocumentException;					
import com.lowagie.text.DocWriter;					
import com.lowagie.text.Element;					
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
 * @version 0.22, 2000/02/02
 *
 * @since   iText0.30
 */

public class XmlWriter extends DocWriter implements DocListener {

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

	/** This is a possible XML-tag. */
	public static final byte[] BEGINCOMMENT = "<!--".getBytes(); 	

	/** This is a possible XML-tag. */
	public static final byte[] ENDCOMMENT = "-->".getBytes();	

	/** This is a possible XML-tag. */
	public static final byte[] XML = "XML".getBytes();	

	/** This is a possible XML-tag. */
	public static final byte[] HEAD = "HEAD".getBytes();	

	/** This is a possible XML-tag. */
	public static final byte[] BODY = "BODY".getBytes();	

	/** This is a possible XML-tag. */
	public static final byte[] META = "META".getBytes();

// static membervariables (attributes)

	/** This is a possible XML attribute for the META tag. */
	public static final String CONTENT = "CONTENT";

	/** This is a possible XML attribute for the META tag. */
	public static final String TYPE = "TYPE";

// membervariables

	/** This represents the indentation of the XML. */
	private int indent = 0;

// constructor

	/**
	 * Constructs an <CODE>XmlWriter</CODE>.
	 *
	 * @param	document	The <CODE>Document</CODE> that has to be written as XML
	 * @param	os			The <CODE>OutputStream</CODE> the writer has to write to.
	 * 
	 * @since	iText0.30
	 */

	protected XmlWriter(Document doc, OutputStream os) {
		super(doc, os);

		System.err.println("Warning: class XmlWriter is not finished yet! The output will not be genuine XML!");

		document.addDocListener(this);
		try {
			writeBeginTag(XML);
			writeBeginTag(HEAD);
			document.addProducer();
			document.addCreationDate();
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
	 * 
	 * @since	iText0.30
	 */

	public static XmlWriter getInstance(Document document, OutputStream os) {
		 return new XmlWriter(document, os);
	}

// implementation of the DocListener methods

    /**
     * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>. 
     *
	 * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
	 * @throws	DocumentException	when a document isn't open yet, or has been closed
	 *
     * @since   iText0.30
     */

    public boolean add(Element element) throws DocumentException {
		try {
			os.write(element.toString().getBytes());
			return true;
		}
		catch(IOException ioe) {
			return false;
		}
	}

    /**
     * Signals that the <CODE>Document</CODE> has been opened and that
	 * <CODE>Elements</CODE> can be added. 
	 *
	 * @return	<CODE>void</CODE>
	 *
     * @since   iText0.30
     */

    public void open() {
		super.open();
		try {
			writeEndTag(HEAD);
			writeBeginTag(BODY);
		}
		catch(IOException ioe) {
		}
	}

    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
	 * <CODE>Elements</CODE> will be added. 
     *
	 * @return	<CODE>void</CODE>
	 *
     * @since   iText0.30
     */

    public void close() {
		try {
			writeEndTag(BODY);
			writeEndTag(XML);
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
	 *
	 * @since	iText0.30
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
	 * Writes an end tag.
	 * <P>
	 * This method writes a given tag between brackets.
	 *
	 * @param	tag		the tag that has to be written
	 * @return	<CODE>void</CODE>
	 * @throws	IOException
	 *
	 * @since	iText0.30
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
	 * Writes a new line to the outputstream.
	 *
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	private void newLine() throws IOException {
		os.write(NEWLINE);
	}

	/**
	 * Writes a number of tabs to the outputstream.
	 *
	 * @return	<CODE>void</CODE>
	 *
	 * @since	iText0.30
	 */

	private void tab() throws IOException {
		for (int i = 0; i < indent; i++) {
			os.write(TAB);
		}
	}
}

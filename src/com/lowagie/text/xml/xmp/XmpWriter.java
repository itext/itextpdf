/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie.
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
 * the Initial Developer are Copyright (C) 1999-2005 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2005 by Paulo Soares. All Rights Reserved.
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

package com.lowagie.text.xml.xmp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Iterator;

import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfString;

/**
 * With this class you can create an Xmp Stream that can be used for adding
 * Metadata to a PDF Dictionary. Remark that this class doesn't cover the
 * complete XMP specification. 
 */
public class XmpWriter {

	/** A possible charset for the XMP. */
	public static final int UTF8 = 0;
	/** A possible charset for the XMP. */
	public static final int UTF16 = 1;
	/** A possible charset for the XMP. */
	public static final int UTF16BE = 2;
	/** A possible charset for the XMP. */
	public static final int UTF16LE = 3;
	
	/** String used to fill the extra space. */
	public static final String EXTRASPACE = "                                                                                                   \n";
	
	/** You can add some extra space in the XMP packet; 1 unit in this variable represents 100 spaces and a newline. */
	protected int extraSpace;
	
	/** The writer to which you can write bytes for the XMP stream. */
	protected OutputStreamWriter writer;
	
	/** The about string that goes into the rdf:Description tags. */
	protected String about;
	
	/** The end attribute. */
	protected char end = 'w';
	
	/**
	 * Creates an XmpWriter. 
	 * @param os
	 * @param utfEncoding
	 * @param extraSpace
	 * @throws IOException
	 */
	public XmpWriter(OutputStream os, int utfEncoding, int extraSpace) throws IOException {
		this.extraSpace = extraSpace;
		Charset charset;
		switch(utfEncoding) {
		case UTF16:
			charset = Charset.forName("UTF-16");
			break;
		case UTF16BE:
			charset = Charset.forName("UTF-16BE");
			break;
		case UTF16LE:
			charset = Charset.forName("UTF-16LE");
			break;
		default:
			charset = Charset.forName("UTF-8");
		}
		writer = new OutputStreamWriter(os, charset);
		writer.write("<?xpacket begin='\uFEFF' id='W5M0MpCehiHzreSzNTczkc9d' ?>\n");
		writer.write("<x:xmpmeta xmlns:x='adobe:ns:meta/'>\n");
		writer.write("<rdf:RDF xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>\n");
		about = "";
	}
	
	/**
	 * Creates an XmpWriter.
	 * @param os
	 * @throws IOException
	 */
	public XmpWriter(OutputStream os) throws IOException {
		this(os, UTF8, 20);
	}
	
	/** Sets the XMP to read-only */
	public void setReadOnly() {
		end = 'r';
	}
	
	/**
	 * @param about The about to set.
	 */
	public void setAbout(String about) {
		this.about = about;
	}
	
	/**
	 * Adds an rdf:Description.
	 * @param xmlns
	 * @param content
	 * @throws IOException
	 */
	public void addRdfDescription(String xmlns, String content) throws IOException {
		writer.write("<rdf:Description rdf:about='");
		writer.write(about);
		writer.write("' ");
		writer.write(xmlns);
		writer.write(">");
		writer.write(content);
		writer.write("</rdf:Description>\n");
	}
	
	/**
	 * Adds an rdf:Description.
	 * @param s
	 * @throws IOException
	 */
	public void addRdfDescription(XmpSchema s) throws IOException {
		writer.write("<rdf:Description rdf:about='");
		writer.write(about);
		writer.write("' ");
		writer.write(s.getXmlns());
		if (s.isShorthand()) {
			writer.write(s.toString());
			writer.write("/>\n");
		}
		else {
			writer.write(">");
			writer.write(s.toString());
			writer.write("</rdf:Description>\n");
		}
	}
	
	/**
	 * Flushes and closes the XmpWriter.
	 * @throws IOException
	 */
	public void close() throws IOException {
		writer.write("</rdf:RDF>");
		writer.write("</x:xmpmeta>\n");
		for (int i = 0; i < extraSpace; i++) {
			writer.write(EXTRASPACE);
		}
		writer.write("<?xpacket ends='" + end + "' ?>");
		writer.flush();
		writer.close();
	}
    
    /**
     * @param os
     * @param info
     * @throws IOException
     */
    public XmpWriter(OutputStream os, PdfDictionary info) throws IOException {
        this(os);
        if (info != null) {
        	DublinCoreSchema dc = new DublinCoreSchema(XmpSchema.FULL);
        	PdfSchema p = new PdfSchema(XmpSchema.SHORTHAND);
        	XmpBasicSchema basic = new XmpBasicSchema(XmpSchema.FULL);
        	for (Iterator it = info.getKeys().iterator(); it.hasNext();) {
        		PdfName key = (PdfName)it.next();
        		PdfObject obj = info.get(key);
        		if (obj == null)
        			continue;
        		if (PdfName.TITLE.equals(key)) {
        			dc.addTitle(((PdfString)obj).toUnicodeString());
        		}
        		if (PdfName.AUTHOR.equals(key)) {
        			dc.addAuthor(((PdfString)obj).toUnicodeString());
        		}
        		if (PdfName.SUBJECT.equals(key)) {
        			dc.addSubject(((PdfString)obj).toUnicodeString());
        		}
        		if (PdfName.KEYWORDS.equals(key)) {
        			p.addKeywords(((PdfString)obj).toUnicodeString());
        		}
        		if (PdfName.CREATOR.equals(key)) {
        			basic.addCreator(((PdfString)obj).toUnicodeString());
        		}
        		if (PdfName.PRODUCER.equals(key)) {
        			p.addProducer(((PdfString)obj).toUnicodeString());
        		}
        		if (PdfName.CREATIONDATE.equals(key)) {
        			basic.addCreationDate(((PdfDate)obj).getW3CDate());
        		}
        		if (PdfName.MODDATE.equals(key)) {
        			basic.addModDate(((PdfDate)obj).getW3CDate());
        		}
        	}
        	if (dc.size() > 0) addRdfDescription(dc);
        	if (p.size() > 0) addRdfDescription(p);
        	if (basic.size() > 0) addRdfDescription(basic);
        }
    }
}
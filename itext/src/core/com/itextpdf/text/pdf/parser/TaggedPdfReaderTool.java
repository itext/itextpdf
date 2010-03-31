/*
 * $Id: ContentOperator.java 4242 2010-01-02 23:22:20Z xlv $
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, et al.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text.pdf.parser;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;

/**
 * Converts a tagged PDF document into an XML file.
 * 
 * @since 5.0.2
 */
public class TaggedPdfReaderTool {

	/** The reader object from which the content streams are read. */
	PdfReader reader;
	/** The writer object to which the XML will be written */
	PrintWriter out;

	/**
	 * Parses a string with structured content.
	 * 
	 * @param reader
	 *            the PdfReader that has access to the PDF file
	 * @param os
	 *            the OutputStream to which the resulting xml will be written
	 */
	public void convertToXml(PdfReader reader, OutputStream os)
			throws IOException {
		this.reader = reader;
		out = new PrintWriter(os);
		// get the StructTreeRoot from the root object
		PdfDictionary catalog = reader.getCatalog();
		PdfDictionary struct = catalog.getAsDict(PdfName.STRUCTTREEROOT);
		// Inspect the child or children of the StructTreeRoot
		inspectChild(struct.getDirectObject(PdfName.K));
		out.flush();
		out.close();
	}

	/**
	 * Inspects a child of a structured element. This can be an array or a
	 * dictionary.
	 * 
	 * @param k
	 *            the child to inspect
	 * @throws IOException
	 */
	public void inspectChild(PdfObject k) throws IOException {
		if (k == null)
			return;
		if (k instanceof PdfArray)
			inspectChildArray((PdfArray) k);
		else if (k instanceof PdfDictionary)
			inspectChildDictionary((PdfDictionary) k);
	}

	/**
	 * If the child of a structured element is an array, we need to loop over
	 * the elements.
	 * 
	 * @param k
	 *            the child array to inspect
	 */
	public void inspectChildArray(PdfArray k) throws IOException {
		if (k == null)
			return;
		for (int i = 0; i < k.size(); i++) {
			inspectChild(k.getDirectObject(i));
		}
	}

	/**
	 * If the child of a structured element is a dictionary, we inspect the
	 * child; we may also draw a tag.
	 * 
	 * @param k
	 *            the child dictionary to inspect
	 */
	public void inspectChildDictionary(PdfDictionary k) throws IOException {
		if (k == null)
			return;
		PdfName s = k.getAsName(PdfName.S);
		if (s != null) {
			String tag = s.toString().substring(1);
			out.print("<");
			out.print(tag);
			out.print(">");
			PdfDictionary dict = k.getAsDict(PdfName.PG);
			if (dict != null)
				parseTag(tag, k.getDirectObject(PdfName.K), dict);
			inspectChild(k.get(PdfName.K));
			out.print("</");
			out.print(tag);
			out.println(">");
		} else
			inspectChild(k.get(PdfName.K));
	}

	/**
	 * Searches for a tag in a page.
	 * 
	 * @param tag
	 *            the name of the tag
	 * @param object
	 *            an identifier to find the marked content
	 * @param page
	 *            a page dictionary
	 * @throws IOException
	 */
	public void parseTag(String tag, PdfObject object, PdfDictionary page)
			throws IOException {
		PRStream stream = (PRStream) page.getAsStream(PdfName.CONTENTS);
		// if the identifier is a number, we can extract the content right away
		if (object instanceof PdfNumber) {
			PdfNumber mcid = (PdfNumber) object;
			RenderFilter filter = new MarkedContentRenderFilter(mcid.intValue());
			TextExtractionStrategy strategy = new SimpleTextExtractionStrategy();
			FilteredTextRenderListener listener = new FilteredTextRenderListener(
					strategy, filter);
			PdfContentStreamProcessor processor = new PdfContentStreamProcessor(
					listener);
			processor.processContent(PdfReader.getStreamBytes(stream), page
					.getAsDict(PdfName.RESOURCES));
			out.print(SimpleXMLParser.escapeXML(listener.getResultantText(), true));
		}
		// if the identifier is an array, we call the parseTag method
		// recursively
		else if (object instanceof PdfArray) {
			PdfArray arr = (PdfArray) object;
			int n = arr.size();
			for (int i = 0; i < n; i++) {
				parseTag(tag, arr.getPdfObject(i), page);
				if (i < n - 1)
					out.println();
			}
		}
		// if the identifier is a dictionary, we get the resources from the
		// dictionary
		else if (object instanceof PdfDictionary) {
			PdfDictionary mcr = (PdfDictionary) object;
			parseTag(tag, mcr.getDirectObject(PdfName.MCID), mcr
					.getAsDict(PdfName.PG));
		}
	}

}

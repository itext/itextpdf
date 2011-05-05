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
package com.itextpdf.tool.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssFileProcessor;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;

/**
 * A helper class for parsing XHTML/CSS flow to PDF.
 *
 * @author redlab_b
 *
 */
public class XMLWorkerHelper {

	private XMLWorker worker;
	private CssFile defaultCssFile;
	private final Object lock = new Object();

	/**
	 *
	 */
	public XMLWorkerHelper() {

	}

	/**
	 * @return the default css file.
	 */
	public CssFile getDefaultCSS() {
		synchronized (lock) {
			if (null == this.defaultCssFile) {
				final InputStream in = XMLWorkerHelper.class.getResourceAsStream("/default.css");
				if (null != in) {
					final CssFileProcessor cssFileProcessor = new CssFileProcessor();
					int i = -1;
					try {
						while (-1 != (i = in.read())) {
							cssFileProcessor.process((char) i);
						}
						this.defaultCssFile = cssFileProcessor.getCss();
					} catch (final IOException e) {
						throw new RuntimeWorkerException(e);
					} finally {
						try {
							in.close();
						} catch (final IOException e) {
							throw new RuntimeWorkerException(e);
						}
					}
				}
			}
		}
		return defaultCssFile;
	}

	/**
	 * Parses the xml data in the given reader and sends created {@link Element}s to the defined ElementHandler.<br />
	 * This method configures the XMLWorker and XMLParser to parse (X)HTML/CSS and accept unknown tags.
	 *
	 * @param d the handler
	 * @param in the reader
	 * @throws IOException thrown when something went wrong with the IO
	 */
	public void parseXHtml(final ElementHandler d, final Reader in) throws IOException {
		XMLWorkerConfigurationImpl config = new XMLWorkerConfigurationImpl();
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver();
		CssFile defaultCSS = getDefaultCSS();
		if (null != defaultCSS) {
			cssResolver.addCssFile(defaultCSS);
		}
		config.tagProcessorFactory(new Tags().getHtmlTagProcessorFactory()).cssResolver(cssResolver)
				.acceptUnknown(true);
		final XMLWorker worker = new XMLWorkerImpl(config);
		worker.setDocumentListener(d);
		XMLParser p = new XMLParser(worker);
		p.parse(in);
	}
	/**
	 * Parses the xml data. This method configures the XMLWorker to parse (X)HTML/CSS and accept unknown tags.
	 * Writes the output in the given PdfWriter with the given document.
	 * @param writer the PdfWriter
	 * @param document the Document
	 * @param in the reader
	 * @throws IOException thrown when something went wrong with the IO
	 */
	public void parseXHtml(final PdfWriter writer, final Document document, final Reader in) throws IOException {
		XMLWorkerConfigurationImpl config = new XMLWorkerConfigurationImpl();
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver();
		CssFile defaultCSS = getDefaultCSS();
		if (null != defaultCSS) {
			cssResolver.addCssFile(defaultCSS);
		}
		config.tagProcessorFactory(new Tags().getHtmlTagProcessorFactory()).cssResolver(cssResolver)
		.acceptUnknown(true).pdfWriter(writer).document(document);
		final XMLWorker worker = new XMLWorkerImpl(config);
		worker.setDocumentListener(new ElementHandler() {
			
			public void addAll(List<Element> currentContent) throws DocumentException {
				for (Element e : currentContent) {
					document.add(e);
				}
			}
			
			public void add(Element e) throws DocumentException {
				document.add(e);
				
			}
		});
		XMLParser p = new XMLParser(worker);
		p.parse(in);
	}

	/**
	 *
	 * @param d
	 * @param in
	 * @param config
	 * @throws IOException
	 */
	public void parseXHtml(final ElementHandler d, final Reader in, final XMLWorkerConfig config) throws IOException {
		final XMLWorker worker = new XMLWorkerImpl(config);
		worker.setDocumentListener(d);
		XMLParser p = new XMLParser(worker);
//		p.addListener(new ParserListenerWriter(new Appender() {
//
//			public Appender append(final String str) {
//				System.out.print(str);
//				return this;
//			}
//
//			public Appender append(final char c) {
//				System.out.print(c);
//				return this;
//			}
//
//		}, true));
		p.parse(in);
	}

	/**
	 * @param xml
	 * @throws IOException
	 */
	@Deprecated
	public void processXML(final byte[] xml) throws IOException {
		XMLParser p = new XMLParser();
		p.addListener(worker);
		p.parse(new ByteArrayInputStream(xml));
	}

	/**
	 * @param html
	 * @throws IOException
	 */
	@Deprecated
	public void processHTML(final byte[] html) throws IOException {
		XMLParser p = new XMLParser();
		p.addListener(worker);
		p.parse(new ByteArrayInputStream(html));
	}

	/**
	 * @param worker the worker to set
	 */
	@Deprecated
	public void setWorker(final XMLWorker worker) {
		this.worker = worker;
	}

	/**
	 * @return the worker
	 */
	@Deprecated
	public SimpleXMLDocHandler getWorker() {
		return worker;
	}

}

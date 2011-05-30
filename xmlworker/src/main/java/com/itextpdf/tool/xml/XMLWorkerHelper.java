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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssFileProcessor;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * A helper class for parsing XHTML/CSS or XML flow to PDF.
 *
 * @author redlab_b
 *
 */
public class XMLWorkerHelper {

	private static XMLWorkerHelper myself = new XMLWorkerHelper();
	private CssFile defaultCssFile;
	private final Object lock = new Object();

	/**
	 * Get a XMLWorkerHelper
	 * @return a singleton instance of XMLWorkerHelper
	 */
	public static XMLWorkerHelper getInstance() {
		return myself ;

	}
	/**
	 *
	 */
	private XMLWorkerHelper() {

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
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext();
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d, null)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(true, worker);
		p.parse(in);
	}
	/**
	 * Parses the xml data. This method configures the XMLWorker to parse (X)HTML/CSS and accept unknown tags.
	 * Writes the output in the given PdfWriter with the given document.
	 * @param writer the PdfWriter
	 * @param doc the Document
	 * @param in the reader
	 * @throws IOException thrown when something went wrong with the IO
	 */
	public void parseXHtml(final PdfWriter writer, final Document doc, final Reader in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext();
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(true, worker);
		p.parse(in);
	}
	/**
	 * @param writer
	 * @param doc
	 * @param in
	 * @throws IOException
	 */
	public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext();
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(true, worker);
		p.parse(in);
	}
	/**
	 * @param d
	 * @param resourceAsStream
	 * @throws IOException
	 */
	public void parseXHtml(final ElementHandler d, final InputStream in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext();
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d, null)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(true, worker);
		p.parse(in);
	}

}

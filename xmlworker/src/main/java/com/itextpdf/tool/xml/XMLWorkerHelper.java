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
package com.itextpdf.tool.xml;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.css.*;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.ElementHandlerPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.io.*;
import java.nio.charset.Charset;

/**
 * A helper class for parsing XHTML/CSS or XML flow to PDF.
 *
 * @author redlab_b
 *
 */
public class XMLWorkerHelper {

	private static XMLWorkerHelper myself = new XMLWorkerHelper();

	/**
	 * Get a Singleton XMLWorkerHelper
	 *
	 * @return a singleton instance of XMLWorkerHelper
	 */
	public synchronized static XMLWorkerHelper getInstance() {
		return myself;

	}

	private TagProcessorFactory tpf;
	private CssFile defaultCssFile;

	/**
	 */
	private XMLWorkerHelper() {

	}

	/**
	 * @return the default css file.
	 */
    public static synchronized CssFile getCSS(InputStream in) {
        CssFile cssFile = null;
        if (null != in) {
            final CssFileProcessor cssFileProcessor = new
                    CssFileProcessor();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            try {
                char[] buffer = new char[8192];
                int length;
                while ((length = br.read(buffer)) > 0) {
                    for(int i = 0 ; i < length; i++) {
                        cssFileProcessor.process(buffer[i]);
                    }
                }
                cssFile = new CSSFileWrapper(cssFileProcessor.getCss(), true);
            } catch (final IOException e) { throw new RuntimeWorkerException(e); }
            finally
            { try { in.close(); } catch (final IOException e) { throw new RuntimeWorkerException(e); } }
        }
        return cssFile;
    }

    public synchronized CssFile getDefaultCSS() {
        if (null == defaultCssFile) {
            defaultCssFile = getCSS(XMLWorkerHelper.class.getResourceAsStream("/default.css"));
        }
        return defaultCssFile;
    }

	/**
	 * Parses the xml data in the given reader and sends created {@link Element}
	 * s to the defined ElementHandler.<br />
	 * This method configures the XMLWorker and XMLParser to parse (X)HTML/CSS
	 * and accept unknown tags.
	 *
	 * @param d the handler
	 * @param in the reader
	 * @throws IOException thrown when something went wrong with the IO
	 */
	public void parseXHtml(final ElementHandler d, final Reader in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext(null);
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d,
				null)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser();
		p.addListener(worker);
		p.parse(in);
	}

	/**
	 * Parses the xml data. This method configures the XMLWorker to parse
	 * (X)HTML/CSS and accept unknown tags. Writes the output in the given
	 * PdfWriter with the given document.
	 *
	 * @param writer the PdfWriter
	 * @param doc the Document
	 * @param in the reader
	 * @throws IOException thrown when something went wrong with the IO
	 */
	public void parseXHtml(final PdfWriter writer, final Document doc, final Reader in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext(null);
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new PdfWriterPipeline(doc,
				writer)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser();
		p.addListener(worker);
		p.parse(in);
	}

	/**
	 * @param writer the writer to use
	 * @param doc the document to use
	 * @param in the {@link InputStream} of the XHTML source.
	 * @throws IOException if the {@link InputStream} could not be read.
	 */
	public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in) throws IOException {
		parseXHtml(writer,doc,in, XMLWorkerHelper.class.getResourceAsStream("/default.css"), null, new XMLWorkerFontProvider());
	}

	/**
	 * @param writer the writer to use
	 * @param doc the document to use
	 * @param in the {@link InputStream} of the XHTML source.
	 * @param charset the charset to use
	 * @throws IOException if the {@link InputStream} could not be read.
	 */
	public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final Charset charset) throws IOException {
		parseXHtml(writer,doc,in, XMLWorkerHelper.class.getResourceAsStream("/default.css"), charset);
	}

    /**
	 * @param writer the writer to use
	 * @param doc the document to use
	 * @param in the {@link InputStream} of the XHTML source.
     * @param in the {@link CssFiles} of the css files.
	 * @param charset the charset to use
	 * @throws IOException if the {@link InputStream} could not be read.
	 */
	public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final Charset charset, final FontProvider fontProvider) throws IOException {
        CssFilesImpl cssFiles = new CssFilesImpl();
        if (inCssFile != null)
            cssFiles.add(getCSS(inCssFile));
        else
            cssFiles.add(getDefaultCSS());
        StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
        HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(fontProvider));
        hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
        HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(doc, writer));
        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        XMLWorker worker = new XMLWorker(pipeline, true);
        XMLParser p = new XMLParser(true, worker, charset);
		if (charset != null)
			p.parse(in, charset);
		else
			p.parse(in);
	}

    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile) throws IOException {
        parseXHtml(writer, doc, in, inCssFile, null, new XMLWorkerFontProvider());
	}

    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final FontProvider fontProvider) throws IOException {
        parseXHtml(writer, doc, in, inCssFile, null, fontProvider);
	}

    public void parseXHtml(final PdfWriter writer, final Document doc, final InputStream in, final InputStream inCssFile, final Charset charset) throws IOException {
        parseXHtml(writer, doc, in, inCssFile, charset, new XMLWorkerFontProvider());
	}

	/**
	 * @param d the ElementHandler
	 * @param in the InputStream
	 * @param charset the charset to use
	 * @throws IOException if something went seriously wrong with IO.
	 */
	public void parseXHtml(final ElementHandler d, final InputStream in, final Charset charset) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
		HtmlPipelineContext hpc = new HtmlPipelineContext(null);
		hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(getDefaultTagProcessorFactory());
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(hpc, new ElementHandlerPipeline(d,
				null)));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser(true, worker, charset);
		if (charset != null)
			p.parse(in, charset);
		else
			p.parse(in);
	}

	/**
	 * Get a CSSResolver implementation.
	 *
	 * @param addDefaultCss true if the defaultCss should already be added.
	 * @return the default CSSResolver
	 *
	 */
	public CSSResolver getDefaultCssResolver(final boolean addDefaultCss) {
		CSSResolver resolver = new StyleAttrCSSResolver();
		if (addDefaultCss) {
			resolver.addCss(getDefaultCSS());
		}
		return resolver;
	}

	/**
	 * Retrieves the default factory for processing HTML tags from
	 * {@link Tags#getHtmlTagProcessorFactory()}. On subsequent calls the same
	 * {@link TagProcessorFactory} is returned every time. <br />
	 * @return a
	 *         <code>DefaultTagProcessorFactory<code> that maps HTML tags to {@link TagProcessor}s
	 */
	protected synchronized TagProcessorFactory getDefaultTagProcessorFactory() {
		if (null == tpf) {
			tpf = Tags.getHtmlTagProcessorFactory();
		}
		return tpf;
	}
        
    /**
     * Parses an HTML string and a string containing CSS into a list of Element objects.
     * The FontProvider will be obtained from iText's FontFactory object.
     * 
     * @param   html    a String containing an XHTML snippet
     * @param   css     a String containing CSS
     * @return  an ElementList instance
     */
    public static ElementList parseToElementList(String html, String css) throws IOException {
        // CSS
        CSSResolver cssResolver = new StyleAttrCSSResolver();
        if (css != null) {
            CssFile cssFile = XMLWorkerHelper.getCSS(new ByteArrayInputStream(css.getBytes()));
            cssResolver.addCss(cssFile);
        }
        
        // HTML
        CssAppliers cssAppliers = new CssAppliersImpl(FontFactory.getFontImp());
        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);
        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
        htmlContext.autoBookmark(false);
        
        // Pipelines
        ElementList elements = new ElementList();
        ElementHandlerPipeline end = new ElementHandlerPipeline(elements, null);
        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, end);
        CssResolverPipeline cssPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
        
        // XML Worker
        XMLWorker worker = new XMLWorker(cssPipeline, true);
        XMLParser p = new XMLParser(worker);
        p.parse(new ByteArrayInputStream(html.getBytes()));
        
        return elements;
    }
}

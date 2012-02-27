/*
 * $Id: $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2012 1T3XT BVBA
 * Authors: VVB, Bruno Lowagie, et al.
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
package com.itextpdf.tool.xml.svg;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CSSFileWrapper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.CssFileProcessor;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.svg.css.StyleAttrSvgCSSResolver;


public class XMLHelperForSVG {
	private static XMLHelperForSVG myself = new XMLHelperForSVG();

	/**
	 * Get a Singleton XMLWorkerHelper
	 *
	 * @return a singleton instance of XMLWorkerHelper
	 */
	public synchronized static XMLHelperForSVG getInstance() {
		return myself;

	}

	private TagProcessorFactory tpf;
	private CssFile defaultCssFile;

	/**
	 */
	private XMLHelperForSVG() {

	}

	/**
	 * @return the default css file.
	 */
	public synchronized CssFile getDefaultCSS() {
		if (null == defaultCssFile) {
			final InputStream in = XMLWorkerHelper.class.getResourceAsStream("/default.css");
			if (null != in) {
				final CssFileProcessor cssFileProcessor = new CssFileProcessor();
				int i = -1;
				try {
					while (-1 != (i = in.read())) {
						cssFileProcessor.process((char) i);
					}
					defaultCssFile = new CSSFileWrapper(cssFileProcessor.getCss(), true);
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
		return defaultCssFile;
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
	public PdfTemplate parseToTemplate(final PdfContentByte cb, final Reader in) throws IOException {
		CssFilesImpl cssFiles = new CssFilesImpl();
		cssFiles.add(getDefaultCSS());
		CSSResolver cssResolver = new StyleAttrSvgCSSResolver(cssFiles);
		SvgPipelineContext hpc = new SvgPipelineContext();
		hpc.setTagFactory(getDefaultTagProcessorFactory());
		PdfTemplatePipeline templatePipeline = new PdfTemplatePipeline(cb);
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new SvgPipeline(hpc, templatePipeline));
		XMLWorker worker = new XMLWorker(pipeline, true);
		XMLParser p = new XMLParser();
		p.addListener(worker);
		p.parse(in);
		return templatePipeline.getTemplate();
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
			tpf = SvgTags.getSvgTagProcessorFactory();
		}
		return tpf;
	}
}

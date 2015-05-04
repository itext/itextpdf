/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.pipeline.html;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Map.Entry;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Experimental;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.PipelineException;
import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.ctx.MapContext;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;

/**
 * This pipeline can automagically create documents. Allowing you to parse
 * continuously, without needing to renew the configuration. This class does
 * expect {@link PdfWriterPipeline} to be the last pipe of the line. If a
 * {@link HtmlPipeline} is available it's context will also be reset.
 *
 * @author redlab_b
 *
 */
@SuppressWarnings("rawtypes")
@Experimental("Untested for a while, forgot about it's existance - thus, not yet documented")
public class AutoDocPipeline extends AbstractPipeline {

	private final FileMaker fm;
	private final String tag;
	private final String opentag;
	private final Rectangle pagesize;

	/**
	 * Constructor
	 * @param fm a FileMaker to provide a stream for every new document
	 * @param tag the tag on with to create a new document and close it
	 * @param opentag the tag on which to open the document ( {@link Document#open()}
	 * @param pagesize the pagesize for the documents
	 *
	 */
	@SuppressWarnings("unchecked")
	public AutoDocPipeline(final FileMaker fm, final String tag, final String opentag, final Rectangle pagesize) {
		super(null);
		this.fm = fm;
		this.tag = tag;
		this.opentag = opentag;
		this.pagesize = pagesize;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.AbstractPipeline#open(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline<?> open(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		try {
			String tagName = t.getName();
			if (tag.equals(tagName)) {
				MapContext cc;
				cc = (MapContext) context.get(PdfWriterPipeline.class.getName());
				Document d = new Document(pagesize);
				try {
					OutputStream os = fm.getStream();
					cc.put(PdfWriterPipeline.DOCUMENT, d);
					cc.put(PdfWriterPipeline.WRITER, PdfWriter.getInstance(d, os));
				} catch (IOException e) {
					throw new PipelineException(e);
				} catch (DocumentException e) {
					throw new PipelineException(e);
				}

			}
			if (t.getName().equalsIgnoreCase(opentag)) {
				MapContext cc;
				cc = (MapContext) context.get(PdfWriterPipeline.class.getName());
				Document d = (Document) cc.get(PdfWriterPipeline.DOCUMENT);
				CssUtils cssUtils = CssUtils.getInstance();
				float pageWidth = d.getPageSize().getWidth();
				float marginLeft = 0;
				float marginRight = 0;
				float marginTop = 0;
				float marginBottom = 0;
				Map<String, String> css = t.getCSS();
				for (Entry<String, String> entry : css.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					if (key.equalsIgnoreCase(CSS.Property.MARGIN_LEFT)) {
						marginLeft = cssUtils.parseValueToPt(value, pageWidth);
					} else if (key.equalsIgnoreCase(CSS.Property.MARGIN_RIGHT)) {
						marginRight = cssUtils.parseValueToPt(value, pageWidth);
					} else if (key.equalsIgnoreCase(CSS.Property.MARGIN_TOP)) {
						marginTop = cssUtils.parseValueToPt(value, pageWidth);
					} else if (key.equalsIgnoreCase(CSS.Property.MARGIN_BOTTOM)) {
						marginBottom = cssUtils.parseValueToPt(value, pageWidth);
					}
				}
				d.setMargins(marginLeft, marginRight, marginTop, marginBottom);
				d.open();

			}
		} catch (NoCustomContextException e) {
			throw new PipelineException(LocaleMessages.getInstance().getMessage(LocaleMessages.PIPELINE_AUTODOC), e);
		}

		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.AbstractPipeline#close(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline<?> close(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		String tagName = t.getName();
		if (tag.equals(tagName)) {
			MapContext cc;
			try {
				cc = (MapContext) context.get(PdfWriterPipeline.class.getName());
				Document d = (Document) cc.get(PdfWriterPipeline.DOCUMENT);
				d.close();
			} catch (NoCustomContextException e) {
				throw new PipelineException("AutoDocPipeline depends on PdfWriterPipeline.", e);
			}
			try {
				HtmlPipelineContext hpc = (HtmlPipelineContext) context.get(HtmlPipeline.class.getName());
				HtmlPipelineContext clone = hpc.clone();
				clone.setPageSize(pagesize);
				((WorkerContextImpl)context).put(HtmlPipeline.class.getName(), clone);
			} catch (NoCustomContextException e) {
			} catch (CloneNotSupportedException e) {
			}
		}
		return getNext();
	}


}

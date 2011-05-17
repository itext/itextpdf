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
package com.itextpdf.tool.xml.pipeline.pipe;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.CustomContext;
import com.itextpdf.tool.xml.pipeline.NoCustomContextException;
import com.itextpdf.tool.xml.pipeline.Pipeline;
import com.itextpdf.tool.xml.pipeline.PipelineException;
import com.itextpdf.tool.xml.pipeline.ProcessObject;
import com.itextpdf.tool.xml.pipeline.Writable;
import com.itextpdf.tool.xml.pipeline.WritableDirect;
import com.itextpdf.tool.xml.pipeline.WritableElement;
import com.itextpdf.tool.xml.pipeline.ctx.MapContext;

/**
 * @author redlab_b
 *
 */
public class PdfWriterPipeline extends AbstractPipeline {

	private static final Logger LOG = LoggerFactory.getLogger(PdfWriterPipeline.class);
	private Document doc;
	private PdfWriter writer;

	/**
	 * @param next
	 */
	public PdfWriterPipeline() {
		super(null);
	}

	/**
	 * @param next
	 */
	public PdfWriterPipeline(final Pipeline next) {
		super(next);
	}

	/**
	 * @param doc
	 * @param writer
	 */
	public PdfWriterPipeline(final Document doc, final PdfWriter writer) {
		super(null);
		this.doc = doc;
		this.writer = writer;
	}

	/**
	 * The key for the {@link Document} in the {@link MapContext} used as {@link CustomContext}.
	 */
	public static final String DOCUMENT = "DOCUMENT";
	/**
	 * The key for the {@link PdfWriter} in the {@link MapContext} used as {@link CustomContext}.
	 */
	public static final String WRITER = "WRITER";
	/**
	 * The key for the a boolean in the {@link MapContext} used as {@link CustomContext}. Setting to true enables swallowing of DocumentExceptions
	 */
	public static final String CONTINUOUS = "CONTINUOUS";

	/**
	 * @param po
	 * @throws PipelineException
	 */
	private void write(final ProcessObject po) throws PipelineException {
		CustomContext cc = getContext().get(PdfWriterPipeline.class);
		MapContext mp = (MapContext) cc;
		if (po.containsWritable()) {
			Document doc = (Document) mp.get(DOCUMENT);
			PdfWriter writer = (PdfWriter) mp.get(WRITER);
			boolean continuousWrite = (Boolean) mp.get(CONTINUOUS);
			Writable writable = null;
			while (null != (writable = po.poll())) {
				if (writable instanceof WritableElement) {
					for (Element e : ((WritableElement) writable).elements()) {
						try {
							if (!doc.add(e)) {
								LOG.trace(String.format("Failed to add %s element to the document, no exception was thrown.", e.toString()));
							}
						} catch (DocumentException e1) {
							if (!continuousWrite) {
								throw new PipelineException(e1);
							} else {
								LOG.error("Adding to document threw exception, I've swallowed it!", e1);
							}
						}
					}
				} else if (writable instanceof WritableDirect) {
					try {
						((WritableDirect) writable).write(writer, doc);
					} catch (DocumentException e) {
						if (!continuousWrite) {
							throw new PipelineException(e);
						} else {
							LOG.error("Adding to document threw exception, I've swallowed it!", e);
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#open(com.itextpdf.tool.
	 * xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline open(final Tag t, final ProcessObject po) throws PipelineException {
		write(po);
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#content(com.itextpdf.tool
	 * .xml.Tag, java.lang.String, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline content(final Tag t, final String content, final ProcessObject po) throws PipelineException {
		write(po);
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#close(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline close(final Tag t, final ProcessObject po) throws PipelineException {
		write(po);
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#getNewCustomContext()
	 */
	public CustomContext getCustomContext() throws NoCustomContextException {
		MapContext mc = new MapContext();
		mc.put(CONTINUOUS, Boolean.TRUE);
		mc.put(DOCUMENT, doc);
		mc.put(WRITER, writer);
		return mc;
	}

	public void setDocument(final Document document) {
		this.doc = document;
	}

	public void setWriter(final PdfWriter writer) {
		this.writer = writer;
	}
}

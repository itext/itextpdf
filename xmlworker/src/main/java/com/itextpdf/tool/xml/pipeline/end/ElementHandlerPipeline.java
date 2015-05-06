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
package com.itextpdf.tool.xml.pipeline.end;

import com.itextpdf.tool.xml.ElementHandler;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.PipelineException;
import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.Writable;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;

/**
 * As the {@link PdfWriterPipeline} but this one just passes everything on to an {@link ElementHandler}.
 * Allowing you to get all {@link Writable}s at the end of the pipeline. (or in between)
 * @author redlab_b
 *
 */
@SuppressWarnings("rawtypes")
public class ElementHandlerPipeline extends AbstractPipeline {

	private final ElementHandler handler;

	/**
	 * Does not use a context.
	 * @param handler the ElementHandler
	 * @param next the next pipeline in line. (or <code>null</code> if none )
	 */
	@SuppressWarnings("unchecked")
	public ElementHandlerPipeline(final ElementHandler handler, final Pipeline next) {
		super(next);
		this.handler =handler;
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#open(com.itextpdf.tool.xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline open(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		consume(po);
		return getNext();
	}

	/**
	 * Called in <code>open</code>, <code>content</code> and <code>close</code> to pass the {@link Writable}s to the handler
	 * @param po
	 */
	private void consume(final ProcessObject po) {
		if (po.containsWritable()) {
			Writable w = null;
			while ( null != (w =po.poll())) {
				handler.add(w);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#content(com.itextpdf.tool.xml.Tag, java.lang.String, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline<?> content(final WorkerContext ctx, final Tag currentTag, final String text, final ProcessObject po) throws PipelineException {
		consume(po);
		return getNext();
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.AbstractPipeline#close(com.itextpdf.tool.xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline close(final WorkerContext context, final Tag t, final ProcessObject po) throws PipelineException {
		consume(po);
		return getNext();
	}

}

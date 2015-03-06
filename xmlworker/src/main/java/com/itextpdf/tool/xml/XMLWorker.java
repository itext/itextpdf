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

import java.util.Map;

import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.parser.XMLParserListener;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;

/**
 * The implementation of the {@link XMLParserListener}.<br />
 * <strong>Important Note</strong>: This class the XMLWorker stores the
 * {@link WorkerContext} (Which is a {@link WorkerContextImpl}) in a ThreadLocal
 * variable, WorkerContext is confined to threads here.
 *
 * @author redlab_b
 *
 */
public class XMLWorker implements XMLParserListener {

	protected final Pipeline<?> rootpPipe;
	private static ThreadLocal<WorkerContextImpl> context = new ThreadLocal<WorkerContextImpl>() {
		@Override
		protected WorkerContextImpl initialValue() {
			return new WorkerContextImpl();
		};
	};
	protected final boolean parseHtml;

	/**
	 * Constructs a new XMLWorker
	 *
	 * @param pipeline the pipeline
	 * @param parseHtml true if this XMLWorker is parsing HTML, this actually
	 *            just means: convert all tags to lowercase.
	 */
	public XMLWorker(final Pipeline<?> pipeline, final boolean parseHtml) {
		this.parseHtml = parseHtml;
		rootpPipe = pipeline;
	}

	public void init()  {
		Pipeline<?> p = rootpPipe;
		try {
			while ((p = p.init(getLocalWC()))!= null);
		} catch (PipelineException e) {
			throw new RuntimeWorkerException(e);
		}
	}

	public void startElement(final String tag, final Map<String, String> attr, final String ns) {
		Tag t = createTag(tag, attr, ns);
		WorkerContext ctx = getLocalWC();
		if (null != ctx.getCurrentTag()) {
			ctx.getCurrentTag().addChild(t);
		}
		ctx.setCurrentTag(t);
		Pipeline<?> wp = rootpPipe;
		ProcessObject po = new ProcessObject();
		try {
			while (null != (wp = wp.open(ctx, t, po)))
				;
		} catch (PipelineException e) {
			throw new RuntimeWorkerException(e);
		}

	}

	/**
	 * Creates a new Tag object from the given parameters.
	 * @param tag the tag name
	 * @param attr the attributes
	 * @param ns the namespace if any
	 * @return a Tag
	 */
	protected Tag createTag(String tag, final Map<String, String> attr, final String ns) {
		if (parseHtml) {
			tag = tag.toLowerCase();
		}
		Tag t = new Tag(tag, attr, ns);
		return t;
	}

	public void endElement(final String tag, final String ns) {
		String thetag = null;
		if (parseHtml) {
			thetag = tag.toLowerCase();
		} else {
			thetag = tag;
		}
		WorkerContext ctx = getLocalWC();
		if (null != ctx.getCurrentTag() && !thetag.equals(ctx.getCurrentTag().getName())) {
			throw new RuntimeWorkerException(String.format(
					LocaleMessages.getInstance().getMessage(LocaleMessages.INVALID_NESTED_TAG), thetag,
					ctx.getCurrentTag().getName()));
		}
		Pipeline<?> wp = rootpPipe;
		ProcessObject po = new ProcessObject();
		try {
			while (null != (wp = wp.close(ctx, ctx.getCurrentTag(), po)))
				;
		} catch (PipelineException e) {
			throw new RuntimeWorkerException(e);
		} finally {
			if (null != ctx.getCurrentTag())
				ctx.setCurrentTag(ctx.getCurrentTag().getParent());
		}
	}

	/**
	 * This method passes encountered text to the pipeline via the
	 * {@link Pipeline#content(WorkerContext, Tag, String, ProcessObject)}
	 * method.
	 */
	public void text(final String text) {
		WorkerContext ctx = getLocalWC();
		if (null != ctx.getCurrentTag()) {
			if (text.length() > 0) {
				Pipeline<?> wp = rootpPipe;
				ProcessObject po = new ProcessObject();
				try {
					while (null != (wp = wp.content(ctx, ctx.getCurrentTag(), text, po)))
						;
				} catch (PipelineException e) {
					throw new RuntimeWorkerException(e);
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.parser.ParserListener#unknownText(java.lang.String)
	 */
	public void unknownText(final String text) {
		// TODO unknown text encountered
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.parser.ParserListener#comment(java.lang.String)
	 */
	public void comment(final String comment) {
		// TODO xml comment encountered
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.parser.XMLParserListener#close()
	 */
	public void close() {
		context.remove();
	}

	/**
	 * Returns the current tag.
	 * @return the current tag
	 */
	protected Tag getCurrentTag() {
		return getLocalWC().getCurrentTag();
	}

	/**
	 * Returns the local WorkerContext, beware: could be a newly initialized
	 * one, if {@link XMLWorker#close()} has been called before.
	 *
	 * @return the local WorkerContext
	 */
	protected WorkerContext getLocalWC() {
		return context.get();
	}
}

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
package com.itextpdf.tool.xml.pipeline.html;

import java.util.List;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.CustomContext;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.PipelineException;
import com.itextpdf.tool.xml.ProcessObject;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.pipeline.AbstractPipeline;
import com.itextpdf.tool.xml.pipeline.WritableElement;

/**
 * @author redlab_b
 *
 */
public class HtmlPipeline extends AbstractPipeline {

	private final HtmlPipelineContext hpc;

	/**
	 * @param hpc the initial {@link HtmlPipelineContext}
	 * @param next the next pipe in row
	 */
	public HtmlPipeline(final HtmlPipelineContext hpc, final Pipeline next) {
		super(next);
		this.hpc = hpc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#open(com.itextpdf.tool.
	 * xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline open(final Tag t, final ProcessObject po) throws PipelineException {
		HtmlPipelineContext hcc = getMyContext();
		try {
			TagProcessor tp = hcc.resolveProcessor(t.getTag(), t.getNameSpace());
			if (tp.isStackOwner()) {
				hcc.addFirst(new StackKeeper(t));
			}
			List<Element> content = tp.startElement(t);
			if (content.size() > 0) {
				if (tp.isStackOwner()) {
					StackKeeper peek;
					try {
						peek = hcc.peek();
						for (Element elem : content) {
							peek.add(elem);
						}
					} catch (NoStackException e) {
						throw new PipelineException(String.format("Could not find stack for %s", t.toString()), e);
					}
				} else {
					for (Element elem : content) {
						hcc.currentContent().add(elem);
					}
				}
			}
		} catch (NoTagProcessorException e) {
			if (!hcc.acceptUnknown()) {
				throw e;
			}
		}
		return getNext();
	}

	/**
	 * @return
	 * @throws PipelineException
	 */
	private HtmlPipelineContext getMyContext() throws PipelineException {
		CustomContext cc;
		try {
			cc = getContext().get(HtmlPipeline.class);
			HtmlPipelineContext hcc = (HtmlPipelineContext) cc;
			return hcc;
		} catch (NoCustomContextException e) {
			throw new PipelineException("HtmlPipeline cries, it cannot find it's own context.", e);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#content(com.itextpdf.tool
	 * .xml.Tag, java.lang.String, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline content(final Tag t, final String content, final ProcessObject po) throws PipelineException {
		HtmlPipelineContext hcc = getMyContext();
		TagProcessor tp;
		try {
			tp = hcc.resolveProcessor(t.getTag(), t.getNameSpace());
			List<Element> elems = tp.content(t, content);
			if (elems.size() > 0) {
				StackKeeper peek;
				try {
					peek = hcc.peek();
					for (Element e : elems) {
						peek.add(e);
					}
				} catch (NoStackException e) {
					WritableElement writableElement = new WritableElement();
					for (Element elem : elems) {
						writableElement.add(elem);
					}
					po.add(writableElement);
				}
			}
		} catch (NoTagProcessorException e) {
			if (!hcc.acceptUnknown()) {
				throw e;
			}
		}
		return getNext();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.itextpdf.tool.xml.pipeline.Pipeline#close(com.itextpdf.tool
	 * .xml.Tag, com.itextpdf.tool.xml.pipeline.ProcessObject)
	 */
	@Override
	public Pipeline close(final Tag t, final ProcessObject po) throws PipelineException {
		HtmlPipelineContext hcc = getMyContext();
		TagProcessor tp;
		try {
			tp = hcc.resolveProcessor(t.getTag(), t.getNameSpace());
			List<Element> elems = null;
			if (tp.isStackOwner()) {
				// remove the element from the StackKeeper Queue if end tag is
				// found
				StackKeeper tagStack;
				try {
					tagStack = hcc.poll();
				} catch (NoStackException e) {
					throw new PipelineException(String.format("Could not find stack for %s", t.toString()), e);
				}
				elems = tp.endElement(t,  tagStack.getElements());
			} else {
				elems = tp.endElement(t, hcc.currentContent());
				hcc.currentContent().clear();
			}
			if (elems.size() > 0) {
				try {
					StackKeeper stack = hcc.peek();
					for (Element elem : elems) {
						stack.add(elem);
					}
				} catch (NoStackException e) {
					WritableElement writableElement = new WritableElement();
						po.add(writableElement);
						writableElement.addAll(elems);
				}

			}
		} catch (NoTagProcessorException e) {
			if (!hcc.acceptUnknown()) {
				throw e;
			}
		}
		return getNext();
	}

	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.pipeline.Pipeline#getNewCustomContext()
	 */
	@Override
	public CustomContext getCustomContext() throws NoCustomContextException {
		try {
			HtmlPipelineContext clone = hpc.clone();
			clone.setContext(context);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeWorkerException(e);
		}
	}

}

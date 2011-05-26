/*
 * $Id$
 *
 * This file is part of the iText (R) project. Copyright (c) 1998-2011 1T3XT
 * BVBA Authors: Balder Van Camp, Emiel Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License version 3 as published by
 * the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED
 * WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT, 1T3XT DISCLAIMS THE WARRANTY
 * OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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

import com.itextpdf.text.Element;
import com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.parser.XMLParserListener;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.ctx.WorkerContextImpl;

/**
 * The implementation of the XMLWorker. For legacy purposes this class also
 * implements {@link SimpleXMLDocHandler}
 *
 * @author redlab_b
 *
 */
public class XMLWorker implements XMLParserListener {

	private Tag current = null;
	private Pipeline rootpPipe;
	private WorkerContextImpl context;
	private boolean parseHtml;

	/**
	 * @param factory
	 * @param acceptUnknown
	 * @param provider
	 */
	private XMLWorker() {
	}

	/**
	 * Constructs a new XMLWorker
	 *
	 * @param pipeline the pipeline
	 * @param parseHtml true if this XMLWorker is parsing HTML, this actually just means:
	 *            convert all tags to lowercase.
	 */
	public XMLWorker(final Pipeline pipeline, final boolean parseHtml) {
		this();
		this.parseHtml = parseHtml;
		rootpPipe = pipeline;
		this.context = new WorkerContextImpl();
		Pipeline p = rootpPipe;
		while (null != (p = setCustomContext(p)))
			;

	}

	/**
	 * @return the {@link Pipeline#getNext()} value
	 *
	 */
	private Pipeline setCustomContext(final Pipeline pipeline) {
		try {
			pipeline.setContext(context);
			CustomContext cc = pipeline.getCustomContext();
			context.add(pipeline.getClass(), cc);
		} catch (NoCustomContextException e) {
		}
		return pipeline.getNext();
	}

	/**
	 * Called when a starting tag has been encountered by the
	 * {@link SimpleXMLParser}. This method creates a {@link Tag} for the
	 * encountered tag. The parent for the encountered tag is set if any. The
	 * css is resolved with the given {@link CSSResolver} if any. A
	 * {@link TagProcessor} for the encountered {@link Tag} is loaded from the
	 * given {@link TagProcessorFactory}. If none found and acceptUknown is
	 * false a {@link NoTagProcessorException} is thrown. If found the
	 * TagProcessors startElement is called.
	 */
	public void startElement(String tag, final Map<String, String> attr, final String ns) {
		if (parseHtml) {
			tag = tag.toLowerCase();
		}
		Tag t = new Tag(tag, attr, ns);
		if (null != current) {
			current.addChild(t);
		}
		current = t;
		Pipeline wp = rootpPipe;
		ProcessObject po = new ProcessObject();
		try {
			while (null != (wp = wp.open(t, po)));
		} catch (PipelineException e) {
			throw new RuntimeWorkerException(e);
		}

	}

	/**
	 * Called when an ending tag is encountered by the {@link SimpleXMLParser}.
	 * This method searches for the tags {@link TagProcessor} in the given
	 * {@link TagProcessorFactory}. If none found and acceptUknown is false a
	 * {@link NoTagProcessorException} is thrown. If found the TagProcessors
	 * endElement is called.<br />
	 * The returned Element by the TagProcessor is added to the currentContent
	 * stack.<br />
	 * If any of the parent tags or the given tags
	 * {@link TagProcessor#isStackOwner()} is true. The returned Element is put
	 * on the respective stack.Else it element is added to the document or the
	 * elementList.
	 *
	 */
	public void endElement(final String tag, final String ns) {
		String thetag = null;
		if (parseHtml) {
			thetag = tag.toLowerCase();
		} else {
			thetag = tag;
		}
		if (null != current && !thetag.equals(current.getTag())) {
			throw new RuntimeWorkerException(String.format("Invalid nested tag %s found, expected closing tag %s", thetag, current.getTag()));
		}
		Pipeline wp = rootpPipe;
		ProcessObject po = new ProcessObject();
		try {
			while (null != (wp = wp.close(current, po)));
		} catch (PipelineException e) {
			throw new RuntimeWorkerException(e);
		} finally {
			if (null != current)
			current = current.getParent();
		}
	}

	/**
	 * This method is called when the {@link SimpleXMLParser} encountered text.
	 * This method searches for the current tag {@link TagProcessor} in the
	 * given {@link TagProcessorFactory}. If none found and acceptUknown is
	 * false a {@link NoTagProcessorException} is thrown. If found the
	 * {@link TagProcessor#content(Tag, String)} is called.<br />
	 * The returned {@link Element} if any is added to the currentContent stack.
	 */
	public void text(final byte[] b) {
		if (null != current) {
			if (b.length > 0) {
				Pipeline wp = rootpPipe;
				ProcessObject po = new ProcessObject();
				try {
					while (null != (wp = wp.content(current, b, po)))
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

}

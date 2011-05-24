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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.CustomContext;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.XMLWorkerConfig;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;

/**
 * @author redlab_b
 *
 */
public class HtmlPipelineContext implements CustomContext {

	public static final String BOOKMARK_TREE = "header.autobookmark.RootNode";
	public static final String LAST_MARGIN_BOTTOM = "lastMarginBottom";
	private final LinkedList<StackKeeper> queue;
	private final boolean acceptUnknown = true;
	private final TagProcessorFactory tagFactory = Tags.getHtmlTagProcessorFactory();
	private final List<Element> ctn = new ArrayList<Element>();
	private final XMLWorkerConfig config;
	private final WorkerContext context;
	private ImageProvider imageProvider;

	/**
	 * @param workerContext
	 *
	 */
	public HtmlPipelineContext(final XMLWorkerConfig config, final WorkerContext workerContext) {
		this.queue = new LinkedList<StackKeeper>();
		this.config = config;
		this.context = workerContext;
	}
	/**
	 * @param tag the tag to find a TagProcessor for
	 * @param nameSpace the namespace.
	 * @return a TagProcessor
	 */
	public TagProcessor resolveProcessor(final String tag, final String nameSpace) {
		TagProcessor tp = tagFactory.getProcessor(tag, nameSpace);
		tp.setContext(context);
		tp.setConfiguration(config);
		return tp;
	}

	/**
	 * @param stackKeeper
	 */
	public void addFirst(final StackKeeper stackKeeper) {
		this.queue.addFirst(stackKeeper);

	}

	/**
	 * Retrieves, but does not remove, the head (first element) of this list.
	 * @return a StackKeeper
	 * @throws NoStackException if there are no elements on the stack
	 */
	public StackKeeper peek() throws NoStackException {

		try {
			return this.queue.getFirst();
		} catch (NoSuchElementException e) {
			throw new NoStackException();
		}
	}

	/**
	 * @return the current content of writables.
	 */
	public List<Element> currentContent() {
		return ctn;
	}

	/**
	 * @return if this pipelines tag processing accept unknown tags: true. False otherwise
	 */
	public boolean acceptUnknown() {
		return this.acceptUnknown;
	}

	/**
	 * @return returns true if the stack is empty
	 */
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * Retrieves and removes the top of the stack.
	 * @return a StackKeeper
	 * @throws NoStackException if there are no elements on the stack
	 */
	public StackKeeper poll() throws NoStackException {
		try {
			return this.queue.removeFirst();
		} catch (NoSuchElementException e) {
			throw new NoStackException();
		}
	}
	/**
	 * @return
	 */
	public boolean autoBookmark() {
		return config.autoBookmark();
	}
	/**
	 * @return
	 */
	public Map<String, Object> getMemory() {
		return config.getMemory();
	}
	/**
	 *
	 */
	public ImageProvider getImageProvider() throws NoImageProviderException{
		if (null == this.imageProvider) {
			throw new NoImageProviderException();
		}
		return this.imageProvider;

	}

}

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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.xml.simpleparser.SimpleXMLParser;
import com.itextpdf.tool.xml.exceptions.NoTagProcessorException;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.HTMLUtils;

/**
 * The implementation of the XMLWorker.
 * 
 * @author redlab_b
 * 
 */
public class XMLWorkerImpl implements XMLWorker {

	private Tag current;
	private final List<Element> currentContent;
	private ElementHandler listener;
	private final LinkedList<StackKeeper> queue;
	private XMLWorkerConfig config;

	/**
	 * @param factory
	 * @param acceptUnknown
	 * @param provider
	 */
	private XMLWorkerImpl() {
		this.queue = new LinkedList<StackKeeper>();
		this.currentContent = new ArrayList<Element>();

	}

	/**
	 * 
	 * @param config
	 */
	public XMLWorkerImpl(final XMLWorkerConfig config) {
		this();
		this.config = config;
		if (config.isParsingHTML()) {
			new HTMLUtils();
		}
	}

	/**
	 * Called when a starting tag has been encountered by the {@link SimpleXMLParser}. This method creates a {@link Tag}
	 * for the encountered tag. The parent for the encountered tag is set if any. The css is resolved with the given
	 * {@link CSSResolver} if any. A {@link TagProcessor} for the encountered {@link Tag} is loaded from the given
	 * {@link TagProcessorFactory}. If none found and acceptUknown is false a {@link NoTagProcessorException} is thrown.
	 * If found the TagProcessors startElement is called.
	 */
	public void startElement(String tag, final Map<String, String> h) {
		if (config.isParsingHTML()) {
			tag = tag.toLowerCase();
		}
		Tag t = new Tag(tag, h);
		if (null != current) {
			current.addChild(t);
			t.setParent(current);
		} 
//		else {
//			t.setParent(config.getDefaultRoot());
//		}
		current = t;
		if (null != config.getCssResolver()) {
			config.getCssResolver().resolveStyles(t);
		}
		try {
			TagProcessor tp = resolveProcessor(tag);
			if (tp.isStackOwner()) {
				queue.addFirst(new StackKeeper(t));
			}
			List<Element> content = tp.startElement(t);
			if (content.size() > 0) {
				if (tp.isStackOwner()) {
					StackKeeper peek = queue.peek();
					for (Element elem : content) {
						peek.add(elem);
					}
				} else {
					for (Element elem : content) {
						currentContent.add(elem);
					}
				}
			}
		} catch (NoTagProcessorException e) {
			if (!config.acceptUnknown()) {
				throw e;
			}
		}
	}

	/**
	 * @param tag
	 * @return
	 */
	private TagProcessor resolveProcessor(final String tag) {
		TagProcessor tp = this.config.getTagFactory().getProcessor(tag);
		tp.setConfiguration(this.config);
		return tp;
	}

	/**
	 * Called when an ending tag is encountered by the {@link SimpleXMLParser}. This method searches for the tags
	 * {@link TagProcessor} in the given {@link TagProcessorFactory}. If none found and acceptUknown is false a
	 * {@link NoTagProcessorException} is thrown. If found the TagProcessors endElement is called.<br />
	 * The returned Element by the TagProcessor is added to the currentContent stack.<br />
	 * If any of the parent tags or the given tags {@link TagProcessor#isStackOwner()} is true. The returned Element is
	 * put on the respective stack.Else it element is added to the document or the elementList.
	 * 
	 */
	public void endElement(String tag) {
		if (config.isParsingHTML()) {
			tag = tag.toLowerCase();
		}
		TagProcessor tp;
		try {
			tp = resolveProcessor(tag);
			if (queue.isEmpty()) {
				List<Element> elems = tp.endElement(current, currentContent);
				if (elems.size() > 0) {
					for (Element e : elems) {
						currentContent.add(e);
					}
				}
				try {
					listener.addAll(currentContent);
				} catch (DocumentException e) {
					throw new RuntimeWorkerException(e);
				}
				currentContent.clear();
			} else if (tp.isStackOwner()) {
				// remove the element from the StackKeeper Queue if end tag is
				// found
				List<Element> elements = queue.poll().getElements();
				List<Element> elems = tp.endElement(current, elements);
				if (queue.isEmpty() && elems.size() > 0) {
					try {
						listener.addAll(elems);
					} catch (DocumentException exc) {
						throw new RuntimeWorkerException(exc);
					}
				} else if (null != elems && elems.size() > 0) {
					StackKeeper peek = queue.peek();
					for (Element elem : elems) {
						peek.add(elem);
					}
				}
				currentContent.clear();
			} else {
				List<Element> elems = tp.endElement(current, currentContent);
				if (elems.size() > 0) {
					StackKeeper peek = queue.peek();
					for (Element elem : elems) {
						peek.add(elem);
					}
				}
			}
		} catch (NoTagProcessorException e) {
			if (!this.config.acceptUnknown()) {
				throw e;
			}
		} finally {
			if (null != current) {
				current = current.getParent();
			}
		}
	}

	/**
	 * This method is called when the {@link SimpleXMLParser} encountered text. This method searches for the current tag
	 * {@link TagProcessor} in the given {@link TagProcessorFactory}. If none found and acceptUknown is false a
	 * {@link NoTagProcessorException} is thrown. If found the {@link TagProcessor#content(Tag, String)} is called.<br />
	 * The returned {@link Element} if any is added to the currentContent stack.
	 */
	public void text(final String str) {
		if (null != current) {
			if (str.length() > 0) {
				try {
					String encoded;
					try {
						// TODO Java 1.6 - replace charste.name() with charset
						// FIXME issues with html entities and utf!
						encoded = new String(str.getBytes(), this.config.charSet().name());
						// encoded = new String(str.getBytes());
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeWorkerException(e);
					}
					TagProcessor tp = resolveProcessor(current.getTag());
					List<Element> content = tp.content(current, encoded);
					if (queue.isEmpty()) {
						try {
							listener.addAll(content);
						} catch (DocumentException e) {
							throw new RuntimeWorkerException(e);
						}
						// currentContent.add(content);
					} else {
						StackKeeper peek = queue.peek();
						for (Element e : content) {
							peek.add(e);
						}
					}
				} catch (NoTagProcessorException e) {
					if (!this.config.acceptUnknown()) {
						throw e;
					}
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler#startDocument()
	 */
	public void startDocument() {
		this.currentContent.clear();
		this.config.getMemory().clear();
		// listener.startDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itextpdf.text.xml.simpleparser.SimpleXMLDocHandler#endDocument()
	 */
	public void endDocument() {
		// listener.endDocument();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itextpdf.tool.xml.XMLWorker#setDocumentListener(com.itextpdf.tool .xml.DocumentListener)
	 */
	public void setDocumentListener(final ElementHandler elementHandler) {
		this.listener = elementHandler;

	}

	/**
	 * @param elementHandler
	 * @return an XMLWorkerImpl object
	 */
	public XMLWorkerImpl elementHandler(final ElementHandler elementHandler) {
		this.listener = elementHandler;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itextpdf.tool.xml.parser.ParserListener#unknownText(java.lang.String)
	 */
	public void unknownText(final String text) {
		// TODO unknown text encountered
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itextpdf.tool.xml.parser.ParserListener#comment(java.lang.String)
	 */
	public void comment(final String comment) {
		// xml comment encountered
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.itextpdf.tool.xml.XMLWorker#setConfiguration(com.itextpdf.tool.xml.XMLWorkerConfiguration)
	 */
	public void setConfiguration(final XMLWorkerConfig config) {
		this.config = config;
	}

}

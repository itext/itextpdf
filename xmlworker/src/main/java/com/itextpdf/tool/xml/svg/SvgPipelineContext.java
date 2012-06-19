/*
 * $Id$
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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.tool.xml.CustomContext;
import com.itextpdf.tool.xml.Experimental;
import com.itextpdf.tool.xml.css.apply.ListStyleTypeCssApplier;
import com.itextpdf.tool.xml.html.Header;
import com.itextpdf.tool.xml.html.Image;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import com.itextpdf.tool.xml.pipeline.html.LinkProvider;
import com.itextpdf.tool.xml.pipeline.html.NoImageProviderException;
import com.itextpdf.tool.xml.pipeline.html.NoStackException;
import com.itextpdf.tool.xml.pipeline.html.StackKeeper;

public class SvgPipelineContext implements CustomContext, Cloneable{
	/**
	 *  Key for the memory, used to store bookmark nodes
	 */
	public static final String BOOKMARK_TREE = "header.autobookmark.RootNode";
	/**
	 * Key for the memory, used in Html TagProcessing
	 */
	public static final String LAST_MARGIN_BOTTOM = "lastMarginBottom";
	private final LinkedList<StackKeeper> queue;
	private boolean acceptUnknown = true;
	private TagProcessorFactory tagFactory;
	private final List<Element> ctn = new ArrayList<Element>();
	private ImageProvider imageProvider;
	private Rectangle pageSize = PageSize.A4;
	private Charset charset;
	private List<String> roottags = Arrays.asList(new String[] { "body", "div" });
	private LinkProvider linkprovider;
	private boolean autoBookmark = true;
	private boolean definition = false;
	
	public boolean isDefinition() {
		return definition;
	}
	public void setDefinition(boolean definition) {
		this.definition = definition;
	}

	private final Map<String, List<Element>> symbols;

	/**
	 * Construct a new CvgPipelineContext object
	 */
	public SvgPipelineContext() {
		this.queue = new LinkedList<StackKeeper>();
		this.symbols = new HashMap<String, List<Element>>();
	}
	/**
	 * @param tag the tag to find a TagProcessor for
	 * @param nameSpace the namespace.
	 * @return a TagProcessor
	 */	
	protected TagProcessor resolveProcessor(final String tag, final String nameSpace) {
		TagProcessor tp = (TagProcessor) tagFactory.getProcessor(tag, nameSpace);
		return tp;
	}

	/**
	 * Add a {@link StackKeeper} to the top of the stack list.
	 * @param stackKeeper the {@link StackKeeper}
	 */
	protected void addFirst(final StackKeeper stackKeeper) {
		this.queue.addFirst(stackKeeper);
	}

	/**
	 * Retrieves, but does not remove, the head (first element) of this list.
	 * @return a StackKeeper
	 * @throws NoStackException if there are no elements on the stack
	 */
	protected StackKeeper peek() throws NoStackException {

		try {
			return this.queue.getFirst();
		} catch (NoSuchElementException e) {
			throw new NoStackException();
		}
	}

	/**
	 * @return the current content of elements.
	 */
	protected List<Element> currentContent() {
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
	protected boolean isEmpty() {
		return queue.isEmpty();
	}

	/**
	 * Retrieves and removes the top of the stack.
	 * @return a StackKeeper
	 * @throws NoStackException if there are no elements on the stack
	 */
	protected StackKeeper poll() throws NoStackException {
		try {
			return this.queue.removeFirst();
		} catch (NoSuchElementException e) {
			throw new NoStackException();
		}
	}
	/**
	 * @return true if auto-bookmarks should be enabled. False otherwise.
	 */
	public boolean autoBookmark() {
		return autoBookmark;
	}

	public List<Element> getSymbolById(String id) {
		return symbols.get(id);
	}
	
	public void addSymbolById(String id, List<Element> elements) {
		symbols.put(id, elements);
	}
	
	/**
	 * @return the image provider.
	 * @throws NoImageProviderException if there is no {@link ImageProvider}
	 *
	 */
	public ImageProvider getImageProvider() throws NoImageProviderException{
		if (null == this.imageProvider) {
			throw new NoImageProviderException();
		}
		return this.imageProvider;

	}

	/**
	 * Set a {@link Charset} to use.
	 * @param cSet the charset.
	 * @return this <code>HtmlPipelineContext</code>
	 */
	@Experimental
	public SvgPipelineContext charSet(final Charset cSet) {
		this.charset = cSet;
		return this;
	}
	/**
	 * @return the {@link Charset} to use, or null if none configured.
	 */
	public Charset charSet() {
		return charset;
	}
	/**
	 * Returns a {@link Rectangle}
	 * @return the pagesize.
	 */
	public Rectangle getPageSize() {
		return this.pageSize;
	}

	/**
	 * @return a list of tags to be taken as root-tags. This matters for
	 *         margins. By default the root-tags are &lt;body&gt; and
	 *         &lt;div&gt;
	 */
	public List<String> getRootTags() {
		return roottags;
	}

	/**
	 * Returns the LinkProvider, used to prepend e.g. http://www.example.org/ to
	 * found &lt;a&gt; tags that have no absolute url.
	 *
	 * @return the LinkProvider if any.
	 */
	public LinkProvider getLinkProvider() {
		return linkprovider;
	}
	/**
	 * If no pageSize is set, the default value A4 is used.
	 * @param pageSize the pageSize to set
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public SvgPipelineContext setPageSize(final Rectangle pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	/**
	 * Create a clone of this HtmlPipelineContext, the clone only contains the
	 * initial values, not the internal values. Beware, the state of the current
	 * Context is not copied to the clone. Only the configurational important
	 * stuff like the LinkProvider (same object), ImageProvider (new
	 * {@link AbstractImageProvider} with same ImageRootPath) ,
	 * TagProcessorFactory (same object), acceptUnknown (primitive), charset
	 * (Charset.forName to get a new charset), autobookmark (primitive) are
	 * copied.
	 */
	@Override
	public SvgPipelineContext clone() throws CloneNotSupportedException {
		SvgPipelineContext newCtx = new SvgPipelineContext();
		if (this.imageProvider != null) {
			final String rootPath =  imageProvider.getImageRootPath();
			newCtx.setImageProvider(new AbstractImageProvider() {

				public String getImageRootPath() {
					return rootPath;
				}
			});
		}
		if (null != this.charset) {
			newCtx.charSet(Charset.forName(this.charset.name()));
		}
		newCtx.setPageSize(new Rectangle(this.pageSize)).setLinkProvider(this.linkprovider)
				.setRootTags(new ArrayList<String>(this.roottags)).autoBookmark(this.autoBookmark)
				.setTagFactory(this.tagFactory).setAcceptUnknown(this.acceptUnknown);
		return newCtx;
	}


	/**
	 * Set to true to allow the HtmlPipeline to accept tags it does not find in
	 * the given {@link TagProcessorFactory}
	 *
	 * @param acceptUnknown true or false
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public SvgPipelineContext setAcceptUnknown(final boolean acceptUnknown) {
		this.acceptUnknown = acceptUnknown;
		return this;
	}
	/**
	 * Set the {@link TagProcessorFactory} to be used. For HTML use {@link Tags#getHtmlTagProcessorFactory()}
	 * @param tagFactory the {@link TagProcessorFactory} that should be used
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public SvgPipelineContext setTagFactory(final TagProcessorFactory tagFactory) {
		this.tagFactory = tagFactory;
		return this;
	}

	/**
	 * Set to true to enable the automatic creation of bookmarks on &lt;h1&gt;
	 * to &lt;h6&gt; tags. Works in conjunction with {@link Header}.
	 *
	 * @param autoBookmark true or false
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public SvgPipelineContext autoBookmark(final boolean autoBookmark) {
		this.autoBookmark = autoBookmark;
		return this;
	}

	/**
	 * Set the root-tags, this matters for margins. By default these are set to
	 * &lt;body&gt; and &lt;div&gt;.
	 *
	 * @param roottags the root tags
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public SvgPipelineContext setRootTags(final List<String> roottags) {
		this.roottags = roottags;
		return this;
	}

	/**
	 * An ImageProvider can be provided and works in conjunction with
	 * {@link Image} and {@link ListStyleTypeCssApplier} for List Images.
	 *
	 * @param imageProvider the {@link ImageProvider} to use.
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public SvgPipelineContext setImageProvider(final ImageProvider imageProvider) {
		this.imageProvider = imageProvider;
		return this;
	}

	/**
	 * Set the LinkProvider to use if any.
	 *
	 * @param linkprovider the LinkProvider (@see
	 *            {@link HtmlPipelineContext#getLinkProvider()}
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public SvgPipelineContext setLinkProvider(final LinkProvider linkprovider) {
		this.linkprovider = linkprovider;
		return this;
	}
}

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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.itextpdf.text.*;
import com.itextpdf.tool.xml.CustomContext;
import com.itextpdf.tool.xml.Experimental;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.css.apply.ListStyleTypeCssApplier;
import com.itextpdf.tool.xml.css.apply.MarginMemory;
import com.itextpdf.tool.xml.css.apply.PageSizeContainable;
import com.itextpdf.tool.xml.exceptions.NoDataException;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersAware;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Header;
import com.itextpdf.tool.xml.html.Image;
import com.itextpdf.tool.xml.html.TagProcessor;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;

/**
 * The CustomContext object for the HtmlPipeline.<br />
 * Use this to configure your {@link HtmlPipeline}.
 * @author redlab_b
 *
 */
public class HtmlPipelineContext implements CustomContext, Cloneable, MarginMemory, PageSizeContainable,
		CssAppliersAware {

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
	private final Map<String, Object> memory;
	private CssAppliers cssAppliers;

	/**
	 * Construct a new HtmlPipelineContext object
	 */
	public HtmlPipelineContext(CssAppliers cssAppliers) {
		this.queue = new LinkedList<StackKeeper>();
		this.memory = new HashMap<String, Object>();
        this.cssAppliers = cssAppliers;
        if (this.cssAppliers == null) {
		    this.cssAppliers = new CssAppliersImpl(new XMLWorkerFontProvider());
        }
	}
	/**
	 * @param tag the tag to find a TagProcessor for
	 * @param nameSpace the namespace.
	 * @return a TagProcessor
	 */
	protected TagProcessor resolveProcessor(final String tag, final String nameSpace) {
		TagProcessor tp = tagFactory.getProcessor(tag, nameSpace);
		if (tp instanceof CssAppliersAware) {
			((CssAppliersAware) tp).setCssAppliers(this.cssAppliers);
		}
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
	 * @return a StackKeeper or null if there are no elements on the stack
	 */
	protected StackKeeper peek() {
		if (!this.queue.isEmpty())
			return this.queue.getFirst();
		return null;
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
	/**
	 * @return the memory
	 */
	public Map<String, Object> getMemory() {
		return memory;
	}
	/**
	 * @return the image provider or null if there is no {@link ImageProvider}.
	 *
	 */
	public ImageProvider getImageProvider() {
		return this.imageProvider;
	}

	/**
	 * Set a {@link Charset} to use.
	 * @param cSet the charset.
	 * @return this <code>HtmlPipelineContext</code>
	 */
	@Experimental
	public HtmlPipelineContext charSet(final Charset cSet) {
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
	public HtmlPipelineContext setPageSize(final Rectangle pageSize) {
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
	public HtmlPipelineContext clone() throws CloneNotSupportedException {
        CssAppliers cloneCssApliers = this.cssAppliers.clone();
		HtmlPipelineContext newCtx = new HtmlPipelineContext(cloneCssApliers);
		if (this.imageProvider != null) {
                    newCtx.setImageProvider(imageProvider);
		}
		if (null != this.charset) {
			newCtx.charSet(Charset.forName(this.charset.name()));
		}
		newCtx.setPageSize(new Rectangle(this.pageSize)).setLinkProvider(this.linkprovider)
				.setRootTags(new ArrayList<String>(this.roottags)).autoBookmark(this.autoBookmark)
				.setTagFactory(this.tagFactory).setAcceptUnknown(this.acceptUnknown).setCssApplier(cloneCssApliers);
		return newCtx;
	}


	/**
	 * Set to true to allow the HtmlPipeline to accept tags it does not find in
	 * the given {@link TagProcessorFactory}
	 *
	 * @param acceptUnknown true or false
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public HtmlPipelineContext setAcceptUnknown(final boolean acceptUnknown) {
		this.acceptUnknown = acceptUnknown;
		return this;
	}
	/**
	 * Set the {@link TagProcessorFactory} to be used. For HTML use {@link Tags#getHtmlTagProcessorFactory()}
	 * @param tagFactory the {@link TagProcessorFactory} that should be used
	 * @return this <code>HtmlPipelineContext</code>
	 */
	public HtmlPipelineContext setTagFactory(final TagProcessorFactory tagFactory) {
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
	public HtmlPipelineContext autoBookmark(final boolean autoBookmark) {
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
	public HtmlPipelineContext setRootTags(final List<String> roottags) {
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
	public HtmlPipelineContext setImageProvider(final ImageProvider imageProvider) {
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
	public HtmlPipelineContext setLinkProvider(final LinkProvider linkprovider) {
		this.linkprovider = linkprovider;
		return this;
	}
	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.css.MarginMemory#getLastMarginBottom()
	 */
	public Float getLastMarginBottom() throws NoDataException {
		Map<String, Object> memory = getMemory();
		Object o =  memory.get(HtmlPipelineContext.LAST_MARGIN_BOTTOM);
		if (null == o) {
			throw new NoDataException();
		} else {
			return (Float) o;
		}
	}
	/* (non-Javadoc)
	 * @see com.itextpdf.tool.xml.css.MarginMemory#setLastMarginBottom(float)
	 */
	public void setLastMarginBottom(final Float lmb) {
		getMemory().put(HtmlPipelineContext.LAST_MARGIN_BOTTOM, lmb);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.html.CssAppliersAware#setCssAppliers(com.itextpdf.tool.xml.html.CssAppliers)
	 */
	public void setCssAppliers(final CssAppliers cssAppliers) {
		this.cssAppliers = cssAppliers;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.html.CssAppliersAware#getCssAppliers()
	 */
	public CssAppliers getCssAppliers() {
		return cssAppliers;
	}

	/**
	 * Fluent variant of {@link #setCssAppliers(CssAppliers)}
	 *
	 * @param cssAppliers the cssAppliers
	 * @return this
	 */
	public HtmlPipelineContext setCssApplier(final CssAppliers cssAppliers) {
		this.cssAppliers = cssAppliers;
		return this;
	}
}

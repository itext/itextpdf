/*
 * $Id$
 *
 * This file is part of the iText (R) project. Copyright (c) 1998-2011 1T3XT BVBA Authors: Balder Van Camp, Emiel
 * Ackermann, et al.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License version 3 as published by the Free Software Foundation with the addition of the following permission
 * added to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * 1T3XT, 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public License along with this program; if not,
 * see http://www.gnu.org/licenses or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL: http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions of this program must display Appropriate
 * Legal Notices, as required under Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License, a covered work must retain the producer
 * line in every PDF that is created or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing a commercial license. Buying such a license is
 * mandatory as soon as you develop commercial activities involving the iText software without disclosing the source
 * code of your own applications. These activities include: offering paid services to customers as an ASP, serving PDFs
 * on the fly in a web application, shipping iText with a closed source product.
 *
 * For more information, please contact iText Software Corp. at this address: sales@itextpdf.com
 */
package com.itextpdf.tool.xml.html;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.css.apply.NoNewLineParagraphCssApplier;
import com.itextpdf.tool.xml.css.apply.ParagraphCssApplier;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.ctx.ObjectContext;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 * Abstract TagProcessor that allows setting the configuration object to a
 * protected member variable.<br />
 * Implements {@link TagProcessor#startElement(WorkerContext, Tag)} and
 * {@link TagProcessor#endElement(WorkerContext, Tag, List)} to calculate font sizes and add
 * new pages if needed.<br />
 * Extend from this class instead of implementing {@link TagProcessor} to
 * benefit from auto fontsize metric conversion to pt and
 * page-break-before/after insertion. Override
 * {@link AbstractTagProcessor#start(WorkerContext, Tag)} and
 * {@link AbstractTagProcessor#end(WorkerContext, Tag, List)} in your extension.
 *
 * @author redlab_b
 *
 */
public abstract class AbstractTagProcessor implements TagProcessor {

	private final FontSizeTranslator fontsizeTrans;

	/**
	 *
	 */
	public AbstractTagProcessor() {
		fontsizeTrans = FontSizeTranslator.getInstance();
	}

	/**
	 * Utility method that fetches the CSSResolver from the if any and if it uses the default key.
	 * @param context the WorkerContext
	 *
	 * @return CSSResolver
	 * @throws NoCustomContextException if the context of the
	 *             {@link CssResolverPipeline} could not be found.
	 */
	@SuppressWarnings("unchecked")
	public CSSResolver getCSSResolver(final WorkerContext context) throws NoCustomContextException {
		return ((ObjectContext<CSSResolver>)context.get(CssResolverPipeline.class.getName())).get();
	}

	/**
	 * Utility method that fetches the HtmlPipelineContext used if any and if it
	 * uses the default key.
	 * @param context the WorkerContext
	 * @return a HtmlPipelineContext
	 * @throws NoCustomContextException if the context of the
	 *             {@link HtmlPipelineContext} could not be found.
	 */
	public HtmlPipelineContext getHtmlPipelineContext(final WorkerContext context) throws NoCustomContextException {
		return ((HtmlPipelineContext) context.get(HtmlPipeline.class.getName()));
	}
	/**
	 * Calculates any found font size to pt values and set it in the CSS before
	 * calling {@link AbstractTagProcessor#start(WorkerContext, Tag)}.<br />
	 * Checks for
	 * {@link com.itextpdf.tool.xml.css.CSS.Property#PAGE_BREAK_BEFORE}, if the
	 * value is always a <code>Chunk.NEXTPAGE</code> added before the
	 * implementors {@link AbstractTagProcessor#start(WorkerContext, Tag)} method.
	 *
	 */
	public final List<Element> startElement(final WorkerContext ctx, final Tag tag) {
		float fontSize = fontsizeTrans.translateFontSize(tag);
		tag.getCSS().put(CSS.Property.FONT_SIZE, fontSize + "pt");
		String pagebreak = tag.getCSS().get(CSS.Property.PAGE_BREAK_BEFORE);
		if (null != pagebreak && CSS.Value.ALWAYS.equalsIgnoreCase(pagebreak)) {
			List<Element> list = new ArrayList<Element>(2);
			list.add(Chunk.NEXTPAGE);
			list.addAll(start(ctx, tag));
			return list;
		}
		return start(ctx, tag);
	}

	/**
	 * Classes extending AbstractTagProcessor should override this method for actions that should be done in
	 * {@link TagProcessor#startElement(WorkerContext, Tag)}. The {@link AbstractTagProcessor#startElement(WorkerContext, Tag)} calls this method
	 * after or before doing certain stuff, (see it's description).
	 * @param ctx the WorkerContext
	 * @param tag the tag
	 *
	 * @return an element to be added to current content, may be null
	 */
	public List<Element> start(final WorkerContext ctx, final Tag tag){ return new ArrayList<Element>(0); };

	/*
	 * (non-Javadoc)
	 *
	 * @see com.itextpdf.tool.xml.TagProcessor#content(com.itextpdf.tool.xml.Tag, java.lang.String)
	 */
	public List<Element> content(final WorkerContext ctx, final Tag tag, final String content) {
		return new ArrayList<Element>(0);
	}

	/**
	 * Checks for
	 * {@link com.itextpdf.tool.xml.css.CSS.Property#PAGE_BREAK_AFTER}, if the
	 * value is always a <code>Chunk.NEXTPAGE</code> is added to the
	 * currentContentList after calling
	 * {@link AbstractTagProcessor#end(WorkerContext, Tag, List)}.
	 */
	public final List<Element> endElement(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
		List<Element> list = end(ctx, tag, currentContent);
		String pagebreak = tag.getCSS().get(CSS.Property.PAGE_BREAK_AFTER);
		if (null != pagebreak && CSS.Value.ALWAYS.equalsIgnoreCase(pagebreak)) {
			list.add(Chunk.NEXTPAGE);
			return list;
		}
		return list;
	}

	/**
	 * Classes extending AbstractTagProcessor should override this method for
	 * actions that should be done in {@link TagProcessor#endElement(WorkerContext, Tag, List)}.
	 * The {@link AbstractTagProcessor#endElement(WorkerContext, Tag, List)} calls this method
	 * after or before doing certain stuff, (see it's description).
	 * @param ctx the WorkerContext
	 * @param tag the tag
	 * @param currentContent the content created from e.g. inner tags, inner content and not yet added to document.
	 *
	 * @return a List containing iText Element objects
	 */
	public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
		return new ArrayList<Element>(currentContent);
	}

	/**
	 * Defaults to false.
	 *
	 * @see com.itextpdf.tool.xml.html.TagProcessor#isStackOwner()
	 */
	public boolean isStackOwner() {
		return false;
	}

	/**
	 * Adds currentContent list to a paragraph element. If addNewLines is true a
	 * Paragraph object is returned, else a NoNewLineParagraph object is
	 * returned.
	 *
	 * @param currentContent List<Element> of the current elements to be added.
	 * @param addNewLines boolean to declare which paragraph element should be
	 *            returned, true if new line should be added or not.
	 * @param applyCSS true if CSS should be applied on the paragraph
	 * @param tag the relevant tag
	 * @param ctx the WorkerContext
	 * @return a List with paragraphs
	 */
	public final List<Element> currentContentToParagraph(final List<Element> currentContent,
			final boolean addNewLines, final boolean applyCSS, final Tag tag, final WorkerContext ctx) {
		try {
			List<Element> list = new ArrayList<Element>();
			if (currentContent.size() > 0) {
				if (addNewLines) {
					Paragraph p = new Paragraph();
					for (Element e : currentContent) {
						p.add(e);
					}
					if (applyCSS) {
						p = new ParagraphCssApplier(getHtmlPipelineContext(ctx)).apply(p, tag);
					}
					list.add(p);
				} else {
					NoNewLineParagraph p = new NoNewLineParagraph();
					for (Element e : currentContent) {
						p.add(e);
					}
					p = new NoNewLineParagraphCssApplier(getHtmlPipelineContext(ctx)).apply(p, tag);
					list.add(p);
				}
			}
			return list;
		} catch (NoCustomContextException e) {
			throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e);
		}
	}

	/**
	 * Default apply CSS to false and tag to null.
	 * @see AbstractTagProcessor#currentContentToParagraph(List, boolean, boolean, Tag, WorkerContext)
	 * @param currentContent List<Element> of the current elements to be added.
	 * @param addNewLines boolean to declare which paragraph element should be
	 *            returned, true if new line should be added or not.
	 * @return a List with paragraphs
	 */
	public final List<Element> currentContentToParagraph(final List<Element> currentContent,
			final boolean addNewLines) {
		return this.currentContentToParagraph(currentContent, addNewLines, false, null, null);
	}
}

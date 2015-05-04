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
 * OF THIRD PARTY RIGHTS.
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

import com.itextpdf.text.*;
import com.itextpdf.text.api.Indentable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.FontSizeTranslator;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.pdfelement.NoNewLineParagraph;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.ctx.ObjectContext;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.util.ParentTreeUtil;

import java.util.ArrayList;
import java.util.List;

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
public abstract class AbstractTagProcessor implements TagProcessor, CssAppliersAware {

	private final FontSizeTranslator fontsizeTrans;
	private CssAppliers cssAppliers;

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
        if (fontSize != Font.UNDEFINED) {
		    tag.getCSS().put(CSS.Property.FONT_SIZE, fontSize + "pt");
        }
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
        
        private String getParentDirection() {
            String result = null;
            for (Tag tag : tree) {
                result = tag.getAttributes().get(HTML.Attribute.DIR);
                if (result != null) break;
                // Nested tables need this check
                result = tag.getCSS().get(CSS.Property.DIRECTION);
                if (result != null) break;
            }
            return result;
        }
        
        private List<Tag> tree;
        
        protected int getRunDirection(Tag tag) {
            /* CSS should get precedence, but a dir attribute defined on the tag
               itself should take precedence over an inherited style tag
            */
            String dirValue = tag.getAttributes().get(HTML.Attribute.DIR);
            if (dirValue == null) {
                // using CSS is actually discouraged, but still supported
                dirValue = tag.getCSS().get(CSS.Property.DIRECTION);
                if (dirValue == null) {
                    // dir attribute is inheritable in HTML but gets trumped by CSS
                    tree = new ParentTreeUtil().getParentTagTree(tag, tree);
                    dirValue = getParentDirection();
                }// */
            }
            if (CSS.Value.RTL.equalsIgnoreCase(dirValue)) {
                return PdfWriter.RUN_DIRECTION_RTL;
            }
            if (CSS.Value.LTR.equalsIgnoreCase(dirValue)) {
                return PdfWriter.RUN_DIRECTION_LTR;
            }
            return PdfWriter.RUN_DIRECTION_DEFAULT;
        }

    protected List<Element> textContent(final WorkerContext ctx, final Tag tag, final String content) {
		List<Chunk> sanitizedChunks = HTMLUtils.sanitize(content, false);
		List<Element> l = new ArrayList<Element>(1);
        for (Chunk sanitized : sanitizedChunks) {
            try {
                l.add(getCssAppliers().apply(sanitized, tag, getHtmlPipelineContext(ctx)));
            } catch (NoCustomContextException e) {
                throw new RuntimeWorkerException(e);
            }
        }
		return l;
	}

	/**
	 * Checks for
	 * {@link com.itextpdf.tool.xml.css.CSS.Property#PAGE_BREAK_AFTER}, if the
	 * value is always a <code>Chunk.NEXTPAGE</code> is added to the
	 * currentContentList after calling
	 * {@link AbstractTagProcessor#end(WorkerContext, Tag, List)}.
	 */
	public final List<Element> endElement(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
        List<Element> list = new ArrayList<Element>();
        if (currentContent.isEmpty()) {
            list = end(ctx, tag, currentContent);
        } else {
            List<Element> elements = new ArrayList<Element>();
            for (Element el : currentContent) {
                if (el instanceof Chunk && ((Chunk) el).hasAttributes() && ((Chunk) el).getAttributes().containsKey(Chunk.NEWPAGE)) {
                    if (elements.size() > 0) {
                        list.addAll(end(ctx, tag, elements));
                        elements.clear();
                    }
                    list.add(el);
                } else {
                    elements.add(el);
                }
            }
            if (elements.size() > 0) {
                list.addAll(end(ctx, tag, elements));
                elements.clear();
            }
        }
		String pagebreak = tag.getCSS().get(CSS.Property.PAGE_BREAK_AFTER);
		if (null != pagebreak && CSS.Value.ALWAYS.equalsIgnoreCase(pagebreak)) {
			list.add(Chunk.NEXTPAGE);
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
	 * @param applyCSS true if CSS should be applied on the paragraph.
	 * @param tag the relevant tag.
	 * @param ctx the WorkerContext.
	 * @return a List of paragraphs.
	 */
	public List<Element> currentContentToParagraph(final List<Element> currentContent,
			final boolean addNewLines, final boolean applyCSS, final Tag tag, final WorkerContext ctx) {
		try {
            int direction = getRunDirection(tag);
			List<Element> list = new ArrayList<Element>();
			if (currentContent.size() > 0) {
				if (addNewLines) {
					Paragraph p = createParagraph();
                    p.setMultipliedLeading(1.2f);
					for (Element e : currentContent) {
                        if (e instanceof LineSeparator) {
                            try {
                                HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
                                Chunk newLine = (Chunk)getCssAppliers().apply(new Chunk(Chunk.NEWLINE), tag, htmlPipelineContext);
                                p.add(newLine);
                            } catch (NoCustomContextException e1) {
                                throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e1);
                            }
                        }
						p.add(e);
					}
                    if (p.trim()) {
                        if (applyCSS) {
                            p = (Paragraph) getCssAppliers().apply(p, tag, getHtmlPipelineContext(ctx));
                        }
                        if (direction == PdfWriter.RUN_DIRECTION_RTL) {
                            doRtlIndentCorrections(p);
                        }
                        list.add(p);
                    }
				} else {
					NoNewLineParagraph p = new NoNewLineParagraph(Float.NaN);
                    p.setMultipliedLeading(1.2f);
					for (Element e : currentContent) {
						p.add(e);
					}
					p = (NoNewLineParagraph) getCssAppliers().apply(p, tag, getHtmlPipelineContext(ctx));
                    if (direction == PdfWriter.RUN_DIRECTION_RTL) {
                        doRtlIndentCorrections(p);
                    }
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

    protected Paragraph createParagraph() {
        return new Paragraph(Float.NaN);
    }

    protected void doRtlIndentCorrections(Indentable p) {
        float right = p.getIndentationRight();
        p.setIndentationRight(p.getIndentationLeft());
        p.setIndentationLeft(right);
    }
}
